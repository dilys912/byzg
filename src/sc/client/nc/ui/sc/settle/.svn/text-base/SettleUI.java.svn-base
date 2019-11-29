package nc.ui.sc.settle;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.baoyin.common.PubDelegator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ml.NCLangRes;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillBodyMenuListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.formulaparse.FormulaParse;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.print.PrintEntry;
import nc.ui.sc.adjust.AdjustbillHelper;
import nc.ui.sc.pub.BillEdit;
import nc.ui.sc.pub.PublicHelper;
import nc.ui.sc.pub.ScPrint;
import nc.ui.sc.pub.ScTool;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.pub.CacheTool;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.mm.pub.pub1020.DisConditionVO;
import nc.vo.mm.pub.pub1020.DisassembleVO;
import nc.vo.ps.estimate.EstimateVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.formulaset.util.StringUtil;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sc.pub.ScConstants;
import nc.vo.sc.settle.BalanceVO;
import nc.vo.sc.settle.MaterialHeaderVO;
import nc.vo.sc.settle.MaterialVO;
import nc.vo.sc.settle.MaterialledgerVO;
import nc.vo.sc.settleCltj.SettleCltjVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;

public class SettleUI extends ToftPanel
  implements BillBodyMenuListener
{
  private ButtonObject boAddLine = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000012"), NCLangRes.getInstance().getStrByID("common", "UC001-0000012"), 0, "增行");

  private ButtonObject boCancel = new ButtonObject(NCLangRes.getInstance().getStrByID("401205", "UPT401205-000025"), NCLangRes.getInstance().getStrByID("common", "UC001-0000008"), 0, "取消");

  private ButtonObject boCancelVerify = new ButtonObject(NCLangRes.getInstance().getStrByID("401205", "UPT401205-000023"), NCLangRes.getInstance().getStrByID("401205", "UPP401205-000035"), 0, "反核销");

  private ButtonObject boCopyLine = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000014"), NCLangRes.getInstance().getStrByID("common", "UC001-0000014"), 0, "复制行");

  private ButtonObject boDelLine = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000013"), NCLangRes.getInstance().getStrByID("common", "UC001-0000013"), 0, "删行");

  private ButtonObject boEdit = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000045"), NCLangRes.getInstance().getStrByID("401205", "UPP401205-000037"), 3, "修改");

  private ButtonObject boFrash = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000009"), NCLangRes.getInstance().getStrByID("common", "UC001-0000009"), 0, "刷新");

  private ButtonObject boInsertLine = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000016"), NCLangRes.getInstance().getStrByID("common", "UC001-0000016"), 0, "插入行");

  private ButtonObject boLineOperator = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000011"), NCLangRes.getInstance().getStrByID("common", "UC001-0000011"), 0, "行操作");

  private ButtonObject boLocate = new ButtonObject(NCLangRes.getInstance().getStrByID("401205", "UPT401205-000021"), NCLangRes.getInstance().getStrByID("401205", "UPT401205-000021"), 0, "定位");

  private ButtonObject boPasteLine = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000015"), NCLangRes.getInstance().getStrByID("common", "UC001-0000015"), 0, "粘贴行");

  private ButtonObject boPrint = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), NCLangRes.getInstance().getStrByID("401205", "UPP401205-000040"), 0, "打印");

  private ButtonObject boQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), 0, "查询");

  private ButtonObject boReturn = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000186"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000186"), 0, "切换");

  private ButtonObject boSave = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000001"), NCLangRes.getInstance().getStrByID("401205", "UPP401205-000042"), 3, "保存");

  private ButtonObject boSelectAll = new ButtonObject(NCLangRes.getInstance().getStrByID("401206", "UPT401206-000027"), NCLangRes.getInstance().getStrByID("401206", "UPT401206-000027"), 0, "全部选择");

  private ButtonObject boSelectNo = new ButtonObject(NCLangRes.getInstance().getStrByID("401206", "UPT401206-000028"), NCLangRes.getInstance().getStrByID("401206", "UPT401206-000028"), 0, "全部取消");

  private ButtonObject boVerify = new ButtonObject(NCLangRes.getInstance().getStrByID("401205", "UPT401205-000022"), NCLangRes.getInstance().getStrByID("401205", "UPP401205-000043"), 0, "核销");
  //add by shikun 材料挑拣、核销参照
  private ButtonObject boCLTJ = new ButtonObject("材料挑拣", "材料挑拣", 0, "材料挑拣");
  private ButtonObject boHXCZ = new ButtonObject("核销参照", "核销参照", 0, "核销参照");
  //add by shikun 材料挑拣、核销参照
  private ButtonObject[] aryButtonGroup = { this.boEdit, this.boSave,this.boLineOperator/*add by shikun 行操作, this.boDelLine*/, this.boCancel, this.boVerify, this.boCancelVerify, this.boReturn,boCLTJ,boHXCZ/*add by shikun 材料挑拣,核销参照*/ };

  private ButtonObject[] aryListButtonGroup = { this.boQuery, this.boVerify, this.boCancelVerify, this.boFrash, this.boReturn };
  private boolean bWastControl;
  private CardLayout cardLayout = null;
  private SettleCardPanel m_BillCardPanel;
  GeneralBillHeaderVO[] m_billHeaderVO = null;

  private int m_billState = ScConstants.STATE_LIST;

  private boolean m_bListSwitchCard = false;

  private boolean m_bVerified = false;

  private boolean m_bBalanced = false;
  private MaterialVO m_curBillVO;
  MaterialHeaderVO[] m_heads = null;

  private int m_iCurBillIndex = 0;

  private SettleQryDlg m_query = null;
  private SettleListPanel m_SettleListPanel;
  private Vector m_vIDs;
  private int numPrecision;

  public SettleUI()
  {
    init();
  }

  private boolean checkVbatch()
  {
    try
    {
      int rownum = getBillCardPanel().getRowCount();
      boolean flag = true;
      String errMsg = "";
      for (int i = 0; i < rownum; i++) {
        Object value = getBillCardPanel().getBodyValueAt(i, "bismaterial");

        if ((value == null) || (value.toString().trim().equals(""))) {
          String cmaterialid = getBillCardPanel().getBodyValueAt(i, "cmangid") == null ? null : getBillCardPanel().getBodyValueAt(i, "cmangid").toString();

          String vbatch = getBillCardPanel().getBodyValueAt(i, "vbatch") == null ? null : getBillCardPanel().getBodyValueAt(i, "vbatch").toString();

          if ((cmaterialid == null) || (cmaterialid.trim().length() <= 0))
          {
            continue;
          }

          Object[] o = (Object[])(Object[])CacheTool.getCellValue("bd_invmandoc", "pk_invmandoc", "wholemanaflag", cmaterialid);

          if ((o == null) || (o.length <= 0) || (o[0] == null) || (!o[0].toString().equals("Y")) || ((vbatch != null) && (vbatch.toString().trim().length() != 0)))
          {
            continue;
          }

          errMsg = errMsg + (errMsg.trim().length() > 0 ? "," + (i + 1) : new StringBuilder().append("").append(i + 1).toString());
        }

      }

      if (errMsg.trim().length() > 0) {
        showErrorMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000044", null, new String[] { errMsg }));

        return false;
      }
      return flag;
    }
    catch (BusinessException be) {
      SCMEnv.out(be.getMessage());
    }return true;
  }

  private boolean checkWastControl()
  {
    if (!this.bWastControl) {
      return true;
    }
    int rowcount = getBillCardPanel().getRowCount();

    Object currSLobj = null;
    UFDouble processSL = new UFDouble(0);

    for (int i = 0; i < rowcount; i++) {
      Object value = getBillCardPanel().getBodyValueAt(i, "bismaterial");
      currSLobj = getBillCardPanel().getBodyValueAt(i, "nnum");
      if ((value != null) && (value.toString().trim().indexOf(ScConstants.PROCESSFLAG) >= 0))
      {
        processSL = currSLobj == null ? new UFDouble(0) : new UFDouble(currSLobj.toString());
      }
      else
      {
        UFDouble mateSL = currSLobj == null ? new UFDouble(0) : new UFDouble(currSLobj.toString());

        Object existSL = getBillCardPanel().getBodyValueAt(i, "nmatenum");
        UFDouble unitProcMateSL = existSL == null ? new UFDouble(0) : new UFDouble(existSL.toString());

        UFDouble limitMateSL = processSL.multiply(unitProcMateSL).multiply(new UFDouble(1.005));//add by shikun 因为6位和8位小数的问题：制盖容许0.5%的范围 

        if (mateSL.compareTo(limitMateSL) > 0) {
          showErrorMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000045", null, new String[] { i + 1 + "", limitMateSL + "" }));

          return false;
        }
      }
    }
    return true;
  }

  private void editProcessRow(int curProcessRow, int lastMaterialRow)
  {
    if ((curProcessRow < 0) || (lastMaterialRow < 0))
      return;
    UFDouble totalMny = new UFDouble(0);
    for (int i = curProcessRow + 1; i < lastMaterialRow + 1; i++) {
      Object money = getBillCardPanel().getBodyValueAt(i, "nmny");
      if ((money != null) && (!money.toString().trim().equals(""))) {
        UFDouble a = new UFDouble(money.toString());
        totalMny = totalMny.add(a);
      }
    }

    getBillCardPanel().setBodyValueAt(totalMny, curProcessRow, "nmny");
    Object number = getBillCardPanel().getBodyValueAt(curProcessRow, "nnum");

    if ((number != null) && (!number.toString().trim().equals(""))) {
      UFDouble nnum = new UFDouble(number.toString());
      if (nnum.doubleValue() != 0.0D)
        getBillCardPanel().setBodyValueAt(totalMny.div(nnum), curProcessRow, "nprice");
    }
  }

  private SettleCardPanel getBillCardPanel()
  {
    if (this.m_BillCardPanel == null) {
      try {
        this.m_BillCardPanel = new SettleCardPanel(this);

        this.m_BillCardPanel.addBodyMenuListener(this);
      }
      catch (Throwable ivjExc) {
        this.m_BillCardPanel = null;
        handleException(ivjExc);
      }
    }
    return this.m_BillCardPanel;
  }

  private SettleListPanel getBillListPanel()
  {
    if (this.m_SettleListPanel == null) {
      try {
        this.m_SettleListPanel = new SettleListPanel(this);
      } catch (Throwable ivjExc) {
        this.m_SettleListPanel = null;
        handleException(ivjExc);
      }
    }
    return this.m_SettleListPanel;
  }

  private String getCvendorbaseid(String cvendormangid)
  {
    String cvendorbaseid = null;
    try {
      cvendorbaseid = PublicHelper.getCvendorbaseid(cvendormangid);
    } catch (Exception e) {
      SCMEnv.out(e);
    }
    return cvendorbaseid;
  }

  private EstimateVO[] getEstimateVOs(MaterialVO billVO)
    throws Exception
  {
    MaterialHeaderVO headVO = (MaterialHeaderVO)this.m_curBillVO.getParentVO();
    MaterialledgerVO[] itemsVO = (MaterialledgerVO[])(MaterialledgerVO[])this.m_curBillVO.getChildrenVO();

    Object cprocessmangid = ""; Object cprocessbaseid = ""; Object cbill_bid = "";
    int rowCount = getBillCardPanel().getRowCount();

    String[] bb3ID = null;
    Vector vTemp = new Vector();
    try {
      for (int i = 0; i < rowCount; i++) {
        Object value = getBillCardPanel().getBodyValueAt(i, "bismaterial");

        if ((value == null) || (value.toString().trim().equals("")))
          continue;
        cbill_bid = getBillCardPanel().getBodyValueAt(i, "cbill_bid");
        vTemp.addElement(cbill_bid.toString());
      }

      if (vTemp.size() > 0) {
        bb3ID = new String[vTemp.size()];
        vTemp.copyInto(bb3ID);
        bb3ID = MaterialledgerHelper.getIc_bb3info(bb3ID);
      }
    } catch (Exception e) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000046"));

      throw e;
    }

    Vector vecItems = new Vector();
    int j = 0;
    for (int i = 0; i < rowCount; i++) {
      Object value = getBillCardPanel().getBodyValueAt(i, "bismaterial");
      if ((value == null) || (value.toString().trim().equals(""))) {
        continue;
      }
      cprocessmangid = getBillCardPanel().getBodyValueAt(i, "cmangid");
      cprocessbaseid = getBillCardPanel().getBodyValueAt(i, "cbaseid");
      cbill_bid = getBillCardPanel().getBodyValueAt(i, "cbill_bid");

      EstimateVO estimateVO = new EstimateVO();

      estimateVO.setPk_corp(headVO.getPk_corp());

      estimateVO.setDbilldate(headVO.getDbilldate());

      estimateVO.setVbillcode(headVO.getCbillcode());

      estimateVO.setCwarehouseid(headVO.getCwarehouseid());

      estimateVO.setCproviderbaseid(headVO.getCvendorid());
      estimateVO.setCprovidermangid(headVO.getCvendormangid());

      estimateVO.setCgeneralbid(cbill_bid.toString());
      estimateVO.setCgeneralhid(headVO.getCbillid().toString());

      estimateVO.setCbaseid(cprocessbaseid.toString());
      estimateVO.setCmangid(cprocessmangid.toString());

      Object nnum = getBillCardPanel().getBodyValueAt(i, "nnum");
      estimateVO.setNinnum(nnum == null ? null : new UFDouble(nnum.toString()));

      Object nprocessmny = getBillCardPanel().getBodyValueAt(i, "nprocessmny");

      estimateVO.setNprice(nprocessmny == null ? null : new UFDouble(nprocessmny.toString()));

      UFDouble nmny = estimateVO.getNinnum() == null ? new UFDouble(0) : estimateVO.getNinnum();

      nmny = nmny.multiply(estimateVO.getNprice() == null ? new UFDouble(0) : estimateVO.getNprice());

      estimateVO.setNmoney(nmny);

      Object nmatemny = getBillCardPanel().getBodyValueAt(i, "nmny");
      estimateVO.setNmaterialmoney(nmatemny == null ? null : new UFDouble(nmatemny.toString()));

      estimateVO.setCbilltypecode("47");

      estimateVO.setCcalbodyid(headVO.getCwareid());

      estimateVO.setCdptid(headVO.getCdeptid());

      estimateVO.setCoperatorid(headVO.getCemployeeid());

      estimateVO.setCbiztype(this.m_billHeaderVO[this.m_iCurBillIndex].getCbiztypeid());

      estimateVO.setVbatchcode(itemsVO[i].getVbatch());

      estimateVO.setDbizdate(itemsVO[i].getDbizdate());

      if ((bb3ID != null) && (bb3ID.length > j)) {
        String s = bb3ID[j];
        estimateVO.setCgeneralbb3(s == null ? null : s);
        j++;
      }

      vecItems.addElement(estimateVO);
    }

    EstimateVO[] estimateVOs = new EstimateVO[vecItems.size()];
    if (vecItems.size() > 0) {
      vecItems.copyInto(estimateVOs);
    }
    return estimateVOs;
  }

  public String getModuleCode()
  {
    return "401205";
  }

  public String getOperatorID()
  {
    return ClientEnvironment.getInstance().getUser().getPrimaryKey();
  }

  public String getPk_corp()
  {
    return ClientEnvironment.getInstance().getCorporation().getPk_corp();
  }

  private String[] getProcessMangID(String pk_corp, String[] cbaseid)
  {
    String[] cmangid = null;
    try {
      cmangid = PublicHelper.getProcessMangID(pk_corp, cbaseid);
    } catch (Exception e) {
      SCMEnv.out(e);
    }
    return cmangid;
  }

  private SettleQryDlg getQryCondition()
  {
    if (this.m_query == null) {
      try
      {
        this.m_query = new SettleQryDlg(this, NCLangRes.getInstance().getStrByID("401205", "UPP401205-000068"), getPk_corp(), getModuleCode(), getOperatorID(), null);
      }
      catch (Throwable ivjExc)
      {
        this.m_query = null;
        handleException(ivjExc);
      }
    }
    return this.m_query;
  }

  public String getTitle()
  {
    String title = NCLangRes.getInstance().getStrByID("401205", "UPP401205-000050");

    if (this.m_BillCardPanel != null)
      title = this.m_BillCardPanel.getTitle();
    return title;
  }

  private void handleException(Throwable ex)
  {
    SCMEnv.out("--------- 未捕捉到的异常 ---------");
    ex.printStackTrace(System.err);
  }

  public void init()
  {
    setName("ScSettle");

//    this.boLineOperator.addChildButton(this.boAddLine);
    this.boLineOperator.addChildButton(this.boDelLine);//add by shikun
//    this.boLineOperator.addChildButton(this.boInsertLine);
    this.boLineOperator.addChildButton(this.boCopyLine);
    this.boLineOperator.addChildButton(this.boPasteLine);

    this.cardLayout = new CardLayout();
    setLayout(this.cardLayout);
    add(getBillCardPanel(), getBillCardPanel().getName());
    add(getBillListPanel(), getBillListPanel().getName());
    this.cardLayout.last(this);

    initButtons();
    initState();
    this.m_vIDs = new Vector();
    setBodyShow();
    updateUI();
    try
    {
      String sWastControl = SysInitBO_Client.getParaString(getPk_corp(), "SC01");

      if ((sWastControl.trim().equals("是")) || (sWastControl.trim().equalsIgnoreCase("Y")))
      {
        this.bWastControl = true;
      }
    } catch (Exception e) {
      this.bWastControl = false;
    }
  }

  private void initButtons()
  {
    if (this.m_billState == ScConstants.STATE_LIST) {
      setButtons(this.aryListButtonGroup);
      this.boReturn.setName(NCLangRes.getInstance().getStrByID("common", "UCH021"));
    }
    else {
      setButtons(this.aryButtonGroup);
      this.boReturn.setName(NCLangRes.getInstance().getStrByID("common", "UCH022"));
    }
  }

  private void initState()
  {
    getBillCardPanel().setTatolRowShow(true);

    getBillCardPanel().setEnabled(false);

    setButtonsState();
  }

  private int insertItemVOByVerify(int rowindex, DisassembleVO[] disassembleVOs)
  {
    if ((disassembleVOs == null) || (disassembleVOs.length == 0)) {
      return 0;
    }
    String cprocessbaseid = getBillCardPanel().getBodyValueAt(rowindex, "cbaseid").toString();

    String cprocessmangid = getBillCardPanel().getBodyValueAt(rowindex, "cmangid").toString();

    String cvendormangid = ((UIRefPane)getBillCardPanel().getHeadItem("cvendormangid").getComponent()).getRefPK();

    int num = disassembleVOs.length;

    Vector vTemp = new Vector();
    for (int i = 0; i < num; i++)
      vTemp.addElement(disassembleVOs[i].getWlbmid());
    String[] cMateBaseID = new String[vTemp.size()];
    vTemp.copyInto(cMateBaseID);
    String[] cMateMangID = getProcessMangID(disassembleVOs[0].getPk_corp(), cMateBaseID);

    if ((cMateMangID == null) || (cMateMangID.length != cMateBaseID.length)) {
      return 0;
    }

    for (int i = 0; i < num; i++) {
      getBillCardPanel().getBillModel().insertRow(rowindex + i + 1);

      Object procSLobj = getBillCardPanel().getBodyValueAt(rowindex, "nnum");

      UFDouble shxs = disassembleVOs[i].getShxs();
      UFDouble processSL = new UFDouble(procSLobj == null ? "0" : procSLobj.toString());

      UFDouble mateSL = disassembleVOs[i].getSl();
      UFDouble trueSL = mateSL.add(shxs.multiply(mateSL));

      String cmatebaseid = disassembleVOs[i].getWlbmid();

      String cmatemangid = cMateMangID[i];
      getBillCardPanel().setBodyValueAt("", rowindex + i + 1, "bismaterial");

      getBillCardPanel().setBodyValueAt(cmatebaseid, rowindex + i + 1, "cbaseid");

      getBillCardPanel().setBodyValueAt(cmatemangid, rowindex + i + 1, "cmangid");

      getBillCardPanel().setBodyValueAt(processSL.multiply(trueSL), rowindex + i + 1, "nnum");

      getBillCardPanel().setBodyValueAt(trueSL, rowindex + i + 1, "nmatenum");

      BalanceVO conBalanceVO = new BalanceVO();
      conBalanceVO.setPk_corp(getPk_corp());
      conBalanceVO.setCprocessbaseid(cprocessbaseid);
      conBalanceVO.setCprocessmangid(cprocessmangid);
      conBalanceVO.setCmaterialbaseid(cmatebaseid);
      conBalanceVO.setCmaterialmangid(cmatemangid);
      conBalanceVO.setCvendormangid(cvendormangid);

      getBillCardPanel().setBodyValueAt(null, rowindex + i + 1, "nprice");

      getBillCardPanel().setBodyValueAt(null, rowindex + i + 1, "nmny");
    }

    return num;
  }

  private void loadData()
  {
    try
    {
      long s = System.currentTimeMillis();

      MaterialledgerVO[] itemsVO = trimToMateLedger(MaterialledgerHelper.findGeneralBody(this.m_vIDs.elementAt(this.m_iCurBillIndex).toString()));

      MaterialHeaderVO headVO = (MaterialHeaderVO)getBillListPanel().getHeadBillModel().getBodyValueRowVO(this.m_iCurBillIndex, MaterialHeaderVO.class.getName());

      for (int h = 0; h < this.m_heads.length; h++) {
        if (this.m_heads[h].getCbillid().equals(headVO.getCbillid())) {
          headVO.setVdef1(this.m_heads[h].getVdef1());
          headVO.setVdef2(this.m_heads[h].getVdef2());
          headVO.setVdef3(this.m_heads[h].getVdef3());
          headVO.setVdef4(this.m_heads[h].getVdef4());
          headVO.setVdef5(this.m_heads[h].getVdef5());
          headVO.setVdef6(this.m_heads[h].getVdef6());
          headVO.setVdef7(this.m_heads[h].getVdef7());
          headVO.setVdef8(this.m_heads[h].getVdef8());
          headVO.setVdef9(this.m_heads[h].getVdef9());
          headVO.setVdef10(this.m_heads[h].getVdef10());

          break;
        }
      }

      Vector allItemVec = new Vector();

      MaterialledgerVO queryVO = null;
      MaterialledgerVO[] queryVOs = new MaterialledgerVO[itemsVO.length];
      Boolean[] flags = new Boolean[itemsVO.length];
      MaterialledgerVO[] materItems = null;

      for (int i = 0; i < itemsVO.length; i++)
      {
        String bill_bid = itemsVO[i].getCbill_bid();
        queryVO = new MaterialledgerVO();
        queryVO.setCbill_bid(bill_bid);
        queryVOs[i] = queryVO;
        flags[i] = new Boolean(true);
      }

      Vector v = MaterialledgerHelper.queryByVOs(queryVOs, flags);

      for (int i = 0; i < itemsVO.length; i++) {
        itemsVO[i].setCinvshow(ScConstants.PROCESSFLAG);
        allItemVec.addElement(itemsVO[i]);

        materItems = (MaterialledgerVO[])(MaterialledgerVO[])v.get(i);
        for (int j = 0; j < materItems.length; j++) {
          allItemVec.addElement(materItems[j]);
        }

      }

      materItems = new MaterialledgerVO[allItemVec.size()];
      if (allItemVec.size() > 0) {
        allItemVec.copyInto(materItems);
      }
      this.m_curBillVO = new MaterialVO();
      this.m_curBillVO.setParentVO(headVO);
      this.m_curBillVO.setChildrenVO(materItems);

      getBillCardPanel().setBillValueVO(this.m_curBillVO);

      if (!this.m_bListSwitchCard) {
        updateInterfaceShow();
      }
      long s1 = System.currentTimeMillis();
      getBillCardPanel().getBillModel().execLoadFormula();

      SCMEnv.out("执行公式[共用时" + (System.currentTimeMillis() - s1) + "]");

      getBillCardPanel().updateValue();
      loadFreeItems();

      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000253"));

      SCMEnv.out("数据加载成功[共用时" + (System.currentTimeMillis() - s) / 1000L + "秒]");
    }
    catch (ValidationException e) {
      showErrorMessage(e.getMessage());
    } catch (Exception e) {
      SCMEnv.out("数据加载失败");
      SCMEnv.out(e);
    }
  }

  private void loadFreeItems()
  {
    try
    {
      int num = getBillCardPanel().getRowCount();
      ArrayList list = new ArrayList();
      Vector tempV = new Vector();
      for (int i = 0; i < num; i++) {
        list.add(getBillCardPanel().getBodyValueAt(i, "cmangid"));
        tempV.addElement(getBillCardPanel().getBodyValueAt(i, "cmangid"));
      }

      list = AdjustbillHelper.queryFreeVOByInvIDs(list);

      if ((list == null) || (list.size() <= 0)) {
        return;
      }
      InvVO invVO = new InvVO();
      FreeVO freeVO = new FreeVO();
      for (int i = 0; i < num; i++) {
        freeVO = (FreeVO)list.get(i);
        if (freeVO.getCinventoryid() == null)
          continue;
        freeVO.setVfree1(getBillCardPanel().getBodyValueAt(i, "vfree1") == null ? "" : getBillCardPanel().getBodyValueAt(i, "vfree1").toString());

        freeVO.setVfree2(getBillCardPanel().getBodyValueAt(i, "vfree2") == null ? "" : getBillCardPanel().getBodyValueAt(i, "vfree2").toString());

        freeVO.setVfree3(getBillCardPanel().getBodyValueAt(i, "vfree3") == null ? "" : getBillCardPanel().getBodyValueAt(i, "vfree3").toString());

        freeVO.setVfree4(getBillCardPanel().getBodyValueAt(i, "vfree4") == null ? "" : getBillCardPanel().getBodyValueAt(i, "vfree4").toString());

        freeVO.setVfree5(getBillCardPanel().getBodyValueAt(i, "vfree5") == null ? "" : getBillCardPanel().getBodyValueAt(i, "vfree5").toString());

        Object pk_invbasedoc = getBillCardPanel().getBodyValueAt(i, "cbaseid");

        Object pk_invmangdoc = getBillCardPanel().getBodyValueAt(i, "cmangid");

        Object cinventorycode = getBillCardPanel().getBodyValueAt(i, "cinventorycode");

        Object cinventoryname = getBillCardPanel().getBodyValueAt(i, "cinventoryname");

        Object invspec = getBillCardPanel().getBodyValueAt(i, "invspec");

        Object invtype = getBillCardPanel().getBodyValueAt(i, "invtype");

        invVO = new InvVO();
        invVO.setFreeItemVO(freeVO);
        invVO.setCinvmanid(pk_invmangdoc.toString());
        invVO.setCinventoryid(pk_invbasedoc.toString());
        invVO.setIsFreeItemMgt(new Integer(1));
        invVO.setCinventorycode(cinventorycode == null ? "" : cinventorycode.toString());

        invVO.setInvname(cinventoryname == null ? null : cinventoryname.toString());

        invVO.setInvspec(invspec == null ? null : invspec.toString());
        invVO.setInvtype(invtype == null ? null : invtype.toString());

        getBillCardPanel().setBodyValueAt(freeVO.getVfree0(), i, "vfree0");

        getBillCardPanel().setBodyValueAt(invVO, i, "invvo");
      }
    }
    catch (Exception e) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000051"));

      SCMEnv.out(e);
    }
  }

  private void loadMateNumByUnit()
  {
    try
    {
      int rowcount = getBillCardPanel().getRowCount();
      DisassembleVO[] disassembleVOs = null;

      for (int i = 0; i < rowcount; i++)
      {
        Object value = getBillCardPanel().getBodyValueAt(i, "bismaterial");

        if ((value != null) && (value.toString().trim().indexOf(ScConstants.PROCESSFLAG) > -1))
        {
          DisConditionVO condition = BillEdit.getDisConditionVO(getPk_corp(), ClientEnvironment.getInstance().getDate(), getOperatorID(), getBillCardPanel().getHeadItem("cwareid").getValue());

          value = getBillCardPanel().getBodyValueAt(i, "cbaseid");
          condition.setWlbmid(value.toString());
          condition.setSl(new UFDouble("1"));

          String formula = "pk_measdoc->getColValue(bd_invbasdoc,pk_measdoc,pk_invbasdoc,cbaseid)";
          FormulaParse f = new FormulaParse();
          f.setExpress(formula);
          Hashtable h = new Hashtable();
          h.put("cbaseid", StringUtil.toString(value.toString()));
          f.setData(h);
          condition.setJldwid(f.getValue());

          disassembleVOs = BillEdit.getBomVO(condition);
        }
        else {
          if ((disassembleVOs == null) || (disassembleVOs.length == 0))
            continue;
          int mateCount = disassembleVOs.length;
          Object cbaseid = getBillCardPanel().getBodyValueAt(i, "cbaseid");

          for (int k = 0; k < mateCount; k++) {
            if (!disassembleVOs[k].getWlbmid().equals(cbaseid))
              continue;
            double shxs = disassembleVOs[k].getShxs().doubleValue();
            double mateSL = disassembleVOs[k].getSl().doubleValue();
            double trueSL = (1.0D + shxs) * mateSL;

            getBillCardPanel().setBodyValueAt("" + trueSL, i, "nmatenum");
          }
        }
      }

    }
    catch (Exception e)
    {
      showErrorMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000052"));

      SCMEnv.out(e);
    }
  }

  private void onAppendLine()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000465"));
    getBillCardPanel().addLine();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH036"));
  }

  public void onButtonClicked(ButtonObject btn)
  {
    if (btn == this.boEdit) {
      onModify();
    }

    if (btn == this.boSave) {
      onSave();
    }

    if (btn == this.boCancel) {
      onCancel();
    }

    if (btn == this.boAddLine) {
      onAppendLine();
    }

    if (btn == this.boDelLine) {
      onDelLine();
    }

    if (btn == this.boCopyLine) {
      onCopyLine();
    }

    if (btn == this.boInsertLine) {
      onInsertLine();
    }

    if (btn == this.boPasteLine) {
      onPasteLine();
    }

    if (btn == this.boVerify) {
      onVerify();
    }

    if (btn == this.boCancelVerify) {
      onCancelVerify();
    }

    if (btn == this.boPrint) {
      onPrint();
    }

    if (btn == this.boQuery) {
      onQuery();
    }

    if (btn == this.boReturn) {
      if (this.m_billState == ScConstants.STATE_LIST)
        onCard();
      else {
        onList();
      }
    }
    if (btn == this.boSelectAll) {
      onSelectAll();
    }

    if (btn == this.boSelectNo) {
      onSelectNo();
    }

    if (btn == this.boFrash)
      onRefresh();
    if (btn == boCLTJ) {
		onCLTJ();
	}
    if (btn == boHXCZ) {
		onHXCZ();
	}
  }

private void onListRefresh()
  {
    onQuery();
  }

  public void onCancel()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000205"));
    getBillCardPanel().resumeValue();
    getBillCardPanel().setEnabled(false);
    this.m_billState = ScConstants.STATE_CARD;
    loadData();
    setButtonsState();
    getBillCardPanel().getBillTable().setEnabled(false);
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH008"));
  }

  private void onCancelVerify()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000074"));
    if (this.m_billState == ScConstants.STATE_LIST) {
      if (getBillListPanel().getHeadTable().getSelectedRow() < 0) {
        showErrorMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000053"));

        return;
      }
      this.m_billState = ScConstants.STATE_UNVERIFY;
      this.cardLayout.first(this);
      initButtons();
      this.m_iCurBillIndex = getBillListPanel().getHeadTable().getSelectedRow();

      loadData();
    }

    this.m_billState = ScConstants.STATE_UNVERIFY;
    loadData();
    int rowCount = getBillCardPanel().getRowCount();
    Vector allDelRow = new Vector();

    for (int i = 0; i < rowCount; i++)
    {
      Object value = getBillCardPanel().getBodyValueAt(i, "bismaterial");
      if ((value == null) || (value.toString().trim().equals(""))) {
        allDelRow.addElement("" + i);
      }
    }

    int[] allrow = new int[allDelRow.size()];

    for (int i = 0; i < allDelRow.size(); i++) {
      allrow[i] = new Integer(allDelRow.elementAt(i).toString()).intValue();
    }

    getBillCardPanel().getBillModel().delLine(allrow);

    setButtonsState();
    updateInterfaceShow();
    updateUI();
    showHintMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000075"));
  }

  public void onCard()
  {
    this.m_iCurBillIndex = getBillListPanel().getHeadTable().getSelectedRow();
    if (this.m_iCurBillIndex < 0) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000053"));

      return;
    }

    this.m_billState = ScConstants.STATE_CARD;
    this.cardLayout.first(this);
    initButtons();

    this.m_bListSwitchCard = true;

    loadData();

    this.m_bListSwitchCard = false;
    setButtonsState();
    updateUI();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH021"));
  }

  private void onCopyLine()
  {
    int[] row = getBillCardPanel().getBillTable().getSelectedRows();
    if (row.length == 0) {
      return;
    }
    for (int i = 0; i < row.length; i++)
    {
      Object value = getBillCardPanel().getBodyValueAt(row[i], "bismaterial");

      if ((value == null) || (value.toString().trim().indexOf(ScConstants.PROCESSFLAG) == -1))
        continue;
      showErrorMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000054"));

      return;
    }

    getBillCardPanel().copyLine();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH039"));
  }

  private void onDelLine()
  {
    int[] row = getBillCardPanel().getBillTable().getSelectedRows();
    if (row.length == 0) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("common", "UC001-0000013"), NCLangRes.getInstance().getStrByID("401205", "UPP401205-000077"));
      showHintMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000077"));
      return;
    }

    Vector allDelRow = new Vector();

    for (int i = 0; i < row.length; i++)
    {
      Object value = getBillCardPanel().getBodyValueAt(row[i], "bismaterial");

      if ((value != null) && (value.toString().trim().indexOf(ScConstants.PROCESSFLAG) != -1)) {
        continue;
      }
      allDelRow.addElement("" + row[i]);
    }

    int[] allrow = new int[allDelRow.size()];
    for (int i = 0; i < allDelRow.size(); i++) {
      allrow[i] = new Integer(allDelRow.elementAt(i).toString()).intValue();
    }

    getBillCardPanel().getBillModel().delLine(allrow);

    updateInterfaceShow();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH037"));
  }

  private void onInsertLine()
  {
    int row = getBillCardPanel().getBillTable().getSelectedRow();
    if (row == 0) {
      showHintMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000055"));

      return;
    }

    getBillCardPanel().insertLine();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH038"));
  }

  private void onList() {
    this.m_billState = ScConstants.STATE_LIST;
    this.cardLayout.last(this);
    getBillListPanel().loadBodyData(this.m_iCurBillIndex);
    getBillListPanel().setAllNoQueried();
    initButtons();
    updateUI();
    setButtonsState();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH022"));
  }

  private void onModify()
  {
    this.m_billState = ScConstants.STATE_MODIFY;
    setButtonsState();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000030"));
  }

  private void onPasteLine()
  {
    int row = getBillCardPanel().getBillTable().getSelectedRow();

    if (row == 0) {
      showHintMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000056"));

      return;
    }
    int iOldRowCnt = getBillCardPanel().getRowCount();
    getBillCardPanel().pasteLine();
    int iNewRowCnt = getBillCardPanel().getRowCount();
    if ((iOldRowCnt > 0) && (iNewRowCnt > 0) && (iOldRowCnt == iNewRowCnt))
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000424"));
    else
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH040"));
  }

  private void onPrint()
  {
    PrintEntry print = new PrintEntry(null, null);

    ScPrint scDataSource = new ScPrint(ScPrint.INIT, getBillCardPanel(), getModuleCode());

    print.setDataSource(scDataSource);

    print.setTemplateID(getPk_corp(), getModuleCode(), getOperatorID(), null);

    print.preview();
  }

  private void onQuery()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000360"));

    getQryCondition().showModal();
    if (!getQryCondition().isCloseOK()) {
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH009"));
      return;
    }
    onRefresh();
  }

  private void onRefresh()
  {
    try
    {
      this.m_billHeaderVO = PublicHelper.findGeneralHead(getQryCondition().getSQL(), new ClientLink(ClientEnvironment.getInstance()));

      if ((this.m_billHeaderVO == null) || (this.m_billHeaderVO.length == 0)) {
        getBillListPanel().getHeadBillModel().clearBodyData();
        getBillListPanel().getBodyBillModel().clearBodyData();
        getBillListPanel().setHeaderValueVO(null);
        setButtonsState();
        showHintMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000057"));
        return;
      }
      getBillListPanel().setHeaderValueVO(trimToMateHead(this.m_billHeaderVO));
      getBillListPanel().getHeadBillModel().execLoadFormula();
      getBillListPanel().getBodyBillModel().clearBodyData();

      this.m_vIDs = new Vector();
      for (int i = 0; i < this.m_billHeaderVO.length; i++) {
        this.m_vIDs.addElement(this.m_billHeaderVO[i].getPrimaryKey());
      }

      if ((this.m_billHeaderVO == null) || (this.m_billHeaderVO.length == 0)) {
        showHintMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000058"));
        setButtonsState();
        return;
      }

      if (getQryCondition().getSettleCondition() == 1) {
        this.m_bVerified = false;
        this.m_bBalanced = false;
      } else if (getQryCondition().getSettleCondition() == 2) {
        this.m_bVerified = true;
        this.m_bBalanced = false;
      } else if (getQryCondition().getSettleCondition() == 3) {
        this.m_bVerified = true;
        this.m_bBalanced = true;
      }

      getBillListPanel().getHeadTable().clearSelection();
      setButtonsState();

      getQryCondition().destroy();

      getBillListPanel().setAllNoQueried();
    }
    catch (Exception e) {
      SCMEnv.out("列表表头数据加载失败！");
      SCMEnv.out(e);
    }
    SCMEnv.out("列表表头数据加载成功！");
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH009"));
  }

  private boolean onSave()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000134"));
    long s = System.currentTimeMillis();

    getBillCardPanel().stopEditing();

    if (!getBillCardPanel().getBillData().execValidateFormulas()) {
      return false;
    }
    try
    {
      int rowCount = getBillCardPanel().getBillModel().getRowCount();
      if (rowCount <= 0) {
        showErrorMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000059"));

        return false;
      }

      for (int i = 0; i < rowCount; i++) {
        Object lobj_temp = getBillCardPanel().getBillModel().getValueAt(i, "nprocessmny");

        if ((lobj_temp == null) || (lobj_temp.toString().trim().equals("")) || (((UFDouble)lobj_temp).doubleValue() >= 0.0D)) {
          continue;
        }
        showErrorMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000060"));

        return false;
      }

      if (this.m_billState == ScConstants.STATE_VERIFY) {
        if (!validateBeforeSave()) {
          return false;
        }
        if (!checkVbatch()) {
          return false;
        }

        if (!checkWastControl()) {
          return false;
        }

        if (!checkFreeInput(getBillCardPanel())) {
          return false;
        }
        trimCurData();

        this.m_curBillVO = ((MaterialVO)getBillCardPanel().getBillValueVO(MaterialVO.class.getName(), MaterialHeaderVO.class.getName(), MaterialledgerVO.class.getName()));

        this.m_curBillVO.validate();
        MaterialledgerHelper.insertArray(trimBillVO(this.m_curBillVO), getEstimateVOs(this.m_curBillVO), getOperatorID(), ClientEnvironment.getInstance().getDate());
      }

      if ((this.m_billState == ScConstants.STATE_MODIFY) || (this.m_billState == ScConstants.STATE_UNVERIFY))
      {
        loadMateNumByUnit();
        if (this.m_billState == ScConstants.STATE_MODIFY) {
          if (!validateBeforeSave())
            return false;
          if (!checkVbatch())
            return false;
          if (!checkWastControl()) {
            return false;
          }
          if (!checkFreeInput(getBillCardPanel())) {
            return false;
          }
        }
        trimCurData();

        this.m_curBillVO = ((MaterialVO)getBillCardPanel().getBillValueChangeVO(MaterialVO.class.getName(), MaterialHeaderVO.class.getName(), MaterialledgerVO.class.getName()));

        this.m_curBillVO.validate();
        MaterialledgerVO[] itemVO = (MaterialledgerVO[])(MaterialledgerVO[])this.m_curBillVO.getChildrenVO();

        for (int i = 0; i < itemVO.length; i++) {
          itemVO[i].setIsourcetype("1");
        }
        MaterialledgerHelper.updateArray(trimBillVO(this.m_curBillVO), getEstimateVOs(this.m_curBillVO), this.m_billState, getOperatorID(), ClientEnvironment.getInstance().getDate());
      }

      getBillCardPanel().updateValue();

      SCMEnv.out("保存结束：" + System.currentTimeMillis());

      showHintMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000061"));

      SCMEnv.out("数据保存成功[共用时" + (System.currentTimeMillis() - s) / 1000L + "秒]");

      if (this.m_billState != ScConstants.STATE_VERIFY) {
        loadData();
      }
      this.m_billState = ScConstants.STATE_CARD;
    }
    catch (ValidationException e) {
      showErrorMessage(e.getMessage());
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000010"));
      return false;
    } catch (Exception e) {
      SCMEnv.out(e);
      showErrorMessage(e.getMessage());
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000010"));
      return false;
    }

    this.m_bVerified = (!this.m_bVerified);
    setButtonsState();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH005"));
    return true;
  }

  private void onSelectAll()
  {
    int listHeadNum = getBillListPanel().getHeadBillModel().getRowCount();
    if (listHeadNum <= 0) {
      showHintMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000076"));
      return;
    }
    for (int i = 0; i < listHeadNum; i++)
      getBillListPanel().getHeadBillModel().setRowState(i, 4);
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000033"));
  }

  private void onSelectNo()
  {
    int listHeadNum = getBillListPanel().getHeadBillModel().getRowCount();
    if (listHeadNum <= 0) {
      showHintMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000076"));
      return;
    }
    for (int i = 0; i < listHeadNum; i++)
      getBillListPanel().getHeadBillModel().setRowState(i, 0);
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000034"));
  }

  private void onVerify()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000071"));

    if (this.m_billState == ScConstants.STATE_LIST) {
      if (getBillListPanel().getHeadTable().getSelectedRow() < 0) {
        showErrorMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000053"));

        return;
      }
      this.m_billState = ScConstants.STATE_VERIFY;
      this.cardLayout.first(this);
      initButtons();
      this.m_iCurBillIndex = getBillListPanel().getHeadTable().getSelectedRow();

      loadData();
    }

    try
    {
      Vector delRow = null;
      int rowcount = getBillCardPanel().getRowCount();

      for (int i = 0; i < rowcount; i++) {
        Object value = getBillCardPanel().getBodyValueAt(i, "bismaterial");
        
        //add by shikun 通过委外订单ID找到BOM版本
        Object corder_bid = getBillCardPanel().getBodyValueAt(i, "corder_bid");
        Object bomer = null;
        if (corder_bid!=null) {
      	    String cxbom = "select bomvers from sc_order_b where nvl(dr,0) = 0 and pk_corp='"+getPk_corp()+"' and corder_bid = '"+corder_bid+"' ";
      	    bomer = PubDelegator.getUAPQueryBS().executeQuery(cxbom, new ColumnProcessor());
		}
        //end 通过委外订单ID找到BOM版本

        if ((value == null) || (!value.toString().trim().equals(ScConstants.PROCESSFLAG)))
        {
          continue;
        }

        delRow = new Vector();
        for (int j = i + 1; j < rowcount; j++) {
          value = getBillCardPanel().getBodyValueAt(j, "bismaterial");

          if ((value != null) && (!value.toString().trim().equals(""))) {
            break;
          }
          delRow.addElement("" + j);
        }
        int[] allrow = new int[delRow.size()];
        for (int k = 0; k < delRow.size(); k++) {
          allrow[k] = new Integer(delRow.elementAt(k).toString()).intValue();
        }

        getBillCardPanel().getBillModel().delLine(allrow);

        rowcount -= allrow.length;

        DisConditionVO condition = BillEdit.getDisConditionVO(getPk_corp(), ClientEnvironment.getInstance().getDate(), getOperatorID(), getBillCardPanel().getHeadItem("cwareid").getValue());

        value = getBillCardPanel().getBodyValueAt(i, "cbaseid");
        condition.setWlbmid(value.toString());
        condition.setSl(new UFDouble("1"));

        String formula = "pk_measdoc->getColValue(bd_invbasdoc,pk_measdoc,pk_invbasdoc,cbaseid)";
        FormulaParse f = new FormulaParse();
        f.setExpress(formula);
        Hashtable h = new Hashtable();
        h.put("cbaseid", StringUtil.toString(value.toString()));
        f.setData(h);
        condition.setJldwid(f.getValue());
        
        //add by shikun 将BOM版本加入到查询条件
        if (bomer!=null) {
        	condition.setId(bomer.toString());
		}
        //end 将BOM版本加入到查询条件

        DisassembleVO[] disassembleVOs = BillEdit.getBomVO(condition);

        int mateCount = insertItemVOByVerify(i, disassembleVOs);

        rowcount += mateCount;
      }

      getBillCardPanel().getBillModel().execLoadFormula();

      setMaterialFreeItem();

      for (int i = 0; i < rowcount; i++) {
        Object value = getBillCardPanel().getBodyValueAt(i, "nnum");
        getBillCardPanel().setBodyValueAt(value, i, "nnumcopy");

        value = getBillCardPanel().getBodyValueAt(i, "nmatenum");
        getBillCardPanel().setBodyValueAt(value, i, "nmatenumcopy");
      }

      getBillCardPanel().getBillTable().setEnabled(true);
    }
    catch (Exception e) {
      SCMEnv.out("核销失败！");
      SCMEnv.out(e);
      showHintMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000072"));
      return;
    }

    if ((this.m_billState != ScConstants.STATE_MODIFY) && (this.m_billState != ScConstants.STATE_UNVERIFY))
    {
      this.m_billState = ScConstants.STATE_VERIFY;
    }
    setButtonsState();
    updateUI();
    showHintMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000073"));
  }

  public void setButtonsState(boolean bVerified)
  {
    this.m_bVerified = bVerified;
    setButtonsState();
  }

  public void setButtonsState()
  {
    if (this.m_billState == ScConstants.STATE_LIST) {
      this.boFrash.setEnabled(true);
      this.boSelectAll.setEnabled(true);
      this.boSelectNo.setEnabled(true);
      this.boLocate.setEnabled(true);
      this.boQuery.setEnabled(true);
      if ((getBillListPanel().getHeadTable().getRowCount() == 0) || (getBillListPanel().getHeadTable().getSelectedRowCount() <= 0))
      {
        this.boVerify.setEnabled(false);
        this.boCancelVerify.setEnabled(false);
      }
      else if (!this.m_bVerified) {
        this.boVerify.setEnabled(true);
        this.boCancelVerify.setEnabled(false);
      } else if ((this.m_bVerified) && (!this.m_bBalanced)) {
        this.boVerify.setEnabled(false);
        this.boCancelVerify.setEnabled(true);
      } else if ((this.m_bVerified) && (this.m_bBalanced)) {
        this.boVerify.setEnabled(false);
        this.boCancelVerify.setEnabled(false);
      }

      this.boPrint.setEnabled(true);
    }
    else if (this.m_billState == ScConstants.STATE_CARD)
    {
      if (this.m_bVerified) {
        this.boVerify.setEnabled(false);
        if (!this.m_bBalanced)
          this.boCancelVerify.setEnabled(true);
        else
          this.boCancelVerify.setEnabled(false);
      } else {
        this.boVerify.setEnabled(true);
        this.boCancelVerify.setEnabled(false);
      }
      this.boEdit.setEnabled(false);
      this.boSave.setEnabled(false);
      this.boCLTJ.setEnabled(false);//ADD BY SHIKUN
      this.boCancel.setEnabled(false);

      this.boLineOperator.setEnabled(false);
      this.boDelLine.setEnabled(false);

      this.boPrint.setEnabled(false);
      this.boReturn.setEnabled(true);

      getBillCardPanel().setEnabled(false);
      getBillCardPanel().getBillTable().setEnabled(true);
    }
    else if (this.m_billState == ScConstants.STATE_VERIFY) {
      this.boEdit.setEnabled(false);
      this.boSave.setEnabled(true);
      this.boCLTJ.setEnabled(true);//ADD BY SHIKUN
      this.boCancel.setEnabled(true);
      this.boLineOperator.setEnabled(true);

      int[] row = getBillCardPanel().getBillTable().getSelectedRows();
      if (row.length == 1) {
        Object value = getBillCardPanel().getBodyValueAt(row[0], "bismaterial");
        if ((value == null) || (value.toString().trim().equals(""))){
          this.boDelLine.setEnabled(true);
        this.boLineOperator.setEnabled(true);}//add by shikun 
        else{
          this.boDelLine.setEnabled(false);
        this.boLineOperator.setEnabled(false);}//add by shikun 
      }
      else {
        this.boDelLine.setEnabled(false);
        this.boLineOperator.setEnabled(false);//add by shikun 
      }

      setBtnBillRowOperationEnable();

      this.boVerify.setEnabled(false);
      this.boCancelVerify.setEnabled(false);
      this.boReturn.setEnabled(false);
      this.boPrint.setEnabled(true);

      getBillCardPanel().setEnabled(true);
    }
    else if (this.m_billState == ScConstants.STATE_MODIFY) {
      this.boEdit.setEnabled(false);
      this.boSave.setEnabled(true);
      this.boCLTJ.setEnabled(true);//ADD BY SHIKUN
      this.boCancel.setEnabled(true);
      this.boLineOperator.setEnabled(true);
      this.boDelLine.setEnabled(true);

      this.boVerify.setEnabled(true);
      this.boCancelVerify.setEnabled(true);
      this.boPrint.setEnabled(true);
      this.boReturn.setEnabled(false);

      getBillCardPanel().setEnabled(true);
    }
    else if (this.m_billState == ScConstants.STATE_UNVERIFY) {
      this.boEdit.setEnabled(false);
      this.boSave.setEnabled(true);
      this.boCLTJ.setEnabled(false);//ADD BY SHIKUN
      this.boCancel.setEnabled(true);
      this.boLineOperator.setEnabled(false);//edit  by shikun true->false
      this.boDelLine.setEnabled(false);

      setBtnBillRowOperationEnable();

      this.boVerify.setEnabled(false);
      this.boCancelVerify.setEnabled(false);
      this.boReturn.setEnabled(false);
      this.boPrint.setEnabled(true);

      getBillCardPanel().setEnabled(true);
    }

    updateButtons();
  }

  private void setMaterialFreeItem()
  {
    ArrayList list = new ArrayList();
    int num = getBillCardPanel().getRowCount();
    for (int i = 0; i < num; i++) {
      Object value = getBillCardPanel().getBodyValueAt(i, "bismaterial");
      if ((value == null) || (value.toString().trim().equals(""))) {
        Object pk_invmangdoc = getBillCardPanel().getBodyValueAt(i, "cmangid");

        if ((pk_invmangdoc == null) || (pk_invmangdoc.toString().trim().length() == 0)) {
          continue;
        }
        list.add(pk_invmangdoc);
      }
    }
    if (list.size() == 0) {
      return;
    }

    ArrayList retList = new ArrayList();
    try {
      retList = AdjustbillHelper.queryFreeVOByInvIDs(list);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      return;
    }
    if ((retList == null) || (retList.size() == 0)) {
      return;
    }

    int j = 0;
    for (int i = 0; i < num; i++) {
      Object value = getBillCardPanel().getBodyValueAt(i, "bismaterial");
      if ((value != null) && (!value.toString().trim().equals("")))
        continue;
      Object pk_invbasedoc = getBillCardPanel().getBodyValueAt(i, "cbaseid");

      Object pk_invmangdoc = getBillCardPanel().getBodyValueAt(i, "cmangid");

      Object cinventorycode = getBillCardPanel().getBodyValueAt(i, "cinventorycode");

      Object cinventoryname = getBillCardPanel().getBodyValueAt(i, "cinventoryname");

      Object invspec = getBillCardPanel().getBodyValueAt(i, "invspec");

      Object invtype = getBillCardPanel().getBodyValueAt(i, "invtype");

      if (pk_invmangdoc == null) {
        continue;
      }
      FreeVO freeVO = null;
      InvVO invVO = null;

      freeVO = (FreeVO)retList.get(j);
      j++;

      invVO = new InvVO();
      invVO.setFreeItemVO(freeVO);
      invVO.setCinvmanid(pk_invmangdoc.toString());
      invVO.setCinventoryid(pk_invbasedoc.toString());
      invVO.setIsFreeItemMgt(new Integer(1));
      invVO.setCinventorycode(cinventorycode == null ? "" : cinventorycode.toString());

      invVO.setInvname(cinventoryname == null ? "" : cinventoryname.toString());

      invVO.setInvspec(invspec == null ? "" : invspec.toString());
      invVO.setInvtype(invtype == null ? "" : invtype.toString());

      FreeItemRefPane freeRef = (FreeItemRefPane)getBillCardPanel().getBodyItem("vfree0").getComponent();

      freeRef.setFreeItemParam(invVO);

      getBillCardPanel().setBodyValueAt(invVO, i, "invvo");
    }
  }

  private MaterialledgerVO[] trimBillVO(MaterialVO billVO)
    throws Exception
  {
    MaterialHeaderVO headVO = (MaterialHeaderVO)this.m_curBillVO.getParentVO();
    if (headVO.getPk_corp() == null) {
      throw new Exception(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000062"));
    }
    if (headVO.getCvendormangid() == null) {
      throw new Exception(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000063"));
    }

    MaterialledgerVO[] itemsVO = (MaterialledgerVO[])(MaterialledgerVO[])this.m_curBillVO.getChildrenVO();

    Vector vecItems = new Vector();

    Object cprocessmangid = ""; Object cprocessbaseid = ""; Object cbill_bid = "";

    int rowcount = getBillCardPanel().getRowCount();

    for (int i = 0; i < rowcount; i++) {
      Object value = getBillCardPanel().getBodyValueAt(i, "bismaterial");
      if ((value == null) || (value.toString().trim().equals(""))) {
        continue;
      }
      cprocessmangid = getBillCardPanel().getBodyValueAt(i, "cmangid");
      cprocessbaseid = getBillCardPanel().getBodyValueAt(i, "cbaseid");
      cbill_bid = getBillCardPanel().getBodyValueAt(i, "cbill_bid");

      for (int j = 0; j < itemsVO.length; j++)
      {
        if ((!itemsVO[j].getCbill_bid().equals(cbill_bid)) || ((itemsVO[j].getCinvshow() != null) && (!itemsVO[j].getCinvshow().trim().equals(""))))
        {
          continue;
        }
        itemsVO[j].setCaccountyear(headVO.getCaccountyear());
        itemsVO[j].setCbillid(headVO.getCbillid());
        itemsVO[j].setPk_corp(headVO.getPk_corp());
        itemsVO[j].setDbilldate(headVO.getDbilldate());
        itemsVO[j].setCvendormangid(headVO.getCvendormangid());
        if ((headVO.getCvendorid() == null) || (headVO.getCvendorid().trim().equals("")))
        {
          itemsVO[j].setCvendorid(getCvendorbaseid(headVO.getCvendormangid()));
        }
        else {
          itemsVO[j].setCvendorid(headVO.getCvendorid());
        }
        itemsVO[j].setPk_corp(headVO.getPk_corp());
        itemsVO[j].setTs(headVO.getTs());

        itemsVO[j].setIsourcetype("1");
        itemsVO[j].setCprocessbaseid(cprocessbaseid == null ? null : cprocessbaseid.toString().trim());

        itemsVO[j].setCprocessmangid(cprocessmangid == null ? null : cprocessmangid.toString().trim());

        vecItems.addElement(itemsVO[j]);
      }

    }

    MaterialledgerVO[] materialItems = new MaterialledgerVO[vecItems.size()];
    if (vecItems.size() > 0) {
      vecItems.copyInto(materialItems);
    }
    return materialItems;
  }

  private void trimCurData()
  {
    Object cbill_bid = "";
    Object corderid = ""; Object corder_bid = "";
    for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
      Object value = getBillCardPanel().getBodyValueAt(i, "bismaterial");
      if ((value != null) && (value.toString().trim().indexOf(ScConstants.PROCESSFLAG) > -1))
      {
        corderid = getBillCardPanel().getBodyValueAt(i, "corderid");
        corder_bid = getBillCardPanel().getBodyValueAt(i, "corder_bid");
        cbill_bid = getBillCardPanel().getBodyValueAt(i, "cbill_bid");
      }
      else {
        getBillCardPanel().setBodyValueAt(corderid, i, "corderid");
        getBillCardPanel().setBodyValueAt(corder_bid, i, "corder_bid");
        getBillCardPanel().setBodyValueAt(cbill_bid, i, "cbill_bid");
      }
    }
  }

  private MaterialHeaderVO[] trimToMateHead(GeneralBillHeaderVO[] headVOs)
  {
    if (headVOs == null)
      return null;
    int num = headVOs.length;
    MaterialHeaderVO[] VOs = new MaterialHeaderVO[num];
    for (int i = 0; i < num; i++) {
      VOs[i] = new MaterialHeaderVO();

      VOs[i].setDbilldate(ClientEnvironment.getInstance().getDate());
      VOs[i].setCbillid(headVOs[i].getCgeneralhid());
      VOs[i].setCbillcode(headVOs[i].getVbillcode());
      VOs[i].setCdeptid(headVOs[i].getCdptid());
      VOs[i].setCvendormangid(headVOs[i].getCproviderid());
      VOs[i].setPk_corp(headVOs[i].getPk_corp());

      VOs[i].setCwarehouseid(headVOs[i].getCwarehouseid());
      VOs[i].setCemployeeid(headVOs[i].getCbizid());
      VOs[i].setTs(headVOs[i].getTimeStamp());

      VOs[i].setVdef1(headVOs[i].getVuserdef1());
      VOs[i].setVdef2(headVOs[i].getVuserdef2());
      VOs[i].setVdef3(headVOs[i].getVuserdef3());
      VOs[i].setVdef4(headVOs[i].getVuserdef4());
      VOs[i].setVdef5(headVOs[i].getVuserdef5());
      VOs[i].setVdef6(headVOs[i].getVuserdef6());
      VOs[i].setVdef7(headVOs[i].getVuserdef7());
      VOs[i].setVdef8(headVOs[i].getVuserdef8());
      VOs[i].setVdef9(headVOs[i].getVuserdef9());
      VOs[i].setVdef10(headVOs[i].getVuserdef10());
    }

    this.m_heads = VOs;
    return VOs;
  }

  private MaterialledgerVO[] trimToMateLedger(GeneralBillItemVO[] itemVOs)
  {
    int num = itemVOs.length;
    MaterialledgerVO[] VOs = new MaterialledgerVO[num];

    for (int i = 0; i < num; i++) {
      VOs[i] = new MaterialledgerVO();

      VOs[i].setCinvshow(ScConstants.PROCESSFLAG);
      VOs[i].setDbizdate(itemVOs[i].getDbizdate());

      VOs[i].setCorderid(itemVOs[i].getCfirstbillhid());
      VOs[i].setCorder_bid(itemVOs[i].getCfirstbillbid());

      VOs[i].setCbillid(itemVOs[i].getCgeneralhid());
      VOs[i].setCbill_bid(itemVOs[i].getCgeneralbid());

      VOs[i].setCmaterialmangid(itemVOs[i].getCinventoryid());

      VOs[i].setCmaterialbaseid((String)ScTool.loadCellFormular("bd_invmandoc", "pk_invmandoc", "pk_invbasdoc", itemVOs[i].getCinventoryid()));

      VOs[i].setNnum(itemVOs[i].getNinnum());

      if ((itemVOs[i].getBzgflag() != null) && (itemVOs[i].getBzgflag().booleanValue()) && (this.m_billState != ScConstants.STATE_UNVERIFY))
      {
        VOs[i].setNprocessmny(itemVOs[i].getNplannedprice());
      }
      else VOs[i].setNprocessmny(itemVOs[i].getNprice());

      VOs[i].setNmny(itemVOs[i].getNplannedmny());

      if ((VOs[i].getNmny() != null) && (VOs[i].getNnum() != null) && (VOs[i].getNnum().doubleValue() != 0.0D))
      {
        VOs[i].setNprice(VOs[i].getNmny().div(VOs[i].getNnum()));
      }
      VOs[i].setVbatch(itemVOs[i].getVbatchcode());
      if (itemVOs[i].getFreeItemVO() != null) {
        VOs[i].setVfree1(itemVOs[i].getFreeItemVO().getVfree1());
        VOs[i].setVfree2(itemVOs[i].getFreeItemVO().getVfree2());
        VOs[i].setVfree3(itemVOs[i].getFreeItemVO().getVfree3());
        VOs[i].setVfree4(itemVOs[i].getFreeItemVO().getVfree4());
        VOs[i].setVfree5(itemVOs[i].getFreeItemVO().getVfree5());
      }

      VOs[i].setVdef1(itemVOs[i].getVuserdef1());
      VOs[i].setVdef2(itemVOs[i].getVuserdef2());
      VOs[i].setVdef3(itemVOs[i].getVuserdef3());
      VOs[i].setVdef4(itemVOs[i].getVuserdef4());
      VOs[i].setVdef5(itemVOs[i].getVuserdef5());
      VOs[i].setVdef6(itemVOs[i].getVuserdef6());
    }

    return VOs;
  }

  private void updateInterfaceShow()
  {
    int rowcount = getBillCardPanel().getRowCount();

    int preProcessRow = 0;
    for (int i = 0; i < rowcount; i++) {
      Object invshow = getBillCardPanel().getBodyValueAt(i, "bismaterial");

      if ((invshow == null) || (invshow.toString().indexOf(ScConstants.PROCESSFLAG) <= -1))
        continue;
      editProcessRow(preProcessRow, i - 1);
      preProcessRow = i;
    }

    editProcessRow(preProcessRow, rowcount - 1);
  }

  private boolean validateBeforeSave()
  {
    int rownum = getBillCardPanel().getRowCount();
    for (int i = 0; i < rownum; i++) {
      Object value = getBillCardPanel().getBodyValueAt(i, "bismaterial");
      if ((value == null) || (value.toString().trim().equals(""))) {
        continue;
      }
      if (i == rownum - 1) {
        showErrorMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000064", null, new String[] { i + 1 + "" }));

        return false;
      }
      value = getBillCardPanel().getBodyValueAt(i + 1, "bismaterial");
      if ((value == null) || (!value.toString().trim().equals(ScConstants.PROCESSFLAG))) {
        continue;
      }
      showErrorMessage(NCLangRes.getInstance().getStrByID("401205", "UPP401205-000064", null, new String[] { i + 1 + "" }));

      return false;
    }

    return true;
  }

  public int getBillState() {
    return this.m_billState;
  }

  public boolean onClosing()
  {
    if ((this.m_billState == ScConstants.STATE_VERIFY) || (this.m_billState == ScConstants.STATE_MODIFY) || (this.m_billState == ScConstants.STATE_UNVERIFY)) {
      int nReturn = MessageDialog.showYesNoCancelDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("common", "UCH001"));

      if (nReturn == 4) {
        return onSave();
      }

      return nReturn == 8;
    }

    return true;
  }

  public void setBodyShow()
  {
    getBillCardPanel().hideBodyTableCol("nmny");
    getBillListPanel().hideBodyTableCol("nmny");
  }

  public static boolean checkFreeInput(BillCardPanel panel)
  {
    panel.stopEditing();
    boolean flag = true;
    int rowCount = panel.getRowCount();
    if (rowCount <= 0) return true;

    int nCol = -1;
    for (int i = 0; i < panel.getBillTable().getColumnCount(); i++) {
      String s = panel.getBodyPanel().getBodyKeyByCol(i);
      if (s.equals("vfree0")) {
        nCol = i;
        break;
      }
    }

    String errMsg = "";
    for (int i = 0; i < rowCount; i++) {
      Object o = panel.getBodyValueAt(i, "invvo");
      if (o == null)
        continue;
      if ((nCol < 0) || 
        (!PuTool.isFreeMngt((String)panel.getBodyValueAt(i, "cmangid")))) continue;
      String free0 = panel.getBodyValueAt(i, "vfree0") == null ? null : panel.getBodyValueAt(i, "vfree0").toString();
      if ((free0 == null) || (free0.trim().length() == 0)) {
        errMsg = errMsg + (errMsg.trim().length() > 0 ? "," + (i + 1) : new StringBuilder().append("").append(i + 1).toString());
      }
    }

    if (errMsg.length() > 0) {
      MessageDialog.showErrorDlg(panel, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), NCLangRes.getInstance().getStrByID("40120002", "UPP40120002-000002", null, new String[] { errMsg }));
      return false;
    }

    return flag;
  }

  public void onMenuItemClick(ActionEvent event)
  {
    UIMenuItem menuItem = (UIMenuItem)event.getSource();

    if (menuItem.equals(getBillCardPanel().getAddLineMenuItem()))
      onAppendLine();
    else if (menuItem.equals(getBillCardPanel().getCopyLineMenuItem()))
      onCopyLine();
    else if (menuItem.equals(getBillCardPanel().getDelLineMenuItem()))
      onDelLine();
    else if (menuItem.equals(getBillCardPanel().getInsertLineMenuItem()))
      onInsertLine();
    else if (menuItem.equals(getBillCardPanel().getPasteLineMenuItem()))
      onPasteLine();
  }

  public void actionPerformed(ActionEvent e)
  {
  }

  private void setBtnBillRowOperationEnable()
  {
    getBillCardPanel().getBodyPanel().getMiAddLine().setVisible(false);
    getBillCardPanel().getBodyPanel().getMiDelLine().setVisible(this.boDelLine.isEnabled());
    getBillCardPanel().getBodyPanel().getMiInsertLine().setVisible(false);
    getBillCardPanel().getBodyPanel().getMiCopyLine().setVisible(false);
    getBillCardPanel().getBodyPanel().getMiPasteLine().setVisible(false);

    getBillCardPanel().getBodyPanel().getMiPasteLineToTail().setVisible(false);
  }

  /**
   * 材料挑拣
   * @author shikun
   * */
  private void onCLTJ() {
	int rows = getBillCardPanel().getRowCount();
	if (rows <=0 ) {
		return;
	}
	//供应商管理ID
    Object cvendormangid = getBillCardPanel().getHeadItem("cvendormangid").getValueObject();
	//查询可挑拣材料
    //加工品管理档案ID
    Object cprocessmangid = null;
    String Spmwhere = "";
	for (int i = 0; i < rows; i++) {
        Object value = getBillCardPanel().getBodyValueAt(i, "bismaterial");
        if ((value != null) && (value.toString().trim().indexOf(ScConstants.PROCESSFLAG) == 0)) {
        	cprocessmangid = getBillCardPanel().getBodyValueAt(i, "cmangid");
        	if (cprocessmangid!=null) {
        		if ("".equals(Spmwhere)) {
            		Spmwhere = "'"+cprocessmangid+"'";
				}else{
					if (Spmwhere.trim().indexOf(cprocessmangid.toString())<0) {
		        		Spmwhere = Spmwhere + ",'"+cprocessmangid+"'";
					}
				}
			}
        }
	}
	if (!"".equals(Spmwhere)) {
		Spmwhere = "(" + Spmwhere + ")";
		StringBuffer sb = new StringBuffer();
		sb.append(" select cvendormangid, ") 
		.append("        cprocessmangid, ") 
		.append("        cmaterialmangid, ") 
		.append("        vbatch, ") 
		.append("        vfree1, ") 
		.append("        sum(case rtrim(isourcetype, ' ') ") 
		.append("              when '0' then ") 
		.append("               nnum ") 
		.append("              when '1' then ") 
		.append("               -nnum ") 
		.append("              when '2' then ") 
		.append("               -nnum ") 
		.append("              when '3' then ") 
		.append("               nnum ") 
		.append("            end) num, ") 
		.append("        cvendorid ") 
		.append("   from sc_materialledger ") 
		.append("  where pk_corp = '"+getPk_corp()+"' and ") 
		.append("        cprocessmangid in "+Spmwhere+" and ") 
		.append("        cvendormangid = '"+cvendormangid+"' and nvl(dr,0) = 0 and ") 
		.append("        vbatch is not null and ") 
		.append("        vbatch in (select vbatch from sc_balance where nbalancenum <> 0)  ") 
		.append("  group by cvendormangid, ") 
		.append("           cprocessmangid, ") 
		.append("           cmaterialmangid, ") 
		.append("           vbatch, ") 
		.append("           vfree1, ") 
		.append("           cvendorid ") ;
		IUAPQueryBS qurey = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		List<SettleCltjVO> list = null;
		try {
			list = (List<SettleCltjVO>) qurey.executeQuery(sb.toString(),new BeanListProcessor(SettleCltjVO.class));
			if (list!=null&&list.size()>0) {
				//KEY：供应商+加工品+材料
				HashMap<String, List<SettleCltjVO>> hmap = new HashMap<String, List<SettleCltjVO>>();
				for (int j = 0; j < list.size(); j++) {
					SettleCltjVO voj = list.get(j);
					String key = voj.getCvendormangid()+voj.getCprocessmangid()+voj.getCmaterialmangid();
					if(!hmap.containsKey(key)){
						List<SettleCltjVO> listj = new ArrayList<SettleCltjVO>();
						listj.add(voj);
						hmap.put(key, listj);
					}else{
						hmap.get(key).add(voj);
					}
				}

				//逐行进行材料挑拣，并且优先考虑零散材料拼合
			    //加工品管理档案ID
			    cprocessmangid = null;
			    StringBuffer error = new StringBuffer("");
				for (int i = 0; i < rows; i++) {
			        Object value = getBillCardPanel().getBodyValueAt(i, "bismaterial");
			        if ((value != null) && (value.toString().trim().indexOf(ScConstants.PROCESSFLAG) == 0)) {
			        	cprocessmangid = getBillCardPanel().getBodyValueAt(i, "cmangid");
			        }
					if (cprocessmangid!=null&&((value == null) || (value.toString().trim().indexOf(ScConstants.PROCESSFLAG) == -1))) {
				        //材料管理档案ID
						Object cmaterialmangid = getBillCardPanel().getBodyValueAt(i, "cmangid");
						if (cmaterialmangid!=null) {
							//实际核销数量
							UFDouble nnum = getBillCardPanel().getBodyValueAt(i, "nnum")==null?
							                new UFDouble(0):new UFDouble(getBillCardPanel().getBodyValueAt(i, "nnum").toString());
							String ikey = ""+cvendormangid+cprocessmangid+cmaterialmangid;
							List<SettleCltjVO> ilist = hmap.get(ikey);
							if (ilist!=null&&ilist.size()>0) {
					            dghxsl(i,0,ilist,nnum);
					        	int rows0 = getBillCardPanel().getRowCount();
					        	int newrow = rows0-rows;//新增行
					        	i = i + newrow;
					        	rows = rows0;
							}else{
								error.append("第"+(i+1)+"行材料没有可供核销材料！/n");
							}
						}
					}
				}
	            getBillCardPanel().getBillModel().execLoadFormula();
				if (error!=null&&!"".equals(error.toString().trim())) {
					showWarningMessage(error.toString());
				}
			}else{
				showWarningMessage("没有可供核销材料！");
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}else{
		showWarningMessage("表体没有加工品！");
	}
  }

    private void dghxsl(int i, int j, List<SettleCltjVO> ilist, UFDouble nnum) {
    	if (j>=ilist.size()) {
			return;
		}
		SettleCltjVO jvo = ilist.get(j);
		UFDouble num = jvo.getNum();//可核销数量
		if (num.doubleValue()>0&&num.doubleValue()>=nnum.doubleValue()) {//可核销数量大于等于实际核销数量
			khxsldysjhxsl(i,jvo,nnum);
		}else if (num.doubleValue()>0&&num.doubleValue()<nnum.doubleValue()) {
			khxslxysjhxsl(i,jvo,nnum);
			UFDouble fcenum = nnum.sub(num);
            getBillCardPanel().getBillModel().insertRow(i+1);
            Object cmangid = getBillCardPanel().getBodyValueAt(i, "cmangid");
            Object cbaseid = getBillCardPanel().getBodyValueAt(i, "cbaseid");
            Object nnum0 = getBillCardPanel().getBodyValueAt(i, "nnum");
            Object nmatenum = getBillCardPanel().getBodyValueAt(i, "nmatenum");
            getBillCardPanel().setBodyValueAt("", i+1, "bismaterial");
            getBillCardPanel().setBodyValueAt(cbaseid, i+1, "cbaseid");
            getBillCardPanel().setBodyValueAt(cmangid, i+1, "cmangid");
            getBillCardPanel().setBodyValueAt(fcenum, i+1, "nnum");
            getBillCardPanel().setBodyValueAt(nmatenum, i+1, "nmatenum");
            getBillCardPanel().setBodyValueAt(null, i+1, "nprice");
            getBillCardPanel().setBodyValueAt(null, i+1, "nmny");
            dghxsl(i+1,j+1,ilist,fcenum);
		}else{
            dghxsl(i,j+1,ilist,nnum);
		}
	}

	/**
     * 小于
     * 
     * */
	private void khxslxysjhxsl(int i, SettleCltjVO jvo, UFDouble nnum) {
		UFDouble num = jvo.getNum();//可核销数量
		jvo.setNum(new UFDouble(0));
		getBillCardPanel().setBodyValueAt(num,i, "nnum");
		getBillCardPanel().setBodyValueAt(jvo.getVbatch(),i, "vbatch");
		getBillCardPanel().setBodyValueAt(jvo.getVfree1(),i, "vfree1");
		getBillCardPanel().setBodyValueAt("[单件号:"+jvo.getVfree1()+"]",i, "vfree0");
    	BillEdit.editFreeItem(getBillCardPanel(), i, "vbatch", "cbaseid", "cmangid");
   }

	/**
	 * 大于
	 * 
	 * */
	private void khxsldysjhxsl(int i, SettleCltjVO jvo, UFDouble nnum) {
		UFDouble num = jvo.getNum();//可核销数量
		UFDouble cenum = num.sub(nnum);
		jvo.setNum(cenum);//更新可核销数量
		getBillCardPanel().setBodyValueAt(jvo.getVbatch(),i, "vbatch");
		getBillCardPanel().setBodyValueAt(jvo.getVfree1(),i, "vfree1");
		getBillCardPanel().setBodyValueAt("[单件号:"+jvo.getVfree1()+"]",i, "vfree0");
		BillEdit.editFreeItem(getBillCardPanel(), i, "vbatch", "cbaseid", "cmangid");
	}

	  /**
	   * 核销参照
	   * @author shikun
	   * */
	  private void onHXCZ() {
		  int srow = getBillCardPanel().getBillTable().getSelectedRow();
		  if (srow<0) {
			showWarningMessage("请选择表体行！");
			return;
		  }
	        Object value = getBillCardPanel().getBodyValueAt(srow, "bismaterial");
	        if ((value != null) && (value.toString().trim().indexOf(ScConstants.PROCESSFLAG) == 0)) {
	        	showWarningMessage("当前选中行为加工品！");
	        	return;
	        }
			//供应商管理ID
		    Object cvendormangid = getBillCardPanel().getHeadItem("cvendormangid").getValueObject();
		    //加工品管理档案ID
		    Object cprocessmangid = null;
		    for (int i = srow-1; i >= 0; i--) {
		    	Object bismaterial = getBillCardPanel().getBodyValueAt(i, "bismaterial");
		    	if ((bismaterial != null) && (bismaterial.toString().trim().indexOf(ScConstants.PROCESSFLAG) == 0)) {
		    		cprocessmangid = getBillCardPanel().getBodyValueAt(i, "cmangid");
		    		break;
		    	}
			}
	        //材料管理档案ID
			Object cmaterialmangid = getBillCardPanel().getBodyValueAt(srow, "cmangid");
			//整合界面已经选择的数量
			HashMap<String, UFDouble> hmap = new HashMap<String, UFDouble>();
			int rows = getBillCardPanel().getRowCount();
			for (int i = 0; i < rows; i++) {
		        Object valuei = getBillCardPanel().getBodyValueAt(i, "bismaterial");
		        //行加工品
		        if ((valuei != null) && (valuei.toString().trim().indexOf(ScConstants.PROCESSFLAG) == 0)) {
				    //行加工品管理档案ID
				    Object cprocessmangidi = getBillCardPanel().getBodyValueAt(i, "cmangid");
					if (cprocessmangid.equals(cprocessmangidi)) {
						for (int j = i+1; j < rows; j++) {
							Object valuej = getBillCardPanel().getBodyValueAt(j, "bismaterial");
							if ((valuej != null) && (valuej.toString().trim().indexOf(ScConstants.PROCESSFLAG) == 0)) {
								break;
							}
					        //行材料管理档案ID
							Object cmaterialmangidj = getBillCardPanel().getBodyValueAt(j, "cmangid");
							if (cmaterialmangid.equals(cmaterialmangidj)) {
						        //行批次号
								Object vbatchj = getBillCardPanel().getBodyValueAt(j, "vbatch");
								if (vbatchj==null) {
									continue;
								}
						        //行自由项
								Object vfree1j = getBillCardPanel().getBodyValueAt(j, "vfree1");
						        //行核销数量
								UFDouble nnumj = getBillCardPanel().getBodyValueAt(j, "nnum")==null?new UFDouble(0)
								                 :new UFDouble(getBillCardPanel().getBodyValueAt(j, "nnum").toString());
								String keyj = ""+cvendormangid+cprocessmangid+cmaterialmangid+vbatchj+vfree1j;
								if (nnumj.doubleValue()!=0) {
									if (!hmap.containsKey(keyj)) {
										hmap.put(keyj, nnumj);
									}else{
										UFDouble number1 = hmap.get(keyj);
										UFDouble number2 = number1.add(nnumj);
										hmap.remove(keyj);
										hmap.put(keyj, number2);
									}
								}
							}
						}
					}
		        }
			}
		    QueryHxcl dialog = new QueryHxcl(this,cvendormangid,cprocessmangid,cmaterialmangid,hmap);
			dialog.showModal();
			Object[] hxcl =  dialog.getValue();
			if (hxcl!=null&&hxcl.length>0) {
				dohxclselect(srow,hxcl);
			}
	  }

	  /**
	   * 核销指定行的材料
	   * @author shikun
	   * */
	private void dohxclselect(int srow, Object[] hxcl) {
		UFDouble nnum = getBillCardPanel().getBodyValueAt(srow, "nnum")==null?new UFDouble(0)
                       :new UFDouble(getBillCardPanel().getBodyValueAt(srow, "nnum").toString());
        UFDouble num = hxcl[2]==null?new UFDouble(0):new UFDouble(hxcl[2].toString());
        if (num.doubleValue()>0&&nnum.doubleValue()<=num.doubleValue()) {//可核销数量大于实际核销数量
    		getBillCardPanel().setBodyValueAt(hxcl[0],srow, "vbatch");
    		getBillCardPanel().setBodyValueAt(hxcl[1],srow, "vfree1");
    		getBillCardPanel().setBodyValueAt("[单件号:"+hxcl[1]+"]",srow, "vfree0");
    		BillEdit.editFreeItem(getBillCardPanel(), srow, "vbatch", "cbaseid", "cmangid");
		}else if (num.doubleValue()>0&&nnum.doubleValue()>num.doubleValue()) {//可核销数量小于实际核销数量
			UFDouble fcenum = nnum.sub(num);//本次未核销的数量
			getBillCardPanel().setBodyValueAt(num,srow, "nnum");
			getBillCardPanel().setBodyValueAt(hxcl[0],srow, "vbatch");
			getBillCardPanel().setBodyValueAt(hxcl[1],srow, "vfree1");
			getBillCardPanel().setBodyValueAt("[单件号:"+hxcl[1]+"]",srow, "vfree0");
	    	BillEdit.editFreeItem(getBillCardPanel(), srow, "vbatch", "cbaseid", "cmangid");
	    	//生成未核销行
            getBillCardPanel().getBillModel().insertRow(srow+1);
            Object cmangid = getBillCardPanel().getBodyValueAt(srow, "cmangid");
            Object cbaseid = getBillCardPanel().getBodyValueAt(srow, "cbaseid");
            Object nmatenum = getBillCardPanel().getBodyValueAt(srow, "nmatenum");
            getBillCardPanel().setBodyValueAt("", srow+1, "bismaterial");
            getBillCardPanel().setBodyValueAt(cbaseid, srow+1, "cbaseid");
            getBillCardPanel().setBodyValueAt(cmangid, srow+1, "cmangid");
            getBillCardPanel().setBodyValueAt(fcenum, srow+1, "nnum");
            getBillCardPanel().setBodyValueAt(nmatenum, srow+1, "nmatenum");
            getBillCardPanel().setBodyValueAt(null, srow+1, "nprice");
            getBillCardPanel().setBodyValueAt(null, srow+1, "nmny");
            getBillCardPanel().getBillModel().execLoadFormula();
		}
	}
	
 }