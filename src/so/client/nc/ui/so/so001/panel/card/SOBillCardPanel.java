package nc.ui.so.so001.panel.card;

import nc.ui.so.so001.panel.card.SaleBillCardUI;

import java.awt.event.ActionEvent;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;
import javax.swing.ListSelectionModel;

import bsh.This;
import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.ct.ref.IValiSaleCtRefModel;
import nc.itf.scm.so.so103.IBuyLargess;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.bd.b21.CurrtypeQuery;
import nc.ui.bd.b39.PhaseRefModel;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.dbcache.DBCacheFacade;
import nc.ui.ic.pub.lot.LotNumbRefPane;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillBodyMenuListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillCellEditor;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.BillSortListener;
import nc.ui.pub.bill.BillTotalListener;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.scm.extend.IFuncExtend;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.pub.InvoInfoBYFormula;
import nc.ui.scm.pub.billutil.ClientCacheHelper;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.scm.ref.prm.CustAddrRefModel;
import nc.ui.so.pub.CalBodySORefModel;
import nc.ui.so.pub.CustMandocSORefModel;
import nc.ui.so.pub.InvAttrCellRenderer;
import nc.ui.so.so001.SaleOrderBO_Client;
import nc.ui.so.so001.panel.bom.BillTools;
import nc.ui.so.so001.panel.list.SaleOrderVOCache;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.bd.CorpVO;
import nc.vo.bd.b20.CurrtypeVO;
import nc.vo.bd.invdoc.InvbindleVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ct.TypeVO;
import nc.vo.scm.ctpo.RetCtToPoQueryVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.so.so001.SOToolVO;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SoVoConst;
import nc.vo.so.so016.SoVoTools;
import nc.vo.so.so102.InvcalbodyVO;
import nc.vo.so.so103.BuylargessBVO;
import nc.vo.so.so103.BuylargessHVO;
import nc.vo.so.so103.BuylargessVO;
import nc.vo.sp.service.PriceAskResultVO;

public class SOBillCardPanel extends BillCardPanel
  implements BillEditListener, BillEditListener2, BillBodyMenuListener, BillTotalListener, BillSortListener, IBillRelaSortListener2
{
  private SaleBillCardUI uipanel;
  private String pk_corp;
  public String strState = NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000340");

  public String sEmployeeRefCondition = null;

  protected FreeItemRefPane ivjFreeItemRefPane = null;

  protected SORefDelegate soRefDelegate = null;

  protected LotNumbRefPane ivjLotNumbRefPane = null;

  public ArrayList alInvs = new ArrayList();

  public Hashtable hCTTypeVO = null;

  protected HashMap m_hConCal = new HashMap();

  public String sInvRefCondition = null;

  protected UITextField tfieldBatch = new UITextField();

  protected Hashtable m_htLargess = new Hashtable();

  public SOBillCardPanel(SaleBillCardUI parent, String name, String billtype, String pk_corp, String operator)
    throws Exception
  {
    this.uipanel = parent;

    setName(name);

    setBillType(billtype);

    setCorp(pk_corp);
    this.pk_corp = pk_corp;

    setOperator(operator);

    addBillEditListenerHeadTail(this);

    setBodyMenuShow(true);

    setTatolRowShow(true);

    setEnabled(false);
  }

  public void afterEdit(BillEditEvent e)
  {
    getBillModel().setNeedCalculate(false);
    try
    {
      if (e.getPos() == 0)
      {
        if (e.getKey().equals("vreceiptcode")) {
          this.uipanel.m_isCodeChanged = true;
        }

        if (e.getKey().equals("ccustomerid")) {
          afterCustomerEdit(e);
          freshBodyLargess(1);
        }
        else if (e.getKey().equals("cdeptid")) {
          afterDeptEdit(e);
        }
        else if (e.getKey().equals("cemployeeid")) {
          afterEmployeeEdit(e);
        }
        else if (e.getKey().equals("csalecorpid"))
        {
          getBillCardTools().setSendCalBodyAndWare(0, getRowCount());
          freshBodyLargess(3);
        }
        else if (e.getKey().equals("ndiscountrate")) {
          afterDiscountrateEdit(e);
        }
        else if (e.getKey().equals("ntaxrate")) {
          afterTaxrateBillEdit(e);
        } else if (e.getKey().equals("dbilldate")) {
          freshBodyLargess(2);
        }
        else if (e.getKey().equals("ccurrencytypeid")) {
          afterCurrencyEdit(e);
          freshBodyLargess(2);
        }
        else if (e.getKey().equals("nexchangeotobrate")) {
          afterChangeotobrateEdit(e);
        }
        else if (e.getKey().equals("nexchangeotoarate")) {
          afterChangeotoarateEdit(e);
        }
        else if (e.getKey().equals("ccalbodyid")) {
          afterCcalbodyidEdit(e);
        }
        else if (e.getKey().equals("cwarehouseid")) {
          afterCwarehouseidEdit(e);
        }
        else if (e.getKey().equals("creceiptcustomerid")) {
          afterCreceiptcorpEdit(e);
        }
        else if (e.getKey().equals("vreceiveaddress")) {
          afterVreceiveaddressEdit(e);
        }
        else if (e.getKey().equals("ctransmodeid")) {
          afterCtransmodeEdit(e);
        } else if (e.getKey().startsWith("vdef")) {
          DefSetTool.afterEditHead(getBillData(), e.getKey(), "pk_defdoc" + e.getKey().substring("vdef".length()));
        }
        else if ("nheadsummny".equals(e.getKey())) {
          afterHeadsummnyEdit(e);
        }

        findPriceWhenHeadItemChg(e.getKey());
      }

      if (e.getPos() == 1)
      {
        if (e.getKey().equals(getRowNoItemKey())) {
          afterRownoEdit(e);
        }

        if (e.getKey().equals("cinventorycode")) {
          afterInventoryMutiEdit(e);
        }
        else if ((e.getKey().equals("cpackunitname")) || (e.getKey().equals("cquoteunit")))
        {
          afterUnitEdit(e);
        }
        else if (e.getKey().equals("ct_name")) {
          afterCtManageEdit(e);
        }
        else if (e.getKey().equals("vfree0")) {
          afterFreeItemEdit(e);
        }

        //if(e.getKey().equals("noriginalcursummny"))
        //{
        	afterNumberEdit(e);
        //}
        

        if (e.getKey().equals("blargessflag")) {
          afterLargessFlagEdit(e);
        }
        else if (e.getKey().equals("cprojectname")) {
          afterProjectEdit(e);
        }
        else if (e.getKey().equals("fbatchstatus")) {
          afterBatchEdit(e);
        }
        else if (e.getKey().equals("cbatchid")) {
          afterBatchIDEdit(e);
        }
        else if (e.getKey().equals("cadvisecalbody")) {
          afterCcalbodyidEdit(e);
        }
        else if (e.getKey().equals("cbodywarehousename")) {
          afterCwarehouseidEdit(e);
        }
        else if (e.getKey().equals("creccalbody")) {
          String creccalbody = getBillCardTools().getBodyStringValue(e.getRow(), "creccalbody");

          if ((creccalbody == null) || (!creccalbody.equals(e.getOldValue())))
          {
            setBodyValueAt(null, e.getRow(), "crecwareid");
            setBodyValueAt(null, e.getRow(), "crecwarehouse");
          }
          if (creccalbody != null)
          {
            if ((getBodyValueAt(e.getRow(), "bdericttrans") != null) && (new UFBoolean(getBodyValueAt(e.getRow(), "bdericttrans").toString()).booleanValue()))
            {
              String[] sFormula = { "crecwareid->getColValue2(bd_stordoc,pk_stordoc,pk_calbody,creccalbodyid,isdirectstore,\"Y\")", "crecwarehouse->getColValue(bd_stordoc,storname,pk_stordoc,crecwareid)" };

              execBodyFormulas(e.getRow(), sFormula);
            }

          }

        }
        else if (e.getKey().equals("crecwarehouse")) {
          afterCrecwarehouseEdit(e);
        }
        else if (e.getKey().equals("boosflag")) {
          afterOOSFlagEdit(e.getRow(), true);
        }
        else if (e.getKey().equals("bsupplyflag")) {
          afterOOSFlagEdit(e.getRow(), false);
        }
        else if (e.getKey().equals("dconsigndate")) {
          afterBodyDateEdit(e.getRow(), true);
        }
        else if (e.getKey().equals("ddeliverdate")) {
          afterBodyDateEdit(e.getRow(), false);
        }
        else if (e.getKey().equals("creceiptcorpname")) {
          afterBodyCreceiptcorpidEdit(e);
        }
        else if (e.getKey().equals("vreceiveaddress")) {
          afterBodyAddressEdit(e);
        }
        else if (e.getKey().equals("bdericttrans")) {
          afterBdericttransEdit(e);
        }
        else if (e.getKey().equals("cconsigncorp")) {
          afterCconsignCorpEdit(e.getRow());
        }
        else if (e.getKey().startsWith("vdef")) {
          DefSetTool.afterEditBody(getBillData().getBillModel(), e.getRow(), e.getKey(), "pk_defdoc" + e.getKey().substring("vdef".length()));
        }
        else if (e.getKey().equals("creceiptareaname")) {
          String sDateDeliver = getBodyValueAt(e.getRow(), "ddeliverdate") == null ? null : getBodyValueAt(e.getRow(), "ddeliverdate").toString().trim();

          if ((sDateDeliver == null) || (sDateDeliver.length() == 0))
            afterBodyDateEdit(e.getRow(), true);
          else {
            afterBodyDateEdit(e.getRow(), false);
          }
        }
        else if (e.getKey().equals("cadvisecalbody")) {
          String sDateDeliver = getBodyValueAt(e.getRow(), "ddeliverdate") == null ? null : getBodyValueAt(e.getRow(), "ddeliverdate").toString().trim();

          if ((sDateDeliver == null) || (sDateDeliver.length() == 0))
            afterBodyDateEdit(e.getRow(), true);
          else {
            afterBodyDateEdit(e.getRow(), false);
          }
        }
        else if (e.getKey().equals("cpricepolicy")) {
          afterPricePolicy(e);
        }
        else if (e.getKey().equals("cpriceitemtablename")) {
          afterPriceItemTable(e);
        }
       

      }

      getBillCardTools().setManualEdit(e.getRow(), true);
    }
    catch (Exception ee)
    {
      SCMEnv.out(ee);
    }
    finally
    {
      if (e.getPos() == 0) {
        String[] sFormulas = getHeadItem(e.getKey()).getEditFormulas();
        if ((sFormulas != null) && (sFormulas.length > 0)) {
          execHeadFormulas(sFormulas);
        }
      }
      getBillModel().setNeedCalculate(true);

      if ((!e.getKey().equals("nheadsummny")) && 
        (getHeadItem("nheadsummny") != null)) {
        getHeadItem("nheadsummny").setValue(getTotalValue("noriginalcursummny"));
      }
      //exeRow(e);
      updateUI();
    }
    
   
    
  }
  
  public void exeRow(BillEditEvent e)
  {
	  int row = this.getRowCount();
	  if(e.getKey().equalsIgnoreCase("cinventorycode"))
	  {
		  row = row-1;
	  }
	  for(int i=0;i<row;i++)
	  {
	  //修改  不管怎么样下列各列都要有值
	    try
	    {
		    String csourceid = (String)this.getBillCardTools().getBodyValue(i, "csourcebillbodyid");
		    String csourcehid = (String)this.getBillCardTools().getBodyValue(i, "csourcebillid");
		    
		    //如果存在销售合同
		    if(csourceid!=null&&!csourceid.trim().equals("")&&!e.getKey().equals("noriginalcursummny"))
		    {
		    	//合同分类
			    String nbusitype = (String) HYPubBO_Client.findColValue("ct_type a,ct_manage b", "to_char(a.nbusitype,'999')", " a.pk_ct_type=b.pk_ct_type and b.pk_ct_manage  ='"+csourcehid+"'");
			    if(nbusitype==null)
			    {
			    	nbusitype = "";
			    }else
			    {
			    	nbusitype = nbusitype.trim();
			    }
			    if(!nbusitype.equalsIgnoreCase("1"))  //如果不是存货分类控制单价方式
			    {
			    	return;
			    }
			    Object dpzk = (String) HYPubBO_Client.findColValue("ct_manage_b", "to_char(dpzk,'990.999999')", " pk_ct_manage_b ='"+csourceid+"'");
			    Object tsbl = (String) HYPubBO_Client.findColValue("ct_manage_b", "to_char(tsbl,'990.999999')", " pk_ct_manage_b ='"+csourceid+"'");
			    Object wsdj = (String) HYPubBO_Client.findColValue("ct_manage_b", "to_char(oriprice,'990.999999')", " pk_ct_manage_b ='"+csourceid+"'");
			    Object hsdj = (String) HYPubBO_Client.findColValue("ct_manage_b", "to_char(oritaxprice,'990.999999')", " pk_ct_manage_b ='"+csourceid+"'");
			    if(tsbl!=null&&!tsbl.toString().trim().equals(""))
			    {
			    	getBillCardTools().setBodyValue(tsbl, i,"tsbl");
			    }
			    if(dpzk!=null&&!dpzk.toString().trim().equals(""))
			    {
			    	getBillCardTools().setBodyValue(dpzk, i,"nitemdiscountrate");
			    }
			    if(wsdj!=null&&!wsdj.toString().trim().equals("")) //无税单价
			    {
			    	getBillCardTools().setBodyValue(wsdj, i,"noriginalcurprice");
			    }
			    if(hsdj!=null&&!hsdj.toString().trim().equals(""))//含税单价
			    {
			    	getBillCardTools().setBodyValue(hsdj, i,"noriginalcurtaxprice");
			    }
			    //noriginalcursummny  价税合计
			    UFDouble obj = (UFDouble)getBillCardTools().getBodyValue(i, "nnumber");
			    if(obj!=null&&hsdj!=null)
			    {
			    	//noriginalcursummny
			    	getBillCardTools().setBodyValue(obj.multiply(Double.parseDouble(hsdj.toString().trim())), i, "noriginalcursummny");
			    } 
		    }else
		    {
			    if(e.getKey().equals("nnumber")||e.getKey().equals("noriginalcurtaxprice"))
			    {
			    	
			    	UFDouble hsdj = (UFDouble)getBillCardTools().getBodyValue( i,"noriginalcurtaxprice");
			    	UFDouble nnumber = (UFDouble)getBillCardTools().getBodyValue( i,"nnumber");
			    	if(hsdj!=null&&(e.getValue()!=null&&!e.getValue().toString().trim().equals("")))
			    	{ 
			    		getBillCardTools().setBodyValue(hsdj.multiply(nnumber), i, "noriginalcursummny");
			    	}
			    }else if(e.getKey().equals("noriginalcursummny")) //noriginalcursummny  价税合计
			    {
			    	UFDouble hsdj = (UFDouble)getBillCardTools().getBodyValue( i,"noriginalcurtaxprice");
			    	UFDouble nnumber = (UFDouble)getBillCardTools().getBodyValue( i,"nnumber");
			    	UFDouble jshj = (UFDouble)getBillCardTools().getBodyValue( i,"noriginalcursummny");
			    	if(hsdj!=null&&nnumber!=null&&jshj!=null)
			    	{
				    	if(jshj.compareTo(hsdj.multiply(nnumber))>0)
				    	{
				    		//折扣额
				    		getBillCardTools().setBodyValue(jshj.sub(hsdj.multiply(nnumber)), i, "noriginalcurdiscountmny");
				    		//单品折扣
				    		getBillCardTools().setBodyValue(jshj.sub(hsdj.multiply(nnumber)).div(nnumber), i, "nitemdiscountrate");
				    	}
			    	}
			    }
		    }
	    }catch(Exception ex)
	    {
	    	Logger.error(ex);
	    }
	  }
  }

  public String getRowNoItemKey()
  {
    return "crowno";
  }

  private void findPriceWhenHeadItemChg(String key)
  {
    if (getBillModel().getRowCount() <= 0) {
      return;
    }
    ArrayList rowlist = new ArrayList();
    int i = 0; for (int loop = getBillModel().getRowCount(); i < loop; i++) {
      if (isFindPrice(i)) {
        rowlist.add(new Integer(i));
      }
    }

    if (rowlist.size() <= 0) {
      return;
    }
    int[] findrows = new int[rowlist.size()];

     i = 0; for (int loop = rowlist.size(); i < loop; i++) {
      findrows[i] = ((Integer)rowlist.get(i)).intValue();
    }

    if (("ccustomerid".equals(key)) || ("csalecorpid".equals(key)) || ("dbilldate".equals(key)) || ("ccurrencytypeid".equals(key)))
    {
      findPrice(findrows, null, false);
    }
  }

  private boolean isFindPrice(int i)
  {
    Integer bindpricetype = (Integer)getBodyValueAt(i, "bindpricetype");
    if ((bindpricetype != null) && (bindpricetype.intValue() == 0))
    {
      return false;
    }
    if ((getBillModel().getRowCount() > 0) && 
      (!"37".equals(getSouceBillType())) && (!"38".equals(getSouceBillType())) && (!"3B".equals(getSouceBillType())))
    {
      if ((getSouceBillType().equals("Z4")) || (getSouceBillType().equals("Z3")))
      {
        String ct_manageid = (String)getBodyValueAt(i, "ct_manageid");
        TypeVO voCtType = null;
        if ((ct_manageid != null) && (ct_manageid.length() != 0))
        {
          if (this.hCTTypeVO != null) {
            voCtType = (TypeVO)this.hCTTypeVO.get(ct_manageid);
          }
          if (voCtType == null) {
            try {
              voCtType = SaleOrderBO_Client.getContractType(ct_manageid);
            }
            catch (Throwable ex) {
              SCMEnv.out("获得合同类型标志出错!");
            }

          }

          if (voCtType != null) {
            int iInvType = voCtType.getNinvctlstyle() == null ? -1 : voCtType.getNinvctlstyle().intValue();

            int iDataType = voCtType.getNdatactlstyle() == null ? -1 : voCtType.getNdatactlstyle().intValue();

            if ((iInvType == 0) && ((iDataType == 0) || (iDataType == 3) || (iDataType == 4) || (iDataType == 6)))
            {
              if (this.uipanel.SO_17.booleanValue())
              {
                return false;
              }

              return this.uipanel.SA_15.booleanValue();
            }

            return this.uipanel.SA_15.booleanValue();
          }
        }
      }

    }

    return this.uipanel.SA_15.booleanValue();
  }

  protected void findPrice(int[] findrows, String oldinvid, boolean isinvchg)
  {
    String errmsg = "";
    getBillCardTools().SA34 = this.uipanel.SA34;

    if (this.uipanel.SA_15.booleanValue()) {
      ArrayList alNeedFind = getNeedFindPriceRows(findrows);

      HashMap hs = getBillCardTools().findPrice(findrows, oldinvid);

      ArrayList rowlist = new ArrayList();
      if ((hs != null) && (hs.size() > 0))
      {
        Integer[] rows = null;
        PriceAskResultVO resultvo = null;
        int pricerow = 0;

        if (isinvchg)
        {
          rows = (Integer[])(Integer[])hs.keySet().toArray(new Integer[hs.size()]);

          pricerow = 0;

          int i = 0; for (int loop = rows.length; i < loop; i++)
          {
            if ((alNeedFind != null) && (!alNeedFind.contains(rows[i])))
              continue;
            pricerow = rows[i].intValue();
            resultvo = (PriceAskResultVO)hs.get(rows[i]);

            if ((resultvo.getErrFlag() != null) && (resultvo.getErrFlag().intValue() != 0))
            {
              continue;
            }

            String stemp = (String)getBillCardTools().getBodyValue(pricerow, "cpricepolicyid");

            if ((stemp == null) || (stemp.trim().length() <= 0)) {
              setBodyValueAt(resultvo.getPricePolicyid(), pricerow, "cpricepolicyid");
            }
            stemp = (String)getBillCardTools().getBodyValue(pricerow, "cpriceitemid");

            if ((stemp == null) || (stemp.trim().length() <= 0)) {
              setBodyValueAt(resultvo.getPriceTypeid(), pricerow, "cpriceitemid");
            }

            setBodyValueAt(resultvo.getFindProcess(), pricerow, "cpricecalproc");

            stemp = (String)getBillCardTools().getBodyValue(pricerow, "cpriceitemtable");

            if ((stemp == null) || (stemp.trim().length() <= 0)) {
              setBodyValueAt(resultvo.getPricetariffid(), pricerow, "cpriceitemtable");
            }

          }

          boolean istowfindprc = false;
          if ((findrows != null) && (findrows.length > 0)) {
            String skey = null; String skey1 = null;
           i = 0; for (int loop = findrows.length; i < loop; i++) {
              skey = getBillCardTools().getSalePriceVOKey(getBillCardTools().getPriceParam(findrows[i]));

              if ((skey == null) || (skey.trim().length() <= 0))
                continue;
              int m = 0; for (int loopm = getRowCount(); m < loopm; m++) {
                if (findrows[i] == m)
                  continue;
                skey1 = getBillCardTools().getSalePriceVOKey(getBillCardTools().getPriceParam(m));

                if (skey.equals(skey1)) {
                  istowfindprc = true;
                  break;
                }
              }
              if (istowfindprc) {
                break;
              }
            }
          }
          if (istowfindprc) {
            hs = getBillCardTools().findPrice(findrows, oldinvid);
          }
          if ((hs == null) || (hs.size() <= 0)) {
              i = 0; for (int loop = rows.length; i < loop; i++)
            {
              if ((alNeedFind != null) && (!alNeedFind.contains(rows[i])))
              {
                continue;
              }
              pricerow = rows[i].intValue();
              resultvo = (PriceAskResultVO)hs.get(rows[i]);
              if (resultvo == null)
                continue;
              if ((resultvo.getErrFlag() != null) && (resultvo.getErrFlag().intValue() != 0))
              {
                continue;
              }

              setBodyValueAt(null, pricerow, "cpricepolicyid");
              setBodyValueAt(null, pricerow, "cpriceitemid");
              setBodyValueAt(null, pricerow, "cpricecalproc");
              setBodyValueAt(null, pricerow, "cpriceitemtable");
            }

            return;
          }

        }

        rows = (Integer[])(Integer[])hs.keySet().toArray(new Integer[hs.size()]);
        pricerow = 0;

        int i = 0; for (int loop = rows.length; i < loop; i++)
        {
          if ((alNeedFind != null) && (!alNeedFind.contains(rows[i]))) {
            continue;
          }
          pricerow = rows[i].intValue();
          resultvo = (PriceAskResultVO)hs.get(rows[i]);
          if (resultvo == null) {
            continue;
          }
          if ((resultvo.getErrFlag() != null) && (resultvo.getErrFlag().intValue() != 0))
          {
            errmsg = errmsg + NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000235", null, new String[] { (String)getBodyValueAt(pricerow, "crowno"), resultvo.getErrMessage() });

            errmsg = errmsg + "\n";

            getBillCardTools().clearBodyValue(SOBillCardTools.pricekeys, pricerow);
          }
          else
          {
            if (this.uipanel.SA_02.booleanValue()) {
              setBodyValueAt(resultvo.getNum(), pricerow, "norgqttaxprc");

              calculateNumber(rows[i].intValue(), "norgqttaxprc");
            } else {
              setBodyValueAt(resultvo.getNum(), pricerow, "norgqtprc");
              calculateNumber(rows[i].intValue(), "norgqtprc");
            }

            setBodyValueAt(getBillCardTools().getBodyUFDoubleValue(pricerow, "noriginalcurprice"), pricerow, "nqtorgprc");

            setBodyValueAt(getBillCardTools().getBodyUFDoubleValue(pricerow, "noriginalcurtaxprice"), pricerow, "nqtorgtaxprc");

            String stemp = (String)getBillCardTools().getBodyValue(pricerow, "cpricepolicyid");

            if ((stemp == null) || (stemp.trim().length() <= 0)) {
              setBodyValueAt(resultvo.getPricePolicyid(), pricerow, "cpricepolicyid");
            }

            stemp = (String)getBillCardTools().getBodyValue(pricerow, "cpriceitemid");

            if ((stemp == null) || (stemp.trim().length() <= 0)) {
              setBodyValueAt(resultvo.getPriceTypeid(), pricerow, "cpriceitemid");
            }

            setBodyValueAt(resultvo.getFindProcess(), pricerow, "cpricecalproc");

            stemp = (String)getBillCardTools().getBodyValue(pricerow, "cpriceitemtable");

            if ((stemp == null) || (stemp.trim().length() <= 0)) {
              setBodyValueAt(resultvo.getPricetariffid(), pricerow, "cpriceitemtable");
            }

            setBodyValueAt(resultvo.getReturnMoney(), pricerow, "breturnprofit");

            setBodyValueAt(resultvo.getPriceProtect(), pricerow, "bsafeprice");

            rowlist.add(rows[i]);

            if (getBillModel().getRowState(pricerow) == 0)
              getBillModel().setRowState(pricerow, 2);
          }
        }
      }
      String[] formulas = { "cpriceitem->getColValue(prm_pricetype,cpricetypename,cpricetypeid,cpriceitemid)", "cpriceitemtablename->getColValue(prm_tariff,cpricetariffname,cpricetariffid,cpriceitemtable)", "cpricepolicy->getColValue(prm_pricepolicy,pricepolicyname,pricepolicyid,cpricepolicyid)" };

      getBillCardTools().execBodyFormulas(formulas, rowlist);
    }
    if ((errmsg != null) && (errmsg.trim().length() > 0))
      this.uipanel.showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000237", null, new String[] { errmsg }));
  }

  private ArrayList getNeedFindPriceRows(int[] irows)
  {
    if (!this.strState.equals("修订")) {
      return null;
    }
    ArrayList al = new ArrayList();

    String csaleid = getHeadItem("csaleid").getValue();
    Hashtable ht = new Hashtable();

    if (csaleid != null) {
      SaleOrderVO saleorder = this.uipanel.vocache.getSaleOrderVO(csaleid);
      if (saleorder != null) {
        SaleorderBVO[] bvos = saleorder.getBodyVOs();
        if ((bvos != null) && (bvos.length > 0)) {
          int i = 0; for (int iLen = bvos.length; i < iLen; i++) {
            ht.put(bvos[i].getPrimaryKey(), bvos[i]);
          }
        }
      }
    }

    int k = 0;
    String sPk = null;
    String sInvid = null; String sOldInvid = null;
    SaleorderBVO bvo = null;

    for (k = 0; k < getRowCount(); k++) {
      if (getBillModel().getRowState(k) == 1) {
        al.add(new Integer(k));
      } else if (getBillModel().getRowState(k) == 2) {
        sPk = (String)getBodyValueAt(k, "corder_bid");
        if (sPk == null) {
          sInvid = (String)getBodyValueAt(k, "cinventoryid");
          if (sInvid != null)
            al.add(new Integer(k));
        } else {
          bvo = (SaleorderBVO)ht.get(sPk);
          if (bvo == null) {
            sInvid = (String)getBodyValueAt(k, "cinventoryid");
            if (sInvid != null)
              al.add(new Integer(k));
          } else {
            sInvid = (String)getBodyValueAt(k, "cinventoryid");
            sOldInvid = bvo.getCinventoryid();
            if ((sInvid != null) && (!sInvid.equals(sOldInvid))) {
              al.add(new Integer(k));
            }
          }
        }
      }
    }

    return al;
  }

  private void freshBodyLargess(int iChgtype)
  {
    if (iChgtype != 3) {
      int irowcount = getRowCount();
      int[] iallrow = new int[irowcount];
      for (int i = 0; i < irowcount; i++)
        iallrow[i] = i;
      int[] inewdelline = setBlargeLineWhenDelLine(iallrow);
      if ((inewdelline != null) && (inewdelline.length > 0)) {
        this.uipanel.onDelLine(inewdelline);
      }
    }
    String sPk = null;
    for (int i = getRowCount() - 1; i >= 0; i--) {
      sPk = (String)getBodyValueAt(i, "cinventoryid");
      if (sPk != null)
        afterInventoryMutiEdit(i, new String[] { sPk }, false, false, null, false, iChgtype);
    }
  }

  public int[] setBlargeLineWhenDelLine(int[] aryRows)
  {
    if ((aryRows == null) || (aryRows.length == 0)) {
      return null;
    }
    String sRow = null;
    Vector vt = new Vector();
    for (int i = 0; i < aryRows.length; i++)
    {
      sRow = (String)getBodyValueAt(aryRows[i], "crowno");
      if ((sRow != null) && (sRow.trim().length() > 0))
        vt.add(sRow);
    }
    UFBoolean bLargess = null;
    Vector vtnew = new Vector();
    for (int i = 0; i < getRowCount(); i++) {
      sRow = (String)getBodyValueAt(i, "clargessrowno");
      bLargess = new UFBoolean(getBodyValueAt(i, "blargessflag") == null ? "false" : getBodyValueAt(i, "blargessflag").toString());

      if ((bLargess == null) || (!bLargess.booleanValue()) || (sRow == null) || (!vt.contains(sRow)))
        continue;
      sRow = (String)getBodyValueAt(i, "crowno");

      if ((sRow != null) && (!vtnew.contains(sRow))) {
        vtnew.add(new Integer(i));
      }
    }

    int[] inewdelline = null;
    if (vtnew.size() > 0) {
      inewdelline = new int[vtnew.size()];
      Integer[] iitmp = new Integer[vtnew.size()];
      vtnew.copyInto(iitmp);
      for (int i = 0; i < iitmp.length; i++) {
        inewdelline[i] = iitmp[i].intValue();
      }
    }
    return inewdelline;
  }

  public void setBodyRowState(int row)
  {
    if (getBillModel().getRowState(row) == 0)
      getBillModel().setRowState(row, 2);
  }

  @SuppressWarnings("deprecation")
public void calculateNumber(int row, String key)
  {
    BillTools.calcEditFun(getHeadItem("dbilldate").getValue(), this.uipanel.BD505, this.uipanel.SA_02.booleanValue(), key, row, this);
  }

  private String getSouceBillType()
  {
    String creceipttype = null;
    if (getRowCount() > 0) {
      creceipttype = (String)getBodyValueAt(0, "creceipttype");
    }

    if ((creceipttype == null) || (creceipttype.trim().equals(""))) {
      creceipttype = "NO";
    }
    return creceipttype;
  }

  private String getSouceBillType(int irow)
  {
    String creceipttype = null;
    if (getRowCount() > irow) {
      creceipttype = (String)getBodyValueAt(irow, "creceipttype");
    }
    if ((creceipttype == null) || (creceipttype.trim().equals("")))
      creceipttype = "NO";
    return creceipttype;
  }

  private void afterCustomerEdit(BillEditEvent e)
  {
    UIRefPane refcemployeeid = (UIRefPane)getHeadItem("cemployeeid").getComponent();

    if (refcemployeeid != null) {
      refcemployeeid.getRefModel().setWherePart(this.sEmployeeRefCondition);
    }

    String cdeptid_old = getBillCardTools().getHeadStringValue("cdeptid");

    String cemployeeid_old = getBillCardTools().getHeadStringValue("cemployeeid");

    UFDouble ndiscountrate_old = getBillCardTools().getHeadUFDoubleValue("ndiscountrate");

    String ctransmodeid_old = getBillCardTools().getHeadStringValue("ctransmodeid");

    String ccurrencytypeid_old = getBillCardTools().getHeadStringValue("ccurrencytypeid");

    String ctermprotocolid_old = getBillCardTools().getHeadStringValue("ctermprotocolid");

    String ccalbodyid_old = getBillCardTools().getHeadStringValue("ccalbodyid");

    String csalecorpid_old = getBillCardTools().getHeadStringValue("csalecorpid");

    String creceiptcorpid_old = getBillCardTools().getHeadStringValue("creceiptcorpid");

    String cwarehouseid_old = getBillCardTools().getHeadStringValue("cwarehouseid");

    String[] formulas = new String[16];

    formulas[0] = "creceiptcorpid->getColValue(bd_cumandoc,pk_cusmandoc2,pk_cumandoc,ccustomerid)";

    formulas[1] = "creceiptcustomerid->getColValue(bd_cumandoc,pk_cusmandoc3,pk_cumandoc,ccustomerid)";

    formulas[2] = "cdeptid->getColValue(bd_cumandoc,pk_respdept1,pk_cumandoc,ccustomerid)";

    formulas[3] = "cemployeeid->getColValue(bd_cumandoc,pk_resppsn1,pk_cumandoc,ccustomerid)";

    formulas[4] = "ndiscountrate->getColValue(bd_cumandoc,discountrate,pk_cumandoc,ccustomerid)";

    formulas[5] = "ctransmodeid->getColValue(bd_cumandoc,pk_sendtype,pk_cumandoc,ccustomerid)";

    formulas[6] = "ccurrencytypeid->getColValue(bd_cumandoc,pk_currtype1,pk_cumandoc,ccustomerid)";

    formulas[7] = "ctermprotocolid->getColValue(bd_cumandoc,pk_payterm,pk_cumandoc,ccustomerid)";

    formulas[8] = "ccalbodyid->getColValue(bd_cumandoc,pk_calbody,pk_cumandoc,ccustomerid)";

    formulas[9] = "csalecorpid->getColValue(bd_cumandoc,pk_salestru,pk_cumandoc,ccustomerid)";

    formulas[10] = "naccountperiod->getColValue(bd_cumandoc,acclimit,pk_cumandoc,ccustomerid)";

    formulas[11] = "ccustbasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,ccustomerid)";

    formulas[12] = "bfreecustflag->getColValue(bd_cubasdoc,freecustflag,pk_cubasdoc,ccustbasid)";

    formulas[13] = "careaclid->getColValue(bd_cubasdoc,pk_areacl,pk_cubasdoc,ccustbasid)";

    formulas[14] = "careaclcode->getColValue(bd_areacl,areaclcode,pk_areacl,careaclid)";
    formulas[15] = "custshortname->getColValue(bd_cubasdoc,custshortname,pk_cubasdoc,ccustbasid)";

    getBillData().execHeadFormulas(formulas);

    String cdeptid = getBillCardTools().getHeadStringValue("cdeptid");
    if (((cdeptid == null) || (cdeptid.trim().length() <= 0)) && (cdeptid_old != null) && (cdeptid_old.trim().length() > 0))
    {
      cdeptid = cdeptid_old;
      setHeadItem("cdeptid", cdeptid_old);
    }

    String cemployeeid = getBillCardTools().getHeadStringValue("cemployeeid");

    if ((cemployeeid == null) || (cemployeeid.trim().length() <= 0)) {
      setHeadItem("cemployeeid", cemployeeid_old);
    }

    String sdiscountrate = getHeadItem("ndiscountrate").getValue();

    UFDouble ndiscountrate = null;
    if ((sdiscountrate == null) || (sdiscountrate.trim().length() == 0)) {
      if (ndiscountrate_old == null) {
        ndiscountrate = new UFDouble(100);
        setHeadItem("ndiscountrate", ndiscountrate);
      }
      else {
        setHeadItem("ndiscountrate", ndiscountrate_old);
        ndiscountrate = ndiscountrate_old;
      }
    }
    else ndiscountrate = new UFDouble(sdiscountrate);

    String stemp = getBillCardTools().getHeadStringValue("ctransmodeid");
    if ((stemp == null) || (stemp.trim().length() <= 0)) {
      setHeadItem("ctransmodeid", ctransmodeid_old);
    }

    String ccurrencytypeid = getBillCardTools().getHeadStringValue("ccurrencytypeid");

    if (((ccurrencytypeid == null) || (ccurrencytypeid.trim().length() <= 0)) && (ccurrencytypeid_old != null) && (ccurrencytypeid_old.trim().length() > 0))
    {
      ccurrencytypeid = ccurrencytypeid_old;
      setHeadItem("ccurrencytypeid", ccurrencytypeid_old);
    }

    stemp = getBillCardTools().getHeadStringValue("ctermprotocolid");
    if ((stemp == null) || (stemp.trim().length() <= 0)) {
      setHeadItem("ctermprotocolid", ctermprotocolid_old);
    }
    String ccalbodyid = null;

    if ((getSouceBillType().equals("4H")) || (getSouceBillType().equals("42"))) {
      setHeadItem("ccalbodyid", ccalbodyid_old);
      ccalbodyid = getBillCardTools().getHeadStringValue("ccalbodyid");
    } else {
      ccalbodyid = getBillCardTools().getHeadStringValue("ccalbodyid");
      if (((ccalbodyid == null) || (ccalbodyid.trim().length() <= 0)) && (ccalbodyid_old != null) && (ccalbodyid_old.trim().length() > 0))
      {
        ccalbodyid = ccalbodyid_old;
        setHeadItem("ccalbodyid", ccalbodyid_old);
      }

    }

    stemp = getBillCardTools().getHeadStringValue("csalecorpid");
    if ((stemp == null) || (stemp.trim().length() <= 0)) {
      setHeadItem("csalecorpid", csalecorpid_old);
    }

    if ((getHeadItem("creceiptcustomerid") != null) && ((getHeadItem("creceiptcustomerid").getValue() == null) || (getHeadItem("creceiptcustomerid").getValue().length() <= 0)))
    {
      getHeadItem("creceiptcustomerid").setValue(getHeadItem("ccustomerid").getValue());
    }

    if ((getHeadItem("creceiptcorpid").getValue() == null) || (getHeadItem("creceiptcorpid").getValue().length() <= 0))
    {
      getHeadItem("creceiptcorpid").setValue(getHeadItem("ccustomerid").getValue());
    }

    UIRefPane vreceiveaddress = (UIRefPane)getHeadItem("vreceiveaddress").getComponent();

    vreceiveaddress.setAutoCheck(false);

    if (getHeadItem("creceiptcustomerid") != null) {
      ((CustAddrRefModel)vreceiveaddress.getRefModel()).setCustId(getHeadItem("creceiptcustomerid").getValue());
    }

    String formula = "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcustomerid)";
    String pk_cubasdoc = (String)execHeadFormula(formula);

    String strvreceiveaddress = BillTools.getColValue2("bd_custaddr", "pk_custaddr", "defaddrflag", "Y", "pk_cubasdoc", pk_cubasdoc);

    vreceiveaddress.setPK(strvreceiveaddress);

    UFBoolean bfreecustflag = getBillCardTools().getHeadUFBooleanValue("bfreecustflag");

    if ((bfreecustflag == null) || (!bfreecustflag.booleanValue()))
      getHeadItem("cfreecustid").setEnabled(false);
    else {
      getHeadItem("cfreecustid").setEnabled(true);
    }

    getHeadItem("cfreecustid").setValue(null);
    try
    {
      UIRefPane refccurrencytypeid = (UIRefPane)getHeadItem("ccurrencytypeid").getComponent();

      if (refccurrencytypeid.getRefPK() == null) {
        refccurrencytypeid.setPK(this.uipanel.currtype.getLocalCurrPK());
        ccurrencytypeid = getBillCardTools().getHeadStringValue("ccurrencytypeid");
      }
    }
    catch (Exception e1) {
      SCMEnv.out(e1.getMessage());
    }

    if ((cdeptid != null) && ((cdeptid_old == null) || (!cdeptid_old.equals(cdeptid))))
    {
      afterDeptEdit(null);
    }

    if ((ndiscountrate != null) && ((ndiscountrate_old == null) || (ndiscountrate_old.compareTo(ndiscountrate) != 0)))
    {
      afterDiscountrateEdit(null);
    }

    String creceiptcorpid = getBillCardTools().getHeadStringValue("creceiptcorpid");

    if (((creceiptcorpid != null) && ((creceiptcorpid_old == null) || (!creceiptcorpid_old.equals(creceiptcorpid)))) || ((creceiptcorpid == null) && (creceiptcorpid_old != null)))
    {
      afterCreceiptcorpEdit(null);
    }

    afterCurrencyEdit(e);

    getBillCardTools().setBdeliverByCtransmodeid();

    getBillCardTools().setSendCalBodyAndWare(0, getRowCount());

    int i = 0; for (int iLen = getRowCount(); i < iLen; i++) {
      afterCconsignCorpEdit(i);
    }

    getBillCardTools().setBodyCchantypeid(0, getRowCount());

    if ((ccalbodyid != null) && ((ccalbodyid_old == null) || (!ccalbodyid_old.equals(ccalbodyid))))
    {
      afterCcalbodyidEdit(null);
    }
    String cwarehouseid = getBillCardTools().getHeadStringValue("cwarehouseid");

    if (((cwarehouseid != null) && ((cwarehouseid_old == null) || (!cwarehouseid_old.equals(cwarehouseid)))) || ((cwarehouseid == null) && (cwarehouseid_old != null)))
    {
      afterCwarehouseidEdit(null);
    }

    showCustManArInfo();
  }

  public void showCustManArInfo()
  {
    try
    {
      BillItem[] bis = new BillItem[5];

      bis[0] = getTailItem("accawmny");

      bis[1] = getTailItem("busawmny");

      bis[2] = getTailItem("ordawmny");

      bis[3] = getTailItem("creditmny");

      bis[4] = getTailItem("creditmoney");

      boolean isshow = false;

      int digit = getBillData().getBodyItem("noriginalcursummny").getDecimalDigits();

      for (int i = 0; i < bis.length; i++) {
        if (bis[i] != null) {
          bis[i].setDecimalDigits(digit);
          if (bis[i].isShow()) {
            isshow = true;
          }
        }
      }

      if (!isshow) {
        return;
      }
      String ccustomerid = getHeadItem("ccustomerid").getValue();
      if ((ccustomerid == null) || (ccustomerid.trim().length() <= 0)) {
        for (int i = 0; i < bis.length; i++) {
          if (bis[i] != null) {
            bis[i].setValue(null);
          }
        }
        return;
      }

      String[] formulas = { "accawmny->getColValue(bd_cumandoc,accawmny,pk_cumandoc,ccustomerid)", "busawmny->getColValue(bd_cumandoc,busawmny,pk_cumandoc,ccustomerid)", "ordawmny->getColValue(bd_cumandoc,ordawmny,pk_cumandoc,ccustomerid)", "creditmny->getColValue(bd_cumandoc,creditmny,pk_cumandoc,ccustomerid)", "creditmoney->getColValue(bd_cumandoc,creditmoney,pk_cumandoc,ccustomerid)" };

      execHeadFormulas(formulas);
    }
    catch (Exception e)
    {
      SCMEnv.out("显示客商管理档案应收相关信息失败");
    }
  }

  private void afterDeptEdit(BillEditEvent e)
  {
    UIRefPane cemployeeid = (UIRefPane)getHeadItem("cemployeeid").getComponent();

    String sRefInitWhere = getBillCardTools().getHeadRefInitWhere("cemployeeid");

    if ((sRefInitWhere == null) || (sRefInitWhere.trim().length() <= 0)) {
      sRefInitWhere = " 1=1 ";
    }
    if ("新增".equals(this.strState)) {
      sRefInitWhere = sRefInitWhere + " and bd_deptdoc.canceled ='N' ";
    }
    cemployeeid.getRefModel().setWherePart(sRefInitWhere);
  }

  public void afterDiscountrateEdit(BillEditEvent e)
  {
    Object oDiscountrate = null;
    if (e == null)
      oDiscountrate = getHeadItem("ndiscountrate").getValue();
    else
      oDiscountrate = e.getValue();
    for (int i = 0; i < getRowCount(); i++) {
      setBodyValueAt(oDiscountrate, i, "ndiscountrate");
      calculateNumber(i, "ndiscountrate");
      setBodyRowState(i);
    }
  }

  public void afterCreceiptcorpEdit(BillEditEvent e)
  {
    UIRefPane vreceiveaddress = (UIRefPane)getHeadItem("vreceiveaddress").getComponent();

    vreceiveaddress.setAutoCheck(false);

    String creceiptcustomerid = null;
    if (getHeadItem("creceiptcustomerid") != null) {
      creceiptcustomerid = getHeadItem("creceiptcustomerid").getValue();
    }
    ((CustAddrRefModel)vreceiveaddress.getRefModel()).setCustId(creceiptcustomerid);

    String formula = "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcustomerid)";
    String pk_cubasdoc = (String)execHeadFormula(formula);

    SOToolVO toolsvo = new SOToolVO();

    toolsvo.setAttributeValue("pk_cumandoc", creceiptcustomerid);

    toolsvo.setAttributeValue("pk_cubasdoc", pk_cubasdoc);

    toolsvo.setAttributeValue("pk_custaddr", "");

    toolsvo.setAttributeValue("crecaddrnode", "");

    toolsvo.setAttributeValue("crecaddrnodename", "");

    String pk_custaddr = BillTools.getColValue2("bd_custaddr", "pk_custaddr", "pk_cubasdoc", pk_cubasdoc, "defaddrflag", "Y");

    toolsvo.setAttributeValue("pk_custaddr", pk_custaddr);

    String[] formulas = { "crecaddrnode->getColValue(bd_custaddr,pk_address,pk_custaddr,pk_custaddr)", "crecaddrnodename->getColValue(bd_address,addrname,pk_address,crecaddrnode)" };

    getBillCardTools().execFormulas(formulas, new SOToolVO[] { toolsvo });

    vreceiveaddress.setPK(pk_custaddr);

    afterVreceiveaddressEdit(null);
    UIRefPane refCreceiptcorpid = null;
    if (getHeadItem("creceiptcustomerid") != null) {
      refCreceiptcorpid = (UIRefPane)getHeadItem("creceiptcustomerid").getComponent();
    }
    if ((refCreceiptcorpid != null) && (refCreceiptcorpid.getRefPK() != null)) {
      for (int i = 0; i < getBillModel().getRowCount(); i++) {
        setBodyValueAt(refCreceiptcorpid.getRefPK(), i, "creceiptcorpid");

        setBodyValueAt(refCreceiptcorpid.getRefName(), i, "creceiptcorpname");

        setBodyValueAt(toolsvo.getAttributeValue("crecaddrnode"), i, "crecaddrnode");

        setBodyValueAt(toolsvo.getAttributeValue("crecaddrnodename"), i, "crecaddrnodename");

        if (getBillModel().getRowState(i) == 0)
          getBillModel().setRowState(i, 2);
      }
    }
    else
      for (int i = 0; i < getBillModel().getRowCount(); i++) {
        setBodyValueAt(null, i, "creceiptcorpid");
        setBodyValueAt(null, i, "creceiptcorpname");

        setBodyValueAt(toolsvo.getAttributeValue("crecaddrnode"), i, "crecaddrnode");

        setBodyValueAt(toolsvo.getAttributeValue("crecaddrnodename"), i, "crecaddrnodename");

        if (getBillModel().getRowState(i) == 0)
          getBillModel().setRowState(i, 2);
      }
  }

  private void afterCurrencyEdit(BillEditEvent event)
  {
    UIRefPane ccurrencytypeid = (UIRefPane)getHeadItem("ccurrencytypeid").getComponent();

    setHeadItem("nexchangeotobrate", null);
    setHeadItem("nexchangeotoarate", null);
    getHeadItem("nexchangeotoarate").setEnabled(true);
    try
    {
      setPanelByCurrency(ccurrencytypeid.getRefPK());
      if ((this.uipanel.BD302 == null) || (!this.uipanel.BD302.booleanValue()))
      {
        UFDouble[] ult = SOBillCardTools.getExchangeRate(ccurrencytypeid.getRefPK(), getHeadItem("dbilldate").getValue(), ClientEnvironment.getInstance().getCorporation().getPk_corp());

        UFDouble dCurr0 = ult[0];
        setHeadItem("nexchangeotobrate", dCurr0);

        setHeadItem("nexchangeotoarate", null);

        if (this.uipanel.currtype.isLocalCurrType(ccurrencytypeid.getRefPK()))
        {
          getHeadItem("nexchangeotobrate").setEnabled(false);
        }
        else getHeadItem("nexchangeotobrate").setEnabled(true);

        getHeadItem("nexchangeotoarate").setEnabled(false);
      } else {
        UFDouble[] ult = SOBillCardTools.getExchangeRate(ccurrencytypeid.getRefPK(), getHeadItem("dbilldate").getValue(), ClientEnvironment.getInstance().getCorporation().getPk_corp());

        UFDouble dCurr0 = ult[0];
        UFDouble dCurr1 = ult[1];

        setHeadItem("nexchangeotobrate", dCurr0);
        setHeadItem("nexchangeotoarate", dCurr1 == null ? new UFDouble(0) : dCurr1);

        SCMEnv.out("折本汇率：" + dCurr0);
        SCMEnv.out("折辅汇率：" + dCurr1);

        if (this.uipanel.currtype.isFracCurrType(ccurrencytypeid.getRefPK())) {
          getHeadItem("nexchangeotoarate").setEnabled(false);
          getHeadItem("nexchangeotobrate").setEnabled(true);
        }
        else if (this.uipanel.currtype.isLocalCurrType(ccurrencytypeid.getRefPK()))
        {
          getHeadItem("nexchangeotobrate").setEnabled(false);
          getHeadItem("nexchangeotoarate").setEnabled(false);
        } else {
          getHeadItem("nexchangeotobrate").setEnabled(true);
          getHeadItem("nexchangeotoarate").setEnabled(true);
        }
      }

      for (int i = 0; i < getRowCount(); i++)
      {
        setBodyValueAt(ccurrencytypeid.getRefPK(), i, "ccurrencytypeid");

        setBodyValueAt(ccurrencytypeid.getRefName(), i, "ccurrencytypename");

        setBodyValueAt(getHeadItem("nexchangeotobrate").getValue(), i, "nexchangeotobrate");

        setBodyValueAt(getHeadItem("nexchangeotoarate").getValue(), i, "nexchangeotoarate");

        getBillCardTools().clearRowData(i, SOBillCardTools.pricekeys);

        setBodyRowState(i);
      }
    }
    catch (Exception e1)
    {
      SCMEnv.out("获得汇率失败！");
    }
  }

  public void setPanelByCurrency(String ccurrencytypeid)
  {
    try
    {
      SOBillCardTools.setCardPanelByCurrency(this, ccurrencytypeid, ClientEnvironment.getInstance().getCorporation().getPk_corp(), this.uipanel.BD302, new String[] { "nexchangeotobrate", "nexchangeotoarate" }, new String[] { "noriginalcurtaxmny", "noriginalcurmny", "noriginalcursummny", "noriginalcurdiscountmny" }, new String[] { "ntaxmny", "nmny", "nsummny", "ndiscountmny" }, new String[] { "nassistcurdiscountmny", "nassistcursummny", "nassistcurmny", "nassistcurtaxmny" }, new String[] { "nreceiptcathmny", "npreceivemny", "nheadsummny" }, null, null);

      if ((ccurrencytypeid == null) || (ccurrencytypeid.length() == 0)) {
        return;
      }
      CurrtypeQuery currquery = CurrtypeQuery.getInstance();
      CurrtypeVO currtypevo = null;
      if (ccurrencytypeid != null) {
        currtypevo = currquery.getCurrtypeVO(ccurrencytypeid);
      }

      int digit = 4;
      try
      {
        digit = currtypevo.getCurrbusidigit() == null ? 4 : currtypevo.getCurrbusidigit().intValue();

        SCMEnv.out(digit);
      } catch (Exception ex2) {
        digit = getBodyItem("noriginalcursummny").getDecimalDigits();
        System.out.println("digit = currVO.getCurrdigit().intValue() erro!");
      }

      if (getBillType().equals("30")) {
        this.uipanel.getBillTreeCardPanel().getBillData().getHeadItem("nsaleprice").setDecimalDigits(digit);

        this.uipanel.getBillTreeCardPanel().getBillData().getBodyItem("nprice").setDecimalDigits(digit);

        String name = this.uipanel.getBillTreeCardPanel().getBodyItem("nprice").getName();

        if (this.uipanel.getBillTreeCardPanel().getBodyPanel().hasShowCol(name))
        {
          this.uipanel.getBillTreeCardPanel().getBodyPanel().resetTableCellRenderer(name);
        }
      }
    }
    catch (Exception e) {
      SCMEnv.out("根据币种设置小数位数失败!");
    }
  }

  private void afterCconsignCorpEdit(int row)
  {
    String pk_corp = getBillCardTools().getHeadStringValue("pk_corp");
    if (pk_corp == null)
      return;
    String cconsigncorp = getBillCardTools().getBodyStringValue(row, "cconsigncorpid");

    if ((cconsigncorp == null) || (pk_corp.equals(cconsigncorp)))
    {
      getBillCardTools().clearBodyValue(new String[] { "cbodywarehousename", "cbodywarehouseid", "cadvisecalbodyid", "cadvisecalbody", "creccalbody", "creccalbodyid", "crecwarehouse", "crecwareid", "bdericttrans" }, row);

      getBillCardTools().setBodyValueByHead("ccalbodyid", row);

      getBillCardTools().setBodyValueByHead("cwarehouseid", row);
    }
    else {
      getBillCardTools().clearBodyValue(new String[] { "cbodywarehousename", "cbodywarehouseid", "cadvisecalbodyid", "cadvisecalbody" }, row);

      ((UIRefPane)getBodyItem("cadvisecalbody").getComponent()).getRefModel().setSelectedData(null);

      getBillCardTools().setBodyValue(new UFBoolean(false), row, "boosflag");

      getBillCardTools().setBodyValue(new UFBoolean(false), row, "bsupplyflag");
    }

    getBillCardTools().setBodyInventory1(row, 1);

    ((UIRefPane)getBodyItem("cadvisecalbody").getComponent()).getUITextField().setText(null);

    ((UIRefPane)getBodyItem("cbodywarehousename").getComponent()).getRefModel().setSelectedData(null);

    ctlUIOnCconsignCorpChg(row);
  }

  public void ctlUIOnCconsignCorpChg(int row)
  {
    String pk_corp = getBillCardTools().getHeadStringValue("pk_corp");
    if (pk_corp == null)
      return;
    String cconsigncorp = getBillCardTools().getBodyStringValue(row, "cconsigncorpid");

    if ((cconsigncorp == null) || (pk_corp.equals(cconsigncorp)))
    {
      getBillCardTools().setBodyCellsEdit(new String[] { "creccalbody", "crecwarehouse", "bdericttrans" }, row, false);

      getBillCardTools().setBodyCellsEdit(new String[] { "boosflag", "bsupplyflag" }, row, true);
    }
    else
    {
      getBillCardTools().setBodyCellsEdit(new String[] { "boosflag", "bsupplyflag" }, row, false);

      getBillCardTools().setBodyCellsEdit(new String[] { "creccalbody", "crecwarehouse", "bdericttrans" }, row, true);
    }
  }

  public void afterEmployeeEdit(BillEditEvent e)
  {
    UIRefPane cemployeeid = (UIRefPane)getHeadItem("cemployeeid").getComponent();

    UIRefPane refpane_dept = (UIRefPane)getHeadItem("cdeptid").getComponent();

    if ((cemployeeid != null) && (refpane_dept != null) && 
      (cemployeeid.getRefPK() != null)) {
      Object new_pkdeptid = cemployeeid.getRefValue("bd_psndoc.pk_deptdoc");

      if ((new_pkdeptid != null) && (new_pkdeptid.toString().trim().length() > 0))
      {
        String old_pkdeptid = refpane_dept.getRefPK();

        refpane_dept.getRefModel().setMatchPkWithWherePart(true);
        refpane_dept.setPK(new_pkdeptid);
        refpane_dept.getRefModel().setMatchPkWithWherePart(false);

        String new_cdeptname = refpane_dept.getUITextField().getText();

        if ((new_cdeptname == null) || (new_cdeptname.trim().length() <= 0))
        {
          refpane_dept.setPK(old_pkdeptid);
        }
      }
    }
  }

  private void afterFreeItemEdit(BillEditEvent e)
  {
    try
    {
      FreeVO voFree = getFreeItemRefPane().getFreeVO();

      for (int i = 0; i < 5; i++) {
        String fieldname = "vfree" + (i + 1);
        Object o = voFree.getAttributeValue(fieldname);
        setBodyValueAt(o, e.getRow(), fieldname);
      }
    } catch (Exception e2) {
      SCMEnv.out(e2);
    }
  }

  public FreeItemRefPane getFreeItemRefPane()
  {
    if (this.ivjFreeItemRefPane == null) {
      try {
        this.ivjFreeItemRefPane = new FreeItemRefPane();
        this.ivjFreeItemRefPane.setName("FreeItemRefPane");
        this.ivjFreeItemRefPane.setLocation(209, 4);
      } catch (Throwable ivjExc) {
        handleException(ivjExc);
      }
    }
    return this.ivjFreeItemRefPane;
  }

  public void afterFreeItemEditBom(BillEditEvent e)
  {
    try
    {
      FreeVO voFree = getFreeItemRefPane().getFreeVO();

      for (int i = 0; i < 5; i++) {
        String fieldname = "vfree" + (i + 1);
        Object o = voFree.getAttributeValue(fieldname);
        this.uipanel.getBillTreeCardPanel().setBodyValueAt(o, e.getRow(), fieldname);
      }
    }
    catch (Exception e2) {
      SCMEnv.out("将自由项填入表体出错!");
    }
  }

  private void afterProjectEdit(BillEditEvent e)
  {
    String[] clearCol = { "cprojectphaseid", "cprojectphasename" };
    clearRowData(e.getRow(), clearCol);
  }

  private void afterTaxrateBillEdit(BillEditEvent e)
  {
    for (int i = 0; i < getRowCount(); i++) {
      setBodyValueAt(e.getValue(), i, "ntaxrate");
      setBodyRowState(i);
      calculateNumber(i, "ntaxrate");
    }
  }

  private void afterUnitEdit(BillEditEvent e)
  {
    afterUnitEdit(e.getRow(), e.getKey());
    if (e.getKey().equals("cpackunitname"))
      setScaleEditableByRow(e.getRow());
  }

  private void afterUnitEdit(int eRow, String key)
  {
    String cunitid = (String)getBodyValueAt(eRow, "cunitid");
    if (("cinventorycode".equals(key)) || ("cpackunitname".equals(key))) {
      String cpackunitid = (String)getBodyValueAt(eRow, "cpackunitid");
      if ((cunitid != null) && (cpackunitid != null)) {
        if (cunitid.equals(cpackunitid)) {
          String[] formulas = new String[1];

          formulas[0] = "cpackunitname->getColValue(bd_measdoc,measname,pk_measdoc,cpackunitid)";

          execBodyFormulas(eRow, formulas);
          setBodyValueAt(new UFDouble(1), eRow, "scalefactor");
          setBodyValueAt(new UFBoolean(true), eRow, "fixedflag");
        } else {
          String[] formulas = new String[3];

          formulas[0] = "cpackunitname->getColValue(bd_measdoc,measname,pk_measdoc,cpackunitid)";

          formulas[1] = "scalefactor->getColValue2(bd_convert,mainmeasrate,pk_invbasdoc,cinvbasdocid,pk_measdoc,cpackunitid)";

          formulas[2] = "fixedflag->getColValue2(bd_convert,fixedflag,pk_invbasdoc,cinvbasdocid,pk_measdoc,cpackunitid)";
          execBodyFormulas(eRow, formulas);
        }
      }
      else {
        setBodyValueAt(null, eRow, "npacknumber");
        setBodyValueAt(null, eRow, "cpackunitid");
        setBodyValueAt(null, eRow, "cpackunitname");
      }

      InvVO voInv = null;
      if ((this.alInvs != null) && (this.alInvs.size() > eRow))
        voInv = (InvVO)this.alInvs.get(eRow);
      if (voInv != null) {
        voInv.setCastunitid(cpackunitid);
        voInv.setCastunitname((String)getBodyValueAt(eRow, "cpackunitname"));
      }
    }

    if (("cinventorycode".equals(key)) || ("cquoteunit".equals(key)))
    {
      String cquoteunitid = (String)getBodyValueAt(eRow, "cquoteunitid");
      if ((cunitid != null) && (cquoteunitid != null)) {
        if (cunitid.equals(cquoteunitid)) {
          String[] formulas = new String[1];

          formulas[0] = "cquoteunit->getColValue(bd_measdoc,measname,pk_measdoc,cquoteunitid)";

          execBodyFormulas(eRow, formulas);
          setBodyValueAt(new UFDouble(1), eRow, "bqtfixedflag");
          setBodyValueAt(new UFBoolean(true), eRow, "bqtfixedflag");
        }
        else
        {
          if (isBuyLargessLine(eRow)) {
            setBodyValueAt(new UFBoolean(false), eRow, "blargessflag");

            setBodyValueAt(null, eRow, "clargessrowno");
          }

          String[] formulas = new String[3];

          formulas[0] = "cquoteunit->getColValue(bd_measdoc,measname,pk_measdoc,cquoteunitid)";

          formulas[1] = "nqtscalefactor->getColValue2(bd_convert,mainmeasrate,pk_invbasdoc,cinvbasdocid,pk_measdoc,cquoteunitid)";

          formulas[2] = "bqtfixedflag->getColValue2(bd_convert,fixedflag,pk_invbasdoc,cinvbasdocid,pk_measdoc,cquoteunitid)";
          execBodyFormulas(eRow, formulas);
        }
      } else if ((cunitid != null) && (cquoteunitid == null)) {
        setBodyValueAt(cunitid, eRow, "cquoteunitid");
        setBodyValueAt(getBodyValueAt(eRow, "cunitname"), eRow, "cquoteunit");

        setBodyValueAt(new UFDouble(1.0D), eRow, "nqtscalefactor");
        setBodyValueAt(new UFBoolean(true), eRow, "bqtfixedflag");
      } else {
        setBodyValueAt(null, eRow, "cquoteunitid");
        setBodyValueAt(null, eRow, "cquoteunit");
        setBodyValueAt(null, eRow, "nqtscalefactor");
        setBodyValueAt(null, eRow, "bqtfixedflag");
      }

      Object sSource = getBodyValueAt(eRow, "creceipttype");
      if ((sSource == null) || (sSource.toString().trim().length() == 0)) {
        UFBoolean blar = new UFBoolean(getBodyValueAt(eRow, "blargessflag") == null ? "false" : getBodyValueAt(eRow, "blargessflag").toString());

        if ((blar == null) || (!blar.booleanValue())) {
          String sPk = (String)getBodyValueAt(eRow, "cinventoryid");

          int[] inewdelline = setBlargeLineWhenDelLine(new int[] { eRow });
          if ((inewdelline != null) && (inewdelline.length > 0)) {
            this.uipanel.onDelLine(inewdelline);
          }
          afterInventoryMutiEdit(eRow, new String[] { sPk }, false, false, null, true, 2);
        }
      }
    }
  }

  public void setScaleEditableByRow(int row)
  {
    String cpackunitid = (String)getBodyValueAt(row, "cpackunitid");
    if ((cpackunitid == null) || (cpackunitid.trim().length() == 0)) {
      setCellEditable(row, "scalefactor", false);
      return;
    }

    Object otemp = getBodyValueAt(row, "fixedflag");
    UFBoolean fixedflag = new UFBoolean(otemp == null ? "N" : otemp.toString());

    if (!fixedflag.booleanValue()) {
      setCellEditable(row, "scalefactor", true);
      BillItem bitem = getBodyItem("scalefactor");
      if (bitem != null)
        getBillCardTools().resumeBillBodyItemEdit(bitem);
    } else {
      setCellEditable(row, "scalefactor", false);
    }
  }

  private void afterVreceiveaddressEdit(BillEditEvent event)
  {
    for (int i = 0; i < getBillModel().getRowCount(); i++) {
      getBillCardTools().setBodyValueByHead("vreceiveaddress", i);
      if (getBillModel().getRowState(i) == 0)
        getBillModel().setRowState(i, 2);
    }
  }

  private void afterChangeotoarateEdit(BillEditEvent e)
  {
    for (int i = 0; i < getRowCount(); i++) {
      setBodyValueAt(getHeadItem("nexchangeotoarate").getValue(), i, "nexchangeotoarate");

      calculateNumber(i, "noriginalcursummny");

      setBodyRowState(i);
    }
  }

  private void afterChangeotobrateEdit(BillEditEvent e)
  {
    for (int i = 0; i < getRowCount(); i++) {
      setBodyValueAt(getHeadItem("nexchangeotobrate").getValue(), i, "nexchangeotobrate");

      calculateNumber(i, "noriginalcursummny");

      setBodyRowState(i);
    }
  }

  public void afterCcalbodyidEdit(BillEditEvent e)
  {
    if ((e == null) || (e.getPos() == 0)) {
      UIRefPane refCalbody = (UIRefPane)getHeadItem("ccalbodyid").getComponent();

      String sRefInitWhere = getBillCardTools().getHeadRefInitWhere("cwarehouseid");

      if ((sRefInitWhere == null) || (sRefInitWhere.trim().length() <= 0)) {
        sRefInitWhere = " 1=1 ";
      }
      if ("新增".equals(this.strState)) {
        sRefInitWhere = sRefInitWhere + " and (sealflag = 'N' or sealflag is null) ";
      }
      if (refCalbody.getRefPK() != null) {
        for (int i = 0; i < getBillModel().getRowCount(); i++)
        {
          if (getBillCardTools().isOtherCorpRow(i))
            continue;
          setBodyValueAt(refCalbody.getRefPK(), i, "cadvisecalbodyid");

          setBodyValueAt(refCalbody.getRefName(), i, "cadvisecalbody");

          setBodyValueAt(null, i, "cbodywarehouseid");
          setBodyValueAt(null, i, "cbodywarehousename");
          setBodyValueAt(null, i, "cbatchid");
          setBodyRowState(i);
        }

        UIRefPane wareRef = (UIRefPane)getHeadItem("cwarehouseid").getComponent();

        if (wareRef == null) {
          return;
        }
        if ("新增".equals(this.strState))
          wareRef.getRefModel().setSealedDataShow(false);
        else {
          wareRef.getRefModel().setSealedDataShow(true);
        }
        wareRef.getRefModel().setWherePart(sRefInitWhere + " and pk_calbody = '" + refCalbody.getRefPK() + "' ");
      }
      else
      {
        for (int i = 0; i < getBillModel().getRowCount(); i++)
        {
          if (getBillCardTools().isOtherCorpRow(i))
            continue;
          setBodyValueAt(null, i, "cadvisecalbodyid");
          setBodyValueAt(null, i, "cadvisecalbody");

          setBodyValueAt(null, i, "cbodywarehouseid");
          setBodyValueAt(null, i, "cbodywarehousename");
          setBodyValueAt(null, i, "cbatchid");
          setBodyRowState(i);
        }

        UIRefPane wareRef = (UIRefPane)getHeadItem("cwarehouseid").getComponent();

        if (wareRef == null) {
          return;
        }
        if ("新增".equals(this.strState))
          wareRef.getRefModel().setSealedDataShow(false);
        else {
          wareRef.getRefModel().setSealedDataShow(true);
        }
        wareRef.getRefModel().setWherePart(sRefInitWhere);
      }
    }
    else if ((e != null) && (e.getPos() == 1))
    {
      setBodyValueAt(null, e.getRow(), "cbodywarehouseid");
      setBodyValueAt(null, e.getRow(), "cbodywarehousename");
      setBodyValueAt(null, e.getRow(), "cbatchid");
    }
  }

  private void afterBodyAddressEdit(BillEditEvent ev)
  {
    UIRefPane vreceiveaddress = (UIRefPane)getBodyItem("vreceiveaddress").getComponent();

    if (vreceiveaddress.getRefPK() != null) {
      setBodyValueAt(vreceiveaddress.getRefValue("bd_custaddr.pk_areacl"), ev.getRow(), "creceiptareaid");

      setBodyValueAt(vreceiveaddress.getRefValue("bd_areacl.areaclname"), ev.getRow(), "creceiptareaname");
    }
  }

  private void afterBodyCreceiptcorpidEdit(BillEditEvent e)
  {
    try
    {
      String sBodyCreceiptcorpid = (String)(String)getBodyValueAt(e.getRow(), "creceiptcorpid");

      SOToolVO toolsvo = new SOToolVO();

      toolsvo.setAttributeValue("pk_cumandoc", sBodyCreceiptcorpid);

      toolsvo.setAttributeValue("pk_cubasdoc", "");

      toolsvo.setAttributeValue("pk_custaddr", "");

      toolsvo.setAttributeValue("vreceiveaddress", "");

      toolsvo.setAttributeValue("creceiptareaid", "");

      toolsvo.setAttributeValue("creceiptareaname", "");

      toolsvo.setAttributeValue("crecaddrnode", "");

      toolsvo.setAttributeValue("crecaddrnodename", "");

      toolsvo.setAttributeValue("defaddrflag", "Y");

      getBillCardTools().execFormulas(new String[] { "pk_cubasdoc->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,pk_cumandoc)" }, new SOToolVO[] { toolsvo });

      String pk_custaddr = BillTools.getColValue2("bd_custaddr", "pk_custaddr", "pk_cubasdoc", (String)toolsvo.getAttributeValue("pk_cubasdoc"), "defaddrflag", "Y");

      toolsvo.setAttributeValue("pk_custaddr", pk_custaddr);

      String[] formulas = { "vreceiveaddress->getColValue(bd_custaddr,addrname,pk_custaddr,pk_custaddr)", "crecaddrnode->getColValue(bd_custaddr,pk_address,pk_custaddr,pk_custaddr)", "creceiptareaid->getColValue(bd_custaddr,pk_areacl,pk_custaddr,pk_custaddr)", "crecaddrnodename->getColValue(bd_address,addrname,pk_address,crecaddrnode)", "creceiptareaname->getColValue(bd_areacl,areaclname,pk_areacl,creceiptareaid)" };

      getBillCardTools().execFormulas(formulas, new SOToolVO[] { toolsvo });

      setBodyValueAt(toolsvo.getAttributeValue("vreceiveaddress"), e.getRow(), "vreceiveaddress");

      setBodyValueAt(toolsvo.getAttributeValue("creceiptareaid"), e.getRow(), "creceiptareaid");

      setBodyValueAt(toolsvo.getAttributeValue("creceiptareaname"), e.getRow(), "creceiptareaname");

      setBodyValueAt(toolsvo.getAttributeValue("crecaddrnode"), e.getRow(), "crecaddrnode");

      setBodyValueAt(toolsvo.getAttributeValue("crecaddrnodename"), e.getRow(), "crecaddrnodename");
    }
    catch (Throwable ex)
    {
      handleException(ex);
    }
  }

  public void afterBodyDateEdit(int row, boolean isSendDate)
  {
    UFDate dateResult = getBillCardTools().getTransDate(row, isSendDate);
    if (isSendDate) {
      if ((dateResult != null) && (dateResult.toString().length() != 0))
        setBodyValueAt(dateResult, row, "ddeliverdate");
    }
    else if ((dateResult != null) && (dateResult.toString().length() != 0))
      setBodyValueAt(dateResult, row, "dconsigndate");
  }

  private void afterBatchEdit(BillEditEvent e)
  {
    Object fbatchstatus = getBodyItem("fbatchstatus").converType(getBodyValueAt(e.getRow(), "fbatchstatus"));

    if (SoVoConst.fbatchstatus_batchset.equals(fbatchstatus)) {
      setCellEditable(e.getRow(), "cbatchid", true);
    } else if (SoVoConst.fbatchstatus_batchall.equals(fbatchstatus)) {
      setCellEditable(e.getRow(), "cbatchid", false);
      setBodyValueAt(null, e.getRow(), "cbatchid");
    } else {
      setCellEditable(e.getRow(), "cbatchid", true);
    }
  }

  private void afterBatchIDEdit(BillEditEvent e)
  {
    UFDouble dNumber = getBodyValueAt(e.getRow(), "nnumber") == null ? new UFDouble(0) : (UFDouble)(UFDouble)getBodyValueAt(e.getRow(), "nnumber");

    if (dNumber.compareTo(new UFDouble(0)) < 0)
      return;
  }

  private void afterBdericttransEdit(BillEditEvent e)
  {
    getBillCardTools().clearBodyValue(new String[] { "crecwareid", "crecwarehouse" }, e.getRow());

    if ((new UFBoolean(e.getValue().toString()).booleanValue()) && (getBodyValueAt(e.getRow(), "creccalbodyid") != null) && (getBodyValueAt(e.getRow(), "creccalbodyid").toString().trim().length() > 0))
    {
      String[] sFormula = { "crecwareid->getColValue2(bd_stordoc,pk_stordoc,pk_calbody,creccalbodyid,isdirectstore,\"Y\")", "crecwarehouse->getColValue(bd_stordoc,storname,pk_stordoc,crecwareid)" };

      execBodyFormulas(e.getRow(), sFormula);
    }
  }

  private void afterCwarehouseidEdit(BillEditEvent e)
  {
    if ((e == null) || (e.getPos() == 0)) {
      UIRefPane refWare = (UIRefPane)getHeadItem("cwarehouseid").getComponent();

      if (refWare.getRefPK() != null) {
        for (int i = 0; i < getBillModel().getRowCount(); i++) {
          String cconsigncorpid = (String)getBodyValueAt(i, "cconsigncorpid");

          if (((cconsigncorpid != null) && (cconsigncorpid.trim().length() > 0) && (!cconsigncorpid.equals(this.pk_corp))) || (refWare.getRefPK().equals(getBodyValueAt(i, "cbodywarehouseid"))))
          {
            continue;
          }

          setBodyValueAt(refWare.getRefPK(), i, "cbodywarehouseid");

          setBodyValueAt(refWare.getRefName(), i, "cbodywarehousename");

          setBodyValueAt(null, i, "cbatchid");
          setBodyRowState(i);
        }
      }

    }
    else
    {
      String cbodywarehouseid = getBillCardTools().getBodyStringValue(e.getRow(), "cbodywarehouseid");

      if ((cbodywarehouseid == null) || (cbodywarehouseid.trim().length() <= 0))
      {
        setBodyValueAt(null, e.getRow(), "cbatchid");
        setBodyValueAt(null, e.getRow(), "cbodywarehouseid");
        setBodyValueAt(null, e.getRow(), "cbodywarehousename");
      }
      else
      {
        setBodyValueAt(null, e.getRow(), "cbatchid");

        String[] formulas = { "cadvisecalbodyid->getColValue(bd_stordoc,pk_calbody,pk_stordoc,cbodywarehouseid)", "cadvisecalbody->getColValue(bd_calbody,bodyname,pk_calbody,cadvisecalbodyid)" };

        execBodyFormulas(e.getRow(), formulas);
      }
    }
  }

  private void afterCtransmodeEdit(BillEditEvent event)
  {
    for (int i = 0; i < getBillModel().getRowCount(); i++) {
      String sDateDeliver = getBodyValueAt(i, "ddeliverdate") == null ? null : getBodyValueAt(i, "ddeliverdate").toString().trim();

      if ((sDateDeliver != null) && (sDateDeliver.length() != 0))
        afterBodyDateEdit(i, false);
    }
  }

  public void afterCurrencyChange()
  {
    UIRefPane ccurrencytypeid = (UIRefPane)getHeadItem("ccurrencytypeid").getComponent();

    setHeadItem("nexchangeotobrate", null);
    setHeadItem("nexchangeotoarate", null);
    getHeadItem("nexchangeotoarate").setEnabled(true);

    setPanelByCurrency(ccurrencytypeid.getRefPK());
    try {
      if ((this.uipanel.BD302 == null) || (!this.uipanel.BD302.booleanValue()))
      {
        setHeadItem("nexchangeotobrate", this.uipanel.currtype.getRate(ccurrencytypeid.getRefPK(), null, getHeadItem("dbilldate").getValue()));

        setHeadItem("nexchangeotoarate", null);

        if (this.uipanel.currtype.isLocalCurrType(ccurrencytypeid.getRefPK()))
        {
          getHeadItem("nexchangeotobrate").setEnabled(false);
        }
        else getHeadItem("nexchangeotobrate").setEnabled(true);

        getHeadItem("nexchangeotoarate").setEnabled(false);
      } else {
        UFDouble dCurr0 = this.uipanel.currtype.getRate(ccurrencytypeid.getRefPK(), this.uipanel.currtype.getLocalCurrPK(), getHeadItem("dbilldate").getValue());

        UFDouble dCurr1 = this.uipanel.currtype.getRate(ccurrencytypeid.getRefPK(), this.uipanel.currtype.getFracCurrPK(), getHeadItem("dbilldate").getValue());

        setHeadItem("nexchangeotobrate", dCurr0);
        setHeadItem("nexchangeotoarate", dCurr1 == null ? new UFDouble(0) : dCurr1);

        SCMEnv.out("折本汇率：" + dCurr0);
        SCMEnv.out("折辅汇率：" + dCurr1);

        if (this.uipanel.currtype.isFracCurrType(ccurrencytypeid.getRefPK())) {
          getHeadItem("nexchangeotoarate").setEnabled(false);
          getHeadItem("nexchangeotobrate").setEnabled(true);
        }
        else if (this.uipanel.currtype.isLocalCurrType(ccurrencytypeid.getRefPK()))
        {
          getHeadItem("nexchangeotobrate").setEnabled(false);
          getHeadItem("nexchangeotoarate").setEnabled(false);
        } else {
          getHeadItem("nexchangeotobrate").setEnabled(true);
          getHeadItem("nexchangeotoarate").setEnabled(true);
        }

      }

      for (int i = 0; i < getRowCount(); i++)
      {
        setBodyValueAt(ccurrencytypeid.getRefPK(), i, "ccurrencytypeid");

        setBodyValueAt(ccurrencytypeid.getRefName(), i, "ccurrencytypename");

        setBodyValueAt(getHeadItem("nexchangeotobrate").getValue(), i, "nexchangeotobrate");

        setBodyValueAt(getHeadItem("nexchangeotoarate").getValue(), i, "nexchangeotoarate");

        setBodyRowState(i);
      }
    } catch (Exception e1) {
      SCMEnv.out("获得汇率失败！");
    }
  }

  private void afterRownoEdit(BillEditEvent e)
  {
    BillRowNo.afterEditWhenRowNo(this, e, getBillType());
    String sOldrow = (String)e.getOldValue();
    if (sOldrow == null)
      return;
    String sRow = null;
    UFBoolean bLargess = null;
    for (int i = 0; i < getRowCount(); i++) {
      sRow = (String)getBodyValueAt(i, "clargessrowno");
      bLargess = new UFBoolean(getBodyValueAt(i, "blargessflag") == null ? "false" : getBodyValueAt(i, "blargessflag").toString());

      if ((sRow != null) && (bLargess.booleanValue()) && (sRow.equals(sOldrow)))
        setBodyValueAt(e.getValue(), i, "clargessrowno");
    }
  }

  public void afterCreceiptcorpEdit()
  {
    UIRefPane vreceiveaddress = (UIRefPane)getHeadItem("vreceiveaddress").getComponent();

    vreceiveaddress.setAutoCheck(false);

    String creceiptcustomerid = null;
    if (getHeadItem("creceiptcustomerid") != null) {
      creceiptcustomerid = getHeadItem("creceiptcustomerid").getValue();
    }
    ((CustAddrRefModel)vreceiveaddress.getRefModel()).setCustId(creceiptcustomerid);

    String formula = "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcustomerid)";
    String pk_cubasdoc = (String)execHeadFormula(formula);

    SOToolVO toolsvo = new SOToolVO();

    toolsvo.setAttributeValue("pk_cumandoc", creceiptcustomerid);

    toolsvo.setAttributeValue("pk_cubasdoc", pk_cubasdoc);

    toolsvo.setAttributeValue("pk_custaddr", "");

    toolsvo.setAttributeValue("crecaddrnode", "");

    toolsvo.setAttributeValue("crecaddrnodename", "");

    String pk_custaddr = BillTools.getColValue2("bd_custaddr", "pk_custaddr", "pk_cubasdoc", pk_cubasdoc, "defaddrflag", "Y");

    toolsvo.setAttributeValue("pk_custaddr", pk_custaddr);

    String[] formulas = { "crecaddrnode->getColValue(bd_custaddr,pk_address,pk_custaddr,pk_custaddr)", "crecaddrnodename->getColValue(bd_address,addrname,pk_address,crecaddrnode)" };

    getBillCardTools().execFormulas(formulas, new SOToolVO[] { toolsvo });

    if ((vreceiveaddress.getUITextField().getText() == null) || (vreceiveaddress.getUITextField().getText().trim().length() <= 0))
    {
      vreceiveaddress.setPK(pk_custaddr);
    }

    afterVreceiveaddressEdit(null);
    if (getHeadItem("creceiptcustomerid") != null) {
      UIRefPane refCreceiptcorpid = (UIRefPane)getHeadItem("creceiptcustomerid").getComponent();

      if (refCreceiptcorpid.getRefPK() != null)
      {
        String creceiptcorpid = null;
        String vreceiveaddressbody = null;

        for (int i = 0; i < getBillModel().getRowCount(); i++)
        {
          creceiptcorpid = (String)getBodyValueAt(i, "creceiptcorpid");

          if ((creceiptcorpid == null) || (creceiptcorpid.trim().length() <= 0))
          {
            setBodyValueAt(refCreceiptcorpid.getRefPK(), i, "creceiptcorpid");

            setBodyValueAt(refCreceiptcorpid.getRefName(), i, "creceiptcorpname");

            setBodyValueAt(toolsvo.getAttributeValue("crecaddrnode"), i, "crecaddrnode");

            setBodyValueAt(toolsvo.getAttributeValue("crecaddrnodename"), i, "crecaddrnodename");

            setBodyRowState(i);
          }

          vreceiveaddressbody = (String)getBodyValueAt(i, "vreceiveaddress");

          if ((vreceiveaddressbody != null) && (vreceiveaddressbody.trim().length() > 0))
            continue;
          getBillCardTools().setBodyValueByHead("vreceiveaddress", i);

          setBodyRowState(i);
        }
      }
    }
  }

  private void afterCrecwarehouseEdit(BillEditEvent e)
  {
    if (e != null) {
      String crecwareid = getBillCardTools().getBodyStringValue(e.getRow(), "crecwareid");

      if ((crecwareid != null) && (crecwareid.trim().length() > 0))
      {
        String[] formulas = { "creccalbodyid->getColValue(bd_stordoc,pk_calbody,pk_stordoc,crecwareid)", "creccalbody->getColValue(bd_calbody,bodyname,pk_calbody,creccalbodyid)" };

        execBodyFormulas(e.getRow(), formulas);
      }
    }
  }

  protected void afterHeadsummnyEdit(BillEditEvent e)
  {
    UFDouble udOld = getNumPriceDisSummny();

    UFDouble udNew = e.getValue() == null ? new UFDouble(0) : new UFDouble(e.getValue().toString());

    UFDouble udDiscountRate = (udOld.doubleValue() == 0.0D ? new UFDouble(1) : udNew.div(udOld)).multiply(100.0D);

    getHeadItem("ndiscountrate").setValue(udDiscountRate);

    int iLastrow = getRowCount() - 1;
    for (int i = iLastrow; (i >= 0) && (
      (getBodyValueAt(i, "cinventoryid") == null) || (getBodyValueAt(i, "cinventoryid").toString().length() <= 0)); i--)
    {
      iLastrow--;
    }
    if (iLastrow < 0) {
      return;
    }

    afterDiscountrateEdit(null);
    getBillModel().setNeedCalculate(true);
    getBillModel().setNeedCalculate(false);

    UFDouble udLast = getTotalValue("noriginalcursummny");

    if ((udLast == null) || (udLast.doubleValue() != udNew.doubleValue()))
    {
      UFDouble udModify = udNew.sub(udLast);
      UFDouble udNow = (UFDouble)getBodyValueAt(iLastrow, "noriginalcursummny");

      udNow = (udNow == null ? new UFDouble(0) : udNow).add(udModify);
      setBodyValueAt(udNow, iLastrow, "noriginalcursummny");
      afterNumberEdit(new int[] { iLastrow }, "noriginalcursummny", null, false, true);
    }
  }

  private UFDouble getNumPriceDisSummny()
  {
    UFDouble ud = new UFDouble(0);
    UFDouble udt = null;
    int icout = getRowCount();
    for (int i = 0; i < icout; i++) {
      udt = getBodyValueAt(i, "noriginalcurtaxprice") == null ? new UFDouble(0) : (UFDouble)getBodyValueAt(i, "noriginalcurtaxprice");

      udt = udt.multiply(getBodyValueAt(i, "nnumber") == null ? new UFDouble(0) : (UFDouble)getBodyValueAt(i, "nnumber"));

      udt = udt.multiply(getBodyValueAt(i, "nitemdiscountrate") == null ? new UFDouble(0) : (UFDouble)getBodyValueAt(i, "nitemdiscountrate")).div(100.0D);

      ud = ud.add(udt);
    }

    return ud;
  }

  public UFDouble getTotalValue(String sItemKey)
  {
    int iCol = getBillModel().getBodyColByKey(sItemKey);

    UFDouble udLast = new UFDouble(0);

    int i = 0; for (int len = getBillModel().getRowCount(); i < len; i++)
    {
      Object blargessflag = getBodyValueAt(i, "blargessflag");

      if ((blargessflag != null) && (((Boolean)blargessflag).booleanValue())) {
        continue;
      }
      UFDouble tmp = (UFDouble)getBillModel().getValueAt(i, iCol);
      if (tmp != null) {
        udLast = udLast.add(tmp);
      }
    }
    return udLast;
  }

  private void afterInventoryMutiEdit(BillEditEvent e)
  {
    if ((e.getOldValue() != null) && (e.getValue() != null) && (!e.getOldValue().equals(e.getValue())) && (isBuyLargessLine(e.getRow())))
    {
      setBodyValueAt(new UFBoolean(false), e.getRow(), "blargessflag");
      setBodyValueAt(null, e.getRow(), "clargessrowno");
    }

    int[] inewdelline = setBlargeLineWhenDelLine(new int[] { e.getRow() });
    if ((inewdelline != null) && (inewdelline.length > 0)) {
      this.uipanel.onDelLine(inewdelline);
    }  
    afterInventoryMutiEdit(e.getRow(), 1);
  }

  private boolean isBuyLargessLine(int i)
  {
    String sRow = (String)getBodyValueAt(i, "clargessrowno");
    UFBoolean bLargess = new UFBoolean(getBodyValueAt(i, "blargessflag") == null ? "false" : getBodyValueAt(i, "blargessflag").toString());

    return (bLargess != null) && (bLargess.booleanValue()) && (sRow != null) && (sRow.trim().length() > 0);
  }

  private void afterInventoryMutiEdit(int irow, int iChg)
  {
    String[] refPks = ((UIRefPane)getBodyItem("cinventorycode").getComponent()).getRefPKs();

    afterInventoryMutiEdit(irow, refPks, true, true, null, true, iChg);

    int i = irow; for (int len = irow + refPks.length; i < len; i++)
      ctlUIOnCconsignCorpChg(i);
  }

  private void afterInventoryMutiEdit(int irow, String[] refPks, boolean bMain, boolean bBinds, String sFormulakey, boolean bNeedFindPrice, int iChg)
  {
    if (refPks == null) {
      return;
    }
    long s = System.currentTimeMillis();
    SCMEnv.out("读取存货相关信息开始...");
    ArrayList alist = setBodyByinvs(refPks, irow, bMain, bBinds, iChg);
    SCMEnv.out("循环设置表体[共用时" + (System.currentTimeMillis() - s) + "]");
    if (alist == null) {
      return;
    }

    setDefaultCtItem(irow, alist.size());

    if ((this.uipanel.SO_01 != null) && (this.uipanel.SO_01.intValue() != 0) && (getRowCount() - 1 > this.uipanel.SO_01.intValue()))
    {
      this.uipanel.showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000171", null, new String[] { this.uipanel.SO_01.intValue() + "" }));
    }

    s = System.currentTimeMillis();
    SCMEnv.out("设置其他相关信息开始...");

    if ((getBillCardTools().getEditInvfomulas_add() != null) && (getBillCardTools().getEditInvfomulas_add().length > 0))
    {
      getBillCardTools().execBodyFormulas(getBillCardTools().getEditInvfomulas_add(), alist);
    }

    if ((getBillType().equals("30")) || (getBillType().equals("3A")))
    {
      String[] appendFormula = { "wholemanaflag->getColValue(bd_invmandoc,wholemanaflag,pk_invmandoc,cinventoryid)", "isconfigable->getColValue(bd_invmandoc,isconfigable,pk_invmandoc,cinventoryid)", "isspecialty->getColValue(bd_invmandoc,isspecialty,pk_invmandoc,cinventoryid)" };

      getBillCardTools().execBodyFormulas(appendFormula, alist);
    }

    if ((getBillType().equals("30")) || (getBillType().equals("3A")))
    {
      Object temp = getBodyValueAt(irow, "wholemanaflag");
      boolean wholemanaflag = temp == null ? false : new UFBoolean(temp.toString()).booleanValue();

      setCellEditable(irow, "fbatchstatus", wholemanaflag);
      setCellEditable(irow, "cbatchid", wholemanaflag);

      temp = getBodyValueAt(irow, "isconfigable");
      boolean isconfigable = temp == null ? false : new UFBoolean(temp.toString()).booleanValue();

      this.uipanel.boBom.setEnabled(isconfigable);
    }
    this.uipanel.updateButton(this.uipanel.boBom);

    String[] appendFormula = { "cprolineid->getColValue(bd_invbasdoc,pk_prodline,pk_invbasdoc,cinvbasdocid)", "laborflag->getColValue(bd_invbasdoc,laborflag,pk_invbasdoc,cinvbasdocid)", "discountflag->getColValue(bd_invbasdoc,discountflag,pk_invbasdoc,cinvbasdocid)", "isappendant->getColValue(bd_invmandoc,isappendant,pk_invmandoc,cinventoryid)", "nreturntaxrate->getColValue(bd_invmandoc,expaybacktax,pk_invmandoc,cinventoryid)", "cprolinename->getColValue(bd_prodline,prodlinename,pk_prodline,cprolineid)" };

    getBillCardTools().execBodyFormulas(appendFormula, alist);

    loadTaxtrate(alist);

    int i = irow; for (int iLen = irow + alist.size(); i < iLen; i++) {
      afterCconsignCorpEdit(i);
    }
    getBillCardTools().setBodyInventory1(irow, alist.size());

    getBillCardTools().setBodyCchantypeid(irow, alist.size());

    SCMEnv.out("设置其他相关信息[共用时" + (System.currentTimeMillis() - s) + "]");
    s = System.currentTimeMillis();
    SCMEnv.out("设置单位转化信息开始...");

    afterInventorysEdit(irow, irow + alist.size(), "ntaxrate", bNeedFindPrice);

    SCMEnv.out("设置单位转化信息[共用时" + (System.currentTimeMillis() - s) + "]");

    setCalByConCalset(this.m_hConCal, irow, irow + alist.size());
    getBillCardTools().execBodyFormulas(new String[] { "cconsigncorp->getColValue(bd_corp,unitname,pk_corp,cconsigncorpid)", "cadvisecalbody->getColValue(bd_calbody,bodyname,pk_calbody,cadvisecalbodyid)", "cbodywarehousename->getColValue(bd_stordoc,storname,pk_stordoc,cbodywarehouseid)" }, alist);

    updateUI();
  }

  protected void setCalByConCalset(HashMap htinvcal, int startrow, int stoprow)
  {
    String sInv = null;
    InvcalbodyVO invbvo = null;
    this.m_hConCal = htinvcal;
    for (int i = startrow; i < stoprow; i++) {
      sInv = (String)getBodyValueAt(i, "cinventoryid");
      if ((sInv == null) || (!htinvcal.containsKey(sInv)))
        continue;
      invbvo = (InvcalbodyVO)htinvcal.get(sInv);

      if (invbvo == null)
      {
        continue;
      }
      setBodyValueAt(invbvo.getCreceiptcorpid(), i, "cconsigncorpid");

      setBodyValueAt(invbvo.getCcalbodyid(), i, "cadvisecalbodyid");

      setBodyValueAt(invbvo.getCwarehouseid(), i, "cbodywarehouseid");
    }
  }

  private ArrayList setBodyByinvs(String[] refPks, int irow, boolean bMain, boolean bBinds, int iChg)
  {
    try
    {
      long s = System.currentTimeMillis();
      long s0 = s;
      SCMEnv.out("读取捆绑买赠信息开始...");

      ArrayList al = getLargessAndBindingsByInvs(refPks, irow, iChg);
      SCMEnv.out("读取捆绑买赠信息用时[" + (System.currentTimeMillis() - s) / 1000.0D + "s]");

      s = System.currentTimeMillis();

      String[] sBinds = (String[])(String[])al.get(0);

      Hashtable htbinds = (Hashtable)al.get(1);

      BuylargessVO[] bvos = (BuylargessVO[])(BuylargessVO[])al.get(2);

      Hashtable htLargess = new Hashtable();
      Vector vt = new Vector();
      for (int i = 0; i < sBinds.length; i++) {
        vt.add(sBinds[i]);
      }

      for (int i = 0; i < refPks.length; i++) {
        vt.add(refPks[i]);
      }

      String larkey = null;
      if ((bvos != null) && (bvos.length > 0)) {
        BuylargessBVO[] bodys = null;
        ArrayList allargess = null;
        for (int i = 0; i < bvos.length; i++)
        {
          larkey = ((BuylargessHVO)bvos[i].getParentVO()).getPk_invmandoc() + ((BuylargessHVO)bvos[i].getParentVO()).getCunitid();

          if (htLargess.containsKey(larkey)) {
            allargess = (ArrayList)htLargess.get(larkey);
            allargess.add(bvos[i]);
          } else {
            allargess = new ArrayList();
            allargess.add(bvos[i]);
            htLargess.put(larkey, allargess);
          }

          bodys = (BuylargessBVO[])(BuylargessBVO[])bvos[i].getChildrenVO();
          for (int j = 0; j < bodys.length; j++) {
            if (!vt.contains(bodys[j].getPk_invmandoc())) {
              vt.add(bodys[j].getPk_invmandoc());
            }
          }
        }
      }

      String[] snewPks = new String[vt.size()];
      vt.copyInto(snewPks);

      InvVO[] invvos = new InvVO[snewPks.length];
      for (int i = 0; i < invvos.length; i++) {
        invvos[i] = new InvVO();
        invvos[i].setCinventoryid(snewPks[i]);
      }

      SCMEnv.out("分析捆绑买赠数据用时[" + (System.currentTimeMillis() - s) / 1000.0D + "s]");

      s = System.currentTimeMillis();

      InvoInfoBYFormula invvosget = new InvoInfoBYFormula();
      invvos = invvosget.getQuryInvVOs(invvos, false, true, 1);

      Hashtable htresult = new Hashtable();
      for (int k = 0; k < invvos.length; k++) {
        htresult.put(invvos[k].getCinventoryid(), invvos[k]);
      }
      SCMEnv.out("查询存货信息用时[" + (System.currentTimeMillis() - s) / 1000.0D + "s]");

      s = System.currentTimeMillis();

      int count = 0;

      if (bMain) {
        count += setBodyByOrginvs(refPks, htresult, irow);
        count--;
        SCMEnv.out("设置主存货用时[" + (System.currentTimeMillis() - s) / 1000.0D + "s]");

        s = System.currentTimeMillis();
      }

      int startrow = irow;
      int stoprow = irow + count;
      if (bBinds)
      {
        if ((htbinds.size() > 0) && 
          (this.uipanel.showOkCancelMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000515")) == 1))
        {
          s0 -= System.currentTimeMillis() - s;
          s = System.currentTimeMillis();
          for (int i = stoprow; i >= startrow; i--) {
            count += setBodyByBindinvs(i, htresult, htbinds);
          }
          SCMEnv.out("设置捆绑件用时[" + (System.currentTimeMillis() - s) / 1000.0D + "s]");

          s = System.currentTimeMillis();
        }

      }

      stoprow = irow + count;

      for (int i = stoprow; i >= startrow; i--) {
        count += setBodyByLargessinvs(i, htresult, htLargess);
      }

      SCMEnv.out("设置买赠件用时[" + (System.currentTimeMillis() - s) / 1000.0D + "s]");

      s = System.currentTimeMillis();

      if (al.size() >= 4) {
        this.m_hConCal = ((HashMap)al.get(3));
      }

      ArrayList alist = new ArrayList();
      stoprow = irow + count;

      for (int i = startrow; i <= stoprow; i++)
      {
        if ((!bMain) && (i == irow)) {
          continue;
        }
        alist.add(new Integer(i));
      }

      if ((bMain) && (this.uipanel.boAddLine.isEnabled()) && ((this.uipanel.SO_01 == null) || (this.uipanel.SO_01.intValue() == 0) || (this.uipanel.SO_01.intValue() > getRowCount())))
      {
        if (irow + alist.size() < getBillModel().getRowCount()) {
          getBillTable().getSelectionModel().setSelectionInterval(irow + alist.size(), irow + alist.size());

          getBillTable().getSelectionModel().setSelectionInterval(irow, irow);
        }
        else
        {
          this.uipanel.onAddLine();
        }
      }
      SCMEnv.out("读取设置捆绑买赠共用时[" + (System.currentTimeMillis() - s0) / 1000.0D + "s]");

      s = System.currentTimeMillis();

      return alist;
    }
    catch (Exception e) {
      SCMEnv.out(e);

      if ((e instanceof BusinessException))
        this.uipanel.showWarningMessage(e.getMessage());
      else
        this.uipanel.showErrorMessage(e.getMessage()); 
    }
    return null;
  }

  private ArrayList getLargessAndBindingsByInvs(String[] refPks, int icurrow, int iChgInv)
    throws Exception
  {
    String dBillDate = null;
    IBuyLargess buyLargess = (IBuyLargess)NCLocator.getInstance().lookup(IBuyLargess.class.getName());

    dBillDate = getHeadItem("dbilldate").getValue() == null ? ClientEnvironment.getInstance().getBusinessDate().toString() : getHeadItem("dbilldate").getValue().toString();

    ArrayList alParam = new ArrayList();

    alParam.add(new Integer(iChgInv));

    alParam.add(ClientEnvironment.getInstance().getCorporation().getPk_corp());

    alParam.add(dBillDate);

    if (getHeadItem("ccustomerid").getValue() != null)
      alParam.add(getHeadItem("ccustomerid").getValue());
    else {
      alParam.add("");
    }
    String ccurrencytypeid = null;
    String cchantypeid = null;
    ccurrencytypeid = (String)getBodyValueAt(icurrow, "ccurrencytypeid");

    if (ccurrencytypeid == null)
      alParam.add("");
    else
      alParam.add(ccurrencytypeid);
    cchantypeid = (String)getBodyValueAt(icurrow, "cchantypeid");

    if (cchantypeid == null)
      alParam.add("");
    else {
      alParam.add(cchantypeid);
    }

    if (getHeadItem("csalecorpid").getValue() != null)
      alParam.add(getHeadItem("csalecorpid").getValue());
    else {
      alParam.add("");
    }
    ArrayList al = buyLargess.getLargessAndBindingsByInvs(refPks, alParam);
    if ((al.get(2) instanceof BuylargessVO[])) {
      BuylargessVO[] bvos = (BuylargessVO[])(BuylargessVO[])al.get(2);
      if ((bvos != null) && (bvos.length > 0)) {
        for (int i = 0; i < bvos.length; i++) {
          this.m_htLargess.put(((BuylargessHVO)bvos[i].getParentVO()).getPk_invmandoc(), bvos[i]);
        }
      }

    }

    return al;
  }

  private int setBodyByOrginvs(String[] refPks, Hashtable htresult, int irow)
  throws Exception
{
  if ((refPks == null) || (refPks.length == 0)) {
    return 0;
  }
  int count = refPks.length;

  if (irow == getRowCount() - 1)
    this.uipanel.addNullLine(irow, count - 1);
  else {
    this.uipanel.insertNullLine(irow, count - 1);
  }
/*  
  //  -------------------------  Add By DB 2012.09.16  ----------------------------------
  if (getSouceBillType().equals("Z4")) //如果是来源于销售合同, 不清0、及设置默认值，直接返回
  {
  	return count;
  }
  //-------------------------  Add By DB 2012.09.16  ----------------------------------
*/  
  if(!getSouceBillType().equals("Z4"))
  {
	  afterInvEditClear(irow);
  }

  InvVO tmpvo = null;
  setBodyDefaultData(irow, irow + count);
  for (int i = 0; i < count; i++)
  {
    tmpvo = (InvVO)htresult.get(refPks[i]);
    setBodyValues(tmpvo, irow + i, irow);
  }

  return count;
}

  private void afterInvEditClear(int iRowIndex)
  {
    String[] clearCol = { "scalefactor", "fixedflag", "nnumber", "npacknumber", "norgviaprice", "norgviapricetax", "noriginalcurprice", "noriginalcurtaxprice", "noriginalcurnetprice", "noriginalcurtaxnetprice", "noriginalcurtaxmny", "noriginalcurmny", "noriginalcursummny", "noriginalcurdiscountmny", "nqtscalefactor", "bqtfixedflag", "nquoteunitnum", "norgqttaxprc", "norgqtprc", "norgqttaxnetprc", "norgqtnetprc", "cbatchid", "vfree0", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5", "cinvclassid", "cinvclasscode", "cpricepolicyid", "cpricepolicy", "cpriceitemid", "cpriceitem", "cpriceitemtable", "cpriceitemtablename", "cpricecalproc", "cinventoryid1", "bsafeprice", "ntaldcnum", "nasttaldcnum", "ntaldcmny", "breturnprofit", "nretprofnum", "nastretprofnum", "nretprofmny", "cpricepolicyid", "cpricepolicy", "cpriceitemid", "cpriceitem", "cpriceitemtable", "cpriceitemtablename", "cpricecalproc", "cpricecalprocname", "cquoteunitid", "cquoteunit", "bindpricetype" };

    Object ctinvclassid = null;
    try {
      ctinvclassid = getBodyValueAt(iRowIndex, "ctinvclassid");
    } catch (Exception e) {
      ctinvclassid = null;
    }

    if ((ctinvclassid != null) && (ctinvclassid.toString().length() != 0))
    {
      if (this.uipanel.SO_17.booleanValue())
      {
        clearCol = new String[] { "scalefactor", "fixedflag", "noriginalcurtaxmny", "noriginalcurmny", "noriginalcursummny", "noriginalcurdiscountmny", "cbatchid", "vfree0", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5", "cinvclassid", "cinvclasscode", "nqtscalefactor", "bqtfixedflag", "cpricepolicyid", "cpricepolicy", "cpriceitemid", "cpriceitem", "cpriceitemtable", "cpriceitemtablename", "cpricecalproc", "cinventoryid1", "bsafeprice", "ntaldcnum", "nasttaldcnum", "ntaldcmny", "breturnprofit", "nretprofnum", "nastretprofnum", "nretprofmny", "cquoteunitid", "cquoteunit" };
      }

    }
    else if ((getSouceBillType().equals("Z4")) || (getSouceBillType().equals("Z3")))
    {
      clearCol = new String[] { "scalefactor", "fixedflag", "nqtscalefactor", "bqtfixedflag", "noriginalcurtaxmny", "noriginalcurmny", "noriginalcursummny", "noriginalcurdiscountmny", "cbatchid", "vfree0", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5", "cinvclassid", "cinvclasscode", "cpricepolicyid", "cpricepolicy", "cpriceitemid", "cpriceitem", "cpriceitemtable", "cpriceitemtablename", "cpricecalproc", "cinventoryid1", "bsafeprice", "ntaldcnum", "nasttaldcnum", "ntaldcmny", "breturnprofit", "nretprofnum", "nastretprofnum", "nretprofmny", "cpricepolicyid", "cpricepolicy", "cpriceitemid", "cpriceitem", "cpriceitemtable", "cpriceitemtablename", "cpricecalproc", "cpricecalprocname", "cquoteunitid", "cquoteunit" };
    }

    getBillCardTools().clearRowData(iRowIndex, clearCol);
    if (PfUtilClient.makeFlag)
    {
      String[] clearCol2 = { "creceipttype", "csourcebillid", "csourcebillbodyid" };

      getBillCardTools().clearRowData(iRowIndex, clearCol2);
    }
  }

  public void setBodyDefaultData(int istartrow, int iendrow)
  {
    SOToolVO vo = null;
    String crecaddrnode = null;
    String crecaddrnodename = null;
    try
    {
      UIRefPane refVreceiveaddress = (UIRefPane)getHeadItem("vreceiveaddress").getComponent();

      String vreceiveaddressid = refVreceiveaddress.getRefPK();

      if ((vreceiveaddressid != null) && (vreceiveaddressid.trim().length() > 0))
      {
        String vreceiveaddress = (String)getBodyValueAt(istartrow, "vreceiveaddress");

        setBodyValueAt(vreceiveaddressid, istartrow, "vreceiveaddress");
        String[] fs = { "crecaddrnode->getColValue(bd_custaddr,pk_address,pk_custaddr,vreceiveaddress)", "crecaddrnodename->getColValue(bd_address,addrname,pk_address,crecaddrnode)" };

        getBillModel().execFormulas(istartrow, fs);
        crecaddrnode = getBillCardTools().getBodyStringValue(istartrow, "crecaddrnode");

        crecaddrnodename = getBillCardTools().getBodyStringValue(istartrow, "crecaddrnodename");

        setBodyValueAt(vreceiveaddress, istartrow, "vreceiveaddress");
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
    }

    if (istartrow > 0)
    {
      String vreceiveaddress = (String)getBodyValueAt(0, "vreceiveaddress");

      String creceiptareaid = (String)getBodyValueAt(0, "creceiptareaid");

      String creceiptareaname = (String)getBodyValueAt(0, "creceiptareaname");

      String crecaddrnode_body = (String)getBodyValueAt(0, "crecaddrnode");

      String crecaddrnodename_body = (String)getBodyValueAt(0, "crecaddrnodename");

      for (int i = istartrow; i < iendrow; i++)
      {
        vo = getBillCardTools().getBodyDefaultData(i);
        getBillCardTools().setBodyValuesByVO(i, vo);
        setBodyValueAt(crecaddrnode, i, "crecaddrnode");
        setBodyValueAt(crecaddrnodename, i, "crecaddrnodename");

        if (SoVoTools.isEmptyString((String)getBodyValueAt(i, "vreceiveaddress")))
        {
          setBodyValueAt(vreceiveaddress, i, "vreceiveaddress");
        }

        if (SoVoTools.isEmptyString((String)getBodyValueAt(i, "creceiptareaid")))
        {
          setBodyValueAt(creceiptareaid, i, "creceiptareaid");
          setBodyValueAt(creceiptareaname, i, "creceiptareaname");
        }

        if (SoVoTools.isEmptyString((String)getBodyValueAt(i, "crecaddrnode")))
        {
          setBodyValueAt(crecaddrnode_body, i, "crecaddrnode");
          setBodyValueAt(crecaddrnodename_body, i, "crecaddrnodename");
        }
        ctlUIOnCconsignCorpChg(i);
      }
    }
    else
    {
      for (int i = istartrow; i < iendrow; i++)
      {
        vo = getBillCardTools().getBodyDefaultData(i);
        getBillCardTools().setBodyValuesByVO(i, vo);
        setBodyValueAt(crecaddrnode, i, "crecaddrnode");
        setBodyValueAt(crecaddrnodename, i, "crecaddrnodename");
        ctlUIOnCconsignCorpChg(i);
      }

    }

    setBodyDefaultDataForAll(istartrow, iendrow);

    afterDiscountrateEdit(null);

    updateUI();
  }

  public void setBodyDefaultDataForAll(int istartrow, int iendrow)
  {
    UFDouble udnouttoplimit = null;
    UFDouble udnoutcloselimit = null;
    for (int i = istartrow; i < iendrow; i++)
    {
      udnouttoplimit = (UFDouble)getBodyValueAt(i, "nouttoplimit");
      udnoutcloselimit = (UFDouble)getBodyValueAt(i, "noutcloselimit");
      if ((udnouttoplimit == null) && (this.uipanel.SO24 != null)) {
        setBodyValueAt(this.uipanel.SO24, i, "nouttoplimit");
      }
      if ((udnoutcloselimit == null) && (this.uipanel.SO29 != null))
        setBodyValueAt(this.uipanel.SO29, i, "noutcloselimit");
    }
  }

  private void setBodyValues(InvVO invvo, int iRow, int iCurrow)
  {
    setBodyValueByInvVO(invvo, iRow);

    setAssistUnit(iRow);

    this.alInvs.add(iRow, invvo);

    if ((getSouceBillType(iCurrow).equals("Z4")) || (getSouceBillType(iCurrow).equals("Z3")))
    {
      String[] sSource = getNeedSetNullSourceItems();
      for (int k = 0; k < sSource.length; k++)
        setBodyValueAt(getBodyValueAt(iCurrow, sSource[k]), iRow, sSource[k]);
    }
  }

  public boolean setAssistUnit(int row)
  {
    UFBoolean assistunit = new UFBoolean(false);
    if (getBodyValueAt(row, "assistunit") != null) {
      assistunit = new UFBoolean(getBodyValueAt(row, "assistunit").toString());
    }

    boolean bEdit = true;
    if (!assistunit.booleanValue()) {
      bEdit = false;
    }
    setCellEditable(row, "cpackunitname", bEdit);
    setCellEditable(row, "npacknumber", bEdit);

    setCellEditable(row, "cquoteunit", bEdit);

    String cunitid = (String)getBodyValueAt(row, "cunitid");

    String cquoteunitid = getBillCardTools().getBodyStringValue(row, "cquoteunitid");

    if ((cquoteunitid == null) || (cquoteunitid.trim().length() <= 0))
    {
      setBodyValueAt(cunitid, row, "cquoteunitid");
      setBodyValueAt(getBodyValueAt(row, "cunitname"), row, "cquoteunit");
      setBodyValueAt(new UFDouble(1.0D), row, "nqtscalefactor");
      setBodyValueAt(new UFBoolean(true), row, "bqtfixedflag");
    }

    return bEdit;
  }

  public String[] getNeedSetNullSourceItems()
  {
    return new String[] { "ct_name", "ct_manageid", "ctinvclassid", "ct_code", "ctinvclass", "creceipttype", "creceipttype", "csourcebillbodyid", "csourcebillid", "cbomorderid", "cbomordercode" };
  }

  protected void setBodyValueByInvVO(InvVO vo, int row)
  {
    if (vo == null)
      return;
    if (row < 0)
      return;
    String temp = "";
    setBodyValueAt(vo.getCinventoryid(), row, "cinventoryid");
    setBodyValueAt(vo.getCinvmanid(), row, "cinvbasdocid");
    setBodyValueAt(vo.getCinventorycode(), row, "cinventorycode");
    setBodyValueAt(vo.getInvname(), row, "cinventoryname");
    if (vo.getInvspec() != null)
      temp = temp + vo.getInvspec();
    if (vo.getInvtype() != null)
      temp = temp + vo.getInvtype();
    setBodyValueAt(temp, row, "GGXX");
    setBodyValueAt(vo.getInvspec(), row, "cinvspec");
    setBodyValueAt(vo.getInvtype(), row, "cinvtype");
    setBodyValueAt(vo.getPk_measdoc(), row, "cunitid");
    setBodyValueAt(vo.getMeasdocname(), row, "cunitname");
    setBodyValueAt(vo.getDiscountflag(), row, "discountflag");

    if ((vo.getIsAstUOMmgt() != null) && (vo.getIsAstUOMmgt().intValue() == 1))
      setBodyValueAt("Y", row, "assistunit");
    else
      setBodyValueAt("N", row, "assistunit");
    setBodyValueAt(vo.getCastunitid(), row, "cpackunitid");
    setBodyValueAt(vo.getCastunitname(), row, "cpackunitname");

    if ((vo.getLaborflag() != null) && (vo.getLaborflag().booleanValue()))
      setBodyValueAt("Y", row, "laborflag");
    else {
      setBodyValueAt("N", row, "laborflag");
    }
    if ((vo.getPk_measdoc() != null) && (vo.getCastunitid() != null)) {
      if (vo.getPk_measdoc().equals(vo.getCastunitid()))
      {
        setBodyValueAt(new UFDouble(1), row, "scalefactor");

        setBodyValueAt("Y", row, "fixedflag");
      }
      else
      {
        setBodyValueAt(vo.getHsl(), row, "scalefactor");

        if ((vo.getIsSolidConvRate() != null) && (vo.getIsSolidConvRate().intValue() == 1))
        {
          setBodyValueAt("Y", row, "fixedflag");
        }
        else setBodyValueAt("N", row, "fixedflag");

      }

      setBodyValueAt(vo.getPk_measdoc(), row, "cquoteunitid");
      setBodyValueAt(getBodyValueAt(row, "cunitname"), row, "cquoteunit");
      setBodyValueAt(new UFDouble(1), row, "nqtscalefactor");
      setBodyValueAt("Y", row, "bqtfixedflag");
    }
    else {
      setBodyValueAt(null, row, "cpackunitid");
      setBodyValueAt(null, row, "cpackunitname");

      setBodyValueAt(vo.getPk_measdoc(), row, "cquoteunitid");
      setBodyValueAt(getBodyValueAt(row, "cunitname"), row, "cquoteunit");
      setBodyValueAt(new UFDouble(1.0D), row, "nqtscalefactor");
      setBodyValueAt("Y", row, "bqtfixedflag");
    }
  }

  private int setBodyByBindinvs(int irow, Hashtable htresult, Hashtable htbinds)
    throws Exception
  {
    String sInvPk = (String)getBodyValueAt(irow, "cinventoryid");
    if (sInvPk == null) {
      return 0;
    }
    UFDouble udnum = (UFDouble)getBodyValueAt(irow, "nnumber");
    if (udnum == null) {
      udnum = new UFDouble(1);
    }
    if (!htbinds.containsKey(sInvPk)) {
      return 0;
    }
    int count = 0;

    ArrayList alneeds = new ArrayList();
    ArrayList altmp = new ArrayList();
    altmp.add(sInvPk);
    altmp.add(udnum);
    alneeds.add(altmp);
    alneeds.add(new ArrayList());

    Integer Icount = new Integer(count);
    ArrayList acount = new ArrayList();
    acount.add(Icount);
    long s = System.currentTimeMillis();
    SCMEnv.out("##连接捆绑件开始");

    UnitBinds(alneeds, htbinds, acount);
    Icount = (Integer)acount.get(0);
    count = Icount.intValue();
    SCMEnv.out("##连接捆绑件用时[" + (System.currentTimeMillis() - s) / 1000.0D + "s]");

    s = System.currentTimeMillis();

    if (irow == getRowCount() - 1)
      this.uipanel.addNullLine(irow, count);
    else {
      this.uipanel.insertNullLine(irow, count);
    }

    ArrayList allcurrow = new ArrayList();
    allcurrow.add(new Integer(irow));
    setBodyDefaultData(irow, irow + count);

    setBinds(alneeds, htresult, allcurrow, irow);
    SCMEnv.out("##设置捆绑件明细用时[" + (System.currentTimeMillis() - s) / 1000.0D + "s]");

    return count;
  }

  private void UnitBinds(ArrayList alneed, Hashtable htBinds, ArrayList count)
  {
    Object o = null;
    String sPk = null;
    InvbindleVO bvo = null;
    Vector vt = null;
    InvbindleVO[] bvos = null;
    ArrayList alold = null;
    ArrayList altmp = null;
    UFDouble udnum = null;

    o = alneed.get(0);
    if ((o instanceof ArrayList)) {
      sPk = (String)((ArrayList)o).get(0);

      udnum = (UFDouble)((ArrayList)o).get(1);
    } else if ((o instanceof InvbindleVO)) {
      bvo = (InvbindleVO)o;
      sPk = bvo.getPk_bindleinvmandoc();
      udnum = bvo.getBindlenum() == null ? new UFDouble(1) : bvo.getBindlenum();
    }

    if (htBinds.containsKey(sPk)) {
      vt = (Vector)htBinds.get(sPk);
      bvos = new InvbindleVO[vt.size()];
      vt.copyInto(bvos);

      Integer icount = (Integer)count.get(0);
      icount = new Integer(icount.intValue() + vt.size());
      count.clear();
      count.add(icount);

      alold = (ArrayList)alneed.get(1);
      for (int j = 0; j < vt.size(); j++) {
        altmp = new ArrayList();
        bvo = (InvbindleVO)bvos[j].clone();
        bvo.setBindlenum((bvo.getBindlenum() == null ? new UFDouble(1) : bvo.getBindlenum()).multiply(udnum));

        altmp.add(bvo);
        altmp.add(new ArrayList());
        alold.add(altmp);
        UnitBinds(altmp, htBinds, count);
      }
    }
  }

  private void setBinds(ArrayList alneeds, Hashtable htresult, ArrayList airow, int irow)
  {
    InvVO tmpvo = null;

    String sPk = null;

    Object o = null;
    InvbindleVO invbvo = null;
    for (int i = 0; i < alneeds.size(); i++)
    {
      o = alneeds.get(i);
      invbvo = null;

      if ((o instanceof InvbindleVO)) {
        invbvo = (InvbindleVO)o;

        Integer iirow = (Integer)airow.get(0);
        int icurrow = iirow.intValue() + 1;
        airow.clear();
        airow.add(new Integer(icurrow));

        sPk = invbvo.getPk_bindleinvmandoc();
        tmpvo = (InvVO)htresult.get(sPk);
        setBodyValues(tmpvo, icurrow, irow);
        if (invbvo != null) {
          setBindingsItems(invbvo, icurrow);
        }
      }
      else if ((o instanceof ArrayList)) {
        setBinds((ArrayList)o, htresult, airow, irow);
      }
    }
  }

  private void setBindingsItems(InvbindleVO invvo, int irow)
  {
    setBodyValueAt(invvo.getBindlenum(), irow, "nnumber");

    setBodyValueAt(invvo.getPricetype(), irow, "bindpricetype");

    if (invvo.getPricetype().intValue() == 0)
    {
      setBodyValueAt(invvo.getPrice(), irow, "noriginalcurtaxprice");
      afterNumberEditLogic(irow, "noriginalcurtaxprice", false);
    }
  }

  private int setBodyByLargessinvs(int irow, Hashtable htresult, Hashtable htLargess)
    throws Exception
  {
    String sInvPk = (String)getBodyValueAt(irow, "cinventoryid") + (String)getBodyValueAt(irow, "cquoteunitid");

    if (sInvPk == null)
      return 0;
    if (!htLargess.containsKey(sInvPk)) {
      return 0;
    }
    ArrayList allargess = (ArrayList)htLargess.get(sInvPk);

    UFDouble nnum = (UFDouble)getBodyValueAt(irow, "nquoteunitnum");
    if (nnum == null) {
      nnum = new UFDouble(1);
    }
    BuylargessVO vo = null;
    BuylargessHVO bhvo = null;

    BuylargessVO votmp = null;
    BuylargessHVO btmphvo = null;

    Vector vt = new Vector();
    int i = 0; for (int isize = allargess.size(); i < isize; i++) {
      votmp = (BuylargessVO)allargess.get(i);
      btmphvo = (BuylargessHVO)votmp.getParentVO();
      if ((btmphvo.getPk_custgroup() == null) || (btmphvo.getPk_custgroup().trim().length() <= 0))
        continue;
      vt.add(btmphvo.getPk_custgroup());
    }
    HashMap hmp = null;
    if (vt.size() > 0) {
      String[] skeys = new String[vt.size()];
      vt.copyInto(skeys);
      hmp = getCustgroupCodes(skeys);
    } else {
      hmp = new HashMap();
    }

    String scustgroup = null;
    String stmpcustgroup = null;
     i = 0; for (int isize = allargess.size(); i < isize; i++) {
      votmp = (BuylargessVO)allargess.get(i);
      btmphvo = (BuylargessHVO)votmp.getParentVO();

      if (btmphvo.getNbuynum().compareTo(nnum) > 0)
        continue;
      if (vo == null) {
        vo = votmp;
      }
      else {
        bhvo = (BuylargessHVO)vo.getParentVO();

        if ((btmphvo.getPk_cumandoc() != null) && (btmphvo.getPk_cumandoc().trim().length() > 0))
        {
          vo = votmp; } else {
          if ((btmphvo.getPk_custgroup() == null) || (btmphvo.getPk_custgroup().trim().length() <= 0))
          {
            continue;
          }
          if ((bhvo.getPk_custgroup() != null) && (bhvo.getPk_custgroup().trim().length() > 0))
          {
            scustgroup = (String)hmp.get(bhvo.getPk_custgroup());
            stmpcustgroup = (String)hmp.get(btmphvo.getPk_custgroup());

            if ((scustgroup == null) || (stmpcustgroup == null) || (scustgroup.trim().length() >= stmpcustgroup.trim().length()))
            {
              continue;
            }
            vo = votmp;
          }
          else {
            if (((bhvo.getPk_cumandoc() != null) && (bhvo.getPk_cumandoc().trim().length() != 0)) || ((bhvo.getPk_custgroup() != null) && (bhvo.getPk_custgroup().trim().length() != 0)))
            {
              continue;
            }
            vo = votmp;
          }
        }
      }

    }

    if (vo == null) {
      return 0;
    }
    int count = vo.getChildrenVO().length;

    if (irow == getRowCount() - 1)
      this.uipanel.addNullLine(irow, count);
    else {
      this.uipanel.insertNullLine(irow, count);
    }

    setBodyDefaultData(irow, irow + count);

    setLargess(vo, htresult, irow);

    return count;
  }

  private HashMap getCustgroupCodes(String[] sKeys)
  {
    HashMap hp = new HashMap();
    if ((sKeys == null) || (sKeys.length == 0))
      return hp;
    String swheres = "pk_defdoc in(";
    for (int i = 0; i < sKeys.length; i++) {
      if (i > 0) {
        swheres = swheres + ",";
      }
      swheres = swheres + "'" + sKeys[i] + "'";
    }
    swheres = swheres + ")";
    ArrayList o = (ArrayList)DBCacheFacade.runQuery("select pk_defdoc,doccode from bd_defdoc where " + swheres, new ArrayListProcessor());

    Object[] o1 = null;
    if ((o == null) || (o.size() == 0))
      return hp;
    for (int i = 0; i < o.size(); i++) {
      o1 = (Object[])(Object[])o.get(i);

      hp.put(o1[0], o1[1]);
    }
    return hp;
  }

  private void setLargess(BuylargessVO bvo, Hashtable htresult, int irow)
  {
    if (bvo == null) {
      return;
    }
    BuylargessBVO[] bodys = (BuylargessBVO[])(BuylargessBVO[])bvo.getChildrenVO();
    InvVO tmpvo = null;

    if ((bodys != null) && (bodys.length > 0))
      for (int j = 0; j < bodys.length; j++) {
        tmpvo = (InvVO)htresult.get(bodys[j].getPk_invmandoc());
        setBodyValues(tmpvo, j + irow + 1, irow);

        setLargessItems((BuylargessHVO)bvo.getParentVO(), bodys[j], j + irow + 1, irow);
      }
  }

  private void setLargessItems(BuylargessHVO hvo, BuylargessBVO bvo, int irow, int imainrow)
  {
    setBodyValueAt(getBodyValueAt(imainrow, "crowno"), irow, "clargessrowno");

    setBodyValueAt(new UFBoolean(true), irow, "blargessflag");

    setBodyValueAt(bvo.getCunitid(), irow, "cquoteunitid");

    afterUnitEdit(irow, "cquoteunitid");

    UFDouble umainnum = (UFDouble)getBodyValueAt(imainrow, "nquoteunitnum");
    if (umainnum == null) {
      umainnum = new UFDouble(1);
    }
    umainnum = umainnum.div(hvo.getNbuynum());

    if ((bvo.getFtoplimittype() != null) && (bvo.getFtoplimittype().intValue() == 0)) if (bvo.getNnum().multiply(umainnum).multiply(bvo.getNprice() == null ? new UFDouble(0) : bvo.getNprice()).compareTo(bvo.getNtoplimitvalue() == null ? new UFDouble(0) : bvo.getNtoplimitvalue()) > 0)
      {
        setBodyValueAt(bvo.getNtoplimitvalue() == null ? new UFDouble(0) : bvo.getNtoplimitvalue(), irow, "noriginalcursummny");

        setBodyValueAt((bvo.getNtoplimitvalue() == null ? new UFDouble(0) : bvo.getNtoplimitvalue()).div(bvo.getNprice()), irow, "nquoteunitnum");

        afterNumberEditLogic(irow, "noriginalcursummny", false);

        return;
      } if ((bvo.getFtoplimittype() != null) && (bvo.getFtoplimittype().intValue() == 1) && (bvo.getNnum().multiply(umainnum).compareTo(bvo.ntoplimitvalue) > 0))
    {
      setBodyValueAt(bvo.ntoplimitvalue, irow, "nquoteunitnum");
      afterNumberEdit(new int[] { irow }, "nquoteunitnum", null, false, true);

      setBodyValueAt(bvo.getNprice() == null ? new UFDouble(0) : bvo.getNprice(), irow, "norgqttaxprc");

      afterNumberEditLogic(irow, "norgqttaxprc", false);
    }
    else
    {
      setBodyValueAt(bvo.getNnum().multiply(umainnum), irow, "nquoteunitnum");

      setBodyValueAt(bvo.getNnum().multiply(umainnum).multiply(bvo.getNprice() == null ? new UFDouble(0) : bvo.getNprice()), irow, "noriginalcursummny");

      setBodyValueAt(bvo.getNprice() == null ? new UFDouble(0) : bvo.getNprice(), irow, "norgqttaxprc");

      afterNumberEditLogic(irow, "norgqttaxprc", false);
    }
  }

  protected void setDefaultCtItem(int istartrow, int ilength)
  {
    try
    {
      String[] sinvs = null;

      String[] smans = null;
      String sman = getHeadItem("ccustomerid").getValue();
      if ((sman == null) || (sman.trim().length() == 0)) {
        return;
      }

      String sSource = null;
      Vector vt = new Vector();
      for (int i = 0; i < ilength; i++) {
        sSource = (String)getBodyValueAt(istartrow + i, "csourcebillbodyid");
        if(!SoVoTools.isEmptyString(sSource))
        {
        	if(getSouceBillType().equals("Z4"))
        	{
        		setBodyValueAt(null,istartrow + i, "csourcebillbodyid");
        		sSource=null;
        	}
        }
        if ((!SoVoTools.isEmptyString(sSource)) || (getBodyValueAt(istartrow + i, "cinventoryid") == null) || (getBodyValueAt(istartrow + i, "cinventoryid").toString().trim().length() <= 0))
        {
          continue;
        }

        vt.add(getBodyValueAt(istartrow + i, "cinventoryid"));
      }

      if (vt.size() > 0) {
        sinvs = new String[vt.size()];
        vt.copyInto(sinvs);
        smans = new String[vt.size()];
        for (int i = 0; i < smans.length; i++)
          smans[i] = sman;
      }
      else {
        return;
      }

      Hashtable ht = SaleOrderBO_Client.queryForCntAll(ClientEnvironment.getInstance().getCorporation().getPk_corp(), sinvs, smans, ClientEnvironment.getInstance().getDate());
   
      if ((ht != null) && (ht.size() > 0) && 
        (this.uipanel.showYesNoMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000508")) == 8))
      {
        for (int i = 0; i < ilength; i++) {
          setCellEditable(istartrow + i, "ct_name", false);
        }

        return;
      }
      
      RetCtToPoQueryVO rect = null;

      ArrayList alrows = new ArrayList();
      int invindex=0;
      for (int i = 0; i < ilength; i++) {
        sSource = (String)getBodyValueAt(istartrow + i, "csourcebillbodyid");

        if ((SoVoTools.isEmptyString(sSource)) && (getBodyValueAt(istartrow + i, "cinventoryid") != null) && (getBodyValueAt(istartrow + i, "cinventoryid").toString().trim().length() > 0) && (ht.containsKey(getBodyValueAt(istartrow + i, "cinventoryid"))))
        {
          rect = (RetCtToPoQueryVO)ht.get(sinvs[invindex]);
          setCtItems(istartrow + i, rect);
          setCellEditable(istartrow + i, "ct_name", true);
          alrows.add(Integer.valueOf(istartrow + i));
          invindex++;
        }
        else {
          setCellEditable(istartrow + i, "ct_name", false);
        }
      }

      initFreeItem(alrows);
    }
    catch (Exception e) {
      handleException(e);
    }
  }

  private void setCtItems(int irow, RetCtToPoQueryVO vo)
  {
    setBodyValueAt(vo.getCContractCode(), irow, "ct_code");
    setBodyValueAt(vo.getCContractID(), irow, "ct_manageid");
    setBodyValueAt(vo.getCtname(), irow, "ct_name");
    setBodyValueAt(vo.getCInvClass(), irow, "ctinvclassid");
    setBodyValueAt("Z4", irow, "creceipttype");
    setBodyValueAt(vo.getCContractRowId(), irow, "csourcebillbodyid");
    setBodyValueAt(vo.getCContractID(), irow, "csourcebillid");
    HashMap hm=new HashMap();
    UFDouble  tsbl=new UFDouble(0);
    try {
		String sql="select tsbl, dpzk,def13,b.pk_defdoc as pk_defdoc13 from ct_manage_b a  ";
			sql+=" left join bd_defdoc b on a.def13=b.docname  where pk_ct_manage_b='"+String.valueOf(vo.getAttributeValue("pk_ct_manage_b"))+"'";
		  IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator
			.getInstance().lookup(IUAPQueryBS.class.getName());
		   hm=(HashMap)  sessionManager.executeQuery(sql,
					new MapProcessor());
		   if(hm.size()>0)
		   {
			    setBodyValueAt(hm.get("tsbl"),irow,"tsbl");
			    setBodyValueAt(hm.get("dpzk"),irow,"nitemdiscountrate");
			    setBodyValueAt(hm.get("def13"),irow,"vdef13");
			    setBodyValueAt(hm.get("pk_defdoc13"),irow,"pk_defdoc13");
			    DefSetTool.afterEditBody(getBillData().getBillModel(),irow, "vdef13", "pk_defdoc13");	
			    UIRefPane refpane = (UIRefPane) getBillData().getBillModel().getItemByKey("vdef13").getComponent();
			    refpane.setText(String.valueOf(hm.get("def13")));
			    refpane.setPK(hm.get("pk_defdoc13"));
			    tsbl=hm.get("tsbl")==null||String.valueOf(hm.get("tsbl")).equals("")||String.valueOf(hm.get("tsbl")).equals("null")?new UFDouble("0"):new UFDouble(String.valueOf(hm.get("tsbl")));
			  
		   }
	} catch (BusinessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    


    if ((getBodyValueAt(irow, "blargessflag") != null) && (((Boolean)getBodyValueAt(irow, "blargessflag")).booleanValue()))
    {
      setBodyValueAt(new UFDouble(0), irow, "noriginalcurprice");
      setBodyValueAt(new UFDouble(0), irow, "noriginalcurtaxprice");
    } else {
      setBodyValueAt(vo.getDOrgPrice(), irow, "norgqtprc");
      setBodyValueAt(vo.getDOrgTaxPrice(), irow, "norgqttaxprc");
      //add by LGY 2012/10/08
      setBodyValueAt(vo.getDOrgPrice(), irow, "noriginalcurprice");
      setBodyValueAt(vo.getDOrgTaxPrice(), irow, "noriginalcurtaxprice");
      setBodyValueAt(vo.getDOrgPrice(),irow,"nqtprc");
      setBodyValueAt(vo.getDOrgTaxPrice(),irow,"nqttaxprc");
  
      if(tsbl.compareTo(new UFDouble(0))>0)
      {
    	  tsbl=(tsbl.div(10)).sub(1).abs();
    	  
    	  setBodyValueAt(tsbl.multiply(vo.getDOrgPrice()), irow, "noriginalcurnetprice")  ;
    	  setBodyValueAt(tsbl.multiply(vo.getDOrgTaxPrice()), irow, "noriginalcurtaxnetprice")  ;
    	  setBodyValueAt(tsbl.multiply(vo.getDOrgPrice()), irow, "norgqtnetprc");
    	  setBodyValueAt(tsbl.multiply(vo.getDOrgTaxPrice()), irow, "norgqttaxnetprc");
     	  setBodyValueAt(tsbl.multiply(vo.getDOrgTaxPrice()), irow, "nqttaxnetprc");
    	  setBodyValueAt(tsbl.multiply(vo.getDOrgPrice()), irow,"nqtnetprc");
      }
      else
      {
    	  setBodyValueAt(vo.getDOrgPrice(), irow, "noriginalcurnetprice")  ;
    	  setBodyValueAt(vo.getDOrgTaxPrice(), irow, "noriginalcurtaxnetprice")  ;
    	  setBodyValueAt(vo.getDOrgPrice(), irow, "norgqtnetprc");
    	  setBodyValueAt(vo.getDOrgTaxPrice(), irow, "norgqttaxnetprc");
    	  setBodyValueAt(vo.getDOrgTaxPrice(), irow, "nqttaxnetprc");
    	  setBodyValueAt(vo.getDOrgPrice(), irow,"nqtnetprc");
      }
      //add by LGY 2012/10/08
    }
    
    BillEditEvent e=new BillEditEvent(this.getBodyItem("norgqtprc").getComponent(),vo.getDOrgPrice(),"norgqtprc");
	afterEdit(e);


    executeCtFormula(irow);
    setCtItemEditable(irow);
  }

  private void initFreeItem(ArrayList<Integer> irows)
  {
    if (getRowCount() <= 0) {
      return;
    }
    try
    {
      InvVO[] invvos = new InvVO[irows.size()];
      for (int i = 0; i < invvos.length; i++) {
        invvos[i] = new InvVO();
        invvos[i].setCinventoryid((String)getBodyValueAt(((Integer)irows.get(i)).intValue(), "cinventoryid"));
      }

      InvoInfoBYFormula invvosget = new InvoInfoBYFormula();
      invvos = invvosget.getQuryInvVOs(invvos, false, true, 1);

      for (int i = 0; i < invvos.length; i++) {
        this.alInvs.add(invvos[i]);
      }
      if (this.alInvs != null)
        for (int i = 0; i < getRowCount(); i++) {
          InvVO voInv = (InvVO)this.alInvs.get(i);
          setBodyFreeValue(((Integer)irows.get(i)).intValue(), voInv);
          setBodyValueAt(voInv.getFreeItemVO().getWholeFreeItem(), ((Integer)irows.get(i)).intValue(), "vfree0");
        }
    }
    catch (Exception ex)
    {
      SCMEnv.out("自由项设置失败!");
    }
  }

  public void setBodyFreeValue(int row, InvVO voInv)
  {
    if (voInv != null) {
      voInv.setFreeItemValue("vfree1", (String)getBodyValueAt(row, "vfree1"));

      voInv.setFreeItemValue("vfree2", (String)getBodyValueAt(row, "vfree2"));

      voInv.setFreeItemValue("vfree3", (String)getBodyValueAt(row, "vfree3"));

      voInv.setFreeItemValue("vfree4", (String)getBodyValueAt(row, "vfree4"));

      voInv.setFreeItemValue("vfree5", (String)getBodyValueAt(row, "vfree5"));
    }
  }

  private void loadTaxtrate(ArrayList rows)
  {
    if (rows == null)
      return;
    String[] targetitemkey = { "ctaxitemid" };
    String tname = "bd_invmandoc";
    String pkname = "pk_invmandoc";
    String[] field = { "mantaxitem" };
    String sourceitemkey = "cinventoryid";

    UFDouble nheadtaxrate = getHeadItem("ntaxrate").getValue() == null ? new UFDouble(0) : new UFDouble(getHeadItem("ntaxrate").getValue());

    ArrayList altax = new ArrayList();
    UFDouble udBodytax = null;
    int count = rows.size();
    int[] irows = new int[count];
    for (int i = 0; i < count; i++)
    {
      if (rows.get(i) != null) {
        irows[i] = ((Integer)rows.get(i)).intValue();
        udBodytax = (UFDouble)getBodyValueAt(irows[i], "ntaxrate");
        altax.add(udBodytax == null ? nheadtaxrate : udBodytax);
      } else {
        irows[i] = -1;
        altax.add(new UFDouble(0));
      }

    }

    ClientCacheHelper.getColValueBatch(this, irows, targetitemkey, tname, pkname, field, sourceitemkey);

    Object temp = null;
    ArrayList newRows = new ArrayList();
    for (int i = 0; i < count; i++) {
      if (rows.get(i) != null) {
        temp = getBodyValueAt(((Integer)rows.get(i)).intValue(), "ctaxitemid");

        if ((temp == null) || (temp.toString().trim().length() < 20)) {
          newRows.add(rows.get(i));
        }
      }
    }
    count = newRows.size();
    int[] inewRows = new int[count];
    for (int i = 0; i < count; i++) {
      if (newRows.get(i) != null) {
        inewRows[i] = ((Integer)newRows.get(i)).intValue();
      }
    }
    if (inewRows.length > 0) {
      targetitemkey = new String[] { "ctaxitemid" };
      tname = "bd_invbasdoc";
      pkname = "pk_invbasdoc";
      field = new String[] { "pk_taxitems" };
      sourceitemkey = "cinvbasdocid";

      ClientCacheHelper.getColValueBatch(this, inewRows, targetitemkey, tname, pkname, field, sourceitemkey);
    }

    targetitemkey = new String[] { "ntaxrate" };
    tname = "bd_taxitems";
    pkname = "pk_taxitems";
    field = new String[] { "taxratio" };
    sourceitemkey = "ctaxitemid";
    ClientCacheHelper.getColValueBatch(this, irows, targetitemkey, tname, pkname, field, sourceitemkey);

    for (int i = 0; i < count; i++) {
      if (irows[i] == -1)
        continue;
      udBodytax = (UFDouble)getBodyValueAt(irows[i], "ntaxrate");
      if (udBodytax == null)
        setBodyValueAt(altax.get(i), irows[i], "ntaxrate");
    }
  }

  private void afterCtManageEdit(BillEditEvent e)
  {
    int irow = e.getRow();
    UIRefPane invRef = (UIRefPane)getBodyItem("ct_name").getComponent();

    String refPk = invRef.getRefPK();
    String refCode = invRef.getRefCode();

    if ((refPk == null) || (refPk.trim().length() == 0)) {
      setBodyValueAt(null, irow, "ct_code");

      setBodyValueAt(null, irow, "ct_manageid");
      setBodyValueAt(null, irow, "ct_name");
      setBodyValueAt(null, irow, "ctinvclassid");
      setBodyValueAt(null, irow, "creceipttype");
      setBodyValueAt(null, irow, "csourcebillbodyid");
      setBodyValueAt(null, irow, "csourcebillid");
      setBodyValueAt(null, irow, "ctinvclass");
    }
    else {
      setBodyValueAt(refCode, irow, "ct_code");

      setBodyValueAt(invRef.getRefValue("ct_b.pk_ct_manage"), irow, "ct_manageid");

      setBodyValueAt("Z4", irow, "creceipttype");
      setBodyValueAt(refPk, irow, "csourcebillbodyid");
      setBodyValueAt(invRef.getRefValue("ct_b.pk_ct_manage"), irow, "csourcebillid");

      if ((getBodyValueAt(irow, "blargessflag") != null) && (((Boolean)getBodyValueAt(irow, "blargessflag")).booleanValue()))
      {
        setBodyValueAt(new UFDouble(0), irow, "noriginalcurprice");
      }
      else setBodyValueAt(invRef.getRefValue("ct_b.oriprice"), irow, "noriginalcurprice");

      executeCtFormula(irow);
    }

    setCtItemEditable(irow);
  }

  private void executeCtFormula(int irow)
  {
    String[] bodyFormula = null;
    int i = 0;
    if (this.strState.equals("新增")) {
      bodyFormula = new String[35];
      bodyFormula[(i++)] = "ctinvclassid->getColValue(ct_manage_b,invclid,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "ctinvclass->getColValue(bd_invcl,invclassname,pk_invcl,ctinvclassid)";
      bodyFormula[(i++)] = "ntaxrate->getColValue(ct_manage_b,taxration,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "bsafeprice->getColValue(ct_manage_b,bsafeprice,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "breturnprofit->getColValue(ct_manage_b,breturnprofit,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "cpriceitemtable->getColValue(ct_manage_b,cpricetableid,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "cpricepolicyid->getColValue(ct_manage_b,sopriceid,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef1->getColValue(ct_manage_b,def1,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef2->getColValue(ct_manage_b,def2,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef3->getColValue(ct_manage_b,def3,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef4->getColValue(ct_manage_b,def4,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef5->getColValue(ct_manage_b,def5,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef6->getColValue(ct_manage_b,def6,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef7->getColValue(ct_manage_b,def7,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef8->getColValue(ct_manage_b,def8,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef9->getColValue(ct_manage_b,def9,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef10->getColValue(ct_manage_b,def10,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef11->getColValue(ct_manage_b,def11,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef12->getColValue(ct_manage_b,def12,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef13->getColValue(ct_manage_b,def13,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef14->getColValue(ct_manage_b,def14,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef15->getColValue(ct_manage_b,def15,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef16->getColValue(ct_manage_b,def16,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef17->getColValue(ct_manage_b,def17,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef18->getColValue(ct_manage_b,def18,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef19->getColValue(ct_manage_b,def19,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef20->getColValue(ct_manage_b,def20,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "nnumber->getColValue(ct_manage_b,amount,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "npacknumber->getColValue(ct_manage_b,ordnum,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[i] = "ts->getColValue(ct_manage,ts,pk_ct_manage,csourcebillid)";
      
    } else {
      bodyFormula =new String[40];
      bodyFormula[(i++)] = "ctinvclassid->getColValue(ct_manage_b,invclid,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "ctinvclass->getColValue(bd_invcl,invclassname,pk_invcl,ctinvclassid)";
      bodyFormula[(i++)] = "ntaxrate->getColValue(ct_manage_b,taxration,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "bsafeprice->getColValue(ct_manage_b,bsafeprice,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "breturnprofit->getColValue(ct_manage_b,breturnprofit,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "cpriceitemtable->getColValue(ct_manage_b,cpricetableid,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "cpricepolicyid->getColValue(ct_manage_b,sopriceid,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef1->getColValue(ct_manage_b,def1,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef2->getColValue(ct_manage_b,def2,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef3->getColValue(ct_manage_b,def3,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef4->getColValue(ct_manage_b,def4,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef5->getColValue(ct_manage_b,def5,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef6->getColValue(ct_manage_b,def6,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef7->getColValue(ct_manage_b,def7,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef8->getColValue(ct_manage_b,def8,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef9->getColValue(ct_manage_b,def9,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef10->getColValue(ct_manage_b,def10,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef11->getColValue(ct_manage_b,def11,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef12->getColValue(ct_manage_b,def12,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef13->getColValue(ct_manage_b,def13,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef14->getColValue(ct_manage_b,def14,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef15->getColValue(ct_manage_b,def15,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef16->getColValue(ct_manage_b,def16,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef17->getColValue(ct_manage_b,def17,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef18->getColValue(ct_manage_b,def18,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef19->getColValue(ct_manage_b,def19,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "vdef20->getColValue(ct_manage_b,def20,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[(i++)] = "nnumber->getColValue(ct_manage_b,amount,pk_ct_manage_b,csourcebillbodyid)";
      bodyFormula[i] = "npacknumber->getColValue(ct_manage_b,ordnum,pk_ct_manage_b,csourcebillbodyid)";
     
/*      if(getSouceBillType().equals("Z4"))
      {
    	  i++;
    	  bodyFormula[i] = "noriginalcurtaxprice->getColValue(ct_manage_b,oritaxprice,pk_ct_manage_b,csourcebillbodyid)";
    	  i++;
    	  bodyFormula[i] = "noriginalcurprice->getColValue(ct_manage_b,oriprice,pk_ct_manage_b,csourcebillbodyid)";
    	  i++;
    	  bodyFormula[i] = "tsbl->getColValue(ct_manage_b,tsbl,pk_ct_manage_b,csourcebillbodyid)";
    	  i++;
    	  bodyFormula[i] = "nitemdiscountrate->getColValue(ct_manage_b,dpzk,pk_ct_manage_b,csourcebillbodyid)";
    	  i++;
    	  bodyFormula[i] = "noriginalcurtaxnetprice->getColValue(ct_manage_b,oritaxprice,pk_ct_manage_b,csourcebillbodyid)*(1-getColValue(ct_manage_b,tsbl,pk_ct_manage_b,csourcebillbodyid))";
    	  i++;
    	  bodyFormula[i] = "noriginalcurnetprice->getColValue(ct_manage_b,oriprice,pk_ct_manage_b,csourcebillbodyid)*(1-getColValue(ct_manage_b,tsbl,pk_ct_manage_b,csourcebillbodyid))";
      }*/
    }

    getBillModel().execFormulas(irow, bodyFormula);
    UFDouble amount = (UFDouble)getBodyValueAt(irow, "nnumber");
    UFDouble ordnum = (UFDouble)getBodyValueAt(irow, "npacknumber");
    if (ordnum == null)
      ordnum = new UFDouble(0);
    if (amount == null) {
      amount = new UFDouble(0);
    }

    setBodyValueAt(amount.sub(ordnum), irow, "nnumber");

    bodyFormula = new String[1];

    bodyFormula[0] = "npacknumber->getColValue(ct_manage_b,astnum,pk_ct_manage_b,csourcebillbodyid)";
    getBillModel().execFormulas(irow, bodyFormula);
    UFDouble astnum = (UFDouble)getBodyValueAt(irow, "npacknumber");
    if ((amount != null) && (amount.doubleValue() != 0.0D) && (astnum != null)) {
      setBodyValueAt(amount.sub(ordnum).multiply(astnum).div(amount), irow, "npacknumber");
    }

    if (this.uipanel.SO_17.booleanValue())
      afterNumberEditLogic(irow, "noriginalcurprice", false);
  }

  public void setCtItemEditable(int i)
  {
    String ct_manageid = getBodyValueAt(i, "ct_manageid") == null ? null : getBodyValueAt(i, "ct_manageid").toString();

    if ((ct_manageid != null) && (ct_manageid.length() != 0))
    {
      getBodyItem("cinventorycode").setEdit(true);
      setCellEditable(i, "cinventorycode", true);
      setCellEditable(i, "ct_name", true);

      if ((this.uipanel.SO_17.booleanValue()) && (!this.uipanel.SA_15.booleanValue())) {
        UFDouble noriginalcurtaxprice = getBodyValueAt(i, "noriginalcurtaxprice") == null ? new UFDouble(0) : new UFDouble(getBodyValueAt(i, "noriginalcurtaxprice").toString());

        UFDouble noriginalcurprice = getBodyValueAt(i, "noriginalcurprice") == null ? new UFDouble(0) : new UFDouble(getBodyValueAt(i, "noriginalcurprice").toString());

        if (noriginalcurtaxprice.doubleValue() != 0.0D) {
          if (getBodyItem("norgqttaxprc").isShow()) {
            getBillModel().setCellEditable(i, "norgqttaxprc", true);
          }
          else {
            setCellEditable(i, "noriginalcurtaxprice", false);
          }
        }
        if (noriginalcurprice.doubleValue() != 0.0D)
          if (getBodyItem("norgqtprc").isShow()) {
            getBillModel().setCellEditable(i, "norgqtprc", true);
          }
          else
            setCellEditable(i, "noriginalcurprice", false);
      }
      else
      {
        String[] sItems = { "noriginalcurtaxprice", "noriginalcurprice", "norgqttaxprc", "noriginalcurtaxprice", "norgqtprc" };

        for (int k = 0; k < sItems.length; k++)
          setCellEditable(i, sItems[k], getBillModel().getItemByKey(sItems[k]).isEdit());
      }
    }
    else
    {
      setCellEditable(i, "ct_name", false);
    }
  }

  private void afterNumberEdit(BillEditEvent e)
  {
    Object otemp = getBodyValueAt(e.getRow(), e.getKey());
    if ("退货".equals(this.strState)) {
      UFDouble nnumber = getBillCardTools().getBodyUFDoubleValue(e.getRow(), "nnumber");

      if (nnumber != null) {
        String csourcebillbodyid = getBillCardTools().getBodyStringValue(e.getRow(), e.getKey());

        if ((csourcebillbodyid != null) && (csourcebillbodyid.trim().length() > 0))
        {
          int pos = SoVoTools.find(getBillCardTools().getOldsaleordervo().getBodyVOs(), new String[] { "corder_bid" }, new Object[] { csourcebillbodyid });

          if ((pos >= 0) && 
            (getBillCardTools().getOldsaleordervo().getBodyVOs()[pos].getNnumber().doubleValue() < nnumber.abs().doubleValue()))
          {
            this.uipanel.showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000173"));

            setBodyValueAt(e.getOldValue(), e.getRow(), "nnumber");

            return;
          }
        }

      }

    }

    String sPk = (String)getBodyValueAt(e.getRow(), "cinventoryid");
    String corder_bid = (String)getBodyValueAt(e.getRow(), "corder_bid");

    if ((("nnumber".equals(e.getKey())) || ("nquoteunitnum".equals(e.getKey()))) && (sPk != null) && (sPk.trim().length() > 0) && ((corder_bid == null) || (!this.strState.equals("修订"))))
    {
      Object sSource = getBodyValueAt(e.getRow(), "creceipttype");
      if ((sSource == null) || (sSource.toString().trim().length() == 0)) {
        UFBoolean blar = new UFBoolean(getBodyValueAt(e.getRow(), "blargessflag") == null ? "false" : getBodyValueAt(e.getRow(), "blargessflag").toString());

        if ((blar == null) || (!blar.booleanValue()))
        {
          int[] inewdelline = setBlargeLineWhenDelLine(new int[] { e.getRow() });

          if ((inewdelline != null) && (inewdelline.length > 0)) {
            this.uipanel.onDelLine(inewdelline);
          }
          afterNumberEdit(new int[] { e.getRow() }, e.getKey(), null, false, true);

          afterInventoryMutiEdit(e.getRow(), new String[] { sPk }, false, false, e.getKey(), true, 2);

          return;
        }
      }

    }

    if ((otemp == null) && (e.getOldValue() == null))
      return;
    if ((otemp != null) && (otemp.equals(e.getOldValue())))
      return;
    if ((e.getOldValue() != null) && (e.getOldValue().equals(otemp)))
      return;
    boolean ischginv = false;
    if (("cinventorycode".equals(e.getKey())) || ("cinventoryid".equals(e.getKey())))
    {
      ischginv = true;
    }afterNumberEdit(new int[] { e.getRow() }, e.getKey(), null, ischginv, true);
  }

  private void afterNumberEditLogic(int row, String key, boolean isFindPrice)
  {
    BillTools.calcUnitNum(row, getBillModel(), key, getBillType());

    if ((isFindPrice) && (
      (PfUtilClient.makeFlag) || ((!getSouceBillType().equals("21")) && (!getSouceBillType().equals("37")) && (!this.strState.equals("退货")) && ((getBodyValueAt(row, "discountflag") == null) || (!getBodyValueAt(row, "discountflag").equals(new Boolean(true)))))))
    {
      if (getBodyValueAt(row, "nnumber") != null)
      {
        if ((key.equals("nnumber")) || (key.equals("npacknumber")) || (key.equals("ccurrencytypename")))
        {
          findPrice(new int[] { row }, null, false);
        }

      }

    }

    calculateNumber(row, key);
  }

  private void afterOOSFlagEdit(int row, boolean isOOS)
  {
    try
    {
      if (isOOS)
        setBodyValueAt("N", row, "bsupplyflag");
      else
        setBodyValueAt("N", row, "boosflag");
    }
    catch (Exception ex) {
      SCMEnv.out(ex);
    }
  }

  private void afterInventorysEdit(int istartrow, int iendrow, String sFormulakey, boolean bNeedFindPrice)
  {
    ArrayList dalist = new ArrayList();
    UFDouble d100 = new UFDouble(100);
    for (int i = istartrow; i < iendrow; i++)
    {
      Object temp = getBodyValueAt(i, "wholemanaflag");
      boolean wholemanaflag = temp == null ? false : new UFBoolean(temp.toString()).booleanValue();

      setCellEditable(i, "fbatchstatus", wholemanaflag);
      setCellEditable(i, "cbatchid", wholemanaflag);

      String cinventoryid = getBillCardTools().getBodyStringValue(i, "cinventoryid");

      if ((cinventoryid == null) || (cinventoryid.trim().length() <= 0))
      {
        setCellEditable(i, "cchantype", false);
      }
      else setCellEditable(i, "cchantype", true);

      Object oTemp = getBodyValueAt(i, "discountflag");
      boolean isDiscount = oTemp == null ? false : new UFBoolean(oTemp.toString()).booleanValue();

      oTemp = getBodyValueAt(i, "laborflag");
      boolean isLabor = oTemp == null ? false : new UFBoolean(oTemp.toString()).booleanValue();

      if (!isDiscount) {
        if (getBillCardTools().getBodyUFDoubleValue(i, "nnumber") == null)
          setBodyValueAt(this.uipanel.SO34, i, "nnumber");
        dalist.add(new Integer(i));
      }
      
      if (!getSouceBillType().equals("Z4")) //如果是来源于销售合同, 不清0、及设置默认值
      {
    	  setBodyValueAt(d100, i, "nitemdiscountrate");
      }

      if ((isLabor) || (isDiscount))
      {
        getBillCardTools().setBodyCellsEdit(new String[] { "cconsigncorp", "creccalbody", "crecwarehouse", "bdericttrans", "boosflag", "bsupplyflag" }, i, false);

        getBillCardTools().setBodyValueByHead("ccalbodyid", i);

        getBillCardTools().setBodyValueByHead("cwarehouseid", i);
      }

      if (getHeadItem("ntaxrate") != null) {
        String sMainTax = getHeadItem("ntaxrate").getValue();
        if ((sMainTax != null) && (new UFDouble(sMainTax).doubleValue() != 0.0D))
        {
          Object oCurRowTax = getBodyValueAt(i, "ntaxrate");
          if ((oCurRowTax == null) || (new UFDouble(oCurRowTax.toString()).doubleValue() == 0.0D))
          {
            setBodyValueAt(sMainTax, i, "ntaxrate");
          }
        }
      }

      ctlUIOnCconsignCorpChg(i);
      setScaleEditableByRow(i);
    }

    if ((dalist != null) && (dalist.size() > 0)) {
      ArrayList alnew = new ArrayList();

      String clargessrowno = null;
      Object blargessflag = null;
      int i = 0; for (int loop = dalist.size(); i < loop; i++) {
        int itmp = ((Integer)dalist.get(i)).intValue();
        clargessrowno = (String)getBodyValueAt(itmp, "clargessrowno");
        blargessflag = getBodyValueAt(itmp, "blargessflag");
        if ((clargessrowno != null) && (clargessrowno.trim().length() > 0) && (blargessflag != null) && (new UFBoolean(blargessflag.toString()).booleanValue()))
        {
          continue;
        }
        alnew.add(new Integer(itmp));
      }

      if (alnew.size() > 0) {
        int[] findrows = new int[alnew.size()];

        i = 0; for (int loop = alnew.size(); i < loop; i++) {
          findrows[i] = ((Integer)alnew.get(i)).intValue();
        }
        if ((sFormulakey != null) && (!sFormulakey.equals("nnumber"))) {
          afterNumberEdit(findrows, sFormulakey, null, true, false);
        }
        afterNumberEdit(findrows, "nnumber", null, true, bNeedFindPrice);
      }
    }
  }

  private void afterLargessFlagEdit(BillEditEvent e)
  {
    int row = e.getRow();
    UFBoolean largess = getBillCardTools().getBodyUFBooleanValue(row, "blargessflag");

    boolean blargess = largess == null ? false : largess.booleanValue();

    if (blargess)
      setBodyValueAt("1", row, "is_total");
    else {
      setBodyValueAt(null, row, "is_total");
    }
    if ((blargess) && (!this.uipanel.SO59.booleanValue()))
    {
      setBodyValueAt(SoVoConst.duf0, row, "noriginalcurprice");
      setBodyValueAt(SoVoConst.duf0, row, "noriginalcurtaxprice");
      setBodyValueAt(SoVoConst.duf0, row, "noriginalcurnetprice");
      setBodyValueAt(SoVoConst.duf0, row, "noriginalcurtaxnetprice");

      setBodyValueAt(SoVoConst.duf0, row, "norgqttaxprc");
      setBodyValueAt(SoVoConst.duf0, row, "norgqtprc");
      setBodyValueAt(SoVoConst.duf0, row, "norgqttaxnetprc");
      setBodyValueAt(SoVoConst.duf0, row, "norgqtnetprc");
      afterNumberEdit(new int[] { row }, "nnumber", null, false, false);
    }

    getBillCardTools().setCellEditableByLargess((blargess) && (!this.uipanel.SO59.booleanValue()), row);

    getBillModel().reCalcurateAll();
  }

  public void afterNumberEdit(int[] rows, String key, String oldinvid, boolean isinvchg, boolean bNeedFindPrice)
  {
    if ((rows == null) || (rows.length <= 0)) {
      return;
    }
    boolean bisCalculate = getBillModel().isNeedCalculate();
    getBillModel().setNeedCalculate(false);

    ArrayList rowlist = null;

    String[] pricekeys = { "cpriceitemid", "cpriceitem", "cpriceitemtable", "cpriceitemtablename", "cpricepolicyid", "cpricepolicy" };

    int i = 0; for (int loop = rows.length; i < loop; i++)
    {
      BillTools.calcUnitNum(rows[i], getBillModel(), key, getBillType());

      if (!isFindPrice(rows[i]))
        continue;
      if ((!PfUtilClient.makeFlag) && ((getSouceBillType().equals("21")) || (getSouceBillType().equals("37")) || (this.strState.equals("退货")) || ((getBodyValueAt(rows[i], "discountflag") != null) && (getBodyValueAt(rows[i], "discountflag").equals(Boolean.TRUE)))))
      {
        continue;
      }

      UFBoolean blargessflag = getBillCardTools().getBodyUFBooleanValue(rows[i], "blargessflag");

      if ((getBodyValueAt(rows[i], "nnumber") == null) || ((blargessflag != null) && (blargessflag.booleanValue())))
      {
        continue;
      }
      if ((!key.equals("nnumber")) && (!key.equals("npacknumber")) && (!key.equals("nquoteunitnum")) && (!key.equals("ccurrencytypename")) && (!key.equals("vfree0")) && (!key.equals("cquoteunit")) && (!key.equals("cpackunitname")) && (!key.equals("cpriceitem")) && (!key.equals("cpriceitemid")) && (!key.equals("cpriceitemtablename")) && (!key.equals("cpriceitemtable")) && (!key.equals("cpricepolicy")) && (!key.equals("cpricepolicyid")) && (!key.equals("creceiptareaname")) && (!key.equals("creceiptareaid")) && (!key.equals("cchantype")) && (!key.equals("cchantypeid")) && ((!key.equals("blargessflag")) || (!isFindPriceAfterlargess(rows[i]))))
      {
        continue;
      }

      if (rowlist == null)
        rowlist = new ArrayList();
      if ((key.equals("cchantype")) || (key.equals("cchantypeid")))
      {
        getBillCardTools().clearBodyValue(pricekeys, rows[i]);
      }

      rowlist.add(new Integer(rows[i]));

      if (key.equals("vfree0")) {
        key = "nnumber";
      }

    }

    if ((bNeedFindPrice) && 
      (rowlist != null) && (rowlist.size() > 0)) {
      int[] findrows = new int[rowlist.size()];

      i = 0; for (int loop = rowlist.size(); i < loop; i++) {
        findrows[i] = ((Integer)rowlist.get(i)).intValue();
      }

      findPrice(findrows, oldinvid, isinvchg);
    }
  //  if(!key.equals("nnumber"))
     
    	i = 0; for (int loop = rows.length; i < loop; i++)
      {
    	
        calculateNumber(rows[i], key);
      }
    
   
    

    getBillModel().setNeedCalculate(bisCalculate);
  }

  private boolean isFindPriceAfterlargess(int row) {
    UFBoolean largess = getBillCardTools().getBodyUFBooleanValue(row, "blargessflag");

    if ((largess == null) || (largess.booleanValue()))
      return false;
    UFDouble nprice = (UFDouble)getBodyValueAt(row, "ntaxprice");

    return (nprice == null) || (nprice.doubleValue() == 0.0D);
  }

  public void initFreeItem()
  {
    this.alInvs = new ArrayList();
    if (getRowCount() <= 0) {
      return;
    }
    try
    {
      InvVO[] invvos = new InvVO[getRowCount()];
      for (int i = 0; i < invvos.length; i++) {
        invvos[i] = new InvVO();
        invvos[i].setCinventoryid((String)getBodyValueAt(i, "cinventoryid"));
      }

      InvoInfoBYFormula invvosget = new InvoInfoBYFormula();
      invvos = invvosget.getQuryInvVOs(invvos, false, true, 1);

      for (int i = 0; i < invvos.length; i++) {
        this.alInvs.add(invvos[i]);
      }
      if (this.alInvs != null)
        for (int i = 0; i < getRowCount(); i++) {
          InvVO voInv = (InvVO)this.alInvs.get(i);
          setBodyFreeValue(i, voInv);
          setBodyValueAt(voInv.getFreeItemVO().getWholeFreeItem(), i, "vfree0");
        }
    }
    catch (Exception ex)
    {
      SCMEnv.out("自由项设置失败!");
    }
  }

  private void afterPricePolicy(BillEditEvent e)
  {
    int[] rows = { e.getRow() };

    setBodyValueAt(((UIRefPane)getBodyItem("cpricepolicy").getComponent()).getRefPK(), e.getRow(), "cpricepolicyid");

    setBodyValueAt(null, e.getRow(), "cpriceitemtable");
    setBodyValueAt(null, e.getRow(), "cpriceitemtablename");
    setBodyValueAt(null, e.getRow(), "cpriceitem");
    setBodyValueAt(null, e.getRow(), "cpriceitemid");

    findPrice(rows, null, false);
  }

  private void afterPriceItemTable(BillEditEvent e)
  {
    int[] rows = { e.getRow() };

    setBodyValueAt(((UIRefPane)getBodyItem("cpriceitemtablename").getComponent()).getRefPK(), e.getRow(), "cpriceitemtable");

    setBodyValueAt(null, e.getRow(), "cpriceitem");
    setBodyValueAt(null, e.getRow(), "cpriceitemid");

    findPrice(rows, null, false);
  }

  public void bodyRowChange(BillEditEvent e)
  {
    UFBoolean isInvBom = getBillCardTools().getBodyUFBooleanValue(e.getRow(), "isconfigable");

    if ((isInvBom != null) && (isInvBom.booleanValue()))
      this.uipanel.boBom.setEnabled(true);
    else
      this.uipanel.boBom.setEnabled(false);
    this.uipanel.updateButton(this.uipanel.boBom);

    if (getBillType().equals("30")) {
      try
      {
        int iStatus = Integer.parseInt(getHeadItem("fstatus").getValue() == null ? "0" : getHeadItem("fstatus").getValue());

        if (e.getRow() > -1)
        {
          Object cfreezeid = getBodyValueAt(e.getRow(), "cfreezeid");

          if ((cfreezeid == null) || (cfreezeid.toString().trim().length() == 0))
          {
            if (iStatus == 2)
              this.uipanel.boStockLock.setEnabled(true);
            else
              this.uipanel.boStockLock.setEnabled(false);
          }
          this.uipanel.updateButton(this.uipanel.boStockLock);
        }

        this.uipanel.freshOnhandnum(e.getRow());
      } catch (Exception e1) {
        SCMEnv.out(e1);
      }
    }

    try
    {
      if (this.uipanel.getFuncExtend() != null)
      {
        this.uipanel.getFuncExtend().rowchange(this.uipanel, this, null, 1, 0);
      }
    }
    catch (Throwable exx)
    {
      SCMEnv.out(exx);
    }
  }

  public boolean beforeEdit(BillEditEvent e)
  {
    boolean bret = true;

    if (e.getPos() == 1)
    {
      if (e.getKey().equals("cadvisecalbody"))
      {
        UIRefPane ctm = (UIRefPane)getBodyItem(e.getKey()).getComponent();

        CalBodySORefModel mol = (CalBodySORefModel)ctm.getRefModel();
        mol.ccustomerid = getHeadItem("ccustomerid").getValue();
        mol.cinventoryid = new String[] { getBodyValueAt(e.getRow(), "cinventoryid") == null ? null : (String)getBodyValueAt(e.getRow(), "cinventoryid") };

        mol.csalestruid = getHeadItem("csalecorpid").getValue();

        String cconsigncorp = getBillCardTools().getBodyStringValue(e.getRow(), "cconsigncorpid");

        if ((cconsigncorp != null) && (!this.pk_corp.equals(cconsigncorp))) {
          mol.isMultiCorp = true;
          mol.multPkCorp = cconsigncorp;
        }

      }
      else if (e.getKey().equals("cconsigncorp"))
      {
        UIRefPane ctm = (UIRefPane)getBodyItem(e.getKey()).getComponent();

        CustMandocSORefModel mol = (CustMandocSORefModel)ctm.getRefModel();

        mol.ccustomerid = getHeadItem("ccustomerid").getValue();
        mol.cinventoryid = new String[] { getBodyValueAt(e.getRow(), "cinventoryid") == null ? null : (String)getBodyValueAt(e.getRow(), "cinventoryid") };

        mol.csalestruid = getHeadItem("csalecorpid").getValue();

        mol.reloadData();
      }
      else if (e.getKey().equals("cinventorycode")) {
        beforeInventoryEdit(e);

        String csourcebillbodyid = (String)getBodyValueAt(e.getRow(), "csourcebillbodyid");

        String cinventoryid = (String)getBodyValueAt(e.getRow(), "cinventoryid");

        if ((!SoVoTools.isEmptyString(csourcebillbodyid)) && (!SoVoTools.isEmptyString(cinventoryid)))
        {
          String ctinvclassid = (String)getBodyValueAt(e.getRow(), "ctinvclassid");

          if (SoVoTools.isEmptyString(ctinvclassid))
          {
            this.uipanel.showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000421"));

            bret = false;
          }
        }

      }
      else if (e.getKey().equals("vreceiveaddress")) {
        beforeBodyAddressEdit(e);
      }
      else if (e.getKey().equals("ct_name")) {
        String invbasid = (String)getBodyValueAt(e.getRow(), "cinvbasdocid");

        String sman = getHeadItem("ccustbasid").getValue();
        if ((invbasid == null) || (invbasid.length() == 0)) {
          setCellEditable(e.getRow(), "ct_name", false);
        } else {
          UIRefPane ctm = (UIRefPane)getBodyItem("ct_name").getComponent();

          setCellEditable(e.getRow(), "ct_name", true);
          try
          {
            if (SaleOrderBO_Client.isModuleEnabled(this.pk_corp, "CT"))
            {
              Class clz = Class.forName("nc.ui.ct.ref.ValiSaleCtRefModel");

              if (clz != null) {
                IValiSaleCtRefModel ref = (IValiSaleCtRefModel)clz.newInstance();

                ref.setWhereParameter(this.pk_corp, sman, invbasid, ClientEnvironment.getInstance().getDate());

                ctm.setRefModel((AbstractRefModel)ref);
                ctm.setReturnCode(false);
              }
            }
            else
            {
              System.err.println("合同没有启用，pk_corp=" + this.pk_corp);
            }
          } catch (Exception e1) {
            SCMEnv.out(e1);
          }

        }

      }
      else if (!e.getKey().equals("npacknumber"))
      {
        if (e.getKey().equals("cprojectphasename")) {
          stopEditing();
          String cprojectid = getBodyValueAt(e.getRow(), "cprojectid") == null ? null : getBodyValueAt(e.getRow(), "cprojectid").toString();

          if ((cprojectid == null) || (cprojectid.equals("")))
            cprojectid = "ABCDEF";
          UIRefPane cprojectphasename = (UIRefPane)getBodyItem("cprojectphasename").getComponent();

          cprojectphasename.setRefModel(new PhaseRefModel(cprojectid));
        }
        else if (e.getKey().equals("cbatchid")) {
          beforeBatchidEdit(e);
        }
        else if (e.getKey().equals("vfree0"))
        {
          try {
            stopEditing();
            InvVO voInv = null;
            if (this.alInvs.size() > e.getRow())
              voInv = (InvVO)this.alInvs.get(e.getRow());
            setBodyFreeValue(e.getRow(), voInv);
            getFreeItemRefPane().setFreeItemParam(voInv);
          } catch (Exception ex) {
            SCMEnv.out("自由项设置失败!");
          }
        }
      }
    }
    getSORefDelegate().beforeEdit(e);

    return bret;
  }

  private void beforeInventoryEdit(BillEditEvent e)
  {
    UIRefPane invRef = (UIRefPane)getBodyItem("cinventorycode").getComponent();

    if (invRef.getRefPK() == null) {
      invRef.setPK(getBodyValueAt(e.getRow(), "cinventoryid"));
    }

    if ((getSouceBillType().equals("Z4")) || (getSouceBillType().equals("Z3")))
    {
      beforeCtInvEdit(e);
    }
    else if (invRef == null);
    AbstractRefModel m = invRef.getRefModel();
    String calid = (String)getBodyValueAt(e.getRow(), "cadvisecalbodyid");
    if ((calid != null) && (calid.trim().length() > 0)) {
      String[] o = { this.pk_corp, calid };
      m.setUserParameter(o);
    } else {
      invRef.setPK(null);
    }

    if ((this.uipanel.SO_03 != null) && (!this.uipanel.SO_03.booleanValue()))
      m.setFilterPks(getInvPks(), 1);
  }

  private String[] getInvPks()
  {
    int rowCount = getRowCount();
    ArrayList list = new ArrayList();
    int icurrow = getBillTable().getSelectedRow();
    Object temp = null;
    for (int i = 0; i < rowCount; i++) {
      if (i == icurrow)
        continue;
      temp = getBodyValueAt(i, "cinventoryid");
      if ((temp != null) && (!list.contains(temp))) {
        list.add(temp);
      }
    }
    String[] sResults = new String[list.size()];
    list.toArray(sResults);
    return sResults;
  }

  private void beforeCtInvEdit(BillEditEvent ev)
  {
    UIRefPane refInv = (UIRefPane)getBodyItem("cinventorycode").getComponent();

    if (refInv == null)
      return;
    if ((getSouceBillType().equals("Z4")) || (getSouceBillType().equals("Z3")))
    {
      String sCtinvclid = (String)getBodyValueAt(ev.getRow(), "ctinvclassid");

      if ((sCtinvclid == null) || (sCtinvclid.length() == 0))
      {
        refInv.getRefModel().setWherePart(this.sInvRefCondition);
      }
      else {
        String[] formula = { "ctinvclassid->getColValue(bd_invcl,invclasscode,pk_invcl,ctinvclassid)" };
        execBodyFormulas(ev.getRow(), formula);
        String sCtinvclcode = (String)getBodyValueAt(ev.getRow(), "ctinvclassid");

        setBodyValueAt(sCtinvclid, ev.getRow(), "ctinvclassid");

        setCellEditable(ev.getRow(), "cinventorycode", true);
        refInv.getRefModel().setWherePart(this.sInvRefCondition + " AND pk_invcl IN (SELECT pk_invcl FROM bd_invcl WHERE invclasscode LIKE '" + sCtinvclcode + "%') ");
      }

    }
    else
    {
      setCellEditable(ev.getRow(), "cinventorycode", true);
      refInv.getRefModel().setWherePart(this.sInvRefCondition);
    }
  }

  private void beforeBodyAddressEdit(BillEditEvent ev)
  {
    String sCustManID = (String)(String)getBodyValueAt(ev.getRow(), "creceiptcorpid");

    UIRefPane vreceiveaddress = (UIRefPane)getBodyItem("vreceiveaddress").getComponent();

    ((CustAddrRefModel)vreceiveaddress.getRefModel()).setCustId(sCustManID);
  }

  private void beforeBatchidEdit(BillEditEvent ev)
  {
    if (getBillCardTools().isOtherCorpRow(ev.getRow())) {
      setCellEditable(ev.getRow(), "cbatchid", false);
      return;
    }
    stopEditing();
    int iEventRow = ev.getRow();
    try {
      Object tempO = getBodyValueAt(iEventRow, "nnumber");
      UFDouble dNumber = (tempO == null) || (tempO.toString().trim().length() == 0) ? new UFDouble(0) : (UFDouble)(UFDouble)getBodyValueAt(iEventRow, "nnumber");

      if (dNumber.compareTo(new UFDouble(0)) < 0)
      {
        this.tfieldBatch.setMaxLength(30);
        getBodyPanel().setTableCellEditor("cbatchid", new BillCellEditor(this.tfieldBatch));
      }
      else {
        getBodyPanel().setTableCellEditor("cbatchid", new BillCellEditor(getLotNumbRefPane()));

        WhVO voWh = null;
        String idCalbody = getHeadItem("ccalbodyid").getValue() == null ? "" : getHeadItem("ccalbodyid").getValue();

        Object oTemp = getBodyValueAt(iEventRow, "cbodywarehouseid");
        String idWareHouse = oTemp == null ? "" : oTemp.toString().trim();

        if ((idCalbody.length() != 0) || (idWareHouse.length() != 0)) {
          voWh = new WhVO();
          voWh.setPk_calbody(idCalbody);
          voWh.setPk_corp(this.pk_corp);
          voWh.setCwarehouseid(idWareHouse);
          voWh.setCwarehousename((String)getBodyValueAt(iEventRow, "cbodywarehousename"));
        }

        InvVO voInv = new InvVO();
        if (this.alInvs.size() > iEventRow) {
          voInv = (InvVO)this.alInvs.get(iEventRow);
        }
        setBodyFreeValue(iEventRow, voInv);
        Object invID = getBodyValueAt(iEventRow, "cinventoryid");
        if (invID != null) {
          LotNumbRefPane batchref = (LotNumbRefPane)(UIRefPane)getBodyItem("cbatchid").getComponent();

          batchref.setAutoCheck(false);

          batchref.setParameter(voWh, voInv);
        }
      }
    } catch (Exception e1) {
      SCMEnv.out("批次查询失败！");
    }
  }

  protected LotNumbRefPane getLotNumbRefPane()
  {
    if (this.ivjLotNumbRefPane == null) {
      try {
        this.ivjLotNumbRefPane = new LotNumbRefPane();
        this.ivjLotNumbRefPane.setName("LotNumbRefPane");
        this.ivjLotNumbRefPane.setLocation(38, 1);
        this.ivjLotNumbRefPane.setMaxLength(30);
      } catch (Throwable ivjExc) {
        handleException(ivjExc);
      }
    }
    return this.ivjLotNumbRefPane;
  }

  private SORefDelegate getSORefDelegate() {
    if (this.soRefDelegate == null)
      this.soRefDelegate = new SORefDelegate(this.uipanel);
    return this.soRefDelegate;
  }

  public void onMenuItemClick(ActionEvent e)
  {
    UIMenuItem item = (UIMenuItem)e.getSource();

    if (item == getInsertLineMenuItem())
      this.uipanel.onInsertLine();
    else if (item == getAddLineMenuItem())
      this.uipanel.onAddLine();
    else if (item == getDelLineMenuItem())
      this.uipanel.onDelLine();
    else if (item == getCopyLineMenuItem())
      this.uipanel.onCopyLine();
    else if (item == getPasteLineMenuItem())
      this.uipanel.onPasteLine();
    else if (item == getPasteLineToTailMenuItem()) {
      this.uipanel.onPasteLineToTail();
    }

    this.uipanel.setButtonsState();
  }

  public void actionPerformed(ActionEvent e) {
  }

  public SOBillCardTools getBillCardTools() {
    return this.uipanel.getBillCardTools();
  }

  protected void handleException(Throwable exception)
  {
    SCMEnv.out("--------- 未捕捉到的异常 ---------");
    SCMEnv.out(exception);
  }

  public void cleanNullLine()
  {
    int rowCount = getRowCount();
    InvVO invvo = null;
    for (int i = rowCount - 1; i >= 0; i--) {
      Object oTemp = getBodyValueAt(i, "cinventoryid");
      if ((oTemp == null) || (oTemp.toString().trim().length() == 0)) {
        setBodyValueAt(null, i, "vfree1");
        setBodyValueAt(null, i, "vfree2");
        setBodyValueAt(null, i, "vfree3");
        setBodyValueAt(null, i, "vfree4");
        setBodyValueAt(null, i, "vfree5");
      }
      else if ((this.alInvs != null) && (this.alInvs.size() > i)) {
        invvo = (InvVO)this.alInvs.get(i);
        if ((invvo != null) && (oTemp.equals(invvo.getCinventoryid()))) {
          if ((invvo.getFreeItemVO() == null) || (invvo.getFreeItemVO().getVfreeid1() == null) || (invvo.getFreeItemVO().getVfreeid1().trim().length() <= 0))
          {
            setBodyValueAt(null, i, "vfree1");
          }if ((invvo.getFreeItemVO() == null) || (invvo.getFreeItemVO().getVfreeid2() == null) || (invvo.getFreeItemVO().getVfreeid2().trim().length() <= 0))
          {
            setBodyValueAt(null, i, "vfree2");
          }if ((invvo.getFreeItemVO() == null) || (invvo.getFreeItemVO().getVfreeid3() == null) || (invvo.getFreeItemVO().getVfreeid3().trim().length() <= 0))
          {
            setBodyValueAt(null, i, "vfree3");
          }if ((invvo.getFreeItemVO() == null) || (invvo.getFreeItemVO().getVfreeid4() == null) || (invvo.getFreeItemVO().getVfreeid4().trim().length() <= 0))
          {
            setBodyValueAt(null, i, "vfree4");
          }if ((invvo.getFreeItemVO() != null) && (invvo.getFreeItemVO().getVfreeid5() != null) && (invvo.getFreeItemVO().getVfreeid5().trim().length() > 0))
          {
            continue;
          }
          setBodyValueAt(null, i, "vfree5");
        }
      }
    }

    for (int i = rowCount - 1; i >= 0; i--) {
      Object oTemp = getBodyValueAt(i, "cinventoryid");
      if ((oTemp == null) || (oTemp.toString().trim().length() == 0)) {
        int[] rowIndex = { i };
        getBillData().getBillModel().delLine(rowIndex);
        if ((this.alInvs != null) && (this.alInvs.size() > i)) {
          this.alInvs.remove(i);
        }

        if ((this.uipanel.vRowATPStatus == null) || (this.uipanel.vRowATPStatus.size() <= i) || (this.uipanel.vRowATPStatus.size() <= 0)) {
          continue;
        }
        this.uipanel.vRowATPStatus.remove(i);
      }
    }
  }

  public void setBindAndLargeWhenPaste(int startrow, int stoprow)
  {
    UFBoolean bLargess = null;
    String sPk = null;
    for (int i = stoprow; i >= startrow; i--) {
      bLargess = new UFBoolean(getBodyValueAt(i, "blargessflag") == null ? "false" : getBodyValueAt(i, "blargessflag").toString());

      sPk = (String)getBodyValueAt(i, "cinventoryid");
      if ((bLargess != null) && (bLargess.booleanValue()))
        continue;
      if ((sPk != null) && (sPk.trim().length() > 0)) {
        setBodyValueAt(null, 0, "clargessrowno");
        afterInventoryMutiEdit(i, new String[] { sPk }, false, true, null, true, 2);
      }
    }
  }

  public void initUnit()
  {
    if (getRowCount() <= 0)
      return;
    try
    {
      String[] formulas = new String[4];

      formulas[0] = "scalefactor->getColValue2(bd_convert,mainmeasrate,pk_invbasdoc,cinvbasdocid,pk_measdoc,cpackunitid)";

      formulas[1] = "fixedflag->getColValue2(bd_convert,fixedflag,pk_invbasdoc,cinvbasdocid,pk_measdoc,cpackunitid)";

      formulas[2] = "nqtscalefactor->getColValue2(bd_convert,mainmeasrate,pk_invbasdoc,cinvbasdocid,pk_measdoc,cquoteunitid)";

      formulas[3] = "bqtfixedflag->getColValue2(bd_convert,fixedflag,pk_invbasdoc,cinvbasdocid,pk_measdoc,cquoteunitid)";

      getBillModel().execFormulas(formulas);

      UFBoolean fixedflag = null;
      UFBoolean bqtfixedflag = null;

      int i = 0; for (int count = getRowCount(); i < count; i++) {
        String cunitid = (String)getBodyValueAt(i, "cunitid");
        String cpackunitid = (String)getBodyValueAt(i, "cpackunitid");

        if ((cunitid != null) && (cpackunitid != null)) {
          if (cpackunitid.equals(cunitid)) {
            setBodyValueAt(new UFDouble(1), i, "scalefactor");
            setBodyValueAt(new UFBoolean("Y"), i, "fixedflag");
          }
        }
        else {
          setBodyValueAt(null, i, "cpackunitid");
          setBodyValueAt(null, i, "cpackunitname");
        }

        String cquoteunitid = (String)getBodyValueAt(i, "cquoteunitid");

        if ((cunitid != null) && (cquoteunitid != null)) {
          if (cquoteunitid.equals(cunitid)) {
            setBodyValueAt(new UFDouble(1), i, "nqtscalefactor");
            setBodyValueAt(new UFBoolean("Y"), i, "bqtfixedflag");
          }
        } else if ((cunitid != null) && (cquoteunitid == null))
        {
          setBodyValueAt(cunitid, i, "cquoteunitid");
          setBodyValueAt(getBodyValueAt(i, "cunitname"), i, "cquoteunit");

          setBodyValueAt(new UFDouble(1), i, "nqtscalefactor");
          setBodyValueAt(new UFBoolean("Y"), i, "bqtfixedflag");
        }
        else {
          setBodyValueAt(null, i, "cquoteunitid");
          setBodyValueAt(null, i, "cquoteunit");
        }

        boolean assistunit = ((Boolean)getBodyValueAt(i, "assistunit")).booleanValue();
        fixedflag = getBillCardTools().getBodyUFBooleanValue(i, "fixedflag");

        if ((assistunit) && (fixedflag != null) && (!fixedflag.booleanValue()))
        {
          setBodyValueAt(BillTools.calc(BillTools.value(i, "nnumber", new UFDouble(1), getBillModel()), BillTools.value(i, "npacknumber", new UFDouble(1), getBillModel()), 3), i, "scalefactor");
        }

        bqtfixedflag = getBillCardTools().getBodyUFBooleanValue(i, "bqtfixedflag");

        if ((bqtfixedflag != null) && (!bqtfixedflag.booleanValue())) {
          setBodyValueAt(BillTools.calc(BillTools.value(i, "nnumber", new UFDouble(1), getBillModel()), BillTools.value(i, "nquoteunitnum", BillTools.value(i, "npacknumber", new UFDouble(1), getBillModel()), getBillModel()), 3), i, "bqtfixedflag");
        }

        InvVO voInv = null;
        if ((this.alInvs != null) && (this.alInvs.size() > i))
          voInv = (InvVO)this.alInvs.get(i);
        if (voInv != null)
        {
          if ((voInv.getIsAstUOMmgt() != null) && (voInv.getIsAstUOMmgt().intValue() == 1))
          {
            setBodyValueAt("Y", i, "assistunit");
          }
          else setBodyValueAt("N", i, "assistunit");

          voInv.setCastunitid(cpackunitid);
          voInv.setCastunitname((String)getBodyValueAt(i, "cpackunitname"));
        }

        setAssistUnit(i);
      }
    }
    catch (Exception ex) {
      SCMEnv.out("初始化换算率失败!");
    }
  }

  protected void setInputLimit()
  {
    getHeadItem("salecorp").setEnabled(false);
    getHeadItem("salecorp").setEdit(false);
    getHeadItem("nreceiptcathmny").setEnabled(false);
    getHeadItem("nreceiptcathmny").setEdit(false);

    getBodyItem("cprolinename").setEdit(false);
    getBodyItem("cprolinename").setEnabled(false);

    ((UIRefPane)getHeadItem("ndiscountrate").getComponent()).setMinValue(0.0D);

    ((UIRefPane)getBodyItem("nitemdiscountrate").getComponent()).setMinValue(0.0D);

    ((UIRefPane)getBodyItem("ntaxrate").getComponent()).setMinValue(0.0D);

    ((UIRefPane)getHeadItem("npreceiverate").getComponent()).setMinValue(0.0D);

    ((UIRefPane)getHeadItem("npreceiverate").getComponent()).setMaxValue(100.0D);

    ((UIRefPane)getHeadItem("npreceiverate").getComponent()).setMaxLength(20);

    getHeadItem("npreceiverate").setLength(20);

    ((UIRefPane)getHeadItem("naccountperiod").getComponent()).setMinValue(0.0D);

    ((UIRefPane)getBodyItem("nreturntaxrate").getComponent()).setMinValue(0.0D);

    ((UIRefPane)getBodyItem("nreturntaxrate").getComponent()).setMaxValue(100.0D);

    UIRefPane ref = null;

    if (getBodyItem("nouttoplimit") != null) {
      ((UIRefPane)getBodyItem("nouttoplimit").getComponent()).setMinValue(0.0D);
    }
    if (getBodyItem("noutcloselimit") != null) {
      ((UIRefPane)getBodyItem("noutcloselimit").getComponent()).setMinValue(0.0D);
    }

    if ((getBillType().equals("30")) || (getBillType().equals("3A")))
    {
      UIRefPane refInv = (UIRefPane)getBodyItem("cinventorycode").getComponent();

      if (refInv != null)
      {
        refInv.getRefModel().setIsDynamicCol(true);
        refInv.getRefModel().setDynamicColClassName("nc.ui.scm.pub.RefDynamic");

        if (refInv.getRefModel().getWherePart().indexOf("and bd_invmandoc.iscansold ='Y'") < 0)
        {
          refInv.getRefModel().setWherePart(refInv.getRefModel().getWherePart() + " and bd_invmandoc.iscansold ='Y' ");
        }

        if (this.sInvRefCondition == null) {
          this.sInvRefCondition = refInv.getRefModel().getWherePart();
        }

      }

      ref = (UIRefPane)getBodyItem("cconsigncorp").getComponent();
      ref.getRefModel().setNotLeafSelectedEnabled(true);
    }

    getBillCardTools().setHeadRefLimit(this.strState);
  }

  public UFDouble calcurateTotal(String key)
  {
    UFDouble total = SoVoConst.duf0;

    for (int i = 0; i < getRowCount(); i++) {
      UFBoolean blargessflag = getBillCardTools().getBodyUFBooleanValue(i, "blargessflag");

      UFBoolean boosflag = getBillCardTools().getBodyUFBooleanValue(i, "boosflag");

      if ((SaleorderBVO.isPriceOrMny(key)) && (((blargessflag != null) && (blargessflag.booleanValue())) || ((boosflag != null) && (boosflag.booleanValue()))))
      {
        continue;
      }

      Object value = getBodyValueAt(i, key);
      String v = (value == null) || (value.toString().trim().length() <= 0) ? "0" : value.toString();

      total = total.add(new UFDouble(v));
    }

    return total;
  }

  public void afterSort(String key)
  {
    if ((this.strState.equals("修改")) || (this.strState.equals("修订")) || (this.strState.equals("新增")))
    {
      initFreeItem();

      InvAttrCellRenderer ficr = new InvAttrCellRenderer();
      ficr.setFreeItemRenderer(this, this.alInvs);
    }
  }

  public Object[] getRelaSortObjectArray()
  {
    AggregatedValueObject avo = this.uipanel.getVo();

    return avo.getChildrenVO();
  }

  public void addSortLstn() {
    getBillModel().addSortListener(this);
  }

  public void addSortRelaLstn() {
    getBillModel().addSortRelaObjectListener2(this);
  }

  public void addEditLstn() {
    addEditListener("table", this);
  }

  public void addEditLstn2() {
    addBodyEditListener2("table", this);
  }

  public void addMenuLstn() {
    addBodyMenuListener("table", this);
  }

  public void addTotalLstn() {
    addBodyTotalListener("table", this);
  }
}