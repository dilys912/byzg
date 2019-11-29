
package nc.ui.ic.pub.pf;

import java.awt.Container;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.ComponentException;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.ic.pub.ICCommonBusi;
import nc.ui.ic.pub.bill.GeneralBillHelper;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.pf.BillSourceDLG;
import nc.ui.pub.pf.PfUtilBO_Client;
import nc.ui.pub.tools.BannerDialog;
import nc.ui.scm.pub.def.DefSetTool;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.para.SysInitException;
import nc.vo.scm.pub.SCMEnv;

@SuppressWarnings("serial")
public class QrySaleBillForInvoiceDlg extends BillSourceDLG
{
	//接收每个出库单据号及未开票数
	HashMap<String, Object> map = new HashMap<String, Object>();
    public QrySaleBillForInvoiceDlg(String pkField, String pkCorp, String operator, String funNode, String queryWhere, String billType, String businessType, 
            String templateId, String currentBillType, Container parent)
    {
        super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId, currentBillType, parent);
        m_iaScale = null;
        listPanel = null;
    }
    
    /*
     * add 未开票数量
     * wkf
     * 2014-03-25
     * start
     * */
    
    @Override
    public void loadHeadData() {

		try {
			//利用产品组传入的条件与当前查询条件获得条件组成主表查询条件
			String tmpWhere = null;
			if (getHeadCondition() != null) {
				if (m_whereStr == null) {
					tmpWhere = " (" + getHeadCondition() + ")";
				} else {
					tmpWhere = " (" + m_whereStr + ") and (" + getHeadCondition() + ")";
				}
			} else {
				tmpWhere = m_whereStr;
			}
			String businessType = null;
			if (getIsBusinessType()) {
				businessType = getBusinessType();
			}
			CircularlyAccessibleValueObject[] tmpHeadVo = PfUtilBO_Client.queryHeadAllData(getBillType(),
					businessType, tmpWhere);

			getbillListPanel().setHeaderValueVO(tmpHeadVo);
			getbillListPanel().getHeadBillModel().execLoadFormula();
			if (tmpHeadVo==null) {
				return;
			}
			StringBuffer sb = new StringBuffer();
			for (int i = 0,len=tmpHeadVo.length; i < len; i++) {
				sb.append("'");
				sb.append(((GeneralBillHeaderVO)tmpHeadVo[i]).getVbillcode());
				sb.append("'");
				if(i!=len-1){
				sb.append(",");
				}
			}
			/*
			String sql="select h.vbillcode,nvl(h.ggfys,0)-" +
					"nvl(sb.ggsl,0) ggsl,nvl(h.gzfys,0)-nvl(sb.gzsl,0) gzsl " +
					"from ic_general_h h left join " +
					"( select cupsourcebillid,substr(invcode , 0, 2), " +
					"case substr(invcode , 0, 2) when '23' then sum(nvl(sb.nnumber,0)) " +
					"else 0 end gzsl, case substr(invcode , 0, 2) " +
					"when '23' then 0 else sum(nvl(sb.nnumber,0)) end ggsl " +
					"from so_saleinvoice_b sb left join bd_invbasdoc inv " +
					"on sb.cinvbasdocid=inv.pk_invbasdoc   " +
					"where nvl(sb.dr,0)=0 and substr(invcode , 0, 2) in ('21','22','23') " +
					"group by cupsourcebillid,substr(invcode , 0, 2)) sb " +
					"on h.cgeneralhid=sb.cupsourcebillid where h.vbillcode in " +
					"("+sb.toString()+") and nvl(h.dr,0)=0 and h.pk_corp='"+getPkCorp()+"'";
			*/
			StringBuffer sss =  new StringBuffer();
			sss.append(" SELECT head.vbillcode, sum(case substr(inv.invcode, 0, 2)  ") 
			.append("                                 when '23' then  ") 
			.append("                                  0  ") 
			.append("                                  else  ") 
			.append("                                  nvl(nvl(ninnum, noutnum), 0.0) - nvl(nretnum, 0.0) - ") 
			.append("  nvl(ntranoutnum, 0.0)- nvl(bb3.nsignnum,0) ") 
			.append("                                end) ggsl, ") 
			.append("                        sum(case substr(inv.invcode, 0, 2)  ") 
			.append("                                 when '23' then  ") 
			.append("                                 nvl(nvl(ninnum, noutnum), 0.0) - nvl(nretnum, 0.0) - ") 
			.append("  nvl(ntranoutnum, 0.0)- nvl(bb3.nsignnum,0) ") 
			.append("                                 else  ") 
			.append("                                   0 ") 
			.append("                                end) gzsl ") 
			.append("   from ic_general_b body,ic_general_h head, ic_general_bb3 bb3, bd_invmandoc man,bd_invbasdoc inv ") 
			.append("  where body.cgeneralbid = bb3.cgeneralbid(+) ") 
			.append("    and head.cgeneralhid = body.cgeneralhid ") 
			.append("    and body.cinvbasid = inv.pk_invbasdoc ") 
			.append("    and bb3.dr(+) = 0 ") 
			.append("    and body.cinventoryid = man.pk_invmandoc ") 
			.append("    and ((man.iscansaleinvoice = 'Y' AND ") 
			.append("        (ABS(nvl(body.noutnum, 0.0)) - ABS(nvl(body.naccumwastnum, 0.0)) - ") 
			.append("        ABS(nvl(bb3.nsignnum, 0.0))) <> 0 and ") 
			.append("        (body.cfirsttype = '3U' or ") 
			.append("        (body.cfirsttype = '30' and exists ") 
			.append("         (select 1 ") 
			.append("               from so_saleexecute ") 
			.append("              where so_saleexecute.csale_bid = body.cfirstbillbid ") 
			.append("                and so_saleexecute.creceipttype = '30' ") 
			.append("                and so_saleexecute.dr = 0 ") 
			.append("                and nvl(so_saleexecute.bifinvoicefinish, 'N') = 'N')))) and ") 
			.append("        body.dr = 0) ") 
			.append("    and head.vbillcode in ("+sb.toString()+") ") 
			.append("    and head.pk_corp = '"+getPkCorp()+"' ") 
			.append(" group by head.vbillcode ");


			

			Object obj= getUAPQuery().executeQuery(sss.toString(), new ArrayListProcessor());

			if (obj instanceof ArrayList) {
				ArrayList list = (ArrayList) obj;
				for (int i = 0; i < list.size(); i++) {
					Object[] objs = (Object[]) list.get(i);
					map.put(objs[0].toString()+"gg",objs[1]);
					map.put(objs[0].toString()+"gz",objs[2]);
				}
			}
			
			//lj+ 2005-4-5
			//selectFirstHeadRow();
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("pfworkflow","UPPpfworkflow-000237")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("pfworkflow","UPPpfworkflow-000490")/*@res "数据加载失败！"*/);
		}
	
    }
    

    IUAPQueryBS iIUAPQueryBS=null;
    private IUAPQueryBS getUAPQuery() throws BusinessException {
    	if (iIUAPQueryBS == null)
    	try {
    		iIUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
    							IUAPQueryBS.class.getName());
    	} catch (ComponentException e) {
    			throw new SysInitException("IUAPQueryBS not found!");
    		}
    	return iIUAPQueryBS;
    	}
    /*
     * add 未开票数量
     * wkf
     * 2014-03-25
     * end 
     * */
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		super.valueChanged(arg0);
	}
	
    public void mouse_doubleclick(BillMouseEnent e)
    {
//  	final String s=ClientEnvironment.getInstance().getCorporation().getUnitcode();
    	final String s=ClientEnvironment.getInstance().getCorporation().getFathercorp(); //edit by zwx 2014-11-26 
        if(e.getPos() == 0)
        {
            final int headRow = e.getRow();
                 
        	(new Thread(new Runnable() {

                public void run()
                {
                    BannerDialog dialog;
                    dialog = new BannerDialog(QrySaleBillForInvoiceDlg.this);
                    dialog.setStartText(NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000491"));
                    dialog.start();
                    headRowDoubleClicked(headRow);
                    dialog.end();
                }
            }
            )).start();
        }else
        {
        	if(s.equals("1095")){//edit by zwx 2014-11-26  1095
        		calcAllTotal1();
        	}
        	else
        	{
        		calcAllTotal();
        	}
        }
    }

    private void calcTotal() {
    	headRowChange(getbillListPanel().getHeadTable().getSelectedRow());
    	BillModel bodyModel = getbillListPanel().getBodyBillModel();
    	UFDouble mainsum = new UFDouble();
    	UFDouble asssum = new UFDouble();
        for(int i = 0; i < bodyModel.getRowCount(); i++)
        {
        	Object noutnum = bodyModel.getValueAt(i, "noutnum");//实发数量
        	Object noutassnum = bodyModel.getValueAt(i, "noutassistnum");//实发辅数量
        	if(noutnum!=null)
        		mainsum = mainsum.add(new UFDouble(noutnum.toString()));
        	if(noutassnum!=null)
        	asssum = asssum.add(new UFDouble(noutassnum.toString()));
        }
        if(txt1 !=null){//add
        	txt1.setText(mainsum.toString());
        }
        if(txt2 !=null){//add
        	txt2.setText(asssum.toString());		
        }
	}
    //当前选择罐盖/罐子总数量（未开票数）wkf
    private void calcTotal1() {
    	headRowChange(getbillListPanel().getHeadTable().getSelectedRow());
    	BillModel headModel = getbillListPanel().getHeadBillModel();
    	UFDouble guansum = new UFDouble();
    	UFDouble gaisum = new UFDouble();
    	int row=getbillListPanel().getHeadTable().getSelectedRow();
    	Object vbillcode = headModel.getValueAt(row, "vbillcode");//取出单据号
    	gaisum = gaisum.add(new UFDouble(map.get(vbillcode+"gg").toString()));
    	guansum = guansum.add(new UFDouble(map.get(vbillcode+"gz").toString()));
//        for(int i = 0; i < headModel.getRowCount(); i++)
//        {
//        	Object noutnum = headModel.getValueAt(i, "noutnum");//取出单据号
//        	gaisum = gaisum.add(new UFDouble(map.get(vbillcode+"gg").toString()));
//        	guansum = guansum.add(new UFDouble(map.get(vbillcode+"gz").toString()));
//        	String cinventorycode = (String) bodyModel.getValueAt(i, "cinventorycode");
//        	String s2 =  cinventorycode.substring(0, 2);
//        	if(s2.equals("23"))
//        	{
//        		guansum = guansum.add(new UFDouble(noutnum.toString()));
//        	}else{
//        		gaisum = gaisum.add(new UFDouble(noutnum.toString()));
//        	}
//        }
        DecimalFormat df = new DecimalFormat("0.00");//保留两位小数
        txt7.setText(df.format(guansum).toString());
        txt8.setText(df.format(gaisum).toString());		
	}
    
	public void bodyRowChange(BillEditEvent billeditevent)
    {
    	calcTotal();
    	calcTotal1();
    }
    
    private synchronized void headRowChange(int iNewRow)
    {
        if(getbillListPanel().getHeadBillModel().getValueAt(iNewRow, getpkField()) != null && !getbillListPanel().setBodyModelData(iNewRow))
        {
            loadBodyData(iNewRow);
            getbillListPanel().setBodyModelDataCopy(iNewRow);
        }
        getbillListPanel().repaint();
    }
    
    private void headRowDoubleClicked(int headRow)
    {
        headRowChange(headRow);
        BillModel bodyModel = getbillListPanel().getBodyBillModel();
        BillModel headModel = getbillListPanel().getHeadBillModel();
//      final String s=ClientEnvironment.getInstance().getCorporation().getUnitcode();
        final String s=ClientEnvironment.getInstance().getCorporation().getFathercorp(); //edit by zwx 2014-11-26
        if(getbillListPanel().isMultiSelect())
        {
            if(headModel.getRowState(headRow) == 4)
            {
                headModel.setRowState(headRow, -1);
                for(int i = 0; i < bodyModel.getRowCount(); i++)
                {
                	bodyModel.setRowState(i, -1);
                }
            } 
            else
            {
                headModel.setRowState(headRow, 4);
                for(int i = 0; i < bodyModel.getRowCount(); i++)
                {
                	bodyModel.setRowState(i, 4);
                }
            }
            if(s.equals("1095")) //edit by zwx 2014-11-26  1095
            {
            	calcAllTotal1();
            }
            else
            {
            	calcAllTotal();
            }
            
            getbillListPanel().setBodyModelDataCopy(headRow);
        }
    }
    private void calcAllTotal() {
    	UFDouble totalSum = new UFDouble();
    	UFDouble totalassSum = new UFDouble();
    	AggregatedValueObject[] multiSelectedVOs = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
    	for (int i = 0;multiSelectedVOs != null && i < multiSelectedVOs.length; i++) {
    		CircularlyAccessibleValueObject[] childrenVO = multiSelectedVOs[i].getChildrenVO();
    		for (int j = 0; j < childrenVO.length; j++) {
    			UFDouble num = (UFDouble)childrenVO[j].getAttributeValue("noutnum");//实发数量
    			UFDouble num1 = (UFDouble)childrenVO[j].getAttributeValue("noutassistnum");//实发辅数量
    			if(num != null)
    				totalSum = totalSum.add(num);
    			if(num1 != null)
    				totalassSum = totalassSum.add(num1);
    		}
    	}
    	if(txt3 != null) {//add
    		txt3.setText(totalSum.toString());
    	}
    	if(txt4 !=null){//add
    		txt4.setText(totalassSum.toString());
    	}
    }


    //罐子/罐盖的累计选择总数量
    private void calcAllTotal1() {
        UFDouble guanzsum = new UFDouble();
        UFDouble gaizsum = new UFDouble();
        BillModel headModel = getbillListPanel().getHeadBillModel();
        AggregatedValueObject[] multiSelectedVOs = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
        try {
        	for (int i = 0;multiSelectedVOs != null && i < multiSelectedVOs.length; i++) {
        		Object vbillcode = ((GeneralBillHeaderVO)multiSelectedVOs[i].getParentVO()).getVbillcode();
        		gaizsum = gaizsum.add(new UFDouble(map.get(vbillcode+"gg").toString()));
        		guanzsum = guanzsum.add(new UFDouble(map.get(vbillcode+"gz").toString()));
        	}
//        		CircularlyAccessibleValueObject[] childrenVO = multiSelectedVOs[i].getChildrenVO();
//        		for (int j = 0; j < childrenVO.length; j++) {
//        			UFDouble vbillcode = (UFDouble)childrenVO[j].getAttributeValue("vbillcode");
//        			String cinventorycode = (String) childrenVO[j].getAttributeValue("cinventorycode");
//        			String s2 =  cinventorycode.substring(0, 2);//截取存货编码前两位
//        			if(s2.equals("23"))
//        			{
//        				guanzSum = guanzSum.add(new UFDouble(vbillcode.toString()));
//        				
//        			}else{
//        				gaizSum = gaizSum.add(new UFDouble(vbillcode.toString()));
//        			}
//        		}
        		
//        	}
		} catch (Exception e) {
			e.getMessage();
		}
		DecimalFormat df = new DecimalFormat("0.00");
        if(txt5 !=null)
        {
        	txt5.setText(df.format(guanzsum).toString());
        }
        if(txt6 !=null)
        {
        	txt6.setText(df.format(gaizsum).toString());
        }
	}
    
	public void onOk()
    {
        if(getbillListPanel().getHeadBillModel().getRowCount() > 0)
        {
            AggregatedValueObject selectedBillVOs[] = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
            retBillVo = selectedBillVOs.length <= 0 ? null : selectedBillVOs[0];
            retBillVos = selectedBillVOs;
            validatePricePolicy(retBillVos);
        }
        closeOK();
    }
    /**
     * 验证价格策略
     * */
    private void validatePricePolicy(AggregatedValueObject[] vos) 
    {
		if(vos == null || vos.length == 0)return;
		ArrayList list = new ArrayList();
		for (int i = 0; i < vos.length; i++) {
			CircularlyAccessibleValueObject parentVO = vos[i].getParentVO();
			Object vdef1 = parentVO.getAttributeValue("vdef1");
			if(vdef1 == null)
				vdef1 = "N";
			if(!list.contains(vdef1))
			{
				list.add(vdef1);
			}
		}
		if(list.size()>1)
		{
			String str = "有锁定和非锁定订单价格";
			MessageDialog.showErrorDlg(null, null, str);
			throw new BusinessRuntimeException(str);
		}
	}
	public BillListPanel getbillListPanel()
    {
        if(listPanel == null)
        {
            listPanel = super.getbillListPanel();
            if(listPanel != null)
            {
                BillListData billDataVo = listPanel.getBillListData();
                nc.vo.bd.def.DefVO defs[] = null;
                defs = DefSetTool.getDefHead(getPkCorp(), "icbill");
                if(defs != null)
                    billDataVo.updateItemByDef(defs, "vuserdef", true);
                defs = DefSetTool.getDefBody(getPkCorp(), "icbill");
                if(defs != null)
                    billDataVo.updateItemByDef(defs, "vuserdef", false);
                listPanel.setListData(billDataVo);
            }
        }
        return listPanel;
    }

    public String getBodyCondition()
    {
        String swhere = (new StringBuilder()).append(" man.iscansaleinvoice = 'Y' AND (ABS(COALESCE(body.noutnum,0.0))").append(SO25.booleanValue() ? (new StringBuilder()).append("*").append(SO26).toString() : "").append(" -ABS(COALESCE(body.naccumwastnum,0.0))-ABS(COALESCE(bb3.nsignnum,0.0))) <> 0 ").append(" and ( body.cfirsttype = '3U' or ( body.cfirsttype = '30' and ").append(" exists ( select 1 from so_saleexecute where so_saleexecute.csale_bid = body.cfirstbillbid and so_saleexecute.creceipttype='30' and so_saleexecute.dr = 0 and COALESCE(so_saleexecute.bifinvoicefinish,'N') = 'N' ) ) ").append(" ) ").toString();
        String sbodywhere = ICCommonBusi.getGeneralBodyDataPowerWhere(getQueyDlg().getConditionVO());
        if(sbodywhere != null)
            swhere = (new StringBuilder()).append(swhere).append(" and ").append(sbodywhere).toString();
        return swhere;
    }

    public String[][] getBodyShowNum()
    {
        initSysParam();
        String sa2Scale[][] = {
            {
                "nshouldinnum", "nshouldoutnum", "nsignnum", "ntranoutnum", "nretnum", "noutnum", "nleftnum", "ninnum", "ninassistnum", "nleftastnum", 
                "nneedinassistnum", "noutassistnum", "nretastnum", "nshouldoutassistnum", "ntranoutastnum", "ntaxprice", "nsaleprice", "ntaxmny", "nsalemny", "naccoutnum", 
                "naccinvoicenum", "navlinvoicenum", "hsl"
            }, {
                (new StringBuilder()).append("").append(m_iaScale[0]).toString(), (new StringBuilder()).append("").append(m_iaScale[0]).toString(), (new StringBuilder()).append("").append(m_iaScale[0]).toString(), (new StringBuilder()).append("").append(m_iaScale[0]).toString(), (new StringBuilder()).append("").append(m_iaScale[0]).toString(), (new StringBuilder()).append("").append(m_iaScale[0]).toString(), (new StringBuilder()).append("").append(m_iaScale[0]).toString(), (new StringBuilder()).append("").append(m_iaScale[0]).toString(), (new StringBuilder()).append("").append(m_iaScale[1]).toString(), (new StringBuilder()).append("").append(m_iaScale[1]).toString(), 
                (new StringBuilder()).append("").append(m_iaScale[1]).toString(), (new StringBuilder()).append("").append(m_iaScale[1]).toString(), (new StringBuilder()).append("").append(m_iaScale[1]).toString(), (new StringBuilder()).append("").append(m_iaScale[1]).toString(), (new StringBuilder()).append("").append(m_iaScale[1]).toString(), (new StringBuilder()).append("").append(m_iaScale[3]).toString(), (new StringBuilder()).append("").append(m_iaScale[3]).toString(), (new StringBuilder()).append("").append(m_iaScale[4]).toString(), (new StringBuilder()).append("").append(m_iaScale[4]).toString(), (new StringBuilder()).append("").append(m_iaScale[0]).toString(), 
                (new StringBuilder()).append("").append(m_iaScale[0]).toString(), (new StringBuilder()).append("").append(m_iaScale[0]).toString(), (new StringBuilder()).append("").append(m_iaScale[2]).toString()
            }
        };
        return sa2Scale;
    }

    public String getHeadCondition()
    
    {
        String sHeadCondition = null;
        if(getPkCorp() != null && getPkCorp().trim().length() > 0)
            sHeadCondition = (new StringBuilder()).append(" head.pk_corp = '").append(getPkCorp().trim()).append("' AND  (head.fbillflag = 3 OR head.fbillflag = 4) and (ABS(COALESCE(body.noutnum,0.0))").append(SO25.booleanValue() ? (new StringBuilder()).append("*").append(SO26).toString() : "").append("-ABS(COALESCE(body.naccumwastnum,0.0))-ABS(COALESCE(bb3.nsignnum,0.0))) <> 0 ").toString();
        else
            sHeadCondition = (new StringBuilder()).append("  (head.fbillflag = 3 OR head.fbillflag = 4) and (ABS(COALESCE(body.noutnum,0.0))").append(SO25.booleanValue() ? (new StringBuilder()).append("*").append(SO26).toString() : "").append("-ABS(COALESCE(body.naccumwastnum,0.0))-ABS(COALESCE(bb3.nsignnum,0.0))) <> 0 ").toString();
        sHeadCondition = (new StringBuilder()).append(sHeadCondition).append(" and ( body.cfirsttype = '3U' or ( body.cfirsttype = '30' and  exists ( select 1 from so_saleexecute where so_saleexecute.csale_bid = body.cfirstbillbid and so_saleexecute.creceipttype='30' and so_saleexecute.dr = 0 and COALESCE(so_saleexecute.bifinvoicefinish,'N') = 'N' ) )  ) ").toString();
        return sHeadCondition;
    }

    public String[] getHeadHideCol()
    {
        return null;
    }

    public String[][] getHeadShowNum()
    {
        initSysParam();
        String sa2Scale[][] = {
            {
                "ndiscountmny", "nnetmny"
            }, {
                (new StringBuilder()).append("").append(m_iaScale[4]).toString(), (new StringBuilder()).append("").append(m_iaScale[4]).toString()
            }
        };
        return sa2Scale;
    }

    protected void initSysParam()
    {
        if(m_iaScale != null)
            return;//break MISSING_BLOCK_LABEL_470;
        String saParam[];
        ArrayList alRetData = null;
        m_iaScale = (new int[] {
            8, 8, 8, 8, 8
        });
        saParam = (new String[] {
            "BD501", "BD502", "BD503", "BD505", "BD301", "SO25", "SO26"
        });
        m_CorpID = getPkCorp();
        m_UserID = getOperator();
        ArrayList alAllParam = new ArrayList();
        ArrayList alParam = new ArrayList();
        alParam.add(m_CorpID.trim());
        alParam.add(saParam);
        alAllParam.add(alParam);
        alAllParam.add(m_UserID.trim());
        try {
			alRetData = (ArrayList)GeneralBillHelper.queryInfo(new Integer(11), alAllParam);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        if(alRetData == null || alRetData.size() < 2)
            return;
        try
        {
            String saParamValue[] = (String[])(String[])alRetData.get(0);
            if(saParamValue != null && saParamValue.length >= saParam.length)
            {
                if(saParamValue[0] != null)
                    m_iaScale[0] = Integer.parseInt(saParamValue[0]);
                if(saParamValue[1] != null)
                    m_iaScale[1] = Integer.parseInt(saParamValue[1]);
                if(saParamValue[2] != null)
                    m_iaScale[2] = Integer.parseInt(saParamValue[2]);
                if(saParamValue[3] != null)
                    m_iaScale[3] = Integer.parseInt(saParamValue[3]);
                if(saParamValue[4] != null)
                    m_iaScale[4] = Integer.parseInt(saParamValue[4]);
                if(saParamValue[5] != null && saParamValue[5].trim().length() > 0)
                    SO25 = new UFBoolean(saParamValue[5].trim());
                else
                    SO25 = new UFBoolean(false);
                if(saParamValue[6] != null && saParamValue[6].trim().length() > 0)
                    SO26 = (new UFDouble(saParamValue[6].trim())).div(new UFDouble(100)).add(new UFDouble(1));
                else
                    SO26 = new UFDouble(1);
            }
        }
        catch(Exception e)
        {
            SCMEnv.out((new StringBuilder()).append("can not get para").append(e.getMessage()).toString());
        }
    }

    protected int m_iaScale[];
    private String m_CorpID;
    private String m_UserID;
    private BillListPanel listPanel;
    private UFBoolean SO25;
    private UFDouble SO26;
	private UITextField txt1;
	private UITextField txt2;
	private UITextField txt3;
	private UITextField txt4;
	private UITextField txt5;
	private UITextField txt6;
	private UITextField txt7;
	private UITextField txt8;
    
    
	@Override
	protected JPanel getUIDialogContentPane() {
		JPanel panel = super.getUIDialogContentPane();
		int count = panel.getComponentCount();
		if(count>0)
		{
			UIPanel comp = (UIPanel)panel.getComponent(0);
			if(comp.getComponentCount() == 5)
			{
				UILabel lbl1 = new UILabel("主"); 
				UILabel lbl2 = new UILabel("辅");
				UILabel lbl3 = new UILabel("主(+)");
				UILabel lbl4 = new UILabel("辅(+)");
				txt1 = new UITextField(8);
				txt2 = new UITextField(8);
				txt3 = new UITextField(8);
				txt4 = new UITextField(8);
				txt1.setEditable(false);
				txt2.setEditable(false);
				txt3.setEditable(false);
				txt4.setEditable(false);
				comp.add(lbl1);
				comp.add(txt1);
				comp.add(lbl2);
				comp.add(txt2);
				comp.add(lbl3);
				comp.add(txt3);
				comp.add(lbl4);
				comp.add(txt4);
				//新增合计框
				UILabel lbl5 = new UILabel("罐子累计数");
				UILabel lbl6 = new UILabel("罐盖累计数");
				UILabel lbl7 = new UILabel("当前行罐子总数");
				UILabel lbl8 = new UILabel("当前行罐盖总数");
				//new
				txt5 = new UITextField(8);
				txt6 = new UITextField(8);
				txt7 = new UITextField(8);
				txt8 = new UITextField(8);
				
				//new
			
				txt5.setEditable(false);
				txt6.setEditable(false);
				txt7.setEditable(false);
				txt8.setEditable(false);
				String s=ClientEnvironment.getInstance().getCorporation().getFathercorp();//获得当前登录上级公司主键
				if(s.equals("1095")){//如果公司 上级为1095启用txt5~txt8   eidt by zwx 2014-11-24
					lbl1.setVisible(false);
					txt1.setVisible(false);
					lbl2.setVisible(false);
					txt2.setVisible(false);
					lbl3.setVisible(false);
					txt3.setVisible(false);
					lbl4.setVisible(false);
					txt4.setVisible(false);
				}
				else
				{
					lbl5.setVisible(false);
					txt5.setVisible(false);
					lbl6.setVisible(false);
					txt6.setVisible(false);
					lbl7.setVisible(false);
					txt7.setVisible(false);
					lbl8.setVisible(false);
					txt8.setVisible(false);
				}
				comp.add(lbl7);
				comp.add(txt7);
				comp.add(lbl8);
				comp.add(txt8);
				comp.add(lbl5);
				comp.add(txt5);
				comp.add(lbl6);
				comp.add(txt6);
			}
		}
		return panel;
	}
	/**
	 * add 全选全消合计框数量未变动问题
	 * wkf
	 * 2014-03-24
	 * start
	 * */
	@Override
	public void onSelectAll() {
		super.onSelectAll();
		UFDouble guanzsum = new UFDouble();
        UFDouble gaizsum = new UFDouble();
        BillModel headModel = getbillListPanel().getHeadBillModel();
        AggregatedValueObject[] multiSelectedVOs = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
        try {
        	for (int i = 0;multiSelectedVOs != null && i < multiSelectedVOs.length; i++) {
        		Object vbillcode = ((GeneralBillHeaderVO)multiSelectedVOs[i].getParentVO()).getVbillcode();
        		gaizsum = gaizsum.add(new UFDouble(map.get(vbillcode+"gg").toString()));
        		guanzsum = guanzsum.add(new UFDouble(map.get(vbillcode+"gz").toString()));
        	}
		} catch (Exception e) {
			e.getMessage();
		}
		DecimalFormat df = new DecimalFormat("0.00");
        if(txt5 !=null)
        {
        	txt5.setText(df.format(guanzsum).toString());
        }
        if(txt6 !=null)
        {
        	txt6.setText(df.format(gaizsum).toString());
        }
		
	}
	@Override
	public void onCancelSelectAll() {
		super.onCancelSelectAll();
		txt5.setText("0.00");
		txt6.setText("0.00");
	}
	/**
	 * add 全选全消合计框数量未变动问题
	 * wkf
	 * 2014-03-24
	 * end
	 * */
	
}
