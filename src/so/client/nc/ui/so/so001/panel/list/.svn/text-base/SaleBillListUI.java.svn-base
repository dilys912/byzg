/*jadclipse*/// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10) 
// Source File Name:   SaleBillListUI.java

package nc.ui.so.so001.panel.list;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.ui.bd.b39.JobRefTreeModel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.*;
import nc.ui.pub.bill.*;
import nc.ui.scm.pub.bill.IBillExtendFun;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.ui.so.so001.panel.bom.BillTools;
import nc.ui.so.so001.panel.card.*;
import nc.vo.bd.CorpVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.QueryConditionVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.sm.UserVO;
import nc.vo.so.so001.*;

// Referenced classes of package nc.ui.so.so001.panel.list:
//            SOBillListPanel, SOBillQueryDlg, SOBusiTypeRefPane, BillListTools, 
//            SaleOrderVOCache

public abstract class SaleBillListUI extends SaleBillCardUI
    implements IBillExtendFun
{

    public SaleBillListUI()
    {
        m_rdoAll = new UIRadioButton();
        m_rdoFree = new UIRadioButton();
        m_rdoAudited = new UIRadioButton();
        m_rdoFreeze = new UIRadioButton();
        m_rdoBatch = new UIRadioButton();
        m_rdoBlankOut = new UIRadioButton();
        m_rdoAuditing = new UIRadioButton();
        strShowState = "\u5217\u8868";
        selectRow = -1;
        num = -1;
        curShowPanel = null;
        alInvs = getBillCardPanel().alInvs;
    }

    public SaleBillListUI(String pk_corp, String billtype, String busitype, String operator, String id)
    {
        super(pk_corp, billtype, busitype, operator, id);
        m_rdoAll = new UIRadioButton();
        m_rdoFree = new UIRadioButton();
        m_rdoAudited = new UIRadioButton();
        m_rdoFreeze = new UIRadioButton();
        m_rdoBatch = new UIRadioButton();
        m_rdoBlankOut = new UIRadioButton();
        m_rdoAuditing = new UIRadioButton();
        strShowState = "\u5217\u8868";
        selectRow = -1;
        num = -1;
        curShowPanel = null;
        alInvs = getBillCardPanel().alInvs;
    }

    protected void initialize()
    {
        long st = System.currentTimeMillis();
        strShowState = "\u5217\u8868";
        selectRow = -1;
        num = -1;
        loadAllBtns();
        super.initialize();
        loadListTemplet();
        initIDs();
        switchInterface();
        getBillListPanel().addBodyTotalLstn();
        getBillListPanel().addHeadSortRelaLstn();
        getBillListPanel().addListSelectionLstn();
        SCMEnv.out((new StringBuilder()).append(" admin initialize cost time ").append(System.currentTimeMillis() - st).toString());
    }

    protected void loadAllBtns()
    {
        getBoAction();
        getBoAdd();
        getBoAddLine();
        getBoAssistant();
        getBoAsstntQry();
        getBoAudit();
        getBoAuditFlowStatus();
        getBoBack();
        getBoBatch();
        getBoBlankOut();
        getBoBrowse();
        getBoCancel();
        getBoCancelAudit();
        getBoCancelFreeze();
        getBoCard();
        getBoCopyBill();
        getBoCopyLine();
        getBoCustCredit();
        getBoCustInfo();
        getBoDelLine();
        getBoDocument();
        getBoEdit();
        getBoFind();
        getBoFinish();
        getBoFirst();
        getBoFreeze();
        getBoInsertLine();
        getBoLast();
        getBoLine();
        getBoListDeselectAll();
        getBoListSelectAll();
        getBoMaintain();
        getBoModification();
        getBoNext();
        getBoOnHandShowHidden();
        getBoOrdBalance();
        getBoOrderExecRpt();
        getBoOrderQuery();
        getBoCachPay();
        getBoPasteLine();
        getBoPre();
        getBoPreview();
        getBoPrifit();
        getBoPrint();
        getBoPrintMgr();
        getBoQuery();
        getBoRefresh();
        getBoRefundment();
        getBoReturn();
        getBoSave();
        getBoSendAudit();
        getBoStockLock();
        getBoImport(); //add by zwx  2014-9-1
    }

    //add by zwx  2014-9-1
	protected ButtonObject getBoImport() {
		if (boImport == null)
			boImport = new ButtonObject("Excel导入", "Excel导入","Excel导入");
		return boImport;
	}

	private void loadListTemplet()
    {
        showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000135"));
        BillListData bd = null;
        billtempletVO = null;
        if(SO_20.booleanValue())
        {
            if(billtempletVO == null)
                billtempletVO = getBillListPanel().getDefaultTemplet(getNodeCode(), null, getBillListPanel().getOperator(), getBillListPanel().getCorp());
            bd = new BillListData(billtempletVO);
        } else
        {
            if(billtempletVO == null)
                billtempletVO = getBillListPanel().getDefaultTemplet(getNodeCode(), getBillListPanel().getBusiType(), getBillListPanel().getOperator(), getBillListPanel().getCorp());
            bd = new BillListData(billtempletVO);
        }
        billtempletVO = null;
        BillItem bm = bd.getHeadItem("naccountperiod");
        if(bm != null)
            bm.setDecimalDigits(0);
        SOBillCardTools.processCTBillItem(null, bd);
        setListPanelByPara(bd);
        getBillListPanel().setListData(bd);
        initBodyComboBox();
        BillItem bmbiztype = bd.getHeadItem("cbiztype");
        if(bmbiztype != null)
        {
            bmbiztype.setShow(false);
            bmbiztype.setEdit(false);
        }
        BillItem cbiztypename = bd.getHeadItem("cbiztypename");
        if(cbiztypename != null)
            cbiztypename.setEdit(false);
        getBillListPanel().getChildListPanel().setTatolRowShow(true);
        showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000147"));
    }

    private void setListPanelByPara(BillListData bdData)
    {
        try
        {
            if(SA_16 != null && SA_16.booleanValue())
            {
                if(bdData.getBodyItem("sharecode2", "cadvisecalbody") != null)
                    bdData.getBodyItem("sharecode2", "cadvisecalbody").setShow(true);
                else
                    bdData.getBodyItem("cadvisecalbody").setShow(true);
            } else
            if(bdData.getBodyItem("sharecode2", "cadvisecalbody") != null)
                bdData.getBodyItem("sharecode2", "cadvisecalbody").setShow(false);
            else
                bdData.getBodyItem("cadvisecalbody").setShow(false);
            bdData.getBodyItem("nexchangeotobrate").setDecimalDigits(4);
            bdData.getBodyItem("nexchangeotoarate").setDecimalDigits(4);
            String aryNum[] = {
                "nnumber", "ntotalreceiptnumber", "ntotalinvoicenumber", "ntotalinventorynumber", "ntotalbalancenumber", "nquoteunitnum", "narrangescornum", "narrangepoapplynum", "narrangetoornum", "norrangetoapplynum", 
                "narrangemonum", "ntotalshouldoutnum", "ntotlbalcostnum"
            };
            if(BD501 != null)
            {
                for(int i = 0; i < aryNum.length; i++)
                    bdData.getBodyItem(aryNum[i]).setDecimalDigits(BD501.intValue());

            }
            if(BD502 != null)
                bdData.getBodyItem("npacknumber").setDecimalDigits(BD502.intValue());
            if(BD505 != null)
            {
                bdData.getBodyItem("noriginalcurprice").setDecimalDigits(BD505.intValue());
                bdData.getBodyItem("noriginalcurtaxprice").setDecimalDigits(BD505.intValue());
                bdData.getBodyItem("noriginalcurnetprice").setDecimalDigits(BD505.intValue());
                bdData.getBodyItem("noriginalcurtaxnetprice").setDecimalDigits(BD505.intValue());
                bdData.getBodyItem("norgqttaxprc").setDecimalDigits(BD505.intValue());
                bdData.getBodyItem("norgqtprc").setDecimalDigits(BD505.intValue());
                bdData.getBodyItem("norgqttaxnetprc").setDecimalDigits(BD505.intValue());
                bdData.getBodyItem("norgqtnetprc").setDecimalDigits(BD505.intValue());
                bdData.getBodyItem("nprice").setDecimalDigits(BD505.intValue());
                bdData.getBodyItem("ntaxprice").setDecimalDigits(BD505.intValue());
                bdData.getBodyItem("nnetprice").setDecimalDigits(BD505.intValue());
                bdData.getBodyItem("ntaxnetprice").setDecimalDigits(BD505.intValue());
                bdData.getBodyItem("nqttaxnetprc").setDecimalDigits(BD505.intValue());
                bdData.getBodyItem("nqtnetprc").setDecimalDigits(BD505.intValue());
                bdData.getBodyItem("nqttaxprc").setDecimalDigits(BD505.intValue());
                bdData.getBodyItem("nqtprc").setDecimalDigits(BD505.intValue());
                bdData.getBodyItem("nprice").setDecimalDigits(BD505.intValue());
                bdData.getBodyItem("ntaxprice").setDecimalDigits(BD505.intValue());
                bdData.getBodyItem("nnetprice").setDecimalDigits(BD505.intValue());
                bdData.getBodyItem("ntaxnetprice").setDecimalDigits(BD505.intValue());
                bdData.getBodyItem("nassistcurtaxnetprice").setDecimalDigits(BD505.intValue());
                bdData.getBodyItem("nassistcurnetprice").setDecimalDigits(BD505.intValue());
                bdData.getBodyItem("nassistcurtaxprice").setDecimalDigits(BD505.intValue());
                bdData.getBodyItem("nassistcurprice").setDecimalDigits(BD505.intValue());
                bdData.getBodyItem("norgviaprice").setDecimalDigits(BD505.intValue());
                bdData.getBodyItem("norgviapricetax").setDecimalDigits(BD505.intValue());
            }
            if(BD503 != null)
            {
                bdData.getBodyItem("scalefactor").setDecimalDigits(BD503.intValue());
                bdData.getBodyItem("nqtscalefactor").setDecimalDigits(BD503.intValue());
                bdData.getHeadItem("npreceiverate").setDecimalDigits(BD503.intValue());
            }
        }
        catch(Exception e)
        {
            SCMEnv.out(e);
        }
        try
        {
            nc.vo.bd.def.DefVO defs[] = null;
            defs = head_defs;
            if(defs != null)
                bdData.updateItemByDef(defs, "vdef", true);
            defs = body_defs;
            if(defs != null)
                bdData.updateItemByDef(defs, "vdef", false);
        }
        catch(Throwable ex)
        {
            ex.printStackTrace(System.out);
        }
    }

    public void initIDs()
    {
        vIDs = new Vector();
        for(int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++)
            vIDs.add(getBillListPanel().getHeadBillModel().getValueAt(i, "csaleid"));

    }

    protected void switchInterface()
    {
        if(strShowState.equals("\u5217\u8868"))
        {
            remove(getSplitPanelBc());
            add(getBillListPanel(), "Center");
            setCurShowPanel(getBillListPanel());
        } else
        {
            remove(getBillListPanel());
            add(getSplitPanelBc(), "Center");
            setCurShowPanel(getSplitPanelBc());
        }
        updateUI();
    }

    public void setCurShowPanel(JComponent newCurShowPanel)
    {
        curShowPanel = newCurShowPanel;
    }

    public SOBillListPanel getBillListPanel()
    {
        if(ivjSaleOrderListPane == null)
            try
            {
                ivjSaleOrderListPane = new SOBillListPanel(this, getCorpPrimaryKey(), vocache);
            }
            catch(Throwable ivjExc)
            {
                handleException(ivjExc);
            }
        return ivjSaleOrderListPane;
    }

    protected abstract void onCard();

    protected SCMQueryConditionDlg getQueryDlg()
    {
        if(dlgQuery == null)
        {
            dlgQuery = new SOBillQueryDlg(this);
            dlgQuery.setSealedDataShow(true);
            if(getNodeCode().equals("40060301") || getNodeCode().equals("40060302"))
                dlgQuery.setTempletID(getCorpPrimaryKey(), "40060302", getClientEnvironment().getUser().getPrimaryKey(), getBillCardPanel().getBusiType());
            else
                dlgQuery.setTempletID(getCorpPrimaryKey(), getNodeCode(), getClientEnvironment().getUser().getPrimaryKey(), getBillCardPanel().getBusiType());
            dlgQuery.hideUnitButton();
            QueryConditionVO vos[] = dlgQuery.getConditionDatas();
            if(vos != null)
            {
                for(int i = 0; i < vos.length; i++)
                {
                    if(vos[i].getFieldCode().indexOf("dbilldate") >= 0)
                        vos[i].setValue(getClientEnvironment().getDate().toString());
                    if(vos[i].getTableName() != null && vos[i].getTableName().equals("bd_cumandoc") && vos[i].getTableCode().startsWith("def"))
                    {
                        vos[i].setFieldCode((new StringBuilder()).append(vos[i].getTableName()).append(".").append(vos[i].getTableCode()).toString());
                        vos[i].setReturnType(new Integer(1));
                    }
                }

            }
            m_rdoAll.setText(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000217"));
            m_rdoAll.setBackground(getBackground());
            m_rdoAll.setForeground(Color.black);
            m_rdoAll.setSize(80, m_rdoAll.getHeight());
            m_rdoAll.setSelected(true);
            m_rdoFree.setText(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000135"));
            m_rdoFree.setBackground(m_rdoAll.getBackground());
            m_rdoFree.setForeground(m_rdoAll.getForeground());
            m_rdoFree.setSize(m_rdoAll.getSize());
            m_rdoAuditing.setText(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000243"));
            m_rdoAuditing.setBackground(m_rdoAll.getBackground());
            m_rdoAuditing.setForeground(m_rdoAll.getForeground());
            m_rdoAuditing.setSize(m_rdoAll.getSize());
            m_rdoAudited.setText(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000301"));
            m_rdoAudited.setBackground(m_rdoAll.getBackground());
            m_rdoAudited.setForeground(m_rdoAll.getForeground());
            m_rdoAudited.setSize(m_rdoAll.getSize());
            m_rdoFreeze.setText(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000150"));
            m_rdoFreeze.setBackground(m_rdoAll.getBackground());
            m_rdoFreeze.setForeground(m_rdoAll.getForeground());
            m_rdoFreeze.setSize(m_rdoAll.getSize());
            m_rdoBatch.setText(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000151"));
            m_rdoBatch.setBackground(m_rdoAll.getBackground());
            m_rdoBatch.setForeground(m_rdoAll.getForeground());
            m_rdoBatch.setSize(m_rdoAll.getSize());
            m_rdoBlankOut.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000005"));
            m_rdoBlankOut.setBackground(m_rdoAll.getBackground());
            m_rdoBlankOut.setForeground(m_rdoAll.getForeground());
            m_rdoBlankOut.setSize(m_rdoAll.getSize());
            m_rdoAll.setLocation(50, 30);
            m_rdoFree.setLocation(m_rdoAll.getX(), m_rdoAll.getY() + m_rdoAll.getHeight() + 15);
            m_rdoAuditing.setLocation(m_rdoFree.getX(), m_rdoFree.getY() + m_rdoAuditing.getHeight() + 15);
            m_rdoAudited.setLocation(m_rdoAuditing.getX(), m_rdoAuditing.getY() + m_rdoAudited.getHeight() + 15);
            if(getBillType().equals("30"))
            {
                m_rdoFreeze.setLocation(m_rdoAudited.getX(), m_rdoAudited.getY() + m_rdoFreeze.getHeight() + 15);
                m_rdoBatch.setLocation(m_rdoFreeze.getX(), m_rdoFreeze.getY() + m_rdoBatch.getHeight() + 15);
                m_rdoBlankOut.setLocation(m_rdoBatch.getX(), m_rdoBatch.getY() + m_rdoBlankOut.getHeight() + 15);
            } else
            {
                m_rdoBlankOut.setLocation(m_rdoAudited.getX(), m_rdoAudited.getY() + m_rdoBlankOut.getHeight() + 15);
            }
            ButtonGroup bg = new ButtonGroup();
            bg.add(m_rdoAll);
            bg.add(m_rdoFree);
            bg.add(m_rdoAuditing);
            bg.add(m_rdoAudited);
            if(getBillType().equals("30"))
            {
                bg.add(m_rdoFreeze);
                bg.add(m_rdoBatch);
            }
            bg.add(m_rdoBlankOut);
            bg.setSelected(m_rdoAll.getModel(), true);
            dlgQuery.getUIPanelNormal().setLayout(null);
            dlgQuery.getUIPanelNormal().add(m_rdoAll);
            dlgQuery.getUIPanelNormal().add(m_rdoFree);
            dlgQuery.getUIPanelNormal().add(m_rdoAuditing);
            dlgQuery.getUIPanelNormal().add(m_rdoAudited);
            if(getBillType().equals("30"))
            {
                dlgQuery.getUIPanelNormal().add(m_rdoFreeze);
                dlgQuery.getUIPanelNormal().add(m_rdoBatch);
            }
            UIRefPane refpane = new UIRefPane();
            refpane.setRefType(2);
            refpane.setRefModel(new JobRefTreeModel("0001", getCorpPrimaryKey(), null));
            dlgQuery.setValueRef("so_saleexecute.cprojectid", refpane);
            SOBusiTypeRefPane biztypeRef = new SOBusiTypeRefPane(getCorpPrimaryKey());
            dlgQuery.setValueRef("so_sale.cbiztype", biztypeRef);
            dlgQuery.setIsWarningWithNoInput(true);
            DefSetTool.updateQueryConditionClientUserDef(dlgQuery, getCorpPrimaryKey(), getBillType(), "so_sale.vdef", "so_saleexecute.vdef");
            DefSetTool.updateQueryConditionForCumandoc(dlgQuery, getCorpPrimaryKey(), "bd_cumandoc.def");
            dlgQuery.setSealedDataShow(true);
            dlgQuery.setNormalShow(true);
            dlgQuery.setShowPrintStatusPanel(true);
            IUserManageQuery config = (IUserManageQuery)NCLocator.getInstance().lookup(nc.itf.uap.rbac.IUserManageQuery.class.getName());
            CorpVO corpVos[] = null;
            try
            {
                corpVos = config.queryAllCorpsByUserPK(ClientEnvironment.getInstance().getUser().getPrimaryKey());
            }
            catch(BusinessException e)
            {
                e.printStackTrace();
            }
            ArrayList alcorp = new ArrayList();
            if(corpVos != null && corpVos.length > 0)
            {
                for(int i = 0; i < corpVos.length; i++)
                    alcorp.add(corpVos[i].getPk_corp());

            }
            dlgQuery.setRefsDataPowerConVOs(ClientEnvironment.getInstance().getUser().getPrimaryKey(), new String[] {
                ClientEnvironment.getInstance().getCorporation().getPk_corp()
            }, new String[] {
                "\u5BA2\u6237\u6863\u6848", "\u9500\u552E\u7EC4\u7EC7", "\u90E8\u95E8\u6863\u6848", "\u4EBA\u5458\u6863\u6848", "\u5B58\u8D27\u6863\u6848"
            }, new String[] {
                "ccustomerid", "so_sale.csalecorpid", "so_sale.cdeptid", "so_sale.cemployeeid", "so_saleorder_b.cinventoryid"
            }, new int[] {
                2, 2, 2, 2, 2
            });
            ConditionVO power_cond_tmp[] = dlgQuery.getCtrTmpDataPowerVOs();
            if(!alcorp.contains(ClientEnvironment.getInstance().getCorporation().getPrimaryKey()))
                alcorp.add(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
            dlgQuery.initCorpRef("so_saleorder_b.pk_corp", ClientEnvironment.getInstance().getCorporation().getPrimaryKey(), alcorp);
            dlgQuery.setCorpRefs("so_saleorder_b.pk_corp", new String[] {
                "so_saleorder_b.cadvisecalbodyid", "so_saleorder_b.creccalbodyid", "so_saleorder_b.crecwareid", "so_saleorder_b.cbodywarehouseid"
            });
            String corps[] = new String[alcorp.size()];
            alcorp.toArray(corps);
            dlgQuery.setRefsDataPowerConVOs(ClientEnvironment.getInstance().getUser().getPrimaryKey(), corps, new String[] {
                "\u5E93\u5B58\u7EC4\u7EC7", "\u5E93\u5B58\u7EC4\u7EC7", "\u4ED3\u5E93\u6863\u6848", "\u4ED3\u5E93\u6863\u6848"
            }, new String[] {
                "so_saleorder_b.cadvisecalbodyid", "so_saleorder_b.creccalbodyid", "so_saleorder_b.crecwareid", "so_saleorder_b.cbodywarehouseid"
            }, new int[] {
                2, 2, 2, 2
            });
            ConditionVO power_cond_tmp2[] = dlgQuery.getCtrTmpDataPowerVOs();
            int len1 = power_cond_tmp != null ? power_cond_tmp.length : 0;
            int len2 = power_cond_tmp2 != null ? power_cond_tmp2.length : 0;
            if(len1 + len2 > 0)
            {
                ConditionVO all_conds[] = new ConditionVO[len1 + len2];
                if(len1 > 0)
                    System.arraycopy(power_cond_tmp, 0, all_conds, 0, len1);
                if(len2 > 0)
                    System.arraycopy(power_cond_tmp2, 0, all_conds, len1, len2);
                dlgQuery.setCtrTmpDataPowerVOs(all_conds);
            }
        }
        return dlgQuery;
    }

    public ButtonObject[] getExtendBtns()
    {
        return null;
    }

    public void onExtendBtnsClick(ButtonObject buttonobject)
    {
    }

    public void setExtendBtnsStat(int i)
    {
    }

    protected void showCustManArInfo()
    {
        getBillCardPanel().showCustManArInfo();
    }

    protected void ctlUIOnCconsignCorpChg(int row)
    {
        String pk_corp = getBillCardTools().getHeadStringValue("pk_corp");
        if(pk_corp == null)
            return;
        String cconsigncorp = getBillCardTools().getBodyStringValue(row, "cconsigncorpid");
        if(cconsigncorp == null || pk_corp.equals(cconsigncorp))
        {
            getBillCardTools().setBodyCellsEdit(new String[] {
                "creccalbody", "crecwarehouse", "bdericttrans"
            }, row, false);
            getBillCardTools().setBodyCellsEdit(new String[] {
                "boosflag", "bsupplyflag"
            }, row, true);
        } else
        {
            getBillCardTools().setBodyCellsEdit(new String[] {
                "boosflag", "bsupplyflag"
            }, row, false);
            getBillCardTools().setBodyCellsEdit(new String[] {
                "creccalbody", "crecwarehouse", "bdericttrans"
            }, row, true);
        }
    }

    protected void updateCacheVO()
    {
        if(strShowState.equals("\u5217\u8868"))
        {
            int row = getBillListPanel().getHeadTable().getSelectedRow();
            if(row < 0)
                return;
            getBillListPanel().fillCacheName();
            getBillListPanel().fillBodyCacheName();
            String sSaleid = (String)getBillListPanel().getHeadBillModel().getValueAt(row, "csaleid");
            if(sSaleid == null)
                return;
            SaleOrderVO saleorder = vocache.getSaleOrderVO(sSaleid);
            getBillListPanel().getHeadBillModel().setBodyRowVO(saleorder.getParentVO(), num);
            try
            {
                BillTools.setMnyToModelByCurrency(getBillListPanel().getHeadBillModel(), saleorder.getParentVO(), num, getCorpPrimaryKey(), saleorder.getBodyVOs()[0].getCcurrencytypeid(), new String[] {
                    "npreceivemny", "nreceiptcathmny"
                });
            }
            catch(Exception e)
            {
                handleException(e);
            }
        } else
        {
            SaleOrderVO saleorder = (SaleOrderVO)getBillCardPanel().getBillValueVO(nc.vo.so.so001.SaleOrderVO.class.getName(), nc.vo.so.so001.SaleorderHVO.class.getName(), nc.vo.so.so001.SaleorderBVO.class.getName());
            SaleOrderVO oldvo = null;
            if(saleorder.getHeadVO().getCsaleid() != null)
                oldvo = vocache.getSaleOrderVO(saleorder.getHeadVO().getCsaleid());
            SaleorderHVO hvo = (SaleorderHVO)saleorder.getParentVO();
            UIRefPane ufref = (UIRefPane)getBillCardPanel().getHeadItem("ccalbodyid").getComponent();
            hvo.setAttributeValue("ccalbodyname", ufref.getUITextField().getText());
            if(oldvo != null)
                hvo.setAttributeValue("cbiztypename", oldvo.getHeadVO().getAttributeValue("cbiztypename"));
            ufref = (UIRefPane)getBillCardPanel().getHeadItem("ccustomerid").getComponent();
            hvo.setAttributeValue("ccustomername", ufref.getUITextField().getText());
            ufref = (UIRefPane)getBillCardPanel().getHeadItem("cdeptid").getComponent();
            hvo.setAttributeValue("cdeptname", ufref.getUITextField().getText());
            ufref = (UIRefPane)getBillCardPanel().getHeadItem("cemployeeid").getComponent();
            hvo.setAttributeValue("cemployeename", ufref.getUITextField().getText());
            ufref = (UIRefPane)getBillCardPanel().getHeadItem("cfreecustid").getComponent();
            hvo.setAttributeValue("cfreecustname", ufref.getUITextField().getText());
            ufref = (UIRefPane)getBillCardPanel().getHeadItem("creceiptcorpid").getComponent();
            hvo.setAttributeValue("creceiptcorpname", ufref.getUITextField().getText());
            ufref = (UIRefPane)getBillCardPanel().getHeadItem("creceiptcustomerid").getComponent();
            hvo.setAttributeValue("creceiptcustomername", ufref.getUITextField().getText());
            ufref = (UIRefPane)getBillCardPanel().getHeadItem("csalecorpid").getComponent();
            hvo.setAttributeValue("csalecorpname", ufref.getUITextField().getText());
            ufref = (UIRefPane)getBillCardPanel().getHeadItem("ctermprotocolid").getComponent();
            hvo.setAttributeValue("ctermprotocolname", ufref.getUITextField().getText());
            ufref = (UIRefPane)getBillCardPanel().getHeadItem("ctransmodeid").getComponent();
            hvo.setAttributeValue("ctransmodename", ufref.getUITextField().getText());
            ufref = (UIRefPane)getBillCardPanel().getHeadItem("cwarehouseid").getComponent();
            hvo.setAttributeValue("cwarehousename", ufref.getUITextField().getText());
            ufref = (UIRefPane)getBillCardPanel().getHeadItem("vreceiveaddress").getComponent();
            hvo.setAttributeValue("vreceiveaddress", ufref.getUITextField().getText());
            ufref = (UIRefPane)getBillCardPanel().getTailItem("capproveid").getComponent();
            hvo.setAttributeValue("capprovename", ufref.getUITextField().getText());
            ufref = (UIRefPane)getBillCardPanel().getTailItem("coperatorid").getComponent();
            hvo.setAttributeValue("coperatorname", ufref.getUITextField().getText());
            vocache.updateSaleOrderVO(hvo.getCsaleid(), saleorder, getMustUpdateHeadNames(), getMustUpdateBodyNames());
            if(num > -1 && num < getBillListPanel().getHeadBillModel().getRowCount())
            {
                hvo = vocache.getSaleOrderVO(hvo.getCsaleid()).getHeadVO();
                getBillListPanel().getHeadBillModel().setBodyRowVO(hvo, num);
            }
        }
    }

    protected void addCacheVO()
    {
        if(!strShowState.equals("\u5217\u8868"))
        {
            SaleOrderVO saleorder = (SaleOrderVO)getBillCardPanel().getBillValueVO(nc.vo.so.so001.SaleOrderVO.class.getName(), nc.vo.so.so001.SaleorderHVO.class.getName(), nc.vo.so.so001.SaleorderBVO.class.getName());
            UIRefPane ufref = (UIRefPane)getBillCardPanel().getHeadItem("ccalbodyid").getComponent();
            saleorder.getHeadVO().setAttributeValue("ccalbodyname", ufref.getUITextField().getText());
            if(boBusiType.getSelectedChildButton() != null && boBusiType.getSelectedChildButton().length > 0 && boBusiType.getSelectedChildButton()[0] != null && boBusiType.getSelectedChildButton()[0].getCode() != null)
            {
                saleorder.getHeadVO().setAttributeValue("cbiztypename", boBusiType.getSelectedChildButton()[0].getCode());
            } else
            {
                ufref = (UIRefPane)getBillCardPanel().getHeadItem("cbiztype").getComponent();
                saleorder.getHeadVO().setAttributeValue("cbiztypename", ufref.getUITextField().getText());
            }
            ufref = (UIRefPane)getBillCardPanel().getHeadItem("ccustomerid").getComponent();
            saleorder.getHeadVO().setAttributeValue("ccustomername", ufref.getUITextField().getText());
            ufref = (UIRefPane)getBillCardPanel().getHeadItem("cdeptid").getComponent();
            saleorder.getHeadVO().setAttributeValue("cdeptname", ufref.getUITextField().getText());
            ufref = (UIRefPane)getBillCardPanel().getHeadItem("cemployeeid").getComponent();
            saleorder.getHeadVO().setAttributeValue("cemployeename", ufref.getUITextField().getText());
            ufref = (UIRefPane)getBillCardPanel().getHeadItem("cfreecustid").getComponent();
            saleorder.getHeadVO().setAttributeValue("cfreecustname", ufref.getUITextField().getText());
            ufref = (UIRefPane)getBillCardPanel().getHeadItem("creceiptcorpid").getComponent();
            saleorder.getHeadVO().setAttributeValue("creceiptcorpname", ufref.getUITextField().getText());
            ufref = (UIRefPane)getBillCardPanel().getHeadItem("creceiptcustomerid").getComponent();
            saleorder.getHeadVO().setAttributeValue("creceiptcustomername", ufref.getUITextField().getText());
            ufref = (UIRefPane)getBillCardPanel().getHeadItem("csalecorpid").getComponent();
            saleorder.getHeadVO().setAttributeValue("csalecorpname", ufref.getUITextField().getText());
            ufref = (UIRefPane)getBillCardPanel().getHeadItem("ctermprotocolid").getComponent();
            saleorder.getHeadVO().setAttributeValue("ctermprotocolname", ufref.getUITextField().getText());
            ufref = (UIRefPane)getBillCardPanel().getHeadItem("ctransmodeid").getComponent();
            saleorder.getHeadVO().setAttributeValue("ctransmodename", ufref.getUITextField().getText());
            ufref = (UIRefPane)getBillCardPanel().getHeadItem("cwarehouseid").getComponent();
            saleorder.getHeadVO().setAttributeValue("cwarehousename", ufref.getUITextField().getText());
            ufref = (UIRefPane)getBillCardPanel().getHeadItem("vreceiveaddress").getComponent();
            saleorder.getHeadVO().setAttributeValue("vreceiveaddress", ufref.getUITextField().getText());
            ufref = (UIRefPane)getBillCardPanel().getTailItem("capproveid").getComponent();
            saleorder.getHeadVO().setAttributeValue("capprovename", ufref.getUITextField().getText());
            ufref = (UIRefPane)getBillCardPanel().getTailItem("coperatorid").getComponent();
            saleorder.getHeadVO().setAttributeValue("coperatorname", ufref.getUITextField().getText());
            vocache.addSaleOrderVO(saleorder);
            getBillListPanel().getHeadBillModel().addLine();
            getBillListPanel().getHeadBillModel().setBodyRowVO(saleorder.getParentVO(), getBillListPanel().getHeadBillModel().getRowCount() - 1);
            int iselrow = getBillListPanel().getHeadBillModel().getRowCount() - 1;
            try
            {
                BillTools.setMnyToModelByCurrency(getBillListPanel().getHeadBillModel(), saleorder.getParentVO(), iselrow, getCorpPrimaryKey(), saleorder.getBodyVOs()[0].getCcurrencytypeid(), new String[] {
                    "npreceivemny", "nreceiptcathmny"
                });
            }
            catch(Exception e)
            {
                handleException(e);
            }
            getBillListPanel().getHeadTable().getSelectionModel().setSelectionInterval(iselrow, iselrow);
        }
    }

    private ArrayList getMustUpdateHeadNames()
    {
        String heads[] = {
            "fstatus", "breceiptendflag", "boutendflag", "binvoicendflag", "ibalanceflag", "bpayendflag", "btransendflag", "ts", "capproveid", "dapprovedate"
        };
        ArrayList al = new ArrayList();
        for(int i = 0; i < heads.length; i++)
            al.add(heads[i]);

        return al;
    }

    private ArrayList getMustUpdateBodyNames()
    {
        String heads[] = {
            "csaleid", "corder_bid", "frowstatus", "ts", "bifinvoicefinish", "bifpaybalance", "bifpayfinish", "bifreceiptfinish", "bifinventoryfinish", "bifpaysign", 
            "biftransfinish", "dlastconsigdate", "dlasttransdate", "dlastoutdate", "dlastinvoicedt", "dlastpaydate", "ntotalreturnnumber", "ntotalcarrynumber", "ntaltransnum", "ntaltransretnum", 
            "ntranslossnum", "ntotalplanreceiptnumber", "ntotalreceiptnumber", "ntotalinvoicenumber", "ntotalbalancenumber", "ntotalpaymny", "ntotalinventorynumber", "ntotalsignnumber", "ntotalcostmny", "narrangescornum", 
            "narrangepoapplynum", "narrangetoornum", "norrangetoapplynum", "ntotlbalcostnum", "barrangedflag", "carrangepersonid", "narrangemonum", "ts"
        };
        ArrayList al = new ArrayList();
        for(int i = 0; i < heads.length; i++)
            al.add(heads[i]);

        return al;
    }

    public BillListTools getBillListTools()
    {
        if(listtools == null)
            listtools = new BillListTools(getBillListPanel().getBillListData());
        return listtools;
    }

    protected ButtonObject getBoDocument()
    {
        if(boDocument == null)
        {
            boDocument = new ButtonObject(NCLangRes.getInstance().getStrByID("40060301", "UPT40060301-000026"), NCLangRes.getInstance().getStrByID("40060301", "UPT40060301-000026"), "\u6587\u6863\u7BA1\u7406");
            boDocument.setTag("document");
        }
        return boDocument;
    }

    protected ButtonObject getBoRefundment()
    {
        if(boRefundment == null)
            boRefundment = new ButtonObject(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000151"), NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000151"), "\u9000\u8D27");
        return boRefundment;
    }

    protected ButtonObject getBoCard()
    {
        if(boCard == null)
            boCard = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UCH021"), NCLangRes.getInstance().getStrByID("common", "UCH021"), 2, "\u5361\u7247\u663E\u793A");
        return boCard;
    }

    protected ButtonObject getBoBatch()
    {
        if(boBatch == null)
            boBatch = new ButtonObject(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000151"), NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000151"), "\u9000\u8D27");
        return boBatch;
    }

    protected ButtonObject getBoPreview()
    {
        if(boPreview == null)
            boPreview = new ButtonObject(NCLangRes.getInstance().getStrByID("40060301", "UPT40060301-000032"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000306"), 0, "\u9884\u89C8");
        return boPreview;
    }

    protected ButtonObject getBoPrint()
    {
        if(boPrint == null)
            boPrint = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000169"), 0, "\u6253\u5370");
        return boPrint;
    }

    protected ButtonObject getBoEdit()
    {
        if(boEdit == null)
            boEdit = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000045"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000291"), 2, "\u4FEE\u6539");
        return boEdit;
    }

    protected ButtonObject getBoOrderQuery()
    {
        if(boOrderQuery == null)
            boOrderQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("40060301", "UPT40060301-000033"), NCLangRes.getInstance().getStrByID("40060301", "UPT40060301-000033"), 0, "\u8054\u67E5");
        return boOrderQuery;
    }

    protected ButtonObject getBoSendAudit()
    {
        if(boSendAudit == null)
            boSendAudit = new ButtonObject(NCLangRes.getInstance().getStrByID("40060301", "UPT40060301-000034"), NCLangRes.getInstance().getStrByID("40060301", "UPT40060301-000034"), 0, "\u9001\u5BA1");
        return boSendAudit;
    }

    protected ButtonObject getBoAuditFlowStatus()
    {
        if(boAuditFlowStatus == null)
        {
            boAuditFlowStatus = new ButtonObject(NCLangRes.getInstance().getStrByID("40060301", "UPT40060301-000025"), NCLangRes.getInstance().getStrByID("40060301", "UPT40060301-000025"), 0);
            boAuditFlowStatus.setTag("auditflowstatus");
        }
        return boAuditFlowStatus;
    }

    protected ButtonObject getBoCachPay()
    {
        if(boCachPay == null)
            boCachPay = new ButtonObject(NCLangRes.getInstance().getStrByID("40060301", "UPT40060301-000028"), NCLangRes.getInstance().getStrByID("40060301", "UPT40060301-000028"), "\u8BA2\u5355\u6536\u6B3E");
        return boCachPay;
    }

    protected ButtonObject getBoListDeselectAll()
    {
        if(boListDeselectAll == null)
            boListDeselectAll = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000042"), NCLangRes.getInstance().getStrByID("common", "UC001-0000042"), 2, "\u5168\u6D88");
        return boListDeselectAll;
    }

    protected ButtonObject getBoListSelectAll()
    {
        if(boListSelectAll == null)
            boListSelectAll = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000041"), NCLangRes.getInstance().getStrByID("common", "UC001-0000041"), 2, "\u5168\u9009");
        return boListSelectAll;
    }

    protected ButtonObject getBoBrowse()
    {
        if(boBrowse == null)
            boBrowse = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000021"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000148"), 1, "\u6D4F\u89C8");
        return boBrowse;
    }

    protected ButtonObject getBoFind()
    {
        if(boFind == null)
            boFind = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000089"), NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000168"), 0, "\u5B9A\u4F4D");
        return boFind;
    }

    protected ButtonObject getBoQuery()
    {
        if(boQuery == null)
            boQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000034"), 0, "\u67E5\u8BE2");
        return boQuery;
    }

    protected ButtonObject getBoMaintain()
    {
        if(boMaintain == null)
            boMaintain = new ButtonObject(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000538"), NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000538"), "\u7EF4\u62A4");
        return boMaintain;
    }

    protected ButtonObject getBoCancel()
    {
        if(boCancel == null)
            boCancel = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000008"), NCLangRes.getInstance().getStrByID("common", "UC001-0000008"), 0, "\u53D6\u6D88");
        return boCancel;
    }

    protected ButtonObject getBoLine()
    {
        if(boLine == null)
            boLine = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000011"), NCLangRes.getInstance().getStrByID("common", "UC001-0000011"), 0, "\u884C\u64CD\u4F5C");
        return boLine;
    }

    protected ButtonObject getBoAddLine()
    {
        if(boAddLine == null)
            boAddLine = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000012"), NCLangRes.getInstance().getStrByID("common", "UC001-0000012"), 0, "\u589E\u884C");
        return boAddLine;
    }

    protected ButtonObject getBoDelLine()
    {
        if(boDelLine == null)
            boDelLine = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000013"), NCLangRes.getInstance().getStrByID("common", "UC001-0000013"), 0, "\u5220\u884C");
        return boDelLine;
    }

    protected ButtonObject getBoInsertLine()
    {
        if(boInsertLine == null)
            boInsertLine = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000016"), NCLangRes.getInstance().getStrByID("common", "UC001-0000016"), "\u63D2\u5165\u884C");
        return boInsertLine;
    }

    protected ButtonObject getBoCopyLine()
    {
        if(boCopyLine == null)
            boCopyLine = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000014"), NCLangRes.getInstance().getStrByID("common", "UC001-0000014"), 0, "\u590D\u5236\u884C");
        return boCopyLine;
    }

    protected ButtonObject getBoPasteLine()
    {
        if(boPasteLine == null)
            boPasteLine = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000015"), NCLangRes.getInstance().getStrByID("common", "UC001-0000015"), 0, "\u7C98\u8D34\u884C");
        return boPasteLine;
    }

    protected ButtonObject getBoBlankOut()
    {
        if(boBlankOut == null)
        {
            boBlankOut = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000039"), NCLangRes.getInstance().getStrByID("common", "UC001-0000039"), 3, "\u5220\u9664");
            boBlankOut.setTag("SoBlankout");
        }
        return boBlankOut;
    }

    protected ButtonObject getBoCopyBill()
    {
        if(boCopyBill == null)
            boCopyBill = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000043"), NCLangRes.getInstance().getStrByID("common", "UC001-0000043"), 0, "\u590D\u5236");
        return boCopyBill;
    }

    protected ButtonObject getBoAction()
    {
        if(boAction == null)
            boAction = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000026"), NCLangRes.getInstance().getStrByID("common", "UC001-0000026"), 0, "\u6267\u884C");
        return boAction;
    }

    protected ButtonObject getBoAudit()
    {
        if(boAudit == null)
        {
            boAudit = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000027"), NCLangRes.getInstance().getStrByID("common", "UC001-0000027"), 0, "\u5BA1\u6279");
            boAudit.setTag("APPROVE");
        }
        return boAudit;
    }

    protected ButtonObject getBoCancelAudit()
    {
        if(boCancelAudit == null)
        {
            boCancelAudit = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000028"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000214"), 0, "\u5F03\u5BA1");
            boCancelAudit.setTag("SoUnApprove");
        }
        return boCancelAudit;
    }

    protected ButtonObject getBoModification()
    {
        if(boModification == null)
            boModification = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000290"), NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000159"), 3, "\u4FEE\u8BA2");
        return boModification;
    }

    protected ButtonObject getBoCancelFreeze()
    {
        if(boCancelFreeze == null)
        {
            boCancelFreeze = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000031"), NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000156"), 0, "\u89E3\u51BB");
            boCancelFreeze.setTag("OrderUnFreeze");
        }
        return boCancelFreeze;
    }

    protected ButtonObject getBoFreeze()
    {
        if(boFreeze == null)
        {
            boFreeze = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000030"), NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000155"), 0, "\u51BB\u7ED3");
            boFreeze.setTag("OrderFreeze");
        }
        return boFreeze;
    }

    protected ButtonObject getBoFinish()
    {
        if(boFinish == null)
        {
            boFinish = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000119"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000119"), 0, "\u5173\u95ED");
            boFinish.setTag("OrderFinish");
        }
        return boFinish;
    }

    protected ButtonObject getBoRefresh()
    {
        if(boRefresh == null)
            boRefresh = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000009"), NCLangRes.getInstance().getStrByID("common", "UC001-0000009"), "\u5237\u65B0");
        return boRefresh;
    }

    protected ButtonObject getBoFirst()
    {
        if(boFirst == null)
            boFirst = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UCH031"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000088"), 0, "\u9996\u9875");
        return boFirst;
    }

    protected ButtonObject getBoPre()
    {
        if(boPre == null)
            boPre = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "SCMCOMMON000000163"), NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000166"), 0, "\u4E0A\u9875");
        return boPre;
    }

    protected ButtonObject getBoNext()
    {
        if(boNext == null)
            boNext = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000023"), NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000167"), 0, "\u4E0B\u9875");
        return boNext;
    }

    protected ButtonObject getBoLast()
    {
        if(boLast == null)
            boLast = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000062"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000342"), 0, "\u672B\u9875");
        return boLast;
    }

    protected ButtonObject getBoAssistant()
    {
        if(boAssistant == null)
            boAssistant = new ButtonObject(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000160"), NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000160"), "\u8F85\u52A9\u529F\u80FD");
        return boAssistant;
    }

    protected ButtonObject getBoOrdBalance()
    {
        if(boOrdBalance == null)
            boOrdBalance = new ButtonObject(NCLangRes.getInstance().getStrByID("40060301", "UPT40060301-000030"), NCLangRes.getInstance().getStrByID("40060301", "UPT40060301-000030"), "\u6536\u6B3E\u6838\u9500");
        return boOrdBalance;
    }

    protected ButtonObject getBoStockLock()
    {
        if(boStockLock == null)
        {
            boStockLock = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC000-0001824"), NCLangRes.getInstance().getStrByID("common", "UC000-0001824"), "\u5E93\u5B58\u786C\u9501\u5B9A");
            boStockLock.setTag("StockLock");
        }
        return boStockLock;
    }

    protected ButtonObject getBoAsstntQry()
    {
        if(boAsstntQry == null)
            boAsstntQry = new ButtonObject(NCLangRes.getInstance().getStrByID("40060301", "UPT40060301-000056"), NCLangRes.getInstance().getStrByID("40060301", "UPT40060301-000056"), "\u8F85\u52A9\u67E5\u8BE2");
        return boAsstntQry;
    }

    protected ButtonObject getBoOnHandShowHidden()
    {
        if(boOnHandShowHidden == null)
            boOnHandShowHidden = new ButtonObject(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000509"), NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000509"), 2, "\u5B58\u91CF\u663E\u793A/\u9690\u85CF");
        return boOnHandShowHidden;
    }

    protected ButtonObject getBoCustCredit()
    {
        if(boCustCredit == null)
        {
            boCustCredit = new ButtonObject(NCLangRes.getInstance().getStrByID("40060301", "UPT40060301-000035"), NCLangRes.getInstance().getStrByID("40060301", "UPT40060301-000035"), 0, "\u5BA2\u6237\u4FE1\u7528");
            boCustCredit.setTag("CustCredited");
        }
        return boCustCredit;
    }

    protected ButtonObject getBoOrderExecRpt()
    {
        if(boOrderExecRpt == null)
        {
            boOrderExecRpt = new ButtonObject(NCLangRes.getInstance().getStrByID("40060302", "UPT40060302-000040"), NCLangRes.getInstance().getStrByID("40060302", "UPT40060302-000040"), 0, "\u8BA2\u5355\u6267\u884C\u60C5\u51B5");
            boOrderExecRpt.setTag("OrderExecRpt");
        }
        return boOrderExecRpt;
    }

    protected ButtonObject getBoCustInfo()
    {
        if(boCustInfo == null)
        {
            boCustInfo = new ButtonObject(NCLangRes.getInstance().getStrByID("40060302", "UPT40060302-000041"), NCLangRes.getInstance().getStrByID("40060302", "UPT40060302-000041"), 0, "\u5BA2\u6237\u4FE1\u606F");
            boCustInfo.setTag("CustInfo");
        }
        return boCustInfo;
    }

    protected ButtonObject getBoPrifit()
    {
        if(boPrifit == null)
        {
            boPrifit = new ButtonObject(NCLangRes.getInstance().getStrByID("40060302", "UPT40060302-000042"), NCLangRes.getInstance().getStrByID("40060302", "UPT40060302-000042"), 0, "\u6BDB\u5229\u9884\u4F30");
            boPrifit.setTag("Prifit");
        }
        return boPrifit;
    }

    protected ButtonObject getBoPrintMgr()
    {
        if(boPrntMgr == null)
            boPrntMgr = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "SCMCOMMON000000225"), NCLangRes.getInstance().getStrByID("common", "SCMCOMMON000000225"), "\u6253\u5370\u7BA1\u7406");
        return boPrntMgr;
    }

    protected ButtonObject getBoAdd()
    {
        if(boAdd == null)
            boAdd = new ButtonObject(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000287"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000288"), 1, "\u65B0\u589E");
        return boAdd;
    }

    protected ButtonObject getBoSave()
    {
        if(boSave == null)
            boSave = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000001"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000008"), 3, "\u4FDD\u5B58");
        return boSave;
    }

    protected ButtonObject getBoReturn()
    {
        if(boReturn == null)
            boReturn = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UCH022"), NCLangRes.getInstance().getStrByID("common", "UCH022"), 0, "\u5217\u8868\u663E\u793A");
        return boReturn;
    }

    protected ButtonObject getBoBack()
    {
        if(boBack == null)
            boBack = new ButtonObject("\u8FD4\u56DE", "\u8FD4\u56DE", "\u8FD4\u56DE");
        return boBack;
    }

    
    protected ButtonObject boImport;//add by zwx 2014-9-1
    private SOBillListPanel ivjSaleOrderListPane;
    private BillListTools listtools;
    protected UIRadioButton m_rdoAll;
    protected UIRadioButton m_rdoFree;
    protected UIRadioButton m_rdoAudited;
    protected UIRadioButton m_rdoFreeze;
    protected UIRadioButton m_rdoBatch;
    protected UIRadioButton m_rdoBlankOut;
    protected UIRadioButton m_rdoAuditing;
    protected ButtonObject aryBatchButtonGroup[];
    protected ButtonObject aryButtonGroup[];
    protected ButtonObject aryListButtonGroup[];
    protected ButtonObject boAdd;
    protected ButtonObject boFirst;
    protected ButtonObject boPre;
    protected ButtonObject boNext;
    protected ButtonObject boLast;
    protected ButtonObject boEdit;
    protected ButtonObject boSave;
    protected ButtonObject boCancel;
    protected ButtonObject boSendAudit;
    protected ButtonObject boOrdPay;
    protected ButtonObject boCachPay;
    public ButtonObject boAction;
    protected ButtonObject boLine;
    protected ButtonObject boPreview;
    protected ButtonObject boPrint;
    public ButtonObject boAssistant;
    protected ButtonObject boDocument;
    protected ButtonObject boReturn;
    protected ButtonObject boAuditFlowStatus;
    protected ButtonObject boOrderQuery;
    protected ButtonObject boBatch;
    protected ButtonObject boBrowse;
    protected ButtonObject boListSelectAll;
    protected ButtonObject boListDeselectAll;
    protected ButtonObject boCard;
    protected ButtonObject boRefundment;
    protected ButtonObject boFind;
    protected ButtonObject boQuery;
    protected ButtonObject boMaintain;
    protected ButtonObject boBlankOut;
    protected ButtonObject boCopyBill;
    protected ButtonObject boAudit;
    protected ButtonObject boCancelAudit;
    protected ButtonObject boModification;
    protected ButtonObject boCancelFreeze;
    protected ButtonObject boFreeze;
    protected ButtonObject boFinish;
    protected ButtonObject boRefresh;
    protected ButtonObject boOrdBalance;
    protected ButtonObject boAsstntQry;
    protected ButtonObject boOnHandShowHidden;
    protected ButtonObject boCustCredit;
    protected ButtonObject boOrderExecRpt;
    protected ButtonObject boCustInfo;
    protected ButtonObject boPrifit;
    protected ButtonObject boPrntMgr;
    protected ButtonObject boBack;
    private SCMQueryConditionDlg dlgQuery;
    public String strShowState;
    protected int selectRow;
    protected int num;
    protected Vector vIDs;
    protected JComponent curShowPanel;
    protected ArrayList alInvs;
}


/*
	DECOMPILATION REPORT

	Decompiled from: F:\nchome_bao\modules\so\client\classes/nc/ui/so/so001/panel/list/SaleBillListUI.class
	Total time: 90 ms
	Jad reported messages/errors:
The class file version is 49.0 (only 45.3, 46.0 and 47.0 are supported)
	Exit status: 0
	Caught exceptions:
*/