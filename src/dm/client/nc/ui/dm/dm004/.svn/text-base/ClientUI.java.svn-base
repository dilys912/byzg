
package nc.ui.dm.dm004;

/**
 * 类的功能、用途、现存BUG，以及其它别人可能感兴趣的介绍。 作者：仲瑞庆
 * 
 * @version 最后修改日期(2002-5-9 13:31:03)
 * @see 需要参见的其它类
 * @since 从产品的那一个版本，此类被添加进来。（可选） 修改人 + 修改日期 修改说明
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
  // 节点由何处触发打开，默认为从节点树正常打开
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
    // 非联查打开节点
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
   * 关闭窗口的客户端接口。可在本方法内完成窗口关闭前的工作。
   * 
   * @return boolean 返回值为true表示允许窗口关闭，返回值为false表示不允许窗口关闭。 创建日期：(2001-8-8
   *         13:52:37)
   */
  public boolean onClosing() {
    if (m_bEdit) {
      int result = MessageDialog.showYesNoCancelDlg(this, null,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH001"),
                MessageDialog.ID_YES);/* @res "是否保存已修改的数据？" */
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
   * 子类实现该方法，返回业务界面的标题。
   * 
   * @version (00-6-6 13:33:25)
   * @return java.lang.String
   */
  public String getTitle() {
    // String title = getBillCardPanel().getBillData().getTitle();
    // if(title==null || title.trim().length()<=0)
    // title = "运费价格表";
    return nc.ui.ml.NCLangRes.getInstance().getStrByID("40140216",
        "UPT40140216-000014")/* @res "运费价格表" */;
  }

  /**
   * 此处插入方法说明。 创建日期：(2002-5-10 11:16:28)
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

    // 计价依据
    BillItem bm = getBillCardPanel().getBodyItem("ipricetype");
    nc.ui.pub.beans.UIComboBox comHeadItem = (nc.ui.pub.beans.UIComboBox) bm
        .getComponent();
    int count = comHeadItem.getItemCount();
    if (count == 0) {
      comHeadItem.setTranslate(true);
      bm.setWithIndex(true);
      comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UC000-0004106")/* @res "重量" */);
      comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UC000-0000198")/* @res "件数" */);
      comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40140216", "UPP40140216-000099")/* @res "整车" */);
      comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UC000-0002282")/* @res "数量" */);
      comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UC000-0000248")/* @res "体积" */);
      // 计价依据中以下项隐藏：体积、运输仓、里程重量
      // comHeadItem.addItem("体积");
      // comHeadItem.addItem("运输仓");
      // comHeadItem.addItem("里程重量");
    }

    // 上限类型
    bm = getBillCardPanel().getBodyItem("iuplimittype");
    comHeadItem = (nc.ui.pub.beans.UIComboBox) bm.getComponent();
    count = comHeadItem.getItemCount();
    if (count == 0) {
      comHeadItem.setTranslate(true);
      bm.setWithIndex(true);
      comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40140216", "UPP40140216-000100")/* @res "无" */);
      comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UC000-0004106")/* @res "重量" */);
    }

    // 上限值
    bm = getBillCardPanel().getBodyItem("nuplimitnum");
    ((nc.ui.pub.beans.UIRefPane) bm.getComponent()).setMinValue(0.0);
    if (BD501 != null)
      bm.setDecimalDigits(BD501.intValue());

    // 超上限后的价格
    bm = getBillCardPanel().getBodyItem("noveruplmtprice");
    ((nc.ui.pub.beans.UIRefPane) bm.getComponent()).setMinValue(0.0);
    if (BD505 != null)
      bm.setDecimalDigits(BD505.intValue());

    // /////////////////////////////
  }

  /**
   * 初试化变量。
   * =========================================================================================
   * 说明： 主要用于设置单据上的顶级菜单按钮，默认按钮定义在类变量定义 代码结构： //卡片按钮 ButtonObject[]
   * aryButtonGroup = {boBrowse,boEdit,boSave,
   * boCancel,boAction,boLine,boPrint,boAssistant}; this.aryButtonGroup =
   * aryButtonGroup; //列表按钮 ButtonObject[] aryListButtonGroup =
   * {boBusiType,boBrowse,boCard,boEdit,boAction,boPrint,boAssistant};
   * this.aryListButtonGroup = aryListButtonGroup;
   * =========================================================================================
   */
  public void initVariable() {
    super.initVariable();
    // 单据显示状态
    setShowState(DMBillStatus.Card);// "表单");

  }

  /**
   * 运输仓修改 创建日期：(2002-5-31 9:36:55)
   * 
   * @param row
   *          int
   */
  private void afterClassEdit(Object value, int row) {

    afterRefEdit(value, row, "vclasscode", "pk_transcontainer", "vclassname");
    afterRefEdit("", row, "vvhcltypecode", "pk_vehicletype", "vvhcltypename");
  }

  /**
   * 编辑后事件处理。
   * =========================================================================================
   * 前提： 每一编辑后操作需相应一方法，命名方式为afterXXXEdit，其中XXX为编辑控件名 如编辑客户：afterCustomerEdit
   * 代码结构： if (e.getPos() == BillItem.HEAD){ //客商 if
   * (e.getKey().equals("ccustomerid")){ afterCustomerEdit(e); } } if
   * (e.getPos() == BillItem.BODY){ //存货编码 清除数据 if
   * (e.getKey().equals("cinventorycode")){ afterInventoryEdit(e); } }
   * =========================================================================================
   */
  public void afterEdit(BillEditEvent e) {

    if (e.getPos() == BillItem.HEAD) {
    }
    else if (e.getPos() == BillItem.BODY) {
      // 承运商
      if (e.getKey().equals("vtranscustcode")) {
        afterTranscustEdit(e.getValue(), e.getRow());
      }
      // 车型
      else if (e.getKey().equals("vvhcltypecode")) {
        afterVhcltypeEdit(e.getValue(), e.getRow());
      }
      // 运输仓
      else if (e.getKey().equals("vclasscode")) {
        afterClassEdit(e.getValue(), e.getRow());
      }
      // 发运方式
      else if (e.getKey().equals("vsendtypecode")) {
        afterSendtypeEdit(e.getValue(), e.getRow());
      }
      // 存货分类
      else if (e.getKey().equals("vinvclasscode")) {
        afterInvclassEdit(e.getValue(), e.getRow());
      }
      // 存货
      else if (e.getKey().equals("vinvcode")) {
        afterInvEdit(e.getValue(), e.getRow());
      }
      // 发货站
      else if (e.getKey().equals("fromarea")) {
        afterFromAreaEdit(e.getValue(), e.getRow());
      }
      // 到达站
      else if (e.getKey().equals("toarea")) {
        afterToAreaEdit(e.getValue(), e.getRow());
      }
      // 包装分类
      else if (e.getKey().equals("packsort")) {
        afterPackSortEdit(e.getValue(), e.getRow());
      }
      else if (e.getKey().equals("vfromaddress")) { // 发货地点
        UIRefPane currentRef = ((UIRefPane) getBillCardPanel().getBodyItem(
            e.getKey()).getComponent());
        getBillCardPanel()
            .setBodyValueAt(currentRef.getRefPK(),
                getBillCardPanel().getBillTable().getSelectedRow(),
                "pkfromaddress");
      }
      else if (e.getKey().equals("vtoaddress")) { // 到货地点
        UIRefPane currentRef = ((UIRefPane) getBillCardPanel().getBodyItem(
            e.getKey()).getComponent());
        getBillCardPanel().setBodyValueAt(currentRef.getRefPK(),
            getBillCardPanel().getBillTable().getSelectedRow(), "pktoaddress");
      }
      // 到达站
      else if (e.getKey().equals("vroute")) {
        afterRouteEdit(e.getValue(), e.getRow());
      }
      // 计价依据
      else if (e.getKey().equals("ipricetype")) {
        afterIPriceTypeEdit(e.getValue(), e.getRow());
      }
      // 是否批量级次价
      else if (e.getKey().equals("bsltfrmlevel")) {
        afterBSltfrmlevelEdit(e.getValue(), e.getRow());
      }
      // 上限类型
      else if (e.getKey().equals("iuplimittype")) {
        afterIUplimittype(e.getValue(), e.getRow());
      }
    }
  }

  /**
   * 存货分类修改 创建日期：(2002-5-31 9:36:55)
   * 
   * @param row
   *          int
   */
  private void afterInvclassEdit(Object value, int row) {

    afterRefEdit(value, row, "vinvclasscode", "pk_invclass", "vinvclassname");
    afterRefEdit("", row, "vinvcode", "pk_inventory", "vinvname");
  }

  /**
   * 存货修改 创建日期：(2002-5-31 9:36:55)
   * 
   * @param row
   *          int
   */
  private void afterInvEdit(Object value, int row) {
    afterRefEdit(value, row, "vinvcode", "pk_inventory", "vinvname");
    afterRefEdit("", row, "vinvclasscode", "pk_invclass", "vinvclassname");
  }

  /**
   * 发运方式修改 创建日期：(2002-5-31 9:36:55)
   * 
   * @param row
   *          int
   */
  private void afterSendtypeEdit(Object value, int row) {

    afterRefEdit(value, row, "vsendtypecode", "pk_sendtype", "vsendtypename");
  }

  /**
   * 承运商修改 创建日期：(2002-5-31 9:36:55)
   * 
   * @param row
   *          int
   */
  private void afterTranscustEdit(Object value, int row) {

    afterRefEdit(value, row, "vtranscustcode", "pk_transcust", "vtranscustname");
  }

  /**
   * 车型修改 创建日期：(2002-5-31 9:36:55)
   * 
   * @param row
   *          int
   */
  private void afterVhcltypeEdit(Object value, int row) {
    afterRefEdit(value, row, "vvhcltypecode", "pk_vehicletype", "vvhcltypename");
    afterRefEdit("", row, "vclasscode", "pk_transcontainer", "vclassname");
  }

  /**
   * 此处插入方法说明。 创建日期：(2002-5-27 14:07:28)
   */
  public void onAddLine() {
	  billstatus = 1;
    super.onAddLine();
    int row = getBillCardPanel().getRowCount() - 1;
    getBillCardPanel().setBodyValueAt(getDelivOrgPK(), row, "pkdelivorg");
    // getBillCardPanel().
    setRowEdit(row);

    String message = NCLangRes.getInstance().getStrByID("common", "UCH036");
    /* @res增行成功 */
    this.showHintMessage(message);
  }

  /**
   * 放弃输入。 创建日期：(2001-4-21 10:36:57)
   */
  public void onCancel() {
    super.onCancel();
    
    getBillCardPanel().getBillModel().execLoadFormula();
    getBillCardPanel().updateValue();
    getBillCardPanel().updateUI();
     // 设置编辑标志
    m_bEdit = false;
    String message = NCLangRes.getInstance().getStrByID("common", "UCH008");
    /* @res取消成功 */
    this.showHintMessage( message );    
  }

  /**
   * 此处插入方法说明。 创建日期：(2002-5-27 14:21:22)
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
                                                   * "删除行,将导致原定义的批量价格被删除,是否继续？"
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
      /* @res删行成功 */
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
   * 修改。 创建日期：(2002-5-16 15:12:48)
   */
  public void onEdit() {
    super.onEdit();

    getBillCardPanel().setHeadItem("vdoname", getDelivOrgName());

    setBodyEdit();
    //add by yhj 2014-05-07
    billstatus = 2;
    //end
    // 设置编辑标志
    m_bEdit = true;
    // ((nc.ui.dm.pub.cardpanel.DMBillCardPanel)
    // getBillCardPanel()).autoAddLineToRowLimit();

    String message = NCLangRes.getInstance().getStrByID("common", "UCH027");
    /* @res正在修改 */
    this.showHintMessage(message);
  }

  /**
   * 功能描述：行操作----粘贴行到表尾 author : hxb 创建日期：(2006-8-14)
   * 
   * @return null
   * @param null
   * @exception null
   */
  public void onPasteToTail() {
    // 粘贴前的表体总行数
    int iRowCount0 = getBillCardPanel().getBodyPanel().getTableModel()
        .getRowCount();

    // 粘贴所选择的行
    getBillCardPanel().pasteLineToTail();

    // 粘贴后的表体总行数
    int iRowCount1 = getBillCardPanel().getBillModel().getRowCount();

    // 要粘贴的行的总行数
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
   * 此处插入方法说明。 创建日期：(2002-5-23 15:41:19)
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
      // 过滤VO
      BillCardPanel billCard = getBillCardPanel();
      super.filterNullLine(itemkey, billCard);
      // 校验界面数据
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
      // 得到userid （zlf）
      dvo.getParentVO().setAttributeValue("userid", getUserID());
      // dvo.getHeaderVO().setAttributeValue("pk_basicprice",dvo.);
      nc.ui.dm.dm007.DmUtils dui = new nc.ui.dm.dm007.DmUtils();
      dui.convertToArrayList(dvo);
      DmBasepriceAggVO dvosave = BasepriceHelper.save(dvo);
      
      /***********************add by yhj 2014-02-22 START************************************/
      if(billstatus == 1){//新增（增行）保存后动作
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
            vo.setBdid(dsvo.getPk_basicprice());//此处去基价表的主键
            vo.setBdtype("CYS");
            vo.setProctype("add");
            vo.setSysflag("Y");
            iserviceDao.insert(vo);
          }
          }  
      }
      }else if(billstatus == 2){//修改保存后动作
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

      // 合并
      dui.combineOtherVO(dvosave,dvo);
      this.showHintMessage(NCLangRes.getInstance().getStrByID("common",
          "UCH005") /* @res "保存成功" */);

      afterSave(dvo);

      // 设置编辑标志
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
   * 由其它原因改变界面。 创建日期：(2001-11-15 9:18:13)
   * 
   * @param bdData
   *          nc.ui.pub.bill.BillData
   */
  protected void setCardPanelByOther(BillData bdData) {
    // ((UIRefPane)
    // bdData.getBodyItem("vinvcode").getComponent()).setWhereString("pk_corp='0001'");
  }

  /**
   * 创建者：韩卫 功能：存货分类，存货两者其中一个必须填写的校验 参数： 返回： 例外： 日期：(2001-6-17 下午 5:17)
   * 修改日期，修改人，修改原因，注释标志：
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
    //eric 增加生效日期，失效日期
    String effectdate = null; String expirationdate = null;
    Hashtable htTable = new Hashtable();
    String sRowString = "";
    String sCheckvalue = "";
    int iRows = 0;
    // 计价依据
    Integer ipricetype = null;
    try {
      // 校验非空项目
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
        // sErrorMessage = "第" + iRows + "行的基价和整车价格不能同时为空。";
        // showErrorMessage(sErrorMessage);
        // return false;
        // }
        // else if ((getBillCardPanel().getBodyValueAt(i, "dbaseprice") != null
        // && getBillCardPanel().getBodyValueAt(i,
        // "dbaseprice").toString().length() != 0)
        // && (getBillCardPanel().getBodyValueAt(i, "dvehicleprice") != null
        // && getBillCardPanel().getBodyValueAt(i,
        // "dvehicleprice").toString().length() != 0)) {
        // sErrorMessage = "第" + iRows + "行的基价和整车价格不能同时录入。";
        // showErrorMessage(sErrorMessage);
        // return false;
        // }
        ipricetype = getBodyComBoxIndex(i, "ipricetype");
        // ---------------整车价格校验
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
          // sErrorMessage = sErrorMessage + "第" + iRows + "行录入整车价格时承运商不能为空。\n";
          // }
          // if (getBillCardPanel().getBodyValueAt(i, "vsendtypecode") == null
          // || getBillCardPanel().getBodyValueAt(i,
          // "vsendtypecode").toString().length() == 0){
          // sErrorMessage = sErrorMessage + "第" + iRows +
          // "行录入整车价格时发运方式不能为空。\n";
          // }
          // if (getBillCardPanel().getBodyValueAt(i, "vroute") == null ||
          // getBillCardPanel().getBodyValueAt(i, "vroute").toString().length()
          // == 0){
          // sErrorMessage = sErrorMessage + "第" + iRows + "行录入整车价格时路线不能为空。\n";
          // }
          // if (getBillCardPanel().getBodyValueAt(i, "vvhcltypecode") == null
          // || getBillCardPanel().getBodyValueAt(i,
          // "vvhcltypecode").toString().length() == 0){
          // sErrorMessage = sErrorMessage + "第" + iRows + "行录入整车价格时车型不能为空。\n";
          // }
          // if (getBillCardPanel().getBodyValueAt(i, "fromarea") != null ||
          // getBillCardPanel().getBodyValueAt(i,
          // "fromarea").toString().length() != 0){
          // sErrorMessage = sErrorMessage + "第" + iRows + "行录入整车价格时发货站不能录入。\n";
          // }
          // if (getBillCardPanel().getBodyValueAt(i, "toarea") != null ||
          // getBillCardPanel().getBodyValueAt(i, "toarea").toString().length()
          // != 0){
          // sErrorMessage = sErrorMessage + "第" + iRows + "行录入整车价格时到达站不能录入。\n";
          // }
          // if (getBillCardPanel().getBodyValueAt(i, "vfromaddress") != null ||
          // getBillCardPanel().getBodyValueAt(i,
          // "vfromaddress").toString().length() != 0){
          // sErrorMessage = sErrorMessage + "第" + iRows +
          // "行录入整车价格时发货地点不能录入。\n";
          // }
          // if (getBillCardPanel().getBodyValueAt(i, "vtoaddress") != null ||
          // getBillCardPanel().getBodyValueAt(i,
          // "vtoaddress").toString().length() != 0){
          // sErrorMessage = sErrorMessage + "第" + iRows +
          // "行录入整车价格时到货地点不能录入。\n";
          // }
        }
        // ---------------基价校验
        else {
          // 承运商编码
          if (getBillCardPanel().getBodyValueAt(i, "vtranscustcode") != null)
            svtranscustcode = getBillCardPanel().getBodyValueAt(i,
                "vtranscustcode").toString().trim();
          // 车型编码
          if (getBillCardPanel().getBodyValueAt(i, "vvhcltypecode") != null)
            svvhcltypecode = getBillCardPanel().getBodyValueAt(i,
                "vvhcltypecode").toString().trim();
          // 运输仓编码
          if (getBillCardPanel().getBodyValueAt(i, "vclasscode") != null)
            svclasscode = getBillCardPanel().getBodyValueAt(i, "vclasscode")
                .toString().trim();
          // 发运方式编码
          if (getBillCardPanel().getBodyValueAt(i, "vsendtypecode") != null)
            svsendtypecode = getBillCardPanel().getBodyValueAt(i,
                "vsendtypecode").toString().trim();
          // 存货分类编码
          // if (getBillCardPanel().getBodyValueAt(i, "vinvclasscode") != null)
          // svinvclasscode = getBillCardPanel().getBodyValueAt(i,
          // "vinvclasscode").toString().trim();
          // 存货编码
          // if (getBillCardPanel().getBodyValueAt(i, "vinvcode") != null)
          // svinvcode = getBillCardPanel().getBodyValueAt(i,
          // "vinvcode").toString().trim();
          // 价格单位
          // if (getBillCardPanel().getBodyValueAt(i, "vpriceunit") != null)
          // svpriceunit = getBillCardPanel().getBodyValueAt(i,
          // "vpriceunit").toString().trim();
          // 发货站
          // if (getBillCardPanel().getBodyValueAt(i, "fromarea") != null)
          // fromarea = getBillCardPanel().getBodyValueAt(i,
          // "fromarea").toString().trim();
          // 到达站
          // if (getBillCardPanel().getBodyValueAt(i, "toarea") != null)
          // toarea = getBillCardPanel().getBodyValueAt(i,
          // "toarea").toString().trim();
          // 包装分类
          if (getBillCardPanel().getBodyValueAt(i, "pkpacksort") != null)
            packing = getBillCardPanel().getBodyValueAt(i, "pkpacksort")
                .toString().trim();

          // 发货地点主键 eric
          if (getBillCardPanel().getBodyValueAt(i, "pkfromaddress") != null)
            pkfromaddress = getBillCardPanel().getBodyValueAt(i, "pkfromaddress")
                  .toString().trim();
          // 到货地点主键
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

          // 存货分类和存货编码不能都为空
          sCheckvalue = svinvclasscode + svinvcode;
          // if (sCheckvalue.equals(sSeprateValue + sSeprateValue))
          // sErrorMessage = sErrorMessage + "第" + iRows +
          // "行录入的数据存货分类和存货编码不能都为空。\n";

          if (sRowString.length() > 0) {

            // sRowString7个录入值不能完全重复
            if (null != htTable) {
              // 校验重复
              if (htTable.containsValue(sRowString)) {
                String[] sValue = new String[] {
                  String.valueOf(iRows)
                };
                sErrorMessage = sErrorMessage
                    + nc.ui.ml.NCLangRes.getInstance().getStrByID("40140216",
                        "UPP40140216-000102", null, sValue)/*
                                                             * @res
                                                             * "第{0}行录入的数据[承运商,发运方式,发货地点,到货地点,车型,包装分类]与其他数据重复。\n" +
                                                             * iRows +
                                                             * "行录入的数据[承运商,发运方式,发货地点,到货地点,车型,包装分类]与其他数据重复。\n"
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
                                             * @res "第{0}行的基价不能为负。\n" + iRows +
                                             * "行的基价不能为负。\n"
                                             */;
            }
          }
          // Modified by xhq 2002/10/06 end

        } // end for

        if (ipricetype != null) {
          // a) 件数：则包装分类必须输入
          if (ipricetype.intValue() == 1) {
            // String pkpacksort=(String)getBillCardPanel().getBodyValueAt(i,
            // "pkpacksort");
            // if(pkpacksort==null || pkpacksort.trim().length()<=0){
            // sErrorMessage = sErrorMessage + "第" + iRows +
            // "行计价依据价为件数时包装分类不能为空。\n";
            // }
          }
          else if (ipricetype.intValue() == 2) {
            // b) 整车：则车型、路线必须输入
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
                                                           * "第{0}行计价依据价为整车时车型不能为空。\n" +
                                                           * iRows +
                                                           * nc.ui.ml.NCLangRes.getInstance().getStrByID("40140216","UPP40140216-000105")/*@res
                                                           * "行计价依据价为整车时车型、路线不能为空。\n"
                                                           */;
            }
          }
          else if (ipricetype.intValue() == 5) {
            // c) 运输仓：则运输仓必须输入
            // String
            // pk_transcontainer=(String)getBillCardPanel().getBodyValueAt(i,
            // "pk_transcontainer");
            // if(pk_transcontainer==null ||
            // pk_transcontainer.trim().length()<=0){
            // sErrorMessage = sErrorMessage + "第" + iRows +
            // "行计价依据价为运输仓时运输仓不能为空。\n";
            // }
          }
        }
      }
    //eric 判断 时间段是否有重叠
    
    
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
        throw new BusinessException("存在重叠时间段，无法保存");
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

  // 编辑标志
  private boolean m_bEdit = false;

  /**
   * 存货修改 创建日期：(2002-5-31 9:36:55)
   * 
   * @param row
   *          int
   */
  private void afterFromAreaEdit(Object value, int row) {
    afterRefEdit(value, row, "fromarea", "pkfromarea", "fromarea");
  }

  /**
   * 存货修改 创建日期：(2002-5-31 9:36:55)
   * 
   * @param row
   *          int
   */
  private void afterPackSortEdit(Object value, int row) {
    afterRefEdit(value, row, "packsort", "pkpacksort", "packsort");
  }

  /**
   * 存货修改 创建日期：(2002-5-31 9:36:55)
   * 
   * @param row
   *          int
   */
  private void afterToAreaEdit(Object value, int row) {
    afterRefEdit(value, row, "toarea", "pktoarea", "toarea");
  }

  /**
   * δ埽篭n参数： 返回： 例外： 日期：(2002-11-13 11:52:46) 修改日期，修改人，修改原因，注释标志：
   * 
   * @param event
   *          nc.ui.pub.bill.BillEditEvent
   */
  public void bodyRowChange(BillEditEvent event) {
    if ((nc.ui.pub.beans.UITable) event.getSource() == getBillCardPanel()
        .getBillTable()) {
      if (m_bEdit) {
        // 修改

        // 承运商编码
        Object pktranscust = getBillCardPanel().getBodyValueAt(event.getRow(),
            "pk_transcust");
        UIRefPane refPane = (UIRefPane) getBillCardPanel().getBodyItem(
            "vtranscustcode").getComponent();
        refPane.setPK(pktranscust);

        // 车型编码
        Object pkvehicletype = getBillCardPanel().getBodyValueAt(
            event.getRow(), "pk_vehicletype");
        refPane = (UIRefPane) getBillCardPanel().getBodyItem("vvhcltypecode")
            .getComponent();
        refPane.setPK(pkvehicletype);

        // 运输仓编码
        Object pktranscontainer = getBillCardPanel().getBodyValueAt(
            event.getRow(), "pk_transcontainer");
        refPane = (UIRefPane) getBillCardPanel().getBodyItem("vclasscode")
            .getComponent();
        refPane.setPK(pktranscontainer);

        // 发运方式编码
        Object pksendtype = getBillCardPanel().getBodyValueAt(event.getRow(),
            "pk_sendtype");
        refPane = (UIRefPane) getBillCardPanel().getBodyItem("vsendtypecode")
            .getComponent();
        refPane.setPK(pksendtype);

        // 存货分类编码
        Object pkinvclass = getBillCardPanel().getBodyValueAt(event.getRow(),
            "pk_invclass");
        refPane = (UIRefPane) getBillCardPanel().getBodyItem("vinvclasscode")
            .getComponent();
        refPane.setPK(pkinvclass);

        // 存货编码
        Object pkinventory = getBillCardPanel().getBodyValueAt(event.getRow(),
            "pk_inventory");
        refPane = (UIRefPane) getBillCardPanel().getBodyItem("vinvcode")
            .getComponent();
        refPane.setPK(pkinventory);

        // 发站编码
        Object pkfromarea = getBillCardPanel().getBodyValueAt(event.getRow(),
            "pkfromarea");
        refPane = (UIRefPane) getBillCardPanel().getBodyItem("fromarea")
            .getComponent();
        refPane.setPK(pkfromarea);

        // 到站编码
        Object pktoarea = getBillCardPanel().getBodyValueAt(event.getRow(),
            "pktoarea");
        refPane = (UIRefPane) getBillCardPanel().getBodyItem("toarea")
            .getComponent();
        refPane.setPK(pktoarea);

        // 包装分类编码
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
   * 此处插入方法说明。 创建日期：(2002-6-27 16:16:11)
   */
  public boolean checkOther(nc.vo.pub.AggregatedValueObject vo)
      throws Exception {
    return true;
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-10-6 17:08:45) 修改日期，修改人，修改原因，注释标志：Added
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
    /* @res粘贴行成功 */
    this.showHintMessage(message);

  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-7-24 11:08:06) 修改日期，修改人，修改原因，注释标志：
   */
  public void onPrint() {
    setNodeCode(DMBillNodeCodeConst.m_delivBasePrice);
    super.onPrint();

    String message = NCLangRes.getInstance().getStrByID("common", "UCH061");
    /* @res正在打印 */
    this.showHintMessage(message);
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-7-24 11:08:06) 修改日期，修改人，修改原因，注释标志：
   */
  public void onPrintPreview() {
    setNodeCode(DMBillNodeCodeConst.m_delivBasePrice);
    super.onPrintPreview();

    String message = NCLangRes.getInstance().getStrByID("common", "UCH061");
    /* @res正在打印 */
    this.showHintMessage(message);
  }

  //
  protected ButtonObject boFeeItemPrice; // 其他费用项价格

  // 切换到批量级次
  protected ButtonObject boOpenDM016;

  protected boolean bOpenDM016 = false;

  private nc.ui.dm.dm004.DM016Dlg dm016Dlg = null;

  private nc.ui.dm.dm004.DMFeeItemDlg feeitemDlg = null;

  private HashMap hsIsHaveQuantityLevel = null;

  /**
   * 是批量级次价修改 创建日期：(2002-5-31 9:36:55)
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

    // "是否批量价格"为"是"后，价格清空，且不可录入
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
                                               * "将是否批量价格改为否,将导致原定义的批量价格被删除,是否继续？"
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
   * 存货修改 创建日期：(2002-5-31 9:36:55)
   * 
   * @param row
   *          int
   */
  private void afterIPriceTypeEdit(Object value, int row) {

    Integer ipricetype = getBodyComBoxIndex(row, "ipricetype");

    nc.vo.pub.lang.UFDouble uf0 = new nc.vo.pub.lang.UFDouble(0.0);

    // 只有计价依据为件数时上限类型才可用
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

      // 只有计价依据为整车时，路线可录入
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
   * 存货修改 创建日期：(2002-5-31 9:36:55)
   * 
   * @param row
   *          int
   */
  private void afterIUplimittype(Object value, int row) {

    Integer iuplimittype = getBodyComBoxIndex(row, "iuplimittype");

    // 只有计价依据为件数时上限类型才可用
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
   * 此处插入方法说明。 创建日期：(2002-5-16 20:33:00)
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
        // 无结果，应清值
        return;
      }
      DmBasepriceAggVO dvo = new DmBasepriceAggVO();
      dvo.setChildrenVO(ddvos);
      dvo.setParentVO(ddvos[0]);
      afterQuery(dvo);

      // 设置编辑标志
      m_bEdit = false;
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH009")/*
                                                                             * @res
                                                                             * 查询完成
                                                                             */);

    }
    catch (Exception e) {
      this.showErrorMessage(e.getMessage());
      handleException(e);
    }
  }

  /**
   * 存货修改 创建日期：(2002-5-31 9:36:55)
   * 
   * @param row
   *          int
   */
  private void afterRouteEdit(Object value, int row) {
    afterRefEdit(value, row, "vroute", "pkroute", "vroute");
  }

  /**
   * ?user> 功能： 参数： 返回： 例外： 日期：(2004-9-2 10:36:31) 修改日期，修改人，修改原因，注释标志：
   * 
   * @return boolean
   * @param e
   *          nc.ui.pub.bill.BillEditEvent
   */
  public boolean beforeEdit(BillEditEvent e) {
    // if(e.getKey().equals("iuplimittype") || e.getKey().equals("nuplimitnum")
    // || e.getKey().equals("noveruplmtprice")){
    // //只有计价依据为件数时上限类型才可用
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
   * 子类实现该方法，返回业务界面的标题。
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
   * 此处插入方法说明。 创建日期：(2002-5-27 14:21:22)
   */
  public ButtonObject getBoFeeItemPrice() {
    if (boFeeItemPrice == null)
      boFeeItemPrice = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40140216", "UPT40140216-000017")/* @res "其它费用项价格" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40140216",
              "UPT40140216-000017")/* @res "其它费用项价格" */, 0, "其它费用项价格"); /*-=notranslate=-*/
    return boFeeItemPrice;
  }

  /**
   * 此处插入方法说明。 创建日期：(2002-5-27 14:21:22)
   */
  public ButtonObject getBoOpenDM016() {
    if (boOpenDM016 == null)
      boOpenDM016 = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40140216", "UPT40140216-000016")/* @res "批量级次" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40140216",
              "UPT40140216-000016")/* @res "批量级次" */, 0, "批量级次"); /*-=notranslate=-*/
    return boOpenDM016;
  }

  /**
   * 修改。 创建日期：(2002-5-16 15:12:48)
   */
  public nc.ui.dm.dm004.DM016Dlg getDm016Dlg() {

    if (dm016Dlg == null) {
      dm016Dlg = new nc.ui.dm.dm004.DM016Dlg(this);
    }
    return dm016Dlg;

  }

  /**
   * 修改。 创建日期：(2002-5-16 15:12:48)
   */
  public nc.ui.dm.dm004.DMFeeItemDlg getFeeItemDlg() {

    if (feeitemDlg == null) {
      feeitemDlg = new nc.ui.dm.dm004.DMFeeItemDlg(this, BD505);
    }
    return feeitemDlg;

  }

  /**
   * ?user> 功能： 参数： 返回： 例外： 日期：(2004-9-3 14:27:25) 修改日期，修改人，修改原因，注释标志：
   * 
   * @return java.util.HashMap
   */
  public HashMap getHsIsHaveQuantityLevel() {
    if (hsIsHaveQuantityLevel == null)
      hsIsHaveQuantityLevel = new HashMap();
    return hsIsHaveQuantityLevel;
  }

  /**
   * 此处插入方法说明。 创建日期：(2002-5-16 15:46:54)
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

      // 存货
      // UIRefPane inventoryRef = new UIRefPane();
      // inventoryRef.setRefType(2); //树表结构
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
          /* @res "重量" */},
          {
              1 + "",
              nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                  "UC000-0000198")
          /* @res "件数" */},
          {
              2 + "",
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40140216",
                  "UPP40140216-000099")
          /* @res "整车" */},
          {
              3 + "",
              nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                  "UC000-0002282")
          /* @res "数量" */},
          {
              4 + "",
              nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                  "UC000-0000248")
          /* @res "体积" */}

      });

      // 承运商
      UIRefPane refpaneTranCust = new UIRefPane();
      TrancustRefModel refmodelTranCust = new TrancustRefModel();
      refmodelTranCust.setDelivOrgPK(getDelivOrgPK());
      refpaneTranCust.setRefModel(refmodelTranCust);
      getQueryConditionDlg().setValueRef("dm_baseprice.pk_transcust",
          refpaneTranCust);

      // 车型
      UIRefPane refPaneVhcltype = new UIRefPane();
      VehicletypeRefModel refmodelVehicle = new nc.ui.dm.pub.ref.VehicletypeRefModel();
      refmodelVehicle.setDelivOrgPK(getDelivOrgPK());
      refPaneVhcltype.setRefModel(refmodelVehicle);
      getQueryConditionDlg().setValueRef("dm_baseprice.pk_vehicletype",
          refPaneVhcltype);

      // 发运方式
      UIRefPane refpaneDelivMode = new UIRefPane();
      refpaneDelivMode.setRefNodeName("发运方式");
      refpaneDelivMode.getRefModel().setWherePart(
          " pk_corp in (" + getStrCorpIDsOfDelivOrg() + ", '"
              + getClientEnvironment().getGroupId() + "') ");
      getQueryConditionDlg().setValueRef("dm_baseprice.pk_sendtype",
          refpaneDelivMode);

      // 路线
      UIRefPane refPaneRoute = new UIRefPane();
      nc.ui.dm.pub.ref.RouteRefModel refmodelRoute = new nc.ui.dm.pub.ref.RouteRefModel();
      refmodelRoute.setDelivOrgPK(getDelivOrgPK());
      refPaneRoute.setRefModel(refmodelRoute);
      getQueryConditionDlg().setValueRef("dm_baseprice.pkroute", refPaneRoute);

    }

    return queryConditionDlg;
  }

  /**
   * ?user> 功能： 参数： 返回： 例外： 日期：(2004-9-3 14:27:25) 修改日期，修改人，修改原因，注释标志：
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
   * 子类实现该方法，响应按钮事件。
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
   * 此处插入方法说明。 创建日期：(2002-5-23 15:41:19)
   */
  public void onFeeItem() {
    int row = getBillCardPanel().getBillTable().getSelectedRow();
    if (row < 0) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140216",
          "UPP40140216-000107")/* @res "请选中一行！" */);
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
   * 放弃输入。 创建日期：(2001-4-21 10:36:57)
   */
  public void onOpentDM016() {

    // opentDM016();

    int row = getBillCardPanel().getBillTable().getSelectedRow();
    if (row < 0) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140216",
          "UPP40140216-000107")/* @res "请选中一行！" */);
      return;
    }

    nc.vo.dm.dm016.DmQuantitylevelHVO dmdatavo = null;
    dmdatavo = (nc.vo.dm.dm016.DmQuantitylevelHVO) getBillCardPanel().getBillModel()
        .getBodyValueRowVO(row, "nc.vo.dm.dm016.DmQuantitylevelHVO");

    UFBoolean bsltfrmlevel = (UFBoolean) dmdatavo
        .getAttributeValue("bsltfrmlevel");

    if (bsltfrmlevel == null || !bsltfrmlevel.booleanValue()) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140216",
          "UPP40140216-000108")/* @res "该行不能指定批量价格！" */);
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
   * 子类实现该方法，返回业务界面的标题。
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
   * 由其它原因改变界面。 创建日期：(2001-11-15 9:18:13)
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
   * 由其它原因改变界面。 创建日期：(2001-11-15 9:18:13)
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

    // 只有计价依据为件数时上限类型才可用
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

      // 只有计价依据为整车时，路线可录入
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
    //整合
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

