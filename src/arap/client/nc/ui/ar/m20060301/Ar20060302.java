package nc.ui.ar.m20060301;

import java.util.ArrayList;

import nc.ui.arap.global.PubData;
import nc.ui.ep.dj.ARAPDjDataBuffer;
import nc.ui.ep.dj.ARAPDjUIController;
import nc.ui.ep.dj.ArapBillWorkPageConst;
import nc.ui.ep.dj.ArapButtonStatUtil;
import nc.ui.ep.dj.DjPanel;
import nc.ui.ep.dj.controller.ARAPDjCtlDjComfirm;
import nc.ui.ep.dj.controller.ARAPDjCtlDjEdit;
import nc.ui.ep.dj.controller.ARAPDjCtlDjEditNor;
import nc.ui.ep.dj.controller.ARAPDjCtlPjSearch;
import nc.ui.ep.dj.controller.ARAPDjCtlPrint;
import nc.ui.ep.dj.controller.ARAPDjCtlSFK;
import nc.ui.ep.dj.controller.ARAPDjCtlSearch;
import nc.ui.ep.dj.controller.ARAPDjCtlSearchQr;
import nc.ui.ep.dj.controller.ARAPDjCtlSuspend;
import nc.ui.ep.dj.controller.ARAPDjCtlTurnPage;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;

public class Ar20060302 extends DjPanel {
	private static final long serialVersionUID = -5934714514456063178L;
	private ButtonObject[] m_listBtnArry = null;

	private ButtonObject[] m_cardBtnArry = null;

	public Ar20060302() {
		super(0);
		setM_Node("2006030103");
		super.postInit();

		setM_Syscode(-9);

		ARAPDjDataBuffer dataBuffer = super.getDjDataBuffer();
		dataBuffer.setCurrentDjdl("ys");

		setDjlxbm(PubData.getDjlxbmByPkcorp(getClientEnvironment()
				.getCorporation().getPrimaryKey(), -9));
	}

	@Override
	/*
	 * * add 彭佳佳 2018年12月5日 限制用户删除应收,收款单
	 */
	public void onButtonClicked(ButtonObject bo) {
		if (bo.getName().equals("删除")) {
			String pk_corp = ClientEnvironment.getInstance().getCorporation()
					.getPk_corp();
			if (pk_corp.equals("1016") || pk_corp.equals("1071")
					|| pk_corp.equals("1103") || pk_corp.equals("1097")
					|| pk_corp.equals("1017") || pk_corp.equals("1018") || pk_corp.equals("1019")) {
				showErrorMessage("不允许删除单据,请用对冲单据解决!");
				return;
			} else {
				super.onButtonClicked(bo);
			}
		}
		super.onButtonClicked(bo);
	}

	public String getNodeCode() {
		return "2006030103";
	}

	public String getTitle() {
		return NCLangRes.getInstance().getStrByID("2006030102",
				"UPP2006030102-000942");
	}

	public ARAPDjUIController[] getUIControllers() {
		if (this.m_arrayControllers == null) {
			this.m_arrayControllers = new ARAPDjUIController[8];

			ARAPDjCtlSearch search = new ARAPDjCtlSearchQr(this,
					getArapDjPanel1());

			ARAPDjCtlDjEdit edit = new ARAPDjCtlDjEditNor(this,
					getArapDjPanel1());
			ARAPDjCtlDjComfirm djComfirm = new ARAPDjCtlDjComfirm(this,
					getArapDjPanel1());

			ARAPDjCtlTurnPage turnPage = new ARAPDjCtlTurnPage(this,
					getArapDjPanel1());

			ARAPDjCtlSuspend suspend = new ARAPDjCtlSuspend(this,
					getArapDjPanel1());

			ARAPDjCtlPrint print = new ARAPDjCtlPrint(this, getArapDjPanel1());
			ARAPDjCtlSFK sfk = new ARAPDjCtlSFK(this, getArapDjPanel1());
			ARAPDjCtlPjSearch pj = new ARAPDjCtlPjSearch(this,
					getArapDjPanel1());
			this.m_arrayControllers[0] = search;
			this.m_arrayControllers[1] = edit;
			this.m_arrayControllers[2] = djComfirm;
			this.m_arrayControllers[3] = turnPage;
			this.m_arrayControllers[4] = suspend;
			this.m_arrayControllers[5] = print;
			this.m_arrayControllers[6] = sfk;
			this.m_arrayControllers[7] = pj;
		}

		return this.m_arrayControllers;
	}

	private ButtonObject[] getCardBtnArry() {
		if (this.m_cardBtnArry == null) {
			this.m_cardBtnArry = new ButtonObject[] { this.m_boNext,
					this.m_boQR, this.m_boUnQR, this.m_boSave, this.m_boCancel,
					this.m_boAddRow, this.m_boDelRow };
		}

		return this.m_cardBtnArry;
	}

	private ButtonObject[] getListBtnArry() {
		if (this.m_listBtnArry == null) {
			this.m_listBtnArry = new ButtonObject[] { this.m_boQuery,
					this.m_boNext, this.m_boDelDj, this.m_boPreviousPage,
					this.m_boNextPage, this.m_boRefreshPage,
					this.m_boSelectAll, this.m_boUnSelectAll };
		}

		return this.m_listBtnArry;
	}

	protected ButtonObject[] getDjButtons() {
		if (!(isInitied())) {
			initButtonGroup();
			setInitied(true);
		}
		if (getCurrWorkPage() == ArapBillWorkPageConst.LISTPAGE) {
			return getListBtnArry();
		}

		return getCardBtnArry();
	}

	public ButtonObject[] getEnableButtonArry() {
		ArrayList alBtn = new ArrayList();
		if (getCurrWorkPage() == ArapBillWorkPageConst.LISTPAGE) {
			alBtn.add(this.m_boQuery);
			
			alBtn.add(this.m_boNext);
			if (getCur_Djcondvo() != null) {
				this.m_boRefreshPage.setEnabled(true);

				this.m_boNextPage.setEnabled(true);
				this.m_boPreviousPage.setEnabled(true);
			}
			if (!(getDjDataBuffer().isEmpty())) {
				ArapButtonStatUtil.setDelEditBtnstat(alBtn, this, false);
				alBtn.add(this.m_boSelectAll);
				alBtn.add(this.m_boUnSelectAll);

			}
		} else if (getCurrWorkPage() == ArapBillWorkPageConst.CARDPAGE) {
			if (getArapDjPanel1().getM_DjState() == ArapBillWorkPageConst.WORKSTAT_EDIT) {
				ArapButtonStatUtil.setEdittingDefaultBtnstat(alBtn, this);
			} else {
				alBtn.add(this.m_boNext);
				ArapButtonStatUtil.setConfirmBtnstat(alBtn, this, true);
			}
		}
		if (alBtn.size() > 0) {
			ButtonObject[] btns = new ButtonObject[alBtn.size()];
			btns = (ButtonObject[]) (ButtonObject[]) alBtn.toArray(btns);
			return btns;
		}
		return new ButtonObject[0];
	}

	protected void initButtonGroup() {
		if (isInitied()) {
			return;
		}
		setInitied(true);
	
		this.m_boLinkQuery.addChildButton(this.m_boApprove);
		this.m_boLinkQuery.addChildButton(this.m_boParticularQuery);
		this.m_boLinkQuery.addChildButton(this.m_boSFK);
		if ((getDjSettingParam().getSyscode() != 2)
				|| (getDjSettingParam().getSyscode() != -999)) {
			this.m_boLinkQuery.addChildButton(this.m_boProvide);
		}
		this.m_boLinkQuery.addChildButton(this.m_boPf);
		this.m_boLinkQuery.addChildButton(this.m_boPJ);

		this.m_boChangePage.addChildButton(this.m_boFirstPage);
		this.m_boChangePage.addChildButton(this.m_boUpPage);
		this.m_boChangePage.addChildButton(this.m_boDownPage);
		this.m_boChangePage.addChildButton(this.m_boLastPage);
		this.m_boShenheOrNo.addChildButton(this.m_boShenhe);
		this.m_boShenheOrNo.addChildButton(this.m_boUnShenhe);
		
		
	}
}