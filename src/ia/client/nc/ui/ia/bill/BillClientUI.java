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
 * 功能描述：单据维护界面 <p/> 作者:崔勇 <p/> 创建日期:(2001-4-29 11:39:46) <p/> 修改记录及日期: <p/> 修改人:
 */
public class BillClientUI extends nc.ui.pub.ToftPanel implements
    UIDialogListener, BillEditListener, BillEditListener2,
    BillTableMouseListener, IBillRelaSortListener2, ValueChangedListener,
    BillBodyMenuListener, ItemListener, ListSelectionListener, ILinkQuery {

  private static final long serialVersionUID = 1L;
  /**
   * 取得节点对应的节点编码 由子类实现
   * @return String
   */
  protected  String getFunCode(){
	  return "";
  }
  /**
   * 联查单据方法
   */
  public void doQueryAction(ILinkQueryData querydata) {
    m_sBillType = querydata.getBillType();
    String sBillID = querydata.getBillID();
    try {

      // 设置查询用VO
      BillVO bvo = new BillVO();
      BillHeaderVO bhvo = new BillHeaderVO();
      bhvo.setCbillid(sBillID);
      bvo.setParentVO(bhvo);
      // 查询数据
      ClientLink cl = ce.getClientLink();
      BillVO[] bills = BillBO_Client.queryByVO(bvo, new Boolean(true),
          "cbillid", cl);
      // 显示数据
      if (bills != null && bills.length != 0 && bills[0] != null) {
        // 普通单据联查
        bhvo = (BillHeaderVO) bills[0].getParentVO();
        String corp = bhvo.getPk_corp();
        //是本公司联查，检查权限
        if(ce.getCorporationID().equals(corp)){
          ConditionVO[] conditionVOs = getQueryClientDlg().getDataPowerConVOs(corp, null);
          getQueryClientDlg().parseConditionVO(conditionVOs);
          String[] tables = getQueryClientDlg().getTables();
          String[] sConnection = getQueryClientDlg().getConnections();
          String[] sConditions = getQueryClientDlg().getConditions();
          if(sConditions != null && sConditions.length > 1){
            //去掉权限SQL字符串开始的第一个and
            sConditions[1] = "(" + sConditions[1].substring(5);
          }
          m_voBills = BillBO_Client.querybillWithOtherTable(tables, sConnection, sConditions,
              bvo, null, null, new Boolean(false), cl);
          if (m_voBills != null && m_voBills.length > 0) {
            setBillsInList(m_voBills);
          }else{
            showErrorMessage(NCLangRes.getInstance().getStrByID("common", 
                "SCMCOMMON000000161"));/*@res"没有查看数据的权限"*/
          }
        }else{
          //不是本公司联查，不检查权限，按钮置灰
          //重新初始化
          initialize(corp);
          //重新载入单据所属公司的单据模版
          loadTemplet(m_sTitle, m_sBillType);
          //按钮置灰
          ButtonObject[] buttons = this.buttonTree.getButtonArray();
          for(int i = 0; i<buttons.length; i++){
            buttons[i].setEnabled(false);
          }
          setButtons(buttons);
          //设置单据信息
          getBillCardPanel().setBillValueVO(bills[0]);
          getBillCardPanel().execHeadTailLoadFormulas();
          getBillCardPanel().getBillModel().execLoadFormula();
          //处理表头
          setComboBoxInHeadFromVO(bills[0], true, 0);
          //处理表体
          BillItemVO btvo[] = (BillItemVO[]) bills[0].getChildrenVO();
          for (int i = 0; i < btvo.length; i++) {
            setComboBoxInBodyFromVO(btvo[i], true, i);
          }
        }
      }
      else if (1 == 1) {
        // 会计平台凭证联查单据时使用
        bvo = new BillVO();
        // 载入模板
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
        // 查询单据
        m_voBills = BillBO_Client.querybillWithOtherTable(null, null, null,
            bvo, null, null, new Boolean(false), cl);

        if (m_voBills != null && m_voBills.length > 0) {
          setBillsInList(m_voBills);
        }
        else {
          /*@res "没有符合条件的单据或此单据是根据计价方式调整生成"*/
          MessageDialog.showHintDlg(this, null, NCLangRes.getInstance()
              .getStrByID("20143010", "UPP20143010-000230"));
        }
      }
      else {
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000084")/* @res "未查询到记录" */);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000103")/* @res "初始化数据出错" */
          + e.getMessage());
    }
  }

  /**
   * 关闭窗口的客户端接口。可在本方法内完成窗口关闭前的工作。
   * 
   * @return boolean 返回值为true表示允许窗口关闭，返回值为false表示不允许窗口关闭。
   */
  public boolean onClosing() {
    if (m_iStatus == ADD_STATUS || m_iStatus == UPDATE_STATUS) {
      int r = MessageDialog.showYesNoCancelDlg(this, null, NCLangRes
          .getInstance().getStrByID("common", "UCH001")
      /* @res是否保存已修改的数据？" */, UIDialog.ID_YES);
      
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
   * 返回界面排序时同时处理的缓存数据
   */
  public Object[] getRelaSortObjectArray() {
    return m_voBills;
  }

  /**
   * 表头和表体中是否转出进项税检查框事件处理类
   * 
   * @param e
   */
  public void itemStateChanged(ItemEvent e) {
    Object o = e.getSource();
    if (o != null && (m_iStatus == UPDATE_STATUS || m_iStatus == ADD_STATUS)) {
      String sName = ((UICheckBox) o).getName();
      // 响应表头的改变
      if ("btransferincometax_h".equals(sName)) {
        BillItem bt = getBillCardPanel().getHeadItem("btransferincometax_h");
        if (bt == null)
          return;
        UICheckBox cb = (UICheckBox) bt.getComponent();
        int[] iRows = getBillCardPanel().getBillTable().getSelectedRows();
        // 修改表体相应行的状态，并调用afterEdit触发编辑后事件，切换进项税率，出口退税率是否能编辑
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
      // 响应表体的改变
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
        // 先去掉监听器
        hcb.removeItemListener(this);
        // 修改表头检查框的值
        if (cb.isSelected()) {
          hcb.setSelected(true);
        }
        else {
          hcb.setSelected(false);
        }
        // 恢复监听器
        hcb.addItemListener(this);
        hbt.setComponent(hcb);

      }
    }
  }

  /**
   * 行选取变化事件的相关操作
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
      // 修改表头检查框的值
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
     * 行改变事件。 创建日期：(2001-3-23 2:02:27)
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
     * 行改变事件。 创建日期：(2001-3-23 2:02:27)
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
   * 函数功能:查询对话框按确定关闭后，按条件进行查找 并在界面显示符合条件的存货信息 参数:无 <p/> 返回值:void <p/> 异常:无
   */
  private void addQuery() {
    // 1. 取得查找条件
    AddQueryVO aqVO = getQueryPlannedPriceDlg().getQueryVO();
    // 2. 查找满足条件的存货
    if (aqVO != null) {
      try {
        aqVO = BillBO_Client.queryAddBillVOs(aqVO);
      }
      catch (Exception e) {
        reportException(e);
        e.printStackTrace();
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000037")/* @res "查询数据出错" */
            + e.getMessage());
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000037")/* @res "查询数据出错" */
            + e.getMessage());
        return;
      }
    }
    // 3. 把查找到的存货添到界面上
    if (aqVO != null) {
      onAdd(aqVO);
    }
  }

  /**
   * 函数功能:审核个别计价的单据分录 <p/> 参数: boolean isInvi ----- 是否是个别计价 <p/> 返回值: <p/> 异常:
   */
  private void auditOneBill(boolean isInvi, String sAudit) {

    try {
      if (sAudit == null)
        sAudit = ce.getParaValue(m_sCorpID, ConstVO.m_iPara_ZDSHJZ);
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000038")/* @res "正在成本计算，请稍候" */);
      // 调用成本计算的方法
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
            // 此类存货，非个别计价出库单，未成本计算，需要成本计算
            vTemp.addElement((BillItemVO) m_voCurBill.getChildrenVO()[i]);
          }
        }
        if (vTemp.size() == 0) {
          // 没有要计算的
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000039")/* @res "此单据没有需成本计算分录" */);
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
            "20143010", "UPP20143010-000022")/* @res "成本计算失败" */);
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000022")/* @res "成本计算失败" */);
        return;
      }
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000040")/* @res "成本计算成功" */);
      // 设置VO
      ((BillHeaderVO) m_voCurBill.getParentVO()).setBauditedflag(new UFBoolean(
          "Y"));
      if (isInvi) {
        // 处理审核人名称、单价、金额
        int iRowIndex = getBillListPanel().getBodyTable().getSelectedRow();
        int iModelIndex = -1;
        iModelIndex = iRowIndex;
        // 处理界面
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
        // 设置修改按钮状态
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
          // 都审核了
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
              // 计算进项税转出金额
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
        // 设置修改按钮状态
        btnCtrl.set(false,IABtnConst.BTN_BILL_EDIT);
      }
      // 设置按钮状态
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
   * 函数功能: <p/> 参数: Object[] oInvPKs <p/> oInvValues[0] = sPKs[i]; 主键
   * oInvValues[1] = sCodes[i]; 编码 oInvValues[2] = sNames[i]; 名称 oInvValues[3] =
   * oInvSpec[i]; 规格 oInvValues[4] = oInvType[i]; 型号 oInvValues[5] = oInvMea[i];
   * 计量单位 oInvValues[6] = oInvKind[i]; 存货类型 oInvValues[7] = oInvJHJ[i]; 计划价
   * oInvValues[8] = oInvAss[i]; 是否辅计量管理 oInvValues[9] = oInvPricemethod[i];
   * 计价方式 oInvValues[10] = oInvIncomeTax[i]; 进项税 <p/> ht :附计量信息 <p/> 返回值: <p/>
   * 异常:
   */
  private void changeInv(Object[] oInvPKs, int iRow, Hashtable ht)
      throws Exception {
    // 获得存货信息
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
    // 判断此存货是否是辅计量管理
    UFBoolean bAstManage = new UFBoolean(false);
    if (oInvPKs[8] != null) {
      bAstManage = new UFBoolean(oInvPKs[8].toString());
    }
    // 计价方式 20050629 zlq 不再控制
    // int iFpricemethod = -1;
    // if (oInvPKs[9] != null) {
    // iFpricemethod = new Integer(oInvPKs[9].toString()).intValue();
    // if (iFpricemethod != ConstVO.JHJ) {
    // //非计划价存货，计划单价为空
    // dPlanedPrice = null;
    // }
    // }
    // 如果存货改变，将批次号、数量、单价、金额、计划金额设为空
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
    // 设置参照
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
//      // 处理辅计量单位等
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
//      // 设置自由项
//      getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree0");
//      getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree1");
//      getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree2");
//      getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree3");
//      getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree4");
//      getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree5");
//      // 设置参照
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
    	//获得出口退税率( 出库单且库存销售未启用 )//
    	if( m_bIsOutBill && !m_bIsICStart && !m_bIsSOStart){
	    	Object[] oexpaybacktax = (Object[]) CacheTool.getCellValue(
	          "bd_invmandoc", "pk_invmandoc","expaybacktax", oInvPK.toString().trim());
	    	 getBillCardPanel().getBillModel().setValueAt(oexpaybacktax[0],
	           iRow, "nexpaybacktax");
    	}
      if (bAstManage.booleanValue()) {
        // 处理辅计量数量
        // 获得辅计量单位
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
              // 是固定换算率，处理辅计量数量 (辅计量数量x换算率=数量)
              Object oTemp = getBillCardPanel().getBillModel().getValueAt(iRow,
                  "nnumber");
              if (oTemp != null && oTemp.toString().trim().length() != 0) {
                UFDouble dNumber = new UFDouble(oTemp.toString().trim());
                if (dRate != null) {
                  UFDouble dAssistNum = dNumber.div(dRate); // (辅计量数量=数量/换算率)
                  getBillCardPanel().getBillModel().setValueAt(dAssistNum,
                      iRow, "nassistnum");
                }
              }
              if (oInvPK != null && oInvPK.toString().trim().length() != 0) {
                // 将固定换算率记入存货
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
   * 函数功能:为固定资产接口用的改变存货的方法 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  // private void changeSingleInv(Object oInvPK, int iRow) throws Exception {
  // //如果存货改变，将批次号、数量、单价、金额、计划金额设为空
  // BillItem bt = getBillCardPanel().getBodyItem("nnumber");
  // if (bt != null) {
  // bt.setValue(null);
  // getBillCardPanel().getBillModel().setValueAt("", iRow, "vbatch");
  // getBillCardPanel().getBillModel().setValueAt("", iRow, "nnumber");
  // //处理辅计量单位等
  // if (getBillCardPanel().getBillModel().getBodyColByKey("castunitname") !=
  // -1) {
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "castunitid");
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "castunitname");
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "nchangerate");
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "nassistnum");
  // }
  // getBillCardPanel().getBillModel().setValueAt("", iRow, "nprice");
  // getBillCardPanel().getBillModel().setValueAt("", iRow, "nmoney");
  // //计算进项税转出金额
  // calcTransIncomeTaxMny(iRow);
  // getBillCardPanel().getBillModel().setValueAt("", iRow, "nplanedmny");
  // BillItem bt2 = getBillCardPanel().getBodyItem("cadjustbillitem");
  // if (bt2 != null) {
  // getBillCardPanel().getBillModel().setValueAt("", iRow, "cadjustbillitem");
  // }
  // //设置自由项
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree0");
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree1");
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree2");
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree3");
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree4");
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree5");
  // //设置参照
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
  // pane.setRefNodeName("存货档案");/*-=notranslate=-*/
  // pane.setRefModel(model);
  // pane.setPK(oInvPK);
  // Object oRD = getBillCardPanel().getHeadItem("crdcenterid").getValue();
  // if (oInvPK != null && oInvPK.toString().trim().length() != 0 && oRD != null
  // && oRD.toString().trim().length() != 0) {
  // //获得计划单价
  // UFDouble dPlanedPrice = CommonDataBO_Client.getPlanedPrice(m_sCorpID,
  // oRD.toString().trim(), oInvPK
  // .toString().trim());
  // //获得存货信息
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
  // //判断此存货是否是辅计量管理
  // Hashtable htIsMngFrAssi = ce.isManageForAssi(m_sCorpID, new
  // String[]{oInvPK.toString()});
  // UFBoolean bAstManage = (UFBoolean)htIsMngFrAssi.get(oInvPK.toString());
  // if (bAstManage.booleanValue() && m_bIsAdjustBill == false) {
  // //处理辅计量数量
  // //获得辅计量单位
  // String sAstUnitName = "";
  // String sAstUnitID = "";
  // String sAstUnitFieldName = "";
  // UFDouble dRate = null;
  // String sFixedflag = null;
  // if (m_sBillType.equals(ConstVO.m_sBillCGRKD) ||
  // m_sBillType.equals(ConstVO.m_sBillWWJGSHD)) {
  // //采购入库单、委外加工收货单取采购辅计量单位
  // sAstUnitFieldName = "pk_measdoc2";
  // } else if (m_sBillType.equals(ConstVO.m_sBillXSCBJZD) ||
  // m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
  // //销售成本结转单取销售辅计量单位
  // sAstUnitFieldName = "pk_measdoc1";
  // } else {
  // //其它取库存辅计量单位
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
  // //是固定换算率，处理辅计量数量 (辅计量数量x换算率=数量)
  // Object oTemp = getBillCardPanel().getBillModel().getValueAt(iRow,
  // "nnumber");
  // if (oTemp != null && oTemp.toString().trim().length() != 0) {
  // UFDouble dNumber = new UFDouble(oTemp.toString().trim());
  // if (dRate != null) {
  // UFDouble dAssistNum = dNumber.div(dRate); //(辅计量数量=数量/换算率)
  // getBillCardPanel().getBillModel().setValueAt(dAssistNum, iRow,
  // "nassistnum");
  // }
  // }
  // if (oInvPK != null && oInvPK.toString().trim().length() != 0) {
  // //将固定换算率记入存货
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
   * 函数功能:检查是否已输入内容 <p/> 参数: <p/> 返回值:是否合法 <p/> 异常:
   */
  private boolean checkBillHeaderItem(BillItem bt) {
    if (bt != null) {
      Object o = bt.getValueObject();
      String sName = bt.getName();
      if (o == null || o.toString().trim().length() == 0) {
        // String sMessage = "请输入" + sName;
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
   * 函数功能:判断是否允许修改非本人制单的单据 <p/> 参数: <p/> 返回值: <p/> 异常: <p/> 创建日期：(2002-11-14
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
          "20143010", "UPP20143010-000042")/* @res "存货核算参数设置为：不允许删改非本人制单的单据。" */);
    }
    return bEnabled;
  }

  /**
   * 根据库存组织和存货ID获得形态
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
   * 函数功能:显示单据 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  private void setBillsInList(BillVO[] bvos) throws Exception {
    if (m_voBills != null && m_voBills.length != 0) {
      // 获得了单据
      BillHeaderVO[] voBillHeads = null;
      Vector vTempHeads = new Vector(1, 1);
      for (int i = 0; i < m_voBills.length; i++) {
        BillVO voBill = m_voBills[i];
        vTempHeads.addElement(voBill.getParentVO());
      }
      voBillHeads = new BillHeaderVO[vTempHeads.size()];
      vTempHeads.copyInto(voBillHeads);
      getBillListPanel().setHeaderValueVO(voBillHeads);
      // 显式调用方法,使公式可用
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
        // 设置假退料,暂估,调拨类型 zlq
        setComboBoxInHeadFromVO(m_voBills[i], false, i);
      }
      if (m_sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
        // 查询来源单据类型
        m_hmBillId2Sourcebilltypecode = BillBO_Client.queryBodyItems(sBillIDs,
            "csourcebilltypecode", ce.getClientLink());
        if (m_hmBillId2Sourcebilltypecode != null) {
          for (int i = 0; i < m_voBills.length; i++) {
            String sBillID = m_voBills[i].getParentVO().getPrimaryKey();
            Object sSource = m_hmBillId2Sourcebilltypecode.get(sBillID);
            if (sSource != null && sSource.toString().trim().length() != 0) {
              String sSourceName = "";
              if (sSource.equals(ConstVO.m_sBillXSFP)) {
                sSourceName = m_ComboItemsVO.name_salebill;// 销售发票
              }
              else if (sSource.equals(ConstVO.m_sBillXSCKD)) {
                sSourceName = m_ComboItemsVO.name_saleoutlist;// 销售出库单
              }
              else if (sSource.equals(ConstVO.m_sBillKCTSD)) {
                sSourceName = m_ComboItemsVO.name_waylossbill;// 库存途损单
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

      // 切换表头到第一张单据，将会调用HeadChanged方法
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
   * 函数功能:列表到表单转换时的处理 1、如果是材料出库单，设置是否假退料 2、如果是入库调整单，处理被调整单据类型 3、处理辅计量单位 <p/>
   * 参数:无 <p/> 返回值: <p/> 异常:
   */
  private void dispListToCard() {
    try {
      // 执行表头表尾公式
      getBillCardPanel().execHeadTailLoadFormulas();
      // 执行表体公式
      getBillCardPanel().getBillModel().execLoadFormula();

      int iRowIndex = getBillListPanel().getHeadTable().getSelectedRow();
      Object oTemp = null;
      // 1、如果是材料出库单，设置是否假退料
      if (m_sBillType.equals(ConstVO.m_sBillCLCKD)) {
        oTemp = getBillListPanel().getHeadBillModel().getValueAt(iRowIndex,
            "bwithdrawalflag");
        getBillCardPanel().getHeadItem("bwithdrawalflag").setValue(oTemp);
      }
      // 2、如果是入库调整单，处理被调整单据类型
      if (m_bIsInAdjustBill) {
        // 将被调整单据类型设置入表单
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
      // 3、如果是销售成本结转单，设置单据来源
      if (m_sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
        oTemp = getBillListPanel().getHeadBillModel().getValueAt(iRowIndex,
            "cbillsource");
        getBillCardPanel().getHeadItem("cbillsource").setValue(oTemp);
      }
      // 设置暂估值
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
      // 设置调拨类型
      if (m_sBillType.equals(ConstVO.m_sBillDBRKD)
          || m_sBillType.equals(ConstVO.m_sBillDBCKD)) {
        oTemp = getBillListPanel().getHeadBillModel().getValueAt(iRowIndex,
            "fallocflag");
        getBillCardPanel().getHeadItem("fallocflag").setValue(oTemp);
      }
      // 处理表体标志
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
          // 设置业务员参照的条件
          bt = m_bd.getHeadItem("cemployeeid");
          if (bt != null) {
            // 设置业务员参照的条件
            ((UIRefPane) bt.getComponent()).setWhereString(m_sOldUserCondition
                + "and bd_psndoc.pk_deptdoc = '" + sDeptID + "'");
          }
        }
        else {
          bt = m_bd.getHeadItem("cemployeeid");
          if (bt != null) {
            // 设置业务员参照的条件
            ((UIRefPane) bt.getComponent()).setWhereString(m_sOldUserCondition);
          }
        }
      }
      if (getBillCardPanel().getHeadItem("cstockrdcenterid") != null) {
        Object obj = getBillCardPanel().getHeadItem("cstockrdcenterid").getValueObject();
        String sRDID = obj == null ? null : obj.toString();
        bt = m_bd.getHeadItem("cwarehouseid");
        if (sRDID != null && sRDID.trim().length() != 0) {
          // 设置仓库参照的条件
          if (bt != null) {
            // 设置仓库参照的条件
            String sWhere = m_sOldWareCondition + "and pk_calbody = '" + sRDID
                + "'";
            // 是否是废品库
            // if (m_sBillType.equals(ConstVO.m_sBillBFD) == false)
            sWhere = sWhere + "and gubflag = 'N'";
            // else
            // sWhere = sWhere + "and gubflag = 'Y'";
            // 没有暂封
            sWhere = sWhere + "and sealflag = 'N'";
            ((UIRefPane) bt.getComponent()).setWhereString(sWhere);
            obj = bt.getValueObject();
            String sWareID = obj == null ? null : obj.toString();
            // 设置仓库
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
          // 恢复仓库的条件 //zlq add 20050305
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
          // 确保行对应正确
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
          // 确保行对应正确
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
        // 确保行对应正确
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
        // 确保行对应正确
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
   * 函数功能:如果存货核算单独启用，数据库没有外系统单据类型，为显示来源单据类型，重写方法 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  private void execListPanelBodyFormula() {
    getBillListPanel().getBodyBillModel().execLoadFormula();
    // 先处理来源单据类型
    if (m_voCurBill != null) {
      String sBillType = ((BillHeaderVO) m_voCurBill.getParentVO())
          .getCbilltypecode();
      // 销售成本结转单
      if (sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
        for (int i = 0; i < getBillListPanel().getBodyBillModel().getRowCount(); i++) {
          Object oBillSource = getBillListPanel().getBodyBillModel()
              .getValueAt(i, "csourcebilltypecode");
          String sVOBillSource = ((BillItemVO) m_voCurBill.getChildrenVO()[i])
              .getCsourcebilltypecode();
          if (sVOBillSource != null
              && sVOBillSource.trim().length() != 0
              && (oBillSource == null || oBillSource.toString().trim().length() == 0)) {
            // VO中有来源单据信息，界面没有
            // 可能是bd_billtype中没有此单据类型
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
      // 将公式获得的数据放入VO
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
   * 返回 BillCard 特性值。
   * 
   * @return nc.ui.ia.bill.IABillCardPanel
   */
  /* 警告：此方法将重新生成。 */
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
   * 返回 IndividualAllotDlg 特性值。
   * 
   * @return nc.ui.ia.bill.IndividualAllotDlg
   */
  /* 警告：此方法将重新生成。 */
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
   * 返回 LocateConditionDlg 特性值。
   * 
   * @return nc.ui.ia.bill.LocateConditionDlg
   */
  /* 警告：此方法将重新生成。 */
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
   * 函数功能:获得查询条件输入对话框 <p/> 参数: <p/> 返回值: <p/> 异常:
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
   * 返回 LocateConditionDlg 特性值。
   * 
   * @return nc.ui.ia.bill.LocateConditionDlg
   */
  /* 警告：此方法将重新生成。 */
  private QueryPlannedPriceDlg getQueryPlannedPriceDlg() {
    if (m_dlgQueryPlannedPrice == null) {
      m_dlgQueryPlannedPrice = new QueryPlannedPriceDlg(this);
      m_dlgQueryPlannedPrice.setIsWarningWithNoInput(true);
    }
    return m_dlgQueryPlannedPrice;
  }

  /**
   * 返回 SaleBillsChooseDlg1 特性值。
   * 
   * @return nc.ui.ia.bill.SaleBillsChooseDlg
   */
  /* 警告：此方法将重新生成。 */
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
   * 函数功能:获得列表界面的单据
   * 
   * @return nc.vo.ia.bill.BillVO[]
   */
  private ArrayList getListBills() throws Exception {
    ArrayList al = new ArrayList();

    for (int i = 0; i < m_voBills.length; i++) {
      if (m_voBills[i].getChildrenVO() == null) {
        // 还没有获得表体数据
        ClientLink cl = ce.getClientLink();
        m_voBills[i] = BillBO_Client.querybillWithOtherTable(null, null, null,
            null, null, m_voBills[i], new Boolean(true), cl)[0];
      }
      al.add(m_voBills[i]);
    }
    return al;
  }

  /**
   * 函数功能:获得列表界面表头选中的单据
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
          // 还没有获得表体数据
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
   * 返回 UIComboBoxSource 特性值。
   * 
   * @return nc.ui.pub.beans.UIComboBox
   */
  /* 警告：此方法将重新生成。 */
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
   * 返回 UIRefPaneAdjustBill 特性值。
   * 
   * @return nc.ui.ia.pub.MyNCAdjustBillRefPane
   */
  /* 警告：此方法将重新生成。 */
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
   * 返回 UIRefPaneAdjustBillItem 特性值。
   * 
   * @return nc.ui.ia.pub.MyNCAdjustBillItemRefPane
   */
  /* 警告：此方法将重新生成。 */
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
   * 返回 UIRefPaneBatch 特性值。
   * 
   * @return nc.ui.ia.pub.MyNCBatchRefPane
   */
  /* 警告：此方法将重新生成。 */
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
   * 返回 UIRefPaneBillType 特性值。
   * 
   * @return nc.ui.ia.pub.MyNCBillTypeRefPane
   */
  /* 警告：此方法将重新生成。 */
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
   * 返回 UIRefPaneBatch 特性值。
   * 
   * @return nc.ui.ia.pub.MyNCBatchRefPane
   */
  /* 警告：此方法将重新生成。 */// 20050525 关闭固定资产和存货接口
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
  // //增加监听
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
   * 返回 UIRefPaneBatch 特性值。
   * 
   * @return nc.ui.ia.pub.MyNCBatchRefPane
   */
  /* 警告：此方法将重新生成。 */// 20050525 关闭固定资产和存货接口
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
  // //增加监听
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
   * 返回 UIRefPaneFreeItem 特性值。
   * 
   * @return nc.ui.ia.pub.freeitem.FreeItemRefPane
   */
  /* 警告：此方法将重新生成。 */
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
   * 返回 UIRefPaneJob 特性值。
   * 
   * @return nc.ui.ia.pub.MyNCJobRefPane
   */
  /* 警告：此方法将重新生成。 */
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
   * 返回 UIRefPaneJobParse 特性值。
   * 
   * @return nc.ui.ia.pub.JobPhaseRef
   */
  /* 警告：此方法将重新生成。 */
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
   * 返回 UIRefPaneBatch 特性值。
   * 
   * @return nc.ui.ia.pub.MyNCBatchRefPane
   */
  /* 警告：此方法将重新生成。 */
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
   * 函数功能:处理被调整单据类型变化后使被调整单据号编辑状态变化 <p/> 参数: DocumentEvent e ----- 文本变化事件 <p/>
   * 返回值: <p/> 异常:
   */
  private void handleAdjustBillType(DocumentEvent e) {
    if (m_iStatus != ADD_STATUS && m_iStatus != UPDATE_STATUS) {
      // 不是增加或修改
      return;
    }
    // 获得信息
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
      // 设置查询条件
      String sWhereString = " and a.cbilltypecode = '"
          + oBillType.toString().trim() + "' ";
      String sWareID = getBillCardPanel().getHeadItem("cwarehouseid")
          .getValue();
      String sRDID = getBillCardPanel().getHeadItem("cstockrdcenterid")
          .getValue();
      // 判断是否已有仓库、库存组织、存货，有设置批次号参照条件
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
      // 设置回冲单据分录
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
   * 每当部件抛出异常时被调用
   * 
   * @param exception
   *          java.lang.Throwable
   */
  private void handleException(java.lang.Throwable exception) {
    /* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
    Log.info("--------- 未捕捉到的异常 ---------");
    exception.printStackTrace(System.out);
  }

  /**
   * 统一读取系统参数： 0：各模块是否启用， 1: 获得业务类型 2：是否是初始帐户 
   * 3：获得数据精度 4：启用的会计期间 5: 表头自定义项处理 6: 表体自定义项处理 
   * 7：是否出现个别指定界面， 7: 是否允许修改、删除非本人制单的单据 
   * 7: 是否保留原始制单人
   */
  private void getParas() throws Exception {
    ServcallVO[] scDisc = new ServcallVO[7];

    // 准备数据
    // 0：各模块是否启用
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

    // 1: 获得业务类型
    /*
     * m_sFQSK 将存放一串以逗号分隔的字符串，每个字符串代表一个分期收款类型的业务类型
     * 将来使用时以indexof方法查找某个字符串是否在m_sFQSK中
     */
    String[] sBizs = new String[2];
    sBizs[0] = ConstVO.m_sBizFQSK;// 分期收款
    sBizs[1] = ConstVO.m_sBizWTDX;// 委托代销
    scDisc[1] = new ServcallVO();
    scDisc[1].setBeanName("nc.itf.ia.pub.ICommonDataQuery");
    scDisc[1].setMethodName("getBizTypeIDs");
    scDisc[1].setParameterTypes(new Class[] {
        String.class, String[].class
    });
    scDisc[1].setParameter(new Object[] {
        m_sCorpID, sBizs
    });

    // 2：是否期初记账
    scDisc[2] = new ServcallVO();
    scDisc[2].setBeanName("nc.itf.ia.pub.ICommonDataQuery");
    scDisc[2].setMethodName("isBeginAccount");
    scDisc[2].setParameterTypes(new Class[] {
      String.class
    });
    scDisc[2].setParameter(new Object[] {
      m_sCorpID
    });

    // 3：获得数据精度
    scDisc[3] = new ServcallVO();
    scDisc[3].setBeanName("nc.itf.ia.pub.ICommonDataQuery");
    scDisc[3].setMethodName("getDataPrecision");
    scDisc[3].setParameterTypes(new Class[] {
      String.class
    });
    scDisc[3].setParameter(new Object[] {
      m_sCorpID
    });

    // 4：起始会计期间
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
    // 5: 是否出现个别指定界面
    sParas[0] = ConstVO.m_sPk_Para[ConstVO.m_iPara_GBZDFS];
    // 5: 是否允许修改、删除非本人制单的单据
    sParas[1] = ConstVO.m_sPk_Para[ConstVO.m_iPara_SFYXSGFBR];
    // 5: 是否保留原始制单人
    sParas[2] = ConstVO.m_sPk_Para[ConstVO.m_iPara_SFBLZDR];
    // 5: 出库单是否允许自定义单价
    sParas[3] = ConstVO.m_sPk_Para[ConstVO.m_iPara_ZDYDJ];

    // 5：是否出现个别指定界面,是否允许修改、删除非本人制单的单据,是否保留原始制单人
    scDisc[5] = new ServcallVO();
    scDisc[5].setBeanName("nc.itf.ia.pub.ICommonDataQuery");
    scDisc[5].setMethodName("getParaValues");
    scDisc[5].setParameterTypes(new Class[] {
        String.class, String[].class
    });
    scDisc[5].setParameter(new Object[] {
        m_sCorpID, sParas
    });

    // 6：当前会计期间起始日期
    scDisc[6] = new ServcallVO();
    scDisc[6].setBeanName("nc.itf.ia.pub.ICommonDataQuery");
    scDisc[6].setMethodName("getMonthDates");
    scDisc[6].setParameterTypes(new Class[] {
        String.class, String.class
    });
    scDisc[6].setParameter(new Object[] {
        m_sCorpID, ce.getAccountPeriod()
    });

    // 执行一次后台调用
    Object[] oParaValue = nc.ui.scm.service.LocalCallService
        .callService(scDisc);

    // 获取结果
    // 0: 各模块是否启用
    Hashtable ht = (Hashtable) oParaValue[0];
    // 库存是否启用
    m_bIsICStart = ((UFBoolean) ht.get(ConstVO.m_sModuleCodeIC)).booleanValue();
    // 销售是否启用
    m_bIsSOStart = ((UFBoolean) ht.get(ConstVO.m_sModuleCodeSO)).booleanValue();
    // 采购是否启用
    m_bIsPOStart = ((UFBoolean) ht.get(ConstVO.m_sModuleCodePO)).booleanValue();
    // 委外加工是否启用
    m_bIsSCStart = ((UFBoolean) ht.get(ConstVO.m_sModuleCodeSC)).booleanValue();

    // 1: 获得业务类型
    ht = (Hashtable) oParaValue[1];
    m_sFQSK = (String) ht.get(ConstVO.m_sBizFQSK);// 分期收款
    m_sWTDX = (String) ht.get(ConstVO.m_sBizWTDX);// 委托代销

    // 2：是否期初记账
    m_bIsBeginAccount = (UFBoolean) oParaValue[2];

    // 3: 获得数据精度
    Integer[] result = (Integer[]) oParaValue[3];
    m_iPeci = new int[result.length];
    for (int i = 0; i < result.length; i++) {
      m_iPeci[i] = result[i].intValue();
    }

    // 4: 启用的会计期间
    m_sStartPeriod = (String) oParaValue[4];

    // 5: 是否出现个别指定界面
    ht = (Hashtable) oParaValue[5];
    String sFlag = (String) ht.get(sParas[0]);
    if (sFlag != null) {
      m_iIndiFlag = new Integer(sFlag.substring(0, 1)).intValue();
    }
    // 5: 是否允许修改、删除非本人制单的单据
    sFlag = (String) ht.get(sParas[1]);
    if (sFlag != null && (sFlag.equals("Y") || sFlag.equals("是"))) {/*-=notranslate=-*/
      m_bAllowChangeBillMkByOthers = true;
    }
    // 5: 是否保留原始制单人
    sFlag = (String) ht.get(sParas[2]);
    if (sFlag != null) {
      if ((sFlag.equals("Y") || sFlag.equals("是"))) {/*-=notranslate=-*/
        m_bKeepOriginalOperator = true;
      }
      else {
        m_bKeepOriginalOperator = false;
      }
    }
    // 5: 出库单是否允许自定义单价
    sFlag = (String) ht.get(sParas[3]);
    if (sFlag != null) {
      if ((sFlag.equals("Y") || sFlag.equals("是"))) {/*-=notranslate=-*/
        m_bAllowDefinePriceByUser = true;
      }
      else {
        m_bAllowDefinePriceByUser = false;
      }
      for (int i = 0; i < ConstVO.m_sInBills.length; i++) {//所有入库单可以自定义
        if (ConstVO.m_sInBills[i].equals(m_sBillType)) {
          m_bAllowDefinePriceByUser = true;
          break;
        }
      }
    }
    // 6: 当前会计期间起始日期
    m_aBeginEndDates = (UFDate[]) oParaValue[6];

    // 自定义项处理
    m_voHeaddef = nc.ui.scm.pub.def.DefSetTool.getDefHead(m_sCorpID,ConstVO.IADEF);
    m_voBodydef = nc.ui.scm.pub.def.DefSetTool.getDefBody(m_sCorpID,ConstVO.IADEF);

  }

  /**
   * 初始化类。
   */
  /* 警告：此方法将重新生成。 */
  private void initialize(String pk_corp) {
    // 获得单位编码
    m_sCorpID = (pk_corp == null ? ce.getCorporationID() : pk_corp);

    setName("BillClientUI");
    setLayout(null);
    setSize(774, 419);
    add(getBillCardPanel(), getBillCardPanel().getName());
    add(getBillListPanel(), getBillListPanel().getName());
    getUIComboBoxSource().addItemListener(billSourceListener);
    setLayout(new java.awt.CardLayout());

    try {
      // 设置按钮状态
      buttonTree = new ButtonTree(getFunCode());
      buttonTree.getButton(IABtnConst.BTN_SWITCH).
      setName(NCLangRes.getInstance().getStrByID("common","UCH022"/* 列表显示 */));
      buttonTree.getButton(IABtnConst.BTN_CHOOSESALEBILL).setVisible(false);
      //add by yhj 2014-03-27
      buttonTree.getButton(IABtnConst.BTN_AUTO_PRICE);//自动询价
      //end
      //add by zy 2019-09-05
      buttonTree.getButton(IABtnConst.BTN_PASS_BIAOCAI);//传标财
      //end
      m_aryButtonGroupCard = buttonTree.getButtonArray();
      m_aryButtonGroupList = buttonTree.getButtonArray();
      btnCtrl = new ButtonControl(buttonTree);
      
      // 获取参数，需要m_sCorpID已初始化
      getParas();
      //将精度设入定位对话框
      getLocateConditionDlg().setPeci(m_iPeci);
    }
    catch (Exception e) {
      e.printStackTrace();
      nc.ui.pub.beans.MessageDialog.showErrorDlg(getParent(),
          nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
              "UPPSCMCommon-000059")/* @res "错误" */, e.getMessage());
    }
  }

  /**
   * 函数功能:初始化数据 <p/> 参数: <p/> 返回值: <p/> 异常:
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
   * 公司的相应会计期间是否已关账
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
      // 查询是否有关账记录
      result = CommonDataBO_Client.queryData(sql.toString());
    }
    catch (Exception e) {
      Log.error(e);
      showErrorMessage(e.getMessage());
      showHintMessage(e.getMessage());
    }
    boolean value = true;
    if (result != null && result.length != 0) {
      Log.debug("存货核算会计期间 " + ce.getAccountPeriod() + " 已经关账，不再处理单据");
      showWarningMessage(NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000291", null, new String[] {
            ce.getAccountPeriod()
          }));
      value = false;
    }
    return value;
  }

  /**
   * 函数功能:为存货编码列加载公式，可以从存货标识列存储的 存货管理档案加载编码，名称，规格，型号，单位，调整前计划价 <p/> 参数: <p/>
   * 返回值: <p/> 异常:
   */
  private void loadInvFormula(BillItem bt) {
    if (bt != null && bt.getKey().equals("cinventoryid")) {
      java.util.Vector vTemp = new java.util.Vector();
      String sFor = "cinventorycode->getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,cinventoryid)";
      vTemp.addElement(sFor);
      // 存货名称
      sFor = "cinventoryname->getColValue(bd_invbasdoc,invname,pk_invbasdoc,cinventorycode)";
      vTemp.addElement(sFor);
      // 规格
      sFor = "cinventoryspec->getColValue(bd_invbasdoc,invspec,pk_invbasdoc,cinventorycode)";
      vTemp.addElement(sFor);
      // 型号
      sFor = "cinventorytype->getColValue(bd_invbasdoc,invtype,pk_invbasdoc,cinventorycode)";
      vTemp.addElement(sFor);
      sFor = "cinventorymeasname->getColValue(bd_invbasdoc,pk_measdoc,pk_invbasdoc,cinventorycode)";
      vTemp.addElement(sFor);
      // 单位
      sFor = "cinventorymeasname->getColValue(bd_measdoc,measname,pk_measdoc,cinventorymeasname)";
      vTemp.addElement(sFor);
      // 存货编码
      sFor = "cinventorycode->getColValue(bd_invbasdoc,invcode,pk_invbasdoc,cinventorycode)";
      vTemp.addElement(sFor);
      String[] sFors = new String[vTemp.size()];
      vTemp.copyInto(sFors);
      bt.setLoadFormula(sFors);
      bt.setEditFormula(sFors);
    }
  }

  /**
   * 函数功能:根据定位条件定位单据 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  private void locateBills() {
    // 按确定按钮
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
        // 判断库存组织
        if (oKCZZ != null && oKCZZ.toString().trim().length() != 0) {
          if (bhvo.getCrdcenterid() == null
              || bhvo.getCrdcenterid().indexOf(oKCZZ.toString().trim()) == -1) {
            continue;
          }
        }
        // 判断仓库编码
        if (oCKBM != null && oCKBM.toString().trim().length() != 0) {
          if (bhvo.getCwarehouseid() == null
              || bhvo.getCwarehouseid().indexOf(oCKBM.toString().trim()) == -1) {
            continue;
          }
        }
        // 判断制单日期
        if (oZDRQ != null && oZDRQ.toString().trim().length() != 0) {
          if (bhvo.getDbilldate() == null
              || bhvo.getDbilldate().toString()
                  .indexOf(oZDRQ.toString().trim()) == -1) {
            continue;
          }
        }
        // 判断单据号
        if (oDJH != null && oDJH.toString().trim().length() != 0) {
          if (bhvo.getVbillcode() == null
              || bhvo.getVbillcode().indexOf(oDJH.toString().trim()) == -1) {
            continue;
          }
        }
        // 判断部门
        if (oBM != null && oBM.toString().trim().length() != 0) {
          if (bhvo.getCdeptid() == null
              || bhvo.getCdeptid().indexOf(oBM.toString().trim()) == -1) {
            continue;
          }
        }
        // 判断业务员
        if (oYWY != null && oYWY.toString().trim().length() != 0) {
          if (bhvo.getCemployeeid() == null
              || bhvo.getCemployeeid().indexOf(oYWY.toString().trim()) == -1) {
            continue;
          }
        }
        // 判断客商
        if (oKS != null && oKS.toString().trim().length() != 0) {
          if (bhvo.getCcustomvendorid() == null
              || bhvo.getCcustomvendorid().indexOf(oKS.toString().trim()) == -1) {
            continue;
          }
        }
        // 表头条件已符合
        bHasFind = true;
        // 判断分录是否有符合条件的记录
        BillItemVO[] btvos = (BillItemVO[]) m_voBills[i].getChildrenVO();
        int length = btvos == null ? 0 : btvos.length;
        if (bHasBodyCondition) {
          // 表体有数据，要检查表体
          bHasFind = false;
          for (int j = 0; j < length; j++) {
            // 判断存货
            if (oCHBM != null && oCHBM.toString().trim().length() != 0) {
              if (btvos[j].getCinventoryid() == null
                  || btvos[j].getCinventoryid()
                      .indexOf(oCHBM.toString().trim()) == -1) {
                continue;
              }
            }
            // 判断批次号
            if (oPCH != null && oPCH.toString().trim().length() != 0) {
              if (btvos[j].getVbatch() == null
                  || btvos[j].getVbatch().indexOf(oPCH.toString().trim()) == -1) {
                continue;
              }
            }
            // 判断数量
            if (oSL != null && oSL.toString().trim().length() != 0) {
              UFDouble ud = new UFDouble(oSL.toString().trim());
              if (btvos[j].getNnumber() == null
                  || btvos[j].getNnumber().equals(ud) == false) {
                continue;
              }
            }
            // 判断单价
            if (oDJ != null && oDJ.toString().trim().length() != 0) {
              UFDouble ud = new UFDouble(oDJ.toString().trim());
              if (btvos[j].getNprice() == null
                  || btvos[j].getNprice().equals(ud) == false) {
                continue;
              }
            }
            // 判断金额
            if (oJE != null && oJE.toString().trim().length() != 0) {
              UFDouble ud = new UFDouble(oJE.toString().trim());
              if (btvos[j].getNmoney() == null
                  || btvos[j].getNmoney().equals(ud) == false) {
                continue;
              }
            }
            // 所有都满足，可能没有录入分录的定位条件
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
            "UPP20143010-000047")/* @res "查找到符合条件的数据" */);
      }
      else {
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000048")/* @res "未查找到符合条件的数据" */);
      }
    }
  }

  /**
   * 函数功能:用公式把库存组织和查询到的存货信息及计划价和基准价填入界面 <p/> 参数：AddQueryVO 包含查询条件和查询的结果 <p/>
   * 返回值: <p/> 异常:
   */
  private void onAdd(AddQueryVO aqVO) {
    try {
      if (aqVO != null) {
        // 设置成增加状态
        m_iStatus = ADD_STATUS;
        // 获得当前日期
        String d1 = ce.getBusinessDate().toString();
        // 增加空单据
        getBillCardPanel().addBill(d1, m_vIsEnable);
        setBtnsForStatus(m_iStatus);
        // 删掉默认增加的第一个空行
        getBillCardPanel().delLine();
        String[] sInvmanIDs = aqVO.getInvmanIDs(); // 存货管理ID
        UFDouble[] ufdbPlannedPrices = aqVO.getPlanedPrices(); // 调整前计划价
        UFDouble[] ufdbBasePrices = aqVO.getBasePrices(); // 调整基准价
        int iLength = 0;
        if (sInvmanIDs != null && ufdbPlannedPrices != null
            && ufdbBasePrices != null) {
          int iLenInv = sInvmanIDs.length;
          int iLenPlan = ufdbPlannedPrices.length;
          int iLenBase = ufdbBasePrices.length;
          if (iLenInv != iLenPlan || iLenInv != iLenBase) {
            Log.info("BillClientUI.onAdd: 查询处的存货数目与价格个数不等");
            showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "20143010", "UPP20143010-000049")/* @res "查询出错,请查看后台日志" */);
            return;
          }
          iLength = iLenInv;
        }
        else {
          Log.info("BillClientUI.onAdd: 查询结果存货ID或计划价或基准价数组为空");
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000049")/* @res "查询出错,请查看后台日志" */);
          return;
        }
        if (iLength == 0) {
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000050")/*
                                                 * @res
                                                 * "未查询到符合条件的记录,请调整查询条件再试或手工增加计划价调整单"
                                                 */);
        }
        else {
          // 为表头的库存组织赋值
          getBillCardPanel().getHeadItem("crdcenterid").setValue(
              aqVO.getRDCenterID());
          // 一次增加多行,数量为查询结果数量
          getBillCardPanel().getBodyPanel().addLine(iLength);
          // 设置合计行在增加每一行时先不计算合计值
          getBillCardPanel().getBillModel().setNeedCalculate(false);
          for (int i = 0; i < iLength; i++) {

            // 为每行添加存货信息
            getBillCardPanel().setBodyValueAt(sInvmanIDs[i], i, "cinventoryid");
            // 显式调用公式
            getBillCardPanel().getBillModel().execEditFormulaByKey(i,
                "cinventoryid");
            // 填写计划价和基准价
            getBillCardPanel().setBodyValueAt(ufdbPlannedPrices[i], i,
                "noriginalprice");
            getBillCardPanel().setBodyValueAt(ufdbPlannedPrices[i], i,
                "nplanedprice");
            getBillCardPanel().setBodyValueAt(ufdbBasePrices[i], i, "nprice");
          }
          // 恢复计算合计值
          getBillCardPanel().getBillModel().setNeedCalculate(true);
          // 重算合计行
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
   * 函数功能:点增行按钮触发 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  private void onButtonAddLineClicked() {
    if (buttonTree.getButton(IABtnConst.BTN_LINE_ADD).isEnabled() == false) {
      return;
    }
    // 对表头合法性判断
    if (getBillCardPanel().getBillModel().getRowCount() == 0) {
      // 单据号可以自动生成，不再必须输入
      // 库存组织是否录入
      BillItem bt = getBillCardPanel().getHeadItem("crdcenterid");
      if (checkBillHeaderItem(bt) == false) {
        return;
      }
      // 制单日期是否录入
      bt = getBillCardPanel().getHeadItem("dbilldate");
      if (checkBillHeaderItem(bt) == false) {
        return;
      }
      // if (m_bIsAdjustBill == false &&
      // m_sBillType.equals(ConstVO.m_sBillXSCBJZD) == false
      // && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD) == false)
      // {
      // //收发类别是否录入
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
      // //业务类型是否录入
      // bt = getBillCardPanel().getHeadItem("cbiztypeid");
      // if (checkBillHeaderItem(bt) == false)
      // {
      // return;
      // }
      // }
    }
    getBillCardPanel().addLine();
    setBtnsForStatus(UPDATE_STATUS);
    // 显示此行
    int i = getBillCardPanel().getRowCount() - 1;
    java.awt.Rectangle rect = getBillCardPanel().getBillTable().getCellRect(i,
        0, false);
    getBillCardPanel().getBillTable().scrollRectToVisible(rect);
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000051")/* @res "增加一行分录" */);
  }

  /**
   * 函数功能:点联查按钮触发 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  private void onButtonAssociateBillsClicked() {
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000052")/* @res "正在查询单据，请稍候" */);
    if (m_voCurBill == null) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000053")/* @res "当前单据没有联查信息" */);
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
   * 函数功能:点审核按钮触发 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  private void onButtonAuditClicked() {
    // 审核单据
    boolean bHasInvi = false;
    int iRowIndex = getBillListPanel().getBodyTable().getSelectedRow();
    if (iRowIndex == -1) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000054")/* @res "请选择要个别指定的分录" */);
      return;
    }

    // 判断是否要出现个别计价分配的界面（入库单不用出现）
    Vector vBillItem = new Vector(1, 1);
    BillHeaderVO bhvo = (BillHeaderVO) m_voCurBill.getParentVO();
    BillItemVO btvo = (BillItemVO) m_voCurBill.getChildrenVO()[iRowIndex];
    // 处理个别计价
    Integer iPriceMode = btvo.getFpricemodeflag();
    Integer iRDFlag = bhvo.getFdispatchflag();
    //
    if (iPriceMode.intValue() == ConstVO.GBJJ) {
      // 是个别计价的存货
      if (iRDFlag.intValue() == 1) {
        if (btvo.getNnumber().doubleValue() > 0) {
          // 是销售成本结转单、材料出库单、其它出库单、报废单、委外加工发料单
          vBillItem.addElement(btvo);
        }
      }
      else if (m_bIsAdjustBill == false && btvo.getNnumber().doubleValue() < 0) {
        // 红字入库单
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
          // 有个别计价的存货,要显示个别指定界面
          getIndividualAllotDlg().showModal();
        }
        else if (getIndividualAllotDlg().getUIButtonOK_pub().isEnabled()) {
          // 可以个别指定
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
   * 函数功能:点取消按钮触发 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  private void onButtonCancelClicked() {
    try {
      m_bIsChangeEvent = false;
      if (m_iStatus == UPDATE_STATUS && m_voCurBill != null) {
        // 设置编辑属性
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
          "UPP20143010-000055")/* @res "解锁失败" */);
      return;
    }
    m_iStatus = CARD_STATUS;
    // 恢复界面数据
    getBillCardPanel().resumeValue();
    if (m_iStatus == ADD_STATUS
        && getBillCardPanel().getHeadItem("vbillcode").getValue() == null) {
      // 除去单据号字段的内容
      getBillCardPanel().getHeadItem("vbillcode").setValue("");
    }
    // 使界面不可编辑
    getBillCardPanel().setEnabled(false);
    // 清空固定资产信息
    // clearFAData();
    setBtnsForStatus(m_iStatus);
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000056")/* @res "取消成功" */);
  }

  /**
   * 函数功能:点参照单据按钮触发 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  private void onButtonChooseSaleBillClicked() {
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000052")/* @res "正在查询单据，请稍候" */);
    // 查询数据
    getSaleBillsChooseDlg().showModal();
  }

  /**
   * 函数功能:点复制行按钮触发 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  private void onButtonCopyLineClicked() {
    int i = getBillCardPanel().getBillTable().getSelectedRow();
    if (i == -1) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000057")/* @res "请选择要复制的行" */);
      return;
    }
    getBillCardPanel().copyLine();
    this.m_bCanPasteLine = true;
    setBtnsForStatus(UPDATE_STATUS);
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000058")/* @res "复制行" */);
  }

  /**
   * 函数功能:点删除按钮触发 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  private void onButtonDelClicked() {
    // 判断是否可以修改其他人制的单据
    if (canAlterBillMadeByOthers(m_voCurBill) == false) {
      return;
    }
    int iStatus = m_iStatus;
    // 显示提示信息
    String sTitle = NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000061");/* @res "单据维护" */
    String sMessage = NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000059")/* @res "确定删除这张单据?" */;
    try {
      // 判断是否已关账,关账后则不能删除单据
      if (!accountIsOpen())
        return;
      /* 当前vo是否在缓存中,如果不在, 是新增加的单据, 如果在, 是查询得到的结果 */
      boolean bCurBillInCache = false;
      
      BillHeaderVO header = (BillHeaderVO ) m_voCurBill.getParentVO();
      BillItemVO[] items = (BillItemVO[]) m_voCurBill.getChildrenVO();
      //是材料出库单并且是假退料单据
      if( header.getCbilltypecode().equals( ConstVO.m_sBillCLCKD) && 
          header.getBwithdrawalflag().booleanValue() ){
        for( int i =0;i< items.length;i++){
          if( items[i].getNnumber().doubleValue() > 0 ){
            String message = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "20143010", "UPP20143010-000295");
            /* @res "本单据是上月假退料单据月结生成的，不能删除" */
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
        // 当前vo不再缓存中,是新增单据,删除时不必设置列表界面的状态
        if (getBillCardPanel().isVisible()) {
          int iType = MessageDialog.showYesNoDlg(this, sTitle, sMessage,
              UIDialog.ID_NO);
          if (iType == MessageDialog.ID_YES) {
            // 删除
            boolean flag = delete(m_voCurBill);
            if (!flag)
              return;// 删除操作出错
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
                "SCMCOMMON", "UPPSCMCommon-000225")/* @res "删除成功" */);
          }
        }
      }
      else {
        // 当前vo在缓存中,是查询出的单据,所以无论处在卡片或列表界面下都要设置列表界面
        // 的状态,如在卡片界面下还要设置卡片的状态
        // 界面上的行号与对应的vo在缓存中的位置的对应
        HashMap hmRow2Index = new HashMap();
        int iFocusRowAfterDelete = -1;
        int iRowNum = getBillListPanel().getHeadTable().getSelectedRowCount();
        if (iRowNum == 0) {
          showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000060")/* @res "请选择要作废的单据" */);
          return;
        }
        /* 记录要删除的单据在界面上的行号和在缓存中的顺序号 */
        if (iRowNum >= 1) {
          sMessage = NCLangRes.getInstance().getStrByID("20143010",
              "UPP20143010-000137", null, new String[] {
                new Integer(iRowNum).toString()
              }/* "确定删除这{0}张单据?" */);
          for (int i = 0; i < iRowNum; i++) {
            int iIndex = getBillListPanel().getHeadTable().getSelectedRows()[i];// 当前行号
            String sCurRow = String.valueOf(iIndex);
            String sCacheRow = String.valueOf(iIndex);
            hmRow2Index.put(sCurRow, sCacheRow);
            if (m_voBills[iIndex].getChildrenVO() == null) {
              // 还没有获得表体数据
              ClientLink cl = ce.getClientLink();
              m_voBills[iIndex] = BillBO_Client.querybillWithOtherTable(null,
                  null, null, null, null, m_voBills[iIndex],
                  new Boolean(false), cl)[0];
            }
          }
          // 找到删除完成后定位到哪一行
          iFocusRowAfterDelete = getBillListPanel().getHeadTable()
              .getSelectedRows()[iRowNum - 1] + 1;// 选中行中最后一行的下一行
          if (iFocusRowAfterDelete >= getBillListPanel().getHeadTable()
              .getRowCount()) {// 选中了最后一行
            iFocusRowAfterDelete = getBillListPanel().getHeadTable()
                .getSelectedRows()[0] - 1;// 选中行中第一行的上一行
          }
          else {
            iFocusRowAfterDelete -= iRowNum;
          }
        }
        int iType = MessageDialog.showYesNoDlg(this, sTitle, sMessage,
            UIDialog.ID_NO);
        if (iType == MessageDialog.ID_YES) {
          /* 对选中的单据执行删除操作 */
          int len = hmRow2Index.size();
          Set keys = hmRow2Index.keySet();
          String[] sKeys = (String[]) keys.toArray(new String[len]);// 当前行号数组
          int[] iRows = new int[len];
          int iRow = 0;
          int iCacheRow = 0;
          BillVO voToBeDeleted = null;
          boolean flag = true;
          for (int i = 0; i < len; i++) {
            iRow = Integer.parseInt(sKeys[i]);// 界面上的行号
            iRows[i] = iRow;
            iCacheRow = Integer.parseInt((String) hmRow2Index.get(sKeys[i]));// 缓存中的顺序号
            voToBeDeleted = m_voBills[iCacheRow];
            // 删除
            flag = delete(voToBeDeleted);
            if (!flag)
              return;// 删除操作出错
            m_voBills[iCacheRow] = null;// 在缓存中标记删除
          }
          getBillListPanel().getHeadBillModel().delLine(iRows);// 列表界面删除
          ArrayList tempvo = new ArrayList(m_voBills.length);// 整理缓存 开始
          for (int i = 0; i < m_voBills.length; i++) {
            if (m_voBills[i] != null) {
              tempvo.add(m_voBills[i]);
            }
          }
          m_voBills = new BillVO[tempvo.size()];
          m_voBills = (BillVO[]) tempvo.toArray(m_voBills);// 整理缓存 结束
          /* 重新设置界面上的选中行 */
          if (m_voBills != null && m_voBills.length != 0) {
            m_voCurBill = null;
            m_iCurBillPrt = -1;
            String sBillId = null;
            if (iFocusRowAfterDelete >= 0) {
              sBillId = (String) getBillListPanel().getHeadBillModel()
                  .getValueAt(iFocusRowAfterDelete, "cbillid");
            }
            if (sBillId != null) {
              // 在缓存中找到界面上选中行的VO
              for (int i = 0; i < m_voBills.length; i++) {
                if (sBillId.equals(m_voBills[i].getParentVO().getPrimaryKey())) {
                  getBillListPanel().getHeadTable().setRowSelectionInterval(
                      iFocusRowAfterDelete, iFocusRowAfterDelete);
                  showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                      "20143010", "UPP20143010-000225")/* @res "删除成功" */);
                  break;
                }
              }
            }
          }
          else {
            // 界面没有数据
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
                "SCMCOMMON", "UPPSCMCommon-000225")/* @res "删除成功" */);
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
              "SCMCOMMON", "UPPSCMCommon-000225")/* @res "删除成功" */);
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
   * 执行删除操作
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
      // 期初单据：目前导入库存的期初单据不能作废，应视为快速录入情况，可作废。
    }
    else if (bhvo.getCsourcemodulename() != null
        && bhvo.getCsourcemodulename().trim().equals(ConstVO.m_sModuleCA) == false
        && bhvo.getCsourcemodulename().trim().equals(ConstVO.m_sModuleIA) == false) {
      // showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010","UPP20143010-000062")/*@res
      // "这张单据是外系统传入的，无法删除"*/);
      // return;
    }

    // 确定删除
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000063")/*
                               * @res "正在删除单据，请稍候"
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
   * 函数功能:点删行按钮触发 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  private void onButtonDelLineClicked() {
    int i = getBillCardPanel().getBillTable().getSelectedRow();
    if (i == -1) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
          "UPPSCMCommon-000201")/*
                                 * @res "请选择要删除的行"
                                 */);
      return;
    }
    getBillCardPanel().delLine();
    setBtnsForStatus(UPDATE_STATUS);
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000066")/*
                               * @res "删除一行分录"
                               */);
  }

  /**
   * 函数功能:点导入按钮触发 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
//  private void onButtonImportClicked() {
//    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
//        "UPP20143010-000067")/* @res "正在导入单据，请稍候" */);
//    // 导入数据，在期初单据录入中实现
//    try {
//      // 导入库存数据
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
//          "20143010", "UPP20143010-000068")/* @res "没有符合条件的期初单据" */);
//      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
//          "UPP20143010-000068")/* @res "没有符合条件的期初单据" */);
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
//    // showHintMessage("导入完成，共导入" + iLength + "张单据");
//  }

  /**
   * 函数功能:点导入按钮触发 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  private void onButtonImportClicked() {
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000067")/* @res "正在导入单据，请稍候" */);
    // 导入数据，在期初单据录入中实现
    BillVO[] bvoBeginBills = null;
    try {
      // 导入库存数据
      BillVO[] bvoICBeginBills = importBills(ConstVO.m_sModuleCodeIC);
      // 导入销售数据
      BillVO[] bvoSOBeginBills = new BillVO[0]; // importBills(ConstVO.m_sModuleCodeSO);
      bvoBeginBills = new BillVO[bvoICBeginBills.length
          + bvoSOBeginBills.length];
      for (int i = 0; i < bvoICBeginBills.length; i++) {
        bvoBeginBills[i] = bvoICBeginBills[i];
      }
      for (int i = 0; i < bvoSOBeginBills.length; i++) {
        bvoBeginBills[i + bvoICBeginBills.length] = bvoSOBeginBills[i];
      }
      // 1、获得库存系统期初单据
      if (bvoBeginBills == null || bvoBeginBills.length == 0) {
        showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000068")/* @res "没有符合条件的期初单据" */);
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000068")/* @res "没有符合条件的期初单据" */);
        return;
      }
      // 2、插入数据
      // 2.1 插入单据
      ClientLink cl = ce.getClientLink();
      m_voBills = BillBO_Client.insertArrayForBeginBills(cl, bvoBeginBills);
      // 3、将库存期初单据显示在列表界面上
      setBillsInList(m_voBills);
      int iLength = 0;
      if (m_voBills != null) {
        iLength = m_voBills.length;
      }
      // showHintMessage("导入完成，共导入" + iLength + "张单据");
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
   * 函数功能:点插入行按钮触发 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  private void onButtonInsertLineClicked() {
    getBillCardPanel().insertLine();
    setBtnsForStatus(UPDATE_STATUS);
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000070")/* @res "插入一行分录" */);
  }

  /**
   * 函数功能:点定位按钮触发 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  private void onButtonLocateClicked() {
    // 显示定位条件对话框
    getLocateConditionDlg().showModal();
  }

  /**
   * 函数功能:点确定按钮触发，保存用户输入的值 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  private boolean onButtonOKClicked() {
    long t1 = System.currentTimeMillis();
    long t2 = 0;
    long t3 = 0;
    long t4 = 0;
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000071")/* @res "正在保存单据，请稍候" */);
    getBillCardPanel().stopEditing();
    // 整理界面数据，将空行删除
    int iRowCount = getBillCardPanel().getRowCount();
    Vector vDelRows = new Vector(1, 1);
    for (int i = iRowCount - 1; i >= 0; i--) {
      Object oInvID = getBillCardPanel().getBillModel().getValueAt(i,
          "cinventoryid");
      if (oInvID == null || oInvID.toString().trim().length() == 0) {
        // 没有输入存货信息，删除此行
        vDelRows.addElement(new Integer(i));
      }
    }
    if (vDelRows.size() != 0) {
      int[] iDelRows = new int[vDelRows.size()];
      for (int i = 0; i < iDelRows.length; i++) {
        iDelRows[i] = ((Integer) vDelRows.elementAt(i)).intValue();
        //
        Log.info("删除第" + (iDelRows[i] + 1) + "行数据");
      }
      getBillCardPanel().getBillModel().delLine(iDelRows);
    }
    // t2 = System.currentTimeMillis() -t1;
    if (getBillCardPanel().getBillModel().getRowCount() == 0) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000072")/* @res "没有表体数据，请调整" */);
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000072")/* @res "没有表体数据，请调整" */);
      return false;
    }
    // 构建VO数组
    if (m_iStatus == ADD_STATUS) {
      // 是增加后保存
      // 通过单据类型获得收发标志
      if ((m_bIsOtherBill || m_bIsOutBill)) {
        // 是出库单
        getBillCardPanel().setHeadItem("fdispatchflag", new Integer(1));
      }
      else {
        // 不是出库单
        getBillCardPanel().setHeadItem("fdispatchflag", new Integer(0));
      }
      BillVO bvo = new BillVO(getBillCardPanel().getRowCount());
      // 插入数据
      try {
        // 是否已关账
        if (!accountIsOpen())
          return false;
        // 把界面上数据写入vo中
        // 获取服务器时间
        UFDateTime time = ClientEnvironment.getServerTime();
        // 制单时间
        getBillCardPanel().setTailItem("tmaketime", time);
        // 最后修改时间
        getBillCardPanel().setTailItem("tlastmaketime", time);
        // 最后修改人
        getBillCardPanel().setTailItem("clastoperatorname",
            ce.getUser().getUserName());
        getBillCardPanel().setTailItem("clastoperatorid",
            ce.getUser().getPrimaryKey());
        getBillCardPanel().getBillValueVO(bvo);
        // 合法性检查并获得数据
        if (checkData(bvo) == false) {
          showErrorMessage(m_sErrorMessage);
          showHintMessage(m_sErrorMessage);
          return false;
        }
        // t3 = System.currentTimeMillis() -t1 - t2;
        ClientLink cl = ce.getClientLink();
        // 插入数据
        if (m_sBillType.equals(ConstVO.m_sBillQCRKD)
            || m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
          // 是期初单据，不生成实时凭证，调用另一个方法
          bvo = BillBO_Client.insertForBeginBills(cl, bvo);
        }
        else {
          bvo = BillBO_Client.insert(cl, bvo);
        }
        t4 = System.currentTimeMillis() - t3 - t2 - t1;
        Log.info("--------------------------插入数据库时间为：" + t4 + "ms");
        m_voCurBill = bvo;
        m_iCurBillPrt = -1;
        // 如果是自动生成的单据号，则把此单据号填入界面相应位置
        BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();
        getBillCardPanel().setHeadItem("vbillcode", bhvo.getVbillcode());
        // 显示信息
        // showHintMessage("增加数据成功" + " " + "共用时："+ t4 + "秒");
        String sInfo = nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000171")/* @res增加数据成功 */;
        String[] value = new String[] {
          new Double(t4 / 1000.0).toString()
        };
        String sTime = nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000172", null, value);/* "共用时："+ t4 + "秒" */
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
            "20143010", "UPP20143010-000073")/* @res "增加数据出错" */
            + e.getMessage());
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000073")/* @res "增加数据出错" */
            + e.getMessage());
        return false;
      }
    }
    else if (m_iStatus == UPDATE_STATUS) {
      // 是修改后保存
      BillVO bvo = new BillVO(getBillCardPanel().getRowCount());
      // 将主键与行号对应起来
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
        // 是否已关账
        if (!accountIsOpen())
          return false;
        // 将行号赋值给行
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
        // 最后修改时间
        getBillCardPanel().setTailItem("tlastmaketime", time);
        // 最后修改人
        getBillCardPanel().setTailItem("clastoperatorname",
            ce.getUser().getUserName());
        getBillCardPanel().setTailItem("clastoperatorid",
            ce.getUser().getPrimaryKey());
        /* 为检查计划价调整单可以同时有2条分录调同一个存货在这里先检查一下全部数据 */
        getBillCardPanel().getBillValueVO(bvo);
        if (checkData(bvo) == false) {
          showErrorMessage(m_sErrorMessage);
          showHintMessage(m_sErrorMessage);
          return false;
        }

//        bvo = (BillVO) getBillCardPanel().getBillValueChangeVO(
//            "nc.vo.ia.bill.BillVO", "nc.vo.ia.bill.BillHeaderVO",
//            "nc.vo.ia.bill.BillItemVO");
        // 修改记录
        // 合法性检查并获得数据
        if (checkData(bvo) == false) {
          showErrorMessage(m_sErrorMessage);
          showHintMessage(m_sErrorMessage);
          return false;
        }
        // 将数据排列为删除的、修改的、增加的
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
        // 排序
        BillItemVO[] curbtVOs = new BillItemVO[vAddData.size()
            + vUpdateData.size() + vDeleteData.size()];
        int ivo = 0;
        int jvo = 0;
        int hvo = 0;
        int iIndex = 0;
        // 删除的从后向前
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
        // 修改数据
        if (m_sBillType.equals(ConstVO.m_sBillQCRKD)
            || m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
          // 是期初单据，不处理实时凭证，调用另一个方法
          m_voCurBill = BillBO_Client.updateForBeginBill(cl, m_voCurBill, bvo,
              ce.getUser().getPrimaryKey());
        }
        else {
          m_voCurBill = BillBO_Client.update(cl, m_voCurBill, bvo,
              new UFBoolean((m_bIsOtherBill || m_bIsOutBill)), new UFBoolean(
                  m_bIsAdjustBill), ce.getUser().getPrimaryKey());
        }
        // 重新组织缓存结果集
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
            // 更新列表形式的表体数据
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
              // 设置假退料，暂估，调拨类型, 来源单据类型
              setComboBoxInHeadFromVO(m_voBills[iii], false, iii);
            }
            // 如果参数设定为不保留原始制单人，则把当前操作员设为制单人
            if (!m_bKeepOriginalOperator) {
              getBillCardPanel().setTailItem("coperatorname",
                  ce.getUser().getUserName());
              getBillCardPanel().setTailItem("coperatorid",
                  ce.getUser().getPrimaryKey());
            }
            // 显式调用方法,使公式可用
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
            "20143010", "UPP20143010-000074")/* @res "修改数据出错" */
            + e.getMessage());
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000074")/* @res "修改数据出错" */
            + e.getMessage());
        return false;
      }
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000075")/* @res "修改数据成功" */);
    }
    // 刷新ts
    // freshTs(m_voCurBill);
    getBillCardPanel().setEnabled(false);
    // 设置可编辑状态
    m_iStatus = CARD_STATUS;
    setBtnsForStatus(m_iStatus);
    // 将VO数据设置到界面上
    setVODataToInterface(m_voCurBill);
    // 删除表格的可编辑列的属性
    getBillCardPanel().getBillModel().clearCellEdit();
    try {
      // 是否保存后立刻审核
      if (m_sBillType.equals(ConstVO.m_sBillQCRKD) == false
          && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD) == false) {
        String sAudit = ce.getParaValue(m_sCorpID, ConstVO.m_iPara_ZDSHJZ);
        if (sAudit != null) {
          if (sAudit.equalsIgnoreCase("Y") || sAudit.equalsIgnoreCase("是")) {/*-=notranslate=-*/
            // 单据保存自动成本计算，以前的版本
            auditOneBill(false, "012");
          }
          else if (sAudit.equalsIgnoreCase("N") || sAudit.equalsIgnoreCase("否")) {/*-=notranslate=-*/
            // 单据保存不自动成本计算，以前的版本
          }
          else if (sAudit.trim().length() != 0) {
            // 新参数，有需要成本计算的
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
          "UPP20143010-000076")/* @res "自动成本计算出错" */
          + e.getMessage());
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000076")/* @res "自动成本计算出错" */
          + e.getMessage());
      return false;
    }
    return true;
  }
  
  /**
   * 切换显示的单据
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
		      // 不是当前单据类型，提示
		      // showWarningMessage("当前功能点单据类型编码是"+"'" + m_sBillType +
		      // "'"+","+"但选择的单据单据类型编码是"+" '" + sBilltype + "'"+"，所以不能显示表单界面");
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

        // 设置来源单据类型
        bt = getBillCardPanel().getHeadItem("cbillsource");
        if (bt != null
            && (m_sBillType.equals(ConstVO.m_sBillXSCBJZD) || m_sBillType
                .equals(ConstVO.m_sBillQCXSCBJZD))) {
          BillItemVO btvo = (BillItemVO) m_voCurBill.getChildrenVO()[0];
          String sSourceBillType = btvo.getCsourcebilltypecode();
          UIComboBox uibox = (UIComboBox) bt.getComponent();
          if (sSourceBillType != null
              && sSourceBillType.equals(ConstVO.m_sBillXSFP)) {
            uibox.setSelectedIndex(m_ComboItemsVO.type_salebill);// ConstVO.m_sBillXSFPName);//销售发票
          }
          else if (sSourceBillType != null
              && sSourceBillType.equals(ConstVO.m_sBillXSCKD)) {
            uibox.setSelectedIndex(m_ComboItemsVO.type_saleoutlist);// ConstVO.m_sBillXSCKDName);//销售出库单
          }
          else {
            uibox.setSelectedIndex(-1);// tem("无");
          }
        }
        // 列表到表单的处理
        dispListToCard();
      }
      else {
        getBillCardPanel().getBillData().clearViewData();
      }
      setBtnsForStatus(m_iStatus);
      // 保存当前界面数据
      getBillCardPanel().updateValue();
    }

  }

  /**
   * 点首张按钮,将显示缓存中第一张单据
   */
  private void onButtonFirstClicked() {
    if (m_voBills != null) {
      switchBill(0);
    }
  }

  /**
   * 点上张按钮,将显示缓存中上张单据
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
   * 点下张按钮,将显示缓存中下张单据
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
   * 点末张按钮,将显示缓存中最后一张单据
   */
  private void onButtonLastClicked() {
    if (m_voBills != null) {
      switchBill(m_voBills.length - 1);
    }
  }

  /**
   * 函数功能:点粘贴行按钮触发 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  private void onButtonPasteLineClicked() {
    getBillCardPanel().pasteLine();
    setBtnsForStatus(UPDATE_STATUS);
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000077")/* @res "粘贴行" */);
  }

  /**
   * 函数功能:点打印按钮触发 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  private void onButtonPrintDirectClicked() {
    String strMsg = "";
    try {
      String sModuleCode = "20142010";
      if (getModuleCode() != null && getModuleCode().trim().length() != 0) {
        sModuleCode = getModuleCode();
      }

      if (m_iStatus == CARD_STATUS) {
        // 表单打印
        showHintMessage(PrintLogClient.getBeforePrintMsg(false, false));
        strMsg = getBillCardPanel().printData(m_bd, getBillListPanel(),
            m_sBillType, m_voCurBill, sModuleCode);

      }
      else if (m_iStatus == LIST_STATUS) {
        // 列表界面批打印
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
   * 函数功能:点打印按钮触发 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  private void onButtonPrintPreviewClicked() {
    String strMsg = "";
    try {
      String sModule = "20142010";
      if (getModuleCode() != null && getModuleCode().trim().length() != 0) {
        sModule = getModuleCode();
      }

      if (m_iStatus == CARD_STATUS) {
        // 表单打印
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
        // 列表界面批打印
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
   * 函数功能:点查询按钮触发 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  private void onButtonQueryClicked() {
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000082")/* @res "正在准备查询条件输入界面，请稍候" */);
    getQueryClientDlg().showModal();
    if (getQueryClientDlg().isCloseOK()) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000052")/* @res "正在查询单据，请稍候" */);
      // 查询单据
      queryBills();
    }
    // 查询数据
    // getQueryConditionDlg().showModal();
  }

  /**
   * 函数功能:点刷新按钮触发 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  private void onButtonRefreshClicked() {
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000083")/* @res "正在刷新单据，请稍候" */);
    // 重新查询数据
    queryBills();
  }

  /**
   * 函数功能:根据查询条件查询单据 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  private void queryBills() {
    // 按确定按钮
    try {
      String[] sTable = getQueryClientDlg().getTables();
      String[] sConnection = getQueryClientDlg().getConnections();
      String[] sConditions = getQueryClientDlg().getConditions();
      /*
       * String[] sTable ----- 要连接的表 String[] sConnectParam ----- 连接条件 String[]
       * sCondition ----- 其它条件 BillVO condBillAllVO ------ 单据条件VO
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
//          "UCH021"/* 卡片显示 */));
      setButtons(m_aryButtonGroupList);
    }
    catch (Exception ex) {
      ExceptionUITools.showMessage(ex, this);
      return;
    }
    if (m_voBills != null && m_voBills.length != 0) {
      // showHintMessage("查询到" + m_voBills.length + "条记录，这是第一条");
      String[] value = new String[] {
        new Integer(m_voBills.length).toString()
      };
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000139", null, value));
    }
    else {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000084")/* @res "未查询到记录" */);
    }
  }

  /**
   * 函数功能:设置按钮状态 <p/> 参数: ButtonObject[] sButtons ----- 按钮 <p/> 返回值: <p/> 异常:
   */
  private void setBtnsForBilltypes(ButtonObject[] sButtons) {
    if (m_bIsSOStart && m_sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
      // 销售启用了
      // 是销售成本结转单
      // 增加、删除、复制、增行、删行、粘贴行、插入行不可用
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
      // 采购启用了
      // 是采购入库单或损益调整单
      // 增加、删除、复制、增行、删行、粘贴行、插入行不可用
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
      // 是入库调整
      UFBoolean bIsEditable = new UFBoolean(true);
      if (m_voCurBill != null) {
        String sSource = ((BillHeaderVO) m_voCurBill.getParentVO())
            .getCsourcemodulename();
        if (sSource != null && sSource.equals(ConstVO.m_sModulePO)) {
          // 是采购传人的
//          bIsEditable = new UFBoolean(false);//edit by shikun 结算单删除后入库调整单没有删除
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
      // 委外加工启用了
      // 是委外加工收货单
      // 增加、删除、复制、增行、删行、粘贴行、插入行不可用
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
      // 库存启用了
      if (m_bIsAdjustBill == false
          && m_sBillType.equals(ConstVO.m_sBillQCRKD) == false
          && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD) == false
          && m_sBillType.equals(ConstVO.m_sBillCLCKD) == false) {
        // 不是调整单，不是期初单据
        // 增加、删除、复制、增行、删行、粘贴行、插入行、复制行不可用
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
        // 是材料出库单
        UFBoolean bIsWithdraw = new UFBoolean(false);
        if (m_voCurBill != null) {
          bIsWithdraw = ((BillHeaderVO) m_voCurBill.getParentVO())
              .getBwithdrawalflag();
        }
        // 删除、复制、增行、删行、粘贴行、插入行不可用
        if (m_iStatus == INIT_STATUS) {
          // 初始化
          btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
        }
        else if (m_iStatus == CARD_STATUS) {
          // 浏览
          if (bIsWithdraw.booleanValue()) {
            // 是假退料，可以删除
            btnCtrl.set(true,IABtnConst.BTN_BILL_DELETE);
          }
          else {
            btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
          }
        }
        else if (m_iStatus == ADD_STATUS) {
          // 增加
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
          // 修改
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
          // 列表
          if (bIsWithdraw.booleanValue()) {
            UFBoolean bAudit = ((BillHeaderVO) m_voCurBill.getParentVO())
                .getBauditedflag();
            if (bAudit.booleanValue() == false) {
              // 是假退料，未成本计算，可以删除
              btnCtrl.set(true,IABtnConst.BTN_BILL_DELETE);
            }
            else {
              // 是假退料，已成本计算，可以删除
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
      // 是期初单据且已期初记账
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
      // 库存没有启用，不能导入
      btnCtrl.set(false,IABtnConst.BTN_IMPORT_BILL);
    }
    // 处理右键菜单
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
        // 是当前单据类型
      }
      else {
        // 不是当前单据类型，不可个别指定
        btnCtrl.set(false,IABtnConst.BTN_AUDIT);
      }
    }
    setButtons(sButtons);
  }

  /**
   * 把vo中的下拉多选框的选中值转换为显示给用户的字符串
   * 
   * @param vo
   *          BillHeaderVO 表头vo
   * @param isCardPanel
   *          boolean true：表单界面上 false：列表界面上
   * @param iRownum
   *          int 在列表界面下记录行数，在表单界面调用时传0即可
   */
  private void setComboBoxInHeadFromVO(BillVO vo, boolean isCardPanel,
      int iRownum) {
    if (vo == null || vo.getParentVO() == null) {
      Log.info("程序错误：参数不正确(BillClientUI.setComboBoxInHeadFromVO)");
      return;
    }
    BillHeaderVO hvo = (BillHeaderVO) vo.getParentVO();
    // 处理暂估选项
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
    // 处理假退料选项
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
    // 处理调拨类型
    if (hvo.getFallocflag() != null) {
      Integer iFlag = hvo.getFallocflag();
      // 直运调拨
      String sAllocFlag = m_ComboItemsVO.name_transfer_direct;
      if (iFlag.intValue() == m_ComboItemsVO.type_transfer_instore) {
        // 入库调拨
        sAllocFlag = m_ComboItemsVO.name_transfer_instore;
      }
      else if (iFlag.intValue() == m_ComboItemsVO.type_transfer_stock) {
        // 集采调拨
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
    // 如果是销售成本结转单，设置来源单据类型
    if (ConstVO.m_sBillXSCBJZD.equals(m_sBillType)) {
      // 销售成本结转单
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
          //销售发票
          sSourceName = m_ComboItemsVO.name_salebill;
        }
        else if (sSource.equals(ConstVO.m_sBillXSCKD)) {
          //销售出库单          
          sSourceName = m_ComboItemsVO.name_saleoutlist;
        }
        else if (sSource.equals(ConstVO.m_sBillKCTSD)) {
          //库存途损单
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
   * 把vo中的下拉多选框的选中值转换为显示给用户的字符串
   * 
   * @param bvo
   *          BillItemVO 表体vo
   * @param isCardPanel
   *          boolean true：表单界面上 false：列表界面上
   */
  private void setComboBoxInBodyFromVO(BillItemVO bvo, boolean isCardPanel,
      int iRownum) {
    if (bvo == null) {
      Log.info("程序错误：参数不正确(BillClientUI.setComboBoxInBody)");
      return;
    }
    if (bvo.getBlargessflag() != null) {
      if (getBillCardPanel().getBodyItem("blargessflag") != null) {
        //赠品
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
    //数据获取方式名称
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
    //计价方式名称
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
   * 函数功能:将增加或修改后的数据赋值到界面上 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  private void setVODataToInterface(BillVO bvo) {
    // 设置界面数据
    BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();
    getBillCardPanel().getHeadItem("pk_corp").setValue(m_sCorpID);
    getBillCardPanel().getHeadItem("cbilltypecode").setValue(m_sBillType);
    getBillCardPanel().getHeadItem("cbillid").setValue(bhvo.getCbillid());
    // 将会计年度、会计月份
    getBillCardPanel().getHeadItem("caccountyear").setValue(
        bhvo.getCaccountyear());
    getBillCardPanel().getHeadItem("caccountmonth").setValue(
        bhvo.getCaccountmonth());
    // 设置暂估，假退料，调拨类型
    setComboBoxInHeadFromVO(bvo, true, 0);

    boolean bEst = bhvo.getBestimateflag().booleanValue();
    String sEst = m_ComboItemsVO.name_estimated_no;// "非暂估";
    if (bEst) {
      sEst = m_ComboItemsVO.name_estimated_yes;// "暂估";
    }
    getBillCardPanel().getHeadItem("bestimateflag").setValue(sEst);
    boolean bIsWith = bhvo.getBwithdrawalflag().booleanValue();
    String sIsWith = m_ComboItemsVO.name_no;// "否";
    if (bIsWith) {
      sIsWith = m_ComboItemsVO.name_yes;// "是";
    }
    getBillCardPanel().getHeadItem("bwithdrawalflag").setValue(sIsWith);

    getBillCardPanel().getHeadItem("bauditedflag").setValue(
        bhvo.getBauditedflag());
    getBillCardPanel().getTailItem("coperatorid").setValue(
        bhvo.getCoperatorid());
    for (int i = 0; i < bvo.getChildrenVO().length; i++) {
      BillItemVO btvo = (BillItemVO) bvo.getChildrenVO()[i];
      btvo.setStatus(BillModel.NORMAL);
      // 公司、单据类型、单据号
      getBillCardPanel().getBillModel().setValueAt(m_sCorpID, i, "pk_corp");
      getBillCardPanel().getBillModel().setValueAt(m_sBillType, i,
          "cbilltypecode");
      getBillCardPanel().getBillModel().setValueAt(bhvo.getVbillcode(), i,
          "vbillcode");
      // 主子ID
      getBillCardPanel().getBillModel().setValueAt(btvo.getCbill_bid(), i,
          "cbill_bid");
      getBillCardPanel().getBillModel().setValueAt(btvo.getCbillid(), i,
          "cbillid");
      // 是否调整了分录
      getBillCardPanel().getBillModel().setValueAt(btvo.getBadjustedItemflag(),
          i, "badjustedItemflag");
      getBillCardPanel().getBillModel().setValueAt(btvo.getFdatagetmodelflag(),
          i, "fdatagetmodelflag");
      getBillCardPanel().getBillModel().setValueAt(
          btvo.getFolddatagetmodelflag(), i, "folddatagetmodelflag");
      getBillCardPanel().getBillModel().setValueAt(
          btvo.getFoutadjustableflag(), i, "foutadjustableflag");
      // 设置行号
      getBillCardPanel().getBillModel().setValueAt(new Integer(i + 1), i,
          "irownumber");
      // 设置表体
      setComboBoxInBodyFromVO(btvo, true, i);

      // 设置业务日期
      getBillCardPanel().getBillModel().setValueAt(btvo.getDbizdate(), i,
          "dbizdate");
      if (m_iStatus == ADD_STATUS) {
        // 设置审核信息
        getBillCardPanel().getBillModel().setValueAt(btvo.getCauditorid(), i,
            "cauditorid");
        getBillCardPanel().getBillModel().setValueAt(btvo.getDauditdate(), i,
            "dauditdate");
        getBillCardPanel().getBillModel().setValueAt(btvo.getIauditsequence(),
            i, "iauditsequence");
        // 计价方式
        getBillCardPanel().getBillModel().setValueAt(btvo.getFpricemodeflag(),
            i, "fpricemodeflag");
        // 单价、金额，可能审核了
        getBillCardPanel().getBillModel().setValueAt(btvo.getNprice(), i,
            "nprice");
        getBillCardPanel().getBillModel().setValueAt(btvo.getNmoney(), i,
            "nmoney");
        // 计算进项税转出金额
        calcTransIncomeTaxMny(i);
      }
      // 设置固定资产
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
   * 函数功能:将参照出的单据赋值到表单界面上 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  private void showBillInForm() {
    try {
      getBillCardPanel().setBillValueVO(
          getSaleBillsChooseDlg().getChooseBillVO());
      // 设置来源单据类型
      BillItem bt = getBillCardPanel().getHeadItem("cbillsource");
      if (bt != null && m_sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
        m_bIsChangeEvent = false;
        getUIComboBoxSource().setSelectedIndex(m_ComboItemsVO.type_salebill);
        m_bIsChangeEvent = true;
      }
      // 将单据ID清空
      getBillCardPanel().setHeadItem("cbillid", null);
      // 将单据号清空
      getBillCardPanel().setHeadItem("vbillcode", null);
      for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
        // 将单据分录ID清空
        getBillCardPanel().getBillModel().setValueAt(null, i, "cbill_bid");
        // 将单据ID清空
        getBillCardPanel().getBillModel().setValueAt(null, i, "cbillid");
        // 单据号为空
        getBillCardPanel().getBillModel().setValueAt(null, i, "vbillcode");
        // 累计发出数量为空
        getBillCardPanel().getBillModel()
            .setValueAt(null, i, "nsettledsendnum");
        // 累计回冲数量为空
        getBillCardPanel().getBillModel().setValueAt(null, i,
            "nsettledretractnum");
        // 累计回冲数量为空
        getBillCardPanel().getBillModel().setValueAt(null, i, "cbill_bid");
        // 设置csaleadviceid
        getBillCardPanel().getBillModel().setValueAt(
            getSaleBillsChooseDlg().getChooseBillVO().getParentVO()
                .getPrimaryKey(), i, "csaleadviceid");
        // 设置ccsaleadviceitemid
        getBillCardPanel().getBillModel().setValueAt(
            getSaleBillsChooseDlg().getChooseBillVO().getChildrenVO()[i]
                .getPrimaryKey(), i, "ccsaleadviceitemid");
        // 审核人为空
        getBillCardPanel().getBillModel().setValueAt(null, i, "cauditorid");
      }
      // 获得当前日期
      String d = ce.getBusinessDate().toString();
      getBillCardPanel().setHeadItem("dbilldate", d);
      // 库存组织、仓库、业务类型、收发类别、部门、业务员、经手人、客户不能修改
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
      // 不能修改部分表体数据
      BillItem[] btItemDatas = getBillCardPanel().getBodyItems();
      for (int i = 0; i < btItemDatas.length; i++) {
        if (btItemDatas[i].getKey().equals("nnumber") == false) {
          getBillCardPanel().getBodyItem(btItemDatas[i].getKey()).setEnabled(
              false);
        }
      }
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000085")/* @res "选择完成，可以修改数量" */);
    }
    catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }

  /**
   * 函数功能:点增加按钮触发 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  protected void addBill() throws Exception {
    m_iStatus = ADD_STATUS;
    // 获得当前日期
    String d = ce.getBusinessDate().toString();
    if (m_sBillType.equals(ConstVO.m_sBillQCRKD)
        || m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
      // 是期初单据，带出启用日期的前一天
      // 期初单据
      UFDate dBeginDate = CommonDataBO_Client.getMonthBeginDate(m_sCorpID,
          m_sStartPeriod);
      if (dBeginDate == null) {
        // 会计期间没有定义
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000086")/* @res "当前会计期间没有定义" */);
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000086")/* @res "当前会计期间没有定义" */);
        return;
      }
      UFDate dPerDate = dBeginDate.getDateBefore(1);
      d = dPerDate.toString();
    }
    getBillCardPanel().addBill(d, m_vIsEnable);
    if (m_sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
      // 设置默认单据来源
      //getUIComboBoxSource().setSelectedIndex(m_ComboItemsVO.type_saleoutlist);// 销售出库单
      if (m_bIsICStart) {
        // 如果库存启用，默认是销售出库单
      	getUIComboBoxSource().setSelectedIndex(m_ComboItemsVO.type_saleoutlist);
      }
      else {
        // 如果库存未启用，默认不选择
      	getUIComboBoxSource().setSelectedIndex(-1);
      }
    }
    BillItem bt = m_bd.getHeadItem("bwithdrawalflag");
    if (bt != null && bt.getComponent() instanceof UIComboBox) {
      UIComboBox box = (UIComboBox) bt.getComponent();
      if (m_bIsICStart) {
        // 如果库存启用，默认是假退料的
        box.setSelectedIndex(m_ComboItemsVO.type_yes);
      }
      else {
        // 如果库存未启用，默认不是假退料的
        box.setSelectedIndex(m_ComboItemsVO.type_no);
      }
    }
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000087")/* @res "手工增加单据" */);
  }

  /**
   *  得到一组不重复的存货主键值
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
   * 函数功能:单据合法性检查 <p/> 参数: <p/> 返回值:是否合法 <p/> 异常:
   */
  protected boolean checkData(BillVO bvo) throws Exception {

    // 整理表头数据
    BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();
    if (bhvo == null) {
      m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000091")/* @res "请输入表头数据" */;
      return false;
    }
    // 设置单位ID、单据类型
    bhvo.setPk_corp(m_sCorpID);
    bhvo.setCbilltypecode(m_sBillType);

    String sSourceModule = "";
    if (m_iStatus == ADD_STATUS) {
      // 将会计年度、会计月份设为空
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
      // 设置来源信息
      bhvo.setCsourcemodulename(sSourceModule);
      // 如果参数设定为不保留原始制单人，则把当前操作员设为制单人
      if (!m_bKeepOriginalOperator) {
        bhvo.setCoperatorid(ce.getUser().getPrimaryKey());
      }
    }
    // 获得表头的制单日期
    UFDate dBillDate = bhvo.getDbilldate();
    UFDate dBeginDate = null;
    if (dBillDate == null) {
      m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000088")/* @res "没有输入制单日期，请调整" */;
      return false;
    }
    else if (m_sBillType.equals(ConstVO.m_sBillQCRKD)
        || m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
      // 期初单据
      UFDate[] sDates = CommonDataBO_Client.getMonthDates(m_sCorpID,
          m_sStartPeriod);
      dBeginDate = sDates[0];
      UFDate dEndDate = sDates[1];
      if (dBeginDate == null || dEndDate == null) {
        // 会计期间没有定义
        m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000086")/* @res "当前会计期间没有定义" */;
        return false;
      }
      if (dBillDate.toString().compareTo(dBeginDate.toString()) >= 0) {
        // 制单日期晚于启用日期
        // m_sErrorMessage = "期初单据制单日期" + dBillDate + "应早于系统启用日期" + dBeginDate +
        // "，请调整";
        String[] value = new String[] {
            dBillDate.toString(), dBeginDate.toString()
        };
        m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000140", null, value);
        return false;
      }
    }
    else if (m_iStatus == ADD_STATUS) {
      // 日常单据
      // UFDate[] sDates = CommonDataBO_Client.getMonthDates(m_sCorpID,
      // ce.getAccountPeriod());
      dBeginDate = m_aBeginEndDates[0];// sDates[0];
      UFDate dEndDate = m_aBeginEndDates[1];// sDates[1];
      if (dBeginDate == null || dEndDate == null) {
        // 当前会计期间没有定义
        m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000086")/* @res "当前会计期间没有定义" */;
        return false;
      }
      if (dBillDate.compareTo(dBeginDate) < 0) {
        // 小于开始日期
        // m_sErrorMessage = "制单日期早于本月开始日期" + dBeginDate + "，请调整";
        String[] value = new String[] {
          dBeginDate.toString()
        };
        m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000141", null, value);
        return false;
      }
      else if (dBillDate.compareTo(dEndDate) > 0) {
        // 大于截止日期
        // m_sErrorMessage = "制单日期晚于本月截止日期" + dEndDate + "，请调整";
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
      // 库存已经启用，增加材料出库单，不是假退料，报错
      m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000092")/* @res "库存管理已经启用，存货核算只能增加假退料类型的材料出库单，请调整" */;
      return false;
    }
    // 是否输入了被调整单据类型
    boolean bIsInputBillType = false;
    if (m_bIsInAdjustBill
        && getBillCardPanel().getBodyItem("cadjustbilltype") != null) {
      // 入库调整单输入被调整单据类型，必须有被调整单据号和单据分录
      String sBillType = getBillCardPanel().getBodyItem("cadjustbilltype")
          .getValue();
      if (sBillType != null && sBillType.trim().length() != 0) {
        String sBillID = getBillCardPanel().getBodyItem("cadjustbill")
            .getValue();
        if (sBillID == null || sBillID.trim().length() == 0) {
          m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000093")/*
                                                 * @res
                                                 * "输入被调整单据类型后必须输入被调整单据号，请调整"
                                                 */;
          return false;
        }
        bIsInputBillType = true;
      }
    }

    // 单据合法性检查（主要检查必输项的非空性）
    // bhvo.validate();
    // 由于validate方法名与父类smartVO中throws子句冲突，所以改名verify
    bhvo.verify();

    // 期初单据检查界面行内容，可能是导入的数据，没有金额，修改时也要判断
    if ((m_sBillType.equals(ConstVO.m_sBillQCRKD) || m_sBillType
        .equals(ConstVO.m_sBillQCXSCBJZD))
        && m_iStatus == UPDATE_STATUS) {
      ArrayList errFields = new ArrayList(); // errFields record those
      // null
      // fields that cannot be null.
      String sErrorString = nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "20143010", "UPP20143010-000094")/* @res "下列字段不能为空:" */;
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
          // errFields.add("第" + (i + 1) + "行的数量");
          String[] value = new String[] {
            String.valueOf(i + 1)
          };
          errFields.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
              "UPP20143010-000143", null, value));
        }
        else if (m_nnumber != null) {
          double dNumber = new UFDouble(m_nnumber.toString()).doubleValue();
          if (dNumber < 0 && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
            // 期初销售成本结转单数量必须大于0
            vNumString.addElement(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "20143010", "UPP20143010-000095")/* @res "期初销售成本结转单单据数量应大于0" */);
          }
        }
        // 判断金额
        // 金额是空且是（期初入库单（非计划价）或期初出库单）
        if (m_nmoney == null
            && ((iPriceFlag != null
                && new Integer(iPriceFlag.toString()).intValue() != nc.vo.ia.pub.ConstVO.JHJ && m_sBillType
                .equals(ConstVO.m_sBillQCRKD)) || m_sBillType
                .equals(ConstVO.m_sBillQCXSCBJZD))) {

          // errFields.add(new String("第" + (i + 1) + "行的金额"));
          String[] value = new String[] {
            String.valueOf(i + 1)
          };
          errFields.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
              "UPP20143010-000144", null, value));
        }
        // 金额是空且是入库调整单或出库调整单
        else if (m_nmoney == null
            && (m_sBillType.equals(ConstVO.m_sBillRKTZD) || m_sBillType
                .equals(ConstVO.m_sBillCKTZD))) {
          // errFields.add(new String("第" + (i + 1) + "行的金额"));
          String[] value = new String[] {
            String.valueOf(i + 1)
          };
          errFields.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
              "UPP20143010-000144", null, value));
        }
        else if (m_nmoney != null) {
          double dMoney = new UFDouble(m_nmoney.toString()).doubleValue();
          if (dMoney < 0 && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
            // 期初销售成本结转单金额必须打大于0
            vNumString
                .addElement(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                    "20143010", "UPP20143010-000096")/*
                                                       * @res
                                                       * "期初销售成本结转单单据金额应大于等于0"
                                                       */);
          }
        }
        // 判断单价
        if (m_nprice == null && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
          // errFields.add(new String("第" + (i + 1) + "行的单价"));
          String[] value = new String[] {
            String.valueOf(i + 1)
          };
          errFields.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
              "UPP20143010-000145", null, value));
        }
        else if (m_nprice != null) {
          double dPrice = new UFDouble(m_nprice.toString()).doubleValue();
          if (dPrice < 0 && m_sBillType.equals(ConstVO.m_sBillQCRKD) == false) {
            // vNumString.addElement("第" + (i + 1) + "行的单价不能小于0");
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

    // 整理表体数据
    BillItemVO[] btvos = (BillItemVO[]) bvo.getChildrenVO();
    if (m_iStatus == ADD_STATUS) {
      if (btvos == null || btvos.length == 0) {
        m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000097")/* @res "请输入表体数据" */;
        return false;
      }
    }
    if (m_iStatus == UPDATE_STATUS
        && (m_sBillType.equals(ConstVO.m_sBillXSCBJZD) || m_sBillType
            .equals(ConstVO.m_sBillQCXSCBJZD))) {
      // 设置来源单据类型
      BillItem bt2 = getBillCardPanel().getHeadItem("cbillsource");
      if (bt2 != null) {
        int iIndex = ((UIComboBox) bt2.getComponent()).getSelectedIndex();
        String sSourceBillType = ConstVO.m_sBillXSCKD;
        if (iIndex != -1 && iIndex == m_ComboItemsVO.type_salebill) {// 销售发票
          sSourceBillType = ConstVO.m_sBillXSFP;
        }
        // 判断业务类型
        boolean bIsFQ = false; // 是否是分期收款
        boolean bIsWT = false; // 是否是委托代销
        BillItem bt3 = getBillCardPanel().getHeadItem("cbiztypeid");
        if (bt3 != null && sSourceBillType.equals(ConstVO.m_sBillXSFP)) {
          String sBizTypeID = bt3.getValue();
          if (sBizTypeID != null && sBizTypeID.trim().length() != 0) {
            UIRefPane pane = (UIRefPane) bt3.getComponent();
            Object oRule = pane.getRef().getRefModel().getValue("verifyrule");
            if (oRule != null) {
              if (oRule.toString().equals("W")) {
                // 是委托代销
                bIsWT = true;
              }
              else if (oRule.toString().equals("F")) {
                // 是分期收款
                bIsFQ = true;
              }
            }
            for (int i = 0; i < m_voCurBill.getChildrenVO().length; i++) {
              // 来源是销售发票
              String sSaleAdviceID = ((BillItemVO) m_voCurBill.getChildrenVO()[i])
                  .getCsaleadviceid();
              if (bIsFQ) {
                if (sSaleAdviceID == null || sSaleAdviceID.trim().length() == 0) {
                  // 没有录入对应销售出库单
                  m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance()
                      .getStrByID("20143010", "UPP20143010-000089")/*
                                                                     * @res
                                                                     * "当前单据来源是销售发票，业务类型是分期收款,但没有选择对应的销售出库单，请调整"
                                                                     */;
                  return false;
                }
              }
              else if (bIsWT) {
                if (sSaleAdviceID == null || sSaleAdviceID.trim().length() == 0) {
                  // 没有录入对应销售出库单
                  m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance()
                      .getStrByID("20143010", "UPP20143010-000090")/*
                                                                     * @res
                                                                     * "当前单据来源是销售发票，业务类型是委托代销,但没有选择对应的销售出库单，请调整"
                                                                     */;
                  return false;
                }
              }
            }
          }
          else {
            // m_sErrorMessage = "当前单据来源是销售发票，但没有业务类型，请调整";
            // return false;
          }
        }
      }
    }

    String[] sInvIds = getUniInvIds(btvos);

    // 获得主表的主键
    String sPK = bhvo.getCbillid();
    // 获得单据号
    String sBillCode = bhvo.getVbillcode();
    // 获得表头成本库存组织
    String sRdcenterid = bhvo.getCrdcenterid();

    Vector vInvID = new Vector(1, 1); // 用于判断计划价调整单的存货不能重复
    Hashtable htPrice = ce.getPricingMode(m_sCorpID, sRdcenterid, sInvIds);// 记录计价方式
    Hashtable htAss = ce.isManageForAssi(m_sCorpID, sInvIds);// 记录是否是辅计量管理
    Hashtable htFree = ce.isManageForFree(m_sCorpID, sInvIds);// 记录是否是自由项管理
    sInvIds = null;

    for (int i = 0; i < btvos.length; i++) {
      // 是删除行不用检查合法性
      if (m_iStatus == UPDATE_STATUS
          && btvos[i].getStatus() == BillVOStatus.DELETED) {
        continue;
      }

      // 设置数据精度
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
        // 设置分录ID
        btvos[i].setCbill_bid(null);
        // 设置行号
        btvos[i].setIrownumber(new Integer(i + 1));
        // 将审核人置为空
        btvos[i].setCauditorid(null);
        btvos[i].setDauditdate(null);
        btvos[i].setIauditsequence(null);
        // 是否调整了分录
        btvos[i].setBadjustedItemflag(new UFBoolean("N"));
        // 是否赠品
        if (btvos[i].getBlargessflag() == null)
          btvos[i].setBlargessflag(new UFBoolean("N"));
        // 退货标志
        if (btvos[i].getBretractflag() == null)
          btvos[i].setBretractflag(new UFBoolean("N"));
        // 是否生成了实时凭证
        btvos[i].setBrtvouchflag(new UFBoolean("N"));
        // 处理业务日期
        if (btvos[i].getDbizdate() == null)
          btvos[i].setDbizdate(dBillDate);
      }
      else if (m_iStatus == UPDATE_STATUS
          && btvos[i].getStatus() == BillVOStatus.UPDATED) {
        // 是修改的
        String sID = btvos[i].getCbill_bid();
        // 调拨类型
        bhvo.setFallocflag(((BillHeaderVO) m_voCurBill.getParentVO())
            .getFallocflag());
        // ts
        bhvo.setTs(((BillHeaderVO) m_voCurBill.getParentVO()).getTs());
        for (int j = 0; j < m_voCurBill.getChildrenVO().length; j++) {
          if (m_voCurBill.getChildrenVO()[j].getPrimaryKey().equals(sID)) {
            // 设置来源单据信息
            btvos[i].setCsourcebilltypecode(((BillItemVO) m_voCurBill
                .getChildrenVO()[j]).getCsourcebilltypecode());
            btvos[i].setVsourcebillcode(((BillItemVO) m_voCurBill
                .getChildrenVO()[j]).getVsourcebillcode());
            btvos[i]
                .setCsaleadviceid(((BillItemVO) m_voCurBill.getChildrenVO()[j])
                    .getCsaleadviceid());
            btvos[i].setCcsaleadviceitemid(((BillItemVO) m_voCurBill
                .getChildrenVO()[j]).getCcsaleadviceitemid());
            // 设置源头单据信息
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
            // 设置是否调整分录和是否赠品
            // 是否调整了分录
            btvos[i].setBadjustedItemflag(((BillItemVO) m_voCurBill
                .getChildrenVO()[j]).getBadjustedItemflag());
            // 是否赠品
            if (btvos[i].getBlargessflag() == null)
              btvos[i].setBlargessflag(((BillItemVO) m_voCurBill
                  .getChildrenVO()[j]).getBlargessflag());
            // 退货标志
            if (btvos[i].getBretractflag() == null)
              btvos[i].setBretractflag(((BillItemVO) m_voCurBill
                  .getChildrenVO()[j]).getBretractflag());
            // 是否生成了实时凭证
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
        // 是增加的
        // 设置分录ID
        btvos[i].setCbill_bid(null);
        // 将审核人置为空
        btvos[i].setCauditorid(null);
        btvos[i].setDauditdate(null);
        btvos[i].setIauditsequence(null);
        // 是否调整了分录
        btvos[i].setBadjustedItemflag(new UFBoolean("N"));
        // 是否赠品
        if (btvos[i].getBlargessflag() == null)
          btvos[i].setBlargessflag(new UFBoolean("N"));
        // 退货标志
        if (btvos[i].getBretractflag() == null)
          btvos[i].setBretractflag(new UFBoolean("N"));
        // 是否生成了实时凭证
        btvos[i].setBrtvouchflag(new UFBoolean("N"));
        // 处理业务日期
        if (btvos[i].getDbizdate() == null)
          btvos[i].setDbizdate(dBillDate);
      }

      UFDate dBizdate = btvos[i].getDbizdate();
      if ((m_sBillType.equals(ConstVO.m_sBillQCRKD) || m_sBillType
          .equals(ConstVO.m_sBillQCXSCBJZD))
          && dBizdate != null) {
        // 期初单据
        if (dBizdate.toString().compareTo(dBeginDate.toString()) >= 0) {
          // 业务日期大于启用日期
          // m_sErrorMessage = "期初单据业务日期" + dBizdate + "应早于系统启用日期" + dBeginDate
          // + "，请调整";
          String[] value = new String[] {
              dBizdate.toString(), dBeginDate.toString()
          };
          m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000147", null, value);
          return false;
        }
      }

      // 设置单位编码、暂估标志
      btvos[i].setPk_corp(m_sCorpID);
      btvos[i].setCbillid(sPK);
      btvos[i].setCbilltypecode(m_sBillType);
      btvos[i].setVbillcode(sBillCode);

      // 设置来源单据类型
      BillItem bt = getBillCardPanel().getHeadItem("cbillsource");
      if (bt != null
          && (m_sBillType.equals(ConstVO.m_sBillXSCBJZD) || m_sBillType
              .equals(ConstVO.m_sBillQCXSCBJZD))) {
        int iIndex = ((UIComboBox) bt.getComponent()).getSelectedIndex();
        String sSourceBillType = null;
        if (iIndex != -1 && iIndex == m_ComboItemsVO.type_salebill) {// 销售发票
          sSourceBillType = ConstVO.m_sBillXSFP;
        }
        else if (iIndex != -1 && iIndex == m_ComboItemsVO.type_saleoutlist) {// 销售出库单
          sSourceBillType = ConstVO.m_sBillXSCKD;
        }
        else if (iIndex != -1 && iIndex == m_ComboItemsVO.type_waylossbill) {// 途损单
          sSourceBillType = ConstVO.m_sBillKCTSD;
        }
        //else if (m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
        //  sSourceBillType = ConstVO.m_sBillXSCKD;
        //}
        btvos[i].setCsourcebilltypecode(sSourceBillType);
      }

      // 得到行号,存货id
      Integer iRow = btvos[i].getIrownumber();
      String sInvID = btvos[i].getCinventoryid();

      if ((m_sBillType.equals(ConstVO.m_sBillQCRKD) == false && m_sBillType
          .equals(ConstVO.m_sBillQCXSCBJZD) == false)
          || sSourceModule == null || sSourceModule.trim().length() == 0) {
        // 1.不是期初单据；2.是期初单据,但是存货核算自己生成的单据
        // 是否自由项管理
        // zlq 20050414 效率优化
        if (!m_sBillType.equals(ConstVO.m_sBillJHJTZD)) {// 20050426 计划价调整单不作限制
          UFBoolean bIsManageForFree = (UFBoolean) htFree.get(sInvID);
          if (bIsManageForFree != null && bIsManageForFree.booleanValue()) {
            // 是自由项管理，是否输入了自由项
            String sFree0 = btvos[i].getVfree0();
            if (sFree0 == null || sFree0.trim().length() == 0) {
              // m_sErrorMessage = "第" + iRow + "行的存货是自由项管理，但没有输入自由项信息，请调整";
              String[] value = new String[] {
                String.valueOf(iRow)
              };
              m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                  "20143010", "UPP20143010-000148", null, value);
              return false;
            }
          }
        }

        // 是否辅计量管理
        UFBoolean bIsManageForAssi = (UFBoolean) htAss.get(sInvID);
        if (bIsManageForAssi == null) {
          Log.info("未能取到是否辅计量管理信息，存货id：" + sInvID);
          return false;
        }
        if (bIsManageForAssi.booleanValue() && m_bIsAdjustBill == false) {
          // 是辅计量管理，不是调整单，是否输入了辅计量数量
          UFDouble dAssiNumber = btvos[i].getNassistnum();
          if (dAssiNumber == null) {
            // m_sErrorMessage = "第" + iRow + "行的存货是辅计量管理，但没有输入辅计量数量，请调整";
            String[] value = new String[] {
              String.valueOf(iRow)
            };
            m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "20143010", "UPP20143010-000149", null, value);
            return false;
          }
        }
      }

      // 获得计价方式编码
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
        // m_sErrorMessage = "第" + iRow + "行的存货在当前库存组织没有定义计价方式，或未被分配到此库存组织，请调整";
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
        // m_sErrorMessage = "第" + iRow + "行的存货在当前库存组织没有定义计价方式，或未被分配到此库存组织，请调整";
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
          // m_sErrorMessage = "第" + iRow + "行的存货在当前库存组织定义为按批次核算，但没有输入批次号，请调整";
          String[] value = new String[] {
            String.valueOf(iRow)
          };
          m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000151", null, value);
          return false;
        }
        // 出入库调整单不是批次核算允许录入批次号 20050526
        // else if ((m_sBillType.equals(ConstVO.m_sBillRKTZD) || m_sBillType
        // .equals(ConstVO.m_sBillCKTZD))
        // && bAuditBatch.booleanValue() == false
        // && sBatch != null && sBatch.trim().length() != 0) {
        // //是入库调整单或出库调整单，不是按批次核算，录入了批次号
        // //m_sErrorMessage = "第" + iRow + "行的存货在当前库存组织定义为不是按批次核算，不能输入批次号，请调整";
        // String[] value = new String[]{String.valueOf(iRow)};
        // m_sErrorMessage =
        // nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        // "UPP20143010-000152", null, value);
        // return false;
        // }
      }
      else {
        // m_sErrorMessage = "第" + iRow + "行的存货在当前库存组织没有定义是否按批次核算，请调整";
        String[] value = new String[] {
          String.valueOf(iRow)
        };
        m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000153", null, value);
        return false;
      }

      // 如果是委外加工发料单，是全月平均计价，不能保存
      if (iPriceCode.intValue() == ConstVO.QYPJ
          && m_sBillType.equals(ConstVO.m_sBillWWJGFLD)) {
        // m_sErrorMessage = "当前库存组织、第" + iRow + "行的存货是全月平均计价方式，委外加工发料无法支持，请调整";
        String[] value = new String[] {
          String.valueOf(iRow)
        };
        m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000175", null, value);
        return false;
      }

      // 如果是计划价调整单，计价方式不是计划价或没有计划价，不能保存
      if( m_bIsPlanedPriceBill ){
        if (iPriceCode.intValue() != ConstVO.JHJ ) {
          // m_sErrorMessage = "第" + iRow + "行的存货在当前库存组织的计价方式不是计划价计价，请调整";
          String[] value = new String[] {
            String.valueOf(iRow)
          };
          m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000154", null, value);
          return false;
        }else {
          //调整前计划价
          UFDouble dPlanedPrice = btvos[i].getNplanedprice();
          //调整后计划价
          UFDouble dNewPlanedPrice = btvos[i].getNprice();
          if (dPlanedPrice == null
              || dPlanedPrice.toString().trim().length() == 0
              || dNewPlanedPrice == null
              || dNewPlanedPrice.toString().trim().length() == 0) {
            // m_sErrorMessage = "第" + iRow + "行的存货在当前库存组织是计划价计价，但没有定义计划价，请调整";
            String[] value = new String[] {
              String.valueOf(iRow)
            };
            m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "20143010", "UPP20143010-000155", null, value);
            return false;
          }
        }
      }
      // 如果是入库调整单且输入了调整单据类型，必须输入调整单据分录
      if (m_bIsInAdjustBill && bIsInputBillType) {
        String sBillItemID = btvos[i].getCadjustbillitemid();
        if (sBillItemID == null || sBillItemID.trim().length() == 0) {
          m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000098")/*
                                                 * @res
                                                 * "输入被调整单据后必须输入被调整单据分录，请调整"
                                                 */;
          return false;
        }
      }

      // 计划价调整单不能同时有两行分录调整同一存货的计划价
      if (m_bIsPlanedPriceBill) {
        if (vInvID.contains(sInvID) == false) {
          vInvID.addElement(sInvID);
        }
        else {
          m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000099")/*
                                                 * @res
                                                 * "一张计划价调整单不能同时有两行分录调整同一存货的计划价，请调整"
                                                 */;
          return false;
        }
      }

      // 如果是销售成本结转单，来源为发票，必须有对应销售出库单ID
      if (m_sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
        String sSourceBilltype = btvos[i].getCsourcebilltypecode();
        if (sSourceBilltype != null
            && sSourceBilltype.equals(ConstVO.m_sBillXSFP)) {
          // 来源是销售发票
          String sSaleAdviceID = btvos[i].getCsaleadviceid();
          // 判断业务类型
          boolean bIsFQ = false; // 是否是分期收款
          boolean bIsWT = false; // 是否是委托代销
          BillItem bt2 = getBillCardPanel().getHeadItem("cbiztypeid");
          if (bt2 != null) {
            String sBizTypeID = bt2.getValue();
            if (sBizTypeID != null && sBizTypeID.trim().length() != 0) {
              UIRefPane pane = (UIRefPane) bt2.getComponent();
              Object oRule = pane.getRef().getRefModel().getValue("verifyrule");
              if (oRule != null) {
                if (oRule.toString().equals("W")) {
                  // 是委托代销
                  bIsWT = true;
                }
                else if (oRule.toString().equals("F")) {
                  // 是分期收款
                  bIsFQ = true;
                }
              }
              if (bIsFQ) {
                if (sSaleAdviceID == null || sSaleAdviceID.trim().length() == 0) {
                  // 没有录入对应销售出库单
                  m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance()
                      .getStrByID("20143010", "UPP20143010-000089")/*
                                                                     * @res
                                                                     * "当前单据来源是销售发票，业务类型是分期收款,但没有选择对应的销售出库单，请调整"
                                                                     */;
                  return false;
                }
              }
              else if (bIsWT) {
                if (sSaleAdviceID == null || sSaleAdviceID.trim().length() == 0) {
                  // 没有录入对应销售出库单
                  m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance()
                      .getStrByID("20143010", "UPP20143010-000090")/*
                                                                     * @res
                                                                     * "当前单据来源是销售发票，业务类型是委托代销,但没有选择对应的销售出库单，请调整"
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

      // 如果是假退料，数量必须为负数
      UFDouble ufdNumber = btvos[i].getNnumber();
      double dNumber = 0;
      if (ufdNumber != null) {
        dNumber = ufdNumber.doubleValue();
      }
      if (bIsWithdrawalflag && dNumber > 0) {
        // m_sErrorMessage = "当前单据是假退料单据，出库数量必须小于0，第" + (i + 1) +
        // "行的存货不符合条件，请调整";
        String[] value = new String[] {
          String.valueOf(i + 1)
        };
        m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000156", null, value);
        return false;
      }

      // 换算率不能为负
      UFDouble ufdAstRate = btvos[i].getNchangerate();
      double dAstRate = 0;
      if (ufdAstRate != null) {
        dAstRate = ufdAstRate.doubleValue();
      }
      else if (btvos[i].getCastunitid() != null) {
        // m_sErrorMessage = "第" + (i + 1) + "行存货分录录入了辅计量单位，但没有录入换算率,不能保存，请调整";
        String[] value = new String[] {
          String.valueOf(i + 1)
        };
        m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000157", null, value);
        return false;
      }
      if (dAstRate < 0) {
        // m_sErrorMessage = "第" + (i + 1) + "行存货分录录入的换算率为负数,不能保存，请调整";
        String[] value = new String[] {
          String.valueOf(i + 1)
        };
        m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000158", null, value);
        return false;
      }

      // 单据合法性检查
      // btvos[i].validate();
      // 由于validate方法名与父类smartVO中throws子句冲突，所以改名verify
      btvos[i].verify();
    }
    if ((btvos == null || btvos.length == 0) && bIsWithdrawalflag) {
      // 是假退料，没有表体，修改的
      for (int i = 0; i < m_voCurBill.getChildrenVO().length; i++) {
        // 如果是假退料，数量必须为负数
        UFDouble ufdNumber = ((BillItemVO) m_voCurBill.getChildrenVO()[i])
            .getNnumber();
        double dNumber = 0;
        if (ufdNumber != null) {
          dNumber = ufdNumber.doubleValue();
        }
        if (dNumber > 0) {
          // m_sErrorMessage = "当前单据是假退料单据，出库数量必须小于0，第" + (i + 1) +
          // "行的存货不符合条件，请调整";
          String[] value = new String[] {
            String.valueOf(i + 1)
          };
          m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000156", null, value);
          return false;
        }
      }
    }

    // 检查必输项
    for (int i = 0; i < m_bd.getHeadShowItems().length; i++) {
      BillItem bt = m_bd.getHeadShowItems()[i];
      if (bt.isNull() && bt.isEdit()) {
        String sAttrName = bt.getKey();
        String sColumnName = bt.getCaptionLabel().getText();
        if (bhvo.getAttributeValue(sAttrName) == null) {
          // m_sErrorMessage = sColumnName + "是必输项，但没有输入数据，请调整";
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
        // 是必输
        String sAttrName = bt.getKey();
        String sColumnName = bt.getCaptionLabel().getText();
        if (bhvo.getAttributeValue(sAttrName) == null) {
          // m_sErrorMessage = sColumnName + "是必输项，但没有输入数据，请调整";
          String[] value = new String[] {
            sColumnName
          };
          m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000159", null, value);
          return false;
        }
      }
    }
    // 检查表体必输项
    for (int i = 0; i < m_bd.getBodyShowItems().length; i++) {
      BillItem bt = m_bd.getBodyShowItems()[i];
      if (bt.isNull() && bt.isEdit()) {
        // 获得列
        String sAttrName = bt.getKey();
        String sColumnName = bt.getName();
        for (int j = 0; j < btvos.length; j++) {
          Integer iRow = btvos[j].getIrownumber();
          if (btvos[j].getAttributeValue(sAttrName) == null) {
            // m_sErrorMessage = sColumnName + "是必输项，但第" + iRow + "行没有输入数据，请调整";
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
   * 返回 BillListPanel 特性值。
   * 
   * @return nc.ui.ia.bill.IABillListPanel
   */
  /* 警告：此方法将重新生成。 */
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
   * 本方法处理热键，以支持全键盘操作。
   */
  protected void hotKeyPressed(javax.swing.KeyStroke hotKey) throws Exception {
    Log.info("按键" + hotKey.getKeyChar());
  }

  /**
   * 函数功能:判断是否可增加 <p/> 参数: <p/> 返回值:是否可增加 <p/> 异常:
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
        // 已月末结帐
        // m_sErrorMessage = "当前会计期间" + sAccountYear + "年" + sAccountMonth +
        // "期间已月末结账，不能再增加单据";
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
            // 登陆日期大于启用期间的截止日期
            // m_sErrorMessage = "当前登录日期" + sLogDate + "晚于启用会计期间截止日期" + sEndDate
            // + "，不能录入期初单据";
            String[] value = new String[] {
                sLogDate, sEndDate
            };
            m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "20143010", "UPP20143010-000162", null, value);
            m_bIsCanAddBill = new UFBoolean(false);
          }
        }
        else if (sLogDate.compareTo(sBeginDate) < 0) {
          // 日常单据，登陆日期小于启用日期
          // m_sErrorMessage = "当前登录日期" + sLogDate + "早于启用日期" + sBeginDate +
          // "，不能录入单据";
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
   * 函数功能:载入单据模板 <p/> 参数: String sTitle ----- 标题 String sBillTypeCode ----- 单据类型
   * <p/> 返回值: <p/> 异常:
   */
  protected void loadCardTemplet(String sTitle, String sBillTypeCode) {
    try {
      m_htInvAndFix = new Hashtable();
      m_refpaneInvBack = new UIRefPane();
      m_refpaneInvBack.setRefNodeName("存货档案");/*-=notranslate=-*/
      // 设置值
      m_sTitle = sTitle;
      m_sBillType = sBillTypeCode;
      // 获得模板
      BillTempletVO btvo = null;
      // 期初入库单变为其它入库单
      // 期初销售成本结转单变为销售成本结转单
      if (sBillTypeCode.equals(ConstVO.m_sBillQCRKD)) {
        btvo = getBillCardPanel().getDefaultTemplet(ConstVO.m_sBillQTRKD, null,
            ce.getUser().getPrimaryKey(), m_sCorpID);
      }
      else if (sBillTypeCode.equals(ConstVO.m_sBillQCXSCBJZD)) {
        btvo = getBillCardPanel().getDefaultTemplet(ConstVO.m_sBillXSCBJZD,
            null, ce.getUser().getPrimaryKey(), m_sCorpID);
      }
      else {
        // 获得模板
        btvo = getBillCardPanel().getDefaultTemplet(sBillTypeCode, null,
            ce.getUser().getPrimaryKey(), m_sCorpID);
      }
      if (btvo == null) {
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000100")/* @res "没有获得单据表单模板数据" */);
        return;
      }
      m_bd = new BillData(btvo);
      // 与ConstVO中定义的出入库类型比较判断是出库类型还是入库类型
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
        // 是入库调整单
        m_bIsInAdjustBill = true;
      }
      else {
        m_bIsInAdjustBill = false;
      }
      if (m_sBillType.equals(ConstVO.m_sBillJHJTZD)) {
        // 是计划价调整单,增加按钮有两个选择
        m_bIsPlanedPriceBill = true;
      }
      else {
        m_bIsPlanedPriceBill = false;
      }
      if (m_sBillType.equals(ConstVO.m_sBillRKTZD)
          || m_sBillType.equals(ConstVO.m_sBillCKTZD)
          || m_sBillType.equals(ConstVO.m_sBillJHJTZD)) {
        // 是调整单
        m_bIsAdjustBill = true;
      }
      else {
        m_bIsAdjustBill = false;
      }
      //非出库单个别指定按钮隐藏
      if(!m_bIsOutBill){
        buttonTree.getButton(IABtnConst.BTN_AUDIT).setVisible(false);
      }
      // 开始设置表头各元素参照
      // 设置库存组织参照
      // "crdcenterid" == 库存组织
      BillItem bt = m_bd.getHeadItem("crdcenterid");
      if (bt != null) {
        UIRefPane pane = (UIRefPane) bt.getComponent();
        String sWhere = pane.getRefModel().getWherePart();
        pane.setWhereString(sWhere + " and property in ("
            + ConstVO.ICalbodyType_All + "," + ConstVO.ICalbodyType_COST + ")");
        // 增加监听
        pane.addValueChangedListener(this);
        pane.setButtonFireEvent(true);
      }
      bt = m_bd.getHeadItem("cstockrdcenterid");
      if (bt != null) {
        UIRefPane pane = (UIRefPane) bt.getComponent();
        String sWhere = pane.getRefModel().getWherePart();
        pane.setWhereString(sWhere + " and property in ("
            + ConstVO.ICalbodyType_All + "," + ConstVO.ICalbodyType_STOR + ")");
        // 增加监听
        pane.addValueChangedListener(this);
        pane.setButtonFireEvent(true);
      }

      // 设置单据类型参照不可见
      // "cbilltypecode" == 单据类型编码
      bt = m_bd.getHeadItem("cbilltypecode");
      if (bt != null) {
        bt.setShow(false);
      }
      // 设置收发类别参照的过滤条件//"cdispatchid" == 入库类别
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
      // 设置客商参照，焦点丢失后显示简称//"ccustomvendorid" == 供应商
      bt = m_bd.getHeadItem("ccustomvendorid");
      if (bt != null) {
        UIRefPane pane = (UIRefPane) bt.getComponent();
        pane.getRef().getRefModel()
            .setRefNameField("bd_cubasdoc.custshortname");
      }

      // 设置业务类型参照的过滤条件//"cbiztypeid" == 业务类型
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
          // 不是期初销售成本结转单或销售成本结转单
          String sAnd = "";
          if (m_bIsAdjustBill == false) {
            // 不是调整单，业务类型不能是分期收款、委托代销
            // 调整单业务类型可以随便选
            sAnd = sAnd + " and verifyrule != 'F' ";
            sAnd = sAnd + " and verifyrule != 'W' ";
          }
          if (m_sBillType.equals(ConstVO.m_sBillCGRKD)) {
            // 是采购入库单
            sAnd = sAnd + " and busiprop in (0,2) ";
          }
          sWhere = sWhere + sAnd;
        }
        else if (m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
          // 是期初销售成本结转单
          String sAnd = " and verifyrule in ('F','W') ";
          sWhere = sWhere + sAnd;
        }
        else if (m_sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
          // 是销售成本结转单
          String sAnd = " and busiprop in (1,2) ";
          sWhere = sWhere + sAnd;
        }
        refmodel.addWherePart(sWhere);
      }
      // 获得缺省的仓库参照的条件//"cwarehouseid" == 仓库
      bt = m_bd.getHeadItem("cwarehouseid");
      if (bt != null) {
        // 获得缺省的仓库参照的条件
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
      // 获得缺省的业务员参照的条件
      // "cemployeeid" == 业务员
      bt = m_bd.getHeadItem("cemployeeid");
      if (bt != null) {
        // 获得缺省的业务员参照的条件
        UIRefPane employeeRef = (UIRefPane) bt.getComponent();
        m_sOldUserCondition = employeeRef.getRef().getRefModel().getWherePart();
      }
      // 将制单人ID转为名称
      bt = m_bd.getTailItem("coperatorid");
      BillItem bt2 = m_bd.getTailItem("coperatorname");
      if (bt != null && bt2 != null) {
        String[] sFor = new String[] {
          "coperatorname->getColValue(sm_user,user_name,cuserid,coperatorid)"
        };
        bt.setLoadFormula(sFor);
      }

      // 对方公司
      bt = m_bd.getHeadItem("cothercorpid");
      if (bt != null) {
        // 不控制数据权限
        UIRefPane uiRef = (UIRefPane) bt.getComponent();

        uiRef.getRefModel().setUseDataPower(false);
        bt.setComponent(uiRef);
      }

      // 对方组织
      bt = m_bd.getHeadItem("cothercalbodyid");
      if (bt != null) {
        // 不控制数据权限
        UIRefPane uiRef = (UIRefPane) bt.getComponent();

        uiRef.setWhereString("");
        uiRef.getRefModel().setUseDataPower(false);
        bt.setComponent(uiRef);
      }

      // 调出公司
      bt = m_bd.getHeadItem("coutcorpid");
      if (bt != null) {
        // 不控制数据权限
        UIRefPane uiRef = (UIRefPane) bt.getComponent();

        uiRef.getRefModel().setUseDataPower(false);
        bt.setComponent(uiRef);
      }

      // 调出组织
      bt = m_bd.getHeadItem("coutcalbodyid");
      if (bt != null) {
        // 不控制数据权限
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

      // 开始设置表体字段的参照
      // 入库调整单调整单据类型是参照
      if (m_bIsInAdjustBill) {
        // "cadjustbilltype" == 被调整单据类型
        bt = m_bd.getBodyItem("cadjustbilltype");
        if (bt != null) {
          // 设置被调整单据选择的参照.
          getUIRefPaneBillType().setVisible(true);
          bt.setComponent(getUIRefPaneBillType());
          bt.setDataType(IBillItem.UFREF);
        }
        // 入库调整单调整单据号是参照
        // "cadjustbill" == 被调整单据号
        bt = m_bd.getBodyItem("cadjustbill");
        if (bt != null) {
          // 设置被调整单据选择的参照.
          getUIRefPaneAdjustBill().setVisible(true);
          getUIRefPaneAdjustBill().setMaxLength(30);
          bt.setComponent(getUIRefPaneAdjustBill());
          bt.setDataType(IBillItem.UFREF);
          getUIRefPaneAdjustBill().addValueChangedListener(this);
          getUIRefPaneAdjustBill().setButtonFireEvent(true);
        }
      }
      // "cadjustbillitem" == 被调整单据分录
      bt = m_bd.getBodyItem("cadjustbillitem");
      if (bt != null) {
        // 设置被调整单据分录选择的参照.
        // 一般单据是回冲单据分录
        // 设置主键列
        bt.setIDColName("cadjustbillitemid");
        getUIRefPaneAdjustBillItem().setVisible(true);
        getUIRefPaneAdjustBillItem().setMaxLength(30);
        bt.setComponent(getUIRefPaneAdjustBillItem());
        bt.setDataType(IBillItem.UFREF);
        getUIRefPaneAdjustBillItem().addValueChangedListener(this);
        getUIRefPaneAdjustBillItem().setButtonFireEvent(true);
      }
      // 设置批次号的参照
      // 如果是出库单且不是期初销售成本结转单、存货是批次号管理，批次号为参照
      if (((m_bIsOtherBill || m_bIsOutBill)
          || m_sBillType.equals(ConstVO.m_sBillCKTZD) || m_sBillType
          .equals(ConstVO.m_sBillRKTZD))
          && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD) == false) {
        bt = m_bd.getBodyItem("vbatch");
        if (bt != null) {
          getUIRefPaneBatch().setVisible(true);
          bt.setComponent(getUIRefPaneBatch());
          bt.setDataType(IBillItem.UFREF);
          // 批次号包括结存为0的
          getUIRefPaneBatch().setHasZero(true);
          if (m_sBillType.equals(ConstVO.m_sBillRKTZD)) {
            // 入库调整单批次号也可以手工输入
            getUIRefPaneBatch().setAutoCheck(false);
            // //入库调整单批次号包括结存为0的
            // getUIRefPaneBatch().setHasZero(true);
          }
          else if (m_sBillType.equals(ConstVO.m_sBillCKTZD)) {
            // //出库调整单批次号包括结存为0的
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
          // 入库单批次号也可以手工输入
          getUIRefPaneBatch().setAutoCheck(false);
          // 入库单批次号包括结存为0的
          getUIRefPaneBatch().setHasZero(true);
        }
      }
      // 自由项参照
      bt = m_bd.getBodyItem("vfree0");
      if (bt != null) {
        getUIRefPaneFreeItem().setVisible(true);
        bt.setComponent(getUIRefPaneFreeItem());
        bt.setDataType(IBillItem.UFREF);
        getUIRefPaneFreeItem().setMaxLength(500);
        // 增加监听
        getUIRefPaneFreeItem().addValueChangedListener(this);
        getUIRefPaneFreeItem().setButtonFireEvent(true);
      }
      // 设置存货参照，显示计量单位//"cinventorycode" == 存货编码
      bt = m_bd.getBodyItem("cinventorycode");
      if (bt != null) {
        // 获得缺省的存货参照的条件
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
          vTemp.addElement("计量单位");/*-=notranslate=-*/
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
        // 增加监听
        m_refpaneInv.addValueChangedListener(this);
        m_refpaneInv.setButtonFireEvent(true);
        // 设置多选
        m_refpaneInv.setMultiSelectedEnabled(true);
        m_refpaneInv.setTreeGridNodeMultiSelected(true);
      }
      // 存货标识
      bt = m_bd.getBodyItem("cinventoryid");
      if (bt != null) {
        // 设置公式
        loadInvFormula(bt);
      }
      // 设置成本对象过滤条件
      bt = m_bd.getBodyItem("vbomcodecode");
      if (bt != null) {
        // 获得缺省的成本对象的条件
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
          // 是委外加工发料单
          m_sOldBomCondition = m_sOldBomCondition
              + " and bd_produce.sfcbdx = 'N' ";
        }
        else if (m_sBillType.equals(ConstVO.m_sBillCLCKD)
            || m_sBillType.equals(ConstVO.m_sBillQTCKD)
            || m_sBillType.equals(ConstVO.m_sBillCKTZD)) {
          // 是材料出库单或其他出库单或出库调整单
          m_sOldBomCondition = m_sOldBomCondition
              + " and bd_produce.sfcbdx = 'Y' ";
        }
        refmodel.setWherePart(m_sOldBomCondition);
      }
      // 设置辅计量的参照
      // "castunitname" == 辅单位
      bt = m_bd.getBodyItem("castunitname");
      if (bt != null) {
        // 获得缺省的计量单位参照的条件
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

      // 设置项目参照
      bt = m_bd.getBodyItem("cprojectcode");
      if (bt != null) {
        // 设置项目参照
        getUIRefPaneJob().setVisible(true);
        bt.setComponent(getUIRefPaneJob());
        bt.setDataType(5);
        String[] sFor = new String[] {
            "cprojectcode->getColValue(bd_jobmngfil,pk_jobbasfil,pk_jobmngfil,cprojectid)",
            "cprojectcode->getColValue(bd_jobbasfil,jobcode,pk_jobbasfil,cprojectcode)"
        };
        bt.setLoadFormula(sFor);
      }
      // 项目名称
      bt = m_bd.getBodyItem("cprojectname");
      if (bt != null) {
        String[] sFor = new String[] {
            "cprojectname->getColValue(bd_jobmngfil,pk_jobbasfil,pk_jobmngfil,cprojectid)",
            "cprojectname->getColValue(bd_jobbasfil,jobname,pk_jobbasfil,cprojectname)"
        };
        bt.setLoadFormula(sFor);
      }
      // 设置项目阶段参照
      bt = m_bd.getBodyItem("cprojectphasecode");
      if (bt != null) {
        // 设置项目阶段参照
        getUIRefPaneJobParse().setVisible(true);
        bt.setComponent(getUIRefPaneJobParse());
        bt.setDataType(5);
        // 设置主键列
        bt.setIDColName("cprojectphase");
        String[] sFors = new String[3];
        sFors[0] = "cprojectphasename->getColValue(bd_jobobjpha,pk_jobphase,pk_jobobjpha,cprojectphase)";
        sFors[1] = "cprojectphasecode->getColValue(bd_jobphase,jobphasecode,pk_jobphase,cprojectphasename)";
        sFors[2] = "cprojectphasename->getColValue(bd_jobphase,jobphasename,pk_jobphase,cprojectphasename)";
        bt.setLoadFormula(sFors);
      }
      // 设置固定资产参照//20050525 关闭固定资产和存货接口
      // bt = m_bd.getBodyItem("cfadevicecode");
      // if (bt != null) {
      // 设置固定资产参照
      // if ((m_bIsOtherBill || m_bIsOutBill)) {
      // //出库单
      // getUIRefPaneFacard().setVisible(true);
      // bt.setComponent(getUIRefPaneFacard());
      // } else {
      // getUIRefPaneFacardEquipment().setVisible(true);
      // bt.setComponent(getUIRefPaneFacardEquipment());
      // }
      // bt.setDataType(5);
      // }
      // 将工作中心ID转为名称
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
      // 将审核人ID转为名称
      bt = m_bd.getBodyItem("cauditorid");
      bt2 = m_bd.getBodyItem("cauditorname");
      if (bt != null && bt2 != null) {
        String[] sFor = new String[] {
          "cauditorname->getColValue(sm_user,user_name,cuserid,cauditorid)"
        };
        bt.setLoadFormula(sFor);
      }
      // 设置工作中心参照
      bt = m_bd.getBodyItem("cwpcode");
      if (bt != null) {
        // 设置工作中心参照
        getUIRefPaneWkCenter().setVisible(true);
        bt.setComponent(getUIRefPaneWkCenter());
        AbstractRefModel refmodel = (AbstractRefModel) getUIRefPaneWkCenter()
            .getRef().getRefModel();
        m_sOldWkCondition = refmodel.getWherePart();
        bt.setDataType(5);
      }

      // 是期初入库单或期初销售成本结转单
      if (sBillTypeCode.equals(ConstVO.m_sBillQCRKD)
          || sBillTypeCode.equals(ConstVO.m_sBillQCXSCBJZD)) {
        // 是期初单据，加入导入按钮
        if (m_bIsAddImportButton == false) {
          // 是期初单据，加入导入按钮
          //buttonTree.getButton(IABtnConst.BTN_ASSIST_QUERY).addChildButton(buttonTree.getButton(IABtnConst.BTN_IMPORT_BILL));
          m_bIsAddImportButton = true;
        }
        // 使界面的回冲单据分录不可见
        bt = m_bd.getBodyItem("cadjustbillitem");
        bt.setShow(false);
        bt.setEdit(false);
        bt.setEnabled(false);
        // 表单时的按钮
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
//        // 列表时的按钮
//        m_aryButtonGroupList = new ButtonObject[7];
//        m_aryButtonGroupList[0] = buttonTree.getButton(IABtnConst.BTN_QUERY);
//        m_aryButtonGroupList[1] = buttonTree.getButton(IABtnConst.BTN_BILL_EDIT);
//        m_aryButtonGroupList[2] = buttonTree.getButton(IABtnConst.BTN_BILL_DELETE);
//        m_aryButtonGroupList[3] = buttonTree.getButton(IABtnConst.BTN_BROWSE_LOCATE);
//        m_aryButtonGroupList[4] = buttonTree.getButton(IABtnConst.BTN_PRINT);
//        m_aryButtonGroupList[5] = buttonTree.getButton(IABtnConst.BTN_BROWSE_REFRESH);
//        m_aryButtonGroupList[6] = buttonTree.getButton(IABtnConst.BTN_ASSIST_QUERY);
      }
      // 处理是否假退料
      bt = m_bd.getHeadItem("bwithdrawalflag");
      if (bt != null && bt.getComponent() instanceof UIComboBox) {
        UIComboBox uicombobox = (UIComboBox) bt.getComponent();
        uicombobox.addItem(m_ComboItemsVO.name_yes);// 是
        uicombobox.addItem(m_ComboItemsVO.name_no);// 否
        if (m_bIsICStart) {
          // 如果库存启用，默认是假退料的
          uicombobox.setSelectedIndex(m_ComboItemsVO.type_yes);
        }
        else {
          // 如果库存未启用，默认不是假退料的
          uicombobox.setSelectedIndex(m_ComboItemsVO.type_no);
        }
      }
      // 采购入库单将暂估标志设为下拉框
      bt = m_bd.getHeadItem("bestimateflag");
      if (bt != null && bt.getComponent() instanceof UIComboBox) {
        UIComboBox uicombobox = (UIComboBox) bt.getComponent();
        uicombobox.addItem(m_ComboItemsVO.name_estimated_yes);// 暂估
        uicombobox.addItem(m_ComboItemsVO.name_estimated_no);// 非暂估
        uicombobox.setSelectedIndex(m_ComboItemsVO.type_estimated_no);
      }
      // 将是否赠品设为下拉框
      bt = m_bd.getBodyItem("blargessflag");
      if (bt != null && bt.getComponent() instanceof UIComboBox) {
        UIComboBox uicombobox = (UIComboBox) bt.getComponent();
        uicombobox.addItem(m_ComboItemsVO.name_yes);// 是
        uicombobox.addItem(m_ComboItemsVO.name_no);// 否
        uicombobox.setSelectedIndex(m_ComboItemsVO.type_no);
      }
      // 将调拨类型设为下拉框
      bt = m_bd.getHeadItem("fallocflag");
      if (bt != null && bt.getComponent() instanceof UIComboBox) {
        UIComboBox uicombobox = (UIComboBox) bt.getComponent();
        uicombobox.addItem(m_ComboItemsVO.name_transfer_direct);// 直运调拨
        uicombobox.addItem(m_ComboItemsVO.name_transfer_instore);// 入库调拨
        uicombobox.addItem(m_ComboItemsVO.name_transfer_stock);// 集采调拨
      }
      if (sBillTypeCode.equals(ConstVO.m_sBillXSCBJZD)) {
        // 销售成本结转单将单据来源设为下拉框
        bt = m_bd.getHeadItem("cbillsource");
        if (bt != null && bt.getComponent() instanceof UIComboBox) {
          //ivjUIComboBoxSource = (UIComboBox) bt.getComponent();
          //getUIComboBoxSource().addItem("");// 销售出库单
          getUIComboBoxSource().addItem(m_ComboItemsVO.name_saleoutlist);// 销售出库单
          getUIComboBoxSource().addItem(m_ComboItemsVO.name_salebill);// 销售发票
          getUIComboBoxSource().addItem(m_ComboItemsVO.name_waylossbill);// 库存途损单
          getUIComboBoxSource().setSelectedIndex(-1);
          getUIComboBoxSource().setVisible(true);
          bt.setComponent(getUIComboBoxSource());
          ((UIComboBox) bt.getComponent()).addItemListener(billSourceListener);
        }
        if (m_bIsAddChooseButton == false) {
          // 是销售成本结转单据，加入选择按钮
          //buttonTree.getButton(IABtnConst.BTN_ASSIST_QUERY).addChildButton(buttonTree.getButton(IABtnConst.BTN_CHOOSESALEBILL));
          m_bIsAddChooseButton = true;
          btnCtrl.set(false,IABtnConst.BTN_CHOOSESALEBILL);
        }
      }
      else if (sBillTypeCode.equals(ConstVO.m_sBillQCXSCBJZD)) {
        // 期初销售成本结转单将单据来源设为下拉框
        bt = m_bd.getHeadItem("cbillsource");
        if (bt != null && bt.getComponent() instanceof UIComboBox) {
          UIComboBox uicombobox = (UIComboBox) bt.getComponent();
          // uicombobox.addItem("销售出库单");
          uicombobox.addItem(m_ComboItemsVO.name_saleoutlist);
          bt.setShow(false);
          bt.setEnabled(false);
        }
      }
      // //表头自定义项处理
      // nc.ui.bd.service.BDDef bdef = new nc.ui.bd.service.BDDef();
      // m_voHeaddef = BDDef.queryDefVO(ConstVO.m_sDefHeadName, m_sCorpID);
      m_bd.updateItemByDef(m_voHeaddef, "vdef", true);
      // //表体自定义项处理
      // m_voBodydef = BDDef.queryDefVO(ConstVO.m_sDefBodyName, m_sCorpID);
      m_bd.updateItemByDef(m_voBodydef, "vdef", false);

      bt = m_bd.getBodyItem("btransferincometax");
      if (bt != null) {
        UICheckBox cb = (UICheckBox) bt.getComponent();
        cb.addItemListener(this);
        bt.setComponent(cb);
      }

      java.awt.Color colorMustInput = null;
      // 设置必输项的颜色
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
          // 是必输
          bt.getCaptionLabel().setILabelType(5);
          if (colorMustInput == null) {
            colorMustInput = bt.getCaptionLabel().getForeground();
          }
        }
      }
      // 设置数据精度及最大值，最大为14位
      // 数量
      bt = m_bd.getBodyItem("nnumber");
      if (bt != null) {
        ((UIRefPane) bt.getComponent()).setMaxValue(m_dMaxValue);
        bt.setDecimalDigits(m_iPeci[0]);
      }
      // 单价
      bt = m_bd.getBodyItem("nprice");
      if (bt != null) {
        ((UIRefPane) bt.getComponent()).setMaxValue(m_dMaxValue);
        bt.setDecimalDigits(m_iPeci[1]);
      }
      // 金额
      bt = m_bd.getBodyItem("nmoney");
      if (bt != null) {
        ((UIRefPane) bt.getComponent()).setMaxValue(m_dMaxValue);
        bt.setDecimalDigits(m_iPeci[2]);
      }
      // 计划单价
      bt = m_bd.getBodyItem("nplanedprice");
      if (bt != null) {
        ((UIRefPane) bt.getComponent()).setMaxValue(m_dMaxValue);
        bt.setDecimalDigits(m_iPeci[1]);
      }
      // 调整前计划单价
      bt = m_bd.getBodyItem("noriginalprice");
      if (bt != null) {
        ((UIRefPane) bt.getComponent()).setMaxValue(m_dMaxValue);
        bt.setDecimalDigits(m_iPeci[1]);
      }
      // 计划金额
      bt = m_bd.getBodyItem("nplanedmny");
      if (bt != null) {
        ((UIRefPane) bt.getComponent()).setMaxValue(m_dMaxValue);
        bt.setDecimalDigits(m_iPeci[2]);
      }
      // 辅计量数量
      bt = m_bd.getBodyItem("nassistnum");
      if (bt != null) {
        // 设置数据精度
        ((UIRefPane) bt.getComponent()).setMaxValue(m_dMaxValue);
        bt.setDecimalDigits(m_iPeci[4]);
      }
      // 换算率
      bt = m_bd.getBodyItem("nchangerate");
      if (bt != null) {
        // 设置数据精度
        bt.setDecimalDigits(m_iPeci[5]);
      }
      //合理损耗数量
      bt = m_bd.getBodyItem("nreasonalwastnum");
      if (bt != null) {
        // 设置数据精度
        bt.setDecimalDigits(m_iPeci[0]);
      }
      //合理损耗单价
      bt = m_bd.getBodyItem("nreasonalwastprice");
      if (bt != null) {
        // 设置数据精度
        bt.setDecimalDigits(m_iPeci[1]);
      }
      //合理损耗金额
      bt = m_bd.getBodyItem("nreasonalwastmny");
      if (bt != null) {
        // 设置数据精度
        bt.setDecimalDigits(m_iPeci[2]);
      }
      // 将修改后的模板数据设置回界面
      getBillCardPanel().setBillData(m_bd);
      // 自由项变色龙
      new InvAttrCellRenderer().setFreeItemRenderer(getBillCardPanel());
      getBillCardPanel().addEditListener(this);
      // 设置负数用红字显示
      getBillCardPanel().getBodyPanel().setShowRed(true);
      // 显示合计列
      getBillCardPanel().setTatolRowShow(true);
      // 以下是为了设置表体必输项的颜色
      for (int i = 0; i < m_bd.getBodyShowItems().length; i++) {
        bt = m_bd.getBodyShowItems()[i];
        if (bt.isNull()
            && bt.getForeground() == nc.ui.bill.tools.ColorConstants.COLOR_DEFAULT) {
          // 获得列
          String sKey = bt.getKey();
          int iColumnIndex = getBillCardPanel().getBillModel().getBodyColByKey(
              sKey);
          if (iColumnIndex != -1) {
            // 设置编辑器
            BillRender br = new BillRender(colorMustInput);
            getBillCardPanel().getBillTable().getColumnModel().getColumn(i)
                .setHeaderRenderer(br);
          }
        }
      }
      // 设置不可编辑
      getBillCardPanel().setEnabled(false);
      // 增加表头的编辑触发
      getBillCardPanel().addBillEditListenerHeadTail(this);
      getBillCardPanel().setAutoExecHeadEditFormula(true);

      // 增加列排序
      // getBillCardPanel().getBodyPanel().addTableSortListener();
      // getBillCardPanel().getBillModel().setRowSort(true);
    }
    catch (Exception e) {
      e.printStackTrace();
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000102")/* @res "载入单据表单模板数据出错" */);
    }
  }

  /**
   * 函数功能:载入单据模板 <p/> 参数: String sTitle ----- 标题 String sBillTypeCode ----- 单据类型
   * <p/> 返回值: <p/> 异常:
   */
  protected void loadListTemplet(String sTitle, String sBillTypeCode) {
    m_bdList = getBillListPanel().loadListTemplet(sTitle, sBillTypeCode,
        m_iPeci, m_voHeaddef, m_voBodydef);
    // 增加监听
    getBillListPanel().addEditListener(this);
    getBillListPanel().addMouseListener(this);
    getBillListPanel().addHeadEditListener(new BillHeadListener());
    getBillListPanel().addBodyEditListener(new BillBodyListener());
    getBillListPanel().getHeadTable().setSelectionMode(
        ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    getBillListPanel().getChildListPanel().setTatolRowShow(true);
  }

  /**
   * 函数功能:载入单据模板 <p/> 参数: String sTitle ----- 标题 String sBillTypeCode ----- 单据类型
   * <p/> 返回值: <p/> 异常:
   */
  protected void loadTemplet(String sTitle, String sBillTypeCode) {
    // 生成脚本文件
    try {
      // 载入表单模板
      loadCardTemplet(sTitle, sBillTypeCode);
      // 载入列表模板
      loadListTemplet(sTitle, sBillTypeCode);
      // getBillListPanel().getHeadBillModel().addSortListener(this);
      getBillListPanel().getHeadBillModel().addSortRelaObjectListener2(this);
      // 设置表体选择变化监听，为进项税是否转出服务
      getBillCardPanel().getBillTable().getSelectionModel()
          .addListSelectionListener(this);
      // 设置表单模版界面初始时不可编辑
      getBillCardPanel().setEnabled(false);
      m_iStatus = INIT_STATUS;
      setBtnsForStatus(m_iStatus);// 显示表单;隐藏列表
      // zlq change begin
      // m_bIsChangeEvent = true;//在定义处设置初值
      // zlq change end
      // 获得业务类型
      /*
       * m_sFQSK 将存放一串以逗号分隔的字符串，每个字符串代表一个分期收款类型的业务类型
       * 将来使用时以indexof方法查找某个字符串是否在m_sFQSK中
       */
      // String[] sBizs = new String[2];
      // sBizs[0] = ConstVO.m_sBizFQSK;//分期收款
      // sBizs[1] = ConstVO.m_sBizWTDX;//委托代销
      // java.util.Hashtable ht = CommonDataBO_Client.getBizTypeIDs(m_sCorpID,
      // sBizs);
      // m_sFQSK = (String) ht.get(ConstVO.m_sBizFQSK);
      // m_sWTDX = (String) ht.get(ConstVO.m_sBizWTDX);
    }
    catch (Exception e) {
      e.printStackTrace();
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000103")/* @res "初始化数据出错" */
          + e.getMessage());
    }
  }

  /**
   * 函数功能:点增加按钮触发 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  protected void onButtonAddManualClicked() {
    try {
      // 是否已关账
      if (!accountIsOpen())
        return;
      // 判断是否可增加，已月末结帐不能增加
      if (isCanAddBill() == false) {
        showErrorMessage(m_sErrorMessage);
        return;
      }
      // 清空固定资产信息
      // clearFAData();
      addBill();
      if (m_sBillType.equals(ConstVO.m_sBillRKTZD)) {
        // 是入库调整单，使可调整单据分录不可编辑
        getBillCardPanel().getBodyItem("cadjustbillitemid").setEdit(false);
        getBillCardPanel().getBodyItem("cadjustbillitemid").setEnabled(false);
      }
      if ((m_bIsOtherBill || m_bIsOutBill)
          && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD) == false
          && m_sBillType.equals(ConstVO.m_sBillCKTZD) == false
          && m_sBillType.equals(ConstVO.m_sBillJHJTZD) == false) {
        if (!m_bAllowDefinePriceByUser) {
          // 出库单不允许自定义单价，但期初单据必须输入单价
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
   * 函数功能:点增加按钮触发 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  protected void onButtonAddQueryClicked() {
    try {
      if (!m_bIsPOStart) {
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000104")/*
                                               * @res
                                               * "此功能需要查询采购系统数据，采购系统未启用，不能使用此功能"
                                               */);
        return;
      }
      // 判断是否可增加，已月末结帐不能增加
      if (isCanAddBill() == false) {
        showErrorMessage(m_sErrorMessage);
        return;
      }

      // 清空固定资产信息
      // clearFAData();//20050525 关闭固定资产和存货接口

      // 显示计划价调价选择查询对话框
      getQueryPlannedPriceDlg().showModal();

      if (getQueryPlannedPriceDlg().isCloseOK()) {

        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000052")/* @res "正在查询单据，请稍候" */);

        // 查询
        addQuery();
      }

    }
    catch (Exception e) {
      e.printStackTrace();
      showErrorMessage(e.getMessage());
    }
  }

  /**
   * 函数功能:点单据复制按钮触发 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  protected void onButtonCopyClicked() {
    try {
      // 关账后不能复制
      if (!accountIsOpen())
        return;
      // 判断是否可增加，已月末结帐不能增加
      if (isCanAddBill() == false) {
        showErrorMessage(m_sErrorMessage);
        return;
      }
      m_iStatus = ADD_STATUS;
      // 清空固定资产信息
      // clearFAData();//20050525 关闭固定资产和存货接口
      if (getBillListPanel().isVisible()) {
        getBillCardPanel().setBillValueVO(m_voCurBill);
        dispListToCard();
        getBillCardPanel().setEnabled(true);
      }
      setBtnsForStatus(m_iStatus);
      // 保存当前界面数据
      getBillCardPanel().updateValue();
      // 将单据ID清空
      getBillCardPanel().setHeadItem("cbillid", null);
      // 将单据号清空
      getBillCardPanel().setHeadItem("vbillcode", null);
      // 将打印次数清空
      getBillCardPanel().setTailItem("iprintcount", null);
      // 最后修改人清空
      getBillCardPanel().setTailItem("clastoperatorid", null);
      getBillCardPanel().setTailItem("clastoperatorname", null);
      // 最后修改时间清空
      getBillCardPanel().setTailItem("tlastmaketime", null);
      // 制单时间清空
      getBillCardPanel().setTailItem("tmaketime", null);
      // 出库单是否允许自定义单价
      // String sParam = ce.getParaValue(m_sCorpID, ConstVO.m_iPara_ZDYDJ);
      // 当前的计划价可能已经变了,要重新查询
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
        // 将单据分录ID清空
        getBillCardPanel().getBillModel().setValueAt(null, i, "cbill_bid");
        // 将单据ID清空
        getBillCardPanel().getBillModel().setValueAt(null, i, "cbillid");
        // 单据号清空
        getBillCardPanel().getBillModel().setValueAt(null, i, "vbillcode");
        // 累计发出数量清空
        getBillCardPanel().getBillModel()
            .setValueAt(null, i, "nsettledsendnum");
        // 累计回冲数量清空
        getBillCardPanel().getBillModel().setValueAt(null, i,
            "nsettledretractnum");
        // 累计回冲数量清空
        getBillCardPanel().getBillModel().setValueAt(null, i, "cbill_bid");
        // 审核人清空
        getBillCardPanel().getBillModel().setValueAt(null, i, "cauditorid");
        // 审核序号清空
        getBillCardPanel().getBillModel().setValueAt(null, i, "iauditsequence");
        // 审核人清空
        getBillCardPanel().getBillModel().setValueAt(null, i, "cauditorname");
        // 审核日期清空
        getBillCardPanel().getBillModel().setValueAt(null, i, "dauditdate");
        // 出库单ID清空
        getBillCardPanel().getBillModel().setValueAt(null, i, "csaleadviceid");
        // 出库单分录ID清空
        getBillCardPanel().getBillModel().setValueAt(null, i,
            "ccsaleadviceitemid");
        if (m_bd.getBodyItem("cadjustbillitem") != null) {
          // 被调整分录ID清空
          getBillCardPanel().getBillModel().setValueAt(null, i,
              "cadjustbillitem");
        }
        if (m_bd.getBodyItem("cadjustbillid") != null) {
          // 被调整单ID清空
          getBillCardPanel().getBillModel()
              .setValueAt(null, i, "cadjustbillid");
        }
        if (m_bd.getBodyItem("cadjustbillitemid") != null) {
          // 被调整分录ID清空
          getBillCardPanel().getBillModel().setValueAt(null, i,
              "cadjustbillitemid");
        }
        // 凭证信息清空
        getBillCardPanel().getBillModel().setValueAt(null, i, "cvoucherid");
        // 成本对象
        getBillCardPanel().getBillModel().setValueAt(null, i, "vbomcode");
        if (m_bd.getBodyItem("vbomcodecode") != null) {
          getBillCardPanel().getBillModel().setValueAt(null, i, "vbomcodecode");
          getBillCardPanel().getBillModel().setValueAt(null, i, "vbomcodename");
        }
        // 与固定资产有关的数据清空
        getBillCardPanel().getBillModel().setValueAt(null, i, "cfadeviceid");
        getBillCardPanel().getBillModel().setValueAt(null, i, "cfadevicecode");
        getBillCardPanel().getBillModel().setValueAt(null, i, "cfadevicename");
        getBillCardPanel().getBillModel().setValueAt(null, i, "cfadevicevo");
        getBillCardPanel().getBillModel().setValueAt(null, i, "cfacardid");
        // if (sParam.equals("否") || sParam.equalsIgnoreCase("N"))
        // {/*-=notranslate=-*/
        if (!m_bAllowDefinePriceByUser) {
          // 单价清空
          getBillCardPanel().getBillModel().setValueAt(null, i, "nprice");
          // 金额清空
          getBillCardPanel().getBillModel().setValueAt(null, i, "nmoney");
          // 计算进项税转出金额
          calcTransIncomeTaxMny(i);
        }
        if (((BillHeaderVO) m_voCurBill.getParentVO()).getCbilltypecode()
            .equals(ConstVO.m_sBillJHJTZD)) {
          // 计划价调整单
          if (((BillItemVO) m_voCurBill.getChildrenVO()[i]).getFpricemodeflag()
              .intValue() == ConstVO.JHJ) {
            // 调整前计划价
            getBillCardPanel().getBillModel().setValueAt(oInvJHJ[i][0], i,
                "noriginalprice");
          }
        }
        if (((BillItemVO) m_voCurBill.getChildrenVO()[i]).getFpricemodeflag()
            .intValue() == ConstVO.JHJ) {
          // 计划价
          getBillCardPanel().getBillModel().setValueAt(oInvJHJ[i][0], i,
              "nplanedprice");
        }
        // 入库差异清空
        getBillCardPanel().getBillModel().setValueAt(null, i, "ninvarymny");
        // 出库差异清空
        getBillCardPanel().getBillModel().setValueAt(null, i, "noutvarymny");
      }
      // 获得日期
      String sDate = ce.getBusinessDate().toString();
      if (m_sBillType.equals(ConstVO.m_sBillQCRKD)
          || m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
        sDate = getBillCardPanel().getHeadItem("dbilldate").getValue();
      }
      if (m_sBillType.equals(ConstVO.m_sBillQCRKD) == false
          && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD) == false) {
        getBillCardPanel().setHeadItem("dbilldate", sDate);
      }
      // 获得操作员
      String sOprator = ce.getUser().getUserName();
      getBillCardPanel().setTailItem("coperatorname", sOprator);
      // 设置暂估值
      // BillItem bt = getBillCardPanel().getHeadItem("bestimateflag");
      // if (bt != null && m_sBillType.equals(ConstVO.m_sBillCGRKD))
      // {
      // UIComboBox ui = (UIComboBox)bt.getComponent();
      // ui.setSelectedItem("非暂估");
      // }
      // 设置赠品值
      BillItem bt = getBillCardPanel().getBodyItem("blargessflag");
      if (bt != null) {
        UIComboBox ui = (UIComboBox) bt.getComponent();
        // ui.setSelectedItem("否");
        ui.setSelectedIndex(m_ComboItemsVO.type_no);
      }
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000105")/* @res "复制单据" */);
      getBillCardPanel().setEnabled(true);
      // 可以修改表头数据
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
   * 函数功能:点表单按钮触发 <p/> 参数: <p/> 返回值: <p/> 异常:
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
    // 显示列表数据
    if (m_voCurBill != null && (m_voBills == null || m_voBills.length == 0)) {
      // 显示当前的单据
      BillHeaderVO[] vos = new BillHeaderVO[] {
        (BillHeaderVO) m_voCurBill.getParentVO()
      };
      getBillListPanel().setBodyValueVO(m_voCurBill.getChildrenVO());
      getBillListPanel().setHeaderValueVO(vos);
      // 设置列表中下拉框列的值为相应的文字串
      setComboBoxInHeadFromVO(m_voCurBill, false, 0);

      String sBillcode = ((BillHeaderVO) m_voCurBill.getParentVO())
          .getVbillcode();
      // 设置查询的单据号
      getQueryClientDlg().setDefaultValue("v.vbillcode", "", sBillcode);
      // 显式调用方法,使公式可用
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
//        "UCH021"/* 卡片显示 */));
    setButtons(m_aryButtonGroupList);
    showHintMessage(NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000106")/* @res "显示列表数据" */);
  
  }
  
  private void displayInCard(){
    if (m_voCurBill != null
        && getBillListPanel().getHeadTable().getSelectedRow() == -1) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON",
          "UPPSCMCommon-000199")/* @res "请选择单据" */);
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

      // 设置来源单据类型
      bt = getBillCardPanel().getHeadItem("cbillsource");
      if (bt != null
          && (m_sBillType.equals(ConstVO.m_sBillXSCBJZD) || m_sBillType
              .equals(ConstVO.m_sBillQCXSCBJZD))) {
        BillItemVO btvo = (BillItemVO) m_voCurBill.getChildrenVO()[0];
        String sSourceBillType = btvo.getCsourcebilltypecode();
        UIComboBox uibox = (UIComboBox) bt.getComponent();
        if (sSourceBillType != null
            && sSourceBillType.equals(ConstVO.m_sBillXSFP)) {
          uibox.setSelectedIndex(m_ComboItemsVO.type_salebill);// ConstVO.m_sBillXSFPName);//销售发票
        }
        else if (sSourceBillType != null
            && sSourceBillType.equals(ConstVO.m_sBillXSCKD)) {
          uibox.setSelectedIndex(m_ComboItemsVO.type_saleoutlist);// ConstVO.m_sBillXSCKDName);//销售出库单
        }
        else {
          uibox.setSelectedIndex(-1);// tem("无");
        }
      }
      // 列表到表单的处理
      dispListToCard();
    }
    else {
      getBillCardPanel().getBillData().clearViewData();
    }
    setBtnsForStatus(m_iStatus);
    // 保存当前界面数据
    getBillCardPanel().updateValue();
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000107")/* @res "显示表单数据" */);
  
  }
  /**
   * 函数功能:点修改按钮触发 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  protected void onButtonUpdateClicked() {
    // 判断是否可以修改其他人制的单据
    if (canAlterBillMadeByOthers(m_voCurBill) == false) {
      return;
    }
    if (!accountIsOpen())
      return;
    BillHeaderVO header = (BillHeaderVO ) m_voCurBill.getParentVO();
    BillItemVO[] items = (BillItemVO[]) m_voCurBill.getChildrenVO();
    //是材料出库单并且是假退料单据
    if( header.getCbilltypecode().equals( ConstVO.m_sBillCLCKD) && 
        header.getBwithdrawalflag().booleanValue() ){
      for( int i =0;i< items.length;i++){
        if( items[i].getNnumber().doubleValue() > 0 ){
          String message = nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000294");
          /* @res "本单据是上月假退料单据月结生成的，不能修改" */
          this.showErrorMessage( message );
          this.showHintMessage( message );
          return;
        }
      }
    }
    
    
    if (getBillListPanel().isVisible()) {
      getBillCardPanel().setBillValueVO(m_voCurBill);
      // 设置来源单据类型
      BillItem bt = getBillCardPanel().getHeadItem("cbillsource");
      if (bt != null
          && (m_sBillType.equals(ConstVO.m_sBillXSCBJZD) || m_sBillType
              .equals(ConstVO.m_sBillQCXSCBJZD))) {
        BillItemVO btvo = (BillItemVO) m_voCurBill.getChildrenVO()[0];
        String sSourceBillType = btvo.getCsourcebilltypecode();
        UIComboBox uibox = (UIComboBox) bt.getComponent();
        if (sSourceBillType != null
            && sSourceBillType.equals(ConstVO.m_sBillXSFP)) {
          uibox.setSelectedIndex(m_ComboItemsVO.type_salebill);// uibox.setSelectedItem(ConstVO.m_sBillXSFPName);//销售发票
        }
        else if (sSourceBillType != null
            && sSourceBillType.equals(ConstVO.m_sBillXSCKD)) {
          uibox.setSelectedIndex(m_ComboItemsVO.type_saleoutlist);// ConstVO.m_sBillXSCKDName);//销售出库单
        }
        else {
          uibox.setSelectedIndex(-1);// tem("无");
        }
      }
      // 列表到表单的处理
      dispListToCard();
    }
    m_iStatus = UPDATE_STATUS;
    setBtnsForStatus(m_iStatus);
    // 设置界面可编辑
    getBillCardPanel().setEnabled(true);
    // 保存当前界面数据
    getBillCardPanel().updateValue();
    BillItem[] bts = getBillCardPanel().getBodyItems();
    // 记录编辑状态
    m_bBodyEditFlags = new boolean[bts.length];
    for (int i = 0; i < bts.length; i++) {
      m_bBodyEditFlags[i] = bts[i].isEdit();
    }
    // 是修改状态，未审核的分录可以修改，删行，已审核的分录不可修改、删行
    boolean bIsAudit = ((BillHeaderVO) m_voCurBill.getParentVO())
        .getBauditedflag().booleanValue();
    // 分录有生成实时凭证的不可修改删行
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
      // 已有分录审核或生成实时凭证了
      // 不能增行、删行、插入行、粘贴行
      btnCtrl.set(false,IABtnConst.BTN_LINE_ADD);
      btnCtrl.set(false,IABtnConst.BTN_LINE_DELETE);
      btnCtrl.set(false,IABtnConst.BTN_LINE_INSERT);
      btnCtrl.set(false,IABtnConst.BTN_LINE_PASTE);
      btnCtrl.set(false,IABtnConst.BTN_LINE_COPY);
      setBtnsForBilltypes(m_aryButtonGroupCard);
      // 不能修改表头数据
      BillItem[] btHeadDatas = getBillCardPanel().getHeadItems();
      for (int i = 0; i < btHeadDatas.length; i++) {
        getBillCardPanel().getHeadItem(btHeadDatas[i].getKey()).setEnabled(
            false);
      }
    }
    else {
      // 单据号、日期、库存组织、仓库、业务类型不能修改
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
      // 标志不可修改
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
        // 不是材料出库单，是否假退料不可编辑
        getBillCardPanel().getHeadItem("bwithdrawalflag").setEdit(false);
        getBillCardPanel().getHeadItem("bwithdrawalflag").setEnabled(false);
      }
      // //业务日期不能修改
      // getBillCardPanel().getBodyItem("dbizdate").setEdit(false);
      // getBillCardPanel().getBodyItem("dbizdate").setEnabled(false);
    }
    String sSource = "";
    if (m_voCurBill != null && m_voCurBill.getParentVO() != null) {
      sSource = ((BillHeaderVO) m_voCurBill.getParentVO())
          .getCsourcemodulename();
    }
    // 处理右键菜单情况
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
    // 处理集成使用情况
    if (m_bIsSOStart && m_sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
      // 销售启用，销售成本结转单只能修改单价、金额
      // 不能修改表头数据
      BillItem[] btHeadDatas = getBillCardPanel().getHeadItems();
      for (int i = 0; i < btHeadDatas.length; i++) {
        getBillCardPanel().getHeadItem(btHeadDatas[i].getKey()).setEnabled(
            false);
      }
      // 不能修改部分表体数据
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
      // 采购启用，采购入库单都不能修改
      // 不能修改表头数据
      BillItem[] btHeadDatas = getBillCardPanel().getHeadItems();
      for (int i = 0; i < btHeadDatas.length; i++) {
        getBillCardPanel().getHeadItem(btHeadDatas[i].getKey()).setEnabled(
            false);
      }
      // 不能修改部分表体数据
      BillItem[] btItemDatas = getBillCardPanel().getBodyItems();
      for (int i = 0; i < btItemDatas.length; i++) {
        if (btItemDatas[i].getKey().equals("vbatch") == false
            && btItemDatas[i].getKey().equals("cadjustbillitem") == false
            && btItemDatas[i].getKey().equals("cfadevicecode") == false) {

          // 2005.07.06 赠品不可修改
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
      // 采购启用，是入库调整
      // 不能修改表头数据
      BillItem[] btHeadDatas = getBillCardPanel().getHeadItems();
      for (int i = 0; i < btHeadDatas.length; i++) {
        getBillCardPanel().getHeadItem(btHeadDatas[i].getKey()).setEnabled(
            false);
      }
      // 不能修改部分表体数据
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
      // 采购启用，是材料出库(VMI)

      // 不能修改表头数据
      BillItem[] btHeadDatas = getBillCardPanel().getHeadItems();
      for (int i = 0; i < btHeadDatas.length; i++) {
        getBillCardPanel().getHeadItem(btHeadDatas[i].getKey()).setEnabled(
            false);
      }
      // 不能修改部分表体数据
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
      // 委外加工启用，委外加工收货单都不能修改
      // 不能修改表头数据
      BillItem[] btHeadDatas = getBillCardPanel().getHeadItems();
      for (int i = 0; i < btHeadDatas.length; i++) {
        getBillCardPanel().getHeadItem(btHeadDatas[i].getKey()).setEnabled(
            false);
      }
      // 不能修改部分表体数据
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
      // 库存管理启用日常单据只能修改单价、金额
      if (m_sBillType.equals(ConstVO.m_sBillCLCKD) == false
          || bIsWithdraw.booleanValue() == false) {
        // 不是材料出库单或不是假退料
        // 不能修改表头数据
        BillItem[] btHeadDatas = getBillCardPanel().getHeadItems();
        for (int i = 0; i < btHeadDatas.length; i++) {
          getBillCardPanel().getHeadItem(btHeadDatas[i].getKey())
              .setEdit(false);
          getBillCardPanel().getHeadItem(btHeadDatas[i].getKey()).setEnabled(
              false);
        }
        // 不能修改部分表体数据
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
        // 材料出库单，假退料
        if (getBillCardPanel().getHeadItem("bwithdrawalflag") != null) {
          // 是材料出库单，是否假退料不可编辑
          getBillCardPanel().getHeadItem("bwithdrawalflag").setEdit(false);
          getBillCardPanel().getHeadItem("bwithdrawalflag").setEnabled(false);
        }
      }
    }
    // 如果是出库单可以修改是否进项税转出标志 zlq add 20050330
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
    // 如果输入了库存组织，设置过滤条件
    String sRDID = getBillCardPanel().getHeadItem("cstockrdcenterid")
        .getValue();
    if (sRDID != null && sRDID.trim().length() != 0) {
      // 获得用户输入的单据号
      // 设置仓库参照的条件
      BillItem bt = m_bd.getHeadItem("cwarehouseid");
      if (bt != null) {
        // 设置仓库参照的条件
        String sWhere = m_sOldWareCondition + "and pk_calbody = '" + sRDID
            + "'";
        // 是否是废品库
        sWhere = sWhere + "and gubflag = 'N'";
        // 没有暂封
        sWhere = sWhere + "and sealflag = 'N'";
        ((UIRefPane) bt.getComponent()).setWhereString(sWhere);
      }
      bt = m_bd.getBodyItem("cinventorycode");
      if (bt != null) {
        String sAddString = m_sOldInvCondition;
        sAddString = sAddString + " and bd_produce.pk_calbody='" + sRDID + "'";
        if (m_bIsPlanedPriceBill) {
          // 是计划价调整单，存货的计价方式只能是计划价
          sAddString = sAddString + " and bd_produce.pricemethod = "
              + ConstVO.JHJ;
        }
        m_refpaneInv.setWhereString(sAddString);
        m_refpaneInvBack.setWhereString(sAddString);
      }
    }
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000108")/* @res "修改记录" */);
  }

  /**
   * 函数功能:显示界面情况 <p/> 参数: int iStatus ----- 当前情况 <p/> 返回值: <p/> 异常:
   */
  protected void setBtnsForStatus(int iStatus) {
    if (iStatus == INIT_STATUS) {
      getBillCardPanel().setVisible(true);
      getBillListPanel().setVisible(false);
      // 设置按钮状态
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
      setName(NCLangRes.getInstance().getStrByID("common","UCH022"/* 列表显示 */));
      
      // 设置按钮状态
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

      // 设置浏览按钮状态
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
          // 都未审核
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
          // 都审核了
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
      // 设置按钮状态
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
      "UCH021"/* 卡片显示 */));
 
      // 设置按钮状态
      btnCtrl.set(true,IABtnConst.BTN_QUERY);
      btnCtrl.set(true,IABtnConst.BTN_SWITCH);
      btnCtrl.set(true,IABtnConst.BTN_PRINT);
      btnCtrl.set(true,IABtnConst.BTN_PRINT_PRINT);
      btnCtrl.set(true,IABtnConst.BTN_PRINT_PREVIEW);
      btnCtrl.set(true,IABtnConst.BTN_ASSIST_QUERY_RELATED);
      btnCtrl.set(true,IABtnConst.BTN_IMPORT_BILL);
      // 设置浏览按钮状态
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
          // 是当前单据类型
          boolean bHasAudit = ((BillHeaderVO) m_voCurBill.getParentVO())
              .getBauditedflag().booleanValue();
          if (bHasAudit == false) {
            // 都未审核
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
            // 都审核了
            btnCtrl.set(false,IABtnConst.BTN_BILL_EDIT);
          }
          else {
            btnCtrl.set(true,IABtnConst.BTN_BILL_EDIT);
          }
        }
        else {
          // 不是当前单据类型
          btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
          btnCtrl.set(false,IABtnConst.BTN_SWITCH);
          btnCtrl.set(false,IABtnConst.BTN_BILL_COPY);
          btnCtrl.set(false,IABtnConst.BTN_BILL_EDIT);
        }
        if (sBilltype.equals(ConstVO.m_sBillDJTQD) == false) {
          // 跌价提取没有计价方式
          // 判断分录是否可审核
          // 个别计价蓝字出库单可以审核
          // 个别计价红字入库单没有指定回冲单据分录可以审核
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
            // 是个别计价未成本计算,且有数量
            double ddnum = dNumber.doubleValue();
            if (iRDFlag == 1
                && ddnum > 0
                && (sSourceBillType == null || sSourceBillType
                    .equals(ConstVO.m_sBillXSFP) == false)) {
              // 是蓝字出库单，且来源不是发票
              btnCtrl.set(true,IABtnConst.BTN_AUDIT);
            }
            else if (iRDFlag == 0 && ddnum < 0) {
              // 是红字入库单
              btnCtrl.set(true,IABtnConst.BTN_AUDIT);
            }
          }
          else {
            // 其它不可以指定
            btnCtrl.set(false,IABtnConst.BTN_AUDIT);
          }
        }
        else {
          // 其它不可以指定
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
   * 函数功能:编辑后触发 <p/> 参数: nc.ui.pub.bill.BillEditEvent e ----- 编辑事件 <p/> 返回值:
   * <p/> 异常:
   */
  public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
    String sKey = e.getKey();
    int iRowCount = getBillCardPanel().getBillTable().getRowCount();
    int iRow = e.getRow();
    if (iRowCount <= iRow) {
      iRow = -1;
    }
    try {
      // 如果输入了业务类型，带出收发类别
      if (sKey.equals("cbiztypeid")) {
        BillItem bt = getBillCardPanel().getHeadItem("cbiztypeid");
        UIRefPane pane = (UIRefPane) bt.getComponent();
        Object oBizPK = pane.getRefPK();
        if (oBizPK != null && oBizPK.toString().trim().length() != 0) {
          String sBizTypeID = oBizPK.toString().trim();
          // 获得收发类别
          Object oRDCLID = pane.getRef().getRefModel().getValue("receipttype");
          if (oRDCLID != null && oRDCLID.toString().trim().length() != 0) {
            BillItem bt2 = getBillCardPanel().getHeadItem("cdispatchid");
            if (bt2 != null) {
              bt2.setValue(oRDCLID);
            }
          }
          // 判断是否要出提示
          bt = m_bd.getHeadItem("cbillsource");
          if (bt != null) {
            int iIndex = getUIComboBoxSource().getSelectedIndex();
            if (iIndex != -1 && iIndex == m_ComboItemsVO.type_salebill) {// 销售发票
              // 是销售发票
              // 判断业务类型
              boolean bIsFQ = false; // 是否是分期收款
              boolean bIsWT = false; // 是否是委托代销
              if (sBizTypeID.trim().length() != 0) {
                Object oRule = pane.getRef().getRefModel().getValue(
                    "verifyrule");
                if (oRule != null) {
                  if (oRule.toString().equals("W")) {
                    // 是委托代销
                    bIsWT = true;
                  }
                  else if (oRule.toString().equals("F")) {
                    // 是分期收款
                    bIsFQ = true;
                  }
                }
              }
              if (bIsFQ) {
                showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                    "20143010", "UPP20143010-000109")/*
                                                       * @res
                                                       * "当前单据来源为销售发票，业务类型是分期收款，请在辅助菜单中选择来源为出库单的对应销售成本结转单"
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
                                                       * "当前单据来源为销售发票，业务类型是委托代销，请在辅助菜单中选择来源为出库单的对应销售成本结转单"
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
      // 如果输入部门，设置业务员的过滤条件
      else if (sKey.equals("cdeptid")) {
        String sDeptID = getBillCardPanel().getHeadItem("cdeptid").getValue();
        // String sUserID =
        // getBillCardPanel().getHeadItem("cemployeeid").getValue();
        if (sDeptID != null && sDeptID.trim().length() != 0) {
          // 设置业务员参照的条件
          BillItem bt = m_bd.getHeadItem("cemployeeid");
          if (bt != null) {
            // 设置业务员参照的条件
            ((UIRefPane) bt.getComponent()).setWhereString(m_sOldUserCondition
                + "and bd_psndoc.pk_deptdoc = '" + sDeptID + "'");
          }
          // 部门业务员解除强制关联关系20050519 zlq
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
            // 设置业务员参照的条件
            ((UIRefPane) bt.getComponent()).setWhereString(m_sOldUserCondition);
          }
        }
      }
      // 如果输入业务员，代出部门
      else if (sKey.equals("cemployeeid")) {
        BillItem bt = m_bd.getHeadItem("cdeptid");
        if (bt != null) {
          UIRefPane ref = (UIRefPane) getBillCardPanel().getHeadItem(
              "cemployeeid").getComponent();
          Object oDept = ref.getRefModel().getValue("bd_psndoc.pk_deptdoc");
          if (oDept != null && oDept.toString().trim().length() != 0) {
            // 设置部门
            bt.setValue(oDept);
          }
          else {
            bt.setValue("");
          }
        }
      }
      // 如果输入公司，过滤库存组织
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
      // 如果输入公司，过滤库存组织
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
      // 如果输入仓库，代出仓储库存组织
      else if (sKey.equals("cwarehouseid")) {
        BillItem bt = m_bd.getHeadItem("cstockrdcenterid");
        if (bt != null) {
          UIRefPane ref = (UIRefPane) getBillCardPanel().getHeadItem(
              "cwarehouseid").getComponent();
          Object oCalbody = ref.getRefModel().getValue("pk_calbody");
          if (oCalbody != null && oCalbody.toString().trim().length() != 0) {
            // 设置仓储库存组织
            bt.setValue(oCalbody);
            bt = m_bd.getHeadItem("cwarehouseid");
            if (bt != null) {
              // 设置仓库参照的条件
              String sWhere = m_sOldWareCondition + "and pk_calbody = '"
                  + oCalbody + "'";
              // 是否是废品库
              sWhere = sWhere + "and gubflag = 'N'";
              // 没有暂封
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
      // 如果输入了调整单据类型，使被调整单据号可编辑
      else if (sKey.equals("cadjustbilltype")) {
        Object oTemp = e.getValue();
        if (oTemp != null && oTemp.toString().trim().length() != 0) {
          getUIRefPaneAdjustBill().setWhereString("");
          getBillCardPanel().getBodyItem("cadjustbill").setEdit(true);
          getBillCardPanel().getBodyItem("cadjustbill").setEnabled(true);
          // 设置查询条件
          String sWhereString = " and a.cbilltypecode = '"
              + oTemp.toString().trim() + "' ";
          String sWareID = getBillCardPanel().getHeadItem("cwarehouseid")
              .getValue();
          String sRDID = getBillCardPanel().getHeadItem("crdcenterid")
              .getValue();
          String sStockRDID = getBillCardPanel()
              .getHeadItem("cstockrdcenterid").getValue();
          // 判断是否已有仓库、库存组织、存货，有设置批次号参照条件
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
          // 设置回冲单据分录
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
      // 如果输入了调整单据ID，使单据分录可编辑
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
          // 设置回冲单据分录
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
      // 输入了辅计量
      else if (sKey.equals("castunitname")) {
        // 设置辅计量标识
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
                // 是固定换算率，处理辅计量数量 (辅计量数量x换算率=数量)
                Object oTemp = getBillCardPanel().getBillModel().getValueAt(
                    iRow, "nnumber");
                if (oTemp != null && oTemp.toString().trim().length() != 0) {
                  UFDouble dNumber = new UFDouble(oTemp.toString().trim());
                  if (oChangeRate != null
                      && oChangeRate.toString().trim().length() != 0) {
                    UFDouble dRate = new UFDouble(oChangeRate.toString().trim());
                    UFDouble dAssistNum = dNumber.div(dRate); // (辅计量数量=数量/换算率)
                    getBillCardPanel().getBillModel().setValueAt(dAssistNum,
                        iRow, "nassistnum");
                  }
                }
                if (oInvPK != null && oInvPK.toString().trim().length() != 0) {
                  // 将固定换算率记入存货
                  m_htInvAndFix.put(oInvPK.toString().trim() + ","
                      + oMeaPK.toString().trim(), "Y");
                }
              }
              else if (oFixedflag != null
                  && oFixedflag.toString().trim().equals("N")) {
                // 不是是固定换算率，将数量清空
                Object oTemp = getBillCardPanel().getBillModel().getValueAt(
                    iRow, "nnumber");
                if (oTemp != null && oTemp.toString().trim().length() != 0) {
                  UFDouble dNumber = new UFDouble(oTemp.toString().trim());
                  UFDouble dRate = new UFDouble(oChangeRate.toString().trim());
                  UFDouble dAssistNum = dNumber.div(dRate); // (辅计量数量=数量/换算率)
                  getBillCardPanel().getBillModel().setValueAt(dAssistNum,
                      iRow, "nassistnum");
                }
                // getBillCardPanel().getBillModel().setValueAt(null,iRow,"nnumber");
              }
            }
            else {
              // 将辅计量数据清空
              getBillCardPanel().getBillModel().setValueAt(null, iRow,
                  "nchangerate");
              getBillCardPanel().getBillModel().setValueAt(null, iRow,
                  "nassistnum");
              if (oInvPK != null && oInvPK.toString().trim().length() != 0) {
                // 将不是固定换算率的删除
                m_htInvAndFix.remove(oInvPK.toString().trim());
              }
            }
          }
          else {
            // 将辅计量数据清空
            getBillCardPanel().getBillModel().setValueAt(null, iRow,
                "castunitid");
            getBillCardPanel().getBillModel().setValueAt(null, iRow,
                "nchangerate");
            getBillCardPanel().getBillModel().setValueAt(null, iRow,
                "nassistnum");
            if (oInvPK != null && oInvPK.toString().trim().length() != 0) {
              // 将不是固定换算率的删除
              m_htInvAndFix.remove(oInvPK.toString().trim());
            }
          }
        }
      }
      // 20050622 zlq 不再对批次号内容限制
      // 检查批次号是否正确
      // else if (sKey.equals("vbatch")) {
      // Object oPCH = e.getValue();
      // if (oPCH != null) {
      // if (checkChar(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
      // "UC000-0002060")/*@res "批次号"*/, oPCH.toString()) == false) {
      // return;
      // }
      // }
      // }
      // 如果输入了调整单据ID，带出存货
      // 输入了加工品
      else if (sKey.equals("vbomcodecode")) {
        // 如果加工品改变，将主键设置为新值
        BillItem bt = getBillCardPanel().getBodyItem("vbomcodecode");
        UIRefPane pane = (UIRefPane) bt.getComponent();
        Object oPK = pane.getRefPK();
        if (oPK != null && oPK.toString().trim().length() != 0) {
          // 获得存货信息
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
      // 数量，
      else if (sKey.equals("nnumber")) {
        Object oTemp = e.getValue();
        if (oTemp != null && oTemp.toString().trim().length() != 0) {
          UFDouble dNumber = new UFDouble(oTemp.toString().trim());
          if (dNumber.doubleValue() < 0) {
            // 是红冲单，使被调整单据分录可调整
            getBillCardPanel().getBodyItem("cadjustbillitem").setEdit(true);
            getBillCardPanel().getBodyItem("cadjustbillitem").setEnabled(true);
          }
          else {
            // 正常单据，置调整单据分录为空
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
            // 设置辅计量标识
            Object oMeaPK = getBillCardPanel().getBillModel().getValueAt(iRow,
                "castunitid");
            if (oInvID != null && oMeaPK != null) {
              // 获得主辅计量名称
              Object oMainMeaName = getBillCardPanel().getBillModel()
                  .getValueAt(iRow, "cinventorymeasname");
              Object oMeaName = getBillCardPanel().getBillModel().getValueAt(
                  iRow, "castunitname");
              Object oFixedflag = "Y";
              Object oChangeRate = new Integer(1);
              if (oMainMeaName.equals(oMeaName) == false) {
                // 取得辅计量固定换算率标志，取得界面上输入的换算率
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
                // 是固定换算率，修改辅计量数量
                if (oChangeRate != null
                    && oChangeRate.toString().trim().length() != 0) {
                  UFDouble dRate = new UFDouble(oChangeRate.toString().trim());
                  UFDouble dAssistNum = dNumber.div(dRate); // (辅计量数量=数量/换算率)
                  dAssistNum = dAssistNum.setScale(m_iPeci[0],
                      UFDouble.ROUND_HALF_UP);
                  getBillCardPanel().getBillModel().setValueAt(dAssistNum,
                      iRow, "nassistnum");
                }
              }
              else if (oFixedflag != null
                  && oFixedflag.toString().trim().equals("N")) {
                // 不是固定换算率，修改换算率
                Object oAssisNum = getBillCardPanel().getBillModel()
                    .getValueAt(iRow, "nassistnum");
                if (oAssisNum != null
                    && oAssisNum.toString().trim().length() != 0) {//辅数量有值
                  UFDouble dAssisNum = new UFDouble(oAssisNum.toString().trim());
                  UFDouble dRate = dNumber.div(dAssisNum); // 换算率=数量/辅计量数量
                  getBillCardPanel().getBillModel().setValueAt(dRate, iRow,
                      "nchangerate");
                }else if (oChangeRate != null
                    && oChangeRate.toString().trim().length() != 0){//辅数量没有值
                	UFDouble dRate = new UFDouble(oChangeRate.toString().trim());
                  UFDouble dAssistNum = dNumber.div(dRate); // (辅计量数量=数量/换算率)
                  dAssistNum = dAssistNum.setScale(m_iPeci[0],
                      UFDouble.ROUND_HALF_UP);
                  getBillCardPanel().getBillModel().setValueAt(dAssistNum,
                      iRow, "nassistnum");
                }
              }
            }
          }
          // 处理公式数据
          // 金额 ＝ 单价 × 数量
          Object oTempDJ = getBillCardPanel().getBillModel().getValueAt(iRow,
              "nprice");
          if (oTempDJ != null && oTempDJ.toString().trim().length() != 0) {
            UFDouble dPrice = new UFDouble(oTempDJ.toString().trim());
            UFDouble dMny = dPrice.multiply(dNumber);
            dMny = dMny.setScale(m_iPeci[2], UFDouble.ROUND_HALF_UP);
            if (dMny.doubleValue() > m_dMaxValue) {
              // showErrorMessage("计算出金额超过最大值" + m_dMaxValue + "，请调整");
              String[] value = new String[] {
                String.valueOf(m_dMaxValue)
              };
              showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                  "20143010", "UPP20143010-000164", null, value));
              getBillCardPanel().getBillModel()
                  .setValueAt(null, iRow, "nmoney");
              // 计算进项税转出金额
              calcTransIncomeTaxMny(e.getRow());
            }
            else {
              getBillCardPanel().getBillModel()
                  .setValueAt(dMny, iRow, "nmoney");
              // 计算进项税转出金额
              calcTransIncomeTaxMny(e.getRow());
            }
          }
          else {
            // 单价 ＝ 金额 / 数量
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
          // 计划金额 ＝ 计划单价 × 数量
          oTempDJ = getBillCardPanel().getBillModel().getValueAt(iRow,
              "nplanedprice");
          if (oTempDJ != null && oTempDJ.toString().trim().length() != 0) {
            UFDouble dPrice = new UFDouble(oTempDJ.toString().trim());
            UFDouble dMny = dPrice.multiply(dNumber);
            dMny = dMny.setScale(m_iPeci[2], UFDouble.ROUND_HALF_UP);
            if (dMny.doubleValue() > m_dMaxValue) {
              // showErrorMessage("计算出计划金额超过最大值" + m_dMaxValue + "，请调整");
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
          // 计算进项税转出金额
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
      // 如果输入金额，处理公式
      else if (sKey.equals("nmoney")) {
        Object oTemp = e.getValue();
        if (oTemp != null && oTemp.toString().trim().length() != 0) {
          UFDouble dMny = new UFDouble(oTemp.toString().trim());
          // 计算进项税转出金额
          calcTransIncomeTaxMny(e.getRow());
          // 处理公式数据
          // 单价 ＝ 金额 / 数量
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
          // 计算进项税转出金额
          calcTransIncomeTaxMny(e.getRow());
        }
      }
      // 如果输入单价，处理公式
      else if (sKey.equals("nprice")) {
        Object oTemp = e.getValue();
        if (oTemp != null && oTemp.toString().trim().length() != 0) {
          UFDouble dPrice = new UFDouble(oTemp.toString().trim());
          // 处理公式数据
          // 金额 ＝ 数量 × 单价
          Object oTempSL = getBillCardPanel().getBillModel().getValueAt(iRow,
              "nnumber");
          if (oTempSL != null && oTempSL.toString().trim().length() != 0) {
            UFDouble dNumber = new UFDouble(oTempSL.toString().trim());
            UFDouble dMny = dNumber.multiply(dPrice);
            dMny = dMny.setScale(m_iPeci[2], UFDouble.ROUND_HALF_UP);
            if (dMny.doubleValue() > m_dMaxValue) {
              // showErrorMessage("计算出金额超过最大值" + m_dMaxValue + "，请调整");
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
            // 计算进项税转出金额
            calcTransIncomeTaxMny(e.getRow());
          }
        }
        else {
          getBillCardPanel().getBillModel().setValueAt(null, iRow, "nmoney");
          // 计算进项税转出金额
          calcTransIncomeTaxMny(e.getRow());
        }
      }
      // 如果将项目信息清空，将项目阶段信息清空
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
      // 处理换算率变化
      else if (sKey.equals("nchangerate")) {
        Object oTemp = e.getValue();
        if (oTemp != null && oTemp.toString().trim().length() != 0) {
          UFDouble dRate = new UFDouble(oTemp.toString().trim());
          Object oAstNum = getBillCardPanel().getBillModel().getValueAt(iRow,
              "nassistnum");
          // 修改主计量数量
          if (oAstNum != null && oAstNum.toString().trim().length() != 0) {
            UFDouble dAstNum = new UFDouble(oAstNum.toString().trim());
            UFDouble dBasNumber = dAstNum.multiply(dRate); // (数量=辅计量数量*换算率)
            getBillCardPanel().getBillModel().setValueAt(dBasNumber, iRow,
                "nnumber");
            // 金额 = 数量 * 单价
            Object oPrice = getBillCardPanel().getBillModel().getValueAt(iRow,
                "nprice");
            if (oPrice != null && oPrice.toString().trim().length() != 0) {
              UFDouble dPrice = new UFDouble(oPrice.toString().trim());
              UFDouble dMny = dBasNumber.multiply(dPrice);
              dMny = dMny.setScale(m_iPeci[2], UFDouble.ROUND_HALF_UP);
              getBillCardPanel().getBillModel()
                  .setValueAt(dMny, iRow, "nmoney");
              // 计算进项税转出金额
              calcTransIncomeTaxMny(e.getRow());
            }
            // 计划金额 = 数量 * 计划单价
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
      // 处理辅计量数量
      else if (sKey.equals("nassistnum")) {
        Object oTemp = e.getValue();
        if (oTemp != null && oTemp.toString().trim().length() != 0) {
          UFDouble dNumber = new UFDouble(oTemp.toString().trim());
          BillItem bt = getBillCardPanel().getBodyItem("castunitname");
          if (bt != null) {
            Object oChangeRate = getBillCardPanel().getBillModel().getValueAt(
                iRow, "nchangerate");
            // 修改主计量数量
            if (oChangeRate != null
                && oChangeRate.toString().trim().length() != 0) {
              UFDouble dRate = new UFDouble(oChangeRate.toString().trim());
              UFDouble dBasNumber = dNumber.multiply(dRate); // (数量=辅计量数量*换算率)
              getBillCardPanel().getBillModel().setValueAt(dBasNumber, iRow,
                  "nnumber");
              // 金额 = 数量 * 单价
              Object oPrice = getBillCardPanel().getBillModel().getValueAt(
                  iRow, "nprice");
              if (oPrice != null && oPrice.toString().trim().length() != 0) {
                UFDouble dPrice = new UFDouble(oPrice.toString().trim());
                UFDouble dMny = dBasNumber.multiply(dPrice);
                if (dMny.doubleValue() > m_dMaxValue) {
                  // showErrorMessage("计算出金额超过最大范围 " + m_dMaxValue + "，请调整");
                  String[] value = new String[] {
                    String.valueOf(m_dMaxValue)
                  };
                  showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                      "20143010", "UPP20143010-000164", null, value));
                  getBillCardPanel().getBillModel().setValueAt(null, iRow,
                      "nmoney");
                  // 计算进项税转出金额
                  calcTransIncomeTaxMny(e.getRow());
                }
                else {
                  getBillCardPanel().getBillModel().setValueAt(dMny, iRow,
                      "nmoney");
                  // 计算进项税转出金额
                  calcTransIncomeTaxMny(e.getRow());
                }
              }
              else {
                // 单价 ＝ 金额 / 数量
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
              // 计划金额 = 数量 * 计划单价
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
      // 处理自定义项
      else if (sKey.startsWith("vdef")) {
        // 表头自定义项编辑以后触发
        if (e.getPos() == BillItem.HEAD) {
          DefSetTool.afterEditHead(getBillCardPanel().getBillData(), sKey,
              "pk_defdoc"
                  + sKey.substring(sKey.indexOf("vdef") + "vdef".length(), sKey
                      .length()));
        }
        // 表体自定义项编辑以后触发
        else if (e.getPos() == BillItem.BODY) {
          DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), e
              .getRow(), sKey, "pk_defdoc"
              + sKey.substring(sKey.indexOf("vdef") + "vdef".length(), sKey
                  .length()));
        }
      }
      // 处理进项税是否转出
      else if (sKey.equals("btransferincometax")) {
        UFBoolean flag = new UFBoolean(getBillCardPanel().getBillModel()
            .getValueAt(e.getRow(), "btransferincometax").toString());
        // 选中
        if (flag.booleanValue()) {
          getBillCardPanel().getBillModel().setCellEditable(e.getRow(),
              "nincometax", true);
          getBillCardPanel().getBillModel().setCellEditable(e.getRow(),
              "nexpaybacktax", true);
          // 带出该行存货的进项税率
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
          // 计算转出金额
          calcTransIncomeTaxMny(e.getRow());
          // 未选中
        }
        else {
          getBillCardPanel().getBillModel().setCellEditable(e.getRow(),
              "nincometax", false);
          getBillCardPanel().getBillModel().setCellEditable(e.getRow(),
              "nexpaybacktax", false);
          // 清空转出金额
          getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
              "ndrawsummny");
        }
      }
      // 检查进项税率是否大于等于出口退税率,并计算转出金额 zlq add 20050330
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
   * 检查进项税率是否大于等于出口退税率,并计算转出金额
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

        // 检查进项税率是否大于等于出口退税率
        if (dbValueEx.doubleValue() > dbValueIn.doubleValue()) {
          showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000283")/* "进项税率应大于等于出口退税率" */);
          return false;
        }
        // 计算转出税额
        if (oValueMny != null) {
          UFDouble dbTransMny = ((dbValueIn.sub(dbValueEx))
              .multiply(dbValueMny)).div(new UFDouble(100));
          if (dbTransMny.doubleValue() > m_dMaxValue) {
            // showErrorMessage("计算出金额超过最大值" + m_dMaxValue + "，请调整");
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
   * 排序后触发。 创建日期：(2001-10-26 14:31:14)
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
    // 触发公式
    execListPanelBodyFormula();
    // showHintMessage("选择第" + (iIndex + 1) + "条单据");
    String[] value = new String[] {
      String.valueOf(iIndex + 1)
    };
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000166", null, value));
    boolean bHasAudit = ((BillHeaderVO) m_voCurBill.getParentVO())
        .getBauditedflag().booleanValue();
    if (bHasAudit == false) {
      // 都未审核
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
      // 是当前单据类型
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
        // 都审核了或都生成凭证了
        btnCtrl.set(false,IABtnConst.BTN_BILL_EDIT);
      }
      else {
        btnCtrl.set(true,IABtnConst.BTN_BILL_EDIT);
      }
    }
    else {
      // 不是当前单据类型
      btnCtrl.set(false,IABtnConst.BTN_SWITCH);
      btnCtrl.set(false,IABtnConst.BTN_BILL_COPY);
      btnCtrl.set(false,IABtnConst.BTN_BILL_EDIT);
    }
    setBtnsForBilltypes(m_aryButtonGroupList);
    if (m_voCurBill.getChildrenVO().length != 0) {
      // 调方法，使“个别指定”按钮状态正确
      BillEditEvent event = new BillEditEvent(
          getBillListPanel().getBodyTable(), -1, 0);
      bodyChanged(event);
    }
  }

  /**
   * 函数功能:编辑前触发 <p/> 参数: nc.ui.pub.bill.BillEditEvent e ----- 编辑事件 <p/> 返回值:
   * <p/> 异常:
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
    // 获得审核状态
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
      // 有分录已经审核，只能修改数量、单价、金额
      if (bLineIsAudited.booleanValue()) {
        // 此行分录已经成本计算
        // bt.setEnabled(false);
        String[] value = new String[] {
            String.valueOf(e.getRow() + 1), sName
        };
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000176"/* 第{0}行分录已经成本计算，不能修改{1} */, null, value));
        return false;
      }
    }
    if (bLineIsVouched != null && bLineIsVouched.booleanValue()) {
      // 有分录已经生成实时凭证
      // 此行分录已经成本计算
      // bt.setEnabled(false);
      String[] value = new String[] {
          String.valueOf(e.getRow() + 1), sName
      };
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000177"/* 第{0}行分录已经生成实时凭证，不能修改{1} */, null, value));
      return false;
    }
    int iColumnCount = getBillCardPanel().getBillModel().getBodyColByKey(sKey);
    if (iColumnCount == -1
        || ((bt.isEnabled() == false || bt.isEdit() == false)))// 库存启用不能修改附计量信息
    {
      if (m_bIsICStart) {
        // showHintMessage("不能修改" + sName);
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
        // showHintMessage("不能修改" + sName);
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
        // 采购启用，是入库调整或材料出库(VMI)
        if (m_voCurBill != null) {
          String sSource = ((BillHeaderVO) m_voCurBill.getParentVO())
              .getCsourcemodulename();
          if (sSource != null && sSource.equals(ConstVO.m_sModulePO)) {
            // 是采购传人的
            // showHintMessage("不能修改" + sName);
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
        // 销售启用，是销售成本结转单
        if (sKey.equals("vbatch") == false) {
          // showHintMessage("不能修改" + sName);
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
        // showHintMessage("不能修改" + sName);
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

      // 是否被调整单据可编辑
      if (sKey.equals("cadjustbill")) {
        Object oBillType = getBillCardPanel().getBodyValueAt(iRow,
            "cadjustbilltype");
        if (oBillType == null || oBillType.toString().trim().length() == 0) {
          return false;
        }
      }
      // 设置换算率
      else if (sKey.equals("nchangerate")) {
        Object oInvID = getBillCardPanel().getBillModel().getValueAt(iRow,
            "cinventoryid");
        if (oInvID != null) {
          // 设置辅计量标识
          Object oMeaPK = getBillCardPanel().getBillModel().getValueAt(iRow,
              "castunitid");
          if (oMeaPK != null && oMeaPK.toString().trim().length() != 0) {
            Object oFixedflag = m_htInvAndFix.get(oInvID.toString().trim()
                + "," + oMeaPK.toString().trim());
            if (oFixedflag != null && oFixedflag.toString().equals("Y")) {
              // 是固定换算率，不能修改换算率
              showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                  "SCMCOMMON", "UPPSCMCommon-000082")/*
                                                       * @res
                                                       * "当前存货主计量与辅计量之间是固定换算率，不能修改换算率"
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
                "SCMCOMMON", "UPPSCMCommon-000155")/* @res "没有录入辅计量单位，不能输入换算率" */);
            return false;
            // getBillCardPanel().getBodyItem("nchangerate").setEnabled(false);
          }
        }
      }
      // 设置辅计量单位的条件
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
                                                   * "当前存货不是辅计量管理，不能输入辅计量单位"
                                                   */);
            // getBillCardPanel().getBodyItem("castunitname").setEnabled(false);
            return false;
          }
        }
        else {
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000112")/* @res "请先输入存货信息，再输入辅计量单位" */);
          return false;
        }
      }
      // 将自由项设置到具体属性上
      else if (sKey.equals("vfree0")) {
        // 设置自由项参照
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
          // 整理参数VO
          InvVO voInv = new InvVO();
          voInv.setCinvmanid(sInvManID);
          voInv.setCinventoryid(sInvManID);
          voInv.setCinventorycode(sInvCode);
          voInv.setInvname(sInvName);
          voInv.setInvspec(sInvSpec);
          voInv.setInvtype(sInvType);
          // 设置自由项VO
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
          // 设置VO
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
      // 设置批次号参照
      else if (sKey.equals("vbatch")) {
        // 如果是出库单且不是期初销售成本结转单、存货是批次号管理，批次号为参照
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
            // 是自由项管理
            Object oFree = getBillCardPanel().getBillModel().getValueAt(iRow,
                "vfree0");
            if (oFree == null || oFree.toString().trim().length() == 0) {
              showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                  "20143010", "UPP20143010-000113")/*
                                                     * @res
                                                     * "当前存货是自由项管理，请输入自由项信息"
                                                     */);
              return false;
            }
          }
          // 是批次管理
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
          // 判断是否已有仓库、库存组织、存货，有设置批次号参照条件
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
          // 设置自由项
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
              "20143010", "UPP20143010-000114")/* @res "当前存货不是批次管理，不能输入批次号" */);
          return false;
        }
        else {
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000115")/* @res "请输入存货信息" */);
          return false;
        }
        Object oBatch = getBillCardPanel().getBillModel().getValueAt(iRow,
            "vbatch");
        if (oBatch != null) {
          getUIRefPaneBatch().setBatchValue(oBatch.toString());
        }
        // 假退料材料出库单可随便输入批次号
        BillItem bt2 = m_bd.getHeadItem("bwithdrawalflag");
        if (m_sBillType.equals(ConstVO.m_sBillCLCKD) && bt2 != null) {
          // Object oValue = bt2.getValue();
          UIComboBox uiCombobox = (UIComboBox) bt2.getComponent();
          int iItem = uiCombobox.getSelectedIndex();
          // if (oValue != null && oValue.toString().equals("是")) {
          if (iItem == m_ComboItemsVO.type_yes) {// 是假退料
            getUIRefPaneBatch().setHasZero(true);
            // 可以手工输入
            getUIRefPaneBatch().setAutoCheck(false);
          }
          else if (m_sBillType.equals(ConstVO.m_sBillCLCKD)) {
            getUIRefPaneBatch().setHasZero(false);
            // 不可手工输入
            getUIRefPaneBatch().setAutoCheck(true);
          }
        }
        else if (m_sBillType.equals(ConstVO.m_sBillCLCKD)) {
          getUIRefPaneBatch().setHasZero(false);
          // 不可手工输入
          getUIRefPaneBatch().setAutoCheck(true);
        }
      }
      // 设置是否可以输入辅计量数量
      else if (sKey.equals("nassistnum")) {
        Object oAstUnitID = getBillCardPanel().getBillModel().getValueAt(iRow,
            "castunitid");
        if (oAstUnitID == null || oAstUnitID.toString().trim().length() == 0) {
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000116")/* @res "没有录入辅计量单位，不能输入辅计量数量" */);
          // getBillCardPanel().getBodyItem("nassistnum").setEnabled(false);
          return false;
        }
        else {
          getBillCardPanel().getBodyItem("nassistnum").setEnabled(true);
          return true;
        }
      }
      // 设置是否允许输入单价
      else if (sKey.equals("nprice") && (m_bIsOtherBill || m_bIsOutBill)
          && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD) == false
          && m_sBillType.equals(ConstVO.m_sBillCKTZD) == false
          && m_sBillType.equals(ConstVO.m_sBillJHJTZD) == false) {
        // String sParam = ce.getParaValue(m_sCorpID, ConstVO.m_iPara_ZDYDJ);
        // if (sParam.equals("否") || sParam.equalsIgnoreCase("N"))
        // {/*-=notranslate=-*/
        if (!m_bAllowDefinePriceByUser) {
          // 出库单不允许自定义单价，但期初单据必须输入单价
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000117")/*
                                                 * @res
                                                 * "参数设置为出库单不允许自定义单价，不能输入单价"
                                                 */);
          // getBillCardPanel().getBodyItem("nprice").setEnabled(false);
          return false;
        }
      }
      // 设置是否允许输入单价
      else if (sKey.equals("nmoney") && (m_bIsOtherBill || m_bIsOutBill)
          && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD) == false
          && m_sBillType.equals(ConstVO.m_sBillCKTZD) == false
          && m_sBillType.equals(ConstVO.m_sBillJHJTZD) == false) {
        // String sParam = ce.getParaValue(m_sCorpID, ConstVO.m_iPara_ZDYDJ);
        if (!m_bAllowDefinePriceByUser) { // sParam.equals("否") ||
                                          // sParam.equalsIgnoreCase("N"))
                                          // {/*-=notranslate=-*/
          // 出库单不允许自定义单价，但期初单据必须输入单价
          // getBillCardPanel().getBodyItem("nmoney").setEnabled(false);
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000118")/*
                                                 * @res
                                                 * "参数设置为出库单不允许自定义单价，不能输入金额"
                                                 */);
          return false;
        }
      }
      // 设置回冲单据分录参照，非入库调整单
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
            // 是红冲单
            getUIRefPaneAdjustBillItem().setWhereString("");
            String sWhereString = " having a.nnumber - " + sNotNull
                + "(a.nsettledretractnum,0) >= " + (-dNumber);
            getUIRefPaneAdjustBillItem().setNumCondition(sWhereString);
            sWhereString = "";
            sWhereString = sWhereString + " and a.cstockrdcenterid = '" + sRDID
                + "'";
            // 判断是否已有仓库，有设置批次号参照条件
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
                "20143010", "UPP20143010-000119")/* @res "当前数量不小于0，不能输入回冲单据分录" */);
            return false;
          }
        }
        else {
          // 不是入库调整单
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000120")/* @res "没有输入数量，不能输入回冲单据分录" */);
          return false;
        }
      }
      else if (sKey.equals("cadjustbillitem") && m_bIsInAdjustBill) {
        // 入库调整单如果没有被调整单据类型或被调整单据，被调整单据分录不可编辑
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
                                                 * "被调整单据类型或被调整单据内容不全，请先输入调整信息，再输入调整单据分录"
                                                 */);
          // getBillCardPanel().getBodyItem("cadjustbillitem").setEnabled(false);
          return false;
        }
      }
      // 设置项目阶段
      else if (sKey.equals("cprojectphasecode")) {
        Object oProjectID = getBillCardPanel().getBillModel().getValueAt(iRow,
            "cprojectid");
        if (oProjectID == null || oProjectID.toString().trim().length() == 0) {
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000122")/* @res "没有输入项目信息，不能输入项目阶段信息" */);
          return false;
        }
        ((nc.ui.bd.b39.PhaseRefModel) getUIRefPaneJobParse().getRefModel())
            .setJobID(oProjectID.toString().trim());
      }
      // 20050525 关闭固定资产和存货接口
      // 设置固定资产参照
      // else if (sKey.equals("cfadevicecode") && (m_bIsOtherBill ||
      // m_bIsOutBill)) {
      // Object oFadeviceID = getBillCardPanel().getBillModel().getValueAt(iRow,
      // "cfadeviceid");
      // if (oFadeviceID != null && oFadeviceID.toString().trim().length() != 0)
      // {
      // getUIRefPaneFacard().setPkSubeq(oFadeviceID.toString());
      // }
      // }
      // //设置固定资产参照
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
      // 设置工作中心参照
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
      // 设置成本对象参照的条件
      else if (sKey.equals("vbomcodecode")) {
        String sCalbodyID = getBillCardPanel().getHeadItem("crdcenterid")
            .getValue();
        if (sCalbodyID != null) {
          bt = m_bd.getBodyItem("vbomcodecode");
          if (bt != null) {
            // 设置成本对象参照的条件
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
            // 是计划价调整单，存货的计价方式只能是计划价
            sAddString += " and bd_produce.pricemethod = " + ConstVO.JHJ + " ";
          }
          m_refpaneInv.setWhereString(sAddString);
          m_refpaneInvBack.setWhereString(sAddString);
        }
        else {
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000285")/* 请先选择库存组织(成本) */);
          return false;
        }
      }
      // 如果是否进项税转出未选中则进项税和出口退税不可编辑 zlq add 20050329
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
              "20143010", "UPP20143010-000278")/* "未选中'是否进项税转出',不能编辑此字段" */);
          return false;
        }
      }

      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000168", "20143010", new String[] {
            sName
          })/* "修改{0}" */);
    }
    catch (Exception ee) {
      showErrorMessage(ee.getMessage());
      ee.printStackTrace();
    }
    return true;
  }

  /**
   * 函数功能:行变化后触发 <p/> 参数: nc.ui.pub.bill.BillEditEvent e ----- 编辑事件 <p/> 返回值:
   * <p/> 异常:
   */
  public void bodyChanged(BillEditEvent e) {
    int iIndex = e.getRow();
    iIndex = getBillListPanel().getBodyTable().getSelectedRow();
    String sBilltype = ((BillHeaderVO) m_voCurBill.getParentVO())
        .getCbilltypecode();
    // 由单据列表界面表体表格触发
    if (e.getSource() == getBillListPanel().getBodyTable()) {

      String sAuditorID = ((BillItemVO) m_voCurBill.getChildrenVO()[iIndex])
          .getCauditorid();
      if (sAuditorID != null && sAuditorID.length() != 0) {
        btnCtrl.set(false,IABtnConst.BTN_AUDIT);
      }
      else if (sBilltype.equals(ConstVO.m_sBillDJTQD) == false) {
        int iPricemode = ((BillItemVO) m_voCurBill.getChildrenVO()[iIndex])
            .getFpricemodeflag().intValue();
        // 未审核
        // 判断分录是否可审核
        // 个别计价蓝字出库单可以审核
        // 个别计价红字入库单没有指定回冲单据分录可以审核
        // 来源是发票的不指定
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
          // 是个别计价未成本计算,且有数量
          double ddnum = dNumber.doubleValue();
          if (iRDFlag == 1 && ddnum > 0) {
            if (sSourceBillType == null) {
              // 是蓝字出库单，且来源为空
              btnCtrl.set(true,IABtnConst.BTN_AUDIT);
            }
            else if (sSourceBillType.equals(ConstVO.m_sBillXSFP) == false) {
              // 是蓝字出库单，且来源不是发票
              btnCtrl.set(true,IABtnConst.BTN_AUDIT);
            }
            else if (sBizTypeID == null
                || (m_sFQSK.indexOf(sBizTypeID) == -1 && m_sWTDX
                    .indexOf(sBizTypeID) == -1)) {
              // 是蓝字出库单，且来源是发票，但不是分期收款和委托代销
              btnCtrl.set(true,IABtnConst.BTN_AUDIT);
            }
            else {
              btnCtrl.set(false,IABtnConst.BTN_AUDIT);
            }
          }
          else if (iRDFlag == 0 && ddnum < 0) {
            // 是红字入库单
            btnCtrl.set(true,IABtnConst.BTN_AUDIT);
          }
        }
        else {
          // 其它不可以指定
          btnCtrl.set(false,IABtnConst.BTN_AUDIT);
        }
      }
      else {
        // 其它不可以指定
        btnCtrl.set(false,IABtnConst.BTN_AUDIT);
      }
      // 设置按钮
      setBtnsForBilltypes(m_aryButtonGroupList);
    }
  }

  /**
   * 函数功能:行变化后触发 <p/> 参数: nc.ui.pub.bill.BillEditEvent e ----- 编辑事件 <p/> 返回值:
   * <p/> 异常:
   */
  public void bodyRowChange(BillEditEvent e) {
  }

  /**
   * 对话框关闭事件监听者必须实现的接口方法
   * 
   * @param event
   *          UIDialogEvent 对话框关闭事件
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
      // showHintMessage("返回单据界面");
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000173"));
    }
  }

  /**
   * 子类实现该方法，返回业务界面的标题。
   * 
   * @return java.lang.String
   */
  public String getTitle() {
    return m_sTitle;
  }

  /**
   * 函数功能:行变化后触发 <p/> 参数: nc.ui.pub.bill.BillEditEvent e ----- 编辑事件 <p/> 返回值:
   * <p/> 异常:
   */
  public void headChanged(BillEditEvent e) {

    int iIndex = e.getRow();
    if( iIndex == -1)
    	return;
    String sBilltype = "";
    try {
      // 由单据列表界面表头表格触发
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
          // 触发公式
          execListPanelBodyFormula();
          // showHintMessage("选择第" + (iIndex + 1) + "条单据");
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
            // 是当前单据类型
            boolean bHasAudit = ((BillHeaderVO) m_voCurBill.getParentVO())
                .getBauditedflag().booleanValue();
            if (bHasAudit == false) {
              // 都未审核
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
              // 都审核了
              btnCtrl.set(false,IABtnConst.BTN_BILL_EDIT);
            }
            else {
              btnCtrl.set(true,IABtnConst.BTN_BILL_EDIT);

            }
          }
          else {
            // 不是当前单据类型
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

            // 设置形态
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
                // 处理表体
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
              // 未审核
              // 判断分录是否可审核
              // 个别计价蓝字出库单可以审核
              // 个别计价红字入库单没有指定回冲单据分录可以审核
              // 来源是发票的不指定
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
                // 是个别计价未成本计算,且有数量
                double ddnum = dNumber.doubleValue();
                if (iRDFlag == 1 && ddnum > 0) {
                  if (sSourceBillType == null) {
                    // 是蓝字出库单，且来源不是发票
                    btnCtrl.set(true,IABtnConst.BTN_AUDIT);
                  }
                  else if (sSourceBillType.equals(ConstVO.m_sBillXSFP) == false) {
                    // 是蓝字出库单，且来源不是发票
                    btnCtrl.set(true,IABtnConst.BTN_AUDIT);
                  }
                  else if (m_sFQSK.indexOf(sBizTypeID) == -1
                      && m_sWTDX.indexOf(sBizTypeID) == -1) {
                    // 是蓝字出库单，且来源是发票，但不是分期收款和委托代销
                    btnCtrl.set(true,IABtnConst.BTN_AUDIT);
                  }
                  else {
                    btnCtrl.set(false,IABtnConst.BTN_AUDIT);
                  }
                }
                else if (iRDFlag == 0 && ddnum < 0) {
                  // 是红字入库单
                  btnCtrl.set(true,IABtnConst.BTN_AUDIT);
                }
              }
              else {
                // 其它不可以指定
                btnCtrl.set(false,IABtnConst.BTN_AUDIT);
              }
            }
            else {
              btnCtrl.set(false,IABtnConst.BTN_AUDIT);
            }
            // 设置选择为空
            getBillListPanel().getBodyTable().clearSelection();
            // 设置按钮
            setBtnsForBilltypes(m_aryButtonGroupList);
          }
          try {
            for (int hh = 0; hh < m_voCurBill.getChildrenVO().length; hh++) {
              // 表体自定义项处理
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
                    if ("统计".equals(sDefType)) {/*-=notranslate=-*/
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
            Log.info("取自定义项信息出错");
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
   * 函数功能:鼠标双击触发 <p/> 参数: nc.ui.pub.bill.BillMouseEnent e ----- 鼠标事件 <p/> 返回值:
   * <p/> 异常:
   */
  public void mouse_doubleclick(nc.ui.pub.bill.BillMouseEnent e) {
    if (e.getPos() == 0) {
      // 是表头数据
      if (m_voBills != null && m_voBills.length != 0) {
        String sBilltype = ((BillHeaderVO) m_voCurBill.getParentVO())
            .getCbilltypecode();
        if (sBilltype.equals(m_sBillType)
            || (sBilltype.equals(ConstVO.m_sBillQCRKD) && m_sBillType
                .equals(ConstVO.m_sBillQCXSCBJZD))
            || (sBilltype.equals(ConstVO.m_sBillQCXSCBJZD) && m_sBillType
                .equals(ConstVO.m_sBillQCRKD))) {
          // 是当前单据类型
          onButtonListClicked();
        }
        else {
          //"当前功能点单据类型编码是 {0}但选择的单据单据类型编码是{1}所以不能显示表单界面
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
   * 子类实现该方法，响应按钮事件
   * 
   * @param bo
   *          ButtonObject
   */
  public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
    // 停止编辑
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
	  StringBuffer err=new StringBuffer();//存储提示信息
	  Boolean result = idetermineService.check(corp);
	  if(result){//判断当前公司是否为国内公司，否则不执行
		  int rows = 0;//选择行数
		  int [] selectRows = getBillListPanel().getHeadTable().getSelectedRows();//获取列表状态下选中所有行序号
		  rows=selectRows.length;//选总行数
		  
		  for(int a=0;a<selectRows.length;a++)
		  {
			  BillVO billvo = null;
			  m_iCurBillPrt = selectRows[a];//把当前选中行的序号赋给当前单据在缓存中的序号
			  billvo = m_voBills[m_iCurBillPrt];//当前正在处理的单据
			  String billCode = ((BillHeaderVO) billvo.getParentVO()).getVbillcode();//单据号
			  if (billvo.getChildrenVO() == null) {
			        //还没有获得表体数据
			        ClientLink cl = ce.getClientLink();
			        try {
			        	billvo = BillBO_Client.querybillWithOtherTable(null, null,
						      null, null, null, billvo, new Boolean(true), cl)[0];
					} catch (Exception e) {
						e.printStackTrace();
					}
			        }
			  int brows = billvo.getChildrenVO().length; //表体行数
			  BillHeaderVO hvo=(BillHeaderVO) billvo.getParentVO();//表头VO
			  
			  TempGeneralBillVO tgbvo=new TempGeneralBillVO();//临时表vo
			  String yn=judgeDJBH(hvo.getVbillcode(),corp);
			  if(yn==null || "N".equals(yn)){
			//遍历
//			  for(int b=0;b<rows;b++)
//			  {
//				  BillItemVO btvo = (BillItemVO) billvo.getChildrenVO()[a];//获取缓存中表体的各条数据VO
//				  for(int i=0;b<brows;i++){
				      Map<String,String> map=new HashMap<String,String>();
					  List list=queryData(hvo.getVbillcode(),hvo.getPk_corp());
					  strs= CheckData(list,hvo.getVbillcode());
					  for(int i=0;i<list.size();i++){
						  map = (Map) list.get(i);
						  String djh=map.get("vbillcode")==null?"":map.get("vbillcode").toString();//单据号
						  String djlx=map.get("cbilltypecode")==null?"":map.get("cbilltypecode").toString();//单据类型
						  String gs=map.get("pk_corp")==null?"":map.get("pk_corp").toString();//公司
						  String djbs=map.get("cbillid")==null?"":map.get("cbillid").toString();//单据标识
						  String bmbs=map.get("cdeptid")==null?"":map.get("cdeptid").toString();//部门标识
						  String rybs=map.get("cemployeeid")==null?"":map.get("cemployeeid").toString();//人员标识
						  String zdrbs=map.get("coperatorid")==null?"":map.get("coperatorid").toString();//制单人标识
						  String sfbs=map.get("cdispatchid")==null?"":map.get("cdispatchid").toString();//收发标识
						  String ksbs=map.get("ccustomvendorid")==null?"":map.get("ccustomvendorid").toString();//客商标识
						  String flbs=map.get("cbill_bid")==null?"":map.get("cbill_bid").toString();//分录标识
						  Double je=map.get("nmoney")==null?0:Double.valueOf(String.valueOf(map.get("nmoney")));//金额
						  Double sl=map.get("nnumber")==null?0:Double.valueOf(String.valueOf(map.get("nnumber")));//数量
						  String chbs=map.get("cinventoryid")==null?"":map.get("cinventoryid").toString();//存货标识
						  Double hh=map.get("irownumber")==null?0:Double.valueOf(String.valueOf(map.get("irownumber")));//行号
						  String zgbs=map.get("bestimateflag")==null?"":map.get("bestimateflag").toString();//暂估标识
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
			  System.out.println("++++++++传完");
			  }else{
				  showWarningMessage("单据"+hvo.getVbillcode()+"已发送过\n");
//				  err.append("单据"+hvo.getVbillcode()+"已发送过\n");
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
			Map<String,List> vomap=new HashMap<String,List>();//创建集合存放相同vo
			for(int k=0;k<list1.size();k++){
//				TempGeneralBillVO tgbvo = (TempGeneralBillVO) list1.get(k);//逐条取选中vo查询相同的key
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
				boolean flag=vomap.containsKey(key);//判断存放相同vo的集合中是否已有相同vo
				if(flag){//如果有，取出list中vo后add进list再存进集合
					List list=vomap.get(key);
					list.add(tgbvo);
					vomap.put(key,list);
				}else{//没有相同，创建新list
					List<TempGeneralBillVO> list=new ArrayList<TempGeneralBillVO>();
					list.add(tgbvo);
					vomap.put(key,list);
				}
			}
			if(strs.length()==0){
			for(Entry<String, List> entry:vomap.entrySet()){  //遍历map取出每个key对应的list，每个list为一个汇总
		        System.out.println(entry.getKey()+"--->"+entry.getValue());
		        List<TempGeneralBillVO> list=new ArrayList<TempGeneralBillVO>();
		        TempGeneralBillVO tgbvo=new TempGeneralBillVO();
		        List vlist=vomap.get(entry.getKey());
		        /*int hnum=0;
		        for(int l=0;l<vlist.size();l++){//遍历list中的vo统一传标财
		        	tgbvo=(TempGeneralBillVO) vlist.get(l);
		        	hnum=vlist.size();//获取行号
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
	  for(int l=0;l<vlist.size();l++){//遍历list中的vo统一传标财
      	tgbvo=(TempGeneralBillVO) vlist.get(l);
      	String flag = checkRequired(tgbvo,err);  //非空检验
      	if("Y".equals(flag)){
      	int hnum=vlist.size();//获取明细行数
      	int lsize=l+1;
      
	//计算总金额
	  
//	  for(int i=0;i<hnum;i++){
		  Object  money=tgbvo.getNmoney();
		  UFDouble nmoney = new UFDouble(money.toString());
	      totalSum=totalSum.add(nmoney.doubleValue());
	      System.out.println("money==="+money);
//		}
	//计算总数量
	  
//	  for(int i=0;i<hnum;i++){
		  Object  number=tgbvo.getNnumber();
		  UFDouble nnumber = new UFDouble(number.toString());
		  totalNum=totalNum.add(nnumber.doubleValue());
	      System.out.println("number==="+number);
//		}
	  
	  val.put("BILL_SEQ_ID",tgbvo.getVbillcode());//单据流水号  生成流水号  实际单据号
  	  String bj="";
  	  if(totalSum.doubleValue()>=0){
  		  bj="1";
  	  }else if(totalSum.doubleValue()<0){
  		  bj="0";
  	  }
  	  val.put("OP_FLAG",bj);//操作标记 -- 金额负数红冲正数入账
  	  String nowtime=new UFDateTime(System.currentTimeMillis()).toString();
  	  String zgbz=tgbvo.getBestimateflag().toString();//暂估标志  N非暂估   Y暂估
  	  System.out.println("暂估标志：：：：："+zgbz);
  	  String type = tgbvo.getCbilltypecode();
  	  System.out.println("单据类型：：：：："+type);
  	  String sourcetype=billSource(tgbvo.getCbillid());//来源单据类型 （销售陈本结转-上游）
  	  Map map=invoiceInfo(tgbvo.getCbillid());
  	  String lytype=map.get("cupsourcebilltype")==null?"":map.get("cupsourcebilltype").toString();//来源单据类型
  	  if("I2".equals(type)){     //OK
  		  if("N".equals(zgbz)){
  			  type = "采购入库单";   //I01003
  			  err.append("单据"+tgbvo.getVbillcode()+"为非暂估单据，不能抛标财\n");
  			  return;
  		  }else if("Y".equals(zgbz)){
  			  /*if("45".equals(lytype)){
  				  type = "月末采购暂估匹配";  //I01007  
  			  }else{
  				  type = "月末采购暂估冲回";   //I01011  统一改为I01007  
  			  }*/
  			type = "月末采购暂估匹配";  //I01007 
  		  }
  	  }else if("I9".equals(type) || "I4".equals(type) || "I7".equals(type) || "IA".equals(type)){     //OK
  		type = "库存调整";  //I03001
  	  }
  	  else if("I5".equals(type)){     //OK
  		  type = "销售出库单";    //I02001
  		  if("4C".equals(sourcetype)){
  		  }else if("32".equals(sourcetype)){
  		  }else{
  			  err.append("单据"+tgbvo.getVbillcode()+"来源单据类型不符合传标财规则\n");
			  return;
  		  }
  	  }else if("I6".equals(type)){     //OK
  		  type = "领用出库单";   //I02002
//  		  Lyck(type,billvo,nowtime);
  	  }else if("I3".equals(type)){     //OK
  		  type = "产成品入库单";       //I01001
//  		  Lyck(type,billvo,nowtime);
  	  }else if("ID".equals(type)){     //OK
  		  type = "委托加工出库单";       //I02003
  	  }else if("IC".equals(type)){     //OK
  		  type = "委托加工入库单";       //I01002
  	  }else if("II".equals(type)){
  		  type = "调拨入库单";         //I01004
  	  }else if("IJ".equals(type)){
  		  type = "调拨出库单";         //I02004
  	  }
  	send(tgbvo,val,type,totalSum,totalNum,hnum,err,nowtime,lsize,bvals);
      	}
	  }
	  sendBC(val,tgbvo,err);
	  
  }
  
  private void send(TempGeneralBillVO tgbvo,JSONObject val,String type,UFDouble totalSum,UFDouble totalNum,int rows,StringBuffer err,String nowtime,int lsize,JSONArray bvals)
  {
  	  String billType=DJLX(type);
  	  val.put("BILL_TYPE",billType);//单据类型  billType
      val.put("BILL_ID",tgbvo.getVbillcode());//实际单据号  同头流水号  实际单据号
      val.put("SYS_ID","JC");//来源系统别
      String pkcorp=tgbvo.getPk_corp();
      System.out.println("pkcorp::::::::::::"+pkcorp);
      String zt=queryZt(pkcorp);
      val.put("COMPANY_CODE",zt);//帐套 -- 公司别   
      String cbillid = tgbvo.getCbillid();
      Map imap=invoiceInfo(cbillid);
      String cyear=imap.get("caccountyear")==null?"":imap.get("caccountyear").toString();
//      String cmonth=nowtime.substring(5, 7);
      System.out.println("");
      val.put("ACCOUNT_PERIOD",nowtime.replace("-", "").substring(0, 6));//会计期 -- 业务日期   表头会计年会计月  ???  发票表 ’caccountyear‘字段只有年  抛账时间
      String bm = tgbvo.getCdeptid();
      System.out.println("bm:::::::::::"+bm);
      String zrzx = queryzrzx(bm);
      val.put("COST_CENTER",zrzx==null?"":zrzx);//责任中心 -- 部门表里取mdm传来的数据   ???  mdm8位代码
      val.put("BILL_DATE",nowtime.replace("-", "").replace(":", "").replace(" ", "").substring(0, 8));//单据日期 -- 当前日期
      val.put("CURRENCY_CODE",imap.get("currtypecode")==null?"CNY":imap.get("currtypecode"));//币种代码
//      val.put("CURRENCY_CODE","cny");//币种代码
      if("IA".equals(tgbvo.getCbilltypecode())){   //若单据类型为出库成本调整单，传相反的金额
    	  val.put("BILL_AMT",totalSum.doubleValue()*-1);//单据金额(原币） -- 单据总金额  单据类型为出库成本调整单，传相反的金额
    	  val.put("BILL_AMT_RMB",totalSum.doubleValue()*-1);//单据金额(折人民币）
      }else{
    	  val.put("BILL_AMT",totalSum.doubleValue());//单据金额(原币） -- 单据总金额
    	  val.put("BILL_AMT_RMB",totalSum.doubleValue());//单据金额(折人民币）
      }
      val.put("BILL_TAX_AMT",imap.get("noriginaltaxmny")==null?0:imap.get("noriginaltaxmny"));//单据税额 -- 采购发票本币税额  ???
      val.put("TAX_RATE",imap.get("ntaxrate")==null?0:imap.get("ntaxrate"));//税率
      val.put("CURRENCY_RATE",imap.get("nexchangeotobrate")==null?0:imap.get("nexchangeotobrate"));//汇率 -- 采购发票折本汇率
//      val.put("BILL_AMT_RMB",totalSum.doubleValue());//单据金额(折人民币）
      val.put("QUALITY",totalNum.doubleValue());//数量 -- 表体数量和
      String psnid = tgbvo.getCemployeeid();
      String pname = queryName(psnid);
      String zdr=tgbvo.getCoperatorid();
      Map map=queryPsn(zdr,pkcorp);
      if(map==null || map.size()==0){
//    	  err.append("单据"+tgbvo.getVbillcode()+"未获取到制单人信息，发送失败\n");
//    	  return;
		}
      val.put("OP_CODE","L00337");//单据录入人工号 -- 先传erp的人员  标财数据：L00031  map.get("psncode")==null?"":map.get("psncode")
      val.put("OP_NAME",map.get("psnname")==null?"":map.get("psnname"));//单据录入人姓名-- 先传erp的人员  标财数据：测试
      val.put("OP_TIME",nowtime.replace("-", "").replace(":", "").replace(" ", ""));//单据录入日期 -- 当前时间
      val.put("REC_CREATE_TIME",nowtime.replace("-", "").replace(":", "").replace(" ", "").substring(0, 10));//记录上传时间 -- 当前时间
      Map jlr=queryPsn(PoPublicUIClass.getLoginUser(),pkcorp);
      val.put("REC_CREATOR",jlr.get("psncode")==null?"":jlr.get("psncode"));//记录上传人
      hzdy(billType,val,rows,tgbvo);//不同单据类型不同自定义项
      System.out.println("val:::::::"+val);

      
//      for (int i = 0; i < rows; i++) {
//    	  BillItemVO btvo = (BillItemVO) billvo.getChildrenVO()[i];//获取缓存中表体的各条数据VO
          JSONObject bval = new JSONObject();
          bval.put("BILL_SEQ_ID",tgbvo.getVbillcode());//单据流水号  同头流水号  tgbvo.getVbillcode()
          bval.put("BILL_SUB_SEQ_ID",lsize);//单据明细行号
          bval.put("BILL_TYPE",billType);//单据类型  billType
//          bval.put("BILL_SUB_AMT",btvo.getNmoney()==null?"0":btvo.getNmoney());//单据金额(原币）
          String nmoney=tgbvo.getNmoney().toString();
          Double nm=Double.parseDouble(nmoney);
          String rmbmoney=tgbvo.getNmoney().toString();
          Double rm=Double.parseDouble(rmbmoney);
          if("IA".equals(tgbvo.getCbilltypecode())){   //若单据类型为出库成本调整单，传相反的金额 
        	  bval.put("BILL_SUB_AMT",nm*-1);//单据金额(原币） -- 单据总金额  单据类型为出库成本调整单，传相反的金额   nm
        	  bval.put("BILL_SUB_AMT_RMB",rm*-1);//单据金额(折人民币）    rm
          }else{
        	  bval.put("BILL_SUB_AMT",nm);//单据金额(原币） -- 单据总金额    nm
        	  bval.put("BILL_SUB_AMT_RMB",rm);//单据金额(折人民币）    rm
          }
          bval.put("BILL_SUB_TAX_AMT",imap.get("noriginaltaxmny")==null?0:imap.get("noriginaltaxmny"));//单据税额
          bval.put("TAX_RATE",imap.get("ntaxrate")==null?0:imap.get("ntaxrate"));//税率
          bval.put("CURRENCY_RATE",imap.get("nexchangeotobrate")==null?0:imap.get("nexchangeotobrate"));//汇率
//          bval.put("BILL_SUB_AMT_RMB",btvo.getNmoney()==null?"0":btvo.getNmoney());//单据金额(折人民币）
          bval.put("SUB_QUALITY",tgbvo.getNnumber());//数量
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
	              //成功逻辑 
	        	  System.out.println("########发送成功########");
//	        	  showWarningMessage("单据"+hvo.getVbillcode()+"发送成功");
	        	  err.append("单据"+tgbvo.getVbillcode()+"发送中\n");
//	        	  showWarningMessage("单据"+tgbvo.getVbillcode()+"发送中\n");
	        	  updateDJBH(tgbvo.getVbillcode(),tgbvo.getPk_corp());
//	          }else{ 
//	              //失败逻辑 
//	          }
	      } 
	  }else{
//		  showWarningMessage("单据"+hvo.getVbillcode()+"发送失败");
		  err.append("单据"+tgbvo.getVbillcode()+"发送失败\n");
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
	  if(result){//判断当前公司是否为国内公司，否则不执行
		  int rows = 0;//选择行数
		  int [] selectRows = getBillListPanel().getHeadTable().getSelectedRows();//获取列表状态下选中所有行序号
		  rows=selectRows.length;
		  //获取表体行数
//		  int brows=this.getBillCardPanel().getRowCount();//卡片状态
		  
		  StringBuffer err=new StringBuffer();//存储提示信息
		  
		  for(int a=0;a<selectRows.length;a++)
		  {
			  BillVO billvo = null;
			  m_iCurBillPrt = selectRows[a];//把当前选中行的序号赋给当前单据在缓存中的序号
			  billvo = m_voBills[m_iCurBillPrt];//当前正在处理的单据
			  String billCode = ((BillHeaderVO) billvo.getParentVO()).getVbillcode();//单据号
			  if (billvo.getChildrenVO() == null) {
			        //还没有获得表体数据
			        ClientLink cl = ce.getClientLink();
			        try {
			        	billvo = BillBO_Client.querybillWithOtherTable(null, null,
						      null, null, null, billvo, new Boolean(true), cl)[0];
					} catch (Exception e) {
						e.printStackTrace();
					}
			        }
			  
			  int brows = billvo.getChildrenVO().length;
			//计算总金额
			  UFDouble totalSum = new UFDouble(0);
			  for(int i=0;i<brows;i++){
				  Object  money=((BillItemVO) billvo.getChildrenVO()[i]).getNmoney()==null?"0":((BillItemVO) billvo.getChildrenVO()[i]).getNmoney();
				  UFDouble nmoney = new UFDouble(money.toString());
			      totalSum=totalSum.add(nmoney.doubleValue());
			      System.out.println("money==="+money);
				}
			  
			//计算总数量
			  UFDouble totalNum = new UFDouble(0);
			  for(int i=0;i<brows;i++){
				  Object  number=((BillItemVO) billvo.getChildrenVO()[i]).getNnumber()==null?"0":((BillItemVO) billvo.getChildrenVO()[i]).getNnumber();
				  UFDouble nnumber = new UFDouble(number.toString());
				  totalNum=totalNum.add(nnumber.doubleValue());
			      System.out.println("number==="+number);
				}
			  BillHeaderVO hvo=(BillHeaderVO) billvo.getParentVO();//表头VO
			  String yn=judgeDJBH(hvo.getVbillcode(),corp);
			  if(yn==null){
			//遍历
			  for(int b=0;b<rows;b++)
			  {
//				  BillItemVO btvo = (BillItemVO) billvo.getChildrenVO()[b];//获取缓存中表体的各条数据VO
				      String flag = null;
//					  flag = checkRequired(hvo,err);  //非空检验
					  if("Y".equals(flag)){
						//XBUS SEND 调用代码  start 
						  JSONObject val = new JSONObject();
						  	  
						  	  val.put("BILL_SEQ_ID",hvo.getVbillcode()==null?"":hvo.getVbillcode());//单据流水号  生成流水号  实际单据号
						  	  String bj="";
						  	  if(totalSum.doubleValue()>=0){
						  		  bj="1";
						  	  }else if(totalSum.doubleValue()<0){
						  		  bj="0";
						  	  }
						  	  val.put("OP_FLAG",bj);//操作标记 -- 金额负数红冲正数入账
						  	  String nowtime=new UFDateTime(System.currentTimeMillis()).toString();
						      System.out.println("当前时间：：：：：：：："+nowtime);
						  	  String zgbz=hvo.getBestimateflag().toString();//暂估标志  N非暂估   Y暂估
						  	  System.out.println("暂估标志：：：：："+zgbz);
//						  	  String lytype=btvo.getCsourcebilltypecode();//来源单据类型
//						  	  System.out.println("来源单据类型：：：：："+lytype);
						  	  String type = hvo.getCbilltypecode();
						  	  System.out.println("单据类型：：：：："+type);
						  	  System.out.println("cbillid:::::::"+hvo.getCbillid());
						  	  String sourcetype=billSource(hvo.getCbillid());//来源单据类型 （销售陈本结转-上游）
						  	  Map map=invoiceInfo(hvo.getCbillid());
						  	  String lytype=map.get("cupsourcebilltype")==null?"":map.get("cupsourcebilltype").toString();//来源单据类型
						  	  if("I2".equals(type)){     //OK
						  		  if("N".equals(zgbz)){
						  			  type = "采购入库单";   //I01003
						  			  cgrk(val,hvo,type,totalSum,totalNum,billvo,brows,err,nowtime);
						  		  }else if("Y".equals(zgbz)){
						  			  if("45".equals(lytype)){
						  				  type = "月末采购暂估匹配";  //I01007  
								  		  cgrk(val,hvo,type,totalSum,totalNum,billvo,brows,err,nowtime);
						  			  }else{
						  				  type = "月末采购暂估冲回";   //I01011
							  			  cgrk(val,hvo,type,totalSum,totalNum,billvo,brows,err,nowtime);
						  			  }
						  		  }
						  	  }else if("I9".equals(type) || "I4".equals(type) || "I7".equals(type) || "IA".equals(type)){     //OK
						  		type = "库存调整";  //I03001
						  		cgrk(val,hvo,type,totalSum,totalNum,billvo,brows,err,nowtime);
						  	  }
						  	  else if("I5".equals(type)){     //OK
						  		  type = "销售出库单";    //I02001
						  		  if("4C".equals(sourcetype)){
						  			cgrk(val,hvo,type,totalSum,totalNum,billvo,brows,err,nowtime);
						  		  }else if("32".equals(sourcetype)){
						  			cgrk(val,hvo,type,totalSum,totalNum,billvo,brows,err,nowtime);
						  		  }else{
						  			  err.append("单据"+hvo.getVbillcode()+"来源单据类型不符合传标财规则\n");
						  		  }
						  	  }else if("I6".equals(type)){     //OK
						  		  judge(billvo);
						  		  type = "领用出库单";   //I02002
//						  		  Lyck(type,billvo,nowtime);
						  		  cgrk(val,hvo,type,totalSum,totalNum,billvo,brows,err,nowtime);
						  	  }else if("I3".equals(type)){     //OK
						  		  type = "产成品入库单";       //I01001
//						  		  Lyck(type,billvo,nowtime);
						  		  cgrk(val,hvo,type,totalSum,totalNum,billvo,brows,err,nowtime);
						  	  }else if("ID".equals(type)){     //OK
						  		  type = "委托加工出库单";       //I02003
						  		  cgrk(val,hvo,type,totalSum,totalNum,billvo,brows,err,nowtime);
						  	  }else if("IC".equals(type)){     //OK
						  		  type = "委托加工入库单";       //I01002
						  		  cgrk(val,hvo,type,totalSum,totalNum,billvo,brows,err,nowtime);
						  	  }else if("II".equals(type)){
						  		  type = "调拨入库单";         //I01004
						  		  cgrk(val,hvo,type,totalSum,totalNum,billvo,brows,err,nowtime);
						  	  }else if("IJ".equals(type)){
						  		  type = "调拨出库单";         //I02004
						  		  cgrk(val,hvo,type,totalSum,totalNum,billvo,brows,err,nowtime);
						  	  }
					  }
					
				  }
			  }else{
//				  showWarningMessage(hvo.getVbillcode()+"已发送过！");
				  err.append("单据"+hvo.getVbillcode()+"已发送过\n");
			  }
		  }
		  if(err!=null || err.length()>0){
				showWarningMessage(err.toString());
			}
		  }

  }
  
  /**
   * 根据条件判断汇总单据
   */
  private BillVO judge(BillVO billvo){
	  
	  BillHeaderVO hvo=(BillHeaderVO) billvo.getParentVO();
	  //获取存货分类、部门、收发类别、供应商、客户信息
	  List list=query(hvo.getVbillcode());
	  String ch=list.get(0).toString(); //存货分类
	  String bm=list.get(1).toString();//部门
	  String sf=list.get(2).toString();//收发类别
	  String gy=list.get(3).toString();//供应商
	  
	  
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
   * 类型为采购入库单、入库调整单传标财
   * add by zy 2019-10-31 
   */
  private void cgrk(JSONObject val,BillHeaderVO hvo,String type,UFDouble totalSum,UFDouble totalNum,BillVO billvo,int rows,StringBuffer err,String nowtime){
	  //当前时间
  	  String billType=DJLX(type);
  	  val.put("BILL_TYPE",billType);//单据类型  
      val.put("BILL_ID",hvo.getVbillcode()==null?"":hvo.getVbillcode());//实际单据号  同头流水号  实际单据号
      val.put("SYS_ID","JC");//来源系统别
      String pkcorp=hvo.getPk_corp()==null?"":hvo.getPk_corp();
      System.out.println("pkcorp::::::::::::"+pkcorp);
      String zt=queryZt(pkcorp);
      val.put("COMPANY_CODE",zt);//帐套 -- 公司别   
      String cbillid = hvo.getCbillid();
      Map imap=invoiceInfo(cbillid);
      String cyear=imap.get("caccountyear")==null?"":imap.get("caccountyear").toString();
//      String cmonth=nowtime.substring(5, 7);
      System.out.println("");
      val.put("ACCOUNT_PERIOD",nowtime.replace("-", "").substring(0, 6));//会计期 -- 业务日期   表头会计年会计月  ???  发票表 ’caccountyear‘字段只有年  抛账时间
      String bm = hvo.getCdeptid();
      System.out.println("bm:::::::::::"+bm);
      String zrzx = queryzrzx(bm);
      val.put("COST_CENTER",zrzx==null?"":zrzx);//责任中心 -- 部门表里取mdm传来的数据   ???  mdm8位代码
      val.put("BILL_DATE",nowtime.replace("-", "").replace(":", "").replace(" ", "").substring(0, 8));//单据日期 -- 当前日期
      val.put("CURRENCY_CODE",imap.get("currtypecode")==null?"CNY":imap.get("currtypecode"));//币种代码
//      val.put("CURRENCY_CODE","cny");//币种代码
      if("IA".equals(hvo.getCbilltypecode())){   //若单据类型为出库成本调整单，传相反的金额
    	  val.put("BILL_AMT",totalSum.doubleValue()*-1);//单据金额(原币） -- 单据总金额  单据类型为出库成本调整单，传相反的金额
    	  val.put("BILL_AMT_RMB",totalSum.doubleValue()*-1);//单据金额(折人民币）
      }else{
    	  val.put("BILL_AMT",totalSum.doubleValue());//单据金额(原币） -- 单据总金额
    	  val.put("BILL_AMT_RMB",totalSum.doubleValue());//单据金额(折人民币）
      }
      val.put("BILL_TAX_AMT",imap.get("noriginaltaxmny")==null?0:imap.get("noriginaltaxmny"));//单据税额 -- 采购发票本币税额  ???
      val.put("TAX_RATE",imap.get("ntaxrate")==null?0:imap.get("ntaxrate"));//税率
      val.put("CURRENCY_RATE",imap.get("nexchangeotobrate")==null?0:imap.get("nexchangeotobrate"));//汇率 -- 采购发票折本汇率
//      val.put("BILL_AMT_RMB",totalSum.doubleValue());//单据金额(折人民币）
      val.put("QUALITY",totalNum.doubleValue());//数量 -- 表体数量和
      String psnid = hvo.getCemployeeid();
      String pname = queryName(psnid);
      String zdr=hvo.getCoperatorid();
      Map map=queryPsn(zdr,pkcorp);
      if(map==null || map.size()==0){
    	  err.append("单据"+hvo.getVbillcode()+"未获取到制单人信息，发送失败\n");
    	  return;
		}
      val.put("OP_CODE",map.get("psncode")==null?"":map.get("psncode"));//单据录入人工号 -- 先传erp的人员  标财数据：L00031  
      val.put("OP_NAME",map.get("psnname")==null?"":map.get("psnname"));//单据录入人姓名-- 先传erp的人员  标财数据：测试
      val.put("OP_TIME",nowtime.replace("-", "").replace(":", "").replace(" ", ""));//单据录入日期 -- 当前时间
      val.put("REC_CREATE_TIME",nowtime.replace("-", "").replace(":", "").replace(" ", "").substring(0, 10));//记录上传时间 -- 当前时间
      Map jlr=queryPsn(PoPublicUIClass.getLoginUser(),pkcorp);
      val.put("REC_CREATOR",jlr.get("psncode")==null?"":jlr.get("psncode"));//记录上传人
//      hzdy(billType,val,rows);//不同单据类型不同自定义项
      System.out.println("val:::::::"+val);

      JSONArray bvals = new JSONArray();
      for (int i = 0; i < rows; i++) {
    	  BillItemVO btvo = (BillItemVO) billvo.getChildrenVO()[i];//获取缓存中表体的各条数据VO
          JSONObject bval = new JSONObject();
          bval.put("BILL_SEQ_ID",hvo.getVbillcode()==null?"":hvo.getVbillcode());//单据流水号  同头流水号
          bval.put("BILL_SUB_SEQ_ID",i+1);//单据明细行号
          bval.put("BILL_TYPE",billType);//单据类型
//          bval.put("BILL_SUB_AMT",btvo.getNmoney()==null?"0":btvo.getNmoney());//单据金额(原币）
          String nmoney=btvo.getNmoney()==null?"0":btvo.getNmoney().toString();
          Double nm=Double.parseDouble(nmoney);
          String rmbmoney=btvo.getNmoney()==null?"0":btvo.getNmoney().toString();
          Double rm=Double.parseDouble(rmbmoney);
          if("IA".equals(hvo.getCbilltypecode())){   //若单据类型为出库成本调整单，传相反的金额
        	  bval.put("BILL_SUB_AMT",nm*-1);//单据金额(原币） -- 单据总金额  单据类型为出库成本调整单，传相反的金额
        	  bval.put("BILL_SUB_AMT_RMB",rm*-1);//单据金额(折人民币）
          }else{
        	  bval.put("BILL_SUB_AMT",nm);//单据金额(原币） -- 单据总金额
        	  bval.put("BILL_SUB_AMT_RMB",rm);//单据金额(折人民币）
          }
          bval.put("BILL_SUB_TAX_AMT",imap.get("noriginaltaxmny")==null?0:imap.get("noriginaltaxmny"));//单据税额
          bval.put("TAX_RATE",imap.get("ntaxrate")==null?0:imap.get("ntaxrate"));//税率
          bval.put("CURRENCY_RATE",imap.get("nexchangeotobrate")==null?0:imap.get("nexchangeotobrate"));//汇率
//          bval.put("BILL_SUB_AMT_RMB",btvo.getNmoney()==null?"0":btvo.getNmoney());//单据金额(折人民币）
          bval.put("SUB_QUALITY",btvo.getNnumber()==null?"0":btvo.getNnumber());//数量
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
              //成功逻辑 
        	  System.out.println("########发送成功########");
//        	  showWarningMessage("单据"+hvo.getVbillcode()+"发送成功");
        	  err.append("单据"+hvo.getVbillcode()+"发送成功\n");
        	  updateDJBH(hvo.getVbillcode(),pkcorp);
//          }else{ 
//              //失败逻辑 
//          }
      } 
  }else{
//	  showWarningMessage("单据"+hvo.getVbillcode()+"发送失败");
	  err.append("单据"+hvo.getVbillcode()+"发送失败\n");
  }
  }
//end
  
  /**
   * 类型为销售成本结转传标财
   * add by zy 2019-10-31 
   */
  private void xscbjz(JSONObject val,BillHeaderVO hvo,BillItemVO btvo,String type,UFDouble totalSum,UFDouble totalNum,int rows,StringBuffer err,String nowtime){
	  //当前时间
      
  	  String billType=DJLX(type);
  	  val.put("BILL_TYPE",billType);//单据类型  
      val.put("BILL_ID",hvo.getVbillcode()==null?"":hvo.getVbillcode());//实际单据号  同头流水号  实际单据号
      val.put("SYS_ID","JC");//来源系统别
      String pkcorp=hvo.getPk_corp()==null?"":hvo.getPk_corp();
      System.out.println("pkcorp::::::::::::"+pkcorp);
      String zt=queryZt(pkcorp);
      val.put("COMPANY_CODE",zt);//帐套 -- 公司别   
      String cbillid = hvo.getCbillid();
      Map imap=invoiceInfo(cbillid);
      String cyear=imap.get("caccountyear")==null?"":imap.get("caccountyear").toString();
//      String cmonth=nowtime.substring(5, 7);
//      String cdate=cyear+cmonth;
      val.put("ACCOUNT_PERIOD",nowtime.replace("-", "").substring(0, 6));//会计期 -- 业务日期   表头会计年会计月  ???  发票表 ’caccountyear‘字段只有年  抛账时间
      String bm = hvo.getCdeptid();
      System.out.println("bm:::::::::::"+bm);
      String zrzx = queryzrzx(bm);
      val.put("COST_CENTER",zrzx==null?"":zrzx);//责任中心 -- 部门表里取mdm传来的数据   ???  mdm8位代码
      val.put("BILL_DATE",nowtime.replace("-", "").replace(":", "").replace(" ", "").substring(0, 8));//单据日期 -- 当前日期
      val.put("CURRENCY_CODE",imap.get("currtypecode"));//币种代码
      val.put("BILL_AMT",totalSum.doubleValue());//单据金额(原币） -- 单据总金额
      val.put("BILL_TAX_AMT",imap.get("noriginaltaxmny"));//单据税额 -- 采购发票本币税额  ???
      val.put("TAX_RATE",imap.get("ntaxrate"));//税率
      val.put("CURRENCY_RATE",imap.get("nexchangeotobrate"));//汇率 -- 采购发票折本汇率
      val.put("BILL_AMT_RMB",totalSum.doubleValue());//单据金额(折人民币）
      val.put("QUALITY",totalNum.doubleValue());//数量 -- 表体数量和
      String psnid = hvo.getCemployeeid();
      String pname = queryName(psnid);
      val.put("OP_CODE","L00031");//单据录入人工号 -- 先传erp的人员  标财数据：L00031  
      val.put("OP_NAME","测试");//单据录入人姓名-- 先传erp的人员  标财数据：测试
      val.put("OP_TIME",nowtime.replace("-", "").replace(":", "").replace(" ", ""));//单据录入日期 -- 当前时间
      val.put("REC_CREATE_TIME",nowtime.replace("-", "").replace(":", "").replace(" ", "").substring(0, 10));//记录上传时间 -- 当前时间
      val.put("REC_CREATOR","L00031");//记录上传人
//      hzdy(billType,val,rows);//不同单据类型不同自定义项

      JSONArray bvals = new JSONArray();
      for (int i = 0; i < rows; i++) {
          JSONObject bval = new JSONObject();
          bval.put("BILL_SEQ_ID",hvo.getVbillcode()==null?"":hvo.getVbillcode());//单据流水号  同头流水号
          bval.put("BILL_SUB_SEQ_ID",i+1);//单据明细行号
          bval.put("BILL_TYPE",billType);//单据类型
          bval.put("BILL_SUB_AMT",btvo.getNmoney()==null?"0":btvo.getNmoney());//单据金额(原币）
          bval.put("BILL_SUB_TAX_AMT",imap.get("noriginaltaxmny"));//单据税额
          bval.put("TAX_RATE",imap.get("ntaxrate"));//税率
          bval.put("CURRENCY_RATE",imap.get("nexchangeotobrate"));//汇率
          bval.put("BILL_SUB_AMT_RMB",btvo.getNmoney()==null?"0":btvo.getNmoney());//单据金额(折人民币）
          bval.put("SUB_QUALITY",btvo.getNnumber()==null?"0":btvo.getNnumber());//数量
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
              //成功逻辑 
        	  System.out.println("########发送成功########");
//        	  showWarningMessage("单据"+hvo.getVbillcode()+"发送成功");
        	  err.append("单据"+hvo.getVbillcode()+"发送成功\n");
        	  updateDJBH(hvo.getVbillcode(),pkcorp);
//          }else{ 
//              //失败逻辑 
//          }
      } 
  }else{
//	  showWarningMessage("单据"+hvo.getVbillcode()+"发送失败");
	  err.append("单据"+hvo.getVbillcode()+"发送失败\n");
  }
  }
//end
  
  /*public void Lyck(String type,BillVO billvo,String nowtime){ 
		
	  	StringBuffer err = new StringBuffer();
	  	int  rows = billvo.getChildrenVO().length;
	  	UFDouble totalSum = new UFDouble(0);
	  	 //计算总金额		
		  for(int i=0;i<rows;i++){
			  Object  money=((BillItemVO) billvo.getChildrenVO()[i]).getNmoney()==null?"0":((BillItemVO) billvo.getChildrenVO()[i]).getNmoney();
			  UFDouble nmoney = new UFDouble(money.toString());
		      totalSum=totalSum.add(nmoney.doubleValue());
		      System.out.println("money==="+money);
			}
		//计算总数量
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
	    val.put("BILL_SEQ_ID",hvo.getVbillcode()==null?"":hvo.getVbillcode());//单据流水号
	    val.put("OP_FLAG",bj);//操作标记
	    val.put("BILL_TYPE",billType);//单据类型
	    val.put("BILL_ID",hvo.getVbillcode()==null?"":hvo.getVbillcode());//实际单据号
	    val.put("SYS_ID","JC");//来源系统别
	    val.put("COMPANY_CODE",zt);//帐套
	    val.put("ACCOUNT_PERIOD",hvo.getTmaketime().toString().replace("-", "").substring(0, 6));//会计期
	    val.put("COST_CENTER",zrzx);//责任中心
	    val.put("BILL_DATE",hvo.getTmaketime().toString().replace("-", "").substring(0, 8));//单据日期
	    val.put("CURRENCY_CODE","CNY");//币种代码
	    val.put("BILL_AMT",String.format("%.6f",Double.valueOf(totalSum.toString())));//单据金额(原币）
	    val.put("BILL_TAX_AMT",0.0);//单据税额
	    val.put("TAX_RATE",0.0);//税率
	    val.put("CURRENCY_RATE",0.0);//汇率
	    val.put("BILL_AMT_RMB",String.format("%.6f",Double.valueOf(totalSum.toString())));//单据金额(折人民币）
	    val.put("QUALITY",String.format("%.6f",Double.valueOf(totalNum.toString())));//数量
	    val.put("OP_CODE","L00031");//单据录入人工号
	    val.put("OP_NAME","测试");//单据录入人姓名
	    val.put("OP_TIME",nowtime.replace("-", "").replace(":", "").replace(" ", ""));//单据录入日期
	    val.put("REC_CREATE_TIME",nowtime.replace("-", "").replace(":", "").replace(" ", "").substring(0, 10));//记录上传时间
	    val.put("REC_CREATOR","L00031");//记录上传人
	    hzdy(billType,val,rows);//不同单据类型不同自定义项
	    JSONArray bvals = new JSONArray();
	    for (int i = 0; i < rows; i++) {
	    	 BillItemVO btvo = (BillItemVO) billvo.getChildrenVO()[i];
	    	JSONObject val1 = new JSONObject();
	        val1.put("BILL_SEQ_ID",hvo.getVbillcode()==null?"":hvo.getVbillcode());//单据流水号
	        val1.put("BILL_SUB_SEQ_ID",i+1);//单据明细行号
	        val1.put("BILL_TYPE",billType);//单据类型
	        val1.put("BILL_SUB_AMT",btvo.getNmoney()==null?"0":btvo.getNmoney());//单据金额(原币）
	        val1.put("BILL_SUB_TAX_AMT",0.0);//单据税额
	        val1.put("TAX_RATE",0.0);//税率
	        val1.put("CURRENCY_RATE",0.0);//汇率
	        val1.put("BILL_SUB_AMT_RMB",btvo.getNmoney()==null?"0":btvo.getNmoney());//单据金额(折人民币）
	        val1.put("SUB_QUALITY",btvo.getNnumber()==null?"0":btvo.getNnumber());//数量
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
	                  //成功逻辑 
	            	  System.out.println("########发送成功########");

	            	  err.append("单据"+hvo.getVbillcode()+"发送成功\n");
//	            	  updateDJBH(hvo.getVbillcode());

		      } 
		  }else{
			  err.append("单据"+hvo.getVbillcode()+"发送失败\n");
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
	    		String temp_pk_corp = (String)bvo.getParentVO().getAttributeValue("pk_corp");//公司
				String temp_cfirstbillbid = null;
				String temp_cinventoryid = null;
	    		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	    		CircularlyAccessibleValueObject[] cvos = bvo.getChildrenVO();
		    	if(cvos != null && cvos.length > 0){
		    		for (int i = 0; i < cvos.length; i++) {
		    			String pk_cbill_bid = (String)cvos[i].getAttributeValue("cbill_bid");//销售成本结转表体主键
						String temp_dh = (String) cvos[i].getAttributeValue("vfree1");//销售成本结转跺号
						UFDouble temp_num = (UFDouble)cvos[i].getAttributeValue("nnumber");//销售成本结转数量
						temp_cinventoryid = (String)cvos[i].getAttributeValue("cinventoryid");//存货主键
						temp_cfirstbillbid = (String)cvos[i].getAttributeValue("csourcebillid");//源头单据表体ID、
						//add by yj 2014-04-04 23:16:17
						if(temp_num.toDouble()>0){//数量大于零，取采购单货单单价，并计算金额
							//根据跺号-》取到货单的单价
							// add by zip: 2014/5/1
							BigDecimal temp_nprice;
							// 先取采购发票单价
							String sql = "select noriginalcurprice from po_invoice_b where nvl(dr,0)=0 and vfree1='"+temp_dh+"' and cmangid='"+temp_cinventoryid+"' and pk_corp='"+temp_pk_corp+"' and ccurrencytypeid='00010000000000000001'";
							Object obj1 = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
							if(obj1 == null) {
								sql = "select nprice from po_arriveorder_b where vfree1='"+temp_dh+"' and cmangid='"+temp_cinventoryid+"' and nvl(dr,0) = 0 and pk_corp='"+temp_pk_corp+"' and ccurrencytypeid='00010000000000000001' ";
								temp_nprice = (BigDecimal)iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
							}else {
								temp_nprice = (BigDecimal) obj1;
							}
							//计算销售成本结转单的金额
							if(temp_nprice==null){
								Integer rowno = i + 1;
								MessageDialog.showErrorDlg(this, "错误","代加工采购入库第"+rowno+"行没有入库信息，请人工指定单价");
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
						}else if(temp_num.toDouble()<0){//数量小于零，取销售出库单单价，金额
							//根据销售成本结转与销售出库单子的关联关系，取出销售出库的单价、金额。带入到对应的销售成本结转单中
							//根据跺号-取销售出库单价
							String sql = "select nprice from ic_general_b where vfree1='"+temp_dh+"' and cinventoryid='"+temp_cinventoryid+"' and nvl(dr,0) = 0  and noutnum>0 and pk_corp='"+temp_pk_corp+"' and cfirsttype='30' ";
							BigDecimal temp_nprice = (BigDecimal)iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
							if(temp_nprice==null){
								Integer rowno = i + 1;
								MessageDialog.showErrorDlg(this, "错误","代加工销售出库第"+rowno+"行没有销售出库信息,请人工指定单价");
								return;
							}
							else{
								//计算销售成本结转单的金额
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
		    	//非代加工业务流程的的单子，并且数量小于零的，取出销售出库单的单价，金额（数量小于零  == 退货单子）
	    	}else if(!bvo.getParentVO().getAttributeValue("cbiztypeid").equals("1016A210000000098ZO3")){
	    		String temp_pk_corp = (String)bvo.getParentVO().getAttributeValue("pk_corp");//公司
				String temp_cfirstbillbid = null;
				String temp_cinventoryid = null;
	    		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	    		CircularlyAccessibleValueObject[] cvos = bvo.getChildrenVO();
	    		if(cvos != null && cvos.length > 0){
	    			for (int i = 0; i < cvos.length; i++) {
	    				String pk_cbill_bid = (String)cvos[i].getAttributeValue("cbill_bid");//销售成本结转表体主键
						String temp_dh = (String) cvos[i].getAttributeValue("vfree1");//销售成本结转跺号
						UFDouble temp_num = (UFDouble)cvos[i].getAttributeValue("nnumber");//销售成本结转数量
						temp_cinventoryid = (String)cvos[i].getAttributeValue("cinventoryid");//存货主键
						temp_cfirstbillbid = (String)cvos[i].getAttributeValue("csourcebillid");//源头单据表体ID、
						if(temp_num.toDouble()<0){
							String sql = "select nprice from ic_general_b where vfree1='"+temp_dh+"' and cinventoryid='"+temp_cinventoryid+"' and nvl(dr,0) = 0  and noutnum>0 and pk_corp='"+temp_pk_corp+"' and cfirsttype='30' ";
							BigDecimal temp_nprice = (BigDecimal)iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
							if(temp_nprice==null){
								Integer rowno = i + 1;
								MessageDialog.showErrorDlg(this, "错误","代加工销售出库第"+rowno+"行没有销售出库信息,请人工指定单价");
								return;
							}else{
								//计算销售成本结转单的金额
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
   * 菜单项选择。 创建日期：(01-2-23 15:03:07)
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
   * 函数功能:设置显示信息 <p/> 参数: <p/> 返回值: <p/> 异常:
   */
  public void postInit() {
    if (m_sBillType == null || m_bd == null)
      return;

    // 设置标题
    if (m_sBillType.equals(ConstVO.m_sBillQCRKD) == false
        && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD) == false) {
      // 不是期初单据
      m_sTitle = m_bd.getTitle();
      if (m_sTitle != null && m_sTitle.trim().length() != 0) {
        setTitleText(m_sTitle);
      }
    }
    if (m_bIsSOStart && m_sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000123")/* @res "销售管理系统已经启用，存货核算不能增加销售成本结转单" */);
    }
    else if (m_bIsPOStart && m_sBillType.equals(ConstVO.m_sBillCGRKD)) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000124")/* @res "采购管理系统已经启用，存货核算不能增加采购入库单" */);
    }
    else if (m_bIsSCStart && m_sBillType.equals(ConstVO.m_sBillWWJGSHD)) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000125")/* @res "委外加工系统已经启用，存货核算不能增加委外加工收货单" */);
    }
    else if (m_bIsICStart && m_sBillType.equals(ConstVO.m_sBillQCRKD) == false
        && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD) == false) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000126")/* @res "库存管理系统已经启用，存货核算只能增加调整单和假退料单据" */);
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
   * 函数功能:单据来源选择对话框触发 <p/> 参数: java.awt.event.ItemEvent itemEvent ----- 事件 <p/>
   * 返回值: <p/> 异常:
   */
  public void uIComboBoxSource_ItemStateChanged(
      java.awt.event.ItemEvent itemEvent) {
    if (m_bIsChangeEvent && m_iStatus == ADD_STATUS) {
      // 选择了单据来源,使选择出库单按钮可用
      if (itemEvent.getStateChange() == ItemEvent.DESELECTED) {
        return;
      }
      if (itemEvent.getItem().toString().equals(
          this.m_ComboItemsVO.name_salebill)) {
        // 是销售发票
        // 判断业务类型
        BillItem bt = getBillCardPanel().getHeadItem("cbiztypeid");
        boolean bIsFQ = false; // 是否是分期收款
        boolean bIsWT = false; // 是否是委托代销
        if (bt != null) {
          String sBizTypeID = bt.getValue();
          if (sBizTypeID != null && sBizTypeID.trim().length() != 0) {
            UIRefPane pane = (UIRefPane) bt.getComponent();
            Object oRule = pane.getRef().getRefModel().getValue("verifyrule");
            if (oRule != null) {
              if (oRule.toString().equals("W")) {
                // 是委托代销
                bIsWT = true;
              }
              else if (oRule.toString().equals("F")) {
                // 是分期收款
                bIsFQ = true;
              }
            }
          }
        }
        if (bIsFQ) {
          showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000127")/*
                                                 * @res
                                                 * "当前单据来源是销售发票，业务类型是分期收款，请在辅助菜单中选择来源是出库单的对应销售成本结转单"
                                                 */);
          // 不能增行，粘贴行，插入行
          btnCtrl.set(false,IABtnConst.BTN_LINE_ADD);
          btnCtrl.set(false,IABtnConst.BTN_LINE_PASTE);
          btnCtrl.set(false,IABtnConst.BTN_LINE_INSERT);
          btnCtrl.set(true,IABtnConst.BTN_CHOOSESALEBILL);
        }
        else if (bIsWT) {
          showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000128")/*
                                                 * @res
                                                 * "当前单据来源是销售发票，业务类型是委托代销，请在辅助菜单中选择来源是出库单的对应销售成本结转单"
                                                 */);
          // 不能增行，粘贴行，插入行
          btnCtrl.set(false,IABtnConst.BTN_LINE_ADD);
          btnCtrl.set(false,IABtnConst.BTN_LINE_PASTE);
          btnCtrl.set(false,IABtnConst.BTN_LINE_INSERT);
          btnCtrl.set(true,IABtnConst.BTN_CHOOSESALEBILL);
        }
        else {
          btnCtrl.set(false,IABtnConst.BTN_CHOOSESALEBILL);
          // 可以增行，粘贴行，插入行
          btnCtrl.set(true,IABtnConst.BTN_LINE_ADD);
          btnCtrl.set(true,IABtnConst.BTN_LINE_PASTE);
          btnCtrl.set(true,IABtnConst.BTN_LINE_INSERT);
        }
      }
      else {
        btnCtrl.set(false,IABtnConst.BTN_CHOOSESALEBILL);
        // 可以增行，粘贴行，插入行
        btnCtrl.set(true,IABtnConst.BTN_LINE_ADD);
        btnCtrl.set(true,IABtnConst.BTN_LINE_PASTE);
        btnCtrl.set(true,IABtnConst.BTN_LINE_INSERT);
      }
      // 库存组织、仓库、业务类型、收发类别、部门、业务员、经手人、客户不能修改
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
      // 不能修改部分表体数据
      BillItem[] btItemDatas = getBillCardPanel().getBodyItems();
      for (int i = 0; i < btItemDatas.length; i++) {
        if (getBillCardPanel().getBodyItem(btItemDatas[i].getKey()).isEdit()) {
          getBillCardPanel().getBodyItem(btItemDatas[i].getKey()).setEnabled(
              true);
        }
      }
      for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
        // 设置csaleadviceid
        getBillCardPanel().getBillModel().setValueAt(null, i, "csaleadviceid");
        // 设置ccsaleadviceitemid
        getBillCardPanel().getBillModel().setValueAt(null, i,
            "ccsaleadviceitemid");
      }
      setBtnsForBilltypes(m_aryButtonGroupCard);
    }
    return;
  }

  /**
   * 函数功能:处理存货多选问题 <p/> 参数: ValueChangedEvent ----- 事件 <p/> 返回值: <p/> 异常:
   */
  public void valueChanged(ValueChangedEvent event) {
    try {
      Object o = event.getSource();
      String sName = ((UIRefPane) o).getName();
      int iRow = getBillCardPanel().getBillTable().getEditingRow();
      if (sName.equals("cinventorycode")) {
        getBillCardPanel().getBillTable().editingStopped(
            new javax.swing.event.ChangeEvent(this));
        // 获得主键
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
        Hashtable ht = new Hashtable(); // 记录存货与计量信息
        if (sPKs != null && sPKs.length > 0) {
          String sAstUnitFieldName = "";
          if (m_sBillType.equals(ConstVO.m_sBillCGRKD)
              || m_sBillType.equals(ConstVO.m_sBillWWJGSHD)) {
            // 采购入库单、委外加工收货单取采购辅计量单位
            sAstUnitFieldName = "pk_measdoc2";
          }
          else if (m_sBillType.equals(ConstVO.m_sBillXSCBJZD)
              || m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
            // 销售成本结转单取销售辅计量单位
            sAstUnitFieldName = "pk_measdoc1";
          }
          else {
            // 其它取库存辅计量单位
            sAstUnitFieldName = "pk_measdoc3";
          }
          String sInvs = "('";
          for (int i = sPKs.length - 1; i >= 0; i--) {
            oInvValues[0] = sPKs[i];
            oInvValues[8] = oInvAss[i];
            // 判断此存货是否是辅计量管理
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
            // 处理辅计量数量
            // 获得辅计量单位
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
              // 增行
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
              // 计划价的存货只从物料生产档案取计划价
              oInvValues[7] = oInvJHJ[i];
            }
            else {
              // 非计划价存货如果物料生产档案中没有定义计划价，就使用管理档案中定义的计划价
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
        // zlq add 20050218 清空参照，以保证下次还能选择同样的存货
        m_refpaneInv.setPK(null);
      }
      else if (sName.equals("crdcenterid")) {

        String sRDID = getBillCardPanel().getHeadItem("crdcenterid").getValue();
        String sDate = getBillCardPanel().getHeadItem("dbilldate").getValue();
        if (sRDID != null && sRDID.trim().length() != 0 && sDate != null
            && sDate.trim().length() != 0) {
          // 设置表体分录的计划价及批次号
          String[] sInvIDs = new String[getBillCardPanel().getRowCount()];
          if (sInvIDs.length != 0) {
            for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
              String sInvID = (String) getBillCardPanel().getBillModel()
                  .getValueAt(i, "cinventoryid");
              sInvIDs[i] = sInvID;
              // 将批次号及入库单据ID清空
              getBillCardPanel().getBillModel().setValueAt(null, i, "vbatch");
              getBillCardPanel().getBillModel().setValueAt(null, i,
                  "cinbillitemid");
            }
            // 获得计价方式及计划价
            String[][] sResults = nc.ui.ia.pub.CommonDataBO_Client.getPrices(
                m_sCorpID, sRDID, sInvIDs);
            // 处理每行数据
            for (int i = 0; i < sInvIDs.length; i++) {
              for (int j = 0; j < sResults.length; j++) {
                if (sInvIDs[i].equals(sResults[j][0])) {
                  // 是这行记录
                  Integer iPrice = new Integer(sResults[j][1]);
                  UFDouble dJHJ = new UFDouble(sResults[j][2]);
                  UFDouble dJHJE = null;
                  if (iPrice.intValue() == 5) {
                    // 是计划价
                    // 获得此行的数量
                    UFDouble dNumber = null;
                    Object oNumber = getBillCardPanel().getBillModel()
                        .getValueAt(i, "nnumber");
                    if (oNumber != null
                        && oNumber.toString().trim().length() != 0) {
                      dNumber = new UFDouble(oNumber.toString().trim());
                    }
                    if (dJHJ != null && dNumber != null) {
                      // 计划金额
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
          // 设置仓库参照的条件

          if (bt != null) {
            // 设置仓库参照的条件
            String sWhere = m_sOldWareCondition + "and pk_calbody = '" + sRDID
                + "'";
            // 是否是废品库
            // if (m_sBillType.equals(ConstVO.m_sBillBFD) == false)
            sWhere = sWhere + "and gubflag = 'N'";
            // else
            // sWhere = sWhere + "and gubflag = 'Y'";
            // 没有暂封
            sWhere = sWhere + "and sealflag = 'N'";
            ((UIRefPane) bt.getComponent()).setWhereString(sWhere);
          }
          // 设置仓库
          if (sWareID != null && sWareID.trim().length() != 0) {
            ((UIRefPane) bt.getComponent()).setPK(sWareID);
            if (((UIRefPane) bt.getComponent()).getRefCode() == null) {
              getBillCardPanel().getHeadItem("cwarehouseid").setValue("");
            }
          }
        }
        else {
          getBillCardPanel().getHeadItem("cwarehouseid").setValue("");
          // 恢复仓库的条件 //zlq add 20050305
          ((UIRefPane) bt.getComponent()).setWhereString(m_sOldWareCondition);
        }
        // 设置回冲单据分录
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
      // 将自由项设置到具体属性上
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
          // 检查单据ID是否正确
          String sPK = getUIRefPaneAdjustBill().getRefPK();
          try {
            if (sPK == null || sPK.trim().length() == 0) {
              getUIRefPaneAdjustBill().setBlurValue(
                  oAdjustBill.toString().trim());
              if (getUIRefPaneAdjustBill().getRefPK() == null) {
                // showErrorMessage("没有与调整单据" + oAdjustBill + "匹配的记录");
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
            // showErrorMessage("没有与调整单据" + oAdjustBill + "匹配的记录");
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
          // 参照中的内容
          getBillCardPanel().getBillModel().setValueAt(sPK, iRow,
              "cadjustbillid");
        }
      }
      else if (sName.equals("UIRefPaneAdjustBillItem")) {
        Object oBillItem = getUIRefPaneAdjustBillItem().getUITextField()
            .getText();
        if (oBillItem != null && oBillItem.toString().trim().length() != 0) {
          // 检查单据ID是否正确
          String sPK = getUIRefPaneAdjustBillItem().getRefPK();
          if (sPK == null || sPK.trim().length() == 0) {
            try {
              getUIRefPaneAdjustBillItem().setBlurValue(
                  oBillItem.toString().trim());
              if (getUIRefPaneAdjustBillItem().getRefPK() == null) {
                // showErrorMessage("没有与调整分录" + oBillItem + "匹配的记录");
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
              // showErrorMessage("没有与调整分录" + oBillItem + "匹配的记录");
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
            // showErrorMessage("没有与调整分录" + oBillItem + "匹配的记录");
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
            // 参照中的内容
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
              // 计算进项税转出金额
              calcTransIncomeTaxMny(iRow);
              // 输入了回冲单据分录，将批次号信息的入库单据分录清空
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
            // 有被调整单据，将存货信息清空
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
            // 计算进项税转出金额
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
      // 20050525 关闭固定资产和存货接口
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
      // //获得主键
      // nc.vo.fa.outer.FaSubequipmentVO[] voFas =
      // getUIRefPaneFacardEquipment().getSelectVos();
      // if (voFas != null && voFas.length > 0) {
      // Object oSourceModule = null;
      // if (m_iStatus == UPDATE_STATUS && m_voCurBill != null) {
      // oSourceModule = ((BillHeaderVO)
      // m_voCurBill.getParentVO()).getCsourcemodulename();
      // }
      // if (oSourceModule != null && voFas.length > 1) {
      // //是外系统传入的单据
      // showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
      // "UPP20143010-000129")/*@res "此单据是外系统传入存货核算的，只能选择一行固定资产的附属设备，请调整"*/);
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
      // //增行
      // getBillCardPanel().addLine();
      // iwriteRow = getBillCardPanel().getRowCount() - 1;
      // }
      // if (oSourceModule == null || oSourceModule.toString().trim().length()
      // == 0) {
      // //不是外系统传入单据
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
   * BillClientUI 构造子注解。
   */
  public BillClientUI(
      boolean bInit) {
    super();
    if (bInit) {
      // 跌价提取单和出口退税调整单调用时，不初始化
      initialize(null);
    }
  }

  /**
   * BillClientUI 构造子注解。
   */
  public BillClientUI(
      String sTitle, String sTemplet, String sBillType) {
    super();
    m_sTitle = sTitle;
    m_sBillType = sBillType;
    initialize(null);
  }

  /**
   * BillClientUI 单据联查构造函数
   * 
   * @param pk_corp
   *          公司
   * @param sBillTypeCode
   *          单据类型
   * @param sBusinessTypeID
   *          业务类型
   * @param sOperatorid
   *          制单人
   * @param sBillID
   *          单据ID
   */
  // public BillClientUI(String pk_corp, String sBillTypeCode, String
  // sBusinessTypeID, String sOperatorid, String sBillID) {
  // super();
  // m_sBillType = sBillTypeCode;
  //
  // try {
  //
  // //设置查询用VO
  // BillVO bvo = new BillVO();
  // BillHeaderVO bhvo = new BillHeaderVO();
  // bhvo.setCbillid(sBillID);
  // bvo.setParentVO(bhvo);
  // //查询数据
  // ClientLink cl = ce.getClientLink();
  // BillVO[] bills = BillBO_Client.queryByVO(bvo, new Boolean(true),
  // "cbillid",cl);
  // //显示数据
  // if (bills != null && bills[0] != null) {
  // bhvo = (BillHeaderVO) bills[0].getParentVO();
  // //初始化操作
  // initialize(bhvo.getPk_corp());
  // //载入表单模板
  // loadCardTemplet(null, sBillTypeCode);
  // //设置表单模版界面初始时不可编辑
  // getBillCardPanel().setEnabled(false);
  //
  // getBillCardPanel().setBillValueVO(bills[0]);
  // getBillCardPanel().execHeadTailLoadFormulas();
  // getBillCardPanel().getBillModel().execLoadFormula();
  //
  // BillItemVO btvo[] = (BillItemVO[]) bills[0].getChildrenVO();
  // setComboBoxInHeadFromVO(bills[0], true, 0);
  // //处理赠品
  // for (int i = 0; i < btvo.length; i++) {
  // if (getBillCardPanel().getBodyItem("blargessflag") != null) {
  // setComboBoxInBodyFromVO(btvo[i], true, i);
  // }
  // }
  // } else {
  // showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
  // "UPP20143010-000084")/*@res "未查询到记录"*/);
  // }
  // } catch (Exception e) {
  // e.printStackTrace();
  // showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
  // "UPP20143010-000103")/*@res "初始化数据出错"*/ + e.getMessage());
  // }
  // }
  //
  // 整体环境
  private IAEnvironment ce = new IAEnvironment();

  private IABillCardPanel ivjBillCardPanel = null;

  private IABillListPanel ivjBillListPanel = null;

  private IndividualAllotDlg ivjIndividualAllotDlg = null;

  private LocateConditionDlg ivjLocateConditionDlg = null;// 定位条件对话框

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
  private boolean[] m_bBodyEditFlags = null;// 记录修改时表体的编辑属性


  /*
   * 按钮组
   */
  private ButtonTree buttonTree = null; 
  //
  // 表单时的按钮
  private ButtonObject[] m_aryButtonGroupCard = null;
//  {
//      buttonTree.getButton(IAButtonConst.BTN_ADD_MANUAL), buttonTree.getButton(IAButtonConst.BTN_BILL_EDIT), buttonTree.getButton(IAButtonConst.BTN_SAVE), buttonTree.getButton(IAButtonConst.BTN_LINE),
//      buttonTree.getButton(IAButtonConst.BTN_BROWSE_TOP), buttonTree.getButton(IAButtonConst.BTN_BROWSE_PREVIOUS), buttonTree.getButton(IAButtonConst.BTN_BROWSE_NEXT), buttonTree.getButton(IAButtonConst.BTN_BROWSE_BOTTOM),
//      buttonTree.getButton(IAButtonConst.BTN_BILL_CANCEL), buttonTree.getButton(IAButtonConst.BTN_BILL_DELETE), buttonTree.getButton(IAButtonConst.BTN_PRINT), buttonTree.getButton(IAButtonConst.BTN_QUERY),
//      buttonTree.getButton(IAButtonConst.BTN_ASSIST_QUERY)
//  };

  // 列表时的按钮
  private ButtonObject[] m_aryButtonGroupList = null;
//  {
//      buttonTree.getButton(IAButtonConst.BTN_ADD_MANUAL), buttonTree.getButton(IAButtonConst.BTN_QUERY), buttonTree.getButton(IAButtonConst.BTN_BILL_EDIT), buttonTree.getButton(IAButtonConst.BTN_AUDIT),
//      buttonTree.getButton(IAButtonConst.BTN_BILL_DELETE), buttonTree.getButton(IAButtonConst.BTN_BROWSE_LOCATE), buttonTree.getButton(IAButtonConst.BTN_PRINT), buttonTree.getButton(IAButtonConst.BTN_BROWSE_REFRESH),
//      buttonTree.getButton(IAButtonConst.BTN_ASSIST_QUERY)
//  };

  
  
  //
  private boolean m_bAllowChangeBillMkByOthers = false;// 是否允许修改非本人制单的单据

  private boolean m_bKeepOriginalOperator = true;// 是否保留原始制单人

  private boolean m_bAllowDefinePriceByUser = true; // 出库单是否自定义单价

  private int m_iIndiFlag = 1;// 是否要出现个别指定的界面(1：出现，2：不出现)

  //
  // 模版数据
  private BillData m_bd = null;// 卡片数据

  private BillListData m_bdList = null;// 列表数据

  private boolean m_bIsAddChooseButton = false;// 是否增加了参照单据按钮

  private boolean m_bIsAddImportButton = false;// 是否增加了导入按钮

  private boolean m_bIsAdjustBill = false;// 是否是调整单，是：不检查批次号；否：检查批次号

  private UFBoolean m_bIsBeginAccount = null;// 是否期初记账

  private String m_sStartPeriod = null;// 起始会计期间

  private UFDate[] m_aBeginEndDates = null;// 当前会计期间开始结束日期

  //
  private boolean m_bIsChangeEvent = true;// 是否触发对话框事件,开始设为真表示处于用户编辑改动状态

  //
  private boolean m_bIsFreeEvent = true;// 是否触发自由项事件

  //

  // 标记单据类型
  private boolean m_bIsInAdjustBill = false;// 是否是入库调整单

  private boolean m_bIsInBill = false;// 是否是入库单

  private boolean m_bIsOutBill = false;// 是否是出库单

  private boolean m_bIsOtherBill = false;// 是否是其他类型单

  private boolean m_bIsPlanedPriceBill = false;// 是否是计划价调整单

  // 外部接口是否启用
  private boolean m_bIsICStart = false;// 库存管理是否启用

  private boolean m_bIsPOStart = false;// 采购管理是否启用

  private boolean m_bIsSCStart = false;// 委外加工是否启用

  private boolean m_bIsSOStart = false;// 销售管理是否启用

  //
  private QueryClientDlg m_dlgQuery = null;// 查询对话框

  private QueryPlannedPriceDlg m_dlgQueryPlannedPrice = null;// 计划价查询对话框

  private double m_dMaxValue = new UFDouble(999999999999.99999999)
      .doubleValue();

  //
  // private double m_dPriceMaxValue = new
  // UFDouble(9999999999.99).doubleValue();
  //
  private Hashtable m_htInvAndFix = null;// 存货管理档案主键与是否固定换算率之间的对应

  private Hashtable m_htInvAndKind = null;// 库存组织＋存货管理档案主键与物料形态之间的对应

  //
  private int[] m_iPeci = null;// 数据精度

  //
  private UIRefPane m_refpaneInv = null;// 用于存货参照

  private UIRefPane m_refpaneInvBack = null;// 用于存货参照

  protected String m_sBillType = ConstVO.m_sBillCGRKD;// 单据类型初始为采购入库单

  // private String m_sOldMeaCondition = ""; //计量档案的条件
  private String m_sOldBomCondition = "";// 成本对象的默认条件

  private String m_sOldInvCondition = ""; // 存货没有设库存组织时的条件

  private String m_sOldUserCondition = ""; // 人员没有设部门时的条件

  private String m_sOldWareCondition = ""; // 仓库没有设库存组织时的条件

  private String m_sOldWkCondition = "";// 工作中心的默认条件

  // 程序中改动对话框状态不触发，用户改动对话框中的值需要触发事件
  //
  private Vector m_vIsEnable = null; // 用于参照出库单生成销售发票

  //
  private AssistantLedgerVO[] m_voAssistantData = null;// 个别计价分配的计价辅助表VO

  //
  private BillVO[] m_voBills = null;// 缓存VO数组，存放查出的数据

  protected BillVO m_voCurBill = null;// 当前正在处理的单据

  protected int m_iCurBillPrt = -1; // 当前单据在缓存中的序号

  private ComboItemsVO m_ComboItemsVO = new ComboItemsVO();// 下拉框类型名称类

  private boolean m_bCanPasteLine = false;// 是否可以粘贴行

  BillSourceListener billSourceListener = new BillSourceListener();

  // 状态
  /**
   * 初始无单据状态
   */
  protected int INIT_STATUS = 0;

  /**
   * 卡片下浏览状态
   */
  protected int CARD_STATUS = 1;

  /**
   * 增加新单据状态
   */
  protected int ADD_STATUS = 2;
  /**
   * 修改单据状态
   */
  protected int UPDATE_STATUS = 3;

  /**
   * 列表浏览状态
   */
  protected int LIST_STATUS = 4;

  //protected int DELETE_STATUS = 5;

  //
  protected UFBoolean m_bIsCanAddBill = null;// 是否可增加单据（期初单据录入会调用，用protected）

  protected int m_iStatus = -1;// 状态

  protected String m_sCorpID = "-1";// 公司

  //
  protected String m_sErrorMessage = "";// 错误信息，用于提示

  //
  protected String m_sWTDX = "";// 委托代销的业务类型

  protected String m_sFQSK = "";// 分期收款的业务类型

  protected String m_sTitle = nc.ui.ml.NCLangRes.getInstance().getStrByID(
      "20143010", "UPP20143010-000061")/* @res "单据维护" */;

  /**
   * 单据主键与来源单据类型(销售成本结转单中用到)
   */
  protected HashMap m_hmBillId2Sourcebilltypecode = new HashMap(); 

  //
  // 自定义项处理
  protected DefVO[] m_voHeaddef = null;

  protected DefVO[] m_voBodydef = null;

  //按钮控制类
  ButtonControl btnCtrl = null;
 
 

  /**
   * 函数功能:获得库存期初结存数量 <p/> 参数: sModuleName ----- 模块信息 <p/> 返回值: <p/> 异常:
   */
  private BillVO[] importBills(String sModuleName) throws Exception {
    BillVO[] bvos = new BillVO[0]; // 返回结果
    SettledInfoVO[] svos = null; // 获得数据
    // 是期初单据，获得启用日期的前一天
    // 期初单据
    UFDate dBeginDate = CommonDataBO_Client.getMonthBeginDate(m_sCorpID,
        m_sStartPeriod);
    if (dBeginDate == null) {
      // 会计期间没有定义
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000043")/* @res "启用会计期间没有定义" */);
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000043")/* @res "启用会计期间没有定义" */);
      return bvos;
    }
    UFDate dPreDate = dBeginDate.getDateBefore(1);
    if (sModuleName.equals(ConstVO.m_sModuleCodeIC)) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000044")/* @res "正在导入库存管理期初数据，请等待" */);
      // 获得库存系统的启用会计期间
      String sICStartPeriod = "";
      String s[] = nc.ui.sm.createcorp.CreatecorpBO_Client.queryEnabledPeriod(
          m_sCorpID, ConstVO.m_sModuleIC);
      if (s != null && s.length != 0) {
        sICStartPeriod = s[0] + "-" + s[1];
      }
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
      "UPP20143010-000046")/* @res "正在整理期初数据，请等待" */);
      if (sICStartPeriod.compareTo(m_sStartPeriod) < 0) {
        // 库存先启用，存货后启用
        Log.info("库存先启用，存货后启用,导入结存数据");
        // 获得库存数据(按收发类别＋库存组织＋仓库＋存货排序)
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
        // 库存与存货同时启用
        Log.info("库存与存货同时启用，导入期初单据数据");
        // 获得库存数据(按收发类别＋库存组织＋仓库＋存货排序)
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
   * 根据部门id查询MDM责任中心
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
   * 判断单据类型
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
   * 根据业务员id查询姓名
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
   * 根据pk_corp查询账套
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
   * 根据cbillid查询采购发票信息（税额、税率...）
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
   * 根据cbillid查询来源单据类型
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
	 * 判断 ia_bill 中  是否为Y,如果为Y,则不能再传第二次,如过为空和N则还可以再传
	 * wy
	 * 2019年10月20日 
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
			System.out.println(djbh+" passbc修改成功");
		} catch (BusinessException e) {
			e.printStackTrace();
			System.out.println(djbh+" passbc修改失败");
		}		
	}
	
	/**
	 * 根据当前登录用户获取当前登录员工主键查询姓名、工号
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
	 * 校验必填字段是否为空
	 * 2019-10-29  by zy
	 * @return
	 */
	private String checkRequired(TempGeneralBillVO hvo,StringBuffer err){
		String flag = "Y";
		String djh=hvo.getVbillcode();
		if(hvo.getVbillcode()==null || "".equals(hvo.getVbillcode())){
//			showWarningMessage("单据号为空，请维护");
			err.append("单据"+djh+"单据号为空，请维护\n");
			return flag = "N";
		}else if(hvo.getCdeptid()==null || "".equals(hvo.getCdeptid())){
//			showWarningMessage("单据"+djh+"部门为空，请维护");
			err.append("单据"+djh+"部门为空，请维护\n");
			return flag = "N";
		}
		return flag;
	}
	
	/**
	   * 材料出库单下根据页面存货编码查询存货分类
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
  //表头自定义项
  private JSONObject hzdy(String billType,JSONObject val,int rows,TempGeneralBillVO tgbvo){
	  if("I01007".equals(billType)){
		  val.put("MAIN_RESERVE_C1","");//预留字符型1   抛帐标记
	      val.put("MAIN_RESERVE_C2",tgbvo.getVbillcode()==null?"":tgbvo.getVbillcode());//预留字符型2   凭证摘要1
	      val.put("MAIN_RESERVE_C3","");//预留字符型3   凭证摘要2
	      val.put("MAIN_RESERVE_C4","");//预留字符型4   凭证摘要3
	      val.put("MAIN_RESERVE_C5","Z");//预留字符型5   凭证类型
	      val.put("MAIN_RESERVE_C6","");//预留字符型6   附件张数
	      val.put("MAIN_RESERVE_C7","");//预留字符型7   采购机构
	      val.put("MAIN_RESERVE_C8","");//预留字符型8   报支单位组织代码
	      val.put("MAIN_RESERVE_C9","");//预留字符型9
	      val.put("MAIN_RESERVE_C10","");//预留字符型10
	      val.put("MAIN_RESERVE_C11","");//预留字符型11
	      val.put("MAIN_RESERVE_C12","");//预留字符型12
	      val.put("MAIN_RESERVE_C13","");//预留字符型13
	      val.put("MAIN_RESERVE_C14","");//预留字符型14
	      val.put("MAIN_RESERVE_C15","");//预留字符型15
	      val.put("MAIN_RESERVE_C16","");//预留字符型16
	      val.put("MAIN_RESERVE_C17","");//预留字符型17
	      val.put("MAIN_RESERVE_C18","");//预留字符型18
	      val.put("MAIN_RESERVE_C19","");//预留字符型19
	      val.put("MAIN_RESERVE_C20","");//预留字符型21
	      val.put("MAIN_RESERVE_C22","");//预留字符型22
	      val.put("MAIN_RESERVE_C23","");//预留字符型23
	      val.put("MAIN_RESERVE_C24","");//预留字符型24
	      val.put("MAIN_RESERVE_C25","");//预留字符型25
	      val.put("MAIN_RESERVE_C26","");//预留字符型26
	      val.put("MAIN_RESERVE_C27","");//预留字符型27
	      val.put("MAIN_RESERVE_C28","");//预留字符型28
	      val.put("MAIN_RESERVE_C29","");//预留字符型29
	      val.put("MAIN_RESERVE_C30","");//预留字符型30
	      val.put("MAIN_RESERVE_N1",rows);//预留数字型1  -- 明细总条数  rows	
	      val.put("MAIN_RESERVE_N2",0);//预留数字型2
	      val.put("MAIN_RESERVE_N3",0);//预留数字型3
	      val.put("MAIN_RESERVE_N4",0);//预留数字型4
	      val.put("MAIN_RESERVE_N5",0);//预留数字型5
	  }else if("I02002".equals(billType)){
		  val.put("MAIN_RESERVE_C1","");//预留字符型1 
	      val.put("MAIN_RESERVE_C2",tgbvo.getVbillcode()==null?"":tgbvo.getVbillcode());//预留字符型2
	      val.put("MAIN_RESERVE_C3","");//预留字符型3
	      val.put("MAIN_RESERVE_C4","");//预留字符型4
	      val.put("MAIN_RESERVE_C5","Z");//预留字符型5  凭证类别  R收款 P付款 Z转款
	      val.put("MAIN_RESERVE_C6","");//预留字符型6
	      val.put("MAIN_RESERVE_C7","");//预留字符型7
	      val.put("MAIN_RESERVE_C8","");//预留字符型8
	      val.put("MAIN_RESERVE_C9","");//预留字符型9
	      val.put("MAIN_RESERVE_C10","");//预留字符型10
	      val.put("MAIN_RESERVE_C11","");//预留字符型11
	      val.put("MAIN_RESERVE_C12","");//预留字符型12
	      val.put("MAIN_RESERVE_C13","");//预留字符型13
	      val.put("MAIN_RESERVE_C14","");//预留字符型14
	      val.put("MAIN_RESERVE_C15","");//预留字符型15
	      val.put("MAIN_RESERVE_C16","");//预留字符型16
	      val.put("MAIN_RESERVE_C17","");//预留字符型17
	      val.put("MAIN_RESERVE_C18","");//预留字符型18
	      val.put("MAIN_RESERVE_C19","");//预留字符型19
	      val.put("MAIN_RESERVE_C20","");//预留字符型21
	      val.put("MAIN_RESERVE_C22","");//预留字符型22
	      val.put("MAIN_RESERVE_C23","");//预留字符型23
	      val.put("MAIN_RESERVE_C24","");//预留字符型24
	      val.put("MAIN_RESERVE_C25","");//预留字符型25
	      val.put("MAIN_RESERVE_C26","");//预留字符型26
	      val.put("MAIN_RESERVE_C27","");//预留字符型27
	      val.put("MAIN_RESERVE_C28","");//预留字符型28
	      val.put("MAIN_RESERVE_C29","");//预留字符型29
	      val.put("MAIN_RESERVE_C30","");//预留字符型30
	      val.put("MAIN_RESERVE_N1",rows);//预留数字型1  -- 明细总条数  rows
	      val.put("MAIN_RESERVE_N2",0);//预留数字型2
	      val.put("MAIN_RESERVE_N3",0);//预留数字型3
	      val.put("MAIN_RESERVE_N4",0);//预留数字型4
	      val.put("MAIN_RESERVE_N5",0);//预留数字型5
	  }else if("I01001".equals(billType)){
		  val.put("MAIN_RESERVE_C1","");//预留字符型1 
	      val.put("MAIN_RESERVE_C2",tgbvo.getVbillcode()==null?"":tgbvo.getVbillcode());//预留字符型2
	      val.put("MAIN_RESERVE_C3","");//预留字符型3
	      val.put("MAIN_RESERVE_C4","");//预留字符型4
	      val.put("MAIN_RESERVE_C5","Z");//预留字符型5  凭证类别  R收款 P付款 Z转款
	      val.put("MAIN_RESERVE_C6","");//预留字符型6  附件张数（类似几张发票）（所有都要有）
	      val.put("MAIN_RESERVE_C7","");//预留字符型7
	      val.put("MAIN_RESERVE_C8","");//预留字符型8
	      val.put("MAIN_RESERVE_C9","");//预留字符型9
	      val.put("MAIN_RESERVE_C10","");//预留字符型10
	      val.put("MAIN_RESERVE_C11","");//预留字符型11
	      val.put("MAIN_RESERVE_C12","");//预留字符型12
	      val.put("MAIN_RESERVE_C13","");//预留字符型13
	      val.put("MAIN_RESERVE_C14","");//预留字符型14
	      val.put("MAIN_RESERVE_C15","");//预留字符型15
	      val.put("MAIN_RESERVE_C16","");//预留字符型16
	      val.put("MAIN_RESERVE_C17","");//预留字符型17
	      val.put("MAIN_RESERVE_C18","");//预留字符型18
	      val.put("MAIN_RESERVE_C19","");//预留字符型19
	      val.put("MAIN_RESERVE_C20","");//预留字符型21
	      val.put("MAIN_RESERVE_C22","");//预留字符型22
	      val.put("MAIN_RESERVE_C23","");//预留字符型23
	      val.put("MAIN_RESERVE_C24","");//预留字符型24
	      val.put("MAIN_RESERVE_C25","");//预留字符型25
	      val.put("MAIN_RESERVE_C26","");//预留字符型26
	      val.put("MAIN_RESERVE_C27","");//预留字符型27
	      val.put("MAIN_RESERVE_C28","");//预留字符型28
	      val.put("MAIN_RESERVE_C29","");//预留字符型29
	      val.put("MAIN_RESERVE_C30","");//预留字符型30
	      val.put("MAIN_RESERVE_N1",rows);//预留数字型1  -- 明细总条数  rows
	      val.put("MAIN_RESERVE_N2",0);//预留数字型2
	      val.put("MAIN_RESERVE_N3",0);//预留数字型3
	      val.put("MAIN_RESERVE_N4",0);//预留数字型4
	      val.put("MAIN_RESERVE_N5",0);//预留数字型5
	  }else{
		  val.put("MAIN_RESERVE_C1","");//预留字符型1 
	      val.put("MAIN_RESERVE_C2",tgbvo.getVbillcode()==null?"":tgbvo.getVbillcode());//预留字符型2
	      val.put("MAIN_RESERVE_C3","");//预留字符型3
	      val.put("MAIN_RESERVE_C4","");//预留字符型4
	      val.put("MAIN_RESERVE_C5","Z");//预留字符型5  
	      val.put("MAIN_RESERVE_C6","");//预留字符型6
	      val.put("MAIN_RESERVE_C7","");//预留字符型7
	      val.put("MAIN_RESERVE_C8","");//预留字符型8
	      val.put("MAIN_RESERVE_C9","");//预留字符型9
	      val.put("MAIN_RESERVE_C10","");//预留字符型10
	      val.put("MAIN_RESERVE_C11","");//预留字符型11
	      val.put("MAIN_RESERVE_C12","");//预留字符型12
	      val.put("MAIN_RESERVE_C13","");//预留字符型13
	      val.put("MAIN_RESERVE_C14","");//预留字符型14
	      val.put("MAIN_RESERVE_C15","");//预留字符型15
	      val.put("MAIN_RESERVE_C16","");//预留字符型16
	      val.put("MAIN_RESERVE_C17","");//预留字符型17
	      val.put("MAIN_RESERVE_C18","");//预留字符型18
	      val.put("MAIN_RESERVE_C19","");//预留字符型19
	      val.put("MAIN_RESERVE_C20","");//预留字符型21
	      val.put("MAIN_RESERVE_C22","");//预留字符型22
	      val.put("MAIN_RESERVE_C23","");//预留字符型23
	      val.put("MAIN_RESERVE_C24","");//预留字符型24
	      val.put("MAIN_RESERVE_C25","");//预留字符型25
	      val.put("MAIN_RESERVE_C26","");//预留字符型26
	      val.put("MAIN_RESERVE_C27","");//预留字符型27
	      val.put("MAIN_RESERVE_C28","");//预留字符型28
	      val.put("MAIN_RESERVE_C29","");//预留字符型29
	      val.put("MAIN_RESERVE_C30","");//预留字符型30
	      val.put("MAIN_RESERVE_N1",rows);//预留数字型1  -- 明细总条数  rows
	      val.put("MAIN_RESERVE_N2",0);//预留数字型2
	      val.put("MAIN_RESERVE_N3",0);//预留数字型3
	      val.put("MAIN_RESERVE_N4",0);//预留数字型4
	      val.put("MAIN_RESERVE_N5",0);//预留数字型5
	  }
	  
	return val;
  }
  
//表体自定义项
  private JSONObject bzdy(String billType,JSONObject bval,TempGeneralBillVO tgbvo,String zrzx){
	  
	  Map map=queryBody(tgbvo.getCbill_bid(),tgbvo.getPk_corp());
	  if("I01007".equals(billType)){
//		  bval.put("DETAIL_RESERVE_C1",btvo.getVbomcode()==null?"":btvo.getVbomcode());//预留字符型1   物料代码  -- 产品
		  bval.put("DETAIL_RESERVE_C1",map.get("vbomcode")==null?"":map.get("vbomcode").toString());//预留字符型1   物料代码  -- 产品
//		  bval.put("DETAIL_RESERVE_C1","");//预留字符型1   物料代码  -- 产品
	      bval.put("DETAIL_RESERVE_C2",zrzx);//预留字符型2     zrzx   "BCGRCWKF"
	      bval.put("DETAIL_RESERVE_C3","");//预留字符型3   物料中类
	      bval.put("DETAIL_RESERVE_C4","");//预留字符型4   物料细类
	      bval.put("DETAIL_RESERVE_C5","");//预留字符型5   业务类别一
	      bval.put("DETAIL_RESERVE_C6","");//预留字符型6   业务类别二
	      String kscode=tgbvo.getCcustomvendorid()==null?"":tgbvo.getCcustomvendorid().toString();
	      String ks = queryks(kscode);
	      bval.put("DETAIL_RESERVE_C7",ks);//预留字符型7   供应商代码
	      bval.put("DETAIL_RESERVE_C8","");//预留字符型8
	      bval.put("DETAIL_RESERVE_C9","");//预留字符型9
	      bval.put("DETAIL_RESERVE_C10","250101");//预留字符型10  物料类别
	      bval.put("DETAIL_RESERVE_C11","");//预留字符型11
	      bval.put("DETAIL_RESERVE_C12","");//预留字符型12
	      bval.put("DETAIL_RESERVE_C13","");//预留字符型13   合同号
	      bval.put("DETAIL_RESERVE_C14","");//预留字符型14
	      bval.put("DETAIL_RESERVE_C15","");//预留字符型15   业务责任中心
	      bval.put("DETAIL_RESERVE_C16","");//预留字符型16   产地属性
	      bval.put("DETAIL_RESERVE_C17","");//预留字符型17   客商代码
	      bval.put("DETAIL_RESERVE_C18","");//预留字符型18
	      bval.put("DETAIL_RESERVE_C19","");//预留字符型19
	      bval.put("DETAIL_RESERVE_C20","");//预留字符型20   买家客商代码
	      bval.put("DETAIL_RESERVE_C21","");//预留字符型21
	      bval.put("DETAIL_RESERVE_C22","");//预留字符型22
	      bval.put("DETAIL_RESERVE_C23","");//预留字符型23
	      bval.put("DETAIL_RESERVE_C24","");//预留字符型24
	      bval.put("DETAIL_RESERVE_C25","");//预留字符型25
	      bval.put("DETAIL_RESERVE_C26","");//预留字符型26   业务大类
	      bval.put("DETAIL_RESERVE_C27","");//预留字符型27   业务细类
	      bval.put("DETAIL_RESERVE_C28","");//预留字符型28
	      bval.put("DETAIL_RESERVE_C29","");//预留字符型29
	      bval.put("DETAIL_RESERVE_C30","");//预留字符型30
	      bval.put("DETAIL_RESERVE_N1",0);//预留数字型1
	      bval.put("DETAIL_RESERVE_N2",0);//预留数字型2
	      bval.put("DETAIL_RESERVE_N3",0);//预留数字型3
	      bval.put("DETAIL_RESERVE_N4",0);//预留数字型4
	      bval.put("DETAIL_RESERVE_N5",0);//预留数字型5
	  }else if("I02002".equals(billType)){
		  bval.put("DETAIL_RESERVE_C1","");//预留字符型1
	      bval.put("DETAIL_RESERVE_C2","BCANCZSC");//预留字符型2  责任中心  zrzx
	      bval.put("DETAIL_RESERVE_C3","");//预留字符型3  
//	      String chtype=queryChfl(btvo.getCinventorycode());
//	      System.out.println("chtype::::::::::"+chtype);
//	      bval.put("DETAIL_RESERVE_C4",chtype);//预留字符型4
	      String chtype=querychtype(map.get("cinventoryid")==null?"":map.get("cinventoryid").toString());
	      bval.put("DETAIL_RESERVE_C4","250101");//预留字符型4  先写死，取chtype
	      bval.put("DETAIL_RESERVE_C5","BCANCZSC"+"10001");//预留字符型5  责任中心+成本科目（10001）zrzx
	      bval.put("DETAIL_RESERVE_C6","");//预留字符型6
	      bval.put("DETAIL_RESERVE_C7","");//预留字符型7
	      bval.put("DETAIL_RESERVE_C8","");//预留字符型8
	      bval.put("DETAIL_RESERVE_C9","");//预留字符型9
	      bval.put("DETAIL_RESERVE_C10","250101");//预留字符型10   物料类别  存货分类  没给规则配默认  250101
	      bval.put("DETAIL_RESERVE_C11","");//预留字符型11
	      bval.put("DETAIL_RESERVE_C12","");//预留字符型12
	      bval.put("DETAIL_RESERVE_C13","");//预留字符型13
	      bval.put("DETAIL_RESERVE_C14","");//预留字符型14
	      bval.put("DETAIL_RESERVE_C15","");//预留字符型15
	      bval.put("DETAIL_RESERVE_C16","");//预留字符型16
	      bval.put("DETAIL_RESERVE_C17","");//预留字符型17
	      bval.put("DETAIL_RESERVE_C18","");//预留字符型18
	      bval.put("DETAIL_RESERVE_C19","");//预留字符型19
	      bval.put("DETAIL_RESERVE_C20","");//预留字符型20
	      bval.put("DETAIL_RESERVE_C21","");//预留字符型21
	      bval.put("DETAIL_RESERVE_C22","");//预留字符型22
	      bval.put("DETAIL_RESERVE_C23","");//预留字符型23
	      bval.put("DETAIL_RESERVE_C24","");//预留字符型24
	      bval.put("DETAIL_RESERVE_C25","");//预留字符型25
	      bval.put("DETAIL_RESERVE_C26","");//预留字符型26
	      bval.put("DETAIL_RESERVE_C27","");//预留字符型27
	      bval.put("DETAIL_RESERVE_C28","");//预留字符型28
	      bval.put("DETAIL_RESERVE_C29","");//预留字符型29
	      bval.put("DETAIL_RESERVE_C30","");//预留字符型30
	      bval.put("DETAIL_RESERVE_N1",0);//预留数字型1
	      bval.put("DETAIL_RESERVE_N2",0);//预留数字型2
	      bval.put("DETAIL_RESERVE_N3",0);//预留数字型3
	      bval.put("DETAIL_RESERVE_N4",0);//预留数字型4
	      bval.put("DETAIL_RESERVE_N5",0);//预留数字型5
	  }else if("I02001".equals(billType)){
		  bval.put("DETAIL_RESERVE_C1","");//预留字符型1
	      bval.put("DETAIL_RESERVE_C2",zrzx);//预留字符型2  责任中心
	      bval.put("DETAIL_RESERVE_C3","");//预留字符型3
//	      String chtype=queryChfl(btvo.getCinventorycode());
//	      System.out.println("chtype::::::::::"+chtype);
//	      bval.put("DETAIL_RESERVE_C4",chtype);//预留字符型4
	      String chtype=querychtype(map.get("cinventoryid")==null?"":map.get("cinventoryid").toString()); 
	      bval.put("DETAIL_RESERVE_C4","250101");//预留字符型4  存货分类  没给规则配默认  250101
	      bval.put("DETAIL_RESERVE_C5","");//预留字符型5
	      bval.put("DETAIL_RESERVE_C6","");//预留字符型6
	      String kscode=tgbvo.getCcustomvendorid()==null?"":tgbvo.getCcustomvendorid().toString();
	      String ks = queryks(kscode);
	      bval.put("DETAIL_RESERVE_C7",ks);//预留字符型7
	      bval.put("DETAIL_RESERVE_C8","");//预留字符型8
	      bval.put("DETAIL_RESERVE_C9","");//预留字符型9
	      bval.put("DETAIL_RESERVE_C10","250101");//预留字符型10  物料类别  存货分类  没给规则配默认  250101
	      bval.put("DETAIL_RESERVE_C11","");//预留字符型11
	      bval.put("DETAIL_RESERVE_C12","");//预留字符型12
	      bval.put("DETAIL_RESERVE_C13","");//预留字符型13
	      bval.put("DETAIL_RESERVE_C14","");//预留字符型14
	      bval.put("DETAIL_RESERVE_C15","");//预留字符型15
	      bval.put("DETAIL_RESERVE_C16","");//预留字符型16
	      bval.put("DETAIL_RESERVE_C17","");//预留字符型17
	      bval.put("DETAIL_RESERVE_C18","");//预留字符型18
	      bval.put("DETAIL_RESERVE_C19","");//预留字符型19
	      bval.put("DETAIL_RESERVE_C20","");//预留字符型20
	      bval.put("DETAIL_RESERVE_C21","");//预留字符型21
	      bval.put("DETAIL_RESERVE_C22","");//预留字符型22
	      bval.put("DETAIL_RESERVE_C23","");//预留字符型23
	      bval.put("DETAIL_RESERVE_C24","");//预留字符型24
	      bval.put("DETAIL_RESERVE_C25","");//预留字符型25
	      bval.put("DETAIL_RESERVE_C26","");//预留字符型26
	      bval.put("DETAIL_RESERVE_C27","");//预留字符型27
	      bval.put("DETAIL_RESERVE_C28","");//预留字符型28
	      bval.put("DETAIL_RESERVE_C29","");//预留字符型29
	      bval.put("DETAIL_RESERVE_C30","");//预留字符型30
	      bval.put("DETAIL_RESERVE_N1",0);//预留数字型1
	      bval.put("DETAIL_RESERVE_N2",0);//预留数字型2
	      bval.put("DETAIL_RESERVE_N3",0);//预留数字型3
	      bval.put("DETAIL_RESERVE_N4",0);//预留数字型4
	      bval.put("DETAIL_RESERVE_N5",0);//预留数字型5
	  }else if("I01001".equals(billType)){
		  bval.put("DETAIL_RESERVE_C1","");//预留字符型1
	      bval.put("DETAIL_RESERVE_C2","BCANCZSC");//预留字符型2  责任中心  zrzx
	      bval.put("DETAIL_RESERVE_C3","");//预留字符型3  
//	      String chtype=queryChfl(btvo.getCinventorycode());
//	      System.out.println("chtype::::::::::"+chtype);
//	      bval.put("DETAIL_RESERVE_C4",chtype);//预留字符型4
	      String chtype=querychtype(map.get("cinventoryid")==null?"":map.get("cinventoryid").toString());
	      bval.put("DETAIL_RESERVE_C4","250101");//预留字符型4  先写死，取chtype
	      bval.put("DETAIL_RESERVE_C5","BCANCZSC"+"10001");//预留字符型5  责任中心+成本科目（10001）zrzx
	      bval.put("DETAIL_RESERVE_C6","");//预留字符型6
	      bval.put("DETAIL_RESERVE_C7","");//预留字符型7
	      bval.put("DETAIL_RESERVE_C8","");//预留字符型8
	      bval.put("DETAIL_RESERVE_C9","");//预留字符型9
	      bval.put("DETAIL_RESERVE_C10","250101");//预留字符型10   物料类别  存货分类  没给规则配默认  250101
	      bval.put("DETAIL_RESERVE_C11","");//预留字符型11
	      bval.put("DETAIL_RESERVE_C12","");//预留字符型12
	      bval.put("DETAIL_RESERVE_C13","");//预留字符型13
	      bval.put("DETAIL_RESERVE_C14","");//预留字符型14
	      bval.put("DETAIL_RESERVE_C15","");//预留字符型15
	      bval.put("DETAIL_RESERVE_C16","");//预留字符型16
	      bval.put("DETAIL_RESERVE_C17","");//预留字符型17
	      bval.put("DETAIL_RESERVE_C18","");//预留字符型18
	      bval.put("DETAIL_RESERVE_C19","");//预留字符型19
	      bval.put("DETAIL_RESERVE_C20","");//预留字符型20
	      bval.put("DETAIL_RESERVE_C21","");//预留字符型21
	      bval.put("DETAIL_RESERVE_C22","");//预留字符型22
	      bval.put("DETAIL_RESERVE_C23","");//预留字符型23
	      bval.put("DETAIL_RESERVE_C24","");//预留字符型24
	      bval.put("DETAIL_RESERVE_C25","");//预留字符型25
	      bval.put("DETAIL_RESERVE_C26","");//预留字符型26
	      bval.put("DETAIL_RESERVE_C27","");//预留字符型27
	      bval.put("DETAIL_RESERVE_C28","");//预留字符型28
	      bval.put("DETAIL_RESERVE_C29","");//预留字符型29
	      bval.put("DETAIL_RESERVE_C30","");//预留字符型30
	      bval.put("DETAIL_RESERVE_N1",0);//预留数字型1
	      bval.put("DETAIL_RESERVE_N2",0);//预留数字型2
	      bval.put("DETAIL_RESERVE_N3",0);//预留数字型3
	      bval.put("DETAIL_RESERVE_N4",0);//预留数字型4
	      bval.put("DETAIL_RESERVE_N5",0);//预留数字型5
	  }else if("C01008".equals(billType)){
//		  bval.put("DETAIL_RESERVE_C1",btvo.getCinventorycode());//预留字符型1
		  bval.put("DETAIL_RESERVE_C1",map.get("cinventorycode"));//预留字符型1
	      bval.put("DETAIL_RESERVE_C2","");//预留字符型2
	      bval.put("DETAIL_RESERVE_C3","");//预留字符型3
	      bval.put("DETAIL_RESERVE_C4","");//预留字符型4
	      bval.put("DETAIL_RESERVE_C5","");//预留字符型5
	      bval.put("DETAIL_RESERVE_C6","");//预留字符型6
	      bval.put("DETAIL_RESERVE_C7","");//预留字符型7
	      bval.put("DETAIL_RESERVE_C8","");//预留字符型8
	      bval.put("DETAIL_RESERVE_C9","");//预留字符型9
	      bval.put("DETAIL_RESERVE_C10","");//预留字符型10
	      bval.put("DETAIL_RESERVE_C11","");//预留字符型11
	      bval.put("DETAIL_RESERVE_C12","");//预留字符型12
	      bval.put("DETAIL_RESERVE_C13","");//预留字符型13
	      bval.put("DETAIL_RESERVE_C14","");//预留字符型14
	      bval.put("DETAIL_RESERVE_C15","");//预留字符型15
	      bval.put("DETAIL_RESERVE_C16","");//预留字符型16
	      bval.put("DETAIL_RESERVE_C17","");//预留字符型17
	      bval.put("DETAIL_RESERVE_C18","");//预留字符型18
	      bval.put("DETAIL_RESERVE_C19","");//预留字符型19
	      bval.put("DETAIL_RESERVE_C20","");//预留字符型20
	      bval.put("DETAIL_RESERVE_C21","");//预留字符型21
	      bval.put("DETAIL_RESERVE_C22","");//预留字符型22
	      bval.put("DETAIL_RESERVE_C23","");//预留字符型23
	      bval.put("DETAIL_RESERVE_C24","");//预留字符型24
	      bval.put("DETAIL_RESERVE_C25","");//预留字符型25
	      bval.put("DETAIL_RESERVE_C26","");//预留字符型26
	      bval.put("DETAIL_RESERVE_C27","");//预留字符型27
	      bval.put("DETAIL_RESERVE_C28","");//预留字符型28
	      bval.put("DETAIL_RESERVE_C29","");//预留字符型29
	      bval.put("DETAIL_RESERVE_C30","");//预留字符型30
	      bval.put("DETAIL_RESERVE_N1",0);//预留数字型1
	      bval.put("DETAIL_RESERVE_N2",0);//预留数字型2
	      bval.put("DETAIL_RESERVE_N3",0);//预留数字型3
	      bval.put("DETAIL_RESERVE_N4",0);//预留数字型4
	      bval.put("DETAIL_RESERVE_N5",0);//预留数字型5
	  }else{
		  bval.put("DETAIL_RESERVE_C1","");//预留字符型1
	      bval.put("DETAIL_RESERVE_C2","");//预留字符型2
	      bval.put("DETAIL_RESERVE_C3","");//预留字符型3
	      bval.put("DETAIL_RESERVE_C4","");//预留字符型4
	      bval.put("DETAIL_RESERVE_C5","");//预留字符型5
	      bval.put("DETAIL_RESERVE_C6","");//预留字符型6
	      bval.put("DETAIL_RESERVE_C7","");//预留字符型7
	      bval.put("DETAIL_RESERVE_C8","");//预留字符型8
	      bval.put("DETAIL_RESERVE_C9","");//预留字符型9
	      bval.put("DETAIL_RESERVE_C10","250101");//预留字符型10
	      bval.put("DETAIL_RESERVE_C11","");//预留字符型11
	      bval.put("DETAIL_RESERVE_C12","");//预留字符型12
	      bval.put("DETAIL_RESERVE_C13","");//预留字符型13
	      bval.put("DETAIL_RESERVE_C14","");//预留字符型14
	      bval.put("DETAIL_RESERVE_C15","");//预留字符型15
	      bval.put("DETAIL_RESERVE_C16","");//预留字符型16
	      bval.put("DETAIL_RESERVE_C17","");//预留字符型17
	      bval.put("DETAIL_RESERVE_C18","");//预留字符型18
	      bval.put("DETAIL_RESERVE_C19","");//预留字符型19
	      bval.put("DETAIL_RESERVE_C20","");//预留字符型20
	      bval.put("DETAIL_RESERVE_C21","");//预留字符型21
	      bval.put("DETAIL_RESERVE_C22","");//预留字符型22
	      bval.put("DETAIL_RESERVE_C23","");//预留字符型23
	      bval.put("DETAIL_RESERVE_C24","");//预留字符型24
	      bval.put("DETAIL_RESERVE_C25","");//预留字符型25
	      bval.put("DETAIL_RESERVE_C26","");//预留字符型26
	      bval.put("DETAIL_RESERVE_C27","");//预留字符型27
	      bval.put("DETAIL_RESERVE_C28","");//预留字符型28
	      bval.put("DETAIL_RESERVE_C29","");//预留字符型29
	      bval.put("DETAIL_RESERVE_C30","");//预留字符型30
	      bval.put("DETAIL_RESERVE_N1",0);//预留数字型1
	      bval.put("DETAIL_RESERVE_N2",0);//预留数字型2
	      bval.put("DETAIL_RESERVE_N3",0);//预留数字型3
	      bval.put("DETAIL_RESERVE_N4",0);//预留数字型4
	      bval.put("DETAIL_RESERVE_N5",0);//预留数字型5
	  }
	  
	return bval;	  
  }
  /**
   * 查询表体vo
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
   * 查询存货分类
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
   * 查询客商编码
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
  
  //校验多行数据
  public StringBuffer CheckData(List list,String val){
	  StringBuffer str = new StringBuffer();
	  for(int i=0;i<list.size();i++){
		  
		  Map<String,String> map=new HashMap<String,String>();
		  map = (Map) list.get(i);
		  if(map.get("vbillcode")==null||map.get("vbillcode").length()==0){
			  str.append("\n"+"单据号"+val+"中第"+i+1+"行单据号为空");
		  }
		  if(map.get("cbilltypecode")==null||map.get("cbilltypecode").length()==0){
			  str.append("\n"+"单据号"+val+"中第"+i+1+"行单据类型为空");
		  }
		  if(map.get("pk_corp")==null||map.get("pk_corp").length()==0){
			  str.append("\n"+"单据号"+val+"中第"+i+1+"行公司为空");
		  }
		  if(map.get("cbillid")==null||map.get("cbillid").length()==0){
			  str.append("\n"+"单据号"+val+"中第"+i+1+"行单据标识为空");
		  }
		  if(map.get("cdeptid")==null||map.get("cdeptid").length()==0){
			  str.append("\n"+"单据号"+val+"中第"+i+1+"行部门标识 为空");
		  }
		  /*if(map.get("cemployeeid")==null||map.get("cemployeeid").length()==0){
			  str.append("\n"+"单据号"+val+"中第"+i+1+"行人员标识为空");
		  }*/
		  if(map.get("coperatorid")==null||map.get("coperatorid").length()==0){
			  str.append("\n"+"单据号"+val+"中第"+i+1+"行制单人标识为空");
		  }
		  /*if(map.get("cdispatchid")==null||map.get("cdispatchid").length()==0){
			  str.append("\n"+"单据号"+val+"中第"+i+1+"行收发标识为空");
		  }*/
		  /*else if(map.get("ccustomvendorid")==null||map.get("ccustomvendorid").length()==0){
			  str.append("\n"+"单据号"+val+"中第"+i+1+"行客商标识为空");
		  }else if(map.get("cbill_bid")==null||map.get("cbill_bid").length()==0){
			  str.append("\n"+"单据号"+val+"中第"+i+1+"行分录标识为空");
		  }else if(map.get("nmoney")==null||map.get("nmoney").length()==0){
			  str.append("\n"+"单据号"+val+"中第"+i+1+"行金额为空"); 
		  }else if(map.get("nnumber")==null||map.get("nnumber").length()==0){
			  str.append("\n"+"单据号"+val+"中第"+i+1+"行数量为空"); 
		  }else if(map.get("cinventoryid")==null||map.get("cinventoryid").length()==0){
			  str.append("\n"+"单据号"+val+"中第"+i+1+"行存货标识为空"); 
		  }else if(map.get("irownumber")==null||map.get("irownumber").length()==0){
			  str.append("\n"+"单据号"+val+"中第"+i+1+"行行号为空");
		  }else if(map.get("bestimateflag")==null||map.get("bestimateflag").length()==0){
			  str.append("\n"+"单据号"+val+"中第"+i+1+"行暂估标识为空");
		  }*/
		  
	  }
	  return str;
	  
  }


}