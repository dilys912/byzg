package nc.ui.gl.assbalance;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.util.Date;
import java.util.Vector;
import nc.bs.logging.Logger;
import nc.ui.bd.BDGLOrgBookAccessor;
import nc.ui.gl.accbook.BillFormatVO;
import nc.ui.gl.accbook.DynamicPrintTool;
import nc.ui.gl.accbook.IBillModel;
import nc.ui.gl.accbook.IColIndex;
import nc.ui.gl.accbook.PrintDialog;
import nc.ui.gl.accbook.TableDataModel;
import nc.ui.gl.accbook.TableFormatTackle;
import nc.ui.gl.accbookprint.IPrintCenterDealClass;
import nc.ui.gl.datacache.AccsubjDataCache;
import nc.ui.gl.datacache.GLParaDataCache;
import nc.ui.gl.datacache.GLPeriodDataCatch;
import nc.ui.glcom.control.GlComboBox;
import nc.ui.glpub.IParent;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialogEvent;
import nc.ui.pub.beans.UIDialogListener;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.print.PrintDirectEntry;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.print.datastruct.CellRange;
import nc.vo.bd.CorpVO;
import nc.vo.bd.access.BddataVO;
import nc.vo.bd.b02.AccsubjVO;
import nc.vo.bd.b54.GlorgbookVO;
import nc.vo.gl.accbook.ColFormatVO;
import nc.vo.gl.accbook.CurrTypeConst;
import nc.vo.gl.accbook.PrintCondVO;
import nc.vo.gl.balancesubjass.BalanceSubjAssBSVO;
import nc.vo.glcom.ass.AssCodeConstructor;
import nc.vo.glcom.ass.AssVO;
import nc.vo.glcom.balance.GLQueryObj;
import nc.vo.glcom.balance.GlQryFormatVO;
import nc.vo.glcom.balance.GlQueryVO;
import nc.vo.glcom.glperiod.GlPeriodVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.print.PrintTempletmanageHeaderVO;
import nc.vo.sm.UserVO;

public class BalanceSubjAssView extends UIPanel
  implements UIDialogListener, IColIndex, IPrintCenterDealClass
{
  private BalanceSubjAssUI ivjUI = null;

  private QueryDlg1 ivjQueryDlg = null;

  GlQueryVO qryVO = null;

  private BalanceSubjAssModel ivjModel = null;

  String pk_user = ClientEnvironment.getInstance().getUser().getPrimaryKey();

  String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();

  private IBillModel billModel = null;
  private IParent iParent;
  private nc.ui.gl.assdetail.ToftPanelView m_AssUI = null;

  private DynamicPrintTool dptool = null;

  private PrintDialog ivjPrintDlg = null;

  private GlQueryVOContainer glQryCtner = null;

  private int iIndex = -1;

  private ToftPanelView parentView = null;

  private int iState = 0;

  private BlnSbjAssMdlSttl ivjMdlSttl = null;

  public BalanceSubjAssView()
  {
    initialize();
  }

  public BalanceSubjAssView(ToftPanelView parent)
  {
    setParentView(parent);
    initialize();
  }

  public BalanceSubjAssView(LayoutManager p0)
  {
    super(p0);
  }

  public BalanceSubjAssView(LayoutManager p0, boolean p1)
  {
    super(p0, p1);
  }

  public BalanceSubjAssView(boolean p0)
  {
    super(p0);
  }

  public BlnSbjAssMdlSttl getIvjMdlSttl() {
    if (this.ivjMdlSttl == null)
    {
      this.ivjMdlSttl = new BlnSbjAssMdlSttl();
    }
    return this.ivjMdlSttl;
  }
  private String getPk_corp(String Pk_orgbook) {
    String corp = null;
    try {
      corp = BDGLOrgBookAccessor.getPk_corp(Pk_orgbook);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return corp;
  }

  public void showHintMessage()
  {
    if ((getGlQryCtner() != null) && (getGlQryCtner().getSize() > 0)) {
      int iCounter = this.iIndex + 1;
      ((ToftPanel)getIParent().getUiManager()).showHintMessage("    第 " + iCounter + " 页，共有数据 " + getGlQryCtner().getSize() + " 页");
    }
  }

  private void windowsCloseOK()
  {
    this.ivjQueryDlg.getResult();
    if (this.ivjQueryDlg.getqryVO() == null) {
      getQueryDlg().showModal();
      return;
    }

    setGlQryCtner(null);
    this.qryVO = ((GlQueryVO)this.ivjQueryDlg.getqryVO().clone());
    try {
      if (this.ivjQueryDlg.getColms() != null) {
        getAssBalanceUI().setColWs(this.ivjQueryDlg.getColms());
        this.ivjQueryDlg.setColms(null);
      }

      setQueryVos(this.qryVO);
      setButtonState();
      if (this.iIndex < 0)
      {
        ((ToftPanel)getIParent().getUiManager()).showHintMessage(NCLangRes.getInstance().getStrByID("commonres", "UC001-0000006") + NCLangRes.getInstance().getStrByID("commonres", "UCH026"));
      }
      else
      {
        ((ToftPanel)getIParent().getUiManager()).showHintMessage(NCLangRes.getInstance().getStrByID("commonres", "UC001-0000006") + NCLangRes.getInstance().getStrByID("commonres", "UCH026") + ",   第 1 页，共有数据 " + getGlQryCtner().getSize() + " 页");
      }

    }
    catch (Exception err)
    {
      err.printStackTrace();
    }
  }

  public void setButtonState()
  {
    if (getParentView() == null)
      return;
    ToftPanelView parentView = getParentView();
    if (this.iState == 0)
    {
      int i = 0;
      for (; (parentView.getButtons() != null) && (i < parentView.getButtons().length); i++) {
        ButtonObject btn = parentView.getButtons()[i];
        String tag = btn.getTag();
        if (tag.equals(ButtonKey.bnQRY)) {
          btn.setEnabled(true);
        }
        else {
          btn.setEnabled(false);
        }

      }

    }
    else if (this.iIndex < 0)
    {
      int i = 0;
      for (; (parentView.getButtons() != null) && (i < parentView.getButtons().length); i++) {
        ButtonObject btn = parentView.getButtons()[i];
        String tag = btn.getTag();
        if (tag.equals(ButtonKey.bnNext))
        {
          btn.setEnabled(false);
        }
        else if (tag.equals(ButtonKey.bnPriv))
          btn.setEnabled(false);
        else if (tag.equals(ButtonKey.bnFirst))
          btn.setEnabled(false);
        else if (tag.equals(ButtonKey.bnEnd))
          btn.setEnabled(false);
        if ((getAssBalanceUI().getM_TableModel().getData() != null) && (getAssBalanceUI().getM_TableModel().getData().length > 0))
        {
          if (tag.equals(ButtonKey.bnDetail))
            btn.setEnabled(true);
          else if (tag.equals(ButtonKey.bnPrint))
            btn.setEnabled(true);
          else if (tag.equals(ButtonKey.bnRefresh))
            btn.setEnabled(true);
          else if (tag.equals(ButtonKey.bnSwitch)) {
            btn.setEnabled(true);
          }

        }
        else if (tag.equals(ButtonKey.bnDetail))
          btn.setEnabled(false);
        else if (tag.equals(ButtonKey.bnPrint))
          btn.setEnabled(false);
        else if (tag.equals(ButtonKey.bnRefresh))
          btn.setEnabled(false);
        else if (tag.equals(ButtonKey.bnSwitch)) {
          btn.setEnabled(false);
        }

      }

    }
    else if (getGlQryCtner() == null)
    {
      for (int i = 0; i < parentView.getButtons().length; i++) {
        ButtonObject btn = parentView.getButtons()[i];
        String tag = btn.getTag();
        if (tag.equals(ButtonKey.bnQRY))
          btn.setEnabled(true);
        if (tag.equals(ButtonKey.bnNext))
          btn.setEnabled(false);
        else if (tag.equals(ButtonKey.bnPriv))
          btn.setEnabled(false);
        else if (tag.equals(ButtonKey.bnFirst))
          btn.setEnabled(false);
        else if (tag.equals(ButtonKey.bnEnd))
          btn.setEnabled(false);
        else if (tag.equals(ButtonKey.bnDetail))
          btn.setEnabled(false);
        else if (tag.equals(ButtonKey.bnPrint))
          btn.setEnabled(false);
        else if (tag.equals(ButtonKey.bnRefresh))
          btn.setEnabled(false);
        else if (tag.equals(ButtonKey.bnSwitch)) {
          btn.setEnabled(false);
        }

      }

    }
    else if (getGlQryCtner().getSize() == 1)
    {
      for (int i = 0; i < parentView.getButtons().length; i++) {
        ButtonObject btn = parentView.getButtons()[i];
        String tag = btn.getTag();
        if (tag.equals(ButtonKey.bnQRY))
          btn.setEnabled(true);
        if (tag.equals(ButtonKey.bnNext))
          btn.setEnabled(false);
        else if (tag.equals(ButtonKey.bnPriv))
          btn.setEnabled(false);
        else if (tag.equals(ButtonKey.bnFirst))
          btn.setEnabled(false);
        else if (tag.equals(ButtonKey.bnEnd))
          btn.setEnabled(false);
        else if (tag.equals(ButtonKey.bnDetail))
          btn.setEnabled(true);
        else if (tag.equals(ButtonKey.bnPrint))
          btn.setEnabled(true);
        else if (tag.equals(ButtonKey.bnRefresh))
          btn.setEnabled(true);
        else if (tag.equals(ButtonKey.bnSwitch)) {
          btn.setEnabled(true);
        }

      }

    }
    else if (this.iIndex == 0) {
      for (int i = 0; i < parentView.getButtons().length; i++) {
        ButtonObject btn = parentView.getButtons()[i];
        String tag = btn.getTag();
        if (tag.equals(ButtonKey.bnNext))
          btn.setEnabled(true);
        else if (tag.equals(ButtonKey.bnPriv))
          btn.setEnabled(false);
        else if (tag.equals(ButtonKey.bnFirst))
          btn.setEnabled(false);
        else if (tag.equals(ButtonKey.bnEnd))
          btn.setEnabled(true);
        else if (tag.equals(ButtonKey.bnDetail))
          btn.setEnabled(true);
        else if (tag.equals(ButtonKey.bnPrint))
          btn.setEnabled(true);
        else if (tag.equals(ButtonKey.bnRefresh))
          btn.setEnabled(true);
        else if (tag.equals(ButtonKey.bnSwitch)) {
          btn.setEnabled(true);
        }
      }
    }
    else if (this.iIndex == getGlQryCtner().getSize() - 1) {
      for (int i = 0; i < parentView.getButtons().length; i++) {
        ButtonObject btn = parentView.getButtons()[i];
        String tag = btn.getTag();
        if (tag.equals(ButtonKey.bnNext))
          btn.setEnabled(false);
        else if (tag.equals(ButtonKey.bnPriv))
          btn.setEnabled(true);
        else if (tag.equals(ButtonKey.bnFirst))
          btn.setEnabled(true);
        else if (tag.equals(ButtonKey.bnEnd))
          btn.setEnabled(false);
        else if (tag.equals(ButtonKey.bnDetail))
          btn.setEnabled(true);
        else if (tag.equals(ButtonKey.bnPrint))
          btn.setEnabled(true);
        else if (tag.equals(ButtonKey.bnRefresh))
          btn.setEnabled(true);
        else if (tag.equals(ButtonKey.bnSwitch)) {
          btn.setEnabled(true);
        }
      }

    }
    else
    {
      for (int i = 0; i < parentView.getButtons().length; i++) {
        ButtonObject btn = parentView.getButtons()[i];
        String tag = btn.getTag();
        if (tag.equals(ButtonKey.bnNext))
          btn.setEnabled(true);
        else if (tag.equals(ButtonKey.bnPriv))
          btn.setEnabled(true);
        else if (tag.equals(ButtonKey.bnFirst))
          btn.setEnabled(true);
        else if (tag.equals(ButtonKey.bnEnd))
          btn.setEnabled(true);
        else if (tag.equals(ButtonKey.bnDetail))
          btn.setEnabled(true);
        else if (tag.equals(ButtonKey.bnPrint))
          btn.setEnabled(true);
        else if (tag.equals(ButtonKey.bnRefresh))
          btn.setEnabled(true);
        else if (tag.equals(ButtonKey.bnSwitch)) {
          btn.setEnabled(true);
        }

      }

    }

    parentView.updateButtons();
  }

  public void dialogClosed(UIDialogEvent event)
  {
    if (event.getSource() == this.ivjQueryDlg) {
      if (event.m_Operation == 202)
      {
        ((ToftPanel)getIParent().getUiManager()).showHintMessage(NCLangRes.getInstance().getStrByID("commonres", "UC001-0000006") + NCLangRes.getInstance().getStrByID("commonres", "UCH026"));

        return;
      }
      if (event.m_Operation == 204)
        windowsCloseOK();
    }
    else if (event.getSource() == getPrintDlg()) {
      if (event.m_Operation == 202) {
        UiManager view = (UiManager)getIParent();
        view.showHintMessage(NCLangRes.getInstance().getStrByID("commonres", "UC001-0000007") + NCLangRes.getInstance().getStrByID("commonres", "UCH026"));

        return;
      }
      if (event.m_Operation == 204) {
        try {
          PrintCondVO printvo = new PrintCondVO();
          getPrintDlg().setPrintData(printvo);
          printvo = getPrintDlg().getPrintData();
          if (printvo.isBlnPrintAsModule()) {
            if (printvo.isBlnScopeAll())
              printAsModule(true);
            else
              printAsModule(false);
          }
          else
            print();
        }
        catch (Exception e)
        {
          Logger.info(e.getMessage());
        }

        ((ToftPanel)getIParent().getUiManager()).showHintMessage(NCLangRes.getInstance().getStrByID("commonres", "UC001-0000007") + NCLangRes.getInstance().getStrByID("commonres", "UCH026"));
      }
    }
  }

  public void firstPage()
    throws Exception
  {
    if (this.qryVO == null) {
      return;
    }

    if ((getGlQryCtner() != null) && (getGlQryCtner().getSize() > 0)) {
      this.iIndex = 0;
      BalanceSubjAssBSVO[] allBvos = (BalanceSubjAssBSVO[])(BalanceSubjAssBSVO[])getGlQryCtner().getVos();

      BalanceSubjAssBSVO[] tempBvos = null;
      GlQueryVO tempQryVO = getGlQryCtner().getQueryAt(this.iIndex);

      if (tempQryVO == null)
      {
        String assCode = (String)getGlQryCtner().getVKeys().elementAt(this.iIndex);
        GlQueryVO copyStartQryvo = (GlQueryVO)this.qryVO.clone();
        GlQueryVOAssTool.suplyCurrentGlQueryVO(copyStartQryvo);
        tempQryVO = GlQueryVOAssTool.getGlQueryVOMultiPage(this, copyStartQryvo, assCode);
        tempBvos = AssBlcResultGetter.getBlnVOsByQryVOs(assCode, allBvos);
        tempBvos = getIvjMdlSttl().getSettleedDatas(tempBvos, tempQryVO);
        getGlQryCtner().setElementAt(tempQryVO, tempBvos, this.iIndex);
      }
      else
      {
        tempBvos = (BalanceSubjAssBSVO[])(BalanceSubjAssBSVO[])getGlQryCtner().getBalaceVOsAt(this.iIndex);
      }

      Boolean isLocalAux = GLParaDataCache.getInstance().IsLocalFrac(tempQryVO.getBaseGlOrgBook());

      String pk_AuxCurrType = null; String pk_LocCurrType = null;
      if (isLocalAux.booleanValue()) {
        pk_AuxCurrType = GLParaDataCache.getInstance().PkFracCurr(tempQryVO.getBaseGlOrgBook());

        pk_LocCurrType = GLParaDataCache.getInstance().PkLocalCurr(tempQryVO.getBaseGlOrgBook());
      }

      TableDataModel dataModel = new TableDataModel(pk_LocCurrType, pk_AuxCurrType, 43, 40);

      dataModel.setData(tempBvos);

      getAssBalanceUI().setTableData(dataModel, tempQryVO);
    } else {
      this.iIndex = -1;
      getAssBalanceUI().setTableData(null, null);
    }
    resetFormat();
    showHintMessage();
    setButtonState();
  }

  public int getAssVOIndex()
  {
    return 0;
  }

  public IBillModel getBillModel()
  {
    return this.billModel;
  }

  public IParent getIParent()
  {
    return this.iParent;
  }

  public String[] getItemValuesByExpress(String itemExpress)
  {
    itemExpress = itemExpress.trim();
    if (itemExpress.equals("headdatescope")) {
      if (this.qryVO.isQueryByPeriod()) {
        return new String[] { this.qryVO.getYear() + "." + this.qryVO.getPeriod() + "-" + this.qryVO.getEndYear() + "." + this.qryVO.getEndPeriod() };
      }

      return new String[] { this.qryVO.getDate() + "-" + this.qryVO.getEndDate() };
    }
    if (itemExpress.equals("headcurrtype")) {
      return new String[] { getAssBalanceUI().getlbCurrTypeForPrint().getText() };
    }
    if (itemExpress.equals("explanation")) {
      return getAssBalanceUI().getPrintData(90);
    }

    if (itemExpress.equals("headcorp"))
    {
      return new String[] { "" };
    }if (itemExpress.equals("headglorgbook")) {
      String[] strPkorgbooks = this.qryVO.getPk_glorgbook();
      String strorgName = "";
      GlorgbookVO[] orgbookVOs = new GlorgbookVO[strPkorgbooks.length];
      try {
        for (int i = 0; i < strPkorgbooks.length; i++) {
          orgbookVOs[i] = BDGLOrgBookAccessor.getGlOrgBookVOByPrimaryKey(strPkorgbooks[i]);

          strorgName = strorgName + "," + orgbookVOs[i].getGlorgbookname();
        }
      }
      catch (Exception ee) {
        handleException(ee);
      }
      strorgName = strorgName.substring(1);
      return new String[] { strorgName };
    }if (itemExpress.equals("corp")) {
      String[] corpName = getAssBalanceUI().getPrintData(1);

      return corpName;
    }if (itemExpress.equals("bodyglorgbook"))
    {
      return getAssBalanceUI().getPrintData(104);
    }

    if (itemExpress.equals("subjcode")) {
      return getAssBalanceUI().getPrintData(45);
    }
    if (itemExpress.equals("subjname")) {
      return getAssBalanceUI().getPrintData(46);
    }
    if (itemExpress.equals("bodycurrtype")) {
      String[] currType = getAssBalanceUI().getPrintData(2);

      return currType;
    }if (itemExpress.equals("drqtbegin")) {
      return getAssBalanceUI().getPrintData(3);
    }
    if (itemExpress.equals("drbegin")) {
      return getAssBalanceUI().getPrintData(4);
    }
    if (itemExpress.equals("drfragbegin")) {
      return getAssBalanceUI().getPrintData(5);
    }
    if (itemExpress.equals("drlocalbegin")) {
      return getAssBalanceUI().getPrintData(6);
    }
    if (itemExpress.equals("crqtbegin")) {
      return getAssBalanceUI().getPrintData(7);
    }
    if (itemExpress.equals("crbegin")) {
      return getAssBalanceUI().getPrintData(8);
    }
    if (itemExpress.equals("crfragbegin")) {
      return getAssBalanceUI().getPrintData(9);
    }
    if (itemExpress.equals("crlocalbegin")) {
      return getAssBalanceUI().getPrintData(10);
    }
    if (itemExpress.equals("beginBalanceOrient")) {
      return getAssBalanceUI().getPrintData(35);
    }
    if (itemExpress.equals("beginBalanceQuantity")) {
      return getAssBalanceUI().getPrintData(60);
    }
    if (itemExpress.equals("beginBalanceAmount")) {
      return getAssBalanceUI().getPrintData(61);
    }
    if (itemExpress.equals("fragbegin")) {
      return getAssBalanceUI().getPrintData(62);
    }
    if (itemExpress.equals("beginBalanceLocAmount")) {
      return getAssBalanceUI().getPrintData(63);
    }
    if (itemExpress.equals("debitQuantity")) {
      return getAssBalanceUI().getPrintData(11);
    }
    if (itemExpress.equals("debitAmount")) {
      return getAssBalanceUI().getPrintData(12);
    }
    if (itemExpress.equals("drfrag")) {
      return getAssBalanceUI().getPrintData(13);
    }
    if (itemExpress.equals("debitLocAmount")) {
      return getAssBalanceUI().getPrintData(14);
    }
    if (itemExpress.equals("creditQuantity")) {
      return getAssBalanceUI().getPrintData(15);
    }
    if (itemExpress.equals("creditAmount")) {
      return getAssBalanceUI().getPrintData(16);
    }
    if (itemExpress.equals("crfrag")) {
      return getAssBalanceUI().getPrintData(17);
    }
    if (itemExpress.equals("creditLocAmount")) {
      return getAssBalanceUI().getPrintData(18);
    }
    if (itemExpress.equals("debitLjQuantity")) {
      return getAssBalanceUI().getPrintData(19);
    }
    if (itemExpress.equals("debitLjAmount")) {
      return getAssBalanceUI().getPrintData(20);
    }
    if (itemExpress.equals("sumdrfrag")) {
      return getAssBalanceUI().getPrintData(21);
    }
    if (itemExpress.equals("debitLjLocAmount")) {
      return getAssBalanceUI().getPrintData(22);
    }
    if (itemExpress.equals("creditLjQuantity")) {
      return getAssBalanceUI().getPrintData(23);
    }
    if (itemExpress.equals("creditLjAmount")) {
      return getAssBalanceUI().getPrintData(24);
    }
    if (itemExpress.equals("sumfrag")) {
      return getAssBalanceUI().getPrintData(25);
    }
    if (itemExpress.equals("creditLjLocAmount")) {
      return getAssBalanceUI().getPrintData(26);
    }
    if (itemExpress.equals("drqtend")) {
      return getAssBalanceUI().getPrintData(27);
    }
    if (itemExpress.equals("drend")) {
      return getAssBalanceUI().getPrintData(28);
    }
    if (itemExpress.equals("drfragend")) {
      return getAssBalanceUI().getPrintData(29);
    }
    if (itemExpress.equals("drlocalend")) {
      return getAssBalanceUI().getPrintData(30);
    }
    if (itemExpress.equals("crqtend")) {
      return getAssBalanceUI().getPrintData(31);
    }
    if (itemExpress.equals("crend")) {
      return getAssBalanceUI().getPrintData(32);
    }
    if (itemExpress.equals("crfragend")) {
      return getAssBalanceUI().getPrintData(33);
    }
    if (itemExpress.equals("crlocalend")) {
      return getAssBalanceUI().getPrintData(34);
    }
    if (itemExpress.equals("endBalanceOrient")) {
      return getAssBalanceUI().getPrintData(36);
    }
    if (itemExpress.equals("endBalanceQuantity")) {
      return getAssBalanceUI().getPrintData(64);
    }
    if (itemExpress.equals("endBalanceAmount")) {
      return getAssBalanceUI().getPrintData(65);
    }
    if (itemExpress.equals("endfrag")) {
      return getAssBalanceUI().getPrintData(66);
    }
    if (itemExpress.equals("endBalanceLocalAmount")) {
      return getAssBalanceUI().getPrintData(67);
    }
    if (!itemExpress.equals("accessorial1"))
    {
      if (!itemExpress.equals("accessorial2"))
      {
        if (!itemExpress.equals("accessorial3"))
        {
          if (!itemExpress.equals("acctype1"))
          {
            if (!itemExpress.equals("acctype2"))
            {
              if (!itemExpress.equals("acctype3"))
              {
                if (itemExpress.equals("accessorial"))
                  return getAssBalanceUI().getPrintData(0);
                if (itemExpress.equals("accessorialtype")) {
                  return new String[] { NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000163") };
                }

                if (itemExpress.equals("queryvalue")) {
                  if (getAssBalanceUI().getFormat().getAssShowType() == 1)
                  {
                    return this.dptool.getDynamicColumnValuesByExpress(1, new BalanceSubjAssView());
                  }

                  if (getAssBalanceUI().getFormat().getAssShowType() == 2)
                  {
                    return this.dptool.getDynamicColumnValuesByExpress(0, new BalanceSubjAssView());
                  }

                  if (getAssBalanceUI().getFormat().getAssShowType() == 3)
                  {
                    return this.dptool.getDynamicColumnValuesByExpress(2, new BalanceSubjAssView());
                  }

                }
                else if (itemExpress.equals("queryobject")) {
                  if (getAssBalanceUI().getFormat().getAssShowType() == 1)
                  {
                    return this.dptool.getDynamicColumnNamesByExpress(1);
                  }

                  if (getAssBalanceUI().getFormat().getAssShowType() == 2)
                  {
                    return this.dptool.getDynamicColumnNamesByExpress(0);
                  }

                  if (getAssBalanceUI().getFormat().getAssShowType() == 3)
                  {
                    return this.dptool.getDynamicColumnNamesByExpress(2);
                  }
                }
                else
                {
                  if (itemExpress.equals("headsubject")) {
                    String ret = getAssBalanceUI().getlbSubj().getText();
                    return new String[] { ret };
                  }if (itemExpress.equals("unit")) {
                    String headUnit = null;
                    if ((this.qryVO.getSubjVersionYear() == null) || (this.qryVO.getSubjVersionYear().equals("")) || (this.qryVO.getSubjVerisonPeriod() == null) || (this.qryVO.getSubjVerisonPeriod().equals("")))
                    {
                      if (AccsubjDataCache.getInstance().getAccsubjVOByPK(this.qryVO.getPk_accsubj()[0]) != null)
                      {
                        headUnit = AccsubjDataCache.getInstance().getAccsubjVOByPK(this.qryVO.getPk_accsubj()[0]).getUnit();
                      }
                    }
                    else if (AccsubjDataCache.getInstance().getAccsubjVOByPK(this.qryVO.getPk_accsubj()[0], this.qryVO.getSubjVersionYear(), this.qryVO.getSubjVerisonPeriod()) != null)
                    {
                      headUnit = AccsubjDataCache.getInstance().getAccsubjVOByPK(this.qryVO.getPk_accsubj()[0], this.qryVO.getSubjVersionYear(), this.qryVO.getSubjVerisonPeriod()).getUnit();
                    }

                    return new String[] { headUnit };
                  }
                }
              }
            }
          }
        }
      }
    }
    return null;
  }

  public PrintDialog getPrintDlg()
  {
    if (this.ivjPrintDlg == null) {
      try {
        this.ivjPrintDlg = new PrintDialog(this);
        this.ivjPrintDlg.setName("PrintDlg");
        this.ivjPrintDlg.setDefaultCloseOperation(2);

        this.ivjPrintDlg.addUIDialogListener(this);

        String pk_user = ClientEnvironment.getInstance().getUser().getPrimaryKey();

        PrintEntry printEntry = new PrintEntry(null);

        String pk_loginGlorgbook = ((GlorgbookVO)ClientEnvironment.getInstance().getValue("pk_glorgbook")).getPrimaryKey();

        printEntry.setTemplateID(pk_loginGlorgbook, "20023055", pk_user, null, null, "2");

        PrintTempletmanageHeaderVO[] headvos = printEntry.getAllTemplates();

        PrintCondVO condvo = new PrintCondVO();
        condvo.setPrintModule(headvos);
        this.ivjPrintDlg.setPrintData(condvo);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjPrintDlg;
  }

  private QueryDlg1 getQueryDlg()
  {
    if (this.ivjQueryDlg == null) {
      try {
        this.ivjQueryDlg = new QueryDlg1(this);
        this.ivjQueryDlg.setName("QueryDlg");

        this.ivjQueryDlg.addUIDialogListener(this);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjQueryDlg;
  }

  public int getSubjCodeIndex()
  {
    return 45;
  }

  public int getSubjNameIndex()
  {
    return 46;
  }

  private BalanceSubjAssUI getAssBalanceUI()
  {
    if (this.ivjUI == null) {
      try {
        this.ivjUI = new BalanceSubjAssUI();
        this.ivjUI.setName("UI");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUI;
  }

  private void goForDetail() {
    if (this.iIndex < 0)
    {
      goForDetailOnePage();
    }
    else
    {
      goForDetailMultiPage();
    }
  }

  private void goForDetailMultiPage()
  {
    try
    {
      int iCurrent = getAssBalanceUI().getSelectedRow();
      if (getGlQryCtner().getSize() <= 0)
        return;
      BalanceSubjAssBSVO[] voData = (BalanceSubjAssBSVO[])(BalanceSubjAssBSVO[])getGlQryCtner().getBalaceVOsAt(this.iIndex);

      GlQueryVO qryVo1 = (GlQueryVO)getGlQryCtner().getQueryAt(this.iIndex).clone();

      if ((qryVo1.getPk_glorgbook().length > 1) && (qryVo1.isMultiCorpCombine()))
      {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000164"), NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000165"));

        return;
      }

      String tempyear = null;
      String tempendyear = null;
      if (qryVo1.isQueryByPeriod()) {
        tempyear = qryVo1.getYear();
        tempendyear = qryVo1.getEndYear();
      } else {
        UFDate date1 = qryVo1.getDate();
        UFDate date2 = qryVo1.getEndDate();

        GlPeriodVO period1 = null; GlPeriodVO period2 = null;
        try
        {
          period1 = GLPeriodDataCatch.getInstance().getGLperiodVOByDate(qryVo1.getPk_glorgbook()[0], date1);
          period2 = GLPeriodDataCatch.getInstance().getGLperiodVOByDate(qryVo1.getPk_glorgbook()[0], date2);
        }
        catch (Exception e) {
          e.printStackTrace();
        }

        tempyear = period1.getYear();
        tempendyear = period2.getYear();
      }

      if (!tempyear.equals(tempendyear)) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000164"), NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000166"));

        return;
      }
      if ((qryVo1.getFormatVO().getBalanceOrient() != 0) || ((qryVo1.getFormatVO().getBalanceRangeFrom() != null) && (qryVo1.getFormatVO().getBalanceRangeFrom().abs().doubleValue() != 0.0D)) || ((qryVo1.getFormatVO().getBalanceRangeTo() != null) && (qryVo1.getFormatVO().getBalanceRangeTo().abs().doubleValue() != 0.0D)))
      {
        MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000167"), NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000168"));
      }

      if ((qryVo1.getPk_glorgbook().length >= 2) && (iCurrent >= 0) && (voData[iCurrent].getValue(90) != null) && (!voData[iCurrent].getValue(90).equals(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000115"))))
      {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000164"), NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000169"));

        return;
      }

      if (this.m_AssUI == null) {
        this.m_AssUI = ((nc.ui.gl.assdetail.ToftPanelView)this.iParent.showNext("nc.ui.gl.assdetail.ToftPanelView", new Integer[] { new Integer(2) }));
      }
      else
      {
        this.iParent.showNext(this.m_AssUI);
      }GLQueryObj[] glQryObjs = null;

      if ((iCurrent < 0) || ((iCurrent >= 0) && (voData[iCurrent].getValue(90) != null) && (voData[iCurrent].getValue(90).equals(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000115")))))
      {
        glQryObjs = qryVo1.getFormatVO().getQryObjs();
        for (int i = 0; i < glQryObjs.length; i++) {
          if (glQryObjs[i].getType().equals("会计科目")) {
            if (glQryObjs[i].getHeadEle()) {
              AssVO assVo = new AssVO();
              assVo.setPk_Checkvalue(qryVo1.getPk_accsubj()[0]);
              assVo.setCheckvaluecode(qryVo1.getSubjCodes()[0]);
              glQryObjs[i].setValueRange(new AssVO[] { assVo });
            }

          }
          else if (glQryObjs[i].getHeadEle()) {
            for (int j = 0; j < qryVo1.getAssVos().length; j++) {
              if (!glQryObjs[i].getValueRange()[0].getPk_Checktype().equals(qryVo1.getAssVos()[j].getPk_Checktype()))
              {
                continue;
              }
              glQryObjs[i].setValueRange(new AssVO[] { qryVo1.getAssVos()[j] });
            }
          }

        }

      }
      else
      {
        GLQueryObj[] glQryObjsTemp = qryVo1.getFormatVO().getQryObjs();

        glQryObjs = new GLQueryObj[glQryObjsTemp.length];
        for (int i = 0; i < glQryObjsTemp.length; i++) {
          glQryObjs[i] = new GLQueryObj();
        }
        for (int i = 0; i < glQryObjsTemp.length; i++) {
          if ((glQryObjsTemp[i].getType() == null) || (glQryObjsTemp[i].getType().equals("")))
            continue;
          if (glQryObjsTemp[i].getType().equals("会计科目")) {
            AssVO[] assVos = null;
            if (voData[iCurrent].getValue(45) == null)
            {
              assVos = new AssVO[qryVo1.getPk_accsubj().length];

              for (int j = 0; j < qryVo1.getPk_accsubj().length; j++) {
                AssVO assVoTemp = new AssVO();
                assVoTemp.setPk_Checkvalue(qryVo1.getPk_accsubj()[j]);

                assVoTemp.setCheckvaluecode(qryVo1.getAccsubjCode()[j]);

                assVos[j] = assVoTemp;
              }
            } else {
              AssVO assVo = new AssVO();
              assVo.setPk_Checkvalue(voData[iCurrent].getValue(42).toString());

              assVo.setCheckvaluecode(voData[iCurrent].getValue(45).toString());

              assVos = new AssVO[1];
              assVos[0] = assVo;
            }
            glQryObjs[i].setValueRange(assVos);
            glQryObjs[i].setHeadEle(glQryObjsTemp[i].getHeadEle());

            glQryObjs[i].setType("会计科目");
            glQryObjs[i].setTypeName(glQryObjsTemp[i].getTypeName());

            glQryObjs[i].setIncludeSub(glQryObjsTemp[i].getIncludeSub());

            glQryObjs[i].setHeadEle(glQryObjsTemp[i].getHeadEle());

            glQryObjs[i].setAccEle(glQryObjsTemp[i].getAccEle());
          }
          else {
            int iIndex = -1;
            for (int j = 0; j < ((AssVO[])(AssVO[])voData[iCurrent].getValue(0)).length; )
            {
              if (glQryObjsTemp[i].getValueRange()[0].getPk_Checktype().equals(((AssVO[])(AssVO[])voData[iCurrent].getValue(0))[j].getPk_Checktype()))
              {
                iIndex = j;
                break;
              }
              j++;
            }

            AssVO assVo = (AssVO)((AssVO[])(AssVO[])voData[iCurrent].getValue(0))[iIndex].clone();

            String pkOrgTemp = voData[iCurrent].getValue(102).toString();

            for (int k = 0; k < glQryObjsTemp[i].getValueRange().length; )
            {
              AssVO assVoTT = glQryObjsTemp[i].getValueRange()[k];

              if ((!qryVo1.isMultiCorpCombine()) && (pkOrgTemp != null))
              {
                AssCodeConstructor constructor = new AssCodeConstructor();
                BddataVO[] dataVos = constructor.getallBasedoc(getPk_corp(pkOrgTemp), assVo.getPk_Checktype());

                for (int l = 0; l < dataVos.length; l++) {
                  if (!assVo.getCheckvaluecode().equals(dataVos[l].getCode()))
                    continue;
                  assVo.setPk_Checkvalue(dataVos[l].getPk());

                  break;
                }
              }

              if ((assVo.getPk_Checktype().equals(assVoTT.getPk_Checktype())) && (assVo.getPk_Checkvalue().equals(assVoTT.getPk_Checkvalue())))
              {
                assVo.setUserData(assVoTT.getUserData());
                assVo.setChecktypecode(assVoTT.getChecktypecode());

                assVo.setChecktypename(assVoTT.getChecktypename());
              }
              k++;
            }

            AssVO[] assVos = null;
            if (assVo.getCheckvaluecode() != null) {
              if (assVo.getCheckvaluecode().trim().equalsIgnoreCase("null"))
              {
                assVo.setPk_Checkvalue("");
                assVo.setCheckvaluecode(null);
                assVo.setCheckvaluename(null);
                assVo.setUserData(null);
              } else if (assVo.getCheckvaluecode().trim().equals(""))
              {
                Vector vAssVosTemp = new Vector();
                for (int j = 0; j < this.qryVO.getAssVos().length; j++) {
                  if (!this.qryVO.getAssVos()[j].getChecktypename().equals(glQryObjsTemp[i].getType()))
                  {
                    continue;
                  }
                  vAssVosTemp.addElement(this.qryVO.getAssVos()[j]);
                }

                assVos = new AssVO[vAssVosTemp.size()];

                vAssVosTemp.copyInto(assVos);
              }

            }

            if (assVos == null) {
              assVos = new AssVO[] { assVo };
            }
            glQryObjs[i].setValueRange(assVos);
            glQryObjs[i].setHeadEle(glQryObjsTemp[i].getHeadEle());

            glQryObjs[i].setType(glQryObjsTemp[i].getType());
            glQryObjs[i].setTypeName(glQryObjsTemp[i].getTypeName());

            glQryObjs[i].setIncludeSub(glQryObjsTemp[i].getIncludeSub());

            glQryObjs[i].setHeadEle(glQryObjsTemp[i].getHeadEle());

            glQryObjs[i].setLinkObj(glQryObjsTemp[i].getLinkObj());

            glQryObjs[i].setAccEle(glQryObjsTemp[i].getAccEle());
          }

        }

        if (qryVo1.getCurrTypeName().equals(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000116")))
        {
          qryVo1.setPk_currtype(voData[iCurrent].getValue(49).toString());
        }

        if (!qryVo1.isMultiCorpCombine()) {
          qryVo1.setPk_glorgbook(new String[] { voData[iCurrent].getValue(102).toString() });

          qryVo1.setBaseGlOrgBook(voData[iCurrent].getValue(102).toString());
        }

        qryVo1.getFormatVO().setQryObjs(glQryObjs);
      }
      this.m_AssUI.invoke(qryVo1, "setQryVO");
    } catch (Exception e) {
      handleException(e);
    }
  }

  private void goForDetailOnePage()
  {
    try
    {
      int iCurrent = getAssBalanceUI().getSelectedRow();
      BalanceSubjAssBSVO[] voData = (BalanceSubjAssBSVO[])getAssBalanceUI().getM_TableModel().getData();
      GlQueryVO qryVo1 = (GlQueryVO)getQueryDlg().getqryVO().clone();
      if ((qryVo1.getPk_glorgbook().length > 1) && (qryVo1.isMultiCorpCombine())) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000164"), NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000165"));
        return;
      }

      String tempyear = null;
      String tempendyear = null;
      if (qryVo1.isQueryByPeriod()) {
        tempyear = qryVo1.getYear();
        tempendyear = qryVo1.getEndYear();
      }
      else
      {
        UFDate date1 = qryVo1.getDate();
        UFDate date2 = qryVo1.getEndDate();

        GlPeriodVO period1 = null; GlPeriodVO period2 = null;
        try
        {
          period1 = GLPeriodDataCatch.getInstance().getGLperiodVOByDate(qryVo1.getPk_glorgbook()[0], date1);
          period2 = GLPeriodDataCatch.getInstance().getGLperiodVOByDate(qryVo1.getPk_glorgbook()[0], date2);
        }
        catch (Exception e) {
          e.printStackTrace();
        }

        tempyear = period1.getYear();
        tempendyear = period2.getYear();
      }

      if (!tempyear.equals(tempendyear)) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000164"), NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000166"));
        return;
      }
      if ((qryVo1.getFormatVO().getBalanceOrient() != 0) || ((qryVo1.getFormatVO().getBalanceRangeFrom() != null) && (qryVo1.getFormatVO().getBalanceRangeFrom().abs().doubleValue() != 0.0D)) || ((qryVo1.getFormatVO().getBalanceRangeTo() != null) && (qryVo1.getFormatVO().getBalanceRangeTo().abs().doubleValue() != 0.0D)))
      {
        MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000167"), NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000168"));
      }
      if ((qryVo1.getPk_glorgbook().length >= 2) && (iCurrent >= 0) && (voData[iCurrent].getValue(90) != null) && (!voData[iCurrent].getValue(90).equals(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000115"))))
      {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000164"), NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000169"));
        return;
      }

      if (this.m_AssUI == null)
        this.m_AssUI = ((nc.ui.gl.assdetail.ToftPanelView)this.iParent.showNext("nc.ui.gl.assdetail.ToftPanelView", new Integer[] { new Integer(2) }));
      else
        this.iParent.showNext(this.m_AssUI);
      GLQueryObj[] glQryObjs = null;

      if ((iCurrent < 0) || ((iCurrent >= 0) && (voData[iCurrent].getValue(90) != null) && (voData[iCurrent].getValue(90).equals(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000115")))))
      {
        glQryObjs = qryVo1.getFormatVO().getQryObjs();
        for (int i = 0; i < glQryObjs.length; i++) {
          if (glQryObjs[i].getType().equals("会计科目")) {
            if (glQryObjs[i].getHeadEle()) {
              AssVO assVo = new AssVO();
              assVo.setPk_Checkvalue(qryVo1.getPk_accsubj()[0]);
              assVo.setCheckvaluecode(qryVo1.getSubjCodes()[0]);
              glQryObjs[i].setValueRange(new AssVO[] { assVo });
            }
          }
          else if (glQryObjs[i].getHeadEle()) {
            for (int j = 0; j < qryVo1.getAssVos().length; j++) {
              if (glQryObjs[i].getValueRange()[0].getPk_Checktype().equals(qryVo1.getAssVos()[j].getPk_Checktype()))
                glQryObjs[i].setValueRange(new AssVO[] { qryVo1.getAssVos()[j] });
            }
          }
        }
      }
      else
      {
        GLQueryObj[] glQryObjsTemp = qryVo1.getFormatVO().getQryObjs();
        glQryObjs = new GLQueryObj[glQryObjsTemp.length];
        for (int i = 0; i < glQryObjsTemp.length; i++) {
          glQryObjs[i] = new GLQueryObj();
        }
        for (int i = 0; i < glQryObjsTemp.length; i++) {
          if ((glQryObjsTemp[i].getType() != null) && (!glQryObjsTemp[i].getType().equals(""))) {
            if (glQryObjsTemp[i].getType().equals("会计科目")) {
              AssVO[] assVos = null;
              if (voData[iCurrent].getValue(45) == null) {
                assVos = new AssVO[qryVo1.getPk_accsubj().length];
                for (int j = 0; j < qryVo1.getPk_accsubj().length; j++) {
                  AssVO assVoTemp = new AssVO();
                  assVoTemp.setPk_Checkvalue(qryVo1.getPk_accsubj()[j]);
                  assVoTemp.setCheckvaluecode(qryVo1.getAccsubjCode()[j]);
                  assVos[j] = assVoTemp;
                }
              } else {
                AssVO assVo = new AssVO();
                assVo.setPk_Checkvalue(voData[iCurrent].getValue(42).toString());
                assVo.setCheckvaluecode(voData[iCurrent].getValue(45).toString());
                assVos = new AssVO[1];
                assVos[0] = assVo;
              }
              glQryObjs[i].setValueRange(assVos);
              glQryObjs[i].setHeadEle(glQryObjsTemp[i].getHeadEle());
              glQryObjs[i].setType("会计科目");
              glQryObjs[i].setTypeName(glQryObjsTemp[i].getTypeName());
              glQryObjs[i].setIncludeSub(glQryObjsTemp[i].getIncludeSub());
              glQryObjs[i].setHeadEle(glQryObjsTemp[i].getHeadEle());
              glQryObjs[i].setAccEle(glQryObjsTemp[i].getAccEle());
            } else {
              int iIndex = -1;
              for (int j = 0; j < ((AssVO[])(AssVO[])voData[iCurrent].getValue(0)).length; j++) {
                if (!glQryObjsTemp[i].getValueRange()[0].getPk_Checktype().equals(((AssVO[])(AssVO[])voData[iCurrent].getValue(0))[j].getPk_Checktype()))
                {
                  continue;
                }
                iIndex = j;
                break;
              }

              AssVO assVo = (AssVO)((AssVO[])(AssVO[])voData[iCurrent].getValue(0))[iIndex].clone();
              String pkOrgTemp = voData[iCurrent].getValue(102).toString();
              for (int k = 0; k < glQryObjsTemp[i].getValueRange().length; k++) {
                AssVO assVoTT = glQryObjsTemp[i].getValueRange()[k];
                if ((!qryVo1.isMultiCorpCombine()) && (pkOrgTemp != null)) {
                  AssCodeConstructor constructor = new AssCodeConstructor();
                  BddataVO[] dataVos = constructor.getallBasedoc(getPk_corp(pkOrgTemp), assVo.getPk_Checktype());
                  for (int l = 0; l < dataVos.length; l++) {
                    if (assVo.getCheckvaluecode().equals(dataVos[l].getCode())) {
                      assVo.setPk_Checkvalue(dataVos[l].getPk());
                      break;
                    }
                  }
                }
                if ((assVo.getPk_Checktype().equals(assVoTT.getPk_Checktype())) && (assVo.getPk_Checkvalue().equals(assVoTT.getPk_Checkvalue()))) {
                  assVo.setUserData(assVoTT.getUserData());
                  assVo.setChecktypecode(assVoTT.getChecktypecode());
                  assVo.setChecktypename(assVoTT.getChecktypename());
                }
              }
              AssVO[] assVos = null;
              if (assVo.getCheckvaluecode() != null) {
                if (assVo.getCheckvaluecode().trim().equalsIgnoreCase("null")) {
                  assVo.setPk_Checkvalue("");
                  assVo.setCheckvaluecode(null);
                  assVo.setCheckvaluename(null);
                  assVo.setUserData(null);
                } else if (assVo.getCheckvaluecode().trim().equals("")) {
                  Vector vAssVosTemp = new Vector();
                  for (int j = 0; j < getQueryDlg().getqryVO().getAssVos().length; j++) {
                    if (getQueryDlg().getqryVO().getAssVos()[j].getChecktypename().equals(glQryObjsTemp[i].getType())) {
                      vAssVosTemp.addElement(getQueryDlg().getqryVO().getAssVos()[j]);
                    }
                  }
                  assVos = new AssVO[vAssVosTemp.size()];
                  vAssVosTemp.copyInto(assVos);
                }

              }

              if (assVos == null) {
                assVos = new AssVO[] { assVo };
              }
              glQryObjs[i].setValueRange(assVos);
              glQryObjs[i].setHeadEle(glQryObjsTemp[i].getHeadEle());
              glQryObjs[i].setType(glQryObjsTemp[i].getType());
              glQryObjs[i].setTypeName(glQryObjsTemp[i].getTypeName());
              glQryObjs[i].setIncludeSub(glQryObjsTemp[i].getIncludeSub());
              glQryObjs[i].setHeadEle(glQryObjsTemp[i].getHeadEle());
              glQryObjs[i].setLinkObj(glQryObjsTemp[i].getLinkObj());
              glQryObjs[i].setAccEle(glQryObjsTemp[i].getAccEle());
            }
          }
        }
        if (qryVo1.getCurrTypeName().equals(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000116"))) {
          qryVo1.setPk_currtype(voData[iCurrent].getValue(49).toString());
        }
        if (!qryVo1.isMultiCorpCombine()) {
          qryVo1.setPk_glorgbook(new String[] { voData[iCurrent].getValue(102).toString() });
          qryVo1.setBaseGlOrgBook(voData[iCurrent].getValue(102).toString());
        }
        qryVo1.getFormatVO().setQryObjs(glQryObjs);
      }
      this.m_AssUI.invoke(qryVo1, "setQryVO");
    } catch (Exception e) {
      handleException(e);
    }
  }

  private void handleException(Throwable exception)
  {
    Logger.info("--------- 未捕捉到的异常 ---------");
    exception.printStackTrace(System.out);
  }

  private void initialize()
  {
    try
    {
      setName("BalanceSubjAssView");
      setLayout(new BorderLayout());
      setSize(777, 411);
      add(getAssBalanceUI(), "Center");
    } catch (Throwable ivjExc) {
      handleException(ivjExc);
    }

    getAssBalanceUI().resetFormat(getAssBalanceUI().getFormat());
    setButtonState();
  }

  public void lastPage()
    throws Exception
  {
    if (this.qryVO == null) {
      return;
    }
    this.iIndex = getGlQryCtner().getSize();
    this.iIndex -= 1;

    BalanceSubjAssBSVO[] allBvos = (BalanceSubjAssBSVO[])(BalanceSubjAssBSVO[])getGlQryCtner().getVos();

    BalanceSubjAssBSVO[] tempBvos = null;
    GlQueryVO tempQryVO = getGlQryCtner().getQueryAt(this.iIndex);

    if (tempQryVO == null)
    {
      String assCode = (String)getGlQryCtner().getVKeys().elementAt(this.iIndex);
      GlQueryVO copyStartQryvo = (GlQueryVO)this.qryVO.clone();
      GlQueryVOAssTool.suplyCurrentGlQueryVO(copyStartQryvo);
      tempQryVO = GlQueryVOAssTool.getGlQueryVOMultiPage(this, copyStartQryvo, assCode);
      tempBvos = AssBlcResultGetter.getBlnVOsByQryVOs(assCode, allBvos);
      tempBvos = getIvjMdlSttl().getSettleedDatas(tempBvos, tempQryVO);
      getGlQryCtner().setElementAt(tempQryVO, tempBvos, this.iIndex);
    }
    else
    {
      tempBvos = (BalanceSubjAssBSVO[])(BalanceSubjAssBSVO[])getGlQryCtner().getBalaceVOsAt(this.iIndex);
    }

    Boolean isLocalAux = GLParaDataCache.getInstance().IsLocalFrac(tempQryVO.getBaseGlOrgBook());

    String pk_AuxCurrType = null; String pk_LocCurrType = null;
    if (isLocalAux.booleanValue()) {
      pk_AuxCurrType = GLParaDataCache.getInstance().PkFracCurr(tempQryVO.getBaseGlOrgBook());

      pk_LocCurrType = GLParaDataCache.getInstance().PkLocalCurr(tempQryVO.getBaseGlOrgBook());
    }

    TableDataModel dataModel = new TableDataModel(pk_LocCurrType, pk_AuxCurrType, 43, 40);

    dataModel.setData(tempBvos);

    getAssBalanceUI().setTableData(dataModel, tempQryVO);
    resetFormat();
    setButtonState();
    showHintMessage();
  }

  public void nextPage()
    throws Exception
  {
    if (this.qryVO == null)
      return;
    if (this.iIndex + 1 >= getGlQryCtner().getSize())
      return;
    this.iIndex += 1;

    BalanceSubjAssBSVO[] allBvos = (BalanceSubjAssBSVO[])(BalanceSubjAssBSVO[])getGlQryCtner().getVos();

    BalanceSubjAssBSVO[] tempBvos = null;
    GlQueryVO tempQryVO = getGlQryCtner().getQueryAt(this.iIndex);

    if (tempQryVO == null)
    {
      String assCode = (String)getGlQryCtner().getVKeys().elementAt(this.iIndex);
      GlQueryVO copyStartQryvo = (GlQueryVO)this.qryVO.clone();
      GlQueryVOAssTool.suplyCurrentGlQueryVO(copyStartQryvo);
      tempQryVO = GlQueryVOAssTool.getGlQueryVOMultiPage(this, copyStartQryvo, assCode);
      tempBvos = AssBlcResultGetter.getBlnVOsByQryVOs(assCode, allBvos);
      tempBvos = getIvjMdlSttl().getSettleedDatas(tempBvos, tempQryVO);
      getGlQryCtner().setElementAt(tempQryVO, tempBvos, this.iIndex);
    }
    else
    {
      tempBvos = (BalanceSubjAssBSVO[])(BalanceSubjAssBSVO[])getGlQryCtner().getBalaceVOsAt(this.iIndex);
    }

    Boolean isLocalAux = GLParaDataCache.getInstance().IsLocalFrac(tempQryVO.getBaseGlOrgBook());

    String pk_AuxCurrType = null; String pk_LocCurrType = null;
    if (isLocalAux.booleanValue()) {
      pk_AuxCurrType = GLParaDataCache.getInstance().PkFracCurr(tempQryVO.getBaseGlOrgBook());

      pk_LocCurrType = GLParaDataCache.getInstance().PkLocalCurr(tempQryVO.getBaseGlOrgBook());
    }

    TableDataModel dataModel = new TableDataModel(pk_LocCurrType, pk_AuxCurrType, 43, 40);

    dataModel.setData(tempBvos);

    getAssBalanceUI().setTableData(dataModel, tempQryVO);
    resetFormat();
    showHintMessage();
    setButtonState();
  }

  public void prevPage()
    throws Exception
  {
    if (this.qryVO == null)
      return;
    if (this.iIndex - 1 < 0)
      return;
    this.iIndex -= 1;

    BalanceSubjAssBSVO[] allBvos = (BalanceSubjAssBSVO[])(BalanceSubjAssBSVO[])getGlQryCtner().getVos();

    BalanceSubjAssBSVO[] tempBvos = null;
    GlQueryVO tempQryVO = getGlQryCtner().getQueryAt(this.iIndex);

    if (tempQryVO == null)
    {
      String assCode = (String)getGlQryCtner().getVKeys().elementAt(this.iIndex);
      GlQueryVO copyStartQryvo = (GlQueryVO)this.qryVO.clone();
      GlQueryVOAssTool.suplyCurrentGlQueryVO(copyStartQryvo);
      tempQryVO = GlQueryVOAssTool.getGlQueryVOMultiPage(this, copyStartQryvo, assCode);
      tempBvos = AssBlcResultGetter.getBlnVOsByQryVOs(assCode, allBvos);
      tempBvos = getIvjMdlSttl().getSettleedDatas(tempBvos, tempQryVO);
      getGlQryCtner().setElementAt(tempQryVO, tempBvos, this.iIndex);
    }
    else
    {
      tempBvos = (BalanceSubjAssBSVO[])(BalanceSubjAssBSVO[])getGlQryCtner().getBalaceVOsAt(this.iIndex);
    }

    Boolean isLocalAux = GLParaDataCache.getInstance().IsLocalFrac(tempQryVO.getBaseGlOrgBook());

    String pk_AuxCurrType = null; String pk_LocCurrType = null;
    if (isLocalAux.booleanValue()) {
      pk_AuxCurrType = GLParaDataCache.getInstance().PkFracCurr(tempQryVO.getBaseGlOrgBook());

      pk_LocCurrType = GLParaDataCache.getInstance().PkLocalCurr(tempQryVO.getBaseGlOrgBook());
    }

    TableDataModel dataModel = new TableDataModel(pk_LocCurrType, pk_AuxCurrType, 43, 40);

    dataModel.setData(tempBvos);

    getAssBalanceUI().setTableData(dataModel, tempQryVO);

    resetFormat();
    showHintMessage();
    setButtonState();
  }

  private void print()
    throws Exception
  {
    PrintDirectEntry printEntry = new PrintDirectEntry(this);

    printEntry.setTitle(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000170"));

    ColFormatVO[] formats = getAssBalanceUI().getFixModel().getVo().getColFormatVOs();

    printEntry.setMargin(10, 10, 10, 10);

    int[] lineHeight = new int[getAssBalanceUI().getFixModel().getRowCount() + 2];

    lineHeight[0] = 15;
    lineHeight[1] = 15;
    for (int i = 0; i < getAssBalanceUI().getFixModel().getRowCount(); i++) {
      lineHeight[(i + 2)] = 15;
    }
    printEntry.setRowHeight(lineHeight);

    Vector vLine1 = new Vector();
    Vector vLine0 = new Vector();
    Vector align = new Vector();

    Vector vColWidths = new Vector();
    int fixCol = 0;

    for (int i = 0; i < formats.length; i++) {
      if (formats[i].isVisiablity()) {
        vLine0.add(formats[i].getMultiHeadStr() == null ? formats[i].getColName() : formats[i].getMultiHeadStr());

        vLine1.add(formats[i].getColName());
        vColWidths.add(new Integer(formats[i].getColWidth()));
        if (formats[i].getAlignment() == 2)
          align.add(new Integer(0));
        else if (formats[i].getAlignment() == 0)
          align.add(new Integer(1));
        else if (formats[i].getAlignment() == 4) {
          align.add(new Integer(2));
        }
        if (formats[i].isFrozen() == true) {
          fixCol++;
        }
      }
    }
    String[] line1 = new String[vLine1.size()];
    String[] line0 = new String[vLine0.size()];
    int[] iAlignFlag = new int[line1.length];
    for (int i = 0; i < iAlignFlag.length; i++) {
      iAlignFlag[i] = ((Integer)align.elementAt(i)).intValue();
    }
    int[] colWidth = new int[vColWidths.size()];
    for (int i = 0; i < colWidth.length; i++) {
      colWidth[i] = ((Integer)vColWidths.elementAt(i)).intValue();
    }
    vLine1.copyInto(line1);
    vLine0.copyInto(line0);
    String[][] colNames = new String[2][];
    colNames[0] = line0;
    colNames[1] = line1;
    printEntry.setColNames(colNames);
    printEntry.setAlignFlag(iAlignFlag);

    String[][] sTop = new String[3][1];
    sTop[0][0] = getAssBalanceUI().getlbSubj().getText();

    sTop[1][0] = (NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000061") + "：" + getAssBalanceUI().getlbPeriod().getText());

    sTop[2][0] = (NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000171") + getAssBalanceUI().getlbCurrTypeForPrint().getText());

    printEntry.setTopStr(sTop);
    printEntry.setTopStrFixed(true);
    printEntry.setFixedRows(2);
    printEntry.setTopStrColRange(new int[] { line1.length - 1 });

    String[][] sBottom = new String[3][1];
    sBottom[0][0] = (NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000172") + ClientEnvironment.getInstance().getCorporation().getUnitname());

    sBottom[1][0] = (NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000173") + ClientEnvironment.getInstance().getUser().getUserName());

    sBottom[2][0] = (NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000174") + new Date().toLocaleString());

    printEntry.setBottomStr(sBottom);
    printEntry.setBottomStrAlign(new int[] { 0, 1 });
    printEntry.setBStrColRange(new int[] { line1.length - 1 });
    printEntry.setPageNumDisp(true);
    printEntry.setPageNumAlign(2);

    Vector vRange = new Vector();
    for (int i = 0; i < line0.length; i++) {
      if (line0[i].equals(line1[i])) {
        CellRange rangeTemp = new CellRange(0, i, 1, i);

        vRange.add(rangeTemp);
      }
    }
    int startCol = 0;
    int endCol = 0;
    boolean flag = false;
    while (startCol < line0.length) {
      flag = startCol != endCol;
      if ((endCol < line0.length) && (line0[startCol].equals(line0[endCol]))) {
        if (startCol < line0.length - 1) {
          endCol++; continue;
        }
        startCol++;
        continue; } if (flag) {
        if (endCol - startCol != 1) {
          CellRange rangeTemp = new CellRange(0, startCol, 0, endCol - 1);

          vRange.add(rangeTemp);
        }
        startCol = endCol;
      }
    }
    CellRange[] range = new CellRange[vRange.size()];

    vRange.copyInto(range);
    printEntry.setCombinCellRange(range);

    int iFixCount = 0;
    int iUNFixCount = 0;
    for (int i = 0; i < formats.length; i++) {
      if (formats[i].isVisiablity()) {
        if (i < getAssBalanceUI().getFixModel().getColumnCount())
          iFixCount++;
        else {
          iUNFixCount++;
        }
      }
    }
    Object[][] data = new Object[getAssBalanceUI().getFixModel().getRowCount()][line1.length];

    int[] fixLocation = new int[iFixCount];
    int[] unFixLocation = new int[iUNFixCount];
    int iFixIndex = 0;
    int iUnFixIndex = 0;
    for (int i = 0; i < formats.length; i++) {
      if (formats[i].isVisiablity()) {
        if (i < getAssBalanceUI().getFixModel().getColumnCount()) {
          fixLocation[iFixIndex] = i;
          iFixIndex++;
        } else {
          unFixLocation[iUnFixIndex] = i;
          iUnFixIndex++;
        }
      }
    }
    for (int i = 0; i < getAssBalanceUI().getFixModel().getRowCount(); i++) {
      int iFIndex = 0;
      for (int j = 0; j < iFixCount; j++) {
        data[i][j] = getAssBalanceUI().getFixModel().getValueAt(i, fixLocation[iFIndex]);

        iFIndex++;
      }
      int iUnFIndex = 0;
      for (int j = iFixCount; j < line1.length; j++) {
        data[i][j] = getAssBalanceUI().getM_TableModel().getValueAt(i, unFixLocation[iUnFIndex] - getAssBalanceUI().getFixModel().getColumnCount());

        iUnFIndex++;
      }
    }
    printEntry.setData(data);

    printEntry.setFixedCols(fixCol);
    Logger.info("-------------" + fixCol + "------------------");
    printEntry.setColWidth(colWidth);

    printEntry.preview();
  }

  private void printAsModule(boolean blnPrintAll)
    throws Exception
  {
    PrintEntry print = new PrintEntry(null);
    String pk_user = ClientEnvironment.getInstance().getUser().getPrimaryKey();

    String pk_loginGlorgbook = ((GlorgbookVO)ClientEnvironment.getInstance().getValue("pk_glorgbook")).getPrimaryKey();

    print.setTemplateID(pk_loginGlorgbook, "20023055", pk_user, null, null, "2");

    PrintTempletmanageHeaderVO[] templateHeaderVOs = print.getAllTemplates();

    String tempName = (String)getPrintDlg().getModuleCombo().getItemAt(getPrintDlg().getModuleCombo().getSelectedIndex());

    String m_templateID = null;

    for (int i = 0; i < templateHeaderVOs.length; i++) {
      if (templateHeaderVOs[i].getVtemplatename().equals(tempName)) {
        m_templateID = templateHeaderVOs[i].getCtemplateid();
        break;
      }
    }
    print.setTemplateID(m_templateID);
    if (blnPrintAll)
    {
      if (getGlQryCtner() == null) {
        TableDataModel data = new TableDataModel();
        data.setData(getAssBalanceUI().getM_TableModel().getData());

        this.dptool = new DynamicPrintTool(this.qryVO, data);
        BlncPrtDataSource source = new BlncPrtDataSource();
        source.setQryVO(this.qryVO);
        source.setIvjUI(getAssBalanceUI());
        source.setDptool(this.dptool);
        print.setDataSource(source);
        print.preview();
      }
      else
      {
        int iSize = getGlQryCtner().getSize();
        BalanceSubjAssBSVO[] allBvos = (BalanceSubjAssBSVO[])(BalanceSubjAssBSVO[])getGlQryCtner().getVos();

        for (int i = 0; i < iSize; i++) {
          BlncPrtDataSource source = new BlncPrtDataSource();

          BalanceSubjAssBSVO[] tempBvos = null;
          GlQueryVO tempQryVO = getGlQryCtner().getQueryAt(i);

          if (tempQryVO == null)
          {
            String assCode = (String)getGlQryCtner().getVKeys().elementAt(i);
            GlQueryVO copyStartQryvo = (GlQueryVO)this.qryVO.clone();
            GlQueryVOAssTool.suplyCurrentGlQueryVO(copyStartQryvo);
            tempQryVO = GlQueryVOAssTool.getGlQueryVOMultiPage(this, copyStartQryvo, assCode);
            tempBvos = AssBlcResultGetter.getBlnVOsByQryVOs(assCode, allBvos);
            tempBvos = getIvjMdlSttl().getSettleedDatas(tempBvos, tempQryVO);
            getGlQryCtner().setElementAt(tempQryVO, tempBvos, i);
          }
          else
          {
            tempBvos = (BalanceSubjAssBSVO[])(BalanceSubjAssBSVO[])getGlQryCtner().getBalaceVOsAt(this.iIndex);
          }

          TableDataModel currModel = new TableDataModel();
          currModel.setData(tempBvos);
          BalanceSubjAssUI blnSbjAssUI = new BalanceSubjAssUI();
          blnSbjAssUI.setTableData(currModel, tempQryVO);
          DynamicPrintTool dpTool = new DynamicPrintTool(tempQryVO, currModel);

          source.setQryVO(tempQryVO);
          source.setIvjUI(blnSbjAssUI);
          source.setDptool(dpTool);
          print.setDataSource(source);
        }

        print.print();
      }
    }
    else {
      TableDataModel data = new TableDataModel();
      data.setData(getAssBalanceUI().getM_TableModel().getData());

      this.dptool = new DynamicPrintTool(this.qryVO, data);
      BlncPrtDataSource source = new BlncPrtDataSource();
      source.setQryVO(this.qryVO);
      source.setIvjUI(getAssBalanceUI());
      source.setDptool(this.dptool);
      print.setDataSource(source);
      print.preview();
    }
  }

  void refresh(int iPos)
    throws Exception
  {
  }

  private void resetFormat()
  {
    BillFormatVO format = getAssBalanceUI().getFormat();
    if (this.qryVO != null) {
      if ((this.qryVO.getPk_glorgbook().length > 1) && (!this.qryVO.isMultiCorpCombine()))
      {
        format.setMultiOrg(true);
      }
      else format.setMultiOrg(false);
      if (this.qryVO.getCurrTypeName().equals(CurrTypeConst.LOC_CURRTYPE)) {
        format.setAuxCurrType(false);
        format.setLocCurrType(true);
        format.setMultiCurrType(false);
      } else if (this.qryVO.getCurrTypeName().equals(CurrTypeConst.AUX_CURRTYPE))
      {
        format.setLocCurrType(false);
        format.setAuxCurrType(true);
        format.setMultiCurrType(false);
      } else if (this.qryVO.getCurrTypeName().equals(CurrTypeConst.ALL_CURRTYPE))
      {
        format.setLocCurrType(false);
        format.setAuxCurrType(false);
        format.setMultiCurrType(true);
      } else {
        format.setLocCurrType(false);
        format.setAuxCurrType(false);
        format.setMultiCurrType(false);
      }

      format.setLocAuxCurrType(this.qryVO.isLocalFrac());
    }
    getAssBalanceUI().resetFormat(format);
  }

  public void setBillModel(IBillModel newBillModel)
  {
    this.billModel = newBillModel;
  }

  public void setIParent(IParent newIParent)
  {
    this.iParent = newIParent;
  }

  public void setQueryVos(GlQueryVO newQryVO)
  {
    this.iState = 1;
    try {
      if (newQryVO == null) {
        throw new BusinessException(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000175"));
      }

      this.qryVO = newQryVO;

      if (!GlQueryVOAssTool.isMultiPages(newQryVO)) {
        Logger.info("*************************************************");

        Logger.info("进入辅助余额表单页处理");
        Logger.info("*************************************************");

        queryOnePage(newQryVO);
        this.iIndex = -1;
      } else {
        Logger.info("*************************************************");

        Logger.info("进入辅助余额表单翻页处理，预分为  " + GlQueryVOAssTool.getPages(newQryVO) + " 页");

        Logger.info("*************************************************");

        query(newQryVO);
        firstPage();
      }
      setButtonState();
    } catch (Exception err) {
      err.printStackTrace();
    }
  }

  public void query(GlQueryVO newQryVO)
    throws Exception
  {
    AssBlnMultiPageRltGetter getter = new AssBlnMultiPageRltGetter();
    GlQueryVOContainer glQryCtner = getter.query(this, newQryVO);
    setGlQryCtner(glQryCtner);
  }

  public void queryOnePage(GlQueryVO newQryVO)
    throws Exception
  {
    GlQueryVO tempQryVO = (GlQueryVO)newQryVO.clone();
    GlQueryVOAssTool.suplyCurrentGlQueryVO(tempQryVO);
    GLQueryObj[] tempObjs = GlQueryVOAssTool.getSuplyedGLQueryObj(tempQryVO.getFormatVO().getQryObjs());

    BalanceSubjAssModel blnsbjAssMdl = new BalanceSubjAssModel();
    BalanceSubjAssBSVO[] bvos = null;
    try {
      bvos = (BalanceSubjAssBSVO[])(BalanceSubjAssBSVO[])blnsbjAssMdl.dealQuery(tempQryVO);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Boolean isLocalAux = GLParaDataCache.getInstance().IsLocalFrac(tempQryVO.getBaseGlOrgBook());

    String pk_AuxCurrType = null; String pk_LocCurrType = null;
    if (isLocalAux.booleanValue()) {
      pk_AuxCurrType = GLParaDataCache.getInstance().PkFracCurr(tempQryVO.getBaseGlOrgBook());

      pk_LocCurrType = GLParaDataCache.getInstance().PkLocalCurr(tempQryVO.getBaseGlOrgBook());
    }

    TableDataModel dataModel = new TableDataModel(pk_LocCurrType, pk_AuxCurrType, 43, 40);

    dataModel.setData(bvos);

    getAssBalanceUI().setTableData(dataModel, tempQryVO);
    resetFormat();
  }

  public void tackleBnsEvent(int eventID)
    throws Exception
  {
    switch (eventID) {
    case 0:
      getQueryDlg().setAssUi(getAssBalanceUI());
      getAssBalanceUI().setColWs(null);
      getQueryDlg().showModal();

      break;
    case 7:
      getAssBalanceUI().getFormat().setAssShowType(getAssBalanceUI().getFormat().getAssShowType() % 3 + 1);

      resetFormat();
      break;
    case 5:
      getPrintDlg().showme("20023055");

      break;
    case 4:
      setQueryVos(this.qryVO);
      break;
    case 10:
      nextPage();
      break;
    case 9:
      prevPage();
      break;
    case 11:
      lastPage();
      break;
    case 8:
      firstPage();
      break;
    case 2:
      getIParent();

      if (getIParent().getClass().getName().equals("nc.ui.gl.assbalance.UiManager"))
      {
        if (getIParent().getUiManager() != null) {
          ((ToftPanel)getIParent().getUiManager()).showHintMessage(NCLangRes.getInstance().getStrByID("20023055", "UPT20023055-000050"));
        }

      }

      goForDetail();
      break;
    case 12:
      this.iParent.closeMe();
    case 1:
    case 3:
    case 6:
    }
  }

  public void PrintAtCenter(GlQueryVO newQuerryVO, PrintCondVO printvo) throws Exception {
    this.qryVO = ((GlQueryVO)newQuerryVO.clone());

    PrintEntry print = new PrintEntry(null);
    print.setTemplateID(printvo.getPrintModule()[0].getCtemplateid());

    if (this.qryVO == null) {
      return;
    }
    setQueryVos(this.qryVO);

    if (getGlQryCtner() == null) {
      TableDataModel data = new TableDataModel();
      data.setData(getAssBalanceUI().getM_TableModel().getData());

      this.dptool = new DynamicPrintTool(this.qryVO, data);
      BlncPrtDataSource source = new BlncPrtDataSource();
      source.setQryVO(this.qryVO);
      source.setIvjUI(getAssBalanceUI());
      source.setDptool(this.dptool);
      print.setDataSource(source);
      print.print();
    }
    else
    {
      int iSize = getGlQryCtner().getSize();
      BalanceSubjAssBSVO[] allBvos = (BalanceSubjAssBSVO[])(BalanceSubjAssBSVO[])getGlQryCtner().getVos();

      for (int i = 0; i < iSize; i++) {
        BlncPrtDataSource source = new BlncPrtDataSource();

        BalanceSubjAssBSVO[] tempBvos = null;
        GlQueryVO tempQryVO = getGlQryCtner().getQueryAt(i);

        if (tempQryVO == null)
        {
          String assCode = (String)getGlQryCtner().getVKeys().elementAt(i);
          GlQueryVO copyStartQryvo = (GlQueryVO)this.qryVO.clone();
          GlQueryVOAssTool.suplyCurrentGlQueryVO(copyStartQryvo);
          tempQryVO = GlQueryVOAssTool.getGlQueryVOMultiPage(this, copyStartQryvo, assCode);
          tempBvos = AssBlcResultGetter.getBlnVOsByQryVOs(assCode, allBvos);
          tempBvos = getIvjMdlSttl().getSettleedDatas(tempBvos, tempQryVO);
          getGlQryCtner().setElementAt(tempQryVO, tempBvos, i);
        }
        else
        {
          tempBvos = (BalanceSubjAssBSVO[])(BalanceSubjAssBSVO[])getGlQryCtner().getBalaceVOsAt(this.iIndex);
        }

        TableDataModel currModel = new TableDataModel();
        currModel.setData(tempBvos);
        BalanceSubjAssUI blnSbjAssUI = new BalanceSubjAssUI();
        blnSbjAssUI.setTableData(currModel, tempQryVO);
        DynamicPrintTool dpTool = new DynamicPrintTool(tempQryVO, currModel);

        source.setQryVO(tempQryVO);
        source.setIvjUI(blnSbjAssUI);
        source.setDptool(dpTool);
        print.setDataSource(source);
      }

      print.print();
    }
  }

  public ToftPanelView getParentView()
  {
    return this.parentView;
  }

  private GlQueryVOContainer getGlQryCtner() {
    return this.glQryCtner;
  }

  private void setGlQryCtner(GlQueryVOContainer glQryCtner) {
    this.glQryCtner = glQryCtner;
  }

  private void setIvjModel(BalanceSubjAssModel ivjModel) {
    this.ivjModel = ivjModel;
  }

  private void setParentView(ToftPanelView parentView) {
    this.parentView = parentView;
  }
}