package nc.ui.ic.ic221;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.ic.auditdlg.ClientUIInAndOut;
import nc.ui.ic.pub.BillFormulaContainer;
import nc.ui.ic.pub.bill.GeneralBillUICtl;
import nc.ui.ic.pub.bill.SpecialBillBaseUI;
import nc.ui.ic.pub.bill.SpecialBillHelper;
import nc.ui.ic.pub.lot.LotNumbRefPane;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.change.PfChangeBO_Client;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.GenMethod;
import nc.vo.ic.pub.SmartVOUtilExt;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.IItemKey;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.bill.SpecialBillHeaderVO;
import nc.vo.ic.pub.bill.SpecialBillItemVO;
import nc.vo.ic.pub.bill.SpecialBillVO;
import nc.vo.ic.pub.lang.ResBase;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;
import nc.vo.rc.receive.ArriveorderItemVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.pub.BillRowNoVO;
import nc.vo.scm.pub.SCMEnv;

public class ClientUI extends SpecialBillBaseUI
{
  ButtonObject m_boDirectOut = null;

  public ClientUI()
  {
    initialize();
  }

  private void setRowNo(GeneralBillVO voBill)
  {
    if ((voBill == null) || (voBill.getChildrenVO().length <= 0)) return;
    BillRowNoVO.setVOsRowNoByRule(voBill.getItemVOs(), "4I", "crowno");
  }

  public void afterEdit(BillEditEvent e)
  {
    super.afterEdit(e);

    String strColName = e.getKey().trim();
    int rownum = e.getRow();
    if (strColName.equals("coutwarehouseid"))
    {
      BillItem bi = getBillCardPanel().getBodyItem("cinventorycode");

      UIRefPane invRef = (UIRefPane)bi.getComponent();
      AbstractRefModel m = invRef.getRefModel();
      BillItem bi1 = getBillCardPanel().getHeadItem("coutwarehouseid");

      UIRefPane invRef1 = (UIRefPane)bi1.getComponent();

      String[] o = { this.m_sCorpID, invRef1.getRefPK() };
      m.setUserParameter(o);
    }
    //卷号扫描--wkf--2015-01-15--
    if(strColName.equals("jhsm")){
    	String pk_corp = getClientEnvironment().getCorporation().getPrimaryKey().toString();
    	if(pk_corp.equals("1078") || pk_corp.equals("1100")|| pk_corp.equals("1108")){
    		Object objjh = getBillCardPanel().getHeadItem("jhsm").getValueObject();
    		if(objjh != null){
    			setJHSMrowVO(objjh.toString(),0);
    		}
    		getBillCardPanel().getHeadItem("jhsm").setValue("");
    		for (int i = 0; i < getBillCardPanel().getBillTable()
    				.getRowCount(); i++) {
    			getBillCardPanel().setBodyValueAt((i + 1) * 10, i, "crowno");
    		}
    		//获得焦点
    		getBillCardPanel().getHeadItem("jhsm").getComponent()
    		.requestFocusInWindow();
    	}
    }
    if (rownum >= 0)
      if (strColName.equals("dshldtransnum"))
        calculateByHsl(rownum, "dshldtransnum", "nshldtransastnum", 0);
      else if (strColName.equals("nshldtransastnum"))
      {
        afterNshldtransastnumEdit(e);
      }
  }
  
  /**
   * setJHSMrowVO
   * 功能：根据扫描出的卷号查出现存量并赋值给表体
   * 作者：王凯飞
   * 日期：2015-01-15
   * */
  @SuppressWarnings("unused")
  private void setJHSMrowVO(String pileno,int pilecount){
	  	String cinventoryid = new String();
		String cinventorycode = new String();
		String cinventoryname = new String();
		String vfree1 = new String();
		UFDouble num = null;
		UFDouble nastnum = null;
		String cspaceid = new String();
		String vbatchcode = new String();
		String csname = new String();
		String Wh = new String();
		Wh = ((UIRefPane) getBillCardPanel().getHeadItem("coutwarehouseid")
				.getComponent()).getRefPK();//出库仓库
		if (Wh == null || Wh.equals("") || Wh.equals("null")) {
			MessageDialog.showErrorDlg(this, "材料出库Sale of the library",
					"仓库不能为空!Warehouse can not be empty!");
			return;
		}
		if ((pilecount == 0) && !pileno.equals(""))// 垛号扫描
		{
			StringBuffer sql = new StringBuffer();
			sql.append(" select * ") 
			.append("   from (select kp.cinventoryid, ") 
			.append("                kp.cspaceid, ") 
			.append("                SUM(nvl(kp.ninspacenum, 0.0) - nvl(kp.noutspacenum, 0.0) - ") 
			.append("                    nvl(icf.nfreezenum, 0.0) + nvl(icf.ndefrznum, 0.0)) num, ") 
			.append("                kp.vbatchcode, ") 
			.append("                kp.vfree1, ") 
			.append("                car.csname, ") 
			.append("                inv.invcode, ") 
			.append("                inv.invname, ") 
			.append("                kp.ninspaceassistnum ") 
			.append("           from v_ic_onhandnum6 kp ") 
			.append("           left join bd_invmandoc man ") 
			.append("             on kp.cinventoryid = man.pk_invmandoc ") 
			.append("           left join bd_invbasdoc inv ") 
			.append("             on man.pk_invbasdoc = inv.pk_invbasdoc ") 
			.append("           left join bd_cargdoc car ") 
			.append("             on car.pk_cargdoc = kp.cspaceid ") 
			.append("           left join ic_freeze icf ") 
			.append("             on kp.cinventoryid = icf.cinventoryid ") 
			.append("            and icf.cwarehouseid = kp.cwarehouseid ") 
			.append("            and nvl(icf.cspaceid, 'byzgyh') = nvl(kp.cspaceid, 'byzgyh') ") 
			.append("            and icf.pk_corp = kp.pk_corp ") 
			.append("            and nvl(kp.vfree1, 'byzgyh') = nvl(icf.vfree1, 'byzgyh') ") 
			.append("            and nvl(kp.vbatchcode, 'byzgyh') = nvl(icf.vbatchcode, 'byzgyh') ") 
			.append("            and nvl(icf.dr, 0) = 0 ") 
			.append("            and nvl(icf.cthawpersonid, '') = '' ") 
			.append("          where kp.vfree1 = '"+pileno+"' ") 
			.append("            and kp.pk_corp = '"+getClientEnvironment().getInstance().getCorporation().getPrimaryKey() + "' ") 
			.append("            and kp.cwarehouseid = '"+Wh+"' ") 
			.append("          group by kp.pk_corp, ") 
			.append("                   kp.ccalbodyid, ") 
			.append("                   kp.cwarehouseid, ") 
			.append("                   kp.cinventoryid, ") 
			.append("                   kp.vbatchcode, ") 
			.append("                   kp.cspaceid, ") 
			.append("                   kp.vfree1, ") 
			.append("                   car.csname, ") 
			.append("                   inv.invcode, ") 
			.append("                   inv.invname, ") 
			.append("                   kp.ninspaceassistnum ) ") 
			.append("  where num > 0 ") ;

			/*
			"select * from (  ";
			SQL += "select  kp.cinventoryid, kp.cspaceid, "
					+ "SUM ( nvl( kp.ninspacenum, 0.0 ) - nvl( kp.noutspacenum, 0.0 ) - nvl(icf.nfreezenum, 0.0) + nvl(icf.ndefrznum, 0.0) ) num"
					+ ",kp.vbatchcode,kp.vfree1,car.csname,inv.invcode,inv.invname,kp.ninspaceassistnum  ";
			SQL += " from v_ic_onhandnum6 kp  ";
			SQL += " left join bd_invmandoc man on kp.cinventoryid = man.pk_invmandoc   ";
			SQL += " left join bd_invbasdoc inv  on man.pk_invbasdoc = inv.pk_invbasdoc  ";
			SQL += " left join bd_cargdoc   car on car.pk_cargdoc=kp.cspaceid  ";
			// edit by shikun 2014-04-18 去除隔离数
			SQL += "left join ic_freeze icf on kp.cinventoryid = icf.cinventoryid ";
			SQL += "                       and icf.cwarehouseid = kp.cwarehouseid ";
			SQL += "                       and nvl(icf.cspaceid,'byzgyh') = nvl(kp.cspaceid,'byzgyh') ";
			SQL += "                       and icf.pk_corp = kp.pk_corp ";
			SQL += "                       and nvl(kp.vfree1,'byzgyh') = nvl(icf.vfree1,'byzgyh') ";
			SQL += "                       and nvl(kp.vbatchcode,'byzgyh') = nvl(icf.vbatchcode,'byzgyh') ";
			SQL += "                       and nvl(icf.dr, 0) = 0 and nvl(icf.cthawpersonid,'')='' ";
			// end shikun
			SQL += "where kp.vfree1 ='"//根据批次号查出现存量信息
					+ pileno
					+ "' and kp.pk_corp='"
					+ getClientEnvironment().getInstance().getCorporation()
							.getPrimaryKey() + "' and kp.cwarehouseid='" + Wh
					+ "'  ";
			SQL += "group by kp.pk_corp, kp.ccalbodyid, kp.cwarehouseid, kp.cinventoryid, kp.vbatchcode,kp.cspaceid ,kp.vfree1,car.csname,inv.invcode,inv.invname,kp.ninspaceassistnum  ";
			SQL += ")  where num>0 ";
			*/

			IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance()
					.lookup(IUAPQueryBS.class.getName());
			List list = null;
			try {
				list = (List) sessionManager.executeQuery(sql.toString(),
						new ArrayListProcessor());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(list.size()==0 || list == null){
				showErrorMessage("卷号:"+pileno+"现存量不足!");
				return;
			}
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				ArrayList values = new ArrayList();
				Object obj = iterator.next();
				if (obj.getClass().isArray()) {
					int len = Array.getLength(obj);
					for (int i = 0; i < len; i++) {
						values.add(Array.get(obj, i));
					}
				}
				cinventoryid = String.valueOf(values.get(0));

				if (values.get(1) != null) {
					cspaceid = (String) values.get(1);
				}
				num = new UFDouble(String.valueOf(values.get(2)));
				if (values.get(3) != null) {
					vbatchcode = (String) values.get(3);
				}
				vfree1 = (String) values.get(4);
				csname = (String) values.get(5);
				cinventorycode = (String) values.get(6);
				cinventoryname = (String) values.get(7);
				nastnum = new UFDouble(String.valueOf(values.get(8)));
			}
			
				int Eindex  = getBillCardPanel().getRowCount()-1;//noutnum
				Object invcode1 = getBillCardPanel().getBodyValueAt(Eindex, "cinventorycode");
				if(Eindex == 0 && invcode1 == null){//如果当前为第一行，并且存货编码项为空
					SpecialBillItemVO newbi = voCopyLine(Eindex);// (GeneralBillItemVO)
					newbi.setStatus(VOStatus.NEW);
					newbi.setCspecialhid(null);
					newbi.setCspecialbid(null);
					newbi.setTs(null);
					getBillCardPanel().getBillModel().setBodyRowVO(newbi,
							Eindex);

					String m_invcode = cinventorycode;
					String m_invid = cinventoryid;
					String m_invname = cinventoryname;

					((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode")
							.getComponent()).setPK(m_invid);
					((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode")
							.getComponent()).setName(m_invname);

					BillEditEvent e = new BillEditEvent(getBillCardPanel()
							.getBodyItem("cinventorycode").getComponent(),
							m_invcode, "cinventorycode", Eindex);
					((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode")
							.getComponent()).setPK(m_invid);
					((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode")
							.getComponent()).setName(m_invname);
					afterEdit(e);
					SetBodyNewValue(vfree1, num, vbatchcode,Eindex,nastnum);
					getBillCardPanel().getBillModel().setRowState(Eindex,
							BillModel.ADD);
				}
				else{
					//校验卷号重复
					for (int i = 0; i < Eindex+1; i++) {
						Object vfree2 = getBillCardPanel().getBodyValueAt(i, "vfree0");
						if(vfree2!=null){
							vfree2 = vfree2.toString().substring(4,vfree2.toString().length()-1);
						}else{
							vfree2 = "null";
						}
						if(!vfree2.equals(null)){
							if(vfree2.equals(vfree1)){
								showErrorMessage("卷号:"+vfree1+"重复！");
								return;
							}
						}
					}
					SpecialBillItemVO newbi = voCopyLine(Eindex);// (GeneralBillItemVO)
//					Object haha= getBillCardPanel().getBodyValueAt(Eindex, "nshouldoutnum");
//					String nshououtnum1 = ""; 
//					if(haha != null){
//						nshououtnum1 = haha.toString();
//					}else{
//						nshououtnum1 = "0";
//					}
					UFDouble nshououtnum = new UFDouble(0);
					onAddRow();
					newbi.setStatus(VOStatus.NEW);
					newbi.setCspecialhid(null);
					newbi.setCspecialbid(null);
					newbi.setTs(null);
					
					getBillCardPanel().getBillModel().setBodyRowVO(newbi,
							Eindex + 1);
					
					String m_invcode = cinventorycode;
					String m_invid = cinventoryid;
					String m_invname = cinventoryname;
					getBillCardPanel().getBillModel().setBodyRowVO(newbi,
							Eindex + 1);
					((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode")
							.getComponent()).setPK(m_invid);
					((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode")
							.getComponent()).setName(m_invname);
					
					BillEditEvent e = new BillEditEvent(getBillCardPanel()
							.getBodyItem("cinventorycode").getComponent(),
							m_invcode, "cinventorycode", Eindex + 1);
					((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode")
							.getComponent()).setPK(m_invid);
					((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode")
							.getComponent()).setName(m_invname);
					afterEdit(e);
					SetBodyNewValue(vfree1, num, vbatchcode,Eindex+1,nastnum);
					getBillCardPanel().getBillModel().setRowState(Eindex + 1,
							BillModel.ADD);
				}
			 
			}
  }
  protected SpecialBillItemVO voCopyLine(int row) {
	  SpecialBillItemVO clonebodyvo =null;
	  clonebodyvo = (SpecialBillItemVO) getBillCardPanel().getBillModel().getBodyValueRowVO(row, SpecialBillItemVO.class.getName());
	  clonebodyvo = (SpecialBillItemVO) clonebodyvo.clone();
	  return clonebodyvo;
  }
  /**
	 * @功能:设置自由项、实发数量、批号、货位
	 * @author ：王凯飞
	 * @2014/12/19
	 */
	private void SetBodyNewValue(String pileno, UFDouble num,String vbatchcode, int sindex, UFDouble nastnum) {
		// setSpace(cspaceid, csname, sindex);
		getBillCardPanel().setBodyValueAt("[卷号:" + pileno + "]", sindex,
				"vfree0");
		getBillCardPanel().setBodyValueAt(nastnum, sindex, "nshldtransastnum");

		nc.vo.scm.ic.bill.FreeVO voFree = new nc.vo.scm.ic.bill.FreeVO();
		voFree.m_vfree0 = "[卷号:" + pileno + "]";
		voFree.m_vfreename1 = "卷号";
		voFree.m_vfree1 = pileno;
		voFree.setVfreeid1("0001A21000000004EIQW");
		m_voBill.setItemFreeVO(sindex, voFree);

		InvVO voInv = (InvVO) getBillCardPanel()
				.getBodyValueAt(sindex, "invvo");
		if (voInv != null) {
			voInv.setFreeItemVO(voFree);
			getBillCardPanel().setBodyValueAt(voInv, sindex, "invvo");
		}
		if (vbatchcode != null && !vbatchcode.equals("")
				&& !vbatchcode.equals("null")
				&& !vbatchcode.equals("_________N/A________")) {
			getBillCardPanel().setBodyValueAt(vbatchcode, sindex, "vbatchcode");
			((LotNumbRefPane) getBillCardPanel().getBodyItem("vbatchcode")
					.getComponent()).setText(vbatchcode);
			((LotNumbRefPane) getBillCardPanel().getBodyItem("vbatchcode")
					.getComponent()).setValue(vbatchcode);
			BillEditEvent e1 = new BillEditEvent(getBillCardPanel()
					.getBodyItem("vbatchcode").getComponent(), vbatchcode,
					"vbatchcode", sindex);
//			afterLotEdit(e1);
		}
		// pickupLotRef("vbatchcode");
		getBillCardPanel().setBodyValueAt(num, sindex, "dshldtransnum");
		BillEditEvent e2 = new BillEditEvent(getBillCardPanel().getBodyItem(
				"dshldtransnum").getComponent(), num, "dshldtransnum", sindex);
//		afterEdit(e2);
	}
  
  
  protected void initialize()
  {
    try
    {
      initVariable();
      super.initialize();
      getBillCardPanel().setTatolRowShow(true);

      super.setQuryPlanprice(true);
    }
    catch (Throwable ivjExc) {
      handleException(ivjExc);
    }
  }

  private GeneralBillVO[] pfVOConvert(SpecialBillVO[] voSp, String sSrcBillType, String sDesBillType)
  {
    GeneralBillVO[] gbvo = null;
    try {
      gbvo = (GeneralBillVO[])PfChangeBO_Client.pfChangeBillToBillArray(voSp, sSrcBillType, sDesBillType);
    }
    catch (Exception e)
    {
      SCMEnv.error(e);
    }
    return gbvo;
  }

  public void onButtonClicked(ButtonObject bo)
  {
    showHintMessage(bo.getName());
    if (bo == this.m_boOut)
      onOut();
    else if (bo == this.m_boIn)
      onIn();
    else if (bo == this.m_boDirectOut)
      onDirectOut();
    else
      super.onButtonClicked(bo);
  }

  public void onAdd()
  {
    super.onAdd();

    getBillCardPanel().setHeadItem("cshlddiliverdate", this.m_sLogDate);
    getBillCardPanel().setHeadItem("vshldarrivedate", this.m_sLogDate);
  }

  protected void setButtonState()
  {
    super.setButtonState();

    switch (this.m_iMode)
    {
    case 0:
      this.m_boDirectOut.setEnabled(false);

      break;
    case 2:
      this.m_boDirectOut.setEnabled(false);
      break;
    case 3:
      if (!isCanPressCopyButton()) {
        this.m_boCopyBill.setEnabled(false);
      }

      this.m_boDirectOut.setEnabled(this.m_iTotalListHeadNum > 0);

      break;
    case 4:
      if (!isCanPressCopyButton()) {
        this.m_boCopyBill.setEnabled(false);
      }

      this.m_boDirectOut.setEnabled(false);
    case 1:
    }

    if (this.m_aryButtonGroup != null)
      updateButtons();
  }

  public void onIn()
  {
    if ((this.m_alListData == null) || (this.m_alListData.size() < this.m_iLastSelListHeadRow) || (this.m_iLastSelListHeadRow < 0))
    {
      return;
    }

    ArrayList alInGeneralVO = new ArrayList();
    SpecialBillVO voSp = (SpecialBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow);

    GeneralBillVO[] gbvo = pfVOConvert(new SpecialBillVO[] { voSp }, "4K", "4A");
    if ((gbvo == null) || (gbvo[0] == null)) {
      showWarningMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000118"));
      return;
    }

    WhVO voWh = voSp.getWhIn();

    if (voWh == null) {
      SCMEnv.out("no wh ,data err.");
      return;
    }

    String sCalBodyID = voWh.getPk_calbody();
    String sCalBodyName = voWh.getVcalbodyname();

    if ((sCalBodyID == null) || (sCalBodyID.trim().length() == 0)) {
      voWh = getWhInfoByID(voWh.getCwarehouseid());
      if (voWh != null) {
        sCalBodyID = voWh.getPk_calbody();
        sCalBodyName = voWh.getVcalbodyname();

        voSp.setWhIn(voWh);
        this.m_alListData.set(this.m_iLastSelListHeadRow, voSp);
      }
    }

    gbvo[0].setHeaderValue("pk_calbody", sCalBodyID);
    gbvo[0].setHeaderValue("vcalbodyname", sCalBodyName);

    BillRowNoVO.setVOsRowNoByRule(gbvo, "4A", "crowno");
    alInGeneralVO.add(gbvo[0]);

    filterZeroBill(gbvo[0]);
    if ((gbvo[0].getItemVOs() == null) || (gbvo[0].getItemCount() == 0)) {
      return;
    }

    getAuditDlg().setIsOK(false);
    getAuditDlg().setSpBill(this.m_voBill);
    int ret = getAuditDlg(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000119"), alInGeneralVO, null).showModal();

    if ((ret == 1) || (getAuditDlg().isOK()))
      try
      {
        SpecialBillVO voMyTempBill = null;
        QryConditionVO qcvo = new QryConditionVO();
        qcvo.setQryCond("cbilltypecode='" + this.m_sBillTypeCode + "' and cspecialhid='" + this.m_voBill.getPrimaryKey().trim() + "'");

        voMyTempBill = (SpecialBillVO)SpecialBillHelper.queryBills(this.m_sBillTypeCode, qcvo).get(0);

        GenMethod mthod = new GenMethod();
        mthod.calAllConvRate(voMyTempBill.getChildrenVO(), "fixedflag", "hsl", "dshldtransnum", "nshldtransastnum", "", "");
        execFormula(voMyTempBill);
        this.m_alListData.set(this.m_iLastSelListHeadRow, voMyTempBill.clone());

        this.m_iFirstSelListHeadRow = -1;
        switchListToBill();
        setButtonState();
        setBillState();

        showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPT40081002-000021") + ResBase.getSuccess());
      }
      catch (Exception e) {
        handleException(e);
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), e.getMessage());
      }
  }

  private void execFormula(SpecialBillVO vo)
  {
    if (vo == null) return;
    ArrayList al = new ArrayList();
    al.add(vo);
    getFormulaBillContainer().formulaBill(al, getFormulaItemHeader(), getFormulaItemBody());
  }

  public void onOut()
  {
    if ((this.m_alListData == null) || (this.m_alListData.size() < this.m_iLastSelListHeadRow) || (this.m_iLastSelListHeadRow < 0))
    {
      return;
    }

    ArrayList alOutGeneralVO = new ArrayList();
    SpecialBillVO voSp = (SpecialBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow);

    GeneralBillVO[] gbvo = pfVOConvert(new SpecialBillVO[] { voSp }, "4K", "4I");
    if ((gbvo == null) || (gbvo[0] == null)) {
      showWarningMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000118"));
      return;
    }

    WhVO voWh = voSp.getWhOut();

    if (voWh == null) {
      SCMEnv.out("no wh ,data err.");
      return;
    }

    String sCalBodyID = voWh.getPk_calbody();
    String sCalBodyName = voWh.getVcalbodyname();

    if ((sCalBodyID == null) || (sCalBodyID.trim().length() == 0)) {
      voWh = getWhInfoByID(voWh.getCwarehouseid());
      if (voWh != null) {
        sCalBodyID = voWh.getPk_calbody();
        sCalBodyName = voWh.getVcalbodyname();

        voSp.setWhOut(voWh);
        this.m_alListData.set(this.m_iLastSelListHeadRow, voSp);
      }
    }
    gbvo[0].setHeaderValue("pk_calbody", sCalBodyID);
    gbvo[0].setHeaderValue("vcalbodyname", sCalBodyName);
    gbvo[0].setHeaderValue("vshldarrivedate", voSp.getHeaderValue("vshldarrivedate"));

    BillRowNoVO.setVOsRowNoByRule(gbvo, "4I", "crowno");
    alOutGeneralVO.add(gbvo[0]);

    filterZeroBill(gbvo[0]);
    if ((gbvo[0].getItemVOs() == null) || (gbvo[0].getItemCount() == 0)) {
      return;
    }
    getAuditDlg().setIsOK(false);
    getAuditDlg().setSpBill(this.m_voBill);
    int ret = getAuditDlg(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000120"), null, alOutGeneralVO).showModal();

    if ((ret == 1) || (getAuditDlg().isOK()))
      try
      {
        SpecialBillVO voMyTempBill = null;
        QryConditionVO qcvo = new QryConditionVO();
        qcvo.setQryCond("cbilltypecode='" + this.m_sBillTypeCode + "' and cspecialhid='" + this.m_voBill.getPrimaryKey().trim() + "'");

        voMyTempBill = (SpecialBillVO)SpecialBillHelper.queryBills(this.m_sBillTypeCode, qcvo).get(0);

        GenMethod mthod = new GenMethod();
        mthod.calAllConvRate(voMyTempBill.getChildrenVO(), "fixedflag", "hsl", "dshldtransnum", "nshldtransastnum", "", "");
        execFormula(voMyTempBill);
        this.m_alListData.set(this.m_iLastSelListHeadRow, voMyTempBill.clone());

        this.m_iFirstSelListHeadRow = -1;
        switchListToBill();
        setButtonState();
        setBillState();

        showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPT40081002-000022") + ResBase.getSuccess());
      }
      catch (Exception e) {
        handleException(e);
        MessageDialog.showErrorDlg(this, null, e.getMessage());
      }
  }

  protected void initButtons()
  {
    this.m_boAdd = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000002"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000308"), 0, "增加");
    this.m_boChange = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000045"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000291"), 0, "修改");
    this.m_boDelete = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000039"), NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000504"), 0, "删除");
    this.m_boCopyBill = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000043"), NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000505"), 0, "复制");
    this.m_boJointAdd = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000003"), NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000506"), 0, "业务类型");
    this.m_boSave = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000001"), NCLangRes.getInstance().getStrByID("common", "UC001-0000001"), 0, "保存");
    this.m_boCancel = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000008"), NCLangRes.getInstance().getStrByID("common", "UC001-0000008"), 0, "取消");

    this.m_boAddRow = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000012"), NCLangRes.getInstance().getStrByID("common", "UC001-0000012"), 0, "增行");
    this.m_boDeleteRow = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000013"), NCLangRes.getInstance().getStrByID("common", "UC001-0000013"), 0, "删行");
    this.m_boInsertRow = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000016"), NCLangRes.getInstance().getStrByID("common", "UC001-0000016"), 0, "插入行");
    this.m_boCopyRow = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000014"), NCLangRes.getInstance().getStrByID("common", "UC001-0000014"), 0, "复制行");
    this.m_boPasteRow = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000015"), NCLangRes.getInstance().getStrByID("common", "UC001-0000015"), 0, "粘贴行");

    this.m_boAuditBill = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000027"), NCLangRes.getInstance().getStrByID("common", "UC001-0000027"), 0, "审批");
    this.m_boCancelAudit = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000028"), NCLangRes.getInstance().getStrByID("common", "UC001-0000028"), 0, "弃审");
    this.m_boQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), 0, "查询");

    this.m_boJointCheck = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000145"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000145"), 0, "联查");

    this.m_boLocate = new ButtonObject(NCLangRes.getInstance().getStrByID("4008spec", "UPPSCMCommon-000089"), NCLangRes.getInstance().getStrByID("4008spec", "UPPSCMCommon-000089"), 0, "定位");
    this.m_PrintMng = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), 0, "打印管理");
    this.m_boPrint = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), 0, "打印");
    this.m_boPreview = new ButtonObject(NCLangRes.getInstance().getStrByID("4008spec", "UPPSCMCommon-000305"), NCLangRes.getInstance().getStrByID("4008spec", "UPPSCMCommon-000305"), 0, "预览");

    this.m_PrintMng.addChildButton(this.m_boPrint);
    this.m_PrintMng.addChildButton(this.m_boPreview);

    this.m_boList = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000186"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000186"), 0, "切换");

    this.m_boOut = new ButtonObject(NCLangRes.getInstance().getStrByID("4008spec", "UPT40081002-000022"), NCLangRes.getInstance().getStrByID("4008spec", "UPT40081002-000022"), 0, "转出");
    this.m_boIn = new ButtonObject(NCLangRes.getInstance().getStrByID("4008spec", "UPT40081002-000021"), NCLangRes.getInstance().getStrByID("4008spec", "UPT40081002-000021"), 0, "转入");

    this.m_billMng = new ButtonObject(NCLangRes.getInstance().getStrByID("4008spec", "UPT40080816-000037"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000074"), 0, "单据维护");
    this.m_billRowMng = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000011"), NCLangRes.getInstance().getStrByID("common", "UC001-0000011"), 0, "行操作");

    this.m_boRowQuyQty = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000359"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000359"), 0, "存量查询");
    this.m_boDirectOut = new ButtonObject(NCLangRes.getInstance().getStrByID("4008spec", "UPT40081002-000024"), NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000507"), 0, "直接转出");

    this.m_billMng.addChildButton(this.m_boAdd);
    this.m_billMng.addChildButton(this.m_boDelete);
    this.m_billMng.addChildButton(this.m_boChange);
    this.m_billMng.addChildButton(this.m_boCopyBill);
    this.m_billMng.addChildButton(this.m_boSave);
    this.m_billMng.addChildButton(this.m_boCancel);

    this.m_billRowMng.addChildButton(this.m_boAddRow);
    this.m_billRowMng.addChildButton(this.m_boDeleteRow);
    this.m_billRowMng.addChildButton(this.m_boInsertRow);
    this.m_billRowMng.addChildButton(this.m_boCopyRow);
    this.m_billRowMng.addChildButton(this.m_boPasteRow);

    this.m_aryButtonGroup = new ButtonObject[] { this.m_billMng, this.m_billRowMng, this.m_boOut, this.m_boIn, this.m_boDirectOut, this.m_boQuery, this.m_boJointCheck, this.m_boRowQuyQty, this.m_boLocate, this.m_PrintMng, this.m_boList };
  }

  protected void initVariable()
  {
    this.m_sBillTypeCode = BillTypeConst.m_transfer;
    this.m_sBillCode = "IC_BILL_TEMPLET_004K";
    this.m_sPNodeCode = "40081002";

    this.m_iFirstAddRows = 1;
  }

  protected boolean checkVO()
  {
    return super.checkVO();
  }

  public void filterRef(String cropid)
  {
    super.filterRef(cropid);

    BillItem bi = getBillCardPanel().getBodyItem("cinventorycode");
    if ((bi != null) && (bi.getComponent() != null))
    {
      UIRefPane invRef = (UIRefPane)bi.getComponent();
      invRef.setTreeGridNodeMultiSelected(true);
      invRef.setMultiSelectedEnabled(true);

      invRef.getRefModel().setIsDynamicCol(true);
      invRef.getRefModel().setDynamicColClassName("nc.ui.ic.ic221.RefOnhandDynamic");

      invRef.setWhereString(" bd_invbasdoc.discountflag='N' and bd_invbasdoc.laborflag='N'  and bd_invmandoc.sealflag ='N' and bd_invmandoc.pk_corp='" + cropid + "'");
    }
  }

  protected String[] getClearIDs(int iBillFlag, String sWhatChange)
  {
    if (sWhatChange == null)
      return null;
    String[] sReturnString = null;
    sWhatChange = sWhatChange.trim();
    if (sWhatChange.equals("cinventorycode"))
    {
      sReturnString = new String[6];
      sReturnString[0] = "vbatchcode";
      sReturnString[1] = "vfree0";
      sReturnString[2] = "dshldtransnum";
      sReturnString[3] = "nshldtransastnum";

      sReturnString[4] = "scrq";
      sReturnString[5] = "dvalidate"; } else {
      if ((sWhatChange.equals("cwarehousename")) && (iBillFlag == 1))
      {
        sReturnString = new String[6];
        sReturnString[0] = "vbatchcode";
        sReturnString[2] = "dshldtransnum";
        sReturnString[3] = "nshldtransastnum";
        sReturnString[4] = "scrq";
        sReturnString[5] = "dvalidate";

        return null;
      }if (sWhatChange.equals("vfree0"))
      {
        sReturnString = new String[3];
        sReturnString[0] = "vbatchcode";

        sReturnString[1] = "scrq";
        sReturnString[2] = "dvalidate";

        return null;
      }if (sWhatChange.equals("coutwarehouseid")) {
        sReturnString = new String[5];
        sReturnString[0] = "vbatchcode";
        sReturnString[1] = "dshldtransnum";
        sReturnString[2] = "nshldtransastnum";
        sReturnString[3] = "scrq";
        sReturnString[4] = "dvalidate";

        return null;
      }if ((sWhatChange.equals("cwarehouseid")) && (iBillFlag == 0)) {
        sReturnString = new String[5];
        sReturnString[0] = "vbatchcode";
        sReturnString[1] = "dshldtransnum";
        sReturnString[2] = "nshldtransastnum";
        sReturnString[3] = "scrq";
        sReturnString[4] = "dvalidate";

        return null;
      }
    }
    return sReturnString;
  }

  public ClientUI(String pk_corp, String billType, String businessType, String operator, String billID)
  {
    initialize(pk_corp, operator, "jc", businessType, "0001", "2003-04-17");

    SpecialBillVO voBill = qryBill(pk_corp, billType, businessType, operator, billID);

    if (voBill == null)
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000121"));
    else
      setBillValueVO(voBill);
  }

  public boolean beforeEdit(BillEditEvent e)
  {
    return true;
  }

  public boolean isCellEditable(boolean value, int row, String itemkey)
  {
    boolean isEditable = super.isCellEditable(value, row, itemkey);
    BillItem bi = getBillCardPanel().getBodyItem(itemkey);
    if (isEditable) {
      if (itemkey.equals("cinventorycode"))
      {
        UIRefPane invRef = (UIRefPane)bi.getComponent();
        AbstractRefModel m = invRef.getRefModel();
        if (getBillCardPanel().getHeadItem("coutwarehouseid") != null) {
          String[] o = { this.m_sCorpID, getBillCardPanel().getHeadItem("coutwarehouseid").getValue() };

          m.setUserParameter(o);
        }

      }
      else if (itemkey.equals("vbatchcode")) {
        WhVO wvo = this.m_voBill.getWhOut();
        getLotNumbRefPane().setParameter(wvo, this.m_voBill.getItemInv(row));
        //add by zwx 2019-9-4转库自由项不填也可以弹出批次参照框，去除控制
        getLotNumbRefPane().setIsMutiSel(true);
        //end by zwx
      }
    }
    bi.setEnabled(isEditable);
    return isEditable;
  }

  protected GeneralBillHeaderVO changeFromSpecialVOtoGeneralVOAboutHeader(GeneralBillVO gvo, SpecialBillVO svo, int iInOutFlag)
  {
    if (iInOutFlag == -1)
      gvo.setWh(svo.getWhOut());
    else {
      gvo.setWh(svo.getWhIn());
    }

    GeneralBillHeaderVO voBillHeader = super.changeFromSpecialVOtoGeneralVOAboutHeader(gvo, svo, iInOutFlag);

    SpecialBillHeaderVO voSpHeader = svo.getHeaderVO();

    if (iInOutFlag == -1)
      voBillHeader.setCdptid(voSpHeader.getCoutdeptid());
    else {
      voBillHeader.setCdptid(voSpHeader.getCindeptid());
    }
    if (iInOutFlag == -1)
      voBillHeader.setCdptname(voSpHeader.getCoutdeptname());
    else {
      voBillHeader.setCdptname(voSpHeader.getCindeptname());
    }
    if (iInOutFlag == -1)
      voBillHeader.setCbizid(voSpHeader.getCoutbsor());
    else {
      voBillHeader.setCbizid(voSpHeader.getCinbsrid());
    }
    if (iInOutFlag == -1)
      voBillHeader.setCbizname(voSpHeader.getCoutbsorname());
    else {
      voBillHeader.setCbizname(voSpHeader.getCinbsrname());
    }
    return voBillHeader;
  }

  protected GeneralBillItemVO changeFromSpecialVOtoGeneralVOAboutItem(GeneralBillVO gbvo, SpecialBillVO sbvo, int iInOutFlag, int j)
  {
    super.changeFromSpecialVOtoGeneralVOAboutItem(gbvo, sbvo, iInOutFlag, j);

    UFDouble ufdTotal = sbvo.getItemValue(j, "dshldtransnum") == null ? this.ZERO : (UFDouble)sbvo.getItemValue(j, "dshldtransnum");

    UFDouble ufdAlreadyIn = sbvo.getItemValue(j, "nadjustnum") == null ? this.ZERO : (UFDouble)sbvo.getItemValue(j, "nadjustnum");

    UFDouble ufdAlreadyOut = sbvo.getItemValue(j, "nchecknum") == null ? this.ZERO : (UFDouble)sbvo.getItemValue(j, "nchecknum");

    UFDouble ufdHsl = (sbvo.getItemValue(j, "hsl") == null) || (sbvo.getItemValue(j, "hsl").toString().trim().length() == 0) ? this.ZERO : (UFDouble)sbvo.getItemValue(j, "hsl");

    if (iInOutFlag == -1) {
      gbvo.setItemValue(j, "nshouldoutnum", ufdTotal.sub(ufdAlreadyOut));
      if ((sbvo.getItemValue(j, "nshldtransastnum") != null) && (sbvo.getItemValue(j, "nshldtransastnum").toString().trim().length() != 0) && (ufdHsl.doubleValue() != 0.0D))
      {
        gbvo.setItemValue(j, "nshouldoutassistnum", ufdTotal.sub(ufdAlreadyOut).div(ufdHsl));
      }

      gbvo.setItemValue(j, "ninnum", null);
      if ((sbvo.getItemValue(j, "nshldtransastnum") != null) && (sbvo.getItemValue(j, "nshldtransastnum").toString().trim().length() != 0) && (ufdHsl.doubleValue() != 0.0D))
      {
        gbvo.setItemValue(j, "noutassistnum", ufdTotal.sub(ufdAlreadyOut).div(ufdHsl));
        gbvo.setItemValue(j, "ninassistnum", null);
      }

    }
    else
    {
      gbvo.setItemValue(j, "nshouldinnum", ufdAlreadyOut.sub(ufdAlreadyIn));
      if ((sbvo.getItemValue(j, "nshldtransastnum") != null) && (sbvo.getItemValue(j, "nshldtransastnum").toString().trim().length() != 0) && (ufdHsl.doubleValue() != 0.0D))
      {
        gbvo.setItemValue(j, "nneedinassistnum", ufdAlreadyOut.sub(ufdAlreadyIn).div(ufdHsl));
      }

      gbvo.setItemValue(j, "noutnum", null);
      if ((sbvo.getItemValue(j, "nshldtransastnum") != null) && (sbvo.getItemValue(j, "nshldtransastnum").toString().trim().length() != 0) && (ufdHsl.doubleValue() != 0.0D))
      {
        gbvo.setItemValue(j, "ninassistnum", ufdAlreadyOut.sub(ufdAlreadyIn).div(ufdHsl));

        gbvo.setItemValue(j, "noutassistnum", null);
      }
    }

    gbvo.setItemValue(j, "dbizdate", null);
    return gbvo.getItemVOs()[j];
  }

  protected void initialize(String pk_corp, String sOperatorid, String sOperatorname, String sBiztypeid, String sGroupid, String sLogDate)
  {
    try
    {
      initVariable();
      super.initialize(pk_corp, sOperatorid, sOperatorname, sBiztypeid, sGroupid, sLogDate);

      getBillCardPanel().setTatolRowShow(true);
    } catch (Throwable ivjExc) {
      handleException(ivjExc);
    }
  }

  protected boolean isCanPressCopyButton()
  {
    if ((this.m_alListData != null) && (this.m_iLastSelListHeadRow >= 0) && (this.m_iLastSelListHeadRow < this.m_alListData.size()) && (this.m_alListData.get(this.m_iLastSelListHeadRow) != null))
    {
      SpecialBillVO vo = (SpecialBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow);
      SpecialBillItemVO[] voaItems = vo.getItemVOs();
      if ((voaItems != null) && (voaItems.length > 0) && (voaItems[0] != null)) {
        Object obj = voaItems[0].getCsourcebillhid();
        if ((obj == null) || (obj.toString().trim().length() == 0)) {
          return true;
        }
        return false;
      }
    }
    return false;
  }

  protected void mustNoNegative(String sFieldCode, int iRow, BillCardPanel bcp, SpecialBillVO vo)
  {
  }

  protected void qryCalbodyByWhid(SpecialBillHeaderVO voHead)
  {
    String sWhID = voHead.getCoutwarehouseid();
    String sCalID = null;
    try {
      if ((sCalID == null) && (sWhID != null)) {
        sCalID = (String)((Object[])(Object[])nc.ui.scm.pub.CacheTool.getCellValue("bd_stordoc", "pk_stordoc", "pk_calbody", sWhID))[0];

        voHead.setPk_calbody_in(sCalID);
        voHead.setPk_calbody_out(sCalID);
      }
    }
    catch (Exception e1) {
      SCMEnv.error(e1);
    }
  }

  public void onDirectOut()
  {
    if ((this.m_alListData == null) || (this.m_alListData.size() < this.m_iLastSelListHeadRow) || (this.m_iLastSelListHeadRow < 0) || (this.m_iMode != 3))
    {
      return;
    }

    showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPT40081002-000022"));

    ArrayList alOutGeneralVO = new ArrayList();
    SpecialBillVO voSp = (SpecialBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow);

    GeneralBillVO[] gbvo = pfVOConvert(new SpecialBillVO[] { voSp }, "4K", "4I");
    if ((gbvo == null) || (gbvo[0] == null)) {
      showWarningMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000118"));
      return;
    }

    ArrayList alInGeneralVO = new ArrayList();
    GeneralBillVO[] gbvo1 = pfVOConvert(new SpecialBillVO[] { voSp }, "4K", "4A");

    if ((gbvo1 == null) || (gbvo1[0] == null)) {
      showWarningMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000118"));
      return;
    }

    for (int i = 0; i < gbvo[0].getItemCount(); i++) {
      gbvo1[0].setItemValue(i, "ninnum", gbvo[0].getItemValue(i, "noutnum"));
      gbvo1[0].setItemValue(i, "ninassistnum", gbvo[0].getItemValue(i, "noutassistnum"));
      gbvo1[0].setItemValue(i, "nshouldinnum", gbvo[0].getItemValue(i, "nshouldoutnum"));
      gbvo1[0].setItemValue(i, "nneedinassistnum", gbvo[0].getItemValue(i, "nshouldoutassistnum"));
    }

    filterZeroBill(gbvo[0]);
    if ((gbvo[0].getItemVOs() == null) || (gbvo[0].getItemCount() == 0)) {
      return;
    }

    setRowNo(gbvo[0]);
    alOutGeneralVO.add(gbvo[0]);

    filterZeroBill(gbvo1[0]);
    if ((gbvo1[0].getItemVOs() == null) || (gbvo1[0].getItemCount() == 0)) {
      return;
    }

    setRowNo(gbvo1[0]);
    alInGeneralVO.add(gbvo1[0]);

    getAuditDlg().setIsOK(false);
    getAuditDlg().setSpBill(this.m_voBill);
    getAuditDlg().setVO4Direct(alInGeneralVO, alOutGeneralVO, BillTypeConst.m_transfer, this.m_voBill.getPrimaryKey().trim(), this.m_sCorpID, this.m_sUserID);

    if (GeneralBillUICtl.isSeriaoInvNegative(gbvo[0])) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000122"));
      return;
    }

    int ret = getAuditDlg().showModal();

    if ((ret == 1) || (getAuditDlg().isOK())) {
      try
      {
        SpecialBillVO voMyTempBill = null;
        QryConditionVO qcvo = new QryConditionVO();
        qcvo.setQryCond("cbilltypecode='" + this.m_sBillTypeCode + "' and cspecialhid='" + this.m_voBill.getPrimaryKey().trim() + "'");
        voMyTempBill = (SpecialBillVO)SpecialBillHelper.queryBills(this.m_sBillTypeCode, qcvo).get(0);

        GenMethod mthod = new GenMethod();
        mthod.calAllConvRate(voMyTempBill.getChildrenVO(), "fixedflag", "hsl", "dshldtransnum", "nshldtransastnum", "", "");
        execFormula(voMyTempBill);
        this.m_alListData.set(this.m_iLastSelListHeadRow, voMyTempBill.clone());

        this.m_iFirstSelListHeadRow = -1;
        switchListToBill();
        setButtonState();
        setBillState();
      } catch (Exception e) {
        getAuditDlg().setIsDirectOut(false);
        handleException(e);
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), e.getMessage());
      }
    }
    getAuditDlg().setIsDirectOut(false);
  }

  private void filterZeroBill(GeneralBillVO vo)
  {
    GeneralBillItemVO[] voItems = vo.getItemVOs();

    Vector v = new Vector();
    if ((voItems != null) && (voItems.length > 0)) {
      for (int i = 0; i < voItems.length; i++)
        if ((GenMethod.isNotEqualsZero(voItems[i].getNshouldinnum())) || (GenMethod.isNotEqualsZero(voItems[i].getNshouldoutnum())))
        {
          v.add(voItems[i]);
        }
    }
    voItems = null;
    if ((v != null) || (v.size() > 0)) {
      for (int i = 0; i < v.size(); i++) {
        voItems = new GeneralBillItemVO[v.size()];
        v.copyInto(voItems);
      }
    }
    vo.setChildrenVO(voItems);
  }
}