package nc.ui.ap.m200802;

import java.util.ArrayList;
import nc.bs.logging.Logger;
import nc.ui.arap.global.PubData;
import nc.ui.arap.pub.MyClientEnvironment;
import nc.ui.arap.pubdj.ArapDjPanel;
import nc.ui.ep.dj.ARAPDjDataBuffer;
import nc.ui.ep.dj.ARAPDjSettingParam;
import nc.ui.ep.dj.ARAPDjUIController;
import nc.ui.ep.dj.ArapBillWorkPageConst;
import nc.ui.ep.dj.ArapButtonStatUtil;
import nc.ui.ep.dj.DjPanel;
import nc.ui.ep.dj.controller.ARAPDjCtlAffiliated;
import nc.ui.ep.dj.controller.ARAPDjCtlDjEdit;
import nc.ui.ep.dj.controller.ARAPDjCtlDjEditQc;
import nc.ui.ep.dj.controller.ARAPDjCtlDocumentMng;
import nc.ui.ep.dj.controller.ARAPDjCtlFreeItem;
import nc.ui.ep.dj.controller.ARAPDjCtlPrint;
import nc.ui.ep.dj.controller.ARAPDjCtlQcClose;
import nc.ui.ep.dj.controller.ARAPDjCtlRelatedQuery;
import nc.ui.ep.dj.controller.ARAPDjCtlSFK;
import nc.ui.ep.dj.controller.ARAPDjCtlSearch;
import nc.ui.ep.dj.controller.ARAPDjCtlSearchNor;
import nc.ui.ep.dj.controller.ARAPDjCtlSuspend;
import nc.ui.ep.dj.controller.ARAPDjCtlTurnPage;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.vo.arap.pub.ArapConstant;
import nc.vo.bd.CorpVO;
import nc.vo.pub.lang.UFBoolean;

public class Ap200802 extends DjPanel
{
  private static final long serialVersionUID = -6878764704719955639L;
  private ButtonObject[] m_listBtnArry = null;

  private ButtonObject[] m_cardBtnArry = null;

  private ButtonObject[] m_zyxBtnArry = null;

  private UFBoolean isSettled = null;

  public Ap200802()
  {
    super(1);
    setM_Node("200802");
    setM_bQc(true);
    setM_Syscode(1);

    ARAPDjDataBuffer dataBuffer = super.getDjDataBuffer();
    dataBuffer.setCurrentDjdl("yf");
  }

  public void postInit()
  {
    if (-1 == getLinkedType()) {
      super.postInit();

      setDjlxbm(PubData.getDjlxbmByPkcorp(getClientEnvironment().getCorporation().getPrimaryKey(), 1));
    }
  }

  public String getNodeCode() {
    return "200802";
  }
  public String getTitle() {
    return NCLangRes.getInstance().getStrByID("common", "UC000-0002522");
  }

  public ARAPDjUIController[] getUIControllers()
  {
    if (this.m_arrayControllers == null) {
      this.m_arrayControllers = new ARAPDjUIController[11];

      ARAPDjCtlSearch search = new ARAPDjCtlSearchNor(this, getArapDjPanel1());

      ARAPDjCtlDjEdit edit = new ARAPDjCtlDjEditQc(this, getArapDjPanel1());
      ARAPDjCtlTurnPage turnPage = new ARAPDjCtlTurnPage(this, getArapDjPanel1());

      ARAPDjCtlSuspend suspend = new ARAPDjCtlSuspend(this, getArapDjPanel1());

      ARAPDjCtlAffiliated affiliated = new ARAPDjCtlAffiliated(this, getArapDjPanel1());

      ARAPDjCtlRelatedQuery relatedQuery = new ARAPDjCtlRelatedQuery(this, getArapDjPanel1());

      ARAPDjCtlPrint print = new ARAPDjCtlPrint(this, getArapDjPanel1());
      ARAPDjCtlSFK sfk = new ARAPDjCtlSFK(this, getArapDjPanel1());
      ARAPDjCtlFreeItem freeItem = new ARAPDjCtlFreeItem(this, getArapDjPanel1());

      ARAPDjCtlDocumentMng doc = new ARAPDjCtlDocumentMng(this, getArapDjPanel1());

      ARAPDjCtlQcClose qc = new ARAPDjCtlQcClose(this, getArapDjPanel1());
      this.m_arrayControllers[0] = search;
      this.m_arrayControllers[1] = edit;
      this.m_arrayControllers[2] = turnPage;
      this.m_arrayControllers[3] = suspend;
      this.m_arrayControllers[4] = affiliated;
      this.m_arrayControllers[5] = relatedQuery;
      this.m_arrayControllers[6] = print;
      this.m_arrayControllers[7] = sfk;
      this.m_arrayControllers[8] = freeItem;
      this.m_arrayControllers[9] = doc;
      this.m_arrayControllers[10] = qc;
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
    if (getCurrWorkPage() == ArapBillWorkPageConst.ZYXPAGE) {
      return getZyxBtnArry();
    }
    return null;
  }

  protected void afterSetButtonsState(int DjState)
  {
    this.m_boParticularQuery.setVisible(true);
  }

  protected void initButtonGroup()
  {
    this.m_boPausetransactP.addChildButton(this.m_boPausetransact);
    this.m_boPausetransactP.addChildButton(this.m_boUnPausetransact);

    this.m_boPrint_OfficialP.addChildButton(this.m_boPrint);
    this.m_boPrint_OfficialP.addChildButton(this.m_boPrint_All);
    this.m_boPrint_OfficialP.addChildButton(this.m_boPrint_Official);
    this.m_boPrint_OfficialP.addChildButton(this.m_boPrint_Official_Cancel);

    this.m_boDjOperation.addChildButton(this.m_boAdd);
    this.m_boDjOperation.addChildButton(this.m_boCopy);
    this.m_boDjOperation.addChildButton(this.m_boDelDj);
    this.m_boDjOperation.addChildButton(this.m_boEdit);
    this.m_boDjOperation.addChildButton(this.m_boYsf);
    this.m_boDjOperation.addChildButton(this.m_boAddRow);
    this.m_boDjOperation.addChildButton(this.m_boDelRow);

    this.m_boLinkQuery.addChildButton(this.m_boParticularQuery);
    this.m_boLinkQuery.addChildButton(this.m_boSFK);
    this.m_boLinkQuery.addChildButton(this.m_boQueryList);

    this.m_boQcCloseOrNo.addChildButton(this.m_boQcClose);
    this.m_boQcCloseOrNo.addChildButton(this.m_boQcCancelClose);
    this.m_boChangePage.addChildButton(this.m_boFirstPage);
    this.m_boChangePage.addChildButton(this.m_boUpPage);
    this.m_boChangePage.addChildButton(this.m_boDownPage);
    this.m_boChangePage.addChildButton(this.m_boLastPage);
    this.m_boShenheOrNo.addChildButton(this.m_boShenhe);
    this.m_boShenheOrNo.addChildButton(this.m_boUnShenhe);
  }

  private ButtonObject[] getListBtnArry()
  {
    if (this.m_listBtnArry == null) {
      this.m_listBtnArry = new ButtonObject[] { this.m_boQuery, this.m_boDjOperation, this.m_boNext, this.m_boPreviousPage, this.m_boNextPage, this.m_boRefreshPage, this.m_boSelectAll, this.m_boUnSelectAll, this.m_boQcCloseOrNo, this.m_boPausetransactP, this.m_boPrint_OfficialP, this.m_boLinkQuery, this.m_btDocMng };
    }
    else
    {
      if (this.m_boDjOperation.getChildButtonGroup() != null) {
        for (int i = 0; i < this.m_boDjOperation.getChildButtonGroup().length; i++) {
          if ((this.m_boDjOperation.getChildButtonGroup()[i] == this.m_boDelDj) || (this.m_boDjOperation.getChildButtonGroup()[i] == this.m_boAdd))
            continue;
          this.m_boDjOperation.getChildButtonGroup()[i].setVisible(false);
        }

      }

      this.m_boPrint.setVisible(true);
      this.m_boXm.setVisible(false);
    }
    return this.m_listBtnArry;
  }

  private ButtonObject[] getCardBtnArry()
  {
    if (this.m_cardBtnArry == null) {
      this.m_cardBtnArry = new ButtonObject[] { this.m_boDjOperation, this.m_boNext, this.m_boDjlx, this.m_boSave, this.m_boCancel, this.m_boChangePage, this.m_boPausetransactP, this.m_boPrint_OfficialP, this.m_boLinkQuery, this.m_btDocMng };
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
      if (!isSysSettled()) {
        alBtn.add(this.m_boQcCloseOrNo);
        ArapButtonStatUtil.setQcCloserStat(alBtn, this);
      }
      alBtn.add(this.m_boDjOperation);
      alBtn.add(this.m_boAdd);
      alBtn.add(this.m_boQuery);
      alBtn.add(this.m_boNext);
      if (getCur_Djcondvo() != null) {
        this.m_boRefreshPage.setEnabled(true);

        this.m_boNextPage.setEnabled(true);
        this.m_boPreviousPage.setEnabled(true);
      }
      if (!getDjDataBuffer().isEmpty()) {
        if ((!isSysSettled()) && (!ArapButtonStatUtil.getQcCloserStat(this))) {
          alBtn.add(this.m_boDjOperation);
          alBtn.add(this.m_boDelDj);
        }

        alBtn.add(this.m_boSelectAll);
        alBtn.add(this.m_boUnSelectAll);

        ArapButtonStatUtil.setPrintBtnsStat(alBtn, this, false);

        ArapButtonStatUtil.setRelatedQueryBtnsStat(alBtn, this, false);
        alBtn.add(this.m_btDocMng);
      }
    } else if (getCurrWorkPage() == ArapBillWorkPageConst.CARDPAGE) {
      if ((!isSysSettled()) && (!ArapButtonStatUtil.getQcCloserStat(this))) {
        alBtn.add(this.m_boDjOperation);
      }
      if ((getArapDjPanel1().getM_DjState() != ArapBillWorkPageConst.WORKSTAT_EDIT) && (getArapDjPanel1().getM_DjState() != ArapBillWorkPageConst.WORKSTAT_NEW))
      {
        alBtn.add(this.m_boAdd);
        alBtn.add(this.m_boDjlx);
      }
      if (getDjDataBuffer().getCurrentDJZBVO() != null) {
        if (getArapDjPanel1().getM_DjState() != ArapBillWorkPageConst.WORKSTAT_NEW) {
          alBtn.add(this.m_btDocMng);
        }
        if ((getArapDjPanel1().getM_DjState() == ArapBillWorkPageConst.WORKSTAT_EDIT) || (getArapDjPanel1().getM_DjState() == ArapBillWorkPageConst.WORKSTAT_NEW))
        {
          ArapButtonStatUtil.setEdittingDefaultBtnstat(alBtn, this);
          ArapButtonStatUtil.setBugetQueryBtnStat(alBtn, this);
        } else {
          alBtn.add(this.m_boNext);

          ArapButtonStatUtil.setPrintBtnsStat(alBtn, this, true);

          ArapButtonStatUtil.setRelatedQueryBtnsStat(alBtn, this, true);

          ArapButtonStatUtil.setTurnPageBtnstat(alBtn, this);

          ArapButtonStatUtil.setPauseBtnstat(alBtn, this);

          ArapButtonStatUtil.setVitBtnstat(alBtn, this);
          if ((!isSysSettled()) && (!ArapButtonStatUtil.getQcCloserStat(this))) {
            alBtn.add(this.m_boEdit);
            alBtn.add(this.m_boDelDj);
            alBtn.add(this.m_boCopy);
          }
          ArapButtonStatUtil.setprePayBtnstat(alBtn, this, true);
        }
      }
      else
      {
        alBtn.add(this.m_boNext);
      }
    } else if (getCurrWorkPage() == ArapBillWorkPageConst.ZYXPAGE)
    {
      ArapButtonStatUtil.setZyxBtnstat(alBtn, this, 1);
    } else {
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

  private boolean isSysSettled()
  {
    if (this.isSettled == null) {
      try
      {
        String[] period = null;
        period = MyClientEnvironment.getPeriod(getDjSettingParam().getPk_corp(), getDjSettingParam().getSyscode());

        if ((period[0] == null) || (period[0].trim().length() < 1))
          this.isSettled = ArapConstant.UFBOOLEAN_FALSE;
        else
          this.isSettled = ArapConstant.UFBOOLEAN_TRUE;
      }
      catch (Exception e)
      {
        Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000761") + e);
      }

    }

    return this.isSettled.booleanValue();
  }
}