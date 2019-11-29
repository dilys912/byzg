package nc.ui.dm.dm103;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;

import nc.bs.framework.common.NCLocator;
import nc.itf.baoyin.common.PubDelegator;
import nc.itf.scm.pub.bill.IScm;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bd.datapower.DataPowerServ;
import nc.ui.dm.dm001.DelivorgHelper;
import nc.ui.dm.dm006.VehicleHelper;
import nc.ui.dm.dm102.DeliverydailyplanBO_Client;
import nc.ui.dm.dm102.PlanFormula;
import nc.ui.dm.dm102.QueryConditionDlg;
import nc.ui.dm.dm104.DelivBillFormula;
import nc.ui.dm.dm104.DelivbillHBO_Client;
import nc.ui.dm.pub.ClientUIforPlan;
import nc.ui.dm.pub.DMBillStatus;
import nc.ui.dm.pub.DMQueryConditionDlg;
import nc.ui.dm.pub.DmHelper;
import nc.ui.dm.pub.cardpanel.DMBillCardPanel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonBar;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.MenuButton;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.util.MiscUtils;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.change.PfChangeBO_Client;
import nc.ui.pub.rino.DbUtil;
import nc.ui.scm.pub.ArryFormula;
import nc.ui.scm.pub.CacheTool;
import nc.ui.scm.pub.FreeVOParse;
import nc.ui.scm.pub.bill.IBillExtendFun;
import nc.ui.scm.pub.query.ConvertQueryCondition;
import nc.ui.scm.pub.report.OrientDialog;
import nc.vo.dm.dm001.DelivorgHeaderVO;
import nc.vo.dm.dm001.DelivorgVO;
import nc.vo.dm.dm006.DmVehicleItemVO;
import nc.vo.dm.dm104.DelivbillHHeaderVO;
import nc.vo.dm.dm104.DelivbillHItemVO;
import nc.vo.dm.dm104.DelivbillHVO;
import nc.vo.dm.dm104.FreightType;
import nc.vo.dm.pub.DMBillTypeConst;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.dm.pub.DMVO;
import nc.vo.dm.pub.DailyPlanStatus;
import nc.vo.dm.pub.DelivBillStatus;
import nc.vo.dm.pub.tools.StringTools;
import nc.vo.dm.pub.tools.UsefulTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.sm.UserVO;

public class ClientUI extends ClientUIforPlan
  implements IBillExtendFun
{
  private static final long serialVersionUID = 1745565793424361359L;
  protected ButtonObject boCalculateFee;
  protected ButtonObject boCancelConfirm;
  protected ButtonObject boGenDeliv;
  protected ButtonObject boGenDelivBatch;
  protected ButtonObject boGenTaskBill;
  protected ButtonObject boInsertDeliv;
  protected ButtonObject boOpenBill;
  protected ButtonObject boOutBill;
  protected ButtonObject boPackageList;
  protected ButtonObject boPrintDelivMinBill;
  protected ButtonObject boQueryDelivDaily;
  private ButtonObject boRowCloseOut;
  private ButtonObject boRowOpenOut;
  protected ButtonObject boSelectAll;
  protected ButtonObject boTestCalculate;
  protected ButtonObject boTransConfirm;
  protected ButtonObject boUnSelectAll;
  private ArrayList m_alOldDelivVOs = new ArrayList();
  public String[] m_aryPlanFormulasBody;
  protected boolean m_isOperationFinish = false;

  private boolean m_bInsertDlvbill = false;
  private ChooseDeliveryBillDLG m_ChooseDeliveryDLG;
  private nc.ui.dm.dm104.ClientUI m_DelivBillClientUI;
  private DMVO m_dmOldPlanVO;
  public Hashtable m_htbPlanBodyItemkey;
  private String m_sTempBodyPK = "thisBody000000000001";

  private String m_sTempHeadPK = "thisHeader0000000001";

  private UFDouble m_ufdLastValue = new UFDouble(0);
  protected DMQueryConditionDlg queryDelivDailyConditionDlg;
  private boolean showed = false;

  private Vector vSrcBillPK4Update = new Vector();

  private int opentype = -1;

  public ClientUI(FramePanel fp) {
    this.opentype = fp.getLinkType();
  }

  protected String checkPrerequisite()
  {
    if (this.opentype != 3) {
      try {
        initializeNew();
      }
      catch (Error ex) {
        ex.printStackTrace();
        return ex.getMessage();
      }
    }
    return null;
  }

  protected void afterDelivDailyQuery(ConditionVO[] voCons)
  {
    setShowState(DMBillStatus.Source);
    loadPanel();
    setTitleText(this.strTitle3);

    DMDataVO[] dmdvos = loadThdBillData(null, voCons, false);

    DMVO dmvo = (DMVO)getThdBillCardPanel().getBillValueVO(DMVO.class.getName(), DMDataVO.class.getName(), DMDataVO.class.getName());

    String[] sSortKeys = { "vsrcbillnum", "vcustname", "vdeststoreorgname", "vsendtypename", "creceiptcorpname" };

    int[] iSortTypes = { 1, 1, 1, 1, 1 };

    int[] iNewRowNumber = dmvo.sortByKeys(sSortKeys, iSortTypes);

    getThdBillCardPanel().setBillValueVO(dmvo);
  }

  protected void afterGenNewLine(AggregatedValueObject vo, String PKFieldName)
  {
    getAllVOs().add(vo);
    CircularlyAccessibleValueObject[] bodyVOs = vo.getChildrenVO();
    for (int w = 0; w < bodyVOs.length; w++) {
      CircularlyAccessibleValueObject bodyVO = bodyVOs[w];
      String PKValue = bodyVO.getAttributeValue(PKFieldName).toString();
      CircularlyAccessibleValueObject headVO = vo.getParentVO();

      DelivbillHVO avo = new DelivbillHVO();
      DelivbillHItemVO[] bodynewVOs = new DelivbillHItemVO[1];
      bodynewVOs[0] = ((DelivbillHItemVO)bodyVO);
      avo.setParentVO(headVO);
      avo.setChildrenVO(bodynewVOs);
      DMDataVO[] ddvos = convertVOtoOneLine(avo);
      ((DMBillCardPanel)getBillCardPanel()).addLine(ddvos[0]);
    }
  }

  protected void afterInsertGenNewLine(DelivbillHVO dhvoAll, DelivbillHVO dhvo, String PKFieldName)
  {
    String PKValue = dhvo.getChildrenVO()[0].getAttributeValue(PKFieldName).toString();

    DelivbillHHeaderVO headVO = null;

    for (int i = 0; i < getAllVOs().size(); i++) {
      headVO = (DelivbillHHeaderVO)((DelivbillHVO)getAllVOs().get(i)).getParentVO();

      if (headVO.getAttributeValue(PKFieldName).toString().equals(PKValue)) {
        getAllVOs().set(i, dhvoAll);
      }

    }

    DMDataVO[] ddvos = convertVOtoOneLine(dhvo);
    for (int i = 0; i < ddvos.length; i++)
      ((DMBillCardPanel)getBillCardPanel()).addLine(ddvos[i]);
  }

  public void afterSwitchForm(int row)
  {
    switchListToCard(row);
  }

  public boolean beforeEdit(BillEditEvent e)
  {
    return true;
  }

  public void bodyRowChange(BillEditEvent e)
  {
    super.bodyRowChange(e);

    if (e.getSource() == getBillCardPanel().getBillTable()) {
      afterSwitchForm(e.getRow());
    }

    if (this.m_strShowState.equals(DMBillStatus.List)) {
      getDelivBillClientUI().listHeadRowChange(e);
      switchButtonStatus(DMBillStatus.ListView);
    }
  }

  /** @deprecated */
  private boolean checkDlvbillwhenInsert(DMDataVO[] ddvos)
  {
    boolean result = false;
    try
    {
      DelivbillHVO[] checkdelivbill = null;
      DMDataVO[] checkddvos = null;

      StringBuffer headerWhereStr = new StringBuffer("select count(*) as delivbillnum from dm_delivbill_b ");

      headerWhereStr.append(" inner join dm_delivbill_h on dm_delivbill_b.pk_delivbill_h=dm_delivbill_h.pk_delivbill_h ");

      headerWhereStr.append(" where dm_delivbill_h.dr=0 and dm_delivbill_b.dr=0 and dm_delivbill_h.bconfirm='N' AND  dm_delivbill_h.bmissionbill='N'");

      headerWhereStr.append(" and dm_delivbill_h.pkdelivorg = '").append(getDelivOrgPK()).append("'");

      headerWhereStr.append(" and dm_delivbill_h.pkdelivmode='" + (ddvos[0].getAttributeValue("pksendtype") == null ? "" : ddvos[0].getAttributeValue("pksendtype")) + "'");

      int iFree = DelivBillStatus.Free;
      StringBuffer whereStr = new StringBuffer(" and (irowstatus=");
      whereStr.append(iFree);
      whereStr.append(" or irowstatus IS NULL ) ");

      StringBuffer sb = new StringBuffer();

      int iSize = ddvos.length;

      int j = 0;
      for (int i = 0; i < iSize; i++) {
        String pkcust = (String)ddvos[i].getAttributeValue("pkcust");
        String pkcalbody = (String)ddvos[i].getAttributeValue("pkdeststoreorg");

        if (j != 0) {
          sb.append(" or ");
        }
        if (pkcust != null) {
          sb.append(" pkcusmandoc='");
          sb.append(pkcust);
          sb.append("' ");
        }
        else {
          sb.append(" pkcusmandoc is null ");
        }

        if (pkcalbody != null) {
          sb.append(" and pkdeststockorg='");
          sb.append(pkcalbody);
          sb.append("' ");
        }
        else {
          sb.append("and  pkdeststockorg is null ");
        }

        j++;
      }

      if (sb.length() > 0) {
        whereStr.append(" and ( " + sb.toString() + " )");
      }

      long t = System.currentTimeMillis();
      checkddvos = DmHelper.queryStringBuffer(headerWhereStr.append(whereStr.toString()));

      t = System.currentTimeMillis() - t;
      SCMEnv.info("only for test 执行方法: checkDlvbillwhenInsert :所消耗的时间为： " + t / 60000L + " 分 " + t / 1000L % 60L + " 秒 \n");

      if ((checkddvos != null) && (checkddvos.length > 0) && (checkddvos[0].getAttributeValue("delivbillnum") != null))
      {
        if (Integer.parseInt(checkddvos[0].getAttributeValue("delivbillnum").toString()) > 0)
        {
          int nDialog = showYesNoMessage(NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000129"));

          if (nDialog != 4)
            result = true;
        }
        //update by danxionghui 
        //运输方式
//        Object pkdelivorg=ddvos[0].getAttributeValue("pkdelivorg");
        //edit by shikun 2014-11-27 推广公司加校验
        String pkdelivorg=ddvos[0].getAttributeValue("pkdelivorg")==null?"":ddvos[0].getAttributeValue("pkdelivorg").toString();
        DelivorgVO bill = DelivorgHelper.findByPrimaryKey(pkdelivorg);
        if (bill!=null) {
            DelivorgHeaderVO head = (DelivorgHeaderVO)bill.getParentVO();
            if (head!=null) {
                String idelivsequence = head.getAttributeValue("vdoname")==null?"":head.getAttributeValue("vdoname").toString();
                if (!"".equals(idelivsequence)&&idelivsequence.indexOf("自提")==-1) {
                    Object pksendtype = ddvos[0].getAttributeValue("pksendtype");
                    //发货地址pksendaddress  
                    Object pksendaddress = ddvos[0].getAttributeValue("pksendaddress");
                    //到货地址vdestaddress
                    Object varriveaddress = ddvos[0].getAttributeValue("pkarriveaddress");
                    Object senddate = ddvos[0].getAttributeValue("requiredate");
                   String sql=" select * from dm_baseprice  where pk_sendtype='"+pksendtype+""
                    		+ "' and pkfromaddress ='"+pksendaddress+"' and pktoaddress='"+varriveaddress
                            +"' and effectdate <= '"+senddate.toString()+"' and expirationdate > '" + senddate.toString()+"' and nvl(dr,0)=0 ";
                   Object obj= null;
                	obj = PubDelegator.getUAPQueryBS().executeQuery(sql, new ColumnProcessor());
                    if(obj==null){
                    	showErrorMessage("没有有效的运输协议信息，请联系物流部门增加运费价格表的相关数据");
                    	result=true;;
                    }//////end 
                }
            }
        }
//        if(pkdelivorg.equals("1016A21000000000WLSK")||pkdelivorg=="1016A21000000000WLSK"){}
        
      }
    }
    catch (Exception e1)
    {
      showErrorMessage(e1.toString());
      handleException(e1);
    }
    return result;
  }

  /** @deprecated */
  private ArrayList checkDplwhenInsert(DMDataVO[] ddvos)
  {
    ArrayList list = null;
    UFBoolean result = new UFBoolean(false);
    try
    {
      StringBuffer whereStr = new StringBuffer("select vdelivdayplcode,pk_delivdaypl from dm_delivdaypl where dm_delivdaypl.dr=0 ");

      whereStr.append(" and dm_delivdaypl.pkdelivorg = '").append(getDelivOrgPK()).append("'");

      whereStr.append(" and dm_delivdaypl.pksendtype='" + (ddvos[0].getAttributeValue("pksendtype") == null ? "" : ddvos[0].getAttributeValue("pksendtype")) + "'");

      int iSize = ddvos.length;
      for (int i = 0; i < iSize; i++) {
        String pk_delivdaypl = (String)ddvos[i].getAttributeValue("pk_delivdaypl");
        if ((pk_delivdaypl != null) && (pk_delivdaypl.trim().length() > 0)) {
          whereStr.append("and pk_delivdaypl<>'");
          whereStr.append(pk_delivdaypl);
          whereStr.append("'");
        }

      }

      DMDataVO[] checkddvos = null;

      int iFree = DailyPlanStatus.Free;
      int iAudit = DailyPlanStatus.Audit;
      whereStr.append(" and (iplanstatus=");

      whereStr.append(iAudit);

      whereStr.append(" ) and (");
      ArrayList al = getAgentCorpIDsofDelivOrg();
      if ((null != al) && (al.size() != 0)) {
        whereStr.append("dm_delivdaypl.pksalecorp=");
        iSize = al.size();
        for (int i = 0; i < iSize; i++) {
          whereStr.append("'").append(al.get(i)).append("'");
          if (i != al.size() - 1) {
            whereStr.append(" or dm_delivdaypl.pksalecorp=");
          }
        }

        whereStr.append(") ");
      }
      else {
        whereStr.append("1=0 ) ");
      }

      StringBuffer sb = new StringBuffer();

      Hashtable htVbilltype = new Hashtable();
      iSize = ddvos.length;
      int j = 0;
      for (int i = 0; i < iSize; i++) {
        String pkcust = (String)ddvos[i].getAttributeValue("pkcust");
        String pkcalbody = (String)ddvos[i].getAttributeValue("pkdeststoreorg");

        if (j != 0) {
          sb.append(" or ");
        }
        if ((pkcust != null) && (pkcust.length() > 0)) {
          sb.append(" pkcust='");
          sb.append(pkcust);
          sb.append("' ");
        }
        else {
          sb.append(" pkcust is null ");
        }

        if ((pkcalbody != null) && (pkcalbody.length() > 0)) {
          sb.append("and  pkdeststoreorg='");
          sb.append(pkcalbody);
          sb.append("' ");
        }
        else {
          sb.append("and  pkdeststoreorg is null ");
        }
        htVbilltype.put(ddvos[i].getAttributeValue("vbilltype"), "");
        j++;
      }

      if (sb.length() > 0) {
        whereStr.append(" and ( " + sb.toString() + " )");
      }

      ArrayList alVbilltypes = UsefulTools.hashtableKeysToArrayList(htVbilltype);

      if (alVbilltypes.get(0).toString().equals("21")) {
        whereStr.append(" and ( dm_delivdaypl.vbilltype = '21' )");
      }
      else {
        whereStr.append(" and ( dm_delivdaypl.vbilltype in ('30','5C','5D','5E','5I' ) )");
      }

      String sPower = DataPowerServ.getInstance().getSubSqlForMutilCorp("bd_calbody", "库存组织", ClientEnvironment.getInstance().getUser().getPrimaryKey(), (String[])(String[])getAgentCorpIDsofDelivOrg().toArray(new String[0]));

      if (StringTools.getSimilarString(sPower) != null) {
        whereStr.append(" AND dm_delivdaypl.pksendstoreorg IN " + sPower);
      }

      long t = System.currentTimeMillis();
      checkddvos = DmHelper.queryStringBuffer(whereStr);
      t = System.currentTimeMillis() - t;

      SCMEnv.info("only for test 执行方法: checkDplwhenInsert :所消耗的时间为： " + t / 60000L + " 分 " + t / 1000L % 60L + " 秒 \n");

      if ((checkddvos != null) && (checkddvos.length > 0) && (checkddvos[0].getAttributeValue("vdelivdayplcode") != null))
      {
        StringBuffer dplcode = new StringBuffer("");
        iSize = checkddvos.length;
        for (int i = 0; i < iSize; i++) {
          if (checkddvos[i].getAttributeValue("vdelivdayplcode") != null) {
            dplcode.append(",");
            dplcode.append(checkddvos[i].getAttributeValue("vdelivdayplcode"));
          }
        }
        int nDialog = showYesNoCancelMessage(NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000137", null, new String[] { dplcode.toString() }));

        if (nDialog == 4)
        {
          result = new UFBoolean(true);

          list = new ArrayList();
          list.add(result);
          list.add(checkddvos);
        }
        else if (nDialog == 8)
        {
          result = new UFBoolean(false);

          list = new ArrayList();
          list.add(result);
          list.add(null);
        }
      }
      else
      {
        result = new UFBoolean(false);

        list = new ArrayList();
        list.add(result);
        list.add(null);
      }

      if (list != null) {
        UFBoolean b = (UFBoolean)list.get(0);
        if (b.booleanValue()) {
          DMDataVO[] tempVO = (DMDataVO[])(DMDataVO[])list.get(1);
          if ((tempVO != null) && (tempVO.length > 0)) {
            String[] saPk_delivdaypl = new String[tempVO.length];
            for (int i = 0; i < tempVO.length; i++) {
              saPk_delivdaypl[i] = ((String)tempVO[i].getAttributeValue("pk_delivdaypl"));
            }

            DMDataVO[] dmdvos = queryDelivDailyPlan(saPk_delivdaypl, null);

            list.remove(1);
            list.add(dmdvos);
          }
        }
      }
    }
    catch (Exception e1) {
      showErrorMessage(e1.toString());
      handleException(e1);
    }

    return list;
  }

  private ArrayList checkDplwhenInsertBatch(DMDataVO[] ddvosForGen)
  {
    ArrayList list = null;
    UFBoolean result = new UFBoolean(false);
    if ((this.DM017 != null) && (this.DM017.equals("不合并")))
    {
      list = new ArrayList();
      list.add(result);
      list.add(null);
      return list;
    }

    Hashtable htSendStoreorg = new Hashtable();
    Hashtable htSendtype = new Hashtable();
    Hashtable htPkdelivdaypl = new Hashtable();
    Hashtable htPkarrivearea = new Hashtable();
    Hashtable htPksendarea = new Hashtable();
    Hashtable htPuBilltype = new Hashtable();
    Hashtable htOtherBilltype = new Hashtable();
    Hashtable htPlanSendDate = new Hashtable();
    Hashtable htBillCode = new Hashtable();
    for (int i = 0; i < ddvosForGen.length; i++)
    {
      if (!ddvosForGen[i].getAttributeValue("vbilltype").equals("21")) {
        if (ddvosForGen[i].getAttributeValue("pksendstoreorg") != null) {
          htSendStoreorg.put(ddvosForGen[i].getAttributeValue("pksendstoreorg"), "");
        }
        if (ddvosForGen[i].getAttributeValue("pkarrivearea") != null) {
          htPkarrivearea.put(ddvosForGen[i].getAttributeValue("pkarrivearea"), "");
        }
        htOtherBilltype.put(ddvosForGen[i].getAttributeValue("vbilltype"), "");
      }
      else
      {
        if ((ddvosForGen[i].getAttributeValue("borderreturn") != null) && (ddvosForGen[i].getAttributeValue("borderreturn").equals("Y")))
        {
          if (ddvosForGen[i].getAttributeValue("pkarrivearea") != null) {
            htPkarrivearea.put(ddvosForGen[i].getAttributeValue("pkarrivearea"), "");
          }
          if (ddvosForGen[i].getAttributeValue("pksendstoreorg") != null)
            htSendStoreorg.put(ddvosForGen[i].getAttributeValue("pksendstoreorg"), "");
        }
        else
        {
          if (ddvosForGen[i].getAttributeValue("pksendrea") != null) {
            htPksendarea.put(ddvosForGen[i].getAttributeValue("pksendrea"), "");
          }
          if (ddvosForGen[i].getAttributeValue("pkdeststoreorg") != null) {
            htSendStoreorg.put(ddvosForGen[i].getAttributeValue("pkdeststoreorg"), "");
          }
        }
        htPuBilltype.put(ddvosForGen[i].getAttributeValue("vbilltype"), "");
      }

      htSendtype.put(ddvosForGen[i].getAttributeValue("pksendtype"), "");
      htPkdelivdaypl.put(ddvosForGen[i].getAttributeValue("pk_delivdaypl"), "");

      UFDate ufdateSend = (UFDate)ddvosForGen[i].getAttributeValue("snddate");

      if ((ufdateSend != null) && (ufdateSend.toString().trim().length() != 0)) {
        htPlanSendDate.put(ufdateSend.toString(), "");
      }
      htBillCode.put(ddvosForGen[i].getAttributeValue("vsrcbillnum"), "");
    }

    String sIDofSendTypes = StringTools.getStrIDsFromHt(htSendtype);

    String sIDofSendStoreorg = StringTools.getStrIDsFromHt(htSendStoreorg);

    String sIDofPkdelilvdaypl = StringTools.getStrIDsFromHt(htPkdelivdaypl);

    String sIDofPkarrivearea = StringTools.getStrIDsFromHt(htPkarrivearea);

    String sIDofPksendarea = StringTools.getStrIDsFromHt(htPksendarea);

    String sPlansenddates = StringTools.getStrIDsFromHt(htPlanSendDate);

    String sBillCodes = StringTools.getStrIDsFromHt(htBillCode);

    Hashtable htCorpsID = new Hashtable();
    ArrayList al = getAgentCorpIDsofDelivOrg();
    if ((null != al) && (al.size() != 0)) {
      for (int i = 0; i < al.size(); i++) {
        htCorpsID.put(al.get(i), "");
      }
    }
    String sIDofPkCorps = StringTools.getStrIDsFromHt(htCorpsID);
    try
    {
      StringBuffer whereStr = new StringBuffer("select vdelivdayplcode,pk_delivdaypl from dm_delivdaypl where dm_delivdaypl.dr=0 ");

      whereStr.append("and (dm_delivdaypl.pk_delivdaypl not in (" + sIDofPkdelilvdaypl + "))");

      whereStr.append(" and (dm_delivdaypl.pkdelivorg = '").append(getDelivOrgPK()).append("')");

      whereStr.append(" and (dm_delivdaypl.pksendtype in (" + sIDofSendTypes + "))");

      whereStr.append(" and (dm_delivdaypl.dnum > dm_delivdaypl.dsendnum or dm_delivdaypl.dsendnum is null) ");
      if ((sIDofPkCorps != null) && (sIDofPkCorps.trim().length() != 0)) {
        whereStr.append(" and (dm_delivdaypl.pksalecorp in (" + sIDofPkCorps + ")) ");
      }

      if ((sBillCodes != null) && (sBillCodes.trim().length() != 0)) {
        whereStr.append(" and (dm_delivdaypl.vsrcbillnum in (" + sBillCodes + ")) ");
      }

      if ((sIDofPksendarea != null) && (sIDofPksendarea.trim().length() != 0) && (sIDofPkarrivearea != null) && (sIDofPkarrivearea.trim().length() != 0))
      {
        whereStr.append(" and ((dm_delivdaypl.pksendarea in (" + sIDofPksendarea + ") and dm_delivdaypl.vbilltype = '21' and dm_delivdaypl.borderreturn = 'N') or (dm_delivdaypl.pkarrivearea in (" + sIDofPkarrivearea + ")))");
      }
      else if ((sIDofPksendarea != null) && (sIDofPksendarea.trim().length() != 0)) {
        whereStr.append(" and (dm_delivdaypl.pksendarea in (" + sIDofPksendarea + ") and dm_delivdaypl.vbilltype = '21' and dm_delivdaypl.borderreturn = 'N') ");
      }
      else if ((sIDofPkarrivearea != null) && (sIDofPkarrivearea.trim().length() != 0))
      {
        whereStr.append(" and (dm_delivdaypl.pkarrivearea in (" + sIDofPkarrivearea + ")) ");
      }

      if ((sIDofSendStoreorg != null) && (sIDofSendStoreorg.trim().length() != 0)) {
        whereStr.append(" and (case when vbilltype = '21'then dm_delivdaypl.pkdeststoreorg else dm_delivdaypl.pksendstoreorg end in (" + sIDofSendStoreorg + "))");
      }

      whereStr.append(" and (dm_delivdaypl.iplanstatus = " + DailyPlanStatus.Audit + ")");

      String sVbilltype = new String();
      if ((htPuBilltype.size() != 0) && (htOtherBilltype.size() != 0)) {
        sVbilltype = "'21', '30','5C','5D','5E','5I' ";
      }
      else if (htPuBilltype.size() != 0) {
        sVbilltype = "'21'";
      }
      else if (htOtherBilltype.size() != 0) {
        sVbilltype = "'30','5C','5D','5E','5I' ";
      }
      if ((sVbilltype != null) && (sVbilltype.trim().length() != 0)) {
        whereStr.append(" and (dm_delivdaypl.vbilltype in (" + sVbilltype + "))");
      }

      if ((this.DM017 != null) && (this.DM017.equals("发货/到货库存组织+发运方式+到货/发货地区+计划发货日期"))) {
        whereStr.append(" and (dm_delivdaypl.snddate in (" + sPlansenddates + "))");
      }

      String sPower = DataPowerServ.getInstance().getSubSqlForMutilCorp("bd_calbody", "库存组织", getUserID(), (String[])(String[])getAgentCorpIDsofDelivOrg().toArray(new String[0]));

      if (StringTools.getSimilarString(sPower) != null) {
        whereStr.append(" AND case when vbilltype = '21' then dm_delivdaypl.pkdeststoreorg else dm_delivdaypl.pksendstoreorg end IN " + sPower);
      }

      long t = System.currentTimeMillis();
      DMDataVO[] checkddvosForGen = DmHelper.queryStringBuffer(whereStr);

      t = System.currentTimeMillis() - t;
      SCMEnv.out(" only for test 执行方法: checkDplwhenInsertBatch :所消耗的时间为：" + t + " ms。");

      if ((checkddvosForGen != null) && (checkddvosForGen.length > 0))
      {
        StringBuffer dplcode = new StringBuffer("");
        for (int i = 0; i < checkddvosForGen.length; i++) {
          if (checkddvosForGen[i].getAttributeValue("vdelivdayplcode") != null) {
            dplcode.append(",");
            dplcode.append(checkddvosForGen[i].getAttributeValue("vdelivdayplcode"));
          }
        }

        int nDialog = showYesNoCancelMessage(NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000138", null, new String[] { dplcode.toString() }));

        if (nDialog == 4)
        {
          result = new UFBoolean(true);

          list = new ArrayList();
          list.add(result);
          list.add(checkddvosForGen);
        }
        else if (nDialog == 8)
        {
          result = new UFBoolean(false);

          list = new ArrayList();
          list.add(result);
          list.add(null);
        }
      }
      else
      {
        result = new UFBoolean(false);

        list = new ArrayList();
        list.add(result);
        list.add(null);
      }

      if (list != null) {
        UFBoolean b = (UFBoolean)list.get(0);
        if (b.booleanValue()) {
          DMDataVO[] tempVO = (DMDataVO[])(DMDataVO[])list.get(1);
          if ((tempVO != null) && (tempVO.length > 0)) {
            String[] saPk_delivdaypl = new String[tempVO.length];
            for (int i = 0; i < tempVO.length; i++) {
              saPk_delivdaypl[i] = ((String)tempVO[i].getAttributeValue("pk_delivdaypl"));
            }

            DMDataVO[] dmdvos = queryDelivDailyPlan(saPk_delivdaypl, null);

            list.remove(1);
            list.add(dmdvos);
          }
        }
      }
    }
    catch (Exception e1) {
      showErrorMessage(e1.toString());
      handleException(e1);
    }

    return list;
  }

  protected DMDataVO[] convertArrayListtoLines(ArrayList alOldVOs)
  {
    if (alOldVOs.size() == 0)
      return null;
    Vector v = new Vector();
    for (int i = 0; i < alOldVOs.size(); i++) {
      DMDataVO[] ddvos = convertVOtoOneLine((AggregatedValueObject)alOldVOs.get(i));

      for (int j = 0; j < ddvos.length; j++) {
        v.add(ddvos[j]);
      }
    }

    DMDataVO[] ddvos = new DMDataVO[v.size()];
    v.copyInto(ddvos);

    return ddvos;
  }

  private DelivbillHVO convertFromDailyPlanToDeliv(DMDataVO[] dmdvos)
  {
    DelivbillHItemVO[] dhivos = new DelivbillHItemVO[dmdvos.length];

    for (int num = 0; num < dmdvos.length; num++) {
      dhivos[num] = new DelivbillHItemVO();

      Object dnum = dmdvos[num].getAttributeValue("dnum");
      Object dSendNum = dmdvos[num].getAttributeValue("dsendnum");
      Object dReturnNum = dmdvos[num].getAttributeValue("dreturnnum");
      if ((null == dnum) || (dnum.toString().trim().length() == 0)) {
        dnum = new UFDouble(0);
      }
      else {
        dnum = new UFDouble(dnum.toString().trim());
      }
      if ((null == dSendNum) || (dSendNum.toString().trim().length() == 0)) {
        dSendNum = new UFDouble(0);
      }
      else {
        dSendNum = new UFDouble(dSendNum.toString().trim());
      }
      if ((null == dReturnNum) || (dReturnNum.toString().trim().length() == 0)) {
        dReturnNum = new UFDouble(0);
      }
      else {
        dReturnNum = new UFDouble(dReturnNum.toString().trim());
      }
      UFDouble value = ((UFDouble)dnum).sub((UFDouble)dSendNum).add((UFDouble)dReturnNum);

      dhivos[num].setAttributeValue("dinvnum", value);

      dhivos[num].setAttributeValue("dinvweight", getNewValueByHSL(dmdvos[num].getAttributeValue("dweight"), dmdvos[num].getAttributeValue("dnum"), dhivos[num].getAttributeValue("dinvnum"), this.BD501));

      dhivos[num].setAttributeValue("dvolumn", getNewValueByHSL(dmdvos[num].getAttributeValue("dvolumn"), dmdvos[num].getAttributeValue("dnum"), dhivos[num].getAttributeValue("dinvnum"), this.BD501));

      dmdvos[num].setAttributeValue("dnum", dhivos[num].getAttributeValue("dinvnum"));

      dmdvos[num].setAttributeValue("dweight", dhivos[num].getAttributeValue("dinvweight"));

      dmdvos[num].setAttributeValue("dvolumn", dhivos[num].getAttributeValue("dvolumn"));

      Object oNum = dhivos[num].getAttributeValue("dinvnum");
      Object oPrice = dmdvos[num].getAttributeValue("dunitprice");
      if ((null == oNum) || (null == oPrice) || (oNum.toString().trim().length() == 0) || (oPrice.toString().trim().length() == 0))
      {
        dhivos[num].setAttributeValue("dmoney", null);
      }
      else {
        UFDouble ufdMoney = new UFDouble(oNum.toString()).multiply(new UFDouble(oPrice.toString()));

        dhivos[num].setAttributeValue("dmoney", ufdMoney);
      }
      dmdvos[num].setAttributeValue("dmoney", dhivos[num].getAttributeValue("dmoney"));

      Object dAstNum = dmdvos[num].getAttributeValue("dassistnum");
      if ((null == dAstNum) || (dAstNum.toString().trim().length() == 0)) {
        continue;
      }
      UFDouble valueAst = value.multiply(new UFDouble(dAstNum.toString()));
      if ((valueAst.doubleValue() != 0.0D) && (((UFDouble)dnum).doubleValue() != 0.0D))
      {
        valueAst = valueAst.div((UFDouble)dnum);
      }
      dhivos[num].setAttributeValue("dinvassist", valueAst);

      dmdvos[num].setAttributeValue("dassistnum", dhivos[num].getAttributeValue("dinvassist"));
    }

    DelivbillHVO dhvo = new DelivbillHVO();
    DMVO vo = new DMVO();
    vo.setChildrenVO(dmdvos);
    try
    {
      Object obj = PfChangeBO_Client.pfChangeBillToBill(vo, DMBillTypeConst.m_delivDelivDayPl, DMBillTypeConst.m_delivDelivBill);

      dhvo = (DelivbillHVO)obj;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    dhvo.getParentVO().setAttributeValue("pkdelivorg", getDelivOrgPK());
    dhvo.getParentVO().setAttributeValue("vdoname", getDelivOrgName());

    return dhvo;
  }

  private DelivbillHVO convertFromDailyPlanToDeliv1(DMDataVO[] dmdvos)
  {
    DelivbillHVO dhvo = new DelivbillHVO();
    DelivbillHItemVO[] dhivos = new DelivbillHItemVO[dmdvos.length];

    dhvo.getParentVO().setAttributeValue("pkdelivorg", getDelivOrgPK());
    dhvo.getParentVO().setAttributeValue("vdoname", getDelivOrgName());

    for (int num = 0; num < dmdvos.length; num++) {
      dhivos[num] = new DelivbillHItemVO();
      String[] sName = dmdvos[num].getAttributeNames();
      for (int i = 0; i < sName.length; i++) {
        if (sName[i].equals("plandate")) {
          Object oTemp = dhvo.getParentVO().getAttributeValue("senddate");
          if ((oTemp == null) || (oTemp.toString().trim().length() == 0)) {
            dhvo.getParentVO().setAttributeValue("senddate", dmdvos[num].getAttributeValue(sName[i]));
          }

        }
        else if (sName[i].equals("pksendtype")) {
          dhvo.getParentVO().setAttributeValue("pkdelivmode", dmdvos[num].getAttributeValue(sName[i]));
        }
        else if (sName[i].equals("pkoprdepart")) {
          dhvo.getParentVO().setAttributeValue("pkoprdepart", dmdvos[num].getAttributeValue(sName[i]));

          dhivos[num].setAttributeValue("pkoprdepart", dmdvos[num].getAttributeValue(sName[i]));
        }
        else if (sName[i].equals("voprdepartname")) {
          dhvo.getParentVO().setAttributeValue("voprdepartname", dmdvos[num].getAttributeValue(sName[i]));

          dhivos[num].setAttributeValue("voprdepartname", dmdvos[num].getAttributeValue(sName[i]));
        }
        else if (sName[i].equals("pkoperator")) {
          dhvo.getParentVO().setAttributeValue("pkoperator", dmdvos[num].getAttributeValue(sName[i]));

          dhivos[num].setAttributeValue("pkoperator", dmdvos[num].getAttributeValue(sName[i]));
        }
        else if (sName[i].equals("voperatorname")) {
          dhvo.getParentVO().setAttributeValue("voperatorname", dmdvos[num].getAttributeValue(sName[i]));

          dhivos[num].setAttributeValue("voperatorname", dmdvos[num].getAttributeValue(sName[i]));
        }
        else {
          if (sName[i].equals("dassistnum")) {
            continue;
          }
          if (sName[i].equals("dnum")) {
            Object value1 = dmdvos[num].getAttributeValue(sName[i]);
            Object value2 = dmdvos[num].getAttributeValue("dsendnum");
            if ((null == value1) || (value1.toString().trim().length() == 0)) {
              value1 = new UFDouble(0);
            }
            if ((null == value2) || (value2.toString().trim().length() == 0)) {
              value2 = new UFDouble(0);
            }
            UFDouble value = ((UFDouble)value1).sub((UFDouble)value2);
            dhivos[num].setAttributeValue("dinvnum", value);

            Object value1ast = dmdvos[num].getAttributeValue("dassistnum");
            if ((null == value1ast) || (value1ast.toString().trim().length() == 0)) {
              continue;
            }
            UFDouble valueAst = value.multiply((UFDouble)value1ast);
            if ((valueAst.doubleValue() != 0.0D) && (((UFDouble)value1).doubleValue() != 0.0D))
            {
              valueAst = valueAst.div((UFDouble)value1);
            }
            dhivos[num].setAttributeValue("dinvassist", valueAst);

            dhivos[num].setAttributeValue("dinvweight", getNewValueByHSL(dmdvos[num].getAttributeValue("dweight"), dmdvos[num].getAttributeValue(sName[i]), dhivos[num].getAttributeValue("dinvnum"), this.BD501));

            dhivos[num].setAttributeValue("dvolumn", getNewValueByHSL(dmdvos[num].getAttributeValue("dvolumn"), dmdvos[num].getAttributeValue(sName[i]), dhivos[num].getAttributeValue("dinvnum"), this.BD501));
          }
          else if (sName[i].equals("dassistnum")) {
            dhivos[num].setAttributeValue("dinvassist", dmdvos[num].getAttributeValue(sName[i]));
          }
          else {
            if (sName[i].equals("dweight"))
            {
              continue;
            }
            if (sName[i].equals("dvolumn"))
            {
              continue;
            }
            if (sName[i].equals("dbacknum")) {
              continue;
            }
            if (sName[i].equals("dsignnum")) {
              continue;
            }
            if (sName[i].equals("doutnum")) {
              continue;
            }
            if (sName[i].equals("bpresent")) {
              dhivos[num].setAttributeValue("blargess", dmdvos[num].getAttributeValue(sName[i]));
            }
            else if (sName[i].equals("requiredate")) {
              dhivos[num].setAttributeValue("requireday", dmdvos[num].getAttributeValue(sName[i]));
            }
            else if (sName[i].equals("pkcust")) {
              dhivos[num].setAttributeValue("pkcusmandoc", dmdvos[num].getAttributeValue(sName[i]));
            }
            else if (sName[i].equals("pkdeststoreorg")) {
              dhivos[num].setAttributeValue("pkdeststockorg", dmdvos[num].getAttributeValue(sName[i]));
            }
            else if (sName[i].equals("pksalecorp")) {
              dhivos[num].setAttributeValue("pksalecorp", dmdvos[num].getAttributeValue(sName[i]));
            }
            else if (sName[i].equals("pksaleorg")) {
              dhivos[num].setAttributeValue("pksalegrp", dmdvos[num].getAttributeValue(sName[i]));
            }
            else if (sName[i].equals("pksendstoreorg")) {
              dhivos[num].setAttributeValue("pksendstockorg", dmdvos[num].getAttributeValue(sName[i]));
            }
            else if (sName[i].equals("pksendstore")) {
              dhivos[num].setAttributeValue("pksendstock", dmdvos[num].getAttributeValue(sName[i]));
            }
            else if (sName[i].equals("vsrcbillnum")) {
              dhivos[num].setAttributeValue("vordercode", dmdvos[num].getAttributeValue(sName[i]));
            }
            else if (sName[i].equals("pkbillh")) {
              dhivos[num].setAttributeValue("pkorder", dmdvos[num].getAttributeValue(sName[i]));
            }
            else if (sName[i].equals("pkbillb")) {
              dhivos[num].setAttributeValue("pkorderrow", dmdvos[num].getAttributeValue(sName[i]));
            }
            else if (sName[i].equals("pk_delivdaypl")) {
              dhivos[num].setAttributeValue("pkdayplan", dmdvos[num].getAttributeValue(sName[i]));
            }
            else if (sName[i].equals("ts")) {
              dhivos[num].setAttributeValue("sourcebillts", dmdvos[num].getAttributeValue(sName[i]));
            }
            else if (sName[i].equals("vdelivdayplcode")) {
              dhivos[num].setAttributeValue("vdayplancode", dmdvos[num].getAttributeValue(sName[i]));
            }
            else if (sName[i].equals("vsendaddr")) {
              dhivos[num].setAttributeValue("vsendstoreorgaddre", dmdvos[num].getAttributeValue(sName[i]));
            }
            else if (sName[i].startsWith("vuserdef")) {
              if (sName[i].equals("vuserdef10")) {
                dhivos[num].setAttributeValue("vuserdef0", dmdvos[num].getAttributeValue(sName[i]));
              }
              else if (sName[i].equals("vuserdef11")) {
                dhivos[num].setAttributeValue("vuserdef1", dmdvos[num].getAttributeValue(sName[i]));
              }
              else if (sName[i].equals("vuserdef12")) {
                dhivos[num].setAttributeValue("vuserdef2", dmdvos[num].getAttributeValue(sName[i]));
              }
              else if (sName[i].equals("vuserdef13")) {
                dhivos[num].setAttributeValue("vuserdef3", dmdvos[num].getAttributeValue(sName[i]));
              }
              else if (sName[i].equals("vuserdef14")) {
                dhivos[num].setAttributeValue("vuserdef4", dmdvos[num].getAttributeValue(sName[i]));
              }
              else if (sName[i].equals("vuserdef15")) {
                dhivos[num].setAttributeValue("vuserdef5", dmdvos[num].getAttributeValue(sName[i]));
              }
              else
              {
                dhvo.getParentVO().setAttributeValue(sName[i], dmdvos[num].getAttributeValue(sName[i]));
              }

            }
            else
            {
              dhivos[num].setAttributeValue(sName[i], dmdvos[num].getAttributeValue(sName[i]));
            }
          }
        }
      }
      Object oNum = dhivos[num].getAttributeValue("dinvnum");
      Object oPrice = dhivos[num].getAttributeValue("dunitprice");
      if ((null == oNum) || (null == oPrice) || (oNum.toString().trim().length() == 0) || (oPrice.toString().trim().length() == 0))
      {
        dhivos[num].setAttributeValue("dmoney", null);
      }
      else {
        UFDouble ufdMoney = new UFDouble(oNum.toString()).multiply(new UFDouble(oPrice.toString()));

        dhivos[num].setAttributeValue("dmoney", ufdMoney);
      }
    }
    dhvo.setChildrenVO(dhivos);

    return dhvo;
  }

  private DelivbillHVO[] convertFromDailyPlanToDelivBatch(DMDataVO[] dmdvos)
  {
    DelivbillHItemVO[] dhivos = new DelivbillHItemVO[dmdvos.length];

    for (int num = 0; num < dmdvos.length; num++) {
      dhivos[num] = new DelivbillHItemVO();
      Object value1 = dmdvos[num].getAttributeValue("dnum");
      Object value2 = dmdvos[num].getAttributeValue("dsendnum");
      Object value3 = dmdvos[num].getAttributeValue("dreturnnum");
      if ((null == value1) || (value1.toString().trim().length() == 0)) {
        value1 = new UFDouble(0);
      }
      else {
        value1 = new UFDouble(value1.toString().trim());
      }
      if ((null == value2) || (value2.toString().trim().length() == 0)) {
        value2 = new UFDouble(0);
      }
      else {
        value2 = new UFDouble(value2.toString().trim());
      }
      if ((null == value3) || (value3.toString().trim().length() == 0)) {
        value3 = new UFDouble(0);
      }
      else {
        value3 = new UFDouble(value3.toString().trim());
      }
      UFDouble value = ((UFDouble)value1).sub((UFDouble)value2).add((UFDouble)value3);

      dhivos[num].setAttributeValue("dinvnum", value);

      dhivos[num].setAttributeValue("dinvweight", getNewValueByHSL(dmdvos[num].getAttributeValue("dweight"), dmdvos[num].getAttributeValue("dnum"), dhivos[num].getAttributeValue("dinvnum"), this.BD501));

      dhivos[num].setAttributeValue("dvolumn", getNewValueByHSL(dmdvos[num].getAttributeValue("dvolumn"), dmdvos[num].getAttributeValue("dnum"), dhivos[num].getAttributeValue("dinvnum"), this.BD501));

      dmdvos[num].setAttributeValue("dnum", dhivos[num].getAttributeValue("dinvnum"));

      dmdvos[num].setAttributeValue("dweight", dhivos[num].getAttributeValue("dinvweight"));

      dmdvos[num].setAttributeValue("dvolumn", dhivos[num].getAttributeValue("dvolumn"));

      Object oNum = dhivos[num].getAttributeValue("dinvnum");
      Object oPrice = dmdvos[num].getAttributeValue("dunitprice");
      if ((null == oNum) || (null == oPrice) || (oNum.toString().trim().length() == 0) || (oPrice.toString().trim().length() == 0))
      {
        dhivos[num].setAttributeValue("dmoney", null);
      }
      else {
        UFDouble ufdMoney = new UFDouble(oNum.toString()).multiply(new UFDouble(oPrice.toString()));

        dhivos[num].setAttributeValue("dmoney", ufdMoney);
      }
      dmdvos[num].setAttributeValue("dmoney", dhivos[num].getAttributeValue("dmoney"));

      Object value1ast = dmdvos[num].getAttributeValue("dassistnum");
      if ((null == value1ast) || (value1ast.toString().trim().length() == 0)) {
        continue;
      }
      UFDouble valueAst = value.multiply(new UFDouble(value1ast.toString()));
      if ((valueAst.doubleValue() != 0.0D) && (((UFDouble)value1).doubleValue() != 0.0D))
      {
        valueAst = valueAst.div((UFDouble)value1);
      }
      dhivos[num].setAttributeValue("dinvassist", valueAst);

      dmdvos[num].setAttributeValue("dassistnum", dhivos[num].getAttributeValue("dinvassist"));
    }

    DMVO[] dmvosForChange = null;
    if ((this.DM017 == null) || (this.DM017.equals("不合并"))) {
      dmvosForChange = new DMVO[dmdvos.length];
      for (int i = 0; i < dmdvos.length; i++) {
        dmvosForChange[i] = new DMVO();
        DMDataVO[] tempbody = { dmdvos[i] };

        dmvosForChange[i].setChildrenVO(tempbody);
      }
    }
    else {
      ArrayList alPu = new ArrayList();
      ArrayList alOther = new ArrayList();
      for (int i = 0; i < dmdvos.length; i++) {
        if ((dmdvos[i].getAttributeValue("vbilltype").equals("21")) && (dmdvos[i].getAttributeValue("borderreturn").equals("N")))
        {
          alPu.add(dmdvos[i]);
        }
        else {
          alOther.add(dmdvos[i]);
        }
      }
      DMDataVO[] dmdvosPU = (DMDataVO[])(DMDataVO[])alPu.toArray(new DMDataVO[0]);
      DMDataVO[] dmdvosOther = (DMDataVO[])(DMDataVO[])alOther.toArray(new DMDataVO[0]);

      DMVO vopu = new DMVO();

      vopu.setChildrenVO(dmdvosPU);
      String[] sPuGroupKeys = null;
      if ((this.DM017 != null) && (this.DM017.equals("发货/到货库存组织+发运方式+到货/发货地区+计划发货日期"))) {
        sPuGroupKeys = new String[] { "pksendtype", "pkdeststoreorg", "vuserdef0", "vuserdef1", "vuserdef2", "vuserdef3", "vuserdef4", "vuserdef5", "vuserdef6", "vuserdef7", "vuserdef8", "vuserdef9", "pksendarea", "snddate" };
      }
      else if ((this.DM017 != null) && (this.DM017.equals("发货/到货库存组织+发运方式+到货/发货地区+订单号"))) {
        sPuGroupKeys = new String[] { "pksendtype", "pkdeststoreorg", "vuserdef0", "vuserdef1", "vuserdef2", "vuserdef3", "vuserdef4", "vuserdef5", "vuserdef6", "vuserdef7", "vuserdef8", "vuserdef9", "pksendarea", "vsrcbillnum" };
      }
      else
      {
        sPuGroupKeys = new String[] { "pksendtype", "pkdeststoreorg", "vuserdef0", "vuserdef1", "vuserdef2", "vuserdef3", "vuserdef4", "vuserdef5", "vuserdef6", "vuserdef7", "vuserdef8", "vuserdef9", "pksendarea" };
      }

      DMVO[] dmvosPU = vopu.getGroupKeyVOs(sPuGroupKeys);

      DMVO voOther = new DMVO();
      voOther.setChildrenVO(dmdvosOther);
      String[] sOtherGroupKeys = null;
      if ((this.DM017 != null) && (this.DM017.equals("发货/到货库存组织+发运方式+到货/发货地区+计划发货日期"))) {
        sOtherGroupKeys = new String[] { "pksendtype", "pksendstoreorg", "vuserdef0", "vuserdef1", "vuserdef2", "vuserdef3", "vuserdef4", "vuserdef5", "vuserdef6", "vuserdef7", "vuserdef8", "vuserdef9", "pkarrivearea", "snddate" };
      }
      else if ((this.DM017 != null) && (this.DM017.equals("发货/到货库存组织+发运方式+到货/发货地区+订单号"))) {
        sOtherGroupKeys = new String[] { "pksendtype", "pksendstoreorg", "vuserdef0", "vuserdef1", "vuserdef2", "vuserdef3", "vuserdef4", "vuserdef5", "vuserdef6", "vuserdef7", "vuserdef8", "vuserdef9", "pkarrivearea", "vsrcbillnum" };
      }
      else
      {
        sOtherGroupKeys = new String[] { "pksendtype", "pksendstoreorg", "vuserdef0", "vuserdef1", "vuserdef2", "vuserdef3", "vuserdef4", "vuserdef5", "vuserdef6", "vuserdef7", "vuserdef8", "vuserdef9", "pkarrivearea" };
      }

      DMVO[] dmvosOther = voOther.getGroupKeyVOs(sOtherGroupKeys);
      dmvosForChange = new DMVO[dmvosPU.length + dmvosOther.length];
      for (int i = 0; i < dmvosPU.length; i++) {
        dmvosForChange[i] = new DMVO();
        dmvosForChange[i] = dmvosPU[i];
      }
      for (int i = 0; i < dmvosOther.length; i++) {
        dmvosForChange[(i + dmvosPU.length)] = new DMVO();
        dmvosForChange[(i + dmvosPU.length)] = dmvosOther[i];
      }
    }

    DelivbillHVO[] dhvos = null;
    try
    {
      dhvos = (DelivbillHVO[])(DelivbillHVO[])PfChangeBO_Client.pfChangeBillToBillArray(dmvosForChange, DMBillTypeConst.m_delivDelivDayPl, DMBillTypeConst.m_delivDelivBill);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    for (int i = 0; i < dhvos.length; i++) {
      dhvos[i].getParentVO().setAttributeValue("pkdelivorg", getDelivOrgPK());
      dhvos[i].getParentVO().setAttributeValue("vdoname", getDelivOrgName());
    }

    return dhvos;
  }

  protected DMDataVO[] convertVOtoOneLine(AggregatedValueObject oldVO)
  {
    String[] sHeadname = oldVO.getParentVO().getAttributeNames();
    String[] sBodyname = new String[0];
    if (oldVO.getChildrenVO().length != 0) {
      sBodyname = oldVO.getChildrenVO()[0].getAttributeNames();
    }
    DMDataVO[] ddvos = new DMDataVO[oldVO.getChildrenVO().length];
    for (int i = 0; i < ddvos.length; i++) {
      ddvos[i] = new DMDataVO();
      for (int j = 0; j < sHeadname.length; j++) {
        ddvos[i].setAttributeValue(sHeadname[j], oldVO.getParentVO().getAttributeValue(sHeadname[j]));
      }

      for (int j = 0; j < sBodyname.length; j++) {
        ddvos[i].setAttributeValue(sBodyname[j], oldVO.getChildrenVO()[i].getAttributeValue(sBodyname[j]));
      }

    }

    return ddvos;
  }

  protected void execPlanFormulaBodys(DMDataVO[] alBodys)
  {
    if ((alBodys == null) || (alBodys.length == 0)) {
      return;
    }
    ArrayList arylistItemField = getPlanItemFormula();

    ArryFormula arryFormula = new ArryFormula();

    ArrayList arylistresult = arryFormula.checkHtbkey(arylistItemField, getPlanItemKeyBody());

    String[] arysFormulaGet = (String[])(String[])arylistresult.get(1);

    String[] arysOldFormula = getPlanOldFormulasBody();

    String[] aryCombineFormula = ArryFormula.combineStringArray(arysOldFormula, arysFormulaGet);

    for (int i = 0; i < aryCombineFormula.length; i++) {
      SCMEnv.info(aryCombineFormula[i]);
    }
    if ((aryCombineFormula != null) && (aryCombineFormula.length > 0))
      getThdBillCardPanel().getBillModel().execFormulasWithVO(alBodys, aryCombineFormula);
  }

  protected DelivbillHVO generateVOFromDailyPlan(boolean bGenDeliv)
  {
    DMDataVO[] ddvos = new DMDataVO[getThdBillCardPanel().getRowCount()];
    DMVO dmvo = new DMVO(getThdBillCardPanel().getRowCount());
    getThdBillCardPanel().getBillValueVO(dmvo);
    ddvos = dmvo.getBodyVOs();
    boolean voNullflag = false;
    voNullflag = checkVO(dmvo);
    if (!voNullflag) {
      return null;
    }
    Hashtable htCheckVuserdef = new Hashtable();

    Vector v = new Vector();

    HashMap hmCheckBillType = new HashMap();

    String sSendType = "";
    int iSize = ddvos.length;
    for (int i = 0; i < iSize; i++) {
      if ((ddvos[i].getAttributeValue("bchoose") == null) || (!ddvos[i].getAttributeValue("bchoose").toString().trim().equals("Y"))) {
        continue;
      }
      if ((ddvos[i].getAttributeValue("pksendtype") == null) || (sSendType.trim().length() == 0) || (sSendType.equals(ddvos[i].getAttributeValue("pksendtype").toString().trim())))
      {
        if (null != ddvos[i].getAttributeValue("pksendtype")) {
          sSendType = ddvos[i].getAttributeValue("pksendtype").toString().trim();
        }

        String sVbilltype = ddvos[i].getAttributeValue("vbilltype").toString();

        if (!sVbilltype.equals("21"))
          sVbilltype = "30";
        if (!hmCheckBillType.containsKey(sVbilltype)) {
          hmCheckBillType.put(sVbilltype, "");
        }

        if (hmCheckBillType.size() > 1) {
          MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000126"), NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000131"));

          getThdBillCardPanel().setBillValueVO(dmvo);
          return null;
        }

        String key = "";
        for (int j = 0; j < 10; j++) {
          Object objvuserdef = ddvos[i].getAttributeValue("vuserdef" + j);
          if (null != objvuserdef)
            key = key + objvuserdef;
        }
        if ((!htCheckVuserdef.containsKey(key)) && (!key.equals(""))) {
          htCheckVuserdef.put(key, key);
        }

        if (htCheckVuserdef.size() > 1) {
          MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000126"), NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000132"));

          getThdBillCardPanel().setBillValueVO(dmvo);
          return null;
        }
        v.add(ddvos[i]);
      }
      else {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000126"), NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000133"));

        getThdBillCardPanel().setBillValueVO(dmvo);
        return null;
      }

    }

    if (v.size() == 0) {
      MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000134"), NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000135"));

      return null;
    }

    DMDataVO[] ddvosforgen = new DMDataVO[v.size()];
    v.copyInto(ddvosforgen);

    if (bGenDeliv) {
      ArrayList list1 = checkDplwhenInsert(ddvosforgen);

      if (list1 == null) {
        return null;
      }
      if (checkDlvbillwhenInsert(ddvosforgen)) {
        return null;
      }
      UFBoolean b = (UFBoolean)list1.get(0);
      if (b.booleanValue()) {
        DMDataVO[] tempVO = (DMDataVO[])(DMDataVO[])list1.get(1);
        if ((tempVO != null) && (tempVO.length > 0)) {
          Vector vTemp = new Vector();
          for (int i = 0; i < ddvosforgen.length; i++)
            vTemp.addElement(ddvosforgen[i]);
          for (int i = 0; i < tempVO.length; i++)
            vTemp.addElement(tempVO[i]);
          ddvosforgen = new DMDataVO[vTemp.size()];
          vTemp.copyInto(ddvosforgen);
        }
      }
    }

    DelivbillHVO dhvo = convertFromDailyPlanToDeliv(ddvosforgen);
    calculatePackNum(dhvo);
    return dhvo;
  }

  public boolean checkVO(DMVO dmvo) {
    if ((dmvo.getBodyVOs() == null) || (dmvo.getBodyVOs().length == 0)) {
      String sErrorMessage = NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000813");
      showErrorMessage(sErrorMessage);
      return false;
    }
    return true;
  }

  private void calculatePackNum(DelivbillHVO dhvo) {
    DelivbillHItemVO[] items = (DelivbillHItemVO[])(DelivbillHItemVO[])dhvo.getChildrenVO();
    int length = items.length;
    Set set = new HashSet();
    for (int i = 0; i < length; i++) {
      String cinventoryid = (String)items[i].getAttributeValue("pkinv");
      set.add(cinventoryid);
    }
    if (set.size() == 0) {
      return;
    }
    Iterator iterator = set.iterator();
    StringBuffer sql = new StringBuffer();
    sql.append(" select ic_wholepack.cinventoryid,   ");
    sql.append(" ic_packtype.pk_packsort, ic_wholepack.npacknum from ");
    sql.append(" ic_wholepack inner join ic_packtype on ");
    sql.append(" ic_wholepack.cpacktypeid = ic_packtype.cpacktypeid  ");
    sql.append(" where ic_wholepack. cinventoryid in  ( ");
    while (iterator.hasNext()) {
      sql.append("'");
      sql.append(iterator.next());
      sql.append("',");
    }
    sql.deleteCharAt(sql.length() - 1);
    sql.append(") ");
    DMDataVO[] result = null;
    try {
      result = DmHelper.queryStringBuffer(sql);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    int size = result.length;
    Map map = new HashMap();
    StringBuffer packsortsql = new StringBuffer();
    for (int i = 0; i < size; i++) {
      String cinventoryid = (String)result[i].getAttributeValue("cinventoryid");
      String pk_packsort = (String)result[i].getAttributeValue("pk_packsort");
      map.put(cinventoryid, result[i]);
      packsortsql.append("'");
      packsortsql.append(pk_packsort);
      packsortsql.append("',");
    }
    if (size > 0) {
      packsortsql.deleteCharAt(packsortsql.length() - 1);
    }

    length = items.length;
    for (int i = 0; i < length; i++) {
      String cinventoryid = (String)items[i].getAttributeValue("pkinv");
      DMDataVO row = (DMDataVO)map.get(cinventoryid);
      if (row == null) {
        continue;
      }
      UFDouble num = (UFDouble)items[i].getAttributeValue("dinvnum");
      Object obj = row.getAttributeValue("npacknum");
      obj = obj == null ? "0" : obj;
      UFDouble ratio = new UFDouble(obj.toString());
      UFDouble dpacknum = num.div(ratio);
      dpacknum = dpacknum.setScale(-this.BD501.intValue(), 4);

      String pk_packsort = (String)row.getAttributeValue("pk_packsort");
      items[i].setAttributeValue("pk_packsort", pk_packsort);
      items[i].setAttributeValue("dpacknum", dpacknum);
    }

    if (packsortsql.length() > 0)
      calculatePackVolumn(items, packsortsql.toString());
  }

  private void calculatePackVolumn(DelivbillHItemVO[] items, String packsortsql)
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" select pk_packsort,packvolumn from dm_packsort where ");
    sql.append(" pk_packsort in ( ");
    sql.append(packsortsql);
    sql.append(" ) ");
    DMDataVO[] result = null;
    try {
      result = DmHelper.queryStringBuffer(sql);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    int size = result.length;
    Map map = new HashMap();
    for (int i = 0; i < size; i++) {
      String pk_packsort = (String)result[i].getAttributeValue("pk_packsort");
      Object obj = result[i].getAttributeValue("packvolumn");
      obj = obj == null ? "0" : obj;
      UFDouble packvolumn = new UFDouble(obj.toString());
      map.put(pk_packsort, packvolumn);
    }
    int length = items.length;
    for (int i = 0; i < length; i++) {
      String pk_packsort = (String)items[i].getAttributeValue("pk_packsort");
      if (pk_packsort == null) {
        continue;
      }
      UFDouble packvolumn = (UFDouble)map.get(pk_packsort);
      if (packvolumn == null) {
        continue;
      }
      UFDouble dpacknum = (UFDouble)items[i].getAttributeValue("dpacknum");
      UFDouble dpackvolumn = dpacknum.multiply(packvolumn);
      dpackvolumn = dpackvolumn.setScale(-this.BD501.intValue(), 4);
      items[i].setAttributeValue("dpackvolumn", dpackvolumn);
    }
  }

  protected DelivbillHVO[] generateVOFromDailyPlanBatch()
  {
    DMVO dmvo = new DMVO(getThdBillCardPanel().getRowCount());
    getThdBillCardPanel().getBillValueVO(dmvo);
    DMDataVO[] ddvos = dmvo.getBodyVOs();

    boolean voNullflag = false;
    voNullflag = checkVO(dmvo);
    if (!voNullflag) {
      return null;
    }
    ArrayList alGenVOs = new ArrayList();
    for (int i = 0; i < ddvos.length; i++) {
      if ((ddvos[i].getAttributeValue("bchoose") == null) || (!ddvos[i].getAttributeValue("bchoose").toString().trim().equals("Y"))) {
        continue;
      }
      alGenVOs.add(ddvos[i]);
    }

    if (alGenVOs.size() == 0) {
      MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000134"), NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000136"));

      return null;
    }
    DMDataVO[] ddvosForGen = (DMDataVO[])(DMDataVO[])alGenVOs.toArray(new DMDataVO[0]);

    ArrayList list1 = checkDplwhenInsertBatch(ddvosForGen);

    if (list1 == null) {
      return null;
    }
    UFBoolean b = (UFBoolean)list1.get(0);
    if (b.booleanValue()) {
      DMDataVO[] tempVO = (DMDataVO[])(DMDataVO[])list1.get(1);
      if ((tempVO != null) && (tempVO.length > 0)) {
        Vector vTemp = new Vector();
        for (int i = 0; i < ddvosForGen.length; i++)
          vTemp.addElement(ddvosForGen[i]);
        for (int i = 0; i < tempVO.length; i++)
          vTemp.addElement(tempVO[i]);
        ddvosForGen = new DMDataVO[vTemp.size()];
        vTemp.copyInto(ddvosForGen);
      }
    }

    DelivbillHVO[] dhvos = convertFromDailyPlanToDelivBatch(ddvosForGen);

    return dhvos;
  }

  private DelivbillHVO generateVOFromDailyPlanBatch(boolean bGenDeliv)
  {
    DMVO dmvo = new DMVO(getThdBillCardPanel().getRowCount());
    DMDataVO[] ddvos = dmvo.getBodyVOs();
    ArrayList alGenVOs = new ArrayList();
    for (int i = 0; i < ddvos.length; i++) {
      if ((ddvos[i].getAttributeValue("bchoose") == null) || (!ddvos[i].getAttributeValue("bchoose").toString().trim().equals("Y"))) {
        continue;
      }
      alGenVOs.add(ddvos[i]);
    }

    DMDataVO[] ddvosGen = (DMDataVO[])(DMDataVO[])alGenVOs.toArray(new DMDataVO[0]);

    Hashtable htSendStoreOrg = new Hashtable();
    Hashtable htSendtype = new Hashtable();
    Hashtable htSendDate = new Hashtable();
    Hashtable htBillCode = new Hashtable();
    for (int i = 0; i < ddvosGen.length; i++);
    Hashtable htCheckVuserdef = new Hashtable();

    Vector v = new Vector();

    HashMap hmCheckBillType = new HashMap();

    String sSendType = "";
    int iSize = ddvos.length;
    for (int i = 0; i < iSize; i++) {
      if ((ddvos[i].getAttributeValue("bchoose") == null) || (!ddvos[i].getAttributeValue("bchoose").toString().trim().equals("Y"))) {
        continue;
      }
      if ((ddvos[i].getAttributeValue("pksendtype") == null) || (sSendType.trim().length() == 0) || (sSendType.equals(ddvos[i].getAttributeValue("pksendtype").toString().trim())))
      {
        if (null != ddvos[i].getAttributeValue("pksendtype")) {
          sSendType = ddvos[i].getAttributeValue("pksendtype").toString().trim();
        }

        if ((null != ddvos[i].getAttributeValue("iplanstatus")) && (new Integer(ddvos[i].getAttributeValue("iplanstatus").toString()).intValue() != DailyPlanStatus.Audit))
        {
          MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000126"), NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000130"));

          getThdBillCardPanel().setBillValueVO(dmvo);
          return null;
        }

        String sVbilltype = ddvos[i].getAttributeValue("vbilltype").toString();

        if (!sVbilltype.equals("21"))
          sVbilltype = "30";
        if (!hmCheckBillType.containsKey(sVbilltype)) {
          hmCheckBillType.put(sVbilltype, "");
        }

        if (hmCheckBillType.size() > 1) {
          MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000126"), NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000131"));

          getThdBillCardPanel().setBillValueVO(dmvo);
          return null;
        }

        String key = "";
        for (int j = 0; j < 10; j++) {
          Object objvuserdef = ddvos[i].getAttributeValue("vuserdef" + j);
          if (null != objvuserdef)
            key = key + objvuserdef;
        }
        if (!htCheckVuserdef.containsKey(key))
        {
          if (!"".equals(key)) {
            htCheckVuserdef.put(key, key);
          }
        }
        if (htCheckVuserdef.size() > 1) {
          MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000126"), NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000132"));

          getThdBillCardPanel().setBillValueVO(dmvo);
          return null;
        }
        v.add(ddvos[i]);
      }
      else {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000126"), NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000133"));

        getThdBillCardPanel().setBillValueVO(dmvo);
        return null;
      }

    }

    if (v.size() == 0) {
      MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000134"), NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000135"));

      return null;
    }

    DMDataVO[] ddvosforgen = new DMDataVO[v.size()];
    v.copyInto(ddvosforgen);

    if (bGenDeliv) {
      ArrayList list1 = checkDplwhenInsert(ddvosforgen);

      if (list1 == null) {
        return null;
      }
      if (checkDlvbillwhenInsert(ddvosforgen)) {
        return null;
      }
      UFBoolean b = (UFBoolean)list1.get(0);
      if (b.booleanValue()) {
        DMDataVO[] tempVO = (DMDataVO[])(DMDataVO[])list1.get(1);
        if ((tempVO != null) && (tempVO.length > 0)) {
          Vector vTemp = new Vector();
          for (int i = 0; i < ddvosforgen.length; i++)
            vTemp.addElement(ddvosforgen[i]);
          for (int i = 0; i < tempVO.length; i++)
            vTemp.addElement(tempVO[i]);
          ddvosforgen = new DMDataVO[vTemp.size()];
          vTemp.copyInto(ddvosforgen);
        }
      }
    }

    DelivbillHVO dhvo = convertFromDailyPlanToDeliv(ddvosforgen);

    return dhvo;
  }

  protected String genTempBodyPK(String OldPK)
  {
    return genTempPK(OldPK, 8);
  }

  protected String genTempHeadPK(String OldPK)
  {
    return genTempPK(OldPK, 10);
  }

  /** @deprecated */
  public DelivbillHHeaderVO[] getAllHeads(DelivbillHVO[] hvos)
  {
    DelivbillHHeaderVO[] allheads = null;
    DelivbillHHeaderVO onebillhead = null;
    ArrayList allheadsArray = new ArrayList();
    for (int i = 0; (hvos != null) && (i < hvos.length); i++) {
      if (hvos[i] != null)
        onebillhead = (DelivbillHHeaderVO)hvos[i].getParentVO();
      if (onebillhead != null) {
        allheadsArray.add(onebillhead);
      }
    }
    allheads = new DelivbillHHeaderVO[allheadsArray.size()];
    allheadsArray.toArray(allheads);
    return allheads;
  }

  /** @deprecated */
  public DelivbillHItemVO[] getAllItems(DelivbillHVO[] hvos)
  {
    DelivbillHItemVO[] allitems = null;
    DelivbillHItemVO[] onebillitems = null;
    ArrayList allitemsArray = new ArrayList();
    for (int i = 0; i < hvos.length; i++) {
      if (hvos[i] != null)
        onebillitems = (DelivbillHItemVO[])(DelivbillHItemVO[])hvos[i].getChildrenVO();
      if ((onebillitems != null) && (onebillitems.length > 0)) {
        for (int j = 0; j < onebillitems.length; j++) {
          allitemsArray.add(onebillitems[j]);
        }
      }
    }
    allitems = new DelivbillHItemVO[allitemsArray.size()];
    allitemsArray.toArray(allitems);
    return allitems;
  }

  public BillListPanel getBillListPanel()
  {
    return getChooseDeliveryBillDLG().getBillListPanel();
  }

  public ChooseDeliveryBillDLG getChooseDeliveryBillDLG()
  {
    if (null == this.m_ChooseDeliveryDLG) {
      DMDataVO ddvo = new DMDataVO();
      ddvo.setAttributeValue("pkcorp", getCorpID());
      ddvo.setAttributeValue("vcorpname", getCorpName());
      ddvo.setAttributeValue("pkdelivorg", getDelivOrgPK());
      ddvo.setAttributeValue("userid", getUserID());
      ddvo.setAttributeValue("username", getUserName());
      ddvo.setAttributeValue("corpid", getCorpID());
      ddvo.setAttributeValue("corpname", getCorpName());
      ddvo.setAttributeValue("BD501", this.BD501);
      ddvo.setAttributeValue("BD502", this.BD502);
      ddvo.setAttributeValue("BD503", this.BD503);
      ddvo.setAttributeValue("BD505", this.BD505);
      this.m_ChooseDeliveryDLG = new ChooseDeliveryBillDLG(this, NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000114"), ddvo);
    }

    return this.m_ChooseDeliveryDLG;
  }

  protected nc.ui.dm.dm104.ClientUI getDelivBillClientUI()
  {
    if (null == this.m_DelivBillClientUI) {
      this.m_DelivBillClientUI = new nc.ui.dm.dm104.ClientUI(getDelivOrgPK(), getDelivOrgCode(), getDelivOrgName(), getAgentCorpIDsofDelivOrg());

      this.m_DelivBillClientUI.setUserID(getUserID());
      this.m_DelivBillClientUI.setUserName(getUserName());
      this.m_DelivBillClientUI.setCorpID(getCorpID());
      this.m_DelivBillClientUI.setCorpName(getCorpName());
      this.m_DelivBillClientUI.getBillListPanel().addEditListener(this);
      if (null != this.m_DelivBillClientUI.getBillListPanel().getBillListData()) {
        this.m_DelivBillClientUI.getBillListPanel().getHeadTable().addMouseListener(this);
      }
    }
    return this.m_DelivBillClientUI;
  }

  protected DMQueryConditionDlg getDelivDailyConditionDlg()
  {
    if (this.queryDelivDailyConditionDlg == null)
    {
      this.queryDelivDailyConditionDlg = new QueryConditionDlg(this);
      new nc.ui.dm.dm102.ClientUI().setDelivDailyConditionDlg(this.queryDelivDailyConditionDlg, "7F");
    }

    return this.queryDelivDailyConditionDlg;
  }

  public ArrayList getItemFormulaBody()
  {
    return DelivBillFormula.getItemFormulaBody();
  }

  public ArrayList getItemFormulaHead()
  {
    return DelivBillFormula.getItemFormulaHead();
  }

  protected ArrayList getOldDelivVOs()
  {
    return this.m_alOldDelivVOs;
  }

  protected DMVO getOldPlanVO()
  {
    return this.m_dmOldPlanVO;
  }

  public ArrayList getPlanItemFormula()
  {
    return PlanFormula.getPlanFormula();
  }

  protected Hashtable getPlanItemKeyBody()
  {
    if ((this.m_htbPlanBodyItemkey == null) || (this.m_htbPlanBodyItemkey.size() == 0)) {
      this.m_htbPlanBodyItemkey = new Hashtable();
      BillItem[] billItems = getThdBillCardPanel().getBillModel().getBodyItems();
      if ((billItems != null) && (billItems.length > 0)) {
        int iLen = billItems.length;
        BillItem billItem = null;
        String sKey = null;
        for (int i = 0; i < iLen; i++) {
          sKey = billItems[i].getKey();
          if ((sKey != null) && (!this.m_htbPlanBodyItemkey.containsKey(sKey))) {
            this.m_htbPlanBodyItemkey.put(sKey, sKey);
          }
        }
      }
    }
    return this.m_htbPlanBodyItemkey;
  }

  protected String[] getPlanOldFormulasBody()
  {
    if (this.m_aryPlanFormulasBody == null) {
      BillItem[] billItemBody = getThdBillCardPanel().getBillModel().getBodyItems();

      this.m_aryPlanFormulasBody = getFormulas(billItemBody);
    }
    return this.m_aryPlanFormulasBody;
  }

  protected String getTempBodyPK()
  {
    return this.m_sTempBodyPK;
  }

  protected String getTempHeadPK()
  {
    return this.m_sTempHeadPK;
  }

  public String getTitle()
  {
    String s = StringTools.getSimilarString(getBillCardPanel().getTitle());
    if (s != null) {
      return s;
    }
    return NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000115");
  }

  protected void initFixSubMenuButton()
  {
    super.initFixSubMenuButton();
    this.boQueryDelivDaily = new ButtonObject(NCLangRes.getInstance().getStrByID("40140406", "UPT40140406-000024"), NCLangRes.getInstance().getStrByID("40140406", "UPT40140406-000024"), 1, "日计划查询");

    this.boGenDeliv = new ButtonObject(NCLangRes.getInstance().getStrByID("40140406", "UPT40140406-000005"), NCLangRes.getInstance().getStrByID("40140406", "UPT40140406-000005"), 1, "生成新发运单");

    this.boInsertDeliv = new ButtonObject(NCLangRes.getInstance().getStrByID("40140406", "UPT40140406-000006"), NCLangRes.getInstance().getStrByID("40140406", "UPT40140406-000006"), 1, "插入到已有发运单");

    this.boGenTaskBill = new ButtonObject(NCLangRes.getInstance().getStrByID("40140406", "UPT40140406-000015"), NCLangRes.getInstance().getStrByID("40140406", "UPT40140406-000015"), 1, "生成任务单");

    this.boTransConfirm = new ButtonObject(NCLangRes.getInstance().getStrByID("40140406", "UPT40140406-000011"), NCLangRes.getInstance().getStrByID("40140406", "UPT40140406-000011"), 1, "承运商确认");

    this.boCancelConfirm = new ButtonObject(NCLangRes.getInstance().getStrByID("40140406", "UPT40140406-000020"), NCLangRes.getInstance().getStrByID("40140406", "UPT40140406-000020"), 1, "承运商取消确认");

    this.boCalculateFee = new ButtonObject(NCLangRes.getInstance().getStrByID("40140406", "UPT40140406-000012"), NCLangRes.getInstance().getStrByID("40140406", "UPT40140406-000012"), 1, "计算运费");

    this.boOutBill = new ButtonObject(NCLangRes.getInstance().getStrByID("40140406", "UPT40140406-000013"), NCLangRes.getInstance().getStrByID("40140406", "UPT40140406-000013"), 1, "出库");

    this.boPackageList = new ButtonObject(NCLangRes.getInstance().getStrByID("40140406", "UPT40140406-000028"), NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000116"), 1, "包装");

    this.boSelectAll = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000041"), NCLangRes.getInstance().getStrByID("common", "UC001-0000041"), 1, "全选");

    this.boUnSelectAll = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000042"), NCLangRes.getInstance().getStrByID("common", "UC001-0000042"), 1, "全消");

    this.boOpenBill = new ButtonObject(NCLangRes.getInstance().getStrByID("40140406", "UPT40140406-000029"), NCLangRes.getInstance().getStrByID("40140406", "UPT40140406-000029"), 1, "打开");

    this.boTestCalculate = new ButtonObject(NCLangRes.getInstance().getStrByID("40140406", "UPT40140406-000010"), NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000117"), 1, "试算");

    this.boGenDelivBatch = new ButtonObject(NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000118"), NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000119"), 1, "批生成");

    this.boNext = getDelivBillClientUI().getNextButton();
    this.boPre = getDelivBillClientUI().getPreButton();
    this.boFirst = getDelivBillClientUI().getFristButton();
    this.boLast = getDelivBillClientUI().getLastButton();
    this.boEdit = getDelivBillClientUI().getEditButton();
    this.boSave = getDelivBillClientUI().getSaveButton();
    this.boCancel = getDelivBillClientUI().getCancelButton();
    this.boAudit = getDelivBillClientUI().getAuditButton();
    this.boCancelAudit = getDelivBillClientUI().getCancelAuditButton();
    this.boEnd = getDelivBillClientUI().getEndButton();
    this.boOpenBill = getDelivBillClientUI().getOpenBillButton();
    this.boQuery = getDelivBillClientUI().getQueryButton();

    this.boQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000120"), 1, "查询");

    this.boPrint = getDelivBillClientUI().getPrintButton();
    this.boDel = getDelivBillClientUI().getDelButton();
    this.boDelLine = getDelivBillClientUI().getDelLineButton();
    this.boGenTaskBill = getDelivBillClientUI().getGenTaskBillButton();
    this.boSort = getDelivBillClientUI().getSortButton();
    this.boTransConfirm = getDelivBillClientUI().getTransConfirmButton();
    this.boCancelConfirm = getDelivBillClientUI().getCancelConfirmButton();
    this.boCalculateFee = getDelivBillClientUI().getCalculateFeeButton();
    this.boOutBill = getDelivBillClientUI().getOutBillButton();
    this.boBill = getDelivBillClientUI().getBillButton();
    this.boDocument = getDelivBillClientUI().getDocumentButton();
    this.boOrderQuery = getDelivBillClientUI().getJoinQueryButton();
    this.boPreview = getDelivBillClientUI().getPreview();
    this.boPrintPreview = getDelivBillClientUI().getPrintPreview();

    this.boBill.removeAllChildren();

    this.boRowCloseOut = new ButtonObject(NCLangRes.getInstance().getStrByID("40140408", "UPT40140408-000091"), NCLangRes.getInstance().getStrByID("40140408", "UPT40140408-000091"), 1, "行出库关闭");

    this.boRowOpenOut = new ButtonObject(NCLangRes.getInstance().getStrByID("40140408", "UPT40140408-000092"), NCLangRes.getInstance().getStrByID("40140408", "UPT40140408-000092"), 1, "行出库打开");

    this.boAction.removeAllChildren();
    this.boAction.addChildButton(this.boAudit);
    this.boAction.addChildButton(this.boCancelAudit);
    this.boAction.addChildButton(this.boEnd);
    this.boAction.addChildButton(this.boOpenBill);

    if (getDelivSequence() == 0) {
      this.boAction.addChildButton(this.boRowCloseOut);
      this.boAction.addChildButton(this.boRowOpenOut);
    }

    this.boAssistant.removeAllChildren();
    this.boAssistant.addChildButton(this.boTestCalculate);
    this.boAssistant.addChildButton(this.boCalculateFee);
    this.boAssistant.addChildButton(this.boGenTaskBill);
    this.boAssistant.addChildButton(this.boTransConfirm);
    this.boAssistant.addChildButton(this.boCancelConfirm);
    this.boAssistant.addChildButton(this.boOrderQuery);
    this.boAssistant.addChildButton(this.boDocument);
    this.boAssistant.addChildButton(this.boPrintPreview);
    this.boAssistant.addChildButton(this.boPrint);

    this.boAssistantList.removeAllChildren();
    this.boAssistantList.addChildButton(this.boOrderQuery);
    this.boAssistantList.addChildButton(this.boDocument);
    this.boAssistantList.addChildButton(this.boPrintPreview);
    this.boAssistantList.addChildButton(this.boPrint);

    this.boMaintain.removeAllChildren();
    this.boMaintain.addChildButton(this.boEdit);
    this.boMaintain.addChildButton(this.boSave);
    this.boMaintain.addChildButton(this.boCancel);
    this.boMaintain.addChildButton(this.boDel);

    this.boMaintainList.removeAllChildren();
    this.boMaintainList.addChildButton(this.boEdit);
    this.boMaintainList.addChildButton(this.boDel);

    this.boBrowse.removeAllChildren();
    this.boBrowse.addChildButton(this.boQuery);

    this.boBrowse.addChildButton(this.boFind);

    this.boBrowseList.removeAllChildren();
    this.boBrowseList.addChildButton(this.boQuery);

    this.boBrowseList.addChildButton(this.boFind);

    this.aryListButtonGroup = new ButtonObject[] { this.boQueryDelivDaily, this.boMaintainList, this.boBrowseList, this.boSwith, this.boAssistantList };

    if (getDelivSequence() == 0)
    {
      this.aryButtonGroup = new ButtonObject[] { this.boQueryDelivDaily, this.boMaintain, this.boDelLine, this.boBrowse, this.boFirst, this.boPre, this.boNext, this.boLast, this.boPackageList, this.boSwith, this.boAction, this.boOutBill, this.boAssistant };
    }
    else if (getDelivSequence() == 1)
    {
      this.aryButtonGroup = new ButtonObject[] { this.boQueryDelivDaily, this.boMaintain, this.boDelLine, this.boBrowse, this.boFirst, this.boPre, this.boNext, this.boLast, this.boPackageList, this.boSwith, this.boAction, this.boAssistant };
    }
    else if (getDelivSequence() == 2)
    {
      this.aryButtonGroup = new ButtonObject[] { this.boQueryDelivDaily, this.boMaintain, this.boDelLine, this.boBrowse, this.boFirst, this.boPre, this.boNext, this.boLast, this.boPackageList, this.boSwith, this.boAction, this.boAssistant };
    }

    this.arySourceButtonGroup = new ButtonObject[] { this.boQueryDelivDaily, this.boSelectAll, this.boUnSelectAll, this.boSort, this.boGenDeliv, this.boGenDelivBatch, this.boInsertDeliv, this.boQuery, this.boSwith };
  }

  public void initialize()
  {
  }

  public void initializeNew()
  {
    setBillTypeCode(DMBillTypeConst.m_delivDelivBillInDelivPlan);
    setSecBillTypeCode(DMBillTypeConst.m_delivDelivBill);
    setThdBillTypeCode(DMBillTypeConst.m_delivDelivDayPl);
    setNodeCode("40140406");
    setVuserdefCode(DMBillTypeConst.m_delivDelivDayPl);
    this.strTitle1 = NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000121");

    this.strTitle2 = NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000122");

    this.strTitle3 = NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000123");

    setANumItemKeys(new String[] { "dassistnum", "dinvassist" });

    setNumItemKeys(new String[] { "dweight", "dvolumn", "ndelivernum", "nonwaynum", "dnum", "dinvnum", "dinvweight", "dvolumn", "dsendnum", "doutnum", "dsignnum", "dbacknum", "dcancelnum" });

    setPriceItemKeys(new String[] { "dunitprice" });
    setMoneyItemKey(new String[] { "dmoney" });

    setSourceANumItemKeys(new String[] { "dassistnum" });
    setSourceNumItemKeys(new String[] { "dnum", "dweight", "dvolumn", "ndelivernum", "dsendnum", "doutnum", "dsignnum", "dbacknum", "nonwaynum" });

    setSourcePriceItemKeys(new String[] { "dunitprice", "dmoney" });

    super.initialize();
  }

  protected void initBodyComboBox()
  {
    initComboBoxOnCard();
    initComboBoxOnList();
  }

  private void initComboBoxOnCard()
  {
    UIComboBox cbCardItem = (UIComboBox)getDelivBillClientUI().getBillCardPanel().getHeadItem("isendtype").getComponent();
    cbCardItem.setTranslate(true);
    if (cbCardItem.getItemCount() > 0)
      cbCardItem.removeAllItems();
    getDelivBillClientUI().getBillCardPanel().getHeadItem("isendtype").setWithIndex(true);

    for (int i = 0; i < FreightType.nameoftype.length; i++) {
      cbCardItem.addItem(FreightType.nameoftype[i]);
    }
    cbCardItem.setSelectedItem(FreightType.sAr);
  }

  private void initComboBoxOnList()
  {
    UIComboBox cbListItem = (UIComboBox)getDelivBillClientUI().getBillListPanel().getHeadItem("isendtype").getComponent();
    cbListItem.setTranslate(true);
    if (cbListItem.getItemCount() > 0)
      cbListItem.removeAllItems();
    getDelivBillClientUI().getBillListPanel().getHeadItem("isendtype").setWithIndex(true);

    for (int i = 0; i < FreightType.nameoftype.length; i++) {
      cbListItem.addItem(FreightType.nameoftype[i]);
    }
    cbListItem.setSelectedItem("");
  }

  protected void initThdBodyComboBox()
  {
    initBodyComboBox(getThdBillCardPanel());
  }

  public void initThdState()
  {
    super.initThdState();
    getThdBillCardPanel().getBodyItem("snddate").setEnabled(false);
    getThdBillCardPanel().getBodyItem("vinvcode").setEnabled(false);
    getThdBillCardPanel().getBodyItem("vspec").setEnabled(false);
    getThdBillCardPanel().getBodyItem("vtype").setEnabled(false);
    getThdBillCardPanel().getBodyItem("vassistmeaname").setEnabled(false);
    getThdBillCardPanel().getBodyItem("vbatchcode").setEnabled(false);
    getThdBillCardPanel().getBodyItem("vfree0").setEnabled(false);
    getThdBillCardPanel().getBodyItem("dassistnum").setEnabled(false);
    getThdBillCardPanel().getBodyItem("dnum").setEnabled(false);
    getThdBillCardPanel().getBodyItem("dweight").setEnabled(false);
    getThdBillCardPanel().getBodyItem("dvolumn").setEnabled(false);
    getThdBillCardPanel().getBodyItem("dunitprice").setEnabled(false);
    getThdBillCardPanel().getBodyItem("dmoney").setEnabled(false);
    getThdBillCardPanel().getBodyItem("vsendtypename").setEnabled(false);
  }

  protected void keepOldVO()
  {
    ArrayList al = new ArrayList();
    for (int i = 0; i < getAllVOs().size(); i++) {
      al.add(getAllVOs().get(i));
    }
    setOldDelivVOs(al);

    getThdBillCardPanel().updateValue();
    DMVO dvo = (DMVO)getThdBillCardPanel().getBillValueVO(DMVO.class.getName(), DMDataVO.class.getName(), DMDataVO.class.getName());

    setOldPlanVO(dvo);

    getThdBillCardPanel().setBillValueVO(dvo);
    getThdBillCardPanel().updateValue();

    dvo = (DMVO)getSecBillCardPanel().getBillValueVO(DMVO.class.getName(), DMDataVO.class.getName(), DMDataVO.class.getName());

    getSecBillCardPanel().setBillValueVO(dvo);
    getSecBillCardPanel().updateValue();

    dvo = (DMVO)getBillCardPanel().getBillValueVO(DMVO.class.getName(), DMDataVO.class.getName(), DMDataVO.class.getName());

    getBillCardPanel().setBillValueVO(dvo);
    getBillCardPanel().updateValue();
  }

  protected DelivbillHVO[] loadBillData(String sMainWhere, String sSubWhere)
  {
    try
    {
      DelivbillHVO[] dmdvos = DelivbillHBO_Client.findDelivBills(getDelivOrgPK(), sMainWhere, sSubWhere);

      setBillData(dmdvos);
      return dmdvos;
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void loadListTemplet()
  {
  }

  public void loadPanel()
  {
    setButtons(getBillButtons());

    if (null == this.ivjThdBillCardPanel) {
      loadThdCardTemplet();
      getThdBillCardPanel().setEnabled(true);
      for (int i = 0; i < getThdBillCardPanel().getBodyItems().length; i++) {
        getThdBillCardPanel().getBodyItems()[i].setEnabled(false);
        getThdBillCardPanel().getBodyItems()[i].setEdit(false);
      }
      getThdBillCardPanel().getBodyItem("bchoose").setEnabled(true);
      getThdBillCardPanel().getBodyItem("bchoose").setEdit(true);
      getThdBillCardPanel().addCheckBoxChangedListener(this);
    }

    switchInterface();
  }

  protected DMDataVO[] loadThdBillData(String[] saPk_delivdaypl, ConditionVO[] voCons, boolean bIsAppendQuery)
  {
    try
    {
      DMDataVO[] dmdvos = queryDelivDailyPlan(saPk_delivdaypl, voCons);

      setThdBillData(dmdvos, bIsAppendQuery);
      return dmdvos;
    }
    catch (Exception e) {
      showErrorMessage(e.getMessage());
      e.printStackTrace();
    }
    return null;
  }

  public void loadThdCardTemplet()
  {
    super.loadThdCardTemplet();

    getThdBillCardPanel().setAutoSetRowSelectStatus(true);
    getThdBillCardPanel().setRowSelectStatusKey("bchoose");

    for (int i = 0; i < getThdBillCardPanel().getBodyItems().length; i++) {
      getThdBillCardPanel().getBodyItems()[i].setEnabled(false);
    }
    getThdBillCardPanel().getBodyItem("bchoose").setEdit(true);
    getThdBillCardPanel().getBodyItem("bchoose").setEnabled(true);
  }

  public void mouseClicked(MouseEvent e)
  {
    if ((e.getSource() == getDelivBillClientUI().getBillListPanel().getHeadTable()) && (e.getClickCount() == 2))
    {
      getDelivBillClientUI().mouseClicked(e);

      onSwitchForm();
    }
  }

  public void onAudit()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("40140408", "UPP40140408-000032"));

    getDelivBillClientUI().onAudit();
    if (getDelivBillClientUI().isOperationFinish()) {
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH010"));

      switchButtonStatus(DMBillStatus.CardView);
    }
  }

  public void onButtonClicked(ButtonObject bo)
  {
    getBillCardPanel().stopEditing();

    if (getDelivBillClientUI().isListHeaderMultiSelected(bo, new ButtonObject[] { this.boQueryDelivDaily, this.boSwitchList, this.boSwitchSource }))
    {
      return;
    }
    if (bo == this.boQueryDelivDaily) {
      onQueryDelivDaily();
    } else if (bo == this.boFirst) {
      onFirst();
    } else if (bo == this.boGenDeliv) {
      onGenDeliv();
    } else if (bo == this.boGenDelivBatch) {
      onGenDelivBatch();
    } else if (bo == this.boGenDelivBatch) {
      onGenDelivBatch();
    } else if (bo == this.boInsertDeliv) {
      onInsertDeliv();
    } else if (bo == this.boGenTaskBill) {
      onGenTaskBill();
    } else if (bo == this.boAudit) {
      onAudit();
    } else if (bo == this.boCancelAudit) {
      onCancelAudit();
    } else if (bo == this.boEnd) {
      onEnd();
    }
    else if (bo == this.boTransConfirm) {
      onTransConfirm();
    } else if (bo == this.boCancelConfirm) {
      onCancelConfirm();
    } else if (bo == this.boCancel) {
      onCancel();
    } else if (bo == this.boCalculateFee) {
      onCalculateFee();
    } else if (bo == this.boOutBill) {
      onOutBill();
    } else if (bo == this.boSort) {
      onSort();
    } else if (bo == this.boSelectAll) {
      onSelectAll();
    } else if (bo == this.boUnSelectAll) {
      onUnSelectAll();
    } else if (bo == this.boOrderQuery) {
      getDelivBillClientUI().onOrderQuery();
    } else if (bo == this.boPackageList) {
      onPackageList();
    } else if (bo == this.boOpenBill) {
      onOpen();
    } else if (bo == this.boTestCalculate) {
      onTestCalculateFee();
    } else if (bo == this.boRowCloseOut) {
      onRowCloseOut();
    } else if (bo == this.boRowOpenOut) {
      onRowOpenOut();
    } else if (bo == this.boRefresh) {
      onRefresh();
    }
    else {
      boolean extraButtonFired = false;
      ButtonObject[] buttons = getExtendBtns();
      if ((buttons != null) && (buttons.length > 0)) {
        int length = buttons.length;
        for (int i = 0; i < length; i++) {
          if (buttons[i] == bo) {
            extraButtonFired = true;
            break;
          }
        }
      }

      if (extraButtonFired) {
        onExtendBtnsClick(bo);
      }
      else
        super.onButtonClicked(bo);
    }
  }

  public void onCalculateFee()
  {
    getDelivBillClientUI().onCalculateFee();
    if (getDelivBillClientUI().isOperationFinish())
      switchButtonStatus(DMBillStatus.ListView);
  }

  public void onCancel()
  {
    getDelivBillClientUI().onCancel();

    if (getDelivBillClientUI().isOperationFinish()) {
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH008"));

      String state = getDelivBillClientUI().getShowState();
      if (state.equals(DMBillStatus.List)) {
        switchButtonStatus(DMBillStatus.ListView);
        onSwitchList();
      }
      else if (state.equals(DMBillStatus.Card)) {
        switchButtonStatus(DMBillStatus.CardView);
        onSwitchForm();
      }
    }

    if (null != getOldPlanVO()) {
      getThdBillCardPanel().setBillValueVO(getOldPlanVO());
    }
    if ((getDelivBillClientUI().getEditFlag() == 0) || (this.m_bInsertDlvbill == true) || (getDelivBillClientUI().getEditFlag() == 2))
    {
      onSwitchSource();
    }
    this.m_bInsertDlvbill = false;
  }

  public void onCancelAudit()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH049"));

    getDelivBillClientUI().onCancelAudit();

    if (getDelivBillClientUI().isOperationFinish()) {
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH011"));

      switchButtonStatus(DMBillStatus.CardView);
    }
  }

  public void onCancelConfirm()
  {
    getDelivBillClientUI().onTransCancelConfirm();
    if (getDelivBillClientUI().isOperationFinish())
      switchButtonStatus(DMBillStatus.CardView);
  }

  public void onDel()
  {
    Vector vRow = new Vector();
    DMVO dmvo = (DMVO)getDelivBillClientUI().getBillCardPanel().getBillValueChangeVO(DMVO.class.getName(), DMDataVO.class.getName(), DMDataVO.class.getName());

    if (null != dmvo.getBodyVOs()) {
      for (int i = 0; i < dmvo.getBodyVOs().length; i++)
      {
        vRow.add(dmvo.getBodyVOs()[i].getAttributeValue("pkdayplan"));
      }

    }

    getDelivBillClientUI().onDel();

    if (getDelivBillClientUI().isOperationFinish()) {
      switchButtonStatus(DMBillStatus.ListView);

      if (vRow.size() > 0)
      {
        loadThdBillData((String[])(String[])vRow.toArray(new String[vRow.size()]), null, true);
      }
      onSwitchList();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH006"));
    }
  }

  public void onDelLine()
  {
    getDelivBillClientUI().onDelLine();
  }

  protected void onDelLine(BillCardPanel bcp1, BillCardPanel bcp2)
  {
    if (bcp1.getBillTable().getSelectedRow() <= -1) {
      return;
    }
    int row = bcp1.getBillTable().getSelectedRow();
    String sBodyPK = bcp1.getBodyValueAt(row, "pk_delivbill_b").toString();

    Vector v = new Vector();
    for (int i = 0; i < getAllVOs().size(); i++) {
      DelivbillHVO dhvo = (DelivbillHVO)getAllVOs().get(i);
      for (int j = 0; j < dhvo.getChildrenVO().length; j++) {
        if (!dhvo.getChildrenVO()[j].getAttributeValue("pk_delivbill_b").toString().equals(sBodyPK))
          continue;
        if (dhvo.getChildrenVO()[j].getStatus() == 2)
        {
          dhvo.deleteBodyVO(j);
          getAllVOs().set(i, dhvo);
          break;
        }

        dhvo.getChildrenVO()[j].setStatus(3);
        getAllVOs().set(i, dhvo);

        break;
      }

    }

    bcp1.delLine();

    for (int i = 0; i < bcp2.getRowCount(); i++) {
      if (!bcp2.getBodyValueAt(i, "pk_delivbill_b").toString().equals(sBodyPK))
        continue;
      bcp2.getBillTable().setRowSelectionInterval(i, i);
      bcp2.delLine();
      break;
    }
  }

  public void onDocument()
  {
    getDelivBillClientUI().onDocument();
  }

  public void onEdit()
  {
    getDelivBillClientUI().onEdit();
    if (getDelivBillClientUI().isOperationFinish()) {
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH027"));

      switchButtonStatus(DMBillStatus.CardEdit);
      onSwitchForm();
    }
  }

  public void onEnd()
  {
    getDelivBillClientUI().onEnd();
    if (getDelivBillClientUI().isOperationFinish())
      switchButtonStatus(DMBillStatus.CardView);
  }

  public void onFind()
  {
    OrientDialog dlgOrient = new OrientDialog(this, getDelivBillClientUI().getBillListPanel().getBodyBillModel(), getDelivBillClientUI().getBillListPanel().getBillListData().getHeadItems(), getDelivBillClientUI().getBillListPanel().getHeadTable());

    dlgOrient.showModal();
  }

  public void onFirst()
  {
    getDelivBillClientUI().onFirst();
    if (getDelivBillClientUI().isOperationFinish()) {
      if (getDelivBillClientUI().getEditFlag() != 2) {
        switchButtonStatus(DMBillStatus.CardView);
      }
      showHintMessage(NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000111"));
    }
  }

  public void onGenDeliv()
  {
    DelivbillHVO dhvo = generateVOFromDailyPlan(true);
    
    //add by shikun 
    if (dhvo==null) {
		return;
	}
    //end shikun 
    // add by zip:2014/4/10 No 4
    // 设置默认承运商
  //发运方式+发货地点+到货地点+订单要求到货日期
    DelivbillHHeaderVO pVO = (DelivbillHHeaderVO) dhvo.getParentVO();
    DelivbillHItemVO[] cVOs = (DelivbillHItemVO[]) dhvo.getChildrenVO();
    if(cVOs != null && cVOs.length > 0) {
    	String pkdelivorg =pVO.getPkdelivorg();//发运组织
    	String xyy_fyfs = pVO.getPkdelivmode();// (String) dmdvo.getAttributeValue("pksendtype");
        String xyy_fhdd = cVOs[0].getPksendaddress();// (String) dmdvo.getAttributeValue("pksendaddress");
        String xyy_dhdd = cVOs[0].getPkarriveaddress();// (String) dmdvo.getAttributeValue("pkarriveaddress");
        UFDate xyy_rq = pVO.getSenddate();// (UFDate) dmdvo.getAttributeValue("requiredate");

        //edit by shikun 所有推广公司拥有该功能。
        DelivorgVO bill;
		try {
			bill = DelivorgHelper.findByPrimaryKey(pkdelivorg);
	        if (bill!=null) {
	            DelivorgHeaderVO head = (DelivorgHeaderVO)bill.getParentVO();
	            if (head!=null) {
	                String idelivsequence = head.getAttributeValue("vdoname")==null?"":head.getAttributeValue("vdoname").toString();
	                if (!"".equals(idelivsequence)&&idelivsequence.indexOf("自提")==-1) {
	                    if(!StringUtils.isEmpty(xyy_fyfs) && !StringUtils.isEmpty(xyy_fhdd)
	                       && !StringUtils.isEmpty(xyy_dhdd) && xyy_rq != null) {
	                    	String sql = new StringBuilder()
	                        .append("select pk_transcust from dm_baseprice")
	                        .append(" where nvl(dr,0)=0")
	                        .append(" and pk_sendtype='"+xyy_fyfs+"'")
	                        .append(" and pkfromaddress ='"+xyy_fhdd+"'")
	                        .append(" and pktoaddress='"+xyy_dhdd+"'")
	                        .append(" and effectdate <= '"+xyy_rq+"'")
	                        .append(" and expirationdate > '"+xyy_rq+"'")
	                        .append(" and nvl(dr,0)=0").toString();
	                        Object cysObj = null;
	                		try {
	                			cysObj = DbUtil.getDMO().getObject(sql);
	                		} catch (BusinessException ex) {
	                			ex.printStackTrace();
	                		}
	                        if(cysObj!=null){
	                        	pVO.setPktrancust(cysObj.toString());
	                        }
	                    }
	                }
	            }
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
//        if(pkdelivorg.equals("1016A21000000000WLSK")||pkdelivorg=="1016A21000000000WLSK"){}
    }
    
    // add end
    
    
//    if (dhvo == null)
//      return;
    try {
      IScm srv = (IScm)NCLocator.getInstance().lookup(IScm.class.getName());
      srv.checkDefDataType(dhvo);
      DelivbillHItemVO[] items = (DelivbillHItemVO[])(DelivbillHItemVO[])dhvo.getChildrenVO();
      if ((null != items) && (items.length > 0) && 
        (getDelivSequence() == 1)) {
        UFDouble zero = new UFDouble(0.0D);
        for (int i = 0; i < items.length; i++) {
          if ((items[i].getDoutnum() == null) || (items[i].getDoutnum().equals(zero)))
          {
            items[i].setDoutnum(items[i].getDinvnum());
          }
          if ((items[i].getDoutassistnum() != null) && (!items[i].getDoutassistnum().equals(zero)))
            continue;
          items[i].setDoutassistnum(items[i].getDoutassistnum());
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    if (null != dhvo)
    {
      setTempHeadPK(genTempHeadPK(getTempHeadPK()));
      dhvo.getParentVO().setAttributeValue("pk_delivbill_h", getTempHeadPK());
      for (int i = 0; i < dhvo.getChildrenVO().length; i++) {
        setTempBodyPK(genTempBodyPK(getTempBodyPK()));
        dhvo.getChildrenVO()[i].setAttributeValue("pk_delivbill_h", getTempHeadPK());

        dhvo.getChildrenVO()[i].setAttributeValue("pk_delivbill_b", getTempBodyPK());
      }

      dhvo.getParentVO().setStatus(2);

      getDelivBillClientUI().onAdd();
      getDelivBillClientUI().getBillCardPanel().setBillValueVO(dhvo);
      getDelivBillClientUI().setCurrentBill(dhvo);

      getDelivBillClientUI().getBillCardPanel().execHeadFormula("isendtype->getColvalue(bd_sendtype,freighttype,pk_sendtype,pkdelivmode)");

      String ssendtype = getDelivBillClientUI().getBillCardPanel().getHeadItem("isendtype").getValue();
      UIComboBox cbCardItem = (UIComboBox)getDelivBillClientUI().getBillCardPanel().getHeadItem("isendtype").getComponent();
      int index = 1;
      if (ssendtype != null) {
        index = Integer.valueOf(ssendtype).intValue();
      }
      cbCardItem.setSelectedIndex(index);
      cbCardItem.setEnabled(true);
      cbCardItem.setEditable(true);
      dhvo.getParentVO().setAttributeValue("isendtype", new Integer(index));

      setShowState(DMBillStatus.Card);
      loadPanel();
      getDelivBillClientUI().setInvItemEditable((DMBillCardPanel)getDelivBillClientUI().getBillCardPanel());

      getDelivBillClientUI().getBillCardPanel().getBillModel().execLoadFormula();
      String[] s = { "vcustcode->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,pkcusmandoc)", "vcustcode->getColValue(bd_cubasdoc,custcode,pk_cubasdoc,vcustcode)", "linkman1->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcorpid)", "phone1->getColValue(bd_cubasdoc,phone1,pk_cubasdoc,linkman1)", "linkman1->getColValue(bd_cubasdoc,linkman1,pk_cubasdoc,linkman1)" };

      DelivbillHItemVO[] bodyVO = (DelivbillHItemVO[])(DelivbillHItemVO[])dhvo.getChildrenVO();
      if ((bodyVO != null) && (bodyVO.length > 0)) {
        getDelivBillClientUI().getBillCardPanel().getBillModel().execFormulas(s);
      }

      getDelivBillClientUI().getBillCardPanel().execHeadFormula("vbillpersonname->getColvalue(sm_user,user_name,cUserId,pkbillperson)");

      switchButtonStatus(DMBillStatus.CardEdit);
      getDelivBillClientUI().setEditFlag(0);

      ((DMBillCardPanel)getDelivBillClientUI().getBillCardPanel()).resetAllRowNo();
    }
    else
    {
      onSwitchSource();
    }
  }

  public void onGenDelivBatch()
  {
    DelivbillHVO[] dhvos = generateVOFromDailyPlanBatch();
    if (dhvos == null)
      return;
    for (int j = 0; j < dhvos.length; j++) {
      DelivbillHVO dhvo = dhvos[j];

      DelivbillHItemVO[] items = (DelivbillHItemVO[])(DelivbillHItemVO[])dhvo.getChildrenVO();
      if ((null != items) && (items.length > 0) && 
        (getDelivSequence() == 1)) {
        UFDouble zero = new UFDouble(0.0D);
        for (int i = 0; i < items.length; i++) {
          if ((items[i].getDoutnum() == null) || (items[i].getDoutnum().equals(zero)))
          {
            items[i].setDoutnum(items[i].getDinvnum());
          }
          if ((items[i].getDoutassistnum() != null) && (!items[i].getDoutassistnum().equals(zero)))
            continue;
          items[i].setDoutassistnum(items[i].getDoutassistnum());
        }

      }

      dhvo.getParentVO().setStatus(2);
      try
      {
        CacheTool cache = new CacheTool();
        Object[] ssendtype = (Object[])(Object[])CacheTool.getColumnValue("bd_sendtype", "pk_sendtype", "freighttype", new String[] { ((DelivbillHHeaderVO)dhvo.getParentVO()).getPkdelivmode() });

        int index = 1;
        if ((ssendtype != null) && (ssendtype.length != 0) && 
          (ssendtype[0] != null)) {
          index = Integer.valueOf(ssendtype[0].toString()).intValue();
        }

        dhvo.getParentVO().setAttributeValue("isendtype", new Integer(index));
      }
      catch (BusinessException e) {
      }
    }
    getDelivBillClientUI().m_batchbills = dhvos;
    DelivbillHVO dhvo = dhvos[0];
    getDelivBillClientUI().onAdd();
    getDelivBillClientUI().execFormulaBodys(dhvo.getChildrenVO());
    getDelivBillClientUI().getBillCardPanel().setBillValueVO(dhvo);
    getDelivBillClientUI().setCurrentBill(dhvo);

    UIComboBox cbCardItem = (UIComboBox)getDelivBillClientUI().getBillCardPanel().getHeadItem("isendtype").getComponent();

    int index = ((Integer)dhvo.getParentVO().getAttributeValue("isendtype")).intValue();
    cbCardItem.setSelectedIndex(index);
    cbCardItem.setEnabled(true);
    cbCardItem.setEditable(true);

    setShowState(DMBillStatus.Card);
    loadPanel();
    getDelivBillClientUI().setInvItemEditable((DMBillCardPanel)getDelivBillClientUI().getBillCardPanel());

    getDelivBillClientUI().getBillCardPanel().getBillModel().execLoadFormula();

    getDelivBillClientUI().switchButtonStatus(9);

    switchButtonStatus(DMBillStatus.CardEdit);

    getDelivBillClientUI().setEditFlag(2);
    getDelivBillClientUI().setListRow(0);

    ((DMBillCardPanel)getDelivBillClientUI().getBillCardPanel()).resetAllRowNo();
  }

  public void onGenTaskBill()
  {
    getDelivBillClientUI().onGenTaskBill();
    if (getDelivBillClientUI().isOperationFinish())
      switchButtonStatus(DMBillStatus.CardView);
  }

  public void onInsertDeliv()
  {
    DelivbillHVO voToBeInserted = generateVOFromDailyPlan(false);
    if (null == voToBeInserted) {
      return;
    }
    DelivbillHItemVO[] items = (DelivbillHItemVO[])(DelivbillHItemVO[])voToBeInserted.getChildrenVO();

    boolean isFromPO = false;
    for (int i = 0; i < items.length; i++) {
      if ((!items[i].getVbilltype().equals("21")) && (isFromPO)) {
        showWarningMessage(NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000124"));

        return;
      }

      if (items[i].getVbilltype().equals("21")) {
        isFromPO = true;
      }

    }

    if (getDelivSequence() == 1) {
      UFDouble zero = new UFDouble(0.0D);
      for (int i = 0; i < items.length; i++) {
        if ((items[i].getDoutnum() == null) || (items[i].getDoutnum().equals(zero)))
        {
          items[i].setDoutnum(items[i].getDinvnum());
        }
        if ((items[i].getDoutassistnum() != null) && (!items[i].getDoutassistnum().equals(zero)))
          continue;
        items[i].setDoutassistnum(items[i].getDoutassistnum());
      }
    }

    String pkSendType = voToBeInserted.getParentVO().getAttributeValue("pkdelivmode").toString();

    DMVO selVO = new DMVO();
    try
    {
      DelivbillHVO[] matchedDelivBillVOs = DelivbillHBO_Client.findDelivBills(getDelivOrgPK(), " (dm_delivbill_h.pkdelivorg='" + getDelivOrgPK() + "' and dm_delivbill_h.bconfirm='N' and (dm_delivbill_h.pkapprperson is null or len(rtrim(dm_delivbill_h.pkapprperson))=0)  " + "and dm_delivbill_h.pkdelivmode = '" + pkSendType + "' ) ", "( dm_delivbill_b.irowstatus is null or dm_delivbill_b.irowstatus=" + DelivBillStatus.Free + " or dm_delivbill_b.irowstatus=" + DelivBillStatus.Confirm + ") ");

      if ((matchedDelivBillVOs == null) || (matchedDelivBillVOs.length == 0)) {
        showWarningMessage(NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000125"));

        return;
      }

      DelivbillHHeaderVO[] allheaders = getAllHeads(matchedDelivBillVOs);
      execFormulaHeads(allheaders);

      FreeVOParse freeVOParse = new FreeVOParse();
      for (int i = 0; i < matchedDelivBillVOs.length; i++) {
        items = (DelivbillHItemVO[])(DelivbillHItemVO[])matchedDelivBillVOs[i].getChildrenVO();
        freeVOParse.setFreeVO(items, null, "pkinv", false);
      }

      getChooseDeliveryBillDLG().setDelivBillVOs(matchedDelivBillVOs, voToBeInserted);

      for (int i = 0; i < allheaders.length; i++) {
        getChooseDeliveryBillDLG().getBillListPanel().getHeadBillModel().setValueAt(allheaders[i].getPkdelivmode(), i, "vsendtypename");
      }

      getChooseDeliveryBillDLG().getBillListPanel().getHeadBillModel().execFormulas(new String[] { "vsendtypename->getColValue(bd_sendtype,sendname,pk_sendtype,vsendtypename)" });

      getChooseDeliveryBillDLG().getBillListPanel().getBodyBillModel().clearBodyData();
    }
    catch (Exception e)
    {
      showErrorMessage(e.getMessage());
      handleException(e);
      return;
    }

    if (getChooseDeliveryBillDLG().showModal() != 1)
    {
      return;
    }
    selVO = getChooseDeliveryBillDLG().getSelectVO();

    String sHeadPK = selVO.getParentVO().getAttributeValue("pk_delivbill_h").toString();

    getDelivBillClientUI().setInsertExist(true);

    for (int i = 0; i < voToBeInserted.getChildrenVO().length; i++) {
      voToBeInserted.getChildrenVO()[i].setStatus(2);

      ((DelivbillHItemVO)voToBeInserted.getChildrenVO()[i]).setIrowstatus(new Integer(DelivBillStatus.Free));
    }

    DelivbillHVO dhvoOld = new DelivbillHVO();
    dhvoOld.resetVOFromDelivBillDMVO(selVO);

    Vector v = new Vector();
    for (int i = 0; i < dhvoOld.getChildrenVO().length; i++) {
      v.add(dhvoOld.getChildrenVO()[i]);
    }
    for (int i = 0; i < voToBeInserted.getChildrenVO().length; i++) {
      v.add(voToBeInserted.getChildrenVO()[i]);
    }
    DelivbillHItemVO[] tmpItems = new DelivbillHItemVO[v.size()];
    v.copyInto(tmpItems);
    dhvoOld.setChildrenVO(tmpItems);

    DelivbillHVO[] dhvoAlreadyHave = getDelivBillClientUI().getArrayBills();
    if (null == dhvoAlreadyHave)
      dhvoAlreadyHave = new DelivbillHVO[0];
    boolean bIsAlreadyHave = false;
    if ((null != dhvoAlreadyHave) && (dhvoAlreadyHave.length != 0)) {
      for (int i = 0; i < dhvoAlreadyHave.length; i++) {
        if (!dhvoAlreadyHave[i].getParentVO().getAttributeValue("pk_delivbill_h").equals(dhvoOld.getParentVO().getAttributeValue("pk_delivbill_h"))) {
          continue;
        }
        bIsAlreadyHave = true;
        getDelivBillClientUI().setListRow(i);
        dhvoAlreadyHave[i] = dhvoOld;
        getDelivBillClientUI().setArrayBills(dhvoAlreadyHave);
        break;
      }

    }

    getDelivBillClientUI().setCurrentBill(dhvoOld);

    getDelivBillClientUI().doForEdit();

    setShowState(DMBillStatus.Card);
    loadPanel();

    switchButtonStatus(DMBillStatus.CardEdit);

    ((DMBillCardPanel)getDelivBillClientUI().getBillCardPanel()).setRowNoOnNullRow();
  }

  public void onLast()
  {
    getDelivBillClientUI().onLast();
    if (getDelivBillClientUI().isOperationFinish()) {
      if (getDelivBillClientUI().getEditFlag() != 2) {
        switchButtonStatus(DMBillStatus.CardView);
      }
      showHintMessage(NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000111"));
    }
  }

  public void onNext()
  {
    getDelivBillClientUI().onNext();
    if (getDelivBillClientUI().isOperationFinish()) {
      if (getDelivBillClientUI().getEditFlag() != 2) {
        switchButtonStatus(DMBillStatus.CardView);
      }
      showHintMessage(NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000111"));
    }
  }

  public void onOpen()
  {
    getDelivBillClientUI().onOpen();
    if (getDelivBillClientUI().isOperationFinish())
      switchButtonStatus(DMBillStatus.CardView);
  }

  public void onOutBill()
  {
    getDelivBillClientUI().onOutBill();
    if (getDelivBillClientUI().isOperationFinish()) {
      switchButtonStatus(DMBillStatus.CardView);
      onSwitchForm();
    }
  }

  public void onPackageList()
  {
    getDelivBillClientUI().onPackageList();
  }

  public void onPre()
  {
    getDelivBillClientUI().onPre();
    if (getDelivBillClientUI().isOperationFinish()) {
      if (getDelivBillClientUI().getEditFlag() != 2) {
        switchButtonStatus(DMBillStatus.CardView);
      }
      showHintMessage(NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000111"));
    }
  }

  public void onPrint()
  {
    getDelivBillClientUI().onPrint();
  }

  public void onPrintDelivMinBill()
  {
    getDelivBillClientUI().onShowListBill();
  }

  public void onPrintPreview()
  {
    getDelivBillClientUI().onPrintPreview();
  }

  public void onQuery()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH046"));

    getDelivBillClientUI().onQuery();
    if (!getDelivBillClientUI().isOperationFinish())
      return;
    switchButtonStatus(DMBillStatus.ListView);
    onSwitchList();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH009"));
  }

  public void onRefresh()
  {
    getDelivBillClientUI().onRefresh();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH007"));

    if (!getDelivBillClientUI().isOperationFinish()) {
      return;
    }
    switchButtonStatus(DMBillStatus.ListView);
    onSwitchList();
  }

  public void onQueryDelivDaily()
  {
    getDelivDailyConditionDlg().hideNormal();

    String sWhereClause = "";
    if (getDelivDailyConditionDlg().showModal() == 1)
    {
      ConditionVO[] voCons = getDelivDailyConditionDlg().getConditionVO();

      voCons = ConvertQueryCondition.getConvertedVO(voCons, null);

      getDelivDailyConditionDlg().checkCondition(voCons);

      afterDelivDailyQuery(voCons);
    }
  }

  private void onRowCloseOut()
  {
    getDelivBillClientUI().onRowCloseOut();
    if (getDelivBillClientUI().isOperationFinish())
      switchButtonStatus(DMBillStatus.CardView);
  }

  private void onRowOpenOut()
  {
    getDelivBillClientUI().onRowOpenOut();
    if (getDelivBillClientUI().isOperationFinish())
      switchButtonStatus(DMBillStatus.CardView);
  }

  public void onSave()
  {
    DMVO dmvo = null;
    if (getDelivBillClientUI().getEditFlag() == 2) {
      DelivbillHVO[] hvos = getDelivBillClientUI().m_batchbills;
      if (null != hvos) {
        for (int i = 0; i < hvos.length; i++) {
          for (int j = 0; j < hvos[i].getChildrenVO().length; j++) {
            this.vSrcBillPK4Update.add(hvos[i].getChildrenVO()[j].getAttributeValue("pkdayplan"));
          }
        }
      }
    }
    else
    {
      dmvo = (DMVO)getDelivBillClientUI().getBillCardPanel().getBillValueChangeVO(DMVO.class.getName(), DMDataVO.class.getName(), DMDataVO.class.getName());

      if (null != dmvo.getBodyVOs()) {
        for (int i = 0; i < dmvo.getBodyVOs().length; i++) {
          this.vSrcBillPK4Update.add(dmvo.getBodyVOs()[i].getAttributeValue("pkdayplan"));
        }

      }

    }

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH044"));
    try
    {
      if (checkOther(dmvo)) {
        getDelivBillClientUI().onBatch();
        showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH005"));
      }
      else {
        return;
      }
    } catch (Exception e) {
      showErrorMessage(e.getMessage());
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "MD2", null, new String[] { "" }));
    }

    if (getDelivBillClientUI().isOperationFinish()) {
      switchButtonStatus(DMBillStatus.CardView);
    }
    else {
      this.vSrcBillPK4Update.removeAllElements();
    }

    this.m_bInsertDlvbill = false;
  }

  public boolean checkOther(AggregatedValueObject vo) throws Exception {
    if (vo == null)
    {
      return true;
    }
    String sAllErrorMessage = "";

    if (this.DM003.booleanValue()) {
      Object oVehiclePK = vo.getParentVO().getAttributeValue("pkvehicle");
      if ((oVehiclePK != null) && (oVehiclePK.toString().trim().length() != 0)) {
        UFDouble ufdSumTotalWeight = new UFDouble(0);
        for (int i = 0; i < vo.getChildrenVO().length; i++) {
          CircularlyAccessibleValueObject cavo = vo.getChildrenVO()[i];
          Object oCalculateWeight = cavo.getAttributeValue("dinvweight");
          if ((null == oCalculateWeight) || (oCalculateWeight.toString().trim().length() == 0))
            continue;
          ufdSumTotalWeight = ufdSumTotalWeight.add(new UFDouble(oCalculateWeight.toString()));
        }

        DmVehicleItemVO vvo = VehicleHelper.findByPrimaryKey(oVehiclePK.toString());

        Object oVehicleWeight = vvo.getAttributeValue("dload");

        if ((null != oVehicleWeight) && (oVehicleWeight.toString().trim().length() != 0))
        {
          UFDouble ufdVehicleWeight = new UFDouble(oVehicleWeight.toString());
          if (ufdSumTotalWeight.doubleValue() > ufdVehicleWeight.doubleValue()) {
            String[] value = { ufdVehicleWeight.toString(), ufdSumTotalWeight.toString() };

            String sErrorMessage = NCLangRes.getInstance().getStrByID("40140408", "UPP40140408-000108", null, value);

            sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
          }
        }
        else if (ufdSumTotalWeight.doubleValue() > 0.0D) {
          String[] value = { ufdSumTotalWeight.toString() };

          String sErrorMessage = NCLangRes.getInstance().getStrByID("40140408", "UPP40140408-000109", null, value);

          sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
        }

      }

    }

    if (this.DM004.booleanValue()) {
      Object oVehiclePK = vo.getParentVO().getAttributeValue("pkvehicle");
      if ((oVehiclePK != null) && (oVehiclePK.toString().trim().length() != 0)) {
        UFDouble ufdSumTotalVolumn = new UFDouble(0);
        for (int i = 0; i < vo.getChildrenVO().length; i++) {
          CircularlyAccessibleValueObject cavo = vo.getChildrenVO()[i];
          Object oCalculateVolumn = cavo.getAttributeValue("dvolumn");
          if ((null == oCalculateVolumn) || (oCalculateVolumn.toString().trim().length() == 0))
            continue;
          ufdSumTotalVolumn = ufdSumTotalVolumn.add(new UFDouble(oCalculateVolumn.toString()));
        }

        DmVehicleItemVO vvo = VehicleHelper.findByPrimaryKey(oVehiclePK.toString());

        Object oVehicleVolumn = vvo.getAttributeValue("dcubage");

        if ((null != oVehicleVolumn) && (oVehicleVolumn.toString().trim().length() != 0))
        {
          UFDouble ufdVehicleVolumn = new UFDouble(oVehicleVolumn.toString());
          if (ufdSumTotalVolumn.doubleValue() > ufdVehicleVolumn.doubleValue()) {
            String[] value = { ufdVehicleVolumn.toString(), ufdSumTotalVolumn.toString() };

            String sErrorMessage = NCLangRes.getInstance().getStrByID("40140408", "UPP40140408-000110", null, value);

            sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
          }
        }
        else if (ufdSumTotalVolumn.doubleValue() > 0.0D) {
          String[] value = { ufdSumTotalVolumn.toString() };

          String sErrorMessage = NCLangRes.getInstance().getStrByID("40140408", "UPP40140408-000111", null, value);

          sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
        }
      }

    }

    if (sAllErrorMessage.trim().length() != 0) {
      showErrorMessage(sAllErrorMessage);
      return false;
    }

    DMDataVO[] changeitemsvo = (DMDataVO[])(DMDataVO[])vo.getChildrenVO();
    StringBuffer sb = new StringBuffer();
    sb.append("select iplanstatus,vdelivdayplcode from dm_delivdaypl where vdelivdayplcode in (");
    for (int i = 0; i < changeitemsvo.length; i++) {
      if (i == changeitemsvo.length - 1)
        sb.append("'" + changeitemsvo[i].getAttributeValue("vdayplancode") + "'");
      else {
        sb.append("'" + changeitemsvo[i].getAttributeValue("vdayplancode") + "'" + ",");
      }
    }
    sb.append(")");

    DMDataVO[] vos = DmHelper.queryStringBuffer(sb);
    for (int i = 0; i < vos.length; i++) {
      if (((Integer)vos[i].getAttributeValue("iplanstatus")).intValue() == 2) {
        String message = (String)vos[i].getAttributeValue("vdelivdayplcode");
        showErrorMessage("日记划状态关闭,单据号:" + message);
        return false;
      }
    }

    return true;
  }

  public void onSelectAll()
  {
    getThdBillCardPanel().stopEditing();
    int row = getThdBillCardPanel().getBillTable().getRowCount();
    if (row == 0)
      return;
    int col = getThdBillCardPanel().getBillModel().getBodyColByKey("bchoose");
    Boolean b = new Boolean(true);
    for (int i = 0; i < row; i++)
      getThdBillCardPanel().getBillModel().setValueAt(b, i, col);
  }

  public void onSort()
  {
    if (this.m_strShowState.equals(DMBillStatus.Source))
    {
      getThdBillCardPanel().onSort();
    }
  }

  public void onSwitchForm()
  {
    if (getDelivBillClientUI().getArrayBills().length == 0) {
      showWarningMessage(NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000128"));

      return;
    }
    if (getDelivBillClientUI().getShowState().equals(DMBillStatus.List)) {
      getDelivBillClientUI().onSwith();
      if (getDelivBillClientUI().isOperationFinish())
        onSwitchForm1();
    }
    else
    {
      onSwitchForm1();

      switchButtonStatus(DMBillStatus.CardView);
    }
  }

  public void onSwitchForm1()
  {
    setShowState(DMBillStatus.Card);

    loadPanel();
    setTitleText(this.strTitle1);
  }

  public void onSwitchList()
  {
    if (getDelivBillClientUI().getShowState().equals(DMBillStatus.Card)) {
      getDelivBillClientUI().onSwith();
    }
    super.onSwitchList();
  }

  public void onTestCalculateFee()
  {
    getDelivBillClientUI().onTestCalculateFee();
    if (getDelivBillClientUI().isOperationFinish())
      switchButtonStatus(DMBillStatus.ListView);
  }

  public void onTransConfirm()
  {
    checkTransConfirm();

    if (getDelivBillClientUI().isOperationFinish())
      switchButtonStatus(DMBillStatus.CardView);
  }

  public void checkTransConfirm()
  {
    try
    {
      this.m_isOperationFinish = false;
      DelivbillHHeaderVO header = (DelivbillHHeaderVO)getDelivBillClientUI().m_currentbill.getParentVO();
      DelivbillHItemVO[] items = (DelivbillHItemVO[])(DelivbillHItemVO[])getDelivBillClientUI().m_currentbill.getChildrenVO();

      for (int i = 0; i < items.length; i++) {
        items[i].setIrowstatus(new Integer(DelivBillStatus.Confirm));
        items[i].setStatus(1);
        Object dConfirmArriveDate = items[i].getAttributeValue("confirmarrivedate");

        if ((dConfirmArriveDate != null) && (dConfirmArriveDate.toString().trim().length() != 0))
          continue;
        showErrorMessage(NCLangRes.getInstance().getStrByID("40140408", "UPP40140408-000047"));

        return;
      }

      header.setBconfirm(new UFBoolean(true));
      header.setStatus(1);

      getDelivBillClientUI().m_currentbill.setGenOIDPK(getCorpID());
      header.setAttributeValue("userid", getUserID());

      ArrayList ary = DelivbillHBO_Client.confirm(getDelivBillClientUI().m_currentbill, null, false, new ClientLink(getClientEnvironment()));

      if ((null != ary) && (ary.size() > 0) && 
        (getDelivBillClientUI().m_currentbill.getParentVO() != null)) {
        getDelivBillClientUI().m_currentbill.getParentVO().setAttributeValue("ts", ary.get(ary.size() - 1));
      }

      getBillCardPanel().setBillValueVO(getDelivBillClientUI().m_currentbill);
      getBillCardPanel().getBillModel().execLoadFormula();
      getBillCardPanel().updateValue();
      getDelivBillClientUI().switchButtonStatusWithBill();
      this.m_isOperationFinish = true;
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "MT9"));
    }
    catch (Exception e)
    {
      showErrorMessage(e.getMessage());
      handleException(e);
    }
  }

  public void onUnSelectAll()
  {
    getThdBillCardPanel().stopEditing();
    int row = getThdBillCardPanel().getBillTable().getRowCount();
    if (row == 0)
      return;
    int col = getThdBillCardPanel().getBillModel().getBodyColByKey("bchoose");
    Boolean b = new Boolean(false);
    for (int i = 0; i < row; i++)
      getThdBillCardPanel().getBillModel().setValueAt(b, i, col);
  }

  public void paint(Graphics g)
  {
    super.paint(g);

  /*  if (!this.showed)
    {
      Runnable con = SwingUtilities.getRootPane(this);
      this.showed = true;
      SwingUtilities.invokeLater(new Thread(con)
      {
        public void run()
        {
          ButtonBar bb = (ButtonBar)MiscUtils.findChildByClass(this.val$con, ButtonBar.class);

          if (bb != null) {
            int count = bb.getComponentCount();
            for (int i = 0; i < count; i++) {
              if ((!(bb.getComponent(i) instanceof MenuButton)) || (!bb.getComponent(i).isEnabled()))
                continue;
              bb.getComponent(i).requestFocus();
              break;
            }
          }
        }
      });
    }*/
  }

  private DMDataVO[] queryDelivDailyPlan(String[] saPk_delivdaypl, ConditionVO[] voCons)
  {
    try
    {
      DMDataVO voNormal = new DMDataVO();
      voNormal.setAttributeValue("ClientLink", new ClientLink(ClientEnvironment.getInstance()));

      voNormal.setAttributeValue("DeputCorpPKs", getAgentCorpIDsofDelivOrg());

      voNormal.setAttributeValue("iplanstatus", new Integer(DailyPlanStatus.Audit));

      voNormal.setAttributeValue("pksendtype", NCLangRes.getInstance().getStrByID("40140406", "UPP40140406-000139"));

      if (saPk_delivdaypl != null) {
        voNormal.setAttributeValue("pk_delivdaypls", saPk_delivdaypl);
      }

      DMDataVO[] dmdvos = DeliverydailyplanBO_Client.query(voNormal, voCons);

      execPlanFormulaBodys(dmdvos);

      FreeVOParse freeVOParse = new FreeVOParse();
      freeVOParse.setFreeVO(dmdvos, null, "pkinv", false);

      if ((null != dmdvos) && (dmdvos.length != 0))
      {
        for (int i = 0; i < dmdvos.length; i++) {
          dmdvos[i].setAttributeValue("weightunit", getWeightUnit());
          dmdvos[i].setAttributeValue("volumnunit", getCapacityUnit());
        }
      }

      return dmdvos;
    }
    catch (Exception e) {
      showErrorMessage(e.getMessage());
      e.printStackTrace();
    }

    return null;
  }

  public void refreshButtonsStatus()
  {
    for (int i = 0; i < this.aryButtonGroup.length; i++) {
      setButton(this.aryButtonGroup[i], this.aryButtonGroup[i].isEnabled());

      if (this.aryButtonGroup[i].getChildCount() > 0) {
        for (int j = 0; j < this.aryButtonGroup[i].getChildCount(); j++) {
          setButton(this.aryButtonGroup[i].getChildButtonGroup()[j], this.aryButtonGroup[i].getChildButtonGroup()[j].isEnabled());
        }
      }
    }
    for (int i = 0; i < this.aryListButtonGroup.length; i++) {
      setButton(this.aryListButtonGroup[i], this.aryListButtonGroup[i].isEnabled());
      if (this.aryListButtonGroup[i].getChildCount() > 0) {
        for (int j = 0; j < this.aryListButtonGroup[i].getChildCount(); j++) {
          setButton(this.aryListButtonGroup[i].getChildButtonGroup()[j], this.aryListButtonGroup[i].getChildButtonGroup()[j].isEnabled());
        }
      }
    }
    for (int i = 0; i < this.arySourceButtonGroup.length; i++) {
      setButton(this.arySourceButtonGroup[i], this.arySourceButtonGroup[i].isEnabled());
      if (this.arySourceButtonGroup[i].getChildCount() > 0)
        for (int j = 0; j < this.arySourceButtonGroup[i].getChildCount(); j++)
          setButton(this.arySourceButtonGroup[i].getChildButtonGroup()[j], this.arySourceButtonGroup[i].getChildButtonGroup()[j].isEnabled());
    }
  }

  protected void setArrayListToCardPanel(ArrayList al)
  {
    DMDataVO[] ddvos = convertArrayListtoLines(al);
    DMVO dmvo = new DMVO();
    dmvo.setChildrenVO(ddvos);
    getBillCardPanel().resumeValue();
    if (null != ddvos)
      getBillCardPanel().setBillValueVO(dmvo);
    getBillCardPanel().updateValue();
  }

  protected void setBillData(DelivbillHVO[] dmdvos)
  {
    ArrayList al = getAllVOs();
    if ((dmdvos != null) || (dmdvos.length != 0)) {
      for (int i = 0; i < dmdvos.length; i++) {
        boolean bFound = false;
        for (int j = 0; j < al.size(); j++) {
          if (!((DelivbillHVO)al.get(j)).getParentVO().getAttributeValue("pk_delivbill_h").equals(dmdvos[i].getParentVO().getAttributeValue("pk_delivbill_h"))) {
            continue;
          }
          al.set(j, dmdvos[i]);
          bFound = true;
          break;
        }

        if (!bFound)
          al.add(dmdvos[i]);
      }
      setAllVOs(al);
    }

    setArrayListToCardPanel(al);
  }

  protected void setDelivBillClientUI(nc.ui.dm.dm104.ClientUI newDelivBillClientUI)
  {
    this.m_DelivBillClientUI = newDelivBillClientUI;
  }

  public void setInvItemEditable(DMBillCardPanel bcp)
  {
    String[] itemkeys = { "pkinv" };

    filterNullLine(itemkeys, bcp);

    DMVO dvo = (DMVO)bcp.getBillValueVO(DMVO.class.getName(), DMDataVO.class.getName(), DMDataVO.class.getName());

    DMDataVO[] dmdvos = dvo.getBodyVOs();

    String[] invkeys = new String[dmdvos.length];
    String[] astkeys = new String[dmdvos.length];
    for (int i = 0; i < dmdvos.length; i++) {
      invkeys[i] = ((String)dmdvos[i].getAttributeValue("pkinv"));
      astkeys[i] = ((String)dmdvos[i].getAttributeValue("pkassistmeasure"));
    }

    InvVO[] invvos = getInvInfo(invkeys, astkeys);

    for (int i = 0; i < dmdvos.length; i++)
    {
      Integer isassistunit = (Integer)invvos[i].getAttributeValue("isAstUOMmgt");

      if (!isassistunit.equals(new Integer(1))) {
        bcp.setCellEditable(i, "vassistmeaname", false);
      }
      Integer isbatch = (Integer)invvos[i].getAttributeValue("isLotMgt");
      if (!isbatch.equals(new Integer(1))) {
        bcp.setCellEditable(i, "vbatchcode", false);
      }
      Integer isfreeitem = (Integer)invvos[i].getAttributeValue("isFreeItemMgt");

      if (!isfreeitem.equals(new Integer(1)))
        bcp.setCellEditable(i, "vfree0", false);
    }
  }

  protected void setOldDelivVOs(ArrayList newOldDelivVOs)
  {
    this.m_alOldDelivVOs = newOldDelivVOs;
  }

  protected void setOldPlanVO(DMVO newOldPlanVO)
  {
    this.m_dmOldPlanVO = newOldPlanVO;
  }

  public void setQueryConditionDlg(DMQueryConditionDlg newConditionDlg)
  {
    this.queryConditionDlg = newConditionDlg;
    this.queryConditionDlg.setTempletID(getCorpID(), "40140408", getUserID(), null);

    this.queryConditionDlg.setDefaultValue("pkdelivorg", getDelivOrgPK(), null);
    this.queryConditionDlg.setConditionEditable("pkdelivorg", false);

    this.queryConditionDlg.initQueryDlgRef();
  }

  protected void setTempBodyPK(String newTempBodyPK)
  {
    this.m_sTempBodyPK = newTempBodyPK;
  }

  protected void setTempHeadPK(String newTempHeadPK)
  {
    this.m_sTempHeadPK = newTempHeadPK;
  }

  protected void setThdBillData(DMDataVO[] dmdvos, boolean bIsAppendQuery)
  {
    if ((dmdvos != null) || (dmdvos.length != 0)) {
      DMVO dmvo = new DMVO();

      if ((bIsAppendQuery) && (getThdBillCardPanel().getRowCount() != 0)) {
        DMVO dmvoOld = new DMVO(getThdBillCardPanel().getRowCount());
        getThdBillCardPanel().getBillValueVO(dmvoOld);
        Vector v = new Vector();
        for (int i = 0; i < dmvoOld.getBodyVOs().length; i++) {
          v.add(dmvoOld.getBodyVOs()[i]);
        }
        for (int i = 0; i < dmdvos.length; i++) {
          boolean bFound = false;
          for (int j = 0; j < dmvoOld.getBodyVOs().length; j++) {
            if ((null == dmvoOld.getBodyVOs()[j].getAttributeValue("pk_delivdaypl")) || (!dmvoOld.getBodyVOs()[j].getAttributeValue("pk_delivdaypl").equals(dmdvos[i].getAttributeValue("pk_delivdaypl"))))
            {
              continue;
            }

            bFound = true;
            v.set(j, dmdvos[i]);
            break;
          }

          if (!bFound) {
            v.add(dmdvos[i]);
          }
          DMDataVO[] ddvos = new DMDataVO[v.size()];
          v.copyInto(ddvos);
          dmvo.setChildrenVO(ddvos);
        }
      }
      else
      {
        dmvo.setChildrenVO(dmdvos);
      }

      if (dmvo.getBodyVOs() == null) {
        getThdBillCardPanel().resumeValue();
        getThdBillCardPanel().updateValue();
      }
      else
      {
        if (getDelivSequence() == 1) {
          dmvo.setAllBodyValueByOtherKey("dinvnum", "doutnum");
        }

        getThdBillCardPanel().setBillValueVO(dmvo, false);
        getThdBillCardPanel().getBillModel().execLoadFormula();

        String[] s = { "vbilltypename->getColValue(bd_billtype,billtypename,pk_billtypecode,vbilltype)", "pkcustbastmp->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,csendcorpid)", "vsendcorpname->getColValue(bd_cubasdoc,custname,pk_cubasdoc,pkcustbastmp)" };

        getThdBillCardPanel().getBillModel().execFormulas(s);

        getThdBillCardPanel().updateValue();
      }
    }
  }

  protected void setThdCardPanelByOther(BillData bdData)
  {
    bdData.getBodyItem("vsendcorpname").setShow(true);
    bdData.getBodyItem("csendcorpid").setShow(false);
  }

  public void switchButtonStatus(int status)
  {
    if (status == DMBillStatus.CardView)
    {
      setButton(this.boSwith, true);
      setButton(this.boQueryDelivDaily, true);
      setButton(this.boGenDeliv, true);
      setButton(this.boInsertDeliv, true);
      setButton(this.boPrintPreview, true);
      setButton(this.boPackageList, true);

      setButton(this.boRowCloseOut, getDelivBillClientUI().getBtnRowCloseOutState());
      setButton(this.boRowOpenOut, getDelivBillClientUI().getBtnRowOpenOutState());

      getDelivBillClientUI().evaluateBtnCalculateFee();
    }
    else if (status == DMBillStatus.ListView)
    {
      setButton(this.boSwith, true);
      setButton(this.boQueryDelivDaily, true);
      setButton(this.boGenDeliv, true);
      setButton(this.boInsertDeliv, true);
      setButton(this.boPrintPreview, true);
    }
    else if (status == DMBillStatus.CardEdit)
    {
      setButton(this.boSwith, false);
      setButton(this.boQueryDelivDaily, false);
      setButton(this.boGenDeliv, false);
      setButton(this.boInsertDeliv, false);
      setButton(this.boPrintPreview, false);
      setButton(this.boPackageList, true);
      setButton(this.boOpenBill, false);

      setButton(this.boRowCloseOut, false);
      setButton(this.boRowOpenOut, false);

      setButton(this.boCalculateFee, false);
    }
    else if (status == DMBillStatus.SourceView)
    {
      setButton(this.boSwith, true);
      setButton(this.boQueryDelivDaily, true);
      setButton(this.boGenDeliv, true);
      setButton(this.boInsertDeliv, true);
      setButton(this.boPrintPreview, true);
    }
    setExtendBtnsStat(status);
    refreshButtonsStatus();
  }

  protected void switchInterface()
  {
    if (this.m_strShowState.equals(DMBillStatus.List)) {
      if (null != this.ivjThdBillCardPanel)
        remove(getThdBillCardPanel());
      add(getDelivBillClientUI(), "Center");
      setTitleText(this.strTitle1);
    }
    else if (this.m_strShowState.equals(DMBillStatus.Card))
    {
      if (null != this.ivjThdBillCardPanel) {
        remove(getThdBillCardPanel());
      }
      add(getDelivBillClientUI(), "Center");
      setTitleText(this.strTitle1);
    }
    else if (this.m_strShowState.equals(DMBillStatus.Source))
    {
      if (this.vSrcBillPK4Update.size() > 0) {
        loadThdBillData((String[])(String[])this.vSrcBillPK4Update.toArray(new String[this.vSrcBillPK4Update.size()]), null, true);

        this.vSrcBillPK4Update.removeAllElements();
      }

      remove(getDelivBillClientUI());

      add(getThdBillCardPanel(), "Center");
      setTitleText(this.strTitle3);
    }

    updateUI();
  }

  protected void switchListToCard(int ListRow)
  {
    String sHeadPK = getBillCardPanel().getBodyValueAt(ListRow, "pk_delivbill_h").toString();

    for (int i = 0; i < getAllVOs().size(); i++) {
      DelivbillHVO dhvo = (DelivbillHVO)getAllVOs().get(i);
      if (!dhvo.getParentVO().getAttributeValue("pk_delivbill_h").toString().equals(sHeadPK))
        continue;
      getSecBillCardPanel().resumeValue();
      getSecBillCardPanel().setBillValueVO(dhvo);
      getSecBillCardPanel().updateValue();
      break;
    }
  }

  protected void synchroBodyEdit(int row, String key, Object value, BillCardPanel bcpfrom, BillCardPanel bcpto)
  {
    String PKFieldName = "pk_delivbill_b";
    String PKValue = bcpfrom.getBodyValueAt(row, PKFieldName).toString();
    for (int i = 0; i < bcpto.getRowCount(); i++) {
      if (bcpto.getBodyValueAt(i, PKFieldName).toString().equals(PKValue)) {
        bcpto.setBodyValueAt(value, i, key);

        if (!key.equals("dinvnum")) break;
        if (bcpfrom.equals(getBillCardPanel())) {
          synchroNumberEdit(row, key, "dsendnum", value, bcpfrom, getThdBillCardPanel());

          break;
        }if (!bcpto.equals(getBillCardPanel())) break;
        synchroNumberEdit(i, key, "dsendnum", value, bcpto, getThdBillCardPanel());

        break;
      }

    }

    for (int i = 0; i < getAllVOs().size(); i++) {
      CircularlyAccessibleValueObject[] bodyvos = ((AggregatedValueObject)getAllVOs().get(i)).getChildrenVO();

      for (int j = 0; j < bodyvos.length; j++) {
        if (!bodyvos[j].getAttributeValue(PKFieldName).toString().equals(PKValue))
          continue;
        bodyvos[j].setAttributeValue(key, value);
        if (bodyvos[j].getStatus() == 0)
          bodyvos[j].setStatus(1);
        ((AggregatedValueObject)getAllVOs().get(i)).setChildrenVO(bodyvos);
      }
    }
  }

  protected void synchroHeadEdit(String key, Object value)
  {
    String PKFieldName = "pk_delivbill_h";
    String PKValue = getSecBillCardPanel().getHeadItem(PKFieldName).getValue();
    for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
      if ((getBillCardPanel().getHeadItem(PKFieldName) == null) || (!getBillCardPanel().getHeadItem(PKFieldName).getValue().equals(PKValue))) {
        continue;
      }
      getBillCardPanel().getHeadItem(key).setValue(value);
    }

    for (int i = 0; i < getAllVOs().size(); i++) {
      DelivbillHHeaderVO headVO = (DelivbillHHeaderVO)((DelivbillHVO)getAllVOs().get(i)).getParentVO();

      if (headVO.getAttributeValue(PKFieldName).toString().equals(PKValue)) {
        if (headVO.getStatus() == 0)
          headVO.setStatus(1);
        headVO.setAttributeValue(key, value);

        DelivbillHVO dhvoAll = (DelivbillHVO)getAllVOs().get(i);
        dhvoAll.setParentVO(headVO);
        getAllVOs().set(i, dhvoAll);
      }
    }
  }

  protected void synchroNumberEdit(int row, String fromkey, String tokey, Object newvalue, BillCardPanel bcpfrom, BillCardPanel bcpto)
  {
    String PKFieldNamefrom = "pkdayplan";
    String PKFieldNameto = "pk_delivdaypl";
    String PKValue = bcpfrom.getBodyValueAt(row, PKFieldNamefrom).toString();
    UFDouble ufdnewvalue = newvalue == null ? new UFDouble(0) : new UFDouble(newvalue.toString());

    for (int i = 0; i < bcpto.getRowCount(); i++)
      if (bcpto.getBodyValueAt(i, PKFieldNameto).toString().equals(PKValue)) {
        Object oldvalue = bcpto.getBodyValueAt(i, tokey);
        UFDouble ufdOldvalue = oldvalue == null ? new UFDouble(0) : new UFDouble(oldvalue.toString());

        bcpto.setBodyValueAt(ufdOldvalue.add(ufdnewvalue.sub(this.m_ufdLastValue)), i, tokey);

        break;
      }
  }

  public void whenEntered(int row, int col, String key, Object value, BillCardPanel bcp)
  {
    super.whenEntered(row, col, key, value, bcp);
    if (key.equals("dinvnum")) {
      Object obj = bcp.getBodyValueAt(row, key);
      this.m_ufdLastValue = (obj == null ? new UFDouble(0) : new UFDouble(obj.toString()));
    }
    else if (key.equals("doutnum"))
    {
      String vbilltype = (String)bcp.getBodyValueAt(row, "vbilltype");
      if ((getDelivSequence() == 1) && (!vbilltype.equals("21"))) {
        bcp.getBodyItem("doutnum").setEnabled(true);
      }
      else
        bcp.getBodyItem("doutnum").setEnabled(false);
    }
  }

  public ButtonObject[] getExtendBtns()
  {
    return null;
  }

  public void onExtendBtnsClick(ButtonObject bo)
  {
  }

  public void setExtendBtnsStat(int iState)
  {
  }

  public ButtonObject[] getBillButtons()
  {
    ButtonObject[] buttons = super.getBillButtons();
    ButtonObject[] extraButtons = getExtendBtns();
    if ((extraButtons != null) && (extraButtons.length > 0)) {
      int buttonSize = buttons.length;
      int extraSize = extraButtons.length;
      int size = buttonSize + extraSize;

      ButtonObject[] allButtons = new ButtonObject[size];
      System.arraycopy(this.aryButtonGroup, 0, allButtons, 0, buttonSize);
      System.arraycopy(extraButtons, 0, allButtons, buttonSize, extraSize);
      buttons = allButtons;
    }
    return buttons;
  }
}