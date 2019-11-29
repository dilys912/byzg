/*jadclipse*/// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10) 
// Source File Name:   ClientUI.java

package nc.ui.mo.mo1040;

import java.awt.BorderLayout;
import java.io.PrintStream;
import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.ml.NCLangRes;
import nc.ui.mm.pub.MMToftPanel;
import nc.ui.mo.mo1020.BasePanel;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillEditEvent;
import nc.vo.pub.BusinessException;

public class ClientUI extends MMToftPanel
{

    @SuppressWarnings("unchecked")
	public ClientUI()
    {
        basePanel = null;
        m_boQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("50081040", "UC001-0000006"), NCLangRes.getInstance().getStrByID("50081040", "UPP50081040-000053"), 2, "\u67E5\u8BE2");
        m_boRefresh = new ButtonObject(NCLangRes.getInstance().getStrByID("50081040", "UC001-0000009"), NCLangRes.getInstance().getStrByID("50081040", "UPP50081040-000054"), 2, "\u5237\u65B0");
        m_boFinish = new ButtonObject(NCLangRes.getInstance().getStrByID("50081040", "UPT50081040-000001"), NCLangRes.getInstance().getStrByID("50081040", "UPP50081040-000055"), 2, "\u5F3A\u5236\u5B8C\u5DE5");
        m_boUnFinish = new ButtonObject(NCLangRes.getInstance().getStrByID("50081040", "UPT50081040-000002"), NCLangRes.getInstance().getStrByID("50081040", "UPP50081040-000056"), 2, "\u53D6\u6D88\u5B8C\u5DE5");
        m_boOver = new ButtonObject(NCLangRes.getInstance().getStrByID("50081040", "UPT50081040-000003"), NCLangRes.getInstance().getStrByID("50081040", "UPP50081040-000057"), 2, "\u7ED3\u675F\u786E\u8BA4");
        m_boUnOver = new ButtonObject(NCLangRes.getInstance().getStrByID("50081040", "UPT50081040-000004"), NCLangRes.getInstance().getStrByID("50081040", "UPP50081040-000055"), 2, "\u53D6\u6D88\u7ED3\u675F");
        String userid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
        StringBuffer sqlsb=new StringBuffer();
        sqlsb.append(" select ro.pk_role from sm_role ro ") 
        .append("        inner join sm_user_role ur on ro.pk_role=ur.pk_role ") 
        .append("        where ro.dr=0 ") 
        .append("        and ur.cuserid='"+userid+"' ") 
        .append("        and ro.role_code='ZG_DDWGWH_QXAN' ") ;
        IUAPQueryBS query = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        try {
			ArrayList<String> pk_roles = (ArrayList<String>) query.executeQuery(sqlsb.toString(), new MapListProcessor());
            if(pk_roles!=null&&pk_roles.size()>0){
	        	m_aryButtons = (new ButtonObject[] {
	        			m_boQuery, m_boFinish, m_boUnFinish, m_boOver, m_boUnOver, m_boRefresh
	        	});
	        }else{
	        	m_aryButtons = (new ButtonObject[] {
	        			m_boQuery, m_boFinish, m_boUnFinish, m_boOver, m_boRefresh
	        	});
	        	
	        }
		} catch (BusinessException e) {
			e.printStackTrace();
		}
        initialize();
    }

    public void afterEdit(BillEditEvent billeditevent)
    {
    }

    private BasePanel getBasePanel()
    {
        if(basePanel == null)
            basePanel = new BasePanel(this);
        return basePanel;
    }

    public String getTitle()
    {
        return NCLangRes.getInstance().getStrByID("50081040", "UPP50081040-000058");
    }

    private void handleException(Throwable exception)
    {
        System.out.println("--------- \u672A\u6355\u6349\u5230\u7684\u5F02\u5E38 ---------");
        exception.printStackTrace(System.out);
    }

    private void initialize()
    {
        try
        {
            setSize(774, 419);
            setLayout(new BorderLayout());
            add(getBasePanel(), "Center");
        }
        catch(Exception ex)
        {
            handleException(ex);
        }
    }

    public void onButtonClicked(ButtonObject bo)
    {
        if(bo == m_boQuery)
        {
            getBasePanel().onQuery();
            return;
        }
        if(bo == m_boFinish)
        {
            getBasePanel().onFinish();
            return;
        }
        if(bo == m_boUnFinish)
        {
            getBasePanel().onUnFinish();
            return;
        }
        if(bo == m_boOver)
        {
            getBasePanel().onOver();
            return;
        }
        if(bo == m_boUnOver)
        {
            getBasePanel().onUnOver();
            return;
        }
        if(bo == m_boRefresh)
            getBasePanel().onRefresh();
    }

    public void onHelp()
    {
    }

    public void onPrint()
    {
    }

    public boolean onRefresh()
    {
        return false;
    }

    public void setState(int st, String p0)
    {
        switch(st)
        {
        case 0: // '\0'
            m_boQuery.setEnabled(true);
            m_boRefresh.setEnabled(false);
            m_boFinish.setEnabled(false);
            m_boUnFinish.setEnabled(false);
            m_boOver.setEnabled(false);
            m_boUnOver.setEnabled(false);
            setButtons(m_aryButtons);
            break;

        default:
            for(int i = 0; i < m_aryButtons.length; i++)
            {
                m_aryButtons[i].setEnabled(true);
                updateButtons();
            }

            break;
        }
    }

    private BasePanel basePanel;
    private ButtonObject m_boQuery;
    private ButtonObject m_boRefresh;
    private ButtonObject m_boFinish;
    private ButtonObject m_boUnFinish;
    private ButtonObject m_boOver;
    private ButtonObject m_boUnOver;
    private ButtonObject m_aryButtons[];
}
