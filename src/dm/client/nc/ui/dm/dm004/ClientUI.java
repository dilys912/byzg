
package nc.ui.dm.dm004;

/**
 * ��Ĺ��ܡ���;���ִ�BUG���Լ��������˿��ܸ���Ȥ�Ľ��ܡ� ���ߣ�������
 * 
 * @version ����޸�����(2002-5-9 13:31:03)
 * @see ��Ҫ�μ���������
 * @since �Ӳ�Ʒ����һ���汾�����౻��ӽ���������ѡ�� �޸��� + �޸����� �޸�˵��
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uif.pub.IUifService;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.dm.pub.ClientUIforCard;
import nc.ui.dm.pub.DMBillStatus;
import nc.ui.dm.pub.DMQueryConditionDlg;
import nc.ui.dm.pub.ExceptionUITools;
import nc.ui.dm.pub.cardpanel.DMBillCardPanel;
import nc.ui.dm.pub.ref.TrancustRefModel;
import nc.ui.dm.pub.ref.VehicletypeRefModel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.FramePanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.to.pub.TOBillTool;
import nc.vo.dm.dm004.ConstForBasePrice;
import nc.vo.dm.dm004.DmBasepriceAggVO;
import nc.vo.dm.dm004.DmBasepriceVO;
import nc.vo.dm.pub.DMBillNodeCodeConst;
import nc.vo.dm.pub.DMBillTypeConst;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.rino.pda.BasicdocVO;


public class ClientUI extends ClientUIforCard {
  // �ڵ��ɺδ������򿪣�Ĭ��Ϊ�ӽڵ���������
  private int opentype = ILinkType.NONLINK_TYPE;
  
//  private CorpVO m_corp = null;
  static IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance()
	      .lookup(IUAPQueryBS.class.getName());
	  static IUifService iserviceDao = (IUifService) NCLocator.getInstance()
	      .lookup(IUifService.class.getName());
	  int billstatus = 0;
  public ClientUI(
      FramePanel fp) {
    this.opentype = fp.getLinkType();
  }

  protected String checkPrerequisite() {
    // ������򿪽ڵ�
    if (this.opentype != ILinkType.LINK_TYPE_QUERY) {
      try {
        initializeNew();
      }
      catch (Error ex) {
        ex.printStackTrace();
        return ex.getMessage();
      }
    }
    return null;
  }
//  nc.ui.pub.ClientEnvironment s =  nc.ui.pub.ClientEnvironment.getInstance();
//  m_corp = s.getCorporation();
//  String pk_corp = m_corp.getPk_corp();
  
  /**
   * �رմ��ڵĿͻ��˽ӿڡ����ڱ���������ɴ��ڹر�ǰ�Ĺ�����
   * 
   * @return boolean ����ֵΪtrue��ʾ�����ڹرգ�����ֵΪfalse��ʾ�������ڹرա� �������ڣ�(2001-8-8
   *         13:52:37)
   */
  public boolean onClosing() {
    if (m_bEdit) {
      int result = MessageDialog.showYesNoCancelDlg(this, null,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH001"),
                MessageDialog.ID_YES);/* @res "�Ƿ񱣴����޸ĵ����ݣ�" */
      if (result == MessageDialog.ID_NO) {
        return true;
      }
      else if (result == MessageDialog.ID_YES) {
        boolean flag = onSaveAction();
        return flag;
      }
      else if (result == MessageDialog.ID_CANCEL) {
        return false;
      }
    }
    return true;
  }

  /**
   * ����ʵ�ָ÷���������ҵ�����ı��⡣
   * 
   * @version (00-6-6 13:33:25)
   * @return java.lang.String
   */
  public String getTitle() {
    // String title = getBillCardPanel().getBillData().getTitle();
    // if(title==null || title.trim().length()<=0)
    // title = "�˷Ѽ۸��";
    return nc.ui.ml.NCLangRes.getInstance().getStrByID("40140216",
        "UPT40140216-000014")/* @res "�˷Ѽ۸��" */;
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2002-5-10 11:16:28)
   */
  protected void initFixSubMenuButton() {

    aryButtonGroup = new ButtonObject[] {
        boEdit, boAddLine, boDelLine, boCopyLine, boPasteLine, boSave,
        boCancel,// boFind,
        boPrintPreview, boPrint, boQuery, boSort, getBoOpenDM016(),
        getBoFeeItemPrice()
    };
  }

  public void initialize() {
  }

  public void initializeNew() {

    setBillTypeCode(DMBillTypeConst.m_delivBasePrice);

    String[] itemPriceKeys = {
      "dbaseprice"
    };
    setPriceItemKeys(itemPriceKeys);
    super.initialize();
    switchButtonStatus(DMBillStatus.CardView);

    getBillCardPanel().setHeadItem("vdoname", getDelivOrgName());
    ((UIRefPane) getBillCardPanel().getBodyItem("fromarea").getComponent())
        .setReturnCode(false);
    ((UIRefPane) getBillCardPanel().getBodyItem("toarea").getComponent())
        .setReturnCode(false);
    ((UIRefPane) getBillCardPanel().getBodyItem("packsort").getComponent())
        .setReturnCode(false);
    ((UIRefPane) getBillCardPanel().getBodyItem("vfromaddress").getComponent())
        .setReturnCode(false);
    ((UIRefPane) getBillCardPanel().getBodyItem("vtoaddress").getComponent())
        .setReturnCode(false);

    UIRefPane refPane = (UIRefPane) getBillCardPanel().getBodyItem(
        "vsendtypecode").getComponent();
    refPane.getRefModel().setWherePart(
        "  pk_corp in (" + getStrCorpIDsOfDelivOrg() + ", '"
            + getClientEnvironment().getGroupId() + "') ");

    UIRefPane refPaneRoute = (UIRefPane) getBillCardPanel().getBodyItem(
        "vroute").getComponent();
    ((nc.ui.dm.pub.ref.RouteRefModel) (refPaneRoute.getRefModel()))
        .setDelivOrgPK(getDelivOrgPK());
    refPaneRoute.setReturnCode(false);

    UIRefPane refPaneVhcltype = (UIRefPane) getBillCardPanel().getBodyItem(
        "vvhcltypecode").getComponent();
    ((nc.ui.dm.pub.ref.VehicletypeRefModel) refPaneVhcltype.getRefModel())
        .setDelivOrgPK(getDelivOrgPK());

    UIRefPane refPaneTrancust = (UIRefPane) getBillCardPanel().getBodyItem(
        "vtranscustcode").getComponent();
    ((nc.ui.dm.pub.ref.TrancustRefModel) refPaneTrancust.getRefModel())
        .setDelivOrgPK(getDelivOrgPK());
    // ((nc.ui.dm.pub.ref.VehicletypeRefModel)refPane.getRefModel()).setDelivOrgPK(getDelivOrgPK());
    // nc.ui.dm.pub.ref.TrancustRef transcustPane = new
    // nc.ui.dm.pub.ref.TrancustRef(getDelivOrgPK());
    // getBillCardPanel().getBodyItem("pk_transcust").setComponent(transcustPane);

    UIRefPane invPane = (UIRefPane) getBillCardPanel().getBodyItem("vinvcode")
        .getComponent();
    invPane.setIsCustomDefined(true);
    invPane.setRefType(2);
    invPane.setRefModel(new nc.ui.dm.pub.ref.InvbaseRefModel());

    // /////////////////////////////ty

    // �Ƽ�����
    BillItem bm = getBillCardPanel().getBodyItem("ipricetype");
    nc.ui.pub.beans.UIComboBox comHeadItem = (nc.ui.pub.beans.UIComboBox) bm
        .getComponent();
    int count = comHeadItem.getItemCount();
    if (count == 0) {
      comHeadItem.setTranslate(true);
      bm.setWithIndex(true);
      comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UC000-0004106")/* @res "����" */);
      comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UC000-0000198")/* @res "����" */);
      comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40140216", "UPP40140216-000099")/* @res "����" */);
      comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UC000-0002282")/* @res "����" */);
      comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UC000-0000248")/* @res "���" */);
      // �Ƽ����������������أ����������֡��������
      // comHeadItem.addItem("���");
      // comHeadItem.addItem("�����");
      // comHeadItem.addItem("�������");
    }

    // ��������
    bm = getBillCardPanel().getBodyItem("iuplimittype");
    comHeadItem = (nc.ui.pub.beans.UIComboBox) bm.getComponent();
    count = comHeadItem.getItemCount();
    if (count == 0) {
      comHeadItem.setTranslate(true);
      bm.setWithIndex(true);
      comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40140216", "UPP40140216-000100")/* @res "��" */);
      comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UC000-0004106")/* @res "����" */);
    }

    // ����ֵ
    bm = getBillCardPanel().getBodyItem("nuplimitnum");
    ((nc.ui.pub.beans.UIRefPane) bm.getComponent()).setMinValue(0.0);
    if (BD501 != null)
      bm.setDecimalDigits(BD501.intValue());

    // �����޺�ļ۸�
    bm = getBillCardPanel().getBodyItem("noveruplmtprice");
    ((nc.ui.pub.beans.UIRefPane) bm.getComponent()).setMinValue(0.0);
    if (BD505 != null)
      bm.setDecimalDigits(BD505.intValue());

    // /////////////////////////////
  }

  /**
   * ���Ի�������
   * =========================================================================================
   * ˵���� ��Ҫ�������õ����ϵĶ����˵���ť��Ĭ�ϰ�ť��������������� ����ṹ�� //��Ƭ��ť ButtonObject[]
   * aryButtonGroup = {boBrowse,boEdit,boSave,
   * boCancel,boAction,boLine,boPrint,boAssistant}; this.aryButtonGroup =
   * aryButtonGroup; //�б�ť ButtonObject[] aryListButtonGroup =
   * {boBusiType,boBrowse,boCard,boEdit,boAction,boPrint,boAssistant};
   * this.aryListButtonGroup = aryListButtonGroup;
   * =========================================================================================
   */
  public void initVariable() {
    super.initVariable();
    // ������ʾ״̬
    setShowState(DMBillStatus.Card);// "��");

  }

  /**
   * ������޸� �������ڣ�(2002-5-31 9:36:55)
   * 
   * @param row
   *          int
   */
  private void afterClassEdit(Object value, int row) {

    afterRefEdit(value, row, "vclasscode", "pk_transcontainer", "vclassname");
    afterRefEdit("", row, "vvhcltypecode", "pk_vehicletype", "vvhcltypename");
  }

  /**
   * �༭���¼�����
   * =========================================================================================
   * ǰ�᣺ ÿһ�༭���������Ӧһ������������ʽΪafterXXXEdit������XXXΪ�༭�ؼ��� ��༭�ͻ���afterCustomerEdit
   * ����ṹ�� if (e.getPos() == BillItem.HEAD){ //���� if
   * (e.getKey().equals("ccustomerid")){ afterCustomerEdit(e); } } if
   * (e.getPos() == BillItem.BODY){ //������� ������� if
   * (e.getKey().equals("cinventorycode")){ afterInventoryEdit(e); } }
   * =========================================================================================
   */
  public void afterEdit(BillEditEvent e) {

    if (e.getPos() == BillItem.HEAD) {
    }
    else if (e.getPos() == BillItem.BODY) {
      // ������
      if (e.getKey().equals("vtranscustcode")) {
        afterTranscustEdit(e.getValue(), e.getRow());
      }
      // ����
      else if (e.getKey().equals("vvhcltypecode")) {
        afterVhcltypeEdit(e.getValue(), e.getRow());
      }
      // �����
      else if (e.getKey().equals("vclasscode")) {
        afterClassEdit(e.getValue(), e.getRow());
      }
      // ���˷�ʽ
      else if (e.getKey().equals("vsendtypecode")) {
        afterSendtypeEdit(e.getValue(), e.getRow());
      }
      // �������
      else if (e.getKey().equals("vinvclasscode")) {
        afterInvclassEdit(e.getValue(), e.getRow());
      }
      // ���
      else if (e.getKey().equals("vinvcode")) {
        afterInvEdit(e.getValue(), e.getRow());
      }
      // ����վ
      else if (e.getKey().equals("fromarea")) {
        afterFromAreaEdit(e.getValue(), e.getRow());
      }
      // ����վ
      else if (e.getKey().equals("toarea")) {
        afterToAreaEdit(e.getValue(), e.getRow());
      }
      // ��װ����
      else if (e.getKey().equals("packsort")) {
        afterPackSortEdit(e.getValue(), e.getRow());
      }
      else if (e.getKey().equals("vfromaddress")) { // �����ص�
        UIRefPane currentRef = ((UIRefPane) getBillCardPanel().getBodyItem(
            e.getKey()).getComponent());
        getBillCardPanel()
            .setBodyValueAt(currentRef.getRefPK(),
                getBillCardPanel().getBillTable().getSelectedRow(),
                "pkfromaddress");
      }
      else if (e.getKey().equals("vtoaddress")) { // �����ص�
        UIRefPane currentRef = ((UIRefPane) getBillCardPanel().getBodyItem(
            e.getKey()).getComponent());
        getBillCardPanel().setBodyValueAt(currentRef.getRefPK(),
            getBillCardPanel().getBillTable().getSelectedRow(), "pktoaddress");
      }
      // ����վ
      else if (e.getKey().equals("vroute")) {
        afterRouteEdit(e.getValue(), e.getRow());
      }
      // �Ƽ�����
      else if (e.getKey().equals("ipricetype")) {
        afterIPriceTypeEdit(e.getValue(), e.getRow());
      }
      // �Ƿ��������μ�
      else if (e.getKey().equals("bsltfrmlevel")) {
        afterBSltfrmlevelEdit(e.getValue(), e.getRow());
      }
      // ��������
      else if (e.getKey().equals("iuplimittype")) {
        afterIUplimittype(e.getValue(), e.getRow());
      }
    }
  }

  /**
   * ��������޸� �������ڣ�(2002-5-31 9:36:55)
   * 
   * @param row
   *          int
   */
  private void afterInvclassEdit(Object value, int row) {

    afterRefEdit(value, row, "vinvclasscode", "pk_invclass", "vinvclassname");
    afterRefEdit("", row, "vinvcode", "pk_inventory", "vinvname");
  }

  /**
   * ����޸� �������ڣ�(2002-5-31 9:36:55)
   * 
   * @param row
   *          int
   */
  private void afterInvEdit(Object value, int row) {
    afterRefEdit(value, row, "vinvcode", "pk_inventory", "vinvname");
    afterRefEdit("", row, "vinvclasscode", "pk_invclass", "vinvclassname");
  }

  /**
   * ���˷�ʽ�޸� �������ڣ�(2002-5-31 9:36:55)
   * 
   * @param row
   *          int
   */
  private void afterSendtypeEdit(Object value, int row) {

    afterRefEdit(value, row, "vsendtypecode", "pk_sendtype", "vsendtypename");
  }

  /**
   * �������޸� �������ڣ�(2002-5-31 9:36:55)
   * 
   * @param row
   *          int
   */
  private void afterTranscustEdit(Object value, int row) {

    afterRefEdit(value, row, "vtranscustcode", "pk_transcust", "vtranscustname");
  }

  /**
   * �����޸� �������ڣ�(2002-5-31 9:36:55)
   * 
   * @param row
   *          int
   */
  private void afterVhcltypeEdit(Object value, int row) {
    afterRefEdit(value, row, "vvhcltypecode", "pk_vehicletype", "vvhcltypename");
    afterRefEdit("", row, "vclasscode", "pk_transcontainer", "vclassname");
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2002-5-27 14:07:28)
   */
  public void onAddLine() {
	  billstatus = 1;
    super.onAddLine();
    int row = getBillCardPanel().getRowCount() - 1;
    getBillCardPanel().setBodyValueAt(getDelivOrgPK(), row, "pkdelivorg");
    // getBillCardPanel().
    setRowEdit(row);

    String message = NCLangRes.getInstance().getStrByID("common", "UCH036");
    /* @res���гɹ� */
    this.showHintMessage(message);
  }

  /**
   * �������롣 �������ڣ�(2001-4-21 10:36:57)
   */
  public void onCancel() {
    super.onCancel();
    
    getBillCardPanel().getBillModel().execLoadFormula();
    getBillCardPanel().updateValue();
    getBillCardPanel().updateUI();
     // ���ñ༭��־
    m_bEdit = false;
    String message = NCLangRes.getInstance().getStrByID("common", "UCH008");
    /* @resȡ���ɹ� */
    this.showHintMessage( message );    
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2002-5-27 14:21:22)
   */
  public void onDelLine() {
      int row = getBillCardPanel().getBodyPanel().getTable().getSelectedRow();
      Object otemp = null;
      billstatus = 3;
      String delPK = null;
      if (row >= 0) {
        otemp = getBillCardPanel().getBodyValueAt(row, "bsltfrmlevel");
        delPK = (String) getBillCardPanel().getBodyValueAt(row, "pk_basicprice");
        if (otemp != null
            && (new nc.vo.pub.lang.UFBoolean(otemp.toString().trim()))
                .booleanValue()) {

          String pk_basicprice = (String) getBillCardPanel().getBodyValueAt(row,
              "pk_basicprice");
          if (pk_basicprice != null && isHaveQuantityLevel(pk_basicprice)) {
            if (MessageDialog.showYesNoDlg(this,null,nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "40140216", "UPP40140216-000101")/*
                                                   * @res
                                                   * "ɾ����,������ԭ����������۸�ɾ��,�Ƿ������"
                                                   */,MessageDialog.ID_NO) == nc.ui.pub.beans.MessageDialog.ID_NO)
              return;
          }

        }

      }
      
      
      super.onDelLine();
        /********************add by yhj 2014-02-22 START***********************/
      if(delPK != null){
        String sql = "select * from pda_basicdoc where bdid='"+ delPK + "' and nvl(dr,0)=0 and sysflag='Y'";
        BasicdocVO checkVO = null;
        try {
          checkVO = (BasicdocVO) iUAPQueryBS.executeQuery(sql,new BeanProcessor(BasicdocVO.class));
          if (checkVO != null) {
            iserviceDao.deleteByWhereClause(BasicdocVO.class,"bdid='" + delPK + "' and sysflag='N'");
            checkVO.setProctype("delete");
            iserviceDao.update(checkVO);
          }
        } catch (BusinessException e) {
          e.printStackTrace();
        }
      }
          
          
//        }
        /********************add by yhj 2014-02-22 END***********************/

      String message = NCLangRes.getInstance().getStrByID("common", "UCH037");
      /* @resɾ�гɹ� */
      this.showHintMessage(message);
    }

  
  /**
   * @author yhj 2014-02-22
   * @param dsvo
   * @return string
   * @throws Exception
   */
  public String getCustName(DmBasepriceVO dsvo) throws Exception{
	  
	  	StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("select distinct bd_cubasdoc.custname");
		stringBuffer.append("   from bd_cubasdoc");
		stringBuffer.append("  inner join dm_trancust");
		stringBuffer.append("     on bd_cubasdoc.pk_cubasdoc = dm_trancust.pkcusmandoc");
		stringBuffer.append("     and nvl(bd_cubasdoc.dr,0) = 0");
		stringBuffer.append("     and nvl(dm_trancust.dr,0) = 0");
		stringBuffer.append("  inner join bd_cumandoc");
		stringBuffer.append("     on bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc");
		stringBuffer.append("     and nvl(bd_cumandoc.dr,0) = 0");
		stringBuffer.append("     where bd_cumandoc.custflag in ('0','2') and dm_trancust.dr = 0 and bd_cumandoc.pk_corp ='"+dsvo.getPkcorp()+"'");
		stringBuffer.append("     and dm_trancust.pk_trancust='"+dsvo.getPk_transcust()+"';");
		
		String custname = (String)iUAPQueryBS.executeQuery(stringBuffer.toString(), new ColumnProcessor());
		 return custname;
		
  }
  
  /**
   * �޸ġ� �������ڣ�(2002-5-16 15:12:48)
   */
  public void onEdit() {
    super.onEdit();

    getBillCardPanel().setHeadItem("vdoname", getDelivOrgName());

    setBodyEdit();
    //add by yhj 2014-05-07
    billstatus = 2;
    //end
    // ���ñ༭��־
    m_bEdit = true;
    // ((nc.ui.dm.pub.cardpanel.DMBillCardPanel)
    // getBillCardPanel()).autoAddLineToRowLimit();

    String message = NCLangRes.getInstance().getStrByID("common", "UCH027");
    /* @res�����޸� */
    this.showHintMessage(message);
  }

  /**
   * �����������в���----ճ���е���β author : hxb �������ڣ�(2006-8-14)
   * 
   * @return null
   * @param null
   * @exception null
   */
  public void onPasteToTail() {
    // ճ��ǰ�ı���������
    int iRowCount0 = getBillCardPanel().getBodyPanel().getTableModel()
        .getRowCount();

    // ճ����ѡ�����
    getBillCardPanel().pasteLineToTail();

    // ճ����ı���������
    int iRowCount1 = getBillCardPanel().getBillModel().getRowCount();

    // Ҫճ�����е�������
    int iSetCount = iRowCount1 - iRowCount0;
    int iTotalCount = iRowCount1;
    int iCurRow = 0;

    int iPreviousRow = iTotalCount - iSetCount - 1;
    String[] saKey = {
      "pk_basicprice"
    };

    for (int i = iPreviousRow; i < iTotalCount - 1; i++) {
      iCurRow = iPreviousRow + 1;

      TOBillTool.clearRowValues(getBillCardPanel(), iCurRow, saKey);
    }

  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2002-5-23 15:41:19)
   */
  public void onSave() {
    onSaveAction();
  }

  private boolean onSaveAction() {
    try {
      getBillCardPanel().tableStopCellEditing();
      getBillCardPanel().stopEditing();

      String[] itemkey = {
          "pk_basicprice", "pk_transcust", "pk_vehicletype",
          "pk_transcontainer", "pk_sendtype", "pk_invclass", "pk_inventory",
          "vpriceunit", "dbaseprice"
      };
      // ����VO
      BillCardPanel billCard = getBillCardPanel();
      super.filterNullLine(itemkey, billCard);
      // У���������
      if (!checkInputVO()) {
        return false;
      }
      DmBasepriceAggVO dvo = new DmBasepriceAggVO();

      for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
        getBillCardPanel().setBodyValueAt(getDelivOrgPK(), i, "pkdelivorg");
      }

      dvo = (DmBasepriceAggVO) getBillCardPanel().getBillValueChangeVO("nc.vo.dm.dm004.DmBasepriceAggVO",
          "nc.vo.dm.dm004.DmBasepriceVO", "nc.vo.dm.dm004.DmBasepriceVO");
      //
      DmBasepriceVO[] bodyVO = (DmBasepriceVO[])dvo.getChildrenVO();
      for (int i = 0; i < bodyVO.length; i++) {
        Object oTemp = bodyVO[i].getAttributeValue("pkpacksort");
        if (oTemp == null || oTemp.toString().trim().length() == 0) {
          bodyVO[i].setAttributeValue("pkpacksort", "");
          bodyVO[i].setAttributeValue("packsort", "");
        }
        oTemp = bodyVO[i].getAttributeValue("pkfromarea");
        if (oTemp == null || oTemp.toString().trim().length() == 0) {
          bodyVO[i].setAttributeValue("pkfromarea", "");
          bodyVO[i].setAttributeValue("fromarea", "");
        }
        oTemp = bodyVO[i].getAttributeValue("pktoarea");
        if (oTemp == null || oTemp.toString().trim().length() == 0) {
          bodyVO[i].setAttributeValue("pktoarea", "");
          bodyVO[i].setAttributeValue("toarea", "");
        }
      }
      //

//      if (!checkVO(((nc.vo.pub.AggregatedValueObject) dvo),
//          (nc.ui.dm.pub.cardpanel.DMBillCardPanel) getBillCardPanel())) {
//        return false;
//      }
      DmBasepriceVO ddvos = null;

      for (int i = 0; i < dvo.getChildrenVO().length; i++) {
        ddvos = (DmBasepriceVO)dvo.getChildrenVO()[i];
        ddvos.setAttributeValue("pkcorp", getCorpID());
      }
      // �õ�userid ��zlf��
      dvo.getParentVO().setAttributeValue("userid", getUserID());
      // dvo.getHeaderVO().setAttributeValue("pk_basicprice",dvo.);
      nc.ui.dm.dm007.DmUtils dui = new nc.ui.dm.dm007.DmUtils();
      dui.convertToArrayList(dvo);
      DmBasepriceAggVO dvosave = BasepriceHelper.save(dvo);
      
      /***********************add by yhj 2014-02-22 START************************************/
      if(billstatus == 1){//���������У��������
        DmBasepriceVO[] dsvos = (DmBasepriceVO[]) dvosave.getChildrenVO();
        if(dsvos != null && dsvos.length > 0){
        for (int i = 0; i < dsvos.length; i++) {
            DmBasepriceVO dsvo = dsvos[i];
            if (dsvo.getMemo() != null && dsvo.getMemo().startsWith("PDA")) {
            BasicdocVO vo = new BasicdocVO();
            String custname = getCustName(dsvo);
          if(custname != null){
            vo.setBdname(custname);
          }
            vo.setPk_corp(dsvo.getPkcorp());
            vo.setBdid(dsvo.getPk_basicprice());//�˴�ȥ���۱������
            vo.setBdtype("CYS");
            vo.setProctype("add");
            vo.setSysflag("Y");
            iserviceDao.insert(vo);
          }
          }  
      }
      }else if(billstatus == 2){//�޸ı������
        DmBasepriceVO[] dsvos = (DmBasepriceVO[]) dvosave.getChildrenVO();
        if(dsvos != null && dsvos.length > 0){
          for (int i = 0; i < dsvos.length; i++) {
            DmBasepriceVO dsvo = dsvos[i];
            String sql = "select * from pda_basicdoc where bdid='"+ dsvo.getPk_basicprice() + "' and nvl(dr,0)=0 and sysflag='Y'";
              BasicdocVO checkVO = (BasicdocVO) iUAPQueryBS.executeQuery(sql,new BeanProcessor(BasicdocVO.class));
              boolean pdaFlag = dsvo.getMemo() != null && dsvo.getMemo().startsWith("PDA");
            if (checkVO == null && pdaFlag) {
              BasicdocVO vo = new BasicdocVO();
              String custname = getCustName(dsvo);
              if(custname != null){
                vo.setBdname(custname);
              }
              vo.setBdid(dsvo.getPk_basicprice());
              vo.setBdtype("CYS");
              vo.setPk_corp(dsvo.getPkcorp());
              vo.setProctype("add");
              vo.setSysflag("Y");
              iserviceDao.insert(vo);
            } else if (checkVO != null && !checkVO.getBdname().equals(dsvo.getPk_transcust()) && pdaFlag) {
              iserviceDao.deleteByWhereClause(BasicdocVO.class, "bdid='"+ dsvo.getPk_basicprice() + "' and sysflag='N'");
              String custname = getCustName(dsvo);
              if(custname != null){
                checkVO.setBdname(custname);
              }
              iserviceDao.update(checkVO);
            } else if (checkVO != null && !pdaFlag) {
              iserviceDao.deleteByWhereClause(BasicdocVO.class, "bdid='"+ dsvo.getPk_basicprice() + "' and sysflag='N'");
              checkVO.setProctype("delete");
              iserviceDao.update(checkVO);
            }
          }
        }
      }
      /***********************add by yhj 2014-02-22 START************************************/

      // �ϲ�
      dui.combineOtherVO(dvosave,dvo);
      this.showHintMessage(NCLangRes.getInstance().getStrByID("common",
          "UCH005") /* @res "����ɹ�" */);

      afterSave(dvo);

      // ���ñ༭��־
      m_bEdit = false;

    }
    catch (Exception e) {
      ExceptionUITools tool = new ExceptionUITools();
      tool.showMessage(e, this);
      return false;
    }
    return true;
  }

  /**
   * ������ԭ��ı���档 �������ڣ�(2001-11-15 9:18:13)
   * 
   * @param bdData
   *          nc.ui.pub.bill.BillData
   */
  protected void setCardPanelByOther(BillData bdData) {
    // ((UIRefPane)
    // bdData.getBodyItem("vinvcode").getComponent()).setWhereString("pk_corp='0001'");
  }

  /**
   * �����ߣ����� ���ܣ�������࣬�����������һ��������д��У�� ������ ���أ� ���⣺ ���ڣ�(2001-6-17 ���� 5:17)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private boolean checkInputVO() {
    DmBasepriceAggVO dvo = new DmBasepriceAggVO();
    dvo = (DmBasepriceAggVO) getBillCardPanel().getBillValueChangeVO("nc.vo.dm.dm004.DmBasepriceAggVO",
        "nc.vo.dm.dm004.DmBasepriceVO", "nc.vo.dm.dm004.DmBasepriceVO");
    String sErrorMessage = "";
    String svtranscustcode = null, svvhcltypecode = null;
    String svclasscode = null, svsendtypecode = null;
    String svinvclasscode = null, svinvcode = null, svpriceunit = null;
    String fromarea = null, toarea = null, packing = null;
    String pkfromaddress = null, pktoaddress = null;
    //eric ������Ч���ڣ�ʧЧ����
    String effectdate = null; String expirationdate = null;
    Hashtable htTable = new Hashtable();
    String sRowString = "";
    String sCheckvalue = "";
    int iRows = 0;
    // �Ƽ�����
    Integer ipricetype = null;
    try {
      // У��ǿ���Ŀ
//      if (!super.checkVO(dvo, (DMBillCardPanel) getBillCardPanel()))
//        return false;
      String sSeprateValue = "^@!";
      ArrayList<HashMap> al = new ArrayList<HashMap>();
      for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
        svtranscustcode = sSeprateValue;
        svvhcltypecode = sSeprateValue;
        svclasscode = sSeprateValue;
        svsendtypecode = sSeprateValue;
        svinvclasscode = sSeprateValue;
        svinvcode = sSeprateValue;
        svpriceunit = sSeprateValue;
        fromarea = sSeprateValue;
        toarea = sSeprateValue;
        packing = sSeprateValue;
        pkfromaddress = sSeprateValue;
        pktoaddress = sSeprateValue;
        effectdate = sSeprateValue;
        expirationdate = sSeprateValue;
        iRows++;
        if(getBillCardPanel().getBodyValueAt(i, "vtranscustcode")==null){
          showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140216",
          "UPP40140216-000120"));
          return false;
        }
        // if ((getBillCardPanel().getBodyValueAt(i, "dbaseprice") == null
        // || getBillCardPanel().getBodyValueAt(i,
        // "dbaseprice").toString().length() == 0)
        // && (getBillCardPanel().getBodyValueAt(i, "dvehicleprice") == null
        // || getBillCardPanel().getBodyValueAt(i,
        // "dvehicleprice").toString().length() == 0)) {
        // sErrorMessage = "��" + iRows + "�еĻ��ۺ������۸���ͬʱΪ�ա�";
        // showErrorMessage(sErrorMessage);
        // return false;
        // }
        // else if ((getBillCardPanel().getBodyValueAt(i, "dbaseprice") != null
        // && getBillCardPanel().getBodyValueAt(i,
        // "dbaseprice").toString().length() != 0)
        // && (getBillCardPanel().getBodyValueAt(i, "dvehicleprice") != null
        // && getBillCardPanel().getBodyValueAt(i,
        // "dvehicleprice").toString().length() != 0)) {
        // sErrorMessage = "��" + iRows + "�еĻ��ۺ������۸���ͬʱ¼�롣";
        // showErrorMessage(sErrorMessage);
        // return false;
        // }
        ipricetype = getBodyComBoxIndex(i, "ipricetype");
        // ---------------�����۸�У��
        // if (getBillCardPanel().getBodyValueAt(i, "dvehicleprice") != null
        // && getBillCardPanel().getBodyValueAt(i,
        // "dvehicleprice").toString().length() != 0) {
        if(ipricetype==null)
        {
          showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140216",
            "UPP40140216-000119"));
          return false;
        }
        else if (ipricetype.intValue() == ConstForBasePrice.IPriceType_WholeVehicle
            .intValue()) {
          // if (getBillCardPanel().getBodyValueAt(i, "vtranscustcode") == null
          // || getBillCardPanel().getBodyValueAt(i,
          // "vtranscustcode").toString().length() == 0){
          // sErrorMessage = sErrorMessage + "��" + iRows + "��¼�������۸�ʱ�����̲���Ϊ�ա�\n";
          // }
          // if (getBillCardPanel().getBodyValueAt(i, "vsendtypecode") == null
          // || getBillCardPanel().getBodyValueAt(i,
          // "vsendtypecode").toString().length() == 0){
          // sErrorMessage = sErrorMessage + "��" + iRows +
          // "��¼�������۸�ʱ���˷�ʽ����Ϊ�ա�\n";
          // }
          // if (getBillCardPanel().getBodyValueAt(i, "vroute") == null ||
          // getBillCardPanel().getBodyValueAt(i, "vroute").toString().length()
          // == 0){
          // sErrorMessage = sErrorMessage + "��" + iRows + "��¼�������۸�ʱ·�߲���Ϊ�ա�\n";
          // }
          // if (getBillCardPanel().getBodyValueAt(i, "vvhcltypecode") == null
          // || getBillCardPanel().getBodyValueAt(i,
          // "vvhcltypecode").toString().length() == 0){
          // sErrorMessage = sErrorMessage + "��" + iRows + "��¼�������۸�ʱ���Ͳ���Ϊ�ա�\n";
          // }
          // if (getBillCardPanel().getBodyValueAt(i, "fromarea") != null ||
          // getBillCardPanel().getBodyValueAt(i,
          // "fromarea").toString().length() != 0){
          // sErrorMessage = sErrorMessage + "��" + iRows + "��¼�������۸�ʱ����վ����¼�롣\n";
          // }
          // if (getBillCardPanel().getBodyValueAt(i, "toarea") != null ||
          // getBillCardPanel().getBodyValueAt(i, "toarea").toString().length()
          // != 0){
          // sErrorMessage = sErrorMessage + "��" + iRows + "��¼�������۸�ʱ����վ����¼�롣\n";
          // }
          // if (getBillCardPanel().getBodyValueAt(i, "vfromaddress") != null ||
          // getBillCardPanel().getBodyValueAt(i,
          // "vfromaddress").toString().length() != 0){
          // sErrorMessage = sErrorMessage + "��" + iRows +
          // "��¼�������۸�ʱ�����ص㲻��¼�롣\n";
          // }
          // if (getBillCardPanel().getBodyValueAt(i, "vtoaddress") != null ||
          // getBillCardPanel().getBodyValueAt(i,
          // "vtoaddress").toString().length() != 0){
          // sErrorMessage = sErrorMessage + "��" + iRows +
          // "��¼�������۸�ʱ�����ص㲻��¼�롣\n";
          // }
        }
        // ---------------����У��
        else {
          // �����̱���
          if (getBillCardPanel().getBodyValueAt(i, "vtranscustcode") != null)
            svtranscustcode = getBillCardPanel().getBodyValueAt(i,
                "vtranscustcode").toString().trim();
          // ���ͱ���
          if (getBillCardPanel().getBodyValueAt(i, "vvhcltypecode") != null)
            svvhcltypecode = getBillCardPanel().getBodyValueAt(i,
                "vvhcltypecode").toString().trim();
          // ����ֱ���
          if (getBillCardPanel().getBodyValueAt(i, "vclasscode") != null)
            svclasscode = getBillCardPanel().getBodyValueAt(i, "vclasscode")
                .toString().trim();
          // ���˷�ʽ����
          if (getBillCardPanel().getBodyValueAt(i, "vsendtypecode") != null)
            svsendtypecode = getBillCardPanel().getBodyValueAt(i,
                "vsendtypecode").toString().trim();
          // ����������
          // if (getBillCardPanel().getBodyValueAt(i, "vinvclasscode") != null)
          // svinvclasscode = getBillCardPanel().getBodyValueAt(i,
          // "vinvclasscode").toString().trim();
          // �������
          // if (getBillCardPanel().getBodyValueAt(i, "vinvcode") != null)
          // svinvcode = getBillCardPanel().getBodyValueAt(i,
          // "vinvcode").toString().trim();
          // �۸�λ
          // if (getBillCardPanel().getBodyValueAt(i, "vpriceunit") != null)
          // svpriceunit = getBillCardPanel().getBodyValueAt(i,
          // "vpriceunit").toString().trim();
          // ����վ
          // if (getBillCardPanel().getBodyValueAt(i, "fromarea") != null)
          // fromarea = getBillCardPanel().getBodyValueAt(i,
          // "fromarea").toString().trim();
          // ����վ
          // if (getBillCardPanel().getBodyValueAt(i, "toarea") != null)
          // toarea = getBillCardPanel().getBodyValueAt(i,
          // "toarea").toString().trim();
          // ��װ����
          if (getBillCardPanel().getBodyValueAt(i, "pkpacksort") != null)
            packing = getBillCardPanel().getBodyValueAt(i, "pkpacksort")
                .toString().trim();

          // �����ص����� eric
          if (getBillCardPanel().getBodyValueAt(i, "pkfromaddress") != null)
            pkfromaddress = getBillCardPanel().getBodyValueAt(i, "pkfromaddress")
                  .toString().trim();
          // �����ص�����
          if (getBillCardPanel().getBodyValueAt(i, "pktoaddress") != null)
            pktoaddress = getBillCardPanel().getBodyValueAt(i, "pktoaddress")
                .toString().trim();
          //eric
          if (getBillCardPanel().getBodyValueAt(i, "effectdate") != null)
            effectdate = getBillCardPanel().getBodyValueAt(i, "effectdate")
                  .toString().trim();
          
          if (getBillCardPanel().getBodyValueAt(i, "expirationdate") != null)
            expirationdate = getBillCardPanel().getBodyValueAt(i, "expirationdate")
                  .toString().trim();
          //eric 
          HashMap<String,Object> hm = new HashMap<String,Object>();
          hm.put("effectdate", new UFDate(effectdate));
          hm.put("expirationdate", new UFDate(expirationdate));
          String key = svtranscustcode + svvhcltypecode
                      + svsendtypecode
                      + packing + pkfromaddress + pktoaddress ;
          hm.put("key", key);
          al.add(hm);
          //~~~~~~~~~~~~~~~~~~~~~~~~~
          sRowString = svtranscustcode + svvhcltypecode
          // + svclasscode
              + svsendtypecode
              // + svinvclasscode
              // + svinvcode
              // + svpriceunit
              // + fromarea
              // + toarea
              + packing + pkfromaddress + pktoaddress + effectdate + expirationdate;

          // �������ʹ�����벻�ܶ�Ϊ��
          sCheckvalue = svinvclasscode + svinvcode;
          // if (sCheckvalue.equals(sSeprateValue + sSeprateValue))
          // sErrorMessage = sErrorMessage + "��" + iRows +
          // "��¼������ݴ������ʹ�����벻�ܶ�Ϊ�ա�\n";

          if (sRowString.length() > 0) {

            // sRowString7��¼��ֵ������ȫ�ظ�
            if (null != htTable) {
              // У���ظ�
              if (htTable.containsValue(sRowString)) {
                String[] sValue = new String[] {
                  String.valueOf(iRows)
                };
                sErrorMessage = sErrorMessage
                    + nc.ui.ml.NCLangRes.getInstance().getStrByID("40140216",
                        "UPP40140216-000102", null, sValue)/*
                                                             * @res
                                                             * "��{0}��¼�������[������,���˷�ʽ,�����ص�,�����ص�,����,��װ����]�����������ظ���\n" +
                                                             * iRows +
                                                             * "��¼�������[������,���˷�ʽ,�����ص�,�����ص�,����,��װ����]�����������ظ���\n"
                                                             */;
              }
            }
            htTable.put(String.valueOf(i), sRowString);
          }

        
          // Modified by xhq 2002/10/06 begin
          Object oTemp = getBillCardPanel().getBodyValueAt(i, "dbaseprice");
          if (oTemp != null && oTemp.toString().trim().length() > 0) {
            String sTemp = oTemp.toString();
            if (sTemp.indexOf("-") >= 0) {
              String[] sValue = new String[] {
                String.valueOf(iRows)
              };
              sErrorMessage = sErrorMessage
                  + nc.ui.ml.NCLangRes.getInstance().getStrByID("40140216",
                      "UPP40140216-000103")/*
                                             * @res "��{0}�еĻ��۲���Ϊ����\n" + iRows +
                                             * "�еĻ��۲���Ϊ����\n"
                                             */;
            }
          }
          // Modified by xhq 2002/10/06 end

        } // end for

        if (ipricetype != null) {
          // a) ���������װ�����������
          if (ipricetype.intValue() == 1) {
            // String pkpacksort=(String)getBillCardPanel().getBodyValueAt(i,
            // "pkpacksort");
            // if(pkpacksort==null || pkpacksort.trim().length()<=0){
            // sErrorMessage = sErrorMessage + "��" + iRows +
            // "�мƼ����ݼ�Ϊ����ʱ��װ���಻��Ϊ�ա�\n";
            // }
          }
          else if (ipricetype.intValue() == 2) {
            // b) ���������͡�·�߱�������
            // String pkroute=(String)getBillCardPanel().getBodyValueAt(i,
            // "pkroute");
            String pk_vehicletype = (String) getBillCardPanel().getBodyValueAt(
                i, "pk_vehicletype");
            // if(pkroute==null || pkroute.trim().length()<=0 ||
            // pk_vehicletype==null || pk_vehicletype.trim().length()<=0)
            if (pk_vehicletype == null || pk_vehicletype.trim().length() <= 0) {
              String[] sValue = new String[] {
                String.valueOf(iRows)
              };
              sErrorMessage = sErrorMessage
                  + nc.ui.ml.NCLangRes.getInstance().getStrByID("40140216",
                      "UPP40140216-000104", null, sValue)/*
                                                           * @res
                                                           * "��{0}�мƼ����ݼ�Ϊ����ʱ���Ͳ���Ϊ�ա�\n" +
                                                           * iRows +
                                                           * nc.ui.ml.NCLangRes.getInstance().getStrByID("40140216","UPP40140216-000105")/*@res
                                                           * "�мƼ����ݼ�Ϊ����ʱ���͡�·�߲���Ϊ�ա�\n"
                                                           */;
            }
          }
          else if (ipricetype.intValue() == 5) {
            // c) ����֣�������ֱ�������
            // String
            // pk_transcontainer=(String)getBillCardPanel().getBodyValueAt(i,
            // "pk_transcontainer");
            // if(pk_transcontainer==null ||
            // pk_transcontainer.trim().length()<=0){
            // sErrorMessage = sErrorMessage + "��" + iRows +
            // "�мƼ����ݼ�Ϊ�����ʱ����ֲ���Ϊ�ա�\n";
            // }
          }
        }
      }
    //eric �ж� ʱ����Ƿ����ص�
    
    
        CheckArrayTime(al);
      
    
      if (sErrorMessage.trim().length() != 0) {
        showErrorMessage(sErrorMessage);
        return false;
      }
      else
        return true;

    }
    catch (Exception e) {
      this.showErrorMessage(e.getMessage());
      handleException(e);
      return false;
    }
  }
  @SuppressWarnings("all")
  public void CheckArrayTime(ArrayList al) throws BusinessException{
    UFDate start1 = (UFDate) ((HashMap)al.get(0)).get("effectdate");
    UFDate end1 = (UFDate) ((HashMap)al.get(0)).get("expirationdate");
    String key1 = (String) ((HashMap)al.get(0)).get("key");
    al.remove(0);
    for(int i=0 ; i<al.size() ; i++){
      UFDate start2 = (UFDate) ((HashMap)al.get(i)).get("effectdate");
      UFDate end2 = (UFDate) ((HashMap)al.get(i)).get("expirationdate");
      String key2 = (String) ((HashMap)al.get(i)).get("key");
      if(key1.equals(key2)&&checkTime(start1,end1,start2,end2))
        throw new BusinessException("�����ص�ʱ��Σ��޷�����");
      }
    if(al!=null&&al.size()>1)
      CheckArrayTime(al);
  }
  
  public boolean checkTime(UFDate start1,UFDate end1,UFDate start2 ,UFDate end2){
    if(start2.compareTo(start1)<=0&&start1.compareTo(end2)<0)
      return true;
    if(start2.compareTo(end1)<0&&end1.compareTo(end2)<=0)
      return true;
    if(start1.compareTo(start2)<=0&&start2.compareTo(end1)<0)
      return true;
    if(start1.compareTo(end2)<0&&end2.compareTo(end1)<=0)
      return true;
    else 
      return false;
  }

  // �༭��־
  private boolean m_bEdit = false;

  /**
   * ����޸� �������ڣ�(2002-5-31 9:36:55)
   * 
   * @param row
   *          int
   */
  private void afterFromAreaEdit(Object value, int row) {
    afterRefEdit(value, row, "fromarea", "pkfromarea", "fromarea");
  }

  /**
   * ����޸� �������ڣ�(2002-5-31 9:36:55)
   * 
   * @param row
   *          int
   */
  private void afterPackSortEdit(Object value, int row) {
    afterRefEdit(value, row, "packsort", "pkpacksort", "packsort");
  }

  /**
   * ����޸� �������ڣ�(2002-5-31 9:36:55)
   * 
   * @param row
   *          int
   */
  private void afterToAreaEdit(Object value, int row) {
    afterRefEdit(value, row, "toarea", "pktoarea", "toarea");
  }

  /**
   * ��ܣ�\n������ ���أ� ���⣺ ���ڣ�(2002-11-13 11:52:46) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 
   * @param event
   *          nc.ui.pub.bill.BillEditEvent
   */
  public void bodyRowChange(BillEditEvent event) {
    if ((nc.ui.pub.beans.UITable) event.getSource() == getBillCardPanel()
        .getBillTable()) {
      if (m_bEdit) {
        // �޸�

        // �����̱���
        Object pktranscust = getBillCardPanel().getBodyValueAt(event.getRow(),
            "pk_transcust");
        UIRefPane refPane = (UIRefPane) getBillCardPanel().getBodyItem(
            "vtranscustcode").getComponent();
        refPane.setPK(pktranscust);

        // ���ͱ���
        Object pkvehicletype = getBillCardPanel().getBodyValueAt(
            event.getRow(), "pk_vehicletype");
        refPane = (UIRefPane) getBillCardPanel().getBodyItem("vvhcltypecode")
            .getComponent();
        refPane.setPK(pkvehicletype);

        // ����ֱ���
        Object pktranscontainer = getBillCardPanel().getBodyValueAt(
            event.getRow(), "pk_transcontainer");
        refPane = (UIRefPane) getBillCardPanel().getBodyItem("vclasscode")
            .getComponent();
        refPane.setPK(pktranscontainer);

        // ���˷�ʽ����
        Object pksendtype = getBillCardPanel().getBodyValueAt(event.getRow(),
            "pk_sendtype");
        refPane = (UIRefPane) getBillCardPanel().getBodyItem("vsendtypecode")
            .getComponent();
        refPane.setPK(pksendtype);

        // ����������
        Object pkinvclass = getBillCardPanel().getBodyValueAt(event.getRow(),
            "pk_invclass");
        refPane = (UIRefPane) getBillCardPanel().getBodyItem("vinvclasscode")
            .getComponent();
        refPane.setPK(pkinvclass);

        // �������
        Object pkinventory = getBillCardPanel().getBodyValueAt(event.getRow(),
            "pk_inventory");
        refPane = (UIRefPane) getBillCardPanel().getBodyItem("vinvcode")
            .getComponent();
        refPane.setPK(pkinventory);

        // ��վ����
        Object pkfromarea = getBillCardPanel().getBodyValueAt(event.getRow(),
            "pkfromarea");
        refPane = (UIRefPane) getBillCardPanel().getBodyItem("fromarea")
            .getComponent();
        refPane.setPK(pkfromarea);

        // ��վ����
        Object pktoarea = getBillCardPanel().getBodyValueAt(event.getRow(),
            "pktoarea");
        refPane = (UIRefPane) getBillCardPanel().getBodyItem("toarea")
            .getComponent();
        refPane.setPK(pktoarea);

        // ��װ�������
        // Object pkpacksort = getBillCardPanel().getBodyValueAt(event.getRow(),
        // "pkpacksort");
        // refPane = (UIRefPane)
        // getBillCardPanel().getBodyItem("packsort").getComponent();
        // refPane.setPK(pkpacksort);

      }
      else {

        if (event.getRow() >= 0) {

          Object otemp = getBillCardPanel().getBodyValueAt(event.getRow(),
              "bsltfrmlevel");
          nc.vo.pub.lang.UFBoolean bsltfrmlevel = null;
          if (otemp == null || otemp.toString().trim().length() <= 0)
            bsltfrmlevel = new nc.vo.pub.lang.UFBoolean(false);
          else
            bsltfrmlevel = new nc.vo.pub.lang.UFBoolean(otemp.toString().trim());

          if (bsltfrmlevel.booleanValue()) {
            getBoOpenDM016().setEnabled(true);
          }
          else {
            getBoOpenDM016().setEnabled(false);
          }
          updateButtons();

        }

      }

    }

  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2002-6-27 16:16:11)
   */
  public boolean checkOther(nc.vo.pub.AggregatedValueObject vo)
      throws Exception {
    return true;
  }

  /**
   * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-10-6 17:08:45) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��Added
   * by xhq 2002/10/06
   */
  public void onPasteLine() {
    int nSelected[] = getBillCardPanel().getBillTable().getSelectedRows();
    if (nSelected != null && nSelected.length > 0) {
      // getBillCardPanel().pasteLineToTail();
      super.onPasteLine();
      for (int i = 0, loop = getBillCardPanel().getRowCount(); i < loop; i++) {
        if (getBillCardPanel().getBillModel().getRowState(i) == BillModel.ADD) {
          getBillCardPanel().setBodyValueAt(null, i, "pk_basicprice");
        }
      }
      setBodyEdit();
    }

    String message = NCLangRes.getInstance().getStrByID("common", "UCH040");
    /* @resճ���гɹ� */
    this.showHintMessage(message);

  }

  /**
   * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-7-24 11:08:06) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public void onPrint() {
    setNodeCode(DMBillNodeCodeConst.m_delivBasePrice);
    super.onPrint();

    String message = NCLangRes.getInstance().getStrByID("common", "UCH061");
    /* @res���ڴ�ӡ */
    this.showHintMessage(message);
  }

  /**
   * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-7-24 11:08:06) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public void onPrintPreview() {
    setNodeCode(DMBillNodeCodeConst.m_delivBasePrice);
    super.onPrintPreview();

    String message = NCLangRes.getInstance().getStrByID("common", "UCH061");
    /* @res���ڴ�ӡ */
    this.showHintMessage(message);
  }

  //
  protected ButtonObject boFeeItemPrice; // ����������۸�

  // �л�����������
  protected ButtonObject boOpenDM016;

  protected boolean bOpenDM016 = false;

  private nc.ui.dm.dm004.DM016Dlg dm016Dlg = null;

  private nc.ui.dm.dm004.DMFeeItemDlg feeitemDlg = null;

  private HashMap hsIsHaveQuantityLevel = null;

  /**
   * ���������μ��޸� �������ڣ�(2002-5-31 9:36:55)
   * 
   * @param row
   *          int
   */
  private void afterBSltfrmlevelEdit(Object value, int row) {

    Object otemp = getBillCardPanel().getBodyValueAt(row, "bsltfrmlevel");
    nc.vo.pub.lang.UFBoolean bsltfrmlevel = null;
    if (otemp == null || otemp.toString().trim().length() <= 0)
      bsltfrmlevel = new nc.vo.pub.lang.UFBoolean(false);
    else
      bsltfrmlevel = new nc.vo.pub.lang.UFBoolean(otemp.toString().trim());

    // "�Ƿ������۸�"Ϊ"��"�󣬼۸���գ��Ҳ���¼��
    if (bsltfrmlevel.booleanValue()) {
      getBillCardPanel().setBodyValueAt(null, row, "dbaseprice");
      getBillCardPanel().setCellEditable(row, "dbaseprice", false);
      // getBoOpenDM016().setEnabled(true);
    }
    else {
      String pk_basicprice = (String) getBillCardPanel().getBodyValueAt(row,
          "pk_basicprice");
      if (pk_basicprice != null && isHaveQuantityLevel(pk_basicprice)) {
        if (showYesNoMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "40140216", "UPP40140216-000106")/*
                                               * @res
                                               * "���Ƿ������۸��Ϊ��,������ԭ����������۸�ɾ��,�Ƿ������"
                                               */) == nc.ui.pub.beans.MessageDialog.ID_NO) {

          getBillCardPanel().setBodyValueAt(new nc.vo.pub.lang.UFBoolean(true),
              row, "bsltfrmlevel");
          getBillCardPanel().setBodyValueAt(null, row, "dbaseprice");
          getBillCardPanel().setCellEditable(row, "dbaseprice", false);
          return;

        }
      }
      getBillCardPanel().setCellEditable(row, "dbaseprice", true);
      // getBoOpenDM016().setEnabled(false);
    }

    // updateButtons();

  }

  /**
   * ����޸� �������ڣ�(2002-5-31 9:36:55)
   * 
   * @param row
   *          int
   */
  private void afterIPriceTypeEdit(Object value, int row) {

    Integer ipricetype = getBodyComBoxIndex(row, "ipricetype");

    nc.vo.pub.lang.UFDouble uf0 = new nc.vo.pub.lang.UFDouble(0.0);

    // ֻ�мƼ�����Ϊ����ʱ�������Ͳſ���
    if (ipricetype != null && ipricetype.intValue() == 1) {

      // setBodyComBoxValue(new Integer(1),row,"iuplimittype");
      // getBillCardPanel().setBodyValueAt(uf0,row,"nuplimitnum");
      // getBillCardPanel().setBodyValueAt(uf0,row,"noveruplmtprice");
      getBillCardPanel().setCellEditable(row, "iuplimittype", true);
      // getBillCardPanel().setCellEditable(row,"nuplimitnum",true);
      // getBillCardPanel().setCellEditable(row,"noveruplmtprice",true);

      getBillCardPanel().setBodyValueAt(null, row, "pkroute");

      getBillCardPanel().setBodyValueAt(null, row, "vroute");
      getBillCardPanel().setCellEditable(row, "vroute", false);

      // ֻ�мƼ�����Ϊ����ʱ��·�߿�¼��
    }
    else if (ipricetype != null && ipricetype.intValue() == 2) {

      getBillCardPanel().setCellEditable(row, "vroute", true);

    }
    else {

      setBodyComBoxValue(new Integer(0), row, "iuplimittype");
      getBillCardPanel().setBodyValueAt(null, row, "nuplimitnum");
      getBillCardPanel().setBodyValueAt(null, row, "noveruplmtprice");
      getBillCardPanel().setCellEditable(row, "iuplimittype", false);
      getBillCardPanel().setCellEditable(row, "nuplimitnum", false);
      getBillCardPanel().setCellEditable(row, "noveruplmtprice", false);

      getBillCardPanel().setBodyValueAt(null, row, "pkroute");
      getBillCardPanel().setBodyValueAt(null, row, "vroute");
      getBillCardPanel().setCellEditable(row, "vroute", false);

    }

  }

  /**
   * ����޸� �������ڣ�(2002-5-31 9:36:55)
   * 
   * @param row
   *          int
   */
  private void afterIUplimittype(Object value, int row) {

    Integer iuplimittype = getBodyComBoxIndex(row, "iuplimittype");

    // ֻ�мƼ�����Ϊ����ʱ�������Ͳſ���
    if (iuplimittype != null && iuplimittype.intValue() > 0) {

      getBillCardPanel().setCellEditable(row, "nuplimitnum", true);
      getBillCardPanel().setCellEditable(row, "noveruplmtprice", true);

    }
    else {

      getBillCardPanel().setBodyValueAt(null, row, "nuplimitnum");
      getBillCardPanel().setBodyValueAt(null, row, "noveruplmtprice");
      getBillCardPanel().setCellEditable(row, "nuplimitnum", false);
      getBillCardPanel().setCellEditable(row, "noveruplmtprice", false);

    }
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2002-5-16 20:33:00)
   */
  public void afterQuery(nc.vo.pub.query.ConditionVO[] voCons) {
    try {
      String sWhere = "dm_baseprice.pkdelivorg='" + getDelivOrgPK() + "' AND ";
      sWhere += getQueryConditionDlg().getWhereSQL(voCons);
      sWhere += " and dm_baseprice.dr=0 ";
      DmBasepriceVO[] ddvos = null;
      ddvos = BasepriceHelper.query(sWhere);
      if (ddvos.length <= 0) {
        getBillCardPanel().addNew();
        getBillCardPanel().updateValue();
        getBillCardPanel().updateUI();
        // �޽����Ӧ��ֵ
        return;
      }
      DmBasepriceAggVO dvo = new DmBasepriceAggVO();
      dvo.setChildrenVO(ddvos);
      dvo.setParentVO(ddvos[0]);
      afterQuery(dvo);

      // ���ñ༭��־
      m_bEdit = false;
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH009")/*
                                                                             * @res
                                                                             * ��ѯ���
                                                                             */);

    }
    catch (Exception e) {
      this.showErrorMessage(e.getMessage());
      handleException(e);
    }
  }

  /**
   * ����޸� �������ڣ�(2002-5-31 9:36:55)
   * 
   * @param row
   *          int
   */
  private void afterRouteEdit(Object value, int row) {
    afterRefEdit(value, row, "vroute", "pkroute", "vroute");
  }

  /**
   * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-9-2 10:36:31) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 
   * @return boolean
   * @param e
   *          nc.ui.pub.bill.BillEditEvent
   */
  public boolean beforeEdit(BillEditEvent e) {
    // if(e.getKey().equals("iuplimittype") || e.getKey().equals("nuplimitnum")
    // || e.getKey().equals("noveruplmtprice")){
    // //ֻ�мƼ�����Ϊ����ʱ�������Ͳſ���
    // Integer ipricetype = getBodyComBoxIndex(e.getRow(),"ipricetype");
    // if(ipricetype!=null && ipricetype.intValue()==1){
    // return true;
    // }else{
    // return false;
    // }

    // }
    return true;
  }

  /**
   * ����ʵ�ָ÷���������ҵ�����ı��⡣
   * 
   * @version (00-6-6 13:33:25)
   * @return java.lang.String
   */
  public Integer getBodyComBoxIndex(int row, String key) {
    Integer index = null;
    Object otemp = getBillCardPanel().getBodyValueAt(row, key);
    if (otemp != null)
      index = (Integer) getBillCardPanel().getBodyItem(key).converType(otemp);

    return index;
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2002-5-27 14:21:22)
   */
  public ButtonObject getBoFeeItemPrice() {
    if (boFeeItemPrice == null)
      boFeeItemPrice = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40140216", "UPT40140216-000017")/* @res "����������۸�" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40140216",
              "UPT40140216-000017")/* @res "����������۸�" */, 0, "����������۸�"); /*-=notranslate=-*/
    return boFeeItemPrice;
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2002-5-27 14:21:22)
   */
  public ButtonObject getBoOpenDM016() {
    if (boOpenDM016 == null)
      boOpenDM016 = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40140216", "UPT40140216-000016")/* @res "��������" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40140216",
              "UPT40140216-000016")/* @res "��������" */, 0, "��������"); /*-=notranslate=-*/
    return boOpenDM016;
  }

  /**
   * �޸ġ� �������ڣ�(2002-5-16 15:12:48)
   */
  public nc.ui.dm.dm004.DM016Dlg getDm016Dlg() {

    if (dm016Dlg == null) {
      dm016Dlg = new nc.ui.dm.dm004.DM016Dlg(this);
    }
    return dm016Dlg;

  }

  /**
   * �޸ġ� �������ڣ�(2002-5-16 15:12:48)
   */
  public nc.ui.dm.dm004.DMFeeItemDlg getFeeItemDlg() {

    if (feeitemDlg == null) {
      feeitemDlg = new nc.ui.dm.dm004.DMFeeItemDlg(this, BD505);
    }
    return feeitemDlg;

  }

  /**
   * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-9-3 14:27:25) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 
   * @return java.util.HashMap
   */
  public HashMap getHsIsHaveQuantityLevel() {
    if (hsIsHaveQuantityLevel == null)
      hsIsHaveQuantityLevel = new HashMap();
    return hsIsHaveQuantityLevel;
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2002-5-16 15:46:54)
   * 
   * @return nc.ui.dm.pub.DMQueryConditionDlg
   */
  public DMQueryConditionDlg getQueryConditionDlg() {
    if (queryConditionDlg == null) {
      queryConditionDlg = new QueryConditionDlg(this);
      queryConditionDlg.setTempletID(getCorpID(),
          nc.vo.dm.pub.DMBillNodeCodeConst.m_delivBasePrice, getUserID(), null);
      queryConditionDlg
          .setDefaultCloseOperation(DMQueryConditionDlg.HIDE_ON_CLOSE);

      // ���
      // UIRefPane inventoryRef = new UIRefPane();
      // inventoryRef.setRefType(2); //����ṹ
      // inventoryRef.setIsCustomDefined(true);
      // inventoryRef.setRefModel(new nc.ui.dm.pub.ref.InvbaseRefModel());
      // String sAgentCorpIds =
      // StringTools.getStrIDsOfArry(nc.vo.dm.pub.StaticMemoryVariable.AgentCorpIDsofDelivOrg);
      // inventoryRef.setWhereString("bd_invbasdoc.pk_corp in (" + sAgentCorpIds
      // + ", '" + getGroupID() + "')");
      // queryConditionDlg.setValueRef("dm_quantitylevel_h.pkinv",
      // inventoryRef);
      // queryConditionDlg.setValueRef("dm_delivdaypl.pkinvname", inventoryRef);
      // queryConditionDlg.setValueRef("bd_invbasdoc.invname", inventoryRef);

      queryConditionDlg.setCombox("dm_baseprice.ipricetype", new String[][] {
          {
              "", ""
          },
          {
              0 + "",
              nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                  "UC000-0004106")
          /* @res "����" */},
          {
              1 + "",
              nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                  "UC000-0000198")
          /* @res "����" */},
          {
              2 + "",
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40140216",
                  "UPP40140216-000099")
          /* @res "����" */},
          {
              3 + "",
              nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                  "UC000-0002282")
          /* @res "����" */},
          {
              4 + "",
              nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                  "UC000-0000248")
          /* @res "���" */}

      });

      // ������
      UIRefPane refpaneTranCust = new UIRefPane();
      TrancustRefModel refmodelTranCust = new TrancustRefModel();
      refmodelTranCust.setDelivOrgPK(getDelivOrgPK());
      refpaneTranCust.setRefModel(refmodelTranCust);
      getQueryConditionDlg().setValueRef("dm_baseprice.pk_transcust",
          refpaneTranCust);

      // ����
      UIRefPane refPaneVhcltype = new UIRefPane();
      VehicletypeRefModel refmodelVehicle = new nc.ui.dm.pub.ref.VehicletypeRefModel();
      refmodelVehicle.setDelivOrgPK(getDelivOrgPK());
      refPaneVhcltype.setRefModel(refmodelVehicle);
      getQueryConditionDlg().setValueRef("dm_baseprice.pk_vehicletype",
          refPaneVhcltype);

      // ���˷�ʽ
      UIRefPane refpaneDelivMode = new UIRefPane();
      refpaneDelivMode.setRefNodeName("���˷�ʽ");
      refpaneDelivMode.getRefModel().setWherePart(
          " pk_corp in (" + getStrCorpIDsOfDelivOrg() + ", '"
              + getClientEnvironment().getGroupId() + "') ");
      getQueryConditionDlg().setValueRef("dm_baseprice.pk_sendtype",
          refpaneDelivMode);

      // ·��
      UIRefPane refPaneRoute = new UIRefPane();
      nc.ui.dm.pub.ref.RouteRefModel refmodelRoute = new nc.ui.dm.pub.ref.RouteRefModel();
      refmodelRoute.setDelivOrgPK(getDelivOrgPK());
      refPaneRoute.setRefModel(refmodelRoute);
      getQueryConditionDlg().setValueRef("dm_baseprice.pkroute", refPaneRoute);

    }

    return queryConditionDlg;
  }

  /**
   * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-9-3 14:27:25) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 
   * @return java.util.HashMap
   */
  public boolean isHaveQuantityLevel(String pk_basicprice) {
    if (pk_basicprice == null || pk_basicprice.trim().length() <= 0)
      return false;

    nc.vo.pub.lang.UFBoolean bret = (nc.vo.pub.lang.UFBoolean) getHsIsHaveQuantityLevel()
        .get(pk_basicprice);
    if (bret == null) {
      try {
        bret = new nc.vo.pub.lang.UFBoolean(BasepriceHelper
            .isHaveQuantityLevel(pk_basicprice));
        getHsIsHaveQuantityLevel().put(pk_basicprice, bret);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    if (bret != null)
      return bret.booleanValue();
    else
      return false;
  }

  /**
   * ����ʵ�ָ÷�������Ӧ��ť�¼���
   * 
   * @version (00-6-1 10:32:59)
   * @param bo
   *          ButtonObject
   */
  public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
    this.showHintMessage("");

    if (bo == getBoFeeItemPrice()) {
      onFeeItem();
    }
    else if (bo == getBoOpenDM016()) {
      onOpentDM016();
    }
    else if (bo == boCancel){
      onCancel();
    }
    else {
      super.onButtonClicked(bo);
    }

    if (m_bEdit) {
      setButton(getBoOpenDM016(), false);
      setButton(getBoFeeItemPrice(), false);
    }
    else {
      setButton(getBoOpenDM016(), true);
      setButton(getBoFeeItemPrice(), true);
    }
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2002-5-23 15:41:19)
   */
  public void onFeeItem() {
    int row = getBillCardPanel().getBillTable().getSelectedRow();
    if (row < 0) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140216",
          "UPP40140216-000107")/* @res "��ѡ��һ�У�" */);
      return;
    }

    nc.vo.dm.dm004.DmFeeitempriceVO dmdatavo = null;
    dmdatavo = (nc.vo.dm.dm004.DmFeeitempriceVO) getBillCardPanel().getBillModel()
        .getBodyValueRowVO(
            getBillCardPanel().getBodyPanel().getTable().getSelectedRow(),
            "nc.vo.dm.dm004.DmFeeitempriceVO");

    getFeeItemDlg().loadDataByDM004VO(dmdatavo);

    getFeeItemDlg().showModal();

  }

  /**
   * �������롣 �������ڣ�(2001-4-21 10:36:57)
   */
  public void onOpentDM016() {

    // opentDM016();

    int row = getBillCardPanel().getBillTable().getSelectedRow();
    if (row < 0) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140216",
          "UPP40140216-000107")/* @res "��ѡ��һ�У�" */);
      return;
    }

    nc.vo.dm.dm016.DmQuantitylevelHVO dmdatavo = null;
    dmdatavo = (nc.vo.dm.dm016.DmQuantitylevelHVO) getBillCardPanel().getBillModel()
        .getBodyValueRowVO(row, "nc.vo.dm.dm016.DmQuantitylevelHVO");

    UFBoolean bsltfrmlevel = (UFBoolean) dmdatavo
        .getAttributeValue("bsltfrmlevel");

    if (bsltfrmlevel == null || !bsltfrmlevel.booleanValue()) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140216",
          "UPP40140216-000108")/* @res "���в���ָ�������۸�" */);
      return;
    }

    getDm016Dlg().loadDataByDM004VO(dmdatavo);

    getDm016Dlg().showModal();

    String pk_basicprice = (String) dmdatavo.getAttributeValue("pk_basicprice");
    if (pk_basicprice == null || pk_basicprice.trim().length() <= 0)
      return;

    try {
      getHsIsHaveQuantityLevel().put(pk_basicprice,
          new UFBoolean(BasepriceHelper.isHaveQuantityLevel(pk_basicprice)));
    }
    catch (Exception e) {
      ExceptionUITools tool = new ExceptionUITools();
      tool.showMessage(e, this);
    }

  }

  /**
   * ����ʵ�ָ÷���������ҵ�����ı��⡣
   * 
   * @version (00-6-6 13:33:25)
   * @return java.lang.String
   */
  public void setBodyComBoxValue(Object value, int row, String key) {

    BillItem bm = getBillCardPanel().getBodyItem(key);
    if (bm == null)
      return;

    nc.ui.pub.beans.UIComboBox comHeadItem = (nc.ui.pub.beans.UIComboBox) bm
        .getComponent();
    int count = comHeadItem.getItemCount();
    if (count == 0)
      return;
    if (value == null) {
      getBillCardPanel().setBodyValueAt(comHeadItem.getItemAt(0), row, key);
      return;
    }

    if (value.getClass() == Integer.class
        && ((Integer) value).intValue() < count) {
      getBillCardPanel().setBodyValueAt(
          comHeadItem.getItemAt(((Integer) value).intValue()), row, key);
    }
    else {
      for (int i = 0; i < count; i++) {
        if (value.equals(comHeadItem.getItemAt(i))) {
          getBillCardPanel().setBodyValueAt(comHeadItem.getItemAt(i), row, key);
        }
      }

    }

  }

  /**
   * ������ԭ��ı���档 �������ڣ�(2001-11-15 9:18:13)
   * 
   * @param bdData
   *          nc.ui.pub.bill.BillData
   */
  protected void setBodyEdit() {

    Integer ipricetype = null;
    Integer iuplimittype = null;

    Object otemp = null;

    for (int i = 0, loop = getBillCardPanel().getRowCount(); i < loop; i++) {
      setRowEdit(i);
    }

  }

  /**
   * ������ԭ��ı���档 �������ڣ�(2001-11-15 9:18:13)
   * 
   * @param bdData
   *          nc.ui.pub.bill.BillData
   */
  protected void setRowEdit(int row) {

    if (row < 0 || row >= getBillCardPanel().getRowCount())
      return;

    Integer ipricetype = null;
    Integer iuplimittype = null;

    Object otemp = null;

    // ֻ�мƼ�����Ϊ����ʱ�������Ͳſ���
    ipricetype = getBodyComBoxIndex(row, "ipricetype");
    if (ipricetype != null && ipricetype.intValue() == 1) {
      getBillCardPanel().setCellEditable(row, "iuplimittype", true);

      iuplimittype = getBodyComBoxIndex(row, "iuplimittype");
      if (iuplimittype != null && iuplimittype.intValue() > 0) {
        getBillCardPanel().setCellEditable(row, "nuplimitnum", true);
        getBillCardPanel().setCellEditable(row, "noveruplmtprice", true);
      }
      else {
        getBillCardPanel().setCellEditable(row, "nuplimitnum", false);
        getBillCardPanel().setCellEditable(row, "noveruplmtprice", false);
      }

      getBillCardPanel().setCellEditable(row, "vroute", false);

      // ֻ�мƼ�����Ϊ����ʱ��·�߿�¼��
    }
    else if (ipricetype != null && ipricetype.intValue() == 2) {

      getBillCardPanel().setCellEditable(row, "vroute", true);

    }
    else {
      getBillCardPanel().setCellEditable(row, "iuplimittype", false);
      getBillCardPanel().setCellEditable(row, "nuplimitnum", false);
      getBillCardPanel().setCellEditable(row, "noveruplmtprice", false);

      getBillCardPanel().setCellEditable(row, "vroute", false);
    }

    otemp = getBillCardPanel().getBodyValueAt(row, "bsltfrmlevel");
    nc.vo.pub.lang.UFBoolean btemp = null;
    if (otemp != null && otemp.toString().trim().length() > 0)
      btemp = new nc.vo.pub.lang.UFBoolean(otemp.toString());

    if (btemp != null && btemp.booleanValue()) {
      getBillCardPanel().setCellEditable(row, "dbaseprice", false);
    }
    else {
      getBillCardPanel().setCellEditable(row, "dbaseprice", true);
    }

  }
  
  public void afterQuery(DmBasepriceAggVO dvo) {
    ArrayList alvos = new ArrayList();
    alvos.add(dvo);

    setAllVOs(alvos);

    if (m_iLastSelListHeadRow < 0)
      m_iLastSelListHeadRow = 0;

    getBillCardPanel().setBillValueVO(dvo);
    getBillCardPanel().getBillModel().execLoadFormula();
    getBillCardPanel().updateValue();
    setButton(boSort, true);
  }
  
  public void afterSave(DmBasepriceAggVO dvosave) {
    //����
    DmBasepriceAggVO dmvo = (DmBasepriceAggVO)((DMBillCardPanel) getBillCardPanel()).getBillData().getBillValueVO(DmBasepriceAggVO.class.getName(), DmBasepriceVO.class.getName(), DmBasepriceVO.class.getName());
    appendBodyVO(dvosave,dmvo);
    setStatusTo(VOStatus.UNCHANGED,dmvo);

    getBillCardPanel().setEnabled(false);
    switchButtonStatus(0);

    //getBillCardPanel().setBillValueVO(dmvo);
    //DMVO dvo = new DMVO(getBillCardPanel().getBillTable().getRowCount());
    //getBillCardPanel().getBillValueVO(dvo);
    afterQuery(dmvo);
  }
  
  public void appendBodyVO(DmBasepriceAggVO otherVOs,DmBasepriceAggVO dmvo) {
    if (otherVOs == null || otherVOs.getChildrenVO().length == 0)
      return;
    ArrayList v = new ArrayList();
    
    for (int i = 0; i < dmvo.getChildrenVO().length; i++) {
      if (dmvo.getChildrenVO()[i].getStatus() == VOStatus.UNCHANGED
          ) {
        v.add(dmvo.getChildrenVO()[i]);
      }
    }
    
    if (null != otherVOs.getChildrenVO()) {
      for (int i = 0; i < otherVOs.getChildrenVO().length; i++) {
        if(otherVOs.getChildrenVO()[i].getStatus()!=VOStatus.DELETED)
        v.add(otherVOs.getChildrenVO()[i]);
      }
    }
    DmBasepriceVO[] ddvos = new DmBasepriceVO[v.size()];
    ddvos = (DmBasepriceVO[]) v.toArray(ddvos);
    dmvo.setChildrenVO(ddvos);
  }
  
  public void setStatusTo(int status,DmBasepriceAggVO dmvo) {
    if (null != dmvo.getParentVO()) {
      dmvo.getParentVO().setStatus(status);
    }
    if (null != dmvo.getChildrenVO() && dmvo.getChildrenVO().length != 0) {
      for (int i = 0; i < dmvo.getChildrenVO().length; i++) {
        if (null != dmvo.getChildrenVO()[i]) {
          dmvo.getChildrenVO()[i].setStatus(status);
        }
      }
    }

  }
  
  
  
  public void refreshCardTable() {
    getBillCardPanel().resumeValue();
    if (getAllVOs().size() != 0) {
      if (m_iLastSelListHeadRow < 0)
        m_iLastSelListHeadRow = 0;
      getBillCardPanel().setBillValueVO((DmBasepriceAggVO) getAllVOs().get(m_iLastSelListHeadRow));

    }
    getBillCardPanel().updateValue();
  }
}

