package nc.ui.ap.m20080301;

import java.util.ArrayList;
import nc.ui.arap.global.PubData;
import nc.ui.arap.pub.MyClientEnvironment;
import nc.ui.arap.pubdj.ArapDjPanel;
import nc.ui.ep.dj.ARAPDjDataBuffer;
import nc.ui.ep.dj.ARAPDjSettingParam;
import nc.ui.ep.dj.ARAPDjUIController;
import nc.ui.ep.dj.ArapBillWorkPageConst;
import nc.ui.ep.dj.ArapButtonStatUtil;
import nc.ui.ep.dj.DjPanel;
import nc.ui.ep.dj.controller.ARAPDJCtlDjred;
import nc.ui.ep.dj.controller.ARAPDjCtlAffiliated;
import nc.ui.ep.dj.controller.ARAPDjCtlCounteract;
import nc.ui.ep.dj.controller.ARAPDjCtlDjEdit;
import nc.ui.ep.dj.controller.ARAPDjCtlDjEditNor;
import nc.ui.ep.dj.controller.ARAPDjCtlDocumentMng;
import nc.ui.ep.dj.controller.ARAPDjCtlFTSPayment;
import nc.ui.ep.dj.controller.ARAPDjCtlFreeItem;
import nc.ui.ep.dj.controller.ARAPDjCtlPjSearch;
import nc.ui.ep.dj.controller.ARAPDjCtlPrint;
import nc.ui.ep.dj.controller.ARAPDjCtlRelatedQuery;
import nc.ui.ep.dj.controller.ARAPDjCtlSFK;
import nc.ui.ep.dj.controller.ARAPDjCtlSearch;
import nc.ui.ep.dj.controller.ARAPDjCtlSearchNor;
import nc.ui.ep.dj.controller.ARAPDjCtlSuspend;
import nc.ui.ep.dj.controller.ARAPDjCtlTurnPage;
import nc.ui.ep.dj.controller.ARAPDjCtlVerify;
import nc.ui.ep.dj.controller.ARAPDjCtlWszz;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.vo.bd.CorpVO;
import nc.vo.ep.dj.DJZBVO;

public class Ap20080301 extends DjPanel
{
  private static final long serialVersionUID = 2214111888482971493L;
  private ButtonObject[] m_listBtnArry = null;

  private ButtonObject[] m_cardBtnArry = null;

  private ButtonObject[] m_zyxBtnArry = null;

  private ButtonObject[] m_vitBtnArry = null;

  private ButtonObject[] m_makeupBtnArry = null;

  public Ap20080301()
  {
    super(0);

    getDjSettingParam().setIsQc(false);

    setM_Syscode(1);

    ARAPDjDataBuffer dataBuffer = super.getDjDataBuffer();
    dataBuffer.setCurrentDjdl("yf");
    setM_Node("2008030102");
  }

  public void postInit()
  {
    if (-1 == getLinkedType()) {
      super.postInit();

      setDjlxbm(PubData.getDjlxbmByPkcorp(getClientEnvironment().getCorporation().getPrimaryKey(), 1));
    }
  }

  public String getNodeCode() {
    return getDjSettingParam().getNodeID();
  }
  public String getTitle() {
    return NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000413");
  }

  public ARAPDjUIController[] getUIControllers()
  {
    if (this.m_arrayControllers == null) {
      this.m_arrayControllers = new ARAPDjUIController[16];
      ARAPDjCtlSearch search = new ARAPDjCtlSearchNor(this, getArapDjPanel1());
      ARAPDjCtlDjEdit edit = new ARAPDjCtlDjEditNor(this, getArapDjPanel1());

      ARAPDjCtlWszz wszz = new ARAPDjCtlWszz(this, getArapDjPanel1());
      ARAPDjCtlVerify verify = new ARAPDjCtlVerify(this, getArapDjPanel1());
      ARAPDjCtlCounteract counteract = new ARAPDjCtlCounteract(this, getArapDjPanel1());
      ARAPDjCtlTurnPage turnPage = new ARAPDjCtlTurnPage(this, getArapDjPanel1());
      ARAPDjCtlSuspend suspend = new ARAPDjCtlSuspend(this, getArapDjPanel1());
      ARAPDjCtlFreeItem freeItem = new ARAPDjCtlFreeItem(this, getArapDjPanel1());
      ARAPDjCtlAffiliated affiliated = new ARAPDjCtlAffiliated(this, getArapDjPanel1());
      ARAPDjCtlRelatedQuery relatedQuery = new ARAPDjCtlRelatedQuery(this, getArapDjPanel1());
      ARAPDjCtlPrint print = new ARAPDjCtlPrint(this, getArapDjPanel1());
      ARAPDjCtlPjSearch pj = new ARAPDjCtlPjSearch(this, getArapDjPanel1());
      ARAPDjCtlSFK sfk = new ARAPDjCtlSFK(this, getArapDjPanel1());
      ARAPDjCtlFTSPayment ftsPayment = new ARAPDjCtlFTSPayment(this, getArapDjPanel1());
      ARAPDjCtlDocumentMng doc = new ARAPDjCtlDocumentMng(this, getArapDjPanel1());
      ARAPDJCtlDjred red = new ARAPDJCtlDjred(this, getArapDjPanel1());

      this.m_arrayControllers[0] = search;
      this.m_arrayControllers[1] = edit;
      this.m_arrayControllers[2] = wszz;
      this.m_arrayControllers[3] = verify;
      this.m_arrayControllers[4] = counteract;
      this.m_arrayControllers[5] = turnPage;
      this.m_arrayControllers[6] = suspend;
      this.m_arrayControllers[7] = freeItem;
      this.m_arrayControllers[8] = affiliated;
      this.m_arrayControllers[9] = relatedQuery;
      this.m_arrayControllers[10] = print;
      this.m_arrayControllers[11] = pj;
      this.m_arrayControllers[12] = sfk;
      this.m_arrayControllers[13] = ftsPayment;
      this.m_arrayControllers[14] = doc;
      this.m_arrayControllers[15] = red;
    }
    return this.m_arrayControllers;
  }

  protected ButtonObject[] getDjButtons()
  {
    if (!isInitied()) {
      initButtonGroup();
      setInitied(true);
    }
    if (getCurrWorkPage() == ArapBillWorkPageConst.LISTPAGE)
      return getListBtnArry();
    if (getCurrWorkPage() == ArapBillWorkPageConst.CARDPAGE)
      return getCardBtnArry();
    if (getCurrWorkPage() == ArapBillWorkPageConst.ZYXPAGE)
      return getZyxBtnArry();
    if (getCurrWorkPage() == ArapBillWorkPageConst.VITPAGE)
      return getVitBtnArry();
    if (getCurrWorkPage() == ArapBillWorkPageConst.MAKEUPPAGE) {
      return getMakeupBtnArry();
    }
    return null;
  }

  protected void afterSetButtonsState(int DjState)
  {
    this.m_boParticularQuery.setVisible(true);
    if (getM_TabIndex() == 1)
    {
      this.m_boArapTofts.setVisible(false);
    }
    else this.m_boArapTofts.setVisible(true);
  }

  protected void initButtonGroup()
  {
    this.m_boPausetransactP.addChildButton(this.m_boPausetransact);
    this.m_boPausetransactP.addChildButton(this.m_boUnPausetransact);

    this.m_boPrint_OfficialP.addChildButton(this.m_boPrint);
    this.m_boPrint_OfficialP.addChildButton(this.m_boPrint_All);
    this.m_boPrint_OfficialP.addChildButton(this.m_boPrint_Official);
    this.m_boPrint_OfficialP.addChildButton(this.m_boPrint_Official_Cancel);
    this.m_boPrint_OfficialP.addChildButton(this.m_boPrintZYX);

    this.m_boDjOperation.addChildButton(this.m_boAdd);
    this.m_boDjOperation.addChildButton(this.m_boCopy);
    this.m_boDjOperation.addChildButton(this.m_boDelDj);
    this.m_boDjOperation.addChildButton(this.m_boEdit);
    this.m_boDjOperation.addChildButton(this.m_boYsf);
    this.m_boDjOperation.addChildButton(this.m_boAddRow);
    this.m_boDjOperation.addChildButton(this.m_boDelRow);

    this.m_boVerify.addChildButton(this.m_boVerify_Search);
    this.m_boVerify.addChildButton(this.m_boVerify_Search_Total);

    this.m_boAssistant.addChildButton(this.m_boRent);

    this.m_boAssistant.addChildButton(this.m_boXm);
    this.m_boAssistant.addChildButton(this.m_boCreatFABill);

    this.m_boLinkQuery.addChildButton(this.m_boBudgetImpl);
    this.m_boLinkQuery.addChildButton(this.m_boApprove);

    this.m_boLinkQuery.addChildButton(this.m_boParticularQuery);

    this.m_boLinkQuery.addChildButton(this.m_boProvide);

    this.m_boLinkQuery.addChildButton(this.m_boPf);
    this.m_boLinkQuery.addChildButton(this.m_boPJ);
    this.m_boLinkQuery.addChildButton(this.m_boXtQuery);
    this.m_boLinkQuery.addChildButton(this.m_boQueryFTSPayment);
    this.m_boLinkQuery.addChildButton(this.m_boDJQrySS);

    if (MyClientEnvironment.isPjUsed())
      this.m_boPJ.setEnabled(true);
    else {
      this.m_boPJ.setEnabled(false);
    }

    this.m_boLinkQuery.addChildButton(this.m_boSFK);
    this.m_boLinkQuery.addChildButton(this.m_boQueryAccount);
    this.m_boLinkQuery.addChildButton(this.m_boQueryList);

    this.m_boChangePage.addChildButton(this.m_boFirstPage);
    this.m_boChangePage.addChildButton(this.m_boUpPage);
    this.m_boChangePage.addChildButton(this.m_boDownPage);
    this.m_boChangePage.addChildButton(this.m_boLastPage);
    this.m_boShenheOrNo.addChildButton(this.m_boShenhe);
    this.m_boShenheOrNo.addChildButton(this.m_boUnShenhe);
  }

  private ButtonObject[] getCardBtnArry()
  {
    if (this.m_cardBtnArry == null) {
      if (this.PanelProp == 1) {
        this.m_cardBtnArry = new ButtonObject[] { this.m_boEdit, this.m_boSave, this.m_boCancel, this.m_boShenheOrNo, this.m_boPrint_OfficialP, this.m_btDocMng, this.m_boLinkQuery };
      }
      else if (this.PanelProp == 2) {
        this.m_cardBtnArry = new ButtonObject[] { this.m_boNext, this.m_boPrint_OfficialP, this.m_boLinkQuery };
      }
      else if (this.PanelProp == 3) {
        this.m_cardBtnArry = new ButtonObject[] { this.m_boEdit, this.m_boSave, this.m_boCancel };
      }
      else
      {
        this.m_cardBtnArry = new ButtonObject[] { this.m_boDjOperation, this.m_boNext, this.m_boWszz, this.m_boCombinedPayment, this.m_boDjlx, this.m_boShenheOrNo, this.m_boTempSave, this.m_boSave, this.m_boCancel, this.m_boVerify, this.m_boChangePage, this.m_red, this.m_boAssistant, this.m_boPausetransactP, this.m_boPrint_OfficialP, this.m_boLinkQuery, this.m_boArapTofts, this.m_btDocMng,this.m_boCBC };
      }

    }

    if (this.m_boDjOperation.getChildButtonGroup() != null) {
      for (int i = 0; i < this.m_boDjOperation.getChildButtonGroup().length; i++) {
        this.m_boDjOperation.getChildButtonGroup()[i].setVisible(true);
      }
    }
    this.m_boPrint.setVisible(false);
    this.m_boXm.setVisible(true);
    return this.m_cardBtnArry;
  }

  private ButtonObject[] getListBtnArry()
  {
    if (this.m_listBtnArry == null) {
      if (this.PanelProp == 1) {
        this.m_listBtnArry = new ButtonObject[] { this.m_boShenheOrNo, this.m_btDocMng, this.m_boLinkQuery };
      }
      else if (this.PanelProp == 2) {
        this.m_listBtnArry = new ButtonObject[] { this.m_boNext, this.m_boPrint_OfficialP, this.m_boLinkQuery };
      }
      else
      {
        this.m_listBtnArry = new ButtonObject[] { this.m_boQuery, this.m_boDjOperation, this.m_boNext, this.m_boWszz, this.m_boCombinedPayment, this.m_boShenheOrNo, this.m_boPreviousPage, this.m_boNextPage, this.m_boRefreshPage, this.m_boSelectAll, this.m_boUnSelectAll, this.m_boAssistant, this.m_boPrint_OfficialP, this.m_boLinkQuery, this.m_btDocMng,this.m_boCBC};
      }

    }
    else
    {
      if (this.m_boDjOperation.getChildButtonGroup() != null) {
        for (int i = 0; i < this.m_boDjOperation.getChildButtonGroup().length; i++) {
          if ((this.m_boDjOperation.getChildButtonGroup()[i] != this.m_boDelDj) && (this.m_boDjOperation.getChildButtonGroup()[i] != this.m_boAdd)) {
            this.m_boDjOperation.getChildButtonGroup()[i].setVisible(false);
          }
        }
      }

      this.m_boPrint.setVisible(true);
      this.m_boXm.setVisible(false);
    }
    return this.m_listBtnArry;
  }

  private ButtonObject[] getMakeupBtnArry()
  {
    if (this.m_makeupBtnArry == null) {
      this.m_makeupBtnArry = new ButtonObject[] { this.m_boVerify_ConfirmBc, this.m_boVerify_CancelBc };
    }

    return this.m_makeupBtnArry;
  }

  private ButtonObject[] getVitBtnArry()
  {
    if (this.m_vitBtnArry == null) {
      this.m_vitBtnArry = new ButtonObject[] { this.m_boVerify_Bc, this.m_boVerify_Fp, this.m_boVerify_Hx, this.m_boVerify_Filter, this.m_boVerify_All, this.m_boVerify_N, this.m_boVerify_GoBack, this.m_boVerify_ShowDj };
    }

    return this.m_vitBtnArry;
  }

  private ButtonObject[] getZyxBtnArry()
  {
    if (this.m_zyxBtnArry == null) {
      this.m_zyxBtnArry = new ButtonObject[] { this.m_boAddZyx, this.m_boDelZyx, this.m_boEditZyx, this.m_boAddRowZyx, this.m_boDelRowZyx, this.m_boSaveZyx, this.m_boCancelZyx, this.m_boPrintZYX };
    }

    return this.m_zyxBtnArry;
  }

  public ButtonObject[] getEnableButtonArry()
  {
    ArrayList alBtn = new ArrayList();
    if (getCurrWorkPage() == ArapBillWorkPageConst.LISTPAGE) {
      alBtn.add(this.m_boDjOperation);
      alBtn.add(this.m_boAdd);
      alBtn.add(this.m_boQuery);
      alBtn.add(this.m_boNext);
      alBtn.add(this.m_boCBC);//朱思晗增加按钮
      if (getCur_Djcondvo() != null) {
        this.m_boRefreshPage.setEnabled(true);
        this.m_boCBC.setEnabled(true);
        this.m_boNextPage.setEnabled(true);
        this.m_boPreviousPage.setEnabled(true);
      }
      else
      {
        this.m_boNextPage.setEnabled(false);
        this.m_boPreviousPage.setEnabled(false);
      }
      if (!getDjDataBuffer().isEmpty()) {
        alBtn.add(this.m_boDjOperation);

        ArapButtonStatUtil.setDelEditBtnstat(alBtn, this, false);
        alBtn.add(this.m_boWszz);
        alBtn.add(this.m_boCombinedPayment);

        ArapButtonStatUtil.setAduitBtnstat(alBtn, this, false);
        alBtn.add(this.m_boSelectAll);
        alBtn.add(this.m_boUnSelectAll);

        ArapButtonStatUtil.setPrintBtnsStat(alBtn, this, false);

        ArapButtonStatUtil.setRelatedQueryBtnsStat(alBtn, this, false);
        alBtn.add(this.m_btDocMng);
      }
    } else if (getCurrWorkPage() == ArapBillWorkPageConst.CARDPAGE)
    { 
      alBtn.add(this.m_boCBC);
      alBtn.add(this.m_boDjOperation);
      if ((getArapDjPanel1().getM_DjState() != ArapBillWorkPageConst.WORKSTAT_EDIT) && (getArapDjPanel1().getM_DjState() != ArapBillWorkPageConst.WORKSTAT_NEW))
      {
    	  
        alBtn.add(this.m_boAdd);
        alBtn.add(this.m_boDjlx);
      }
      if (getDjDataBuffer().getCurrentDJZBVO() != null) {
        DJZBVO djvo = getDjDataBuffer().getCurrentDJZBVO();
        if (getArapDjPanel1().getM_DjState() != ArapBillWorkPageConst.WORKSTAT_NEW) {
          alBtn.add(this.m_btDocMng);
        }
        if ((getArapDjPanel1().getM_DjState() == ArapBillWorkPageConst.WORKSTAT_EDIT) || (getArapDjPanel1().getM_DjState() == ArapBillWorkPageConst.WORKSTAT_NEW))
        {
          ArapButtonStatUtil.setEdittingDefaultBtnstat(alBtn, this);
          ArapButtonStatUtil.setBugetQueryBtnStat(alBtn, this);
        } else {
          alBtn.add(this.m_boNext);
          alBtn.add(this.m_boCopy);

          ArapButtonStatUtil.setPrintBtnsStat(alBtn, this, true);

          ArapButtonStatUtil.setRelatedQueryBtnsStat(alBtn, this, true);

          ArapButtonStatUtil.setprePayBtnstat(alBtn, this, true);

          ArapButtonStatUtil.setTurnPageBtnstat(alBtn, this);

          ArapButtonStatUtil.setFtsPayBtnstat(alBtn, djvo, this.m_boArapTofts, getDjSettingParam(), getDjDataBuffer());

          ArapButtonStatUtil.setPauseBtnstat(alBtn, this);

          ArapButtonStatUtil.setOnlinePayBtnstat(alBtn, djvo, this.m_boWszz, getDjDataBuffer());
          ArapButtonStatUtil.setOnlinePayBtnstat(alBtn, djvo, this.m_boCombinedPayment, getDjDataBuffer());

          ArapButtonStatUtil.setVitBtnstat(alBtn, this);

          ArapButtonStatUtil.setDelEditBtnstat(alBtn, this, true);

          ArapButtonStatUtil.setAduitBtnstat(alBtn, this, true);
        }
      } else {
        alBtn.add(this.m_boNext);
      }
    } else if (getCurrWorkPage() == ArapBillWorkPageConst.ZYXPAGE)
    {
      ArapButtonStatUtil.setZyxBtnstat(alBtn, this, 1); } else {
      if (getCurrWorkPage() == ArapBillWorkPageConst.VITPAGE)
        return getDjButtons();
      if (getCurrWorkPage() == ArapBillWorkPageConst.MAKEUPPAGE) {
        return getDjButtons();
      }
      return getDjButtons();
    }
    if (alBtn.size() > 0) {
      ButtonObject[] btns = new ButtonObject[alBtn.size()];
      btns = (ButtonObject[])(ButtonObject[])alBtn.toArray(btns);
      return btns;
    }
    return new ButtonObject[0];
  }
}