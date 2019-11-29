/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package nc.ui.rc.receive;

import java.awt.Color;
import java.awt.Container;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.*;
import nc.ui.pub.pf.IinitQueryData;
import nc.ui.rc.pub.*;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.vo.bd.CorpVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pub.SCMEnv;

public class ArrFrmOrdQueDLG extends SCMQueryConditionDlg
    implements IinitQueryData
{

    public ArrFrmOrdQueDLG(Container parent)
    {
        super(parent);
        m_busitype = null;
        m_operator = null;
        m_pkCorp = null;
        m_rdoOk = null;
        m_rdoNo = null;
        m_sourcebilltype = null;
        sCurBillType = null;
        sSrcBillType = null;
    }

    public ArrFrmOrdQueDLG(Container parent, String pk_corp, String operator, String nodecode, String strBizType, String strCurrBillType, String strBillType, 
            Object obUser)
    {
        super(parent);
        m_busitype = null;
        m_operator = null;
        m_pkCorp = null;
        m_rdoOk = null;
        m_rdoNo = null;
        m_sourcebilltype = null;
        sCurBillType = null;
        sSrcBillType = null;
        setBillType(strBillType);
        m_operator = operator;
        m_pkCorp = pk_corp;
        m_sourcebilltype = strBillType;
        m_busitype = strBizType;
        try
        {
            initData(pk_corp, operator, nodecode, strBizType, strCurrBillType, strBillType, obUser);
        }
        catch(Exception e)
        {
            SCMEnv.out(e);
        }
    }

    public void closeOK()
    {
        super.closeOK();
    }

    public Integer getArrType()
    {
        Integer iRet = new Integer(1);
        if(m_rdoOk != null && m_rdoOk.isSelected())
            iRet = new Integer(0);
        return iRet;
    }

    private String getCorpId()
    {
        return ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
    }

    public String getWhereSQL()
    {
        String strWhereSql = super.getWhereSQL();
        strWhereSql = CPurchseMethods.getLikeStr(strWhereSql);
        if(strWhereSql == null)
            strWhereSql = "thissqlisnull";
        strWhereSql = (new StringBuilder()).append(strWhereSql).append("#").toString();
        strWhereSql = (new StringBuilder()).append(strWhereSql).append(getArrType()).toString();
        return strWhereSql;
    }

    public void initData(String pkCorp, String operator, String funNode, String businessType, String currentBillType, String sourceBilltype, Object userObj)
        throws Exception
    {
        sCurBillType = currentBillType;
        sSrcBillType = sourceBilltype;
        m_operator = operator;
        m_pkCorp = pkCorp;
        m_sourcebilltype = sourceBilltype;
        m_busitype = businessType;
        initiNormal();
        initiDefine();
        initiTitle();
        setIsWarningWithNoInput(true);
        setShowPrintStatusPanel(true);
    }

    private void initiDefine()
        throws Exception
    {
        String strFunCode = null;
        String strNodeKey = null;
        if(m_sourcebilltype.equals("21"))
        {
            strFunCode = "40040301";
            strNodeKey = "40041002";
        } else
        {
            strFunCode = "401201";
            strNodeKey = "40041003";
        }
        setTempletID(m_pkCorp, strFunCode, m_operator, m_busitype, strNodeKey);
        if(sSrcBillType != null && sSrcBillType.trim().equals("61"))
        {
            DefSetTool.updateQueryConditionClientUserDef(this, getCorpId(), "61", "sc_order.vdef", "sc_order_b.vdef");
            setValueRef("sc_order.dorderdate", "\u65E5\u5386");
            setDefaultValue("sc_order.dorderdate", "sc_order.dorderdate", ClientEnvironment.getInstance().getDate().toString());
            initCorpRefsDataPower("po_order.pk_corp", new String[] {
                "bd_invbasdoc.invcode", "bd_cubasdoc.custcode"
            }, new String[] {
                ClientEnvironment.getInstance().getCorporation().getPrimaryKey()
            });
        } else
        {
            DefSetTool.updateQueryConditionClientUserDef(this, getCorpId(), "21", "po_order.vdef", "po_order_b.vdef");
            setValueRef("po_order.dorderdate", "\u65E5\u5386");
            setDefaultValue("po_order.dorderdate", "po_order.dorderdate", ClientEnvironment.getInstance().getDate().toString());
        }
        UIRefPane refInvcode = new UIRefPane();
        refInvcode.setRefModel(new InvCodeRefModelForArr(getCorpId()));
        setValueRef("bd_invbasdoc.invcode", refInvcode);
        UIRefPane refVendor = new UIRefPane();
        refVendor.setRefModel(new CustRefModelForArr(getCorpId()));
        setValueRef("bd_cubasdoc.custcode", refVendor);
    }

    public void initCorpRefsDataPower(String strCorpKey, String saRefDocKey[], String saCorpId[])
    {
        ArrayList alcorp = new ArrayList();
        if(saCorpId == null)
        {
            alcorp.add(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
        } else
        {
            for(int i = 0; i < saCorpId.length; i++)
                alcorp.add(saCorpId[i]);

        }
        initCorpRef(strCorpKey, ClientEnvironment.getInstance().getCorporation().getPrimaryKey(), alcorp);
        setCorpRefs(strCorpKey, saRefDocKey);
    }

    private void initiNormal()
        throws Exception
    {
        if(sSrcBillType.trim().equals("61"))
        {
            hideNormal();
            return;
        } else
        {
            m_rdoOk = new UIRadioButton();
            m_rdoOk.setText(NCLangRes.getInstance().getStrByID("40040301", "UPP40040301-000207"));
            m_rdoOk.setBackground(getUIPanelNormal().getBackground());
            m_rdoOk.setForeground(Color.black);
            m_rdoOk.setSize(80, m_rdoOk.getHeight());
            m_rdoOk.setSelected(true);
            m_rdoNo = new UIRadioButton();
            m_rdoNo.setText(NCLangRes.getInstance().getStrByID("40040301", "UPT40040301-000025"));
            m_rdoNo.setBackground(m_rdoOk.getBackground());
            m_rdoNo.setForeground(m_rdoOk.getForeground());
            m_rdoNo.setSize(m_rdoOk.getSize());
            m_rdoOk.setLocation(50, 30);
            m_rdoNo.setLocation(m_rdoOk.getX(), m_rdoOk.getY() + m_rdoOk.getHeight() + 20);
            ButtonGroup bg = new ButtonGroup();
            bg.add(m_rdoOk);
            bg.add(m_rdoNo);
            bg.setSelected(m_rdoOk.getModel(), true);
            getUIPanelNormal().setLayout(null);
            getUIPanelNormal().add(m_rdoOk);
            getUIPanelNormal().add(m_rdoNo);
            return;
        }
    }

    private void initiTitle()
    {
        setTitle(NCLangRes.getInstance().getStrByID("40040301", "UPP40040301-000208"));
    }

    private void setBillType(String newBizType)
    {
        sCurBillType = newBizType;
    }

    String m_busitype;
    String m_operator;
    String m_pkCorp;
    UIRadioButton m_rdoOk;
    UIRadioButton m_rdoNo;
    String m_sourcebilltype;
    String sCurBillType;
    String sSrcBillType;
}


/*
	DECOMPILATION REPORT

	Decompiled from: D:\nchome_byzg\modules\pu\client\classes/nc/ui/rc/receive/ArrFrmOrdQueDLG.class
	Total time: 40 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/