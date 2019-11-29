package nc.ui.dm.dm102;

import java.awt.Container;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

import nc.bs.framework.common.NCLocator;
import nc.itf.baoyin.common.PubDelegator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bd.datapower.DataPowerServ;
import nc.ui.dm.dm001.DelivorgHelper;
import nc.ui.dm.dm104.QueryXcl;
import nc.ui.dm.pub.ClientUIforPlan;
import nc.ui.dm.pub.DMBillStatus;
import nc.ui.dm.pub.DMQueryConditionDlg;
import nc.ui.dm.pub.DmHelper;
import nc.ui.dm.pub.cardpanel.DMBillCardPanel;
import nc.ui.dm.pub.mvc.ShowDelivOrg;
import nc.ui.dm.pub.ref.DMTextDocument;
import nc.ui.dm.pub.ref.DelivorgRef;
import nc.ui.dm.pub.ref.QueryToCalBodyRefModel;
import nc.ui.dm.pub.ref.QueryToWareHouseRefModel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.textfield.UITextType;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.bill.IBillRelaSortListener;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.pub.rino.DbUtil;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.pub.bill.IBillExtendFun;
import nc.ui.so.so033.ATPForOneInvUI;
import nc.vo.bd.def.DefVO;
import nc.vo.dm.dm001.DelivorgHeaderVO;
import nc.vo.dm.dm001.DelivorgVO;
import nc.vo.dm.dm102.OutbillHHeaderVO;
import nc.vo.dm.dm102.ValueRangeHashtableDeliverydailyplan;
import nc.vo.dm.pub.DMBillTypeConst;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.dm.pub.DMFreeVO;
import nc.vo.dm.pub.DMVO;
import nc.vo.dm.pub.DailyPlanStatus;
import nc.vo.dm.pub.VOCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.bill.BillTempletBodyVO;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.QueryConditionVO;
import nc.vo.scm.constant.CConstant;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.exp.ATPNotEnoughException;
import nc.vo.scm.ic.exp.ICNullFieldException;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;

public class ClientUI extends ClientUIforPlan implements IBillExtendFun,
    ILinkQuery {
	//参考按钮
	protected ButtonObject btnCKKCXX ;
  // 订单查询
  protected ButtonObject boOrder;

  // 生成日计划
  protected ButtonObject boGenDelivDailyPlan;

  // 执行
  protected ButtonObject boAction;

  // 全选
  protected ButtonObject boSelectAll;

  // 全消
  protected ButtonObject boUnSelectAll;
  
  //add by zwx 2014-12-23 现存量查询按钮
  protected ButtonObject boOnhandnum; 

  protected ChooseCloseOrderDLG m_ChooseCloseOrderDLG;

  protected String m_Title = nc.ui.ml.NCLangRes.getInstance().getStrByID(
      "40140404", "UPP40140404-000008")/* @res "发运日计划-卡片" */;

  private DMQueryConditionDlg ivjQueryConditionDlg = null;

  private QueryOrderConditionDlg ivjQueryOrderConditionDlg = null;

  // private DMBillCardPanel ivjFthBillCardPanell = null;
  // 发运日计划批修改界面
  private DMBillCardPanel ivjBatchBillCardPanel = null;

  protected DMDataVO[] m_headListVOs; // 列表表头

  protected Hashtable m_bodyListHt; // 列表表体

  protected ButtonObject boBatch;

  private boolean m_bIsBatch;

  private ButtonObject[] aryButtonBatchGroup = null;// 批改按钮

  protected ButtonObject boReturn;// 批改返回按钮
  
  //节点由何处触发打开，默认为从节点树正常打开
  private int opentype = ILinkType.NONLINK_TYPE ;

  public ClientUI() {
    super();
    initializeNew();
  }

  
  public ClientUI( FramePanel fp ) {
    this.opentype = fp.getLinkType();
  }
  
  protected String checkPrerequisite() {
    //非联查打开节点
    if( this.opentype  != ILinkType.LINK_TYPE_QUERY ){
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
  
  
  /**
   * 初始化前改变界面。 创建日期：(2001-11-15 9:20:13)
   * 
   * @param bdData
   *          nc.ui.pub.bill.BillData
   */
  public void setSecCardPanel(BillData bdData) {

    super.setSecCardPanel(bdData);

    try {
      // 修改自定义项
      bdData = changeSecBillDataByUserDef(bdData);
    }
    catch (Exception e) {
    }
  }

  /**
   * 功能：修改日计划批修改界面的自定义项 日期：(2001-11-8 19:47:29) 修改日期，修改人，修改原因，注释标志：
   * 
   * @author zxping
   * @return nc.ui.pub.bill.BillData
   * @param oldBillData
   *          nc.ui.pub.bill.BillData
   */
  private BillData changeBatchEditBillDataByUserDef(BillData oldBillData) {
    updateItemByDef(oldBillData, getDefVOsH(), "vuserdef", true, 0);

    // 日计划批修改表头的自定义项包含了源订单表头的20个自定义项，还有源订单表体的20个自定义项
    updateItemByDef(oldBillData, getDefVOsH(), "vuserdef", false, 0);
    updateItemByDef(oldBillData, getDefVOsB(), "vuserdef_b_", false, 0);

    return oldBillData;
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
	  
    if (m_bIsBatch) {
      afterBatchEdit(e);
    }
    else {
      if(getEditFlag() != DMBillStatus.CardEdit && getEditFlag() != DMBillStatus.CardNew)
  	  {
  		  return;
  	  }
      String sKey = e.getKey();
      if (e.getPos() == BillItem.HEAD) {
        // 数量
        if (sKey.equals("dnum")) {
          afterNumEdit(e);
          getSecBillCardPanel().execHeadFormula("dmoney->dnum*dunitprice");
        }
        // 辅数量
        else if (sKey.equals("dassistnum")) {
          afterAstNumEdit(e);
        }
        /*
         * //单价 else if (sKey.equals("dunitprice"))
         * getSecBillCardPanel().execHeadFormula("dmoney->dnum*dunitprice");
         * //金额 else if (sKey.equals("dmoney"))
         * getSecBillCardPanel().execHeadFormula("dunitprice->dmoney/dnum");
         */
        // 订单要求到货
        else if (sKey.equals("requiredate"))
          afterRequireDate();
        // 计划发货日期
        else if (sKey.equals("snddate"))
          afterSendDate();
        // 发货库存组织
        else if (sKey.equals("pksendstoreorg"))
        {
          afterSendStoreOrg();
          //清空发货仓库
          UIRefPane sendstoreRef = (UIRefPane) getSecBillCardPanel().getHeadItem(
          "pksendstore").getComponent();
          sendstoreRef.setPK(null);
        }
        // 发运方式
        else if (sKey.equals("pksendtype"))
          afterSendType();
        // 收货单位
        else if (sKey.equals("creceiptcorpid")) {
          afterReceiptCorp();
        }
        // 到货地区
        else if (sKey.equals("pkarrivearea")) {
          UIRefPane arriveRef = (UIRefPane) getSecBillCardPanel().getHeadItem(
              "pkarrivearea").getComponent();
          getSecBillCardPanel().getHeadItem("vdestarea").setValue(
              arriveRef.getText());
        }
        // 发货仓库
        else if (sKey.equals("pksendstore")) {
          UIRefPane arriveRef = (UIRefPane) getSecBillCardPanel().getHeadItem(
              "pksendstore").getComponent();
          getSecBillCardPanel().getHeadItem("vsendstorename").setValue(
              arriveRef.getText());
          //清批次号
          getSecBillCardPanel().getHeadItem("vbatchcode").setValue(null);
          getLotNumbRefPaneHead().setValue(null);
          initHeaderLot(getSecBillCardPanel());
        }
        // 辅计量
        else if (sKey.equals("pkassistmeasure")) {
          afterAstUOMEdit(e);
          UIRefPane arriveRef = (UIRefPane) getSecBillCardPanel().getHeadItem(
              "pkassistmeasure").getComponent();
          getSecBillCardPanel().getHeadItem("vassistmeaname").setValue(
              arriveRef.getText());
        }
        // 自由项
        else if (sKey.equals("vfree0")) {
          afterHeaderFreeItemEdit(getSecBillCardPanel());
        }
        // 批次号
        else if (sKey.equals("vbatchcode")) {
          // getSecBillCardPanel().getHeadItem("vbatchcode").setValue(e.getValue());
          afterHeaderLotEdit(getSecBillCardPanel());
        }
        else if (sKey.equals("pkdeststore")) {
          getSecBillCardPanel()
              .execHeadFormula(
                  "vdeststoreaddre->getColValue(bd_stordoc,storaddr,pk_stordoc,pkdeststore)");
        }
        else if (sKey.equals("pksendaddress")) { // 发货地点
          UIRefPane currentRef = ((UIRefPane) getBillCardPanel().getBodyItem(
              sKey).getComponent());
          getSecBillCardPanel().getHeadItem("vsendaddress").setValue(
              currentRef.getText());
        }
        else if (sKey.equals("pkarriveaddress")) { // 收货地点
          UIRefPane currentRef = ((UIRefPane) getBillCardPanel().getBodyItem(
              sKey).getComponent());
          getSecBillCardPanel().getHeadItem("varriveaddress").setValue(
              currentRef.getText());
        }
      }
    }
  }

  /**
   * 功能：修改日计划卡片界面的自定义项 日期：(2001-11-8 19:47:29) 修改日期，修改人，修改原因，注释标志：
   * 
   * @author zxping
   * @return nc.ui.pub.bill.BillData
   * @param oldBillData
   *          nc.ui.pub.bill.BillData
   */
  private BillData changeSecBillDataByUserDef(BillData oldBillData) {

    // 日计划表单表头的自定义项包含了源订单表头的20个自定义项，还有源订单表体的20个自定义项
    updateItemByDef(oldBillData, getDefVOsH(), "vuserdef", true, 0);
    updateItemByDef(oldBillData, getDefVOsB(), "vuserdef_b_", true, 0);

    return oldBillData;
  }

  /**
   * 应发辅数量编辑后，进行的事件处理。
   * <p>
   * 注意： 辅数量*换算率=数量
   * 
   * @param e
   *          单据编辑事件
   * @author 余大英
   */
  private void afterBatchAstNumEdit(nc.ui.pub.bill.BillEditEvent e) {
    String sNumItemKey = "dnum";
    String sAstItemKey = "dassistnum";
    int row = e.getRow(); // 编辑事件所涉及的行
    Object oTemp = null;

    // 取得存货id
    oTemp = getBatchBillCardPanel().getBodyValueAt(row, pkinv);
    if (oTemp == null) {
      SCMEnv.info("缺少存货 id ");
      return;
    }

    UFDouble hsl = null;

    // commented by zxping
    // if (!getHsl(row, hsl))
    // return;

    // 如果辅单位主键为空，则清除“辅单位”和“辅数量”的值
    Object oTemp0 = getBatchBillCardPanel().getBodyValueAt(row,
        "pkassistmeasure");
    // 辅单位主键
    if (oTemp0 == null || oTemp0.toString().trim().length() == 0) {
      nc.ui.pub.beans.MessageDialog
          .showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "40140404", "UPP40140404-000009")/* @res "辅数量编辑" */,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
                  "UPP40140404-000010")/* @res "请输入辅计量！" */);
      getBatchBillCardPanel().setBodyValueAt(null, row, "vassistmeaname"); // 辅单位
      getBatchBillCardPanel().setBodyValueAt(null, row, "dassistnum"); // 辅数量
    }

    // 根据辅计量id和存货id，查询存货的详细信息（包括换算率）
    String sInvID = oTemp.toString();
    oTemp = getBatchBillCardPanel().getBodyValueAt(row, "pkassistmeasure");
    String sAstID = null;
    if (oTemp != null)
      sAstID = oTemp.toString();
    InvVO voaInv[] = getInvInfo(new String[] {
      sInvID
    }, new String[] {
      sAstID
    });
    // 查存货信息，带辅计量单位
    if (voaInv == null || voaInv.length == 0 || voaInv[0] == null) {
      SCMEnv.info("没有对应的存货 vo");
      return;
    }
    InvVO invvo = voaInv[0];

    // 当辅数量为空时的处理
    if (getBatchBillCardPanel().getBodyValueAt(row, sAstItemKey) == null
        || getBatchBillCardPanel().getBodyValueAt(row, sAstItemKey).toString()
            .trim().length() == 0) {

      // 辅数量为空清空，则清空数量
      getBatchBillCardPanel().setBodyValueAt(null, row, sNumItemKey);

      // 数量列的值清空后，强制执行对表体行数量改变后的处理
      afterBatchNumEdit(e);
      return;
    }

    UFDouble ninassistnum = new UFDouble(getBatchBillCardPanel()
        .getBodyValueAt(row, sAstItemKey).toString().trim());

    // 修改数量
    // hsl = invvo.getHsl();
    hsl = new UFDouble(dTransRate);

    if (hsl != null && hsl.doubleValue() > 0) {
      UFDouble ninnum = ninassistnum.multiply(hsl);
      getBatchBillCardPanel().setBodyValueAt(ninnum, row, sNumItemKey);

      // 数量列的值改变后，强制执行对表体行数量改变后的处理
      afterBatchNumEdit(e);
    }

  }

  /**
   * 单据编辑事件处理
   * 
   * @param e
   *          单据编辑事件
   * @author 王乃军
   */
  private void afterBatchAstUOMEdit(nc.ui.pub.bill.BillEditEvent e) {
    int row = e.getRow();

    String sNumItemKey = "dnum";
    String sAstItemKey = "dassistnum";

    UFDouble hsl = null;
    if (!getBatchHsl(row, hsl))
      return;

    UFDouble ninnum = null;
    UFDouble ninassistnum = null;
    Object oinnum = getBatchBillCardPanel().getBodyValueAt(row, sNumItemKey);
    Object oinassistnum = getBatchBillCardPanel().getBodyValueAt(row,
        sAstItemKey);
    if (oinnum != null)
      ninnum = new UFDouble(oinnum.toString().trim());
    if (oinassistnum != null)
      ninassistnum = new UFDouble(oinassistnum.toString().trim());

    if ((ninnum == null && ninassistnum == null) || hsl == null)
      // || isFixFlag == null)
      return;

    /* 辅计量：换算率；无论固定，变动换算率，按辅数量＊换算率来重新计算主数量 */
    if (ninassistnum != null) {
      ninnum = ninassistnum.multiply(hsl);
      getBatchBillCardPanel().setBodyValueAt(ninnum, row, sNumItemKey);
    }
    else {
      getBatchBillCardPanel().setBodyValueAt(null, row, sNumItemKey);
    }
    // 强制调用“数量”改变事件处理
    afterBatchNumEdit(e);

    // 清批次号
    getBatchBillCardPanel().setBodyValueAt(null, row, "vbatchcode");
    getLotNumbRefPane().setValue(null);
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
  private void afterBatchEdit(BillEditEvent e) {
    String sKey = e.getKey();
    int iRow = e.getRow();
    if (e.getPos() == BillItem.HEAD) {
      UIRefPane ref = (UIRefPane) getBatchBillCardPanel().getHeadItem(sKey)
          .getComponent();
      String sNameValue = ref.getRefName();
      String sName = null;
      String sValue = ref.getRefPK();
      // 发运方式
      if (sKey.equals("pksendtype")) {
        sName = "vsendtypename";
      }
      // 发货库存组织
      else if (sKey.equals("pksendstoreorg")) {
        sName = "vsendstoreorgname";
        // 发货仓库
        UIRefPane refPaneSendstore = (UIRefPane) getBatchBillCardPanel()
            .getHeadItem("pksendstore").getComponent();
        if (sValue != null && sValue.trim().length() > 0) {
          refPaneSendstore.setPK(null);
          getBatchBillCardPanel().getHeadItem("pksendstore").setEnabled(true);
          refPaneSendstore.setWhereString(" pk_calbody = '" + sValue
              + "' and gubflag = 'N' and sealflag ='N' ");
        }
        else {
          refPaneSendstore.setPK(null);
          getBatchBillCardPanel().getHeadItem("pksendstore").setEnabled(false);
        }
        // 引起仓库变化
        afterBatchEdit(new BillEditEvent(getBatchBillCardPanel(), null, null,
            "pksendstore", 0, BillItem.HEAD));
      }
      // 发货仓库
      else if (sKey.equals("pksendstore")) {
        sName = "vsendstorename";
        for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
          getBatchBillCardPanel().setBodyValueAt(null, i, "vbatchcode");
        }
      }
      // 发货地区
      else if (sKey.equals("pksrccalbodyar")) {
        sName = "vsendarea";
      }
      // 发货地点
      else if (sKey.equals("pksendaddress")) {
        sName = "vsendaddress";
      }
      // 到货库存组织
      else if (sKey.equals("pkdeststockorg")) {
        sName = "vdeststoreorgname";
      }
      // 到货仓库
      else if (sKey.equals("pkdeststore")) {
        sName = "vdeststorename";
      }
      // 到货地区
      else if (sKey.equals("pkarrivearea")) {
        sName = "vdestarea";
      }
      // 到货地点
      else if (sKey.equals("pkarriveaddress")) {
        sName = "varriveaddress";
      }
      // 到货地址
      else if (sKey.equals("vdestaddress")) {
        sName = null;
        sValue = getBatchBillCardPanel().getHeadItem(sKey).getValue();
      }
      // 发货地址
      else if (sKey.equals("vsendaddr")) {
        sName = null;
        sValue = getBatchBillCardPanel().getHeadItem(sKey).getValue();
      }
      // 收货单位
      else if (sKey.equals("creceiptcorpid")) {
        sName = "creceiptcorpname";
        sValue = getBatchBillCardPanel().getHeadItem(sKey).getValue();
      }
      for (int i = 0; i < getBatchBillCardPanel().getRowCount(); i++) {
        if (getBatchBillCardPanel().getBillModel().getValueAt(i, "iplanstatus")
            .toString().equals(
                nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
                    "UPP40140404-000011")/* @res "自由状态" */)) {
          if (sKey != null)
            getBatchBillCardPanel().getBillModel().setValueAt(sValue, i, sKey);
          if (sName != null)
            getBatchBillCardPanel().getBillModel().setValueAt(sNameValue, i,
                sName);
          getBatchBillCardPanel().getBillModel().setRowState(i,
              BillModel.MODIFICATION);
        }
      }
    }
    else if (e.getPos() == BillItem.BODY) {
      String sName = null;
      // 数量
      if (sKey.equals("dnum")) {
        afterBatchNumEdit(e);
      }
      // 辅数量
      else if (sKey.equals("dassistnum")) {
        afterBatchAstNumEdit(e);
      }
      // 辅计量
      else if (sKey.equals("vassistmeaname")) {
        afterBatchAstUOMEdit(e);
        // UIRefPane arriveRef = (UIRefPane)
        // getSecBillCardPanel().getHeadItem("pkassistmeasure").getComponent();
        // getSecBillCardPanel().getHeadItem("vassistmeaname").setValue(arriveRef.getText());
      }
      // 自由项
      else if (sKey.equals("vfree0")) {
        afterBodyFreeItemEdit(iRow, getBatchBillCardPanel().getBillModel()
            .getBodyColByKey("vfree0"),
            (DMBillCardPanel) getBatchBillCardPanel());

      }
      // 批次
      else if (sKey.equals("vbatchcode")) {
        afterBodyLotEdit(iRow, getBatchBillCardPanel().getBillModel()
            .getBodyColByKey("vbatchcode"),
            (DMBillCardPanel) getBatchBillCardPanel());
      }
      // 发运方式
      else if (sKey.equals("vsendtypename")) {
        sName = "pksendtype";
      }
      // 发货库存组织
      else if (sKey.equals("vsendstoreorgname")) {
        sName = "pksendstoreorg";
        getBatchBillCardPanel().setBodyValueAt(null, iRow, "vsendstorename");
        getBatchBillCardPanel().setBodyValueAt(null, iRow, "pksendstore");
        getBatchBillCardPanel().setBodyValueAt(null, iRow, "vbatchcode");
        getLotNumbRefPane().setValue(null);
        // initBodyLot(iRow,)
      }
      // 发货仓库
      else if (sKey.equals("vsendstorename")) {
        sName = "pksendstore";
        getBatchBillCardPanel().setBodyValueAt(null, iRow, "vbatchcode");
        getLotNumbRefPane().setValue(null);
        // initBodyLot(iRow,)
      }
      // 发货地区
      else if (sKey.equals("vsendarea")) {
        sName = "pksrccalbodyar";
      }
      // 发货地点
      else if (sKey.equals("vsendaddress")) {
        sName = "pksendaddress";
      }
      // 到货库存组织
      else if (sKey.equals("vdeststoreorgname")) {
        sName = "pkdeststockorg";
      }
      // 到货仓库
      else if (sKey.equals("vdeststorename")) {
        sName = "pkdeststore";
      }
      // 到货地区
      else if (sKey.equals("vdestarea")) {
        sName = "pkarrivearea";
      }
      // 到货地点
      else if (sKey.equals("varriveaddress")) {
        sName = "pkarriveaddress";
      }
      // 到货地址
      else if (sKey.equals("vdestaddress")) {
        sName = "vdestaddress";
      }
      // 发货地址
      else if (sKey.equals("vsendaddr")) {
        sName = "vsendaddr";
      }
      if (sName != null && getBatchBillCardPanel().getHeadItem(sName) != null) {
        getBatchBillCardPanel().getHeadItem(sName).setValue(null);
      }

    }
  }

  /**
   * 订单查询后加载数据。 创建日期：(2002-5-16 20:33:00)
   */
  private void afterOrderQuery(nc.vo.pub.query.ConditionVO[] voCons) {
    try {
      if (null == ivjSecBillCardPanel)
        loadSecCardTemplet();
      if (null == ivjThdBillCardPanel)
        loadThdCardTemplet();

      // 查询调拨订单
      long lStartTime = System.currentTimeMillis();
      long lTime = 0;

      // 常用条件
      DMDataVO voNormal = new DMDataVO();
      voNormal.setAttributeValue("ClientLink", new ClientLink(ClientEnvironment
          .getInstance()));
      voNormal.setAttributeValue("DeputCorpPKs", getAgentCorpIDsofDelivOrg());
      ArrayList alAllOrderVOs = DeliverydailyplanBO_Client
          .queryOrders4DailyPlan(voCons, voNormal);

      // 库存调拨订单（3.0以后已经废除）
      DMDataVO[] dmdata4U = (DMDataVO[]) alAllOrderVOs.get(0);
      // 销售订单
      DMDataVO[] dmdataSO = (DMDataVO[]) alAllOrderVOs.get(1);
      // 三方调拨订单
      DMDataVO[] dmdata5C = (DMDataVO[]) alAllOrderVOs.get(2);
      // 公司间调拨订单
      DMDataVO[] dmdata5D = (DMDataVO[]) alAllOrderVOs.get(3);
      // 组织间调拨订单
      DMDataVO[] dmdata5E = (DMDataVO[]) alAllOrderVOs.get(4);
      // 组织内调拨订单
      DMDataVO[] dmdata5I = (DMDataVO[]) alAllOrderVOs.get(5);
      // 采购订单
      DMDataVO[] dmdataPO = (DMDataVO[]) alAllOrderVOs.get(6);

      DMVO ordervo = new DMVO();

      ordervo.setChildrenVO(dmdata4U);
      ordervo.appendBodyVO(dmdataSO);
      ordervo.appendBodyVO(dmdata5C);
      ordervo.appendBodyVO(dmdata5D);
      ordervo.appendBodyVO(dmdata5E);
      ordervo.appendBodyVO(dmdata5I);
      ordervo.appendBodyVO(dmdataPO);

      // 存货自由项改为公式带出
      DMDataVO[] dmdvos = ordervo.getBodyVOs();
      if (dmdvos != null && dmdvos.length > 0) {
        nc.ui.scm.pub.FreeVOParse freeVOParse = new nc.ui.scm.pub.FreeVOParse();
        freeVOParse.setFreeVO(dmdvos, null, "pkinv", false);
      }
      m_initOrdervos = ordervo;

      if (null == m_initOrdervos.getBodyVOs()
          || m_initOrdervos.getBodyVOs().length == 0) {
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
            "UPP40140404-000012")/* @res "请检查查询内容以及发运组织代理库存组织权限是否正确!" */);
      }

      lStartTime = System.currentTimeMillis();
      getThdBillCardPanel().setBillValueVO(m_initOrdervos, false);
      showMethodTime("加载数据", lStartTime);
      // lTime = System.currentTimeMillis() - lStartTime;
      // SCMEnv.info("================执行<" + "加载数据" + ">消耗的时间为：" + (lTime /
      // 60000) + "分" + ((lTime / 1000) % 60) + "秒"
      // + (lTime % 1000) + "毫秒================");

      lStartTime = System.currentTimeMillis();
      // getThdBillCardPanel().getBillModel().execLoadFormula();

      //add by danxionghui NO 20
      //add end
      for (int i = 0; dmdvos != null && i < dmdvos.length; i++) {
    	  //add by danxionghui NO 20
    	  String invpk = getThdBillCardPanel().getBodyValueAt(i, "pkinv").toString();
    	  String pk_corp =getCorpID();
    	  //库存组织主键
    	  String kczz="select pk_calbody from bd_calbody where pk_corp='"+pk_corp+"'";
    	  Object ob = null;
          ob=PubDelegator.getUAPQueryBS().executeQuery(kczz, new ColumnProcessor());
    	  String sql="select sum(nonhandnum) from ic_onhandnum where nvl(dr, 0) = 0 and  cinventoryid='"+invpk+"' and pk_corp='"+pk_corp+"' and ccalbodyid='"+ob+"'";
    	  //结存数
    	  getThdBillCardPanel().setBodyValueAt(DbUtil.getDMO().getObject(sql), i, "nonhandnum");
    	  //冻结数
    	  String sql2="select sum(nfreezenum) from ic_freeze  where nvl(dr, 0) = 0 and cinventoryid='"+invpk+"' and pk_corp='"+pk_corp+"' and ccalbodyid='"+ob+"' and cthawpersonid is null and cspaceid is not null ";
    	 // String sql2="select sum(nfreezenum) from ic_freeze  where nvl(dr, 0) = 0 and cinventoryid='"+invpk+"' and pk_corp='"+pk_corp+"' and ccalbodyid='"+ob+"' ";
    	  getThdBillCardPanel().setBodyValueAt(DbUtil.getDMO().getObject(sql2), i, "nfreezenum");
    	  //add end
    	  
    	  // 对于来源于采购订单，发货地区、发货地址由上游单据 vo 对照直接传递过来
        // 对于来源于销售订单或者内部调拨订单，发货地区、发货地址由发货库存组织的值确定
        if (!getThdBillCardPanel().getBodyValueAt(i, "vbilltype").toString()
            .equals("21")) {
          getThdBillCardPanel()
              .getBillModel()
              .execFormulas(
                  i,
                  new String[] {
                      "pksendarea->getColValue(bd_calbody,pk_areacl,pk_calbody,pksendstoreorg)",
                      "vsendaddr->getColValue(bd_stordoc,storaddr,pk_stordoc,pksendstore)",
                      "vsendaddr->iif(vsendaddr==null,getColValue(bd_calbody,area,pk_calbody,pksendstoreorg),vsendaddr)",
                      "pksendaddress->getColValue(bd_stordoc,pk_address,pk_stordoc,pksendstore)",
                      "pksendaddress->iif(pksendaddress==null,getColValue(bd_calbody,pk_address,pk_calbody,pksendstoreorg),pksendaddress)",
                  });
        }
      }
      // 由于客户化代码出现错误，
      // 因此将所有的加载公式改到代码中执行2005-01-31
      getThdBillCardPanel()
          .getBillModel()
          .execFormulas(
              new String[] { 
            		  //add by danxionghui  no 22
            		  //doutnum  
            		 // 剩余未发货数= 订单总量-已发货数量 (dnum-项目主键	doutnum)
            	  "sywhs->dnum-doutnum",
            	
            	  //存货档案主键
                  "pkbasinv->getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,pkinv)",
                  //kckhs  库存总可发货数= 结存数-冻结数(nonhandnum-nfreezenum)
            	"kckhs->nonhandnum-nfreezenum",
            	  //add end 
                  "vinvcode->getColValue(bd_invbasdoc,invcode,pk_invbasdoc,pkbasinv)",
                  "vinvname->getColValue(bd_invbasdoc,invname,pk_invbasdoc,pkbasinv)",
                  "vspec->getColValue(bd_invbasdoc,invspec,pk_invbasdoc,pkbasinv)",
                  "vtype->getColValue(bd_invbasdoc,invtype,pk_invbasdoc,pkbasinv)",
                  "vunitid->getColValue(bd_invbasdoc,pk_measdoc,pk_invbasdoc,pkbasinv)",
                  "vunit->getColValue(bd_measdoc,measname,pk_measdoc,vunitid)",
                  "cquoteunitname->getColValue(bd_measdoc,measname,pk_measdoc,cquoteunitid)",
                  "dvolumntmp->getColValue(bd_invbasdoc,unitvolume,pk_invbasdoc,pkbasinv)",
                  "dvolumn->dnum*toNumber(dvolumntmp)",
                  "dweighttmp->getColValue(bd_invbasdoc,unitweight,pk_invbasdoc,pkbasinv)",
                  "dweight->dnum*toNumber(dweighttmp)",
                  "vassistmeaname->getColValue(bd_measdoc,measname,pk_measdoc,pkassistmeasure)",

                  "pkbascust->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc, pkcust)",
                  "vcustname->getColValue(bd_cubasdoc,custname,pk_cubasdoc,pkbascust)",

                  "vdeststoreorgname->getColValue(bd_calbody,bodyname,pk_calbody,pkdeststoreorg)",

                  "vdeststorename->getColValue(bd_stordoc,storname,pk_stordoc,pkdeststore)",
                  "vdeststoreaddre->getColValue(bd_stordoc,storaddr,pk_stordoc,pkdeststore)",

                  "vsendtypename->getColValue(bd_sendtype,sendname,pk_sendtype,pksendtype)",

                  "vsalecorpname->getColValue(bd_corp,unitname,pk_corp,pksalecorp)",

                  "vsaleorgname->getColValue(bd_salestru,vsalestruname,csalestruid,pksaleorg)",

                  "vsendstoreorgname->getColValue(bd_calbody,bodyname,pk_calbody,pksendstoreorg)",

                  "vsendstorename->getColValue(bd_stordoc,storname,pk_stordoc,pksendstore)",

                  "vbilltypename->getColValue(bd_billtype,billtypename,pk_billtypecode,vbilltype)",

                  "vunit->getColValue(bd_measdoc,measname,pk_measdoc,vunitid)",

                  "vdestarea->getColValue(bd_areacl,areaclname,pk_areacl,pkarrivearea)",

                  "operatorname->getColValue(bd_psndoc,psnname,pk_psndoc,pkoperator)",

                  "pksendcorpbas->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcorpid)",
                  "creceiptcorpname->getColValue(bd_cubasdoc,custname,pk_cubasdoc,pksendcorpbas)",

                  "varrivecorpname->getColValue(bd_corp,unitname,pk_corp,pkarrivecorp)",

                  "varriveaddress->getColValue(bd_address,addrname,pk_address,pkarriveaddress)",
                  // "pksendaddress->getColValue(bd_stordoc,pk_address,pk_stordoc,pksendstore)",
                  // "pksendaddress->iif(pksendaddress==null,getColValue(bd_calbody,pk_address,pk_calbody,pksendstoreorg),pksendaddress)",
                  "vsendaddress->getColValue(bd_address,addrname,pk_address,pksendaddress)",

                  "pkreceiptcorpbas->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,csendcorpid)",
                  "csendcorpname->getColValue(bd_cubasdoc,custname,pk_cubasdoc,pkreceiptcorpbas)",
                  "vsendarea->getColValue(bd_areacl,areaclname,pk_areacl,pksendarea)"
              });

      showMethodTime("加载公式", lStartTime);

      getThdBillCardPanel().updateValue();
      getThdBillCardPanel().setEnabled(true);
      getThdBillCardPanel().getBodyItem("bchoose").setEnabled(true);
      getThdBillCardPanel().getBodyItem("bchoose").setEdit(true);
      switchButtonStatus(DMBillStatus.ListView);

      // //排序界面
      // DMListData[] dmlds= new DMListData[5];
      // dmlds[0]= new DMListData("vsrcbillnum", "订单号", 1);
      // dmlds[1]= new DMListData("vcustname", "订货客户", 1);
      // dmlds[2]= new DMListData("vdeststoreorgname", "到货库存组织", 1);
      // dmlds[3]= new DMListData("vsendtypename", "发运方式", 1);
      // dmlds[4]= new DMListData("creceiptcorpname", "收货单位", 1);
      // ((DMBillCardPanel)
      // getThdBillCardPanel()).setArylistdataMustSelect(dmlds);
      // ((DMBillCardPanel) getThdBillCardPanel()).onSortWithoutShowDlg();

      // 取得界面VO数组
      DMVO dmvo = (DMVO) getThdBillCardPanel().getBillValueVO(
          DMVO.class.getName(), DMDataVO.class.getName(),
          DMDataVO.class.getName());
      // 生成新的VO
      String[] sSortKeys = new String[] {
          "vsrcbillnum", "vcustname", "vdeststoreorgname", "vsendtypename",
          "creceiptcorpname"
      };
      int[] iSortTypes = new int[] {
          nc.vo.scm.pub.SortType.ASC, nc.vo.scm.pub.SortType.ASC,
          nc.vo.scm.pub.SortType.ASC, nc.vo.scm.pub.SortType.ASC,
          nc.vo.scm.pub.SortType.ASC
      };
      // 向界面中置入新的VO
      //add by danxionghui
      if(dmvo != null && dmvo.getBodyVOs().length > 0){
    	  DMDataVO[] dvos =   dmvo.getBodyVOs();
    	  for (int i = 0; i < dvos.length; i++) {
    		  DMDataVO dvo = dvos[i];
    		  UFDouble str = (UFDouble)dvo.getAttributeValue("dsendnum");//dsendnum
    		  //项目主键	doutnum已出库数量
    		  UFDouble str1 = (UFDouble)dvo.getAttributeValue("doutnum");//dsendnum
    		  UFDouble str2 = (UFDouble)dvo.getAttributeValue("nfreezenum");//nfreezenum
    		  if(str == null ){
    			  dvo.setAttributeValue("dsendnum", new UFDouble(0.00));
    		  }
    		  if(str1 == null){
    			  dvo.setAttributeValue("doutnum", new UFDouble(0.00));
    		  }
    		  if(str2 == null ){
    			  dvo.setAttributeValue("nfreezenum", new UFDouble(0.00));
    		  }
    		}
      }
      //String dsendnum = getThdBillCardPanel().getBodyItem("dsendnum").getValue();
      //end 
      
      getThdBillCardPanel().setBillValueVO(dmvo, false);
      String[] value = new String[] {dmdvos == null ? "0" : String.valueOf(dmdvos.length)};
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
              "UPP40140404-000110", null, value)/* @res "共有{0}条符合条件的记录！" */);
      // 转入相应的窗口——订单界面
      if (getEditFlag() == DMBillStatus.CardNew
          || getEditFlag() == DMBillStatus.CardEdit) {
        setShowState(DMBillStatus.Card);
        setTitleText(strTitle2);
      }
      else {
        setShowState(DMBillStatus.Source);
        setTitleText(strTitle3);
      }
      setButtons(getBillButtons());
      switchInterface();
    }
    catch (Exception e) {
      e.printStackTrace();
      showErrorMessage(e.getMessage());
    }
  }

  /**
   * 此处插入方法说明。 创建日期：(2002-5-16 20:33:00)
   */
  public void afterQuery(nc.vo.pub.query.ConditionVO[] vos) {

    // 常用条件
    DMDataVO voNormal = new DMDataVO();
    voNormal.setAttributeValue("ClientLink", new ClientLink(ClientEnvironment
        .getInstance()));
    voNormal.setAttributeValue("DeputCorpPKs", getAgentCorpIDsofDelivOrg());

    queryBill(voNormal,vos);
  }

  /**
   * 订单要求发货日期。 功能： 参数： 返回： 例外： 日期：(2002-9-11 15:08:43) 修改日期，修改人，修改原因，注释标志：
   */
  private void afterRequireDate() {
    // 计划日期
    UFDate snddate = new UFDate(getSecBillCardPanel().getHeadItem("snddate")
        .getValue());
    // 要求到货日期
    UFDate requiredate = new UFDate(getSecBillCardPanel().getHeadItem(
        "requiredate").getValue());
    // 发货库存组织
    String pksendstoreorg = (String) getSecBillCardPanel().getHeadItem(
        "pksendstoreorg").getValue();
    // 到货地区
    String pkarrivearea = (String) getSecBillCardPanel().getHeadItem(
        "pkarrivearea").getValue();
    // 发运方式
    String pksendtype = (String) getSecBillCardPanel()
        .getHeadItem("pksendtype").getValue();
    try {
      UFDate retdate = DeliverydailyplanBO_Client.getOtherDateByAheadPeriod(
          pksendtype, pksendstoreorg, pkarrivearea, null, requiredate);
      if (retdate != null)
        getSecBillCardPanel().getHeadItem("snddate").setValue(retdate);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 实际发货日期。 功能： 参数： 返回： 例外： 日期：(2002-9-11 15:08:43) 修改日期，修改人，修改原因，注释标志：
   */
  private void afterSendDate() {
    // 计划日期
    UFDate snddate = new UFDate(getSecBillCardPanel().getHeadItem("snddate")
        .getValue());
    // 要求到货日期
    UFDate requiredate = new UFDate(getSecBillCardPanel().getHeadItem(
        "requiredate").getValue());
    // 发货库存组织
    String pksendstoreorg = (String) getSecBillCardPanel().getHeadItem(
        "pksendstoreorg").getValue();
    // 到货地区
    String pkarrivearea = (String) getSecBillCardPanel().getHeadItem(
        "pkarrivearea").getValue();
    // 发运方式
    String pksendtype = (String) getSecBillCardPanel()
        .getHeadItem("pksendtype").getValue();
    try {
      UFDate retdate = DeliverydailyplanBO_Client.getOtherDateByAheadPeriod(
          pksendtype, pksendstoreorg, pkarrivearea, snddate, null);
      if (retdate != null)
        getSecBillCardPanel().getHeadItem("requiredate").setValue(retdate);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 编辑前事件处理。 创建日期：(2001-6-23 13:42:53)
   * 
   * @return boolean
   */
  public boolean beforeEdit(BillEditEvent e) {
    String sKey = e.getKey();
    if (m_bIsBatch) {
      if (e.getPos() == BillItem.BODY) {
        if (getBatchBillCardPanel().getBillModel().getValueAt(e.getRow(),
            "iplanstatus").toString().equals(
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
                "UPP40140404-000011")/* @res "自由状态" */)) {
          return true;
        }
        else {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * 行列变换 创建日期：(01-2-26 13:29:17)
   */
  public void bodyRowChange(BillEditEvent e) {
    if (m_bIsBatch)
      return;
    if (!m_strShowState.equals(DMBillStatus.List))
      return;

    if (e.getSource() == getBillListPanel().getHeadTable()) {
      m_selectRow = e.getRow();
      Object pkbillh = m_headListVOs[m_selectRow].getAttributeValue("pkbillh");
      m_oSelectHid = pkbillh;
      ArrayList v = (ArrayList) m_bodyListHt.get(pkbillh);
      if (v != null && v.size() != 0) {
        DMDataVO[] bodyvos = new DMDataVO[v.size()];
        bodyvos = (DMDataVO[]) v.toArray(bodyvos);
        v = null;
        execFormulaBodys(bodyvos);
        getBillListPanel().setBodyValueVO(bodyvos);
        getBillListPanel().getBodyBillModel().execLoadFormula();
        getBillListPanel().getBodyBillModel().updateValue();
        getBillListPanel().getBodyTable().clearSelection();
        m_planvos = bodyvos;
        m_num = 0;
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "SCMCOMMON", "UPPSCMCommon-000253")/* @res "数据加载成功" */);
      }
    }
    else if (e.getSource() == getBillListPanel().getBodyTable()) {
      m_num = e.getRow();
      // int rowCount=
      // getBillListPanel().getBodyBillModel().getRowCount();
      m_planvos = (DMDataVO[]) getBillListPanel().getBodyBillModel()
          .getBodyValueVOs(DMDataVO.class.getName());
      // cloneVOs();
    }

  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-8-8 20:13:47) 修改日期，修改人，修改原因，注释标志：
   */
  public void checkIfOrderClose(UFDouble[] sonums, UFDouble[] alnums) {
    String sbillType = null; // 单据类型
    String sCode = null; // 日计划编号
    Hashtable ht = new Hashtable();
    UFDouble sparenum = new UFDouble(0);
    for (int i = 0; i < m_planvos.length; i++) {
      sbillType = (String) m_planvos[i].getAttributeValue("vbilltype");
      sCode = (String) m_planvos[i].getAttributeValue("vdelivdayplcode");
      if (sCode.equals("30"))
        ht.put(sCode, sonums[i]);
      else if (sCode.equals("4U"))
        ht.put(sCode, alnums[i]);
    }
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-7-25 20:05:39) 修改日期，修改人，修改原因，注释标志：
   */
  public boolean checkOther(AggregatedValueObject dmvo) {
    DMDataVO[] dmdvos = (DMDataVO[]) dmvo.getChildrenVO();
    UFDouble dnum = null; // 数量
    UFDouble dsendnum = null; // 已发运数量
    String sCode = null; // 单据号
    UFDate snddate = null; // new
    // UFDate(getBillCardPanel().getHeadItem("snddate").getValue());
    // //发货日期
    int ooo = 0;
    // 检查发运数量
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < dmdvos.length; i++) {
      dnum = (UFDouble) dmdvos[i].getAttributeValue("dnum");
      dsendnum = (UFDouble) dmdvos[i].getAttributeValue("dsendnum");
      sCode = (String) dmdvos[i].getAttributeValue("vdelivdayplcode");
      dmdvos[i].setAttributeValue("pkcorp", getCorpID()); // 用于getOID，不保存
      if (dsendnum == null)
        dsendnum = new UFDouble(0);
      if (dnum.doubleValue() < dsendnum.doubleValue()) {
        if (ooo != 0)
          sb.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
              "UPPSCMCommon-000000")/* @res "、" */
              + sCode);
        else
          sb.append(sCode);
        ooo++;
      }
    }
    if (sb.toString().trim().length() > 0) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
          "UPP40140404-000081", null, new String[] {
            sb.toString()
          })/* @res "数量大于已发运数量，单据号为：{0} 的日计划不正确！" */);
      return false;
    }
    // 检查发货日期
    StringBuffer strb = new StringBuffer();
    for (int i = 0; i < dmdvos.length; i++) {
      snddate = (UFDate) dmdvos[i].getAttributeValue("snddate");
      sCode = (String) dmdvos[i].getAttributeValue("vdelivdayplcode");
      dmdvos[i].setAttributeValue("pkcorp", getCorpID()); // 用于getOID，不保存

      if (snddate == null || snddate.before(getClientEnvironment().getDate())) {
        if (ooo != 0)
          strb.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
              "UPPSCMCommon-000000")/* @res "、" */
              + sCode);
        else
          strb.append(sCode);
        ooo++;
      }
    }
    if (strb.toString().trim().length() > 0) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
          "UPP40140404-000082", null, new String[] {
            strb.toString()
          })/* @res "发货日期不可空、不可小于当前日期，单据号为：{0} 的日计划不正确！" */);
      return false;
    }
    return true;
  }

  /*
   * VO转换 创建日期：(2002-6-19 16:17:42) @return nc.vo.dm.dm102.DMOrderBodyDataVO[]
   */
  /*
   * public DMDataVO[] convertSourceVO(AggregatedValueObject[] vos, String
   * sourceBillType, String destBillType) { ArrayList v = new ArrayList(); try {
   * //VO转换 DMVO[] dvo = (DMVO[]) nc.ui.pub.change.PfChangeBO_Client
   * .pfChangeBillToBillArray(vos, sourceBillType, destBillType); for (int i =
   * 0; i < dvo.length; i++) { for (int j = 0; j <
   * dvo[i].getChildrenVO().length; j++) { v.add(dvo[i].getChildrenVO()[j]); } } }
   * catch (Exception e) { e.printStackTrace(); } DMDataVO[] dmdvos = new
   * DMDataVO[v.size()]; dmdvos = (DMDataVO[]) v.toArray(dmdvos); v = null; for
   * (int i = 0; i < dmdvos.length; i++) { if
   * (dmdvos[i].getAttributeValue("vbilltype").equals("30")) { //处理来自内部交易非直运订单
   * if (dmdvos[i].getAttributeValue("pkdeststoreorg") != null &&
   * dmdvos[i].getAttributeValue("pkdeststoreorg") .toString().trim().length() >
   * 0) { dmdvos[i].setAttributeValue("pksendstoreorg", dmdvos[i]
   * .getAttributeValue("pkdeststoreorg"));
   * dmdvos[i].setAttributeValue("pkdeststoreorg", null); //将非直运收货仓库填入发货仓库 if
   * (dmdvos[i].getAttributeValue("pkdeststore") != null &&
   * dmdvos[i].getAttributeValue("pkdeststore") .toString().trim().length() > 0) {
   * dmdvos[i].setAttributeValue("pksendstore", dmdvos[i]
   * .getAttributeValue("pkdeststore"));
   * dmdvos[i].removeAttributeName("pkdeststore"); } //将非直运收货仓库填入发货仓库 if
   * (dmdvos[i].getAttributeValue("pkdeststore") != null &&
   * dmdvos[i].getAttributeValue("pkdeststore") .toString().trim().length() > 0) {
   * dmdvos[i].setAttributeValue("pksendstore", dmdvos[i]
   * .getAttributeValue("pkdeststore"));
   * dmdvos[i].removeAttributeName("pkdeststore"); } //清空到货地区
   * //dmdvos[i].removeAttributeName("pkarrivearea"); //清空到货地点
   * //dmdvos[i].removeAttributeName("pkarriveaddress"); } } } //将发货地区填入
   * //将仓库地点填入发货地点 SuperVOUtil .execFormulaWithVOs( dmdvos, new String[] {
   * "pksendarea->getColValue(bd_calbody,pk_areacl,pk_calbody,pksendstoreorg)",
   * "pksendaddress->iif(getColValue(bd_stordoc,pk_address,pk_stordoc,pksendstore) =
   * \"\",getColValue(bd_calbody,pk_address,pk_calbody,pksendstoreorg),getColValue(bd_stordoc,pk_address,pk_stordoc,pksendstore))" },
   * null); return dmdvos; }
   */

  /**
   * 创建者：毕晖 得到完整的单据表体VO，包括删除的行。 功能： 参数： 返回： 例外： 日期：(2002-7-10 17:01:33)
   * 修改日期，修改人，修改原因，注释标志：
   */
  public DMDataVO[] getBodyChangeVOs() {
    DMVO dmvo = (DMVO) getBillCardPanel().getBillValueVO(DMVO.class.getName(),
        DMDataVO.class.getName(), DMDataVO.class.getName());
    DMVO dmchgvo = (DMVO) getBillCardPanel().getBillValueChangeVO(
        DMVO.class.getName(), DMDataVO.class.getName(),
        DMDataVO.class.getName());

    if (dmvo == null) {
      SCMEnv.info("bd null ERROR!");
      return null;
    }
    DMDataVO[] dmdvos = dmvo.getBodyVOs();
    if (dmdvos == null || dmdvos.length == 0) {
      SCMEnv.info("body null ERROR!");
      return null;
    }

    ArrayList v = new ArrayList();
    for (int i = 0; i < dmdvos.length; i++) {
      v.add(dmdvos[i]);
    }
    DMDataVO[] dmdchgvos = dmchgvo.getBodyVOs();
    if (dmdchgvos != null && dmdchgvos.length != 0) {
      for (int i = 0; i < dmdchgvos.length; i++) {
        if (dmdchgvos[i].getStatus() == VOStatus.DELETED) {
          v.add(dmdchgvos[i]);
        }
      }
    }
    DMDataVO[] tmpVOs = new DMDataVO[v.size()];
    tmpVOs = (DMDataVO[]) v.toArray(tmpVOs);
    v = null;
    return tmpVOs;
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-8-1 20:24:56) 修改日期，修改人，修改原因，注释标志：
   */
  public DMDataVO getCardData() {
    if (m_planvos == null || m_planvos.length == 0)
      return null;
    getSecBillCardPanel().setAutoFiltNullValueRow(false);
    DMVO dmvo = (DMVO) getSecBillCardPanel().getBillValueVO(
        DMVO.class.getName(), DMDataVO.class.getName(),
        DMDataVO.class.getName());

    DMDataVO dmdvo = (DMDataVO) dmvo.getParentVO();
    dmdvo.setAttributeValue("pkcorp", getCorpID());
    // 收货地址
    UIRefPane vdestaddress = (UIRefPane) getSecBillCardPanel().getHeadItem(
        "vdestaddress").getComponent();
    dmdvo.setAttributeValue("vdestaddress", vdestaddress.getText());
    // 到货地区
    UIRefPane pkarrivearea = (UIRefPane) getSecBillCardPanel().getHeadItem(
        "pkarrivearea").getComponent();
    dmdvo.setAttributeValue("vdestarea", pkarrivearea.getRefName());
    // dmdvo.setAttributeValue("vbatchcode",
    // ((nc.ui.ic.pub.lot.LotNumbRefPane)getSecBillCardPanel().getHeadItem("vbatchcode").getComponent()).getLotNumbRefVO().getVbatchcode());

    //自由项
    if(getSecBillCardPanel().getHeadItem("vfree0") != null
    		&& (getSecBillCardPanel().getHeadItem("vfree0").getValueObject() == null
    				|| getSecBillCardPanel().getHeadItem("vfree0").getValueObject().toString().trim().length() <= 0))
    {
    	for(int i = 1; i <= DMFreeVO.FREE_ITEM_NUM; i++)
    		dmdvo.setAttributeValue("vfree"+i, null);
    }
    if (getEditFlag() == DMBillStatus.CardNew) {
      dmdvo.setStatus(VOStatus.NEW);
      m_planvos[m_num] = dmdvo; // 用于批处理
      // m_selectRow= m_num;
    }
    else if (getEditFlag() == DMBillStatus.CardEdit) {
      m_planvos[m_num] = dmdvo; // 用于批处理
      dmdvo.setStatus(VOStatus.UPDATED);
    }    
    // dmvo.setParentVO(dmdvo);
    return dmdvo;
  }

  /**
   * 此处插入方法说明。 创建日期：(2002-6-5 10:23:40)
   */
  public DMQueryConditionDlg getQueryConditionDlg() {
    // ivjQueryConditionDlg = null;
    if (ivjQueryConditionDlg == null) {
      setDelivDailyConditionDlg(new QueryConditionDlg(this), "7D");
    }

    return ivjQueryConditionDlg;
  }

  /**
   * 该查询对话框的设置，需要提供给 发运安排 节点查询日计划时使用
   * 
   * @param newDelivDailyConditionDlg
   * @param sBillType
   */
  public void setDelivDailyConditionDlg(
      DMQueryConditionDlg newDelivDailyConditionDlg, String sBillType) {
    ivjQueryConditionDlg = newDelivDailyConditionDlg;
    // ivjQueryConditionDlg.setTempletID(getCorpPrimaryKey(), "40140404",
    // getClientEnvironment().getUser()
    // .getPrimaryKey(), null);
    ivjQueryConditionDlg.setTempletID(getCorpID(), "40140404", getUserID(),
        null);
    ivjQueryConditionDlg
        .setDefaultCloseOperation(QueryConditionDlg.HIDE_ON_CLOSE);

    // 如果是 发运安排 节点查询日计划，需要省略掉几个查询条件
    if (!sBillType.equals("7D")) {
      Vector v = new Vector();
      QueryConditionVO[] datas = ivjQueryConditionDlg.getAllTempletDatas();
      for (int i = 0; i < datas.length; i++) {
        if (datas[i].getFieldCode().equals("bbillrowendout") // 订单是否行出库结束
            || datas[i].getFieldCode().equals("bbillrowenddeliv") // 订单是否行发运结束
            || datas[i].getFieldCode().equals("bhasgenoutbill") // 是否已生成出库单
            || datas[i].getFieldCode().equals("bbillrowclose")) { // 订单是否行关闭
          continue;
        }
        else {
          v.add(datas[i]);
        }
      }
      ivjQueryConditionDlg.initTempletDatas((QueryConditionVO[]) v
          .toArray(new QueryConditionVO[v.size()]));
    }

    if (sBillType.equals("7D")) {
      // 订单是否行关闭
      ivjQueryConditionDlg.setCombox("bbillrowclose", new String[][] {
          {
              CConstant.ALL, ""
          },
          {
              CConstant.NOT,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                  "UPPSCMCommon-000108")
          /* @res "否" */},
          {
              CConstant.YES,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                  "UPPSCMCommon-000244")
          /* @res "是" */}
      });

      // 订单是否行发运结束
      ivjQueryConditionDlg.setCombox("bbillrowenddeliv", new String[][] {
          {
              CConstant.ALL, ""
          },
          {
              CConstant.NOT,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                  "UPPSCMCommon-000108")
          /* @res "否" */},
          {
              CConstant.YES,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                  "UPPSCMCommon-000244")
          /* @res "是" */}
      });

      // 订单是否行出库结束
      ivjQueryConditionDlg.setCombox("bbillrowendout", new String[][] {
          {
              CConstant.ALL, ""
          },
          {
              CConstant.NOT,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                  "UPPSCMCommon-000108")
          /* @res "否" */},
          {
              CConstant.YES,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                  "UPPSCMCommon-000244")
          /* @res "是" */}
      });
      // 是否已生成出库单
      ivjQueryConditionDlg.setCombox("bhasgenoutbill", new String[][] {
          {
              CConstant.ALL, ""
          },
          {
              CConstant.NOT,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                  "UPPSCMCommon-000108")
          /* @res "否" */},
          {
              CConstant.YES,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                  "UPPSCMCommon-000244")
          /* @res "是" */}
      });

      ivjQueryConditionDlg.setCombox("dm_delivdaypl.iplanstatus",
          new String[][] {
              {
                  "", ""
              },
              {
                  "0",
                  nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
                      "UPP40140404-000011")
              /* @res "自由状态" */},
              {
                  "1",
                  nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                      "UC000-0001558")
              /* @res "审批状态" */},
              {
                  "2",
                  nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
                      "UPP40140404-000013")
              /* @res "关闭状态" */}
          });
    }
    else {
      ivjQueryConditionDlg.setCombox("dm_delivdaypl.iplanstatus",
          new String[][] {
            {
                "1",
                nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                    "UC000-0001558")
            /* @res "审批状态" */}
          });
    }

    ivjQueryConditionDlg.setDefaultValue("bd_delivorg.pk_delivorg",
        getDelivOrgPK(), null);
    ivjQueryConditionDlg.setConditionEditable("bd_delivorg.pk_delivorg", false);

    ivjQueryConditionDlg.setCombox("dm_delivdaypl.vbilltype", new String[][] {
        {
            CConstant.ALL, ""
        },
        {
            "30",
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
                "UPP40140404-000014")
        /* @res "销售订单" */},
        {
            "21",
            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                "UPPSCMCommon-000025")
        /* @res "采购订单" */},
        {
            "5C",
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
                "UPP40140404-000015")
        /* @res "总部结算的公司调拨定单" */},
        {
            "5D",
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
                "UPP40140404-000016")
        /* @res "公司间调拨订单" */},
        {
            "5E",
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
                "UPP40140404-000017")
        /* @res "组织间调拨订单" */},
        {
            "5I",
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
                "UPP40140404-000018")
        /* @res "组织内调拨订单" */}
    });
    // 日计划出库是否完成
    ivjQueryConditionDlg.setCombox("boutend", new String[][] {
        {
            CConstant.ALL, ""
        },
        {
            CConstant.NOT,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                "UPPSCMCommon-000108")
        /* @res "否" */},
        {
            CConstant.YES,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                "UPPSCMCommon-000244")
        /* @res "是" */}
    });

    // 是否退货
    ivjQueryConditionDlg.setCombox("dm_delivdaypl.borderreturn",
        new String[][] {
            {
                CConstant.ALL, ""
            },
            {
                CConstant.NOT,
                nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                    "UPPSCMCommon-000108")
            /* @res "否" */},
            {
                CConstant.YES,
                nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                    "UPPSCMCommon-000244")
            /* @res "是" */}
        });

    // 是否已生成发运单
    ivjQueryConditionDlg.setCombox("bhasgendelivbill", new String[][] {
        {
            CConstant.ALL, ""
        },
        {
            CConstant.NOT,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                "UPPSCMCommon-000108")
        /* @res "否" */},
        {
            CConstant.YES,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                "UPPSCMCommon-000244")
        /* @res "是" */}
    });

    // 日期默认时间
    ivjQueryConditionDlg.setInitDate("dm_delivdaypl.plandate",
        getClientEnvironment().getDate().toString());
    ivjQueryConditionDlg.setInitDate("dm_delivdaypl.ordsndate",
        getClientEnvironment().getDate().toString());
    ivjQueryConditionDlg.setInitDate("dm_delivdaypl.snddate",
        getClientEnvironment().getDate().toString());
    ivjQueryConditionDlg.setInitDate("dm_delivdaypl.ordsndate",
        getClientEnvironment().getDate().toString());

    // Add by xhq 2002/12/05
    // 设置查询对话框参照
    // 客户
    UIRefPane customerRef = new UIRefPane();
    customerRef.setRefType(2); // 树表结构
    customerRef.setIsCustomDefined(true);
    customerRef.setRefModel(new nc.ui.dm.pub.ref.CustbaseRefModel());
    customerRef.setWhereString("bd_cubasdoc.pk_corp in ("
        + getStrCorpIDsOfDelivOrg() + ", '"
        + getClientEnvironment().getGroupId() + "')");
    ivjQueryConditionDlg.setValueRef("dm_delivdaypl.pkcustcode", customerRef);
    ivjQueryConditionDlg.setValueRef("dm_delivdaypl.pkcustname", customerRef);

    // 收货单位
    UIRefPane creceiptRef = new UIRefPane();
    creceiptRef.setRefType(2); // 树表结构
    creceiptRef.setIsCustomDefined(true);
    creceiptRef.setRefModel(new nc.ui.dm.pub.ref.CustbaseRefModel());
    creceiptRef.setWhereString("bd_cubasdoc.pk_corp in ("
        + getStrCorpIDsOfDelivOrg() + ", '"
        + getClientEnvironment().getGroupId() + "')");
    ivjQueryConditionDlg.setValueRef("dm_delivdaypl.creceiptcorpid",
        creceiptRef);

    // 存货
    UIRefPane inventoryRef = new UIRefPane();
    inventoryRef.setRefType(2); // 树表结构
    inventoryRef.setIsCustomDefined(true);
    inventoryRef.setRefModel(new nc.ui.dm.pub.ref.InvbaseRefModel());
    inventoryRef.setWhereString("bd_invbasdoc.pk_corp in ("
        + getStrCorpIDsOfDelivOrg() + ", '"
        + getClientEnvironment().getGroupId() + "')");
    ivjQueryConditionDlg.setValueRef("dm_delivdaypl.pkinvcode", inventoryRef);
    ivjQueryConditionDlg.setValueRef("dm_delivdaypl.pkinvname", inventoryRef);
    // ivjQueryConditionDlg.setValueRef("bd_invbasdoc.invname",
    // inventoryRef);
    // Add by xhq 2002/12/05

    // 发货库存组织
    UIRefPane refpaneCalbodySend = new UIRefPane();
    nc.ui.dm.pub.ref.QuerySendCalBodyRefModel querySendCalBodyRefModel = new nc.ui.dm.pub.ref.QuerySendCalBodyRefModel();
    try {
      String sDataPowerSql = DeliverydailyplanBO_Client.getDataPowerSubSql(
          "bd_calbody", "库存组织", "bd_calbody.pk_calbody",
          (String[]) getAgentCorpIDsofDelivOrg().toArray(new String[0]),
          getUserID());
      querySendCalBodyRefModel.setWherePart(sDataPowerSql + " and "
          + nc.vo.dm.pub.sql.RefSql.sCalbodyPropertySql);
    }
    catch (Exception e) {
      e.printStackTrace();
      showErrorMessage(e.getMessage());
    }
    refpaneCalbodySend.setRefModel(querySendCalBodyRefModel);
    ivjQueryConditionDlg.setValueRef("dm_delivdaypl.pksendstoreorg",
        refpaneCalbodySend);

    // 发货仓库
    UIRefPane refpaneWarehouseFrom = new UIRefPane();
    nc.ui.dm.pub.ref.QueryToWareHouseRefModel querySendStoreRefModel = new nc.ui.dm.pub.ref.QueryToWareHouseRefModel();
    try {
      querySendStoreRefModel.setWherePart(DeliverydailyplanBO_Client
          .getDataPowerSubSql("bd_stordoc", "仓库档案", "bd_stordoc.pk_stordoc",
              (String[]) getAgentCorpIDsofDelivOrg().toArray(new String[0]),
              getUserID()));
    }
    catch (Exception e) {
      e.printStackTrace();
      showErrorMessage(e.getMessage());
    }
    refpaneWarehouseFrom.setRefModel(querySendStoreRefModel);
    ivjQueryConditionDlg.setValueRef("dm_delivdaypl.pksendstore",
        refpaneWarehouseFrom);

    // 到货库存组织
    UIRefPane refpaneCalbodyTo = new UIRefPane();
    refpaneCalbodyTo.setRefModel(new QueryToCalBodyRefModel());
    ivjQueryConditionDlg.setValueRef("dm_delivdaypl.pkdeststoreorg",
        refpaneCalbodyTo);

    // 到货仓库
    UIRefPane refpaneWarehouseTo = new UIRefPane();
    refpaneWarehouseTo.setRefModel(new QueryToWareHouseRefModel());
    ivjQueryConditionDlg.setValueRef("dm_delivdaypl.pkdeststore",
        refpaneWarehouseTo);

    // 制单人
    UIRefPane refpanePlanperson = new UIRefPane();
    refpanePlanperson.setRefNodeName("操作员");
    refpanePlanperson.setWhereString("sm_user.pk_corp in ("
        + getStrCorpIDsOfDelivOrg() + ", '"
        + getClientEnvironment().getGroupId() + "')");
    ivjQueryConditionDlg.setValueRef("dm_delivdaypl.pkplanperson",
        refpanePlanperson);

    // 日计划单据号
    UIRefPane refpaneDayplcode = new UIRefPane();
    nc.ui.dm.pub.ref.DelivdayplcodeRefModel refModelDayplcode = new nc.ui.dm.pub.ref.DelivdayplcodeRefModel();
    refModelDayplcode.setDelivOrgPK(getDelivOrgPK());
    refpaneDayplcode.setRefModel(refModelDayplcode);
    refpaneDayplcode.setAutoCheck(false);
    ivjQueryConditionDlg.setValueRef("dm_delivdaypl.vdelivdayplcode",
        refpaneDayplcode);

    ivjQueryConditionDlg.setRefMultiInit("dm_delivdaypl.vdelivdayplcode", "",
        "", new String[][] {
          {
              "dm_delivdaypl.plandate", "dm_delivdaypl.plandate"
          }
        }, false);

    /*
     * //订单号 UIRefPane refpaneSaleordercode= new UIRefPane();
     * nc.ui.dm.pub.ref.SaleOrderCodeRefModel refModelSaleordercode= new
     * nc.ui.dm.pub.ref.SaleOrderCodeRefModel();
     * refModelSaleordercode.setDelivOrgPK(getDelivOrgPK());
     * refpaneSaleordercode.setRefModel(refModelSaleordercode);
     * refpaneSaleordercode.setAutoCheck(false);
     * ivjQueryConditionDlg.setValueRef("dm_delivdaypl.vsrcbillnum",
     * refpaneSaleordercode); ivjQueryConditionDlg .setRefMultiInit(
     * "dm_delivdaypl.vsrcbillnum", "", "", new String[][] { {
     * "dm_delivdaypl.ordsndate", "dbilldate" } }, false);
     */
    nc.ui.scm.pub.def.DefSetTool.updateQueryConditionClientUserDef(
        (nc.ui.pub.query.QueryConditionClient) ivjQueryConditionDlg,
        getCorpPrimaryKey(),vUserdefCode ,"dm_delivdaypl.vuserdef", null, 1,
        "dm_delivdaypl.vuserdef_b_", "_b_", 1);

  }

  /**
   * 此处插入方法说明。 创建日期：(2002-6-5 10:23:40)
   */
  public DMQueryConditionDlg getQueryOrderConditionDlg() {
    if (ivjQueryOrderConditionDlg == null) {
      ivjQueryOrderConditionDlg = new QueryOrderConditionDlg(this);
      ivjQueryOrderConditionDlg
          .setDefaultCloseOperation(QueryOrderConditionDlg.HIDE_ON_CLOSE);

      ivjQueryOrderConditionDlg.setDefaultValue("pkdelivorg", getDelivOrgPK(),
          null);
      ivjQueryOrderConditionDlg.setConditionEditable("pkdelivorg", false);
      ivjQueryOrderConditionDlg.setCombox("vbilltype", new String[][] {
          {
              "", ""
          },
          {
              "30",
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
                  "UPP40140404-000014")
          /* @res "销售订单" */},
          {
              "21",
              nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                  "UPPSCMCommon-000025")
          /* @res "采购订单" */},
          {
              "tobill",
              nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub",
                  "UPPscmpub-000780")
          /* @res "调拨订单" */},
      });

      // 是否退货
      ivjQueryOrderConditionDlg.setCombox("borderreturn", new String[][] {
          {
              CConstant.ALL, ""
          },
          {
              CConstant.NOT,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                  "UPPSCMCommon-000108")
          /* @res "否" */},
          {
              CConstant.YES,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                  "UPPSCMCommon-000244")
          /* @res "是" */}
      });

      // 日期默认时间
      ivjQueryOrderConditionDlg.setInitDate("datefrom", getClientEnvironment()
          .getDate().toString());
      ivjQueryOrderConditionDlg.setInitDate("dateto", getClientEnvironment()
          .getDate().toString());

      // Add by xhq 2002/12/05
      // 设置查询对话框参照
      // 客户
      UIRefPane customerRef = new UIRefPane();
      customerRef.setRefType(2); // 树表结构
      customerRef.setIsCustomDefined(true);
      customerRef.setRefModel(new nc.ui.dm.pub.ref.CustbaseRefModel());
      customerRef.setWhereString("bd_cubasdoc.pk_corp in ("
          + getStrCorpIDsOfDelivOrg() + ", '"
          + getClientEnvironment().getGroupId() + "')");
      ivjQueryOrderConditionDlg.setValueRef("pkcust", customerRef);

      // 收货单位
      UIRefPane creceiptRef = new UIRefPane();
      creceiptRef.setRefType(2); // 树表结构
      creceiptRef.setIsCustomDefined(true);
      creceiptRef.setRefModel(new nc.ui.dm.pub.ref.CustbaseRefModel());
      creceiptRef.setWhereString("bd_cubasdoc.pk_corp in ("
          + getStrCorpIDsOfDelivOrg() + ", '"
          + getClientEnvironment().getGroupId() + "')");
      ivjQueryOrderConditionDlg.setValueRef("pkreceiptcorpid", creceiptRef);

      // 存货
      UIRefPane inventoryRef = new UIRefPane();
      inventoryRef.setRefType(2); // 树表结构
      inventoryRef.setIsCustomDefined(true);
      inventoryRef.setRefModel(new nc.ui.dm.pub.ref.InvbaseRefModel());
      inventoryRef.setWhereString("bd_invbasdoc.pk_corp in ("
          + getStrCorpIDsOfDelivOrg() + ", '"
          + getClientEnvironment().getGroupId() + "')");
      ivjQueryOrderConditionDlg.setValueRef("pkinv", inventoryRef);
      // Add by xhq 2002/12/05

      // 发货库存组织
      UIRefPane refpaneCalbodySend = new UIRefPane();
      nc.ui.dm.pub.ref.QuerySendCalBodyRefModel querySendCalBodyRefModel = new nc.ui.dm.pub.ref.QuerySendCalBodyRefModel();
      try {
        String sDataPowerSql = DeliverydailyplanBO_Client.getDataPowerSubSql(
            "bd_calbody", "库存组织", "bd_calbody.pk_calbody",
            (String[]) getAgentCorpIDsofDelivOrg().toArray(new String[0]),
            getUserID());
        querySendCalBodyRefModel.setWherePart(sDataPowerSql + " and "
            + nc.vo.dm.pub.sql.RefSql.sCalbodyPropertySql);
        // querySendCalBodyRefModel.setWherePart(DeliverydailyplanBO_Client.getDataPowerSubSql("bd_calbody",
        // "库存组织",
        // "bd_calbody.pk_calbody", (String[])
        // getAgentCorpIDsofDelivOrg().toArray(new String[0]), getUserID()));
      }
      catch (Exception e) {
        e.printStackTrace();
        showErrorMessage(e.getMessage());
      }
      refpaneCalbodySend.setRefModel(querySendCalBodyRefModel);
      ivjQueryOrderConditionDlg.setValueRef("pksendstockorg",
          refpaneCalbodySend);
      // UIRefPane refpaneCalbodySend = new UIRefPane();
      // refpaneCalbodySend.setRefModel(new QueryToCalBodyRefModel());
      // ivjQueryOrderConditionDlg.setValueRef("pksendstockorg",
      // refpaneCalbodySend);

      // 到货库存组织
      UIRefPane refpaneCalbodyTo = new UIRefPane();
      refpaneCalbodyTo.setRefModel(new QueryToCalBodyRefModel());
      ivjQueryOrderConditionDlg.setValueRef("pkdeststoreorg", refpaneCalbodyTo);

      // 制单人
      UIRefPane refpaneUserid = new UIRefPane();
      refpaneUserid.setRefNodeName("操作员");
      refpaneUserid.setWhereString("sm_user.pk_corp in ("
          + getStrCorpIDsOfDelivOrg() + ", '"
          + getClientEnvironment().getGroupId() + "')");
      ivjQueryOrderConditionDlg.setValueRef("userid", refpaneUserid);

      // 业务类型
      UIRefPane refpanebiztype = new UIRefPane();
      refpanebiztype.setRefNodeName("业务类型");
      refpanebiztype.setWhereString("pk_corp in (" + getStrCorpIDsOfDelivOrg()
          + ", '" + getClientEnvironment().getGroupId() + "')");
      ivjQueryOrderConditionDlg.setValueRef("cbiztype", refpanebiztype);

      // 订单号
      // UIRefPane refpaneSaleordercode = new UIRefPane();
      // nc.ui.dm.pub.ref.SaleOrderCodeRefModel refModelSaleordercode =
      // new nc.ui.dm.pub.ref.SaleOrderCodeRefModel();
      // refModelSaleordercode.setDelivOrgPK(getDelivOrgPK());
      // refpaneSaleordercode.setRefModel(refModelSaleordercode);
      // refpaneSaleordercode.setAutoCheck(false);
      // ivjQueryOrderConditionDlg.setValueRef("vbillcode",
      // refpaneSaleordercode);
      // ivjQueryOrderConditionDlg
      // .setRefMultiInit("vbillcode", "", " AND (fstatus = 2) AND
      // (breceiptendflag = 'N') ", new String[][] { { "datefrom",
      // "dbilldate" }, {
      // "dateto", "dbilldate" }
      // }, false);

      // 调拨订单号
      // UIRefPane refpaneAllocationcode = new UIRefPane();
      // nc.ui.dm.pub.ref.AllocationCodeRefModel refModelAllocationcode =
      // new nc.ui.dm.pub.ref.AllocationCodeRefModel();
      // refModelAllocationcode.setDelivOrgPK(getDelivOrgPK());
      // refpaneAllocationcode.setRefModel(refModelAllocationcode);
      // refpaneAllocationcode.setAutoCheck(false);
      // ivjQueryOrderConditionDlg.setValueRef("vallocationbillcode",
      // refpaneAllocationcode);

      // ivjQueryOrderConditionDlg
      // .setRefMultiInit("vallocationbillcode", "", " and (fbillflag = 3)
      // and (dr = 0) ", new String[][] { { "datefrom", "dbilldate" }, {
      // "dateto", "dbilldate" }
      // }, false);

    }
    return ivjQueryOrderConditionDlg;
  }

  /**
   * 子类实现该方法，返回业务界面的标题。
   * 
   * @version (00-6-6 13:33:25)
   * @return java.lang.String
   */
  public String getTitle() {
    return strTitle1;
  }

  /**
   * 此处插入方法说明。 创建日期：(2002-6-17 18:53:34)
   */
  public void initBodyComboBox() {
    if (getBillListPanel().getBodyItem("iplanstatus") != null
        && getBillListPanel().getBodyItem("iplanstatus").getComponent() != null) {
      // 计划状态
      UIComboBox iplanstatus = (UIComboBox) getBillListPanel().getBodyItem(
          "iplanstatus").getComponent();
      getBillListPanel().getBodyItem("iplanstatus").setWithIndex(true);
      iplanstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40140404", "UPP40140404-000011")/* @res "自由状态" */);
      iplanstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UC000-0001558")/* @res "审批状态" */);
      iplanstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40140404", "UPP40140404-000013")/* @res "关闭状态" */);
      iplanstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40140404", "UPP40140404-000019")/* @res "出库状态" */);
      iplanstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40140404", "UPP40140404-000020")/* @res "签收状态" */);
      iplanstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40140404", "UPP40140404-000021")/* @res "发运下达" */);
    }
  }

  /**
   * 此处插入方法说明。 创建日期：(2002-5-10 11:16:28)
   */
  protected void initFixSubMenuButton() {
    super.initFixSubMenuButton();
    btnCKKCXX = new ButtonObject("参考库存信息");
    // boQueryOrder = new ButtonObject("订单查询", "订单查询", 1);
    //add by zwx 2014-12-23 
    boOnhandnum = new ButtonObject("现存查询及打印", "现存查询及打印", 1, "现存查询及打印");
    boOrder = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "40140404", "UPT40140404-000028")/* @res "订单查询" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
            "UPT40140404-000028")/* @res "订单查询" */, 1, "订单查询"); /*-=notranslate=-*/
    boCancel = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "common", "UC001-0000008")/* @res "取消" */, nc.ui.ml.NCLangRes
        .getInstance().getStrByID("common", "UC001-0000008")/* @res "取消" */, 1,
        "取消"); /*-=notranslate=-*/
    boGenDelivDailyPlan = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("40140404", "UPT40140404-000040")/* @res "生成日计划" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
            "UPT40140404-000040")/* @res "生成日计划" */, 1, "生成日计划"); /*-=notranslate=-*/
    boSelectAll = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "common", "UC001-0000041")/* @res "全选" */, nc.ui.ml.NCLangRes
        .getInstance().getStrByID("common", "UC001-0000041")/* @res "全选" */, 1,
        "全选"); /*-=notranslate=-*/
    boUnSelectAll = new ButtonObject(
        nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000042")/*
                                                                               * @res
                                                                               * "全消"
                                                                               */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000042")/*
                                                                               * @res
                                                                               * "全消"
                                                                               */,
        1, "全消"); /*-=notranslate=-*/
    boAction = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "common", "UC001-0000026")/* @res "执行" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
            "UPPSCMCommon-000323")/* @res "执行操作" */, 0, "执行"); /*-=notranslate=-*/
    boOutBill = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "40140404", "UPT40140404-000043")/* @res "出库" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
            "UPT40140404-000043")/* @res "出库" */, 0, "出库"); /*-=notranslate=-*/
    boATP = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "common", "UC000-0001084")/* @res "可用量" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
            "UPP40140404-000022")/* @res "查询可用量" */, 0, "可用量"); /*-=notranslate=-*/
    boReOpen = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "40140404", "UPT40140404-000035")/* @res "打开" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
            "UPT40140404-000035")/* @res "打开" */, 0, "打开"); /*-=notranslate=-*/
    boBatch = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "40140404", "UPT40140404-000044")/* @res "批修改" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
            "UPT40140404-000044")/* @res "批修改" */, 0, "批修改"); /*-=notranslate=-*/
    boReturn = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "common", "UC001-0000038")/* @res "返回" */, nc.ui.ml.NCLangRes
        .getInstance().getStrByID("common", "UC001-0000038")/* @res "返回" */, 0,
        "返回"); /*-=notranslate=-*/
    boAction.addChildButton(boAudit);
    boAction.addChildButton(boCancelAudit);
    boAction.addChildButton(boEnd);
    boAction.addChildButton(boReOpen);
    boBrowseList.removeAllChildren();
    boBrowseList.addChildButton(boQuery);
    boBrowseList.addChildButton(boFind);
    boAssistantList.removeAllChildren();
    boAssistantList.addChildButton(boBatch);
    boAssistantList.addChildButton(boOrderQuery);
    boAssistantList.addChildButton(boDocument);
    boAssistantList.addChildButton(boPrintPreview);
    boAssistantList.addChildButton(boPrint);
    boBrowse.removeAllChildren();
    boBrowse.addChildButton(boFirst);
    boBrowse.addChildButton(boPre);
    boBrowse.addChildButton(boNext);
    boBrowse.addChildButton(boLast);
    boMaintain.removeAllChildren();
    boMaintain.addChildButton(boEdit);
    boMaintain.addChildButton(boSave);
    boMaintain.addChildButton(boCancel);
    boMaintain.addChildButton(boDel);
    boAssistant.removeAllChildren();
    boAssistant.addChildButton(boOrderQuery);
    boAssistant.addChildButton(boATP);
    boAssistant.addChildButton(boDocument);

    // boAdd.setName("订单查询");
    aryButtonGroup = new ButtonObject[] {
        boOrder, boMaintain, boBrowse, boSwith, boAssistant,btnCKKCXX
    };
    if (getDelivSequence() == 0) {
      // 先发运后出库
      aryListButtonGroup = new ButtonObject[] {
          boOrder,
          // boSort,
          boSelectAll, boUnSelectAll, boBrowseList, boAction, boSwith,btnCKKCXX,
          boAssistantList, boOnhandnum // add   by zwx    , boOnhandnum 2014-12-23 
      };
    }
    else if (getDelivSequence() == 1) {
      // 发运出库并行
      aryListButtonGroup = new ButtonObject[] {
          boOrder,
          // boSort,
          boSelectAll, boUnSelectAll, boBrowseList, boAction, boOutBill,btnCKKCXX,
          boSwith, boAssistantList, boOnhandnum// add   by zwx    , boOnhandnum 2014-12-23 
      };
    }
    else if (getDelivSequence() == 2) {
      // 先出库后发运
      aryListButtonGroup = new ButtonObject[] {
          boOrder,
          // boSort,
          boSelectAll, boUnSelectAll, boBrowseList, boAction, boOutBill,btnCKKCXX,
          boSwith, boAssistantList, boOnhandnum// add   by zwx    , boOnhandnum 2014-12-23 
      };
    }
    arySourceButtonGroup = new ButtonObject[] {
        boOrder, boSort, boGenDelivDailyPlan, boSwith, boSelectAll,btnCKKCXX,
        boUnSelectAll
    };

    aryButtonBatchGroup = new ButtonObject[] {
        boEdit, boSave, boCancel, boReturn,btnCKKCXX
    };
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-5-9 16:00:39) 修改日期，修改人，修改原因，注释标志：
   */
  public void initialize() {
  }

  /**
   * 此处插入方法说明。 创建日期：(2002-6-17 18:53:34)
   */
  public void initBatchBodyItem() {
    // 计划状态
    UIComboBox iplanstatus = (UIComboBox) getBatchBillCardPanel().getBodyItem(
        "iplanstatus").getComponent();
    getBatchBillCardPanel().getBodyItem("iplanstatus").setWithIndex(true);
    iplanstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
        "UPP40140404-000011")/* @res "自由状态" */);
    iplanstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
        "UC000-0001558")/* @res "审批状态" */);
    iplanstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
        "UPP40140404-000013")/* @res "关闭状态" */);
    iplanstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
        "UPP40140404-000019")/* @res "出库状态" */);
    iplanstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
        "UPP40140404-000020")/* @res "签收状态" */);
    iplanstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
        "UPP40140404-000021")/* @res "发运下达" */);
    // 到货地址:不用自动检查，返回名称。
    if (getBatchBillCardPanel().getHeadItem("vdestaddress") != null
        && getBatchBillCardPanel().getHeadItem("vdestaddress").getComponent() != null) {
      UIRefPane vdestaddressHead = (UIRefPane) getBatchBillCardPanel()
          .getHeadItem("vdestaddress").getComponent();
      vdestaddressHead.setAutoCheck(false);
      vdestaddressHead.setReturnCode(true);
      getBatchBillCardPanel().getHeadItem("vdestaddress").setDataType(
          nc.ui.pub.bill.BillItem.USERDEF);
      UIRefPane vdestaddressBody = (UIRefPane) getBatchBillCardPanel()
          .getBodyItem("vdestaddress").getComponent();
      vdestaddressBody.setAutoCheck(false);
      vdestaddressBody.setReturnCode(true);
      getBatchBillCardPanel().getBodyItem("vdestaddress").setDataType(
          nc.ui.pub.bill.BillItem.USERDEF);
    }
    // if (getBatchBillCardPanel().getBodyItem("vbatchcode") != null
    // && getBatchBillCardPanel().getBodyItem("vbatchcode").getComponent() !=
    // null) {
    // UIRefPane vdestaddress = (UIRefPane)
    // getBatchBillCardPanel().getBodyItem("vdestaddress").getComponent();
    // vdestaddress.setAutoCheck(false);
    // vdestaddress.setReturnCode(true);
    // getBatchBillCardPanel().getBodyItem("vbatchcode").setDataType(nc.ui.pub.bill.BillItem.USERDEF);
    // }
    ((UIRefPane) (getBatchBillCardPanel().getBodyItem("vsendtypename")
        .getComponent())).setReturnCode(false);
    ((UIRefPane) (getBatchBillCardPanel().getBodyItem("creceiptcorpname")
        .getComponent())).setReturnCode(false);
    ((UIRefPane) (getBatchBillCardPanel().getBodyItem("vsendstoreorgname")
        .getComponent())).setReturnCode(false);
    ((UIRefPane) (getBatchBillCardPanel().getBodyItem("vsendstorename")
        .getComponent())).setReturnCode(false);
    ((UIRefPane) (getBatchBillCardPanel().getBodyItem("vsendarea")
        .getComponent())).setReturnCode(false);
    ((UIRefPane) (getBatchBillCardPanel().getBodyItem("vdeststorename")
        .getComponent())).setReturnCode(false);
    // ((UIRefPane)(getBatchBillCardPanel().getBodyItem("vdestaddress").getComponent())).setReturnCode(false);
    ((UIRefPane) (getBatchBillCardPanel().getBodyItem("vdestarea")
        .getComponent())).setReturnCode(false);
    ((UIRefPane) (getBatchBillCardPanel().getBodyItem("vitemname")
        .getComponent())).setReturnCode(false);
    ((UIRefPane) (getBatchBillCardPanel().getBodyItem("vitemperiodname")
        .getComponent())).setReturnCode(false);
    ((UIRefPane) (getBatchBillCardPanel().getBodyItem("vsendaddress")
        .getComponent())).setReturnCode(false);
    ((UIRefPane) (getBatchBillCardPanel().getBodyItem("varriveaddress")
        .getComponent())).setReturnCode(false);
    ((UIRefPane) (getBatchBillCardPanel().getBodyItem("vassistmeaname")
        .getComponent())).setReturnCode(false);
  }

  /**
   * 此处插入方法说明。 创建日期：(2002-6-17 18:53:34)
   */
  public void initSecHeadComboBox() {
    if (getBillListPanel().getBodyItem("iplanstatus") != null
        && getBillListPanel().getBodyItem("iplanstatus").getComponent() != null) {
      // 计划状态
      UIComboBox iplanstatus = (UIComboBox) getSecBillCardPanel().getHeadItem(
          "iplanstatus").getComponent();
      getSecBillCardPanel().getHeadItem("iplanstatus").setWithIndex(true);
      iplanstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40140404", "UPP40140404-000011")/* @res "自由状态" */);
      iplanstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UC000-0001558")/* @res "审批状态" */);
      iplanstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40140404", "UPP40140404-000013")/* @res "关闭状态" */);
      iplanstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40140404", "UPP40140404-000019")/* @res "出库状态" */);
      iplanstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40140404", "UPP40140404-000020")/* @res "签收状态" */);
      iplanstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40140404", "UPP40140404-000021")/* @res "发运下达" */);
    }
  }

  /**
   * 初试化单据状态 创建日期：(2001-11-15 9:02:22)
   */
  public void initSecState() {

    getSecBillCardPanel().setEnabled(false);

    switchButtonStatus(DMBillStatus.CardView);
  }

  /**
   * 加载卡片数据。 创建日期：(2001-11-15 9:02:22) 2004-09-21 wyf 修改参照的公司
   */
  public void loadCardData() {
    DMVO dmvo = new DMVO();
    // 置重量单位、体积单位
    m_planvos[m_num].setAttributeValue("weightunit", getWeightUnit());
    m_planvos[m_num].setAttributeValue("volumnunit", getCapacityUnit());
    //
    dmvo.setParentVO(m_planvos[m_num]);

    /** ----根据销售公司---重置参照条件------------------------- */
    // 销售公司
    String sPkSaleCorp = (String) m_planvos[m_num]
        .getAttributeValue("pksalecorp");
    // 到货公司
    String pkDestCorp = (String) m_planvos[m_num]
        .getAttributeValue("pkarrivecorp");
    // 发货库存组织
    String pksendstoreorg = (String) m_planvos[m_num]
        .getAttributeValue("pksendstoreorg");
    // 到货库存组织
    String pkdeststoreorg = (String) m_planvos[m_num]
        .getAttributeValue("pkdeststoreorg");
    // 来源单据类型
    String sSourceBillType = (String) m_planvos[m_num]
        .getAttributeValue("vbilltype");

    // 确定是否该日计划是否来源于采购退货
    UFBoolean ufb = (UFBoolean) m_planvos[m_num]
        .getAttributeValue("borderreturn");
    boolean borderreturn = false;
    if (ufb != null)
      borderreturn = ufb.booleanValue();

    // 发运方式
    if (getSecBillCardPanel().getHeadItem("pksendtype") != null
        && getSecBillCardPanel().getHeadItem("pksendtype").getComponent() != null) {
      String pkCurCorp = sPkSaleCorp == null ? pkDestCorp : sPkSaleCorp;
      UIRefPane ref = (UIRefPane) getSecBillCardPanel().getHeadItem(
          "pksendtype").getComponent();
      // ref.setWhereString(" issendarranged = 'Y' ");
      ref.setWhereString(" (pk_corp = '" + pkCurCorp
          + "' or pk_corp='0001' or pk_corp is null)");
      // " issendarranged = 'Y' and (transporttype=2 or transporttype=3 ) and
      // wyf
      ref.setPk_corp(pkCurCorp);
      ref.getRefModel().setPk_corp(pkCurCorp);
      // wyf
    }
    // 发货仓库
    if (getSecBillCardPanel().getHeadItem("pksendstore") != null
        && getSecBillCardPanel().getHeadItem("pksendstore").getComponent() != null) {
      UIRefPane sendstoreRef = (UIRefPane) getSecBillCardPanel().getHeadItem(
          "pksendstore").getComponent();
      if (getSecBillCardPanel().getHeadItem("pksendstoreorg") != null
          && getSecBillCardPanel().getHeadItem("pksendstoreorg").getComponent() != null) {
        UIRefPane paneSendstoreOrg = (UIRefPane) getSecBillCardPanel()
            .getHeadItem("pksendstore").getComponent();
        // 根据发货库存组织过滤发货仓库：非废品、未封存
        sendstoreRef.setWhereString(" pk_calbody ='" + pksendstoreorg
            + "' and gubflag = 'N' and sealflag ='N' and pk_corp = '"
            + sPkSaleCorp + "'");
        // wyf
        paneSendstoreOrg.setPk_corp(sPkSaleCorp);
        paneSendstoreOrg.getRefModel().setPk_corp(sPkSaleCorp);
        // wyf
      }
    }
    //
    // 到货仓库
    if (getSecBillCardPanel().getHeadItem("pkdeststore") != null
        && getSecBillCardPanel().getHeadItem("pkdeststore").getComponent() != null) {
      UIRefPane sendstoreRef = (UIRefPane) getSecBillCardPanel().getHeadItem(
          "pkdeststore").getComponent();
      if (getSecBillCardPanel().getHeadItem("pksendstoreorg") != null
          && getSecBillCardPanel().getHeadItem("pksendstoreorg").getComponent() != null) {
        UIRefPane sendstoreOrgRef = (UIRefPane) getSecBillCardPanel()
            .getHeadItem("pkdeststore").getComponent();
        // 根据到货库存组织过滤发货仓库：非废品、未封存
        sendstoreRef.setWhereString(" pk_calbody ='" + pkdeststoreorg
            + "' and gubflag = 'N' and sealflag ='N' ");
      }
      else {
        sendstoreRef.setWhereString("1<0");
      }
    }

    // 收货单位
    if (getSecBillCardPanel().getHeadItem("creceiptcorpid") != null
        && getSecBillCardPanel().getHeadItem("creceiptcorpid").getComponent() != null) {
      UIRefPane ref = (UIRefPane) getSecBillCardPanel().getHeadItem(
          "creceiptcorpid").getComponent();
      if (sSourceBillType.equals(nc.vo.so.pub.SOBillType.SaleOrder)) {
        // wyf
        ref.setPk_corp(sPkSaleCorp);
        ref.getRefModel().setPk_corp(sPkSaleCorp);
        // wyf
        ref
            .setWhereString("(bd_cumandoc.pk_corp = '"
                + sPkSaleCorp
                + "') AND (bd_cumandoc.custflag = '0' OR bd_cumandoc.custflag = '2') ");
      }
      else {
        ref
            .setWhereString(" (bd_cumandoc.custflag = '0' OR bd_cumandoc.custflag = '2') ");
      }
    }

    // 改变“发货单位”参照
    UIRefPane ufRefsend = null;
    // String sPreRefNodeName = ufRef.getRefNodeName();
    if (!borderreturn) {// && sPreRefNodeName.equals("客商档案")) {
      // getSecBillCardPanel().getHeadItem("csendcorpid").setRefType("供应商档案");
      ufRefsend = (UIRefPane) getSecBillCardPanel().getHeadItem("csendcorpid")
          .getComponent();
      ufRefsend.setRefNodeName("供应商档案");
    }
    else if (borderreturn) {// && sPreRefNodeName.equals("供应商档案")) {
      // getSecBillCardPanel().getHeadItem("csendcorpid").setRefType("客商档案");
      ufRefsend = (UIRefPane) getSecBillCardPanel().getHeadItem("csendcorpid")
          .getComponent();
      ufRefsend.setRefNodeName("客商档案");
    }
    ufRefsend.setPk_corp(sPkSaleCorp);
    ufRefsend.getRefModel().setPk_corp(sPkSaleCorp);
    // ufRefsend.setPK((String)
    // m_planvos[m_num].getAttributeValue("csendcorpid"));

    // 改变“收货单位”参照
    UIRefPane ufRefreceive = null;
    // String sPreRefNodeName = ufRef.getRefNodeName();
    if (borderreturn) {// && sPreRefNodeName.equals("客商档案")) {
      // getSecBillCardPanel().getHeadItem("creceiptcorpid").setRefType("供应商档案");
      ufRefreceive = (UIRefPane) getSecBillCardPanel().getHeadItem(
          "creceiptcorpid").getComponent();
      ufRefreceive.setRefNodeName("供应商档案");
    }
    else if (!borderreturn) {// && sPreRefNodeName.equals("供应商档案")) {
      // getSecBillCardPanel().getHeadItem("creceiptcorpid").setRefType("客商档案");
      ufRefreceive = (UIRefPane) getSecBillCardPanel().getHeadItem(
          "creceiptcorpid").getComponent();
      ufRefreceive.setRefNodeName("客商档案");
    }
    ufRefreceive.setPk_corp(sPkSaleCorp);
    ufRefreceive.getRefModel().setPk_corp(sPkSaleCorp);
    // ufRef.setPK((String)
    // m_planvos[m_num].getAttributeValue("creceiptcorpid"));

    // 发货库存组织
    if (getSecBillCardPanel().getHeadItem("pksendstoreorg") != null
        && getSecBillCardPanel().getHeadItem("pksendstoreorg").getComponent() != null) {
      UIRefPane ref = (UIRefPane) getSecBillCardPanel().getHeadItem(
          "pksendstoreorg").getComponent();
      // wyf
      // ref.setWhereString("pk_corp = '" + sPkSaleCorp + "'");
      ref.setPk_corp(sPkSaleCorp);
      ref.getRefModel().setUseDataPower(true);
      ref.getRefModel().setWherePart(
          nc.vo.dm.pub.sql.RefSql.sCalbodyPropertySql
              + " and bd_calbody.pk_corp = '" + sPkSaleCorp + "'", true);
      ref.getRefModel().setPk_corp(sPkSaleCorp);
      // wyf
    }
    // 到货地区
    if (getSecBillCardPanel().getHeadItem("pkarrivearea") != null
        && getSecBillCardPanel().getHeadItem("pkarrivearea").getComponent() != null) {
      UIRefPane ref = (UIRefPane) getSecBillCardPanel().getHeadItem(
          "pkarrivearea").getComponent();
      ref.setWhereString("pk_corp = '" + sPkSaleCorp
          + "' or pk_corp='0001' or pk_corp is null");
      // wyf
      ref.setPk_corp(sPkSaleCorp);
      // wyf
    }

    // 发货地点
//    if (getSecBillCardPanel().getHeadItem("pksendaddress") != null
//        && getSecBillCardPanel().getHeadItem("pksendaddress").getComponent() != null) {
////      UIRefPane ref = (UIRefPane) getSecBillCardPanel().getHeadItem( "pksendaddress").getComponent();
////      ref.getRefModel().setWherePart(null);
////      // wyf
////       ref.setPk_corp(sPkSaleCorp);
////      // //wyf
////       ref.setWhereString("pk_corp = '" + sPkSaleCorp + "' or pk_corp='0001' or pk_corp is null");
//    }

    // 到货地点
//    if (getSecBillCardPanel().getHeadItem("pkarriveaddress") != null
//        && getSecBillCardPanel().getHeadItem("pkarriveaddress").getComponent() != null) {
////      UIRefPane ref = (UIRefPane) getSecBillCardPanel().getHeadItem("pkarriveaddress").getComponent();
////
////      ref.getRefModel().setWherePart(null);
////      // wyf
////       ref.setPk_corp(pkDestCorp);
////      // ref.getRefModel().setPk_corp(pkDestCorp);
////       ref.setWhereString( "pk_corp = '" + pkDestCorp + "' or pk_corp='0001' or pk_corp is null");
////      // wyf
//    }

    // 辅计量
    if (getSecBillCardPanel().getHeadItem("pkinv") != null
        && getSecBillCardPanel().getHeadItem("pkinv").getComponent() != null) {
      getSecBillCardPanel().getHeadItem("pkassistmeasure").getValue();

      filterHeadMeas((String) m_planvos[m_num].getAttributeValue("pkinv"),
          "pkassistmeasure",
          (sSourceBillType.equals("21") && !borderreturn) ? pkDestCorp
              : sPkSaleCorp // 自提的采购订单，取到货公司的值与存货关联
      );
    }

    // 项目
    if (getSecBillCardPanel().getHeadItem("pkitem") != null
        && getSecBillCardPanel().getHeadItem("pkitem").getComponent() != null) {
      UIRefPane ref = (UIRefPane) getSecBillCardPanel().getHeadItem("pkitem")
          .getComponent();
      ref.setPk_corp(sPkSaleCorp);
      // ref.getRefModel().setPk_corp(sPkSaleCorp);
      // ref.setWhereString(" bd_jobmngfil.pk_corp='" + sPkSaleCorp
      // + "' and bd_jobbasfil.sealflag='N' and bd_jobbasfil.finishedflag='N'
      // ");
      // "v_jc_job.pk_corp= '" + sPkSaleCorp + "' or
      // v_jc_job.pk_corp='0001'");
      // ref.setWhereString(
      // "pk_corp = '" + sPkSaleCorp + "' or pk_corp='0001' or pk_corp is
      // null");
    }

    DMDataVO dmdFreevo = new DMDataVO();
    String[] names = dmvo.getHeaderVO().getAttributeNames();

    for (int i = 0; i < names.length; i++) {
      if (names[i].startsWith("vfree")) {
        dmdFreevo.setAttributeValue(names[i], dmvo.getHeaderVO()
            .getAttributeValue(names[i]));
        // dmvo.getHeaderVO().removeAttributeName(names[i]);
      }
    }

    dmdFreevo.setAttributeValue("keyoffreevo", dmvo.getHeaderVO()
        .getAttributeValue("keyoffreevo"));
    dmvo.getHeaderVO().removeAttributeName("keyoffreevo");

    // ((UIRefPane)getSecBillCardPanel().getHeadItem("vuserdef0").getComponent()).setAutoCheck(true);
    // Object o = getSecBillCardPanel().getHeadItem("vuserdef0").getValue();
    // SCMEnv.info("beforesavecard:"+o);
    // SCMEnv.info("beforesavevo:"+dmvo.getHeaderVO().getAttributeValue("vuserdef0"));

    getSecBillCardPanel().setBillValueVO(dmvo, true);

    // Object o1 = getSecBillCardPanel().getHeadItem("vuserdef0").getValue();
    // SCMEnv.info("aftersavecard:"+o1);
    // SCMEnv.info("aftersavevo:"+dmvo.getHeaderVO().getAttributeValue("vuserdef0"));

    String[] sFreeNames = dmdFreevo.getAttributeNames();
    if (sFreeNames != null) {
      for (int i = 0; i < dmdFreevo.getAttributeNames().length; i++) {
        if (getSecBillCardPanel().getHeadItem(sFreeNames[i]) != null) {
          getSecBillCardPanel().getHeadItem(sFreeNames[i]).setValue(
              dmdFreevo.getAttributeValue(sFreeNames[i]));
        }
      }
      dmvo.getHeaderVO().setAttributeValue("keyoffreevo",
          dmdFreevo.getAttributeValue("keyoffreevo"));
    }

    // 收货地址
    if (null != getSecBillCardPanel().getHeadItem("vdestaddress")
        && getSecBillCardPanel().getHeadItem("vdestaddress").getComponent() != null
        && null != getSecBillCardPanel().getHeadItem("creceiptcorpid")) {
      UIRefPane vdestaddress = (UIRefPane) getSecBillCardPanel().getHeadItem(
          "vdestaddress").getComponent();
      // if
      // (getSecBillCardPanel().getHeadItem("vbilltype").getValue().equals("30"))
      // {
      ((nc.ui.scm.ref.prm.CustAddrRefModel) vdestaddress.getRefModel())
          .setCustId(getSecBillCardPanel().getHeadItem("creceiptcorpid")
              .getValue());
      // }
      // else if
      // (getSecBillCardPanel().getHeadItem("vbilltype").getValue().equals("4U"))
      // {
      // ((nc.ui.prm.ref.CustAddrRefModel)
      // vdestaddress.getRefModel()).setWherePart("0>1");
      // }
    }

    // 自由项和批次号
    initHeaderFreeItem(getSecBillCardPanel());
    ((UIRefPane) getSecBillCardPanel().getHeadItem("vbatchcode").getComponent())
        .setText(dmvo.getHeaderVO().getAttributeValue("vbatchcode") == null ? null
            : dmvo.getHeaderVO().getAttributeValue("vbatchcode").toString());
    initHeaderLot(getSecBillCardPanel());
    // getSecBillCardPanel().setBillValueVO(dmvo, true);
    //

    // --------------------置表头相应参照是否可编辑
    // 上游单据类型
    // if (getEditFlag() == DMBillStatus.CardNew || getEditFlag() ==
    // DMBillStatus.CardEdit) {
    if (getEditFlag() == DMBillStatus.CardNew) {
      // String sSourceBillType= (String)
      // dmvo.getHeaderVO().getAttributeValue("vbilltype");
      /*
       * if (null == sSourceBillType || sSourceBillType.trim().length() == 0)
       * sSourceBillType = "Error"; sSourceBillType = sSourceBillType.trim();
       * //到货单位 getSecBillCardPanel().getHeadItem("creceiptcorpid").setEdit(
       * sSourceBillType.equals(nc.vo.so.pub.SOBillType.SaleOrder)); ////到货仓库
       * getSecBillCardPanel().getHeadItem("pkdeststore").setEdit(
       * sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_AllocationOrder) ||
       * sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.TO_ORDER3) ||
       * sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.TO_ORDER1) ||
       * sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.TO_ORDER2));
       */
      setInvItemEditable(m_planvos[m_num]);
      setOtherItemEditable(m_planvos[m_num]);
    }
    else if(getEditFlag() == DMBillStatus.CardView)
    {
    	getSecBillCardPanel().setEnabled(false);
    }
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
        "UPPSCMCommon-000253")/* @res "数据加载成功" */);
  }

  /**
   * 加载日计划列表模板。 创建日期：(2001-11-15 9:03:35)
   */
  public void loadListTemplet() {

    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
        "UPPSCMCommon-000135")/* @res "开始加载列表模板...." */);

    BillListData bd = new BillListData(getBillListPanel().getDefaultTemplet(
        "7D", getBillListPanel().getBusiType(),
        getBillListPanel().getOperator(), getBillListPanel().getCorp()));

    getBillListPanel().setEnabled(true);
    getBillListPanel().getBodyTable().setEnabled(true);
    bd.setHeadItems(getBillListHeadItems(bd));
    for (int i = 0; i < bd.getHeadItems().length; i++) {
      bd.getHeadItems()[i].setEnabled(false);
    }
    for (int i = 0; i < bd.getBodyItems().length; i++) {
      bd.getBodyItems()[i].setEnabled(false);
    }
    bd.getBodyItem("bchoose").setEdit(true);
    bd.getBodyItem("bchoose").setEnabled(true);

    // 改变界面
    setListPanelByPara(bd);

    changeBillListDataByUserDef(bd);

    // 设置界面，置入数据源
    getBillListPanel().setListData(bd);

    // 表体表加上合计行
    getBillListPanel().getChildListPanel().setTatolRowShow(true);

    // 设置下拉框
    initBodyComboBox();

    // getBillListPanel().getBodyTable().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
        "UPPSCMCommon-000147")/* @res "列表模板加载成功！" */);
  }

  /**
   * 加载批修改卡片模板。 创建日期：(2001-11-15 9:03:35)
   */
  private void loadBatchBillCardTemplet() {

    if (ivjBatchBillCardPanel == null) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
          "UPPSCMCommon-000136")/* @res "开始加载模板...." */);

      BillTempletVO btvo = (BillTempletVO) getSecBillCardPanel()
          .getTempletData(getSecBillType(), null, getUserID(), getCorpID())
          .clone();

      // 改变模板数据的显示位置-所有数据项显示在表头
      changeBatchBillTempletVO(btvo);

      BillData bd = new BillData(btvo);

      bd.setHeadItems(getBillCardHeadItemsBatch(bd));
      // 改变界面
      // bd.getHeadItem("bchoose").setShow(false);
      setBatchCardPanelByPara(bd);

      // 设置界面，置入数据源
      getBatchBillCardPanel().setBillData(bd);

      getBatchBillCardPanel().setPkCorpKey("pksalecorp");

      // 限制输入长度
      // setSecInputLimit();

      // 设置下拉框
      initBatchBodyItem();

      // 初始化公式
      // initFormulaParse();

      // 初试化状态
      // initSecState();

      // String[] ss = bd.getBodyTableCodes();
      // for (int i=0;i<ss.length;i++)
      // SCMEnv.info(ss[i]);

      // 设置合计监听
      getBatchBillCardPanel().setAutoFiltNullValueRow(false);
      getBatchBillCardPanel().setAutoAddLimitLine(false);
      getBatchBillCardPanel().getBillTable().getColumnModel()
          .addColumnModelListener(this);
      // getBatchBillCardPanel().getBillTable().getSelectionModel().addListSelectionListener(this);

      getBatchBillCardPanel().setBodyValueRangeHashtable(
          new ValueRangeHashtableDeliverydailyplan());
      // getBatchBillCardPanel().setHeaderValueRangeHashtable(
      // new ValueRangeHashtableDeliverydailyplan());
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
          "UPPSCMCommon-000176")/* @res "模板加载成功！" */);
    }
  }

  /**
   * 加载卡片模板。 创建日期：(2001-11-15 9:03:35)
   */
  public void loadSecCardTemplet() {
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
        "UPPSCMCommon-000136")/* @res "开始加载模板...." */);

    BillTempletVO btvo = (BillTempletVO) getSecBillCardPanel().getTempletData(
        getSecBillType(), null, getUserID(), getCorpID()).clone();

    // 改变模板数据的显示位置-所有数据项显示在表头
    changeSecBillTempletVO(btvo);

    BillData bd = new BillData(btvo);

    // 改变界面
    bd.getHeadItem("bchoose").setShow(false);
    setSecCardPanel(bd);
    changeBillDataByUserDef(bd);
    // 设置界面，置入数据源
    getSecBillCardPanel().setBillData(bd);

    getSecBillCardPanel().setPkCorpKey("pksalecorp");

    // 到货地址:不用自动检查，返回名称。
    if (getSecBillCardPanel().getHeadItem("vdestaddress") != null
        && getSecBillCardPanel().getHeadItem("vdestaddress").getComponent() != null) {
      UIRefPane vdestaddress = (UIRefPane) getSecBillCardPanel().getHeadItem(
          "vdestaddress").getComponent();
      vdestaddress.setAutoCheck(false);
      vdestaddress.setReturnCode(true);
      getSecBillCardPanel().getHeadItem("vdestaddress").setDataType(
          nc.ui.pub.bill.BillItem.USERDEF);
    }
    if (getSecBillCardPanel().getHeadItem("vbatchcode") != null
        && getSecBillCardPanel().getHeadItem("vbatchcode").getComponent() != null) {
      UIRefPane vdestaddress = (UIRefPane) getSecBillCardPanel().getHeadItem(
          "vdestaddress").getComponent();
      vdestaddress.setAutoCheck(false);
      vdestaddress.setReturnCode(true);
      getSecBillCardPanel().getHeadItem("vbatchcode").setDataType(
          nc.ui.pub.bill.BillItem.USERDEF);
    }

    // 限制输入长度
    setSecInputLimit();

    // 设置下拉框
    initSecHeadComboBox();

    // 初始化公式
    // initFormulaParse();

    // 初试化状态
    initSecState();

    // String[] ss = bd.getBodyTableCodes();
    // for (int i=0;i<ss.length;i++)
    // SCMEnv.info(ss[i]);

    // 设置合计监听
    // getSecBillCardPanel().addBodyTotalListener(this);
    // getSecBillCardPanel().getBodyUIPanel().setVisible(false);
    getSecBillCardPanel().setEnabled(false);
    // ((DMBillCardPanel) getSecBillCardPanel()).addAfterEditListener(this);

    // getSecBillCardPanel().updateValue();
    // getSecBillCardPanel().resumeValue();

    getSecBillCardPanel().setHeaderValueRangeHashtable(
        new ValueRangeHashtableDeliverydailyplan());
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
        "UPPSCMCommon-000176")/* @res "模板加载成功！" */);
  }
  protected BillData changeBillDataByUserDef(BillData oldBillData) {
    //skg st
    if (getDefVOsH() != null) {
      updateItemByDef(oldBillData, getDefVOsH(), "vuserdef", true, 0);
    }
    if (getDefVOsB() != null) {
      updateItemByDef(oldBillData, getDefVOsB(), "vuserdef_b_", false, 0);
    }
    return oldBillData;
    //skg end
  }
  /**
   * 更新自定义项。
   * 创建日期：(2001-11-9 16:18:40)
   */
  protected void updateItemByDef(BillData oldBillData, nc.vo.bd.def.DefVO[] defVOs, String fieldPrefix, boolean isHead,
      int iStartIndex) {
    
    String z1 = nc.ui.ml.NCLangRes.getInstance().getStrByID("_Bill",
                  "UPP_Bill-000502");//自定义
          String z2 = nc.ui.ml.NCLangRes.getInstance().getStrByID("_Bill",
                  "UPP_Bill-000503");//自由项
    if (defVOs == null)
      return;

    for (int i = 0; i < defVOs.length; i++) {
      nc.vo.bd.def.DefVO defVO = defVOs[i];
      //String itemkey = defVO.getFieldName();
      String itemkey = fieldPrefix + (i + iStartIndex);
      BillItem item = null;

      //位置
      if (isHead)
        item = oldBillData.getHeadItem(itemkey);
      else {
        item = oldBillData.getBodyItem(itemkey);
      }

      if (item != null) {
        if (defVO != null) {
          //默认名称
          String defaultshowname = item.getName();
          if (defaultshowname == null 
              || defaultshowname.startsWith("自定义") 
              || defaultshowname.startsWith("自由项")/*-=notranslate=-*/
              || defaultshowname.startsWith("表体自定义") //日计划节点有此情形/*-=notranslate=-*/
              || defaultshowname.startsWith(z1)
                          || defaultshowname.startsWith(z2)
                          || defaultshowname.startsWith("H-UDC")
                          || defaultshowname.startsWith("B-UDC")

              //zxping 20050224
              //其实，在发运的单据模板定义中，并不存在 defaultshowname like 'vuser%' 的情况
              //也就是说，如下一个判断条件完全可以除去
              || defaultshowname.startsWith(fieldPrefix)) {

            defaultshowname = defVO.getDefname();
            item.setName(defaultshowname);
          }
          //输入长度
          int inputlength = defVO.getLengthnum().intValue();
          item.setLength(inputlength);
          //数据类型
          String type = defVO.getType();
          int datatype = BillItem.STRING;
          if (type.equals("备注"))/*-=notranslate=-*/
            datatype = BillItem.STRING;
          else if (type.equals("日期"))/*-=notranslate=-*/
            datatype = BillItem.DATE;
          else if (type.equals("数字")) {/*-=notranslate=-*/
            datatype = BillItem.INTEGER;
            if ((defVO.getDigitnum() != null) && (defVO.getDigitnum().intValue() > 0)) {
              datatype = BillItem.DECIMAL;
              item.setDecimalDigits(defVO.getDigitnum().intValue());
            }
          }
          if (type.equals("统计"))/*-=notranslate=-*/
            datatype = BillItem.USERDEF;
          item.setDataType(datatype);

          //参照类型
           
          String reftype = defVO.getDefdef().getPk_bdinfo();
          if (type.equals("统计")) {/*-=notranslate=-*/
            item.setRefType(reftype);
          }

          item.reCreateComponent();
          item.setIsDef(true);
        }
      }
    }
  }
  /**
   * 加载订单数据 创建日期：(2002-6-17 16:21:39)
   */
  /*
   * public void loadSourceData(DMVO dmvo) { DMDataVO[] dmdvos=
   * dmvo.getBodyVOs(); try { DMVO dmordvo= new DMVO(); if (dmdvos != null &&
   * dmdvos.length != 0) { //反查调拨订单 AllocationHVO[] alhvos=
   * AllocationHBO_Client.queryOutBillsByChildrenPK(dmdvos); if (alhvos != null) {
   * DMDataVO[] converOrdervos= convertSourceVO(alhvos, "4U", "7D");
   * dmordvo.setChildrenVO(converOrdervos); //appendBodyVO(converOrdervos); }
   * //else // showErrorMessage("没有查到订单数据！"); //反查销售订单 AggregatedValueObject[]
   * saleorders= DeliverydailyplanBO_Client.querySoOrderByVOs(dmdvos);
   * DMDataVO[] sales= convertSourceVO(saleorders, "30", "7D");
   * dmordvo.setChildrenVO(sales); //appendBodyVO(converOrdervos); }
   * //setShowState(DMBillStatus.Source); loadPanel(); if (dmordvo != null)
   * getThdBillCardPanel().setBillValueVO(dmordvo);
   * getThdBillCardPanel().getBillModel().execLoadFormula(); } catch (Exception
   * e) { e.printStackTrace(); showErrorMessage("订单查询错误！"); } }
   */

  /**
   * 此处插入方法说明。 创建日期：(2002-6-17 20:35:40)
   */
  public void onAudit() {
    if (m_selectRow < 0 || m_headListVOs == null || m_headListVOs.length == 0) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
          "UPP40140404-000023")/* @res "请选择待审日计划！" */);
      return;
    }
    // 审批前行，审批后切换至
    // int irow = getBillListPanel().getHeadTable().getSelectedRow();
    String sLastPkbillh = m_headListVOs[m_selectRow].getAttributeValue(
        "pkbillh").toString();
    // 得到界面数据
    DMVO dmvo = new DMVO();
    // Hashtable ht = new Hashtable();
    DMDataVO[] dmdvos = (DMDataVO[]) getBillListPanel().getBodyBillModel()
        .getBodyValueVOs(DMDataVO.class.getName());
    // 滤出选择的多行
    ArrayList v = new ArrayList();
    for (int i = 0; i < dmdvos.length; i++) {
      Object iplanstatus = dmdvos[i].getAttributeValue("iplanstatus");
      if (dmdvos[i].getAttributeValue("bchoose") != null
          && dmdvos[i].getAttributeValue("bchoose").toString().equals("Y")
          && iplanstatus != null && iplanstatus.toString().length() > 0
          && new Integer(iplanstatus.toString()).intValue() == DailyPlanStatus.Free
          && dmdvos[i].getAttributeValue("pk_delivdaypl") != null) {
        dmdvos[i].setAttributeValue("iplanstatus", new Integer(
            DailyPlanStatus.Audit));
        dmdvos[i].setAttributeValue("pkapprperson", getUserID()); // 审核人
        dmdvos[i].setAttributeValue("vapprpersonname", getUserName());
        dmdvos[i].setAttributeValue("apprdate", getClientEnvironment()
            .getDate());
        dmdvos[i].setAttributeValue("pkcorp", getCorpID());
        dmdvos[i].setAttributeValue("userid", getOperator()); // 操作人
        dmdvos[i].setStatus(VOStatus.UPDATED);
        // ht.put("pk_delivdaypl",
        // dmdvos[i].getAttributeValue("pk_delivdaypl"));
        v.add(dmdvos[i]);
      }
    }
    if (v.size() == 0) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
          "UPP40140404-000023")/* @res "请选择待审日计划！" */);
      return;
    }
    DMDataVO[] savevos = new DMDataVO[v.size()];
    savevos = (DMDataVO[]) v.toArray(savevos);
    v = null;

    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
        "UPP40140404-000024")/* @res "开始审核………………" */);
    try {
      dmvo.setChildrenVO(savevos);
      // 保存检查
      if (!checkVO(dmvo))
        return;

      // 验证补货标志(对于销售订单)
      String vbilltype = (String) savevos[0].getAttributeValue("vbilltype");
      if (vbilltype.equals("30")) {
        UFBoolean[] bFlags = DeliverydailyplanBO_Client.getSupplyFlag(savevos);
        StringBuffer sb = new StringBuffer();
        String vdelivdayplcode = null;
        for (int i = 0; i < bFlags.length; i++) {
          vdelivdayplcode = (String) savevos[i]
              .getAttributeValue("vdelivdayplcode");
          if (bFlags[i].booleanValue() == false)
            sb.append(vdelivdayplcode
                + nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                    "UPPSCMCommon-000000")/* @res "、" */);
        }
        if (sb != null && sb.toString().trim().length() != 0) {
          if (showOkCancelMessage(sb.toString()
              + nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
                  "UPP40140404-000025")/* @res "等日计划未进行补货入库，是否审核？" */) == UIDialog.ID_CANCEL)
            return;
        }
      }
      // 执行审核
      DMDataVO[] retVos = DeliverydailyplanBO_Client.audit(savevos);
      // 重置界面
      reloadUI(retVos, savevos);
      // 切至选中行
      gotoSelectRow(sLastPkbillh);

      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH010")/* @res "审核成功！！！" */);
      setButton(boEnd, true);

    }
    catch (Exception e) {
      e.printStackTrace();
      if (e instanceof BusinessException)
        showErrorMessage(e.getMessage());
      else
        showErrorMessage(e.getMessage());
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
          "UPP40140404-000027")/* @res "审核失败！！！" */);
    }

  }

  /**
   * 切换到日计划卡片状态 创建日期：(2002-6-20 20:47:09)
   */
  private void onBatch() {
    if (getBillListPanel().getHeadTable().getSelectedRow() < 0) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
          "UPP40140404-000028")/* @res "请选择要批修改的单据！" */);
      return;
    }
    m_bIsBatch = false;
    removeAll();
    loadBatchBillCardTemplet();
    add(getBatchBillCardPanel(), "Center");
    DMVO vo = new DMVO();
    vo.setParentVO(getBillListPanel().getHeadBillModel().getBodyValueRowVO(
        getBillListPanel().getHeadTable().getSelectedRow(),
        DMDataVO.class.getName()));
    vo.setChildrenVO(getBillListPanel().getBodyBillModel().getBodyValueVOs(
        DMDataVO.class.getName()));
    getBatchBillCardPanel().setBillValueVO(vo);
    setButtons(aryButtonBatchGroup);
    setButton(boEdit, true);
    setButton(boSave, false);
    setButton(boCancel, false);
    setButton(boReturn, true);
    getBatchBillCardPanel().setEnabled(false);
    getBatchBillCardPanel().updateValue();
    getBatchBillCardPanel().updateUI();
    m_bIsBatch = true;
  }

  /**
   * 子类实现该方法，响应按钮事件。
   * 
   * @version (00-6-1 10:32:59)
   * @param bo
   *          ButtonObject
 * @throws BusinessException 
   */
	// add by zip:2014/4/5 No 22
	private void onCKKCXX() {
		try {
			BillModel bm=ivjBillListPane.getBodyBillModel();
			int headSelectedRow = getBillListPanel().getHeadTable().getSelectedRow();
//			String pk_corp = (String) getBillListPanel().getHeadBillModel().getValueAt(headSelectedRow, "pk_corp");
			//edit by zwx 2015-9-12 
			String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
			//end by zwx
			IUAPQueryBS queryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			int bodyRowCount = bm.getRowCount();
			for (int i = 0; i < bodyRowCount; i++) {
				String pkinv =(String) bm.getValueAt(i, "pkinv");
				// 已冻结数量
				//edit by zwx 2015-9-12
//				String sql1 = new StringBuilder().append("select sum(nvl(nfreezenum,0)) as rst from ic_freeze where nvl(dr, 0) = 0 and pk_corp = '" + pk_corp + "' and cinvbasid = '" + pkinv + "' and cthawpersonid is null and cspaceid is not null").toString();
				String sql1 = new StringBuilder().append("select sum(nvl(nfreezenum,0)) as rst from ic_freeze where nvl(dr, 0) = 0 and pk_corp = '" + pk_corp + "' and cinventoryid  = '" + pkinv + "' and cthawpersonid is null and cspaceid is not null").toString();
				
				// 现存量
				//edit by zwx 2015-9-12
//				String sql2 = new StringBuilder().append("select sum(nonhandnum) from ic_onhandnum where nvl(dr,0)=0 and cinvbasid='"+pkinv+"' and pk_corp='"+pk_corp+"'").toString();
				String sql2 = new StringBuilder().append("select sum(nvl(nonhandnum,0)) from ic_onhandnum where nvl(dr,0)=0 and cinventoryid='"+pkinv+"' and pk_corp='"+pk_corp+"'").toString();
				double num1,num2,num3;
				Object obj = queryBS.executeQuery(sql1, new ColumnProcessor());
				num1 = obj == null ? 0 : Double.parseDouble(obj.toString());//冻结数量
				obj = queryBS.executeQuery(sql2, new ColumnProcessor());
				num2 = obj == null ? 0 : Double.parseDouble(obj.toString());//现存量
				/*bm.setValueAt(num1, i, "nonhandnum");
				bm.setValueAt(num2, i, "nfreezenum");*/
				//edit by zwx 2015-9-12
				bm.setValueAt(num2, i, "nonhandnum");
				bm.setValueAt(num1, i, "nfreezenum");
				bm.setValueAt(num2-num1, i, "kckhs");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		 }
	// add end
  public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
    if (isListHeaderMultiSelected(bo, null))
      return;
    if(bo == btnCKKCXX) {
		onCKKCXX();
	}
    // 生成日计划
    if (bo == boGenDelivDailyPlan)
      onGenDelivDailyPlan();
    // 订单查询
    else if (bo == boOrder)
      onOrder();
    // 审核
    else if (bo == boAudit)
      onAudit();
    else if (bo == boCancelAudit) // 弃审
      onUnAudit();
    else if (bo == boEnd) // 关闭
      onEnd();
    else if (bo == boSelectAll) // 全选
      onSelectAll();
    else if (bo == boUnSelectAll) // 全消
      onUnSelectAll();
    else if (bo == boSort) // 排序
      onSort();
    else if (bo == boATP) // 可用量
      onATP();
    else if (bo == boReOpen) // 打开
      onReOpen();
    else if (bo == boOutBill) // 出库
      onOutBill();
    else if (bo == boBatch) // 批修改
      onBatch();
    else if (bo == boReturn) // 返回
      onReturn();
    // add by zwx 2014年12月23日 添加现存量查询
    else if (bo == boOnhandnum) 
		onHandnumQuery();
    else {
      boolean extraButtonFired = false;
      ButtonObject[] buttons = getExtendBtns();
      if (buttons != null && buttons.length > 0) {
        int length = buttons.length;
        for (int i = 0; i < length; i++) {
          if (buttons[i] == bo) {
            extraButtonFired = true;
            break;
          }
        }
      }
      // 二次开发按钮出发的点击事件
      if (extraButtonFired) {
        onExtendBtnsClick(bo);
      }
      else {
        super.onButtonClicked(bo);
      }
    }
  }

  // add by zwx 2014-12-23 现存量查询
	private void onHandnumQuery() {
		UITable table = this.getBillListPanel().getBodyTable(); 
		int[] rows = table.getSelectedRows();
		int rowIndex=table.getSelectedRow();
		if (rows.length < 1) {
			MessageDialog.showErrorDlg(this, "提示Prompt",
					"请选择表体行数据！Select the table body line data!");
			return;
		}

		ArrayList<String> invlist = new ArrayList<String>();
		ArrayList<String> storlist = new ArrayList<String>();
		for (int i = 0; i < rows.length; i++) {
			String Invid = String.valueOf(this.getBillListPanel()
					.getBodyBillModel().getValueAt(rowIndex, "pkinv")); 
			String cwarehouseid = String.valueOf(this.getBillListPanel()
					.getBodyBillModel().getValueAt(rowIndex, "pksendstore")); 
			if (invlist.indexOf(Invid) < 0) {
				invlist.add(Invid);
			}
			if (storlist.indexOf(cwarehouseid) < 0) {
				storlist.add(cwarehouseid);
			}
		}
		
		String[] pkInv = new String[invlist.size()];
		for (int i = 0; i < invlist.size(); i++) {
			pkInv[i] = String.valueOf(invlist.get(i));
		}
		String[] pkStor = new String[storlist.size()];
		for (int i = 0; i < storlist.size(); i++) {
			pkStor[i] = String.valueOf(storlist.get(i));
		}
		int result=this.showYesNoMessage("是否只查询选择行发货仓库的可用现存量？");
		if(result==UIDialog.ID_YES){
			QueryXcl dialog = new QueryXcl(getBillListPanel(), pkInv, pkStor);
			dialog.showModal();
		}else{
			nc.ui.dm.dm104.QueryXcl dialog = new nc.ui.dm.dm104.QueryXcl(
					getBillListPanel(), pkInv);
			dialog.showModal();
		}
	
	}
//end by zwx

/**
   * 此处插入方法说明。 创建日期：(2002-6-11 9:29:50)
   */
  public void onCancel() {
    if (m_bIsBatch) {
        getBatchBillCardPanel().stopEditing();
        getBatchBillCardPanel().setEnabled(false);
        // DMVO vo = new DMVO();
        // vo.setChildrenVO(getBillListPanel().getBodyBillModel().getBodyValueVOs(DMDataVO.class.getName()));
        // getBatchBillCardPanel().setBillValueVO(vo);
        getBatchBillCardPanel().resumeValue();
        getBatchBillCardPanel().updateValue();
        setButton(boEdit, true);
        setButton(boSave, false);
        setButton(boCancel, false);
        setButton(boReturn, true);
        updateButtons();
    }
    // m_bIsBatch = false;
    else {
      try {
        if (getEditFlag() == DMBillStatus.CardNew) {
          // 释放单据号
          DeliverydailyplanBO_Client.returnBillCodeForUI("vdayplanbilltype",
              "pkcorp", "vdelivdayplcode", m_planvos);

          if (m_planvos != null || m_planvos.length != 0) {
            getSecBillCardPanel().getBillData().clearViewData();
            switchButtonStatus(DMBillStatus.CardView);
            setEditFlag(DMBillStatus.CardView);
            getSecBillCardPanel().setEnabled(false);
            m_planvos = null;
            onSwitchSource();
            return;
          }
        }
        else if (getEditFlag() == DMBillStatus.CardEdit) {
          // DMVO dmvo = new DMVO();

          Object pkbillh = m_planvos[m_num].getAttributeValue("pkbillh");
          ArrayList v = (ArrayList) m_bodyListHt.get(pkbillh);
          if (v != null && v.size() != 0) {
            DMDataVO[] bodyvos = new DMDataVO[v.size()];
            bodyvos = (DMDataVO[]) v.toArray(bodyvos);
            v = null;
            m_planvos = bodyvos;
          }

          // 此句冗余，因为在切换到该卡片界面时，
          // 会设置其中的数据.
          // （PS, 该冗余，在 2005-02-01 招致不必要的错误）
          // by zxping
          // getSecBillCardPanel().resumeValue();

          switchButtonStatus(DMBillStatus.ListView);
          getSecBillCardPanel().setEnabled(false);
          setEditFlag(DMBillStatus.ListView);
          onSwitchList();
        }
        String message = NCLangRes.getInstance().getStrByID("common", "UCH008");
        /* @res取消成功 */
        this.showHintMessage( message );
      }
      catch (Exception e) {
        e.printStackTrace();
        if (e instanceof BusinessException)
          showErrorMessage(e.getMessage());
        else
          showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "40140404", "UPP40140404-000029")/* @res "取消失败！" */);
      }
    }
  }

  /**
   * 删除日计划。 创建日期：(2002-5-10 11:25:52)
   */
  public void onDel() {
    boolean isAutoCancelOder = false;
    String pkdelivdaypl = getSecBillCardPanel().getHeadItem("pk_delivdaypl")
        .getValue();
    if (pkdelivdaypl == null || pkdelivdaypl.trim().length() == 0)
      return;
    if (m_planvos == null || m_planvos.length == 0)
      return;
    if (MessageDialog.showYesNoDlg(this,null,nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "40140404", "UPP40140404-000030")/* @res "确定删除！！！" */,MessageDialog.ID_NO) == MessageDialog.ID_NO)
      return;

    if (getEditFlag() == DMBillStatus.CardView) {
      DMDataVO[] dmdvos = new DMDataVO[1];
      dmdvos[0] = getCardData();
      Object osendnum = dmdvos[0].getAttributeValue("dsendnum");
      Integer iplanstatus = (Integer) dmdvos[0]
          .getAttributeValue("iplanstatus");
      if (osendnum != null && ((UFDouble) osendnum).doubleValue() != 0) {
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "40140404", "UPP40140404-000031")/* @res "日计划已安排发运数量，不可删除！！" */);
        return;
      }
      if (iplanstatus.intValue() == DailyPlanStatus.Audit) {
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "40140404", "UPP40140404-000032")/* @res "日计划已审批，不可删除！！！" */);
        return;
      }
      if (iplanstatus.intValue() == DailyPlanStatus.End) {
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "40140404", "UPP40140404-000032")/* @res "日计划已审批，不可删除！！！" */);
        return;
      }

      String sBilltype = getSecBillCardPanel().getHeadItem("vbilltype")
          .getValue();
      if (sBilltype.equals("30")) {
        if (showYesNoMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "40140404", "UPP40140404-000033")/* @res "发运日计划删除后是否自动取消订单的关闭状态？" */) == UIDialog.ID_YES)
          isAutoCancelOder = true;
      }

      try {
        dmdvos[0].setStatus(VOStatus.DELETED);
        dmdvos[0].setAttributeValue("userid", getOperator()); // 操作人
        // if (isAutoCancelOder == true)
        for (int k = 0; k < dmdvos.length; k++)
          dmdvos[k].setAttributeValue("delautocanceloder", new UFBoolean(
              !isAutoCancelOder));
        DMDataVO[] ordervos = getWriteOrderData(dmdvos);

        // 删除日计划
        DeliverydailyplanBO_Client.saveAndWrite(ordervos, dmdvos);

        m_allHt.remove(pkdelivdaypl);

        loadBillDate();

        m_planvos = null;

        // 重置卡片界面
        getSecBillCardPanel().getBillData().clearViewData();
        switchButtonStatus(DMBillStatus.CardView);
        setEditFlag(DMBillStatus.CardView);
        getSecBillCardPanel().setEnabled(false);
        // 重置列表界面
        getBillListPanel().getBodyBillModel().clearBodyData();
        getBillListPanel().getHeadTable().clearSelection();
        onSwitchList();
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
            "UCH006")/* @res "删除成功！" */);
      }
      catch (Exception e) {
        e.printStackTrace();
        if (e instanceof BusinessException)
          showErrorMessage(e.getMessage());
        else
          showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "40140404", "UPP40140404-000034")/* @res "保存数据失败！" */);
      }

    }
  }

  /**
   * 此处插入方法说明。 创建日期：(2002-6-10 9:50:13)
   * huxiaobo modified at 2060829 14:14
   */
  public void onEdit() {
    if (m_bIsBatch) {
    	//modified begin
      int length=getBatchBillCardPanel().getBillData().getBillValueVO(
      		  "nc.vo.dm.dm102.DMOrderVO","nc.vo.dm.dm102.DMOrderHeadDataVO",
      		  "nc.vo.dm.dm102.DMOrderBodyDataVO").getChildrenVO().length;
      Integer iplanstatus;
      for(int i=0;i<length;i++)
      {  
    	  iplanstatus=new Integer((Integer)getBatchBillCardPanel().getBillData().getBillValueVO(
    		  "nc.vo.dm.dm102.DMOrderVO","nc.vo.dm.dm102.DMOrderHeadDataVO",
    		  "nc.vo.dm.dm102.DMOrderBodyDataVO").getChildrenVO()[i].getAttributeValue("iplanstatus"));
    	  if (iplanstatus.equals(new Integer(DailyPlanStatus.Audit))
    	      || iplanstatus.equals(new Integer(DailyPlanStatus.End))) {
    	      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
    	        "40140404", "UPP40140404-000035")/* @res "自由状态的日计划才可修改，请检查日计划状态！！！" */);
    	      return;
    	   }
      }
      //modified end
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH027"));
      getBatchBillCardPanel().setEnabled(true);
      getBatchBillCardPanel().getHeadItem("pksendstore").setEnabled(false);
      
      setInvItemEditableBatch(getBatchBillCardPanel());
      setOtherItemEditableBatch(getBatchBillCardPanel());
      setButton(boEdit, false);
      setButton(boSave, true);
      setButton(boCancel, true);
      setButton(boReturn, false);
      updateButtons();
    }
    else {
      if (m_planvos == null || m_planvos.length == 0)
        return;
      DMDataVO dmdvo = getCardData();
      Object oplanstatus = dmdvo.getAttributeValue("iplanstatus");
      Integer iplanstatus = null;
      if(oplanstatus != null && oplanstatus.toString().length() > 0)
      {
    	  iplanstatus =  new Integer(oplanstatus.toString());
      }
      if (iplanstatus != null && (iplanstatus.intValue() == DailyPlanStatus.Audit
          || iplanstatus.intValue()== DailyPlanStatus.End)) {
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "40140404", "UPP40140404-000035")/* @res "自由状态的日计划才可修改，请检查日计划状态！！！" */);
        return;
      }
      switchButtonStatus(DMBillStatus.CardEdit);
      setEditFlag(DMBillStatus.CardEdit);
      getSecBillCardPanel().setEnabled(true);
      setInvItemEditable(m_planvos[m_num]);
      setOtherItemEditable(m_planvos[m_num]);
    }
    afterSendStoreOrg();
  }
  /**
   * 作者：毕晖 查存货属性 创建日期：(2002-6-11 9:47:30)
   * 
   * @param sInv
   *          java.lang.String[]
   */
  private void setInvItemEditableBatch(DMBillCardPanel bcp) {
    // 根据存货过滤空行
    // String[] itemkeys = { "pkinv" };
    // filterNullLine(itemkeys, bcp);
    // 获得界面数据
    DMVO dvo = (DMVO) bcp.getBillValueVO(DMVO.class.getName(), DMDataVO.class
        .getName(), DMDataVO.class.getName());
    DMDataVO[] dmdvos = dvo.getBodyVOs();
    // 获得存货主键、辅计量主键
    String[] invkeys = new String[dmdvos.length];
    String[] astkeys = new String[dmdvos.length];
    for (int i = 0; i < dmdvos.length; i++) {
      invkeys[i] = (String) dmdvos[i].getAttributeValue("pkinv");
      astkeys[i] = (String) dmdvos[i].getAttributeValue("pkassistmeasure");
    }
    // bcp.getBodyItem("vbatchcode").setEnabled(true);
    // bcp.getBodyItem("vbatchcode").setEdit(true);
    // 获得存货信息
    InvVO[] invvos = getInvInfo(invkeys, astkeys);
    // 置存货属性是否可编辑
    for (int i = 0; i < dmdvos.length; i++) {
      // 上游单据类型
      String sSourceBillType = (String) dmdvos[i]
          .getAttributeValue("vbilltype");
      // 是否辅计量
      // 辅数量
      Integer isassistunit = (Integer) invvos[i]
          .getAttributeValue("isAstUOMmgt");
      getBatchHsl(i, null);
      // bcp.setCellEditable(i, "vassistmeaname", (isassistunit.intValue() ==
      // 1));
      bcp.setCellEditable(i, "dassistnum", (isassistunit.intValue() == 1));
      // 批次管理
      Integer isbatch = (Integer) invvos[i].getAttributeValue("isLotMgt");
      if (!sSourceBillType.equals("21")) {
        bcp.setCellEditable(i, "vbatchcode", (isbatch != null && isbatch
            .intValue() == 1));
      }
      // 自由项
      Integer isfreeitem = (Integer) invvos[i]
          .getAttributeValue("isFreeItemMgt");
     // bcp.setCellEditable(i, "vfree0", false);
       bcp.setCellEditable(i, "vfree0", (isfreeitem!=null &&
       isfreeitem.intValue() == 1));

      // if (null == sSourceBillType || sSourceBillType.trim().length() == 0)
      // sSourceBillType= "Error";
      // sSourceBillType= sSourceBillType.trim();

      // 到货单位
      // bcp.setCellEditable(
      // i,
      // "creceiptcorp",
      // sSourceBillType.equals(nc.vo.so.pub.SOBillType.SaleOrder));
      // //到货仓库
      // bcp.setCellEditable(
      // i,
      // "vdeststorename",
      // sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_AllocationOrder));
    }
  }

  /**
   * 此处插入方法说明。 创建日期：(2002-6-17 20:35:40)
   */
  public void onEnd() {
    if (m_selectRow < 0 || m_headListVOs == null || m_headListVOs.length == 0) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
          "UPP40140404-000036")/* @res "请选择待关闭日计划！" */);
      return;
    }
    // 审批前行，审批后切换至
    // int irow = getBillListPanel().getHeadTable().getSelectedRow();
    String sLastPkbillh = m_headListVOs[m_selectRow].getAttributeValue(
        "pkbillh").toString();
    // 得到界面数据
    Hashtable ht = new Hashtable();
    
    DMDataVO[] dmdvos = (DMDataVO[]) getBillListPanel().getBodyBillModel()
        .getBodyValueVOs(DMDataVO.class.getName());
    // 滤出选择的多行
    ArrayList v = new ArrayList();
    for (int i = 0; i < dmdvos.length; i++) {
      Object iplanstatus = dmdvos[i].getAttributeValue("iplanstatus");
      if (dmdvos[i].getAttributeValue("bchoose") != null
          && dmdvos[i].getAttributeValue("bchoose").toString().equals("Y")
          && iplanstatus != null && iplanstatus.toString().length() > 0
      	  && new Integer(iplanstatus.toString()).intValue() == DailyPlanStatus.Audit) {
        dmdvos[i].setAttributeValue("iplanstatus", new Integer(
            DailyPlanStatus.End));
        dmdvos[i].setAttributeValue("pkcorp", getCorpID());
        dmdvos[i].setAttributeValue("userid", getOperator()); // 操作人
        dmdvos[i].setStatus(VOStatus.UPDATED);
        ht.put("pk_delivdaypl", dmdvos[i].getAttributeValue("pk_delivdaypl"));
        v.add(dmdvos[i]);
      }
    }
    if (v.size() == 0) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
          "UPP40140404-000036")/* @res "请选择待关闭日计划！" */);
      return;
    }
    DMDataVO[] savevos = new DMDataVO[v.size()];
    savevos = (DMDataVO[]) v.toArray(savevos);
    v = null;
    try {
      DMDataVO[] retVos = DeliverydailyplanBO_Client.end(savevos);
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH013"));/* 已关闭 */
      setButton(boReOpen, true);
      // 重置回写上游数量
      if (DM016.booleanValue()) {
        for (int i = 0; i < savevos.length; i++) {
          if (getDelivSequence() == 1)
            savevos[i].setAttributeValue("nfeedbacknum", savevos[i]
                .getAttributeValue("doutnum"));
          else if (getDelivSequence() == 0)
            savevos[i].setAttributeValue("nfeedbacknum", savevos[i]
                .getAttributeValue("dsendnum"));
        }
      }
      // 重置界面
      reloadUI(retVos, savevos);

    }
    catch (Exception e) {
      e.printStackTrace();
      if (e instanceof BusinessException)
        showErrorMessage(e.getMessage());
      else
        showErrorMessage(e.getMessage());
    }
    // 切至选中行
    gotoSelectRow(sLastPkbillh);
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-8-7 11:00:57) 修改日期，修改人，修改原因，注释标志：
   */
  public void onFrist() {
    if (m_planvos == null || m_planvos.length == 0) {
      getSecBillCardPanel().getBillData().clearViewData();
      return;
    }
    if (m_num == 0) {
      showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40140404", "UPP40140404-000037")/* @res "已经是第一张!" */);
      return;
    }
    getCardData();
    m_num = 0;
    loadCardData();
    afterSendStoreOrg();//从list切换过来的时候，要用发火库存组织约束发货仓库 huxiaobo modified at 20060828
  }

  /**
   * 此处插入方法说明。 创建日期：(2002-5-22 12:50:14)
   */
  public void onGenDelivDailyPlan() {
    // 得到界面数据
    DMVO dmvo = new DMVO();
    dmvo = (DMVO) getThdBillCardPanel().getBillValueVO(DMVO.class.getName(),
        DMDataVO.class.getName(), DMDataVO.class.getName());
    DMDataVO[] dmdvos = dmvo.getBodyVOs();
    // 滤出选择的多行
    ArrayList v = new ArrayList();
    for (int i = 0; i < dmdvos.length; i++) {
      if (dmdvos[i].getAttributeValue("bchoose") != null
          && dmdvos[i].getAttributeValue("bchoose").toString().equals("Y")) {
        v.add(dmdvos[i]);
      }
    }
    if (v.size() == 0) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
          "UPP40140404-000038")/* @res "请选择订单！" */);
      return;
    }
    m_planvos = new DMDataVO[v.size()];
    m_planvos = (DMDataVO[]) v.toArray(m_planvos);
    v = null;

    // 生成数据
    // 已安排发运数量
    UFDouble ndelivernum = null;
    // 途损数量
    UFDouble nwaylossnum = null;
    // 退回数量
    UFDouble nbacknum = null;
    for (int i = 0; i < m_planvos.length; i++) {

      ndelivernum = m_planvos[i].getAttributeValue("ndelivernum") == null ? new UFDouble(
          0)
          : new UFDouble(m_planvos[i].getAttributeValue("ndelivernum")
              .toString());
      nwaylossnum = m_planvos[i].getAttributeValue("dwaylossnum") == null ? new UFDouble(
          0)
          : new UFDouble(m_planvos[i].getAttributeValue("dwaylossnum")
              .toString());
      nbacknum = m_planvos[i].getAttributeValue("dbacknum") == null ? new UFDouble(
          0)
          : new UFDouble(m_planvos[i].getAttributeValue("dbacknum").toString());
      // "赛格日立"使用
      if (bseg) {
        m_planvos[i].setAttributeValue("dnum", null);
        m_planvos[i].setAttributeValue("dassistnum", null);
        m_planvos[i].setAttributeValue("dweight", null);
        m_planvos[i].setAttributeValue("dvolumn", null);
      }
      // 通版
      else {

        // 数量
        UFDouble dnum = m_planvos[i].getAttributeValue("dnum") == null ? new UFDouble(
            0)
            : new UFDouble(m_planvos[i].getAttributeValue("dnum").toString());
        UFDouble doldnum = dnum;

        // 辅数量
        if (m_planvos[i].getAttributeValue("dassistnum") == null) {
          // 20060626 发运日计划数量=订单数量+途损数量+退回数量-已安排发运数量
          dnum = dnum.add(nwaylossnum).add(nbacknum).sub(ndelivernum);
          m_planvos[i].setAttributeValue("dassistnum", null);

        }
        else {
          UFDouble dassistnum = new UFDouble(m_planvos[i].getAttributeValue(
              "dassistnum").toString());

          // 换算率
          UFDouble dconvertRate = null;
          if (dnum.doubleValue() != 0 && dassistnum.doubleValue() != 0)
            dconvertRate = dassistnum.div(dnum);
          else
            dconvertRate = new UFDouble(0);

          dnum = dnum.add(nwaylossnum).add(nbacknum).sub(ndelivernum);
          dassistnum = dnum.multiply(dconvertRate);
          m_planvos[i].setAttributeValue("dassistnum", dassistnum);
        }

        m_planvos[i].setAttributeValue("dnum", dnum);

        // 重量
        m_planvos[i].setAttributeValue("dweight", getNewValueByHSL(m_planvos[i]
            .getAttributeValue("dweight"), doldnum, dnum, BD501));
        // 体积
        m_planvos[i].setAttributeValue("dvolumn", getNewValueByHSL(m_planvos[i]
            .getAttributeValue("dvolumn"), doldnum, dnum, BD501));
      }

      m_planvos[i].setAttributeValue("dsendnum", null);
      m_planvos[i].setAttributeValue("dsignnum", null);
      m_planvos[i].setAttributeValue("doutnum", null);
      m_planvos[i].setAttributeValue("dbacknum", null);
      m_planvos[i].setAttributeValue("iplanstatus", new Integer(
          DailyPlanStatus.Free));
      m_planvos[i].setAttributeValue("bchoose", new UFBoolean(false));
      m_planvos[i].setAttributeValue("pkcorp", getBelongCorpIDofDelivOrg());
      // 用于生成单据号，界面和数据库均不保存
      m_planvos[i].setAttributeValue("pkplanperson", getUserID());
      m_planvos[i].setAttributeValue("vplanpersonname", getUserName());
      m_planvos[i].setAttributeValue("plandate", getClientEnvironment()
          .getDate());
      // 实际发货日期
      m_planvos[i].setAttributeValue("snddate", m_planvos[i]
          .getAttributeValue("ordsndate"));
      // 更改ts
      m_planvos[i].setAttributeValue("sourcebillts", m_planvos[i]
          .getAttributeValue("ts"));
      m_planvos[i].setAttributeValue("pkdelivorg", getDelivOrgPK());
      m_planvos[i].setAttributeValue("ts", null);
      m_planvos[i].setStatus(VOStatus.NEW);
      m_planvos[i].setAttributeValue("nfeedbacknum", m_planvos[i]
          .getAttributeValue("dnum"));

      // //获得单据号
      // if (m_planvos[i].getAttributeValue("vdelivdayplcode") == null
      // ||
      // m_planvos[i].getAttributeValue("vdelivdayplcode").toString().trim().length()
      // == 0) {
      // if (!GeneralMethod
      // .setBillCode(
      // m_planvos[i],
      // DMBillTypeConst.m_delivDelivDayPl,
      // getBillCardPanel(),
      // "vdelivdayplcode",
      // "pkcorp")) {
      // nc.ui.pub.beans.MessageDialog.showErrorDlg(this, "错误",
      // "获得单据号失败！");
      // return;
      // }
      // }
    }

    // 获得单据号
    try {
      m_planvos = DeliverydailyplanBO_Client.setBillCode(m_planvos,
          DMBillTypeConst.m_delivDelivDayPl, "vdelivdayplcode", "pkcorp");
    }
    catch (Exception e) {
      reportException(e);
      showErrorMessage(e.getMessage());
      // nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
      // nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
      // "UPPSCMCommon-000059")/*@res "错误"*/,
      // nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
      // "UPP40140404-000039")/*@res "获得单据号失败！"*/);
      return;
    }

    // 转入相应的窗口——日计划表单界面
    setShowState(DMBillStatus.Card);
    loadPanel();
    switchButtonStatus(DMBillStatus.CardNew);
    setEditFlag(DMBillStatus.CardNew);
    getSecBillCardPanel().setEnabled(true);
    setTitleText(strTitle2);

    // 置入界面
    DMVO cardvo = new DMVO();
    m_num = 0;
    loadCardData();
    afterSendStoreOrg();//从list切换过来的时候，要用发火库存组织约束发货仓库 huxiaobo modified at 20060828
    // 记录发生时间
    try {
      m_ufdtAddTime = nc.ui.scm.recordtime.RecordTimeHelper.getTimeStamp();

    }
    catch (Exception e) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
          "UPP40140404-000040")/* @res "取得生成时间错误!" */);
      m_ufdtAddTime = null;
    }
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-8-7 11:04:15) 修改日期，修改人，修改原因，注释标志：
   */
  public void onLast() {
    if (m_planvos == null || m_planvos.length == 0)
      return;
    if (m_num == m_planvos.length - 1) {
      showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40140404", "UPP40140404-000041")/* @res "已经是最后一张!" */);
      return;
    }
    getCardData();
    m_num = m_planvos.length - 1;
    loadCardData();
    afterSendStoreOrg();//从list切换过来的时候，要用发火库存组织约束发货仓库 huxiaobo modified at 20060828
  }

  /**
   * 下一单据。 创建日期：(2001-4-24 9:55:56)
   */
  public void onNext() {
    // if (m_bcardpagestatus == true) {
    if (m_planvos == null || m_planvos.length == 0)
      return;

    if (m_num < m_planvos.length - 1) {
      getCardData();
      m_num++;
      loadCardData();
      afterSendStoreOrg();//从list切换过来的时候，要用发火库存组织约束发货仓库 huxiaobo modified at 20060828
      // setButtonsState();
    }
    else {
      showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40140404", "UPP40140404-000041")/* @res "已经是最后一张!" */);
    }
    // }
    /*
     * else if (m_bcardpagestatus == false) { if (m_planvos == null ||
     * m_planvos.length == 0) return; getCardData(); if (m_num <
     * m_planvos.length - 1) { m_num++; loadCardData(); //setButtonsState(); }
     * //相当于由列表中相应订单对应的日计划切换到表单界面后再翻页 m_bcardpagestatus = true; }
     */
  }

  /**
   * 查询订单 创建日期：(2001-4-21 14:11:14)
   */
  private void onOrder() {
    // ivjQueryOrderConditionDlg = null;
    // 查询订单
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
        "UPP40140404-000109")/* @res "正在进行订单查询" */);
    if (ivjQueryOrderConditionDlg == null)
      getQueryOrderConditionDlg().setTempletID(getCorpPrimaryKey(), "40149901",
          getClientEnvironment().getUser().getPrimaryKey(), null);
    getQueryOrderConditionDlg().hideNormal();
    getQueryOrderConditionDlg().showModal();
    if (!getQueryOrderConditionDlg().isCloseOK()) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
          ""));
      return;
    }
    // 获得查询条件
    m_voCons = getQueryOrderConditionDlg().getConditionVO();
    m_voCons = nc.ui.scm.pub.query.ConvertQueryCondition.getConvertedVO(
        m_voCons, null);
    m_voCons = nc.vo.scm.pub.query.ConvertQueryCondition
        .getLikeCondition(m_voCons);

    getQueryOrderConditionDlg().checkCondition(m_voCons);
    //
    m_initOrdervos = null;

    afterOrderQuery(m_voCons);

  }

  /**
   * 返回。 创建日期：(2001-4-24 9:55:42)
   */
  private void onReturn() {
    String sLastPkBillh = getBatchBillCardPanel().getHeadItem("pkbillh")
        .getValue();
    getBatchBillCardPanel().setEnabled(false);
    DMDataVO[] bodys = (DMDataVO[]) getBatchBillCardPanel().getBillModel()
        .getBodyValueVOs(DMDataVO.class.getName());
    getBillListPanel().setBodyValueVO(bodys);
    onSwitchList();
    gotoSelectRow(sLastPkBillh);
    m_bIsBatch = false;
    // ivjBatchBillCardPanel = null;
  }

  /**
   * 前一单据。 创建日期：(2001-4-24 9:55:42)
   */
  public void onPre() {
    if (m_planvos == null || m_planvos.length == 0)
      return;
    if (m_num == 0) {
      showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40140404", "UPP40140404-000037")/* @res "已经是第一张!" */);
      return;
    }

    getCardData();
    if (m_num > 0) {
      m_num--;
      loadCardData();
      afterSendStoreOrg();//从list切换过来的时候，要用发火库存组织约束发货仓库 huxiaobo modified at 20060828
      // setButtonsState();
    }
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-7-4 14:42:22) 修改日期， 修改人， 修改原因， 注释标志：
   */
  public void onPrint() {
    if (m_strShowState.equals(DMBillStatus.List)) {

      DMDataVO[] dmdvos = getVOsFromHashtable(m_allHt);
      if (dmdvos == null || dmdvos.length == 0)
        return;

      String sBillPkKey = "pk_delivdaypl";
      String[] oldPrimaryKeys = new String[dmdvos.length];// 用于记录原始的 primaryKey
                                                          // 的值，便于打印后恢复
      for (int i = 0; i < dmdvos.length; i++) {
        oldPrimaryKeys[i] = dmdvos[i].getPrimaryKey();

        // 必须调用方法 dmdvos[i].setPrimaryKey( xx )
        // 以设置 vo 的 billpk, 否则打印的次数不能更新
        dmdvos[i].setPrimaryKey((String) dmdvos[i]
            .getAttributeValue(sBillPkKey));
      }

      DMVO vo = new DMVO();
      vo.setParentVO(new DMDataVO());
      vo.setChildrenVO(dmdvos);

      // ((DMBillCardPanel) getBillCardPanel()).onCardPrint(vo);

      /** ************ Begin print: by zxping ******* */
      ArrayList alBill = new ArrayList();
      alBill.add(vo);
      try {

        BillPrintTool bpt = new BillPrintTool("40140404", // getModuleCode(),
            alBill, getSecBillCardPanel().getBillData(), null, null, null,
            "vdelivdayplcode", sBillPkKey);

        bpt.onCardPrint(getSecBillCardPanel(), getBillListPanel(),
            DMBillTypeConst.m_delivDelivDayPl);

        // 打印后恢复 primaryKey 的值
        for (int i = 0; i < dmdvos.length; i++) {
          dmdvos[i].setPrimaryKey(oldPrimaryKeys[i]);
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      /** ************ End print: by zxping ******* */

      this.setAllVOs(new ArrayList());
    }
  }

  /**
   * 此处插入方法说明。 创建日期：(2002-6-7 15:58:25)
   */
  public void onQuery() {
    // super.onQuery();
    // if (ivjQueryConditionDlg == null)
    // ivjQueryConditionDlg.setTempletID(getCorpPrimaryKey(), "40140404",
    // getClientEnvironment().getUser().getPrimaryKey(), null);
    // ivjQueryConditionDlg = null;
    getQueryConditionDlg().hideNormal();
    getQueryConditionDlg().showModal();

    if (!getQueryConditionDlg().isCloseOK())
      return;
    // 获得查询条件
    nc.vo.pub.query.ConditionVO[] voCons = getQueryConditionDlg()
        .getConditionVO();
    voCons = nc.ui.scm.pub.query.ConvertQueryCondition.getConvertedVO(voCons,
        null);

    // getQueryConditionDlg().checkCondition(voCons);
    afterQuery(voCons);
  }

  /**
   * 此处插入方法说明。 创建日期：(2002-5-16 11:14:39)
   */
  
  public void onSave() {
    if (m_bIsBatch) {
      onSaveBatch();
    }
    else {
      getSecBillCardPanel().stopEditing();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
          "UPP40140404-000042")/* @res "开始保存………………" */);
      try {
        DMDataVO dmdvo = getCardData();

        if (dmdvo == null)
          return;
        if(dmdvo!=null){
        UFDate ordsndate = (UFDate)dmdvo.getAttributeValue("ordsndate");
        UFDate requiredate = (UFDate)dmdvo.getAttributeValue("requiredate");
        if(ordsndate==null){
          showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "40140404", "UPP40140404-000112")/* @res "发货日期必填！" */);
          return;
        }
        if(requiredate==null){
          showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "40140404", "UPP40140404-000113")/* @res "到货日期必填！" */);
          return;
        }
        if(!requiredate.toString().equals(ordsndate.toString())){
        if(!requiredate.after(ordsndate)){
          showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "40140404", "UPP40140404-000111")/* @res "到货日期必须在发货日期之后！" */);
          return;
        }
        }
        
      //update by danxionghui  在价格表中查询数据 没有的话提示  不能继续操作
      //发运方式+发货地点+到货地点+订单要求到货日期
        //根据发运组织 判读提示
        //add by shikun 2014-11-26 改为各公司都可以进行校验
        String pkdelivorg=(String) dmdvo.getAttributeValue("pkdelivorg");
        DelivorgVO bill = DelivorgHelper.findByPrimaryKey(pkdelivorg);
        if (bill!=null) {
            DelivorgHeaderVO head = (DelivorgHeaderVO)bill.getParentVO();
            if (head!=null) {
                String idelivsequence = head.getAttributeValue("vdoname")==null?"":head.getAttributeValue("vdoname").toString();
                if (!"".equals(idelivsequence)&&idelivsequence.indexOf("自提")==-1) {
                    String xyy_fyfs = (String) dmdvo.getAttributeValue("pksendtype");
                    String xyy_fhdd = (String) dmdvo.getAttributeValue("pksendaddress");
                    String xyy_dhdd = (String) dmdvo.getAttributeValue("pkarriveaddress");
                    UFDate xyy_rq = (UFDate) dmdvo.getAttributeValue("requiredate");
                    if(StringUtils.isEmpty(xyy_fyfs)) {
                      	showErrorMessage("请选择发运方式");
                      	return;
                      }
                      if(StringUtils.isEmpty(xyy_fhdd)) {
                      	showErrorMessage("请选择发货地点");
                      	return;
                      }
                      if(StringUtils.isEmpty(xyy_dhdd)) {
                      	showErrorMessage("请选择到货地点");
                      	return;
                      }
                      if(xyy_rq == null) {
                      	showErrorMessage("请选择发货日期");
                      	return;
                      }
                      String sql = new StringBuilder()
                      .append("select pk_basicprice from dm_baseprice")
                      .append(" where nvl(dr,0)=0")
                      .append(" and pk_sendtype='"+xyy_fyfs+"'")
                      .append(" and pkfromaddress ='"+xyy_fhdd+"'")
                      .append(" and pktoaddress='"+xyy_dhdd+"'")
                      .append(" and effectdate <= '"+xyy_rq+"'")
                      .append(" and expirationdate > '"+xyy_rq+"'")
                      .append(" and nvl(dr,0)=0").toString();
                      Object cysObj = DbUtil.getDMO().getObject(sql);
                      if(cysObj==null){
                      	showErrorMessage("没有有效的运输协议信息，请联系物流部门增加运费价格表的相关数据");
                      	return;
                      }
				}
			}
		}
        //end shikun 2014-11-26
        }

        DMDataVO[] savevos = null;
     
        if (getEditFlag() == DMBillStatus.CardEdit) {
        	
            
          savevos = new DMDataVO[1];
          savevos[0] = dmdvo;
          savevos[0].setAttributeValue("userid", getOperator()); // 操作人
          // 2002-10-21 用于可用量
          savevos[0].setAttributeValue("thisaction", "修改");
          // 为了回写采购订单，提供修改前的数量
          // 对于删除、新增，oldnum 字段的值在保存时根据vo行标志进行构建
          String pkdelivdaypl = (String) savevos[0]
              .getAttributeValue("pk_delivdaypl");
          Object objOldNum = ((DMDataVO) m_allHt.get(pkdelivdaypl))
              .getAttributeValue("dnum");
          savevos[0].setAttributeValue("doldnum", objOldNum);
        }
        else if (getEditFlag() == DMBillStatus.CardNew) {
          savevos = new DMDataVO[m_planvos.length];
          for (int i = 0; i < m_planvos.length; i++) {
            savevos[i] = m_planvos[i];
            savevos[i].setAttributeValue("userid", getOperator()); // 操作人
            savevos[i].setAttributeValue("pkdelivorg", getDelivOrgPK()); // 发运组织
          }
          // modify by zxj 20030530
          m_bcardpagestatus = false;
        }

        // 保存检查
        DMVO dvo = new DMVO();
        dvo.setChildrenVO(savevos);
        if (!checkVO(dvo))
          return;

        // 获得回写定单的数据
        DMDataVO[] ordervos = getWriteOrderData(savevos);

        // 获得订单发货剩余数量和是否关闭
        DMDataVO[] sourceInfos = DeliverydailyplanBO_Client
            .getSourceSpareNumAndStatus(ordervos);

        if (sourceInfos == null || sourceInfos.length != savevos.length) {
          showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "40140404", "UPP40140404-000043")/* @res "订单已改变。请重查！" */);
          return;
        }

        // String vbilltype = null;
        for (int i = 0; i < savevos.length; i++) {
          // vbilltype = (String) savevos[i].getAttributeValue("vbilltype");
          UFDouble dsparenum = new UFDouble(0);
          if (sourceInfos[i] == null) {
            showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "40140404", "UPP40140404-000043")/* @res "订单已改变。请重查！" */);
            return;
          }
          UFDouble dSourceSpares = (UFDouble) sourceInfos[i]
              .getAttributeValue("ndelivernum");

          if (dSourceSpares == null) {
            showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "40140404", "UPP40140404-000043")/* @res "订单已改变。请重查！" */);
            return;
          }
          UFBoolean isClosed = (UFBoolean) sourceInfos[i]
              .getAttributeValue("orderstatus");

          // 剩余数量
          dsparenum = dSourceSpares.sub(ordervos[i]
              .getAttributeValue("ndelivernum") == null ? new UFDouble(0)
              : new UFDouble(ordervos[i].getAttributeValue("ndelivernum")
                  .toString()));

          savevos[i].setAttributeValue("sparenum", dsparenum);

          if (dsparenum.doubleValue() <= 0 || isClosed.booleanValue() == true)
            savevos[i].setAttributeValue("orderstatus", new UFBoolean(true));
          else
            savevos[i].setAttributeValue("orderstatus", new UFBoolean(false));
        }
        // 选择关闭的订单
        if (!getChooseCloseOrderDlg(savevos))
          return;
        // 置订单是否关闭
        setIfCloseOrder(ordervos);

        // 填入新增点击时间
        if (getEditFlag() == DMBillStatus.CardNew) {
          for (int i = 0; i < savevos.length; i++) {
            if (null == m_ufdtAddTime) {
              showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                  "40140404", "UPP40140404-000044")/* @res "未能获得新增单据时的 时间!" */);
              return;
            }
            savevos[i].setAttributeValue("billnewaddtime", m_ufdtAddTime);
          }
          // m_ufdtAddTime = null;
        }

        DMDataVO[] retVos = null;
        int iATPYN = UIDialog.ID_YES;
        try {
          for (int i = 0; i < savevos.length; i++) {
            savevos[i].setAttributeValue("ischeckatp", "Y");
          }
          // 保存日计划
          retVos = DeliverydailyplanBO_Client.saveAndWrite(ordervos, savevos);
        }
        catch (ATPNotEnoughException ane) {
        	if (ane.getHint() == null) 
        	{
        		showErrorMessage(ane.getMessage());
				return;
        	}
        	else
        	{
        		iATPYN = showYesNoMessage(ane.getMessage());
        		if (iATPYN == UIDialog.ID_YES) 
        		{
        			for (int i = 0; i < savevos.length; i++) {
        				savevos[i].setAttributeValue("ischeckatp", "N");
        			}
        			retVos = DeliverydailyplanBO_Client.saveAndWrite(ordervos, savevos);
        		}
        		else
        		{
        			return;
        		}
            }
        }
        // 清空新增点击时间
        if (getEditFlag() == DMBillStatus.CardNew) {
          m_ufdtAddTime = null;
        }
        // 重置回写上游数量
        // if (DM016.booleanValue()) {
        for (int i = 0; i < savevos.length; i++) {
          savevos[i].setAttributeValue("nfeedbacknum", savevos[i]
              .getAttributeValue("dnum"));
        }
        // }

        // 重置界面
        reloadUI(retVos, savevos);
        Object o = getSecBillCardPanel().getHeadItem("vuserdef0").getValue();
        SCMEnv.info("nowecard:" + o);
        // SCMEnv.info("beforesavevo:"+dmvo.getHeaderVO().getAttributeValue("vuserdef0"));
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
            "UPP40140404-000045")/* @res "保存成功！！！" */);
        // /重新选中行
        if (m_selectRow >= 0) {
          getBillListPanel().getHeadTable().getSelectionModel()
              .setSelectionInterval(m_selectRow, m_selectRow);
          BillEditEvent e = new BillEditEvent(
              getBillListPanel().getHeadTable(), 0, m_selectRow);
          bodyRowChange(e);
        }
        SCMEnv.info("nowecard:" + o);
      }
      catch (Exception e) {
        e.printStackTrace();
        if (e instanceof BusinessException)
          showErrorMessage(e.getMessage());
        else
          showErrorMessage(e.getMessage()); // "保存数据失败！");
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
            "UPP40140404-000047")/* @res "保存失败！" */);
      }
    }
  }

  /**
   * 此处插入方法说明。 创建日期：(2002-5-16 11:14:39)
   */
  public void onSaveBatch() {
    try {
      getBatchBillCardPanel().tableStopCellEditing();
      getBatchBillCardPanel().stopEditing();

      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
          "UPP40140404-000042")/* @res "开始保存………………" */);
      DMVO vo = (DMVO) getBatchBillCardPanel().getBillValueChangeVO(
          DMVO.class.getName(), DMDataVO.class.getName(),
          DMDataVO.class.getName());
      DMDataVO[] savevos = vo.getBodyVOs();
      if(savevos==null||savevos.length==0)
      {
          setButton(boEdit, true);
          setButton(boSave, false);
          setButton(boCancel, false);
          setButton(boReturn, true);
          updateButtons();
          getBatchBillCardPanel().setEnabled(false);
    	  showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
          "UPP40140404-000045")/* @res "保存成功！！！" */);
    	  return;
      }
      // savevos = new DMDataVO[m_planvos.length];
      for (int i = 0; i < savevos.length; i++) {
        // savevos[i] = m_planvos[i];
        savevos[i].setAttributeValue("userid", getOperator()); // 操作人
        savevos[i].setAttributeValue("pkdelivorg", getDelivOrgPK()); // 发运组织
        savevos[i].setAttributeValue("thisaction", nc.ui.ml.NCLangRes
            .getInstance().getStrByID("common", "UC001-0000045")/* @res "修改" */);
      }
      // 保存检查
      DMVO dvo = new DMVO();
      dvo.setChildrenVO(savevos);
      if (!checkVO(dvo))
        return;
      // 获得回写定单的数据
      DMDataVO[] ordervos = getWriteOrderData(savevos);
      // 为了回写采购订单，提供修改前的数量
      // 对于删除、新增，oldnum 字段的值在保存时根据vo行标志进行构建
      for (int i = 0; i < savevos.length; i++) {
        if (savevos[i].getAttributeValue("vbilltype").equals("21")) {
          String pkdelivdaypl = (String) savevos[i]
              .getAttributeValue("pk_delivdaypl");
          Object objOldNum = ((DMDataVO) m_allHt.get(pkdelivdaypl))
              .getAttributeValue("dnum");
          savevos[i].setAttributeValue("doldnum", objOldNum);
        }
      }
      DMDataVO[] retVos = null;
      int iATPYN = UIDialog.ID_YES;
      try {
        // 保存日计划
        retVos = DeliverydailyplanBO_Client.saveAndWrite(ordervos, savevos);
      }
      catch (ATPNotEnoughException e) {
        iATPYN = showYesNoMessage(e.getMessage());
        if (iATPYN == UIDialog.ID_YES) {
          retVos = DeliverydailyplanBO_Client.saveAndWrite(ordervos, savevos);
        }
        else
          return;
      }
      // 重置回写上游数量
//      if (DM016.booleanValue()) {
        for (int i = 0; i < savevos.length; i++) {
          savevos[i].setAttributeValue("nfeedbacknum", savevos[i]
              .getAttributeValue("dnum"));
        }
//      }
      // 重置界面
      reloadUI(retVos, savevos);

      setButton(boEdit, true);
      setButton(boSave, false);
      setButton(boCancel, false);
      setButton(boReturn, true);
      updateButtons();
      getBatchBillCardPanel().setEnabled(false);
      getBatchBillCardPanel().updateValue();

      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
          "UPP40140404-000045")/* @res "保存成功！！！" */);
    }
    catch (Exception e) {
      e.printStackTrace();
      if (e instanceof BusinessException)
        showErrorMessage(e.getMessage());
      else
        showErrorMessage(e.getMessage()); // "保存数据失败！");
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
          "UPP40140404-000047")/* @res "保存失败！！！" */);
    }
  }

  /**
   * 全选。 功能： 参数： 返回： 例外： 日期：(2002-9-10 9:44:25) 修改日期，修改人，修改原因，注释标志：
   */
  private void onSelectAll() {
    if (m_strShowState.equals(DMBillStatus.Source)) {
      getThdBillCardPanel().stopEditing();
      int row = getThdBillCardPanel().getBillTable().getRowCount();
      if (row == 0)
        return;
      int col = getThdBillCardPanel().getBillModel().getBodyColByKey("bchoose");
      Boolean b = new Boolean(true);
      for (int i = 0; i < row; i++) {
        getThdBillCardPanel().getBillModel().setValueAt(b, i, col);
      }
    }
    else if (m_strShowState.equals(DMBillStatus.List)) {
      if (getBillListPanel().getChildListPanel() != null
          && getBillListPanel().getChildListPanel().getTable() != null
          && getBillListPanel().getChildListPanel().getTable().getCellEditor() != null)
        getBillListPanel().getChildListPanel().getTable().getCellEditor()
            .stopCellEditing();
      int row = getBillListPanel().getBodyTable().getRowCount();
      if (row == 0)
        return;
      // DMDataVO[] bodyvos= (DMDataVO[])
      // getBillListPanel().getBodyBillModel().getBodyValueVOs(DMDataVO.class.getName());
      int col = getBillListPanel().getBodyBillModel()
          .getBodyColByKey("bchoose");
      Boolean b = new Boolean(true);
      for (int i = 0; i < row; i++) {
        getBillListPanel().getBodyBillModel().setValueAt(b, i, col);
      }
      // setButton(boSelectAll, false);
      // setButton(boUnSelectAll, true);
    }
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-7-18 16:19:50) 修改日期，修改人，修改原因，注释标志：
   */
  private void onSort() {
    if (m_strShowState.equals(DMBillStatus.Source)) {
      // getThdBillCardPanel().onSort("bchoose");
      getThdBillCardPanel().onSort();
    }
  }

  /**
   * 切换到日计划卡片状态 创建日期：(2002-6-20 20:47:09)
   */
  public void onSwitchForm() {
    super.onSwitchForm();
    if (m_planvos != null && m_planvos.length > 0 && m_num >= 0)
      loadCardData();
    getSecBillCardPanel().setEnabled(false);
    setEditFlag(DMBillStatus.CardView);
    switchButtonStatus(DMBillStatus.CardView);
    getSecBillCardPanel().updateValue();
    getSecBillCardPanel().updateUI();
  }

  /**
   * 此处插入方法说明。 创建日期：(2002-6-17 20:37:36)
   */
  private void onUnAudit() {
    if (m_selectRow < 0 || m_headListVOs == null || m_headListVOs.length == 0) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
          "UPP40140404-000048")/* @res "请选择待弃审日计划！" */);
      return;
    }
    // 审批前行，审批后切换至
    // int irow = getBillListPanel().getHeadTable().getSelectedRow();
    String sLastPkbillh = m_headListVOs[m_selectRow].getAttributeValue(
        "pkbillh").toString();
    // 得到界面数据
    Hashtable ht = new Hashtable();
    DMVO dmvo = new DMVO();
    DMDataVO[] dmdvos = (DMDataVO[]) getBillListPanel().getBodyBillModel()
        .getBodyValueVOs(DMDataVO.class.getName());
    // 滤出选择的多行
    ArrayList v = new ArrayList();
    Object[] dsendnum = new Object[dmdvos.length];
    String[] sCode = new String[dmdvos.length];
    for (int i = 0; i < dmdvos.length; i++) {
      Object iplanstatus = dmdvos[i].getAttributeValue("iplanstatus");
    	
      if (dmdvos[i].getAttributeValue("bchoose") != null
          && dmdvos[i].getAttributeValue("bchoose").toString().equals("Y")
          && iplanstatus != null && iplanstatus.toString().length() > 0
    	  && new Integer(iplanstatus.toString()).intValue() == DailyPlanStatus.Audit) {
        dsendnum[i] = ((dmdvos[i].getAttributeValue("doutnum") == null || ((UFDouble) dmdvos[i]
            .getAttributeValue("doutnum")).doubleValue() == 0) ? dmdvos[i]
            .getAttributeValue("dsendnum") : dmdvos[i]
            .getAttributeValue("doutnum"));
        sCode[i] = (String) dmdvos[i].getAttributeValue("vdelivdayplcode");
        dmdvos[i].setAttributeValue("iplanstatus", new Integer(
            DailyPlanStatus.Free));
        dmdvos[i].setAttributeValue("pkapprperson", null); // 审核人
        dmdvos[i].setAttributeValue("vapprpersonname", null);
        dmdvos[i].setAttributeValue("apprdate", null);
        dmdvos[i].setAttributeValue("pkcorp", getCorpID());
        dmdvos[i].setAttributeValue("userid", getOperator()); // 操作人
        dmdvos[i].setStatus(VOStatus.UPDATED);
        ht.put("pk_delivdaypl", dmdvos[i].getAttributeValue("pk_delivdaypl"));
        v.add(dmdvos[i]);
      }
    }
    if (v.size() == 0) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
          "UPP40140404-000048")/* @res "请选择待弃审日计划！" */);
      return;
    }
    StringBuffer sb = new StringBuffer("");
    int ooo = 0;
    for (int i = 0; i < dsendnum.length; i++) {
      if (dsendnum[i] == null || ((UFDouble) dsendnum[i]).doubleValue() == 0)
        continue;
      if (ooo != 0) {
        sb.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
            "UPP40140404-000049")/* @res " 、" */
            + sCode[i]);
        ooo++;
      }
      else
        sb.append(sCode[i]);

    }
    if (sb.toString().trim().length() > 0) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
          "UPP40140404-000083", null, new String[] {
            sb.toString()
          })/* @res "单据号为：{0}的日计划已有发运数量或出库数量，不可弃审！！" */);
      return;
    }
    DMDataVO[] savevos = new DMDataVO[v.size()];
    savevos = (DMDataVO[]) v.toArray(savevos);
    v = null;
    try {
      dmvo.setChildrenVO(dmdvos);
      DMDataVO[] retVos = DeliverydailyplanBO_Client.unAudit(savevos);
      // 重置界面
      reloadUI(retVos, savevos);
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH011")/* @res "弃审成功！" */);
    }
    catch (Exception e) {
      e.printStackTrace();
      if (e instanceof BusinessException)
        showErrorMessage(e.getMessage());
      else
        showErrorMessage(e.getMessage());
      // "弃审失败！");
    }
    // 切至选中行
    gotoSelectRow(sLastPkbillh);
  }

  /**
   * 全消。 功能： 参数： 返回： 例外： 日期：(2002-9-10 9:44:58) 修改日期，修改人，修改原因，注释标志：
   */
  private void onUnSelectAll() {
    if (m_strShowState.equals(DMBillStatus.Source)) {
      getThdBillCardPanel().stopEditing();
      int row = getThdBillCardPanel().getBillTable().getRowCount();
      if (row == 0)
        return;
      int col = getThdBillCardPanel().getBillModel().getBodyColByKey("bchoose");
      Boolean b = new Boolean(false);
      for (int i = 0; i < row; i++) {
        getThdBillCardPanel().getBillModel().setValueAt(b, i, col);
      }
    }
    else if (m_strShowState.equals(DMBillStatus.List)) {
      if (getBillListPanel().getChildListPanel() != null
          && getBillListPanel().getChildListPanel().getTable() != null
          && getBillListPanel().getChildListPanel().getTable().getCellEditor() != null)
        getBillListPanel().getChildListPanel().getTable().getCellEditor()
            .stopCellEditing();
      int row = getBillListPanel().getBodyTable().getRowCount();
      if (row == 0)
        return;
      // DMDataVO[] bodyvos= (DMDataVO[])
      // getBillListPanel().getBodyBillModel().getBodyValueVOs(DMDataVO.class.getName());
      int col = getBillListPanel().getBodyBillModel()
          .getBodyColByKey("bchoose");
      Boolean b = new Boolean(false);
      for (int i = 0; i < row; i++) {
        getBillListPanel().getBodyBillModel().setValueAt(b, i, col);
      }
      // setButton(boSelectAll, false);
      // setButton(boUnSelectAll, true);
    }
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-8-6 11:08:35) 修改日期，修改人，修改原因，注释标志：
   */
  private void reloadUI(DMDataVO[] retvos, DMDataVO[] savevos) {
    if (retvos != null && retvos.length != 0) {
      // 重组数据
      DMVO retvo = new DMVO();
      retvo.setChildrenVO(retvos);
      DMVO savevo = new DMVO();
      savevo.setChildrenVO(savevos);
      savevo.combineOtherVO(retvo);
      DMVO dmvo = new DMVO();
      dmvo.setChildrenVO(m_planvos);
      dmvo.combineOtherVOByPK(savevo, "pk_delivdaypl");

      m_planvos = (DMDataVO[]) dmvo.getChildrenVO();

      //
      Object pkdelivdaypl = null;
      for (int i = 0; i < m_planvos.length; i++) {
        pkdelivdaypl = m_planvos[i].getAttributeValue("pk_delivdaypl");
        m_allHt.put(pkdelivdaypl, m_planvos[i]);
      }

      // 重置列表界面
      loadBillDate();

      // 重置源表
      if (getEditFlag() == DMBillStatus.CardNew
          || getEditFlag() == DMBillStatus.CardEdit && m_voCons != null
          && m_voCons.length != 0) {
        ConditionVO[] voCons = new ConditionVO[m_planvos.length];

        // 构造查询条件
        for (int i = 0; i < m_planvos.length; i++) {
          voCons[i] = new ConditionVO();

          if (i == 0)
            voCons[i].setNoLeft(false);
          else
            voCons[i].setNoLeft(true);

          voCons[i].setFieldCode("pkbillb");
          voCons[i].setFieldName(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "40140404", "UPP40140404-000002")/* @res "订单行主键" */);
          voCons[i].setOperaCode("=");
          voCons[i]
              .setValue((String) m_planvos[i].getAttributeValue("pkbillb"));

          if (i != 0)
            voCons[i].setLogic(false);

          if (i == m_planvos.length - 1)
            voCons[i].setNoRight(false);
          else
            voCons[i].setNoRight(true);
        }
        afterOrderQuery(voCons);
      }

      // 重置卡片界面

      if (m_bIsBatch) {
        dmvo.setParentVO(getBillListPanel().getHeadBillModel()
            .getBodyValueRowVO(m_selectRow, DMDataVO.class.getName()));
        getBatchBillCardPanel().setBillValueVO(dmvo);
      }
      else {
        if (m_strShowState == DMBillStatus.Card)
          loadCardData();
      }

      // 重置界面状态
      switchButtonStatus(DMBillStatus.CardView);
      setEditFlag(DMBillStatus.CardView);
      getSecBillCardPanel().setEnabled(false);

    }
  }

  private void setOtherItemEditable(DMDataVO dmdvo) {
	  
    // 对来源单据为采购订单的发运日计划、发运单修改时，发货仓库不能参照录入；
    // 发货库存组织在任何时候一直不能编辑
    // 日计划、发运单审批时不需校验上述两字段是否为空。
    // UFBoolean ufb = (UFBoolean) dmdvo.getAttributeValue("borderreturn");
    String vBillType = (String) dmdvo.getAttributeValue("vbilltype");
    // boolean borderreturn = false;
    // if (ufb != null)
    // borderreturn = ufb.booleanValue();
    // sSourceBillType.equals(nc.vo.so.pub.SOBillType.SaleOrder);
    if (vBillType.equals("21")) {
      getSecBillCardPanel().getHeadItem("pksendstoreorg").setEnabled(false);
      getSecBillCardPanel().getHeadItem("pksendstore").setEnabled(false);
      getSecBillCardPanel().getHeadItem("vbatchcode").setEnabled(false);
      getSecBillCardPanel().getHeadItem("creceiptcorpid").setEnabled(true);
    }
    else {
      getSecBillCardPanel().getHeadItem("pksendstoreorg").setEnabled(true);
      getSecBillCardPanel().getHeadItem("pksendstore").setEnabled(true);
      // getSecBillCardPanel().getHeadItem("vbatchcode").setEnabled(true);
      if (vBillType.equals("30")) {
        getSecBillCardPanel().getHeadItem("creceiptcorpid").setEnabled(true);
      }
      else
        getSecBillCardPanel().getHeadItem("creceiptcorpid").setEnabled(false);
    }
    // 辅单位不可编辑
    getSecBillCardPanel().getHeadItem("pkassistmeasure").setEnabled(false);
  }

  /**
   * 查存货属性 创建日期：(2002-6-11 9:47:30)
   * 
   * @param sInv
   *          java.lang.String[]
   */
  private void setInvItemEditable(DMDataVO dmdvo) {
    String[] invkey = {
      (String) dmdvo.getAttributeValue("pkinv")
    };
    String pkassistmeasure = (String) dmdvo
        .getAttributeValue("pkassistmeasure");
    InvVO voaRetVO[] = getInvInfo(invkey, new String[] {
      pkassistmeasure
    });
    if (voaRetVO != null && voaRetVO.length > 0 && voaRetVO[0] != null) {
      InvVO invvo = voaRetVO[0];
      // 是否辅计量 、辅数量
      Integer isassistunit = (Integer) invvo.getAttributeValue("isAstUOMmgt");
      if (!isassistunit.equals(new Integer(1))) {
        // && dmdvo.getAttributeValue("pkassistmeasure") == null) {
        // getSecBillCardPanel().getHeadItem("pkassistmeasure").setEnabled(false);
        getSecBillCardPanel().getHeadItem("dassistnum").setEnabled(false);
      }
      else {
        // getSecBillCardPanel().getHeadItem("pkassistmeasure").setEnabled(true);
        getSecBillCardPanel().getHeadItem("dassistnum").setEnabled(true);
      } // 批次管理
      Integer isbatch = (Integer) invvo.getAttributeValue("isLotMgt");
      if (null != isbatch && isbatch.intValue() == 1) {
        // && dmdvo.getAttributeValue("vbatchcode") == null) {
        getSecBillCardPanel().getHeadItem("vbatchcode").setEnabled(true);
      }
      else
        getSecBillCardPanel().getHeadItem("vbatchcode").setEnabled(false);
      
    
       //自由项 
       Integer isfreeitem = (Integer)invvo.getAttributeValue("isFreeItemMgt");
       if (null != isfreeitem 
    		   && !isfreeitem.equals(new Integer(1))) 
       {
    	   getSecBillCardPanel().getHeadItem("vfree0").setEnabled(false); 
       } 
       else
       {
    	   getSecBillCardPanel().getHeadItem("vfree0").setEnabled(true);
       }
       
    }
  }

  /**
   * 根据参数改变界面。 创建日期：(2001-9-27 16:13:57)
   */
  private void setBatchCardPanelByPara(BillData bdData) {
    super.setSecCardPanelByPara(bdData);
    try {
      // 改变新参照的长度
      getFreeItemRefPane().setMaxLength(
          bdData.getBodyItem("vfree0").getLength());
      // 设置新的参照，要求指出相应的字段名
      if (null != bdData.getBodyItem("vfree0"))
        bdData.getBodyItem("vfree0").setComponent(getFreeItemRefPane()); // 表体,自由项
    }
    catch (Exception e) {
    }

    try {
      // 改变新参照的长度
      getLotNumbRefPane().setMaxLength(
          bdData.getBodyItem("vbatchcode").getLength());
      // 设置新的参照，要求指出相应的字段名
      if (null != bdData.getBodyItem("vbatchcode"))
        bdData.getBodyItem("vbatchcode").setComponent(getLotNumbRefPane()); // 表体,批次
    }
    catch (Exception e) {
    }

    try {
      // 修改自定义项
      bdData = changeBatchEditBillDataByUserDef(bdData); // , true);
    }
    catch (Exception e) {
    }

    try {
      // 计划发货时间
      UITextField uitfout = (UITextField) ((UIRefPane) bdData.getBodyItem(
          "cplansendtime").getComponent()).getUITextField();
      uitfout.setTextType(UITextType.TextTime);
      DMTextDocument dmtdout = new DMTextDocument(uitfout);
      uitfout.setDocument(dmtdout);
    }
    catch (Exception e) {
    }

    try {
      // 订单要求到货时间
      UITextField uitfout = (UITextField) ((UIRefPane) bdData.getBodyItem(
          "crequiretime").getComponent()).getUITextField();
      uitfout.setTextType(UITextType.TextTime);
      DMTextDocument dmtdout = new DMTextDocument(uitfout);
      uitfout.setDocument(dmtdout);
    }
    catch (Exception e) {
    }

  }

  /**
   * 根据参数改变界面。 创建日期：(2001-9-27 16:13:57)
   */
  protected void setSecCardPanelByPara(BillData bdData) {
    super.setSecCardPanelByPara(bdData);
    try {
      // 改变新参照的长度
      getFreeItemRefPaneHead().setMaxLength(
          bdData.getHeadItem("vfree0").getLength());
      // 设置新的参照，要求指出相应的字段名
      if (null != bdData.getHeadItem("vfree0"))
        bdData.getHeadItem("vfree0").setComponent(getFreeItemRefPaneHead()); // 表体,自由项
    }
    catch (Exception e) {
    }

    try {
      // 改变新参照的长度
      getLotNumbRefPaneHead().setMaxLength(
          bdData.getHeadItem("vbatchcode").getLength());
      // 设置新的参照，要求指出相应的字段名
      if (null != bdData.getHeadItem("vbatchcode"))
        bdData.getHeadItem("vbatchcode").setComponent(getLotNumbRefPaneHead()); // 表体,批次
    }
    catch (Exception e) {
    }

    try {
      // 修改自定义项
      bdData = changeSingleTalbeBillDataByUserDef(bdData, true);
    }
    catch (Exception e) {
    }

    try {
      // 计划发货时间
      UITextField uitfout = (UITextField) ((UIRefPane) bdData.getHeadItem(
          "cplansendtime").getComponent()).getUITextField();
      uitfout.setTextType(UITextType.TextTime);
      DMTextDocument dmtdout = new DMTextDocument(uitfout);
      uitfout.setDocument(dmtdout);
    }
    catch (Exception e) {
    }

    try {
      // 订单要求到货时间
      UITextField uitfout = (UITextField) ((UIRefPane) bdData.getHeadItem(
          "crequiretime").getComponent()).getUITextField();
      uitfout.setTextType(UITextType.TextTime);
      DMTextDocument dmtdout = new DMTextDocument(uitfout);
      uitfout.setDocument(dmtdout);
    }
    catch (Exception e) {
    }

  }

  /**
   * 切换按钮状态 创建日期：(2002-5-27 17:00:05)
   * 
   * @param istatus
   *          int
   */
  public void switchButtonStatus(int status) {
    if (status == DMBillStatus.CardView) // 初始，“保存”或“取消”后进入表单非编辑状态
    {
      setButton(boEdit, true);
      setButton(boSave, false);
      setButton(boDel, true);
      setButton(boCancel, false);
      setButton(boQuery, true);
      setButton(boPrint, true);
      setButton(boPre, true);
      setButton(boNext, true);
      setButton(boFirst, true);
      setButton(boLast, true);
      setButton(boSwith, true);
      setButton(boATP, true);
      setButton(boOrder, true);
      setButton(btnCKKCXX, true);
      setButton(boDocument, true);
    }
    else if (status == DMBillStatus.CardNew) // “表单新增”状态
    {
      setButton(boEdit, false);
      setButton(boSave, true);
      setButton(boCancel, true);
      setButton(boQuery, true);
      setButton(boPrint, false);
      setButton(boPre, true);
      setButton(boNext, true);
      setButton(boFirst, true);
      setButton(boLast, true);
      setButton(boDel, false);
      setButton(boSwith, false);
      setButton(boATP, false);
      setButton(boOrder, false);
      setButton(btnCKKCXX,true); 
      // 编辑状态下，“文档管理”按钮不可用
      setButton(boDocument, false);
    }
    else if (status == DMBillStatus.CardEdit) // “表单编辑”后进入表单状态
    {
      setButton(boEdit, false);
      setButton(boSave, true);
      setButton(boCancel, true);
      setButton(boQuery, true);
      setButton(boPrint, false);
      setButton(boPre, false);
      setButton(boNext, false);
      setButton(boFirst, false);
      setButton(boLast, false);
      setButton(boDel, false);
      setButton(boSwith, false);
      setButton(boATP, false);
      setButton(boOrder, false);
      setButton(btnCKKCXX, true);
      // 编辑状态下，“文档管理”按钮不可用
      setButton(boDocument, false);
    }
    else if (status == DMBillStatus.ListView) // 初始，“保存”或“取消”后进入列表非编辑状态
    {
      setButton(boQuery, true);
      setButton(boPrint, true);
      setButton(boAudit, true);
      setButton(boCancelAudit, true);
      setButton(boEnd, true);
      setButton(boSort, true);
      setButton(boSwith, true);
      setButton(boOrder, true);
      setButton(btnCKKCXX, true);
      setButton(boDocument, true);
    }
    this.setExtendBtnsStat(status);
  }

  /**
   * 切换界面。 创建日期：(2001-10-25 12:34:10)
   */
  protected void switchInterface() {
    removeAll();
    if (m_strShowState.equals(DMBillStatus.List)) {
      if (null != ivjSecBillCardPanel)
        remove(getSecBillCardPanel());
      if (null != ivjThdBillCardPanel)
        remove(getThdBillCardPanel());
      add(getBillListPanel(), "Center");
      // switchButtonStatus(DMBillStatus.ListView);
    }
    else if (m_strShowState.equals(DMBillStatus.Card)) {
      remove(getBillListPanel());
      remove(getThdBillCardPanel());
      add(getSecBillCardPanel(), "Center");
      // switchButtonStatus(DMBillStatus.CardView);
    }
    else if (m_strShowState.equals(DMBillStatus.Source)) {
      remove(getBillListPanel());
      remove(getSecBillCardPanel());
      add(getThdBillCardPanel(), "Center");
      // setTitleText(strTitle3);
    }

    updateUI();
  }

  private ATPForOneInvUI atpDlg; // 可用量

  protected ButtonObject boATP;

  private Hashtable m_allHt = new Hashtable(); // 当前所有日计划

  private DMDataVO[] m_choosevos;

  protected Object m_oSelectHid;

  protected DMDataVO[] m_planvos;

  UFDateTime m_ufdtAddTime = null; // 新增时的当前时间(指中间件时间)

  private double dTransRate; // 换算率: added by zxping

  private ConditionVO[] m_voCons; // 订单查询条件

  // 辅计量处理字段

  private final String pkinv = "pkinv";

  private final String vassistmeaname = "vassistmeaname";

  /**
   * 创建者：余大英 功能：应发辅数量编辑事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
   * 修改日期，修改人，修改原因，注释标志： 辅数量*换算率=数量
   */
  private void afterAstNumEdit(nc.ui.pub.bill.BillEditEvent e) {

    String pkassistmeasure = (String) getSecBillCardPanel().getHeadItem(
        "pkassistmeasure").getValue();
    if (pkassistmeasure == null || pkassistmeasure.trim().length() == 0) {
      showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40140404", "UPP40140404-000051")/* @res "请先输入辅单位！！！" */);
      getSecBillCardPanel().getHeadItem("dassistnum").setValue(null);
      return;
    }

    String sNumItemKey = "dnum";
    String sAstItemKey = "dassistnum";
    if (m_invht == null || getSecBillCardPanel().getHeadItem("pkinv") == null
        || getSecBillCardPanel().getHeadItem(sNumItemKey) == null
        || getSecBillCardPanel().getHeadItem(sAstItemKey) == null) {
      SCMEnv.info("no ht inv");
      return;
    }
    String sInvID = getSecBillCardPanel().getHeadItem("pkinv").getValue();
    String sAstID = getSecBillCardPanel().getHeadItem("pkassistmeasure")
        .getValue();
    if (sInvID == null) {
      SCMEnv.info("no inv id");
      return;
    }
    InvVO invvos[] = (InvVO[]) getInvInfo(new String[] {
      sInvID
    }, new String[] {
      sAstID
    });
    if (invvos == null || invvos.length == 0) {
      SCMEnv.info("no inv vo");
      return;
    }
    InvVO invvo = invvos[0];
    Integer isAstMgt = (Integer) invvo.getIsAstUOMmgt();
    Integer isFixFlag = (Integer) invvo.getIsSolidConvRate();
    if (isAstMgt == null || isAstMgt.intValue() == 0) {
      SCMEnv.info("not ast mgt");
      return;
    }
    // 辅数量为空，返回
    if (getSecBillCardPanel().getHeadItem(sAstItemKey).getValue() == null
        || getSecBillCardPanel().getHeadItem(sAstItemKey).getValue().trim()
            .length() < 1) {
      /** 辅数量清空：清空数量； */
      // 固定换算率清数量返回
      getSecBillCardPanel().getHeadItem(sNumItemKey).setValue(null);
      /** 强制执行表体行，数量列清空后的处理 */
      afterNumEdit(e);
      // }
      return;
    }

    UFDouble ninassistnum = new UFDouble(getSecBillCardPanel().getHeadItem(
        sAstItemKey).getValue().toString().trim());

    // 修改数量
    // the next line is commented by zxping
    // UFDouble hsl= invvo.getHsl();
    // ( (DMBillCardPanel)getBillCardPanel()).getCellOldValueAt()

    // Begin: added by zxping
    // 如果主辅数量都存在，则可以计算换算率
    Object oAstNum = ((DMBillCardPanel) getSecBillCardPanel())
        .getHeaderCellControlAt(sAstItemKey).getOldValue();
    String strNum = ((DMBillCardPanel) getSecBillCardPanel()).getHeadItem(
        sNumItemKey).getValue();

    UFDouble hsl = null;

    // 如果是固定换算率
    if (isFixFlag != null && isFixFlag.intValue() == 1) {
      hsl = invvo.getHsl();
    }
    else if (oAstNum != null && strNum != null
        && oAstNum.toString().trim().length() > 0 && strNum.trim().length() > 0) {
      hsl = (new UFDouble(strNum.toString())).div(new UFDouble(oAstNum
          .toString()));
    }
    else {
      hsl = invvo.getHsl();
    }

    // 如果辅数量不为空,则将其保存，以备此后进行换算率的计算
    String strAstNum = ((DMBillCardPanel) getSecBillCardPanel()).getHeadItem(
        sAstItemKey).getValue();
    if (strAstNum != null && strAstNum.trim().length() > 0) {
      ((DMBillCardPanel) getSecBillCardPanel()).getHeaderCellControlAt(
          sAstItemKey).setOldValue(strAstNum);
    }
    // End: added by zxping

    if (hsl != null && hsl.doubleValue() > 0) {
      UFDouble ninnum = ninassistnum.multiply(hsl);
      getSecBillCardPanel().getHeadItem(sNumItemKey).setValue(ninnum);
      afterNumEdit(e);
    }
    // --------------
  }

  /**
   * 创建者：王乃军 功能：单据编辑事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
   * 修改日期，修改人，修改原因，注释标志：
   */
  private void afterAstUOMEdit(nc.ui.pub.bill.BillEditEvent e) {

    String pkassistmeasure = (String) getSecBillCardPanel().getHeadItem(
        "pkassistmeasure").getValue();
    if (pkassistmeasure == null || pkassistmeasure.trim().length() == 0) {
      getSecBillCardPanel().getHeadItem("dassistnum").setValue(null);
      SCMEnv.info("no assistmeasure");
      return;
    }

    final String sNumItemKey = "dnum";
    final String sAstItemKey = "dassistnum";

    if (getSecBillCardPanel().getHeadItem("pkinv") == null
        || getSecBillCardPanel().getHeadItem(sNumItemKey) == null
        || getSecBillCardPanel().getHeadItem(sAstItemKey) == null) {
      SCMEnv.info("no num inv");
      return;
    }

    int row = e.getRow();
    Object oTemp = null;
    oTemp = getSecBillCardPanel().getHeadItem(pkinv).getValue();
    if (oTemp == null) {
      SCMEnv.info("no inv id");
      return;
    }
    String sInvID = oTemp.toString();
    oTemp = getSecBillCardPanel().getHeadItem("pkassistmeasure").getValue();
    String sAstID = null;
    if (oTemp != null)
      sAstID = oTemp.toString();
    InvVO voaInv[] = getInvInfo(new String[] {
      sInvID
    }, new String[] {
      sAstID
    });
    if (voaInv == null || voaInv.length == 0 || voaInv[0] == null) {
      SCMEnv.info("no inv vo");
      return;
    }
    InvVO invvo = voaInv[0];
    // -------------------------
    Integer isAstMgt = (Integer) invvo.getIsAstUOMmgt(); // 辅计量管理？
    Integer isFixFlag = (Integer) invvo.getIsSolidConvRate(); // 固定换算率
    SCMEnv.info("isFixFlag=" + isFixFlag);

    if (isAstMgt == null || isAstMgt.intValue() == 0) {
      SCMEnv.info("not ast mgt");
      return;
    }

    // 00000000
    if (sAstID == null || sAstID.trim().length() == 0) {
      /**
       * 若清空辅计量：换算率，if 固定换算率： 清空数量，辅数量，应发数量，应发辅数量，金额，计划金额 if
       * 变动换算率：清空数量，应发数量，金额，计划金额
       */
      /** 清空界面和VO中的辅计量名称和ID */
      getSecBillCardPanel().getHeadItem(vassistmeaname).setValue(null);
      getSecBillCardPanel().getHeadItem("pkassistmeasure").setValue(null);
      /** 清空换算率 */
      // 重算存货的换算率
      invvo.setHsl(null);
      // 更新存货vo
      updateInvInfo(invvo);
      return;
    }
    UFDouble hsl = invvo.getHsl();
    // //辅计量单位
    // nc.ui.pub.beans.UIRefPane refCastunit=
    // ((nc.ui.pub.beans.UIRefPane)
    // getSecBillCardPanel().getHeadItem(vassistmeaname).getComponent());

    // String sPK= refCastunit.getRefPK();
    // String sName= refCastunit.getRefName();

    // getSecBillCardPanel().getHeadItem(vassistmeaname).setValue(sName);
    // getSecBillCardPanel().getHeadItem("pkassistmeasure").setValue(sPK);
    // nc.ui.ic.pub.measurerate.InvMeasRate imr= new
    // nc.ui.ic.pub.measurerate.InvMeasRate();
    // nc.ui.pub.beans.UIRefPane refCast=
    // (nc.ui.pub.beans.UIRefPane)
    // getSecBillCardPanel().getHeadItem(vassistmeaname).getComponent();

    // imr.filterMeas(m_sCorpID, sInvID, refCast);

    // nc.vo.bd.b15.MeasureRateVO voMeas= imr.getMeasureRate(sInvID, sPK);
    // if (voMeas != null) {
    // hsl= voMeas.getMainmeasrate();
    // //重算存货的换算率
    // invvo.setHsl(hsl);
    // //更新存货vo
    // updateInvInfo(invvo);
    // }

    //

    UFDouble ninnum = null;
    UFDouble ninassistnum = null;
    Object oinnum = getSecBillCardPanel().getHeadItem(sNumItemKey).getValue();
    Object oinassistnum = getSecBillCardPanel().getHeadItem(sAstItemKey)
        .getValue();
    if (oinnum != null)
      ninnum = new UFDouble(oinnum.toString().trim());
    if (oinassistnum != null)
      ninassistnum = new UFDouble(oinassistnum.toString().trim());

    if ((ninnum == null && ninassistnum == null) || hsl == null)
      // || isFixFlag == null)
      return;

    /* 辅计量：换算率；无论固定，变动换算率，按辅数量＊换算率来重新计算主数量 */
    if (ninassistnum != null) {
      ninnum = ninassistnum.multiply(hsl);
      getSecBillCardPanel().getHeadItem(sNumItemKey).setValue(ninnum);
    }
    else {
      getSecBillCardPanel().getHeadItem(sNumItemKey).setValue(null);
    }
    afterNumEdit(e);

    // 清批次号
    getSecBillCardPanel().getHeadItem("vbatchcode").setValue(null);
    getLotNumbRefPaneHead().setValue(null);
    // 向批次号传递参数
    initHeaderLot(getSecBillCardPanel());
  }

  /**
   * 创建者：余大英 功能：数量改变事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05) 修改日期， 修改人，
   * 修改原因， 注释标志：
   */
  private void afterBatchNumEdit(nc.ui.pub.bill.BillEditEvent e) {

    int row = e.getRow();
    String sNumItemKey = "dnum";
    String sAstItemKey = "dassistnum";

    // 计算重量、体积
    UFDouble dweight = new UFDouble(0);
    UFDouble dvolumn = new UFDouble(0);

    Object onum = getBatchBillCardPanel().getBodyValueAt(row, sNumItemKey);
    UFDouble dnum = (onum == null ? new UFDouble(0) : new UFDouble(onum
        .toString()));

    // String formula =
    // "getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,pkinv)";
    String sPkinv = getBatchBillCardPanel().getBillModel().getValueAt(row,
        "pkinv").toString();
    String formula = "cvs(\"bd_invmandoc\",\"pk_invbasdoc\",\"pk_invmandoc\","
        + "\"" + sPkinv + "\")";
    String pkbasinv = (String) getBatchBillCardPanel().execHeadFormula(formula);

    formula = "cvs(\"bd_invbasdoc\",\"unitweight\",\"pk_invbasdoc\"," + "\""
        + pkbasinv + "\")";
    String unitweight = (String) getBatchBillCardPanel().execHeadFormula(
        formula);

    formula = "cvs(\"bd_invbasdoc\",\"unitvolume\",\"pk_invbasdoc\"," + "\""
        + pkbasinv + "\")";
    String unitvolume = (String) getBatchBillCardPanel().execHeadFormula(
        formula);

    if (unitweight == null || unitweight.trim().length() == 0)
      getBatchBillCardPanel().setBodyValueAt(dweight, row, "dweight");
    else
      getBatchBillCardPanel().setBodyValueAt(
          dnum.multiply(new UFDouble(unitweight)), row, "dweight");

    if (unitvolume == null || unitvolume.trim().length() == 0)
      getBatchBillCardPanel().setBodyValueAt(dvolumn, row, "dvolumn");
    else
      getBatchBillCardPanel().setBodyValueAt(
          dnum.multiply(new UFDouble(unitvolume)), row, "dvolumn");

    // Modified by xhq 2002/10/07 begin
    // 数量变化，金额也变化
    UFDouble nInvNum = null; // 数量
    UFDouble nUnitPrice = null; // 单价
    Object oObject = getBatchBillCardPanel().getBodyValueAt(row, sNumItemKey);
    if (oObject != null && oObject.toString().trim().length() > 0) {
      // 数量不为空
      nInvNum = new UFDouble(oObject.toString());
      oObject = getBatchBillCardPanel().getBodyValueAt(row, "dunitprice");
      if (oObject != null && oObject.toString().trim().length() > 0) {
        // 单价不为空
        nUnitPrice = new UFDouble(oObject.toString());
        double nmny = nInvNum.doubleValue() * nUnitPrice.doubleValue();
        getBatchBillCardPanel().setBodyValueAt(new UFDouble(nmny), row,
            "dmoney");
      }
      else {
        // 单价为空
        oObject = getBatchBillCardPanel().getBodyValueAt(row, "dmoney");
        if (oObject != null && oObject.toString().trim().length() > 0) {
          // 金额不为空，计算单价
          UFDouble nMoney = new UFDouble(oObject.toString());
          if (nInvNum.doubleValue() == 0) {
            // 数量为0，则金额为空
            getBatchBillCardPanel().setBodyValueAt(null, row, "dmoney");
          }
          else {
            // 数量不为0
            double nprice = nMoney.doubleValue() / nInvNum.doubleValue();
            getBatchBillCardPanel().setBodyValueAt(new UFDouble(nprice), row,
                "dunitprice");
          }
        }
        else {
          // 金额为空
        }
      }
    }
    else {
      // 数量为空，则金额为空
      getBatchBillCardPanel().setBodyValueAt(null, row, "dmoney");
    }
    // Modified by xhq 2002/10/07 end

    Object oTemp = null;
    oTemp = getBatchBillCardPanel().getBodyValueAt(row, pkinv);
    if (oTemp == null) {
      SCMEnv.info("no inv id");
      return;
    }
    String sInvID = oTemp.toString();
    // oTemp = getBatchBillCardPanel().getBodyValueAt(row, pkassistmeasure);
    oTemp = getBatchBillCardPanel().getBodyValueAt(row, "pkassistmeasure");
    String sAstID = null;
    if (oTemp != null)
      sAstID = oTemp.toString();
    InvVO voaInv[] = getInvInfo(new String[] {
      sInvID
    }, new String[] {
      sAstID
    });
    if (voaInv == null || voaInv.length == 0 || voaInv[0] == null) {
      SCMEnv.info("no inv vo");
      return;
    }
    InvVO invvo = voaInv[0];
    // -------------------------
    Integer isAstMgt = (Integer) invvo.getIsAstUOMmgt(); // 辅计量管理？
    Integer isFixFlag = (Integer) invvo.getIsSolidConvRate(); // 固定换算率
    UFDouble hsl = invvo.getHsl();
    if (isAstMgt == null || isAstMgt.intValue() == 0) {
      SCMEnv.info("not ast mgt");
      return;
    }

    /** 数量清空：if 固定换算率：清空辅数量。 */
    Object oNumValue = getBatchBillCardPanel().getBodyValueAt(row, sNumItemKey);
    if (oNumValue == null || oNumValue.toString().trim().length() < 1) {
      getBatchBillCardPanel().setBodyValueAt(null, row, sNumItemKey);
      // 固定换算率清辅数量返回
      if (isFixFlag != null && isFixFlag.intValue() == 1) {
        getBatchBillCardPanel().setBodyValueAt(null, row, sAstItemKey);
      }
      /** If 变动换算率，清空换算率 */
      if (isFixFlag != null && isFixFlag.intValue() == 0) {
        // 重算存货的换算率
        invvo.setHsl(null);
        // 更新存货vo
        updateInvInfo(invvo);
      }
      return;
    }
    UFDouble nNum = new UFDouble(oNumValue.toString().trim());

    if (isAstMgt == null || isAstMgt.intValue() == 0 || isFixFlag == null)
      return;
    /** if 为 固定换算率，数量修改，则重算辅数量。 */

    // //如果是固定换算率或辅数量为空，推算辅数量
    if (isFixFlag.intValue() == 1
        || getBatchBillCardPanel().getBodyValueAt(row, sAstItemKey) == null) {

      if (hsl != null && hsl.doubleValue() != 0.0) {
        UFDouble ninassistnum = nNum.div(hsl);
        getBatchBillCardPanel().setBodyValueAt(ninassistnum, row, sAstItemKey);
      }

    }
    /* If 为变动换算率，数量修改，不影响辅数量，但要重算换算率 */
    if (isFixFlag.intValue() == 0
        && getBatchBillCardPanel().getBodyValueAt(row, sAstItemKey) != null) {
      Object oAstNum = getBatchBillCardPanel().getBodyValueAt(row, sAstItemKey);
      UFDouble nastnum = new UFDouble(0);
      if (null != oAstNum && oAstNum.toString().trim().length() != 0) {
        nastnum = (UFDouble) oAstNum;
      }
      else {
      }

      if (nNum != null && nastnum.doubleValue() != 0.0) {
        UFDouble dhsl = nNum.div(nastnum);

        // added by zxping
        // dTransRate = dhsl.doubleValue();

        if (dhsl != null && dhsl.doubleValue() >= 0) {
          // 重算存货的换算率
          invvo.setHsl(dhsl);
          // 更新存货vo
          updateInvInfo(invvo);
        }
        else {
          getBatchBillCardPanel().setBodyValueAt(null, row, sNumItemKey);
        }
      }
    }
    // 主数量换算报价单位数量
    Object oQuoteunitRate = null;
    if (getBatchBillCardPanel().getBillModel()
        .getValueAt(row, "nquoteunitrate") != null) {
      oQuoteunitRate = getBatchBillCardPanel().getBillModel().getValueAt(row,
          "nquoteunitrate");
    }
    if (oQuoteunitRate == null || oQuoteunitRate.toString().trim().length() < 1) {
      UFDouble ufdQuoteunitRate = new UFDouble(oQuoteunitRate.toString().trim());
      UFDouble ufdPricenum = nNum.div(ufdQuoteunitRate);
      getBatchBillCardPanel().setBodyValueAt(ufdQuoteunitRate, row,
          "nquoteunitnum");
    }

  }

  /**
   * 创建者：余大英 功能：数量改变事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
   * 修改日期，修改人，修改原因，注释标志：
   */
  private void afterNumEdit(nc.ui.pub.bill.BillEditEvent e) {
    // 计算重量、体积
    UFDouble dweight = new UFDouble(0);
    UFDouble dvolumn = new UFDouble(0);

    Object onum = getSecBillCardPanel().getHeadItem("dnum").getValue();
    UFDouble dnum = (onum == null ? new UFDouble(0) : new UFDouble(onum
        .toString()));

    // String formula=
    // "cvs(\"bd_invmandoc\",\"pk_invbasdoc\",\"pk_invmandoc\"," +
    // "\"pkinv\")";
    String formula = "getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,pkinv)";
    String pkbasinv = (String) getSecBillCardPanel().execHeadFormula(formula);

    formula = "cvs(\"bd_invbasdoc\",\"unitweight\",\"pk_invbasdoc\"," + "\""
        + pkbasinv + "\")";
    // formula= "getColValue(bd_invbasdoc,unitweight,pk_invbasdoc," + "\"" +
    // pkbasinv + "\")";
    String unitweight = (String) getSecBillCardPanel().execHeadFormula(formula);

    formula = "cvs(\"bd_invbasdoc\",\"unitvolume\",\"pk_invbasdoc\"," + "\""
        + pkbasinv + "\")";
    // formula= "getColValue(bd_invbasdoc,unitvolume,pk_invbasdoc," + "\"" +
    // pkbasinv + "\")";
    String unitvolume = (String) getSecBillCardPanel().execHeadFormula(formula);

    if (unitweight == null || unitweight.trim().length() == 0)
      getSecBillCardPanel().setHeadItem("dweight", dweight);
    else
      getSecBillCardPanel().setHeadItem("dweight",
          dnum.multiply(new UFDouble(unitweight)));

    if (unitvolume == null || unitvolume.trim().length() == 0)
      getSecBillCardPanel().setHeadItem("dvolumn", dvolumn);
    else
      getSecBillCardPanel().setHeadItem("dvolumn",
          dnum.multiply(new UFDouble(unitvolume)));

    // 主辅数量换算
    String sNumItemKey = "dnum";
    String sAstItemKey = "dassistnum";
    if (m_invht == null || getSecBillCardPanel().getHeadItem("pkinv") == null
        || getSecBillCardPanel().getHeadItem(sNumItemKey) == null
        || getSecBillCardPanel().getHeadItem(sAstItemKey) == null) {
      SCMEnv.info("no ht inv");
      return;
    }
    String sInvID = getSecBillCardPanel().getHeadItem("pkinv").getValue();
    String sAstID = getSecBillCardPanel().getHeadItem("pkassistmeasure")
        .getValue();
    if (sInvID == null) {
      SCMEnv.info("no inv id");
      return;
    }
    InvVO invvos[] = (InvVO[]) getInvInfo(new String[] {
      sInvID
    }, new String[] {
      sAstID
    });
    if (invvos == null || invvos.length == 0) {
      SCMEnv.info("no inv vo");
      return;
    }
    InvVO invvo = invvos[0];
    if (invvo == null) {
      SCMEnv.info("no inv vo");
      return;
    }
    Integer isAstMgt = (Integer) invvo.getIsAstUOMmgt();
    Integer isFixFlag = (Integer) invvo.getIsSolidConvRate();
    if (isAstMgt == null || isAstMgt.intValue() == 0) {
      SCMEnv.info("not ast mgt");
      return;
    }
    /** 数量清空：if 固定换算率：清空辅数量。 */
    Object oNumValue = getSecBillCardPanel().getHeadItem(sNumItemKey)
        .getValue();
    if (oNumValue == null || oNumValue.toString().trim().length() < 1) {
      getSecBillCardPanel().getHeadItem(sNumItemKey).setValue(null);
      // 固定换算率清辅数量返回
      if (isFixFlag != null && isFixFlag.intValue() == 1) {
        getSecBillCardPanel().getHeadItem(sAstItemKey).setValue(null);
      }
      /** If 变动换算率，清空换算率 */
      if (isFixFlag != null && isFixFlag.intValue() == 0) {
        // 重算存货的换算率
        invvo.setHsl(null);
        // 更新存货vo
        m_invht.remove(sInvID);
        m_invht.put(sInvID, invvo);
      }
      return;
    }
    UFDouble nNum = new UFDouble(oNumValue.toString().trim());

    /** if 为 固定换算率，数量修改，则重算辅数量。 */

    // 如果是固定换算率或辅数量为空，推算辅数量
    if (isFixFlag != null && isFixFlag.intValue() == 1
        || getSecBillCardPanel().getHeadItem(sAstItemKey).getValue() == null) {
      UFDouble hsl = invvo.getHsl();
      if (hsl != null && hsl.doubleValue() != 0.0) {
        UFDouble ninassistnum = nNum.div(hsl);
        getSecBillCardPanel().getHeadItem(sAstItemKey).setValue(ninassistnum);
      }
    }

    /* If 为变动换算率，数量修改，不影响辅数量，但要重算换算率 */
    if (isFixFlag != null && isFixFlag.intValue() == 0
        && getSecBillCardPanel().getHeadItem(sAstItemKey).getValue() != null) {
      UFDouble nastnum = new UFDouble(getSecBillCardPanel().getHeadItem(
          sAstItemKey).getValue());
      if (nNum != null && nastnum.doubleValue() != 0.0) {
        UFDouble dhsl = nNum.div(nastnum);
        if (dhsl.doubleValue() >= 0) {
          // 重算存货的换算率
          invvo.setHsl(dhsl);
          // 更新存货vo
          m_invht.remove(sInvID);
          m_invht.put(sInvID, invvo);
        }
        else {
          getSecBillCardPanel().getHeadItem(sNumItemKey).setValue(null);
        }

      }
    }

    // 主数量换算报价单位数量
    Object oQuoteunitRate = null;
    if (getSecBillCardPanel().getHeadItem("nquoteunitrate") != null) {
      oQuoteunitRate = getSecBillCardPanel().getHeadItem("nquoteunitrate")
          .getValue();
    }
    if (oQuoteunitRate == null || oQuoteunitRate.toString().trim().length() < 1) {
      UFDouble ufdQuoteunitRate = new UFDouble(oQuoteunitRate.toString().trim());
      UFDouble ufdPricenum = nNum.div(ufdQuoteunitRate);
      getSecBillCardPanel().getHeadItem("nquoteunitnum").setValue(ufdPricenum);
    }
  }

  /**
   * 收货单位编辑后事件处理。 功能： 参数： 返回： 例外： 日期：(2002-10-14 20:14:12) 修改日期，修改人，修改原因，注释标志：
   */
  private void afterReceiptCorp() {
    // 订单类型
    String vbilltype = getSecBillCardPanel().getHeadItem("vbilltype")
        .getValue();

    // 收货地址
    UIRefPane vdestaddressRef = (UIRefPane) getSecBillCardPanel().getHeadItem(
        "vdestaddress").getComponent();
    vdestaddressRef.setAutoCheck(false);

    // 收货地址参照(对于销售订单、调拨订单)
    // if (vbilltype.equals("30"))
    ((nc.ui.scm.ref.prm.CustAddrRefModel) vdestaddressRef.getRefModel())
        .setCustId(getSecBillCardPanel().getHeadItem("creceiptcorpid")
            .getValue());
    // else if (vbilltype.equals("4U"))
    // ((nc.ui.prm.ref.CustAddrRefModel)
    // vdestaddressRef.getRefModel()).setWherePart(" 0>1 ");

    // 基础档案ID
    String formula = "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcorpid)";
    String pk_cubasdoc = (String) getSecBillCardPanel()
        .execHeadFormula(formula);

    if (vbilltype.equals("30")) {
      // 收货地址
      formula =
      // "fetchValue(\"bd_custaddr\",\"pk_custaddr\"," + "\" defaddrflag =
      // 'Y' and pk_cubasdoc='" + pk_cubasdoc + "'\")";
      formula = "getColValue2(\"bd_custaddr\",\"pk_custaddr\",\"defaddrflag\",\"Y\",\"pk_cubasdoc\",\""
          + pk_cubasdoc + "\")";
      String vdestaddress = (String) getSecBillCardPanel().execHeadFormula(
          formula);
      vdestaddressRef.setPK(vdestaddress);

      // 到货地区
      UIRefPane arriveareaRef = (UIRefPane) getSecBillCardPanel().getHeadItem(
          "pkarrivearea").getComponent();
      // formula= "getColValue(bd_cubasdoc,pk_areacl,pk_cubasdoc," + "\""
      // + pk_cubasdoc + "\")";
      // String pkarrivearea= (String)
      // getSecBillCardPanel().execHeadFormula(formula);
      String pkarrivearea = (String) vdestaddressRef
          .getRefValue("bd_custaddr.pk_areacl");
      arriveareaRef.setPK(pkarrivearea);
    }
  }

  /**
   * 实际发货日期。 功能： 参数： 返回： 例外： 日期：(2002-9-11 15:08:43) 修改日期，修改人，修改原因，注释标志：
   */
  private void afterSendStoreOrg() {
    String pkcorp = getSecBillCardPanel().getHeadItem("pksalecorp").getValue();
    /** 重置发货库存组织名称 */
    UIRefPane sendstoreOrgRef = (UIRefPane) getSecBillCardPanel().getHeadItem(
        "pksendstoreorg").getComponent();
    getSecBillCardPanel().getHeadItem("vsendstoreorgname").setValue(
        sendstoreOrgRef.getText());

    /** 根据发货库存组织过滤发货仓库：非废品、未封存 */
    UIRefPane sendstoreRef = (UIRefPane) getSecBillCardPanel().getHeadItem(
        "pksendstore").getComponent();
    sendstoreRef
        .setWhereString(" pk_calbody ='" + sendstoreOrgRef.getRefPK()
            + "' and gubflag = 'N' and sealflag ='N' and pk_corp ='" + pkcorp
            + "'");

    //
    // 基础档案ID
    String[] formulas = {
        "pksendarea->getColValue(bd_calbody,pk_areacl,pk_calbody,pksendstoreorg)",
        "vsendarea->getColValue(bd_areacl,areaclname,pk_areacl,pksendarea)",
        "vsendaddr->getColValue(bd_calbody,area,pk_calbody,pksendstoreorg)"
    };
    getSecBillCardPanel().execHeadFormulas(formulas);
    //

    /** 重置实际发货日期 */
    // 要求到货日期
    Object oTemp =  getSecBillCardPanel().getHeadItem("requiredate").getValueObject();
    UFDate requiredate = null;
    if(oTemp != null && oTemp.toString().length() > 0)
    {
        requiredate = new UFDate(oTemp.toString());
    }
    // 发货库存组织
    String pksendstoreorg = (String) getSecBillCardPanel().getHeadItem(
        "pksendstoreorg").getValue();
    // 到货地区
    String pkarrivearea = (String) getSecBillCardPanel().getHeadItem(
        "pkarrivearea").getValue();
    // 发运方式
    String pksendtype = (String) getSecBillCardPanel()
        .getHeadItem("pksendtype").getValue();
    try {
      UFDate retdate = DeliverydailyplanBO_Client.getOtherDateByAheadPeriod(
          pksendtype, pksendstoreorg, pkarrivearea, null, requiredate);
      if (retdate != null)
        getSecBillCardPanel().getHeadItem("snddate").setValue(retdate);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 实际发货日期。 功能： 参数： 返回： 例外： 日期：(2002-9-11 15:08:43) 修改日期，修改人，修改原因，注释标志：
   */
  private void afterSendType() {
    /** 重置发运方式名称 */
    UIRefPane sendTypeRef = (UIRefPane) getSecBillCardPanel().getHeadItem(
        "pksendtype").getComponent();
    getSecBillCardPanel().getHeadItem("vsendtypename").setValue(
        sendTypeRef.getText());
    /** ================================ */

    // 计划日期
    UFDate snddate = new UFDate(getSecBillCardPanel().getHeadItem("snddate")
        .getValue());
    // 要求到货日期
    UFDate requiredate = new UFDate(getSecBillCardPanel().getHeadItem(
        "requiredate").getValue());
    // 发货库存组织
    String pksendstoreorg = (String) getSecBillCardPanel().getHeadItem(
        "pksendstoreorg").getValue();
    // 到货地区
    String pkarrivearea = (String) getSecBillCardPanel().getHeadItem(
        "pkarrivearea").getValue();
    // 发运方式
    String pksendtype = (String) getSecBillCardPanel()
        .getHeadItem("pksendtype").getValue();
    try {
      UFDate retdate = DeliverydailyplanBO_Client.getOtherDateByAheadPeriod(
          pksendtype, pksendstoreorg, pkarrivearea, null, requiredate);
      if (retdate != null)
        getSecBillCardPanel().getHeadItem("snddate").setValue(retdate);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 创建者：仲瑞庆 功能： 参数： 返回： 例外： 日期：(2001-11-8 19:47:29) 修改日期，修改人，修改原因，注释标志：
   * 
   * @return nc.ui.pub.bill.BillData
   * @param oldBillData
   *          nc.ui.pub.bill.BillData
   */
  protected BillListData changeBillListDataByUserDef(BillListData billData) {
    // 进行自定义项定义用
    DefVO[] defsHead = getDefVOsH();
    DefVO[] defsBody = getDefVOsB();

    // int iHeadDefSize = 0;
    // int iBodyDefSize = 0;
    // if ((defsHead != null)) {
    // iHeadDefSize = defsHead.length;
    // }
    // if ((defsBody != null)) {
    // iBodyDefSize = defsBody.length;
    // }
    // DefVO[] defs = new DefVO[iHeadDefSize + iBodyDefSize];
    //
    // //表头
    // //查得对应于公司的该单据的自定义项设置
    // if ((defsHead != null)) {
    // for (int i = 0; i < iHeadDefSize; i++) {
    // defs[i] = defsHead[i];
    // }
    // }
    // //表体
    // //查得对应于公司的该单据的自定义项设置
    // if ((defsBody != null)) {
    // for (int i = 0; i < iBodyDefSize; i++) {
    // defs[i + iHeadDefSize] = defsBody[i];
    // }
    // }

    // updateItemByDef(billData, defsHead, "vuserdef", true, 0);//表头加上自定义项显示
    //
    // //日计划列表表体的自定义项包含了源订单表头的20个自定义项，还有源订单表体的20个子定义项
    // updateItemByDef(billData, defs, "vuserdef", false, 0);
    // updateItemByDef(billData, defs, "vuserdef_b_", false, 0);

    //updateItemByDef(billData, defsHead, "vuserdef", true, 0);// 表头加上自定义项显示

    // 日计划列表表体的自定义项包含了源订单表头的20个自定义项，还有源订单表体的20个自定义项
    updateItemByDef(billData, defsHead, "vuserdef", false, 0);
    updateItemByDef(billData, defsBody, "vuserdef_b_", false, 0);

    return billData;
  }

  /**
   * 改变模板数据的显示位置-所有数据项显示在表头 功能： 参数： 返回： 例外： 日期：(2002-10-7 19:11:02)
   * 修改日期，修改人，修改原因，注释标志：
   */
  private void changeBatchBillTempletVO(BillTempletVO btvo) {
    Boolean ifEditable = null;
    BillTempletBodyVO[] bodyvos = btvo.getBodyVO();
    Integer iShoworder = new Integer(0);
    ArrayList alAllTmpVO = new ArrayList();
    for (int i = 0; i < bodyvos.length; i++) {
      BillTempletBodyVO tempHeadVO;
      if (bodyvos[i].getItemkey().equals("bchoose")) {
        bodyvos[i].setShowflag(new Boolean(false));
      }
      // 发运方式
      else if (bodyvos[i].getItemkey().equals("vsendtypename")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        bodyvos[i].setDatatype(new Integer(5));
        bodyvos[i].setReftype("发运方式");
        bodyvos[i].setIdcolname("pksendtype");
        iShoworder = bodyvos[i].getShoworder();
        ifEditable = bodyvos[i].getEditflag();
        for (int j = 0; j < bodyvos.length; j++) {
          if (bodyvos[j].getItemkey().equals("pksendtype")) {
            bodyvos[j].setShowflag(new Boolean(false));
            tempHeadVO = new BillTempletBodyVO();
            tempHeadVO = (BillTempletBodyVO) bodyvos[j].clone();
            tempHeadVO.setShowflag(new Boolean(true));
            tempHeadVO.setDatatype(new Integer(5));
            tempHeadVO.setReftype("发运方式");
            tempHeadVO.setEditflag(ifEditable);
            tempHeadVO.setShoworder(iShoworder);
            tempHeadVO.setPos(new Integer(0));
            tempHeadVO.setWidth(new Integer(
                bodyvos[j].getWidth().intValue() / 80));
            alAllTmpVO.add(tempHeadVO);
            break;
          }
        }
      }
      // 收货单位
      else if (bodyvos[i].getItemkey().equals("creceiptcorpname")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        // bodyvos[i].setShowflag(new Boolean(true));
        bodyvos[i].setDatatype(new Integer(5));
        bodyvos[i].setReftype("客户档案");
        bodyvos[i].setIdcolname("creceiptcorpid");
        iShoworder = bodyvos[i].getShoworder();
        ifEditable = bodyvos[i].getEditflag();
        for (int j = 0; j < bodyvos.length; j++) {
          if (bodyvos[j].getItemkey().equals("creceiptcorpid")) {
            bodyvos[j].setShowflag(new Boolean(false));
            tempHeadVO = new BillTempletBodyVO();
            tempHeadVO = (BillTempletBodyVO) bodyvos[j].clone();
            tempHeadVO.setShowflag(new Boolean(false));
            tempHeadVO.setDatatype(new Integer(5));
            tempHeadVO.setReftype("客户档案");
            tempHeadVO.setEditflag(ifEditable);
            tempHeadVO.setShoworder(iShoworder);
            tempHeadVO.setPos(new Integer(0));
            tempHeadVO.setWidth(new Integer(
                bodyvos[j].getWidth().intValue() / 80));
            alAllTmpVO.add(tempHeadVO);
            break;
          }
        }
      }
      // 发货库存组织
      else if (bodyvos[i].getItemkey().equals("vsendstoreorgname")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        // bodyvos[i].setShowflag(new Boolean(true));
        bodyvos[i].setDatatype(new Integer(5));
        bodyvos[i].setReftype("库存组织");
        bodyvos[i].setIdcolname("pksendstoreorg");
        ifEditable = bodyvos[i].getEditflag();
        iShoworder = bodyvos[i].getShoworder();
        for (int j = 0; j < bodyvos.length; j++) {
          if (bodyvos[j].getItemkey().equals("pksendstoreorg")) {
            bodyvos[j].setShowflag(new Boolean(false));
            tempHeadVO = new BillTempletBodyVO();
            tempHeadVO = (BillTempletBodyVO) bodyvos[j].clone();
            tempHeadVO.setShoworder(iShoworder);
            tempHeadVO.setShowflag(new Boolean(false));
            tempHeadVO.setDatatype(new Integer(5));
            tempHeadVO.setReftype("库存组织");
            tempHeadVO.setEditflag(ifEditable);
            tempHeadVO.setPos(new Integer(0));
            tempHeadVO.setWidth(new Integer(
                bodyvos[j].getWidth().intValue() / 80));
            alAllTmpVO.add(tempHeadVO);
            break;
          }
        }
      }
      // 发货仓库
      else if (bodyvos[i].getItemkey().equals("vsendstorename")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        bodyvos[i].setShowflag(new Boolean(true));
        bodyvos[i].setDatatype(new Integer(5));
        bodyvos[i].setReftype("仓库档案");
        bodyvos[i].setIdcolname("pksendstore");
        ifEditable = bodyvos[i].getEditflag();
        iShoworder = bodyvos[i].getShoworder();
        for (int j = 0; j < bodyvos.length; j++) {
          if (bodyvos[j].getItemkey().equals("pksendstore")) {
            bodyvos[j].setShowflag(new Boolean(false));
            tempHeadVO = new BillTempletBodyVO();
            tempHeadVO = (BillTempletBodyVO) bodyvos[j].clone();
            tempHeadVO.setShoworder(iShoworder);
            tempHeadVO.setShowflag(new Boolean(false));
            tempHeadVO.setDatatype(new Integer(5));
            tempHeadVO.setReftype("仓库档案");
            tempHeadVO.setEditflag(ifEditable);
            tempHeadVO.setPos(new Integer(0));
            tempHeadVO.setWidth(new Integer(
                bodyvos[j].getWidth().intValue() / 80));
            alAllTmpVO.add(tempHeadVO);
            break;
          }
        }
      }
      // 发货地区
      else if (bodyvos[i].getItemkey().equals("vsendarea")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        // bodyvos[i].setShowflag(new Boolean(true));
        bodyvos[i].setDatatype(new Integer(5));
        bodyvos[i].setReftype("地区分类");
        bodyvos[i].setIdcolname("pksendarea");
        ifEditable = bodyvos[i].getEditflag();
        iShoworder = bodyvos[i].getShoworder();
        for (int j = 0; j < bodyvos.length; j++) {
          if (bodyvos[j].getItemkey().equals("pksendarea")) {
            bodyvos[j].setShowflag(new Boolean(false));
            tempHeadVO = new BillTempletBodyVO();
            tempHeadVO = (BillTempletBodyVO) bodyvos[j].clone();
            tempHeadVO.setShoworder(iShoworder);
            tempHeadVO.setShowflag(new Boolean(false));
            tempHeadVO.setDatatype(new Integer(5));
            tempHeadVO.setReftype("地区分类");
            tempHeadVO.setEditflag(ifEditable);
            tempHeadVO.setPos(new Integer(0));
            tempHeadVO.setWidth(new Integer(
                bodyvos[j].getWidth().intValue() / 80));
            alAllTmpVO.add(tempHeadVO);
            break;
          }
        }
      }
      // 到货仓库
      else if (bodyvos[i].getItemkey().equals("vdeststorename")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        // bodyvos[i].setShowflag(new Boolean(true));
        bodyvos[i].setDatatype(new Integer(5));
        bodyvos[i].setReftype("仓库档案");
        bodyvos[i].setIdcolname("pkdeststore");
        ifEditable = bodyvos[i].getEditflag();
        iShoworder = bodyvos[i].getShoworder();
        for (int j = 0; j < bodyvos.length; j++) {
          if (bodyvos[j].getItemkey().equals("pkdeststore")) {
            bodyvos[j].setShowflag(new Boolean(false));
            /*
             * tempHeadVO = new BillTempletBodyVO(); tempHeadVO =
             * (BillTempletBodyVO) bodyvos[j].clone();
             * tempHeadVO.setShoworder(iShoworder); tempHeadVO.setShowflag(new
             * Boolean(false)); tempHeadVO.setDatatype(new Integer(5));
             * tempHeadVO.setReftype("仓库档案");
             * tempHeadVO.setEditflag(ifEditable); tempHeadVO.setPos(new
             * Integer(0)); tempHeadVO.setWidth(new
             * Integer(bodyvos[j].getWidth().intValue() / 80));
             * alAllTmpVO.add(tempHeadVO);
             */
            break;
            // the next line added by zxping
            // bodyvos[j].setEditformula("vdeststoreaddre->getColValue(bd_stordoc,storaddr,pk_stordoc,pkdeststore)");
          }
        }
      }
      // 到货地址
      else if (bodyvos[i].getItemkey().equals("vdestaddress")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        bodyvos[i].setDatatype(new Integer(5));
        bodyvos[i].setReftype("<nc.ui.scm.ref.prm.CustAddrRefModel>");
        bodyvos[i].setPos(new Integer(0));
        bodyvos[i].setWidth(new Integer(bodyvos[i].getWidth().intValue() / 80));
        tempHeadVO = new BillTempletBodyVO();
        tempHeadVO = (BillTempletBodyVO) bodyvos[i].clone();
        tempHeadVO.setShowflag(new Boolean(false));
        alAllTmpVO.add(tempHeadVO);
      }
      // 到货地区
      else if (bodyvos[i].getItemkey().equals("vdestarea")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        bodyvos[i].setDatatype(new Integer(5));
        bodyvos[i].setReftype("地区分类");
        bodyvos[i].setIdcolname("pkarrivearea");
        iShoworder = bodyvos[i].getShoworder();
        ifEditable = bodyvos[i].getEditflag();
        for (int j = 0; j < bodyvos.length; j++) {
          if (bodyvos[j].getItemkey().equals("pkarrivearea")) {
            bodyvos[j].setShowflag(new Boolean(false));
            tempHeadVO = new BillTempletBodyVO();
            tempHeadVO = (BillTempletBodyVO) bodyvos[j].clone();
            tempHeadVO.setShoworder(iShoworder);
            tempHeadVO.setShowflag(new Boolean(false));
            tempHeadVO.setDatatype(new Integer(5));
            tempHeadVO.setReftype("地区分类");
            tempHeadVO.setEditflag(ifEditable);
            tempHeadVO.setPos(new Integer(0));
            tempHeadVO.setWidth(new Integer(
                bodyvos[j].getWidth().intValue() / 80));
            alAllTmpVO.add(tempHeadVO);
            break;
          }
        }
      }
      // 项目
      else if (bodyvos[i].getItemkey().equals("vitemname")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        // bodyvos[i].setShowflag(new Boolean(false));
        bodyvos[i].setDatatype(new Integer(5));
        bodyvos[i].setReftype("项目管理档案");
        bodyvos[i].setIdcolname("pkitem");
        iShoworder = bodyvos[i].getShoworder();
        ifEditable = bodyvos[i].getEditflag();
        for (int j = 0; j < bodyvos.length; j++) {
          if (bodyvos[j].getItemkey().equals("pkitem")) {
            bodyvos[j].setShowflag(new Boolean(false));
            tempHeadVO = new BillTempletBodyVO();
            tempHeadVO = (BillTempletBodyVO) bodyvos[j].clone();
            tempHeadVO.setShoworder(iShoworder);
            // tempHeadVO.setDefaultshowname("项目");
            tempHeadVO.setShowflag(new Boolean(false));
            tempHeadVO.setDatatype(new Integer(5));
            tempHeadVO.setReftype("项目管理档案");
            // tempHeadVO.setReftype("<nc.ui.bd.b39.JobRefTreeModel>");
            tempHeadVO.setEditflag(ifEditable);
            tempHeadVO.setPos(new Integer(0));
            tempHeadVO.setWidth(new Integer(
                bodyvos[j].getWidth().intValue() / 80));
            alAllTmpVO.add(tempHeadVO);
            break;
            // bodyvos[j].setEditflag(new Boolean(true));
          }
        }
      }
      // 项目阶段
      else if (bodyvos[i].getItemkey().equals("vitemperiodname")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        // bodyvos[i].setShowflag(new Boolean(false));
        bodyvos[i].setDatatype(new Integer(5));
        bodyvos[i].setReftype("项目阶段");
        bodyvos[i].setIdcolname("pkitemperiod");
        iShoworder = bodyvos[i].getShoworder();
        ifEditable = bodyvos[i].getEditflag();
        for (int j = 0; j < bodyvos.length; j++) {
          if (bodyvos[j].getItemkey().equals("pkitemperiod")) {
            bodyvos[j].setShowflag(new Boolean(false));
            tempHeadVO = new BillTempletBodyVO();
            tempHeadVO = (BillTempletBodyVO) bodyvos[j].clone();
            tempHeadVO.setShoworder(iShoworder);
            tempHeadVO.setDefaultshowname("项目阶段");
            tempHeadVO.setShowflag(new Boolean(false));
            tempHeadVO.setDatatype(new Integer(5));
            tempHeadVO.setReftype("<nc.ui.bd.b39.PhaseRefModel>");
            tempHeadVO.setEditflag(ifEditable);
            tempHeadVO.setPos(new Integer(0));
            tempHeadVO.setWidth(new Integer(
                bodyvos[j].getWidth().intValue() / 80));
            alAllTmpVO.add(tempHeadVO);
            // bodyvos[j].setEditflag(new Boolean(true));
            break;
          }
        }
      }
      // 发货地点
      else if (bodyvos[i].getItemkey().equals("vsendaddress")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        // bodyvos[i].setShowflag(new Boolean(false));
        bodyvos[i].setDatatype(new Integer(5));
        bodyvos[i].setReftype("地点档案");
        bodyvos[i].setIdcolname("pksendaddress");
        iShoworder = bodyvos[i].getShoworder();
        ifEditable = bodyvos[i].getEditflag();
        for (int j = 0; j < bodyvos.length; j++) {
          if (bodyvos[j].getItemkey().equals("pksendaddress")) {
            bodyvos[j].setShowflag(new Boolean(false));
            tempHeadVO = new BillTempletBodyVO();
            tempHeadVO = (BillTempletBodyVO) bodyvos[j].clone();
            tempHeadVO.setShoworder(iShoworder);
            tempHeadVO.setShowflag(new Boolean(false));
            tempHeadVO.setDatatype(new Integer(5));
            tempHeadVO.setReftype("地点档案");
            tempHeadVO.setPos(new Integer(0));
            tempHeadVO.setWidth(new Integer(
                bodyvos[j].getWidth().intValue() / 80));
            alAllTmpVO.add(tempHeadVO);
            break;
            // bodyvos[j].setEditflag(ifEditable);
          }
        }
      }
      // 收货地点
      else if (bodyvos[i].getItemkey().equals("varriveaddress")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        // bodyvos[i].setShowflag(new Boolean(false));
        bodyvos[i].setDatatype(new Integer(5));
        bodyvos[i].setReftype("地点档案");
        bodyvos[i].setIdcolname("pkarriveaddress");
        iShoworder = bodyvos[i].getShoworder();
        // ifEditable= bodyvos[i].getEditflag();
        for (int j = 0; j < bodyvos.length; j++) {
          if (bodyvos[j].getItemkey().equals("pkarriveaddress")) {
            bodyvos[j].setShowflag(new Boolean(false));
            tempHeadVO = new BillTempletBodyVO();
            tempHeadVO = (BillTempletBodyVO) bodyvos[j].clone();
            tempHeadVO.setShoworder(iShoworder);
            tempHeadVO.setShowflag(new Boolean(false));
            tempHeadVO.setDatatype(new Integer(5));
            tempHeadVO.setReftype("地点档案");
            tempHeadVO.setPos(new Integer(0));
            tempHeadVO.setWidth(new Integer(
                bodyvos[j].getWidth().intValue() / 80));
            alAllTmpVO.add(tempHeadVO);
            break;
            // bodyvos[j].setEditflag(ifEditable);
          }
        }
      }
      // 辅计量
      else if (bodyvos[i].getItemkey().equals("vassistmeaname")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        // bodyvos[i].setShowflag(new Boolean(false));
        // ifEditable = bodyvos[i].getEditflag();
        // iShoworder = bodyvos[i].getShoworder();
        bodyvos[i].setDatatype(new Integer(5));
        bodyvos[i].setReftype("计量档案");
        bodyvos[i].setIdcolname("pkassistmeasure");
        /*
         * tempHeadVO = new BillTempletBodyVO(); tempHeadVO =
         * (BillTempletBodyVO) bodyvos[i].clone();
         * tempHeadVO.setShoworder(iShoworder); tempHeadVO.setShowflag(new
         * Boolean(false)); tempHeadVO.setIdcolname("pkassistmeasure");
         * tempHeadVO.setDatatype(new Integer(5));
         * tempHeadVO.setReftype("计量档案"); alAllTmpVO.add(tempHeadVO);
         */
        // break;
      }
      else if (bodyvos[i].getItemkey().equals("weightunit")) {
        bodyvos[i].setShowflag(new Boolean(false));
      }
      else if (bodyvos[i].getItemkey().equals("volumnunit")) {
        bodyvos[i].setShowflag(new Boolean(false));
      }
      // 发货单位
      else if (bodyvos[i].getItemkey().equals("vsendcorpname")
          && bodyvos[i].getListshowflag().booleanValue() == true) {
        bodyvos[i].setShowflag(new Boolean(true));
        bodyvos[i].setDatatype(new Integer(5));
        bodyvos[i].setReftype("客商档案");
        bodyvos[i].setIdcolname("csendcorpid");
        iShoworder = bodyvos[i].getShoworder();
        ifEditable = bodyvos[i].getEditflag();
        for (int j = 0; j < bodyvos.length; j++) {
          if (bodyvos[j].getItemkey().equals("csendcorpid")) {
            bodyvos[j].setShowflag(new Boolean(false));
            // tempHeadVO = new BillTempletBodyVO();
            // tempHeadVO = (BillTempletBodyVO) bodyvos[j].clone();
            // tempHeadVO.setShowflag(new Boolean(false));
            // tempHeadVO.setDatatype(new Integer(5));
            // tempHeadVO.setReftype("客商档案");
            // tempHeadVO.setEditflag(ifEditable);
            // tempHeadVO.setShoworder(iShoworder);
            // tempHeadVO.setPos(new Integer(0));
            // tempHeadVO.setWidth(new Integer(bodyvos[j].getWidth().intValue()
            // / 80));
            // alAllTmpVO.add(tempHeadVO);
            break;
          }
        }
      }
      // else if (bodyvos[i].getItemkey().startsWith("vuserdef")) {
      // bodyvos[i].setShowflag(new Boolean(false));
      // }
    }
    // 处理表头数据
    BillTempletBodyVO[] originBodyTVOs = btvo.getBodyVO();
    for (int i = 0; i < originBodyTVOs.length; i++) {
      originBodyTVOs[i].setPos(new Integer(1));
      alAllTmpVO.add(originBodyTVOs[i]);
    }
    BillTempletBodyVO[] allBodyVOs = (BillTempletBodyVO[]) alAllTmpVO
        .toArray(new BillTempletBodyVO[0]);
    btvo.setChildrenVO(allBodyVOs);
  }

  /**
   * 改变模板数据的显示位置-所有数据项显示在表头 功能： 参数： 返回： 例外： 日期：(2002-10-7 19:11:02)
   * 修改日期，修改人，修改原因，注释标志：
   */
  public void changeSecBillTempletVO(BillTempletVO btvo) {
    Boolean ifEditable = null;
    BillTempletBodyVO[] bodyvos = btvo.getBodyVO();
    Integer iShoworder = new Integer(0);
    for (int i = 0; i < bodyvos.length; i++) {
      // 发运方式
      if (bodyvos[i].getItemkey().equals("vsendtypename")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        iShoworder = bodyvos[i].getShoworder();
        bodyvos[i].setShowflag(new Boolean(false));
        ifEditable = bodyvos[i].getEditflag();
        for (int j = 0; j < bodyvos.length; j++) {
          if (bodyvos[j].getItemkey().equals("pksendtype")) {
            bodyvos[j].setShowflag(new Boolean(true));
            bodyvos[j].setDatatype(new Integer(5));
            bodyvos[j].setReftype("发运方式");
            bodyvos[j].setEditflag(ifEditable);
            bodyvos[j].setShoworder(iShoworder);
            break;
          }
        }
      }
      // 收货单位
      else if (bodyvos[i].getItemkey().equals("creceiptcorpname")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        bodyvos[i].setShowflag(new Boolean(false));
        iShoworder = bodyvos[i].getShoworder();
        ifEditable = bodyvos[i].getEditflag();
        for (int j = 0; j < bodyvos.length; j++) {
          if (bodyvos[j].getItemkey().equals("creceiptcorpid")) {
            bodyvos[j].setShowflag(new Boolean(true));
            bodyvos[j].setDatatype(new Integer(5));
            bodyvos[j].setReftype("客户档案");
            bodyvos[j].setEditflag(ifEditable);
            bodyvos[j].setShoworder(iShoworder);
            break;
          }
        }
      }
      // 发货库存组织
      else if (bodyvos[i].getItemkey().equals("vsendstoreorgname")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        bodyvos[i].setShowflag(new Boolean(false));
        ifEditable = bodyvos[i].getEditflag();
        iShoworder = bodyvos[i].getShoworder();
        for (int j = 0; j < bodyvos.length; j++) {
          if (bodyvos[j].getItemkey().equals("pksendstoreorg")) {
            bodyvos[j].setShoworder(iShoworder);
            bodyvos[j].setShowflag(new Boolean(true));
            bodyvos[j].setDatatype(new Integer(5));
            bodyvos[j].setReftype("库存组织");
            bodyvos[j].setEditflag(ifEditable);
            break;
          }
        }
      }
      // 发货仓库
      else if (bodyvos[i].getItemkey().equals("vsendstorename")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        bodyvos[i].setShowflag(new Boolean(false));
        ifEditable = bodyvos[i].getEditflag();
        iShoworder = bodyvos[i].getShoworder();
        for (int j = 0; j < bodyvos.length; j++) {
          if (bodyvos[j].getItemkey().equals("pksendstore")) {
            bodyvos[j].setShoworder(iShoworder);
            bodyvos[j].setShowflag(new Boolean(true));
            bodyvos[j].setDatatype(new Integer(5));
            bodyvos[j].setReftype("仓库档案");
            bodyvos[j].setEditflag(ifEditable);
            break;
          }
        }
      }
      // 发货地区
      else if (bodyvos[i].getItemkey().equals("vsendarea")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        bodyvos[i].setShowflag(new Boolean(false));
        ifEditable = bodyvos[i].getEditflag();
        iShoworder = bodyvos[i].getShoworder();
        for (int j = 0; j < bodyvos.length; j++) {
          if (bodyvos[j].getItemkey().equals("pksendarea")) {
            bodyvos[j].setShoworder(iShoworder);
            bodyvos[j].setShowflag(new Boolean(true));
            bodyvos[j].setDatatype(new Integer(5));
            bodyvos[j].setReftype("地区分类");
            bodyvos[j].setEditflag(ifEditable);
            break;
          }
        }
      }
      // 到货仓库
      else if (bodyvos[i].getItemkey().equals("vdeststorename")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        bodyvos[i].setShowflag(new Boolean(false));
        ifEditable = bodyvos[i].getEditflag();
        iShoworder = bodyvos[i].getShoworder();
        for (int j = 0; j < bodyvos.length; j++) {
          if (bodyvos[j].getItemkey().equals("pkdeststore")) {
            bodyvos[j].setShoworder(iShoworder);
            bodyvos[j].setShowflag(new Boolean(true));
            bodyvos[j].setDatatype(new Integer(5));
            bodyvos[j].setReftype("仓库档案");
            bodyvos[j].setEditflag(ifEditable);

            // the next line added by zxping
            // bodyvos[j].setEditformula("vdeststoreaddre->getColValue(bd_stordoc,storaddr,pk_stordoc,pkdeststore)");

            break;
          }
        }
      }
      // 到货地址
      else if (bodyvos[i].getItemkey().equals("vdestaddress")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        bodyvos[i].setDatatype(new Integer(5));
        bodyvos[i].setReftype("<nc.ui.scm.ref.prm.CustAddrRefModel>");
      }
      // 到货地区
      else if (bodyvos[i].getItemkey().equals("vdestarea")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        bodyvos[i].setShowflag(new Boolean(false));
        iShoworder = bodyvos[i].getShoworder();
        ifEditable = bodyvos[i].getEditflag();
        for (int j = 0; j < bodyvos.length; j++) {
          if (bodyvos[j].getItemkey().equals("pkarrivearea")) {
            bodyvos[j].setShoworder(iShoworder);
            bodyvos[j].setShowflag(new Boolean(true));
            bodyvos[j].setDatatype(new Integer(5));
            bodyvos[j].setReftype("地区分类");
            bodyvos[j].setEditflag(ifEditable);
            break;
          }
        }
      }
      // 项目
      else if (bodyvos[i].getItemkey().equals("vitemname")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        bodyvos[i].setShowflag(new Boolean(false));
        iShoworder = bodyvos[i].getShoworder();
        ifEditable = bodyvos[i].getEditflag();
        for (int j = 0; j < bodyvos.length; j++) {
          if (bodyvos[j].getItemkey().equals("pkitem")) {
            bodyvos[j].setShoworder(iShoworder);
            bodyvos[j].setDefaultshowname("项目");
            bodyvos[j].setShowflag(new Boolean(true));
            bodyvos[j].setDatatype(new Integer(5));
            bodyvos[j].setReftype("项目管理档案");
            // bodyvos[j].setReftype("<nc.ui.bd.b39.JobRefTreeModel>");
            bodyvos[j].setEditflag(ifEditable);
            // bodyvos[j].setEditflag(new Boolean(true));
            break;
          }
        }
      }
      // 项目阶段
      else if (bodyvos[i].getItemkey().equals("vitemperiodname")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        bodyvos[i].setShowflag(new Boolean(false));
        iShoworder = bodyvos[i].getShoworder();
        ifEditable = bodyvos[i].getEditflag();
        for (int j = 0; j < bodyvos.length; j++) {
          if (bodyvos[j].getItemkey().equals("pkitemperiod")) {
            bodyvos[j].setShoworder(iShoworder);
            bodyvos[j].setDefaultshowname("项目阶段");
            bodyvos[j].setShowflag(new Boolean(true));
            bodyvos[j].setDatatype(new Integer(5));
            bodyvos[j].setReftype("<nc.ui.bd.b39.PhaseRefModel>");
            bodyvos[j].setEditflag(ifEditable);
            // bodyvos[j].setEditflag(new Boolean(true));
            break;
          }
        }
      }
      // 辅计量
      else if (bodyvos[i].getItemkey().equals("vassistmeaname")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        bodyvos[i].setShowflag(new Boolean(false));
        ifEditable = bodyvos[i].getEditflag();
        iShoworder = bodyvos[i].getShoworder();
        for (int j = 0; j < bodyvos.length; j++) {
          if (bodyvos[j].getItemkey().equals("pkassistmeasure")) {
            bodyvos[j].setShoworder(iShoworder);
            bodyvos[j].setShowflag(new Boolean(true));
            bodyvos[j].setDatatype(new Integer(5));
            bodyvos[j].setReftype("计量档案");
            bodyvos[j].setEditflag(ifEditable);
            break;
          }
        }
      }
      // 报价计量单位
      else if (bodyvos[i].getItemkey().equals("cquoteunitname")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        bodyvos[i].setShowflag(new Boolean(false));
        ifEditable = bodyvos[i].getEditflag();
        iShoworder = bodyvos[i].getShoworder();
        for (int j = 0; j < bodyvos.length; j++) {
          if (bodyvos[j].getItemkey().equals("cquoteunitid")) {
            bodyvos[j].setShoworder(iShoworder);
            bodyvos[j].setShowflag(new Boolean(true));
            bodyvos[j].setDatatype(new Integer(5));
            bodyvos[j].setReftype("计量档案");
            bodyvos[j].setEditflag(ifEditable);
            break;
          }
        }
      }
      // 批次号
      else if (bodyvos[i].getItemkey().equals("vbatchcode")) {
        bodyvos[i].setShowflag(bodyvos[i].getShowflag());
      }
      else if (bodyvos[i].getItemkey().equals("vfree0")) {
        // 自由项
        bodyvos[i].setShowflag(bodyvos[i].getShowflag());
      }
      // 发货地点
      else if (bodyvos[i].getItemkey().equals("vsendaddress")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        bodyvos[i].setShowflag(new Boolean(false));
        iShoworder = bodyvos[i].getShoworder();
        // ifEditable= bodyvos[i].getEditflag();
        for (int j = 0; j < bodyvos.length; j++) {
          if (bodyvos[j].getItemkey().equals("pksendaddress")) {
            bodyvos[j].setShoworder(iShoworder);
            bodyvos[j].setShowflag(new Boolean(true));
            bodyvos[j].setDatatype(new Integer(5));
            bodyvos[j].setReftype("地点档案");
            // bodyvos[j].setEditflag(ifEditable);
            break;
          }
        }
      }
      // 收货地点
      else if (bodyvos[i].getItemkey().equals("varriveaddress")
          && bodyvos[i].getShowflag().booleanValue() == true) {
        bodyvos[i].setShowflag(new Boolean(false));
        iShoworder = bodyvos[i].getShoworder();
        // ifEditable= bodyvos[i].getEditflag();
        for (int j = 0; j < bodyvos.length; j++) {
          if (bodyvos[j].getItemkey().equals("pkarriveaddress")) {
            bodyvos[j].setShoworder(iShoworder);
            bodyvos[j].setShowflag(new Boolean(true));
            bodyvos[j].setDatatype(new Integer(5));
            bodyvos[j].setReftype("地点档案");
            // bodyvos[j].setEditflag(ifEditable);
            break;
          }
        }
      }
      bodyvos[i].setPos(new Integer(0));
      bodyvos[i].setWidth(new Integer(bodyvos[i].getWidth().intValue() / 80));
      // if (bodyvos[i].getItemkey().equals("vnote")) {
      // bodyvos[i].setWidth(new Integer(3));
      // }
    }
    btvo.setChildrenVO(bodyvos);
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-8-21 17:23:09) 修改日期，修改人，修改原因，注释标志：
   */
  public boolean checkVO(DMVO dvo) {

    try {
      // 表头表体非空检查
      VOCheck.dmvalidate(dvo, null, nc.ui.scm.ic.exp.GeneralMethod
          .getHeaderCanotNullString(getSecBillCardPanel()));
    }
    catch (ICNullFieldException e) {
      // 显示提示
      String sErrorMessage = nc.ui.dm.dm102.GeneralMethod
          .getOtherHeadErrorMessage(getSecBillCardPanel(), e.getErrorRowNums(),
              e.getHint());
      // sErrorMessage= sErrorMessage.substring(22);
      nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
              "UPP40140404-000052")/* @res "错误！" */, sErrorMessage);
      return false;
    }
    catch (ValidationException e) {
      SCMEnv.info("较验异常！其他未知故障...");
      nc.ui.pub.beans.MessageDialog
          .showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "40140404", "UPP40140404-000052")/* @res "错误！" */,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
                  "UPP40140404-000053")/* @res "较验异常！其他未知故障..." */);
      handleException(e);
      return false;
    }

    DMDataVO[] dmdvos = (DMDataVO[]) dvo.getChildrenVO();
    UFDate snddate = null;
    UFDate currdate = getClientEnvironment().getDate();
    String vplancode = null;

    Object onum = null;
    UFDouble dnum = new UFDouble(0);

    StringBuffer sbdate = new StringBuffer();
    StringBuffer sbnum = new StringBuffer();
    StringBuffer sbStockOrg = new StringBuffer();

    for (int i = 0; i < dmdvos.length; i++) {

      // 日期检查
      snddate = (UFDate) dmdvos[i].getAttributeValue("snddate");// 是否应为requiredate
      vplancode = (String) dmdvos[i].getAttributeValue("vdelivdayplcode");

      if (snddate.before(currdate))
        sbdate.append("<" + vplancode + ">");

      // 数量检查
      onum = dmdvos[i].getAttributeValue("dnum");
      dnum = (onum == null ? new UFDouble(0) : new UFDouble(onum.toString()));

      if (dnum.doubleValue() < 0)
        sbnum.append("<" + vplancode + ">");

      String pksendstoreorg = (String) dmdvos[i]
          .getAttributeValue("pksendstoreorg");
      String vbilltype = (String) dmdvos[i].getAttributeValue("vbilltype");
      if (!vbilltype.equals("21")) {// 从采购自提过来的日计划不需校验 发货库存组织 是否为空
        if (pksendstoreorg == null || pksendstoreorg.trim().length() == 0) {
          sbStockOrg.append("<" + vplancode + ">");
        }
      }
    }

    // if (sbdate.toString().trim().length() != 0) {
    // if (nc.ui.pub.beans.MessageDialog
    // .showYesNoDlg(this,
    // nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
    // "UPP40140404-000054")/*@res "警告！"*/,
    // nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
    // "UPP40140404-000055")/*@res "日计划："*/
    // + sbdate.toString()
    // + nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
    // "UPP40140404-000056")/*@res "客户要求到货日期无法满足，是否继续？"*/) == UIDialog.ID_NO)
    // return false;
    // }

    if (sbnum.toString().trim().length() != 0) {
      nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
              "UPP40140404-000052")/* @res "错误！" */, nc.ui.ml.NCLangRes
              .getInstance().getStrByID("40140404", "UPP40140404-000055")/*
                                                                           * @res
                                                                           * "日计划："
                                                                           */
              + sbnum.toString()
              + nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
                  "UPP40140404-000057")/* @res "数量不可为负数！！！" */);
      return false;
    }

    if (sbStockOrg.toString().length() != 0) {
      nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
              "UPP40140404-000052")/* @res "错误！" */, nc.ui.ml.NCLangRes
              .getInstance().getStrByID("40140404", "UPP40140404-000055")/*
                                                                           * @res
                                                                           * "日计划："
                                                                           */
              + sbStockOrg.toString()
              + nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
                  "UPP40140404-000058")/* @res "发货库存组织不可为空！！！" */);
      return false;
    }
    return true;
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-8-21 17:23:09) 修改日期， 修改人， 修改原因， 注释标志：
   */
  public void checkSendStorePower(DMVO dvo) throws BusinessException,
      RemoteException {
    String sSendStore = new String();
    DMDataVO[] dmdvos = (DMDataVO[]) dvo.getChildrenVO();
    for (int i = 0; i < dmdvos.length; i++) {
      String sVbilltype = dmdvos[i].getAttributeValue("vbilltype").toString();
      // String sVbilltype =
      // dmdvos[i].getAttributeValue("vbilltype").toString();
      // String sBorderreturn =
      // dmdvos[i].getAttributeValue("borderreturn").toString();
      if (!sVbilltype.equals("21")) {
        String sPKsendstore = (String) dmdvos[i]
            .getAttributeValue("pksendstore");
        String sPKsalecorp = (String) dmdvos[i].getAttributeValue("pksalecorp");
        String sSendstorename = (String) dmdvos[i]
            .getAttributeValue("vsendstorename");
        String[] sPowers = DataPowerServ.hasPower("bd_stordoc", "仓库档案",
            getUserID(), sPKsalecorp);
      }
    }
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-8-9 16:27:02) 修改日期，修改人，修改原因，注释标志：
   */
  public boolean getChooseCloseOrderDlg(DMDataVO[] dmdvos) {
    m_choosevos = null;
    DMVO dvo = new DMVO();
    dvo.setChildrenVO(null);
    DMDataVO[] tmpvos = new DMDataVO[1];
    ArrayList v = new ArrayList();
    // 当剩余数量<=0时，订单自动关闭，不弹出选择窗口
    for (int i = 0; i < dmdvos.length; i++) {
      tmpvos[0] = new DMDataVO();
      tmpvos[0].setAttributeValue("pkbillb", dmdvos[i]
          .getAttributeValue("pkbillb"));
      UFBoolean bclose = (UFBoolean) dmdvos[i].getAttributeValue("orderstatus");
      // , new UFBoolean(false));
      if (bclose.booleanValue() == false) {
        v.add(dmdvos[i]);
      }
      tmpvos[0].setAttributeValue("orderstatus", bclose);
      dvo.appendBodyVO(tmpvos);
    }
    m_choosevos = (DMDataVO[]) dvo.getChildrenVO();
    if (v.size() == 0) {
      return true;
    }
    // 初始化DLG界面数据
    DMDataVO[] vos = new DMDataVO[v.size()];
    vos = (DMDataVO[]) v.toArray(vos);
    v = null;
    if (m_ChooseCloseOrderDLG == null)
      m_ChooseCloseOrderDLG = new ChooseCloseOrderDLG(this);
    DMVO tmpdvo = new DMVO();
    // /DMDataVO[] choosvos= new DMDataVO[m_planvos.length]
    tmpdvo.setChildrenVO(vos);
    m_ChooseCloseOrderDLG.getBillCardPanel().setBillValueVO(tmpdvo);
    if (m_ChooseCloseOrderDLG.showModal() == UIDialog.ID_OK) {
      tmpvos = m_ChooseCloseOrderDLG.getReturnOrderData();
      tmpdvo.setChildrenVO(tmpvos);
      dvo.combineOtherVOByPK(tmpdvo, "pkbillb");
      // dvo.appendBodyVO(tmpvos);
      m_choosevos = (DMDataVO[]) dvo.getChildrenVO();
      return true;
    }
    m_choosevos = (DMDataVO[]) dvo.getChildrenVO();
    return false;
  }

  /**
   * 遍历Hashtable
   * 
   * @return java.lang.Object[]
   * @param Keys
   *          java.lang.String[]
   */
  private DMDataVO[] getVOsFromHashtable(Hashtable ht) {
    ArrayList v = new ArrayList();
    java.util.Enumeration enumr = ht.keys();
    DMDataVO vo = new DMDataVO();
    Object key;
    while (enumr.hasMoreElements()) {
      key = enumr.nextElement();
      vo = (DMDataVO) m_allHt.get(key);
      if (vo != null)
        v.add(vo);
    }

    if (v.size() == 0)
      return new DMDataVO[] {};
    else {
      DMDataVO[] vos = new DMDataVO[v.size()];
      vos = (DMDataVO[]) v.toArray(vos);
      v = null;
      return vos;
    }
  }

  /**
   * 获得回写销售订单的数据。 功能： 参数： 返回： 例外： 日期：(2002-8-6 13:12:37) 修改日期，修改人，修改原因，注释标志：
   * 
   * @param vos
   *          nc.vo.dm.pub.DMDataVO[]
   */
  public DMDataVO[] getWriteOrderData(DMDataVO[] vos) {
    if (vos == null || vos.length == 0)
      return null;
    DMDataVO[] ordervos = new DMDataVO[vos.length];

    String pkdelivdaypl;
    UFDouble dnum;

    for (int i = 0; i < vos.length; i++) {

      ordervos[i] = new DMDataVO();
      ordervos[i].setStatus(vos[i].getStatus());
      pkdelivdaypl = (String) vos[i].getAttributeValue("pk_delivdaypl");

      Object o = vos[i].getAttributeValue("dnum");
      dnum = (o == null ? new UFDouble(0) : new UFDouble(o.toString()));

      ordervos[i].setAttributeValue("pkbillh", vos[i]
          .getAttributeValue("pkbillh"));
      ordervos[i].setAttributeValue("pkbillb", vos[i]
          .getAttributeValue("pkbillb"));
      ordervos[i].setAttributeValue("vbilltype", vos[i]
          .getAttributeValue("vbilltype"));
      ordervos[i].setAttributeValue("pksalecorp", vos[i]
          .getAttributeValue("pksalecorp"));

      ordervos[i].setAttributeValue("orderstatus", vos[i]
          .getAttributeValue("delautocanceloder"));

      if (ordervos[i].getStatus() == VOStatus.DELETED)
        ordervos[i].setAttributeValue("ndelivernum", dnum.multiply(-1));

      else if (ordervos[i].getStatus() == VOStatus.NEW)
        ordervos[i].setAttributeValue("ndelivernum", dnum);

      else if (ordervos[i].getStatus() == VOStatus.UPDATED) {
        DMDataVO vo = (DMDataVO) m_allHt.get(pkdelivdaypl);
        Object oinitnum = vo.getAttributeValue("dnum");
        UFDouble initnum = (oinitnum == null ? new UFDouble(0) : new UFDouble(
            oinitnum.toString()));
        ordervos[i].setAttributeValue("ndelivernum", dnum.sub(initnum));
      }

    }

    return ordervos;
  }

  /**
   * 载入列表数据 创建日期：(2002-6-10 19:17:44)
   * 
   * @param sWhereSql
   *          java.lang.String
   */
  public void loadBillDate() {

    DMDataVO[] dmdvos = getVOsFromHashtable(m_allHt);

    DMVO dmvo = new DMVO();
    Hashtable hht = new Hashtable();
    Hashtable bht = new Hashtable();
    String pkbillh = new String();
    ArrayList bodyv = new ArrayList();
    ArrayList headv = new ArrayList();
    DMDataVO[] bodyvos = null;
    m_bodyListHt = new Hashtable();

    // 清除界面
    getBillListPanel().getHeadBillModel().clearBodyData();
    getBillListPanel().getBodyBillModel().clearBodyData();
    getBillListPanel().getHeadTable().clearSelection();

    // 生成列表表头数据
    for (int i = 0; i < dmdvos.length; i++) {
      pkbillh = (String) dmdvos[i].getAttributeValue("pkbillh");
      if (!hht.containsKey(pkbillh)) {
        hht.put(pkbillh, dmdvos[i]);
        headv.add(dmdvos[i]);
        bodyv = new ArrayList();
        bodyv.add(dmdvos[i]);
        m_bodyListHt.put(pkbillh, bodyv);
      }
      else {
        bodyv = (ArrayList) m_bodyListHt.get(pkbillh);
        bodyv.add(dmdvos[i]);
        m_bodyListHt.put(pkbillh, bodyv);
      }
    }
    if (headv.size() != 0) {
      // 记录已排序旧数据
      ArrayList alOldHead = new ArrayList();
      if (m_headListVOs != null) {
        for (int i = 0; i < m_headListVOs.length; i++) {
          alOldHead.add(m_headListVOs[i].clone());
        }
      }
      DMDataVO[] dmdOldHeads = (DMDataVO[]) alOldHead.toArray(new DMDataVO[0]);

      // m_headListVOs = new DMDataVO[headv.size()];
      // m_headListVOs = (DMDataVO[]) headv.toArray(m_headListVOs);
      m_headListVOs = (DMDataVO[]) headv.toArray(new DMDataVO[0]);
      headv = null;
      // 依据旧记录排序新纪录
      java.util.HashMap hmSortNewHead = new java.util.HashMap();
      ArrayList alSortNewHead = new ArrayList();
      ArrayList alUnSortNewHead = new ArrayList();
      for (int i = 0; i < dmdOldHeads.length; i++) {
        for (int j = 0; j < m_headListVOs.length; j++) {
          if (dmdOldHeads[i].getAttributeValue("pkbillh").toString().equals(
              m_headListVOs[j].getAttributeValue("pkbillh").toString())) {
            alSortNewHead.add(m_headListVOs[j]);
            hmSortNewHead.put(dmdOldHeads[i].getAttributeValue("pkbillh")
                .toString(), "");
          }
        }
      }
      for (int i = 0; i < m_headListVOs.length; i++) {
        if (!hmSortNewHead.containsKey(m_headListVOs[i]
            .getAttributeValue("pkbillh"))) {
          alUnSortNewHead.add(m_headListVOs[i]);
        }
      }
      // ArrayList alSortHeads =
      // nc.vo.dm.pub.tools.UsefulTools.hashtableValuesToArrayList(htSortNewHead);
      // ArrayList alUnSortHeads =
      // nc.vo.dm.pub.tools.UsefulTools.hashtableValuesToArrayList(htUnSortNewHead);
      for (int i = 0; i < alUnSortNewHead.size(); i++) {
        alSortNewHead.add(alUnSortNewHead.get(i));
      }
      m_headListVOs = (DMDataVO[]) alSortNewHead.toArray(new DMDataVO[0]);
      // for (int i = 0; i < alUnSortNewHead.size(); i++){
      // alSortNewHead.add(alUnSortNewHead.get(i));
      // }
      // m_headListVOs = (DMDataVO[]) alSortNewHead.toArray(new DMDataVO[0]);

      getBillListPanel().setHeaderValueVO(m_headListVOs);
      getBillListPanel().getHeadBillModel().execLoadFormula();
      getBillListPanel().getBodyBillModel().clearBodyData();
      getBillListPanel().getHeadTable().clearSelection();
      getBillListPanel().getBodyBillModel().updateValue();
    }

  }

  /**
   * 查询可用量。 功能： 参数： 返回： 例外： 日期：(2002-10-21 16:21:16) 修改日期，修改人，修改原因，注释标志：
   */
  private void onATP() {
    DMVO dmvo = (DMVO) getSecBillCardPanel().getBillValueVO(
        DMVO.class.getName(), DMDataVO.class.getName(),
        DMDataVO.class.getName());
    if (dmvo == null
        || dmvo.getParentVO() == null
        || dmvo.getParentVO().getAttributeValue("vdelivdayplcode") == null
        || dmvo.getParentVO().getAttributeValue("vdelivdayplcode").toString()
            .trim().length() == 0)
      return;
    // 构造初始化数据
    DMVO atpvo = new DMVO();
    DMDataVO[] atpbodyvos = new DMDataVO[1];
    atpbodyvos[0] = new DMDataVO();
    DMDataVO headvo = (DMDataVO) dmvo.getParentVO();
    atpvo.getParentVO().setAttributeValue("pk_corp",
        headvo.getAttributeValue("pksalecorp"));
    atpbodyvos[0].setAttributeValue("cinventoryid", headvo
        .getAttributeValue("pkinv"));
    atpbodyvos[0].setAttributeValue("dconsigndate", headvo
        .getAttributeValue("snddate"));
    atpbodyvos[0].setAttributeValue("vfree1", headvo
        .getAttributeValue("vfree1"));
    atpbodyvos[0].setAttributeValue("vfree2", headvo
        .getAttributeValue("vfree2"));
    atpbodyvos[0].setAttributeValue("vfree3", headvo
        .getAttributeValue("vfree3"));
    atpbodyvos[0].setAttributeValue("vfree4", headvo
        .getAttributeValue("vfree4"));
    atpbodyvos[0].setAttributeValue("vfree5", headvo
        .getAttributeValue("vfree5"));
    atpvo.setChildrenVO(atpbodyvos);

    // 初始化可用量界面
    if (atpDlg == null)
      atpDlg = new ATPForOneInvUI();
    atpDlg.initData(atpvo);
    atpDlg.showModal();

  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-10-21 18:59:04) 修改日期，修改人，修改原因，注释标志：
   */
  private void setIfCloseOrder(DMDataVO[] ordervos) {
    // 判断是否关闭订单
    for (int i = 0; i < ordervos.length; i++) {
      String pkbillb = (String) ordervos[i].getAttributeValue("pkbillb");
      String billtype = (String) ordervos[i].getAttributeValue("vbilltype"); // 单据类型
      for (int j = 0; j < m_choosevos.length; j++) {
        String tmpbillb = (String) m_choosevos[j].getAttributeValue("pkbillb");
        if (pkbillb.equals(tmpbillb)) {
          // if (billtype.equals("30") &&
          if (ordervos[i].getStatus() != VOStatus.DELETED) {
            // if (m_choosevos != null && m_choosevos.length != 0)
            ordervos[i].setAttributeValue("orderstatus",
                (UFBoolean) m_choosevos[j].getAttributeValue("orderstatus"));
            // else
            // ordervos[i].setAttributeValue("orderstatus", new
            // UFBoolean(false));
          }
          // else if (billtype.equals("4U") && ordervos[i].getStatus()
          // != VOStatus.DELETED)
          // if (((UFBoolean)
          // m_choosevos[j].getAttributeValue("orderstatus")).booleanValue()
          // == true)
          // ordervos[i].setAttributeValue("orderstatus", new
          // Integer(nc.vo.ic.pub.bill.BillStatus.CLOSE));
        }
      }
    }
  }

  /**
   * 删除后重置列表界面。 功能： 参数： 返回： 例外： 日期：(2002-8-5 15:26:55) 修改日期，修改人，修改原因，注释标志：
   */
  private void updateBillData2() {
    DMDataVO[] updatevos = new DMDataVO[m_planvos.length];
    for (int i = 0; i < updatevos.length; i++) {
      updatevos[i] = (DMDataVO) m_planvos[i].clone();
    }
    m_planvos = new DMDataVO[updatevos.length - 1];
    if (m_planvos.length == 0) {
      ArrayList headv = new ArrayList();
      for (int i = 0; i < m_headListVOs.length; i++) {
        if (i != m_selectRow)
          headv.add(m_headListVOs[i]);
      }
      // if (headv.size() != 0) {
      m_headListVOs = new DMDataVO[headv.size()];
      m_headListVOs = (DMDataVO[]) headv.toArray(m_headListVOs);
      headv = null;
      // }
      getBillListPanel().setHeaderValueVO(m_headListVOs);
      getBillListPanel().getHeadBillModel().execLoadFormula();
      return;
    }
    for (int i = 0; i < m_num; i++) {
      m_planvos[i] = updatevos[i];
    }
    for (int i = m_num; i < updatevos.length - 1; i++) {
      m_planvos[i] = (DMDataVO) updatevos[i + 1].clone();
    }
    //
    ArrayList v = new ArrayList();
    for (int i = 0; i < m_planvos.length; i++) {
      v.add(m_planvos[i]);
    }

    Object pkbillh = m_oSelectHid; // m_headListVOs[m_selectRow].getAttributeValue("pkbillh");
    m_bodyListHt.put(pkbillh, v);
    // DMVO dmvo= new DMVO();
    // dmvo.setChildrenVO(m_planvos);
    // getBillCardPanel().setBillValueVO(dmvo);
    // getBillCardPanel().updateValue();
  }

  /**
   * Called whenever the value of the selection changes.
   * 
   * @param e
   *          the event that characterizes the change.
   */
  public void valueChanged(javax.swing.event.ListSelectionEvent e) {
    if (m_bIsBatch) {
      int colnow = getBatchBillCardPanel().getBillTable().getSelectedColumn();
      int rownow = getBatchBillCardPanel().getBillTable().getSelectedRow();
      if (colnow >= 0 && rownow >= 0) {
        initRef(rownow, colnow, (DMBillCardPanel) getBatchBillCardPanel());
        String key = getBatchBillCardPanel().getBodyShowItems()[colnow]
            .getKey();
        Object value = getBatchBillCardPanel().getBodyValueAt(rownow, key);
        whenEntered(rownow, colnow, key, value, getBatchBillCardPanel());
      }
    }
  }

  public void whenEntered(int row, int col, String key, Object value,
      nc.ui.pub.bill.BillCardPanel bcp) {
    if (bcp != getBatchBillCardPanel())
      return;
    super.whenEntered(row, col, key, value, bcp);

    // Begin: addded by zxping
    // 辅数量
    if (key.equals("dassistnum")) {
      Object oinvassist = bcp.getBodyValueAt(row, "dassistnum"); // 辅数量
      Object odinvnum = bcp.getBodyValueAt(row, "dnum"); // 辅数量
      if (oinvassist != null && odinvnum != null
          && oinvassist.toString().trim().length() > 0
          && odinvnum.toString().trim().length() > 0) {
        dTransRate = (new Double(odinvnum.toString())).doubleValue()
            / (new Double(oinvassist.toString())).doubleValue();
      }
      return;
    }
    // vsendaddress
    else if (key.equals("vsendaddress")) {
      UIRefPane refPane = (UIRefPane) bcp.getBodyItem("vsendaddress")
          .getComponent();
      refPane.setWhereString(null);
    }
    // varriveaddress
    else if (key.equals("varriveaddress")) {
      UIRefPane refPane = (UIRefPane) bcp.getBodyItem("varriveaddress")
          .getComponent();
      refPane.setWhereString(null);
    }
    // End: added by zxping
    // 发运单表体根据销售公司ID重新设置参照
    Object oTemp = bcp.getBodyValueAt(row, "pksalecorp");
    if (oTemp != null && oTemp.toString().trim().length() > 0) {
      String pksalecorp = oTemp.toString();
      UIRefPane refPane = null;
      Object s = null;
      if (key.equals("vdestarea")) {
        // 到货地区
        refPane = (UIRefPane) bcp.getBodyItem("vdestarea").getComponent();

        refPane.getRefModel().setPk_corp(pksalecorp);
        refPane.setPk_corp(pksalecorp);

        refPane.setWhereString(" pk_corp = '" + pksalecorp
            + "' or pk_corp='0001' or pk_corp is null ");
      }

      else if (key.equals("creceiptcorp")) {
        // 收货单位
        refPane = (UIRefPane) bcp.getBodyItem("creceiptcorp").getComponent();

        refPane.getRefModel().setPk_corp(pksalecorp);
        refPane.setPk_corp(pksalecorp);

        refPane
            .setWhereString(" bd_cumandoc.pk_corp = '"
                + pksalecorp
                + "' AND bd_cumandoc.custflag = '0' OR bd_cumandoc.custflag = '2' ");
      }

      else if (key.equals("vdestaddress")) {
        // 到货地址
        refPane = (UIRefPane) bcp.getBodyItem("vdestaddress").getComponent();
        s = bcp.getBodyValueAt(row, "creceiptcorpid");
        if (s != null && s.toString().trim().length() > 0) {
          ((nc.ui.scm.ref.prm.CustAddrRefModel) refPane.getRefModel())
              .setCustId(s.toString());
        }
      }

      else if (key.equals("vsendstoreorgname")) {
        // 发货库存组织
        refPane = (UIRefPane) bcp.getBodyItem("vsendstoreorgname")
            .getComponent();

        refPane.getRefModel().setPk_corp(pksalecorp);
        refPane.setPk_corp(pksalecorp);

        refPane.getRefModel().setPk_corp(pksalecorp);
        refPane.getRefModel().setUseDataPower(true);
        refPane.getRefModel().setWherePart(
            nc.vo.dm.pub.sql.RefSql.sCalbodyPropertySql
                + " and bd_calbody.pk_corp = '" + pksalecorp + "'", true);
        // refPane.setWhereString(" pk_corp = '" + pksalecorp + "' ");
      }

      else if (key.equals("vsendstorename")) {
        // 发货仓库
        refPane = (UIRefPane) bcp.getBodyItem("vsendstorename").getComponent();
        s = bcp.getBodyValueAt(row, "pksendstoreorg");
        if (s != null && s.toString().trim().length() > 0) {

          refPane.getRefModel().setPk_corp(pksalecorp);
          refPane.setPk_corp(pksalecorp);

          refPane.setWhereString(" pk_calbody = '" + s.toString()
              + "' and gubflag = 'N' ");
        }
        else {
          refPane.setWhereString(" 1<0 ");
        }
      }
      else if (key.equals("vdeststorename")) {
        // 到货仓库
        refPane = (UIRefPane) bcp.getBodyItem("vdeststorename").getComponent();
        s = bcp.getBodyValueAt(row, "pkdeststoreorg");
        if (s != null && s.toString().trim().length() > 0) {

          refPane.getRefModel().setPk_corp(pksalecorp);
          refPane.setPk_corp(pksalecorp);

          refPane.setWhereString(" pk_calbody = '" + s.toString()
              + "' and gubflag = 'N' and sealflag ='N' ");
        }
        else {
          refPane.setWhereString(" 1<0 ");
        }
      }

    }

    // 辅单位
    if (key.equals("vassistmeaname")) {
      String sInvID = (String) bcp.getBodyValueAt(row, "pkinv");
      String pksalecorp = (String) bcp.getBodyValueAt(row, "pksalecorp");
      if (null != sInvID) {
        filterMeasBatch(sInvID, key, pksalecorp);
      }
    }

    // 自由项
    else if (key.equals("vfree0"))
      initBodyFreeItem(row, col, (DMBillCardPanel) bcp);

    // 批次号
    else if (key.equals("vbatchcode")) {
      initBodyLot(row, col, (DMBillCardPanel) bcp);
      // getLotNumbRefPane().setEnabled(false);
    }
  }

  protected ButtonObject boOutBill;

  protected ButtonObject boReOpen;

  private final boolean bseg = false;// 是否“赛格日立”使用

  private boolean m_bcardpagestatus = true;

  private DMVO m_initOrdervos; // 初始订单VOs

  // 生成出库单对话框

  protected OutBillDlg m_outBillDlg = null;

  protected boolean showed = false;

  /**
   * 此处插入方法说明。 功能：对表头辅计量限制 参数： 返回： 例外： 日期：(2002-9-19 15:09:00)
   * 修改日期，修改人，修改原因，注释标志：
   * 
   * @param sInvID
   *          java.lang.String
   * @param sCastUnitNameField
   *          java.lang.String
   */
  private void filterHeadMeas(String sInvID, String sCastUnitNameField,
      String pkcorp) {
    // nc.vo.ic.pub.bill.InvVO voInv= queryInvInfo(sInvID);

    nc.ui.pub.beans.UIRefPane refCast = (nc.ui.pub.beans.UIRefPane) getSecBillCardPanel()
        .getHeadItem(sCastUnitNameField).getComponent();

    m_voInvMeas.filterMeas(pkcorp, sInvID, refCast);
  }

  /**
   * 返回 BillCardPanel。
   * 
   * @return nc.ui.pub.bill.BillCardPanel
   */
  public DMBillCardPanel getBatchBillCardPanel() {
    if (ivjBatchBillCardPanel == null) {
      try {
        ivjBatchBillCardPanel = new DMBillCardPanel(getNodeCode());
        ivjBatchBillCardPanel.setName("BatchBillCardPanel");

        // 单据类型
        ivjBatchBillCardPanel.setBillType(getBillType());
        // 公司
        ivjBatchBillCardPanel.setCorp(getCorpPrimaryKey());
        // 操作员
        ivjBatchBillCardPanel.setOperator(getOperator());
        // 节点号
        ((DMBillCardPanel) ivjBatchBillCardPanel).setNodeCode(getNodeCode());

        // 填加监听
        ivjBatchBillCardPanel.addEditListener(this);
        ivjBatchBillCardPanel.addBodyEditListener2(this);
        ivjBatchBillCardPanel.setBodyMenuShow(false);
        // ivjBatchBillCardPanel.addBodyMenuListener(this);

        // ((DMBillCardPanel)
        // ivjBatchBillCardPanel).addTableColumnModelListener(this);

        ivjBatchBillCardPanel.addCheckBoxChangedListener(this);
        ivjBatchBillCardPanel.removeListSelectionListener();
        ivjBatchBillCardPanel.removeTableColumnModelListener();
        // ivjBatchBillCardPanel.sInvID = "pkinv";

        // 填加合计行
        ivjBatchBillCardPanel.setTatolRowShow(true);

        // 初始化行号列
        // ((DMBillCardPanel) ivjBillCardPanel).initRowNumItem();

      }
      catch (java.lang.Throwable ivjExc) {
        handleException(ivjExc);
      }
    }
    // loadBatchBillCardTemplet();
    return ivjBatchBillCardPanel;
  }

  /**
   * 返回当前处理单据ID。 创建日期：(2001-11-15 9:10:05)
   * 
   * @return java.lang.String
   */
  public java.lang.String getBillID() {
    String curBillID = null;
    if (getShowState().equals(DMBillStatus.List)) {
      int[] iCurRows = getBillListPanel().getBodyTable().getSelectedRows();
      if (iCurRows.length != 1 || iCurRows[0] < 0)
        return null;
      DMDataVO curBillVO = (DMDataVO) m_planvos[m_num];
      if (curBillVO == null) {
        return null;
      }
      curBillID = curBillVO.getAttributeValue(getPrimaryKeyName()).toString();
    }
    else if (getShowState().equals(DMBillStatus.Card)) {
      DMDataVO curBillVO = (DMDataVO) m_planvos[m_num];
      if (curBillVO == null) {
        return null;
      }
      curBillID = curBillVO.getAttributeValue(getPrimaryKeyName()).toString();
    }
    return curBillID;
  }

  /**
   * 列表表头数据项定义。 功能： 参数： 返回： 例外： 日期：(2002-9-16 15:06:12) 修改日期，修改人，修改原因，注释标志：
   */
  private BillItem[] getBillListHeadItems(BillListData bd) {
    BillItem[] items = new BillItem[7];
    for (int i = 0; i < 7; i++)
      items[i] = new BillItem();

//    for (int i = 0; i < 20; i++) {
//      items[i + 7].setName(nc.ui.ml.NCLangRes.getInstance().getStrByID(
//          "40140404", "UPP40140404-000060")/* @res "自定义项" */
//          + i);
//      items[i + 7].setKey("vuserdef" + i);
//      items[i + 7].setWidth(80);
//      items[i + 7].setEdit(true);
//      items[i + 7].setShow(bd.getBodyItem("vuserdef" + i).isShow());
//      items[i + 7].setShowOrder(i + 7);
//      items[i + 7].setPos(0);
//
//      items[i + 27].setName("pk_defdoc" + i);
//      items[i + 27].setKey("pk_defdoc" + i);
//      items[i + 27].setWidth(80);
//      items[i + 27].setEdit(false);
//      items[i + 27].setShow(bd.getBodyItem("pk_defdoc" + i).isShow());
//      items[i + 27].setShowOrder(i + 27);
//      items[i + 27].setPos(0);
//    }

    items[0].setName(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
        "UPP40140404-000061")/* @res "订单表头主键" */);
    items[0].setKey("pkbillh");
    items[0].setWidth(80);
    items[0].setEdit(false);
    items[0].setShow(false);
    items[0].setShowOrder(0);
    items[0].setPos(0);
    // /////
    items[1].setName(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
        "UC000-0003556")/* @res "订单类型" */);
    items[1].setKey("vbilltypename");
    items[1].setWidth(80);
    items[1].setEdit(false);
    items[1].setShowOrder(1);
    items[1]
        .setLoadFormula(new String[] {
          "vbilltypename->getColValueRes(bd_billtype,billtypename,pk_billtypecode,vbilltype)"
        });
    items[1].setPos(0);
    items[2].setName(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
        "UC000-0003534")/* @res "订单号" */);
    items[2].setKey("vsrcbillnum");
    items[2].setWidth(160);
    items[2].setEdit(false);
    items[2].setShowOrder(2);
    items[2].setDataType(0);
    items[2].setPos(0);
    items[3].setName(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
        "UC000-0001024")/* @res "发运方式" */);
    items[3].setKey("vsendtypename");
    items[3].setWidth(80);
    items[3].setEdit(false);
    items[3].setShow(false);
    items[3].setDataType(0);
    items[3].setShowOrder(3);
    items[3].setPos(0);
    items[4].setName(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
        "UPT40140404-000002")/* @res "订单类型编码" */);
    items[4].setKey("vbilltype");
    items[4].setWidth(80);
    items[4].setEdit(false);
    items[4].setShow(false);
    items[4].setDataType(0);
    items[4].setShowOrder(4);
    items[5].setName(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
        "UC000-0001589")/* @res "客户" */);
    items[5].setKey("vcustname");
    items[5].setWidth(80);
    items[5].setEdit(false);
    items[5].setDataType(0);
    items[5].setShowOrder(5);
    items[6].setName(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
        "UC000-0000645")/* @res "到货库存组织" */);
    items[6].setKey("vdeststoreorgname");
    items[6].setDataType(0);
    items[6].setWidth(80);
    items[6].setEdit(false);
    items[6].setShowOrder(6);
    // items[7].setName("简称");
    // items[7].setKey("custshortname");
    // items[7].setDataType(0);
    // items[7].setWidth(80);
    // items[7].setEdit(false);
    // //((UIRefPane) items[2].getComponent()).setReturnCode(true);
    // items[8].setName("纳税人登记号");
    // items[8].setKey("taxpayerid");
    // items[8].setDataType(0);
    // items[8].setWidth(80);
    // items[8].setEdit(false);
    return items;
  }

  /**
   * 返回列表。
   * 
   * @return nc.ui.pub.bill.BillListPanel
   */
  public nc.ui.pub.bill.BillListPanel getBillListPanel() {
    if (ivjBillListPane == null) {
      try {
        ivjBillListPane = new BillListPanel();
        ivjBillListPane.setName("BillListPanel");

        // 单据类型
        ivjBillListPane.setBillType(getBillType());
        // 公司
        ivjBillListPane.setCorp(getCorpPrimaryKey());
        // 操作员
        ivjBillListPane.setOperator(getOperator());

        // 填加监听
        // ivjBillListPane.addMouseListener(this);
        ivjBillListPane.addHeadEditListener(new ListHeadListener());
        ivjBillListPane.getParentListPanel().addTableSortListener();

        ivjBillListPane.updateUI();
      }
      catch (java.lang.Throwable ivjExc) {
        handleException(ivjExc);
      }
    }
    return ivjBillListPane;
  }

  /**
   * 篭n创建日期：(2003-4-15 15:28:26) 作者：左小军 修改日期： 修改人： 修改原因： 算法说明：
   * 
   * @return nc.vo.pub.lang.UFDouble
   * @param row
   *          int
   */
  protected boolean getBatchHsl(int row, UFDouble hsl) {
    // UFDouble hsl= null;
    String pkassistmeasure = "pkassistmeasure";
    Object oTemp = null;
    oTemp = getBatchBillCardPanel().getBodyValueAt(row, pkinv);
    if (oTemp == null) {
      SCMEnv.info("no inv id");
      return false;
    }
    String sInvID = oTemp.toString();
    oTemp = getBatchBillCardPanel().getBodyValueAt(row, pkassistmeasure);
    String sAstName = null;
    String sAstID = null;
    if (oTemp != null
        && getBatchBillCardPanel().getBodyValueAt(row, vassistmeaname) != null) {
      sAstName = getBatchBillCardPanel().getBodyValueAt(row, vassistmeaname)
          .toString();
      sAstID = oTemp.toString();
    }
    InvVO voaInv[] = getInvInfo(new String[] {
      sInvID
    }, new String[] {
      sAstID
    });
    if (voaInv == null || voaInv.length == 0 || voaInv[0] == null) {
      SCMEnv.info("no inv vo");
      return false;
    }
    InvVO invvo = voaInv[0];
    // -------------------------
    Integer isAstMgt = (Integer) invvo.getIsAstUOMmgt(); // 辅计量管理？
    Integer isFixFlag = (Integer) invvo.getIsSolidConvRate(); // 固定换算率

    if (isAstMgt == null || isAstMgt.intValue() == 0) {
      SCMEnv.info("not ast mgt");
      return false;
    }

    // 00000000
    if (sAstID == null || sAstID.trim().length() == 0) {
      /**
       * 若清空辅计量：换算率，if 固定换算率： 清空数量，辅数量，应发数量，应发辅数量，金额，计划金额 if
       * 变动换算率：清空数量，应发数量，金额，计划金额
       */
      /** 清空界面和VO中的辅计量名称和ID */
      getBatchBillCardPanel().setBodyValueAt(null, row, vassistmeaname);
      getBatchBillCardPanel().setBodyValueAt(null, row, pkassistmeasure);
      /** 清空换算率 */
      // 重算存货的换算率
      invvo.setHsl(null);
      // 更新存货vo
      updateInvInfo(invvo);
      return false;
    }

    // 辅计量单位
    nc.ui.pub.beans.UIRefPane refCastunit = ((nc.ui.pub.beans.UIRefPane) getBatchBillCardPanel()
        .getBodyItem(vassistmeaname).getComponent());

    // String sPK = refCastunit.getRefPK();
    // String sName = refCastunit.getRefName();
    String sPK = sAstID;
    String sName = sAstName;

    getBatchBillCardPanel().setBodyValueAt(sName, row, vassistmeaname);
    getBatchBillCardPanel().setBodyValueAt(sPK, row, pkassistmeasure);
    nc.ui.scm.ic.measurerate.InvMeasRate imr = new nc.ui.scm.ic.measurerate.InvMeasRate();
    nc.ui.pub.beans.UIRefPane refCast = (nc.ui.pub.beans.UIRefPane) getBatchBillCardPanel()
        .getBodyItem(vassistmeaname).getComponent();

    imr.filterMeas(m_sCorpID, sInvID, refCast);

    nc.vo.bd.b15.MeasureRateVO voMeas = imr.getMeasureRate(sInvID, sPK);
    if (voMeas != null) {
      hsl = voMeas.getMainmeasrate();
      // 重算存货的换算率
      invvo.setHsl(hsl);
      // 更新存货vo
      updateInvInfo(invvo);
    }
    return true;
  }

  /**
   * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理: 日期:
   * 
   * @return java.util.ArrayList
   */
  public ArrayList getItemFormulaBody() {
    // ArrayList arylistItemField =
    // nc.ui.dm.dm102.PlanFormula.getPlanFormula();
    return PlanFormula.getPlanFormula();
  }

  /**
   * 返回 生成出库单对话框。
   * 
   * @return nc.ui.pub.query.QueryConditionClient
   */
  private OutBillDlg getOutBillDlg() {
    try {
      if (m_outBillDlg == null) {
        /** 实例化对话框是必须指定他的父窗口，否则另外的窗口得到焦点时，对话框会隐藏到其它窗口的后面。 */
        // m_dlg = new StatbDlg(); //this.getParent());
        DMDataVO dmdvo = new DMDataVO();
        dmdvo.setAttributeValue("userid", getUserID());
        if (BD501 == null) {
          BD501 = new Integer(2);
        }
        DMDataVO ddvo = new DMDataVO();
        // ddvo.setAttributeValue("pkcorp", getCorpID());
        // ddvo.setAttributeValue("vcorpname", getCorpName());
        ddvo.setAttributeValue("pkdelivorg", getDelivOrgPK());
        ddvo.setAttributeValue("userid", getUserID());
        ddvo.setAttributeValue("username", getUserName());
        ddvo.setAttributeValue("corpid", getCorpID());
        ddvo.setAttributeValue("corpname", getCorpName());
        ddvo.setAttributeValue("BD501", BD501); // 主计量数量小数位数
        ddvo.setAttributeValue("BD502", BD502); // 辅计量数量小数位数
        ddvo.setAttributeValue("BD503", BD503); // 换算率
        ddvo.setAttributeValue("BD505", BD505); // 单价小数位数
        ddvo.setAttributeValue("BD301", BD301); // 金额小数位数
        ddvo.setAttributeValue("DM010", DM010); // 本次是否超发运单生成出库单
        m_outBillDlg = new OutBillDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40148868", "UPT40148868-000004")/* @res "生成出库单" */,
            ddvo);

      }
    }
    catch (Throwable e) {
      handleException(e);
    }
    return m_outBillDlg;
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-5-9 16:00:39) 修改日期，修改人，修改原因，注释标志：
   */
  public void initialize(String pk_corp, String billtype, String busitype,
      String operator, String id) {
    setBillTypeCode(DMBillTypeConst.m_delivDelivDayPl); // 发运日计划-列表
    setSecBillTypeCode(DMBillTypeConst.m_delivDelivDayPl); // 发运日计划-表单
    setThdBillTypeCode(DMBillTypeConst.m_delivOrderInDelivDayPl); // 订单
    setVuserdefCode(DMBillTypeConst.m_delivDelivDayPl);
    setNodeCode("40140404");

    strTitle1 = nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
        "UPP40140404-000062")/* @res "发运日计划-列表" */;
    strTitle2 = nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
        "UPP40140404-000063")/* @res "发运日计划-表单" */;
    strTitle3 = nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
        "UPP40140404-000064")/* @res "订单" */;

    // 设置精度
    setANumItemKeys(new String[] {
      "dassistnum"
    }); // 辅数量
    // 数量
    setNumItemKeys(new String[] {
        "dnum", "dweight", "dvolumn", "ndelivernum", "dsendnum", "doutnum",
        "dsignnum", "dbacknum", "nonwaynum"
    });
    // 价格
    setPriceItemKeys(new String[] {
        "dunitprice", "dmoney"
    });

    // skg
    setDelivOrgNoShow(true);
    super.initialize();
    setCorpID(pk_corp);
    setUserID(operator);
    loadSecCardTemplet();

    ConditionVO[] voCons = new ConditionVO[1];
    voCons[0] = new ConditionVO();
    voCons[0].setFieldCode("pk_delivdaypl");
    voCons[0].setOperaCode("=");
    voCons[0].setValue(id);
    queryBill(new DMDataVO(),voCons);
    BillEditEvent ev = new BillEditEvent(getBillListPanel().getHeadTable(), 0,
        0);
    m_strShowState = DMBillStatus.List;
    bodyRowChange(ev);
    BillEditEvent ev1 = new BillEditEvent(getBillListPanel().getBodyTable(), 0,
        0);
    bodyRowChange(ev1);
    onSwitchForm();

    setPrimaryKeyName("pk_delivdaypl");
    setBillCodeKeyName("vdelivdayplcode");

  }
  
  private void queryBill(DMDataVO voNormal,ConditionVO[] voCons)
  {
	    try {
	      // 查询日计划
	      DMDataVO[] dmdvos = DeliverydailyplanBO_Client.query(voNormal, voCons);

	      if (dmdvos == null) {
	        String[] value = new String[] {
	          "0"
	        };
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
	            "UPP40140404-000110", null, value)/* @res "共有{0}条符合条件的记录！" */);
	      }
	      else {
	        String[] value = new String[] {
	          String.valueOf(dmdvos.length)
	        };
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
	            "UPP40140404-000110", null, value)/* @res "共有{0}条符合条件的记录！" */);
	      }

	      //
	      Object pkdelivdaypl = null;
	      m_allHt = new Hashtable();
	      // ArrayList albodys = new ArrayList();
	      for (int i = 0; i < dmdvos.length; i++) {
	        pkdelivdaypl = dmdvos[i].getAttributeValue("pk_delivdaypl");
	        m_allHt.put(pkdelivdaypl, dmdvos[i]);
	        // albodys.add(dmdvos[i]);
	      }
	      // 动态生成公式并执行
	      execFormulaBodys(dmdvos);
	      // 存货自由项改为公式带出
	      if (dmdvos != null && dmdvos.length > 0) {
	        nc.ui.scm.pub.FreeVOParse freeVOParse = new nc.ui.scm.pub.FreeVOParse();
	        freeVOParse.setFreeVO(dmdvos, null, "pkinv", false);
	      }
	      //
	      for (int i = 0; i < dmdvos.length; i++) {
	        dmdvos[i].setAttributeValue("weightunit", getWeightUnit());
	        dmdvos[i].setAttributeValue("volumnunit", getCapacityUnit());
	      }
	      // 将数据载入列表
	      loadBillDate();
	      m_selectRow = -1;

	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }

	    switchButtonStatus(DMBillStatus.ListView);
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-5-9 16:00:39) 修改日期，修改人，修改原因，注释标志：
   */
  public void initializeNew() {
    setBillTypeCode(DMBillTypeConst.m_delivDelivDayPl); // 发运日计划-列表
    setSecBillTypeCode(DMBillTypeConst.m_delivDelivDayPl); // 发运日计划-表单
    setThdBillTypeCode(DMBillTypeConst.m_delivOrderInDelivDayPl); // 订单
    setNodeCode("40140404");
    setVuserdefCode(DMBillTypeConst.m_delivDelivDayPl);

    strTitle1 = nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
        "UPP40140404-000062")/* @res "发运日计划-列表" */;
    strTitle2 = nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
        "UPP40140404-000063")/* @res "发运日计划-表单" */;
    strTitle3 = nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
        "UPP40140404-000064")/* @res "订单" */;

    // 设置精度
    setANumItemKeys(new String[] {
      "dassistnum"
    }); // 辅数量
    setNumItemKeys(new String[] {
        "dnum", "dweight", "dvolumn", "ndelivernum", "dsendnum", "doutnum",
        "dsignnum", "dbacknum", "nonwaynum","nfeedbacknum"
    });
    // 数量
    setPriceItemKeys(new String[] {
        "dunitprice", "dmoney"
    });
    // 价格

    // 设置精度
    setSourceANumItemKeys(new String[] {
      "dassistnum"
    }); // 辅数量
    setSourceNumItemKeys(new String[] {
        "dnum", "dweight", "dvolumn", "ndelivernum", "dsendnum", "doutnum",
        "dsignnum", "dbacknum", "nonwaynum","nfeedbacknum"
    });
    // 数量
    // setSourcePriceItemKeys(new String[] { "dunitprice", "dmoney" });

    super.initialize();

    loadSecCardTemplet();

    getBillCardPanel().setEnabled(false);
    getBillCardPanel().getBodyItem("bchoose").setEnabled(true);

    if (null != getBillListPanel().getBillListData()) {
      getBillListPanel().addEditListener(this);
      getBillListPanel().getHeadTable().addMouseListener(this);
      getBillListPanel().getBodyTable().addMouseListener(this);
      getBillListPanel().addBodyEditListener(this);
      getBillListPanel().getHeadBillModel().addSortRelaObjectListener(
          new IBillRelaSortListener() {
        	  /**
        	   * 表头排序
        	   */
        	  public List getRelaSortObject() {
        	  	return Arrays.asList(m_headListVOs);
        	  }
          });
      getBillListPanel().getBodyBillModel().addSortRelaObjectListener2(
          new IBillRelaSortListener2() {
        	  /**
        	   * 表体排序
        	   * @return
        	   */
        	  public Object[] getRelaSortObjectArray() {
        	  	if(m_selectRow == -1)
        	  		return null;
        	  	
        	  	Object pkbillh = m_headListVOs[m_selectRow].getAttributeValue("pkbillh");
        	    ArrayList lst = (ArrayList) m_bodyListHt.get(pkbillh);
        	    if (lst != null && lst.size() != 0) {
        	        DMDataVO[] bodyvos = new DMDataVO[lst.size()];
        	        bodyvos = (DMDataVO[]) lst.toArray(bodyvos);
        	        execFormulaBodys(bodyvos);
        	        return bodyvos;
        	    }
        	    return null;
        	  }
          });

    }

    ((DMBillCardPanel) getBillCardPanel())
        .setDataSource(new DeliveryDailyPrint());

    setPrimaryKeyName("pk_delivdaypl");
    setBillCodeKeyName("vdelivdayplcode");
    switchButtonStatus(DMBillStatus.CardView);
  }

  public boolean onClosing() {
    if (boSave.isEnabled()) {
      int result = MessageDialog.showYesNoCancelDlg(this, null,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH001"),
          MessageDialog.ID_YES);/* @res "是否保存已修改的数据？" */
      if (result == MessageDialog.ID_NO) {
        return true;
      }
      else if (result == MessageDialog.ID_YES) {
        onSave();
        return true;
      }
      else if (result == MessageDialog.ID_CANCEL) {
        return false;
      }
    }
    return true;
  }

  /**
   * 加载卡片模板。 创建日期：(2001-11-15 9:03:35)
   */
  public void loadThdCardTemplet() {
    super.loadThdCardTemplet();

    getThdBillCardPanel().setAutoSetRowSelectStatus(true);
    getThdBillCardPanel().setRowSelectStatusKey("bchoose");

    for (int i = 0; i < getThdBillCardPanel().getBodyItems().length; i++) {
      getThdBillCardPanel().getBodyItems()[i].setEnabled(false);
    }
    getThdBillCardPanel().getBodyItem("bchoose").setEdit(true);
    getThdBillCardPanel().getBodyItem("bchoose").setEnabled(true);
  }

  /**
   * Invoked when the mouse has been clicked on a component.
   */
  public void mouseClicked(java.awt.event.MouseEvent e) {
    if (e.getClickCount() > 1) {
      if (e.getSource() == getBillListPanel().getHeadTable()) {
        onBatch();
      }
      else if (e.getSource() == getBillListPanel().getBodyTable()) {
        onSwitchForm();
      }
    }
  }

  /**
   * 文档管理。 创建日期：(2001-12-4 10:56:17)
   */
  public void onDocument() {
    String[] sIDs = null;
    String[] sCodes = null;
    if (m_strShowState.equals(DMBillStatus.Card)) {
      if (getSecBillCardPanel().getHeadItem("pksendtype").isEnabled()) {////huxiaobo modified at 20060828
        showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "SCMCOMMON", "UPPSCMCommon-000016")/* @res "编辑状态不可执行此操作！" */);
        return;
      }
      sIDs = new String[1];
      sCodes = new String[1];
      sIDs[0] = getSecBillCardPanel().getHeadItem(getPrimaryKeyName()).getValue();//huxiaobo modified at 20060828
      sCodes[0] = getSecBillCardPanel().getHeadItem(getBillCodeKeyName())////huxiaobo modified at 20060828
          .getValue();
      if (null == sIDs[0] || sIDs[0].trim().length() == 0 || null == sCodes[0]
          || sCodes[0].trim().length() == 0) {
        showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "40140404", "UPP40140404-000065")/* @res "未有单据ID或单据号！" */);
        return;
      }
    }
    else if (m_strShowState.equals(DMBillStatus.List)) {
      int[] iRows = getBillListPanel().getBodyTable().getSelectedRows();
      if (iRows.length == 0) {
        showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "SCMCOMMON", "UPPSCMCommon-000275")/* @res "未选择行！" */);
        return;
      }
      sIDs = new String[iRows.length];
      sCodes = new String[iRows.length];
      for (int i = 0; i < iRows.length; i++) {
        sIDs[i] = getBillListPanel().getBodyBillModel().getValueAt(iRows[i],
            getPrimaryKeyName()).toString();
        sCodes[i] = getBillListPanel().getBodyBillModel().getValueAt(iRows[i],
            getBillCodeKeyName()).toString();
      }
    }
    nc.ui.scm.file.DocumentManager.showDM(this, sIDs, sCodes);
  }

  /**
   * 函数功能:弹出生成出库单对话框,并设置对话框数据及界面各元素状态 参数:
   * 
   * @param 返回值:
   *          异常: 创建日期：(2003-8-12 8:49:20) 备注: 修改: 作用:
   */
  public void onOutBill() {
    try {
      if (m_selectRow < 0 || m_headListVOs == null || m_headListVOs.length == 0) {
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "40140404", "UPP40140404-000066")/* @res "请选择审批状态日计划！" */);
        return;
      }

      // 采购走发运不允许出库-根据列表表头的相关字段进行判断
      int iSelectedRow = getBillListPanel().getHeadTable().getSelectedRow();
      String vbilltype = (String) getBillListPanel().getHeadBillModel()
          .getValueAt(iSelectedRow, "vbilltype");
      if (vbilltype.equals("21")) {
        nc.ui.pub.beans.MessageDialog
            .showYesNoDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "40140404", "UPP40140404-000067")/* @res "采购走发运不允许出库" */,
                nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
                    "UPP40140404-000067")/* @res "采购走发运不允许出库" */);
        return;
      }

      // 审批前行，审批后切换至
      // int irow = getBillListPanel().getHeadTable().getSelectedRow();
      String sLastPkbillh = m_headListVOs[m_selectRow].getAttributeValue(
          "pkbillh").toString();

      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
          "UPP40140404-000068")/* @res "开始出库！" */);

      // 得到需要出库的日计划数据vo
      ArrayList aryliOutItems = new ArrayList();
      OutbillHHeaderVO[] items = (OutbillHHeaderVO[]) getBillListPanel()
          .getBodyBillModel().getBodyValueVOs(OutbillHHeaderVO.class.getName());
      for (int i = 0; i < items.length; i++) {
        if (items[i].getAttributeValue("bchoose") != null
            && ((UFBoolean) items[i].getAttributeValue("bchoose"))
                .booleanValue()
            && !items[i].getAttributeValue("iplanstatus").toString()
                .equals("0")
            && !items[i].getAttributeValue("iplanstatus").toString()
                .equals("2")) {
          aryliOutItems.add(items[i]);
          /*
           * 与发运方式无关，发运组织为并行时加载出库按钮，即可出库 if (!isOutButtonCanClicked(true,
           * "pksendtype", items[i])) {
           * nc.ui.pub.beans.MessageDialog.showHintDlg( this, "出库",
           * "只有发运方式为用户自提或发运组织允许出库时才可出库"); return; }
           */
        }
      }
      OutbillHHeaderVO[] outitems = new OutbillHHeaderVO[aryliOutItems.size()];
      if (aryliOutItems.size() > 0) {
        outitems = (OutbillHHeaderVO[]) aryliOutItems
            .toArray(new OutbillHHeaderVO[0]);
        aryliOutItems = null;
      }
      // 得到生成出库单的数据并置入生成出库单对话框
      for (int i = 0; i < outitems.length; i++) {
        outitems[i].setAttributeValue("userid", getUserID());
        outitems[i].setAttributeValue("pkcorp", getCorpID());
      }

      // 待更新日计划vo
      ArrayList aryliPlanvos = new ArrayList();// 更新后台用
      ArrayList aryliChoosePlanvos = new ArrayList();// 更新界面用
      DMDataVO[] allplanvos = (DMDataVO[]) getBillListPanel()
          .getBodyBillModel().getBodyValueVOs(DMDataVO.class.getName());
      for (int i = 0; i < allplanvos.length; i++) {
        if (allplanvos[i].getAttributeValue("bchoose") != null
            && ((UFBoolean) items[i].getAttributeValue("bchoose"))
                .booleanValue()
            && !allplanvos[i].getAttributeValue("iplanstatus").toString()
                .equals("0")
            && !allplanvos[i].getAttributeValue("iplanstatus").toString()
                .equals("2")) {
          DMDataVO updatePlanvo = new DMDataVO();
          updatePlanvo.setAttributeValue("pk_delivdaypl", allplanvos[i]
              .getAttributeValue("pk_delivdaypl"));
          updatePlanvo.setStatus(VOStatus.UPDATED);
          updatePlanvo.setAttributeValue("ts", allplanvos[i]
              .getAttributeValue("ts"));
          updatePlanvo.setAttributeValue("userid", getUserID());
          updatePlanvo.setAttributeValue("pkcorp", getCorpID());
          updatePlanvo.setAttributeValue("vbilltype", allplanvos[i]
              .getAttributeValue("vbilltype"));
          aryliPlanvos.add(updatePlanvo);
          aryliChoosePlanvos.add(allplanvos[i]);
        }
      }
      DMDataVO[] updatePlanvos = (DMDataVO[]) aryliPlanvos
          .toArray(new DMDataVO[0]);
      DMDataVO[] choosePlanvos = (DMDataVO[]) aryliChoosePlanvos
          .toArray(new DMDataVO[0]);
      aryliPlanvos = null;
      aryliChoosePlanvos = null;
      if (updatePlanvos == null || updatePlanvos.length < 1) {
        nc.ui.pub.beans.MessageDialog
            .showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "40140404", "UPT40140404-000043")/* @res "出库" */,
                nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
                    "UPP40140404-000066")/* @res "请选择审批状态日计划！" */);
        return;
      }

      //
      nc.vo.dm.dm102.OutbillHVO[] outbills = DeliverydailyplanBO_Client
          .getOnhandnum(outitems, getUserID(), DM011);
      getOutBillDlg().setOutbillHVOs(outbills);
      getOutBillDlg().setPlanvos(updatePlanvos);
      getOutBillDlg().clearListRow();
      getOutBillDlg().getBillListPanel().getHeadTable().clearSelection();
      getOutBillDlg().getBillListPanel().getBodyBillModel().clearBodyData();
      // getOutBillDlg().getBillListPanel().setBodyValueVO(null);
      getOutBillDlg().showModal();

      // 根据用户点击“确定”或者“取消”做相应操作
      if (getOutBillDlg().getResult() == nc.ui.pub.beans.UIDialog.ID_OK) {
        reloadUI(getOutBillDlg().getPlanvos(), choosePlanvos);
        gotoSelectRow(sLastPkbillh);
        // 更新时间戳
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
            "UPP40140404-000069")/* @res "出库成功！" */);
      }
      else {
        // 用户点击“取消”或者关闭对话框
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
            "UPP40140404-000070")/* @res "放弃出库！" */);
      }
    }
    catch (Exception e) {
      //showErrorMessage(e.getMessage());
      handleException(e);
    }
  }

  /**
   * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-7-4 14:42:22) 修改日期，修改人，修改原因，注释标志：
   */
  public void onPrintPreview() {
    if (m_strShowState.equals(DMBillStatus.List)) {

      DMDataVO[] dmdvos = getVOsFromHashtable(m_allHt);
      if (dmdvos == null || dmdvos.length == 0)
        return;

      String sBillPkKey = "pk_delivdaypl";
      String[] oldPrimaryKeys = new String[dmdvos.length];// 用于记录原始的 primaryKey
                                                          // 的值，便于打印后恢复
      for (int i = 0; i < dmdvos.length; i++) {
        oldPrimaryKeys[i] = dmdvos[i].getPrimaryKey();

        // 必须调用方法 dmdvos[i].setPrimaryKey( xx )
        // 以设置 vo 的 billpk, 否则打印的次数不能更新
        dmdvos[i].setPrimaryKey((String) dmdvos[i]
            .getAttributeValue(sBillPkKey));
      }

      DMVO vo = new DMVO();
      vo.setParentVO(new DMDataVO());
      vo.setChildrenVO(dmdvos);

      // ((DMBillCardPanel) getBillCardPanel()).onCardPrint(vo);

      /** ************ Begin print: by zxping ******* */
      ArrayList alBill = new ArrayList();
      alBill.add(vo);
      try {
        BillPrintTool bpt = new BillPrintTool("40140404", // getModuleCode(),
            alBill, getSecBillCardPanel().getBillData(), null, null, null,
            "vdelivdayplcode", sBillPkKey);

        bpt.onCardPrintPreview(getSecBillCardPanel(), getBillListPanel(),
            DMBillTypeConst.m_delivDelivDayPl);

        // 打印后恢复 primaryKey 的值
        for (int i = 0; i < dmdvos.length; i++) {
          dmdvos[i].setPrimaryKey(oldPrimaryKeys[i]);
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      /** ************ End print: by zxping ******* */

      this.setAllVOs(new ArrayList());
    }
  }

  /**
   * 此处插入方法说明。 创建日期：(2002-6-17 20:35:40)
   */
  private void onReOpen() {
    try {
      if (m_selectRow < 0 || m_headListVOs == null || m_headListVOs.length == 0) {
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "40140404", "UPP40140404-000071")/* @res "请选择待打开日计划！" */);
        return;
      }
      // 审批前行，审批后切换至
      // int irow = getBillListPanel().getHeadTable().getSelectedRow();
      String sLastPkbillh = m_headListVOs[m_selectRow].getAttributeValue(
          "pkbillh").toString();
      // 得到界面数据
      Hashtable ht = new Hashtable();
      DMVO dmvo = new DMVO();
      DMDataVO[] dmdvos = (DMDataVO[]) getBillListPanel().getBodyBillModel()
          .getBodyValueVOs(DMDataVO.class.getName());
      // 滤出选择的多行
      ArrayList v = new ArrayList();
      for (int i = 0; i < dmdvos.length; i++) {
    	Object iplanstatus = dmdvos[i].getAttributeValue("iplanstatus");
    	  
        if (dmdvos[i].getAttributeValue("bchoose") != null
            && dmdvos[i].getAttributeValue("bchoose").toString().equals("Y")
            && iplanstatus != null && iplanstatus.toString().length() > 0
        	  && new Integer(iplanstatus.toString()).intValue() == DailyPlanStatus.End) {
          dmdvos[i].setAttributeValue("iplanstatus", new Integer(
              DailyPlanStatus.Audit));
          dmdvos[i].setAttributeValue("pkcorp", getCorpID());
          dmdvos[i].setAttributeValue("userid", getOperator()); // 操作人
          dmdvos[i].setStatus(VOStatus.UPDATED);
          ht.put("pk_delivdaypl", dmdvos[i].getAttributeValue("pk_delivdaypl"));
          v.add(dmdvos[i]);
        }
      }
      if (v.size() == 0) {
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "40140404", "UPP40140404-000071")/* @res "请选择待打开日计划！" */);
        return;
      }
      DMDataVO[] savevos = new DMDataVO[v.size()];
      savevos = (DMDataVO[]) v.toArray(savevos);
      v = null;

      // int iATPYN = UIDialog.ID_YES;
      DMDataVO[] retVos = null;
      // try {
      // for (int i = 0; i < savevos.length; i++) {
      // savevos[i].setAttributeValue("ischeckatp","Y");
      // }
      retVos = DeliverydailyplanBO_Client.reopen(savevos);
      // }
      // catch (ATPNotEnoughException e) {
      // iATPYN = showYesNoMessage(e.getMessage());
      // if(iATPYN == UIDialog.ID_YES) {
      // for (int i = 0; i < savevos.length; i++) {
      // savevos[i].setAttributeValue("ischeckatp","N");
      // }
      // retVos = DeliverydailyplanBO_Client.reopen(savevos);
      // }
      // else return;
      // }
      // DMDataVO[] retVos = DeliverydailyplanBO_Client.reopen(savevos);
      // 重置回写上游数量
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH012"));/* 已打开 */
      setButton(boEnd, true);
      if (DM016.booleanValue()) {
        for (int i = 0; i < savevos.length; i++) {
          savevos[i].setAttributeValue("nfeedbacknum", savevos[i]
              .getAttributeValue("dnum"));
        }
      }
      // 重置界面
      reloadUI(retVos, savevos);

      // 切至选中行
      gotoSelectRow(sLastPkbillh);

    }
    catch (Exception e) {
      e.printStackTrace();
      if (e instanceof BusinessException)
        showErrorMessage(e.getMessage());
      else
        showErrorMessage(e.getMessage());
    }
  }

  /**
   * 切换到日计划列表界面
   * 
   * @author zhongyue
   */
  public void onSwitchList() {
    super.onSwitchList();
    if (m_selectRow >= 0
        && m_selectRow < getBillListPanel().getHeadTable().getRowCount()) {
      getBillListPanel().getHeadTable().getSelectionModel()
          .setSelectionInterval(m_selectRow, m_selectRow);
      BillEditEvent e = new BillEditEvent(getBillListPanel().getHeadTable(), 0,
          m_selectRow);
      bodyRowChange(e);
    }
    m_bcardpagestatus = true;
  }

  public void paint(java.awt.Graphics g) {
    super.paint(g);

    /*
     * if (!showed) { final java.awt.Container con =
     * javax.swing.SwingUtilities.getRootPane(this); showed = true;
     * javax.swing.SwingUtilities.invokeLater(new Thread() { public void run() {
     * //SCMEnv.info("######" + // System.currentTimeMillis());
     * //getBillListPanel().getParentListPanel().getTable().requestFocus();
     * //try { //sleep(1000); //} catch (Exception e) { //} ButtonBar bb =
     * (ButtonBar) nc.ui.pub.beans.util.MiscUtils.findChildByClass(con,
     * ButtonBar.class); if (bb != null) { int count = bb.getComponentCount();
     * for (int i = 0; i < count; i++) { if (bb.getComponent(i) instanceof
     * nc.ui.pub.MenuButton && bb.getComponent(i).isEnabled()) {
     * bb.getComponent(i).requestFocus(); break; } } } //bb.transferFocus(); }
     * }); }
     */
  }

  /**
   * 排序后触发。 创建日期：(2001-10-26 14:31:14)
   * 
   * @param key
   *          java.lang.String
   */
  private void afterSortBody(java.lang.String key) {
    // clearOrientColor();
    // 如果是表头排序
    nc.vo.scm.pub.SCMEnv.out(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "40140404", "UPP40140404-000072")/* @res "表体排序" */);
    if (m_planvos == null || m_planvos.length <= 1) {
      // 说明没有排序的必要
      return;
    }
    // 原行号
    int row = getBillListPanel().getBodyTable().getSelectedRow();
    int newrow = 0;

    if (row < 0 || row >= m_planvos.length)
      row = 0;
    int[] iSortIndex = getBillListPanel().getBodyBillModel().getSortIndex();

    // 对数组进行重新排序
    if (iSortIndex == null || iSortIndex.length == 0
        || iSortIndex.length != m_planvos.length) {
      nc.vo.scm.pub.SCMEnv.out(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40140404", "UPP40140404-000073")/* @res "排序返回数组错误。" */);
      return;
    }
    // 临时存放
    ArrayList alNewArray = new ArrayList();
    String sNewSort = "";
    for (int i = 0; i < iSortIndex.length; i++) {
      if (row == iSortIndex[i])
        newrow = i;

      alNewArray.add(((DMDataVO) m_planvos[iSortIndex[i]]).clone());
      if (i > 0 && i % 10 == 0)
        sNewSort = sNewSort + "\n";
      sNewSort += iSortIndex[i]
          + nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
              "UPPSCMCommon-000000")/* @res "、" */;
    }
    nc.vo.scm.pub.SCMEnv.out("new sort=" + sNewSort);
    // 重置数据
    m_num = newrow;
    m_planvos = (DMDataVO[]) alNewArray.toArray(new DMDataVO[0]);
    getBillListPanel().setBodyValueVO(m_planvos);
    getBillListPanel().getBodyTable().getSelectionModel().setSelectionInterval(
        m_num, m_num);
  }

  /**
   * 排序后触发。 创建日期：(2001-10-26 14:31:14)
   * 
   * @param key
   *          java.lang.String
   */
  private void afterSortHead(java.lang.String key) {
    // clearOrientColor();
    // 如果是表头排序
    nc.vo.scm.pub.SCMEnv.out(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "40140404", "UPP40140404-000074")/* @res "表头排序" */);
    if (m_headListVOs == null || m_headListVOs.length <= 1) {
      // 说明没有排序的必要
      return;
    }
    // 原行号
    int row = getBillListPanel().getHeadTable().getSelectedRow();
    int newrow = 0;

    if (row < 0 || row >= m_headListVOs.length)
      row = 0;
    int[] iSortIndex = getBillListPanel().getHeadBillModel().getSortIndex();

    // 对数组进行重新排序
    if (iSortIndex == null || iSortIndex.length == 0
        || iSortIndex.length != m_headListVOs.length) {
      nc.vo.scm.pub.SCMEnv.out(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40140404", "UPP40140404-000073")/* @res "排序返回数组错误。" */);
      return;
    }
    // 临时存放
    ArrayList alNewArray = new ArrayList();
    String sNewSort = "";
    for (int i = 0; i < iSortIndex.length; i++) {
      if (row == iSortIndex[i]) {
        newrow = i;
      }
      alNewArray.add(((DMDataVO) m_headListVOs[iSortIndex[i]]).clone());
      if (i > 0 && i % 10 == 0) {
        sNewSort = sNewSort + "\n";
      }
      sNewSort += iSortIndex[i]
          + nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
              "UPPSCMCommon-000000")/* @res "、" */;
    }
    nc.vo.scm.pub.SCMEnv.out("new sort=" + sNewSort);
    // 重置数据
    m_selectRow = newrow;
    m_headListVOs = (DMDataVO[]) alNewArray.toArray(new DMDataVO[0]);
    getBillListPanel().setHeaderValueVO(m_headListVOs);
    getBillListPanel().getHeadTable().getSelectionModel().setSelectionInterval(
        m_selectRow, m_selectRow);
  }

  public void columnSelectionChanged(javax.swing.event.ListSelectionEvent e) {
    if (m_bIsBatch) {
      int colnow = getBatchBillCardPanel().getBillTable().getSelectedColumn();
      int rownow = getBatchBillCardPanel().getBillTable().getSelectedRow();
      if (colnow >= 0 && rownow >= 0) {
        initRef(rownow, colnow, (DMBillCardPanel) getBatchBillCardPanel());
        String key = getBatchBillCardPanel().getBodyShowItems()[colnow]
            .getKey();
        Object value = getBatchBillCardPanel().getBodyValueAt(rownow, key);
        whenEntered(rownow, colnow, key, value, getBatchBillCardPanel());
      }
    }
  }

  /**
   * 创建者：仲瑞庆 功能：产生辅计量 参数： 返回： 例外： 日期：(2001-5-16 下午 6:32) 修改日期，修改人，修改原因，注释标志：
   */
  private void filterMeasBatch(String sInvID, String sCastUnitNameField,
      String pkcorp) {

    // nc.vo.ic.pub.bill.InvVO voInv= getInvInfo(sInvID, null);

    nc.ui.pub.beans.UIRefPane refCast = (nc.ui.pub.beans.UIRefPane) getBatchBillCardPanel()
        .getBodyItem(sCastUnitNameField).getComponent();

    refCast.setReturnCode(false);

    m_voInvMeas.filterMeas(pkcorp, sInvID, refCast);
    // m_voInvMeas.filterMeas(getCorpID(), voInv.getCinventoryid(), refCast);

  }

  /**
   * 列表表头数据项定义。 功能： 参数： 返回： 例外： 日期：(2002-9-16 15:06:12) 修改日期，修改人，修改原因，注释标志：
   */
  public BillItem[] getBillCardHeadItemsBatch(BillData bd) {
    BillItem[] items = new BillItem[7];
    final String[] saKey = new String[]{"pkbillh","vbilltypename","vsrcbillnum","vsendtypename",
    		                             "vbilltype","vcustname","vdeststoreorgname"};   
    final String[] saName = new String[]{nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
                                          "UPP40140404-000061")/* @res "订单表头主键" */,
                                          nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                                          "UC000-0003556")/* @res "订单类型" */,
                                          nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                                          "UC000-0003534")/* @res "订单号" */,
                                          nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                                          "UC000-0001024")/* @res "发运方式" */,
                                          nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
                                          "UPT40140404-000002")/* @res "订单类型编码" */,
                                          nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                                          "UC000-0001589")/* @res "客户" */,
                                          nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                                          "UC000-0000645")/* @res "到货库存组织" */};
    for (int i = 0; i < 7; i++)
    {
      items[i] = getDefaultHeadItem(saKey[i], saName[i]);
      //特殊处理：为和从表体转向表头的items的tablecode保持一致，此处设置tablecode为系统默认的表体tablecode
      items[i].setTableCode(BillData.DEFAULT_BODY_TABLECODE);
    }
    items[0].setShow(false);//订单表头主键 不显示
    items[1].setLoadFormula(new String[] {
          "vbilltypename->getColValueRes(bd_billtype,billtypename,pk_billtypecode,vbilltype)"
        });
    items[4].setShow(false);//发运方式 不显示
    items[5].setShow(false);//订单类型编码 不显示

    // for (int i = 7; i < 17; i++) {
    // items[i].setName(nc.ui.ml.NCLangRes.getInstance().getStrByID("40140404",
    // "UPP40140404-000060")/*@res "自定义项"*/+ (i - 7));
    // items[i].setKey("vuserdef" + (i - 7));
    // items[i].setWidth(80);
    // items[i].setEdit(true);
    // items[i].setShow(bd.getBodyItem("vuserdef" + (i - 7)).isShow());
    // items[i].setShowOrder(i);
    // items[i].setPos(0);
    // }

    // items[7].setName("简称");
    // items[7].setKey("custshortname");
    // items[7].setDataType(0);
    // items[7].setWidth(80);
    // items[7].setEdit(false);
    // //((UIRefPane) items[2].getComponent()).setReturnCode(true);
    // items[8].setName("纳税人登记号");
    // items[8].setKey("taxpayerid");
    // items[8].setDataType(0);
    // items[8].setWidth(80);
    // items[8].setEdit(false);
    java.util.ArrayList alHeadItems = new java.util.ArrayList();
    alHeadItems.addAll(Arrays.asList(bd.getHeadItems()));
    alHeadItems.addAll(Arrays.asList(items));
    
    BillItem[] retItems = (BillItem[]) alHeadItems.toArray(new BillItem[0]);
    return retItems;
  }
  private BillItem getDefaultHeadItem(String sKey,String sName)
  {
	    BillItem item = new BillItem();
	    item.setKey(sKey);
	    item.setName(sName);
	    item.setDataType(IBillItem.STRING);
	    item.setWidth(1);
	    item.setEdit(false);
	    item.setShow(true);
	    item.setPos(IBillItem.HEAD);
	    item.setTableName(null);
	    return item;
  }

  /**
   * 如果用户手工修改批次号，则查库，正确带出失效日期及对应单据号，不正确，清空。 创建者：张欣 功能： 参数： 返回： 例外： 日期：(2001-6-14
   * 10:25:33) 修改日期，修改人，修改原因，注释标志：
   */
  protected void getHeaderLotRefbyHand(String ColName, DMBillCardPanel dmbcp) {
    if (ColName == null) {
      return;
    }
    if (null == dmbcp.getHeadItem(ColName))
      return;
    String sbatchcode = ((nc.ui.ic.pub.lot.LotNumbRefPane) dmbcp.getHeadItem(
        ColName).getComponent()).getText();
    // if
    // (((LotNumbRefPane)dmbcp.getHeadItem(ColName).getComponent()).getLotNumbRefVO()
    // != null)
    // sbatchcode=
    // ((LotNumbRefPane)dmbcp.getHeadItem(ColName).getComponent()).getLotNumbRefVO().getVbatchcode();
    /** 当批次号为空， */
    if ((sbatchcode == null || sbatchcode.trim().length() <= 0)
        && getLotNumbRefPaneHead().isClicked())
      return;
    /** 用户手工填写批次号时，查库，检查输入的正确与否？ */
    boolean isLotRight = getLotNumbRefPaneHead().checkData();

    if (!isLotRight && !dmbcp.getHeadItem("vbilltype").getValue().equals("21")) {
      dmbcp.getHeadItem(ColName).setValue(null);
      getLotNumbRefPaneHead().setValue(null);
    }

  }

  /**
   * ?user> 功能： 参数： 返回： 例外： 日期：(2005-1-19 16:06:15) 修改日期，修改人，修改原因，注释标志：
   * 
   * @param sPkBillh
   *          java.lang.String
   */
  private void gotoSelectRow(String sPkBillh) {
    // 重新选中行
    if (m_selectRow >= 0) {
      for (int i = 0; i < m_headListVOs.length; i++) {
        String sCurPkbillh = m_headListVOs[i].getAttributeValue("pkbillh")
            .toString();
        if (sPkBillh.equals(sCurPkbillh)) {
          m_selectRow = i;
        }
      }
      getBillListPanel().getHeadTable().getSelectionModel()
          .setSelectionInterval(m_selectRow, m_selectRow);
      BillEditEvent e = new BillEditEvent(getBillListPanel().getHeadTable(), 0,
          m_selectRow);
      bodyRowChange(e);
      // getBillListPanel().getHeadTable().s
    }
  }

  /**
   * 函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。 表体批次号初始化
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @author zhongyue
   * @param row
   *          int
   * @param col
   *          int
   */
  public void initBodyLot(int row, int col, DMBillCardPanel dmbcp) {
    try {
      // 取值
      InvVO invvo = getBodyInvVOFromPanel(row, col, dmbcp);

      // 向批次号参照传入数据
      // 初始化
      nc.vo.scm.ic.bill.WhVO whvo = queryWhInfo((String) dmbcp.getBodyValueAt(
          row, "pksendstore"));
      getLotNumbRefPane().setParameter(whvo, invvo);
      // getLotNumbRefPane().setEnabled(true);

      getLotNumbRefPane().setValue(
          (String) dmbcp.getBodyValueAt(row, "vbatchcode"));
    }
    catch (Exception e) {
    }
  }

  private void setOtherItemEditableBatch(DMBillCardPanel bcp) {
    // /对来源单据为采购订单的发运日计划、发运单修改时，发货仓库不能参照录入；
    // 发货库存组织在任何时候一直不能编辑
    // 日计划、发运单审批时不需校验上述两字段是否为空。
    // UFBoolean ufb = (UFBoolean) dmdvo.getAttributeValue("borderreturn");
    String vBillType = (String) bcp.getBodyValueAt(0, "vbilltype");
    // boolean borderreturn = false;
    // if (ufb != null)
    // borderreturn = ufb.booleanValue();
    if (vBillType.equals("21")) {
      getBatchBillCardPanel().getHeadItem("pksendstoreorg").setEnabled(false);
      getBatchBillCardPanel().getHeadItem("pksendstore").setEnabled(false);
      getBatchBillCardPanel().getBodyItem("vsendstoreorgname")
          .setEnabled(false);
      getBatchBillCardPanel().getBodyItem("vsendstoreorgname").setEdit(false);
      getBatchBillCardPanel().getBodyItem("vsendstorename").setEnabled(false);
      getBatchBillCardPanel().getBodyItem("vsendstorename").setEdit(false);
      getBatchBillCardPanel().getBodyItem("vbatchcode").setEnabled(false);
      getBatchBillCardPanel().getBodyItem("vbatchcode").setEdit(false);
      getBatchBillCardPanel().getBodyItem("creceiptcorpname").setEnabled(true);
      getBatchBillCardPanel().getBodyItem("creceiptcorpname").setEdit(true);
    }
    else {
      getBatchBillCardPanel().getHeadItem("pksendstoreorg").setEnabled(true);
      getBatchBillCardPanel().getHeadItem("pksendstore").setEnabled(true);
      getBatchBillCardPanel().getBodyItem("vsendstoreorgname").setEnabled(true);
      getBatchBillCardPanel().getBodyItem("vsendstoreorgname").setEdit(true);
      getBatchBillCardPanel().getBodyItem("vsendstorename").setEnabled(true);
      getBatchBillCardPanel().getBodyItem("vsendstorename").setEdit(true);
      getBatchBillCardPanel().getBodyItem("vbatchcode").setEnabled(true);
      getBatchBillCardPanel().getBodyItem("vbatchcode").setEdit(true);
      if (vBillType.equals("30")) {
        getBatchBillCardPanel().getBodyItem("creceiptcorpname")
            .setEnabled(true);
        getBatchBillCardPanel().getBodyItem("creceiptcorpname").setEdit(true);
      }
      else {
        getBatchBillCardPanel().getBodyItem("creceiptcorpname").setEnabled(
            false);
        getBatchBillCardPanel().getBodyItem("creceiptcorpname").setEdit(false);
      }
    }
    getBatchBillCardPanel().getBodyItem("pkassistmeasure").setEnabled(false);
    getBatchBillCardPanel().getBodyItem("pkassistmeasure").setEdit(false);
    getBatchBillCardPanel().getBodyItem("vassistmeaname").setEnabled(false);
    getBatchBillCardPanel().getBodyItem("vassistmeaname").setEdit(false);
  }

  public ButtonObject[] getExtendBtns() {
    return null;
  }

  public void onExtendBtnsClick(ButtonObject bo) {
  }

  public void setExtendBtnsStat(int iState) {

  }

  // 由于需要支持二次开发接口nc.ui.scm.pub.bill.IBillExtendFun(，因此做下面的调整
  // 2005-09-23 v31sp1 钟鸣修改
  public ButtonObject[] getBillButtons() {
    ButtonObject[] buttons = super.getBillButtons();
    ButtonObject[] extraButtons = getExtendBtns();
    if (extraButtons != null && extraButtons.length > 0) {
      int buttonSize = buttons.length;
      int extraSize = extraButtons.length;
      int size = buttonSize + extraSize;

      ButtonObject[] allButtons = new ButtonObject[size];
      System.arraycopy(aryButtonGroup, 0, allButtons, 0, buttonSize);
      System.arraycopy(extraButtons, 0, allButtons, buttonSize, extraSize);
      buttons = allButtons;
    }
    return buttons;
  }

  public void doQueryAction(ILinkQueryData querydata) {
    String billid = querydata.getBillID();
    String pk_corp = querydata.getPkOrg();
    String billtype = querydata.getBillType();
    Object[] objs = (Object[]) querydata.getUserObject();
    String userid = objs[2].toString();
    initialize(pk_corp, billtype, null,
    		userid, billid); 
  }
  
  public String matchpk_Corp(String pkDelivorg){
	    DelivorgVO vo = null;
	    try{
	     vo = DelivorgHelper.findByPrimaryKey(pkDelivorg);
	    }catch(Exception e){
	      e.printStackTrace();
	    }
	    DelivorgHeaderVO vos = (DelivorgHeaderVO)vo.getParentVO();
	    String pkcorp = vos.getPkcorp();
	    return pkcorp;
 }

}
