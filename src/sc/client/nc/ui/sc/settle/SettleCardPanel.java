package nc.ui.sc.settle;

import java.util.ArrayList;
import nc.bs.bd.b21.CurrencyRateUtil;
import nc.ui.bd.b21.CurrtypeQuery;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.sc.adjust.AdjustbillHelper;
import nc.ui.sc.pub.BillEdit;
import nc.ui.sc.pub.OtherRefModel;
import nc.ui.sc.pub.PublicHelper;
import nc.ui.sc.pub.ScTool;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.vo.bd.b20.CurrtypeVO;
import nc.vo.mm.pub.pub1020.BomBTdItemVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sc.pub.ScConstants;
import nc.vo.sc.settle.BalanceVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pub.SCMEnv;

public class SettleCardPanel extends BillCardPanel
{
  private SettleUI settleui;
  private String pkCorp;
  private String pkUser;

  public SettleCardPanel(SettleUI ui)
  {
    this.settleui = ui;
    this.pkCorp = this.settleui.getPk_corp();
    this.pkUser = this.settleui.getOperatorID();
    initpanel();
  }

  private void initpanel()
  {
    setName("CardPanel");

    loadTemplet("66", null, this.pkUser, this.pkCorp);

    String groupStr = "where pk_corp = '" + this.pkCorp + "' " + "and dr = 0 " + "and vbatch is not null " + "and vbatch in(select vbatch from sc_balance where nbalancenum <> 0) ";

    UIRefPane ref = new UIRefPane();
    ref.setIsCustomDefined(true);
    ref.setRefModel(new OtherRefModel(OtherRefModel.REF_VBATCH));
    ref.setCacheEnabled(false);
    ref.setReturnCode(true);
    ref.setWhereString(groupStr);
    ref.getRefModel().setGroupPart(" cvendorid ,cprocessbaseid,cmaterialbaseid,vbatch,vfree1 ");//edit by hk

    getBodyItem("vbatch").setComponent(ref);

    BillData billData = getBillData();
    FreeItemRefPane freeRef = new FreeItemRefPane();
    freeRef.setMaxLength(billData.getBodyItem("vfree0").getLength());
    billData.getBodyItem("vfree0").setComponent(freeRef);
    setBillData(billData);

    ScTool.changeBillDataByUserDef(this, this.pkCorp, "66", this.pkUser);

    setEnabled(false);

    setPrecision();

    addEditListener(new BillEditListener()
    {
      public void afterEdit(BillEditEvent e)
      {
        try {
          int rowindex = e.getRow();

          if (e.getKey().equals("cinventorycode"))
          {
            if (SettleCardPanel.this.getBodyValueAt(rowindex, "cbaseid") == null) {
              return;
            }
            String strPk_CLBaseidnew = SettleCardPanel.this.getBodyValueAt(rowindex, "cbaseid").toString();

            String strPk_CLBaseidOld = SettleCardPanel.this.getBodyValueAt(rowindex, "cbaseidcopy").toString();

            String strPk_CLMangidnew = SettleCardPanel.this.getBodyValueAt(rowindex, "cmangid").toString();

            String strWareID = SettleCardPanel.this.getHeadItem("cwareid").getValue();
            String strPk_JGPBaseid = null;
            String strPk_JGPMangid = null;
            String strvendormangid = ((UIRefPane)SettleCardPanel.this.getHeadItem("cvendormangid").getComponent()).getRefPK();

            if (strPk_CLBaseidnew != null) {
              if (strPk_CLBaseidnew.equals(strPk_CLBaseidOld)) {
                Object value = SettleCardPanel.this.getBodyValueAt(rowindex, "nnumcopy");

                SettleCardPanel.this.setBodyValueAt(value, rowindex, "nnum");
              }
              else {
                for (int i = rowindex; i >= 0; i--) {
                  Object value = SettleCardPanel.this.getBodyValueAt(i, "bismaterial");

                  if ((value == null) || (!value.toString().trim().equals(ScConstants.PROCESSFLAG))) {
                    continue;
                  }
                  strPk_JGPBaseid = SettleCardPanel.this.getBodyValueAt(i, "cbaseid").toString();

                  strPk_JGPMangid = SettleCardPanel.this.getBodyValueAt(i, "cmangid").toString();

                  break;
                }

                ArrayList l_arylst = PublicHelper.getBomTdwl(SettleCardPanel.this.pkCorp, strWareID, strPk_JGPBaseid, new String[] { strPk_CLBaseidOld }, ClientEnvironment.getInstance().getDate(), null);

                BomBTdItemVO[] tdItems = (BomBTdItemVO[])(BomBTdItemVO[])l_arylst.get(0);

                double ldbl_TDXS = 1.0D;
                for (int i = 0; i < tdItems.length; i++) {
                  if (!tdItems[i].getWlbmid().equals(strPk_CLBaseidnew))
                    continue;
                  ldbl_TDXS = tdItems[i].getTdxs().doubleValue();

                  break;
                }

                Object value = SettleCardPanel.this.getBodyValueAt(rowindex, "nnumcopy");

                if (value != null) {
                  UFDouble lUFD_Value = ((UFDouble)value).multiply(ldbl_TDXS);

                  SettleCardPanel.this.setBodyValueAt(lUFD_Value, rowindex, "nnum");
                }

                Object nmatenum = SettleCardPanel.this.getBodyValueAt(rowindex, "nmatenumcopy");

                if (nmatenum != null) {
                  UFDouble lUFD_nmatenum = ((UFDouble)nmatenum).multiply(ldbl_TDXS);

                  SettleCardPanel.this.setBodyValueAt(lUFD_nmatenum, rowindex, "nmatenum");
                }

              }

              BalanceVO conBalanceVO = new BalanceVO();
              conBalanceVO.setCprocessbaseid(strPk_JGPBaseid);
              conBalanceVO.setCprocessmangid(strPk_JGPMangid);
              conBalanceVO.setCmaterialbaseid(strPk_CLBaseidnew);
              conBalanceVO.setCmaterialmangid(strPk_CLMangidnew);
              conBalanceVO.setCvendormangid(strvendormangid);
              UFDouble[] nMatePrice = getMatePrice(new BalanceVO[] { conBalanceVO });

              if (nMatePrice.length == 1) {
                SettleCardPanel.this.setBodyValueAt(nMatePrice[0], rowindex, "nprice");

                UFDouble lUFD_nnum = (UFDouble)SettleCardPanel.this.getBodyValueAt(rowindex, "nnum");

                SettleCardPanel.this.setBodyValueAt(lUFD_nnum.multiply(nMatePrice[0]), rowindex, "nmny");
              }

            }

          }

          if (e.getKey().equals("cinventorycode"))
          {
            SettleCardPanel.this.setBodyValueAt("", rowindex, "vbatch");
            boolean editFlag = BillEdit.editVbatch(SettleCardPanel.this, rowindex, "cmangid");

            if (editFlag)
            {
              Object cvendormangid = SettleCardPanel.this.getHeadItem("cvendormangid").getValue();

              Object cmaterialmangid = SettleCardPanel.this.getBodyValueAt(rowindex, "cmangid");

              int curProcessRow = -1;

              for (int i = rowindex; i >= 0; i--) {
                Object value = SettleCardPanel.this.getBodyValueAt(i, "bismaterial");
                if ((value == null) || (value.toString().trim().indexOf(ScConstants.PROCESSFLAG) == -1)) {
                  continue;
                }
                curProcessRow = i;
                break;
              }

              Object cprocessmangid = SettleCardPanel.this.getBodyValueAt(curProcessRow, "cmangid");

              if ((cvendormangid == null) || (cmaterialmangid == null) || (cprocessmangid == null))
              {
                SettleCardPanel.this.getBodyItem("vbatch").setEnabled(false);
              } else {
                SettleCardPanel.this.getBodyItem("vbatch").setEnabled(true);
                UIRefPane ref = (UIRefPane)SettleCardPanel.this.getBodyItem("vbatch").getComponent();

                ref.setWhereString("a.dr=0 and a.pk_corp='" + SettleCardPanel.this.pkCorp + "'and  cmaterialmangid='" + cmaterialmangid + "' and cprocessmangid='" + cprocessmangid + "' and cvendormangid='" + cvendormangid + "'");
              }

            }

            SettleCardPanel.this.setBodyValueAt("", rowindex, "vfree0");
            setMaterialFreeItemForOneRow(rowindex);
            InvVO invVO = (InvVO)SettleCardPanel.this.getBodyValueAt(rowindex, "invvo");

            if ((invVO == null) || (invVO.getFreeItemVO() == null) || (!BillEdit.definedFreeItem(invVO.getFreeItemVO())))
            {
              SettleCardPanel.this.setCellEditable(rowindex, "vfree0", false);
            } else {
              SettleCardPanel.this.setCellEditable(rowindex, "vfree0", true);
              FreeItemRefPane freeRef = (FreeItemRefPane)SettleCardPanel.this.getBodyItem("vfree0").getComponent();

              freeRef.setFreeItemParam(invVO);
            }

          }

          if ((e.getKey().equals("nnum")) || (e.getKey().equals("nprice")) || (e.getKey().equals("nmny")) || (e.getKey().equals("cinventorycode")))
          {
            int curProcessRow = -1;
            int lastMaterialRow = -1;

            for (int i = rowindex; i >= 0; i--) {
              Object value = SettleCardPanel.this.getBodyValueAt(i, "bismaterial");
              if ((value == null) || (value.toString().trim().indexOf(ScConstants.PROCESSFLAG) == -1)) {
                continue;
              }
              curProcessRow = i;
              break;
            }

            for (int i = rowindex; i < SettleCardPanel.this.getRowCount(); i++) {
              Object value = SettleCardPanel.this.getBodyValueAt(i, "bismaterial");
              if ((value == null) || (value.toString().trim().indexOf(ScConstants.PROCESSFLAG) == -1)) {
                continue;
              }
              lastMaterialRow = i - 1;
              break;
            }

            if (lastMaterialRow == -1)
              lastMaterialRow = SettleCardPanel.this.getRowCount() - 1;
            editProcessRow(curProcessRow, lastMaterialRow);

            SettleCardPanel.this.updateUI();
          }

          if (e.getKey().equals("vfree0"))
            BillEdit.editFreeItem(SettleCardPanel.this, rowindex, e.getKey(), "cbaseid", "cmangid");
          //add by hk
          if(e.getKey().equals("vbatch")&&("1078".equals(getCorp())||"1078".equals(getCorp()))){//edit by mcw
        	  Object vbatch = SettleCardPanel.this.getBodyValueAt(rowindex, "vbatch");
        	  String[] a = vbatch.toString().split("&");
        	  SettleCardPanel.this.setBodyValueAt(a[0],rowindex, "vbatch");
        	  SettleCardPanel.this.setBodyValueAt(a[1],rowindex, "vfree1");
        	  SettleCardPanel.this.setBodyValueAt("[¸Ö¾íºÅ:"+a[1]+"]",rowindex, "vfree0");
        	  BillEdit.editFreeItem(SettleCardPanel.this, rowindex, e.getKey(), "cbaseid", "cmangid");
          }
          //add by hk end
        }
        catch (Exception exception)
        {
          SCMEnv.out("ºËÏúÊ§°Ü");
          exception.printStackTrace(System.out);
          return;
        }
      }

      private UFDouble[] getMatePrice(BalanceVO[] conBalanceVO)
      {
        UFDouble[] d = new UFDouble[conBalanceVO.length];
        for (int i = 0; i < d.length; i++)
          d[i] = new UFDouble(0);
        try
        {
          BalanceVO[] resultBalanceVO = MaterialledgerHelper.queryPriceBatch(conBalanceVO);

          if ((resultBalanceVO != null) && (resultBalanceVO.length > 0))
            for (int i = 0; i < resultBalanceVO.length; i++)
              if ((resultBalanceVO[i].getNbalancenum() == null) || (resultBalanceVO[i].getNbalancemny() == null))
              {
                d[i] = new UFDouble(0);
              }
              else if (resultBalanceVO[i].getNbalancenum().doubleValue() == 0.0D)
              {
                d[i] = new UFDouble(0);
              }
              else
              {
                UFDouble nbalancenum = resultBalanceVO[i].getNbalancenum();

                UFDouble nbalancemny = resultBalanceVO[i].getNbalancemny();

                d[i] = nbalancemny.div(nbalancenum);
              }
        }
        catch (Exception e)
        {
          SCMEnv.out(e);
        }

        return d;
      }

      private void setMaterialFreeItemForOneRow(int rowindex)
      {
        Object value = SettleCardPanel.this.getBodyValueAt(rowindex, "bismaterial");
        Object pk_invmangdoc;

        if ((value == null) || (value.toString().trim().equals(""))) {
          pk_invmangdoc = SettleCardPanel.this.getBodyValueAt(rowindex, "cmangid");
          if (pk_invmangdoc == null)
            return;
        } else {
          return;
        }
        FreeVO freeVO = null;
        InvVO invVO = null;
        try
        {
          freeVO = AdjustbillHelper.queryFreeVOByInvID(pk_invmangdoc.toString());
        }
        catch (Exception e) {
          SCMEnv.out(e);
          return;
        }

        Object pk_invbasedoc = SettleCardPanel.this.getBodyValueAt(rowindex, "cbaseid");

        Object cinventorycode = SettleCardPanel.this.getBodyValueAt(rowindex, "cinventorycode");

        Object cinventoryname = SettleCardPanel.this.getBodyValueAt(rowindex, "cinventoryname");

        Object invspec = SettleCardPanel.this.getBodyValueAt(rowindex, "invspec");
        Object invtype = SettleCardPanel.this.getBodyValueAt(rowindex, "invtype");

        invVO = new InvVO();
        invVO.setFreeItemVO(freeVO);
        invVO.setCinvmanid(pk_invmangdoc.toString());
        invVO.setCinventoryid(pk_invbasedoc.toString());
        invVO.setIsFreeItemMgt(new Integer(1));
        invVO.setCinventorycode(cinventorycode == null ? "" : cinventorycode.toString());

        invVO.setInvname(cinventoryname == null ? "" : cinventoryname.toString());

        invVO.setInvspec(invspec == null ? "" : invspec.toString());
        invVO.setInvtype(invtype == null ? "" : invtype.toString());

        FreeItemRefPane freeRef = (FreeItemRefPane)SettleCardPanel.this.getBodyItem("vfree0").getComponent();

        freeRef.setFreeItemParam(invVO);

        SettleCardPanel.this.setBodyValueAt(invVO, rowindex, "invvo");
      }

      private void editProcessRow(int curProcessRow, int lastMaterialRow)
      {
        if ((curProcessRow < 0) || (lastMaterialRow < 0))
          return;
        UFDouble totalMny = new UFDouble(0);
        for (int i = curProcessRow + 1; i < lastMaterialRow + 1; i++) {
          Object money = SettleCardPanel.this.getBodyValueAt(i, "nmny");
          if ((money != null) && (!money.toString().trim().equals(""))) {
            UFDouble a = new UFDouble(money.toString());
            totalMny = totalMny.add(a);
          }
        }

        SettleCardPanel.this.setBodyValueAt(totalMny, curProcessRow, "nmny");
        Object number = SettleCardPanel.this.getBodyValueAt(curProcessRow, "nnum");
        if ((number != null) && (!number.toString().trim().equals(""))) {
          UFDouble nnum = new UFDouble(number.toString());
          if (nnum.doubleValue() != 0.0D)
            SettleCardPanel.this.setBodyValueAt(totalMny.div(nnum), curProcessRow, "nprice");
        }
      }

      public void bodyRowChange(BillEditEvent e)
      {
        int rowindex = e.getRow();
        if (rowindex == -1) return;
        Object tempBatch = SettleCardPanel.this.getBodyValueAt(rowindex, "vbatch");

        Object value = SettleCardPanel.this.getBodyValueAt(rowindex, "bismaterial");
        boolean canEdit = false;
        if ((value == null) || (value.toString().trim().equals("")))
        {
          canEdit = true;
        }

        SettleCardPanel.this.setCellEditable(rowindex, "cinventorycode", canEdit);

        SettleCardPanel.this.setCellEditable(rowindex, "nnum", canEdit);
        SettleCardPanel.this.setCellEditable(rowindex, "nmny", canEdit);
        SettleCardPanel.this.setCellEditable(rowindex, "nprice", canEdit);
        SettleCardPanel.this.setCellEditable(rowindex, "vfree0", canEdit);
        SettleCardPanel.this.setCellEditable(rowindex, "vbatch", canEdit);
        SettleCardPanel.this.setCellEditable(rowindex, "vmemo", canEdit);

        if (!canEdit) {
          SettleCardPanel.this.settleui.setButtonsState();
          return;
        }

        InvVO invVO = (InvVO)SettleCardPanel.this.getBodyValueAt(rowindex, "invvo");

        if ((invVO == null) || (invVO.getFreeItemVO() == null) || (!BillEdit.definedFreeItem(invVO.getFreeItemVO())))
        {
          SettleCardPanel.this.setCellEditable(rowindex, "vfree0", false);
        } else {
          SettleCardPanel.this.setCellEditable(rowindex, "vfree0", true);
          FreeItemRefPane freeRef = (FreeItemRefPane)SettleCardPanel.this.getBodyItem("vfree0").getComponent();

          freeRef.setFreeItemParam(invVO);
        }

        boolean editFlag = BillEdit.editVbatch(SettleCardPanel.this, rowindex, "cmangid");

        if (editFlag)
        {
          Object cvendormangid = SettleCardPanel.this.getHeadItem("cvendormangid").getValue();

          Object cmaterialmangid = SettleCardPanel.this.getBodyValueAt(rowindex, "cmangid");
          int curProcessRow = -1;

          for (int i = rowindex; i >= 0; i--) {
            value = SettleCardPanel.this.getBodyValueAt(i, "bismaterial");
            if ((value == null) || (value.toString().trim().indexOf(ScConstants.PROCESSFLAG) == -1)) {
              continue;
            }
            curProcessRow = i;
            break;
          }

          Object cprocessmangid = SettleCardPanel.this.getBodyValueAt(curProcessRow, "cmangid");

          if ((cvendormangid == null) || (cmaterialmangid == null) || (cprocessmangid == null))
          {
            SettleCardPanel.this.getBodyItem("vbatch").setEnabled(false);
          }
          else {
            SettleCardPanel.this.getBodyItem("vbatch").setEnabled(true);
            UIRefPane ref = (UIRefPane)SettleCardPanel.this.getBodyItem("vbatch").getComponent();

            ref.setWhereString("pk_corp='" + SettleCardPanel.this.pkCorp + "' and  cmaterialmangid='" + cmaterialmangid + "' and cprocessmangid='" + cprocessmangid + "' and cvendormangid='" + cvendormangid + "' and dr = 0" + " and vbatch is not null" + " and vbatch in (select vbatch from sc_balance where nbalancenum <> 0)");

            if ((tempBatch != null) && (!tempBatch.toString().equals("")))
            {
              ref.setPK(tempBatch);
            }

            SettleCardPanel.this.setBodyValueAt(tempBatch, rowindex, "vbatch");
          }

        }

        SettleCardPanel.this.settleui.setButtonsState();
      }
    });
    addBodyEditListener2(new BillEditListener2()
    {
      public boolean beforeEdit(BillEditEvent e)
      {
        int rowindex = e.getRow();
        if (e.getKey().equals("cinventorycode"))
        {
          String cmaterialbaseid = SettleCardPanel.this.getBodyValueAt(rowindex, "cbaseidcopy").toString().trim();

          int curProcessRow = -1;
          for (int i = rowindex; i >= 0; i--) {
            Object value = SettleCardPanel.this.getBodyValueAt(i, "bismaterial");
            if ((value == null) || (value.toString().trim().indexOf(ScConstants.PROCESSFLAG) == -1)) {
              continue;
            }
            curProcessRow = i;
            break;
          }

          String cprocessbaseid = SettleCardPanel.this.getBodyValueAt(curProcessRow, "cbaseidcopy").toString().trim();

          String cwareid = SettleCardPanel.this.getHeadItem("cwareid").getValue();

          String cbilldate = SettleCardPanel.this.getHeadItem("dbilldate").getValue();

          UIRefPane ref = (UIRefPane)SettleCardPanel.this.getBodyItem("cinventorycode").getComponent();

          String ls_where = " where bd_produce.pk_produce in(select td.pk_produce from bd_bom a, bd_bom_b b, bd_bom_td td where a.pk_bomid = b.pk_bomid and b.pk_bom_bid = td.pk_bom_bid and a.wlbmid = '" + cprocessbaseid + "' and a.gcbm = '" + cwareid + "' and a.sfmr = 'Y' and b.zxbmid = '" + cmaterialbaseid + "' and b.sdate <= '" + cbilldate + "' and b.edate >= '" + cbilldate + "') or (bd_invbasdoc.pk_invbasdoc = '" + cmaterialbaseid + "' and bd_produce.pk_calbody = '" + cwareid + "')";

          ref.setWhereString(ls_where);
        }

        return true;
      }
    });
    addBodyMouseListener(new BillTableMouseListener()
    {
      public void mouse_doubleclick(BillMouseEnent e)
      {
        int m_billState = SettleCardPanel.this.settleui.getBillState();

        if ((m_billState == ScConstants.STATE_VERIFY) || (m_billState == ScConstants.STATE_MODIFY) || (m_billState == ScConstants.STATE_UNVERIFY))
        {
          Object invShow = SettleCardPanel.this.getBodyValueAt(e.getRow(), "bismaterial");
          if ((invShow != null) && (invShow.equals(ScConstants.PROCESSFLAG)))
          {
            SettleCardPanel.this.setBodyValueAt(ScConstants.FIXPROCESS, e.getRow(), "bismaterial");
          }
          if ((invShow != null) && (invShow.equals(ScConstants.FIXPROCESS)))
          {
            SettleCardPanel.this.setBodyValueAt(ScConstants.PROCESSFLAG, e.getRow(), "bismaterial");
          }
        }
      } } );
  }

  private void setPrecision() {
    int numPrecision = 2;
    int pricePrecision = 2;
    int moneyPrecision = 2;

    int[] precisions = new int[2];
    int i;
    try {
      precisions = PublicHelper.getDigitBatch(this.pkCorp, new String[] { "BD501", "BD505" });
    }
    catch (Exception e) {
       for (i = 0; i < precisions.length; i++)precisions[i] = 2;
    }

    numPrecision = precisions[0];
    pricePrecision = precisions[1];
    try
    {
      Integer localPrecision = CurrtypeQuery.getInstance().getCurrtypeVO(new CurrencyRateUtil(this.pkCorp).getLocalCurrPK()).getCurrdigit();

      moneyPrecision = localPrecision.intValue();
    }
    catch (Exception e) {
      moneyPrecision = 2;
    }

    getBodyItem("nnum").setDecimalDigits(numPrecision);
    getBodyItem("nnumcopy").setDecimalDigits(numPrecision);
    getBodyItem("nmatenum").setDecimalDigits(numPrecision);
    getBodyItem("nmatenumcopy").setDecimalDigits(numPrecision);
    getBodyItem("oldnum").setDecimalDigits(numPrecision);

    getBodyItem("nprice").setDecimalDigits(pricePrecision);
    getBodyItem("nprocessmny").setDecimalDigits(pricePrecision);

    getBodyItem("nmny").setDecimalDigits(moneyPrecision);
    getBodyItem("oldmoney").setDecimalDigits(moneyPrecision);
  }
}