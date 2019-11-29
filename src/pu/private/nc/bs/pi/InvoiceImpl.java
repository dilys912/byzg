package nc.bs.pi;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.po.OrderImpl;
import nc.bs.ps.settle.SettleImpl;
import nc.bs.ps.vmi.VMIImpl;
import nc.bs.pu.pub.GetSysBillCode;
import nc.bs.pu.pub.PubDMO;
import nc.bs.pu.pub.PubImpl;
import nc.bs.pub.para.SysInitDMO;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.scm.datapower.ScmDps;
import nc.bs.scm.pub.BillRowNoDMO;
import nc.bs.scm.pub.TempTableDMO;
import nc.itf.arap.pub.IArapForGYLPublic;
import nc.itf.ic.service.IICToPU_Ic2puDMO;
import nc.itf.ic.service.IICToPU_VmiSumDMO;
import nc.itf.pi.IInvoice;
import nc.itf.pu.inter.IPuToIc_InvoiceImpl;
import nc.itf.pu.pub.fw.LockTool;
import nc.itf.scm.pub.bill.IScm;
import nc.itf.uap.busibean.ISysInitQry;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pi.InvoiceHeaderVO;
import nc.vo.pi.InvoiceItemVO;
import nc.vo.pi.InvoicePubVO;
import nc.vo.pi.InvoiceVO;
import nc.vo.pi.NormalCondVO;
import nc.vo.pi.RelatedTableVO;
import nc.vo.po.rewrite.ParaPiToPoRewriteVO;
import nc.vo.ps.estimate.GeneralBb3VO;
import nc.vo.ps.settle.IAdjuestVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.para.SysInitVO;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.util.StringUtil;
import nc.vo.sc.order.OrderVO;
import nc.vo.scm.pu.BillStatus;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.Timer;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.puic.ParaPoToIcLendRewriteVO;

public class InvoiceImpl
  implements IInvoice, IPuToIc_InvoiceImpl
{
  public InvoiceVO findByPrimaryKey(String key)
    throws BusinessException
  {
    InvoiceVO invoice = null;
    try {
      InvoiceDMO dmo = new InvoiceDMO();
      invoice = dmo.findByPrimaryKey(key);
    }
    catch (Exception e)
    {
    }

    return invoice;
  }

  public String insert(InvoiceVO invoice)
    throws BusinessException
  {
    String key = null;
    try {
      InvoiceDMO dmo = new InvoiceDMO();
      key = dmo.insert(invoice);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException("nc.bs.piInvocieImpl.insert(InvoiceVO)", e);
    }
    return key;
  }

  public void delete(InvoiceVO vo)
    throws BusinessException
  {
    try
    {
      InvoiceDMO dmo = new InvoiceDMO();
      dmo.delete(vo);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException("nc.bs.piInvocieImpl.delete(InvoiceVO)", e);
    }
  }

  public void update(InvoiceVO invoice)
    throws BusinessException
  {
    try
    {
      InvoiceDMO dmo = new InvoiceDMO();
      dmo.update(invoice);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException("nc.bs.piInvocieImpl.update(InvoiceVO)", e);
    }
  }

  public void queryIfExecVMI(String[] saHid)
    throws BusinessException
  {
    if (saHid.length == 0) {
      return;
    }
    boolean isAlreadyGen = false;
    try
    {
      Vector vTemp = new Vector();
      for (int i = 0; i < saHid.length; i++) {
        if (vTemp.contains(saHid[i])) continue; vTemp.addElement(saHid[i]);
      }
      String[] sTemp = new String[vTemp.size()];
      vTemp.copyInto(sTemp);

      TempTableDMO dmoTempTbl = new TempTableDMO();
      String strSetId = dmoTempTbl.insertTempTable(sTemp, "t_pu_id_in_23", "pk_pu");

      if ((strSetId == null) || (strSetId.trim().equals("()"))) {
        strSetId = " ('ErrorPk') ";
      }

      InvoiceDMO dmo = new InvoiceDMO();
      isAlreadyGen = dmo.queryIfExecVMI(strSetId);
      if (isAlreadyGen) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000235"));
      }
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException("nc.bs.piInvocieImpl.queryIfExecVMI(String[])", e);
    }
  }

  public void generateInvoicesByPush(GeneralBillVO[] VOs)
    throws BusinessException
  {
    try
    {
      InvoiceVO[] invVOs = (InvoiceVO[])(InvoiceVO[])PfUtilTools.runChangeDataAry(VOs[0].getHeaderVO().getCbilltypecode(), "25", VOs);
      if ((invVOs == null) || (invVOs.length == 0)) return;

      ISysInitQry initDMO = (ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName());
      SysInitVO initVO = initDMO.queryByParaCode(VOs[0].getHeaderVO().getPk_corp(), "BD301");
      String cCurrencyTypeID = null;
      if (initVO != null) cCurrencyTypeID = initVO.getPkvalue();

      for (int i = 0; i < invVOs.length; i++)
      {
        invVOs[i].setSource(2);

        invVOs[i].getHeadVO().setDr(new Integer(0));

        invVOs[i].getHeadVO().setDinvoicedate(VOs[0].getHeaderVO().getDaccountdate());
        invVOs[i].getHeadVO().setDarrivedate(VOs[0].getHeaderVO().getDaccountdate());
        invVOs[i].getHeadVO().setIbillstatus(new Integer(0));
        invVOs[i].getHeadVO().setFinitflag(new Integer(0));
        invVOs[i].getHeadVO().setIinvoicetype(new Integer(0));
        invVOs[i].getHeadVO().setIdiscounttaxtype(new Integer(1));

        invVOs[i].getHeadVO().setCoperator(VOs[0].getHeaderVO().getCregister());

        invVOs[i].getHeadVO().setNtaxrate(null);

        InvoiceItemVO[] voaInvItem = invVOs[i].getBodyVO();
        GeneralBillItemVO[] itemVO = VOs[i].getItemVOs();
        for (int j = 0; j < voaInvItem.length; j++)
        {
          voaInvItem[j].setIdiscounttaxtype(new Integer(1));

          voaInvItem[j].setDr(new Integer(0));

          voaInvItem[j].setNexchangeotobrate(null);
          voaInvItem[j].setNexchangeotoarate(null);

          voaInvItem[j].setNinvoicenum(itemVO[j].getNinnum());
          voaInvItem[j].setNoriginalcurprice(itemVO[j].getNprice());
          voaInvItem[j].setNorgnettaxprice(itemVO[j].getNprice());

          if ((voaInvItem[j].getNinvoicenum() != null) && (voaInvItem[j].getNoriginalcurprice() != null)) {
            voaInvItem[j].setNmoney(voaInvItem[j].getNinvoicenum().multiply(voaInvItem[j].getNoriginalcurprice()));
            voaInvItem[j].setNoriginalcurmny(voaInvItem[j].getNmoney());
          }
          voaInvItem[j].setNsummny(voaInvItem[j].getNmoney());
          voaInvItem[j].setNoriginalsummny(voaInvItem[j].getNmoney());

          voaInvItem[j].setNaccumsettmny(new UFDouble(0));
          voaInvItem[j].setNaccumsettnum(new UFDouble(0));

          voaInvItem[j].setCcurrencytypeid(cCurrencyTypeID);
        }
      }
      BillRowNoDMO.setVOsRowNoByRule(invVOs, "25", "crowno");

      ArrayList paraList = new ArrayList();
      paraList.add(null);
      paraList.add(null);
      if (paraList.size() != 3)
      {
        ArrayList forSettleList = new ArrayList();
        forSettleList.add(VOs[0].getHeaderVO().getDaccountdate());
        forSettleList.add(new UFBoolean(false));
        forSettleList.add(new UFBoolean(false));

        paraList.add(forSettleList);
      }

      paraList.add(new Integer(0));
      paraList.add("cvendormangid");

      for (int i = 0; i < invVOs.length; i++) {
        saveInvoice(invVOs[i], paraList);
      }
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException("nc.bs.piInvocieImpl.generateInvoicesByPush(GeneralBillVO)", e);
    }
  }

  private String getInvQueFromWhere(NormalCondVO[] normalVOs, ConditionVO[] definedVOs)
    throws BusinessException
  {
    RelatedTableVO tableVO = new RelatedTableVO("发票");
    String condStr = "po_invoice.dr=0 AND po_invoice_b.dr=0";

    String sAuditStatusWhere = "";

    String pk_corp = null;

    for (int i = 0; i < normalVOs.length; i++)
    {
      if (normalVOs[i].getKey().equals("公司")) {
        pk_corp = (String)normalVOs[i].getValue();
        condStr = condStr + " AND " + normalVOs[i].getWhereStr("po_invoice.pk_corp", "=", NormalCondVO.STRING);
      }
      else if (normalVOs[i].getKey().equals("业务类型")) {
        condStr = condStr + " AND " + normalVOs[i].getWhereStr("po_invoice.cbiztype", "=", NormalCondVO.STRING);
      } else if (normalVOs[i].getKey().equals("单据ID")) {
        condStr = condStr + " AND (po_invoice.cinvoiceid = '" + normalVOs[i].getValue() + "')";
      }
      else if (normalVOs[i].getKey().equals("期初")) {
        if (normalVOs[i].getValue().equals("是"))
          condStr = condStr + " AND po_invoice.finitflag=1";
        else if (normalVOs[i].getValue().equals("否"))
          condStr = condStr + " AND po_invoice.finitflag=0";
      } else if (normalVOs[i].getKey().equals("审批"))
      {
        if (normalVOs[i].getValue().equals("是"))
          condStr = condStr + " AND (po_invoice.ibillstatus=3)";
        else if (normalVOs[i].getValue().equals("否")) {
          condStr = condStr + " AND (po_invoice.ibillstatus IN (0,2,4))";
        }
      }
      else if (normalVOs[i].getKey().equals("未审批")) {
        sAuditStatusWhere = sAuditStatusWhere + (sAuditStatusWhere.length() == 0 ? BillStatus.FREE.toString() : new StringBuilder().append(",").append(BillStatus.FREE.toString()).toString());
      } else if (normalVOs[i].getKey().equals("正在审批")) {
        sAuditStatusWhere = sAuditStatusWhere + (sAuditStatusWhere.length() == 0 ? BillStatus.AUDITING.toString() : new StringBuilder().append(",").append(BillStatus.AUDITING.toString()).toString());
      } else if ((normalVOs[i].getKey().equals("审批未通过")) && (normalVOs[i].getValue().equals("是"))) {
        sAuditStatusWhere = sAuditStatusWhere + (sAuditStatusWhere.length() == 0 ? BillStatus.AUDITFAIL.toString() : new StringBuilder().append(",").append(BillStatus.AUDITFAIL.toString()).toString());
      }
      else if (normalVOs[i].getKey().equals("费用")) {
        if (normalVOs[i].getValue().equals("是")) {
          tableVO.addElement("存货基本档案");
          condStr = condStr + " AND bd_invbasdoc.laborflag='Y'";
        } else if (normalVOs[i].getValue().equals("否")) {
          tableVO.addElement("存货基本档案");
          condStr = condStr + " AND bd_invbasdoc.laborflag<>'Y'";
        }
      } else if (normalVOs[i].getKey().equals("含虚拟发票"))
      {
        if (normalVOs[i].getValue().equals("否"))
          condStr = condStr + " AND po_invoice.iinvoicetype<>3";
      } else if (normalVOs[i].getKey().equals("单据ID"))
      {
        condStr = condStr + " AND po_invoice.cinvoiceid='" + normalVOs[i].getValue() + "'"; } else {
        if (!normalVOs[i].getKey().equals("来源单据"))
          continue;
        condStr = condStr + " AND " + normalVOs[i].getValue();
      }
    }

    if (sAuditStatusWhere.trim().length() > 1) {
      condStr = condStr + " AND (po_invoice.ibillstatus IN (" + sAuditStatusWhere + "))";
    }

    definedVOs = dealInvoiceType(definedVOs);

    definedVOs = dealAreaForVendor(definedVOs, pk_corp);

    definedVOs = dealContainRelation(definedVOs);

    String sSQL = "";
    if ((definedVOs != null) && (definedVOs.length != 0))
    {
      for (int i = 0; i < definedVOs.length; i++) {
        String fieldCode = definedVOs[i].getFieldCode();

        if (definedVOs[i].getOperaCode().equalsIgnoreCase("in"))
        {
          if (InvoicePubVO._Hash_InvoiceUI.get(fieldCode) != null) {
            definedVOs[i].setFieldCode((String)InvoicePubVO._Hash_InvoiceUI.get(fieldCode));
          }
          fieldCode = definedVOs[i].getFieldCode();

          if ((fieldCode.equals("po_invoice_b.cwarehouseid")) || (fieldCode.equals("po_invoice.cpayunit")) || (fieldCode.equals("po_invoice_b.cusedeptid")) || (fieldCode.equals("po_invoice_b.cprojectid")) || (fieldCode.equals("po_invoice_b.cwarehouseid")) || (fieldCode.equals("po_invoice.cstoreorganization")))
          {
            definedVOs[i].setOperaCode(" is null or " + fieldCode + " = '' or " + fieldCode + " in ");
            definedVOs[i].setDataType(1);
          }
          sSQL = definedVOs[i].getSQLStr();
          if ((sSQL != null) && (!sSQL.trim().equals(""))) {
            sSQL = StringUtil.replaceAllString(sSQL, "custcode", "m.pk_cumandoc");
            sSQL = StringUtil.replaceAllString(sSQL, "psncode", "bd_psndoc.pk_psndoc");
            sSQL = StringUtil.replaceAllString(sSQL, "deptcode", "bd_deptdoc.pk_deptdoc");
            sSQL = StringUtil.replaceAllString(sSQL, "bodycode", "bd_calbody.pk_calbody");
            sSQL = StringUtil.replaceAllString(sSQL, "storcode", "pk_stordoc");
            sSQL = StringUtil.replaceAllString(sSQL, " or (bd_areacl.pk_areacl in ( (select areaclcode", " or (bd_areacl.pk_areacl in ( (select pk_areacl");
            condStr = condStr + sSQL;
          }

          int index = fieldCode.indexOf(".", 1);
          String table = null;
          if (index > 0) {
            table = fieldCode.substring(0, index);
            if (((!table.equals("po_invoice")) && (!table.equals("po_invoice_b"))) || (!fieldCode.equals("iprintcount"))) {
              String describtion = RelatedTableVO.getJoinTableDescription(table);
              if (describtion != null)
                tableVO.addElement(describtion);
            }
          }
        }
        else
        {
          int index = fieldCode.indexOf(".", 1);
          String table = null;
          if (index > 0) {
            table = fieldCode.substring(0, index);
            if (((!table.equals("po_invoice")) && (!table.equals("po_invoice_b"))) || (!fieldCode.equals("iprintcount"))) {
              String describtion = RelatedTableVO.getJoinTableDescription(table);
              if (describtion != null)
                tableVO.addElement(describtion);
            }
          }
          if ((index < 0) && (fieldCode.equals("iprintcount")))
            condStr = condStr + " AND po_invoice.iprintcount  " + definedVOs[i].getOperaCode() + definedVOs[i].getValue();
          else {
            condStr = condStr + definedVOs[i].getSQLStr();
          }

        }

      }

    }

    condStr = "FROM " + StringUtil.replaceAllString(tableVO.getFromTable(), "INNER", "LEFT") + " WHERE " + condStr;

    return condStr;
  }

  private ConditionVO[] dealAreaForVendor(ConditionVO[] cons, String pk_corp)
    throws BusinessException
  {
    ConditionVO[] vos = null;
    try {
      Vector v = new Vector();
      for (int i = 0; (cons != null) && (i < cons.length); i++)
      {
        if ((cons[i].getOperaCode().equalsIgnoreCase("in")) || (!cons[i].getFieldCode().equals("bd_areacl.areaclcode"))) {
          v.add(cons[i]);
        } else {
          PubDMO dmo = new PubDMO();
          String[] areaCodes = dmo.getSubAreaCodes(pk_corp, cons[i].getValue(), cons[i].getOperaCode());
          for (int j = 0; j < areaCodes.length; j++)
          {
            ConditionVO con = (ConditionVO)cons[i].clone();
            if (j == 0)
              con.setNoLeft(false);
            else {
              con.setNoLeft(true);
            }
            con.setOperaCode("=");
            con.setValue(areaCodes[j]);

            if (j > 0) {
              con.setLogic(false);
            }
            v.add(con);
          }
        }
      }

      if (v.size() > 0) {
        vos = new ConditionVO[v.size()];
        v.copyInto(vos);
      }
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000070"), e);
    }
    return vos;
  }

  private ConditionVO[] dealContainRelation(ConditionVO[] cons)
  {
    for (int i = 0; (cons != null) && (i < cons.length); i++)
    {
      if ((!cons[i].getOperaCode().equalsIgnoreCase("like")) || 
        (cons[i].getValue().startsWith("%"))) continue;
      cons[i].setValue("%" + cons[i].getValue());
    }

    return cons;
  }

  private ConditionVO[] dealInvoiceType(ConditionVO[] cons)
  {
    for (int i = 0; (cons != null) && (i < cons.length); i++)
    {
      if (cons[i].getFieldCode().equals("po_invoice.iinvoicetype")) {
        cons[i].setDataType(1);
        if (cons[i].getValue().equals("国内专用")) {
          cons[i].setValue("0");
        }
        if (cons[i].getValue().equals("国内普通")) {
          cons[i].setValue("1");
        }
        if (cons[i].getValue().equals("国外专用")) {
          cons[i].setValue("2");
        }
        if (cons[i].getValue().equals("虚拟")) {
          cons[i].setValue("3");
        }
      }
    }
    return cons;
  }

  public void discardInvoiceArray(InvoiceVO[] voaInvoice, Object[] oaParaList)
    throws BusinessException
  {
    int iLen = voaInvoice.length;
    String[] sCinvoiceId = new String[iLen];
    for (int i = 0; i < iLen; i++) {
      sCinvoiceId[i] = voaInvoice[i].getHeadVO().getCinvoiceid();
    }

    InvoiceVO writeBackVo = new InvoiceVO();
    try
    {
      IICToPU_VmiSumDMO oInstance = (IICToPU_VmiSumDMO)NCLocator.getInstance().lookup(IICToPU_VmiSumDMO.class.getName());
      InvoiceDMO dmoInvoice = new InvoiceDMO();

      dmoInvoice.deleteHIdArray(sCinvoiceId);

      HashMap hRet = dmoInvoice.findItemsByPrimaryKeys(sCinvoiceId);

      for (int i = 0; i < iLen; i++)
      {
        InvoiceVO invVO = voaInvoice[i];

        InvoiceHeaderVO headVO = invVO.getHeadVO();

        headVO.setDr(new Integer(1));

        InvoiceItemVO[] items = (InvoiceItemVO[])(InvoiceItemVO[])hRet.get(headVO.getCinvoiceid());
        for (int j = 0; j < items.length; j++) {
          items[j].setStatus(3);
        }

        String sourcebilltype = items[0].getCsourcebilltype();
        if ((sourcebilltype != null) && (sourcebilltype.equalsIgnoreCase("50"))) {
          Object[][] rowinfo = (Object[][])null;
          rowinfo = new Object[items.length][2];
          int k = 0; for (int loopk = items.length; k < loopk; k++) {
            rowinfo[k][0] = items[k].getCsourcebillid();
            rowinfo[k][1] = items[k].getNinvoicenum().multiply(-1.0D);
          }

          oInstance.writeBackTotalInvoiceNum(rowinfo);
        }

        writeBackVo.setChildrenVO(items);
        writeBackVo.setParentVO(headVO);
        writeBackVo.setUserConfirmFlag(voaInvoice[i].getUserConfirmFlag());
        writeBackBill(writeBackVo);
      }

      dmoInvoice.deleteBIdArrayForHIdArray(sCinvoiceId);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException("nc.bs.piInvocieImpl.discardInvoiceArray(InvoiceVO[] , Object[] )", e);
    }
  }

  public void discardInvoiceForSettle(String[] saInvoiceId)
    throws BusinessException
  {
    if ((saInvoiceId == null) || (saInvoiceId.length == 0)) {
      SCMEnv.out("nc.bs.pi.InvoiceBO.discardInvoiceForSettle(String [])传入参数为空，不正确！");
      return;
    }
    int iLen = saInvoiceId.length;
    for (int i = 0; i < iLen; i++) {
      if ((saInvoiceId[i] == null) || (saInvoiceId[i].trim().length() == 0)) {
        SCMEnv.out("nc.bs.pi.InvoiceBO.discardInvoiceForSettle(String [])传入参数数组中有为空的元素！");
        return;
      }
    }

    try
    {
      for (int i = 0; i < iLen; i++) {
        isASettledInvoiceCouldBeDiscarded(saInvoiceId[i]);
      }
      InvoiceDMO dmoInvoice = new InvoiceDMO();

      dmoInvoice.deleteHIdArray(saInvoiceId);
      dmoInvoice.deleteBIdArrayForHIdArray(saInvoiceId);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException("nc.bs.piInvocieImpl.discardInvoiceForSettle(String[])", e);
    }
  }

  public void discardVirtualInvoiceForSettle(String[] saInvoiceId)
    throws BusinessException
  {
    discardInvoiceForSettle(saInvoiceId);
  }

  private ArrayList doSettle(InvoiceVO vo, int nSource, ArrayList paraList, String sSettleRule)
    throws BusinessException
  {
    Timer timer = new Timer();
    timer.start("采购发票自动结算 doSettle 操作开始");

    String sMethodName = "nc.bs.pi.InvoiceBO.doSettle(InvoiceVO, int, ArrayList)";

    InvoiceItemVO[] items = vo.getBodyVO();
    ArrayList arrRet = null;
    InvoiceVO settleVO = new InvoiceVO();
    settleVO.setParentVO(vo.getParentVO());

    boolean isFromOrder = false;
    for (int i = 0; i < items.length; i++) {
      if ((items[i].getCupsourcebilltype() == null) || ((!items[i].getCupsourcebilltype().equals("21")) && (!items[i].getCupsourcebilltype().equals("61")))) {
        continue;
      }
      isFromOrder = true;
      break;
    }

    try
    {
      PubDMO dmoPuPub = new PubDMO();

      int rowSize = items.length;
      String[] sStoreId = new String[rowSize];
      ArrayList arrVO = new ArrayList();
      InvoiceItemVO[] tempItems = null;
      for (int i = 0; i < rowSize; i++) {
        sStoreId[i] = items[i].getCwarehouseid();
        if (sStoreId[i] == null)
          sStoreId[i] = " ";
      }
      Object[][] oIsCostCal = dmoPuPub.queryArrayValue("bd_stordoc", "pk_stordoc", new String[] { "iscalculatedinvcost" }, sStoreId, null);

      if ((oIsCostCal != null) && (oIsCostCal.length > 0)) {
        for (int i = 0; i < rowSize; i++) {
          if ((oIsCostCal[i] != null) && (oIsCostCal[i][0] != null) && (oIsCostCal[i][0].toString().equals("N"))) {
            continue;
          }
          arrVO.add(items[i]);
        }
        if (arrVO.size() == 0) {
          ArrayList list = new ArrayList();
          list.add(null);
          list.add(new UFBoolean(false));
          return list;
        }
        tempItems = new InvoiceItemVO[arrVO.size()];
        arrVO.toArray(tempItems);

        settleVO.setChildrenVO(tempItems);
      }
      else {
        settleVO.setChildrenVO(items);
      }

      timer.addExecutePhase("获取核算规则");

      if (isFromOrder) {
        arrRet = settleForOrder(settleVO, sSettleRule, paraList);
        timer.addExecutePhase("settleForOrder method");
      }
      else if (nSource == 2) {
        arrRet = settleForSto(settleVO, paraList);
        timer.addExecutePhase("settleForSto method");
      } else if (nSource == 3) {
        arrRet = settleForVMI(settleVO, paraList);
        timer.addExecutePhase("settleForSto method");
      }

      String[] sCinvoice_bId = new String[vo.getBodyVO().length];
      for (int i = 0; i < vo.getBodyVO().length; i++) {
        sCinvoice_bId[i] = vo.getBodyVO()[i].getCinvoice_bid();
      }
      Object[][] ob = (Object[][])dmoPuPub.queryArrayValue("po_invoice_b", "cinvoice_bid", new String[] { "naccumsettnum", "naccumsettmny" }, sCinvoice_bId, "po_invoice_b.dr=0");

      for (int i = 0; i < vo.getBodyVO().length; i++) {
        if ((ob == null) || (ob[i] == null)) {
          SCMEnv.out("未找到某发票行ID=" + vo.getBodyVO()[i].getCinvoice_bid() + "累计结算数量或金额时未找到!");
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000071"));
        }
        vo.getBodyVO()[i].setNaccumsettnum(ob[i][0] == null ? null : new UFDouble(ob[i][0].toString()));
        vo.getBodyVO()[i].setNaccumsettmny(ob[i][1] == null ? null : new UFDouble(ob[i][1].toString()));
      }

      timer.showAllExecutePhase("采购发票自动结算 doSettle 操作结束");
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }
    return arrRet;
  }

  public ArrayList doSettleArray(InvoiceVO[] vos, String sDate)
    throws BusinessException
  {
    if ((vos == null) || (vos.length == 0) || (sDate == null)) {
      return null;
    }
    ArrayList arrRet = new ArrayList();
    InvoiceHeaderVO hVO = vos[0].getHeadVO();

    String sSettleParaForGeneral = null;

    String sSettleParaForOrder = null;

    String sSettleParaForVMI = null;

    String pk_corp = null;
    ArrayList arrPara = new ArrayList();
    String temp = null;
    if (sDate.length() > 10)
      temp = sDate.substring(0, 10);
    arrPara.add(new UFDate(temp));
    arrPara.add(new UFBoolean(false));
    arrPara.add(new UFBoolean(true));
    try
    {
      ISysInitQry initDMO = (ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName());
      pk_corp = hVO.getPk_corp();

      sSettleParaForGeneral = initDMO.getParaString(pk_corp, "PO30");
      sSettleParaForOrder = initDMO.getParaString(pk_corp, "PO46");
      sSettleParaForVMI = initDMO.getParaString(pk_corp, "PO77");
      if ((sSettleParaForGeneral == null) || (sSettleParaForGeneral.length() == 0))
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000068"));
      if ((sSettleParaForOrder == null) || (sSettleParaForOrder.length() == 0))
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000069"));
      if ((sSettleParaForVMI == null) || (sSettleParaForVMI.length() == 0)) {
        throw new BusinessException("无法获取参数，不知道消耗汇总转发票时是否要立即结算！");
      }

      String[] cinvoiceid = new String[vos.length];
      for (int i = 0; i < vos.length; i++) {
        cinvoiceid[i] = vos[i].getHeadVO().getCinvoiceid();
      }
      InvoiceDMO dmo = new InvoiceDMO();
      Hashtable h = dmo.queryVerifyRuleAndBillStatus(cinvoiceid);
      if ((h == null) || (h.size() == 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000072"));
      }
      int size = vos.length;
      for (int i = 0; i < size; i++) {
        InvoiceItemVO[] items = vos[i].getBodyVO();
        if ((items == null) || (items.length <= 0)) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000073"));
        }
        String sUpSourceBillType = items[0].getCupsourcebilltype();
        Object oTemp = h.get(vos[i].getHeadVO().getCinvoiceid());
        if (oTemp != null) {
          Object[] data = (Object[])(Object[])oTemp;
          if ((sUpSourceBillType != null) && (sUpSourceBillType.trim().length() > 0) && (vos[i].getHeadVO().getIinvoicetype().intValue() != 3)) {
            String[] sRet = null;
            String verifyrule = (String)data[1];
            Integer billstatus = new Integer(data[0].toString());
            if ((sUpSourceBillType.equals("45")) || (sUpSourceBillType.equals("47")) || (sUpSourceBillType.equals("4T"))) {
              if ((sSettleParaForGeneral.equals("审批时自动结算")) && (!verifyrule.equals("S")) && (!verifyrule.equals("N")) && (billstatus.intValue() == BillStatus.AUDITED.intValue()))
              {
                arrRet = doSettle(vos[i], 2, arrPara, verifyrule);
              }
            } else if (sUpSourceBillType.equals("21")) {
              if ((sSettleParaForOrder.equals("审批时自动结算")) && (!verifyrule.equals("Z")) && (!verifyrule.equals("N")) && (billstatus.intValue() == BillStatus.AUDITED.intValue()))
              {
                arrRet = doSettle(vos[i], 2, arrPara, verifyrule);
              }
            } else if ((sUpSourceBillType.equals("50")) && 
              (sSettleParaForVMI.equals("审批时自动结算")) && (billstatus.intValue() == BillStatus.AUDITED.intValue()))
            {
              arrRet = doSettle(vos[i], 3, arrPara, verifyrule);
            }

            UFBoolean bSucceed = new UFBoolean(false);
            ArrayList arrJS = null;
            if ((arrRet != null) && (arrRet.size() > 0)) {
              bSucceed = (UFBoolean)arrRet.get(1);
              arrJS = (ArrayList)arrRet.get(0);
            }
            if ((arrJS != null) && (arrJS.size() > 0)) {
              sRet = new String[arrJS.size()];
              arrJS.toArray(sRet);
              LockTool.releaseLockForPks(sRet, vos[i].getHeadVO().getCauditpsn());
            }
            if (((sUpSourceBillType.equals("45")) || (sUpSourceBillType.equals("47")) || (sUpSourceBillType.equals("4T"))) && (sSettleParaForGeneral.equals("审批时自动结算")) && (!verifyrule.equals("S")) && (!verifyrule.equals("N")) && (!bSucceed.booleanValue()) && (billstatus.intValue() == BillStatus.AUDITED.intValue()))
            {
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000093"));
            }
            if ((!sUpSourceBillType.equals("50")) || (!sSettleParaForVMI.equals("审批时自动结算")) || (bSucceed.booleanValue()) || (billstatus.intValue() != BillStatus.AUDITED.intValue()))
            {
              continue;
            }
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000093"));
          }
        }
      }
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException("nc.bs.piInvocieImpl.doSettleArray(InvoiceVO[], String)", e);
    }
    return arrRet;
  }

  public InvoiceVO[] filterVirtualInvoice(InvoiceVO[] voaInv)
    throws BusinessException
  {
    if (voaInv == null) {
      SCMEnv.out("nc.bs.pi.InvoiceBO.isVirtual(InvoiceVO)传入参数不正确！");
      throw new BusinessException("nc.bs.pi.InvoiceBO.isVirtual(InvoiceVO)\tException!", new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000074")));
    }

    int iLen = voaInv.length;
    for (int i = 0; i < iLen; i++) {
      if ((voaInv[i] == null) || (voaInv[i].getHeadVO() == null) || (voaInv[i].getHeadVO().getIinvoicetype() == null))
      {
        SCMEnv.out("nc.bs.pi.InvoiceBO.isVirtual(InvoiceVO)传入参数不正确！");
        throw new BusinessException("nc.bs.pi.InvoiceBO.isVirtual(InvoiceVO)\tException!", new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000074")));
      }

      if (voaInv[i].isVirtual())
        voaInv[i] = null;
    }
    return voaInv;
  }

  public void adjustForZGYF(InvoiceVO[] voaInv)
    throws BusinessException
  {
    try
    {
      if ((voaInv == null) || (voaInv.length == 0)) {
        return;
      }
      for (int i = 0; i < voaInv.length; i++) {
        if (voaInv[i] == null) return;

      }

      Hashtable htHeadId = new Hashtable();
      Hashtable htAuditPsnId = new Hashtable();
      Hashtable htAuditDate = new Hashtable();
      InvoiceItemVO[] aInvoiceItemVOs = null;
      InvoiceHeaderVO aInvoiceHeaderVO = null;
      for (int i = 0; i < voaInv.length; i++)
      {
        aInvoiceHeaderVO = (InvoiceHeaderVO)voaInv[i].getParentVO();

        htAuditPsnId.put(aInvoiceHeaderVO.getCinvoiceid(), aInvoiceHeaderVO.getCauditpsn());
        htAuditDate.put(aInvoiceHeaderVO.getCinvoiceid(), aInvoiceHeaderVO.getDauditdate());

        aInvoiceItemVOs = (InvoiceItemVO[])(InvoiceItemVO[])voaInv[i].getChildrenVO();
        if ((aInvoiceItemVOs == null) || (aInvoiceItemVOs.length == 0))
        {
          continue;
        }
        for (int j = 0; j < aInvoiceItemVOs.length; j++) {
          htHeadId.put(aInvoiceItemVOs[j].getCinvoice_bid(), aInvoiceItemVOs[j].getCinvoiceid());
        }

      }

      ICreateCorpQueryService myService0 = null;
      myService0 = (ICreateCorpQueryService)NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());
      String unitCode = ((InvoiceHeaderVO)voaInv[0].getParentVO()).getPk_corp();
      boolean bAPStartUp = myService0.isEnabled(unitCode, "AP");
      if (!bAPStartUp) {
        return;
      }

      IAdjuestVO[] washVO = new InvoiceDMO().washDataForZGYF(voaInv);

      if ((washVO != null) && (washVO.length > 0))
      {
        String[] saGeneralBid = new String[washVO.length];
        for (int i = 0; i < washVO.length; i++) {
          saGeneralBid[i] = washVO[i].getDdhh();
        }

        HashMap htInNum = new PubDMO().queryArrayValues("ic_general_b", "cgeneralbid", new String[] { "ninnum" }, saGeneralBid, "dr=0");

        HashMap htAccumWashNum = new PubDMO().queryArrayValues("ic_general_bb3", "cgeneralbid", new String[] { "naccumwashnum" }, saGeneralBid, "dr=0");

        String strGeneralBid = null;
        String strInvoiceHid = null;
        String strAuditPsnId = null;
        UFDate ufdatAuditDate = null;

        Object objTemp = null;
        Object[] oaTemp = null;
        UFDouble ufdInNum = new UFDouble(0);
        UFDouble ufdAccumWashNum = new UFDouble(0);
        UFBoolean[] ufbLast = new UFBoolean[washVO.length];
        String[] ddhh = new String[washVO.length];
        double[] shl = new double[washVO.length];

        IArapForGYLPublic iArap = (IArapForGYLPublic)NCLocator.getInstance().lookup(IArapForGYLPublic.class.getName());
        InvoiceDMO invoiceDMO = new InvoiceDMO();

        for (int i = 0; i < washVO.length; i++)
        {
          strGeneralBid = washVO[i].getDdhh();
          if ((strGeneralBid == null) || (strGeneralBid.trim().length() == 0)) {
            continue;
          }
          objTemp = htInNum.get(strGeneralBid);
          if (objTemp != null) {
            oaTemp = (Object[])(Object[])objTemp;
            if ((oaTemp.length > 0) && (oaTemp[0] != null)) {
              ufdInNum = new UFDouble(oaTemp[0].toString());
            }

          }

          ufdAccumWashNum = washVO[i].getShl();
          objTemp = htAccumWashNum.get(strGeneralBid);
          if (objTemp != null) {
            oaTemp = (Object[])(Object[])objTemp;
            if ((oaTemp.length > 0) && (oaTemp[0] != null)) {
              ufdAccumWashNum = new UFDouble(oaTemp[0].toString()).add(ufdAccumWashNum);
            }
          }
          htAccumWashNum.put(strGeneralBid, new UFDouble[] { ufdAccumWashNum });

          if (ufdInNum.doubleValue() == ufdAccumWashNum.doubleValue())
            ufbLast[i] = new UFBoolean(true);
          else {
            ufbLast[i] = new UFBoolean(false);
          }

          objTemp = htHeadId.get(washVO[i].getCinvoice_bid());
          if ((objTemp != null) && (objTemp.toString().trim().length() > 0)) {
            strInvoiceHid = objTemp.toString();
          }

          objTemp = htAuditPsnId.get(strInvoiceHid);
          if ((objTemp != null) && (objTemp.toString().trim().length() > 0)) {
            strAuditPsnId = objTemp.toString();
          }

          objTemp = htAuditDate.get(strInvoiceHid);
          if ((objTemp != null) && (objTemp.toString().trim().length() > 0)) {
            ufdatAuditDate = new UFDate(objTemp.toString());
          }

          ddhh[i] = washVO[i].getDdhh();
          shl[i] = washVO[i].getShl().doubleValue();

          washVO[i].setIsdone(ufbLast[i]);
        }

        iArap.Adjuest(washVO, strInvoiceHid, strAuditPsnId, ufdatAuditDate.toString(), unitCode, 1, 1);

        invoiceDMO.updateAccumWashNumForIC(ddhh, shl, true);
      }

    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
  }

  public void unAdjustForZGYF(InvoiceVO[] voaInv)
    throws BusinessException
  {
    try
    {
      if ((voaInv == null) || (voaInv.length == 0)) {
        return;
      }

      Hashtable htHeadId = new Hashtable();

      InvoiceItemVO[] aInvoiceItemVOs = null;
      for (int i = 0; i < voaInv.length; i++)
      {
        aInvoiceItemVOs = (InvoiceItemVO[])(InvoiceItemVO[])voaInv[i].getChildrenVO();
        if ((aInvoiceItemVOs == null) || (aInvoiceItemVOs.length == 0))
        {
          continue;
        }
        for (int j = 0; j < aInvoiceItemVOs.length; j++) {
          htHeadId.put(aInvoiceItemVOs[j].getCinvoice_bid(), aInvoiceItemVOs[j].getCinvoiceid());
        }

      }

      ICreateCorpQueryService myService0 = null;
      myService0 = (ICreateCorpQueryService)NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());
      String unitCode = ((InvoiceHeaderVO)voaInv[0].getParentVO()).getPk_corp();
      boolean bAPStartUp = myService0.isEnabled(unitCode, "AP");
      if (!bAPStartUp) {
        return;
      }

      IAdjuestVO[] washVO = new InvoiceDMO().antiWashDataForZGYF(voaInv);

      if ((washVO != null) && (washVO.length > 0))
      {
        String[] saGeneralBid = new String[washVO.length];
        for (int i = 0; i < washVO.length; i++) {
          saGeneralBid[i] = washVO[i].getDdhh();
        }

        String strInvoiceHid = null;
        Object objTemp = null;

        IArapForGYLPublic iArap = (IArapForGYLPublic)NCLocator.getInstance().lookup(IArapForGYLPublic.class.getName());
        InvoiceDMO invoiceDMO = new InvoiceDMO();

        for (int i = 0; i < washVO.length; i++)
        {
          objTemp = htHeadId.get(washVO[i].getCinvoice_bid());
          if ((objTemp != null) && (objTemp.toString().trim().length() != 0)) {
            strInvoiceHid = objTemp.toString();

            iArap.unAdjuest(strInvoiceHid, unitCode);

            invoiceDMO.updateAccumWashNumForIC(new String[] { washVO[i].getDdhh() }, new double[] { washVO[i].getShl().doubleValue() }, false);
          }

        }

      }

    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
  }

  public InvoiceVO[] filterVMIInvoice(InvoiceVO[] voaInv)
    throws BusinessException
  {
    if (voaInv == null) {
      SCMEnv.out("nc.bs.pi.InvoiceBO.filterVMIInvoice(InvoiceVO [])传入参数不正确！");
      throw new BusinessException("nc.bs.pi.InvoiceBO.filterVMIInvoice(InvoiceVO [])\tException!", new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000075")));
    }

    int iLen = voaInv.length;
    String[] saBizType = new String[iLen];
    for (int i = 0; i < iLen; i++) {
      if ((voaInv[i] == null) || (voaInv[i].getHeadVO() == null) || (voaInv[i].getHeadVO().getCbiztype() == null)) {
        SCMEnv.out("nc.bs.pi.InvoiceBO.filterVMIInvoice(InvoiceVO [])传入参数不正确！");
        throw new BusinessException("nc.bs.pi.InvoiceBO.filterVMIInvoice(InvoiceVO [])\tException!", new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000075")));
      }
      saBizType[i] = voaInv[i].getHeadVO().getCbiztype();
    }

    Object[][] oa2Ret = (Object[][])null;
    try {
      oa2Ret = new PubDMO().queryArrayValue("bd_busitype", "pk_busitype", new String[] { "verifyrule" }, saBizType);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.filterVMIInvoice(InvoiceVO [])\tException!", e);
    }

    if (oa2Ret == null) {
      throw new BusinessException("nc.bs.pi.InvoiceBO.filterVMIInvoice(InvoiceVO [])\tException!", new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000076")));
    }

    for (int i = 0; i < iLen; i++) {
      if ((oa2Ret[i] == null) || (oa2Ret[i][0] == null)) {
        throw new BusinessException("nc.bs.pi.InvoiceBO.filterVMIInvoice(InvoiceVO [])\tException!", new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000076")));
      }

      if (oa2Ret[i][0].toString().equalsIgnoreCase("V")) {
        voaInv[i] = null;
      }
    }

    return voaInv;
  }

  private String getAuditPsnCondition(String auditPsn, boolean bAudited, String pk_corp)
  {
    if (auditPsn == null) {
      return "";
    }

    String con = " ";

    if (!bAudited)
    {
      con = con + "AND (";
      con = con + "po_invoice.cinvoiceid IN ";
      con = con + "(SELECT billid FROM pub_workflownote ";
      con = con + "where receivedeleteflag = 'N' and ischeck = 'N' ";
      con = con + "and checkman = '" + auditPsn + "') ";

      con = con + " OR (po_invoice.cinvoiceid not in ";

      con = con + "(SELECT billid FROM pub_workflownote, po_invoice where billid = po_invoice.cinvoiceid) ";
      con = con + "\tAND po_invoice.pk_corp='" + pk_corp + "')";
      con = con + " ) ";
    }
    else
    {
      con = con + "AND (";
      con = con + "po_invoice.cinvoiceid IN ";
      con = con + "(SELECT billid FROM pub_workflownote ";
      con = con + "where receivedeleteflag = 'N' and ischeck = 'Y' ";
      con = con + "and checkman = '" + auditPsn + "') ";

      con = con + "OR (po_invoice.cinvoiceid NOT IN ";
      con = con + "(SELECT billid FROM pub_workflownote, po_invoice where billid = po_invoice.cinvoiceid) ";
      con = con + "\tAND po_invoice.pk_corp='" + pk_corp + "')";
      con = con + ") ";
    }

    return con;
  }

  private OrderImpl getBean_Order()
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.getBean_Order()";
    OrderImpl bean = null;
    try {
      bean = new OrderImpl();
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(sMethodName, e);
    }
    return bean;
  }

  public UFDouble[] getPlanPricesForPr(String[] sBaseIds, String sStoreOrgId, String sCorpId)
    throws BusinessException
  {
    if ((sBaseIds == null) || (sBaseIds.length == 0) || (sCorpId == null)) {
      return null;
    }

    UFDouble[] uPlanPrice = null;

    HashMap hResult = new HashMap();
    try {
      int size = sBaseIds.length;
      uPlanPrice = new UFDouble[size];
      InvoiceDMO dmo = new InvoiceDMO();

      if ((sStoreOrgId == null) || (sStoreOrgId.trim().length() == 0))
        hResult = dmo.getPlanPricesFrmInvMan(sBaseIds, sCorpId);
      else
        hResult = dmo.getPlanPricesForPr(sBaseIds, sStoreOrgId, sCorpId);
      if ((hResult == null) || (hResult.size() == 0))
        return null;
      for (int i = 0; i < size; i++) {
        if (sBaseIds[i] == null)
          continue;
        uPlanPrice[i] = ((UFDouble)hResult.get(sBaseIds[i]));
      }
    }
    catch (Exception e) {
      PubDMO.throwBusinessException("nc.bs.pr.pray.PraybillImpl.getPlanPricesForPr(String [], String, String)", e);
    }
    return uPlanPrice;
  }

  private UFBoolean isASettledInvoiceCouldBeDiscarded(String sInvoiceId)
    throws BusinessException
  {
    if ((sInvoiceId == null) || (sInvoiceId.trim().length() == 0)) {
      return new UFBoolean(false);
    }
    try
    {
      PubDMO dmoPuPub = new PubDMO();
      Object[][] ob = dmoPuPub.queryResultsFromAnyTable("po_invoice", new String[] { "dr", "ibillstatus" }, "cinvoiceid='" + sInvoiceId + "'");
      if (ob == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000077"));
      }
      int dr = new Integer(ob[0][0].toString()).intValue();
      int status = new Integer(ob[0][1].toString()).intValue();
      if (dr != BillStatus.FREE.intValue()) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000078"));
      }
      if (status == BillStatus.AUDITING.intValue()) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000079"));
      }
      if (status == BillStatus.AUDITED.intValue()) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000080"));
      }
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.isAInvoiceCouldBeDiscarded(InvoiceVO) Exception!", e);
    }
    return new UFBoolean(true);
  }

  public String queryDiffModule(String pk_corp)
    throws BusinessException
  {
    String strPara = null;
    try {
      ISysInitQry initDMO = (ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName());
      strPara = initDMO.getParaString(pk_corp, "PO13");
    }
    catch (Exception e) {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.getDiffModule(String)\tException!", new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000083")));
    }

    return strPara;
  }

  public String queryEstimateModule(String pk_corp)
    throws BusinessException
  {
    String strPara = null;
    try {
      ISysInitQry initDMO = (ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName());
      strPara = initDMO.getParaString(pk_corp, "PO12");
    }
    catch (Exception e) {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.getEstimateModule(String)\tException!", new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000084")));
    }

    return strPara;
  }

  public ArrayList queryForSaveAudit(String key)
    throws BusinessException
  {
    ArrayList arr = new ArrayList();
    try {
      InvoiceDMO dmo = new InvoiceDMO();
      arr = dmo.queryForSaveAudit(key);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.queryForSaveAudit(String)!", e);
    }
    return arr;
  }

  public Object[] queryGenenelVOsByFromWhere(String strBillType, String strFromWhere, ConditionVO[] powerVOs)
    throws BusinessException
  {
    Object[] ob = null;
    String sOperator = null;
    String ss = null;
    String corp = null;
    try {
      InvoiceDMO dmo = new InvoiceDMO();

      if (powerVOs[(powerVOs.length - 2)].getFieldName().equals("操作员")) {
        sOperator = powerVOs[(powerVOs.length - 2)].getValue();
      }
      if (powerVOs[(powerVOs.length - 1)].getFieldName().equals("公司")) {
        corp = powerVOs[(powerVOs.length - 1)].getValue();
      }
      if (strBillType.equals("47")) {
        ss = ScmDps.getSubSql("ic_general_h", "ic_general_h", sOperator, new String[] { corp });
        if ((ss != null) && (ss.trim().length() > 0)) strFromWhere = strFromWhere + " and " + ss + " ";
        ss = ScmDps.getSubSql("ic_general_b", "ic_general_b", sOperator, new String[] { corp });
        if ((ss != null) && (ss.trim().length() > 0)) strFromWhere = strFromWhere + " and " + ss + " ";
        ss = ScmDps.getSubSql("sc_order", "sc_order", sOperator, new String[] { corp });
        if ((ss != null) && (ss.trim().length() > 0)) strFromWhere = strFromWhere + " and " + ss + " ";
        ss = ScmDps.getSubSql("sc_order_b", "sc_order_b", sOperator, new String[] { corp });
        if ((ss != null) && (ss.trim().length() > 0)) strFromWhere = strFromWhere + " and " + ss + " ";
        ob = dmo.findGenenelVOsFromScOrderByCondsMy(strFromWhere);
      } else {
        ss = ScmDps.getSubSql("ic_general_h", "ic_general_h", sOperator, new String[] { corp });
        if ((ss != null) && (ss.trim().length() > 0)) strFromWhere = strFromWhere + " and " + ss + " ";
        ss = ScmDps.getSubSql("ic_general_b", "ic_general_b", sOperator, new String[] { corp });
        if ((ss != null) && (ss.trim().length() > 0)) strFromWhere = strFromWhere + " and " + ss + " ";
        ss = ScmDps.getSubSql("po_order", "po_order", sOperator, new String[] { corp });
        if ((ss != null) && (ss.trim().length() > 0)) strFromWhere = strFromWhere + " and " + ss + " ";
        ss = ScmDps.getSubSql("po_order_b", "po_order_b", sOperator, new String[] { corp });
        if ((ss != null) && (ss.trim().length() > 0)) strFromWhere = strFromWhere + " and " + ss + " ";
        ob = dmo.findGenenelVOsFromPoOrderByCondsMy(strFromWhere);
      }
    }
    catch (Exception e) {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.findInvFrmStoVOsAndGeneralVOsMy(ConditionVO []) Exception!", e);
    }

    return ob;
  }

  public Object queryNaccumsettnum(String bllid_b)
    throws BusinessException
  {
    Object ob = null;
    try {
      InvoiceDMO dmo = new InvoiceDMO();

      ob = dmo.findNaccumsettnumForScOrder(bllid_b);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.findInvFrmStoVOsAndGeneralVOsMy(ConditionVO []) Exception!", e);
    }

    return ob;
  }

  public InvoiceVO[] queryInvoiceVOsByCondsMy(NormalCondVO[] normalVOs, ConditionVO[] definedVOs)
    throws BusinessException
  {
    if ((definedVOs != null) && (definedVOs.length > 0)) {
      for (int i = 0; i < definedVOs.length; i++) {
        if ((!definedVOs[i].getOperaCode().equalsIgnoreCase("in")) || (InvoicePubVO._Hash_InvoiceUI.get(definedVOs[i].getFieldCode()) == null)) {
          continue;
        }
        definedVOs[i].setFieldCode((String)InvoicePubVO._Hash_InvoiceUI.get(definedVOs[i].getFieldCode()));
      }
    }

    String fromWhere = getInvQueFromWhere(normalVOs, definedVOs);

    InvoiceVO[] retVOs = null;
    try {
      InvoiceDMO dmo = new InvoiceDMO();
      retVOs = dmo.findInvoiceVOsByNoneItems(fromWhere);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.findInvoiceVOsByNoneItems(ConditionVO [], String, String, String, String, String [], String []) Exception!", e);
    }
    return retVOs;
  }

  public InvoiceVO[] queryVoByHid(String billid)
    throws BusinessException
  {
    String fromWhere = " from po_invoice inner join po_invoice_b on po_invoice.dr =0 and po_invoice_b.dr =0 and po_invoice.cinvoiceid = po_invoice_b.cinvoiceid and po_invoice.cinvoiceid = '" + billid + "' ";

    InvoiceVO[] retVOs = null;
    try
    {
      InvoiceDMO dmo = new InvoiceDMO();
      retVOs = dmo.findInvoiceVOsByNoneItems(fromWhere);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.findInvoiceVOsByNoneItems(ConditionVO [], String, String, String, String, String [], String []) Exception!", e);
    }
    return retVOs;
  }

  public InvoiceVO[] queryInvoiceVOsByCondsMy(NormalCondVO[] normalVOs, ConditionVO[] definedVOs, String auditPsn)
    throws BusinessException
  {
    boolean bAudited = false;
    String pk_corp = null; String sOperator = null; String ss1 = null; String ss2 = null;

    for (int i = 0; (normalVOs != null) && (i < normalVOs.length); i++)
    {
      if (normalVOs[i].getKey().equals("公司")) {
        pk_corp = (String)normalVOs[i].getValue();
      }
      else if (normalVOs[i].getKey().equals("审批")) {
        if (!normalVOs[i].getValue().equals("是")) break;
        bAudited = true; break;
      }

    }

    if (definedVOs[(definedVOs.length - 1)].getFieldName().equals("操作员")) {
      sOperator = definedVOs[(definedVOs.length - 1)].getValue();
    }

    if ((definedVOs != null) && (definedVOs.length > 0)) {
      Vector v = new Vector();
      for (int i = 0; i < definedVOs.length - 1; i++) v.addElement(definedVOs[i]);

      definedVOs = new ConditionVO[v.size()];
      v.copyInto(definedVOs);
    }

    String sFromWhere = getInvQueFromWhere(normalVOs, definedVOs);
    sFromWhere = sFromWhere + getAuditPsnCondition(auditPsn, bAudited, pk_corp);

    InvoiceVO[] retVOs = null;
    try {
      ss1 = ScmDps.getSubSql("po_invoice", "po_invoice", sOperator, new String[] { pk_corp });
      if ((ss1 != null) && (ss1.trim().length() > 0)) sFromWhere = sFromWhere + " and " + ss1 + " ";
      ss2 = ScmDps.getSubSql("po_invoice_b", "po_invoice_b", sOperator, new String[] { pk_corp });
      if ((ss2 != null) && (ss2.trim().length() > 0)) sFromWhere = sFromWhere + " and " + ss2 + " ";

      InvoiceDMO dmo = new InvoiceDMO();
      retVOs = dmo.findInvoiceVOsByNoneItems(sFromWhere);
    }
    catch (Exception e)
    {
      throw new BusinessException("nc.bs.pi.InvoiceBO.queryInvoiceVOsByCondsMy(NormalCondVO [], ConditionVO [], String) Exception!", e);
    }
    return retVOs;
  }

  public InvoiceVO[] queryItemsForInvoices(NormalCondVO[] normalVOs, ConditionVO[] definedVOs, InvoiceVO[] vos)
    throws BusinessException
  {
    if ((vos == null) || (vos.length == 0))
      return null;
    Vector tempVOs = new Vector();
    NormalCondVO[] normalTempVOs = new NormalCondVO[normalVOs.length];
    for (int i = 0; i < normalVOs.length; i++)
    {
      if (!normalVOs[i].getKey().equals("公司")) {
        tempVOs.add(normalVOs[i]);
      }
    }
    if ((tempVOs != null) && (tempVOs.size() > 0)) {
      normalTempVOs = new NormalCondVO[tempVOs.size()];
      tempVOs.copyInto(normalTempVOs);
    }

    String bizSQL = getInvQueFromWhere(normalTempVOs, definedVOs);

    String id_ts = " and((po_invoice.cinvoiceid = '" + vos[0].getHeadVO().getPrimaryKey() + "'" + " and po_invoice.ts = '" + vos[0].getHeadVO().getTs() + "')";
    for (int i = 1; i < vos.length; i++) {
      id_ts = id_ts + " or (po_invoice.cinvoiceid = '" + vos[i].getHeadVO().getPrimaryKey() + "'" + " and po_invoice.ts = '" + vos[i].getHeadVO().getTs() + "')";
    }
    id_ts = id_ts + ")";

    InvoiceVO[] invs = null;
    try {
      InvoiceDMO dmo = new InvoiceDMO();
      invs = dmo.findInvoiceVOsByAllItems(bizSQL + id_ts);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException("InvoiceBean::queryItemsForInvoices(InvoicePK) Exception!", e);
    }

    if ((invs == null) || (vos.length != invs.length))
    {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000085"), new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000086")));
    }

    for (int i = 0; i < vos.length; i++) {
      String key1 = vos[i].getHeadVO().getPrimaryKey();
      for (int j = 0; j < invs.length; j++) {
        String key2 = invs[j].getHeadVO().getPrimaryKey();
        if (key1.equals(key2)) {
          vos[i].setChildrenVO(invs[j].getChildrenVO());
          break;
        }
      }
    }
    return vos;
  }

  public UFDouble queryNumPresControl(String pk_corp)
    throws BusinessException
  {
    UFDouble dPara = null;
    try
    {
      ISysInitQry initDMO = (ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName());
      dPara = initDMO.getParaDbl(pk_corp, "PO03");
    }
    catch (Exception e) {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.getNumPresControl(String)\tException!", new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000087")));
    }
    return dPara;
  }

  public String queryNumPresControlKind(String pk_corp)
    throws BusinessException
  {
    String strPara = null;
    try {
      ISysInitQry initDMO = (ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName());
      strPara = initDMO.getParaString(pk_corp, "PO02");
    }
    catch (Exception e) {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.getNumPresControlKind(String)\tException!", new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000087")));
    }
    return strPara;
  }

  public UFDouble queryPricePresControl(String pk_corp)
    throws BusinessException
  {
    UFDouble dPara = null;
    try
    {
      ISysInitQry initDMO = (ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName());
      dPara = initDMO.getParaDbl(pk_corp, "PO05");
    }
    catch (Exception e) {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.getPricePresControl(String)\tException!", new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000088")));
    }

    return dPara;
  }

  public String queryPricePresControlKind(String pk_corp)
    throws BusinessException
  {
    String strPara = null;
    try {
      ISysInitQry initDMO = (ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName());
      strPara = initDMO.getParaString(pk_corp, "PO04");
    }
    catch (Exception e) {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.getPricePresControlKind(String)\tException!", new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000088")));
    }

    return strPara;
  }

  public OrderVO[] queryScOrderVOsByFromWhere(String strFromWhere, ConditionVO[] powerVOs)
    throws BusinessException
  {
    OrderVO[] ordVOs = null;
    String ss = null;
    String sOperator = null;
    String unitCode = null;
    try {
      InvoiceDMO dmo = new InvoiceDMO();

      if (powerVOs[(powerVOs.length - 2)].getFieldName().equals("操作员"))
        sOperator = powerVOs[(powerVOs.length - 2)].getValue();
      if (powerVOs[(powerVOs.length - 1)].getFieldName().equals("公司")) {
        unitCode = powerVOs[(powerVOs.length - 1)].getValue();
      }
      ss = ScmDps.getSubSql("sc_order", "sc_order", sOperator, new String[] { unitCode });
      if ((ss != null) && (ss.trim().length() > 0)) strFromWhere = strFromWhere + " and " + ss + " ";
      ss = ScmDps.getSubSql("sc_order_b", "sc_order_b", sOperator, new String[] { unitCode });
      if ((ss != null) && (ss.trim().length() > 0)) strFromWhere = strFromWhere + " and " + ss + " ";

      ordVOs = dmo.findScOrderVOsByCondsMy(strFromWhere);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.findOrderVOsByCondsMy(ConditionVO [], String) Exception!", e);
    }

    return ordVOs;
  }

  private ArrayList refreshVO(ArrayList arr)
    throws BusinessException
  {
    InvoiceDMO dmo = null;
    ArrayList arrRet = new ArrayList();
    String key = (String)arr.get(0);
    try {
      dmo = new InvoiceDMO();
      InvoiceVO vo = dmo.findByPrimaryKey(key);
      arrRet.add(key);
      arrRet.add(vo);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.refreshVO(ArrayList)", e);
    }
    return arrRet;
  }

  private ArrayList saveAInvoice(InvoiceVO invVO, Object paraList, String sSettleRule)
    throws BusinessException
  {
    Timer timer = new Timer();
    timer.start("saveAInvoice method begin:");

    ArrayList realParaList = (ArrayList)paraList;

    InvoiceHeaderVO hVO = invVO.getHeadVO();
    InvoiceItemVO[] bVOs = invVO.getBodyVO();

    InvoiceVO resultVO = null;
    Vector resultBodyVec = new Vector();

    ArrayList list = null;
    try
    {
      PubImpl dmoPuPub = new PubImpl();

      InvoiceDMO dmoInvoice = new InvoiceDMO();

      IICToPU_VmiSumDMO oInstance = (IICToPU_VmiSumDMO)NCLocator.getInstance().lookup(IICToPU_VmiSumDMO.class.getName());

      String headKey = null;

      hVO.setDr(new Integer(0));
      if ((hVO.getPrimaryKey() == null) || (hVO.getPrimaryKey().trim().length() == 0))
      {
        String strVinvoiceCode = new GetSysBillCode().getSysBillNO(invVO);

        timer.addExecutePhase("获取发票号");

        hVO.setVinvoicecode(strVinvoiceCode);
        headKey = dmoInvoice.insertHeader(hVO);
        hVO.setPrimaryKey(headKey);
      } else {
        if ((realParaList != null) && (realParaList.size() > 5) && (realParaList.get(5) != null))
        {
          InvoiceVO oldVO = (InvoiceVO)realParaList.get(5);
          new GetSysBillCode().returnBillNoWhenModify(invVO, oldVO, "vinvoicecode");
        }

        headKey = hVO.getPrimaryKey();
        dmoInvoice.updateHeader(hVO);
      }

      timer.addExecutePhase("保存或更新发票表头");

      Vector insertV = new Vector();

      Vector updateV = new Vector();

      Object[][] oIsCanPurchased = (Object[][])null;
      int bodyLength = 0;
      int iRowCount = bVOs.length;
      String[] sCmanids = new String[iRowCount];
      for (int i = 0; i < iRowCount; i++) {
        sCmanids[i] = bVOs[i].getCmangid();
      }
      oIsCanPurchased = dmoPuPub.queryArrayValue("bd_invmandoc", "pk_invmandoc", new String[] { "iscanpurchased" }, sCmanids, "bd_invmandoc.dr=0");

      timer.addExecutePhase("检查是否可采购");

      for (int i = 0; i < iRowCount; i++)
      {
        if ((oIsCanPurchased != null) && (oIsCanPurchased[i] != null) && (oIsCanPurchased[i][0] != null) && (oIsCanPurchased[i][0].toString().trim().length() > 0) && 
          (oIsCanPurchased[i][0].toString().toUpperCase().equals("N"))) {
          bodyLength++;
        }
        else
        {
          switch (bVOs[i].getStatus())
          {
          case 2:
            bVOs[i].setDr(new Integer(0));

            insertV.add(bVOs[i]);
            resultBodyVec.addElement(bVOs[i]);
            break;
          case 1:
            bVOs[i].setDr(new Integer(0));

            if ((bVOs[i].getCinvoice_bid() == null) || (bVOs[i].getCinvoice_bid().trim().length() == 0))
            {
              insertV.add(bVOs[i]);
            }
            else {
              updateV.add(bVOs[i]);
            }
            resultBodyVec.addElement(bVOs[i]);
            break;
          case 3:
            bVOs[i].setDr(new Integer(1));

            updateV.add(bVOs[i]);
            break;
          case 0:
            bVOs[i].setDr(new Integer(0));

            if ((bVOs[i].getCinvoice_bid() == null) || (bVOs[i].getCinvoice_bid().trim().equals("")))
            {
              insertV.add(bVOs[i]);
            }
            else {
              updateV.add(bVOs[i]);
            }
            resultBodyVec.addElement(bVOs[i]);
          }
        }

      }

      if (bodyLength == bVOs.length) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000089"));
      }
      timer.addExecutePhase("表体操作前的准备");

      writeBackBill(invVO);

      timer.addExecutePhase("回写操作");
      String sourcebilltype = null;
      if (insertV.size() > 0)
      {
        InvoiceItemVO[] vos = new InvoiceItemVO[insertV.size()];
        insertV.copyInto(vos);
        dmoInvoice.insertItems(vos, headKey);

        sourcebilltype = vos[0].getCsourcebilltype();
        if ((sourcebilltype != null) && (sourcebilltype.equalsIgnoreCase("50"))) {
          Object[][] rowinfo = (Object[][])null;
          rowinfo = new Object[vos.length][2];
          int k = 0; for (int loopk = vos.length; k < loopk; k++) {
            rowinfo[k][0] = vos[k].getCsourcebillid();
            rowinfo[k][1] = vos[k].getNinvoicenum();
          }

          oInstance.writeBackTotalInvoiceNum(rowinfo);
        }
      }
      if (updateV.size() > 0)
      {
        InvoiceItemVO[] vos = new InvoiceItemVO[updateV.size()];
        updateV.copyInto(vos);

        String key = vos[0].getCinvoiceid();
        Hashtable hIsRel = new Hashtable();
        InvoiceDMO dmo = new InvoiceDMO();
        InvoiceVO oldvos = dmo.findByPrimaryKey(key);
        InvoiceItemVO[] oldItemVOs = (InvoiceItemVO[])(InvoiceItemVO[])oldvos.getChildrenVO();
        UFDouble num = null;
        if ((oldItemVOs != null) && (oldItemVOs.length > 0) && (vos != null) && (vos.length > 0))
        {
          for (int i = 0; i < oldItemVOs.length; i++) {
            InvoiceItemVO tempItem = oldItemVOs[i];
            for (int j = 0; j < vos.length; j++) {
              if (tempItem.getCinvoice_bid().equals(vos[j].getCinvoice_bid()))
              {
                num = vos[j].getNinvoicenum().sub(tempItem.getNinvoicenum());
              }

              if (num == null) {
                num = new UFDouble(0);
              }
              hIsRel.put(tempItem.getCinvoice_bid(), num);
            }
          }
        }

        dmoInvoice.updateItems(vos);

        sourcebilltype = vos[0].getCsourcebilltype();
        if ((sourcebilltype != null) && (sourcebilltype.equalsIgnoreCase("50"))) {
          Object[][] rowinfoDel = (Object[][])null;
          Object[][] rowinfoUpdate = (Object[][])null;
          rowinfoDel = new Object[vos.length][2];
          rowinfoUpdate = new Object[vos.length][2];
          int delNum = 0;
          int updateNum = 0;
          int k = 0; for (int loopk = vos.length; k < loopk; k++) {
            if (vos[k].getStatus() == 3) {
              rowinfoDel[delNum][0] = vos[delNum].getCsourcebillid();
              rowinfoDel[delNum][1] = vos[delNum].getNinvoicenum().multiply(-1.0D);
              delNum++;
            } else if (vos[k].getStatus() == 1) {
              rowinfoUpdate[updateNum][0] = vos[k].getCsourcebillid();
              rowinfoUpdate[updateNum][1] = hIsRel.get(vos[k].getCinvoice_bid());

              updateNum++;
            }
          }

          if (rowinfoDel.length >= 1) {
            oInstance.writeBackTotalInvoiceNum(rowinfoDel);
          }

          if (rowinfoUpdate.length >= 1) {
            oInstance.writeBackTotalInvoiceNum(rowinfoUpdate);
          }

        }

      }

      resultVO = new InvoiceVO(resultBodyVec.size());
      resultVO.setParentVO(hVO);
      InvoiceItemVO[] items = new InvoiceItemVO[resultBodyVec.size()];
      resultBodyVec.copyInto(items);
      resultVO.setChildrenVO(items);

      timer.addExecutePhase("增加、更新发票表体");

      if ((bVOs == null) || (bVOs.length <= 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000073"));
      }

      ArrayList arrRet = autoSettle(resultVO, invVO, (ArrayList)realParaList.get(2), sSettleRule);
      timer.addExecutePhase("autoSettle method");

      list = new ArrayList();
      list.add(headKey);
      list.add(resultVO);
      list.add(arrRet);

      timer.showAllExecutePhase("采购发票保存操作：saveAInvoice结束");
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.saveAInvoice(InvoiceVO, Object) Exception!", e);
    }
    return list;
  }

  private ArrayList autoSettle(InvoiceVO resultVO, InvoiceVO invVO, ArrayList arrPara, String sSettleRule)
    throws BusinessException
  {
    Timer timer = new Timer();
    timer.start("autoSettle method begin:");

    String sSettleParaForGeneral = null;

    String sSettleParaForOrder = null;

    String sSettleParaForVMI = null;

    String pk_corp = null;
    ArrayList arrRet = null;
    try
    {
      ISysInitQry initDMO = (ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName());
      pk_corp = invVO.getHeadVO().getPk_corp();

      Hashtable tPara = initDMO.queryBatchParaValues(pk_corp, new String[] { "PO30", "PO46", "PO77" });
      if ((tPara != null) && (tPara.size() > 0)) {
        Object oTemp = tPara.get("PO30");
        if (oTemp != null) sSettleParaForGeneral = oTemp.toString();
        oTemp = tPara.get("PO46");
        if (oTemp != null) sSettleParaForOrder = oTemp.toString();
        oTemp = tPara.get("PO77");
        if (oTemp != null) sSettleParaForVMI = oTemp.toString();
      }
      if ((sSettleParaForGeneral == null) || (sSettleParaForGeneral.length() == 0))
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000068"));
      if ((sSettleParaForOrder == null) || (sSettleParaForOrder.length() == 0))
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000069"));
      if ((sSettleParaForVMI == null) || (sSettleParaForVMI.length() == 0)) {
        throw new BusinessException("无法获取参数，不知道VMI转发票时是否要立即结算！");
      }
      timer.addExecutePhase("获取是否结算参数");

      String sUpSourceBillType = invVO.getBodyVO()[0].getCupsourcebilltype();
      if ((sUpSourceBillType != null) && (sUpSourceBillType.trim().length() > 0) && (resultVO.getHeadVO().getIinvoicetype().intValue() != 3)) {
        if ((sUpSourceBillType.equals("45")) || (sUpSourceBillType.equals("47")) || (sUpSourceBillType.equals("4T")))
        {
          if ((sSettleParaForGeneral.equals("保存时自动结算")) && (!sSettleRule.trim().equals("S")) && (!sSettleRule.trim().equals("N")))
          {
            arrRet = doSettle(resultVO, 2, arrPara, sSettleRule);
          }
        } else if ((sUpSourceBillType.equals("21")) && (!sSettleRule.trim().equals("S")) && (!sSettleRule.trim().equals("N"))) {
          if ((sSettleParaForOrder.equals("保存时自动结算")) || (sSettleRule.trim().equals("Z")))
          {
            arrRet = doSettle(resultVO, 1, arrPara, sSettleRule);
          }
        } else if ((sUpSourceBillType.equals("50")) && 
          (sSettleParaForVMI.equals("保存时自动结算")))
        {
          arrRet = doSettle(resultVO, 3, arrPara, sSettleRule);
        }
      }

      String[] sRet = null;
      UFBoolean bSucceed = null;
      if ((arrRet != null) && (arrRet.size() > 0)) {
        bSucceed = (UFBoolean)arrRet.get(1);
        arrRet = (ArrayList)arrRet.get(0);
      }
      if ((arrRet != null) && (arrRet.size() > 0)) {
        sRet = new String[arrRet.size()];
        arrRet.toArray(sRet);

        LockTool.releaseLockForPks(sRet, (String)invVO.getHeadVO().getAttributeValue("cuserid"));
      }

      if (PuPubVO.getString_TrimZeroLenAsNull(sUpSourceBillType) != null)
      {
        if (((sUpSourceBillType.equals("45")) || (sUpSourceBillType.equals("47")) || (sUpSourceBillType.equals("4T"))) && (sSettleParaForGeneral.equals("保存时自动结算")) && (!sSettleRule.equals("S")) && (!sSettleRule.equals("N")) && (!bSucceed.booleanValue()))
        {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000093"));
        }
        if ((sUpSourceBillType.equals("50")) && (sSettleParaForVMI.equals("保存时自动结算")) && (!bSucceed.booleanValue()))
        {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000093"));
        }
      }

      timer.addExecutePhase("doSettle method");
      timer.showAllExecutePhase("采购发票自动结算 autoSettle 操作结束");
    }
    catch (Exception e) {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.autoSettle", e);
    }
    return arrRet;
  }

  public ArrayList saveInvoice(InvoiceVO invVO, Object paraList)
    throws BusinessException
  {
    IScm srv = (IScm)NCLocator.getInstance().lookup(IScm.class.getName());
    srv.checkDefDataType(invVO);

    InvoiceHeaderVO hVO = invVO.getHeadVO();
    if ((hVO.getCinvoiceid() != null) && (hVO.getCinvoiceid().trim().length() > 0)) {
      hVO.setTlastmaketime(new UFDateTime(new Date()).toString());
      hVO.setTaudittime(null);
    } else {
      hVO.setTmaketime(new UFDateTime(new Date()).toString());
      hVO.setTaudittime(null);
      hVO.setTlastmaketime(hVO.getTmaketime());
    }

    ArrayList list = null;
    try
    {
      InvoiceItemVO[] newItems = (InvoiceItemVO[])(InvoiceItemVO[])invVO.getChildrenVO();

      Vector vTemp = new Vector();
      String sUpSourceRowID = null;
      String sUpSourceType = null;
      for (int i = 0; i < newItems.length; i++) {
        sUpSourceRowID = newItems[i].getCupsourcebillrowid();
        if (sUpSourceRowID != null) vTemp.addElement(sUpSourceRowID);
        if (newItems[i].getCupsourcebilltype() == null) continue; sUpSourceType = newItems[i].getCupsourcebilltype();
      }

      if ((vTemp.size() > 0) && (sUpSourceType != null) && ((sUpSourceType.equals("21")) || (sUpSourceType.equals("61")) || (sUpSourceType.equals("45")) || (sUpSourceType.equals("47"))))
      {
        String[] sUpSourceRowIDs = new String[vTemp.size()];
        vTemp.copyInto(sUpSourceRowIDs);

        Object[][] oTemp = (Object[][])null;
        Object o = new InvoiceDMO().queryRelatedData(sUpSourceType, sUpSourceRowIDs);
        if (o != null) oTemp = (Object[][])(Object[][])o;

        if (oTemp != null) {
          for (int i = 0; i < newItems.length; i++) {
            sUpSourceRowID = newItems[i].getCupsourcebillrowid();
            if ((sUpSourceRowID != null) && (newItems[i].getStatus() != 3)) {
              UFDouble dInvoiceNum = newItems[i].getNinvoicenum();
              if (dInvoiceNum != null) {
                for (int j = 0; j < sUpSourceRowIDs.length; j++) {
                  if (sUpSourceRowID.equals(sUpSourceRowIDs[j])) {
                    Object o1 = oTemp[j][0];
                    Object o2 = oTemp[j][1];
                    if ((o1 != null) && (o2 != null)) {
                      UFDouble dAccumInvoiceNum = new UFDouble(o1.toString());
                      UFDouble nNum = new UFDouble(o2.toString());
                      if ((dInvoiceNum.doubleValue() + dAccumInvoiceNum.doubleValue()) * nNum.doubleValue() < 0.0D) {
                        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000063"));
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
      BusinessCurrencyRateUtil dmoCurrArith = new BusinessCurrencyRateUtil(hVO.getPk_corp());
      UFDouble zbRate = hVO.getNexchangeotobrate();
      UFDouble zfRate = hVO.getNexchangeotoarate();
      if (dmoCurrArith.isBlnLocalFrac())
      {
        if (!dmoCurrArith.isLocalCurrType(hVO.getCcurrencytypeid())) {
          if ((zbRate == null) || (zbRate.toString().trim().length() == 0) || (zbRate.doubleValue() <= 0.0D)) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000231"));
          }
          if ((zfRate == null) || (zfRate.toString().trim().length() == 0) || (zfRate.doubleValue() <= 0.0D)) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000232"));
          }
        }

      }
      else if ((zbRate == null) || (zbRate.toString().trim().length() == 0) || (zbRate.doubleValue() <= 0.0D)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000233"));
      }

      PubDMO dmoPuPub = new PubDMO();

      Object[][] oRule = dmoPuPub.queryResultsFromAnyTable("bd_busitype", new String[] { "verifyrule" }, "pk_busitype='" + hVO.getCbiztype() + "'");

      if ((oRule == null) || (oRule[0] == null) || (oRule[0][0] == null))
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000072"));
      String sStoreOrgId = hVO.getCstoreorganization();

      if ((((String)oRule[0][0]).trim().equals("Z")) && ((sStoreOrgId == null) || (sStoreOrgId.trim().length() == 0))) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000090"));
      }

      list = saveAInvoice(invVO, paraList, (String)oRule[0][0]);

      list = refreshVO(list);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.saveInvoice(InvoiceVO, Object) Exception!", e);
    }
    return list;
  }

  private ArrayList settleForOrder(InvoiceVO vo, String sRule, ArrayList paraList)
    throws BusinessException
  {
    InvoiceItemVO[] items = vo.getBodyVO();
    InvoiceItemVO[] validItems = null;
    InvoiceHeaderVO head = vo.getHeadVO();
    ArrayList arrRet = null;
    try {
      SettleImpl beanSettle = new SettleImpl();

      PubDMO dmoPuPub = new PubDMO();

      if (!sRule.trim().equals("Z")) {
        arrRet = beanSettle.doOrderToInvoiceSettle(new InvoiceVO[] { vo }, paraList);

        InvoiceDMO dmoInvoice = new InvoiceDMO();
        InvoiceItemVO[] tempBodyVO = null;

        ArrayList list = null;
        if ((arrRet != null) && (arrRet.size() > 0)) list = (ArrayList)arrRet.get(0);
        if ((list != null) && (list.size() > 0))
          tempBodyVO = dmoInvoice.findItemsForHeader(head.getCinvoiceid());
        if ((tempBodyVO != null) && (tempBodyVO.length > 0))
          vo.setChildrenVO(tempBodyVO);
        return arrRet;
      }

      Vector settleVOVEC = new Vector();
      Vector settleStoOrgVEC = new Vector();

      String[] sCorderId1 = null;
      ArrayList arrCorderId1 = new ArrayList();

      String[] sCorderId2 = null;
      ArrayList arrCorderId2 = new ArrayList();

      String[] sCbaseId = null;
      ArrayList arrCbaseId = new ArrayList();

      String[] sCorder_Bid = null;
      ArrayList arrCorder_Bid = new ArrayList();

      for (int i = 0; i < items.length; i++) {
        if (items[i].getCupsourcebilltype().equals("21")) {
          arrCorderId1.add(items[i].getCorderid());
          arrCorder_Bid.add(items[i].getCorder_bid());
        }
        else if (items[i].getCupsourcebilltype().equals("61")) {
          arrCorderId2.add(items[i].getCorderid());
        }

        if ((items[i].getCupsourcebilltype() != null) && (items[i].getCupsourcebilltype().trim().length() > 1)) {
          arrCbaseId.add(items[i].getCbaseid());
        }
      }
      if (arrCorderId1.size() > 0) {
        sCorderId1 = new String[arrCorderId1.size()];
        arrCorderId1.toArray(sCorderId1);
      }
      if (arrCorderId2.size() > 0) {
        sCorderId2 = new String[arrCorderId2.size()];
        arrCorderId2.toArray(sCorderId2);
      }

      if (arrCbaseId.size() > 0) {
        sCbaseId = new String[arrCbaseId.size()];
        arrCbaseId.toArray(sCbaseId);
      }

      if (arrCorder_Bid.size() > 0)
      {
        sCorder_Bid = new String[arrCorder_Bid.size()];
        arrCorder_Bid.toArray(sCorder_Bid);
      }

      HashMap hCorder_Bid = null;
      if ((sCorder_Bid != null) && (sCorder_Bid.length > 0)) {
        hCorder_Bid = dmoPuPub.queryArrayValues("po_order_b", "corder_bid", new String[] { "pk_arrvstoorg" }, sCorder_Bid, "po_order_b.dr=0");
      }

      HashMap hCorderId2 = null;
      if ((sCorderId2 != null) && (sCorderId2.length > 0)) {
        hCorderId2 = dmoPuPub.queryArrayValues("sc_order", "corderid", new String[] { "cwareid" }, sCorderId2, "sc_order.dr=0");
      }

      HashMap hCbaseId = null;
      if ((sCbaseId != null) && (sCbaseId.length > 0)) {
        hCbaseId = dmoPuPub.queryArrayValues("bd_invbasdoc", "pk_invbasdoc", new String[] { "discountflag", "laborflag" }, sCbaseId, "bd_invbasdoc.dr=0");
      }

      for (int i = 0; i < items.length; i++)
      {
        if ((items[i].getCupsourcebilltype() == null) || (items[i].getCupsourcebilltype().trim().length() <= 1)) {
          continue;
        }
        Object[] s1 = null;

        Object[] s3 = null;
        if (items[i].getCupsourcebilltype().equals("21"))
        {
          if (hCorder_Bid != null) {
            s1 = (Object[])(Object[])hCorder_Bid.get(items[i].getCorder_bid());
          }
        }
        else if ((items[i].getCupsourcebilltype().equals("61")) && 
          (hCorderId2 != null)) {
          s1 = (Object[])(Object[])hCorderId2.get(items[i].getCorderid());
        }

        if ((s1 == null) && (s1[0] == null)) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000091"));
        }

        if (hCbaseId != null)
          s3 = (Object[])(Object[])hCbaseId.get(items[i].getCbaseid());
        if (s3 == null) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000092"));
        }
        if ((s3[0].toString().equals("Y")) || (s3[1].toString().equals("Y")))
        {
          continue;
        }
        settleVOVEC.add(items[i]);
        settleStoOrgVEC.add((s1 == null) || (s1[0] == null) ? null : (String)s1[0]);
      }

      if (settleVOVEC.size() == 0) {
        SCMEnv.out("直运订单生成发票所有发票行都不是从订单转入.");
        return null;
      }

      validItems = new InvoiceItemVO[settleVOVEC.size()];
      String[] validOrgs = new String[settleStoOrgVEC.size()];

      settleVOVEC.copyInto(validItems);
      settleStoOrgVEC.copyInto(validOrgs);

      if (((validOrgs == null) || (validOrgs.length == 0) || (validOrgs[0] == null)) && 
        (sRule.trim().equals("Z"))) {
        int size = validItems.length;
        validOrgs = new String[size];
        for (int i = 0; i < size; i++) {
          validOrgs[i] = head.getCstoreorganization();
        }
      }
      vo.setChildrenVO(validItems);

      if (head.getCauditpsn() != null) {
        beanSettle.doDirectInvoiceBalance(head.getCaccountyear(), (UFDate)paraList.get(0), head.getCbiztype(), head.getCauditpsn(), head.getPk_corp(), vo, validOrgs);
      }
      else
      {
        beanSettle.doDirectInvoiceBalance(head.getCaccountyear(), (UFDate)paraList.get(0), head.getCbiztype(), head.getCoperator(), head.getPk_corp(), vo, validOrgs);
      }

    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.settleForOrder", e);
    }

    return arrRet;
  }

  private ArrayList settleForVMI(InvoiceVO vo, ArrayList paraList)
    throws BusinessException
  {
    InvoiceHeaderVO head = vo.getHeadVO();
    ArrayList arrRet = null;
    try
    {
      if (head.getCauditpsn() != null) {
        arrRet = new VMIImpl().doVMI2InvoiceSettle(vo, head.getCaccountyear(), (UFDate)paraList.get(0), head.getPk_corp(), head.getCauditpsn(), queryEstimateModule(vo.getHeadVO().getPk_corp()), queryDiffModule(vo.getHeadVO().getPk_corp()), (UFBoolean)paraList.get(2));
      }
      else
        arrRet = new VMIImpl().doVMI2InvoiceSettle(vo, head.getCaccountyear(), (UFDate)paraList.get(0), head.getPk_corp(), head.getCoperator(), queryEstimateModule(vo.getHeadVO().getPk_corp()), queryDiffModule(vo.getHeadVO().getPk_corp()), (UFBoolean)paraList.get(2));
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }

    return arrRet;
  }

  private ArrayList settleForSto(InvoiceVO vo, ArrayList paraList)
    throws BusinessException
  {
    InvoiceItemVO[] items = vo.getBodyVO();
    InvoiceItemVO[] validItems = null;
    InvoiceHeaderVO head = vo.getHeadVO();
    ArrayList arrRet = null;
    try
    {
      SettleImpl beanSettle = new SettleImpl();

      PubDMO dmoPuPub = new PubDMO();
      InvoiceDMO dmoInvoice = new InvoiceDMO();

      String[] sCbaseId = null;
      ArrayList arrCbaseId = new ArrayList();
      int size = items.length;

      for (int i = 0; i < size; i++) {
        if ((items[i].getCupsourcebilltype() != null) && (items[i].getCupsourcebilltype().trim().length() > 1)) {
          arrCbaseId.add(items[i].getCbaseid());
        }
      }
      if (arrCbaseId.size() > 0) {
        sCbaseId = new String[arrCbaseId.size()];
        arrCbaseId.toArray(sCbaseId);
      }

      HashMap hCbaseId = null;
      if ((sCbaseId != null) && (sCbaseId.length > 0)) {
        hCbaseId = dmoPuPub.queryArrayValues("bd_invbasdoc", "pk_invbasdoc", new String[] { "discountflag", "laborflag" }, sCbaseId, "bd_invbasdoc.dr=0");
      }

      Vector settleVOVEC = new Vector();
      Vector settleHVEC = new Vector();
      Vector settleBVEC = new Vector();
      Vector settleBb3VEC = new Vector();

      ArrayList arrWhere = new ArrayList();
      String[] sWhere = null;
      for (int i = 0; i < items.length; i++)
      {
        if ((items[i].getCupsourcebilltype() != null) && (items[i].getCupsourcebilltype().trim().length() > 1)) {
          Object[] s1 = null;

          if (hCbaseId != null)
            s1 = (Object[])(Object[])hCbaseId.get(items[i].getCbaseid());
          if (s1 == null) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000092"));
          }
          if ((s1[0].toString().equals("Y")) || (s1[1].toString().equals("Y"))) {
            continue;
          }
          arrWhere.add(items[i].getCupsourcebillrowid());
          settleVOVEC.add(items[i]);
        }
      }

      if (settleVOVEC.size() == 0) {
        SCMEnv.out("入库单生成发票所有发票行都不是从入库单转入.");
        return null;
      }
      if (arrWhere.size() > 0) {
        sWhere = new String[arrWhere.size()];
        arrWhere.toArray(sWhere);
      }
      String strIdsSet = "";
      TempTableDMO tmpTblDmo = new TempTableDMO();
      strIdsSet = tmpTblDmo.insertTempTable(sWhere, "t_pu_pi_008", "pk_pu");

      if ((strIdsSet == null) || (strIdsSet.trim().length() == 0)) {
        strIdsSet = "('TempTableDMOError')";
      }
      String strWhere = "( ic_general_b.cgeneralbid in " + strIdsSet + ")";
      Object[] obGeneVO = dmoInvoice.findGeneralVOsFromIDs(strWhere);
      Vector geneHVEC = (Vector)obGeneVO[0];
      Vector geneBVEC = (Vector)obGeneVO[1];
      Vector geneBB3VEC = (Vector)obGeneVO[2];

      for (int i = 0; i < settleVOVEC.size(); i++) {
        String strUPBid = ((InvoiceItemVO)settleVOVEC.get(i)).getCupsourcebillrowid();

        boolean bVOExisted = false;
        for (int j = 0; j < geneHVEC.size(); j++) {
          if (((GeneralBillItemVO)geneBVEC.get(j)).getCgeneralbid().equals(strUPBid)) {
            settleHVEC.add((GeneralBillHeaderVO)geneHVEC.get(j));
            settleBVEC.add((GeneralBillItemVO)geneBVEC.get(j));
            settleBb3VEC.add((GeneralBb3VO)geneBB3VEC.get(j));

            geneHVEC.remove(j);
            geneBVEC.remove(j);
            geneBB3VEC.remove(j);

            bVOExisted = true;
            break;
          }
        }
        if (!bVOExisted) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000093"));
        }

      }

      validItems = new InvoiceItemVO[settleVOVEC.size()];
      GeneralBillHeaderVO[] gHeads = new GeneralBillHeaderVO[settleHVEC.size()];
      GeneralBillItemVO[] gItems = new GeneralBillItemVO[settleBVEC.size()];
      GeneralBb3VO[] gBb3s = new GeneralBb3VO[settleBb3VEC.size()];
      settleVOVEC.copyInto(validItems);
      settleHVEC.copyInto(gHeads);
      settleBVEC.copyInto(gItems);
      settleBb3VEC.copyInto(gBb3s);
      vo.setChildrenVO(validItems);

      if (head.getCauditpsn() != null) {
        arrRet = beanSettle.doStockToInvoiceSettle(head.getCaccountyear(), (UFDate)paraList.get(0), head.getCbiztype(), head.getCauditpsn(), head.getPk_corp(), vo, gHeads, gItems, gBb3s, queryEstimateModule(vo.getHeadVO().getPk_corp()), queryDiffModule(vo.getHeadVO().getPk_corp()), (UFBoolean)paraList.get(2));
      }
      else
      {
        arrRet = beanSettle.doStockToInvoiceSettle(head.getCaccountyear(), (UFDate)paraList.get(0), head.getCbiztype(), head.getCoperator(), head.getPk_corp(), vo, gHeads, gItems, gBb3s, queryEstimateModule(vo.getHeadVO().getPk_corp()), queryDiffModule(vo.getHeadVO().getPk_corp()), (UFBoolean)paraList.get(2));
      }

    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.settleForOrder", e);
    }
    return arrRet;
  }

  private void unAuditAInvoice(InvoiceVO invVO)
    throws BusinessException
  {
    InvoiceHeaderVO headVO = invVO.getHeadVO();
    try
    {
      new InvoiceDMO().updateIbillStatusAndApproveForHID(headVO.getCinvoiceid(), 0, null, null);

      headVO.setDr(new Integer(0));

      headVO.setIbillstatus(new Integer(0));
      headVO.setCauditpsn(null);
      headVO.setDauditdate(null);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.unAuditAInvoice(InvoiceVO)", e);
    }
  }

  public void unAuditInvoiceArray(InvoiceVO[] voaInvoice)
    throws BusinessException
  {
    try
    {
      SysInitDMO initDMO = new SysInitDMO();
      InvoiceHeaderVO headVO = voaInvoice[0].getHeadVO();
      String pk_corp = headVO.getPk_corp();

      String sParaSettleInTime = initDMO.getParaString(pk_corp, "PO30");
      String sParaSettleInTimeOrder = initDMO.getParaString(pk_corp, "PO46");
      String sParaZGYF = initDMO.getParaString(pk_corp, "PO52");
      UFBoolean bZGYF = new UFBoolean(false);
      if (sParaZGYF != null) bZGYF = new UFBoolean(sParaZGYF);

      if ((sParaSettleInTime == null) || (sParaSettleInTime.length() <= 0))
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000094"));
      if ((sParaSettleInTimeOrder == null) || (sParaSettleInTimeOrder.length() <= 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000229"));
      }
      Hashtable hRule = new Hashtable();
      String sRule = null;
      for (int i = 0; i < voaInvoice.length; i++) {
        headVO = voaInvoice[i].getHeadVO();
        if (hRule.get(headVO.getCbiztype()) == null)
        {
          Object[][] oRule = new PubDMO().queryResultsFromAnyTable("bd_busitype", new String[] { "verifyrule" }, "pk_busitype='" + headVO.getCbiztype() + "'");
          if ((oRule == null) || (oRule[0][0] == null)) throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000072"));
          hRule.put(headVO.getCbiztype(), oRule[0][0]);
        }
        sRule = (String)hRule.get(headVO.getCbiztype());

        String sUpSourceBillType = voaInvoice[i].getBodyVO()[0].getCupsourcebilltype();

        if ((sUpSourceBillType != null) && (sUpSourceBillType.trim().length() > 0) && (headVO.getIinvoicetype().intValue() != 3)) {
          if ((sUpSourceBillType.equals("45")) || (sUpSourceBillType.equals("47")) || (sUpSourceBillType.equals("4T"))) {
            if ((!sRule.trim().equals("S")) && (sParaSettleInTime.equals("审批时自动结算"))) {
              double d1 = 0.0D;
              for (int j = 0; j < voaInvoice[i].getBodyVO().length; j++) {
                d1 = 0.0D;
                if (voaInvoice[i].getBodyVO()[j].getNaccumsettmny() != null) {
                  d1 = voaInvoice[i].getBodyVO()[j].getNaccumsettmny().doubleValue();
                }
                if ((d1 != 0.0D) && (!bZGYF.booleanValue()))
                  throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000095"));
              }
            }
          }
          else if ((sUpSourceBillType.equals("21")) && 
            (!sRule.trim().equals("Z")) && (sParaSettleInTimeOrder.equals("审批时自动结算"))) {
            double d1 = 0.0D;
            for (int j = 0; j < voaInvoice[i].getBodyVO().length; j++) {
              d1 = 0.0D;
              if (voaInvoice[i].getBodyVO()[j].getNaccumsettmny() != null) {
                d1 = voaInvoice[i].getBodyVO()[j].getNaccumsettmny().doubleValue();
              }
              if ((d1 != 0.0D) && (!bZGYF.booleanValue())) {
                throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000095"));
              }
            }

          }

        }

        unAuditAInvoice(voaInvoice[i]);
      }
    }
    catch (Exception e) {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.unAuditInvoiceArray(InvoiceVO [])", e);
    }
  }

  public void updateCostPriceForInv(InvoiceVO vo)
    throws BusinessException
  {
    if ((vo == null) || (vo.getChildrenVO() == null)) {
      return;
    }

    Vector v = new Vector();
    InvoiceItemVO[] items = (InvoiceItemVO[])(InvoiceItemVO[])vo.getChildrenVO();
    String sStoreOrg = vo.getHeadVO().getCstoreorganization();
    String sPk_corp = vo.getHeadVO().getPk_corp();
    for (int i = 0; i < items.length; i++) {
      UFDouble num = items[i].getNinvoicenum();
      UFDouble sum = items[i].getNmoney();

      if ((sum == null) || (num == null))
      {
        continue;
      }
      if ((sum.doubleValue() == 0.0D) || (num.doubleValue() == 0.0D)) {
        continue;
      }
      v.add(items[i]);
    }

    InvoiceItemVO[] updateItems = null;
    if (v.size() > 0) {
      updateItems = new InvoiceItemVO[v.size()];
      v.copyInto(updateItems);
    }
    if (updateItems != null)
      try {
        InvoiceDMO dmo = new InvoiceDMO();
        dmo.updateCostPriceForInv(updateItems, sStoreOrg, sPk_corp);
      }
      catch (Exception e) {
        PubDMO.throwBusinessException("InvoiceBean::updateRefSalePriceForInv(InvoiceVO) Exception!", e);
      }
  }

  private void writeBackBill(InvoiceVO invVO)
    throws BusinessException
  {
    Timer timer = new Timer();
    timer.start("采购发票回写操作writeBackBill开始");

    InvoiceItemVO[] bVOs = invVO.getBodyVO();
    InvoiceVO writeBackVO = new InvoiceVO();
    writeBackVO.setParentVO(invVO.getParentVO());
    if (invVO.getUserConfirmFlag() != null) writeBackVO.setUserConfirmFlag(invVO.getUserConfirmFlag()); try
    {
      String sBillType = bVOs[0].getCupsourcebilltype();

      if ((sBillType != null) && (sBillType.equals("21"))) {
        writeBackToPoOrder(invVO, false);
        timer.addExecutePhase("回写采购订单writeBackToPoOrder");
      }
      else if ((sBillType != null) && (sBillType.equals("61"))) {
        writeBackToScOrder(invVO);
        timer.addExecutePhase("回写委外订单writeBackToScOrder");
      }
      else if ((sBillType != null) && ((sBillType.equals("45")) || (sBillType.equals("4T")) || (sBillType.equals("47")))) {
        writeBackToIc(invVO);

        timer.addExecutePhase("回写采购入库单writeBackToIc");

        ArrayList arr = new ArrayList();
        InvoiceItemVO[] items = (InvoiceItemVO[])(InvoiceItemVO[])invVO.getChildrenVO();
        for (int i = 0; i < items.length; i++) {
          if ((items[i].getCorder_bid() != null) && (items[i].getCorder_bid().length() > 0))
            arr.add(items[i]);
        }
        InvoiceItemVO[] bodyItems = null;
        if (arr.size() > 0) {
          bodyItems = new InvoiceItemVO[arr.size()];
          arr.toArray(bodyItems);
        }
        if ((bodyItems != null) && (bodyItems.length > 0)) {
          writeBackVO.setChildrenVO(bodyItems);

          if ((sBillType.equals("45")) || (sBillType.equals("4T"))) writeBackToPoOrder(writeBackVO, true);
          else if (sBillType.equals("47")) writeBackToScOrder(writeBackVO);
          timer.addExecutePhase("回写采购订单writeBackToPoOrder");
        }
      }
      timer.showAllExecutePhase("采购发票回写操作writeBackBill结束");
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.writeBackBill(InvoiceVO) Exception!", e);
    }
  }

  private void writeBackToIc(InvoiceVO invVO)
    throws BusinessException
  {
    Timer timer = new Timer();
    timer.start("回写单据采购入库单操作writeBackToIc开始");

    InvoiceItemVO[] bVOs = invVO.getBodyVO();
    try
    {
      InvoiceDMO dmoInvoice = new InvoiceDMO();

      ArrayList arrIc = new ArrayList();
      ParaPoToIcLendRewriteVO[] vos = null;

      int iRowCount = bVOs.length;
      String[] sPrimaryKeys = null;
      ArrayList arr = new ArrayList();
      for (int i = 0; i < iRowCount; i++) {
        String temp = bVOs[i].getPrimaryKey();
        if ((temp != null) && (temp.length() > 0))
          arr.add(temp);
      }
      if (arr.size() > 0) {
        sPrimaryKeys = new String[arr.size()];
        arr.toArray(sPrimaryKeys);
      }

      HashMap hInvoiceNum = new HashMap();
      if ((sPrimaryKeys != null) && (sPrimaryKeys.length > 0)) {
        hInvoiceNum = dmoInvoice.findInvoiceNumByInvoiceBPKMyForIc2In(sPrimaryKeys);
      }
      timer.addExecutePhase("得到当前发票行的原有发票数量findInvoiceNumByInvoiceBPKMy");

      for (int i = 0; i < iRowCount; i++) {
        if ((bVOs[i].getCupsourcebillrowid() == null) || (bVOs[i].getCupsourcebillrowid().trim().equals(""))) {
          continue;
        }
        double nOriInvNum = 0.0D;
        double nOriWasteNum = 0.0D;

        if ((bVOs[i].getPrimaryKey() != null) && 
          (hInvoiceNum.get(bVOs[i].getPrimaryKey()) != null)) {
          nOriInvNum = ((UFDouble[])(UFDouble[])hInvoiceNum.get(bVOs[i].getPrimaryKey()))[0].doubleValue();
          nOriWasteNum = ((UFDouble[])(UFDouble[])hInvoiceNum.get(bVOs[i].getPrimaryKey()))[1].doubleValue();
        }

        if ((bVOs[i].getNreasonwastenum() == null) || (bVOs[i].getNreasonwastenum().toString().trim().length() == 0)) bVOs[i].setNreasonwastenum(new UFDouble(0));
        if ((bVOs[i].getNinvoicenum().doubleValue() == nOriInvNum) && (bVOs[i].getNreasonwastenum().doubleValue() == nOriWasteNum) && (bVOs[i].getStatus() != 3))
          continue;
        ParaPoToIcLendRewriteVO vo = new ParaPoToIcLendRewriteVO();
        vo.setCRowID(bVOs[i].getCupsourcebillrowid());
        double d1 = nOriInvNum;
        double dd1 = nOriWasteNum;
        double d2 = 0.0D;
        double dd2 = 0.0D;
        if (bVOs[i].getStatus() == 3) {
          d2 = 0.0D;
          dd2 = 0.0D;
        } else {
          if (bVOs[i].getNinvoicenum() != null) d2 = bVOs[i].getNinvoicenum().doubleValue();
          if (bVOs[i].getNreasonwastenum() != null) dd2 = bVOs[i].getNreasonwastenum().doubleValue();
        }

        vo.setDSubNum(new UFDouble(d2 - d1));
        vo.setDWasteSubNum(new UFDouble(dd2 - dd1));
        arrIc.add(vo);
      }

      if (arrIc.size() > 0) {
        vos = new ParaPoToIcLendRewriteVO[arrIc.size()];
        arrIc.toArray(vos);
      }

      timer.addExecutePhase("回写采购入库单前的准备");

      if ((vos != null) && (vos.length > 0))
      {
        dmoInvoice.isSignNumExceedInNum(vos);

        IICToPU_Ic2puDMO dmo = (IICToPU_Ic2puDMO)NCLocator.getInstance().lookup(IICToPU_Ic2puDMO.class.getName());
        dmo.writebackNsignnum(vos);

        timer.addExecutePhase("回写采购入库单writebackNsignnum");
      }

      timer.showAllExecutePhase("回写单据采购入库单操作writeBackToIc结束");
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.writeBackToIc(InvoiceVO) Exception!", e);
    }
  }

  private void writeBackToPoOrder(InvoiceVO invVO, boolean isFrmIc)
    throws BusinessException
  {
    Timer timer = new Timer();
    timer.start("回写单据采购订单操作writeBackToPoOrder开始");

    InvoiceItemVO[] bVOs = invVO.getBodyVO();
    OrderImpl beanOrder = null;
    try
    {
      InvoiceDMO dmoInvoice = new InvoiceDMO();

      ArrayList arrWriteBackId = new ArrayList();

      ArrayList arrWriteBackOriNum = new ArrayList();
      ArrayList arrWriteBackCurNum = new ArrayList();
      ArrayList arrWriteBackPrice = new ArrayList();

      String[] sIds = null;
      UFDouble[] ufOriNums = null;
      UFDouble[] ufCurNums = null;
      UFDouble[] ufCurPrices = null;

      int iRowCount = bVOs.length;
      String[] sPrimaryKeys = null;
      String[] sCorder_bIds = null;
      ArrayList arr = new ArrayList();
      ArrayList arr1 = new ArrayList();
      for (int i = 0; i < iRowCount; i++) {
        String temp = bVOs[i].getPrimaryKey();
        if ((temp != null) && (temp.length() > 0))
          arr.add(temp);
        String temp1 = bVOs[i].getCorder_bid();
        if ((temp1 != null) && (temp1.length() > 0))
          arr1.add(temp1);
      }
      if (arr.size() > 0) {
        sPrimaryKeys = new String[arr.size()];
        arr.toArray(sPrimaryKeys);
      }
      if (arr1.size() > 0) {
        sCorder_bIds = new String[arr1.size()];
        arr1.toArray(sCorder_bIds);
      }

      HashMap hInvoiceNum = new HashMap();
      if ((sPrimaryKeys != null) && (sPrimaryKeys.length > 0) && (arr1.size() > 0)) {
        hInvoiceNum = dmoInvoice.findInvoiceNumByInvoiceBPKMy(sPrimaryKeys);
      }
      timer.addExecutePhase("得到当前发票行的原有发票数量findInvoiceNumByInvoiceBPKMy");

      for (int i = 0; i < iRowCount; i++)
      {
        if ((bVOs[i].getCorder_bid() == null) || (bVOs[i].getCorder_bid().trim().length() <= 0))
        {
          continue;
        }
        double nOriInvNum = 0.0D;

        if ((bVOs[i].getPrimaryKey() != null) && 
          (hInvoiceNum.get(bVOs[i].getPrimaryKey()) != null)) {
          nOriInvNum = ((UFDouble)hInvoiceNum.get(bVOs[i].getPrimaryKey())).doubleValue();
        }

        arrWriteBackId.add(bVOs[i].getCorder_bid());

        arrWriteBackOriNum.add(new UFDouble(nOriInvNum));
        if (bVOs[i].getStatus() == 3)
          arrWriteBackCurNum.add(new UFDouble(0.0D));
        else {
          arrWriteBackCurNum.add(bVOs[i].getNinvoicenum());
        }

        if ((bVOs[i].getNinvoicenum() != null) && (!bVOs[i].getNinvoicenum().equals(new UFDouble(0.0D))))
          arrWriteBackPrice.add(bVOs[i].getNmoney().div(bVOs[i].getNinvoicenum()));
        else {
          arrWriteBackPrice.add(null);
        }
      }

      if (arrWriteBackId.size() > 0) {
        sIds = new String[arrWriteBackId.size()];
        arrWriteBackId.toArray(sIds);
      }
      if (arrWriteBackOriNum.size() > 0) {
        ufOriNums = new UFDouble[arrWriteBackOriNum.size()];
        arrWriteBackOriNum.toArray(ufOriNums);
      }
      if (arrWriteBackCurNum.size() > 0) {
        ufCurNums = new UFDouble[arrWriteBackCurNum.size()];
        arrWriteBackCurNum.toArray(ufCurNums);
      }
      if (arrWriteBackPrice.size() > 0) {
        ufCurPrices = new UFDouble[arrWriteBackPrice.size()];
        arrWriteBackPrice.toArray(ufCurPrices);
      }

      beanOrder = getBean_Order();

      timer.addExecutePhase("回写采购订单前的准备");

      if ((ufOriNums != null) && (ufOriNums.length > 0) && (ufCurNums != null) && (ufCurNums.length > 0))
      {
        ParaPiToPoRewriteVO voPara = new ParaPiToPoRewriteVO();
        voPara.setPk_corp(invVO.getHeadVO().getPk_purcorp());
        voPara.setCBodyIdArray(sIds);
        voPara.setDOldNumArray(ufOriNums);
        voPara.setDNumArray(ufCurNums);

        voPara.setIsFromIc(isFrmIc);
        voPara.setDLocalNetPriceArray(ufCurPrices);
        if (invVO.getUserConfirmFlag() != null) voPara.setUserConfirm(invVO.getUserConfirmFlag().booleanValue());

        beanOrder.rewritePiNum(voPara);

        timer.addExecutePhase("回写采购订单rewritePiNum");
      }
      timer.showAllExecutePhase("回写单据采购订单操作writeBackToPoOrder结束");
    }
    catch (Exception e) {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.writeBackToPoOrder(InvoiceVO) Exception!", e);
    }
  }

  private void writeBackToScOrder(InvoiceVO invVO)
    throws BusinessException
  {
    Timer timer = new Timer();
    timer.start("回写单据委外订单操作writeBackToScOrder开始");

    InvoiceItemVO[] bVOs = invVO.getBodyVO();
    try
    {
      InvoiceDMO dmoInvoice = new InvoiceDMO();
      ArrayList arrWriteBackId = new ArrayList();

      ArrayList arrWriteBackScOrderNum = new ArrayList();
      ArrayList arrWriteBackScOrderPrice = new ArrayList();

      String[] sIds = null;
      UFDouble[] ufScOrderNums = null;
      UFDouble[] ufScOrderPrices = null;

      int iRowCount = bVOs.length;
      String[] sPrimaryKeys = null;
      String[] sCorder_bIds = null;
      ArrayList arr = new ArrayList();
      ArrayList arr1 = new ArrayList();
      for (int i = 0; i < iRowCount; i++) {
        String temp = bVOs[i].getPrimaryKey();
        if ((temp != null) && (temp.length() > 0))
          arr.add(temp);
        String temp1 = bVOs[i].getCorder_bid();
        if ((temp1 != null) && (temp1.length() > 0))
          arr1.add(temp1);
      }
      if (arr.size() > 0) {
        sPrimaryKeys = new String[arr.size()];
        arr.toArray(sPrimaryKeys);
      }
      if (arr1.size() > 0) {
        sCorder_bIds = new String[arr1.size()];
        arr1.toArray(sCorder_bIds);
      }

      HashMap hInvoiceNum = new HashMap();
      if ((sPrimaryKeys != null) && (sPrimaryKeys.length > 0) && (arr1.size() > 0)) {
        hInvoiceNum = dmoInvoice.findInvoiceNumByInvoiceBPKMy(sPrimaryKeys);
      }
      timer.addExecutePhase("得到当前发票行的原有发票数量findInvoiceNumByInvoiceBPKMy");

      HashMap hAccumInvoiceNum = new HashMap();
      if ((sCorder_bIds != null) && (sCorder_bIds.length > 0))
      {
        hAccumInvoiceNum = dmoInvoice.findAccumInvoiceNumByScOrderBPKMy(sCorder_bIds);

        timer.addExecutePhase("得到累计发票数量findAccumInvoiceNumByScOrderBPKMy");
      }
      for (int i = 0; i < iRowCount; i++) {
        if ((bVOs[i].getCupsourcebillrowid() == null) || (bVOs[i].getCupsourcebillrowid().trim().equals(""))) {
          continue;
        }
        double nOriInvNum = 0.0D;

        if ((bVOs[i].getPrimaryKey() != null) && 
          (hInvoiceNum.get(bVOs[i].getPrimaryKey()) != null)) {
          nOriInvNum = ((UFDouble)hInvoiceNum.get(bVOs[i].getPrimaryKey())).doubleValue();
        }

        arrWriteBackId.add(bVOs[i].getCorder_bid());

        UFDouble temp = (UFDouble)hAccumInvoiceNum.get(bVOs[i].getCorder_bid());
        double nOriAccumInvNum = temp == null ? 0.0D : temp.doubleValue();
        double nNewAccumInvValue = 0.0D;
        if (bVOs[i].getStatus() == 3)
          nNewAccumInvValue = nOriAccumInvNum - nOriInvNum + 0.0D;
        else {
          nNewAccumInvValue = nOriAccumInvNum - nOriInvNum + bVOs[i].getNinvoicenum().doubleValue();
        }

        arrWriteBackScOrderNum.add(new UFDouble(nNewAccumInvValue));

        if ((bVOs[i].getNinvoicenum() != null) && (!bVOs[i].getNinvoicenum().equals(new UFDouble(0.0D))))
          arrWriteBackScOrderPrice.add(bVOs[i].getNmoney().div(bVOs[i].getNinvoicenum()));
        else {
          arrWriteBackScOrderPrice.add(null);
        }

      }

      if (arrWriteBackId.size() > 0) {
        sIds = new String[arrWriteBackId.size()];
        arrWriteBackId.toArray(sIds);
      }
      if (arrWriteBackScOrderNum.size() > 0) {
        ufScOrderNums = new UFDouble[arrWriteBackScOrderNum.size()];
        arrWriteBackScOrderNum.toArray(ufScOrderNums);
      }

      if (arrWriteBackScOrderPrice.size() > 0) {
        ufScOrderPrices = new UFDouble[arrWriteBackScOrderPrice.size()];
        arrWriteBackScOrderPrice.toArray(ufScOrderPrices);
      }

      timer.addExecutePhase("委外订单回写前的准备");

      if ((ufScOrderNums != null) && (ufScOrderNums.length > 0)) {
        double[] nums = new double[ufScOrderNums.length];
        double[] prices = new double[ufScOrderPrices.length];
        for (int i = 0; i < ufScOrderNums.length; i++) {
          nums[i] = ufScOrderNums[i].doubleValue();
        }

        for (int i = 0; i < ufScOrderPrices.length; i++) {
          prices[i] = ufScOrderPrices[i].doubleValue();
        }

        dmoInvoice.updateAccumInvoiceNumByScOrderBPKMy(sIds, nums, prices, invVO);

        timer.addExecutePhase("委外订单回写操作updateAccumInvoiceNumByScOrderBPKMy");
      }
      timer.showAllExecutePhase("回写单据委外订单操作writeBackToScOrder结束");
    }
    catch (Exception e) {
      PubDMO.throwBusinessException("nc.bs.pi.InvoiceBO.writeBackToScOrder(InvoiceVO) Exception!", e);
    }
  }
}