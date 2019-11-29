package nc.bs.ps.settle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pi.InvoiceDMO;
import nc.bs.pi.InvoiceImpl;
import nc.bs.ps.estimate.EstimateDMO;
import nc.bs.ps.vmi.VMIDMO;
import nc.bs.ps.vmi.VMIImpl;
import nc.bs.pu.pub.GetSysBillCode;
import nc.bs.pu.pub.PubDMO;
import nc.bs.pu.pub.PubImpl;
import nc.bs.pub.billcodemanage.BillcodeGenerater;
import nc.bs.pub.formulaparse.FormulaParse;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.scm.inter.ArapHelper;
import nc.bs.scm.pub.TempTableDMO;
import nc.itf.arap.prv.IArapBillPrivate;
import nc.itf.arap.pub.IArapForGYLPublic;
import nc.itf.ia.service.IIAToPUBill;
import nc.itf.ps.estimate.IEstimate;
import nc.itf.ps.settle.ISettle_yf;
import nc.itf.pu.inter.IPuToIc_SettleImpl;
import nc.itf.pu.pub.fw.LockTool;
import nc.itf.uap.busibean.ISysInitQry;
import nc.itf.uap.pf.IPFMetaModel;
import nc.itf.uap.pf.IplatFormEntry;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.arap.change.VoTools;
import nc.vo.arap.gyl.AdjuestVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ia.bill.BillHeaderVO;
import nc.vo.ia.bill.BillItemVO;
import nc.vo.ia.bill.BillVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pi.InvoiceHeaderVO;
import nc.vo.pi.InvoiceItemVO;
import nc.vo.pi.InvoiceVO;
import nc.vo.ps.estimate.EstimateVO;
import nc.vo.ps.estimate.GeneralBb3VO;
import nc.vo.ps.estimate.GeneralHHeaderVO;
import nc.vo.ps.estimate.GeneralHItemVO;
import nc.vo.ps.factor.CostfactorHeaderVO;
import nc.vo.ps.factor.CostfactorVO;
import nc.vo.ps.settle.FeeinvoiceVO;
import nc.vo.ps.settle.IBillHeaderVO;
import nc.vo.ps.settle.IBillItemVO;
import nc.vo.ps.settle.IdTsData;
import nc.vo.ps.settle.IinvoiceVO;
import nc.vo.ps.settle.MaterialVO;
import nc.vo.ps.settle.OorderVO;
import nc.vo.ps.settle.SaledataVO;
import nc.vo.ps.settle.SettlebillHeaderVO;
import nc.vo.ps.settle.SettlebillItemVO;
import nc.vo.ps.settle.SettlebillVO;
import nc.vo.ps.settle.StockVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.pub.formulaset.VarryVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.para.SysInitVO;
import nc.vo.pub.pfflow04.MessagedriveVO;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.BillStatus;
import nc.vo.scm.pu.Timer;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.relacal.SCMRelationsCal;

public class SettleImpl_yf
  implements ISettle_yf, IPuToIc_SettleImpl
{
  private String[] status = { 
    "未暂估且未结算", 
    "已暂估且未结算", 
    "已暂估部分结算", 
    "已结算", 
    "已结案" };

  private BaseDAO basedao = new BaseDAO();

  private GeneralBillVO switchVOFromStock2IC(StockVO stockVO)
  {
    GeneralBillItemVO bodyVO = new GeneralBillItemVO();

    bodyVO.setCgeneralbid(stockVO.getCgeneralbid());
    bodyVO.setCgeneralhid(stockVO.getCgeneralhid());

    bodyVO.setCinventoryid(stockVO.getCmangid());
    bodyVO.setVbatchcode(stockVO.getVbatchcode());
    bodyVO.setNinnum(stockVO.getNinnum());
    bodyVO.setNmny(stockVO.getNmoney());
    bodyVO.setNprice(stockVO.getNprice());

    bodyVO.setCaccountunitid(stockVO.getCastunitid());
    bodyVO.setHsl(stockVO.getHsl());
    if ((stockVO.getHsl() != null) && (Math.abs(stockVO.getHsl().doubleValue()) > 0.0D)) stockVO.setNassistinnum(stockVO.getNinnum().div(stockVO.getHsl()));
    bodyVO.setNinassistnum(stockVO.getNassistinnum());

    bodyVO.setCfirstbillhid(stockVO.getCfirstbillhid());
    bodyVO.setCfirstbillbid(stockVO.getCfirstbillbid());
    bodyVO.setCfirsttype(stockVO.getCfirsttype());

    bodyVO.setVfree1(stockVO.getVfree1());
    bodyVO.setVfree2(stockVO.getVfree2());
    bodyVO.setVfree3(stockVO.getVfree3());
    bodyVO.setVfree4(stockVO.getVfree4());
    bodyVO.setVfree5(stockVO.getVfree5());

    bodyVO.setCprojectid(stockVO.getCprojectid());
    bodyVO.setCprojectphaseid(stockVO.getCprojectphaseid());

    bodyVO.setVuserdef1(stockVO.getVuserdefb1());
    bodyVO.setVuserdef2(stockVO.getVuserdefb2());
    bodyVO.setVuserdef3(stockVO.getVuserdefb3());
    bodyVO.setVuserdef4(stockVO.getVuserdefb4());
    bodyVO.setVuserdef5(stockVO.getVuserdefb5());
    bodyVO.setVuserdef6(stockVO.getVuserdefb6());
    bodyVO.setVuserdef7(stockVO.getVuserdefb7());
    bodyVO.setVuserdef8(stockVO.getVuserdefb8());
    bodyVO.setVuserdef9(stockVO.getVuserdefb9());
    bodyVO.setVuserdef10(stockVO.getVuserdefb10());

    bodyVO.setAttributeValue("vuserdef11", stockVO.getVuserdefb11());
    bodyVO.setAttributeValue("vuserdef12", stockVO.getVuserdefb12());
    bodyVO.setAttributeValue("vuserdef13", stockVO.getVuserdefb13());
    bodyVO.setAttributeValue("vuserdef14", stockVO.getVuserdefb14());
    bodyVO.setAttributeValue("vuserdef15", stockVO.getVuserdefb15());
    bodyVO.setAttributeValue("vuserdef16", stockVO.getVuserdefb16());
    bodyVO.setAttributeValue("vuserdef17", stockVO.getVuserdefb17());
    bodyVO.setAttributeValue("vuserdef18", stockVO.getVuserdefb18());
    bodyVO.setAttributeValue("vuserdef19", stockVO.getVuserdefb19());
    bodyVO.setAttributeValue("vuserdef20", stockVO.getVuserdefb20());

    bodyVO.setAttributeValue("pk_defdoc1", stockVO.getPk_defdocb1());
    bodyVO.setAttributeValue("pk_defdoc2", stockVO.getPk_defdocb2());
    bodyVO.setAttributeValue("pk_defdoc3", stockVO.getPk_defdocb3());
    bodyVO.setAttributeValue("pk_defdoc4", stockVO.getPk_defdocb4());
    bodyVO.setAttributeValue("pk_defdoc5", stockVO.getPk_defdocb5());
    bodyVO.setAttributeValue("pk_defdoc6", stockVO.getPk_defdocb6());
    bodyVO.setAttributeValue("pk_defdoc7", stockVO.getPk_defdocb7());
    bodyVO.setAttributeValue("pk_defdoc8", stockVO.getPk_defdocb8());
    bodyVO.setAttributeValue("pk_defdoc9", stockVO.getPk_defdocb9());
    bodyVO.setAttributeValue("pk_defdoc10", stockVO.getPk_defdocb10());

    bodyVO.setAttributeValue("pk_defdoc11", stockVO.getPk_defdocb11());
    bodyVO.setAttributeValue("pk_defdoc12", stockVO.getPk_defdocb12());
    bodyVO.setAttributeValue("pk_defdoc13", stockVO.getPk_defdocb13());
    bodyVO.setAttributeValue("pk_defdoc14", stockVO.getPk_defdocb14());
    bodyVO.setAttributeValue("pk_defdoc15", stockVO.getPk_defdocb15());
    bodyVO.setAttributeValue("pk_defdoc16", stockVO.getPk_defdocb16());
    bodyVO.setAttributeValue("pk_defdoc17", stockVO.getPk_defdocb17());
    bodyVO.setAttributeValue("pk_defdoc18", stockVO.getPk_defdocb18());
    bodyVO.setAttributeValue("pk_defdoc19", stockVO.getPk_defdocb19());
    bodyVO.setAttributeValue("pk_defdoc20", stockVO.getPk_defdocb20());

    bodyVO.setDbizdate(stockVO.getDbizdate());
    bodyVO.setCvendorid(stockVO.getCvendorid());

    GeneralBillHeaderVO headVO = new GeneralBillHeaderVO();
    headVO.setPk_corp(stockVO.getPk_corp());
    headVO.setCbilltypecode(stockVO.getCbilltype());
    headVO.setCgeneralhid(stockVO.getCgeneralhid());

    headVO.setCdispatcherid(stockVO.getCdispatcherid());

    headVO.setCbiztypeid(stockVO.getCbiztype());
    headVO.setPk_calbody(stockVO.getCstoreorganization());
    headVO.setCwarehouseid(stockVO.getCwarehouseid());

    headVO.setCdptid(stockVO.getCdeptid());
    headVO.setCproviderid(stockVO.getCprovidermangid());

    headVO.setVnote(stockVO.getVnote());
    headVO.setCwhsmanagerid(stockVO.getCwhsmanagerid());
    headVO.setCbizid(stockVO.getCoperatorid());

    headVO.setVbillcode(stockVO.getVbillcode());
    headVO.setIscalculatedinvcost(stockVO.getBcalculatecost());

    headVO.setVuserdef1(stockVO.getVuserdefh1());
    headVO.setVuserdef2(stockVO.getVuserdefh2());
    headVO.setVuserdef3(stockVO.getVuserdefh3());
    headVO.setVuserdef4(stockVO.getVuserdefh4());
    headVO.setVuserdef5(stockVO.getVuserdefh5());
    headVO.setVuserdef6(stockVO.getVuserdefh6());
    headVO.setVuserdef7(stockVO.getVuserdefh7());
    headVO.setVuserdef8(stockVO.getVuserdefh8());
    headVO.setVuserdef9(stockVO.getVuserdefh9());
    headVO.setVuserdef10(stockVO.getVuserdefh10());

    headVO.setAttributeValue("vuserdef11", stockVO.getVuserdefh11());
    headVO.setAttributeValue("vuserdef12", stockVO.getVuserdefh12());
    headVO.setAttributeValue("vuserdef13", stockVO.getVuserdefh13());
    headVO.setAttributeValue("vuserdef14", stockVO.getVuserdefh14());
    headVO.setAttributeValue("vuserdef15", stockVO.getVuserdefh15());
    headVO.setAttributeValue("vuserdef16", stockVO.getVuserdefh16());
    headVO.setAttributeValue("vuserdef17", stockVO.getVuserdefh17());
    headVO.setAttributeValue("vuserdef18", stockVO.getVuserdefh18());
    headVO.setAttributeValue("vuserdef19", stockVO.getVuserdefh19());
    headVO.setAttributeValue("vuserdef20", stockVO.getVuserdefh20());

    headVO.setAttributeValue("pk_defdoc1", stockVO.getPk_defdoch1());
    headVO.setAttributeValue("pk_defdoc2", stockVO.getPk_defdoch2());
    headVO.setAttributeValue("pk_defdoc3", stockVO.getPk_defdoch3());
    headVO.setAttributeValue("pk_defdoc4", stockVO.getPk_defdoch4());
    headVO.setAttributeValue("pk_defdoc5", stockVO.getPk_defdoch5());
    headVO.setAttributeValue("pk_defdoc6", stockVO.getPk_defdoch6());
    headVO.setAttributeValue("pk_defdoc7", stockVO.getPk_defdoch7());
    headVO.setAttributeValue("pk_defdoc8", stockVO.getPk_defdoch8());
    headVO.setAttributeValue("pk_defdoc9", stockVO.getPk_defdoch9());
    headVO.setAttributeValue("pk_defdoc10", stockVO.getPk_defdoch10());

    headVO.setAttributeValue("pk_defdoc11", stockVO.getPk_defdoch11());
    headVO.setAttributeValue("pk_defdoc12", stockVO.getPk_defdoch12());
    headVO.setAttributeValue("pk_defdoc13", stockVO.getPk_defdoch13());
    headVO.setAttributeValue("pk_defdoc14", stockVO.getPk_defdoch14());
    headVO.setAttributeValue("pk_defdoc15", stockVO.getPk_defdoch15());
    headVO.setAttributeValue("pk_defdoc16", stockVO.getPk_defdoch16());
    headVO.setAttributeValue("pk_defdoc17", stockVO.getPk_defdoch17());
    headVO.setAttributeValue("pk_defdoc18", stockVO.getPk_defdoch18());
    headVO.setAttributeValue("pk_defdoc19", stockVO.getPk_defdoch19());
    headVO.setAttributeValue("pk_defdoc20", stockVO.getPk_defdoch20());

    GeneralBillVO VO = new GeneralBillVO(1);
    VO.setParentVO(headVO);
    VO.setChildrenVO(new GeneralBillItemVO[] { bodyVO });
    return VO;
  }

  private GeneralBillVO switchVOFromGeneral2IC(GeneralHHeaderVO generalHHeaderVO, GeneralHItemVO generalHItemVO)
  {
    GeneralBillItemVO bodyVO = new GeneralBillItemVO();

    bodyVO.setCgeneralbid(generalHItemVO.getCgeneralbid());
    bodyVO.setCgeneralhid(generalHItemVO.getCgeneralhid());

    bodyVO.setCinventoryid(generalHItemVO.getCinventoryid());
    bodyVO.setVbatchcode(generalHItemVO.getVbatchcode());
    bodyVO.setNinnum(generalHItemVO.getNinnum());
    bodyVO.setNmny(generalHItemVO.getNmny());
    bodyVO.setNprice(generalHItemVO.getNprice());

    bodyVO.setCaccountunitid(generalHItemVO.getCastunitid());
    bodyVO.setHsl(generalHItemVO.getHsl());
    if ((generalHItemVO.getNinnum() != null) && (generalHItemVO.getHsl() != null) && (Math.abs(generalHItemVO.getHsl().doubleValue()) > 0.0D)) generalHItemVO.setNinassistnum(generalHItemVO.getNinnum().div(generalHItemVO.getHsl()));
    bodyVO.setNinassistnum(generalHItemVO.getNinassistnum());

    bodyVO.setCfirstbillhid(generalHItemVO.getCfirstbillhid());
    bodyVO.setCfirstbillbid(generalHItemVO.getCfirstbillbid());
    bodyVO.setCfirsttype(generalHItemVO.getCfirsttype());

    bodyVO.setVfree1(generalHItemVO.getVfree1());
    bodyVO.setVfree2(generalHItemVO.getVfree2());
    bodyVO.setVfree3(generalHItemVO.getVfree3());
    bodyVO.setVfree4(generalHItemVO.getVfree4());
    bodyVO.setVfree5(generalHItemVO.getVfree5());

    bodyVO.setCprojectid(generalHItemVO.getCprojectid());
    bodyVO.setCprojectphaseid(generalHItemVO.getCprojectphaseid());

    bodyVO.setVuserdef1(generalHItemVO.getVuserdef1());
    bodyVO.setVuserdef2(generalHItemVO.getVuserdef2());
    bodyVO.setVuserdef3(generalHItemVO.getVuserdef3());
    bodyVO.setVuserdef4(generalHItemVO.getVuserdef4());
    bodyVO.setVuserdef5(generalHItemVO.getVuserdef5());
    bodyVO.setVuserdef6(generalHItemVO.getVuserdef6());
    bodyVO.setVuserdef7(generalHItemVO.getVuserdef7());
    bodyVO.setVuserdef8(generalHItemVO.getVuserdef8());
    bodyVO.setVuserdef9(generalHItemVO.getVuserdef9());
    bodyVO.setVuserdef10(generalHItemVO.getVuserdef10());

    bodyVO.setAttributeValue("vuserdef11", generalHItemVO.getVuserdef11());
    bodyVO.setAttributeValue("vuserdef12", generalHItemVO.getVuserdef12());
    bodyVO.setAttributeValue("vuserdef13", generalHItemVO.getVuserdef13());
    bodyVO.setAttributeValue("vuserdef14", generalHItemVO.getVuserdef14());
    bodyVO.setAttributeValue("vuserdef15", generalHItemVO.getVuserdef15());
    bodyVO.setAttributeValue("vuserdef16", generalHItemVO.getVuserdef16());
    bodyVO.setAttributeValue("vuserdef17", generalHItemVO.getVuserdef17());
    bodyVO.setAttributeValue("vuserdef18", generalHItemVO.getVuserdef18());
    bodyVO.setAttributeValue("vuserdef19", generalHItemVO.getVuserdef19());
    bodyVO.setAttributeValue("vuserdef20", generalHItemVO.getVuserdef20());

    bodyVO.setAttributeValue("pk_defdoc1", generalHItemVO.getPk_defdoc1());
    bodyVO.setAttributeValue("pk_defdoc2", generalHItemVO.getPk_defdoc2());
    bodyVO.setAttributeValue("pk_defdoc3", generalHItemVO.getPk_defdoc3());
    bodyVO.setAttributeValue("pk_defdoc4", generalHItemVO.getPk_defdoc4());
    bodyVO.setAttributeValue("pk_defdoc5", generalHItemVO.getPk_defdoc5());
    bodyVO.setAttributeValue("pk_defdoc6", generalHItemVO.getPk_defdoc6());
    bodyVO.setAttributeValue("pk_defdoc7", generalHItemVO.getPk_defdoc7());
    bodyVO.setAttributeValue("pk_defdoc8", generalHItemVO.getPk_defdoc8());
    bodyVO.setAttributeValue("pk_defdoc9", generalHItemVO.getPk_defdoc9());
    bodyVO.setAttributeValue("pk_defdoc10", generalHItemVO.getPk_defdoc10());

    bodyVO.setAttributeValue("pk_defdoc11", generalHItemVO.getPk_defdoc11());
    bodyVO.setAttributeValue("pk_defdoc12", generalHItemVO.getPk_defdoc12());
    bodyVO.setAttributeValue("pk_defdoc13", generalHItemVO.getPk_defdoc13());
    bodyVO.setAttributeValue("pk_defdoc14", generalHItemVO.getPk_defdoc14());
    bodyVO.setAttributeValue("pk_defdoc15", generalHItemVO.getPk_defdoc15());
    bodyVO.setAttributeValue("pk_defdoc16", generalHItemVO.getPk_defdoc16());
    bodyVO.setAttributeValue("pk_defdoc17", generalHItemVO.getPk_defdoc17());
    bodyVO.setAttributeValue("pk_defdoc18", generalHItemVO.getPk_defdoc18());
    bodyVO.setAttributeValue("pk_defdoc19", generalHItemVO.getPk_defdoc19());
    bodyVO.setAttributeValue("pk_defdoc20", generalHItemVO.getPk_defdoc20());

    bodyVO.setDbizdate(generalHItemVO.getDbizdate());
    bodyVO.setCvendorid(generalHItemVO.getCvendorid());

    GeneralBillHeaderVO headVO = new GeneralBillHeaderVO();
    headVO.setPk_corp(generalHHeaderVO.getPk_corp());
    headVO.setCbilltypecode(generalHHeaderVO.getCbilltypecode());
    headVO.setCgeneralhid(generalHHeaderVO.getCgeneralhid());

    headVO.setCdispatcherid(generalHHeaderVO.getCdispatcherid());

    headVO.setCbiztypeid(generalHHeaderVO.getCbiztype());
    headVO.setPk_calbody(generalHHeaderVO.getCstoreorganization());
    headVO.setCwarehouseid(generalHHeaderVO.getCwarehouseid());

    headVO.setCdptid(generalHHeaderVO.getCdptid());
    headVO.setCproviderid(generalHHeaderVO.getCproviderid());

    headVO.setVnote(generalHHeaderVO.getVnote());
    headVO.setCwhsmanagerid(generalHHeaderVO.getCwhsmanagerid());
    headVO.setCbizid(generalHHeaderVO.getCoperatorid());

    headVO.setVbillcode(generalHHeaderVO.getVbillcode());
    headVO.setIscalculatedinvcost(generalHHeaderVO.getBcalculatecost());

    headVO.setVuserdef1(generalHHeaderVO.getVuserdef1());
    headVO.setVuserdef2(generalHHeaderVO.getVuserdef2());
    headVO.setVuserdef3(generalHHeaderVO.getVuserdef3());
    headVO.setVuserdef4(generalHHeaderVO.getVuserdef4());
    headVO.setVuserdef5(generalHHeaderVO.getVuserdef5());
    headVO.setVuserdef6(generalHHeaderVO.getVuserdef6());
    headVO.setVuserdef7(generalHHeaderVO.getVuserdef7());
    headVO.setVuserdef8(generalHHeaderVO.getVuserdef8());
    headVO.setVuserdef9(generalHHeaderVO.getVuserdef9());
    headVO.setVuserdef10(generalHHeaderVO.getVuserdef10());

    headVO.setAttributeValue("vuserdef11", generalHHeaderVO.getVuserdef11());
    headVO.setAttributeValue("vuserdef12", generalHHeaderVO.getVuserdef12());
    headVO.setAttributeValue("vuserdef13", generalHHeaderVO.getVuserdef13());
    headVO.setAttributeValue("vuserdef14", generalHHeaderVO.getVuserdef14());
    headVO.setAttributeValue("vuserdef15", generalHHeaderVO.getVuserdef15());
    headVO.setAttributeValue("vuserdef16", generalHHeaderVO.getVuserdef16());
    headVO.setAttributeValue("vuserdef17", generalHHeaderVO.getVuserdef17());
    headVO.setAttributeValue("vuserdef18", generalHHeaderVO.getVuserdef18());
    headVO.setAttributeValue("vuserdef19", generalHHeaderVO.getVuserdef19());
    headVO.setAttributeValue("vuserdef20", generalHHeaderVO.getVuserdef20());

    headVO.setAttributeValue("pk_defdoc1", generalHHeaderVO.getPk_defdoc1());
    headVO.setAttributeValue("pk_defdoc2", generalHHeaderVO.getPk_defdoc2());
    headVO.setAttributeValue("pk_defdoc3", generalHHeaderVO.getPk_defdoc3());
    headVO.setAttributeValue("pk_defdoc4", generalHHeaderVO.getPk_defdoc4());
    headVO.setAttributeValue("pk_defdoc5", generalHHeaderVO.getPk_defdoc5());
    headVO.setAttributeValue("pk_defdoc6", generalHHeaderVO.getPk_defdoc6());
    headVO.setAttributeValue("pk_defdoc7", generalHHeaderVO.getPk_defdoc7());
    headVO.setAttributeValue("pk_defdoc8", generalHHeaderVO.getPk_defdoc8());
    headVO.setAttributeValue("pk_defdoc9", generalHHeaderVO.getPk_defdoc9());
    headVO.setAttributeValue("pk_defdoc10", generalHHeaderVO.getPk_defdoc10());

    headVO.setAttributeValue("pk_defdoc11", generalHHeaderVO.getPk_defdoc11());
    headVO.setAttributeValue("pk_defdoc12", generalHHeaderVO.getPk_defdoc12());
    headVO.setAttributeValue("pk_defdoc13", generalHHeaderVO.getPk_defdoc13());
    headVO.setAttributeValue("pk_defdoc14", generalHHeaderVO.getPk_defdoc14());
    headVO.setAttributeValue("pk_defdoc15", generalHHeaderVO.getPk_defdoc15());
    headVO.setAttributeValue("pk_defdoc16", generalHHeaderVO.getPk_defdoc16());
    headVO.setAttributeValue("pk_defdoc17", generalHHeaderVO.getPk_defdoc17());
    headVO.setAttributeValue("pk_defdoc18", generalHHeaderVO.getPk_defdoc18());
    headVO.setAttributeValue("pk_defdoc19", generalHHeaderVO.getPk_defdoc19());
    headVO.setAttributeValue("pk_defdoc20", generalHHeaderVO.getPk_defdoc20());

    GeneralBillVO VO = new GeneralBillVO(1);
    VO.setParentVO(headVO);
    VO.setChildrenVO(new GeneralBillItemVO[] { bodyVO });
    return VO;
  }

  private void deleteBillForARAP(String[] cGeneralhid, String cOperator)
    throws BusinessException
  {
    ArapHelper.deleteOutArapBillByPks(cGeneralhid);
  }

  private void deleteBillFromOutter(SettlebillVO settleVO, String strCorpId, String strOperatorId)
    throws BusinessException
  {
    IIAToPUBill myService = null;
    try {
      if ((settleVO == null) || (settleVO.getParentVO() == null) || 
        (settleVO.getParentVO().getPrimaryKey() == null)) {
        SCMEnv.out("无法获取结算单头ID，没有存货核算单据被删除，直接返回!");
        return;
      }

      myService = (IIAToPUBill)NCLocator.getInstance().lookup(
        IIAToPUBill.class.getName());
      myService.deleteBillFromOutterArray(new String[] { settleVO
        .getParentVO().getPrimaryKey() }, strCorpId, strOperatorId);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
  }

  public void deleteVendorRecord(String[] pk_cubasdoc)
    throws BusinessException
  {
    try
    {
      SettleDMO dmo = new SettleDMO();
      dmo.deleteVendorRecord(pk_cubasdoc);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
  }

  private void discardSettlebill(ArrayList listPara)
    throws BusinessException
  {
    Timer timer = new Timer();
    timer.start();
    if ((listPara == null) || (listPara.size() < 8)) {
      SCMEnv.out("程序BUG:传入参数错误，直接返回！");
      return;
    }
    SettlebillVO settleVO = (SettlebillVO)listPara.get(0);
    StockVO[] stockVOs = (StockVO[])listPara.get(1);
    IinvoiceVO[] iinvoiceVOs = (IinvoiceVO[])listPara.get(2);
    FeeinvoiceVO[] feeVOs = (FeeinvoiceVO[])listPara.get(3);
    FeeinvoiceVO[] discountVOs = (FeeinvoiceVO[])listPara.get(4);
    FeeinvoiceVO[] specialVOs = (FeeinvoiceVO[])listPara.get(5);
    String strOperatorId = (String)listPara.get(6);
    String strCorpId = (String)listPara.get(7);

    SettleDMO dmo = null;
    InvoiceImpl myService1 = null;
    ICreateCorpQueryService myService0 = null;
    boolean bLock = false;
    boolean bVirtual = false;

    Hashtable hStock = new Hashtable();
    Hashtable hInvoice = new Hashtable();
    if ((stockVOs != null) && (stockVOs.length > 0)) {
      for (int i = 0; i < stockVOs.length; i++)
        hStock.put(stockVOs[i].getCgeneralbid(), stockVOs[i].getBzgyfflag());
    }
    if ((iinvoiceVOs != null) && (iinvoiceVOs.length > 0)) {
      for (int i = 0; i < iinvoiceVOs.length; i++) {
        hInvoice.put(iinvoiceVOs[i].getCinvoice_bid(), new Integer[] { iinvoiceVOs[i].getIInvoiceType(), iinvoiceVOs[i].getIBillStatus() });
      }

    }

    String[] sKeys = getSettleLockKeys(stockVOs, iinvoiceVOs, feeVOs, 
      discountVOs, specialVOs, null, null);

    timer.addExecutePhase("组合所有需要加锁的主键");

    Vector v = new Vector();
    for (int i = 0; i < sKeys.length; i++) v.addElement(sKeys[i]);
    SettlebillItemVO[] bodyVO = settleVO.getBodyVO();
    v.addElement(settleVO.getHeadVO().getCsettlebillid());

    for (int i = 0; i < bodyVO.length; i++) {
      if ((bodyVO[i].getCvmiid() == null) || (v.contains(bodyVO[i].getCvmiid()))) continue; v.addElement(bodyVO[i].getCvmiid());
    }

    sKeys = new String[v.size()];
    v.copyInto(sKeys);
    try
    {
      dmo = new SettleDMO();

      bLock = LockTool.setLockForPks(sKeys, strOperatorId);
      if (!bLock) {
        throw new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("40040502", 
          "UPP40040502-000009"));
      }

      timer.addExecutePhase("对单据加锁");

      String sMessage = isTimeStampChangedForDepose(stockVOs, 
        iinvoiceVOs, feeVOs, discountVOs, specialVOs, 
        new SettlebillVO[] { settleVO });
      if ((sMessage != null) && (sMessage.length() > 0)) {
        sMessage = sMessage + NCLangResOnserver.getInstance()
          .getStrByID("40040502", "UPP40040502-000002");

        throw new BusinessException(sMessage);
      }

      timer.addExecutePhase("判断时间戳是否改变(总)");

      if ((hStock.size() > 0) && (hInvoice.size() > 0) && (settleVO != null)) {
        for (int i = 0; i < bodyVO.length; i++) {
          String cstockrow = bodyVO[i].getCstockrow();
          String cinvoice_bid = bodyVO[i].getCinvoice_bid();
          if ((cstockrow != null) && (cinvoice_bid != null) && (hStock.get(cstockrow) != null) && (hInvoice.get(cinvoice_bid) != null)) {
            UFBoolean bZGYF = (UFBoolean)hStock.get(cstockrow);
            Integer[] n = (Integer[])hInvoice.get(cinvoice_bid);
            int ibillstatus = n[1].intValue();
            if ((bZGYF.booleanValue()) && ((ibillstatus == BillStatus.AUDITING.intValue()) || (ibillstatus == BillStatus.AUDITED.intValue()))) {
              throw new BusinessException(NCLangResOnserver.getInstance()
                .getStrByID("40040502", "UPP40040502-000224"));
            }
          }
        }

      }

      if ((stockVOs != null) && (stockVOs.length > 0)) {
        dmo.updateStock(stockVOs, false);
      }

      timer.addExecutePhase("恢复入库单");

      if ((iinvoiceVOs != null) && (iinvoiceVOs.length > 0)) {
        dmo.updateInvoice(iinvoiceVOs);
      }

      timer.addExecutePhase("恢复发票");

      if ((feeVOs != null) && (feeVOs.length > 0)) {
        dmo.updateFee(feeVOs);
      }

      timer.addExecutePhase("恢复费用发票");

      if ((discountVOs != null) && (discountVOs.length > 0)) {
        dmo.updateDiscount(discountVOs);
      }

      timer.addExecutePhase("恢复折扣");

      if ((specialVOs != null) && (specialVOs.length > 0)) {
        dmo.updateDiscount(specialVOs);
      }

      timer.addExecutePhase("恢复一般发票");

      if (settleVO != null) {
        bVirtual = discardVirtualInvoices(settleVO);
      }

      timer.addExecutePhase("作废虚拟发票");

      dmo.discardSettlebill(settleVO);

      timer.addExecutePhase("作废结算单");

      boolean bIsFrmSaleData = false;
      if ((bodyVO != null) && (bodyVO.length > 0) && 
        (bodyVO[0] != null) && 
        (bodyVO[0].getCsale_bid() != null) && 
        (bodyVO[0].getCsale_bid().trim().length() > 0)) {
        bIsFrmSaleData = true;
      }
      if (bIsFrmSaleData)
      {
        int iLen = bodyVO == null ? 0 : bodyVO.length;
        for (int i = 0; i < iLen; i++) {
          if (bodyVO[i].getNsettlenum() != null) {
            bodyVO[i].setNsettlenum(bodyVO[i].getNsettlenum()
              .multiply(-1.0D));
          }
        }
        dmo.updateSaleDataBatch(bodyVO, false);

        if ((iinvoiceVOs != null) && (iinvoiceVOs.length > 0)) dmo.updateAccumInvoiceNumForOrder(iinvoiceVOs);

        timer.addExecutePhase("受托代销结算单回写销售发票的累计结算数量");

        Vector vTemp = new Vector();
        String s1 = bodyVO[0].getCinvoiceid();
        if ((s1 != null) && (s1.trim().length() > 0))
          vTemp.addElement(s1.trim());
        for (int i = 1; i < bodyVO.length; i++) {
          s1 = bodyVO[i].getCinvoiceid();
          if ((s1 == null) || (s1.trim().length() <= 0) || 
            (vTemp.contains(s1.trim()))) continue;
          vTemp.addElement(s1.trim());
        }

        if (vTemp.size() > 0) {
          String[] s = new String[vTemp.size()];
          vTemp.copyInto(s);

          myService1 = new InvoiceImpl();
          myService1.discardInvoiceForSettle(s);
        }

        timer.addExecutePhase("受托代销结算单作废发票");
      }

      rewriteVMIAntiSettle(settleVO);

      timer.addExecutePhase("回写VMI明细");

      if (settleVO != null) {
        String unitCode = settleVO.getHeadVO().getPk_corp();
        myService0 = (ICreateCorpQueryService)
          NCLocator.getInstance().lookup(
          ICreateCorpQueryService.class.getName());

        boolean bIsFromVMI = isFromVMI(settleVO);

        boolean bIAStartUp = myService0.isEnabled(unitCode, "IA");
        if (bIAStartUp)
        {
          if (!bIsFromVMI) {
            String[] s = new String[1];
            s[0] = settleVO.getBodyVO()[0].getCsettlebillid();
            deleteBillFromOutters(s, strCorpId, strOperatorId);

            timer.addExecutePhase("结算单作废时向存货核算系统传送数据-非VMI");
          }
          else {
            deleteBillFromOutter(settleVO, strCorpId, strOperatorId);

            timer.addExecutePhase("结算单作废时向存货核算系统传送数据-VMI");
          }

        }

        if (!bIsFromVMI) {
          boolean bAPStartUp = myService0.isEnabled(unitCode, "AP");
          if (bAPStartUp)
          {
            if ((stockVOs != null) && (stockVOs.length > 0))
            {
              IArapForGYLPublic iArap = (IArapForGYLPublic)NCLocator.getInstance().lookup(IArapForGYLPublic.class.getName());
              iArap.unAdjuest(settleVO.getHeadVO().getCsettlebillid(), unitCode);
            }
          }
        }

      }

      timer.showAllExecutePhase("作废结算单(discardSettlebill(ArrayList))时间分布");
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
      
      
      //反编译后报错，edit by yqq  2016-07-18
    /* try
      {
        if (bLock)
        {
          LockTool.releaseLockForPks(sKeys, strOperatorId);
        }
      }
      catch (Exception e)
      {
        PubDMO.throwBusinessException(e);
      }*/
    }
    finally
    {
      try
      {
        if (bLock)
        {
          LockTool.releaseLockForPks(sKeys, strOperatorId);
        }
      }
      catch (Exception e)
      {
        PubDMO.throwBusinessException(e);
      }
    }
  }

  public void discardSettlebillSpecially(InvoiceVO invoiceVO, String cOperator)
    throws BusinessException
  {
    Timer timer = new Timer();
    timer.start();
    SettleDMO dmo = null;
    ICreateCorpQueryService myService0 = null;
    try
    {
      InvoiceHeaderVO invoiceHeadVO = invoiceVO.getHeadVO();
      String invoiceKey = invoiceHeadVO.getCinvoiceid().trim();
      String sCondition = " and cinvoiceid = '" + invoiceKey + "'";
      dmo = new SettleDMO();
      SettlebillItemVO[] settlebillItemVOs = dmo.querySettleBody(
        invoiceHeadVO.getPk_corp(), sCondition);
      if ((settlebillItemVOs == null) || (settlebillItemVOs.length == 0)) {
        throw new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("40040502", 
          "UPP40040502-000003"));
      }
      timer.addExecutePhase("通过发票查找结算单--表体");

      String settlebillKey = settlebillItemVOs[0].getCsettlebillid()
        .trim();
      sCondition = " and A.csettlebillid = '" + settlebillKey + "'";
      SettlebillHeaderVO[] settlebillHeaderVOs = dmo
        .querySettlebillHead_Stock(invoiceHeadVO.getPk_corp(), 
        sCondition);
      if ((settlebillHeaderVOs == null) || (settlebillHeaderVOs.length == 0)) {
        throw new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("40040502", 
          "UPP40040502-000003"));
      }
      timer.addExecutePhase("通过发票查找结算单--表头");

      SettlebillVO vo = new SettlebillVO(settlebillItemVOs.length);
      vo.setParentVO(settlebillHeaderVOs[0]);
      vo.setChildrenVO(settlebillItemVOs);
      dmo.discardSettlebill(vo);

      timer.addExecutePhase("作废结算单");

      if (vo != null) {
        String unitCode = vo.getHeadVO().getPk_corp();
        myService0 = (ICreateCorpQueryService)
          NCLocator.getInstance().lookup(
          ICreateCorpQueryService.class.getName());
        boolean bIAStartUp = myService0.isEnabled(unitCode, "IA");
        if (bIAStartUp) {
          String[] s = new String[1];
          s[0] = vo.getHeadVO().getCsettlebillid();
          deleteBillFromOutters(s, unitCode, cOperator);
        }
      }
      timer.addExecutePhase("结算单作废时向存货核算系统传送数据");

      GetSysBillCode billCode = new GetSysBillCode();
      billCode.returnBillNo(vo);
      timer.addExecutePhase("回退结算单号");

      timer.showAllExecutePhase("作废结算单，专为直运业务类型时发票作废调用时间分布");
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
  }

  private boolean discardVirtualInvoices(SettlebillVO VO)
    throws BusinessException
  {
    boolean bVirtual = false;
    SettlebillItemVO[] itemVOs = VO.getBodyVO();
    if ((itemVOs == null) || (itemVOs.length == 0)) {
      return bVirtual;
    }
    String[] cInvoiceID = (String[])null;
    Vector vTemp1 = new Vector(); Vector vTemp2 = new Vector();
    for (int i = 0; i < itemVOs.length; i++) {
      String sTemp = itemVOs[i].getCinvoiceid();
      if ((sTemp != null) && (sTemp.trim().length() > 0) && (!vTemp1.contains(sTemp))) {
        vTemp1.addElement(sTemp);
      }

      InvoiceItemVO itemVO = new InvoiceItemVO();
      itemVO.setNinvoicenum(itemVOs[i].getNsettlenum().multiply(-1.0D));
      itemVO.setCupsourcebillrowid(itemVOs[i].getCstockrow());
      vTemp2.addElement(itemVO);
    }
    if (vTemp1.size() == 0)
      return bVirtual;
    cInvoiceID = new String[vTemp1.size()];
    vTemp1.copyInto(cInvoiceID);

    InvoiceImpl myService = null;
    try {
      SettleDMO dmo = new SettleDMO();
      cInvoiceID = dmo.getVirtualInvoices(cInvoiceID);
      if ((cInvoiceID != null) && (cInvoiceID.length > 0))
      {
        myService = new InvoiceImpl();
        myService.discardInvoiceForSettle(cInvoiceID);
        bVirtual = true;

        if (vTemp2.size() > 0) {
          InvoiceItemVO[] itemVO = new InvoiceItemVO[vTemp2.size()];
          vTemp2.copyInto(itemVO);
          dmo.updateSignNum(itemVO);
        }
      }
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }

    return bVirtual;
  }

  private ArrayList doAutoBalance(ArrayList listPara, IinvoiceVO[] m_invoiceVOs, StockVO[] m_stockVOs, boolean m_bStockRB, boolean m_bInvoiceRB)
    throws BusinessException
  {
    Timer timeDebug = new Timer();
    timeDebug.start();

    String m_sUnitCode = (String)listPara.get(0);
    Vector vAllItems = new Vector();
    ArrayList listRet = new ArrayList();

    boolean[] b1 = (boolean[])null;
    boolean[] b2 = (boolean[])null;
    if ((m_stockVOs != null) && (m_stockVOs.length > 0)) {
      b1 = new boolean[m_stockVOs.length];
      for (int i = 0; i < m_stockVOs.length; i++)
        b1[i] = false;
    }
    if ((m_invoiceVOs != null) && (m_invoiceVOs.length > 0)) {
      b2 = new boolean[m_invoiceVOs.length];
      for (int i = 0; i < m_invoiceVOs.length; i++)
        b2[i] = false;
    }
    timeDebug.addExecutePhase("初始化未结算标志");

    String sBiztype1 = null; String sVendorID1 = null; String sDeptID1 = null; String sInvID1 = null; String pk_corp1 = null;
    String sBiztype2 = null; String sVendorID2 = null; String sDeptID2 = null; String sInvID2 = null; String pk_corp2 = null;
    String s = null; String ss = null;
    double nNosettlenum1 = 0.0D; double nNosettlemny1 = 0.0D;
    double nNosettlenum2 = 0.0D; double nNosettlemny2 = 0.0D;

    StockVO[] stockVOs = (StockVO[])null;
    if (m_bStockRB) {
      Vector v1 = new Vector();
      if ((m_stockVOs != null) && (m_stockVOs.length > 0)) {
        Vector vv = new Vector();
        for (int i = 0; i < m_stockVOs.length - 1; i++) {
        	
            //反编译后报错，edit by yqq  2016-07-18      	
         if (!b1[i] || (m_stockVOs[i].getBzgyfflag().booleanValue())) {
 //      if ((b1[i] != 0) || (m_stockVOs[i].getBzgyfflag().booleanValue())) {
            continue;
          }
          sBiztype1 = m_stockVOs[i].getCbiztype();
          if ((sBiztype1 == null) || (sBiztype1.trim().length() == 0))
            continue;
          sBiztype1 = sBiztype1.trim();
          sVendorID1 = m_stockVOs[i].getCproviderbaseid();
          if ((sVendorID1 == null) || (sVendorID1.trim().length() == 0))
            continue;
          sVendorID1 = sVendorID1.trim();
          sDeptID1 = m_stockVOs[i].getCdeptid();
          if ((sDeptID1 == null) || (sDeptID1.trim().length() == 0))
            continue;
          sDeptID1 = sDeptID1.trim();
          pk_corp1 = m_stockVOs[i].getPk_stockcorp();
          if ((pk_corp1 != null) && (pk_corp1.trim().length() != 0)) {
            pk_corp1 = pk_corp1.trim();
            sInvID1 = m_stockVOs[i].getCbaseid().trim();
            nNosettlenum1 = m_stockVOs[i].getNnosettlenum()
              .doubleValue();
            nNosettlemny1 = m_stockVOs[i].getNnosettlemny()
              .doubleValue();
            for (int j = i + 1; j < m_stockVOs.length; j++) {
            	
                //反编译后报错，edit by yqq  2016-07-18      	
              if (!b1[j] || (m_stockVOs[j].getBzgyfflag().booleanValue())) {           	
       //     if ((b1[j] != 0) || (m_stockVOs[j].getBzgyfflag().booleanValue())) {

                continue;
              }
              sBiztype2 = m_stockVOs[j].getCbiztype();
              if ((sBiztype2 == null) || (sBiztype2.trim().length() == 0))
                continue;
              sBiztype2 = sBiztype2.trim();
              sVendorID2 = m_stockVOs[j].getCproviderbaseid();
              if ((sVendorID2 == null) || 
                (sVendorID2.trim().length() == 0))
                continue;
              sVendorID2 = sVendorID2.trim();
              sDeptID2 = m_stockVOs[j].getCdeptid();
              if ((sDeptID2 == null) || (sDeptID2.trim().length() == 0))
                continue;
              sDeptID2 = sDeptID2.trim();
              pk_corp2 = m_stockVOs[j].getPk_stockcorp();
              if ((pk_corp2 != null) && (pk_corp2.trim().length() != 0)) {
                pk_corp2 = pk_corp2.trim();
                sInvID2 = m_stockVOs[j].getCbaseid().trim();
                nNosettlenum2 = m_stockVOs[j].getNnosettlenum()
                  .doubleValue();
                nNosettlemny2 = m_stockVOs[j].getNnosettlemny()
                  .doubleValue();
                if ((!sBiztype1.equals(sBiztype2)) || 
                  (!sVendorID1.equals(sVendorID2)) || 
                  (!sDeptID1.equals(sDeptID2)) || 
                  (!sInvID1.equals(sInvID2)) || 
                  (!pk_corp1.equals(pk_corp2)) || 
                  (nNosettlenum1 != -nNosettlenum2) || 
                  (nNosettlemny1 != -nNosettlemny2)) continue;
                v1.addElement(m_stockVOs[i]);
                v1.addElement(m_stockVOs[j]);
                b1[j] = true;
                b1[i] = true;
                break;
              }
            }
            //反编译后报错，edit by yqq  2016-07-18      	 
            if (!b1[i])
   //       if (b1[i] == 0)
              vv.addElement(m_stockVOs[i]); 
          }
        }
        //反编译后报错，edit by yqq  2016-07-18      	       
        if(!b1[m_stockVOs.length - 1])
 //     if (b1[(m_stockVOs.length - 1)] == 0)
          vv.addElement(m_stockVOs[(m_stockVOs.length - 1)]);
        stockVOs = new StockVO[vv.size()];
        vv.copyInto(stockVOs);
        if (v1.size() > 0) {
          Vector vTmp = doAutoBalanceStockRedBlue(m_sUnitCode, v1);
          if ((vTmp != null) && (vTmp.size() > 0)) {
            int iSize = vTmp.size();
            for (int i = 0; i < iSize; i++)
              vAllItems.addElement(vTmp.elementAt(i));
          }
        }
      }
    }
    else {
      stockVOs = m_stockVOs;
    }
    timeDebug.addExecutePhase("组合能自动结算的红/蓝入库单，并过滤掉满足自动结算条件的入库单");

    IinvoiceVO[] invoiceVOs = (IinvoiceVO[])null;
    if (m_bInvoiceRB) {
      Vector v2 = new Vector();
      if ((m_invoiceVOs != null) && (m_invoiceVOs.length > 0)) {
        Vector vv = new Vector();
        for (int i = 0; i < m_invoiceVOs.length - 1; i++) {
            //反编译后报错，edit by yqq  2016-07-18      	 
          if (!b2[i]) {
   //     if (b2[i] != 0) {
            continue;
          }
          sBiztype1 = m_invoiceVOs[i].getCbiztype();
          if ((sBiztype1 == null) || (sBiztype1.trim().length() == 0))
            continue;
          sBiztype1 = sBiztype1.trim();
          sVendorID1 = m_invoiceVOs[i].getCvendorbaseid();
          if ((sVendorID1 == null) || (sVendorID1.trim().length() == 0))
            continue;
          sVendorID1 = sVendorID1.trim();
          sDeptID1 = m_invoiceVOs[i].getCdeptid();
          if ((sDeptID1 == null) || (sDeptID1.trim().length() == 0))
            continue;
          sDeptID1 = sDeptID1.trim();
          sInvID1 = m_invoiceVOs[i].getCbaseid().trim();
          nNosettlenum1 = m_invoiceVOs[i].getNnosettlenum()
            .doubleValue();
          nNosettlemny1 = m_invoiceVOs[i].getNnosettlemny()
            .doubleValue();
          for (int j = i + 1; j < m_invoiceVOs.length; j++) {
              //反编译后报错，edit by yqq  2016-07-18   
            if (!b2[j]) {
     //     if (b2[j] != 0) {
              continue;
            }
            sBiztype2 = m_invoiceVOs[j].getCbiztype();
            if ((sBiztype2 == null) || (sBiztype2.trim().length() == 0))
              continue;
            sBiztype2 = sBiztype2.trim();
            sVendorID2 = m_invoiceVOs[j].getCvendorbaseid();
            if ((sVendorID2 == null) || 
              (sVendorID2.trim().length() == 0))
              continue;
            sVendorID2 = sVendorID2.trim();
            sDeptID2 = m_invoiceVOs[j].getCdeptid();
            if ((sDeptID2 == null) || (sDeptID2.trim().length() == 0))
              continue;
            sDeptID2 = sDeptID2.trim();
            sInvID2 = m_invoiceVOs[j].getCbaseid().trim();
            nNosettlenum2 = m_invoiceVOs[j].getNnosettlenum()
              .doubleValue();
            nNosettlemny2 = m_invoiceVOs[j].getNnosettlemny()
              .doubleValue();
            if ((!sBiztype1.equals(sBiztype2)) || 
              (!sVendorID1.equals(sVendorID2)) || 
              (!sDeptID1.equals(sDeptID2)) || 
              (!sInvID1.equals(sInvID2)) || 
              (nNosettlenum1 != -nNosettlenum2) || 
              (nNosettlemny1 != -nNosettlemny2)) continue;
            v2.addElement(m_invoiceVOs[i]);
            v2.addElement(m_invoiceVOs[j]);
            b2[j] = true;
            b2[i] = true;
            break;
          }
          //反编译后报错，edit by yqq  2016-07-18 
          if (!b2[i])
 //       if (b2[i] == 0)
            vv.addElement(m_invoiceVOs[i]);
        }
        
        //反编译后报错，edit by yqq  2016-07-18 
        if (!b2[(m_invoiceVOs.length - 1)])
 //     if (b2[(m_invoiceVOs.length - 1)] == 0)
          vv.addElement(m_invoiceVOs[(m_invoiceVOs.length - 1)]);
        invoiceVOs = new IinvoiceVO[vv.size()];
        vv.copyInto(invoiceVOs);
        if (v2.size() > 0) {
          Vector vTmp = doAutoBalanceInvoiceRedBlue(m_sUnitCode, v2);
          if ((vTmp != null) && (vTmp.size() > 0)) {
            int iSize = vTmp.size();
            for (int i = 0; i < iSize; i++)
              vAllItems.addElement(vTmp.elementAt(i));
          }
        }
      }
    }
    else {
      invoiceVOs = m_invoiceVOs;
    }
    timeDebug.addExecutePhase("组合能自动结算的红/蓝入库单，并过滤掉满足自动结算条件的入库单");

    Vector v = new Vector();
    if ((stockVOs != null) && (invoiceVOs != null)) {
      b1 = new boolean[invoiceVOs.length];
      for (int i = 0; i < b1.length; i++)
        b1[i] = false;
      b2 = new boolean[stockVOs.length];
      for (int i = 0; i < b2.length; i++) {
        b2[i] = false;
      }

      for (int i = 0; i < invoiceVOs.length; i++) {
          //反编译后报错，edit by yqq  2016-07-18 
        if (!b1[i]) {
  //    if (b1[i] != 0) {
          continue;
        }
        sBiztype1 = invoiceVOs[i].getCbiztype();
        if ((sBiztype1 == null) || (sBiztype1.trim().length() == 0))
          continue;
        sBiztype1 = sBiztype1.trim();
        sVendorID1 = invoiceVOs[i].getCvendorbaseid();
        if ((sVendorID1 == null) || (sVendorID1.trim().length() == 0))
          continue;
        sVendorID1 = sVendorID1.trim();
        sDeptID1 = invoiceVOs[i].getCdeptid();
        if ((sDeptID1 == null) || (sDeptID1.trim().length() == 0))
          continue;
        sDeptID1 = sDeptID1.trim();
        sInvID1 = invoiceVOs[i].getCbaseid().trim();
        nNosettlenum1 = invoiceVOs[i].getNnosettlenum().doubleValue();
        s = invoiceVOs[i].getCupsourcebillrowid();
        if ((s == null) || (s.trim().length() == 0))
          continue;
        for (int j = 0; j < stockVOs.length; j++) {
            //反编译后报错，edit by yqq  2016-07-18 
          if (!b2[j]) {
 //       if (b2[j] != 0) {
            continue;
          }
          sBiztype2 = stockVOs[j].getCbiztype();
          if ((sBiztype2 == null) || (sBiztype2.trim().length() == 0))
            continue;
          sBiztype2 = sBiztype2.trim();
          sVendorID2 = stockVOs[j].getCproviderbaseid();
          if ((sVendorID2 == null) || (sVendorID2.trim().length() == 0))
            continue;
          sVendorID2 = sVendorID2.trim();
          sDeptID2 = stockVOs[j].getCdeptid();
          if ((sDeptID2 == null) || (sDeptID2.trim().length() == 0))
            continue;
          sDeptID2 = sDeptID2.trim();
          sInvID2 = stockVOs[j].getCbaseid().trim();
          nNosettlenum2 = stockVOs[j].getNnosettlenum().doubleValue();
          ss = stockVOs[j].getCgeneralbid();
          if ((!sBiztype1.equals(sBiztype2)) || 
            (!sVendorID1.equals(sVendorID2)) || 
            (!sDeptID1.equals(sDeptID2)) || 
            (!sInvID1.equals(sInvID2)) || 
            (nNosettlenum1 != nNosettlenum2) || (!s.equals(ss))) continue;
          Vector vTemp = new Vector();
          vTemp.addElement(stockVOs[j]);
          vTemp.addElement(invoiceVOs[i]);
          v.addElement(vTemp);
          b1[i] = true;
          b2[j] = true;
        }

      }

      for (int i = 0; i < invoiceVOs.length; i++) {
        s = invoiceVOs[i].getCsourcebillbid();
        if ((s == null) || (s.trim().length() == 0)) {
          continue;
        }
        //反编译后报错，edit by yqq  2016-07-18 
        if (!b1[i]) {
  //    if (b1[i] != 0) {
          continue;
        }
        sBiztype1 = invoiceVOs[i].getCbiztype();
        if ((sBiztype1 == null) || (sBiztype1.trim().length() == 0))
          continue;
        sBiztype1 = sBiztype1.trim();
        sVendorID1 = invoiceVOs[i].getCvendormangid();
        if ((sVendorID1 == null) || (sVendorID1.trim().length() == 0))
          continue;
        sVendorID1 = sVendorID1.trim();
        sDeptID1 = invoiceVOs[i].getCdeptid();
        if ((sDeptID1 == null) || (sDeptID1.trim().length() == 0))
          continue;
        sDeptID1 = sDeptID1.trim();
        sInvID1 = invoiceVOs[i].getCmangid().trim();
        nNosettlenum1 = invoiceVOs[i].getNnosettlenum().doubleValue();
        for (int j = 0; j < stockVOs.length; j++) {
            //反编译后报错，edit by yqq  2016-07-18 
          if (!b2[j]) {
  //      if (b2[j] != 0) {
            continue;
          }
          if ((stockVOs[j].getCfirstbillbid() == null) || 
            (stockVOs[j].getCfirstbillbid().trim().equals("")) || 
            (!stockVOs[j].getCfirstbillbid().trim().equals(
            s.trim())))
            continue;
          sBiztype2 = stockVOs[j].getCbiztype();
          if ((sBiztype2 == null) || (sBiztype2.trim().length() == 0))
            continue;
          sBiztype2 = sBiztype2.trim();
          sVendorID2 = stockVOs[j].getCprovidermangid();
          if ((sVendorID2 == null) || (sVendorID2.trim().length() == 0))
            continue;
          sVendorID2 = sVendorID2.trim();
          sDeptID2 = stockVOs[j].getCdeptid();
          if ((sDeptID2 == null) || (sDeptID2.trim().length() == 0))
            continue;
          sDeptID2 = sDeptID2.trim();
          sInvID2 = stockVOs[j].getCmangid().trim();
          nNosettlenum2 = stockVOs[j].getNnosettlenum().doubleValue();
          ss = stockVOs[j].getCfirstbillbid();
          if ((!sBiztype1.equals(sBiztype2)) || 
            (!sVendorID1.equals(sVendorID2)) || 
            (!sDeptID1.equals(sDeptID2)) || 
            (!sInvID1.equals(sInvID2)) || 
            (nNosettlenum1 != nNosettlenum2) || (!s.equals(ss))) continue;
          Vector vTemp = new Vector();
          vTemp.addElement(stockVOs[j]);
          vTemp.addElement(invoiceVOs[i]);
          v.addElement(vTemp);
          b1[i] = true;
          b2[j] = true;
        }

      }

      for (int i = 0; i < invoiceVOs.length; i++)
      {
          //反编译后报错，edit by yqq  2016-07-18 
        if (!b1[i]) {
  //    if (b1[i] != 0) {
          continue;
        }
        sBiztype1 = invoiceVOs[i].getCbiztype();
        if ((sBiztype1 == null) || (sBiztype1.trim().length() == 0))
          continue;
        sBiztype1 = sBiztype1.trim();
        sVendorID1 = invoiceVOs[i].getCvendormangid();
        if ((sVendorID1 == null) || (sVendorID1.trim().length() == 0))
          continue;
        sVendorID1 = sVendorID1.trim();
        sDeptID1 = invoiceVOs[i].getCdeptid();
        if ((sDeptID1 == null) || (sDeptID1.trim().length() == 0))
          continue;
        sDeptID1 = sDeptID1.trim();
        sInvID1 = invoiceVOs[i].getCmangid().trim();
        nNosettlenum1 = invoiceVOs[i].getNnosettlenum().doubleValue();

        for (int j = 0; j < stockVOs.length; j++) {
            //反编译后报错，edit by yqq  2016-07-18 
          if (!b2[j]) {
  //      if (b2[j] != 0) {
            continue;
          }
          sBiztype2 = stockVOs[j].getCbiztype();
          if ((sBiztype2 == null) || (sBiztype2.trim().length() == 0)) {
            continue;
          }
          sBiztype2 = sBiztype2.trim();
          sVendorID2 = stockVOs[j].getCprovidermangid();
          if ((sVendorID2 == null) || (sVendorID2.trim().length() == 0)) {
            continue;
          }
          sVendorID2 = sVendorID2.trim();
          sDeptID2 = stockVOs[j].getCdeptid();
          if ((sDeptID2 == null) || (sDeptID2.trim().length() == 0)) {
            continue;
          }
          sDeptID2 = sDeptID2.trim();
          sInvID2 = stockVOs[j].getCmangid().trim();
          nNosettlenum2 = stockVOs[j].getNnosettlenum().doubleValue();
          if ((!sBiztype1.equals(sBiztype2)) || 
            (!sVendorID1.equals(sVendorID2)) || 
            (!sDeptID1.equals(sDeptID2)) || 
            (!sInvID1.equals(sInvID2)) || 
            (nNosettlenum1 != nNosettlenum2)) continue;
          Vector vTemp = new Vector();
          vTemp.addElement(stockVOs[j]);
          vTemp.addElement(invoiceVOs[i]);
          v.addElement(vTemp);
          b1[i] = true;
          b2[j] = true;
        }

      }

      if (v.size() > 0) {
        Vector vTmp = doAutoBalanceRedBlue(m_sUnitCode, v);
        if ((vTmp != null) && (vTmp.size() > 0)) {
          int iSize = vTmp.size();
          for (int i = 0; i < iSize; i++) {
            vAllItems.addElement(vTmp.elementAt(i));
          }
        }
      }
    }
    timeDebug.addExecutePhase("组合能自动结算的入库单/发票");

    if ((vAllItems == null) || (vAllItems.size() == 0)) {
      throw new BusinessException(
        NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000004"));
    }

    SettlebillItemVO[] m_settleVOs = (SettlebillItemVO[])null;
    m_settleVOs = new SettlebillItemVO[vAllItems.size()];
    vAllItems.copyInto(m_settleVOs);

    SettlebillVO vo = doAutoBalanceModification(m_invoiceVOs, m_stockVOs, 
      listPara, m_settleVOs);
    timeDebug.addExecutePhase("修改入库单和发票的数据并返回结算单VO");

    timeDebug.showAllExecutePhase("自动结算-doAutoBalance()-时间分布(总-1)");

    vo.getHeadVO().setTmaketime(new UFDateTime(new Date()).toString());
    listRet.add(vo);
    return listRet;
  }

  private Vector doAutoBalanceInvoiceRedBlue(String m_sUnitCode, Vector v)
  {
    Vector vv = new Vector();
    int iSize = v.size();
    SettlebillItemVO body = null;
    IinvoiceVO tempVO = null;
    for (int i = 0; i < iSize; i++) {
      tempVO = (IinvoiceVO)v.elementAt(i);
      body = new SettlebillItemVO();
      body.setPk_corp(m_sUnitCode);
      body.setCinvoice_bid(tempVO.getCinvoice_bid());
      body.setCinvoiceid(tempVO.getCinvoiceid());
      body.setCmangid(tempVO.getCmangid());
      body.setCbaseid(tempVO.getCbaseid());
      body.setNmoney(tempVO.getNnosettlemny());
      body.setNsettlenum(tempVO.getNnosettlenum());

      UFDouble d1 = body.getNmoney();
      UFDouble d2 = body.getNsettlenum();
      if ((d1 != null) && (d2 != null) && 
        (d2.doubleValue() != 0.0D)) {
        double d = d1.doubleValue() / d2.doubleValue();
        body.setNprice(new UFDouble(d));
      }

      body.setVinvoicecode(tempVO.getVinvoicecode());
      vv.addElement(body);
    }
    return vv;
  }

  private SettlebillVO doAutoBalanceModification(IinvoiceVO[] m_invoiceVOs, StockVO[] m_stockVOs, ArrayList listPara, SettlebillItemVO[] m_settleVOs)
    throws BusinessException
  {
    Timer timeDebug = new Timer();
    timeDebug.start();

    String m_sUnitCode = (String)listPara.get(0);
    String strOprId = (String)listPara.get(2);
    String strAccYear = (String)listPara.get(3);
    UFDate dateLogin = (UFDate)listPara.get(4);

    Vector vStockKey = new Vector();
    for (int i = 0; i < m_settleVOs.length - 1; i++) {
      String s = m_settleVOs[i].getCstockrow();
      if ((s == null) || (s.length() == 0))
        continue;
      s = s.trim();
      boolean b = false;
      for (int j = i + 1; j < m_settleVOs.length; j++) {
        String ss = m_settleVOs[j].getCstockrow();
        if ((ss == null) || (ss.length() == 0))
          continue;
        ss = ss.trim();
        if (s.equals(ss)) {
          b = true;
          break;
        }
      }
      if (!b)
        vStockKey.addElement(s);
    }
    String sss = m_settleVOs[(m_settleVOs.length - 1)].getCstockrow();
    UFDouble ddd = m_settleVOs[(m_settleVOs.length - 1)].getNsettlenum();
    if ((sss != null) && (sss.length() > 0) && (ddd != null)) {
      vStockKey.addElement(sss);
    }
    timeDebug.addExecutePhase("入库单行主键归类");

    Vector vInvoiceKey = new Vector();
    for (int i = 0; i < m_settleVOs.length - 1; i++) {
      String s = m_settleVOs[i].getCinvoice_bid();
      UFDouble d = m_settleVOs[i].getNsettlenum();
      if (d == null)
        continue;
      if ((s == null) || (s.length() == 0))
        continue;
      s = s.trim();
      boolean b = false;
      for (int j = i + 1; j < m_settleVOs.length; j++) {
        String ss = m_settleVOs[j].getCinvoice_bid();
        if ((ss == null) || (ss.length() == 0))
          continue;
        ss = ss.trim();
        if (s.equals(ss)) {
          b = true;
          break;
        }
      }
      if (!b)
        vInvoiceKey.addElement(s);
    }
    sss = m_settleVOs[(m_settleVOs.length - 1)].getCinvoice_bid();
    ddd = m_settleVOs[(m_settleVOs.length - 1)].getNsettlenum();
    if ((sss != null) && (sss.length() > 0) && (ddd != null)) {
      vInvoiceKey.addElement(sss);
    }
    timeDebug.addExecutePhase("发票行主键归类（不包括费用发票和折扣）");

    StockVO[] stockVOs = doAutoBalanceModifyStock(m_stockVOs, vStockKey, 
      m_settleVOs);

    timeDebug.addExecutePhase("修改入库单数据");

    IinvoiceVO[] iinvoiceVOs = doAutoBalanceModifyInvoice(m_invoiceVOs, 
      vInvoiceKey, m_settleVOs);

    timeDebug.addExecutePhase("修改发票数据");

    SettlebillHeaderVO head = new SettlebillHeaderVO();
    head.setPk_corp(m_sUnitCode);
    head.setCaccountyear(strAccYear);
    head.setDsettledate(dateLogin);
    head.setIbillstatus(new Integer(0));
    head.setCbilltype("27");
    head.setCoperator(strOprId);
    head.setTmaketime(new UFDateTime(new Date()).toString());

    SettlebillVO settlebillVO = new SettlebillVO(m_settleVOs.length);
    settlebillVO.setParentVO(head);
    settlebillVO.setChildrenVO(m_settleVOs);

    String m_sEstimateMode = null;
    String m_sDifferenceMode = null;
    UFBoolean m_bIc2PiSettle = new UFBoolean(false);
    UFBoolean m_bZGYF = new UFBoolean(false);
    int nMoneyDecimal = 2;
    ISysInitQry myService = (ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName());

    Hashtable t = null;
    try {
      t = myService.queryBatchParaValues(m_sUnitCode, new String[] { "PO51", "PO52", "PO12", "PO13" });
      nMoneyDecimal = PubDMO.getCCurrDecimal(m_sUnitCode);
    } catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    if ((t == null) || (t.size() == 0)) {
      throw new BusinessException(
        NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000005"));
    }

    Object temp = null;
    if (t.get("PO51") != null) {
      temp = t.get("PO51");
      m_bIc2PiSettle = new UFBoolean(temp.toString());
    }
    if (t.get("PO52") != null) {
      temp = t.get("PO52");
      m_bZGYF = new UFBoolean(temp.toString());
    }
    if (t.get("PO12") != null) {
      temp = t.get("PO12");
      m_sEstimateMode = temp.toString();
    }
    if (t.get("PO13") != null) {
      temp = t.get("PO13");
      m_sDifferenceMode = temp.toString();
    }

    timeDebug.addExecutePhase("设置结算单、获得系统设置的暂估方式和差异转入方式");

    String headKey = null;
    SettlebillVO VO = null;
    try
    {
      ArrayList listPara1 = new ArrayList();
      listPara1.add(settlebillVO);
      listPara1.add(stockVOs);
      listPara1.add(iinvoiceVOs);
      listPara1.add(null);
      listPara1.add(null);
      listPara1.add(m_sEstimateMode);
      listPara1.add(m_sDifferenceMode);
      listPara1.add(strOprId);
      listPara1.add(m_bIc2PiSettle);
      listPara1.add(m_bZGYF);
      listPara1.add(new Integer(nMoneyDecimal));
      listPara1.add(new UFBoolean(false));

      headKey = doManualBalance(listPara1);

      if ((headKey != null) && (headKey.trim().length() > 0)) {
        try {
          VO = querySettlebillByHeadKey(m_sUnitCode, headKey);
        } catch (Exception e) {
          SCMEnv.out(e);
          throw new BusinessException(
            NCLangResOnserver.getInstance().getStrByID("40040502", 
            "UPP40040502-000006"));
        }

      }

    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);

      timeDebug.addExecutePhase("调用手工结算传存货核算");

      timeDebug
        .showAllExecutePhase("修改入库单、发票数据；调用手工结算传存货核算-doAutoBalanceModification()-时间分布(总-1-1)");
    }
    return VO;
  }

  private IinvoiceVO[] doAutoBalanceModifyInvoice(IinvoiceVO[] m_invoiceVOs, Vector vInvoiceKey, SettlebillItemVO[] m_settleVOs)
    throws BusinessException
  {
    Vector v = new Vector();
    for (int i = 0; i < vInvoiceKey.size(); i++) {
      String s = (String)vInvoiceKey.elementAt(i);
      double nSettlenum = 0.0D;
      double nSettlemny = 0.0D;
      String s0 = "";
      for (int j = 0; j < m_settleVOs.length; j++) {
        String ss = m_settleVOs[j].getCinvoice_bid();
        if ((ss == null) || (ss.length() == 0))
          continue;
        ss = ss.trim();
        if (s.equals(ss)) {
          nSettlenum += m_settleVOs[j].getNsettlenum().doubleValue();
          nSettlemny += m_settleVOs[j].getNmoney().doubleValue();
          s0 = m_settleVOs[j].getCinvoiceid();
        }
      }

      int k = -1;
      for (int j = 0; j < m_invoiceVOs.length; j++) {
        String s1 = m_invoiceVOs[j].getCinvoice_bid().trim();
        String s2 = m_invoiceVOs[j].getCinvoiceid().trim();
        if ((s.equals(s1)) && (s0.equals(s2))) {
          k = j;
          break;
        }
      }
      if (k == -1) {
        throw new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("40040502", 
          "UPP40040502-000007"));
      }

      double d = m_invoiceVOs[k].getNaccumsettlenum().doubleValue();
      d += nSettlenum;
      m_invoiceVOs[k].setNaccumsettlenum(new UFDouble(d));
      d = m_invoiceVOs[k].getNaccumsettlemny().doubleValue();
      d += nSettlemny;
      m_invoiceVOs[k].setNaccumsettlemny(new UFDouble(d));

      v.addElement(m_invoiceVOs[k]);
    }
    if (v.size() > 0) {
      IinvoiceVO[] vos = new IinvoiceVO[v.size()];
      v.copyInto(vos);
      return vos;
    }
    return null;
  }

  private StockVO[] doAutoBalanceModifyStock(StockVO[] m_stockVOs, Vector vStockKey, SettlebillItemVO[] m_settleVOs)
    throws BusinessException
  {
    Vector v = new Vector();
    for (int i = 0; i < vStockKey.size(); i++) {
      String s = (String)vStockKey.elementAt(i);
      double nSettlenum = 0.0D;
      double nGaugemny = 0.0D;
      String s0 = "";
      for (int j = 0; j < m_settleVOs.length; j++) {
        String ss = m_settleVOs[j].getCstockrow();
        if ((ss == null) || (ss.length() == 0))
          continue;
        ss = ss.trim();
        if (s.equals(ss)) {
          nSettlenum += m_settleVOs[j].getNsettlenum().doubleValue();
          nGaugemny += m_settleVOs[j].getNgaugemny().doubleValue();
          s0 = m_settleVOs[j].getCstockid();
        }
      }

      int k = -1;
      for (int j = 0; j < m_stockVOs.length; j++) {
        String s1 = m_stockVOs[j].getCgeneralbid().trim();
        String s2 = m_stockVOs[j].getCgeneralhid().trim();
        if ((s.equals(s1)) && (s0.equals(s2))) {
          k = j;
          break;
        }
      }
      if (k == -1) {
        throw new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("40040502", 
          "UPP40040502-000008"));
      }

      double d = m_stockVOs[k].getNaccumsettlenum().doubleValue();
      d += nSettlenum;
      m_stockVOs[k].setNaccumsettlenum(new UFDouble(d));
      d = m_stockVOs[k].getNaccumsettlemny().doubleValue();
      d += nGaugemny;
      m_stockVOs[k].setNaccumsettlemny(new UFDouble(d));

      v.addElement(m_stockVOs[k]);
    }
    if (v.size() > 0) {
      StockVO[] vos = new StockVO[v.size()];
      v.copyInto(vos);
      return vos;
    }
    return null;
  }

  private Vector doAutoBalanceRedBlue(String m_sUnitCode, Vector v)
  {
    Vector v1 = new Vector();
    Vector v2 = new Vector();
    for (int i = 0; i < v.size(); i++) {
      Vector vTemp = (Vector)v.elementAt(i);
      v1.addElement(vTemp.elementAt(0));
      v2.addElement(vTemp.elementAt(1));
    }
    StockVO[] settleVO1 = new StockVO[v1.size()];
    IinvoiceVO[] settleVO2 = new IinvoiceVO[v2.size()];
    v1.copyInto(settleVO1);
    v2.copyInto(settleVO2);

    Vector vv = new Vector();
    SettlebillItemVO body = null;
    int iSize = v.size();
    for (int i = 0; i < iSize; i++) {
      body = new SettlebillItemVO();
      body.setPk_corp(m_sUnitCode);
      body.setPk_stockcorp(settleVO1[i].getPk_stockcorp());
      body.setCinvoice_bid(settleVO2[i].getCinvoice_bid());
      body.setCinvoiceid(settleVO2[i].getCinvoiceid());
      body.setCstockrow(settleVO1[i].getCgeneralbid());
      body.setCstockid(settleVO1[i].getCgeneralhid());
      body.setCmangid(settleVO1[i].getCmangid());
      body.setCbaseid(settleVO1[i].getCbaseid());
      body.setNmoney(settleVO2[i].getNnosettlemny());
      body.setNsettlenum(settleVO1[i].getNnosettlenum());
      body.setNgaugemny(settleVO1[i].getNnosettlemny());

      UFDouble d1 = body.getNmoney();
      UFDouble d2 = body.getNsettlenum();
      if ((d1 != null) && (d2 != null) && (d2.doubleValue() != 0.0D)) {
        double d = d1.doubleValue() / d2.doubleValue();
        body.setNprice(new UFDouble(d));
      }

      body.setVbillcode(settleVO1[i].getVbillcode());

      body.setVinvoicecode(settleVO2[i].getVinvoicecode());
      vv.addElement(body);
    }
    return vv;
  }

  private Vector doAutoBalanceStockRedBlue(String m_sUnitCode, Vector v)
  {
    SettlebillItemVO body = null;
    Vector vv = new Vector();
    int iSize = v.size();
    for (int i = 0; i < iSize; i++) {
      StockVO tempVO = (StockVO)v.elementAt(i);
      body = new SettlebillItemVO();
      body = new SettlebillItemVO();
      body.setPk_corp(m_sUnitCode);
      body.setPk_stockcorp(tempVO.getPk_stockcorp());
      body.setCstockrow(tempVO.getCgeneralbid());
      body.setCstockid(tempVO.getCgeneralhid());
      body.setCmangid(tempVO.getCmangid());
      body.setCbaseid(tempVO.getCbaseid());
      body.setNmoney(tempVO.getNnosettlemny());
      body.setNsettlenum(tempVO.getNnosettlenum());
      body.setNgaugemny(tempVO.getNnosettlemny());

      body.setNprice(tempVO.getNprice());

      body.setVbillcode(tempVO.getVbillcode());

      vv.addElement(body);
    }
    return vv;
  }

  public void doFeeBalance(SettlebillVO settlebillVO, StockVO[] stockVOs, FeeinvoiceVO[] feeVOs, FeeinvoiceVO[] discountVOs, FeeinvoiceVO[] specialVOs, String sDiffMode, String cOperator)
    throws BusinessException
  {
    SettleDMO dmo = null;
    ICreateCorpQueryService myService0 = null;
    boolean bLock = false;
    boolean bGetBillCodeSucc = false;

    Timer timer = new Timer();
    timer.start();

    String[] sKeys = getSettleLockKeys(stockVOs, null, feeVOs, discountVOs, 
      specialVOs, null, null);
    try
    {
      dmo = new SettleDMO();

      bLock = LockTool.setLockForPks(sKeys, cOperator);
      if (!bLock) {
        throw new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("40040502", 
          "UPP40040502-000009"));
      }

      timer.addExecutePhase("对单据加锁");

      String sMessage = isTimeStampChangedForSettle(stockVOs, null, 
        feeVOs, discountVOs, specialVOs);
      if ((sMessage != null) && (sMessage.length() > 0)) {
        sMessage = sMessage + NCLangResOnserver.getInstance()
          .getStrByID("40040502", "UPP40040502-000002");

        throw new BusinessException(sMessage);
      }

      timer.addExecutePhase("判断时间戳是否改变");

      SettlebillHeaderVO head = settlebillVO.getHeadVO();
      head.setTmaketime(new UFDateTime(new Date()).toString());

      String vSettleCode = getSettleBillCode(head);
      head.setAttributeValue("vsettlebillcode", vSettleCode);
      bGetBillCodeSucc = true;

      timer.addExecutePhase("获得结算单号");

      String key = dmo.insertHead(head);

      timer.addExecutePhase("插入结算单--头");

      SettlebillItemVO[] body = settlebillVO.getBodyVO();
      String[] s = dmo.insertBody(body, key);
      for (int i = 0; i < body.length; i++) {
        body[i].setCsettlebill_bid(s[i]);
        body[i].setCsettlebillid(key);
      }

      timer.addExecutePhase("插入结算单--体");

      if ((feeVOs != null) && (feeVOs.length > 0)) {
        dmo.updateFee(feeVOs);
      }

      timer.addExecutePhase("更新费用发票");

      if ((discountVOs != null) && (discountVOs.length > 0)) {
        dmo.updateDiscount(discountVOs);
      }

      timer.addExecutePhase("更新折扣");

      if ((specialVOs != null) && (specialVOs.length > 0)) {
        dmo.updateDiscount(specialVOs);
      }

      timer.addExecutePhase("更新一般发票");

      if ((stockVOs != null) && (stockVOs.length > 0)) {
        String unitCode = stockVOs[0].getPk_corp();
        myService0 = (ICreateCorpQueryService)
          NCLocator.getInstance().lookup(
          ICreateCorpQueryService.class.getName());
        boolean bIAStartUp = myService0.isEnabled(unitCode, "IA");

        if (bIAStartUp) {
          saveBillFromOutterForFee(sDiffMode, settlebillVO, 
            cOperator, head.getDsettledate());
        }

      }
      else if (bGetBillCodeSucc) {
        try {
          GetSysBillCode billCode = new GetSysBillCode();
          billCode.returnBillNo(settlebillVO);
        }
        catch (Exception ex)
        {
          PubDMO.throwBusinessException(ex);
        }

      }

      timer.addExecutePhase("存货核算启用时向存货核算传数据");

      timer.showAllExecutePhase("费用单独结算");
    }
    catch (Exception e) {
      if (bGetBillCodeSucc) {
        try {
          GetSysBillCode billCode = new GetSysBillCode();
          billCode.returnBillNo(settlebillVO);
        }
        catch (Exception ex)
        {
          PubDMO.throwBusinessException(e);
        }

      }

      PubDMO.throwBusinessException(e);
      
      //反编译后报错，edit by yqq  2016-07-18
/*      try
      {
        if (bLock)
        {
          LockTool.releaseLockForPks(sKeys, cOperator);
        }
      } catch (Exception e) {
        PubDMO.throwBusinessException(e);
      }*/
    }
    finally
    {
      try
      {
        if (bLock)
        {
          LockTool.releaseLockForPks(sKeys, cOperator);
        }
      } catch (Exception e) {
        PubDMO.throwBusinessException(e);
      }
    }
  }

  public String doManualBalance(ArrayList listPara)
    throws BusinessException
  {
    if ((listPara == null) || (listPara.size() < 8)) {
      SCMEnv.out("传入参数为空或参数不足，直接返回 NULL！");
      return null;
    }
    Timer timer = new Timer();
    timer.start();

    SettlebillVO settlebillVO = (SettlebillVO)listPara.get(0);
    StockVO[] stockVOs = (StockVO[])listPara.get(1);
    IinvoiceVO[] iinvoiceVOs = (IinvoiceVO[])listPara.get(2);
    FeeinvoiceVO[] feeVOs = (FeeinvoiceVO[])listPara.get(3);
    FeeinvoiceVO[] discountVOs = (FeeinvoiceVO[])listPara.get(4);
    String sEstMode = (String)listPara.get(5);
    String sDiffMode = (String)listPara.get(6);
    String cOperator = (String)listPara.get(7);
    UFBoolean bIc2PiSettle = (UFBoolean)listPara.get(8);
    UFBoolean bZGYF = (UFBoolean)listPara.get(9);
    int nMnyDecimal = ((Integer)listPara.get(10)).intValue();
    UFBoolean bDifferSettle = (UFBoolean)listPara.get(11);

    if (bDifferSettle.booleanValue()) {
      if (bZGYF.booleanValue())
      {
        bIc2PiSettle = new UFBoolean(false);
      }
      bZGYF = new UFBoolean(false);
    }

    SettleDMO dmo = null;
    ICreateCorpQueryService myService0 = null;
    boolean bLock = false;
    String key = null;
    boolean bGetBillCodeSucc = false;

    String[] sKeys = getSettleLockKeys(stockVOs, iinvoiceVOs, feeVOs, 
      discountVOs, null, null, null);
    try {
      dmo = new SettleDMO();

      bLock = LockTool.setLockForPks(sKeys, cOperator);
      if (!bLock) {
        throw new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("40040502", 
          "UPP40040502-000009"));
      }

      timer.addExecutePhase(NCLangResOnserver.getInstance()
        .getStrByID("40040502", "UPP40040502-000010"));

      String sMessage = isTimeStampChangedForSettle(stockVOs, 
        iinvoiceVOs, feeVOs, discountVOs, null);
      if ((sMessage != null) && (sMessage.length() > 0)) {
        sMessage = sMessage + NCLangResOnserver.getInstance()
          .getStrByID("40040502", "UPP40040502-000002");

        throw new BusinessException(sMessage);
      }
      timer.addExecutePhase("判断时间戳是否改变");
      SettlebillHeaderVO head = settlebillVO.getHeadVO();
      head.setTmaketime(new UFDateTime(new Date()).toString());

      String vSettleCode = getSettleBillCode(head);
      head.setAttributeValue("vsettlebillcode", vSettleCode);
      bGetBillCodeSucc = true;

      timer.addExecutePhase("获得结算单号");

      SettlebillItemVO[] body = settlebillVO.getBodyVO();

      if ((bIc2PiSettle.booleanValue()) && (stockVOs != null) && (stockVOs.length > 0)) {
        Vector vTemp = new Vector();
        for (int i = 0; i < stockVOs.length; i++) {
          if ((stockVOs[i].getBzgflag().booleanValue()) || 
            (stockVOs[i].getBsettled().booleanValue()) || 
            (Math.abs(stockVOs[i].getNinnum().doubleValue() - stockVOs[i].getNaccumsettlenum().doubleValue()) <= 0.0D) || 
            (!stockVOs[i].getPk_corp().equals(stockVOs[i].getPk_stockcorp()))) continue;
          vTemp.addElement(stockVOs[i]);
        }
        if (vTemp.size() > 0) {
          StockVO[] zgStockVOs = new StockVO[vTemp.size()];
          vTemp.copyInto(zgStockVOs);

          zgStockVOs = dmo.replacePrice(zgStockVOs);

          for (int i = 0; i < zgStockVOs.length; i++) {
            zgStockVOs[i].setBzgflag(new UFBoolean(true));
            zgStockVOs[i].setDzgdate(head.getDsettledate());
          }

          EstimateVO[] estVO = switchVOForZGYF(zgStockVOs, bZGYF);

          for (int i = 0; i < stockVOs.length; i++) {
            double d3 = 0.0D;
            for (int j = 0; j < zgStockVOs.length; j++) {
              if (stockVOs[i].getCgeneralbid().equals(zgStockVOs[j].getCgeneralbid())) {
                if (estVO != null) {
                  zgStockVOs[j].setNprice(estVO[j].getNprice());
                  zgStockVOs[j].setNmoney(estVO[j].getNmoney());
                }
                stockVOs[i] = zgStockVOs[j];
                break;
              }
            }
            double d1 = stockVOs[i].getNprice().doubleValue();
            for (int j = 0; j < body.length; j++) {
              if ((body[j].getCstockrow() == null) || 
                (!body[j].getCstockrow().equals(stockVOs[i].getCgeneralbid()))) continue;
              double d2 = body[j].getNsettlenum().doubleValue();
              d3 += d2;
              body[j].setNgaugemny(new UFDouble(PubDMO.getRoundDouble(nMnyDecimal, d1 * d2)));
              break;
            }

            d3 *= stockVOs[i].getNprice().doubleValue();
            d3 = PubDMO.getRoundDouble(nMnyDecimal, d3);
            d3 += stockVOs[i].getNaccumsettlemny().doubleValue();
            stockVOs[i].setNaccumsettlemny(new UFDouble(d3));
          }

          estimateForPartSettle(zgStockVOs, estVO, cOperator, head.getDsettledate(), bZGYF);
        }

      }

      key = dmo.insertHead(head);
      String[] s = dmo.insertBody(body, key);
      for (int i = 0; i < body.length; i++) {
        body[i].setCsettlebill_bid(s[i]);
        body[i].setCsettlebillid(key);
      }
      timer.addExecutePhase("插入结算单");

      if ((stockVOs != null) && (stockVOs.length > 0)) {
        dmo.updateStock(stockVOs, false);
      }
      timer.addExecutePhase("更新入库单");

      if ((iinvoiceVOs != null) && (iinvoiceVOs.length > 0)) {
        dmo.updateInvoice(iinvoiceVOs);
      }
      timer.addExecutePhase("更新发票");

      if ((feeVOs != null) && (feeVOs.length > 0)) {
        dmo.updateFee(feeVOs);
      }
      timer.addExecutePhase("更新费用发票");

      if ((discountVOs != null) && (discountVOs.length > 0)) {
        dmo.updateDiscount(discountVOs);
      }
      timer.addExecutePhase("更新折扣");

      if ((stockVOs != null) && (stockVOs.length > 0)) {
        String unitCode = stockVOs[0].getPk_corp();
        myService0 = (ICreateCorpQueryService)
          NCLocator.getInstance().lookup(
          ICreateCorpQueryService.class.getName());
        boolean bIAStartUp = myService0.isEnabled(unitCode, "IA");
        if (bIAStartUp)
        {
          GeneralHItemVO[] generalHItemVO = new GeneralHItemVO[stockVOs.length];
          for (int i = 0; i < stockVOs.length; i++) {
            generalHItemVO[i] = new GeneralHItemVO();
            generalHItemVO[i].setCgeneralhid(stockVOs[i]
              .getCgeneralhid());
            generalHItemVO[i].setCgeneralbid(stockVOs[i]
              .getCgeneralbid());
            generalHItemVO[i].setVfree1(stockVOs[i].getVfree1());
            generalHItemVO[i].setVfree2(stockVOs[i].getVfree2());
            generalHItemVO[i].setVfree3(stockVOs[i].getVfree3());
            generalHItemVO[i].setVfree4(stockVOs[i].getVfree4());
            generalHItemVO[i].setVfree5(stockVOs[i].getVfree5());

            generalHItemVO[i].setBzgflag(stockVOs[i].getBzgflag());
            generalHItemVO[i].setNinnum(stockVOs[i].getNinnum());
            generalHItemVO[i].setNmny(stockVOs[i].getNmoney());
          }
          listPara = new ArrayList();
          listPara.add(sEstMode);
          listPara.add(sDiffMode);
          listPara.add(settlebillVO);
          listPara.add(cOperator);
          listPara.add(head.getDsettledate());
          listPara.add(generalHItemVO);
          listPara.add(stockVOs);
          saveBillFromOutter1(listPara);
        }

      }
      else if (bGetBillCodeSucc) {
        try {
          GetSysBillCode billCode = new GetSysBillCode();
          billCode.returnBillNo(settlebillVO);
        }
        catch (Exception ex)
        {
          PubDMO.throwBusinessException(ex);
        }
      }

      timer.addExecutePhase("向存货核算传送数据-saveBillFromOutter1-(总)");

      timer.showAllExecutePhase("手工结算-doManualBalance()-时间分布");
    }
    catch (Exception e)
    {
      if (bGetBillCodeSucc) {
        try {
          GetSysBillCode billCode = new GetSysBillCode();
          billCode.returnBillNo(settlebillVO);
        }
        catch (Exception ex)
        {
          PubDMO.throwBusinessException(e);
        }

      }

      PubDMO.throwBusinessException(e);
      
      //反编译后报错，edit by yqq  2016-07-18
/*      try
      {
        if (bLock)
        {
          LockTool.releaseLockForPks(sKeys, cOperator);
        }
      } catch (Exception e) {
        PubDMO.throwBusinessException(e);
      }*/
    }
    finally
    {
      try
      {
        if (bLock)
        {
          LockTool.releaseLockForPks(sKeys, cOperator);
        }
      } catch (Exception e) {
        PubDMO.throwBusinessException(e);
      }
    }
    return key;
  }

  public void doNoneBalance(SettlebillVO settlebillVO, StockVO[] stockVOs, InvoiceVO[] invoiceVOs, String sEstMode, String sDiffMode, String cOperator, String currType)
    throws BusinessException
  {
    SettleDMO dmo = null;
    ICreateCorpQueryService myService0 = null;
    boolean bLock = false;
    boolean bGetBillCodeSucc = false;
    String sTime = new UFDateTime(new Date()).toString();

    Timer timer = new Timer();
    timer.start();

    String[] sKeys = getSettleLockKeys(stockVOs, null, null, null, null, 
      null, null);
    try
    {
      dmo = new SettleDMO();

      bLock = LockTool.setLockForPks(sKeys, cOperator);
      if (!bLock) {
        throw new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("40040502", 
          "UPP40040502-000009"));
      }

      timer.addExecutePhase("对单据加锁");

      String sMessage = isTimeStampChangedForSettle(stockVOs, null, null, 
        null, null);
      if ((sMessage != null) && (sMessage.length() > 0)) {
        sMessage = sMessage + NCLangResOnserver.getInstance()
          .getStrByID("40040502", "UPP40040502-000002");

        throw new BusinessException(sMessage);
      }

      timer.addExecutePhase("判断时间戳是否改变");

      Vector vKey1 = new Vector();
      Vector vKey2 = new Vector();
      if ((invoiceVOs != null) && (invoiceVOs.length > 0)) {
        Vector v = new Vector();
        for (int i = 0; i < invoiceVOs.length; i++) {
          InvoiceHeaderVO headVO = invoiceVOs[i].getHeadVO();
          InvoiceItemVO[] itemVOs = invoiceVOs[i].getBodyVO();

          String vInvoiceCode = getInvoiceCode(headVO);
          headVO.setVinvoicecode(vInvoiceCode);
          headVO.setTmaketime(sTime);
          headVO.setTlastmaketime(sTime);

          String s = dmo.insertInvoiceHead(headVO);
          for (int j = 0; j < itemVOs.length; j++) {
            itemVOs[j].setCinvoiceid(s);
            v.addElement(itemVOs[j]);
          }
          String[] ss = dmo.insertInvoiceBody(itemVOs);

          for (int j = 0; j < itemVOs.length; j++) {
            itemVOs[j].setCinvoice_bid(ss[j]);
            vKey1.addElement(s);
            vKey2.addElement(ss[j]);
          }

        }

        if (v.size() > 0) {
          InvoiceItemVO[] itemVOs = new InvoiceItemVO[v.size()];
          v.copyInto(itemVOs);
          dmo.updateSignNum(itemVOs);
        }
      }

      timer.addExecutePhase("插入虚拟发票");

      SettlebillHeaderVO head = settlebillVO.getHeadVO();
      head.setTmaketime(sTime);

      String vSettleCode = getSettleBillCode(head);
      head.setAttributeValue("vsettlebillcode", vSettleCode);
      bGetBillCodeSucc = true;

      timer.addExecutePhase("获得结算单号");

      String key = dmo.insertHead(head);

      timer.addExecutePhase("插入结算单--表头");

      SettlebillItemVO[] body = settlebillVO.getBodyVO();
      if ((vKey1 != null) && (vKey1.size() > 0)) {
        for (int i = 0; i < body.length; i++) {
          body[i].setCinvoiceid((String)vKey1.elementAt(i));
          body[i].setCinvoice_bid((String)vKey2.elementAt(i));
        }
      }
      String[] s = dmo.insertBody(body, key);
      for (int i = 0; i < body.length; i++) {
        body[i].setCsettlebill_bid(s[i]);
        body[i].setCsettlebillid(key);
      }

      timer.addExecutePhase("插入结算单--表体");

      if ((stockVOs != null) && (stockVOs.length > 0)) {
        dmo.updateStock(stockVOs, false);
      }

      timer.addExecutePhase("更新入库单");

      if ((stockVOs != null) && (stockVOs.length > 0)) {
        String unitCode = stockVOs[0].getPk_corp();
        myService0 = (ICreateCorpQueryService)
          NCLocator.getInstance().lookup(
          ICreateCorpQueryService.class.getName());
        boolean bIAStartUp = myService0.isEnabled(unitCode, "IA");

        if (bIAStartUp)
        {
          ArrayList listPara = new ArrayList();
          listPara.add(sEstMode);
          listPara.add(sDiffMode);
          listPara.add(settlebillVO);
          listPara.add(cOperator);
          listPara.add(head.getDsettledate());
          listPara.add(null);
          listPara.add(null);
          saveBillFromOutter1(listPara);
        }
        timer.addExecutePhase("向存货核算传送数据(总)");

        boolean bAPStartUp = myService0.isEnabled(unitCode, "AP");

        if (bAPStartUp)
        {
          GeneralBillVO[] billVOs = transferARAPDataNone(stockVOs, 
            body, cOperator, head.getDsettledate());

          timer.addExecutePhase("向应付传送数据--入库单VO，结算单体VO转换为标准入库单VO(总)");

          if ((billVOs != null) && (billVOs.length > 0)) {
            saveBillForARAPNone(billVOs, body, currType, cOperator);
          }

          AdjuestVO[] washVO = new InvoiceDMO().washDataForZGYF(invoiceVOs);

          Hashtable hLastWash = new Hashtable();
          for (int i = 0; i < stockVOs.length; i++) {
            if (Math.abs(stockVOs[i].getNinnum().doubleValue() - stockVOs[i].getNaccumwashnum().doubleValue()) < VMIDMO.getDigitRMB(unitCode).doubleValue()) {
              hLastWash.put(stockVOs[i].getCgeneralbid(), new UFBoolean(true));
            }

          }

          if ((washVO != null) && (washVO.length > 0)) {
            IArapForGYLPublic iArap = (IArapForGYLPublic)NCLocator.getInstance().lookup(IArapForGYLPublic.class.getName());
            UFBoolean[] bLast = new UFBoolean[washVO.length];
            for (int i = 0; i < washVO.length; i++) {
              if (hLastWash.get(washVO[i].getDdhh()) != null) bLast[i] = new UFBoolean(true); else {
                bLast[i] = new UFBoolean(false);
              }
              washVO[i].setIsdone(bLast[i]);
            }
            iArap.Adjuest(washVO, key, cOperator, head.getDsettledate().toString(), unitCode, 1, 1);
          }

          timer.addExecutePhase("向应付传送数据--调用接口(总)");
        }

      }
      else if (bGetBillCodeSucc) {
        try {
          GetSysBillCode billCode = new GetSysBillCode();
          billCode.returnBillNo(settlebillVO);
        }
        catch (Exception ex)
        {
          PubDMO.throwBusinessException(ex);
        }
      }

      timer.showAllExecutePhase("无发票结算时间分布");
    }
    catch (Exception e)
    {
      if (bGetBillCodeSucc) {
        try {
          GetSysBillCode billCode = new GetSysBillCode();
          billCode.returnBillNo(settlebillVO);
        }
        catch (Exception ex)
        {
          PubDMO.throwBusinessException(e);
        }

      }

      PubDMO.throwBusinessException(e);
      
      //反编译后报错，edit by yqq  2016-07-18
/*      try
      {
        if (bLock)
        {
          LockTool.releaseLockForPks(sKeys, cOperator);
        }
      } catch (Exception e) {
        PubDMO.throwBusinessException(e);
      }*/
    }
    finally
    {
      try
      {
        if (bLock)
        {
          LockTool.releaseLockForPks(sKeys, cOperator);
        }
      } catch (Exception e) {
        PubDMO.throwBusinessException(e);
      }
    }
  }

  public ArrayList doSaleBalance(ArrayList listPara)
    throws BusinessException
  {
    if ((listPara == null) || (listPara.size() < 10)) {
      SCMEnv.out("传入参数为空或参数不足，直接返回 NULL！");
      return null;
    }

    Timer timer = new Timer();
    timer.start();

    InvoiceVO invoiceVO = (InvoiceVO)listPara.get(0);
    String sKey = (String)listPara.get(1);
    SettlebillVO settlebillVO = (SettlebillVO)listPara.get(2);
    StockVO[] stockVOs = (StockVO[])listPara.get(3);
    IinvoiceVO[] iinvoiceVOs = (IinvoiceVO[])listPara.get(4);
    String sEstMode = (String)listPara.get(5);
    String sDiffMode = (String)listPara.get(6);
    String cOperator = (String)listPara.get(7);
    OorderVO[] orderVOs = (OorderVO[])listPara.get(8);
    IdTsData struIdTs = (IdTsData)listPara.get(9);

    String sTime = new UFDateTime(new Date()).toString();

    if ((stockVOs != null) && (stockVOs.length > 0) && (iinvoiceVOs != null) && 
      (iinvoiceVOs.length > 0)) {
      Vector v1 = new Vector(); Vector v2 = new Vector();
      for (int i = 0; i < iinvoiceVOs.length; i++) {
        if (!v2.contains(iinvoiceVOs[i].getCupsourcebillrowid()))
          v2.addElement(iinvoiceVOs[i].getCupsourcebillrowid());
      }
      for (int i = 0; i < stockVOs.length; i++) {
        if (v2.contains(stockVOs[i].getCgeneralbid()))
          v1.addElement(stockVOs[i]);
      }
      stockVOs = new StockVO[v1.size()];
      v1.copyInto(stockVOs);
    }

    SettleDMO dmo = null;
    String key = "";
    Vector vRet = new Vector();
    int iSize = 5;
    ArrayList listRet = new ArrayList(iSize);
    for (int i = 0; i < iSize; i++) {
      listRet.add(null);
    }
    IdTsData dataIdTs = null;
    ICreateCorpQueryService myService0 = null;
    boolean bLock = false;

    String[] sKeys = getSettleLockKeys(stockVOs, null, null, null, null, 
      orderVOs, null);
    if ((struIdTs != null) && (struIdTs.getIDs() != null) && (sKeys != null) && 
      (sKeys.length > 0)) {
      Vector vTmp = new Vector();
      int iLen = sKeys.length;
      for (int i = 0; i < iLen; i++) {
        vTmp.addElement(sKeys[i]);
      }
      iLen = struIdTs.getIDs().length;
      for (int i = 0; i < iLen; i++) {
        vTmp.addElement(struIdTs.getIDs()[i]);
      }
      sKeys = new String[vTmp.size()];
      vTmp.copyInto(sKeys);
    }
    try {
      dmo = new SettleDMO();

      bLock = LockTool.setLockForPks(sKeys, cOperator);
      if (!bLock) {
        throw new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("40040502", 
          "UPP40040502-000009"));
      }

      timer.addExecutePhase(NCLangResOnserver.getInstance()
        .getStrByID("40040502", "UPP40040502-000011"));

      String sMessage = isTimeStampChangedForSales(stockVOs, orderVOs, 
        struIdTs);
      if ((sMessage != null) && (sMessage.length() > 0) && (
        (sKey == null) || (sKey.trim().equals("")))) {
        sMessage = sMessage + NCLangResOnserver.getInstance()
          .getStrByID("40040502", "UPP40040502-000002");

        throw new BusinessException(sMessage);
      }
      timer.addExecutePhase("并发判断");

      InvoiceHeaderVO headVO = invoiceVO.getHeadVO();
      InvoiceItemVO[] bodyVO = invoiceVO.getBodyVO();
      String vInvoiceCode = getInvoiceCode(headVO);
      headVO.setVinvoicecode(vInvoiceCode);
      headVO.setTmaketime(sTime);
      headVO.setTlastmaketime(sTime);
      String key1 = dmo.insertInvoiceHead(headVO);
      vRet.addElement(key1);
      for (int i = 0; i < bodyVO.length; i++) {
        bodyVO[i].setCinvoiceid(key1);
      }
      String[] key2 = dmo.insertInvoiceBody(bodyVO);
      for (int i = 0; i < bodyVO.length; i++) {
        vRet.addElement(key2[i]);
      }
      timer.addExecutePhase("插入发票");

      dataIdTs = dmo.updateOrderInvoiceNum(bodyVO, true);
      timer.addExecutePhase("更新订单的累计发票数量");

      listRet.add(0, dataIdTs);
      timer.addExecutePhase("返回采购订单子表新时间戳");

      vRet.addElement(vInvoiceCode);

      SettlebillHeaderVO head = settlebillVO.getHeadVO();
      head.setTmaketime(sTime);
      if ((sKey == null) || (sKey.trim().length() == 0)) {
        String vSettleCode = getSettleBillCode(head);
        head.setAttributeValue("vsettlebillcode", vSettleCode);
        key = dmo.insertHead(head);
      } else {
        key = sKey;
      }
      timer.addExecutePhase("插入结算单");

      vRet.addElement(key);
      SettlebillItemVO[] body = settlebillVO.getBodyVO();
      for (int i = 0; i < body.length; i++) {
        body[i].setCinvoiceid(key1);
        body[i].setCinvoice_bid((String)vRet.elementAt(i + 1));

        body[i].setVinvoicecode(vInvoiceCode);
      }
      String[] s = dmo.insertBody(body, key);
      for (int i = 0; i < body.length; i++) {
        body[i].setCsettlebill_bid(s[i]);
        body[i].setCsettlebillid(key);
      }

      IdTsData[] datas = (IdTsData[])null;
      if ((stockVOs != null) && (stockVOs.length > 0)) {
        datas = dmo.updateStock(stockVOs, true);

        listRet.add(1, datas[0]);
        listRet.add(2, datas[1]);
      }
      timer.addExecutePhase("更新入库单");

      if ((iinvoiceVOs != null) && (iinvoiceVOs.length > 0)) {
        for (int i = 0; i < iinvoiceVOs.length; i++) {
          iinvoiceVOs[i].setCinvoiceid((String)vRet.elementAt(0));
          iinvoiceVOs[i].setCinvoice_bid(
            (String)vRet
            .elementAt(i + 1));
        }
        dmo.updateInvoice(iinvoiceVOs);
      }
      timer.addExecutePhase("更新发票");

      if (settlebillVO != null) {
        dataIdTs = dmo.updateSaleDataBatch(settlebillVO.getBodyVO(), 
          true);

        listRet.add(3, dataIdTs);
      }
      timer.addExecutePhase("更新采购销售发票数据");

      if ((stockVOs != null) && (stockVOs.length > 0)) {
        String unitCode = stockVOs[0].getPk_corp();
        myService0 = (ICreateCorpQueryService)
          NCLocator.getInstance().lookup(
          ICreateCorpQueryService.class.getName());
        boolean bIAStartUp = myService0.isEnabled(unitCode, "IA");
        if (bIAStartUp) {
          listPara = new ArrayList();
          listPara.add(sEstMode);
          listPara.add(sDiffMode);
          listPara.add(settlebillVO);
          listPara.add(cOperator);
          listPara.add(head.getDsettledate());
          listPara.add(null);
          listPara.add(null);
          saveBillFromOutter1(listPara);
        }
      }
      else {
        try {
          GetSysBillCode billCode = new GetSysBillCode();
          billCode.returnBillNo(settlebillVO);
        }
        catch (Exception ex)
        {
          PubDMO.throwBusinessException(ex);
        }
      }
      timer.addExecutePhase("向存货核算传数据(总)");

      timer.showAllExecutePhase("根据销售结算");
    }
    catch (Exception e)
    {
      try {
        GetSysBillCode billCode = new GetSysBillCode();
        billCode.returnBillNo(settlebillVO);
      }
      catch (Exception ex)
      {
        PubDMO.throwBusinessException(e);
      }

      PubDMO.throwBusinessException(e);
      
      //反编译后报错，edit by yqq  2016-07-18
/*      try
      {
        if (bLock)
        {
          LockTool.releaseLockForPks(sKeys, cOperator);
        }
      } catch (Exception e) {
        PubDMO.throwBusinessException(e);
      }*/
    }
    finally
    {
      try
      {
        if (bLock)
        {
          LockTool.releaseLockForPks(sKeys, cOperator);
        }
      } catch (Exception e) {
        PubDMO.throwBusinessException(e);
      }
    }
    if (vRet.size() > 0) {
      String[] sReturn = new String[vRet.size()];
      vRet.copyInto(sReturn);
      listRet.add(4, sReturn);
    }

    return listRet;
  }

  private BillVO generateI9ForWithraw(BillVO billVO, UFDouble nSettleMny, GeneralHItemVO[] generalHItemVOs, ArrayList tsList)
    throws BusinessException, BusinessException
  {
    IBillHeaderVO headerVO = (IBillHeaderVO)billVO.getParentVO();
    IBillItemVO[] itemVO = (IBillItemVO[])billVO.getChildrenVO();

    if (headerVO == null)
      return null;
    if ((itemVO == null) || (itemVO.length == 0))
      return null;
    if ((generalHItemVOs == null) || (generalHItemVOs.length == 0)) {
      return null;
    }
    String sWithdraw = null;
    String sWithdrawPos = null;
    String sWithdrawID = null;
    UFBoolean bWithdraw = new UFBoolean(false);
    if ((tsList != null) && (tsList.size() > 0)) {
      sWithdraw = (String)tsList.get(0);
      sWithdrawPos = (String)tsList.get(1);
      sWithdrawID = (String)tsList.get(2);
      if (sWithdraw != null) bWithdraw = new UFBoolean(sWithdraw.trim());
    }
    if (!bWithdraw.booleanValue()) {
      return null;
    }

    GeneralHItemVO generalHItemVO = null;
    for (int i = 0; i < generalHItemVOs.length; i++) {
      String s1 = generalHItemVOs[i].getCgeneralbid().trim();
      for (int j = 0; j < itemVO.length; j++) {
        String s2 = itemVO[j].getCbill_bid();
        if ((s2 == null) || (s2.trim().length() == 0))
          continue;
        s2 = s2.trim();
        if (s1.equals(s2)) {
          generalHItemVO = new GeneralHItemVO();
          generalHItemVO = generalHItemVOs[i];
          i = generalHItemVOs.length;
          break;
        }
      }
    }
    if (generalHItemVO == null) {
      return null;
    }

    Vector vReturn = new Vector();
    try {
      if ((sWithdrawID == null) || (sWithdrawID.trim().length() == 0))
        throw new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("40040502", 
          "UPP40040502-000012"));
      sWithdrawID = sWithdrawID.trim();

      String sWithdrawRate = null;
      if (sWithdrawPos.equals("1"))
        sWithdrawRate = generalHItemVO.getVfree1();
      else if (sWithdrawPos.equals("2"))
        sWithdrawRate = generalHItemVO.getVfree2();
      else if (sWithdrawPos.equals("3"))
        sWithdrawRate = generalHItemVO.getVfree3();
      else if (sWithdrawPos.equals("4"))
        sWithdrawRate = generalHItemVO.getVfree4();
      else if (sWithdrawPos.equals("5")) {
        sWithdrawRate = generalHItemVO.getVfree5();
      }
      UFDouble dWithdrawRate = new UFDouble(0);
      if ((sWithdrawRate != null) && (sWithdrawRate.trim().length() > 0))
        dWithdrawRate = new UFDouble(sWithdrawRate.trim());
      if ((dWithdrawRate.doubleValue() < 0.0D) || 
        (dWithdrawRate.doubleValue() > 17.0D)) {
        throw new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("40040502", 
          "UPP40040502-000013"));
      }
      if ((dWithdrawRate.doubleValue() != 17.0D) && 
        (dWithdrawRate.doubleValue() != 0.0D))
      {
        BillVO gbillVO = new BillVO();
        IBillHeaderVO gheaderVO = (IBillHeaderVO)headerVO.clone();
        IBillItemVO gitemVO = (IBillItemVO)itemVO[0].clone();
        gitemVO.setCastunitid(null);
        gitemVO.setNassistnum(null);
        gheaderVO.setCbilltypecode("I9");
        gitemVO.setCbilltypecode("I9");
        gitemVO.setNnumber(null);
        gitemVO.setNprice(null);
        gitemVO.setCprojectid(sWithdrawID);
        if (nSettleMny != null) {
          double d = nSettleMny.doubleValue();
          gitemVO.setNmoney(
            new UFDouble(d * (
            17.0D - dWithdrawRate.doubleValue()) / 100.0D));
        }

        Vector vTemp = new Vector();
        vTemp.addElement(gitemVO);
        IBillItemVO[] tempVO = new IBillItemVO[1];
        vTemp.copyInto(tempVO);
        gbillVO.setChildrenVO(tempVO);
        gbillVO.setParentVO(gheaderVO);

        ((IBillHeaderVO)gbillVO.getParentVO())
          .setBestimateflag(headerVO.getBestimateflag());

        if ((gitemVO.getNmoney() != null) && 
          (gitemVO.getNmoney().doubleValue() != 0.0D)) {
          vReturn.addElement(gbillVO);
        }
      }

    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }

    if (vReturn.size() > 0) {
      return (BillVO)vReturn.elementAt(0);
    }

    return null;
  }

  public String[] getAccountBankIDBatch(String[] cVendorBaseID)
    throws BusinessException
  {
    FormulaParse f = new FormulaParse();

    String sExpress = "getColValue(bd_custbank,pk_custbank,pk_cubasdoc,cVendorBaseID)";
    f.setExpress(sExpress);
    VarryVO varry = f.getVarry();
    Hashtable table = new Hashtable();
    String[] sParam = new String[cVendorBaseID.length];
    for (int i = 0; i < cVendorBaseID.length; i++)
      sParam[i] = nc.vo.pub.formulaset.util.StringUtil.toString(cVendorBaseID[i]);
    table.put(varry.getVarry()[0], sParam);
    f.setDataS(table);
    String[] sBankKey = f.getValueS();

    if ((sBankKey != null) && (sBankKey.length > 0))
      return sBankKey;
    return null;
  }

  public String[] getCorpIDs()
    throws BusinessException
  {
    String[] s = (String[])null;
    try {
      SettleDMO dmo = new SettleDMO();
      s = dmo.getCorpIDs();
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }

    return s;
  }

  public Vector getCustIDAndNameFromCust(String pk_corp)
    throws BusinessException
  {
    Vector v = null;
    try {
      SettleDMO dmo = new SettleDMO();
      v = dmo.getCustIDAndNameFromCust(pk_corp);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }

    return v;
  }

  public Vector getCustIDAndNameFromVendor(String pk_corp)
    throws BusinessException
  {
    Vector v = null;
    try {
      SettleDMO dmo = new SettleDMO();
      v = dmo.getCustIDAndNameFromVendor(pk_corp);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }

    return v;
  }

  private String getInvoiceCode(InvoiceHeaderVO headVO)
    throws BusinessException
  {
    String vBillCode = null;
    BillcodeGenerater myService = null;
    try
    {
      BillCodeObjValueVO vo = new BillCodeObjValueVO();

      String[] sNames = { "公司", "部门", "业务类型" };
      String[] sKeys = { "pk_corp", "cdeptid", "cbiztype" };
      for (int i = 0; i < sNames.length; i++) {
        Object o = headVO.getAttributeValue(sKeys[i]);
        vo.setAttributeValue(sNames[i], o);
      }

      myService = new BillcodeGenerater();

      vBillCode = headVO.getVinvoicecode();
      if ((vBillCode == null) || (vBillCode.length() == 0)) {
        vBillCode = null;
      }
      vBillCode = myService.getBillCode(headVO.getCbilltype(), headVO
        .getPk_corp(), vBillCode, vo);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }

    return vBillCode;
  }

  private Vector getInvoiceLockKeys(IinvoiceVO[] invoiceVOs, FeeinvoiceVO[] feeVOs, FeeinvoiceVO[] discountVOs, FeeinvoiceVO[] specialVOs, Vector v)
  {
    if ((v == null) || (v.size() == 0)) {
      v = new Vector();
    }

    Vector v1 = new Vector();
    Vector v2 = new Vector();
    if ((invoiceVOs != null) && (invoiceVOs.length > 0)) {
      for (int i = 0; i < invoiceVOs.length; i++) {
        v1.addElement(invoiceVOs[i].getCinvoiceid());
        v2.addElement(invoiceVOs[i].getCinvoice_bid());
      }
    }
    if ((feeVOs != null) && (feeVOs.length > 0)) {
      for (int i = 0; i < feeVOs.length; i++) {
        v1.addElement(feeVOs[i].getCinvoiceid());
        v2.addElement(feeVOs[i].getCinvoice_bid());
      }
    }
    if ((discountVOs != null) && (discountVOs.length > 0)) {
      for (int i = 0; i < discountVOs.length; i++) {
        v1.addElement(discountVOs[i].getCinvoiceid());
        v2.addElement(discountVOs[i].getCinvoice_bid());
      }
    }
    if ((specialVOs != null) && (specialVOs.length > 0)) {
      for (int i = 0; i < specialVOs.length; i++) {
        v1.addElement(specialVOs[i].getCinvoiceid());
        v2.addElement(specialVOs[i].getCinvoice_bid());
      }

    }

    if ((v1 != null) && (v1.size() > 0))
    {
      v.addElement(v1.elementAt(0));
      for (int i = 1; i < v1.size(); i++) {
        String s1 = (String)v1.elementAt(i);
        s1 = s1.trim();
        if (!v.contains(s1)) {
          v.addElement(s1);
        }
      }
    }

    if ((v2 != null) && (v2.size() > 0))
    {
      v.addElement(v2.elementAt(0));
      for (int i = 1; i < v2.size(); i++) {
        String s1 = (String)v2.elementAt(i);
        s1 = s1.trim();
        if (!v.contains(s1)) {
          v.addElement(s1);
        }
      }
    }
    return v;
  }

  public Vector getIUnitWeightAndVolume(IinvoiceVO[] vo)
    throws BusinessException
  {
    FormulaParse f = new FormulaParse();

    String sExpress1 = "getColValue(bd_invbasdoc,unitweight,pk_invbasdoc,cBaseid)";

    String sExpress2 = "getColValue(bd_invbasdoc,unitvolume,pk_invbasdoc,cBaseid)";

    f.setExpressArray(new String[] { sExpress1, sExpress2 });

    VarryVO[] varry = f.getVarryArray();

    String[] sParam = new String[vo.length];
    for (int i = 0; i < vo.length; i++) {
      sParam[i] = vo[i].getCbaseid();
    }
    f.addVariable(varry[0].getVarry()[0], sParam);
    f.addVariable(varry[1].getVarry()[0], sParam);

    String[] sUnitWeight = f.getValueSArray()[0];
    String[] sUnitVolume = f.getValueSArray()[1];

    Vector v = new Vector();
    for (int i = 0; i < vo.length; i++) {
      Vector vv = new Vector();
      vv.addElement(sUnitWeight[i]);
      vv.addElement(sUnitVolume[i]);
      v.addElement(vv);
    }

    return v;
  }

  public SaledataVO[] getRefSaleInvoice(SaledataVO[] VOs)
    throws BusinessException
  {
    FormulaParse f = new FormulaParse();

    String sExpress1 = "getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,cMangid)";

    String sExpress2 = "getColValue(bd_invmandoc,pk_cumandoc,pk_invmandoc,cMangid)";
    f.setExpressArray(new String[] { sExpress1, sExpress2 });
    VarryVO[] varry = f.getVarryArray();
    String[] sParam = new String[VOs.length];
    for (int i = 0; i < sParam.length; i++)
      sParam[i] = VOs[i].getCmangid();
    f.addVariable(varry[0].getVarry()[0], sParam);
    f.addVariable(varry[1].getVarry()[0], sParam);
    String[] sBaseid = f.getValueSArray()[0];
    String[] sVendormangid = f.getValueSArray()[1];

    for (int i = 0; i < VOs.length; i++) {
      VOs[i].setCbaseid(sBaseid[i]);
      VOs[i].setCvendormangid(sVendormangid[i]);
    }
    return VOs;
  }

  private String getReplacedSQL(String sSource, String sOld, String sReplace)
  {
    if ((sReplace == null) || (sReplace.trim().length() == 0))
      return sSource;
    int nStart = sSource.indexOf(sOld);
    if (nStart < 0)
      return sSource;
    int nMiddle = sSource.indexOf("'", nStart + 1);
    if (nMiddle < 0)
      return sSource;
    int nEnd = sSource.indexOf("'", nMiddle + 1);
    String s1 = sSource.substring(0, nStart);
    String s2 = sSource.substring(nEnd + 1);
    String s = s1 + sReplace + s2;
    return s;
  }

  private String getSettleBillCode(SettlebillHeaderVO head)
    throws BusinessException
  {
    String vSettleCode = null;
    try
    {
      SettlebillVO vo = new SettlebillVO(1);
      vo.setParentVO(head);
      vo.setChildrenVO(new SettlebillItemVO[] { new SettlebillItemVO() });
      GetSysBillCode billCode = new GetSysBillCode();
      do
        vSettleCode = billCode.getSysBillNO(vo);
      while (
        isSettlebillCodeDuplicate(head.getPk_corp(), vSettleCode));
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return vSettleCode;
  }

  private String[] getSettleLockKeys(StockVO[] stockVOs, IinvoiceVO[] invoiceVOs, FeeinvoiceVO[] feeVOs, FeeinvoiceVO[] discountVOs, FeeinvoiceVO[] specialVOs, OorderVO[] orderVOs, MaterialVO[] materialVOs)
  {
    Vector v = new Vector();

    if ((stockVOs != null) && (stockVOs.length > 0))
    {
      v.addElement(stockVOs[0].getCgeneralhid().trim());
      for (int i = 1; i < stockVOs.length; i++) {
        String s1 = stockVOs[i].getCgeneralhid().trim();
        if (!v.contains(s1)) {
          v.addElement(s1);
        }
      }
      v.addElement(stockVOs[0].getCgeneralbid().trim());
      for (int i = 1; i < stockVOs.length; i++) {
        String s1 = stockVOs[i].getCgeneralbid().trim();
        if (!v.contains(s1)) {
          v.addElement(s1);
        }
      }
      v.addElement(stockVOs[0].getCgeneralbb3().trim());
      for (int i = 1; i < stockVOs.length; i++) {
        String s1 = stockVOs[i].getCgeneralbb3().trim();
        if (!v.contains(s1)) {
          v.addElement(s1);
        }
      }
    }
    v = getInvoiceLockKeys(invoiceVOs, feeVOs, discountVOs, specialVOs, v);

    if ((orderVOs != null) && (orderVOs.length > 0))
    {
      v.addElement(orderVOs[0].getCorderid().trim());
      for (int i = 1; i < orderVOs.length; i++) {
        String s1 = orderVOs[i].getCorderid().trim();
        if (!v.contains(s1)) {
          v.addElement(s1);
        }
      }
      v.addElement(orderVOs[0].getCorder_bid().trim());
      for (int i = 1; i < orderVOs.length; i++) {
        String s1 = orderVOs[i].getCorder_bid().trim();
        if (!v.contains(s1)) {
          v.addElement(s1);
        }
      }
    }
    if ((materialVOs != null) && (materialVOs.length > 0))
    {
      v.addElement(materialVOs[0].getCgeneralhid().trim());
      for (int i = 1; i < materialVOs.length; i++) {
        String s1 = materialVOs[i].getCgeneralhid().trim();
        if (!v.contains(s1)) {
          v.addElement(s1);
        }
      }
      v.addElement(materialVOs[0].getCgeneralbid().trim());
      for (int i = 1; i < materialVOs.length; i++) {
        String s1 = materialVOs[i].getCgeneralbid().trim();
        if (!v.contains(s1)) {
          v.addElement(s1);
        }
      }
    }
    if (v.size() > 0) {
      String[] keys = new String[v.size()];
      v.copyInto(keys);
      return keys;
    }
    return null;
  }

  private GeneralHItemVO[] getStockBodyInfo(SettlebillItemVO[] itemVO)
    throws BusinessException
  {
    if ((itemVO == null) || (itemVO.length == 0)) {
      return null;
    }
    GeneralHItemVO[] bodyVO = (GeneralHItemVO[])null;
    Vector v = new Vector();
    for (int i = 0; i < itemVO.length; i++) {
      String s = itemVO[i].getCstockrow();
      if ((s != null) && (s.trim().length() > 0))
        v.addElement(s);
    }
    if (v.size() == 0)
      return null;
    String[] bodyKey = new String[v.size()];
    v.copyInto(bodyKey);
    try {
      EstimateDMO dmo = new EstimateDMO();

      bodyVO = dmo.queryStockBodyForIA(bodyKey);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return bodyVO;
  }

  private GeneralHHeaderVO[] getStockHeadInfo(SettlebillItemVO[] itemVO)
    throws BusinessException
  {
    if ((itemVO == null) || (itemVO.length == 0)) {
      return null;
    }
    GeneralHHeaderVO[] headVO = (GeneralHHeaderVO[])null;
    Vector v = new Vector();
    for (int i = 0; i < itemVO.length; i++) {
      String s = itemVO[i].getCstockid();
      if ((s != null) && (s.trim().length() > 0))
        v.addElement(s);
    }
    if (v.size() == 0)
      return null;
    String[] headKey = new String[v.size()];
    v.copyInto(headKey);
    try {
      EstimateDMO dmo = new EstimateDMO();

      headVO = dmo.queryStockHeadForIA(headKey);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return headVO;
  }

  public Vector getSUnitWeightAndVolume(StockVO[] vo)
    throws BusinessException
  {
    FormulaParse f = new FormulaParse();

    String sExpress1 = "getColValue(bd_invbasdoc,unitweight,pk_invbasdoc,cBaseid)";

    String sExpress2 = "getColValue(bd_invbasdoc,unitvolume,pk_invbasdoc,cBaseid)";
    f.setExpressArray(new String[] { sExpress1, sExpress2 });
    VarryVO[] varry = f.getVarryArray();
    String[] sParam = new String[vo.length];
    for (int i = 0; i < vo.length; i++) {
      sParam[i] = vo[i].getCbaseid();
    }
    f.addVariable(varry[0].getVarry()[0], sParam);
    f.addVariable(varry[1].getVarry()[0], sParam);

    String[] sUnitWeight = f.getValueSArray()[0];
    String[] sUnitVolume = f.getValueSArray()[1];

    Vector v = new Vector();
    for (int i = 0; i < vo.length; i++) {
      Vector vv = new Vector();
      vv.addElement(sUnitWeight[i]);
      vv.addElement(sUnitVolume[i]);
      v.addElement(vv);
    }
    return v;
  }

  private String[] getVendorBaseKey(String[] cVendorID)
  {
    FormulaParse f = new FormulaParse();

    String sExpress = "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,cVendorID)";
    f.setExpress(sExpress);
    VarryVO varry = f.getVarry();
    Hashtable table = new Hashtable();
    String[] sParam = new String[cVendorID.length];
    for (int i = 0; i < sParam.length; i++)
      sParam[i] = nc.vo.pub.formulaset.util.StringUtil.toString(cVendorID[i]);
    table.put(varry.getVarry()[0], sParam);
    f.setDataS(table);
    String[] sVendorBaseID = f.getValueS();
    return sVendorBaseID;
  }

  public ArrayList getVendorNameAndInvByBaseIDBatch(String[] cVendorID, String[] cBaseID, String[] cStoreorganization)
    throws BusinessException
  {
    for (int i = 0; i < cVendorID.length; i++)
      cVendorID[i] = nc.vo.pub.formulaset.util.StringUtil.toString(cVendorID[i]);
    for (int i = 0; i < cBaseID.length; i++)
      cBaseID[i] = nc.vo.pub.formulaset.util.StringUtil.toString(cBaseID[i]);
    for (int i = 0; i < cStoreorganization.length; i++) {
      cStoreorganization[i] = nc.vo.pub.formulaset.util.StringUtil.toString(cStoreorganization[i]);
    }
    FormulaParse f = new FormulaParse();

    String sExpress = "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,cVendorID)";
    f.setExpress(sExpress);
    VarryVO varry = f.getVarry();
    Hashtable table = new Hashtable();
    table.put(varry.getVarry()[0], cVendorID);
    f.setDataS(table);
    String[] sVendorBaseID = f.getValueS();

    sExpress = "getColValue(bd_cubasdoc,custshortname,pk_cubasdoc,cBaseID)";
    f.setExpress(sExpress);
    varry = f.getVarry();
    table.put(varry.getVarry()[0], sVendorBaseID);
    f.setDataS(table);
    String[] sVendorName = f.getValueS();

    sExpress = "getColValue(bd_invbasdoc,invname,pk_invbasdoc,cInvID)";
    f.setExpress(sExpress);
    varry = f.getVarry();
    table.put(varry.getVarry()[0], cBaseID);
    f.setDataS(table);
    String[] sInvName = f.getValueS();

    sExpress = "getColValue(bd_calbody,bodyname,pk_calbody,pk_calbody)";
    f.setExpress(sExpress);
    varry = f.getVarry();
    table.put(varry.getVarry()[0], cStoreorganization);
    f.setDataS(table);
    String[] sStoreorganizationName = f.getValueS();
    ArrayList list = new ArrayList();
    list.add(sVendorName);
    list.add(sInvName);
    list.add(sStoreorganizationName);
    return list;
  }

  public boolean isCodeDuplicateForSettle(String unitCode, String billCode, String key)
    throws BusinessException
  {
    boolean b = false;
    try {
      SettleDMO dmo = new SettleDMO();
      b = dmo.isCodeDuplicate(unitCode, billCode, key);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return b;
  }

  private boolean isFromVMI(SettlebillVO settleVO)
    throws BusinessException
  {
    if ((settleVO == null) || (settleVO.getChildrenVO() == null) || 
      (settleVO.getChildrenVO().length <= 0))
      return false;
    boolean bIsFromVMI = false;
    SettlebillItemVO[] bodyVOs = (SettlebillItemVO[])settleVO
      .getChildrenVO();
    int iLen = bodyVOs.length;
    for (int i = 0; i < iLen; i++) {
      if ((bodyVOs[i].getCvmiid() == null) || 
        (bodyVOs[i].getCvmiid().trim().length() <= 0)) continue;
      bIsFromVMI = true;
      break;
    }

    return bIsFromVMI;
  }

  public boolean isSettlebillCodeDuplicate(String unitCode, String billCode)
    throws BusinessException
  {
    boolean b = false;
    try {
      SettleDMO dmo = new SettleDMO();
      b = dmo.isSettlebillCodeDuplicate(unitCode, billCode);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return b;
  }

  private String isTimeStampChangedForDepose(StockVO[] stockVOs, IinvoiceVO[] iinvoiceVOs, FeeinvoiceVO[] feeVOs, FeeinvoiceVO[] discountVOs, FeeinvoiceVO[] specialVOs, SettlebillVO[] settlebillVOs)
    throws BusinessException
  {
      String sMessage = "";
      Timer timer = new Timer();
      timer.start();
      try
      {
          PubDMO pubDMO = new PubDMO();
          String sHeadKey[] = (String[])null;
          String sBodyKey[] = (String[])null;
          String sBody3Key[] = (String[])null;
          if(stockVOs != null && stockVOs.length > 0)
          {
              int iLen = stockVOs.length;
              sHeadKey = new String[iLen];
              sBodyKey = new String[iLen];
              sBody3Key = new String[iLen];
              for(int i = 0; i < iLen; i++)
              {
                  sHeadKey[i] = stockVOs[i].getCgeneralhid();
                  sBodyKey[i] = stockVOs[i].getCgeneralbid();
                  sBody3Key[i] = stockVOs[i].getCgeneralbb3();
              }

              Object ts[] = pubDMO.queryHBTsArrayByHBIDArray("45", sHeadKey, sBodyKey, sBody3Key);
              if(ts != null && ts.length > 0)
              {
                  String ts1[] = (String[])ts[0];
                  String ts2[] = (String[])ts[1];
                  String ts3[] = (String[])ts[2];
                  if(ts1 != null && ts1.length > 0 && ts1.length == iLen)
                  {
                      for(int i = 0; i < iLen; i++)
                      {
                          String temp1 = ts1[i] != null ? ts1[i].trim() : "";
                          String temp2 = stockVOs[i].getTs1() != null ? stockVOs[i].getTs1().trim() : "";
                          if(temp1.equals(temp2))
                              continue;
                          sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000014")).toString();
                          break;
                      }

                  } else
                  {
                      sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000014")).toString();
                  }
                  if(ts2 != null && ts2.length > 0 && ts2.length == iLen)
                  {
                      for(int i = 0; i < iLen; i++)
                      {
                          String temp1 = ts2[i] != null ? ts2[i].trim() : "";
                          String temp2 = stockVOs[i].getTs2() != null ? stockVOs[i].getTs2().trim() : "";
                          if(temp1.equals(temp2))
                              continue;
                          sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000015")).toString();
                          break;
                      }

                  } else
                  {
                      sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000015")).toString();
                  }
                  if(ts3 != null && ts3.length > 0 && ts3.length == iLen)
                  {
                      for(int i = 0; i < iLen; i++)
                      {
                          String temp1 = ts3[i] != null ? ts3[i].trim() : "";
                          String temp2 = stockVOs[i].getTs3() != null ? stockVOs[i].getTs3().trim() : "";
                          if(temp1.equals(temp2))
                              continue;
                          sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000016")).toString();
                          break;
                      }

                  } else
                  {
                      sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000016")).toString();
                  }
              }
          }
          timer.addExecutePhase("\u5165\u5E93\u5355");
          if(iinvoiceVOs != null && iinvoiceVOs.length > 0)
          {
              int iLenInv = iinvoiceVOs.length;
              sHeadKey = new String[iLenInv];
              sBodyKey = new String[iLenInv];
              for(int i = 0; i < iLenInv; i++)
              {
                  sHeadKey[i] = iinvoiceVOs[i].getCinvoiceid();
                  sBodyKey[i] = iinvoiceVOs[i].getCinvoice_bid();
              }

              Object ts[] = pubDMO.queryHBTsArrayByHBIDArray("25", sHeadKey, sBodyKey, null);
              if(ts != null && ts.length > 0)
              {
                  String ts1[] = (String[])ts[0];
                  String ts2[] = (String[])ts[1];
                  if(ts1 != null && ts1.length > 0 && ts1.length == iLenInv)
                  {
                      for(int i = 0; i < iLenInv; i++)
                      {
                          String temp1 = ts1[i] != null ? ts1[i].trim() : "";
                          String temp2 = iinvoiceVOs[i].getTs1() != null ? iinvoiceVOs[i].getTs1().trim() : "";
                          if(temp1.equals(temp2))
                              continue;
                          sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000017")).toString();
                          break;
                      }

                  } else
                  {
                      sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000017")).toString();
                  }
                  if(ts2 != null && ts2.length > 0 && ts2.length == iLenInv)
                  {
                      for(int i = 0; i < iLenInv; i++)
                      {
                          String temp1 = ts2[i] != null ? ts2[i].trim() : "";
                          String temp2 = iinvoiceVOs[i].getTs2() != null ? iinvoiceVOs[i].getTs2().trim() : "";
                          if(temp1.equals(temp2))
                              continue;
                          sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000018")).toString();
                          break;
                      }

                  } else
                  {
                      sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000018")).toString();
                  }
              }
          }
          timer.addExecutePhase("\u53D1\u7968");
          if(feeVOs != null && feeVOs.length > 0)
          {
              int iLenFee = feeVOs.length;
              sHeadKey = new String[iLenFee];
              sBodyKey = new String[iLenFee];
              for(int i = 0; i < iLenFee; i++)
              {
                  sHeadKey[i] = feeVOs[i].getCinvoiceid();
                  sBodyKey[i] = feeVOs[i].getCinvoice_bid();
              }

              Object ts[] = pubDMO.queryHBTsArrayByHBIDArray("25", sHeadKey, sBodyKey, null);
              if(ts != null && ts.length > 0)
              {
                  String ts1[] = (String[])ts[0];
                  String ts2[] = (String[])ts[1];
                  if(ts1 != null && ts1.length > 0 && ts1.length == iLenFee)
                  {
                      for(int i = 0; i < iLenFee; i++)
                      {
                          String temp1 = ts1[i] != null ? ts1[i].trim() : "";
                          String temp2 = feeVOs[i].getTs1() != null ? feeVOs[i].getTs1().trim() : "";
                          if(temp1.equals(temp2))
                              continue;
                          sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000019")).toString();
                          break;
                      }

                  } else
                  {
                      sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000019")).toString();
                  }
                  if(ts2 != null && ts2.length > 0 && ts2.length == iLenFee)
                  {
                      for(int i = 0; i < iLenFee; i++)
                      {
                          String temp1 = ts2[i] != null ? ts2[i].trim() : "";
                          String temp2 = feeVOs[i].getTs2() != null ? feeVOs[i].getTs2().trim() : "";
                          if(temp1.equals(temp2))
                              continue;
                          sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000020")).toString();
                          break;
                      }

                  } else
                  {
                      sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000020")).toString();
                  }
              }
          }
          timer.addExecutePhase("\u8D39\u7528\u53D1\u7968");
          if(discountVOs != null && discountVOs.length > 0)
          {
              int iLenDis = discountVOs.length;
              sHeadKey = new String[iLenDis];
              sBodyKey = new String[iLenDis];
              for(int i = 0; i < iLenDis; i++)
              {
                  sHeadKey[i] = discountVOs[i].getCinvoiceid();
                  sBodyKey[i] = discountVOs[i].getCinvoice_bid();
              }

              Object ts[] = pubDMO.queryHBTsArrayByHBIDArray("25", sHeadKey, sBodyKey, null);
              if(ts != null && ts.length > 0)
              {
                  String ts1[] = (String[])ts[0];
                  String ts2[] = (String[])ts[1];
                  if(ts1 != null && ts1.length > 0 && ts1.length == iLenDis)
                  {
                      for(int i = 0; i < iLenDis; i++)
                      {
                          String temp1 = ts1[i] != null ? ts1[i].trim() : "";
                          String temp2 = discountVOs[i].getTs1() != null ? discountVOs[i].getTs1().trim() : "";
                          if(temp1.equals(temp2))
                              continue;
                          sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000021")).toString();
                          break;
                      }

                  } else
                  {
                      sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000021")).toString();
                  }
                  if(ts2 != null && ts2.length > 0 && ts2.length == iLenDis)
                  {
                      for(int i = 0; i < iLenDis; i++)
                      {
                          String temp1 = ts2[i] != null ? ts2[i].trim() : "";
                          String temp2 = discountVOs[i].getTs2() != null ? discountVOs[i].getTs2().trim() : "";
                          if(temp1.equals(temp2))
                              continue;
                          sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000022")).toString();
                          break;
                      }

                  } else
                  {
                      sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000022")).toString();
                  }
              }
          }
          timer.addExecutePhase("\u6298\u6263");
          if(specialVOs != null && specialVOs.length > 0)
          {
              int iLenSpe = specialVOs.length;
              sHeadKey = new String[iLenSpe];
              sBodyKey = new String[iLenSpe];
              for(int i = 0; i < iLenSpe; i++)
              {
                  sHeadKey[i] = specialVOs[i].getCinvoiceid();
                  sBodyKey[i] = specialVOs[i].getCinvoice_bid();
              }

              Object ts[] = pubDMO.queryHBTsArrayByHBIDArray("25", sHeadKey, sBodyKey, null);
              if(ts != null && ts.length > 0)
              {
                  String ts1[] = (String[])ts[0];
                  String ts2[] = (String[])ts[1];
                  if(ts1 != null && ts1.length > 0 && ts1.length == iLenSpe)
                  {
                      for(int i = 0; i < iLenSpe; i++)
                      {
                          String temp1 = ts1[i] != null ? ts1[i].trim() : "";
                          String temp2 = specialVOs[i].getTs1() != null ? specialVOs[i].getTs1().trim() : "";
                          if(temp1.equals(temp2))
                              continue;
                          sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000023")).toString();
                          break;
                      }

                  } else
                  {
                      sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000023")).toString();
                  }
                  if(ts2 != null && ts2.length > 0 && ts2.length == iLenSpe)
                  {
                      for(int i = 0; i < iLenSpe; i++)
                      {
                          String temp1 = ts2[i] != null ? ts2[i].trim() : "";
                          String temp2 = specialVOs[i].getTs2() != null ? specialVOs[i].getTs2().trim() : "";
                          if(temp1.equals(temp2))
                              continue;
                          sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000024")).toString();
                          break;
                      }

                  } else
                  {
                      sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000024")).toString();
                  }
              }
          }
          timer.addExecutePhase("\u4E00\u822C\u53D1\u7968\uFF08\u65E0\u6570\u91CF\uFF09");
          if(settlebillVOs != null && settlebillVOs.length > 0)
          {
              int iLenSett = settlebillVOs.length;
              sHeadKey = new String[iLenSett];
              for(int i = 0; i < iLenSett; i++)
                  sHeadKey[i] = settlebillVOs[i].getHeadVO().getCsettlebillid();

              Object ts[] = pubDMO.queryHBTsArrayByHBIDArray("27", sHeadKey, null, null);
              if(ts != null && ts.length > 0)
              {
                  String ts1[] = (String[])ts[0];
                  if(ts1 != null && ts1.length > 0 && ts1.length == iLenSett)
                  {
                      for(int i = 0; i < iLenSett; i++)
                      {
                          String temp1 = ts1[i] != null ? ts1[i].trim() : "";
                          String temp2 = settlebillVOs[i].getHeadVO().getTs() != null ? settlebillVOs[i].getHeadVO().getTs().trim() : "";
                          if(temp1.equals(temp2))
                              continue;
                          sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000025")).toString();
                          break;
                      }

                  } else
                  {
                      sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000025")).toString();
                  }
              }
          }
          timer.addExecutePhase("\u7ED3\u7B97\u5355\u5934");
          timer.showAllExecutePhase("\u5224\u65AD\u65F6\u95F4\u6233\u662F\u5426\u6539\u53D8(\u660E\u7EC6)");
      }
      catch(Exception e)
      {
          PubDMO.throwBusinessException(e);
      }
      return sMessage;
  }

  private String isTimeStampChangedForSales(StockVO[] stockVOs, OorderVO[] orderVOs, IdTsData struIdTs)
    throws BusinessException
  {
      String sMessage = "";
      try
      {
          PubDMO pubDMO = new PubDMO();
          String sHeadKey[] = (String[])null;
          String sBodyKey[] = (String[])null;
          String sBBKey[] = (String[])null;
          if(stockVOs != null && stockVOs.length > 0)
          {
              int iLen = stockVOs.length;
              sHeadKey = new String[iLen];
              sBodyKey = new String[iLen];
              sBBKey = new String[iLen];
              for(int i = 0; i < iLen; i++)
              {
                  sHeadKey[i] = stockVOs[i].getCgeneralhid();
                  sBodyKey[i] = stockVOs[i].getCgeneralbid();
                  sBBKey[i] = stockVOs[i].getCgeneralbb3();
              }

              Object ts[] = pubDMO.queryHBTsArrayByHBIDArray("45", sHeadKey, sBodyKey, sBBKey);
              if(ts != null && ts.length > 0)
              {
                  String ts1[] = (String[])ts[0];
                  String ts2[] = (String[])ts[1];
                  String ts3[] = (String[])ts[2];
                  if(ts1 != null && ts1.length > 0 && ts1.length == iLen)
                  {
                      for(int i = 0; i < iLen; i++)
                      {
                          String temp1 = ts1[i] != null ? ts1[i].trim() : "";
                          String temp2 = stockVOs[i].getTs1() != null ? stockVOs[i].getTs1().trim() : "";
                          if(temp1.equals(temp2))
                              continue;
                          sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000014")).toString();
                          break;
                      }

                  } else
                  {
                      sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000014")).toString();
                  }
                  if(ts2 != null && ts2.length > 0 && ts2.length == iLen)
                  {
                      for(int i = 0; i < iLen; i++)
                      {
                          String temp1 = ts2[i] != null ? ts2[i].trim() : "";
                          String temp2 = stockVOs[i].getTs2() != null ? stockVOs[i].getTs2().trim() : "";
                          if(temp1.equals(temp2))
                              continue;
                          sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000015")).toString();
                          break;
                      }

                  } else
                  {
                      sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000015")).toString();
                  }
                  if(ts3 != null && ts3.length > 0 && ts3.length == iLen)
                  {
                      for(int i = 0; i < iLen; i++)
                      {
                          String temp1 = ts3[i] != null ? ts3[i].trim() : "";
                          String temp2 = stockVOs[i].getTs3() != null ? stockVOs[i].getTs3().trim() : "";
                          if(temp1.equals(temp2))
                              continue;
                          sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000026")).toString();
                          break;
                      }

                  } else
                  {
                      sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000026")).toString();
                  }
              }
          }
          if(orderVOs != null && orderVOs.length > 0)
          {
              int iLen = orderVOs.length;
              sHeadKey = new String[iLen];
              sBodyKey = new String[iLen];
              for(int i = 0; i < iLen; i++)
              {
                  sHeadKey[i] = orderVOs[i].getCorderid();
                  sBodyKey[i] = orderVOs[i].getCorder_bid();
              }

              Object ts[] = pubDMO.queryHBTsArrayByHBIDArray("21", sHeadKey, sBodyKey, null);
              if(ts != null && ts.length > 0)
              {
                  String ts1[] = (String[])ts[0];
                  String ts2[] = (String[])ts[1];
                  if(ts1 != null && ts1.length > 0 && ts1.length == iLen)
                  {
                      for(int i = 0; i < iLen; i++)
                      {
                          String temp1 = ts1[i] != null ? ts1[i].trim() : "";
                          String temp2 = orderVOs[i].getTs1() != null ? orderVOs[i].getTs1().trim() : "";
                          if(!temp1.equals(temp2))
                              sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000027")).toString();
                      }

                  } else
                  {
                      sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000027")).toString();
                  }
                  if(ts2 != null && ts2.length > 0 && ts2.length == iLen)
                  {
                      for(int i = 0; i < iLen; i++)
                      {
                          String temp1 = ts2[i] != null ? ts2[i].trim() : "";
                          String temp2 = orderVOs[i].getTs2() != null ? orderVOs[i].getTs2().trim() : "";
                          if(!temp1.equals(temp2))
                              sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000028")).toString();
                      }

                  } else
                  {
                      sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000028")).toString();
                  }
              }
          }
          if(struIdTs != null && "po_saledata".equalsIgnoreCase(struIdTs.getTableName()) && struIdTs.getIDs() != null && struIdTs.getIDs().length > 0)
          {
              sHeadKey = struIdTs.getIDs();
              Object ts[] = pubDMO.queryHBTsArrayByHBIDArray("2#", sHeadKey, null, null);
              if(ts != null && ts.length > 0)
              {
                  String ts1[] = (String[])ts[0];
                  String saTs[] = struIdTs.getTSs();
                  int iLen = saTs.length;
                  if(ts1 != null && ts1.length > 0 && ts1.length == iLen)
                  {
                      for(int i = 0; i < iLen; i++)
                      {
                          if(saTs[i] == null)
                              saTs[i] = "";
                          if(ts1[i] == null)
                              ts1[i] = "";
                          if(saTs[i].equals(ts1[i]))
                              continue;
                          sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000029")).toString();
                          break;
                      }

                  } else
                  {
                      sMessage = (new StringBuilder(String.valueOf(sMessage))).append(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000029")).toString();
                  }
              }
          }
      }
      catch(Exception e)
      {
          PubDMO.throwBusinessException(e);
      }
      return sMessage;
  }

  private String isTimeStampChangedForSettle(StockVO[] stockVOs, IinvoiceVO[] iinvoiceVOs, FeeinvoiceVO[] feeVOs, FeeinvoiceVO[] discountVOs, FeeinvoiceVO[] specialVOs)
    throws BusinessException
  {
    return isTimeStampChangedForDepose(stockVOs, iinvoiceVOs, feeVOs, 
      discountVOs, specialVOs, null);
  }

  public ArrayList onAutoBalance(ArrayList listPara)
    throws BusinessException
  {
    Timer timeDebug = new Timer();
    timeDebug.start();

    if ((listPara == null) || (listPara.size() < 5)) {
      SCMEnv.out("程序BUG：传入参数不正确，返回NULL！");
      return null;
    }

    ArrayList listRet = new ArrayList();
    String m_sUnitCode = (String)listPara.get(0);
    ConditionVO[] conditionVO = (ConditionVO[])listPara.get(1);

    boolean m_bStockRB = true;
    boolean m_bInvoiceRB = true;
    try {
      ISysInitQry srcSysInit = (ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName());
      SysInitVO[] initVO = srcSysInit.querySysInit(
        m_sUnitCode, "PO36");
      if ((initVO == null) || (initVO.length == 0)) {
        throw new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("40040502", 
          "UPP40040502-000030"));
      }

      if (initVO[0].getValue().equals("否"))
        m_bStockRB = false;
      if (initVO[1].getValue().equals("否"))
        m_bInvoiceRB = false;
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    timeDebug.addExecutePhase("是否红蓝对冲参数获取");

    IinvoiceVO[] invoiceVOs = (IinvoiceVO[])null;
    StockVO[] stockVOs = (StockVO[])null;
    try
    {
      stockVOs = queryStock(m_sUnitCode, conditionVO);
      boolean bStockNull = (stockVOs == null) || (stockVOs.length == 0);

      if ((!m_bStockRB) && (!m_bInvoiceRB) && (bStockNull)) {
        throw new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("40040502", 
          "UPP40040502-000031"));
      }

      timeDebug.addExecutePhase("查询入库单");

      ArrayList listPara1 = new ArrayList();
      listPara1.add(m_sUnitCode);
      listPara1.add(conditionVO);
      listPara1.add(new UFBoolean(false));
      invoiceVOs = queryAllInvoice(listPara1);
      timeDebug.addExecutePhase("查询发票(包括费用发票和折扣)");

      boolean bInvoiceNull = (invoiceVOs == null) || (invoiceVOs.length == 0);

      if ((bStockNull) && (bInvoiceNull)) {
        throw new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("40040502", 
          "UPP40040502-000032"));
      }

      if ((!m_bStockRB) && (!m_bInvoiceRB) && (bInvoiceNull)) {
        throw new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("40040502", 
          "UPP40040502-000033"));
      }

      if ((!m_bStockRB) && (m_bInvoiceRB) && (bInvoiceNull)) {
        throw new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("40040502", 
          "UPP40040502-000034"));
      }

      if ((m_bStockRB) && (!m_bInvoiceRB) && (bStockNull)) {
        throw new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("40040502", 
          "UPP40040502-000035"));
      }

      listRet = doAutoBalance(listPara, invoiceVOs, stockVOs, m_bStockRB, 
        m_bInvoiceRB);
      timeDebug.addExecutePhase("自动结算");

      timeDebug.showAllExecutePhase("自动结算时间分布-onAutoBalance()-(总)");
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return listRet;
  }

  private IinvoiceVO[] queryAllInvoice(ArrayList listPara)
    throws BusinessException
  {
    if ((listPara == null) || (listPara.size() < 3)) {
      SCMEnv.out("程序BUG：查询发票（不含费用发票和折扣发票）参数为NULL！");
      return null;
    }
    String sUnitCode = (String)listPara.get(0);
    ConditionVO[] conditionVOs = (ConditionVO[])listPara.get(1);
    UFBoolean bIsVMI = (UFBoolean)listPara.get(2);

    if ((sUnitCode == null) || (bIsVMI == null)) {
      SCMEnv.out("程序BUG：查询发票（不含费用发票和折扣发票）参数存在NULL！");
      return null;
    }
    SettleDMO dmo = null;
    IinvoiceVO[] vo = (IinvoiceVO[])null;
    try {
      dmo = new SettleDMO();

      String sCondition = "";

      ArrayList listRet = dealCondVosForPowerForSale(conditionVOs);
      conditionVOs = (ConditionVO[])listRet.get(0);
      String strDataPowerSql = (String)listRet.get(1);

      int iLen = conditionVOs == null ? 0 : conditionVOs.length;
      for (int i = 0; i < iLen; i++) {
        String sName = conditionVOs[i].getFieldCode().trim();
        String sOpera = conditionVOs[i].getOperaCode().trim();
        String sValue = conditionVOs[i].getValue();
        String sSQL = conditionVOs[i].getSQLStr();
        String sReplace = null;
        if ((sName.equals("dinvoicedate")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "dinvoicedate " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        if ((sName.equals("ddate")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "dinvoicedate " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        if ((sName.equals("vinvoicecode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "vinvoicecode " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        if ((sName.equals("cbaseid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B where A.pk_invbasdoc = B.pk_invbasdoc and invcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        if ((sName.equals("cinvclassid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          try {
            EstimateDMO ddmo = new EstimateDMO();
            String[] sClassCode = ddmo.getSubInvClassCode(sValue);
            if ((sClassCode != null) && (sClassCode.length > 0)) {
              sValue = "(";
              for (int j = 0; j < sClassCode.length; j++) {
                if (j < sClassCode.length - 1)
                  sValue = sValue + "invclasscode " + sOpera + " '" + 
                    sClassCode[j] + "' or ";
                else
                  sValue = sValue + "invclasscode " + sOpera + " '" + 
                    sClassCode[j] + "')";
              }
              sReplace = "cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and " + 
                sValue + ")";
            } else {
              sReplace = "cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and invclasscode " + 
                sOpera + " '" + sValue + "')";
            }
            String s = getReplacedSQL(sSQL, sName, sReplace);
            sCondition = sCondition + s;
          }
          catch (Exception e) {
            SCMEnv.out(e);
            return null;
          }
        }
        if ((sName.equals("cvendorbaseid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cvendorbaseid in (select A.pk_cubasdoc from bd_cubasdoc A,bd_cumandoc B where A.pk_cubasdoc = B.pk_cubasdoc and custcode " + 
            sOpera + 
            " '" + 
            sValue + 
            "' and (custflag = '1' or custflag = '3'))";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        if ((sName.equals("cdeptid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cdeptid in (select pk_deptdoc from bd_deptdoc where deptcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        if ((sName.equals("cbiztype")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cbiztype in (select pk_busitype from bd_busitype where busicode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        if ((sName.equals("coperator")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "coperator in (select cuserid from sm_user where user_code " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        if ((sName.equals("vordercode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          if (sUnitCode != null)
            sReplace = "corderid in (select corderid from po_order where pk_corp = '" + 
              sUnitCode + 
              "' and vordercode " + 
              sOpera + 
              " '" + sValue + "')";
          else
            sReplace = "corderid in (select corderid from po_order where vordercode " + 
              sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else {
          if ((!sName.equals("cwarehouseid")) || (sValue == null) || 
            (sValue.length() <= 0)) continue;
          sReplace = "cwarehouseid in (select pk_stordoc from bd_stordoc where storcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
      }
      sCondition = PubDMO.processFirst(sCondition);

      sCondition = sCondition + " and abs(nmoney - naccumsettmny) >= " + 
        VMIDMO.getDigitRMB(sUnitCode);
      if (bIsVMI.booleanValue())
        sCondition = sCondition + " and E.verifyrule = 'V' ";
      else {
        sCondition = sCondition + " and E.verifyrule not in ('V','S') ";
      }

      if (strDataPowerSql != null) sCondition = sCondition + " and " + strDataPowerSql;

      vo = dmo.queryAllInvoice(sUnitCode, sCondition);
      if ((vo != null) && (vo.length > 0))
      {
        for (int i = 0; i < vo.length; i++) {
          UFDouble d1 = vo[i].getNinvoicenum();
          UFDouble d2 = vo[i].getNaccumsettlenum();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            vo[i].setNnosettlenum(new UFDouble(d));
          }
          d1 = vo[i].getNmoney();
          d2 = vo[i].getNaccumsettlemny();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            vo[i].setNnosettlemny(new UFDouble(d));
          }
          d1 = vo[i].getNinvoicenum();
          d2 = vo[i].getNmoney();
          if ((d1 != null) && (d2 != null) && (d1.doubleValue() != 0.0D)) {
            double d = d2.doubleValue() / d1.doubleValue();
            vo[i].setNprice(new UFDouble(d));
          }
        }
      }
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return vo;
  }

  public ArrayList queryAllInvoiceForFee(ArrayList listPara)
    throws BusinessException
  {
    if ((listPara == null) || (listPara.size() < 3)) {
      SCMEnv.out("程序BUG：查询费用发票、折扣发票、数量为0发票参数为NULL！");
      return null;
    }
    String sUnitCode = (String)listPara.get(0);
    ConditionVO[] conditionVOs = (ConditionVO[])listPara.get(1);
    UFBoolean bIsVMI = (UFBoolean)listPara.get(2);
    String strDataPower = (String)listPara.get(3);

    if ((sUnitCode == null) || (bIsVMI == null)) {
      SCMEnv.out("程序BUG：查询费用发票、折扣发票、数量为0发票参数存在NULL！");
      return null;
    }
    ArrayList list = new ArrayList();
    SettleDMO dmo = null;
    try {
      dmo = new SettleDMO();

      String sCondition = "";
      int iLen = conditionVOs == null ? 0 : conditionVOs.length;
      for (int i = 0; i < iLen; i++) {
        String sName = conditionVOs[i].getFieldCode().trim();
        String sOpera = conditionVOs[i].getOperaCode().trim();
        String sValue = conditionVOs[i].getValue();
        String sSQL = conditionVOs[i].getSQLStr();
        String sReplace = null;
        if ((sName.equals("dinvoicedate")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "dinvoicedate " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("ddate")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "dinvoicedate " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("vinvoicecode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "vinvoicecode " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cbaseid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "B.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B where A.pk_invbasdoc = B.pk_invbasdoc and invcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cinvclassid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          try {
            EstimateDMO ddmo = new EstimateDMO();
            String[] sClassCode = ddmo.getSubInvClassCode(sValue);
            if ((sClassCode != null) && (sClassCode.length > 0)) {
              sValue = "(";
              for (int j = 0; j < sClassCode.length; j++) {
                if (j < sClassCode.length - 1)
                  sValue = sValue + "invclasscode " + sOpera + " '" + 
                    sClassCode[j] + "' or ";
                else
                  sValue = sValue + "invclasscode " + sOpera + " '" + 
                    sClassCode[j] + "')";
              }
              sReplace = "B.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and " + 
                sValue + ")";
            } else {
              sReplace = "B.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and invclasscode " + 
                sOpera + " '" + sValue + "')";
            }
            String s = getReplacedSQL(sSQL, sName, sReplace);
            sCondition = sCondition + s;
          }
          catch (Exception e) {
            SCMEnv.out(e);
            return null;
          }
        } else if ((sName.equals("cvendorbaseid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cvendorbaseid in (select A.pk_cubasdoc from bd_cubasdoc A,bd_cumandoc B where A.pk_cubasdoc = B.pk_cubasdoc and custcode " + 
            sOpera + 
            " '" + 
            sValue + 
            "' and (custflag = '1' or custflag = '3'))";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cdeptid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cdeptid in (select pk_deptdoc from bd_deptdoc where deptcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cbiztype")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cbiztype in (select pk_busitype from bd_busitype where busicode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("coperator")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "coperator in (select cuserid from sm_user where user_code " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("vordercode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "corderid in (select corderid from po_order where vordercode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.indexOf("po_invoice_b.vdef") >= 0) && 
          (sValue != null) && (sValue.length() > 0)) {
          sReplace = "B." + sName.substring(sName.indexOf(".") + 1) + 
            " " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.indexOf("po_invoice.vdef") >= 0) && 
          (sValue != null) && (sValue.length() > 0)) {
          sReplace = "A." + sName.substring(sName.indexOf(".") + 1) + 
            " " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.indexOf("po_invoice_b.vmemo") >= 0) && 
          (sValue != null) && (sValue.length() > 0)) {
          sReplace = "B." + sName.substring(sName.indexOf(".") + 1) + 
            " " + sOpera;
          if ("like".equalsIgnoreCase(sOpera))
            sReplace = sReplace + " '%" + sValue + "%'";
          else {
            sReplace = sReplace + " '" + sValue + "'";
          }
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.equals("vcontractcode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "corderid in (select corderid from po_order_b A, ct_manage B where pk_ct_manage = ccontractid and ct_code " + 
            sOpera + 
            " '" + 
            sValue + 
            "' and A.dr = 0 and B.dr = 0)";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else {
          if ((!sName.equals("cwarehouseid")) || (sValue == null) || 
            (sValue.length() <= 0)) continue;
          sReplace = "cwarehouseid in (select pk_stordoc from bd_stordoc where storcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
      }
      sCondition = PubDMO.processFirst(sCondition);

      sCondition = sCondition + " and abs(nmoney - naccumsettmny) >= " + 
        VMIDMO.getDigitRMB(sUnitCode);
      if (bIsVMI.booleanValue())
        sCondition = sCondition + " and E.verifyrule = 'V' ";
      else {
        sCondition = sCondition + " and E.verifyrule not in ('V','S') ";
      }

      if (strDataPower != null) sCondition = sCondition + " and " + strDataPower;

      FeeinvoiceVO[] discountVOs = dmo.queryDiscount(sUnitCode, 
        sCondition);

      FeeinvoiceVO[] feeVOs = dmo.queryFee(sUnitCode, sCondition);

      FeeinvoiceVO[] specialVOs = dmo.queryInvoiceNoNum(sUnitCode, 
        sCondition);

      HashMap mapExistId = null;

      if ((discountVOs != null) && (discountVOs.length > 0)) {
        mapExistId = new HashMap();
        int mLen = discountVOs.length;
        for (int i = 0; i < mLen; i++) {
          mapExistId.put(discountVOs[i].getCinvoice_bid(), "");
          discountVOs[i].setVfactorname("折扣");
          UFDouble d1 = discountVOs[i].getNmny();
          UFDouble d2 = discountVOs[i].getNaccumsettlemny();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            discountVOs[i].setNnosettlemny(new UFDouble(d));
            discountVOs[i].setNsettlemny(new UFDouble(d));
          }
        }

      }

      if ((mapExistId != null) && (mapExistId.size() > 0) && 
        (feeVOs != null) && (feeVOs.length > 0)) {
        Vector vTmp = new Vector();
        int jLen = feeVOs.length;
        for (int i = 0; i < jLen; i++) {
          if (mapExistId.containsKey(feeVOs[i].getCinvoice_bid())) {
            continue;
          }
          vTmp.addElement(feeVOs[i]);
        }
        if (vTmp.size() > 0) {
          feeVOs = new FeeinvoiceVO[vTmp.size()];
          vTmp.copyInto(feeVOs);
        } else {
          feeVOs = (FeeinvoiceVO[])null;
        }

      }

      if ((feeVOs != null) && (feeVOs.length > 0)) {
        for (int i = 0; i < feeVOs.length; i++) {
          UFDouble d1 = feeVOs[i].getNmny();
          UFDouble d2 = feeVOs[i].getNaccumsettlemny();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            feeVOs[i].setNnosettlemny(new UFDouble(d));
            feeVOs[i].setNsettlemny(new UFDouble(d));
          }
        }
      }

      if ((specialVOs != null) && (specialVOs.length > 0)) {
        for (int i = 0; i < specialVOs.length; i++) {
          UFDouble d1 = specialVOs[i].getNmny();
          UFDouble d2 = specialVOs[i].getNaccumsettlemny();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            specialVOs[i].setNnosettlemny(new UFDouble(d));
            specialVOs[i].setNsettlemny(new UFDouble(d));
          }
        }
      }
      list.add(feeVOs);
      list.add(discountVOs);
      list.add(specialVOs);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return list;
  }

  public ArrayList queryAllInvoiceForManual(ArrayList listPara)
    throws BusinessException
  {
    if ((listPara == null) || (listPara.size() < 3)) {
      SCMEnv.out("程序BUG：查询发票（含费用发票和折扣发票）参数为NULL！");
      return null;
    }
    String sUnitCode = (String)listPara.get(0);
    ConditionVO[] conditionVOs = (ConditionVO[])listPara.get(1);
    UFBoolean bIsVMI = (UFBoolean)listPara.get(2);
    String strDataPower = (String)listPara.get(3);

    if ((sUnitCode == null) || (bIsVMI == null)) {
      SCMEnv.out("程序BUG：查询发票（含费用发票和折扣发票）参数存在NULL！");
      return null;
    }
    ArrayList list = new ArrayList();
    SettleDMO dmo = null;
    try {
      dmo = new SettleDMO();

      String sCondition = "";
      int iLen = conditionVOs == null ? 0 : conditionVOs.length;
      for (int i = 0; i < iLen; i++) {
        String sName = conditionVOs[i].getFieldCode().trim();
        String sOpera = conditionVOs[i].getOperaCode().trim();
        String sValue = conditionVOs[i].getValue();
        String sSQL = conditionVOs[i].getSQLStr();
        String sReplace = null;

        if ((sName.equals("dinvoicedate")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "dinvoicedate " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("ddate")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "dinvoicedate " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("vinvoicecode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "vinvoicecode " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cbaseid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "B.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B where A.pk_invbasdoc = B.pk_invbasdoc and invcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cinvclassid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          try {
            EstimateDMO ddmo = new EstimateDMO();
            String[] sClassCode = ddmo.getSubInvClassCode(sValue);

            if ((sClassCode != null) && (sClassCode.length > 0)) {
              sValue = "(";
              for (int j = 0; j < sClassCode.length; j++) {
                if (j < sClassCode.length - 1)
                  sValue = sValue + "invclasscode " + sOpera + " '" + 
                    sClassCode[j] + "' or ";
                else
                  sValue = sValue + "invclasscode " + sOpera + " '" + 
                    sClassCode[j] + "')";
              }
              sReplace = "B.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and " + 
                sValue + ")";
            } else {
              sReplace = "B.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and invclasscode " + 
                sOpera + " '" + sValue + "')";
            }
            String s = getReplacedSQL(sSQL, sName, sReplace);
            sCondition = sCondition + s;
          }
          catch (Exception e) {
            SCMEnv.out(e);
            return null;
          }
        } else if ((sName.equals("cvendorbaseid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cvendorbaseid in (select A.pk_cubasdoc from bd_cubasdoc A,bd_cumandoc B where A.pk_cubasdoc = B.pk_cubasdoc and custcode " + 
            sOpera + 
            " '" + 
            sValue + 
            "' and (custflag = '1' or custflag = '3'))";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cdeptid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cdeptid in (select pk_deptdoc from bd_deptdoc where deptcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cbiztype")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cbiztype in (select pk_busitype from bd_busitype where busicode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("coperator")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "coperator in (select cuserid from sm_user where user_code " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("vordercode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "corderid in (select corderid from po_order where vordercode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.indexOf("po_invoice_b.vdef") >= 0) && 
          (sValue != null) && (sValue.length() > 0)) {
          sReplace = "B." + sName.substring(sName.indexOf(".") + 1) + 
            " " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.indexOf("po_invoice.vdef") >= 0) && 
          (sValue != null) && (sValue.length() > 0)) {
          sReplace = "A." + sName.substring(sName.indexOf(".") + 1) + 
            " " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.equals("vcontractcode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "corderid in (select corderid from po_order_b A, ct_manage B where pk_ct_manage = ccontractid and ct_code " + 
            sOpera + 
            " '" + 
            sValue + 
            "' and A.dr = 0 and B.dr = 0)";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.indexOf("po_invoice_b.vmemo") >= 0) && 
          (sValue != null) && (sValue.length() > 0)) {
          sReplace = "B." + sName.substring(sName.indexOf(".") + 1) + 
            " " + sOpera;
          if ("like".equalsIgnoreCase(sOpera))
            sReplace = sReplace + " '%" + sValue + "%'";
          else {
            sReplace = sReplace + " '" + sValue + "'";
          }
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else {
          if ((!sName.equals("cwarehouseid")) || (sValue == null) || 
            (sValue.length() <= 0)) continue;
          sReplace = "cwarehouseid in (select pk_stordoc from bd_stordoc where storcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
      }

      sCondition = PubDMO.processFirst(sCondition);

      sCondition = sCondition + " and abs(nmoney - naccumsettmny) >= " + 
        VMIDMO.getDigitRMB(sUnitCode);

      if (bIsVMI.booleanValue())
        sCondition = sCondition + " and E.verifyrule = 'V' ";
      else {
        sCondition = sCondition + " and E.verifyrule not in ('V','S') ";
      }

      if (strDataPower != null) sCondition = sCondition + " and " + strDataPower;

      ArrayList listPara1 = new ArrayList();
      listPara1.add(sUnitCode);
      listPara1.add(sCondition + " and abs(isnull(ninvoicenum,0)) > 0 ");
      listPara1.add(new UFBoolean(false));
      listPara1.add(new UFBoolean(true));
      listPara1.add(new UFBoolean(true));
      IinvoiceVO[] VOs = dmo.queryInvoice(listPara1);
      FeeinvoiceVO[] discountVOs = dmo.queryDiscount(sUnitCode, 
        sCondition);
      FeeinvoiceVO[] feeVOs = dmo.queryFee(sUnitCode, sCondition);

      if ((VOs != null) && (VOs.length > 0))
      {
        for (int i = 0; i < VOs.length; i++) {
          UFDouble d1 = VOs[i].getNinvoicenum();
          UFDouble d2 = VOs[i].getNaccumsettlenum();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            VOs[i].setNnosettlenum(new UFDouble(d));
          }
          d1 = VOs[i].getNmoney();
          d2 = VOs[i].getNaccumsettlemny();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            VOs[i].setNnosettlemny(new UFDouble(d));
          }

          d1 = VOs[i].getNinvoicenum();
          d2 = VOs[i].getNmoney();
          if ((d1 != null) && (d2 != null) && (d1.doubleValue() != 0.0D)) {
            double d = d2.doubleValue() / d1.doubleValue();
            VOs[i].setNprice(new UFDouble(d));
          }
        }
      }

      HashMap mapExistId = null;
      if ((discountVOs != null) && (discountVOs.length > 0)) {
        mapExistId = new HashMap();
        int mLen = discountVOs.length;

        for (int i = 0; i < mLen; i++) {
          mapExistId.put(discountVOs[i].getCinvoice_bid(), "");
          discountVOs[i].setVfactorname("折扣");
          UFDouble d1 = discountVOs[i].getNmny();
          UFDouble d2 = discountVOs[i].getNaccumsettlemny();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            discountVOs[i].setNnosettlemny(new UFDouble(d));
            discountVOs[i].setNsettlemny(new UFDouble(d));
          }
        }

      }

      if ((mapExistId != null) && (mapExistId.size() > 0) && 
        (feeVOs != null) && (feeVOs.length > 0)) {
        Vector vTmp = new Vector();
        int jLen = feeVOs.length;
        for (int i = 0; i < jLen; i++) {
          if (mapExistId.containsKey(feeVOs[i].getCinvoice_bid())) {
            continue;
          }
          vTmp.addElement(feeVOs[i]);
        }
        if (vTmp.size() > 0) {
          feeVOs = new FeeinvoiceVO[vTmp.size()];
          vTmp.copyInto(feeVOs);
        } else {
          feeVOs = (FeeinvoiceVO[])null;
        }

      }

      if ((feeVOs != null) && (feeVOs.length > 0)) {
        for (int i = 0; i < feeVOs.length; i++) {
          UFDouble d1 = feeVOs[i].getNmny();
          UFDouble d2 = feeVOs[i].getNaccumsettlemny();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            feeVOs[i].setNnosettlemny(new UFDouble(d));
            feeVOs[i].setNsettlemny(new UFDouble(d));
          }
        }
      }

      list.add(VOs);
      list.add(feeVOs);
      list.add(discountVOs);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }

    return list;
  }

  public Vector queryCostfactorNO(String unitCode, String[] cId)
    throws BusinessException
  {
    Vector v = null;
    try {
      SettleDMO dmo = new SettleDMO();
      v = dmo.queryCostfactorNO(unitCode, cId);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return v;
  }

  public ArrayList queryDetailBatchForDispose(String sUnitCode, String[] stockBodyKey, String[] invoiceBodyKey, String[] feeBodyKey)
    throws BusinessException
  {
    ArrayList list = new ArrayList();
    Timer timer = new Timer();
    timer.start();
    try {
      StockVO[] stockVOs = queryStockDetailBatch(sUnitCode, stockBodyKey);
      timer.addExecutePhase("查询入库单");
      IinvoiceVO[] iinvoiceVOs = queryInvoiceDetailBatch(sUnitCode, 
        invoiceBodyKey);
      timer.addExecutePhase("查询发票（普通）");
      ArrayList list0 = queryFeeDetailBatch(sUnitCode, feeBodyKey);
      timer.addExecutePhase("查询发票（费用）");

      list.add(stockVOs);
      list.add(iinvoiceVOs);
      list.add(list0);

      timer.showAllExecutePhase("查询入库单、发票、费用发票(明细)时间分布");
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }

    return list;
  }

  public StockVO[] queryEndStock(String sUnitCode, ConditionVO[] conditionVO)
    throws BusinessException
  {
    StockVO[] vo = (StockVO[])null;
    int k = 0;
    try {
      SettleDMO dmo = new SettleDMO();

      String sCondition = "";

      ArrayList listRet = dealCondVosForPower(conditionVO);
      conditionVO = (ConditionVO[])listRet.get(0);
      String strDataPowerSql = (String)listRet.get(1);

      int iLen = conditionVO == null ? 0 : conditionVO.length;
      for (int i = 0; i < iLen; i++) {
        String sName = conditionVO[i].getFieldCode().trim();
        String sOpera = conditionVO[i].getOperaCode().trim();
        String sValue = conditionVO[i].getValue();
        String sSQL = conditionVO[i].getSQLStr();
        String sReplace = null;

        if ((sName.equals("dbilldate")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "dbilldate " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("vbillcode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "vbillcode " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cbaseid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "B.cinvbasid in (select pk_invbasdoc from bd_invbasdoc where invcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cinvclassid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          try {
            EstimateDMO ddmo = new EstimateDMO();
            String[] sClassCode = ddmo.getSubInvClassCode(sValue);

            if ((sClassCode != null) && (sClassCode.length > 0)) {
              sValue = "(";
              for (int j = 0; j < sClassCode.length; j++) {
                if (j < sClassCode.length - 1)
                  sValue = sValue + "invclasscode " + sOpera + " '" + 
                    sClassCode[j] + "' or ";
                else
                  sValue = sValue + "invclasscode " + sOpera + " '" + 
                    sClassCode[j] + "')";
              }
              sReplace = "B.cinvbasid in (select pk_invbasdoc from bd_invbasdoc A,bd_invcl C where A.pk_invcl = C.pk_invcl and " + 
                sValue + ")";
            } else {
              sReplace = "B.cinvbasid in (select pk_invbasdoc from bd_invbasdoc A,bd_invcl C where A.pk_invcl = C.pk_invcl and invclasscode " + 
                sOpera + " '" + sValue + "')";
            }
            String s = getReplacedSQL(sSQL, sName, sReplace);
            sCondition = sCondition + s;
          }
          catch (Exception e) {
            SCMEnv.out(e);
            return null;
          }
        } else if ((sName.equals("cvendorbaseid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cproviderid in (select pk_cumandoc from bd_cumandoc A, bd_cubasdoc B where custcode " + 
            sOpera + 
            " '" + 
            sValue + 
            "'and A.pk_cubasdoc = B.pk_cubasdoc and (custflag = '1' or custflag = '3'))";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.indexOf("ic_general_b.vuserdef") >= 0) && 
          (sValue != null) && (sValue.length() > 0)) {
          sReplace = "B." + sName.substring(sName.indexOf(".") + 1) + 
            " " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.indexOf("vuserdef") >= 0) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "A." + sName + " " + sOpera + " '" + sValue + 
            "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if (sName.equalsIgnoreCase("ic_general_b.vnotebody")) {
          sReplace = "B." + sName.substring(sName.indexOf(".") + 1) + 
            " " + sOpera;
          if ("like".equalsIgnoreCase(sOpera))
            sReplace = sReplace + " '%" + sValue + "%'";
          else {
            sReplace = sReplace + " '" + sValue + "'";
          }
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.equals("vcontractcode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cfirstbillhid in (select corderid from po_order_b A, ct_manage B where ccontractid = pk_ct_manage and ct_code " + 
            sOpera + 
            " '" + 
            sValue + 
            "' and A.dr = 0 and B.dr = 0)";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.indexOf("vinvoicecode") >= 0) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "B.cgeneralbid in (select cstockrow from po_settlebill_b where cinvoiceid in (select cinvoiceid from po_invoice where vinvoicecode " + 
            sOpera + " '" + sValue + "' and dr = 0))";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.equals("pk_stockcorp")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "A.pk_corp in (select pk_corp from bd_corp where unitcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.equals("cwarehouseid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cwarehouseid in (select pk_stordoc from bd_stordoc where storcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.equals("coperatorid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "A.coperatorid in (select cuserid from sm_user where user_code " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.equals("vordercode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cfirstbillhid in (select corderid from po_order where vordercode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.equals("bFeeSettled")) && (sValue != null) && (sValue.length() > 0)) {
          k = Integer.parseInt(sValue);
        }
      }

      sCondition = PubDMO.processFirst(sCondition);

      sCondition = sCondition + " and coalesce(B.flargess,'N') = 'N' ";

      sCondition = sCondition + " and coalesce(S.iscalculatedinvcost,'N') = 'Y' ";

      sCondition = sCondition + " and E.verifyrule not in ('V','S','Z','N') ";

      if (strDataPowerSql != null) sCondition = sCondition + " and " + strDataPowerSql;

      vo = dmo.queryEndStock(sUnitCode, sCondition);
      if ((vo != null) && (vo.length > 0))
      {
        vo = dmo.preDealStockVO(sUnitCode, vo);
        if (k == 0) return vo;

        Vector vID = null;
        if (k == 1)
        {
          vID = dmo.queryStockRowID(sUnitCode, true);
        } else if (k == 2)
        {
          vID = dmo.queryStockRowID(sUnitCode, false);
        }
        if ((vID != null) && (vID.size() > 0) && (vo != null) && (vo.length > 0)) {
          Vector v = new Vector();
          for (int i = 0; i < vo.length; i++) {
            if (!vID.contains(vo[i].getCgeneralbid())) continue; v.addElement(vo[i]);
          }
          vo = new StockVO[v.size()];
          v.copyInto(vo);
        } else {
          vo = (StockVO[])null;
        }
      }
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return vo;
  }

  public ArrayList queryFeeDetailBatch(String sUnitCode, String[] bodyKey)
    throws BusinessException
  {
    ArrayList list = new ArrayList();
    try {
      SettleDMO dmo = new SettleDMO();
      if ((bodyKey == null) || (bodyKey.length == 0)) {
        return list;
      }
      String sCondition = " and B.cinvoice_bid in ";

      String strSetId = null;
      TempTableDMO dmoTempTbl = new TempTableDMO();
      strSetId = dmoTempTbl.insertTempTable(bodyKey, 
        "t_pu_ps_41", 
        "pk_pu");
      if ((strSetId == null) || (strSetId.trim().equals("()"))) {
        strSetId = " ('ErrorPk') ";
      }
      sCondition = sCondition + strSetId;

      FeeinvoiceVO[] feeVOs = dmo.queryFee(sUnitCode, sCondition);
      FeeinvoiceVO[] discountVOs = dmo.queryDiscount(sUnitCode, 
        sCondition);
      FeeinvoiceVO[] specialVOs = dmo.queryInvoiceNoNum(sUnitCode, 
        sCondition);

      HashMap mapExistId = null;

      if ((discountVOs != null) && (discountVOs.length > 0)) {
        mapExistId = new HashMap();
        int mLen = discountVOs.length;
        for (int i = 0; i < mLen; i++) {
          mapExistId.put(discountVOs[i].getCinvoice_bid(), "");
          discountVOs[i].setVfactorname("折扣");
          UFDouble d1 = discountVOs[i].getNmny();
          UFDouble d2 = discountVOs[i].getNaccumsettlemny();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            discountVOs[i].setNnosettlemny(new UFDouble(d));
            discountVOs[i].setNsettlemny(new UFDouble(d));
          }
        }

      }

      if ((mapExistId != null) && (mapExistId.size() > 0) && 
        (feeVOs != null) && (feeVOs.length > 0)) {
        Vector vTmp = new Vector();
        int jLen = feeVOs.length;
        for (int i = 0; i < jLen; i++) {
          if (mapExistId.containsKey(feeVOs[i].getCinvoice_bid())) {
            continue;
          }
          vTmp.addElement(feeVOs[i]);
        }
        if (vTmp.size() > 0) {
          feeVOs = new FeeinvoiceVO[vTmp.size()];
          vTmp.copyInto(feeVOs);
        } else {
          feeVOs = (FeeinvoiceVO[])null;
        }

      }

      if ((feeVOs != null) && (feeVOs.length > 0)) {
        for (int i = 0; i < feeVOs.length; i++) {
          UFDouble d1 = feeVOs[i].getNmny();
          UFDouble d2 = feeVOs[i].getNaccumsettlemny();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            feeVOs[i].setNnosettlemny(new UFDouble(d));
            feeVOs[i].setNsettlemny(new UFDouble(d));
          }
        }
      }

      if ((specialVOs != null) && (specialVOs.length > 0)) {
        for (int i = 0; i < specialVOs.length; i++) {
          specialVOs[i].setVfactorname("一般发票");
          UFDouble d1 = specialVOs[i].getNmny();
          UFDouble d2 = specialVOs[i].getNaccumsettlemny();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            specialVOs[i].setNnosettlemny(new UFDouble(d));
            specialVOs[i].setNsettlemny(new UFDouble(d));
          }
        }
      }
      list.add(feeVOs);
      list.add(discountVOs);
      list.add(specialVOs);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return list;
  }

  public IinvoiceVO[] queryInvoiceDetailBatch(String sUnitCode, String[] bodyKey)
    throws BusinessException
  {
    IinvoiceVO[] vo = (IinvoiceVO[])null;
    try {
      SettleDMO dmo = new SettleDMO();
      if ((bodyKey == null) || (bodyKey.length == 0)) {
        return null;
      }
      String sCondition = " and B.cinvoice_bid in ";

      String strSetId = null;
      TempTableDMO dmoTempTbl = new TempTableDMO();
      strSetId = dmoTempTbl.insertTempTable(bodyKey, 
        "t_pu_ps_42", 
        "pk_pu");
      if ((strSetId == null) || (strSetId.trim().equals("()"))) {
        strSetId = " ('ErrorPk') ";
      }
      sCondition = sCondition + strSetId;

      vo = dmo.queryInvoiceDetail(sUnitCode, sCondition, 
        new UFBoolean(true));

      if ((vo != null) && (vo.length > 0)) {
        if (vo.length != bodyKey.length) {
          throw new BusinessException(
            NCLangResOnserver.getInstance().getStrByID("40040502", 
            "UPP40040502-000036"));
        }
        for (int i = 0; i < vo.length; i++) {
          UFDouble d1 = vo[i].getNinvoicenum();
          UFDouble d2 = vo[i].getNaccumsettlenum();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            vo[i].setNnosettlenum(new UFDouble(d));
          }
          d1 = vo[i].getNmoney();
          d2 = vo[i].getNaccumsettlemny();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            vo[i].setNnosettlemny(new UFDouble(d));
          }
        }
      }
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return vo;
  }

  private OorderVO[] queryOrderDetail(String sUnitCode, String headKey, String bodyKey)
    throws BusinessException
  {
    OorderVO[] vo = (OorderVO[])null;
    try
    {
      SettleDMO dmo = new SettleDMO();
      String sCondition = " and A.corderid = '" + headKey + 
        "' and B.corder_bid = '" + bodyKey + "'";
      vo = dmo.queryOrderDetail(sUnitCode, sCondition);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return vo;
  }

  public ArrayList queryOrderDetailBatch(String sUnitCode, StockVO[] stockVOs)
    throws BusinessException
  {
    ArrayList list = new ArrayList();

    if ((sUnitCode == null) || (sUnitCode.trim().length() == 0))
      return list;
    if ((stockVOs == null) || (stockVOs.length == 0)) {
      return list;
    }
    for (int i = 0; i < stockVOs.length; i++) {
      OorderVO[] orderVOs = (OorderVO[])null;
      String sType = stockVOs[i].getCsourcebilltype();
      if ((sType == null) || (sType.length() == 0)) {
        list.add(orderVOs);
      }
      else
      {
        orderVOs = queryOrderDetail(sUnitCode, stockVOs[i]
          .getCfirstbillhid(), stockVOs[i].getCfirstbillbid());

        list.add(orderVOs);
      }
    }
    return list;
  }

  public SaledataVO[] querySaleinvoice(String sUnitCode, ConditionVO[] conditionVO)
    throws BusinessException
  {
    SaledataVO[] rsltVOs = (SaledataVO[])null;
    try {
      SettleDMO dmo = new SettleDMO();

      String sCondition = "";

      ArrayList listRet = dealCondVosForPowerForSale(conditionVO);
      conditionVO = (ConditionVO[])listRet.get(0);
      String strDataPowerSql = (String)listRet.get(1);

      int iLen0 = conditionVO == null ? 0 : conditionVO.length;
      for (int i = 0; i < iLen0; i++) {
        String sName = conditionVO[i].getFieldCode().trim();
        String sOpera = conditionVO[i].getOperaCode().trim();
        String sValue = conditionVO[i].getValue();
        String sSQL = conditionVO[i].getSQLStr();
        String sReplace = null;
        if ((sName.equals("dsaledate")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "dsaledate " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cdeptid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cdeptid in (select pk_deptdoc from bd_deptdoc where deptcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cbaseid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cmangid in (select pk_invmandoc from bd_invbasdoc A,bd_invmandoc B where A.pk_invbasdoc = B.pk_invbasdoc and invcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cinvclassid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          try {
            EstimateDMO ddmo = new EstimateDMO();
            String[] sClassCode = ddmo.getSubInvClassCode(sValue);
            if ((sClassCode != null) && (sClassCode.length > 0)) {
              sValue = "(";
              for (int j = 0; j < sClassCode.length; j++) {
                if (j < sClassCode.length - 1)
                  sValue = sValue + "invclasscode " + sOpera + " '" + 
                    sClassCode[j] + "' or ";
                else
                  sValue = sValue + "invclasscode " + sOpera + " '" + 
                    sClassCode[j] + "')";
              }
              sReplace = "cmangid in (select pk_invmandoc from bd_invbasdoc A, bd_invmandoc B,bd_invcl C where A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and " + 
                sValue + ")";
            } else {
              sReplace = "cmangid in (select pk_invmandoc from bd_invbasdoc A, bd_invmandoc B,bd_invcl C where A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and invclasscode " + 
                sOpera + " '" + sValue + "')";
            }
            String s = getReplacedSQL(sSQL, sName, sReplace);
            sCondition = sCondition + s;
          }
          catch (Exception e) {
            SCMEnv.out(e);
            return null;
          }
        } else {
          if ((!sName.equals("cvendorbaseid")) || (sValue == null) || 
            (sValue.length() <= 0)) continue;
          sReplace = "cvendormangid in (select pk_cumandoc from bd_cumandoc A, bd_cubasdoc B where custcode " + 
            sOpera + " '" + sValue + 
            "'and A.pk_cubasdoc = B.pk_cubasdoc )";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
      }

      sCondition = PubDMO.processFirst(sCondition);
      sCondition = sCondition + " and po_saledata.pk_corp = '" + sUnitCode + "' ";

      if (strDataPowerSql != null) sCondition = sCondition + " and " + strDataPowerSql;

      rsltVOs = dmo.querySaleinvoice(sCondition);

      if ((rsltVOs == null) || (rsltVOs.length == 0)) {
        return null;
      }
      int iLen = rsltVOs.length;
      UFDouble ufdNum = null; UFDouble ufdMny = null; UFDouble ufdNumAcc = null;
      for (int i = 0; i < iLen; i++) {
        ufdNum = rsltVOs[i].getNsalenum();
        if (ufdNum == null)
          ufdNum = new UFDouble(0.0D);
        ufdMny = rsltVOs[i].getNsalemny();
        if (ufdMny == null)
          ufdMny = new UFDouble(0.0D);
        ufdNumAcc = rsltVOs[i].getNaccumnum();
        if (ufdNumAcc == null)
          ufdNumAcc = new UFDouble(0.0D);
        rsltVOs[i].setNsalenum(ufdNum.sub(ufdNumAcc));
        if (ufdNum.doubleValue() != 0.0D) {
          rsltVOs[i].setNsaleprice(ufdMny.div(ufdNum));
          rsltVOs[i].setNsalemny(ufdMny.div(ufdNum).multiply(
            rsltVOs[i].getNsalenum()));
        }
      }
      rsltVOs = getRefSaleInvoice(rsltVOs);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return rsltVOs;
  }

  public SettlebillVO[] querySettlebill(String unitCode, ConditionVO[] conditionVO)
    throws BusinessException
  {
    SettlebillVO[] settleVOs = (SettlebillVO[])null;
    try {
      ArrayList listReturn = new ArrayList();
      ArrayList listHid = new ArrayList();

      settleVOs = querySettlebill_Stock(unitCode, conditionVO);
      if ((settleVOs != null) && (settleVOs.length > 0)) {
        int iLen = settleVOs.length;
        for (int i = 0; i < iLen; i++) {
          listReturn.add(settleVOs[i]);
          listHid.add(settleVOs[i].getHeadVO().getPrimaryKey());
        }
      }

      settleVOs = querySettlebill_Vmi(unitCode, conditionVO, listHid);
      if ((settleVOs != null) && (settleVOs.length > 0)) {
        int iLen = settleVOs.length;
        for (int i = 0; i < iLen; i++)
        {
          listReturn.add(settleVOs[i]);
        }
      }
      if (listReturn.size() > 0) {
        settleVOs = new SettlebillVO[listReturn.size()];
        listReturn.toArray(settleVOs);
      } else {
        settleVOs = (SettlebillVO[])null;
      }
    } catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    return settleVOs;
  }

  private SettlebillVO[] querySettlebill_Stock(String unitCode, ConditionVO[] conditionVO)
    throws BusinessException
  {
    SettlebillVO[] returnVO = (SettlebillVO[])null;
    try {
      SettleDMO dmo = new SettleDMO();

      String sCondition = "";

      ArrayList listRet = dealCondVosForPowerForSettle(conditionVO);
      conditionVO = (ConditionVO[])listRet.get(0);
      String strDataPowerSql = (String)listRet.get(1);

      int iLen0 = conditionVO == null ? 0 : conditionVO.length;
      for (int i = 0; i < iLen0; i++) {
        String sName = conditionVO[i].getFieldCode().trim();
        String sOpera = conditionVO[i].getOperaCode().trim();
        String sValue = conditionVO[i].getValue();
        String sSQL = conditionVO[i].getSQLStr();
        String sReplace = null;

        if ((sName.equals("cbaseid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B where A.pk_invbasdoc = B.pk_invbasdoc and invcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cinvclassid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          try {
            EstimateDMO ddmo = new EstimateDMO();
            String[] sClassCode = ddmo.getSubInvClassCode(sValue);

            if ((sClassCode != null) && (sClassCode.length > 0)) {
              sValue = "(";
              for (int j = 0; j < sClassCode.length; j++) {
                if (j < sClassCode.length - 1)
                  sValue = sValue + "invclasscode " + sOpera + " '" + 
                    sClassCode[j] + "' or ";
                else
                  sValue = sValue + "invclasscode " + sOpera + " '" + 
                    sClassCode[j] + "')";
              }
              sReplace = "cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invcl C where A.pk_invcl = C.pk_invcl and " + 
                sValue + ")";
            } else {
              sReplace = "cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invcl C where A.pk_invcl = C.pk_invcl and invclasscode " + 
                sOpera + " '" + sValue + "')";
            }
            String s = getReplacedSQL(sSQL, sName, sReplace);
            sCondition = sCondition + s;
          }
          catch (Exception e) {
            SCMEnv.out(e);
            return null;
          }
        } else if ((sName.equals("vsettlebillcode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "vsettlebillcode " + sOpera + " '" + sValue + 
            "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("dsettledate")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "dsettledate " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cvendorbaseid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cproviderid in (select pk_cumandoc from bd_cumandoc A,bd_cubasdoc B where A.pk_cubasdoc = B.pk_cubasdoc and custcode " + 
            sOpera + 
            " '" + 
            sValue + 
            "' and (custflag = '1' or custflag = '3'))";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cdeptid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cdptid in (select pk_deptdoc from bd_deptdoc where deptcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cemployeeid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cbizid in (select pk_psndoc from bd_psndoc where psncode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("coperator")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "coperator in (select cuserid from sm_user where user_code " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.equals("vbillcode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cstockid in (select cgeneralhid from ic_general_h where dr = 0 and (fbillflag = 3 or fbillflag = 4)  and vbillcode " + 
            sOpera + 
            " '" + 
            sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.equals("vinvoicecode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cinvoiceid in (select cinvoiceid from po_invoice where dr = 0  and vinvoicecode " + 
            sOpera + 
            " '" + 
            sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.equals("iprintcount")) && (sValue != null) && 
          (sValue.length() > 0)) {
          String s = " and (A.iprintcount" + sOpera + sValue + ")";
          sCondition = sCondition + s;
        }
        else if ((sName.equals("pk_stockcorp")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "B.pk_arrvcorp in (select pk_corp from bd_corp where dr = 0 and unitcode " + 
            sOpera + 
            " '" + 
            sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else {
          if ((!sName.equals("csettlebillid")) || (sValue == null) || 
            (sValue.length() <= 0)) continue;
          String s = " and (A.csettlebillid " + sOpera + "'" + sValue + "')";
          sCondition = sCondition + s;
        }

      }

      sCondition = PubDMO.processFirst(sCondition);

      if (strDataPowerSql != null) sCondition = sCondition + " and " + strDataPowerSql;

      SettlebillHeaderVO[] hVO = (SettlebillHeaderVO[])null;
      if ((sCondition != null) && (sCondition.trim().length() > 0)) hVO = dmo.querySettlebillHead_Stock(unitCode, 
          sCondition);
      if ((hVO == null) || (hVO.length == 0)) {
        return null;
      }

      Vector v = new Vector();
      int iLen = hVO.length;
      String strSqlHid = " and A.csettlebillid= '" + 
        hVO[0].getCsettlebillid() + "' ";
      SettlebillItemVO[] bVO = dmo.querySettleBody(unitCode, strSqlHid);
      if ((bVO != null) && (bVO.length > 0))
      {
        String[] temp1 = dmo.queryStockCode(bVO);
        String[] temp2 = dmo.queryInvoiceCode(bVO);

        String[] cgeneralhid = new String[bVO.length];
        for (int j = 0; j < bVO.length; j++)
          cgeneralhid[j] = bVO[j].getCstockid();
        ArrayList list2 = dmo.queryVendorName(cgeneralhid);
        String[] cVendorName = (String[])list2.get(0);
        String[] cDeptName = (String[])list2.get(1);
        String[] cEmployeeName = (String[])list2.get(2);
        for (int j = 0; j < bVO.length; j++) {
          bVO[j].setVbillcode(temp1[j]);
          bVO[j].setVinvoicecode(temp2[j]);
          bVO[j].setCvendorname(cVendorName[j]);
          bVO[j].setCdeptname(cDeptName[j]);
          bVO[j].setCemployeename(cEmployeeName[j]);
        }
      }
      SettlebillVO vo = new SettlebillVO(bVO.length);
      vo.setParentVO(hVO[0]);
      vo.setChildrenVO(bVO);
      v.addElement(vo);
      for (int i = 1; i < iLen; i++) {
        vo = new SettlebillVO(0);
        vo.setParentVO(hVO[i]);
        v.addElement(vo);
      }
      if (v.size() > 0) {
        returnVO = new SettlebillVO[v.size()];
        v.copyInto(returnVO);
      }
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return returnVO;
  }

  private ArrayList dealCondVosForPowerForVMI(ConditionVO[] voaCond)
  {
    ArrayList listUserInputVos = new ArrayList();
    ArrayList listPowerVos = new ArrayList();

    int iLen = voaCond.length;

    for (int i = 0; i < iLen; i++) {
      if ((voaCond[i].getOperaCode().trim().equalsIgnoreCase("IS")) && (voaCond[i].getValue().trim().equalsIgnoreCase("NULL"))) {
        listPowerVos.add(voaCond[i]);
        i++;
        listPowerVos.add(voaCond[i]);
      } else {
        listUserInputVos.add(voaCond[i]);
      }

    }

    ArrayList listRet = new ArrayList();

    ConditionVO[] voaCondUserInput = (ConditionVO[])null;
    if (listUserInputVos.size() > 0) {
      voaCondUserInput = new ConditionVO[listUserInputVos.size()];
      listUserInputVos.toArray(voaCondUserInput);
    }
    listRet.add(voaCondUserInput);

    ConditionVO[] voaCondPower = (ConditionVO[])null;
    if (listPowerVos.size() > 0) {
      voaCondPower = new ConditionVO[listPowerVos.size()];
      listPowerVos.toArray(voaCondPower);
      String strPowerWherePart = voaCondPower[0].getWhereSQL(voaCondPower);

      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "custcode", "pk_cumandoc");
      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "cvendorbaseid", "cvendorid");

      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "storcode", "pk_stordoc");

      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "invcode", "pk_invmandoc");
      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "cbaseid", "cinventoryid");

      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "cinvclassid", "cinventoryid");
      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "select invclasscode from bd_invcl where 0=0  and pk_invcl", "select pk_invmandoc from bd_invbasdoc A, bd_invmandoc B, bd_invcl C where A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and C.pk_invcl");

      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "bodycode", "pk_calbody");
      listRet.add(strPowerWherePart);
    } else {
      listRet.add(null);
    }

    return listRet;
  }

  private SettlebillVO[] querySettlebill_Vmi(String unitCode, ConditionVO[] conditionVO, ArrayList listHid)
    throws BusinessException
  {
    SettlebillVO[] returnVO = (SettlebillVO[])null;
    try {
      SettleDMO dmo = new SettleDMO();

      String sCondition = "";

      ArrayList listRet = dealCondVosForPowerForVMI(conditionVO);
      conditionVO = (ConditionVO[])listRet.get(0);
      String strDataPowerSql = (String)listRet.get(1);

      int iLen0 = conditionVO == null ? 0 : conditionVO.length;
      for (int i = 0; i < iLen0; i++) {
        String sName = conditionVO[i].getFieldCode().trim();
        String sOpera = conditionVO[i].getOperaCode().trim();
        String sValue = conditionVO[i].getValue();
        String sSQL = conditionVO[i].getSQLStr();
        String sReplace = null;

        if ((sName.equals("cbaseid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "B.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B where A.pk_invbasdoc = B.pk_invbasdoc and invcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cinvclassid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          try {
            EstimateDMO ddmo = new EstimateDMO();
            String[] sClassCode = ddmo.getSubInvClassCode(sValue);

            if ((sClassCode != null) && (sClassCode.length > 0)) {
              sValue = "(";
              for (int j = 0; j < sClassCode.length; j++) {
                if (j < sClassCode.length - 1)
                  sValue = sValue + "invclasscode " + sOpera + " '" + 
                    sClassCode[j] + "' or ";
                else
                  sValue = sValue + "invclasscode " + sOpera + " '" + 
                    sClassCode[j] + "')";
              }
              sReplace = "B.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and " + 
                sValue + ")";
            } else {
              sReplace = "B.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and invclasscode " + 
                sOpera + " '" + sValue + "')";
            }
            String s = getReplacedSQL(sSQL, sName, sReplace);
            sCondition = sCondition + s;
          }
          catch (Exception e) {
            SCMEnv.out(e);
            return null;
          }
        } else if ((sName.equals("vsettlebillcode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "vsettlebillcode " + sOpera + " '" + sValue + 
            "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("dsettledate")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "dsettledate " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cvendorbaseid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "C.cvendorid in (select pk_cumandoc from bd_cumandoc A,bd_cubasdoc B where A.pk_cubasdoc = B.pk_cubasdoc and custcode " + 
            sOpera + 
            " '" + 
            sValue + 
            "' and (custflag = '1' or custflag = '3'))";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cdeptid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "A.cdeptid in (select pk_deptdoc from bd_deptdoc where deptcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cemployeeid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "A.cemployeeid in (select pk_psndoc from bd_psndoc where psncode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("coperator")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "A.coperator in (select cuserid from sm_user where user_code " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.equals("vbillcode")) && (sValue != null) && 
          (sValue.length() > 0))
        {
          sReplace = "B.cstockid in (select cgeneralhid from ic_general_h where dr = 0 and (fbillflag = 3 or fbillflag = 4)  and vbillcode " + 
            sOpera + 
            " '" + 
            sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.equals("vinvoicecode")) && (sValue != null) && 
          (sValue.length() > 0))
        {
          sReplace = "B.cinvoiceid in (select cinvoiceid from po_invoice where dr = 0  and vinvoicecode " + 
            sOpera + 
            " '" + 
            sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.equals("vbillcode1")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "C.vbillcode " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.equals("iprintcount")) && (sValue != null) && 
          (sValue.length() > 0)) {
          String s = " and (A.iprintcount" + sOpera + sValue + ")";
          sCondition = sCondition + s;
        } else {
          if ((!sName.equals("csettlebillid")) || (sValue == null) || 
            (sValue.length() <= 0)) continue;
          String s = " and (A.csettlebillid " + sOpera + "'" + sValue + "')";
          sCondition = sCondition + s;
        }

      }

      sCondition = PubDMO.processFirst(sCondition);

      if (strDataPowerSql != null) sCondition = sCondition + " and " + strDataPowerSql;

      if ((listHid != null) && (listHid.size() > 0)) {
        String strSetId = null;
        try {
          TempTableDMO dmoTmpTbl = new TempTableDMO();
          strSetId = dmoTmpTbl.insertTempTable(listHid, 
            "t_pu_id_in_19", 
            "pk_pu");
          if ((strSetId == null) || (strSetId.trim().equals("()")))
            strSetId = " ('ErrorPk') ";
        }
        catch (Exception e) {
          throw new SQLException(
            NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", 
            "UPPSCMCommon-000413") + 
            e.getMessage());
        }
        sCondition = sCondition + " and A.csettlebillid not in ";
        sCondition = sCondition + strSetId;
      }

      SettlebillHeaderVO[] hVO = dmo.querySettlebillHead_Vmi(unitCode, 
        sCondition);
      if ((hVO == null) || (hVO.length == 0)) {
        return null;
      }

      Vector v = new Vector();
      int iLen = hVO.length;
      String strSqlHid = " and A.csettlebillid= '" + 
        hVO[0].getCsettlebillid() + "' ";
      SettlebillVO vo = null;

      SettlebillItemVO[] bVO = dmo.querySettleBody(unitCode, strSqlHid);
      if ((bVO != null) && (bVO.length > 0))
      {
        String[] temp1 = dmo.queryVMIBillCode(bVO);
        String[] temp2 = dmo.queryInvoiceCode(bVO);

        String[] saVMIHid = new String[bVO.length];
        for (int j = 0; j < bVO.length; j++) {
          saVMIHid[j] = bVO[j].getCvmiid();
        }
        String[] cVendorName = dmo.queryVendorName_Vmi(saVMIHid);
        for (int j = 0; j < bVO.length; j++) {
          bVO[j].setVbillcode(temp1[j]);
          bVO[j].setVinvoicecode(temp2[j]);
          bVO[j].setCvendorname(cVendorName[j]);
        }
        vo = new SettlebillVO(bVO.length);
        vo.setParentVO(hVO[0]);
        vo.setChildrenVO(bVO);
        v.addElement(vo);
      }

      for (int i = 1; i < iLen; i++) {
        vo = new SettlebillVO(0);
        vo.setParentVO(hVO[i]);
        v.addElement(vo);
      }
      if (v.size() > 0) {
        returnVO = new SettlebillVO[v.size()];
        v.copyInto(returnVO);
      }
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return returnVO;
  }

  public SettlebillVO querySettlebillByHeadKey(String unitCode, String headKey)
    throws BusinessException
  {
    SettlebillVO[] returnVO = (SettlebillVO[])null;
    if ((headKey == null) || (headKey.trim().length() == 0))
      return null;
    try {
      SettleDMO dmo = new SettleDMO();
      boolean bVMI = false;

      String sCondition = " and A.csettlebillid = '" + headKey + "'";
      SettlebillHeaderVO[] hVO = dmo.querySettlebillHead_Stock(unitCode, 
        sCondition);
      if ((hVO == null) || (hVO.length == 0)) {
        hVO = dmo.querySettlebillHead_Vmi(unitCode, sCondition);
        bVMI = true;
      }
      if ((hVO == null) || (hVO.length == 0)) {
        return null;
      }
      Vector v = new Vector();
      String temp = " and A.csettlebillid= '" + hVO[0].getCsettlebillid() + 
        "'";
      SettlebillItemVO[] bVO = dmo.querySettleBody(unitCode, temp);
      if ((bVO != null) && (bVO.length > 0))
      {
        String[] temp1 = (String[])null;
        if (!bVMI) {
          temp1 = dmo.queryStockCode(bVO);
        }
        else {
          temp1 = dmo.queryVMIBillCode(bVO);
        }

        String[] temp2 = dmo.queryInvoiceCode(bVO);

        String[] cgeneralhid = new String[bVO.length];
        for (int j = 0; j < bVO.length; j++) {
          if (!bVMI)
            cgeneralhid[j] = bVO[j].getCstockid();
          else
            cgeneralhid[j] = bVO[j].getCvmiid();
        }
        if (!bVMI) {
          ArrayList list2 = dmo.queryVendorName(cgeneralhid);
          String[] cVendorName = (String[])list2.get(0);
          String[] cDeptName = (String[])list2.get(1);
          String[] cEmployeeName = (String[])list2.get(2);
          for (int j = 0; j < bVO.length; j++) {
            bVO[j].setVbillcode(temp1[j]);
            bVO[j].setVinvoicecode(temp2[j]);
            bVO[j].setCvendorname(cVendorName[j]);
            bVO[j].setCdeptname(cDeptName[j]);
            bVO[j].setCemployeename(cEmployeeName[j]);
          }
        } else {
          String[] cVendorName = dmo.queryVendorName_Vmi(cgeneralhid);
          for (int j = 0; j < bVO.length; j++) {
            bVO[j].setVbillcode(temp1[j]);
            bVO[j].setVinvoicecode(temp2[j]);
            bVO[j].setCvendorname(cVendorName[j]);
          }
        }
      }
      SettlebillVO vo = new SettlebillVO(bVO.length);
      vo.setParentVO(hVO[0]);
      vo.setChildrenVO(bVO);
      v.addElement(vo);

      for (int i = 1; i < hVO.length; i++) {
        vo = new SettlebillVO(1);
        vo.setParentVO(hVO[i]);
        v.addElement(vo);
      }
      if (v.size() > 0) {
        returnVO = new SettlebillVO[v.size()];
        v.copyInto(returnVO);
      }
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return returnVO[0];
  }

  public boolean querySettlebillStatusSpecially(InvoiceVO invoiceVO)
    throws BusinessException
  {
    try
    {
      SettleDMO dmo = new SettleDMO();

      InvoiceHeaderVO invoiceHeadVO = invoiceVO.getHeadVO();
      String invoiceKey = invoiceHeadVO.getCinvoiceid().trim();
      String sCondition = " and cinvoiceid = '" + invoiceKey + "'";
      SettlebillItemVO[] settlebillItemVOs = dmo.querySettleBody(
        invoiceHeadVO.getPk_corp(), sCondition);
      if ((settlebillItemVOs == null) || (settlebillItemVOs.length == 0)) {
        throw new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("40040502", 
          "UPP40040502-000003"));
      }
      String settlebillKey = settlebillItemVOs[0].getCsettlebillid()
        .trim();
      sCondition = " and A.csettlebillid = '" + settlebillKey + "'";
      SettlebillHeaderVO[] settlebillHeadVOs = dmo
        .querySettlebillHead_Stock(invoiceHeadVO.getPk_corp(), 
        sCondition);
      if ((settlebillHeadVOs == null) || (settlebillHeadVOs.length == 0)) {
        throw new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("40040502", 
          "UPP40040502-000003"));
      }
      if (settlebillHeadVOs[0].getIbillstatus().intValue() == 1)
        return true;
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return false;
  }

  public SettlebillItemVO[] querySettleBody(String unitCode, String key, String ts)
    throws BusinessException
  {
    SettlebillItemVO[] bodyVO = (SettlebillItemVO[])null;
    try
    {
      SettleDMO dmo = new SettleDMO();
      String s = " and B.csettlebillid= '" + key + "' and B.ts = '" + ts + 
        "' ";
      bodyVO = dmo.querySettleBody(unitCode, s);
      if ((bodyVO != null) && (bodyVO.length > 0)) {
        boolean bIsFromVMI = (bodyVO[0].getCvmiid() != null) && 
          (bodyVO[0]
          .getCvmiid().trim().length() > 0);
        String[] temp1 = (String[])null; String[] temp2 = (String[])null; String[] cVendorName = (String[])null;

        if (bIsFromVMI)
        {
          temp1 = dmo.queryVMIBillCode(bodyVO);

          String[] saVMIHid = new String[bodyVO.length];
          for (int j = 0; j < bodyVO.length; j++)
            saVMIHid[j] = bodyVO[j].getCvmiid();
          cVendorName = dmo.queryVendorName_Vmi(saVMIHid);
        }
        else {
          temp1 = dmo.queryStockCode(bodyVO);

          String[] cgeneralhid = new String[bodyVO.length];
          for (int j = 0; j < bodyVO.length; j++)
            cgeneralhid[j] = bodyVO[j].getCstockid();
          ArrayList list2 = dmo.queryVendorName(cgeneralhid);
          cVendorName = (String[])list2.get(0);

          String[] cDeptName = (String[])list2.get(1);
          String[] cEmployeeName = (String[])list2.get(2);
          for (int j = 0; j < bodyVO.length; j++) {
            bodyVO[j].setCdeptname(cDeptName[j]);
            bodyVO[j].setCemployeename(cEmployeeName[j]);
          }
        }

        temp2 = dmo.queryInvoiceCode(bodyVO);

        for (int j = 0; j < bodyVO.length; j++) {
          bodyVO[j].setVbillcode(temp1[j]);
          bodyVO[j].setVinvoicecode(temp2[j]);
          bodyVO[j].setCvendorname(cVendorName[j]);
        }
      } else {
        return null;
      }
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return bodyVO;
  }

  public FeeinvoiceVO[] querySpecialDetail(String sUnitCode, String bodyKey)
    throws BusinessException
  {
    FeeinvoiceVO[] vo = (FeeinvoiceVO[])null;
    try {
      SettleDMO dmo = new SettleDMO();
      String sCondition = " and B.cinvoice_bid = '" + bodyKey + "'";

      vo = dmo.queryInvoiceNoNum(sUnitCode, sCondition);

      if ((vo != null) && (vo.length > 0)) {
        for (int i = 0; i < vo.length; i++) {
          vo[i].setVfactorname("一般发票");
          UFDouble d1 = vo[i].getNmny();
          UFDouble d2 = vo[i].getNaccumsettlemny();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            vo[i].setNnosettlemny(new UFDouble(d));
            vo[i].setNsettlemny(new UFDouble(d));
          }
        }
      }
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return vo;
  }

  private ArrayList dealCondVosForPowerForSettle(ConditionVO[] voaCond)
  {
    ArrayList listUserInputVos = new ArrayList();
    ArrayList listPowerVos = new ArrayList();

    int iLen = voaCond.length;

    for (int i = 0; i < iLen; i++) {
      if ((voaCond[i].getOperaCode().trim().equalsIgnoreCase("IS")) && (voaCond[i].getValue().trim().equalsIgnoreCase("NULL"))) {
        listPowerVos.add(voaCond[i]);
        i++;
        listPowerVos.add(voaCond[i]);
      } else {
        listUserInputVos.add(voaCond[i]);
      }

    }

    ArrayList listRet = new ArrayList();

    ConditionVO[] voaCondUserInput = (ConditionVO[])null;
    if (listUserInputVos.size() > 0) {
      voaCondUserInput = new ConditionVO[listUserInputVos.size()];
      listUserInputVos.toArray(voaCondUserInput);
    }
    listRet.add(voaCondUserInput);

    ConditionVO[] voaCondPower = (ConditionVO[])null;
    if (listPowerVos.size() > 0) {
      voaCondPower = new ConditionVO[listPowerVos.size()];
      listPowerVos.toArray(voaCondPower);
      String strPowerWherePart = voaCondPower[0].getWhereSQL(voaCondPower);

      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "custcode", "pk_cumandoc");
      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "cvendorbaseid", "cproviderid");

      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "invcode", "b.pk_invbasdoc");

      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "cinvclassid", "cbaseid");
      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "invclasscode", "pk_invbasdoc");
      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "bd_invcl where 0=0  and pk_invcl", "bd_invcl, bd_invbasdoc where bd_invcl.pk_invcl = bd_invbasdoc.pk_invcl and bd_invcl.pk_invcl");

      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "deptcode", "pk_deptdoc");
      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "cdeptid", "cdptid");

      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "psncode", "pk_psndoc");
      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "cemployeeid", "cbizid");

      listRet.add(strPowerWherePart);
    } else {
      listRet.add(null);
    }

    return listRet;
  }

  private ArrayList dealCondVosForPower(ConditionVO[] voaCond)
  {
    ArrayList listUserInputVos = new ArrayList();
    ArrayList listPowerVos = new ArrayList();

    int iLen = voaCond.length;

    for (int i = 0; i < iLen; i++) {
      if ((voaCond[i].getOperaCode().trim().equalsIgnoreCase("IS")) && (voaCond[i].getValue().trim().equalsIgnoreCase("NULL"))) {
        listPowerVos.add(voaCond[i]);
        i++;
        listPowerVos.add(voaCond[i]);
      } else {
        listUserInputVos.add(voaCond[i]);
      }

    }

    ArrayList listRet = new ArrayList();

    ConditionVO[] voaCondUserInput = (ConditionVO[])null;
    if (listUserInputVos.size() > 0) {
      voaCondUserInput = new ConditionVO[listUserInputVos.size()];
      listUserInputVos.toArray(voaCondUserInput);
    }
    listRet.add(voaCondUserInput);

    ConditionVO[] voaCondPower = (ConditionVO[])null;
    if (listPowerVos.size() > 0) {
      voaCondPower = new ConditionVO[listPowerVos.size()];
      listPowerVos.toArray(voaCondPower);
      String strPowerWherePart = voaCondPower[0].getWhereSQL(voaCondPower);

      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "custcode", "pk_cumandoc");
      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "cvendorbaseid", "cproviderid");

      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "invcode", "b.pk_invbasdoc");
      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "cbaseid", "cinvbasid");

      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "cinvclassid", "cinvbasid");
      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "invclasscode", "pk_invbasdoc");
      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "bd_invcl where 0=0  and pk_invcl", "bd_invcl, bd_invbasdoc where bd_invcl.pk_invcl = bd_invbasdoc.pk_invcl and bd_invcl.pk_invcl");

      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "deptcode", "pk_deptdoc");
      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "cdeptid", "cdptid");

      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "psncode", "pk_psndoc");
      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "cemployeeid", "cbizid");

      listRet.add(strPowerWherePart);
    } else {
      listRet.add(null);
    }

    return listRet;
  }

  private ArrayList dealCondVosForPowerForSale(ConditionVO[] voaCond)
  {
    ArrayList listUserInputVos = new ArrayList();
    ArrayList listPowerVos = new ArrayList();

    int iLen = voaCond.length;

    for (int i = 0; i < iLen; i++) {
      if ((voaCond[i].getOperaCode().trim().equalsIgnoreCase("IS")) && (voaCond[i].getValue().trim().equalsIgnoreCase("NULL"))) {
        listPowerVos.add(voaCond[i]);
        i++;
        listPowerVos.add(voaCond[i]);
      } else {
        listUserInputVos.add(voaCond[i]);
      }

    }

    ArrayList listRet = new ArrayList();

    ConditionVO[] voaCondUserInput = (ConditionVO[])null;
    if (listUserInputVos.size() > 0) {
      voaCondUserInput = new ConditionVO[listUserInputVos.size()];
      listUserInputVos.toArray(voaCondUserInput);
    }
    listRet.add(voaCondUserInput);

    ConditionVO[] voaCondPower = (ConditionVO[])null;
    if (listPowerVos.size() > 0) {
      voaCondPower = new ConditionVO[listPowerVos.size()];
      listPowerVos.toArray(voaCondPower);
      String strPowerWherePart = voaCondPower[0].getWhereSQL(voaCondPower);

      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "custcode", "b.pk_cubasdoc");

      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "invcode", "b.pk_invbasdoc");

      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "cinvclassid", "cbaseid");
      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "invclasscode", "pk_invbasdoc");
      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "bd_invcl where 0=0  and pk_invcl", "bd_invcl, bd_invbasdoc where bd_invcl.pk_invcl = bd_invbasdoc.pk_invcl and bd_invcl.pk_invcl");

      strPowerWherePart = nc.vo.jcom.lang.StringUtil.replaceAllString(strPowerWherePart, "deptcode", "pk_deptdoc");

      listRet.add(strPowerWherePart);
    } else {
      listRet.add(null);
    }

    return listRet;
  }

  public StockVO[] queryStock(String sUnitCode, ConditionVO[] conditionVOs)
    throws BusinessException
  {
    StockVO[] vo = (StockVO[])null;
    try {
      SettleDMO dmo = new SettleDMO();

      String sCondition = "";

      ArrayList listRet = dealCondVosForPower(conditionVOs);
      conditionVOs = (ConditionVO[])listRet.get(0);
      String strDataPowerSql = (String)listRet.get(1);

      int iLen = conditionVOs == null ? 0 : conditionVOs.length;
      for (int i = 0; i < iLen; i++) {
        String sName = conditionVOs[i].getFieldCode().trim();
        String sOpera = conditionVOs[i].getOperaCode().trim();
        String sValue = conditionVOs[i].getValue();
        String sSQL = conditionVOs[i].getSQLStr();
        String sReplace = null;

        if ((sName.equals("dbilldate")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "dbilldate " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("ddate")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "dbilldate " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("vbillcode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "vbillcode " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cbaseid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "B.cinvbasid in (select pk_invbasdoc from bd_invbasdoc where invcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cinvclassid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          try {
            EstimateDMO ddmo = new EstimateDMO();
            String[] sClassCode = ddmo.getSubInvClassCode(sValue);

            if ((sClassCode != null) && (sClassCode.length > 0)) {
              sValue = "(";
              for (int j = 0; j < sClassCode.length; j++) {
                if (j < sClassCode.length - 1)
                  sValue = sValue + "invclasscode " + sOpera + " '" + 
                    sClassCode[j] + "' or ";
                else
                  sValue = sValue + "invclasscode " + sOpera + " '" + 
                    sClassCode[j] + "')";
              }
              sReplace = "B.cinvbasid in (select pk_invbasdoc from bd_invbasdoc A,bd_invcl C where A.pk_invcl = C.pk_invcl and " + 
                sValue + ")";
            } else {
              sReplace = "B.cinvbasid in (select pk_invbasdoc from bd_invbasdoc A,bd_invcl C where A.pk_invcl = C.pk_invcl and invclasscode " + 
                sOpera + " '" + sValue + "')";
            }
            String s = getReplacedSQL(sSQL, sName, sReplace);
            sCondition = sCondition + s;
          }
          catch (Exception e) {
            SCMEnv.out(e);
            return null;
          }
        } else if ((sName.equals("cvendorbaseid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cproviderid in (select pk_cumandoc from bd_cumandoc A, bd_cubasdoc B where custcode " + 
            sOpera + 
            " '" + 
            sValue + 
            "'and A.pk_cubasdoc = B.pk_cubasdoc and (custflag = '1' or custflag = '3'))";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cdeptid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cdptid in (select pk_deptdoc from bd_deptdoc where deptcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cbiztype")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cbiztype in (select pk_busitype from bd_busitype where busicode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("vordercode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cfirstbillhid in (select corderid from po_order where vordercode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.indexOf("ic_general_b.vuserdef") >= 0) && 
          (sValue != null) && (sValue.length() > 0)) {
          sReplace = "B." + sName.substring(sName.indexOf(".") + 1) + 
            " " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.indexOf("vuserdef") >= 0) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "A." + sName + " " + sOpera + " '" + sValue + 
            "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if (sName.equalsIgnoreCase("ic_general_b.vnotebody")) {
          sReplace = "B." + sName.substring(sName.indexOf(".") + 1) + 
            " " + sOpera;
          if ("like".equalsIgnoreCase(sOpera))
            sReplace = sReplace + " '%" + sValue + "%'";
          else {
            sReplace = sReplace + " '" + sValue + "'";
          }
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.equals("vcontractcode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cfirstbillhid in (select corderid from po_order_b A, ct_manage B where ccontractid = pk_ct_manage and ct_code " + 
            sOpera + 
            " '" + 
            sValue + 
            "' and A.dr = 0 and B.dr = 0)";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.indexOf("vinvoicecode") >= 0) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "B.cgeneralbid in (select B.cupsourcebillrowid from po_invoice A, po_invoice_b B where A.cinvoiceid = B.cinvoiceid and vinvoicecode " + 
            sOpera + 
            " '" + 
            sValue + 
            "' and A.dr = 0 and B.dr = 0)";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.equals("cwarehouseid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cwarehouseid in (select pk_stordoc from bd_stordoc where storcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.equals("coperatorid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "A.coperatorid in (select cuserid from sm_user where user_code " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else {
          if ((!sName.equals("pk_stockcorp")) || (sValue == null) || 
            (sValue.length() <= 0)) continue;
          sReplace = "A.pk_corp in (select pk_corp from bd_corp where unitcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
      }

      sCondition = PubDMO.processFirst(sCondition);

      sCondition = sCondition + " and upper(isok) = 'N'";

      sCondition = sCondition + " and coalesce(flargess,'N') = 'N' ";

      sCondition = sCondition + " and (coalesce(S.iscalculatedinvcost,'N') = 'Y' or A.cwarehouseid is null) ";

      sCondition = sCondition + " and BT.verifyrule not in ('V','S','Z','N') ";

      if (strDataPowerSql != null) sCondition = sCondition + " and " + strDataPowerSql;

      vo = dmo.queryStock(sUnitCode, sCondition);
      if ((vo != null) && (vo.length > 0))
      {
        vo = dmo.preDealStockVO(sUnitCode, vo);
        if ((vo == null) || (vo.length == 0)) return null;

        Vector vTemp = new Vector();
        for (int i = 0; i < vo.length; i++)
          vTemp.addElement(vo[i].getCprovidermangid());
        String[] sVendorMangID = new String[vTemp.size()];
        vTemp.copyInto(sVendorMangID);
        String[] sVendorBaseID = getVendorBaseKey(sVendorMangID);

        for (int i = 0; i < vo.length; i++) {
          vo[i].setCproviderbaseid(sVendorBaseID[i]);
          UFDouble d1 = vo[i].getNinnum();
          UFDouble d2 = vo[i].getNaccumsettlenum();

          if ((d2 != null) && (Math.abs(d2.doubleValue()) > 0.0D)) vo[i].setBsettled(new UFBoolean(true)); else {
            vo[i].setBsettled(new UFBoolean(false));
          }
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            vo[i].setNnosettlenum(new UFDouble(d));
          }
          d1 = vo[i].getNmoney();
          d2 = vo[i].getNaccumsettlemny();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            vo[i].setNnosettlemny(new UFDouble(d));
          }
        }
      }
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return vo;
  }

  public StockVO[] queryStockDetailBatch(String sUnitCode, String[] bodyKey)
    throws BusinessException
  {
    StockVO[] VOs = (StockVO[])null;
    try {
      SettleDMO dmo = new SettleDMO();

      if ((bodyKey == null) || (bodyKey.length == 0)) {
        return null;
      }
      String sCondition = " and B.cgeneralbid in ";

      String strSetId = null;
      TempTableDMO dmoTempTbl = new TempTableDMO();
      strSetId = dmoTempTbl.insertTempTable(bodyKey, 
        "t_pu_ps_32", 
        "pk_pu");
      if ((strSetId == null) || (strSetId.trim().equals("()"))) {
        strSetId = " ('ErrorPk') ";
      }
      sCondition = sCondition + strSetId;

      VOs = dmo.queryStockDetail(sCondition);
      if ((VOs != null) && (VOs.length > 0)) {
        if (VOs.length != bodyKey.length) {
          throw new BusinessException(
            NCLangResOnserver.getInstance().getStrByID("40040502", 
            "UPP40040502-000037"));
        }
        for (int i = 0; i < VOs.length; i++) {
          UFDouble d1 = VOs[i].getNinnum();
          UFDouble d2 = VOs[i].getNaccumsettlenum();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            VOs[i].setNnosettlenum(new UFDouble(d));
          }
          d1 = VOs[i].getNmoney();
          d2 = VOs[i].getNaccumsettlemny();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            VOs[i].setNnosettlemny(new UFDouble(d));
          }
        }
      }
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return VOs;
  }

  private StockVO[] queryStockInSales(String sUnitCode, String sInvID, String sVendorID, String sStoreorganization)
    throws BusinessException
  {
    StockVO[] vo = (StockVO[])null;
    try {
      SettleDMO dmo = new SettleDMO();

      String sCondition = "";
      if ((sInvID != null) && (sInvID.length() > 0))
        sCondition = " and B.cinventoryid = '" + sInvID + "'";
      if ((sVendorID != null) && (sVendorID.length() > 0))
        sCondition = sCondition + " and cproviderid = '" + sVendorID + "'";
      if ((sStoreorganization != null) && 
        (sStoreorganization.trim().length() > 0))
        sCondition = sCondition + " and pk_calbody = '" + sStoreorganization + "'";
      sCondition = sCondition + " and upper(isok) = 'N'";

      sCondition = sCondition + "and coalesce(flargess,'N') = 'N' ";
      sCondition = sCondition + "order by dbilldate ";

      vo = dmo.queryStockInSales(sUnitCode, sCondition);
      if ((vo != null) && (vo.length > 0))
      {
        Vector vTemp = new Vector();
        for (int i = 0; i < vo.length; i++)
          vTemp.addElement(vo[i].getCprovidermangid());
        String[] sVendorMangID = new String[vTemp.size()];
        vTemp.copyInto(sVendorMangID);
        String[] sVendorBaseID = getVendorBaseKey(sVendorMangID);

        for (int i = 0; i < vo.length; i++) {
          vo[i].setCproviderbaseid(sVendorBaseID[i]);
          UFDouble d1 = vo[i].getNinnum();
          UFDouble d2 = vo[i].getNaccumsettlenum();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            vo[i].setNnosettlenum(new UFDouble(d));
          }
          d1 = vo[i].getNmoney();
          d2 = vo[i].getNaccumsettlemny();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            vo[i].setNnosettlemny(new UFDouble(d));
          }
        }
      }
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return vo;
  }

  public ArrayList queryStockInSalesBatch(String sUnitCode, String[] sInvID, String[] sVendorID, String[] sStoreorganization)
    throws BusinessException
  {
    ArrayList list = new ArrayList();
    if ((sUnitCode == null) || (sUnitCode.trim().length() == 0))
      return list;
    if ((sInvID == null) || (sInvID.length == 0))
      return list;
    if ((sVendorID == null) || (sVendorID.length == 0))
      return list;
    if ((sStoreorganization == null) || (sStoreorganization.length == 0))
      return list;
    for (int i = 0; i < sInvID.length; i++) {
      StockVO[] stockVOs = queryStockInSales(sUnitCode, sInvID[i], 
        sVendorID[i], sStoreorganization[i]);
      list.add(stockVOs);
    }
    return list;
  }

  private void rewriteVMIAntiSettle(SettlebillVO vo)
    throws BusinessException
  {
    if ((vo == null) || (vo.getChildrenVO() == null) || 
      (vo.getChildrenVO().length <= 0)) {
      SCMEnv.out("传入参数为空，直接返回！");
      return;
    }
    SettlebillItemVO[] bodyVos = (SettlebillItemVO[])vo.getChildrenVO();
    int iChildLen = bodyVos.length;
    ArrayList listBody = new ArrayList();
    try {
      for (int i = 0; i < iChildLen; i++)
      {
        if ((bodyVos[i] == null) || (bodyVos[i].getCvmiid() == null))
        {
          continue;
        }
        if ((bodyVos[i].getCvmiid() == null) || (bodyVos[i].getCvmiid().trim().length() == 0))
          continue;
        if ((bodyVos[i].getCinvoice_bid() != null) && (bodyVos[i].getCinvoice_bid().trim().length() > 0) && (
          (bodyVos[i].getNsettlenum() == null) || (Math.abs(bodyVos[i].getNsettlenum().doubleValue()) < VMIDMO.getDigitRMB(bodyVos[i].getPk_corp()).doubleValue())))
        {
          continue;
        }

        listBody.add(bodyVos[i]);
      }
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    if (listBody.size() <= 0) {
      SCMEnv.out("传入结算单行来自于VMI费用单独结算或者不是来自于VMI汇总，未回写库存系统返回！");
      return;
    }
    SettlebillItemVO[] rewriteBodyVos = new SettlebillItemVO[listBody
      .size()];
    listBody.toArray(rewriteBodyVos);
    VMIImpl srcVMI = null;
    try {
      srcVMI = new VMIImpl();
      srcVMI.rewriteVMIAntiSettle(rewriteBodyVos);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
  }

  private String[] getInvoiceIDs(String cgeneralbid, SettlebillItemVO[] body)
  {
    String[] s = new String[2];
    for (int i = 0; i < body.length; i++) {
      if ((body[i].getCstockrow() == null) || 
        (!body[i].getCstockrow().equals(cgeneralbid))) continue;
      s[0] = body[i].getCinvoiceid();
      s[1] = body[i].getCinvoice_bid();
      break;
    }

    return s;
  }

  private void saveBillForARAPNone(GeneralBillVO[] billVOs, SettlebillItemVO[] body, String currType, String cOperator)
    throws BusinessException
  {
    Timer timer = new Timer();
    timer.start();
    try {
      EstimateDMO dmo = new EstimateDMO();

      timer.addExecutePhase("根据采购入库单(暂估)的来源为订单，查询订单的币种、汇率（折本、折辅）、扣税类别、税率");

      IPFMetaModel myService = (IPFMetaModel)NCLocator.getInstance().lookup(IPFMetaModel.class.getName());
      Hashtable tBillType = new Hashtable();
      for (int i = 0; i < billVOs.length; i++) {
        GeneralBillHeaderVO aheadVO = (GeneralBillHeaderVO)billVOs[i].getParentVO();
        String key = "25" + aheadVO.getCbiztypeid() + "APPROVE";
        if (tBillType.get(key) == null) {
          MessagedriveVO[] driveVO = myService.queryAllMsgdrvVOs(null, "25", aheadVO.getCbiztypeid(), "APPROVE");
          if ((driveVO == null) || (driveVO.length <= 0) || (driveVO[0].getPk_billtype() == null)) continue; tBillType.put(key, driveVO[0].getPk_billtype());
        }
      }

      Hashtable tBillTemplet = new Hashtable();
      for (int i = 0; i < billVOs.length; i++) {
        GeneralBillHeaderVO aheadVO = (GeneralBillHeaderVO)billVOs[i].getParentVO();
        String key = "25" + aheadVO.getCbiztypeid() + "APPROVE";
        if (tBillType.get(key) == null) tBillType.put(key, "D1");

        String key1 = aheadVO.getPk_corp() + tBillType.get(key);
        if (tBillTemplet.get(key1) == null) {
          String[] ss = dmo.getDJDataForARAP(aheadVO.getPk_corp(), tBillType.get(key).toString());
          if (ss == null) continue; tBillTemplet.put(key1, ss);
        }

      }

      DJZBVO[] arapVO = (DJZBVO[])PfUtilTools.runChangeDataAry("45", "D1", billVOs);
      if ((arapVO == null) || (arapVO.length == 0)) return;

      Vector v1 = new Vector();
      Vector v2 = new Vector();
      Vector v3 = new Vector();
      Vector vPkInvMang = new Vector();
      for (int i = 0; i < billVOs.length; i++) {
        GeneralBillHeaderVO aheadVO = (GeneralBillHeaderVO)billVOs[i]
          .getParentVO();
        GeneralBillItemVO[] abodyVO = (GeneralBillItemVO[])billVOs[i]
          .getChildrenVO();

        DJZBHeaderVO headVO = (DJZBHeaderVO)arapVO[i].getParentVO();
        UFDouble nJSHJ = null;
        headVO.setVouchid(aheadVO.getCgeneralhid());

        String key = "25" + aheadVO.getCbiztypeid() + "APPROVE";
        String cBillType = tBillType.get(key).toString();
        String[] ss = (String[])null;
        key = aheadVO.getPk_corp() + tBillType.get(key);
        if (tBillTemplet.get(key) != null) ss = (String[])tBillTemplet.get(key);
        if ((ss != null) && (ss.length > 0)) headVO.setYwbm(ss[0]);

        headVO.setLrr(aheadVO.getCoperatorid());
        headVO.setDjrq(aheadVO.getDbilldate());
        headVO.setDjkjnd(Integer.toString(aheadVO.getDbilldate()
          .getYear()));

        String sTemp = Integer.toString(aheadVO.getDbilldate().getMonth());
        if (sTemp.length() < 2) sTemp = "0" + sTemp;
        headVO.setDjkjqj(sTemp);

        headVO.setPrepay(new UFBoolean(false));
        headVO.setDjlxbm(cBillType);
        headVO.setQcbz(new UFBoolean(false));
        headVO.setLybz(new Integer(4));
        headVO.setDjzt(new Integer(1));
        headVO.setDjdl("yf");
        headVO.setPzglh(new Integer(1));
        headVO.setWldx(new Integer(1));
        headVO.setZdr(cOperator);
        headVO.setEnduser(cOperator);

        v1.addElement(headVO);

        DJZBItemVO bodyVO = ((DJZBItemVO[])arapVO[i].getChildrenVO())[0];
        bodyVO.setDwbm(aheadVO.getPk_corp());
        bodyVO.setFb_oid(abodyVO[0].getCgeneralbid());

        v3.addElement(bodyVO.getKsbm_cl());
        vPkInvMang.addElement(bodyVO.getChbm_cl());

        if ((abodyVO[0].getNprice() == null) || 
          (abodyVO[0].getNprice().doubleValue() == 0.0D))
          continue;
        if ((abodyVO[0].getNmny() == null) || 
          (abodyVO[0].getNmny().doubleValue() == 0.0D)) {
          continue;
        }
        bodyVO.setFbye(new UFDouble(0));
        bodyVO.setDffbje(new UFDouble(0));

        String[] sInvoiceIDs = getInvoiceIDs(abodyVO[0]
          .getCgeneralbid(), body);
        bodyVO.setJsfsbm("25");
        bodyVO.setDdlx(sInvoiceIDs[0]);
        bodyVO.setDdhh(sInvoiceIDs[1]);
        bodyVO.setWldx(new Integer(1));

        bodyVO.setBzbm(currType);
        bodyVO.setBbhl(new UFDouble(1));

        int nType = 2;
        bodyVO.setKslb(new Integer(nType));

        bodyVO.setSl(new UFDouble(0));

        bodyVO.setOld_sys_flag(new UFBoolean(false));
        bodyVO.setFx(new Integer(-1));
        bodyVO.setJffbje(new UFDouble(0));
        bodyVO.setJfbbje(new UFDouble(0));
        bodyVO.setJfybje(new UFDouble(0));

        bodyVO.setJfybsj(new UFDouble(0));
        bodyVO.setJfybwsje(new UFDouble(0));

        bodyVO.setWbfybje(new UFDouble(0));
        bodyVO.setWbffbje(new UFDouble(0));
        bodyVO.setWbfbbje(new UFDouble(0));

        bodyVO.setYwybm(aheadVO.getCbizid());

        UFDouble d1 = abodyVO[0].getNmny();
        UFDouble d2 = bodyVO.getSl();

        if (nType == 0)
        {
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() * d2.doubleValue() * 0.01D / (
              1.0D - d2.doubleValue() * 0.01D);
            bodyVO.setDfbbsj(new UFDouble(d));
            bodyVO.setDfybsj(new UFDouble(d));
            d += d1.doubleValue();
            nJSHJ = new UFDouble(d);
          }
          if ((bodyVO.getDj() != null) && (d2 != null)) {
            double d = bodyVO.getDj().doubleValue() / (
              1.0D - d2.doubleValue() * 0.01D);
            bodyVO.setHsdj(new UFDouble(d));
          }
        } else if (nType == 1)
        {
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() * d2.doubleValue() * 0.01D;
            bodyVO.setDfbbsj(new UFDouble(d));
            bodyVO.setDfybsj(new UFDouble(d));
            d += d1.doubleValue();
            nJSHJ = new UFDouble(d);
          }
          if ((bodyVO.getDj() != null) && (d2 != null)) {
            double d = bodyVO.getDj().doubleValue() * (
              1.0D + d2.doubleValue() * 0.01D);
            bodyVO.setHsdj(new UFDouble(d));
          }
        }
        else {
          bodyVO.setDfbbsj(new UFDouble(0));
          bodyVO.setDfybsj(new UFDouble(0));
          nJSHJ = d1;
          bodyVO.setHsdj(bodyVO.getDj());
        }

        d2 = nJSHJ;
        d1 = bodyVO.getDfbbsj();
        if ((d1 != null) && (d2 != null)) {
          double d = d2.doubleValue() - d1.doubleValue();
          bodyVO.setDfbbwsje(new UFDouble(d));
          bodyVO.setDfybwsje(new UFDouble(d));
        }

        bodyVO.setDfbbje(nJSHJ);
        bodyVO.setDfybje(nJSHJ);
        bodyVO.setYbye(nJSHJ);
        bodyVO.setBbye(nJSHJ);
        v2.addElement(bodyVO);
      }

      timer.addExecutePhase("VO 对照");

      DJZBHeaderVO[] headVOs = new DJZBHeaderVO[v1.size()];
      v1.copyInto(headVOs);
      DJZBItemVO[] bodyVOs = new DJZBItemVO[v2.size()];
      v2.copyInto(bodyVOs);

      if (v3.size() > 0) {
        String[] pk_cumandoc = new String[v3.size()];
        v3.copyInto(pk_cumandoc);
        SettleDMO settleDMO = new SettleDMO();
        String[] pk_cubasdoc = settleDMO
          .queryVendorBaseIDForARAP(pk_cumandoc);
        if ((pk_cubasdoc != null) && (pk_cubasdoc.length > 0)) {
          for (int i = 0; i < bodyVOs.length; i++) {
            bodyVOs[i].setHbbm(pk_cubasdoc[i]);
          }
        }
      }
      timer.addExecutePhase("为应付获得供应商基础ID");

      if (vPkInvMang.size() > 0) {
        String[] pk_invmandocs = new String[vPkInvMang.size()];
        vPkInvMang.copyInto(pk_invmandocs);
        SettleDMO dmoQueryPkInvBase = new SettleDMO();
        String[] pk_invbasdocs = dmoQueryPkInvBase
          .getInvBaseID(pk_invmandocs);
        if ((pk_invbasdocs != null) && (pk_invbasdocs.length > 0)) {
          for (int i = 0; i < bodyVOs.length; i++) {
            bodyVOs[i].setCinventoryid(pk_invbasdocs[i]);
          }
        }
      }
      timer.addExecutePhase("为应付获得存货基础ID");

      v1 = new Vector();
      Vector vTemp = new Vector();
      v1.addElement(headVOs[0]);
      vTemp.addElement(headVOs[0].getVouchid().trim());
      for (int i = 1; i < headVOs.length; i++) {
        String s1 = headVOs[i].getVouchid().trim();
        if (!vTemp.contains(s1)) {
          vTemp.addElement(s1);
          v1.addElement(headVOs[i]);
        }
      }
      headVOs = new DJZBHeaderVO[v1.size()];
      v1.copyInto(headVOs);
      v1 = new Vector();
      for (int i = 0; i < headVOs.length; i++) {
        DJZBVO VO = new DJZBVO();
        String s1 = headVOs[i].getVouchid().trim();
        v2 = new Vector();
        for (int j = 0; j < bodyVOs.length; j++) {
          String s2 = bodyVOs[j].getVouchid().trim();
          if (s1.equals(s2))
            v2.addElement(bodyVOs[j]);
        }
        if (v2.size() > 0) {
          DJZBItemVO[] tempVOs = new DJZBItemVO[v2.size()];
          v2.copyInto(tempVOs);
          VO.setParentVO(headVOs[i]);
          VO.setChildrenVO(tempVOs);
          v1.addElement(VO);
        }
      }
      DJZBVO[] VOs = new DJZBVO[v1.size()];
      v1.copyInto(VOs);
      timer.addExecutePhase("组合VO[]");

      if ((VOs != null) && (VOs.length > 0)) {
        VoTools tools = new VoTools();
        for (int i = 0; i < VOs.length; i++) {
          VOs[i] = tools.getSumCG(VOs[i]);
        }
      }
      timer.addExecutePhase("调用应付的VO转换工具");

      ArapHelper.saveArapBillForSettle(VOs);

      timer.addExecutePhase("调用应付提供接口传送单据");

      timer.showAllExecutePhase("无发票结算向应付传送数据时间分布--明细");
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
  }

  private void saveBillFromOutter0(String sEstMode, String sDiffMode, SettlebillVO settlebillVO, String cOperator, UFDate dCurrDate, GeneralHItemVO[] generalHItemVO)
    throws BusinessException
  {
    IIAToPUBill myService = null;
    Timer timer = new Timer();
    timer.start();
    try
    {
      SettlebillItemVO[] VOs = settlebillVO.getBodyVO();
      GeneralHHeaderVO[] stockHeadVO = getStockHeadInfo(VOs);
      timer.addExecutePhase("批次获得入库单头信息");
      GeneralHItemVO[] stockBodyVO = getStockBodyInfo(VOs);
      timer.addExecutePhase("批次获得入库单体信息");
      if ((stockHeadVO == null) || (stockHeadVO.length == 0))
        return;
      if ((stockBodyVO == null) || (stockBodyVO.length == 0)) {
        return;
      }
      myService = (IIAToPUBill)NCLocator.getInstance().lookup(
        IIAToPUBill.class.getName());
      int nCount = 0;

      int iLenVOs = VOs.length;

      HashMap mapStockHsl = new HashMap();
      int iLen = stockBodyVO.length;
      for (int i = 0; i < iLen; i++) {
        if ((stockBodyVO[i].getCgeneralbid() == null) || 
          (stockBodyVO[i].getHsl() == null))
          continue;
        mapStockHsl.put(stockBodyVO[i].getCgeneralbid(), stockBodyVO[i]
          .getHsl());
      }
      String strBid = null;
      UFDouble[] uaAssistNum = new UFDouble[iLenVOs];
      UFDouble uNum = null; UFDouble uHsl = null;
      for (int i = 0; i < iLenVOs; i++) {
        uaAssistNum[i] = new UFDouble(0.0D);
        strBid = VOs[i].getCstockrow();
        if (strBid == null)
          continue;
        uHsl = (UFDouble)mapStockHsl.get(strBid);
        if ((uHsl == null) || (uHsl.doubleValue() == 0.0D))
          continue;
        uNum = VOs[i].getNsettlenum();
        if (uNum == null)
          continue;
        uaAssistNum[i] = uNum.div(uHsl);
      }
      timer.addExecutePhase("获得辅数量");

      ArrayList tsList = new ArrayList();
      try
      {
        ISysInitQry myServiceTemp = (ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName());
        SysInitVO[] initVO = myServiceTemp.querySysInit(
          VOs[0].getPk_corp(), "PO35");
        if ((initVO != null) && (initVO.length > 0)) {
          tsList.add(initVO[0].getValue());
          tsList.add(initVO[1].getValue());
          tsList.add(initVO[2].getPkvalue());
        }
      }
      catch (Exception e)
      {
        PubDMO.throwBusinessException(e);
      }

      Vector v0 = new Vector();
      for (int i = 0; i < VOs.length; i++) {
        String stockID = VOs[i].getCstockid();
        String stockRow = VOs[i].getCstockrow();
        if ((stockID == null) || (stockID.length() <= 0) || (stockRow == null) || 
          (stockRow.length() <= 0)) continue;
        BillVO[] billVOs = (BillVO[])null;
        if ((generalHItemVO == null) || (generalHItemVO.length == 0))
          billVOs = transferIAData(stockHeadVO[nCount], 
            stockBodyVO[nCount], sEstMode, sDiffMode, 
            VOs[i], cOperator, dCurrDate, null, 
            uaAssistNum[i], stockHeadVO[nCount].getCstoreorganization(), tsList);
        else {
          billVOs = transferIAData(stockHeadVO[nCount], 
            stockBodyVO[nCount], sEstMode, sDiffMode, 
            VOs[i], cOperator, dCurrDate, generalHItemVO, 
            uaAssistNum[i], stockHeadVO[nCount].getCstoreorganization(), tsList);
        }
        nCount++;
        if ((billVOs == null) || (billVOs.length == 0))
          continue;
        for (int j = 0; j < billVOs.length; j++) {
          v0.addElement(billVOs[j]);
        }
      }
      timer.addExecutePhase("VO转换transferIAData()<按结算单行循环>");
      if ((v0 == null) || (v0.size() == 0))
        return;
      BillVO[] billVOs = new BillVO[v0.size()];
      v0.copyInto(billVOs);

      billVOs = new SettleDMO().postDealBillVO(settlebillVO.getHeadVO().getPk_corp(), stockBodyVO, billVOs);

      Hashtable tZG = new Hashtable();
      Vector vTemp = new Vector();
      if ((generalHItemVO != null) && (generalHItemVO.length > 0)) {
        for (int i = 0; i < generalHItemVO.length; i++) {
          if ((generalHItemVO[i].getBzgflag() != null) && (generalHItemVO[i].getBzgflag().booleanValue()) && (!vTemp.contains(generalHItemVO[i].getCgeneralbid()))) {
            vTemp.addElement(generalHItemVO[i].getCgeneralbid());
          }
        }
      }
      if (vTemp.size() > 0) {
        String[] cgeneralbid = new String[vTemp.size()];
        vTemp.copyInto(cgeneralbid);
        tZG = new SettleDMO().queryStockZGInfo(cgeneralbid);
      }
      if (tZG.size() > 0) {
        if (sEstMode.equals("单到回冲")) billVOs = crackBillDDHC(tZG, billVOs); else
          billVOs = crackBillDDBC(tZG, billVOs);
      }
      if ((billVOs == null) || (billVOs.length == 0)) return;

      Vector v = new Vector();
      for (int i = 0; i < billVOs.length - 1; i++) {
        String s1 = ((IBillHeaderVO)billVOs[i].getParentVO())
          .getCbillid().trim();
        String ss1 = ((IBillHeaderVO)billVOs[i].getParentVO())
          .getBestimateflag().toString().toUpperCase().trim();
        String sss1 = ((IBillHeaderVO)billVOs[i].getParentVO())
          .getCbilltypecode().trim();
        boolean b = false;
        for (int j = i + 1; j < billVOs.length; j++) {
          String s2 = ((IBillHeaderVO)billVOs[j].getParentVO())
            .getCbillid().trim();
          String ss2 = ((IBillHeaderVO)billVOs[j].getParentVO())
            .getBestimateflag().toString().toUpperCase().trim();
          String sss2 = ((IBillHeaderVO)billVOs[j].getParentVO())
            .getCbilltypecode().trim();
          if ((s1.equals(s2)) && (ss1.equals(ss2)) && (sss1.equals(sss2))) {
            b = true;
            break;
          }
        }
        if (!b) {
          v.addElement(billVOs[i]);
        }
      }
      v.addElement(billVOs[(billVOs.length - 1)]);
      timer.addExecutePhase("(1) 存货核算单据来源单据头ID和暂估标志和单据类型唯一性组合");

      Vector vv = new Vector();
      for (int i = 0; i < v.size(); i++) {
        BillVO tempVO = (BillVO)v.elementAt(i);
        String s1 = ((IBillHeaderVO)tempVO.getParentVO()).getCbillid()
          .trim();
        String ss1 = ((IBillHeaderVO)tempVO.getParentVO())
          .getBestimateflag().toString().toUpperCase().trim();
        String sss1 = ((IBillHeaderVO)tempVO.getParentVO())
          .getCbilltypecode().trim();
        vTemp = new Vector();
        for (int j = 0; j < billVOs.length; j++) {
          String s2 = ((IBillHeaderVO)billVOs[j].getParentVO())
            .getCbillid().trim();
          String ss2 = ((IBillHeaderVO)billVOs[j].getParentVO())
            .getBestimateflag().toString().toUpperCase().trim();
          String sss2 = ((IBillHeaderVO)billVOs[j].getParentVO())
            .getCbilltypecode().trim();
          if ((s1.equals(s2)) && (ss1.equals(ss2)) && (sss1.equals(sss2))) {
            IBillItemVO[] tempItemVO = (IBillItemVO[])billVOs[j]
              .getChildrenVO();
            for (int k = 0; k < tempItemVO.length; k++)
              vTemp.addElement(tempItemVO[k]);
          }
        }
        IBillItemVO[] tempItemVOs = new IBillItemVO[vTemp.size()];
        vTemp.copyInto(tempItemVOs);

        for (int j = 0; j < tempItemVOs.length; j++) {
          tempItemVOs[j].setIrownumber(new Integer(j));
          if (tempItemVOs[j].getCsourcebilltypecode().equals("27"))
            tempItemVOs[j].setVsourcebillcode(settlebillVO
              .getHeadVO().getVsettlebillcode());
        }
        tempVO.setChildrenVO(tempItemVOs);
        vv.addElement(tempVO);
      }
      timer
        .addExecutePhase("(3) 所有存货核算单据体的来源单据头ID相同, 而且暂估标志和单据类型相同时,归为同一张存货核算单据");

      billVOs = (BillVO[])null;
      billVOs = new BillVO[vv.size()];
      vv.copyInto(billVOs);
      timer.addExecutePhase("(4) 整理需向存货传送的单据");

      if ((billVOs == null) || (billVOs.length == 0))
        return;
      String s1 = ((IBillHeaderVO)billVOs[0].getParentVO())
        .getCsourcemodulename();
      String s2 = ((IBillItemVO[])billVOs[0].getChildrenVO())[0]
        .getCsourcebilltypecode();

      String sTime = new UFDateTime(new Date()).toString();
      for (int i = 0; i < billVOs.length; i++) {
        ((IBillHeaderVO)billVOs[i].getParentVO()).setVbillcode(null);
        ((IBillHeaderVO)billVOs[i].getParentVO()).setTmaketime(sTime);
        ((IBillHeaderVO)billVOs[i].getParentVO()).setTlastmaketime(sTime);
        ((IBillHeaderVO)billVOs[i].getParentVO()).setClastoperatorid(cOperator);
        IBillItemVO[] bodyVO = (IBillItemVO[])billVOs[i].getChildrenVO();
        if ((bodyVO != null) && (bodyVO.length > 0)) {
          for (int j = 0; j < bodyVO.length; j++) {
            bodyVO[j].setVbillcode(null);
          }
        }
      }

      myService.saveBillFromOutterArray(billVOs, s1, s2);
      timer.addExecutePhase("传送数据<存货核算接口>");
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    timer
      .showAllExecutePhase("向存货核算系统传送数据(成批单据传送)[SettleBO.saveBillFromOutter0()]");
  }

  private BillVO[] crackBillDDHC(Hashtable tStock, BillVO[] VOs)
    throws Exception
  {
    if ((VOs == null) || (VOs.length == 0)) return VOs;

    Hashtable t = new Hashtable();
    Object oTemp = null; Object[] data = (Object[])null;
    double d1 = 0.0D; double d2 = 0.0D; double d3 = 0.0D; double d4 = 0.0D;

    Vector v = new Vector();
    for (int i = 0; i < VOs.length; i++) {
      IBillHeaderVO headVO = (IBillHeaderVO)VOs[i].getParentVO();
      if ((headVO.getBRedBill() == null) || (!headVO.getBRedBill().booleanValue()))
        continue;
      IBillItemVO[] bodyVO = (IBillItemVO[])VOs[i].getChildrenVO();
      for (int j = 0; j < bodyVO.length; j++) {
        if (v.contains(bodyVO[j].getCbill_bid())) continue; v.addElement(bodyVO[j].getCbill_bid());
      }
    }

    String[] sTemp = new String[v.size()];
    v.copyInto(sTemp);
    String ss = null;
    try {
      TempTableDMO dmoTmpTbl = new TempTableDMO();
      ss = dmoTmpTbl.insertTempTable(sTemp, "t_pu_id_in_31", "pk_pu");
      if ((ss == null) || (ss.trim().equals("()")))
        ss = " ('ErrorPk') ";
    }
    catch (Exception e) {
      throw new SQLException(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000046") + e.getMessage());
    }
    ss = " dr = 0 and cstockrow in " + ss;
    Hashtable hSettle = new PubDMO().queryHtResultFromAnyTable("po_settlebill_b", "cstockrow", new String[] { "nsettlenum", "ngaugemny" }, ss);

    for (int i = 0; i < VOs.length; i++) {
      IBillHeaderVO headVO = (IBillHeaderVO)VOs[i].getParentVO();
      if ((headVO.getBRedBill() != null) && (headVO.getBRedBill().booleanValue())) {
        IBillItemVO[] bodyVO = (IBillItemVO[])VOs[i].getChildrenVO();

        for (int j = 0; j < bodyVO.length; j++) {
          oTemp = hSettle.get(bodyVO[j].getCbill_bid());
          if (oTemp != null) {
            v = (Vector)oTemp;
            if ((v != null) && (v.size() > 0)) {
              for (int k = 0; k < v.size(); k++) {
                data = (Object[])v.elementAt(k);
                d1 = new UFDouble(data[0].toString()).doubleValue();
                d2 = new UFDouble(data[1].toString()).doubleValue();

                oTemp = t.get(bodyVO[j].getCbill_bid());
                if (oTemp != null) {
                  data = (Object[])oTemp;
                  d1 += new UFDouble(data[0].toString()).doubleValue();
                  d2 += new UFDouble(data[1].toString()).doubleValue();
                }
                t.put(bodyVO[j].getCbill_bid(), new UFDouble[] { new UFDouble(d1), new UFDouble(d2) });
              }
            }
          }

          oTemp = t.get(bodyVO[j].getCbill_bid());
          if (oTemp != null) {
            data = (Object[])oTemp;
            d1 = new UFDouble(data[0].toString()).doubleValue();
            d2 = new UFDouble(data[1].toString()).doubleValue();
          }

          oTemp = tStock.get(bodyVO[j].getCbill_bid());
          if (oTemp != null) {
            data = (Object[])oTemp;
            d3 = new UFDouble(data[0].toString()).doubleValue();
            d4 = new UFDouble(data[1].toString()).doubleValue();
          }

          if (Math.abs(d1 - d3) >= VMIDMO.getDigitRMB(headVO.getPk_corp()).doubleValue())
            continue;
          d4 -= d2 + bodyVO[j].getNmoney().doubleValue();
          bodyVO[j].setNmoney(new UFDouble(d4 * -1.0D));
        }
      }
    }

    return VOs;
  }

  private BillVO[] crackBillDDBC(Hashtable tStock, BillVO[] VOs)
    throws Exception
  {
    if ((VOs == null) || (VOs.length == 0)) return VOs;

    Hashtable t = new Hashtable();
    Object oTemp = null; Object[] data = (Object[])null;
    double d1 = 0.0D; double d2 = 0.0D; double d22 = 0.0D; double d3 = 0.0D; double d4 = 0.0D; double d44 = 0.0D;

    Vector v = new Vector();
    for (int i = 0; i < VOs.length; i++) {
      IBillItemVO[] bodyVO = (IBillItemVO[])VOs[i].getChildrenVO();
      for (int j = 0; j < bodyVO.length; j++) {
        if (v.contains(bodyVO[j].getCbill_bid())) continue; v.addElement(bodyVO[j].getCbill_bid());
      }
    }

    String[] sTemp = new String[v.size()];
    v.copyInto(sTemp);
    String ss = null;
    try {
      TempTableDMO dmoTmpTbl = new TempTableDMO();
      ss = dmoTmpTbl.insertTempTable(sTemp, "t_pu_id_in_31", "pk_pu");
      if ((ss == null) || (ss.trim().equals("()")))
        ss = " ('ErrorPk') ";
    }
    catch (Exception e) {
      throw new SQLException(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000046") + e.getMessage());
    }
    ss = " dr = 0 and cstockrow in " + ss;
    Hashtable hSettle = new PubDMO().queryHtResultFromAnyTable("po_settlebill_b", "cstockrow", new String[] { "nsettlenum", "nmoney", "ngaugemny" }, ss);

    for (int i = 0; i < VOs.length; i++) {
      IBillHeaderVO headVO = (IBillHeaderVO)VOs[i].getParentVO();
      IBillItemVO[] bodyVO = (IBillItemVO[])VOs[i].getChildrenVO();

      for (int j = 0; j < bodyVO.length; j++) {
        oTemp = hSettle.get(bodyVO[j].getCbill_bid());
        if (oTemp != null) {
          v = (Vector)oTemp;
          if ((v != null) && (v.size() > 0)) {
            for (int k = 0; k < v.size(); k++) {
              data = (Object[])v.elementAt(k);
              d1 = new UFDouble(data[0].toString()).doubleValue();
              d2 = new UFDouble(data[1].toString()).doubleValue();
              if (data[2] != null) {
                d22 = new UFDouble(data[2].toString()).doubleValue();
              }

              oTemp = t.get(bodyVO[j].getCbill_bid());
              if (oTemp != null) {
                data = (Object[])oTemp;
                d1 += new UFDouble(data[0].toString()).doubleValue();
                d2 += new UFDouble(data[1].toString()).doubleValue();
                if (data[2] != null) d22 += new UFDouble(data[2].toString()).doubleValue();
              }
              t.put(bodyVO[j].getCbill_bid(), new UFDouble[] { new UFDouble(d1), new UFDouble(d2), new UFDouble(d22) });
            }
          }
        }

        oTemp = t.get(bodyVO[j].getCbill_bid());
        if (oTemp != null) {
          data = (Object[])oTemp;
          d1 = new UFDouble(data[0].toString()).doubleValue();
          d2 = new UFDouble(data[1].toString()).doubleValue();
          d22 = new UFDouble(data[2].toString()).doubleValue();
        }

        oTemp = tStock.get(bodyVO[j].getCbill_bid());
        if (oTemp != null) {
          data = (Object[])oTemp;
          d3 = new UFDouble(data[0].toString()).doubleValue();
          d4 = new UFDouble(data[1].toString()).doubleValue();
        }

        if (Math.abs(d1 - d3) >= VMIDMO.getDigitRMB(headVO.getPk_corp()).doubleValue())
          continue;
        d44 = d2 - d4 - bodyVO[j].getNmoney().doubleValue();
        d4 = d2 - d4 - d44;
        bodyVO[j].setNmoney(new UFDouble(d4));
      }

    }

    return VOs;
  }

  public void saveBillFromOutter1(ArrayList listPara)
    throws BusinessException
  {
    if ((listPara == null) || (listPara.size() < 7)) {
      SCMEnv.out("程序BUG：传入参数数不正确");
      SCMEnv.out(new Exception());
    }

    Timer timer = new Timer();
    timer.start();

    String sEstMode = (String)listPara.get(0);
    String sDiffMode = (String)listPara.get(1);
    SettlebillVO settlebillVO = (SettlebillVO)listPara.get(2);
    String cOperator = (String)listPara.get(3);
    UFDate dCurrDate = (UFDate)listPara.get(4);
    GeneralHItemVO[] generalHItemVOs = (GeneralHItemVO[])listPara.get(5);
    StockVO[] stocks = (StockVO[])listPara.get(6);

    if (stocks == null) {
      saveBillFromOutter0(sEstMode, sDiffMode, settlebillVO, cOperator, 
        dCurrDate, generalHItemVOs);
      return;
    }

    SettlebillItemVO[] VOs = settlebillVO.getBodyVO();
    int iLenVOs = VOs.length;
    if ((VOs != null) && (stocks.length < VOs.length)) {
      Hashtable t = new Hashtable();
      for (int i = 0; i < stocks.length; i++)
        t.put(stocks[i].getCgeneralbid(), stocks[i]);
      Vector v = new Vector();
      for (int i = 0; i < VOs.length; i++) {
        String key = VOs[i].getCstockrow();
        if ((key != null) && (t.containsKey(key)))
          v.addElement(t.get(key));
      }
      stocks = new StockVO[v.size()];
      v.copyInto(stocks);
    }

    IIAToPUBill myService = null;
    try
    {
      HashMap mapStockHsl = new HashMap();
      int iLen = stocks.length;
      for (int i = 0; i < iLen; i++) {
        if ((stocks[i].getCgeneralbid() == null) || 
          (stocks[i].getHsl() == null))
          continue;
        mapStockHsl.put(stocks[i].getCgeneralbid(), stocks[i].getHsl());
      }
      String strBid = null;
      UFDouble[] uaAssistNum = new UFDouble[iLenVOs];
      UFDouble uNum = null; UFDouble uHsl = null;
      for (int i = 0; i < iLenVOs; i++) {
        uaAssistNum[i] = new UFDouble(0.0D);
        strBid = VOs[i].getCstockrow();
        if (strBid == null)
          continue;
        uHsl = (UFDouble)mapStockHsl.get(strBid);
        if ((uHsl == null) || (uHsl.doubleValue() == 0.0D))
          continue;
        uNum = VOs[i].getNsettlenum();
        if (uNum == null)
          continue;
        uaAssistNum[i] = uNum.div(uHsl);
      }
      timer.addExecutePhase("获得辅数量");

      ArrayList tsList = new ArrayList();
      try
      {
        ISysInitQry myServiceTemp = (ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName());
        SysInitVO[] initVO = myServiceTemp.querySysInit(
          VOs[0].getPk_corp(), "PO35");
        if ((initVO != null) && (initVO.length > 0)) {
          tsList.add(initVO[0].getValue());
          tsList.add(initVO[1].getValue());
          tsList.add(initVO[2].getPkvalue());
        }
      }
      catch (Exception e)
      {
        PubDMO.throwBusinessException(e);
      }

      Vector v0 = new Vector();
      int nCount = 0;
      boolean isNull = (generalHItemVOs == null) || 
        (generalHItemVOs.length == 0);
      for (int i = 0; i < iLenVOs; i++) {
        String stockID = VOs[i].getCstockid();
        String stockRow = VOs[i].getCstockrow();
        if ((stockID == null) || (stockID.length() <= 0) || (stockRow == null) || 
          (stockRow.length() <= 0)) continue;
        BillVO[] billVOs = (BillVO[])null;
        if (isNull)
          billVOs = transferIAData1(stocks[nCount], sEstMode, 
            sDiffMode, VOs[i], cOperator, dCurrDate, null, 
            uaAssistNum[i], stocks[nCount].getCstoreorganization(), tsList);
        else {
          billVOs = transferIAData1(stocks[nCount], sEstMode, 
            sDiffMode, VOs[i], cOperator, dCurrDate, 
            generalHItemVOs, uaAssistNum[i], 
            stocks[nCount].getCstoreorganization(), tsList);
        }
        nCount++;
        if ((billVOs == null) || (billVOs.length == 0))
          continue;
        for (int j = 0; j < billVOs.length; j++) {
          v0.addElement(billVOs[j]);
        }
      }
      timer.addExecutePhase("VO转换transferIAData()<按结算单行循环>");
      if ((v0 == null) || (v0.size() == 0))
        return;
      BillVO[] billVOs = new BillVO[v0.size()];
      v0.copyInto(billVOs);

      billVOs = new SettleDMO().postDealBillVOSP(settlebillVO.getHeadVO().getPk_corp(), stocks, billVOs);

      Hashtable tZG = new Hashtable();
      Vector vTemp = new Vector();
      if ((generalHItemVOs != null) && (generalHItemVOs.length > 0)) {
        for (int i = 0; i < generalHItemVOs.length; i++) {
          if ((generalHItemVOs[i].getBzgflag() != null) && (generalHItemVOs[i].getBzgflag().booleanValue()) && (!vTemp.contains(generalHItemVOs[i].getCgeneralbid()))) {
            vTemp.addElement(generalHItemVOs[i].getCgeneralbid());
          }
        }
      }
      if (vTemp.size() > 0) {
        String[] cgeneralbid = new String[vTemp.size()];
        vTemp.copyInto(cgeneralbid);
        tZG = new SettleDMO().queryStockZGInfo(cgeneralbid);
      }
      if (tZG.size() > 0) {
        if (sEstMode.equals("单到回冲")) billVOs = crackBillDDHC(tZG, billVOs); else
          billVOs = crackBillDDBC(tZG, billVOs);
      }
      if ((billVOs == null) || (billVOs.length == 0)) return;

      Vector v = new Vector();
      for (int i = 0; i < billVOs.length - 1; i++) {
        String s1 = ((IBillHeaderVO)billVOs[i].getParentVO())
          .getCbillid().trim();
        String ss1 = ((IBillHeaderVO)billVOs[i].getParentVO())
          .getBestimateflag().toString().toUpperCase().trim();
        String sss1 = ((IBillHeaderVO)billVOs[i].getParentVO())
          .getCbilltypecode().trim();
        boolean b = false;
        for (int j = i + 1; j < billVOs.length; j++) {
          String s2 = ((IBillHeaderVO)billVOs[j].getParentVO())
            .getCbillid().trim();
          String ss2 = ((IBillHeaderVO)billVOs[j].getParentVO())
            .getBestimateflag().toString().toUpperCase().trim();
          String sss2 = ((IBillHeaderVO)billVOs[j].getParentVO())
            .getCbilltypecode().trim();
          if ((s1.equals(s2)) && (ss1.equals(ss2)) && (sss1.equals(sss2))) {
            b = true;
            break;
          }
        }
        if (!b) {
          v.addElement(billVOs[i]);
        }
      }
      v.addElement(billVOs[(billVOs.length - 1)]);
      timer.addExecutePhase("(1) 存货核算单据来源单据头ID和暂估标志和单据类型唯一性组合");

      Vector vv = new Vector();
      for (int i = 0; i < v.size(); i++) {
        BillVO tempVO = (BillVO)v.elementAt(i);
        String s1 = ((IBillHeaderVO)tempVO.getParentVO()).getCbillid()
          .trim();
        String ss1 = ((IBillHeaderVO)tempVO.getParentVO())
          .getBestimateflag().toString().toUpperCase().trim();
        String sss1 = ((IBillHeaderVO)tempVO.getParentVO())
          .getCbilltypecode().trim();
        vTemp = new Vector();
        for (int j = 0; j < billVOs.length; j++) {
          String s2 = ((IBillHeaderVO)billVOs[j].getParentVO())
            .getCbillid().trim();
          String ss2 = ((IBillHeaderVO)billVOs[j].getParentVO())
            .getBestimateflag().toString().toUpperCase().trim();
          String sss2 = ((IBillHeaderVO)billVOs[j].getParentVO())
            .getCbilltypecode().trim();
          if ((s1.equals(s2)) && (ss1.equals(ss2)) && (sss1.equals(sss2))) {
            IBillItemVO[] tempItemVO = (IBillItemVO[])billVOs[j]
              .getChildrenVO();
            for (int k = 0; k < tempItemVO.length; k++)
              vTemp.addElement(tempItemVO[k]);
          }
        }
        IBillItemVO[] tempItemVOs = new IBillItemVO[vTemp.size()];
        vTemp.copyInto(tempItemVOs);

        for (int j = 0; j < tempItemVOs.length; j++) {
          tempItemVOs[j].setIrownumber(new Integer(j));
          if (tempItemVOs[j].getCsourcebilltypecode().equals("27"))
            tempItemVOs[j].setVsourcebillcode(settlebillVO
              .getHeadVO().getVsettlebillcode());
        }
        tempVO.setChildrenVO(tempItemVOs);
        vv.addElement(tempVO);
      }
      timer
        .addExecutePhase("(3) 所有存货核算单据体的来源单据头ID相同, 而且暂估标志和单据类型相同时,归为同一张存货核算单据");

      billVOs = (BillVO[])null;
      billVOs = new BillVO[vv.size()];
      vv.copyInto(billVOs);
      timer.addExecutePhase("(4) 整理需向存货传送的单据");

      if ((billVOs == null) || (billVOs.length == 0))
        return;
      String s1 = ((IBillHeaderVO)billVOs[0].getParentVO())
        .getCsourcemodulename();
      String s2 = ((IBillItemVO[])billVOs[0].getChildrenVO())[0]
        .getCsourcebilltypecode();

      String sTime = new UFDateTime(new Date()).toString();
      for (int i = 0; i < billVOs.length; i++) {
        ((IBillHeaderVO)billVOs[i].getParentVO()).setVbillcode(null);
        ((IBillHeaderVO)billVOs[i].getParentVO()).setTmaketime(sTime);
        ((IBillHeaderVO)billVOs[i].getParentVO()).setTlastmaketime(sTime);
        ((IBillHeaderVO)billVOs[i].getParentVO()).setClastoperatorid(cOperator);
        IBillItemVO[] bodyVO = (IBillItemVO[])billVOs[i].getChildrenVO();
        if ((bodyVO != null) && (bodyVO.length > 0)) {
          for (int j = 0; j < bodyVO.length; j++) {
            bodyVO[j].setVbillcode(null);
          }
        }
      }

      myService = (IIAToPUBill)NCLocator.getInstance().lookup(
        IIAToPUBill.class.getName());
      myService.saveBillFromOutterArray(billVOs, s1, s2);
      timer.addExecutePhase("传送数据<存货核算接口>");
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    timer
      .showAllExecutePhase("向存货核算系统传送数据-SettleBO.saveBillFromOutter1()-时间分布");
  }

  private void saveBillFromOutterForDirect(IinvoiceVO[] invoiceVO, String[] sStoreOrgID, UFDate currDate, SettlebillVO settlebillVO, String cOperator)
    throws BusinessException
  {
    IIAToPUBill myService = null;
    Timer timer = new Timer();
    timer.start();
    try {
      myService = (IIAToPUBill)NCLocator.getInstance().lookup(
        IIAToPUBill.class.getName());
      SettlebillItemVO[] VO = settlebillVO.getBodyVO();
      Vector v0 = new Vector();
      for (int i = 0; i < VO.length; i++) {
        String s1 = VO[i].getCinvoiceid();
        String s2 = VO[i].getCinvoice_bid();
        if ((s1 == null) || (s1.length() <= 0) || (s2 == null) || 
          (s2.length() <= 0)) continue;
        BillVO billVO = transferIADataForDirect(invoiceVO[i], 
          sStoreOrgID[i], currDate, VO[i], cOperator);
        if (billVO == null)
          continue;
        v0.addElement(billVO);
      }

      timer.addExecutePhase("VO对照操作");
      if ((v0 == null) || (v0.size() == 0))
        return;
      BillVO[] billVOs = new BillVO[v0.size()];
      v0.copyInto(billVOs);

      Vector v = new Vector();
      Vector vTemp0 = new Vector();
      v.addElement(billVOs[0]);
      vTemp0.addElement(((IBillHeaderVO)billVOs[0].getParentVO())
        .getCbillid().trim());
      for (int i = 1; i < billVOs.length; i++) {
        String s1 = ((IBillHeaderVO)billVOs[i].getParentVO())
          .getCbillid().trim();
        if (!vTemp0.contains(s1)) {
          vTemp0.addElement(s1);
          v.addElement(billVOs[i]);
        }
      }
      timer.addExecutePhase("(1) 存货核算单据来源单据头ID唯一性组合");

      Vector vv = new Vector();
      for (int i = 0; i < v.size(); i++) {
        BillVO tempVO = (BillVO)v.elementAt(i);
        String s1 = ((IBillHeaderVO)tempVO.getParentVO()).getCbillid()
          .trim();
        Vector vTemp = new Vector();
        for (int j = 0; j < billVOs.length; j++) {
          String s2 = ((IBillHeaderVO)billVOs[j].getParentVO())
            .getCbillid().trim();
          if (s1.equals(s2)) {
            IBillItemVO[] tempItemVO = (IBillItemVO[])billVOs[j]
              .getChildrenVO();
            for (int k = 0; k < tempItemVO.length; k++)
              vTemp.addElement(tempItemVO[k]);
          }
        }
        IBillItemVO[] tempItemVOs = new IBillItemVO[vTemp.size()];
        vTemp.copyInto(tempItemVOs);

        for (int j = 0; j < tempItemVOs.length; j++) {
          tempItemVOs[j].setIrownumber(new Integer(j));
          if (tempItemVOs[j].getCsourcebilltypecode().equals("27"))
            tempItemVOs[j].setVsourcebillcode(settlebillVO
              .getHeadVO().getVsettlebillcode());
        }
        tempVO.setChildrenVO(tempItemVOs);
        vv.addElement(tempVO);
      }
      timer.addExecutePhase("(3) 所有存货核算单据体的来源单据头ID相同, 归为同一张存货核算单据");

      billVOs = (BillVO[])null;
      billVOs = new BillVO[vv.size()];
      vv.copyInto(billVOs);

      if ((billVOs == null) || (billVOs.length == 0))
        return;
      String s1 = ((IBillHeaderVO)billVOs[0].getParentVO())
        .getCsourcemodulename();
      String s2 = ((IBillItemVO[])billVOs[0].getChildrenVO())[0]
        .getCsourcebilltypecode();

      String sTime = new UFDateTime(new Date()).toString();
      for (int i = 0; i < billVOs.length; i++) {
        ((IBillHeaderVO)billVOs[i].getParentVO()).setVbillcode(null);
        ((IBillHeaderVO)billVOs[i].getParentVO()).setTlastmaketime(sTime);
        ((IBillHeaderVO)billVOs[i].getParentVO()).setTmaketime(sTime);
        ((IBillHeaderVO)billVOs[i].getParentVO()).setClastoperatorid(cOperator);
        IBillItemVO[] bodyVO = (IBillItemVO[])billVOs[i].getChildrenVO();
        if ((bodyVO != null) && (bodyVO.length > 0)) {
          for (int j = 0; j < bodyVO.length; j++) {
            bodyVO[j].setVbillcode(null);
          }
        }
      }

      myService.saveBillFromOutterArray(billVOs, s1, s2);
      timer.addExecutePhase("向存货传送数据");

      timer.showAllExecutePhase("直运业务类型结算时向存货核算系统传送数据(明细)");
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
  }

  private void saveBillFromOutterForFee(String sDiffMode, SettlebillVO settlebillVO, String cOperator, UFDate dCurrDate)
    throws BusinessException
  {
    IIAToPUBill myService = null;

    Timer timer = new Timer();
    timer.start();
    try
    {
      SettlebillItemVO[] VOs = settlebillVO.getBodyVO();

      GeneralHHeaderVO[] stockHeadVO = getStockHeadInfo(VOs);
      GeneralHItemVO[] stockBodyVO = getStockBodyInfo(VOs);
      if ((stockHeadVO == null) || (stockHeadVO.length == 0))
        return;
      if ((stockBodyVO == null) || (stockBodyVO.length == 0)) {
        return;
      }
      timer.addExecutePhase("批次获得入库单头/体信息");

      myService = (IIAToPUBill)NCLocator.getInstance().lookup(
        IIAToPUBill.class.getName());
      int nCount = 0;

      timer.addExecutePhase("批次获得入库单头/体信息");

      Vector v0 = new Vector();
      for (int i = 0; i < VOs.length; i++) {
        String stockID = VOs[i].getCstockid();
        String stockRow = VOs[i].getCstockrow();
        if ((stockID == null) || (stockID.length() <= 0) || (stockRow == null) || 
          (stockRow.length() <= 0)) continue;
        BillVO[] billVOs = transferIADataForFee(
          stockHeadVO[nCount], stockBodyVO[nCount], 
          sDiffMode, VOs[i], cOperator, dCurrDate, 
          stockHeadVO[nCount].getCstoreorganization());
        nCount++;
        if ((billVOs == null) || (billVOs.length == 0))
          continue;
        for (int j = 0; j < billVOs.length; j++) {
          v0.addElement(billVOs[j]);
        }
      }
      timer.addExecutePhase("VO对照");

      if ((v0 == null) || (v0.size() == 0))
        return;
      BillVO[] billVOs = new BillVO[v0.size()];
      v0.copyInto(billVOs);

      billVOs = new SettleDMO().postDealBillVO(settlebillVO.getHeadVO().getPk_corp(), stockBodyVO, billVOs);
      if ((billVOs == null) || (billVOs.length == 0)) return;

      Vector v = new Vector();
      Vector vTemp0 = new Vector();
      v.addElement(billVOs[0]);
      vTemp0.addElement(((IBillHeaderVO)billVOs[0].getParentVO())
        .getCbillid().trim());
      for (int i = 1; i < billVOs.length; i++) {
        String s1 = ((IBillHeaderVO)billVOs[i].getParentVO())
          .getCbillid().trim();
        if (!vTemp0.contains(s1)) {
          vTemp0.addElement(s1);
          v.addElement(billVOs[i]);
        }
      }
      timer.addExecutePhase("(1) 存货核算单据来源单据头ID唯一性组合");

      Vector vv = new Vector();
      for (int i = 0; i < v.size(); i++) {
        BillVO tempVO = (BillVO)v.elementAt(i);
        String s1 = ((IBillHeaderVO)tempVO.getParentVO()).getCbillid()
          .trim();
        Vector vTemp = new Vector();
        for (int j = 0; j < billVOs.length; j++) {
          String s2 = ((IBillHeaderVO)billVOs[j].getParentVO())
            .getCbillid().trim();
          if (s1.equals(s2)) {
            IBillItemVO[] tempItemVO = (IBillItemVO[])billVOs[j]
              .getChildrenVO();
            for (int k = 0; k < tempItemVO.length; k++)
              vTemp.addElement(tempItemVO[k]);
          }
        }
        IBillItemVO[] tempItemVOs = new IBillItemVO[vTemp.size()];
        vTemp.copyInto(tempItemVOs);

        for (int j = 0; j < tempItemVOs.length; j++) {
          tempItemVOs[j].setIrownumber(new Integer(j));
          if (tempItemVOs[j].getCsourcebilltypecode().equals("27"))
            tempItemVOs[j].setVsourcebillcode(settlebillVO
              .getHeadVO().getVsettlebillcode());
        }
        tempVO.setChildrenVO(tempItemVOs);
        vv.addElement(tempVO);
      }
      timer.addExecutePhase("(3) 所有存货核算单据体的来源单据头ID相同, 归为同一张存货核算单据");

      billVOs = (BillVO[])null;
      billVOs = new BillVO[vv.size()];
      vv.copyInto(billVOs);
      if ((billVOs == null) || (billVOs.length == 0))
        return;
      String s1 = ((IBillHeaderVO)billVOs[0].getParentVO())
        .getCsourcemodulename();
      String s2 = ((IBillItemVO[])billVOs[0].getChildrenVO())[0]
        .getCsourcebilltypecode();

      String sTime = new UFDateTime(new Date()).toString();
      for (int i = 0; i < billVOs.length; i++) {
        ((IBillHeaderVO)billVOs[i].getParentVO()).setVbillcode(null);
        ((IBillHeaderVO)billVOs[i].getParentVO()).setTlastmaketime(sTime);
        ((IBillHeaderVO)billVOs[i].getParentVO()).setTmaketime(sTime);
        ((IBillHeaderVO)billVOs[i].getParentVO()).setClastoperatorid(cOperator);
        IBillItemVO[] bodyVO = (IBillItemVO[])billVOs[i].getChildrenVO();
        if ((bodyVO != null) && (bodyVO.length > 0)) {
          for (int j = 0; j < bodyVO.length; j++) {
            bodyVO[j].setVbillcode(null);
          }
        }

      }

      myService.saveBillFromOutterArray(billVOs, s1, s2);
      timer.addExecutePhase("(4) 整理并向存货传送的单据");

      timer.showAllExecutePhase("费用单独结算时向存货核算系统传送数据--明细");
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
  }

  private GeneralBillVO[] transferARAPDataNone(StockVO[] VOs, SettlebillItemVO[] settlebillVOs, String cOperator, UFDate dCurrDate)
    throws BusinessException
  {
    Vector vTemp = new Vector();
    Vector vTemp0 = new Vector();

    for (int i = 0; i < VOs.length; i++) {
      vTemp.addElement(VOs[i].getCgeneralbid());
      vTemp0.addElement(VOs[i].getCgeneralhid());
    }
    String[] sKeys = new String[vTemp.size()];
    vTemp.copyInto(sKeys);
    String[] sKeys0 = new String[vTemp0.size()];
    vTemp0.copyInto(sKeys0);
    GeneralHItemVO[] itemVO = (GeneralHItemVO[])null;
    GeneralHHeaderVO[] headerVO = (GeneralHHeaderVO[])null;

    Timer timer = new Timer();
    timer.start();
    try
    {
      EstimateDMO dmo = new EstimateDMO();
      itemVO = dmo.queryStockBodyForIA(sKeys);
      timer.addExecutePhase("查询入库单表体");
      headerVO = dmo.queryStockHeadForIA(sKeys0);
      timer.addExecutePhase("查询入库单表头");
    }
    catch (Exception e) {
      SCMEnv.out(e);
      return null;
    }
    if ((itemVO == null) || (itemVO.length == 0))
      return null;
    if ((headerVO == null) || (headerVO.length == 0))
      return null;
    vTemp = new Vector();
    for (int i = 0; i < VOs.length; i++)
    {
      if ((VOs[i].getFlargess() != null) && 
        (VOs[i].getFlargess().booleanValue()))
      {
        continue;
      }
      GeneralBillHeaderVO headVO = new GeneralBillHeaderVO();
      headVO.setCgeneralhid(VOs[i].getCgeneralhid());
      headVO.setCbiztypeid(VOs[i].getCbiztype());
      headVO.setCproviderid(VOs[i].getCprovidermangid());
      headVO.setPk_corp(VOs[i].getPk_corp());
      headVO.setDbilldate(dCurrDate);
      headVO.setCoperatorid(cOperator);
      headVO.setCbilltypecode(VOs[i].getCbilltype());

      headVO.setVuserdef1(headerVO[0].getVuserdef1());
      headVO.setVuserdef2(headerVO[0].getVuserdef2());
      headVO.setVuserdef3(headerVO[0].getVuserdef3());
      headVO.setVuserdef4(headerVO[0].getVuserdef4());
      headVO.setVuserdef5(headerVO[0].getVuserdef5());
      headVO.setVuserdef6(headerVO[0].getVuserdef6());
      headVO.setVuserdef7(headerVO[0].getVuserdef7());
      headVO.setVuserdef8(headerVO[0].getVuserdef8());
      headVO.setVuserdef9(headerVO[0].getVuserdef9());
      headVO.setVuserdef10(headerVO[0].getVuserdef10());

      headVO.setAttributeValue("vuserdef11", headerVO[0].getVuserdef11());
      headVO.setAttributeValue("vuserdef12", headerVO[0].getVuserdef12());
      headVO.setAttributeValue("vuserdef13", headerVO[0].getVuserdef13());
      headVO.setAttributeValue("vuserdef14", headerVO[0].getVuserdef14());
      headVO.setAttributeValue("vuserdef15", headerVO[0].getVuserdef15());
      headVO.setAttributeValue("vuserdef16", headerVO[0].getVuserdef16());
      headVO.setAttributeValue("vuserdef17", headerVO[0].getVuserdef17());
      headVO.setAttributeValue("vuserdef18", headerVO[0].getVuserdef18());
      headVO.setAttributeValue("vuserdef19", headerVO[0].getVuserdef19());
      headVO.setAttributeValue("vuserdef20", headerVO[0].getVuserdef20());

      headVO.setAttributeValue("pk_defdoc1", headerVO[0].getPk_defdoc1());
      headVO.setAttributeValue("pk_defdoc2", headerVO[0].getPk_defdoc2());
      headVO.setAttributeValue("pk_defdoc3", headerVO[0].getPk_defdoc3());
      headVO.setAttributeValue("pk_defdoc4", headerVO[0].getPk_defdoc4());
      headVO.setAttributeValue("pk_defdoc5", headerVO[0].getPk_defdoc5());
      headVO.setAttributeValue("pk_defdoc6", headerVO[0].getPk_defdoc6());
      headVO.setAttributeValue("pk_defdoc7", headerVO[0].getPk_defdoc7());
      headVO.setAttributeValue("pk_defdoc8", headerVO[0].getPk_defdoc8());
      headVO.setAttributeValue("pk_defdoc9", headerVO[0].getPk_defdoc9());
      headVO.setAttributeValue("pk_defdoc10", headerVO[0].getPk_defdoc10());

      headVO.setAttributeValue("pk_defdoc11", headerVO[0].getPk_defdoc11());
      headVO.setAttributeValue("pk_defdoc12", headerVO[0].getPk_defdoc12());
      headVO.setAttributeValue("pk_defdoc13", headerVO[0].getPk_defdoc13());
      headVO.setAttributeValue("pk_defdoc14", headerVO[0].getPk_defdoc14());
      headVO.setAttributeValue("pk_defdoc15", headerVO[0].getPk_defdoc15());
      headVO.setAttributeValue("pk_defdoc16", headerVO[0].getPk_defdoc16());
      headVO.setAttributeValue("pk_defdoc17", headerVO[0].getPk_defdoc17());
      headVO.setAttributeValue("pk_defdoc18", headerVO[0].getPk_defdoc18());
      headVO.setAttributeValue("pk_defdoc19", headerVO[0].getPk_defdoc19());
      headVO.setAttributeValue("pk_defdoc20", headerVO[0].getPk_defdoc20());

      headVO.setCdptid(VOs[i].getCdeptid());
      headVO.setCbizid(VOs[i].getCbizid());

      GeneralBillItemVO bodyVO = new GeneralBillItemVO();
      bodyVO.setCgeneralhid(VOs[i].getCgeneralhid());
      bodyVO.setCgeneralbid(VOs[i].getCgeneralbid());
      bodyVO.setCinventoryid(VOs[i].getCmangid());
      bodyVO.setNinnum(settlebillVOs[i].getNsettlenum());
      bodyVO.setNprice(settlebillVOs[i].getNprice());
      bodyVO.setNmny(settlebillVOs[i].getNmoney());
      bodyVO.setCsourcetype(itemVO[i].getCsourcetype());
      bodyVO.setCsourcebillhid(itemVO[i].getCsourcebillhid());
      bodyVO.setCsourcebillbid(itemVO[i].getCsourcebillbid());
      bodyVO.setCfirsttype(itemVO[i].getCfirsttype());
      bodyVO.setCfirstbillhid(itemVO[i].getCfirstbillhid());
      bodyVO.setCfirstbillbid(itemVO[i].getCfirstbillbid());
      bodyVO.setCprojectid(itemVO[i].getCprojectid());
      bodyVO.setCprojectphaseid(itemVO[i].getCprojectphaseid());

      bodyVO.setVuserdef1(itemVO[i].getVuserdef1());
      bodyVO.setVuserdef2(itemVO[i].getVuserdef2());
      bodyVO.setVuserdef3(itemVO[i].getVuserdef3());
      bodyVO.setVuserdef4(itemVO[i].getVuserdef4());
      bodyVO.setVuserdef5(itemVO[i].getVuserdef5());
      bodyVO.setVuserdef6(itemVO[i].getVuserdef6());
      bodyVO.setVuserdef7(itemVO[i].getVuserdef7());
      bodyVO.setVuserdef8(itemVO[i].getVuserdef8());
      bodyVO.setVuserdef9(itemVO[i].getVuserdef9());
      bodyVO.setVuserdef10(itemVO[i].getVuserdef10());

      bodyVO.setAttributeValue("vuserdef11", itemVO[i].getVuserdef11());
      bodyVO.setAttributeValue("vuserdef12", itemVO[i].getVuserdef12());
      bodyVO.setAttributeValue("vuserdef13", itemVO[i].getVuserdef13());
      bodyVO.setAttributeValue("vuserdef14", itemVO[i].getVuserdef14());
      bodyVO.setAttributeValue("vuserdef15", itemVO[i].getVuserdef15());
      bodyVO.setAttributeValue("vuserdef16", itemVO[i].getVuserdef16());
      bodyVO.setAttributeValue("vuserdef17", itemVO[i].getVuserdef17());
      bodyVO.setAttributeValue("vuserdef18", itemVO[i].getVuserdef18());
      bodyVO.setAttributeValue("vuserdef19", itemVO[i].getVuserdef19());
      bodyVO.setAttributeValue("vuserdef20", itemVO[i].getVuserdef20());

      bodyVO.setAttributeValue("pk_defdoc1", itemVO[i].getPk_defdoc1());
      bodyVO.setAttributeValue("pk_defdoc2", itemVO[i].getPk_defdoc2());
      bodyVO.setAttributeValue("pk_defdoc3", itemVO[i].getPk_defdoc3());
      bodyVO.setAttributeValue("pk_defdoc4", itemVO[i].getPk_defdoc4());
      bodyVO.setAttributeValue("pk_defdoc5", itemVO[i].getPk_defdoc5());
      bodyVO.setAttributeValue("pk_defdoc6", itemVO[i].getPk_defdoc6());
      bodyVO.setAttributeValue("pk_defdoc7", itemVO[i].getPk_defdoc7());
      bodyVO.setAttributeValue("pk_defdoc8", itemVO[i].getPk_defdoc8());
      bodyVO.setAttributeValue("pk_defdoc9", itemVO[i].getPk_defdoc9());
      bodyVO.setAttributeValue("pk_defdoc10", itemVO[i].getPk_defdoc10());

      bodyVO.setAttributeValue("pk_defdoc11", itemVO[i].getPk_defdoc11());
      bodyVO.setAttributeValue("pk_defdoc12", itemVO[i].getPk_defdoc12());
      bodyVO.setAttributeValue("pk_defdoc13", itemVO[i].getPk_defdoc13());
      bodyVO.setAttributeValue("pk_defdoc14", itemVO[i].getPk_defdoc14());
      bodyVO.setAttributeValue("pk_defdoc15", itemVO[i].getPk_defdoc15());
      bodyVO.setAttributeValue("pk_defdoc16", itemVO[i].getPk_defdoc16());
      bodyVO.setAttributeValue("pk_defdoc17", itemVO[i].getPk_defdoc17());
      bodyVO.setAttributeValue("pk_defdoc18", itemVO[i].getPk_defdoc18());
      bodyVO.setAttributeValue("pk_defdoc19", itemVO[i].getPk_defdoc19());
      bodyVO.setAttributeValue("pk_defdoc20", itemVO[i].getPk_defdoc20());

      bodyVO.setDbizdate(itemVO[i].getDbizdate());
      GeneralBillVO VO = new GeneralBillVO();
      VO.setParentVO(headVO);
      VO.setChildrenVO(new GeneralBillItemVO[] { bodyVO });
      if ((bodyVO.getNprice() == null) || 
        (Math.abs(bodyVO.getNprice().doubleValue()) <= 0.0D)) continue;
      vTemp.addElement(VO);
    }
    timer.addExecutePhase("VO对照");

    timer.showAllExecutePhase("入库单VO，结算单体VO转化为标准入库单VO(明细)");

    if (vTemp.size() > 0) {
      GeneralBillVO[] tempVOs = new GeneralBillVO[vTemp.size()];
      vTemp.copyInto(tempVOs);
      return tempVOs;
    }
    return null;
  }

  private InvoiceVO switchVOForDirectInvSettle(IinvoiceVO invVO)
  {
    InvoiceHeaderVO headVO = new InvoiceHeaderVO();

    headVO.setPk_corp(invVO.getPk_corp());
    headVO.setVinvoicecode(invVO.getVinvoicecode());
    headVO.setCbiztype(invVO.getCbiztype());
    headVO.setCdeptid(invVO.getCdeptid());
    headVO.setCvendorbaseid(invVO.getCvendorbaseid());
    headVO.setCvendormangid(invVO.getCvendormangid());
    headVO.setCstoreorganization(invVO.getPk_calbody());

    headVO.setVdef1(invVO.getVhdef1());
    headVO.setVdef2(invVO.getVhdef2());
    headVO.setVdef3(invVO.getVhdef3());
    headVO.setVdef4(invVO.getVhdef4());
    headVO.setVdef5(invVO.getVhdef5());
    headVO.setVdef6(invVO.getVhdef6());
    headVO.setVdef7(invVO.getVhdef7());
    headVO.setVdef8(invVO.getVhdef8());
    headVO.setVdef9(invVO.getVhdef9());
    headVO.setVdef10(invVO.getVhdef10());

    headVO.setVdef11(invVO.getVhdef11());
    headVO.setVdef12(invVO.getVhdef12());
    headVO.setVdef13(invVO.getVhdef13());
    headVO.setVdef14(invVO.getVhdef14());
    headVO.setVdef15(invVO.getVhdef15());
    headVO.setVdef16(invVO.getVhdef16());
    headVO.setVdef17(invVO.getVhdef17());
    headVO.setVdef18(invVO.getVhdef18());
    headVO.setVdef19(invVO.getVhdef19());
    headVO.setVdef20(invVO.getVhdef20());

    headVO.setPKDefDoc1(invVO.getPk_defdoch1());
    headVO.setPKDefDoc2(invVO.getPk_defdoch2());
    headVO.setPKDefDoc3(invVO.getPk_defdoch3());
    headVO.setPKDefDoc4(invVO.getPk_defdoch4());
    headVO.setPKDefDoc5(invVO.getPk_defdoch5());
    headVO.setPKDefDoc6(invVO.getPk_defdoch6());
    headVO.setPKDefDoc7(invVO.getPk_defdoch7());
    headVO.setPKDefDoc8(invVO.getPk_defdoch8());
    headVO.setPKDefDoc9(invVO.getPk_defdoch9());
    headVO.setPKDefDoc10(invVO.getPk_defdoch10());

    headVO.setPKDefDoc11(invVO.getPk_defdoch11());
    headVO.setPKDefDoc12(invVO.getPk_defdoch12());
    headVO.setPKDefDoc13(invVO.getPk_defdoch13());
    headVO.setPKDefDoc14(invVO.getPk_defdoch14());
    headVO.setPKDefDoc15(invVO.getPk_defdoch15());
    headVO.setPKDefDoc16(invVO.getPk_defdoch16());
    headVO.setPKDefDoc17(invVO.getPk_defdoch17());
    headVO.setPKDefDoc18(invVO.getPk_defdoch18());
    headVO.setPKDefDoc19(invVO.getPk_defdoch19());
    headVO.setPKDefDoc20(invVO.getPk_defdoch20());

    InvoiceItemVO bodyVO = new InvoiceItemVO();
    bodyVO.setPk_corp(invVO.getPk_corp());
    bodyVO.setCmangid(invVO.getCmangid());
    bodyVO.setCbaseid(invVO.getCbaseid());
    bodyVO.setVproducenum(invVO.getVbatchcode());
    bodyVO.setCprojectid(invVO.getCprojectid());
    bodyVO.setCprojectphaseid(invVO.getCprojectphaseid());

    bodyVO.setVfree1(invVO.getVfree1());
    bodyVO.setVfree2(invVO.getVfree2());
    bodyVO.setVfree3(invVO.getVfree3());
    bodyVO.setVfree4(invVO.getVfree4());
    bodyVO.setVfree5(invVO.getVfree5());

    bodyVO.setVdef1(invVO.getVbdef1());
    bodyVO.setVdef2(invVO.getVbdef2());
    bodyVO.setVdef3(invVO.getVbdef3());
    bodyVO.setVdef4(invVO.getVbdef4());
    bodyVO.setVdef5(invVO.getVbdef5());
    bodyVO.setVdef6(invVO.getVbdef6());
    bodyVO.setVdef7(invVO.getVbdef7());
    bodyVO.setVdef8(invVO.getVbdef8());
    bodyVO.setVdef9(invVO.getVbdef9());
    bodyVO.setVdef10(invVO.getVbdef10());

    bodyVO.setVdef11(invVO.getVbdef11());
    bodyVO.setVdef12(invVO.getVbdef12());
    bodyVO.setVdef13(invVO.getVbdef13());
    bodyVO.setVdef14(invVO.getVbdef14());
    bodyVO.setVdef15(invVO.getVbdef15());
    bodyVO.setVdef16(invVO.getVbdef16());
    bodyVO.setVdef17(invVO.getVbdef17());
    bodyVO.setVdef18(invVO.getVbdef18());
    bodyVO.setVdef19(invVO.getVbdef19());
    bodyVO.setVdef20(invVO.getVbdef20());

    bodyVO.setPKDefDoc1(invVO.getPk_defdocb1());
    bodyVO.setPKDefDoc2(invVO.getPk_defdocb2());
    bodyVO.setPKDefDoc3(invVO.getPk_defdocb3());
    bodyVO.setPKDefDoc4(invVO.getPk_defdocb4());
    bodyVO.setPKDefDoc5(invVO.getPk_defdocb5());
    bodyVO.setPKDefDoc6(invVO.getPk_defdocb6());
    bodyVO.setPKDefDoc7(invVO.getPk_defdocb7());
    bodyVO.setPKDefDoc8(invVO.getPk_defdocb8());
    bodyVO.setPKDefDoc9(invVO.getPk_defdocb9());
    bodyVO.setPKDefDoc10(invVO.getPk_defdocb10());

    bodyVO.setPKDefDoc11(invVO.getPk_defdocb11());
    bodyVO.setPKDefDoc12(invVO.getPk_defdocb12());
    bodyVO.setPKDefDoc13(invVO.getPk_defdocb13());
    bodyVO.setPKDefDoc14(invVO.getPk_defdocb14());
    bodyVO.setPKDefDoc15(invVO.getPk_defdocb15());
    bodyVO.setPKDefDoc16(invVO.getPk_defdocb16());
    bodyVO.setPKDefDoc17(invVO.getPk_defdocb17());
    bodyVO.setPKDefDoc18(invVO.getPk_defdocb18());
    bodyVO.setPKDefDoc19(invVO.getPk_defdocb19());
    bodyVO.setPKDefDoc20(invVO.getPk_defdocb20());

    InvoiceVO VO = new InvoiceVO(1);
    VO.setParentVO(headVO);
    VO.setChildrenVO(new InvoiceItemVO[] { bodyVO });
    return VO;
  }

  private BillVO transferIADataForDirect(IinvoiceVO invoiceVO, String cStoreOrgID, UFDate currDate, SettlebillItemVO VO, String cOperator)
    throws BusinessException
  {
    BillVO billVO = null;
    try
    {
      InvoiceVO invVO = switchVOForDirectInvSettle(invoiceVO);
      billVO = (BillVO)PfUtilTools.runChangeData("25", "I2", invVO);
      if (billVO == null) return null;

      EstimateDMO dmo = new EstimateDMO();

      UFDouble nSettleNum = VO.getNsettlenum();
      UFDouble nSettleMny = VO.getNmoney();

      BillHeaderVO tempheadVO = (BillHeaderVO)billVO.getParentVO();
      IBillHeaderVO headVO = new IBillHeaderVO();
      String[] s = tempheadVO.getAttributeNames();
      for (int i = 0; i < s.length; i++) headVO.setAttributeValue(s[i], tempheadVO.getAttributeValue(s[i]));

      headVO.setCbilltypecode("I2");
      headVO.setCbillid(invoiceVO.getCinvoiceid());

      headVO.setDbilldate(currDate);
      headVO.setCsourcemodulename("PO");
      headVO.setFdispatchflag(new Integer(0));

      String ss = dmo.queryRSMode(invoiceVO.getCbiztype());
      if ((ss != null) && (ss.length() > 0))
        headVO.setCdispatchid(ss);
      else {
        headVO.setCdispatchid(null);
      }
      headVO.setCrdcenterid(cStoreOrgID);
      headVO.setCoperatorid(cOperator);

      headVO.setBestimateflag(new UFBoolean(false));
      headVO.setCemployeeid(invoiceVO.getCoperatorid());

      headVO.setVbillcode(invoiceVO.getVinvoicecode());
      headVO.setBwithdrawalflag(new UFBoolean(false));
      headVO.setBdisableflag(new UFBoolean(false));
      headVO.setBauditedflag(new UFBoolean(false));

      BillItemVO tempbodyVO = ((BillItemVO[])billVO.getChildrenVO())[0];
      IBillItemVO bodyVO = new IBillItemVO();
      s = tempbodyVO.getAttributeNames();
      for (int i = 0; i < s.length; i++) bodyVO.setAttributeValue(s[i], tempbodyVO.getAttributeValue(s[i]));

      bodyVO.setCbilltypecode("I2");
      bodyVO.setCbill_bid(invoiceVO.getCinvoice_bid());
      bodyVO.setCbillid(invoiceVO.getCinvoiceid());

      bodyVO.setCsourcebillid(VO.getCsettlebillid());
      bodyVO.setCsourcebillitemid(VO.getCsettlebill_bid());
      bodyVO.setCsourcebilltypecode("27");

      bodyVO.setNnumber(nSettleNum);
      bodyVO.setNmoney(nSettleMny);
      if ((nSettleNum != null) && (nSettleMny != null) && 
        (Math.abs(nSettleNum.doubleValue()) >= 0.0D)) {
        double d = nSettleMny.doubleValue() / nSettleNum.doubleValue();
        bodyVO.setNprice(new UFDouble(d));
      }

      bodyVO.setIrownumber(new Integer(0));
      bodyVO.setVbillcode(invoiceVO.getVinvoicecode());
      bodyVO.setDbizdate(invoiceVO.getDarrivedate());

      billVO = new BillVO();
      billVO.setParentVO(headVO);

      Vector vTemp = new Vector();
      vTemp.addElement(bodyVO);
      IBillItemVO[] tempBodyVO = new IBillItemVO[1];
      vTemp.copyInto(tempBodyVO);

      billVO.setChildrenVO(tempBodyVO);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }

    return billVO;
  }

  private Vector convertVOForIA(GeneralHHeaderVO stockHeadVO, GeneralHItemVO stockBodyVO, String settleID, String settleRow, String cOperator, UFDate dCurrDate, String cCalbody)
    throws BusinessException
  {
    Vector vRslt = null;
    try
    {
      GeneralBillVO icVO = switchVOFromGeneral2IC(stockHeadVO, stockBodyVO);
      BillVO billVO = (BillVO)PfUtilTools.runChangeData("45", "I2", icVO);
      if (billVO == null) return null;

      GeneralHHeaderVO headerVO = stockHeadVO;

      BillHeaderVO tempheadVO = (BillHeaderVO)billVO.getParentVO();
      IBillHeaderVO headVO = new IBillHeaderVO();
      String[] s = tempheadVO.getAttributeNames();
      for (int i = 0; i < s.length; i++) headVO.setAttributeValue(s[i], tempheadVO.getAttributeValue(s[i]));

      headVO.setCbillid(headerVO.getCgeneralhid());
      headVO.setDbilldate(dCurrDate);
      headVO.setCsourcemodulename("PO");
      headVO.setFdispatchflag(new Integer(0));

      headVO.setCoperatorid(cOperator);
      headVO.setBestimateflag(new UFBoolean(false));
      headVO.setCwarehousemanagerid(headerVO.getCwhsmanagerid());
      headVO.setCemployeeid(headerVO.getCbizid());

      headVO.setVbillcode(headerVO.getVbillcode());
      headVO.setBwithdrawalflag(new UFBoolean(false));
      headVO.setBdisableflag(new UFBoolean(false));
      headVO.setBauditedflag(new UFBoolean(false));

      headVO.setCstockrdcenterid(null);

      GeneralHItemVO itemVO = stockBodyVO;

      BillItemVO tempbodyVO = ((BillItemVO[])billVO.getChildrenVO())[0];
      IBillItemVO bodyVO = new IBillItemVO();
      s = tempbodyVO.getAttributeNames();
      for (int i = 0; i < s.length; i++) bodyVO.setAttributeValue(s[i], tempbodyVO.getAttributeValue(s[i]));

      bodyVO.setPk_corp(headVO.getPk_corp());
      bodyVO.setCbill_bid(itemVO.getCgeneralbid());
      bodyVO.setCbillid(itemVO.getCgeneralhid());

      bodyVO.setCicbilltype(headerVO.getCbilltypecode());
      bodyVO.setCicbillcode(headerVO.getVbillcode());
      bodyVO.setCicbillid(itemVO.getCgeneralhid());
      bodyVO.setCicitemid(itemVO.getCgeneralbid());

      bodyVO.setCsourcebillid(settleID);
      bodyVO.setCsourcebillitemid(settleRow);
      bodyVO.setCsourcebilltypecode("27");

      bodyVO.setCfirstbillid(itemVO.getCfirstbillhid());
      bodyVO.setCfirstbillitemid(itemVO.getCfirstbillbid());
      bodyVO.setCfirstbilltypecode(itemVO.getCfirsttype());

      bodyVO.setIrownumber(new Integer(0));
      bodyVO.setVbillcode(headVO.getVbillcode());
      bodyVO.setVsourcebillcode(headVO.getVbillcode());

      bodyVO.setDbizdate(itemVO.getDbizdate());
      bodyVO.setCvendorid(itemVO.getCvendorid());

      bodyVO.setBlargessflag(itemVO.getFlargess());

      billVO = new BillVO();
      billVO.setParentVO(headVO);
      IBillItemVO[] tempBodyVO = new IBillItemVO[1];
      Vector v = new Vector();
      v.addElement(bodyVO);
      v.copyInto(tempBodyVO);
      billVO.setChildrenVO(tempBodyVO);

      vRslt = new Vector();
      vRslt.addElement(billVO);
      vRslt.addElement(itemVO.getBzgflag());
      vRslt.addElement(headerVO.getCbilltypecode());
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    return vRslt;
  }

  private Vector convertVOForIA1(StockVO stock, String settleID, String settleRow, String cOperator, UFDate dCurrDate, String cCalbody)
    throws BusinessException
  {
    Vector vRslt = null;
    try
    {
      GeneralBillVO icVO = switchVOFromStock2IC(stock);
      BillVO billVO = (BillVO)PfUtilTools.runChangeData("45", "I2", icVO);
      if (billVO == null) return null;

      BillHeaderVO tempheadVO = (BillHeaderVO)billVO.getParentVO();
      IBillHeaderVO headVO = new IBillHeaderVO();
      String[] s = tempheadVO.getAttributeNames();
      for (int i = 0; i < s.length; i++) headVO.setAttributeValue(s[i], tempheadVO.getAttributeValue(s[i]));

      headVO.setCbillid(stock.getCgeneralhid());
      headVO.setDbilldate(dCurrDate);
      headVO.setCsourcemodulename("PO");
      headVO.setFdispatchflag(new Integer(0));

      headVO.setCoperatorid(cOperator);
      headVO.setBestimateflag(new UFBoolean(false));
      headVO.setCwarehousemanagerid(stock.getCwhsmanagerid());
      headVO.setCemployeeid(stock.getCbizid());

      headVO.setVbillcode(stock.getVbillcode());
      headVO.setBwithdrawalflag(new UFBoolean(false));
      headVO.setBdisableflag(new UFBoolean(false));
      headVO.setBauditedflag(new UFBoolean(false));

      headVO.setCstockrdcenterid(null);

      BillItemVO tempbodyVO = ((BillItemVO[])billVO.getChildrenVO())[0];
      IBillItemVO bodyVO = new IBillItemVO();
      s = tempbodyVO.getAttributeNames();
      for (int i = 0; i < s.length; i++) bodyVO.setAttributeValue(s[i], tempbodyVO.getAttributeValue(s[i]));

      bodyVO.setPk_corp(headVO.getPk_corp());
      bodyVO.setCbill_bid(stock.getCgeneralbid());
      bodyVO.setCbillid(stock.getCgeneralhid());

      bodyVO.setCsourcebillid(settleID);
      bodyVO.setCsourcebillitemid(settleRow);
      bodyVO.setCsourcebilltypecode("27");

      bodyVO.setCfirstbillid(stock.getCfirstbillhid());
      bodyVO.setCfirstbillitemid(stock.getCfirstbillbid());
      bodyVO.setCfirstbilltypecode(stock.getCfirsttype());

      bodyVO.setIrownumber(new Integer(0));
      bodyVO.setVbillcode(headVO.getVbillcode());
      bodyVO.setVsourcebillcode(headVO.getVbillcode());

      bodyVO.setCicbilltype(stock.getCbilltype());
      bodyVO.setCicbillcode(stock.getVbillcode());
      bodyVO.setCicbillid(stock.getCgeneralhid());
      bodyVO.setCicitemid(stock.getCgeneralbid());

      bodyVO.setDbizdate(stock.getDbizdate());

      bodyVO.setBlargessflag(stock.getFlargess());
      bodyVO.setCvendorid(stock.getCvendorid());

      billVO = new BillVO();
      billVO.setParentVO(headVO);
      IBillItemVO[] tempBodyVO = new IBillItemVO[1];
      Vector v = new Vector();
      v.addElement(bodyVO);
      v.copyInto(tempBodyVO);
      billVO.setChildrenVO(tempBodyVO);

      vRslt = new Vector();
      vRslt.addElement(billVO);
      vRslt.addElement(stock.getBzgflag());
      vRslt.addElement(stock.getCbilltype());
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    return vRslt;
  }

  public ArrayList doOrderToICSettle(GeneralBillVO[] VO)
    throws BusinessException
  {
    ArrayList listLockId = null;
    String sEstMode = null;
    String sDiffMode = null;
    SettlebillVO voBillCode = null;
    boolean bGetBillCodeSucc = false;
    boolean bLock = false;

    Timer timer = new Timer();
    timer.start();
    try
    {
      ISysInitQry myService1 = (ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName());
      Hashtable t = myService1.queryBatchParaValues(VO[0]
        .getHeaderVO().getPk_corp(), new String[] { "PO12", "PO13" });
      if ((t == null) || (t.size() == 0))
        return null;
      sEstMode = (String)t.get("PO12");
      sDiffMode = (String)t.get("PO13");

      timer.addExecutePhase("获得系统设置的暂估方式和差异转入方式");

      String vSettlebillcode = "";
      GetSysBillCode billCode = new GetSysBillCode();
      SettlebillHeaderVO head = new SettlebillHeaderVO();
      head.setPk_corp(VO[0].getHeaderVO().getPk_corp());
      voBillCode = new SettlebillVO(1);
      voBillCode.setParentVO(head);
      voBillCode
        .setChildrenVO(new SettlebillItemVO[] { new SettlebillItemVO() });
      do
        vSettlebillcode = billCode.getSysBillNO(voBillCode);
      while (
        isSettlebillCodeDuplicate(
        VO[0].getHeaderVO().getPk_corp(), vSettlebillcode));
      voBillCode.getHeadVO().setVsettlebillcode(vSettlebillcode);
      bGetBillCodeSucc = true;

      timer.addExecutePhase("生成结算单号");

      AutoSettleOrderToIC orderToIC = new AutoSettleOrderToIC(null, 
        vSettlebillcode);
      Vector v = null;
      v = orderToIC.doBalance(VO);

      timer.addExecutePhase("执行结算");

      if ((v != null) && (v.size() > 0)) {
        StockVO[] stockVOs = (StockVO[])v.elementAt(0);
        SettlebillVO settlebillVO = (SettlebillVO)v.elementAt(1);

        if ((stockVOs != null) && (stockVOs.length > 0)) {
          ICreateCorpQueryService myService0 = null;
          myService0 = (ICreateCorpQueryService)
            NCLocator.getInstance().lookup(
            ICreateCorpQueryService.class.getName());
          boolean bIAStartUp = myService0.isEnabled(VO[0]
            .getHeaderVO().getPk_corp(), "IA");
          if (bIAStartUp)
          {
            listLockId = (ArrayList)v.elementAt(2);
            if ((listLockId != null) && (listLockId.size() > 0)) {
              String[] saLockId = new String[listLockId.size()];
              saLockId = (String[])listLockId.toArray(saLockId);
              String strOprId = VO[0].getCurUserID();
              bLock = LockTool.setLockForPks(saLockId, strOprId);
              if (!bLock) {
                listLockId = null;
                throw new Exception(
                  NCLangResOnserver.getInstance().getStrByID("40040502", 
                  "UPP40040502-000038"));
              }

            }

            ArrayList listPara = new ArrayList();
            listPara.add(sEstMode);
            listPara.add(sDiffMode);
            listPara.add(settlebillVO);
            listPara.add(VO[0].getHeaderVO().getCregister());
            listPara.add(VO[0].getHeaderVO().getDaccountdate());
            listPara.add(null);
            listPara.add(stockVOs);
            saveBillFromOutter1(listPara);
          }
        }

      }
      else if (bGetBillCodeSucc) {
        try {
          billCode = new GetSysBillCode();
          billCode.returnBillNo(voBillCode);
        } catch (Exception ex) {
          SCMEnv.out("结算失败,回退结算单号");

          SCMEnv.out(ex);
        }

      }

      timer.addExecutePhase("向存货核算传送数据(总)");

      timer.showAllExecutePhase("基于订单入库时自动结算，入库单签字调用该结算接口时间分布");
    }
    catch (Exception e) {
      SCMEnv.out("向存货核算传送数据失败");

      SCMEnv.out(e);

      if (bGetBillCodeSucc) {
        try {
          GetSysBillCode billCode = new GetSysBillCode();
          billCode.returnBillNo(voBillCode);
        } catch (Exception ex) {
          SCMEnv.out("结算失败,回退结算单号");

          SCMEnv.out(ex);
        }

      }

      if ((bLock) && (listLockId != null) && (listLockId.size() > 0)) {
        String[] saLockId = new String[listLockId.size()];
        saLockId = (String[])listLockId.toArray(saLockId);
        String strOprId = VO[0].getCurUserID();
        try {
          LockTool.releaseLockForPks(saLockId, strOprId);
        } catch (Exception ee) {
          PubDMO.throwBusinessException(ee);
        }
      }
      PubDMO.throwBusinessException(e);
    }

    return listLockId;
  }

  public ArrayList doOrderToInvoiceSettle(InvoiceVO[] VO, ArrayList list)
    throws BusinessException
  {
    ArrayList listLockId = null;
    SettlebillVO voBillCode = null;
    boolean bGetBillCodeSucc = false;
    boolean bLock = false;
    UFBoolean bSucceed = new UFBoolean(false);

    Timer timer = new Timer();
    timer.start();
    try
    {
      String accountYear = VO[0].getHeadVO().getCaccountyear();
      UFDate currentDate = VO[0].getHeadVO().getDinvoicedate();
      String operatorID = null;
      if (VO[0].getHeadVO().getCauditpsn() != null) operatorID = VO[0].getHeadVO().getCauditpsn(); else
        operatorID = VO[0].getHeadVO().getCoperator();
      if ((list != null) && (list.size() > 0)) {
        Object oTemp = list.get(0);
        if (oTemp != null) {
          currentDate = new UFDate(oTemp.toString());
        }
      }

      String sEstMode = null;
      String sDiffMode = null;
      ISysInitQry myService1 = (ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName());
      Hashtable t = myService1.queryBatchParaValues(VO[0]
        .getHeadVO().getPk_corp(), new String[] { "PO12", "PO13" });
      if ((t == null) || (t.size() == 0))
        return null;
      sEstMode = (String)t.get("PO12");
      sDiffMode = (String)t.get("PO13");

      timer.addExecutePhase("获得系统设置的暂估方式和差异转入方式");

      String vSettlebillcode = "";
      GetSysBillCode billCode = new GetSysBillCode();
      SettlebillHeaderVO head = new SettlebillHeaderVO();
      head.setPk_corp(VO[0].getHeadVO().getPk_corp());
      voBillCode = new SettlebillVO(1);
      voBillCode.setParentVO(head);
      voBillCode
        .setChildrenVO(new SettlebillItemVO[] { new SettlebillItemVO() });
      do
        vSettlebillcode = billCode.getSysBillNO(voBillCode);
      while (
        isSettlebillCodeDuplicate(VO[0].getHeadVO().getPk_corp(), 
        vSettlebillcode));
      voBillCode.getHeadVO().setVsettlebillcode(vSettlebillcode);
      bGetBillCodeSucc = true;

      timer.addExecutePhase("生成结算单号");

      AutoSettleOrderToInvoice orderToInvoice = new AutoSettleOrderToInvoice(
        accountYear, currentDate, operatorID, vSettlebillcode);
      Vector v = null;
      v = orderToInvoice.doBalance(VO);

      timer.addExecutePhase("执行结算");

      if ((v != null) && (v.size() > 0)) {
        bSucceed = new UFBoolean(true);
        StockVO[] stockVOs = (StockVO[])v.elementAt(0);
        SettlebillVO settlebillVO = (SettlebillVO)v.elementAt(1);

        if ((stockVOs != null) && (stockVOs.length > 0))
        {
          Vector vNotNull = new Vector();
          int iLenSto = stockVOs.length;
          for (int i = 0; i < iLenSto; i++) {
            if (stockVOs[i] != null)
              vNotNull.addElement(stockVOs[i]);
          }
          if (vNotNull.size() > 0) {
            stockVOs = new StockVO[vNotNull.size()];
            vNotNull.copyInto(stockVOs);
          } else {
            stockVOs = (StockVO[])null;
          }
          ICreateCorpQueryService myService0 = null;
          myService0 = (ICreateCorpQueryService)
            NCLocator.getInstance().lookup(
            ICreateCorpQueryService.class.getName());
          boolean bIAStartUp = myService0.isEnabled(VO[0].getHeadVO()
            .getPk_corp(), "IA");
          if (bIAStartUp)
          {
            listLockId = (ArrayList)v.elementAt(2);
            if ((listLockId != null) && (listLockId.size() > 0)) {
              String[] saLockId = new String[listLockId.size()];
              saLockId = (String[])listLockId.toArray(saLockId);
              String strOprId = VO[0].getHeadVO().getCuserid();
              bLock = LockTool.setLockForPks(saLockId, strOprId);
              if (!bLock) {
                listLockId = null;
                throw new Exception(
                  NCLangResOnserver.getInstance().getStrByID("40040502", 
                  "UPP40040502-000039"));
              }

            }

            ArrayList listPara = new ArrayList();
            listPara.add(sEstMode);
            listPara.add(sDiffMode);
            listPara.add(settlebillVO);
            listPara.add(operatorID);
            listPara.add(currentDate);
            listPara.add(null);
            listPara.add(stockVOs);
            saveBillFromOutter1(listPara);
          }
        }

      }
      else if (bGetBillCodeSucc) {
        try {
          GetSysBillCode billCode1 = new GetSysBillCode();
          billCode1.returnBillNo(voBillCode);
        }
        catch (Exception ex) {
          SCMEnv.out(ex);
        }

      }

      timer.addExecutePhase("向存货核算传送数据(总)");

      timer.showAllExecutePhase("基于订单接收发票自动结算时，发票调用该结算接口（除直运业务类型外）时间分布");
    }
    catch (Exception e)
    {
      SCMEnv.out(e);

      if (bGetBillCodeSucc) {
        try {
          GetSysBillCode billCode = new GetSysBillCode();
          billCode.returnBillNo(voBillCode);
        }
        catch (Exception ex) {
          SCMEnv.out(ex);
        }

      }

      if ((bLock) && (listLockId != null) && (listLockId.size() > 0)) {
        String[] saLockId = new String[listLockId.size()];
        saLockId = (String[])listLockId.toArray(saLockId);
        String strOprId = VO[0].getHeadVO().getCuserid();
        try {
          LockTool.releaseLockForPks(saLockId, strOprId);
        } catch (Exception ee) {
          PubDMO.throwBusinessException(ee);
        }
      }
      PubDMO.throwBusinessException(e);
    }

    ArrayList list0 = new ArrayList();
    list0.add(listLockId);
    list0.add(bSucceed);
    return list0;
  }

  private BillVO[] transferIAData(GeneralHHeaderVO stockHeadVO, GeneralHItemVO stockBodyVO, String sZGmode, String sCYZRmode, SettlebillItemVO VO, String cOperator, UFDate dCurrDate, GeneralHItemVO[] generalHItemVOs, UFDouble nAssistNum, String cCalbody, ArrayList tsList)
    throws BusinessException
  {
    Vector vReturn = new Vector();
    String strPkCorp = null;
    try {
      strPkCorp = stockHeadVO.getPk_corp();

      Vector v = convertVOForIA(stockHeadVO, stockBodyVO, VO
        .getCsettlebillid(), VO.getCsettlebill_bid(), cOperator, 
        dCurrDate, cCalbody);
      UFDouble nSettleNum = VO.getNsettlenum();
      UFDouble nSettleMny = VO.getNmoney();
      UFDouble nGaugeMny = VO.getNgaugemny();
      UFDouble nPrice = VO.getNprice();
      BillVO billVO = (BillVO)v.elementAt(0);
      IBillHeaderVO headerVO = (IBillHeaderVO)billVO.getParentVO();
      IBillItemVO[] itemVO = (IBillItemVO[])billVO.getChildrenVO();
      UFBoolean bZG = (UFBoolean)v.elementAt(1);
      String sStockTypeCode = (String)v.elementAt(2);

      BillVO gBillVO = generateI9ForWithraw(billVO, nSettleMny, 
        generalHItemVOs, tsList);

      if (!bZG.booleanValue())
      {
        headerVO.setBestimateflag(new UFBoolean(false));
        headerVO.setCbilltypecode("I2");
        itemVO[0].setCbilltypecode("I2");
        itemVO[0].setNnumber(nSettleNum);
        itemVO[0].setNmoney(nSettleMny);

        itemVO[0].setNreasonalwastnum(VO.getNreasonalwastnum());
        itemVO[0].setNreasonalwastprice(VO.getNreasonalwastprice());
        itemVO[0].setNreasonalwastmny(VO.getNreasonalwastmny());

        if (nPrice != null) {
          itemVO[0].setNprice(nPrice);
        } else if ((nSettleMny != null) && (nSettleNum != null) && 
          (Math.abs(nSettleNum.doubleValue()) >= 0.0D)) {
          double d = nSettleMny.doubleValue() / 
            nSettleNum.doubleValue();
          itemVO[0].setNprice(new UFDouble(d));
        }

        itemVO[0]
          .setNassistnum((itemVO[0].getCastunitid() == null) || 
          (itemVO[0]
          .getCastunitid().trim().equals("")) ? 
          null : 
          nAssistNum);
        vReturn.addElement(billVO);
      }
      else {
        headerVO.setBestimateflag(new UFBoolean(true));

        if (sStockTypeCode.trim().equals("47"))
          sZGmode = "单到补差";
        if (sZGmode.trim().equals("单到回冲"))
        {
          if (sStockTypeCode.trim().equals("47")) {
            headerVO.setCbilltypecode("ID");
            itemVO[0].setCbilltypecode("ID");
          } else {
            headerVO.setCbilltypecode("I2");
            itemVO[0].setCbilltypecode("I2");
          }
          headerVO.setBRedBill(new UFBoolean(false));
          itemVO[0].setNnumber(nSettleNum);
          itemVO[0].setNmoney(nSettleMny);

          itemVO[0].setNreasonalwastnum(VO.getNreasonalwastnum());
          itemVO[0].setNreasonalwastprice(VO.getNreasonalwastprice());
          itemVO[0].setNreasonalwastmny(VO.getNreasonalwastmny());

          if (nPrice != null) {
            itemVO[0].setNprice(nPrice);
          } else if ((nSettleMny != null) && (nSettleNum != null) && 
            (Math.abs(nSettleNum.doubleValue()) >= 0.0D)) {
            double d = nSettleMny.doubleValue() / 
              nSettleNum.doubleValue();
            itemVO[0].setNprice(new UFDouble(d));
          }

          itemVO[0]
            .setNassistnum((itemVO[0].getCastunitid() == null) || 
            (itemVO[0]
            .getCastunitid().trim().equals("")) ? 
            null : 
            nAssistNum);

          ((IBillHeaderVO)billVO.getParentVO())
            .setBestimateflag(new UFBoolean(false));

          BillVO gbillVO = new BillVO();
          IBillHeaderVO gheaderVO = (IBillHeaderVO)headerVO.clone();
          IBillItemVO gitemVO = (IBillItemVO)itemVO[0].clone();

          gitemVO.setNreasonalwastnum(null);
          gitemVO.setNreasonalwastprice(null);
          gitemVO.setNreasonalwastmny(null);

          if (nSettleNum != null)
            gitemVO.setNnumber(
              new UFDouble(-nSettleNum
              .doubleValue()));
          if (nGaugeMny != null)
            gitemVO
              .setNmoney(new UFDouble(
              -nGaugeMny
              .doubleValue()));
          if ((nGaugeMny != null) && (nSettleNum != null) && 
            (Math.abs(nSettleNum.doubleValue()) >= 0.0D)) {
            double d = nGaugeMny.doubleValue() / 
              nSettleNum.doubleValue();
            gitemVO.setNprice(new UFDouble(d));
          }

          gitemVO
            .setNassistnum((gitemVO.getCastunitid() == null) || 
            (gitemVO
            .getCastunitid().trim().equals("")) ? 
            null : 
            nAssistNum);
          if (gitemVO.getNassistnum() != null)
            gitemVO.setNassistnum(gitemVO.getNassistnum().multiply(
              -1.0D));
          gheaderVO.setBRedBill(new UFBoolean(true));
          Vector vTemp = new Vector();
          vTemp.addElement(gitemVO);
          IBillItemVO[] tempVO = new IBillItemVO[1];
          vTemp.copyInto(tempVO);
          gbillVO.setChildrenVO(tempVO);
          gbillVO.setParentVO(gheaderVO);

          ((IBillHeaderVO)gbillVO.getParentVO())
            .setBestimateflag(new UFBoolean(true));

          vReturn.addElement(gbillVO);
          vReturn.addElement(billVO);
        }
        else
        {
          for (int i = 0; i < itemVO.length; i++) {
            itemVO[i].setCastunitid(null);
            itemVO[i].setNassistnum(null);
            itemVO[i].setNSettleNum(nSettleNum);
            itemVO[i].setNSettleMny(nSettleMny);

            if (!sStockTypeCode.equals("4T")) {
              itemVO[i].setCadjustbillid(itemVO[i].getCbillid());
              itemVO[i].setCadjustbillitemid(itemVO[i]
                .getCbill_bid());
            }
          }

          if (sCYZRmode.trim().equals("成本"))
          {
            headerVO.setCbilltypecode("I9");
            headerVO.setBestimateflag(new UFBoolean(false));
            itemVO[0].setCbilltypecode("I9");
            itemVO[0].setNnumber(null);
            itemVO[0].setNprice(null);
            if ((nSettleMny != null) && (nGaugeMny != null)) {
              double d = nSettleMny.doubleValue() - 
                nGaugeMny.doubleValue();
              itemVO[0].setNmoney(new UFDouble(d));
            }

            itemVO[0].setBretractflag(new UFBoolean(false));
            if (!sStockTypeCode.equals("4T")) itemVO[0].setNadjustnum(nSettleNum);

            BillVO gbillVO = null;
            if ((VO.getNreasonalwastnum() != null) && 
              (Math.abs(VO.getNreasonalwastnum().doubleValue()) > VMIDMO.getDigitRMB(VO.getPk_corp()).doubleValue())) {
              gbillVO = new BillVO();
              IBillHeaderVO gheaderVO = (IBillHeaderVO)headerVO.clone();
              IBillItemVO gitemVO = (IBillItemVO)itemVO[0].clone();

              gheaderVO.setCbilltypecode("IG");
              gitemVO.setCbilltypecode("IG");

              gitemVO.setNreasonalwastnum(VO.getNreasonalwastnum());
              gitemVO.setNreasonalwastprice(VO.getNreasonalwastprice());
              gitemVO.setNreasonalwastmny(VO.getNreasonalwastmny());

              Vector vTemp = new Vector();
              vTemp.addElement(gitemVO);
              IBillItemVO[] tempVO = new IBillItemVO[1];
              vTemp.copyInto(tempVO);
              gbillVO.setChildrenVO(tempVO);
              gbillVO.setParentVO(gheaderVO);
            }

            if (itemVO[0].getNmoney() != null)
            {
              if (Math.abs(itemVO[0].getNmoney()
                .doubleValue()) >= 
                VMIDMO.getDigitRMB(strPkCorp).doubleValue())
                vReturn.addElement(billVO);
            }
            if (gbillVO != null) vReturn.addElement(gbillVO);
          }
          else
          {
            headerVO.setCbilltypecode("IG");
            headerVO.setBestimateflag(new UFBoolean(false));
            itemVO[0].setCbilltypecode("IG");
            itemVO[0].setNnumber(null);
            itemVO[0].setNprice(null);
            itemVO[0].setBretractflag(new UFBoolean(false));
            if ((nSettleMny != null) && (nGaugeMny != null)) {
              double d = nSettleMny.doubleValue() - 
                nGaugeMny.doubleValue();
              itemVO[0].setNmoney(new UFDouble(d));
            }

            itemVO[0].setNreasonalwastnum(VO.getNreasonalwastnum());
            itemVO[0].setNreasonalwastprice(VO.getNreasonalwastprice());
            itemVO[0].setNreasonalwastmny(VO.getNreasonalwastmny());

            if (itemVO[0].getNmoney() != null)
            {
              if (Math.abs(itemVO[0].getNmoney()
                .doubleValue()) >= 
                VMIDMO.getDigitRMB(strPkCorp).doubleValue()) {
                vReturn.addElement(billVO);
              }
            }
          }
        }
      }
      if (gBillVO != null)
        vReturn.addElement(gBillVO);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    if (vReturn.size() > 0) {
      BillVO[] returnVOs = new BillVO[vReturn.size()];
      vReturn.copyInto(returnVOs);
      return returnVOs;
    }
    return null;
  }

  private BillVO[] transferIAData1(StockVO stock, String sZGmode, String sCYZRmode, SettlebillItemVO VO, String cOperator, UFDate dCurrDate, GeneralHItemVO[] generalHItemVOs, UFDouble nAssistNum, String cCalbody, ArrayList tsList)
    throws BusinessException
  {
    Vector vReturn = new Vector();
    String strPkCorp = null;
    try {
      strPkCorp = stock.getPk_corp();

      Vector v = convertVOForIA1(stock, VO.getCsettlebillid(), VO
        .getCsettlebill_bid(), cOperator, dCurrDate, cCalbody);
      UFDouble nSettleNum = VO.getNsettlenum();
      UFDouble nSettleMny = VO.getNmoney();
      UFDouble nGaugeMny = VO.getNgaugemny();
      UFDouble nPrice = VO.getNprice();
      BillVO billVO = (BillVO)v.elementAt(0);
      IBillHeaderVO headerVO = (IBillHeaderVO)billVO.getParentVO();
      IBillItemVO[] itemVO = (IBillItemVO[])billVO.getChildrenVO();
      UFBoolean bZG = (UFBoolean)v.elementAt(1);
      String sStockTypeCode = (String)v.elementAt(2);

      BillVO gBillVO = generateI9ForWithraw(billVO, nSettleMny, 
        generalHItemVOs, tsList);

      if ((bZG == null) || (!bZG.booleanValue()))
      {
        headerVO.setBestimateflag(new UFBoolean(false));
        headerVO.setCbilltypecode("I2");
        itemVO[0].setCbilltypecode("I2");
        itemVO[0].setNnumber(nSettleNum);
        itemVO[0].setNmoney(nSettleMny);

        itemVO[0].setNreasonalwastnum(VO.getNreasonalwastnum());
        itemVO[0].setNreasonalwastprice(VO.getNreasonalwastprice());
        itemVO[0].setNreasonalwastmny(VO.getNreasonalwastmny());

        if (nPrice != null) {
          itemVO[0].setNprice(nPrice);
        } else if ((nSettleMny != null) && (nSettleNum != null) && 
          (Math.abs(nSettleNum.doubleValue()) >= 0.0D)) {
          double d = nSettleMny.doubleValue() / 
            nSettleNum.doubleValue();
          itemVO[0].setNprice(new UFDouble(d));
        }
        itemVO[0]
          .setNassistnum((itemVO[0].getCastunitid() == null) || 
          (itemVO[0]
          .getCastunitid().trim().equals("")) ? 
          null : 
          nAssistNum);
        vReturn.addElement(billVO);
      }
      else {
        headerVO.setBestimateflag(new UFBoolean(true));

        if (sStockTypeCode.trim().equals("47"))
          sZGmode = "单到补差";
        if (sZGmode.trim().equals("单到回冲"))
        {
          if (sStockTypeCode.trim().equals("47")) {
            headerVO.setCbilltypecode("ID");
            itemVO[0].setCbilltypecode("ID");
          } else {
            headerVO.setCbilltypecode("I2");
            itemVO[0].setCbilltypecode("I2");
          }
          itemVO[0].setNnumber(nSettleNum);
          itemVO[0].setNmoney(nSettleMny);

          itemVO[0].setNreasonalwastnum(VO.getNreasonalwastnum());
          itemVO[0].setNreasonalwastprice(VO.getNreasonalwastprice());
          itemVO[0].setNreasonalwastmny(VO.getNreasonalwastmny());

          if (nPrice != null) {
            itemVO[0].setNprice(nPrice);
          } else if ((nSettleMny != null) && (nSettleNum != null) && 
            (Math.abs(nSettleNum.doubleValue()) >= 0.0D)) {
            double d = nSettleMny.doubleValue() / 
              nSettleNum.doubleValue();
            itemVO[0].setNprice(new UFDouble(d));
          }
          itemVO[0]
            .setNassistnum((itemVO[0].getCastunitid() == null) || 
            (itemVO[0]
            .getCastunitid().trim().equals("")) ? 
            null : 
            nAssistNum);
          ((IBillHeaderVO)billVO.getParentVO())
            .setBestimateflag(new UFBoolean(false));

          BillVO gRedVO = new BillVO();
          IBillHeaderVO gRedHeaderVO = (IBillHeaderVO)headerVO.clone();
          IBillItemVO gRedItemVO = (IBillItemVO)itemVO[0].clone();

          gRedItemVO.setNreasonalwastnum(null);
          gRedItemVO.setNreasonalwastprice(null);
          gRedItemVO.setNreasonalwastmny(null);

          if (nSettleNum != null)
            gRedItemVO.setNnumber(
              new UFDouble(-nSettleNum
              .doubleValue()));
          if (nGaugeMny != null)
            gRedItemVO.setNmoney(
              new UFDouble(-nGaugeMny
              .doubleValue()));
          if ((nGaugeMny != null) && (nSettleNum != null) && 
            (Math.abs(nSettleNum.doubleValue()) >= 0.0D)) {
            double d = nGaugeMny.doubleValue() / 
              nSettleNum.doubleValue();
            gRedItemVO.setNprice(new UFDouble(d));
          }
          gRedItemVO
            .setNassistnum((gRedItemVO.getCastunitid() == null) || 
            (gRedItemVO
            .getCastunitid().trim().equals("")) ? 
            null : 
            nAssistNum);
          if (gRedItemVO.getNassistnum() != null)
            gRedItemVO.setNassistnum(gRedItemVO.getNassistnum()
              .multiply(-1.0D));
          Vector vTemp = new Vector();
          vTemp.addElement(gRedItemVO);
          IBillItemVO[] tempVO = new IBillItemVO[1];
          vTemp.copyInto(tempVO);
          gRedVO.setChildrenVO(tempVO);
          gRedVO.setParentVO(gRedHeaderVO);
          ((IBillHeaderVO)gRedVO.getParentVO())
            .setBestimateflag(new UFBoolean(true));

          vReturn.addElement(gRedVO);
          vReturn.addElement(billVO);
        }
        else {
          for (int i = 0; i < itemVO.length; i++) {
            itemVO[i].setCastunitid(null);
            itemVO[i].setNassistnum(null);
            itemVO[i].setNSettleNum(nSettleNum);
            itemVO[i].setNSettleMny(nSettleMny);

            if (!sStockTypeCode.equals("4T")) {
              itemVO[i].setCadjustbillid(itemVO[i].getCbillid());
              itemVO[i].setCadjustbillitemid(itemVO[i]
                .getCbill_bid());
            }
          }
          if (sCYZRmode.trim().equals("成本")) {
            headerVO.setCbilltypecode("I9");
            headerVO.setBestimateflag(new UFBoolean(false));
            itemVO[0].setCbilltypecode("I9");
            itemVO[0].setNnumber(null);
            itemVO[0].setNprice(null);
            if ((nSettleMny != null) && (nGaugeMny != null)) {
              double d = nSettleMny.doubleValue() - 
                nGaugeMny.doubleValue();
              itemVO[0].setNmoney(new UFDouble(d));
            }

            itemVO[0].setBretractflag(new UFBoolean(false));
            if (!sStockTypeCode.equals("4T")) itemVO[0].setNadjustnum(nSettleNum);

            BillVO gbillVO = null;
            if ((VO.getNreasonalwastnum() != null) && 
              (Math.abs(VO.getNreasonalwastnum().doubleValue()) > VMIDMO.getDigitRMB(VO.getPk_corp()).doubleValue())) {
              gbillVO = new BillVO();
              IBillHeaderVO gheaderVO = (IBillHeaderVO)headerVO.clone();
              IBillItemVO gitemVO = (IBillItemVO)itemVO[0].clone();

              gheaderVO.setCbilltypecode("IG");
              gitemVO.setCbilltypecode("IG");

              gitemVO.setNreasonalwastnum(VO.getNreasonalwastnum());
              gitemVO.setNreasonalwastprice(VO.getNreasonalwastprice());
              gitemVO.setNreasonalwastmny(VO.getNreasonalwastmny());

              Vector vTemp = new Vector();
              vTemp.addElement(gitemVO);
              IBillItemVO[] tempVO = new IBillItemVO[1];
              vTemp.copyInto(tempVO);
              gbillVO.setChildrenVO(tempVO);
              gbillVO.setParentVO(gheaderVO);
            }

            if (itemVO[0].getNmoney() != null)
            {
              if (Math.abs(itemVO[0].getNmoney()
                .doubleValue()) >= 
                VMIDMO.getDigitRMB(strPkCorp).doubleValue())
                vReturn.addElement(billVO);
            }
            if (gbillVO != null) vReturn.addElement(gbillVO); 
          }
          else
          {
            headerVO.setCbilltypecode("IG");
            headerVO.setBestimateflag(new UFBoolean(false));
            itemVO[0].setCbilltypecode("IG");
            itemVO[0].setNnumber(null);
            itemVO[0].setNprice(null);
            itemVO[0].setBretractflag(new UFBoolean(false));
            if ((nSettleMny != null) && (nGaugeMny != null)) {
              double d = nSettleMny.doubleValue() - 
                nGaugeMny.doubleValue();
              itemVO[0].setNmoney(new UFDouble(d));
            }

            itemVO[0].setNreasonalwastnum(VO.getNreasonalwastnum());
            itemVO[0].setNreasonalwastprice(VO.getNreasonalwastprice());
            itemVO[0].setNreasonalwastmny(VO.getNreasonalwastmny());

            if (itemVO[0].getNmoney() != null)
            {
              if (Math.abs(itemVO[0].getNmoney()
                .doubleValue()) >= 
                VMIDMO.getDigitRMB(strPkCorp).doubleValue()) {
                vReturn.addElement(billVO);
              }
            }
          }
        }
      }
      if (gBillVO != null)
        vReturn.addElement(gBillVO);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    if (vReturn.size() > 0) {
      BillVO[] returnVOs = new BillVO[vReturn.size()];
      vReturn.copyInto(returnVOs);
      return returnVOs;
    }
    return null;
  }

  private BillVO[] transferIADataForFee(GeneralHHeaderVO stockHeadVO, GeneralHItemVO stockBodyVO, String sCYZRmode, SettlebillItemVO VO, String cOperator, UFDate dCurrDate, String cCalbody)
    throws BusinessException
  {
    Vector vReturn = new Vector();
    String strPkCorp = null;
    try {
      strPkCorp = stockHeadVO.getPk_corp();

      Vector v = convertVOForIA(stockHeadVO, stockBodyVO, VO
        .getCsettlebillid(), VO.getCsettlebill_bid(), cOperator, 
        dCurrDate, cCalbody);
      UFDouble nSettleMny = VO.getNmoney();
      BillVO billVO = (BillVO)v.elementAt(0);
      IBillHeaderVO headerVO = (IBillHeaderVO)billVO.getParentVO();
      IBillItemVO[] itemVO = (IBillItemVO[])billVO.getChildrenVO();
      UFBoolean bZG = (UFBoolean)v.elementAt(1);
      String sStockTypeCode = (String)v.elementAt(2);

      for (int i = 0; i < itemVO.length; i++) {
        itemVO[i].setCastunitid(null);
        itemVO[i].setNassistnum(null);

        if ((bZG == null) || (!bZG.booleanValue()) || 
          (sStockTypeCode.equals("4T"))) continue;
        itemVO[i].setCadjustbillid(itemVO[i].getCbillid());
        itemVO[i].setCadjustbillitemid(itemVO[i].getCbill_bid());
      }

      if (sCYZRmode.trim().equals("成本"))
      {
        headerVO.setCbilltypecode("I9");
        headerVO.setBestimateflag(new UFBoolean(false));

        itemVO = rearrangeBillItemVOForFee(itemVO[0], VO, false, bZG);
        for (int i = 0; i < itemVO.length; i++) {
          itemVO[i].setCbilltypecode("I9");
          itemVO[i].setNnumber(null);
          itemVO[i].setNprice(null);
          itemVO[i].setBretractflag(new UFBoolean(true));
        }
        billVO.setChildrenVO(itemVO);

        if (itemVO[0].getNmoney() != null)
        {
          if (Math.abs(itemVO[0].getNmoney().doubleValue()) >= 
            VMIDMO.getDigitRMB(strPkCorp).doubleValue())
            vReturn.addElement(billVO);
        }
      }
      else {
        headerVO.setCbilltypecode("IG");
        headerVO.setBestimateflag(new UFBoolean(false));
        itemVO[0].setCbilltypecode("IG");
        itemVO[0].setNnumber(null);
        itemVO[0].setNprice(null);
        itemVO[0].setNmoney(nSettleMny);
        itemVO[0].setBretractflag(new UFBoolean(true));

        if (itemVO[0].getNmoney() != null)
        {
          if (Math.abs(itemVO[0].getNmoney().doubleValue()) >= 
            VMIDMO.getDigitRMB(strPkCorp).doubleValue())
            vReturn.addElement(billVO);
        }
      }
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    if (vReturn.size() > 0) {
      BillVO[] returnVOs = new BillVO[vReturn.size()];
      vReturn.copyInto(returnVOs);
      return returnVOs;
    }
    return null;
  }

  public IBillItemVO[] rearrangeBillItemVOForFee(IBillItemVO itemVO, SettlebillItemVO VO, boolean bVMI, UFBoolean bZG)
    throws Exception
  {
    Hashtable hSettle = null;
    if (!bVMI)
      hSettle = new PubDMO().queryHtResultFromAnyTable("po_settlebill_b", "cstockrow", 
        new String[] { "csettlebillid", "csettlebill_bid", "nsettlenum", "nreasonalwastnum", "nmoney", "ngaugemny" }, 
        " dr = 0 and cstockrow = '" + itemVO.getCbill_bid() + "'");
    else {
      hSettle = new PubDMO().queryHtResultFromAnyTable("po_settlebill_b", "cvmiid", 
        new String[] { "csettlebillid", "csettlebill_bid", "nsettlenum", "nreasonalwastnum", "nmoney", "ngaugemny" }, 
        " dr = 0 and cvmiid = '" + itemVO.getCbill_bid() + "'");
    }
    if ((hSettle == null) || (hSettle.size() == 0)) return new IBillItemVO[] { itemVO };

    Vector vTemp = null; Vector v1 = new Vector();
    Object key = null; Object data = null; Object[] d = (Object[])null;
    UFDouble nSettleNum = null; UFDouble nSettleMny = null; UFDouble nGaugeMny = null;
    String unitCode = itemVO.getPk_corp();
    IBillItemVO tempVO = null;

    Enumeration keys = hSettle.keys();
    double total = 0.0D; double dd = 0.0D;
    while (keys.hasMoreElements()) {
      key = keys.nextElement();
      data = hSettle.get(key);
      if (data != null) {
        vTemp = (Vector)data;
        if ((vTemp != null) && (vTemp.size() > 0)) {
          for (int i = 0; i < vTemp.size(); i++) {
            d = (Object[])vTemp.elementAt(i);
            if ((d != null) && (d.length >= 6)) {
              if (d[2] != null) nSettleNum = new UFDouble(d[2].toString()); else
                nSettleNum = new UFDouble(0);
              if (Math.abs(nSettleNum.doubleValue()) < VMIDMO.getDigitRMB(unitCode).doubleValue())
                continue;
              tempVO = (IBillItemVO)itemVO.clone();
              tempVO.setNadjustnum(nSettleNum);
              total += nSettleNum.doubleValue();

              if (d[4] != null) nSettleMny = new UFDouble(d[4].toString()); else
                nSettleMny = new UFDouble(0);
              if (d[5] != null) nGaugeMny = new UFDouble(d[5].toString()); else {
                nGaugeMny = new UFDouble(0);
              }

              double ddd = Math.abs(nSettleMny.doubleValue() - nGaugeMny.doubleValue());
              if (bZG.booleanValue()) {
                if ((d[0] != null) && (ddd > VMIDMO.getDigitRMB(unitCode).doubleValue())) tempVO.setCadjustbillid(d[0].toString());
                if ((d[1] != null) && (ddd > VMIDMO.getDigitRMB(unitCode).doubleValue())) tempVO.setCadjustbillitemid(d[1].toString()); 
              }
              else {
                if (d[0] != null) tempVO.setCadjustbillid(d[0].toString());
                if (d[1] != null) tempVO.setCadjustbillitemid(d[1].toString());
              }

              v1.addElement(tempVO);
            }
          }
        }
      }
    }
    if (v1.size() == 0) return new IBillItemVO[] { itemVO };

    IBillItemVO[] bodyVO = new IBillItemVO[v1.size()];
    v1.copyInto(bodyVO);

    double ddd = 0.0D;
    for (int i = 0; i < bodyVO.length; i++) {
      String s = VO.getCstockrow();
      if (bVMI) s = VO.getCvmiid();
      if (bodyVO[i].getCbill_bid().equals(s)) {
        if (Math.abs(total) < VMIDMO.getDigitRMB(unitCode).doubleValue()) { bodyVO[i].setNmoney(VO.getNmoney());
        } else {
          dd = bodyVO[i].getNadjustnum().doubleValue() / total * VO.getNmoney().doubleValue();
          if (i == bodyVO.length - 1) dd = VO.getNmoney().doubleValue() - ddd; else
            ddd += dd;
          bodyVO[i].setNmoney(new UFDouble(dd));
        }
      }
    }

    return bodyVO;
  }

  private void deleteBillFromOutters(String[] cGeneralhid, String cPkcorp, String cOperator)
    throws BusinessException
  {
    IIAToPUBill myService = null;
    try
    {
      myService = (IIAToPUBill)NCLocator.getInstance().lookup(
        IIAToPUBill.class.getName());
      myService
        .deleteBillFromOutterArray(cGeneralhid, cPkcorp, cOperator);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
  }

  public boolean doDirectInvoiceBalance(String accountYear, UFDate currentDate, String busitypeID, String operatorID, String unitCode, InvoiceVO invoiceVO, String[] cStoreOrganizationID)
    throws BusinessException
  {
    String vSettlebillcode = "";
    Timer timer = new Timer();
    timer.start();
    boolean bGetBillCodeSucc = false;
    SettlebillVO voBillcode = null;
    try
    {
      GetSysBillCode billCode = new GetSysBillCode();
      SettlebillHeaderVO head = new SettlebillHeaderVO();
      head.setPk_corp(unitCode);
      voBillcode = new SettlebillVO(1);
      voBillcode.setParentVO(head);
      voBillcode
        .setChildrenVO(new SettlebillItemVO[] { new SettlebillItemVO() });
      do
        vSettlebillcode = billCode.getSysBillNO(voBillcode);
      while (
        isSettlebillCodeDuplicate(unitCode, vSettlebillcode));
      bGetBillCodeSucc = true;

      timer.addExecutePhase("生成结算单号");

      DirectInvoiceSettle directInvoiceSettle = new DirectInvoiceSettle(
        accountYear, currentDate, busitypeID, operatorID, unitCode, 
        vSettlebillcode);

      Vector v = directInvoiceSettle.doBalance(invoiceVO, 
        cStoreOrganizationID);

      timer.addExecutePhase("执行结算");

      if ((v != null) && (v.size() > 0)) {
        IinvoiceVO[] invoiceVOs = (IinvoiceVO[])v.elementAt(0);
        SettlebillVO settlebillVO = (SettlebillVO)v.elementAt(1);
        if ((invoiceVOs != null) && (invoiceVOs.length > 0)) {
          ICreateCorpQueryService myService0 = null;
          myService0 = (ICreateCorpQueryService)
            NCLocator.getInstance().lookup(
            ICreateCorpQueryService.class.getName());
          boolean bIAStartUp = myService0.isEnabled(unitCode, "IA");
          if (bIAStartUp) {
            saveBillFromOutterForDirect(invoiceVOs, 
              cStoreOrganizationID, currentDate, 
              settlebillVO, operatorID);
          }
        }
        timer.addExecutePhase("向存货核算传送数据");

        timer.showAllExecutePhase("业务类型为直运时，订单生成发票结算时间分布");

        return true;
      }

      if (bGetBillCodeSucc) {
        try {
          GetSysBillCode billCode1 = new GetSysBillCode();
          billCode1.returnBillNo(voBillcode);
        }
        catch (Exception ex) {
          PubDMO.throwBusinessException(ex);
        }
      }
    }
    catch (Exception e)
    {
      if (bGetBillCodeSucc) {
        try {
          GetSysBillCode billCode = new GetSysBillCode();
          billCode.returnBillNo(voBillcode);
        }
        catch (Exception ex) {
          PubDMO.throwBusinessException(e);
        }
      }

      PubDMO.throwBusinessException(e);
    }
    timer.showAllExecutePhase("业务类型为直运时，订单生成发票结算时间分布");
    return false;
  }

  public ArrayList doStockToInvoiceSettle(String accountYear, UFDate currentDate, String busitypeID, String operatorID, String unitCode, InvoiceVO invoiceVO, GeneralBillHeaderVO[] generalBillHeaderVOs, GeneralBillItemVO[] generalBillItemVOs, GeneralBb3VO[] generalBb3VOs, String sEstMode, String sDiffMode, UFBoolean ufbNeedLock)
    throws BusinessException
  {
    ArrayList listLockId = null;
    boolean bLockSucc = false;
    String vSettlebillcode = "";
    SettlebillVO voBillCode = null;
    boolean bGetBillCodeSucc = false;
    UFBoolean bSucceed = new UFBoolean(false);

    Timer timer = new Timer();
    timer.start();
    try
    {
      GetSysBillCode billCode = new GetSysBillCode();
      SettlebillHeaderVO head = new SettlebillHeaderVO();
      head.setPk_corp(unitCode);
      voBillCode = new SettlebillVO(1);
      voBillCode.setParentVO(head);
      voBillCode
        .setChildrenVO(new SettlebillItemVO[] { new SettlebillItemVO() });
      do
        vSettlebillcode = billCode.getSysBillNO(voBillCode);
      while (
        isSettlebillCodeDuplicate(unitCode, vSettlebillcode));
      voBillCode.getHeadVO().setVsettlebillcode(vSettlebillcode);
      bGetBillCodeSucc = true;

      timer.addExecutePhase("生成结算单号");

      GeneralHHeaderVO[] generalHHeaderVOs = new GeneralHHeaderVO[generalBillHeaderVOs.length];
      InvoiceItemVO[] invoiceItemVO = invoiceVO.getBodyVO();
      InvoiceHeaderVO invoiceHeaderVO = invoiceVO.getHeadVO();
      for (int i = 0; i < generalHHeaderVOs.length; i++) {
        generalHHeaderVOs[i] = new GeneralHHeaderVO();
        generalHHeaderVOs[i].setCbiztype(generalBillHeaderVOs[i]
          .getCbiztypeid());
        generalHHeaderVOs[i].setCdptid(generalBillHeaderVOs[i]
          .getCdptid());
        generalHHeaderVOs[i].setCgeneralhid(generalBillHeaderVOs[i]
          .getCgeneralhid());
        generalHHeaderVOs[i].setCoperatorid(generalBillHeaderVOs[i]
          .getCoperatorid());
        generalHHeaderVOs[i].setCproviderid(generalBillHeaderVOs[i]
          .getCproviderid());
        generalHHeaderVOs[i].setPk_corp(generalBillHeaderVOs[i]
          .getPk_corp());
        generalHHeaderVOs[i].setVbillcode(generalBillHeaderVOs[i]
          .getVbillcode());
        generalHHeaderVOs[i].setCbilltypecode(generalBillHeaderVOs[i].getCbilltypecode());
        generalHHeaderVOs[i].setCwarehouseid(generalBillHeaderVOs[i].getCwarehouseid());
        generalHHeaderVOs[i].setCstoreorganization((String)generalBillHeaderVOs[i].getAttributeValue("pk_calbody"));
        generalHHeaderVOs[i].setCbizid(generalBillHeaderVOs[i].getCbizid());

        generalHHeaderVOs[i].setVuserdef1(invoiceHeaderVO.getVdef1());
        generalHHeaderVOs[i].setVuserdef2(invoiceHeaderVO.getVdef2());
        generalHHeaderVOs[i].setVuserdef3(invoiceHeaderVO.getVdef3());
        generalHHeaderVOs[i].setVuserdef4(invoiceHeaderVO.getVdef4());
        generalHHeaderVOs[i].setVuserdef5(invoiceHeaderVO.getVdef5());
        generalHHeaderVOs[i].setVuserdef6(invoiceHeaderVO.getVdef6());
        generalHHeaderVOs[i].setVuserdef7(invoiceHeaderVO.getVdef7());
        generalHHeaderVOs[i].setVuserdef8(invoiceHeaderVO.getVdef8());
        generalHHeaderVOs[i].setVuserdef9(invoiceHeaderVO.getVdef9());
        generalHHeaderVOs[i].setVuserdef10(invoiceHeaderVO.getVdef10());

        generalHHeaderVOs[i].setVuserdef11(invoiceHeaderVO.getVdef11());
        generalHHeaderVOs[i].setVuserdef12(invoiceHeaderVO.getVdef12());
        generalHHeaderVOs[i].setVuserdef13(invoiceHeaderVO.getVdef13());
        generalHHeaderVOs[i].setVuserdef14(invoiceHeaderVO.getVdef14());
        generalHHeaderVOs[i].setVuserdef15(invoiceHeaderVO.getVdef15());
        generalHHeaderVOs[i].setVuserdef16(invoiceHeaderVO.getVdef16());
        generalHHeaderVOs[i].setVuserdef17(invoiceHeaderVO.getVdef17());
        generalHHeaderVOs[i].setVuserdef18(invoiceHeaderVO.getVdef18());
        generalHHeaderVOs[i].setVuserdef19(invoiceHeaderVO.getVdef19());
        generalHHeaderVOs[i].setVuserdef20(invoiceHeaderVO.getVdef20());

        generalHHeaderVOs[i].setPk_defdoc1(invoiceHeaderVO
          .getPKDefDoc1());
        generalHHeaderVOs[i].setPk_defdoc2(invoiceHeaderVO
          .getPKDefDoc2());
        generalHHeaderVOs[i].setPk_defdoc3(invoiceHeaderVO
          .getPKDefDoc3());
        generalHHeaderVOs[i].setPk_defdoc4(invoiceHeaderVO
          .getPKDefDoc4());
        generalHHeaderVOs[i].setPk_defdoc5(invoiceHeaderVO
          .getPKDefDoc5());
        generalHHeaderVOs[i].setPk_defdoc6(invoiceHeaderVO
          .getPKDefDoc6());
        generalHHeaderVOs[i].setPk_defdoc7(invoiceHeaderVO
          .getPKDefDoc7());
        generalHHeaderVOs[i].setPk_defdoc8(invoiceHeaderVO
          .getPKDefDoc8());
        generalHHeaderVOs[i].setPk_defdoc9(invoiceHeaderVO
          .getPKDefDoc9());
        generalHHeaderVOs[i].setPk_defdoc10(invoiceHeaderVO
          .getPKDefDoc10());

        generalHHeaderVOs[i].setPk_defdoc11(invoiceHeaderVO
          .getPKDefDoc11());
        generalHHeaderVOs[i].setPk_defdoc12(invoiceHeaderVO
          .getPKDefDoc12());
        generalHHeaderVOs[i].setPk_defdoc13(invoiceHeaderVO
          .getPKDefDoc13());
        generalHHeaderVOs[i].setPk_defdoc14(invoiceHeaderVO
          .getPKDefDoc14());
        generalHHeaderVOs[i].setPk_defdoc15(invoiceHeaderVO
          .getPKDefDoc15());
        generalHHeaderVOs[i].setPk_defdoc16(invoiceHeaderVO
          .getPKDefDoc16());
        generalHHeaderVOs[i].setPk_defdoc17(invoiceHeaderVO
          .getPKDefDoc17());
        generalHHeaderVOs[i].setPk_defdoc18(invoiceHeaderVO
          .getPKDefDoc18());
        generalHHeaderVOs[i].setPk_defdoc19(invoiceHeaderVO
          .getPKDefDoc19());
        generalHHeaderVOs[i].setPk_defdoc20(invoiceHeaderVO
          .getPKDefDoc20());
      }

      GeneralHItemVO[] generalHItemVOs = new GeneralHItemVO[generalBillItemVOs.length];
      for (int i = 0; i < generalHItemVOs.length; i++) {
        generalHItemVOs[i] = new GeneralHItemVO();
        generalHItemVOs[i].setCgeneralbid(generalBillItemVOs[i]
          .getCgeneralbid());
        generalHItemVOs[i].setCgeneralhid(generalBillItemVOs[i]
          .getCgeneralhid());
        generalHItemVOs[i].setCinventoryid(generalBillItemVOs[i]
          .getCinventoryid());
        generalHItemVOs[i].setCbaseid(generalBillItemVOs[i].getCinvbasid());
        generalHItemVOs[i].setDbizdate(generalBillItemVOs[i].getDbizdate());
        generalHItemVOs[i].setCsourcebillbid(generalBillItemVOs[i]
          .getCsourcebillbid());
        generalHItemVOs[i].setCsourcebillhid(generalBillItemVOs[i]
          .getCsourcebillhid());
        generalHItemVOs[i].setCsourcetype(generalBillItemVOs[i]
          .getCsourcetype());
        generalHItemVOs[i].setNinnum(generalBillItemVOs[i].getNinnum());
        generalHItemVOs[i].setNmny(generalBillItemVOs[i].getNmny());
        generalHItemVOs[i].setVsourcebillcode(generalBillItemVOs[i]
          .getVsourcebillcode());

        generalHItemVOs[i].setCfirstbillbid(generalBillItemVOs[i].getCfirstbillbid());
        generalHItemVOs[i].setCfirstbillhid(generalBillItemVOs[i].getCfirstbillhid());
        generalHItemVOs[i].setCfirsttype(generalBillItemVOs[i].getCfirsttype());
        generalHItemVOs[i].setPk_invoicecorp((String)generalBillItemVOs[i].getAttributeValue("pk_invoicecorp"));
        generalHItemVOs[i].setBzgflag(generalBillItemVOs[i].getBzgflag());
        Object oTemp = generalBillItemVOs[i].getAttributeValue("bzgyfflag");
        if (oTemp == null) generalHItemVOs[i].setBzgyfflag(new UFBoolean(false)); else {
          generalHItemVOs[i].setBzgyfflag(new UFBoolean(oTemp.toString()));
        }
        generalHItemVOs[i].setVfree1(invoiceItemVO[i].getVfree1());
        generalHItemVOs[i].setVfree2(invoiceItemVO[i].getVfree2());
        generalHItemVOs[i].setVfree3(invoiceItemVO[i].getVfree3());
        generalHItemVOs[i].setVfree4(invoiceItemVO[i].getVfree4());
        generalHItemVOs[i].setVfree5(invoiceItemVO[i].getVfree5());
        generalHItemVOs[i].setHsl(generalBillItemVOs[i].getHsl());

        generalHItemVOs[i].setVuserdef1(invoiceItemVO[i].getVdef1());
        generalHItemVOs[i].setVuserdef2(invoiceItemVO[i].getVdef2());
        generalHItemVOs[i].setVuserdef3(invoiceItemVO[i].getVdef3());
        generalHItemVOs[i].setVuserdef4(invoiceItemVO[i].getVdef4());
        generalHItemVOs[i].setVuserdef5(invoiceItemVO[i].getVdef5());
        generalHItemVOs[i].setVuserdef6(invoiceItemVO[i].getVdef6());
        generalHItemVOs[i].setVuserdef7(invoiceItemVO[i].getVdef7());
        generalHItemVOs[i].setVuserdef8(invoiceItemVO[i].getVdef8());
        generalHItemVOs[i].setVuserdef9(invoiceItemVO[i].getVdef9());
        generalHItemVOs[i].setVuserdef10(invoiceItemVO[i].getVdef10());

        generalHItemVOs[i].setVuserdef11(invoiceItemVO[i].getVdef11());
        generalHItemVOs[i].setVuserdef12(invoiceItemVO[i].getVdef12());
        generalHItemVOs[i].setVuserdef13(invoiceItemVO[i].getVdef13());
        generalHItemVOs[i].setVuserdef14(invoiceItemVO[i].getVdef14());
        generalHItemVOs[i].setVuserdef15(invoiceItemVO[i].getVdef15());
        generalHItemVOs[i].setVuserdef16(invoiceItemVO[i].getVdef16());
        generalHItemVOs[i].setVuserdef17(invoiceItemVO[i].getVdef17());
        generalHItemVOs[i].setVuserdef18(invoiceItemVO[i].getVdef18());
        generalHItemVOs[i].setVuserdef19(invoiceItemVO[i].getVdef19());
        generalHItemVOs[i].setVuserdef20(invoiceItemVO[i].getVdef20());

        generalHItemVOs[i].setPk_defdoc1(invoiceItemVO[i]
          .getPKDefDoc1());
        generalHItemVOs[i].setPk_defdoc2(invoiceItemVO[i]
          .getPKDefDoc2());
        generalHItemVOs[i].setPk_defdoc3(invoiceItemVO[i]
          .getPKDefDoc3());
        generalHItemVOs[i].setPk_defdoc4(invoiceItemVO[i]
          .getPKDefDoc4());
        generalHItemVOs[i].setPk_defdoc5(invoiceItemVO[i]
          .getPKDefDoc5());
        generalHItemVOs[i].setPk_defdoc6(invoiceItemVO[i]
          .getPKDefDoc6());
        generalHItemVOs[i].setPk_defdoc7(invoiceItemVO[i]
          .getPKDefDoc7());
        generalHItemVOs[i].setPk_defdoc8(invoiceItemVO[i]
          .getPKDefDoc8());
        generalHItemVOs[i].setPk_defdoc9(invoiceItemVO[i]
          .getPKDefDoc9());
        generalHItemVOs[i].setPk_defdoc10(invoiceItemVO[i]
          .getPKDefDoc10());

        generalHItemVOs[i].setPk_defdoc11(invoiceItemVO[i]
          .getPKDefDoc11());
        generalHItemVOs[i].setPk_defdoc12(invoiceItemVO[i]
          .getPKDefDoc12());
        generalHItemVOs[i].setPk_defdoc13(invoiceItemVO[i]
          .getPKDefDoc13());
        generalHItemVOs[i].setPk_defdoc14(invoiceItemVO[i]
          .getPKDefDoc14());
        generalHItemVOs[i].setPk_defdoc15(invoiceItemVO[i]
          .getPKDefDoc15());
        generalHItemVOs[i].setPk_defdoc16(invoiceItemVO[i]
          .getPKDefDoc16());
        generalHItemVOs[i].setPk_defdoc17(invoiceItemVO[i]
          .getPKDefDoc17());
        generalHItemVOs[i].setPk_defdoc18(invoiceItemVO[i]
          .getPKDefDoc18());
        generalHItemVOs[i].setPk_defdoc19(invoiceItemVO[i]
          .getPKDefDoc19());
        generalHItemVOs[i].setPk_defdoc20(invoiceItemVO[i]
          .getPKDefDoc20());
      }

      timer.addExecutePhase("来自库存的入库单VO转化为采购结算的入库单VO");

      StockToInvoiceSettle stockToInvoice = new StockToInvoiceSettle(
        accountYear, currentDate, busitypeID, operatorID, unitCode, 
        vSettlebillcode);
      Vector v = stockToInvoice.doBalance(invoiceVO, generalHHeaderVOs, 
        generalHItemVOs, generalBb3VOs);

      timer.addExecutePhase("执行结算");

      if ((v != null) && (v.size() > 0)) {
        bSucceed = new UFBoolean(true);
        StockVO[] stockVOs = (StockVO[])v.elementAt(0);
        SettlebillVO settlebillVO = (SettlebillVO)v.elementAt(1);

        if ((stockVOs != null) && (stockVOs.length > 0)) {
          ICreateCorpQueryService myService0 = null;
          String[] saLockId = (String[])null;
          myService0 = (ICreateCorpQueryService)
            NCLocator.getInstance().lookup(
            ICreateCorpQueryService.class.getName());
          boolean bIAStartUp = myService0.isEnabled(unitCode, "IA");
          if (bIAStartUp)
          {
            if (ufbNeedLock.booleanValue()) {
              listLockId = new ArrayList();
              int iLen = stockVOs.length;
              for (int i = 0; i < iLen; i++) {
                if (stockVOs[i].getCgeneralhid() != null) {
                  listLockId
                    .add(stockVOs[i].getCgeneralhid());
                }
                if (stockVOs[i].getCgeneralbid() != null) {
                  listLockId
                    .add(stockVOs[i].getCgeneralbid());
                }
                if (stockVOs[i].getCgeneralbb3() == null) continue;
                listLockId
                  .add(stockVOs[i].getCgeneralbb3());
              }

              if ((listLockId != null) && (listLockId.size() > 0)) {
                saLockId = new String[listLockId.size()];
                saLockId = 
                  (String[])listLockId
                  .toArray(saLockId);
                bLockSucc = LockTool.setLockForPks(saLockId, 
                  operatorID);
                if (!bLockSucc) {
                  throw new Exception(
                    NCLangResOnserver.getInstance()
                    .getStrByID("40040502", 
                    "UPP40040502-000040"));
                }

              }

            }

            ArrayList listPara = new ArrayList();
            listPara.add(sEstMode);
            listPara.add(sDiffMode);
            listPara.add(settlebillVO);
            listPara.add(operatorID);
            listPara.add(currentDate);
            listPara.add(generalHItemVOs);
            listPara.add(null);
            saveBillFromOutter1(listPara);
          }
        }

      }
      else if (bGetBillCodeSucc) {
        try {
          GetSysBillCode billCode1 = new GetSysBillCode();
          billCode1.returnBillNo(voBillCode);
        }
        catch (Exception ex) {
          PubDMO.throwBusinessException(ex);
        }
      }

      timer.addExecutePhase("向存货核算传送数据(总)");

      timer.showAllExecutePhase("入库单生成发票时结算时间分布");
    }
    catch (Exception e)
    {
      if (bGetBillCodeSucc) {
        try {
          GetSysBillCode billCode = new GetSysBillCode();
          billCode.returnBillNo(voBillCode);
        }
        catch (Exception ex) {
          PubDMO.throwBusinessException(e);
        }
      }

      if ((bLockSucc) && (listLockId != null) && (listLockId.size() > 0)) {
        String[] saLockId = new String[listLockId.size()];
        saLockId = (String[])listLockId.toArray(saLockId);
        try {
          LockTool.releaseLockForPks(saLockId, operatorID);
        } catch (Exception ee) {
          PubDMO.throwBusinessException(ee);
        }
      }
      PubDMO.throwBusinessException(e);
    }

    ArrayList list = new ArrayList();
    list.add(listLockId);
    list.add(bSucceed);
    return list;
  }

  private int[] getFeeType(Vector vbodyKey, FeeinvoiceVO[] feeVOs, FeeinvoiceVO[] discountVOs, FeeinvoiceVO[] specialVOs)
  {
    if ((vbodyKey == null) || (vbodyKey.size() == 0))
      return null;
    String[] bodyKey = new String[vbodyKey.size()];
    vbodyKey.copyInto(bodyKey);

    int[] nType = new int[bodyKey.length];
    for (int i = 0; i < bodyKey.length; i++) {
      nType[i] = -1;

      String s1 = bodyKey[i];
      if ((s1 == null) || (s1.trim().length() == 0))
        continue;
      s1 = s1.trim();

      int jLen = feeVOs == null ? 0 : feeVOs.length;
      for (int j = 0; j < jLen; j++) {
        String s2 = feeVOs[j].getCinvoice_bid();
        if ((s2 == null) || (s2.trim().length() == 0))
          continue;
        s2 = s2.trim();
        if (s1.equals(s2)) {
          nType[i] = 0;
          break;
        }
      }

      if (nType[i] < 0)
      {
        for (int j = 0; j < discountVOs.length; j++) {
          String s2 = discountVOs[j].getCinvoice_bid();
          if ((s2 == null) || (s2.trim().length() == 0))
            continue;
          s2 = s2.trim();
          if (s1.equals(s2)) {
            nType[i] = 1;
            break;
          }
        }
      }

      if (nType[i] >= 0)
        continue;
      for (int j = 0; j < discountVOs.length; j++) {
        String s2 = discountVOs[j].getCinvoice_bid();
        if ((s2 == null) || (s2.trim().length() == 0))
          continue;
        s2 = s2.trim();
        if (s1.equals(s2)) {
          nType[i] = 2;
          break;
        }
      }

    }

    return nType;
  }

  private Hashtable isFeeDiscount(String[] cbaseid)
    throws BusinessException
  {
    Hashtable t = new Hashtable();
    try {
      SettleDMO dmo = new SettleDMO();
      boolean[] b = dmo.isFeeDiscount(cbaseid);
      if ((b != null) && (b.length > 0))
        for (int i = 0; i < b.length; i++)
          t.put(cbaseid[i], new UFBoolean(b[i]));
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return t;
  }

  private void onDis_DoRegret(SettlebillVO vo, String strPkCorp, String strOprId, CostfactorVO[] factors)
    throws BusinessException
  {
    if ((vo == null) || (vo.getBodyVO() == null) || (vo.getBodyVO().length <= 0)) {
      SCMEnv.out("结算单或结算单表体为NULL，直接返回!");
      return;
    }

    Timer timer = new Timer();
    timer.start();

    SettlebillItemVO[] items = vo.getBodyVO();
    int iLen = items.length;

    String strTmp = null;
    Vector vStockKey = new Vector();
    for (int i = 0; i < iLen; i++) {
      strTmp = items[i].getCstockrow();
      if ((strTmp == null) || (strTmp.length() == 0) || 
        (vStockKey.contains(strTmp)))
        continue;
      vStockKey.addElement(strTmp);
    }

    timer.addExecutePhase("入库单行主键归类");

    Hashtable tFeeDiscount = null;
    if ((items != null) && (iLen > 0)) {
      Vector vTemp = new Vector();
      for (int i = 0; i < iLen; i++) {
        strTmp = items[i].getCinvoice_bid();
        if ((strTmp != null) && (strTmp.trim().length() > 0))
          vTemp.addElement(items[i].getCbaseid());
      }
      if (vTemp.size() > 0) {
        String[] saTmp = new String[vTemp.size()];
        vTemp.copyInto(saTmp);
        tFeeDiscount = isFeeDiscount(saTmp);
      }
    }
    timer.addExecutePhase("判断发票是否为费用/折扣");

    Vector vInvoiceKey = new Vector();
    Vector vFeeKey = new Vector();
    for (int i = 0; i < iLen; i++) {
      strTmp = items[i].getCinvoice_bid();
      if ((strTmp == null) || (strTmp.length() == 0)) {
        continue;
      }
      String cbaseid = items[i].getCbaseid();
      UFBoolean b = (UFBoolean)tFeeDiscount.get(cbaseid);
      if (b.booleanValue()) {
        if (!vFeeKey.contains(strTmp)) {
          vFeeKey.addElement(strTmp);
        }
      }
      else if (!vInvoiceKey.contains(strTmp)) {
        vInvoiceKey.addElement(strTmp);
      }
    }

    timer.addExecutePhase("发票行主键归类（包括普通和费用、折扣两类）");

    ArrayList batchList = null;
    String[] stockBodyKey = new String[vStockKey.size()];
    vStockKey.copyInto(stockBodyKey);
    String[] invoiceBodyKey = new String[vInvoiceKey.size()];
    vInvoiceKey.copyInto(invoiceBodyKey);
    String[] feeBodyKey = new String[vFeeKey.size()];
    vFeeKey.copyInto(feeBodyKey);

    batchList = queryDetailBatchForDispose(strPkCorp, stockBodyKey, 
      invoiceBodyKey, feeBodyKey);

    timer.addExecutePhase("查询入库单、发票、费用发票");

    Vector v0 = new Vector();
    if ((batchList != null) && (batchList.size() == 3)) {
      ArrayList list = (ArrayList)batchList.get(2);
      if ((list != null) && (list.size() > 0)) {
        FeeinvoiceVO[] feeTempVOs = (FeeinvoiceVO[])list.get(0);
        if ((feeTempVOs != null) && (feeTempVOs.length > 0)) {
          for (int i = 0; i < feeTempVOs.length; i++) {
            v0.addElement(feeTempVOs[i]);
          }
        }
      }
    }
    FeeinvoiceVO[] feeTempVOs = new FeeinvoiceVO[v0.size()];
    v0.copyInto(feeTempVOs);

    timer.addExecutePhase("查询费用发票,确定结算单的费用发票是否进入成本");

    UFBoolean[] m_bIsentercost = (UFBoolean[])null;
    if ((feeTempVOs != null) && (feeTempVOs.length > 0))
    {
      Vector vBCost = new Vector();
      Vector vTemp = new Vector();
      vBCost.addElement(factors[0].getHeadVO().getBisentercost());
      vTemp.addElement(factors[0].getHeadVO().getCcostfactorid().trim());
      for (int i = 1; i < factors.length; i++) {
        String s1 = factors[i].getHeadVO().getCcostfactorid().trim();
        if (!vTemp.contains(s1)) {
          vTemp.addElement(s1);
          vBCost.addElement(factors[i].getHeadVO().getBisentercost());
        }
      }
      m_bIsentercost = new UFBoolean[vBCost.size()];
      vBCost.copyInto(m_bIsentercost);
    }

    timer.addExecutePhase("记录费用是否进入成本");

    StockVO[] stockVOs = (StockVO[])null;
    if ((batchList != null) && (batchList.size() == 3)) {
      Object oTemp = batchList.get(0);
      if ((oTemp != null) && (vStockKey != null) && (vStockKey.size() > 0)) {
        stockVOs = (StockVO[])oTemp;
        stockVOs = onDis_RestoreStock(vStockKey, items, stockVOs);
      }
    }

    timer.addExecutePhase("恢复入库单数据");

    IinvoiceVO[] iinvoiceVOs = (IinvoiceVO[])null;
    if ((batchList != null) && (batchList.size() == 3)) {
      Object oTemp = batchList.get(1);
      if ((oTemp != null) && (vInvoiceKey != null) && (vInvoiceKey.size() > 0)) {
        iinvoiceVOs = (IinvoiceVO[])oTemp;
        iinvoiceVOs = onDis_RestoreInvoice(vInvoiceKey, items, 
          iinvoiceVOs, m_bIsentercost);
      }
    }

    timer.addExecutePhase("恢复发票数据");

    FeeinvoiceVO[] feeVOs = (FeeinvoiceVO[])null;
    FeeinvoiceVO[] discountVOs = (FeeinvoiceVO[])null;
    FeeinvoiceVO[] specialVOs = (FeeinvoiceVO[])null;
    if ((vFeeKey != null) && (vFeeKey.size() > 0)) {
      Vector v = null;
      if ((batchList != null) && (batchList.size() == 3)) {
        Object oTemp = batchList.get(2);
        if (oTemp != null) {
          ArrayList list0 = (ArrayList)oTemp;
          v = onDis_RestoreFee(vFeeKey, items, list0);
        }
      }

      if ((v == null) || (v.size() < 3)) {
        throw new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("40040502", 
          "UPP40040502-000041"));
      }

      Vector v1 = (Vector)v.elementAt(0);
      Vector v2 = (Vector)v.elementAt(1);
      Vector v3 = (Vector)v.elementAt(2);
      feeVOs = new FeeinvoiceVO[v1.size()];
      v1.copyInto(feeVOs);
      discountVOs = new FeeinvoiceVO[v2.size()];
      v2.copyInto(discountVOs);
      specialVOs = new FeeinvoiceVO[v3.size()];
      v3.copyInto(specialVOs);
    }
    timer.addExecutePhase("恢复费用发票和折扣数据");

    ArrayList listPara = new ArrayList();
    listPara.add(vo);
    listPara.add(stockVOs);
    listPara.add(iinvoiceVOs);
    listPara.add(feeVOs);
    listPara.add(discountVOs);
    listPara.add(specialVOs);
    listPara.add(strOprId);
    listPara.add(strPkCorp);

    discardSettlebill(listPara);

    timer.addExecutePhase("作废操作（总）");

    timer.showAllExecutePhase("结算单作废（核心算法）时间分布");
  }

  private Vector onDis_RestoreFee(Vector vFeeKey, SettlebillItemVO[] VOs, ArrayList list)
    throws BusinessException
  {
    String[] bodyKey = new String[vFeeKey.size()];
    vFeeKey.copyInto(bodyKey);

    if ((list == null) || (list.size() == 0)) {
      throw new BusinessException("找不到费用发票和折扣！");
    }
    FeeinvoiceVO[] feeVO = (FeeinvoiceVO[])list.get(0);
    FeeinvoiceVO[] discountVO = (FeeinvoiceVO[])list.get(1);
    FeeinvoiceVO[] specialVO = (FeeinvoiceVO[])list.get(2);

    int[] nType = getFeeType(vFeeKey, feeVO, discountVO, specialVO);
    if ((nType == null) || (nType.length == 0)) {
      throw new BusinessException("找不到费用发票种类！");
    }

    Vector v = new Vector();
    Vector v1 = new Vector();
    Vector v2 = new Vector();
    Vector v3 = new Vector();

    for (int i = 0; i < vFeeKey.size(); i++) {
      String s = (String)vFeeKey.elementAt(i);
      double nSettlemny = 0.0D;
      for (int j = 0; j < VOs.length; j++) {
        String ss = VOs[j].getCinvoice_bid();
        if ((ss == null) || (ss.length() == 0))
          continue;
        ss = ss.trim();
        if (s.equals(ss)) {
          nSettlemny += VOs[j].getNmoney().doubleValue();
        }

      }

      if (nType[i] == 1)
      {
        for (int j = 0; j < discountVO.length; j++) {
          String ss = discountVO[j].getCinvoice_bid();
          if ((ss == null) || (ss.trim().length() == 0))
            continue;
          ss = ss.trim();
          if (s.equals(ss)) {
            double d = discountVO[j].getNaccumsettlemny()
              .doubleValue();
            d -= nSettlemny;
            discountVO[j].setNaccumsettlemny(new UFDouble(d));

            v2.addElement(discountVO[j]);
          }
        }

      }
      else if (nType[i] == 0)
      {
        for (int j = 0; j < feeVO.length; j++) {
          String ss = feeVO[j].getCinvoice_bid();
          if ((ss == null) || (ss.trim().length() == 0))
            continue;
          ss = ss.trim();
          if (s.equals(ss)) {
            double d = feeVO[j].getNaccumsettlemny().doubleValue();
            d -= nSettlemny;
            feeVO[j].setNaccumsettlemny(new UFDouble(d));

            v1.addElement(feeVO[j]);
          }
        }

      }
      else if (nType[i] == 2)
      {
        for (int j = 0; j < specialVO.length; j++) {
          String ss = specialVO[j].getCinvoice_bid();
          if ((ss == null) || (ss.trim().length() == 0))
            continue;
          ss = ss.trim();
          if (s.equals(ss)) {
            double d = specialVO[j].getNaccumsettlemny()
              .doubleValue();
            d -= nSettlemny;
            specialVO[j].setNaccumsettlemny(new UFDouble(d));

            v3.addElement(specialVO[j]);
          }
        }
      }
      else
      {
        throw new BusinessException(
          NCLangResOnserver.getInstance().getStrByID(
          "40040502", "UPP40040502-000042"));
      }

    }

    v.addElement(v1);
    v.addElement(v2);
    v.addElement(v3);
    return v;
  }

  private IinvoiceVO[] onDis_RestoreInvoice(Vector vInvoiceKey, SettlebillItemVO[] VOs, IinvoiceVO[] invoiceVOs, UFBoolean[] m_bIsentercost)
    throws BusinessException
  {
    String[] bodyKey = new String[vInvoiceKey.size()];
    vInvoiceKey.copyInto(bodyKey);

    if ((invoiceVOs == null) || (invoiceVOs.length == 0)) {
      throw new BusinessException(
        NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000043"));
    }

    Vector v = new Vector();
    for (int i = 0; i < vInvoiceKey.size(); i++) {
      String s = (String)vInvoiceKey.elementAt(i);
      double nSettlenum = 0.0D;
      double nSettlemny = 0.0D;
      for (int j = 0; j < VOs.length; j++) {
        String ss = VOs[j].getCinvoice_bid();
        if ((ss == null) || (ss.length() == 0))
          continue;
        ss = ss.trim();
        if (s.equals(ss)) {
          nSettlenum += VOs[j].getNsettlenum().doubleValue();
          if (VOs[j].getNreasonalwastnum() != null)
          {
            nSettlenum = nSettlenum + VOs[j].getNreasonalwastnum()
              .doubleValue();
          }nSettlemny += VOs[j].getNmoney().doubleValue();
          if (VOs[j].getNreasonalwastmny() != null) nSettlemny += VOs[j].getNreasonalwastmny().doubleValue();

          UFDouble d1 = null;
          UFDouble d2 = VOs[j].getNsettledisctmny();
          if ((m_bIsentercost != null) && (m_bIsentercost.length > 0)) {
            double sum = 0.0D;
            for (int k = 0; k < m_bIsentercost.length; k++) {
              if ((k == 0) && (m_bIsentercost[k].booleanValue()) && 
                (VOs[j].getNfactor1() != null))
                sum += VOs[j].getNfactor1().doubleValue();
              if ((k == 1) && (m_bIsentercost[k].booleanValue()) && 
                (VOs[j].getNfactor2() != null))
                sum += VOs[j].getNfactor2().doubleValue();
              if ((k == 2) && (m_bIsentercost[k].booleanValue()) && 
                (VOs[j].getNfactor3() != null))
                sum += VOs[j].getNfactor3().doubleValue();
              if ((k == 3) && (m_bIsentercost[k].booleanValue()) && 
                (VOs[j].getNfactor4() != null))
                sum += VOs[j].getNfactor4().doubleValue();
              if ((k != 4) || (!m_bIsentercost[k].booleanValue()) || 
                (VOs[j].getNfactor5() == null)) continue;
              sum += VOs[j].getNfactor5().doubleValue();
            }
            d1 = new UFDouble(sum);
          }
          if (d1 != null)
            nSettlemny -= d1.doubleValue();
          if (d2 != null) {
            nSettlemny -= d2.doubleValue();
          }

        }

      }

      for (int j = 0; j < invoiceVOs.length; j++) {
        String ss = invoiceVOs[j].getCinvoice_bid();
        if ((ss == null) || (ss.length() == 0))
          continue;
        ss = ss.trim();
        if (s.equals(ss)) {
          double d = invoiceVOs[j].getNaccumsettlenum().doubleValue();
          d -= nSettlenum;
          invoiceVOs[j].setNaccumsettlenum(new UFDouble(d));
          d = invoiceVOs[j].getNaccumsettlemny().doubleValue();
          d -= nSettlemny;
          invoiceVOs[j].setNaccumsettlemny(new UFDouble(d));

          v.addElement(invoiceVOs[j]);
        }
      }
    }

    if (v.size() > 0) {
      IinvoiceVO[] vos = new IinvoiceVO[v.size()];
      v.copyInto(vos);
      return vos;
    }

    return null;
  }

  private StockVO[] onDis_RestoreStock(Vector vStockKey, SettlebillItemVO[] VOs, StockVO[] stockVOs)
    throws BusinessException
  {
    String[] bodyKey = new String[vStockKey.size()];
    vStockKey.copyInto(bodyKey);

    if ((stockVOs == null) || (stockVOs.length == 0)) {
      SCMEnv.out("没有入库单数据！恢复入库单数据方法直接返回 NULL ！");
      return null;
    }
    Vector v = new Vector();
    for (int i = 0; i < vStockKey.size(); i++) {
      String s = (String)vStockKey.elementAt(i);
      double nSettlenum = 0.0D;
      double nGaugemny = 0.0D;
      double nWastenum = 0.0D;
      for (int j = 0; j < VOs.length; j++) {
        String ss = VOs[j].getCstockrow();
        if ((ss == null) || (ss.length() == 0))
          continue;
        ss = ss.trim();
        if (s.equals(ss)) {
          nSettlenum += VOs[j].getNsettlenum().doubleValue();
          nGaugemny += VOs[j].getNgaugemny().doubleValue();
          nWastenum += VOs[j].getNreasonalwastnum().doubleValue();
        }

      }

      for (int j = 0; j < stockVOs.length; j++) {
        String ss = stockVOs[j].getCgeneralbid();
        if ((ss == null) || (ss.length() == 0))
          continue;
        ss = ss.trim();
        if (s.equals(ss)) {
          double d = stockVOs[j].getNaccumsettlenum().doubleValue();
          d -= nSettlenum;
          stockVOs[j].setNaccumsettlenum(new UFDouble(d));
          d = stockVOs[j].getNaccumsettlemny().doubleValue();
          d -= nGaugemny;
          stockVOs[j].setNaccumsettlemny(new UFDouble(d));

          v.addElement(stockVOs[j]);
        }
      }
    }

    if (v.size() > 0) {
      StockVO[] vos = new StockVO[v.size()];
      v.copyInto(vos);
      return vos;
    }

    return null;
  }

  public ArrayList onDiscard(ArrayList listPara)
    throws BusinessException
  {
    ICreateCorpQueryService myService0 = null;
    Timer timer = new Timer();
    timer.start();
    try
    {
      if ((listPara == null) || (listPara.size() < 3)) {
        SCMEnv.out("程序BUG，参数传入不正确，直接返回NULL!");
        return null;
      }
      SettlebillVO[] vos = (SettlebillVO[])listPara.get(0);
      String strPkCorp = (String)listPara.get(1);
      String strOprId = (String)listPara.get(2);
      CostfactorVO[] factors = 
        (CostfactorVO[])listPara
        .get(3);
      int iLen = vos.length;

      myService0 = (ICreateCorpQueryService)NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());
      boolean bAPStartUp = myService0.isEnabled(strPkCorp, "AP");
      GetSysBillCode billCode = new GetSysBillCode();
      for (int i = 0; i < vos.length; i++) {
        billCode.returnBillNo(vos[i]);

        SettlebillItemVO[] bodyVO = vos[i].getBodyVO();
        boolean bVirtual = false;
        Vector vTemp = new Vector();
        for (int j = 0; j < bodyVO.length; j++) {
          String s = bodyVO[j].getCinvoiceid();
          if ((s == null) || (s.trim().length() <= 0) || (vTemp.contains(s))) continue; vTemp.addElement(s);
        }
        if (vTemp.size() > 0) {
          String[] cInvoiceID = new String[vTemp.size()];
          vTemp.copyInto(cInvoiceID);
          cInvoiceID = new SettleDMO().getVirtualInvoices(cInvoiceID);
          if ((cInvoiceID != null) && (cInvoiceID.length > 0)) bVirtual = true;
        }

        boolean bIsFromVMI = isFromVMI(vos[i]);

        if ((bIsFromVMI) || 
          (!bAPStartUp))
          continue;
        String[] s = new String[bodyVO.length];
        for (int j = 0; j < s.length; j++) s[j] = bodyVO[j].getCinvoiceid();
        if (bVirtual) {
          deleteBillForARAP(s, strOprId);
        }

      }

      ArrayList listHid = new ArrayList();
      ArrayList listHts = new ArrayList();
      String[] saHid = (String[])null;
      String[] saHts = (String[])null;
      for (int i = 0; i < iLen; i++) {
        if ((vos[i].getBodyVO() != null) && 
          (vos[i].getBodyVO().length > 0)) continue;
        listHid.add(vos[i].getHeadVO().getPrimaryKey());
        listHts.add(vos[i].getHeadVO().getTs());
      }

      if (listHid.size() > 0) {
        int iLenNullBody = listHid.size();
        saHid = new String[iLenNullBody];
        listHid.toArray(saHid);
        saHts = new String[iLenNullBody];
        listHts.toArray(saHts);
        ArrayList listAllBody = querySettleBodyBatch(strPkCorp, saHid, 
          saHts, new UFBoolean(false));
        if ((listAllBody == null) || (listAllBody.size() != iLenNullBody)) {
          throw new BusinessException(
            NCLangResOnserver.getInstance().getStrByID("40040502", 
            "UPP40040502-000044"));
        }

        int iPos = 0;
        for (int i = 0; i < iLen; i++) {
          if ((vos[i].getBodyVO() != null) && 
            (vos[i].getBodyVO().length > 0)) continue;
          vos[i].setChildrenVO(
            (SettlebillItemVO[])listAllBody
            .get(iPos));
          iPos++;
        }
      }

      timer.addExecutePhase("查询前台未加载过的表体数据");

      for (int i = 0; i < iLen; i++) {
        onDis_DoRegret(vos[i], strPkCorp, strOprId, factors);
      }
      timer.addExecutePhase("作废操作");

      timer.showAllExecutePhase("作废结算单时间分布");
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return null;
  }

  public Vector getInitParaForSalesUI(String a_strUnitCode)
    throws BusinessException
  {
    Vector l_vecReturn = new Vector();
    try
    {
      ISysInitQry l_SysInitDMO = (ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName());

      SysInitVO l_initVO = l_SysInitDMO.queryByParaCode(
        a_strUnitCode, "PO20");
      SysInitVO l_tempVO = l_SysInitDMO.queryByParaCode(
        a_strUnitCode, "BD301");

      SysInitVO[] l_aryinitVO = (SysInitVO[])null;

      SysInitVO[] l_arynumVO = (SysInitVO[])null;

      SysInitVO[] l_arypriceVO = (SysInitVO[])null;

      Hashtable t = l_SysInitDMO.queryBatchParaValues(
        a_strUnitCode, new String[] { "PO12", "PO13", "PO02", "PO03", "PO04", "PO05" });
      if ((t != null) && (t.size() > 0)) {
        SysInitVO tempVO1 = new SysInitVO();
        tempVO1.setInitcode("PO12");
        tempVO1.setValue(t.get("PO12").toString());

        SysInitVO tempVO2 = new SysInitVO();
        tempVO2.setInitcode("PO13");
        tempVO2.setValue(t.get("PO13").toString());
        l_aryinitVO = new SysInitVO[] { tempVO1, tempVO2 };

        SysInitVO tempVO3 = new SysInitVO();
        tempVO3.setInitcode("PO02");
        tempVO3.setValue(t.get("PO02").toString());
        SysInitVO tempVO4 = new SysInitVO();
        tempVO4.setInitcode("PO03");
        tempVO4.setValue(t.get("PO03").toString());
        l_arynumVO = new SysInitVO[] { tempVO3, tempVO4 };

        SysInitVO tempVO5 = new SysInitVO();
        tempVO5.setInitcode("PO04");
        tempVO5.setValue(t.get("PO04").toString());
        SysInitVO tempVO6 = new SysInitVO();
        tempVO6.setInitcode("PO05");
        tempVO6.setValue(t.get("PO05").toString());
        l_arypriceVO = new SysInitVO[] { tempVO5, tempVO6 };
      }

      ICreateCorpQueryService l_CreatecorpBO = (ICreateCorpQueryService)
        NCLocator.getInstance().lookup(
        ICreateCorpQueryService.class.getName());

      boolean bSOStartUp = l_CreatecorpBO.isEnabled(a_strUnitCode, "SO");

      boolean bICStartUp = l_CreatecorpBO.isEnabled(a_strUnitCode, "IC");

      l_vecReturn.addElement(l_initVO);
      l_vecReturn.addElement(l_tempVO);
      l_vecReturn.addElement(l_aryinitVO);
      l_vecReturn.addElement(l_arynumVO);
      l_vecReturn.addElement(l_arypriceVO);
      l_vecReturn.addElement(new Boolean(bSOStartUp));
      l_vecReturn.addElement(new Boolean(bICStartUp));
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }

    return l_vecReturn;
  }

  public ArrayList querySettleBodyBatch(String unitCode, String[] key, String[] ts, UFBoolean ufbNeedFindOthers)
    throws BusinessException
  {
    SettlebillItemVO[] bodyVO = (SettlebillItemVO[])null;
    try
    {
      SettleDMO dmo = new SettleDMO();
      bodyVO = dmo.querySettleBodyBatch(unitCode, key, ts);

      if (ufbNeedFindOthers.booleanValue())
      {
        Vector vBodyStock = new Vector();
        Vector vBodyVmi = new Vector();
        int iLen = bodyVO.length;
        for (int i = 0; i < iLen; i++) {
          if (bodyVO[i].getCvmiid() != null)
            vBodyVmi.addElement(bodyVO[i]);
          else {
            vBodyStock.addElement(bodyVO[i]);
          }
        }

        SettlebillItemVO[] bodys = (SettlebillItemVO[])null;
        int iLenSpec = 0;
        String[] saBillCode = (String[])null; String[] saVendorName = (String[])null;

        Vector vAllWithName = new Vector();

        if (vBodyStock.size() > 0) {
          bodys = new SettlebillItemVO[vBodyStock.size()];
          vBodyStock.copyInto(bodys);
          iLenSpec = bodys.length;

          saBillCode = dmo.queryStockCode(bodys);

          String[] saKey = new String[iLenSpec];
          for (int j = 0; j < iLenSpec; j++)
            saKey[j] = bodys[j].getCstockid();
          ArrayList list2 = dmo.queryVendorName(saKey);
          saVendorName = (String[])list2.get(0);
          String[] cDeptName = (String[])list2.get(1);
          String[] cEmployeeName = (String[])list2.get(2);
          for (int j = 0; j < iLenSpec; j++) {
            bodys[j].setVbillcode(saBillCode[j]);
            bodys[j].setCvendorname(saVendorName[j]);
            bodys[j].setCdeptname(cDeptName[j]);
            bodys[j].setCemployeename(cEmployeeName[j]);
            vAllWithName.addElement(bodys[j]);
          }
        }

        if (vBodyVmi.size() > 0) {
          bodys = new SettlebillItemVO[vBodyVmi.size()];
          vBodyVmi.copyInto(bodys);
          iLenSpec = bodys.length;

          saBillCode = dmo.queryVMIBillCode(bodys);

          String[] saKey = new String[iLenSpec];
          for (int j = 0; j < iLenSpec; j++)
            saKey[j] = bodys[j].getCvmiid();
          saVendorName = dmo.queryVendorName_Vmi(saKey);
          for (int j = 0; j < iLenSpec; j++) {
            bodys[j].setVbillcode(saBillCode[j]);
            bodys[j].setCvendorname(saVendorName[j]);
            vAllWithName.addElement(bodys[j]);
          }
        }

        bodyVO = new SettlebillItemVO[vAllWithName.size()];
        vAllWithName.copyInto(bodyVO);
        iLen = bodyVO.length;
        saBillCode = dmo.queryInvoiceCode(bodyVO);
        if (saBillCode != null)
          for (int i = 0; i < iLen; i++) {
            if (saBillCode[i] == null)
              continue;
            bodyVO[i].setVinvoicecode(saBillCode[i]);
          }
      }
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    ArrayList list = new ArrayList();
    for (int i = 0; i < key.length; i++) {
      Vector v = new Vector();
      String s1 = key[i].trim();
      for (int j = 0; j < bodyVO.length; j++) {
        String s2 = bodyVO[j].getCsettlebillid().trim();
        if (s1.equals(s2))
          v.addElement(bodyVO[j]);
      }
      SettlebillItemVO[] bodyVOs = new SettlebillItemVO[v.size()];
      v.copyInto(bodyVOs);
      if ((bodyVOs == null) || (bodyVOs.length == 0))
        return null;
      list.add(bodyVOs);
    }
    return list;
  }

  public StockVO[] queryStockForNone(ArrayList list, ConditionVO[] conditionVO)
    throws BusinessException
  {
    String sUnitCode = (String)list.get(0);
    UFBoolean bShow = (UFBoolean)list.get(1);
    StockVO[] vo = (StockVO[])null;
    try {
      SettleDMO dmo = new SettleDMO();

      String sCondition = "";

      ArrayList listRet = dealCondVosForPower(conditionVO);
      conditionVO = (ConditionVO[])listRet.get(0);
      String strDataPowerSql = (String)listRet.get(1);

      int iLen = conditionVO == null ? 0 : conditionVO.length;
      for (int i = 0; i < iLen; i++) {
        String sName = conditionVO[i].getFieldCode().trim();
        String sOpera = conditionVO[i].getOperaCode().trim();
        String sValue = conditionVO[i].getValue();
        String sSQL = conditionVO[i].getSQLStr();
        String sReplace = null;
        if ((sName.equals("dbilldate")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "dbilldate " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("ddate")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "dbilldate " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("vbillcode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "vbillcode " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cbaseid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "B.cinvbasid in (select pk_invbasdoc from bd_invbasdoc where invcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cinvclassid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          try {
            EstimateDMO ddmo = new EstimateDMO();
            String[] sClassCode = ddmo.getSubInvClassCode(sValue);
            if ((sClassCode != null) && (sClassCode.length > 0)) {
              sValue = "(";
              for (int j = 0; j < sClassCode.length; j++) {
                if (j < sClassCode.length - 1)
                  sValue = sValue + "invclasscode " + sOpera + " '" + 
                    sClassCode[j] + "' or ";
                else
                  sValue = sValue + "invclasscode " + sOpera + " '" + 
                    sClassCode[j] + "')";
              }
              sReplace = "B.cinvbasid in (select pk_invbasdoc from bd_invbasdoc A,bd_invcl C where A.pk_invcl = C.pk_invcl and " + 
                sValue + ")";
            } else {
              sReplace = "B.cinvbasid in (select pk_invbasdoc from bd_invbasdoc A,bd_invcl C where A.pk_invcl = C.pk_invcl and invclasscode " + 
                sOpera + " '" + sValue + "')";
            }
            String s = getReplacedSQL(sSQL, sName, sReplace);
            sCondition = sCondition + s;
          }
          catch (Exception e) {
            SCMEnv.out(e);
            return null;
          }
        } else if ((sName.equals("cvendorbaseid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cproviderid in (select pk_cumandoc from bd_cumandoc A, bd_cubasdoc B where custcode " + 
            sOpera + 
            " '" + 
            sValue + 
            "'and A.pk_cubasdoc = B.pk_cubasdoc and (custflag = '1' or custflag = '3'))";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cdeptid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cdptid in (select pk_deptdoc from bd_deptdoc where deptcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cbiztype")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cbiztype in (select pk_busitype from bd_busitype where busicode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("vordercode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cfirstbillhid in (select corderid from po_order where vordercode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.indexOf("ic_general_b.vuserdef") >= 0) && 
          (sValue != null) && (sValue.length() > 0)) {
          sReplace = "B." + sName.substring(sName.indexOf(".") + 1) + 
            " " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.indexOf("vuserdef") >= 0) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "A." + sName + " " + sOpera + " '" + sValue + 
            "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.equals("vcontractcode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cfirstbillhid in (select corderid from po_order_b A, ct_manage B where ccontractid = pk_ct_manage and ct_code " + 
            sOpera + 
            " '" + 
            sValue + 
            "' and A.dr = 0 and B.dr = 0)";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.indexOf("ic_general_b.vnotebody") >= 0) && 
          (sValue != null) && (sValue.length() > 0)) {
          sReplace = "B." + sName.substring(sName.indexOf(".") + 1) + 
            " " + sOpera;
          if ("like".equalsIgnoreCase(sOpera))
            sReplace = sReplace + " '%" + sValue + "%'";
          else {
            sReplace = sReplace + " '" + sValue + "'";
          }
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.equals("pk_stockcorp")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "A.pk_corp in (select pk_corp from bd_corp where unitcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.indexOf("vinvoicecode") >= 0) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "B.cgeneralbid in (select B.cupsourcebillrowid from po_invoice A, po_invoice_b B where A.cinvoiceid = B.cinvoiceid and vinvoicecode " + 
            sOpera + 
            " '" + 
            sValue + 
            "' and A.dr = 0 and B.dr = 0)";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.equals("cwarehouseid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cwarehouseid in (select pk_stordoc from bd_stordoc where storcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else {
          if (((!sName.equals("coperatorid")) && (!sName.equals("coperator"))) || (sValue == null) || 
            (sValue.length() <= 0)) continue;
          sReplace = "A.coperatorid in (select cuserid from sm_user where user_code " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
      }

      sCondition = PubDMO.processFirst(sCondition);

      sCondition = sCondition + " and upper(flargess) = 'N'";

      sCondition = sCondition + " and upper(isok) = 'N'";

      sCondition = sCondition + " and coalesce(S.iscalculatedinvcost,'N') = 'Y' ";

      sCondition = sCondition + " and BT.verifyrule not in ('V','S','Z','N') ";

      if (bShow.booleanValue()) {
        sCondition = sCondition + " and (nprice is null or nprice = 0) ";
      }
      if (strDataPowerSql != null) sCondition = sCondition + " and " + strDataPowerSql;

      vo = dmo.queryStock(sUnitCode, sCondition);
      Vector vTemp = new Vector();
      if ((vo != null) && (vo.length > 0))
      {
        vo = dmo.preDealStockVO(sUnitCode, vo);

        for (int i = 0; i < vo.length; i++)
          vTemp.addElement(vo[i].getCprovidermangid());
        String[] sVendorMangID = new String[vTemp.size()];
        vTemp.copyInto(sVendorMangID);
        String[] sVendorBaseID = getVendorBaseKey(sVendorMangID);

        vTemp = new Vector();
        for (int i = 0; i < vo.length; i++) {
          vo[i].setCproviderbaseid(sVendorBaseID[i]);
          UFDouble d1 = vo[i].getNinnum();
          UFDouble d2 = vo[i].getNaccumsettlenum();
          UFDouble d3 = vo[i].getNsignnum();
          if ((d1 != null) && (d2 != null) && (d3 != null)) {
            if (Math.abs(d2.doubleValue()) < Math.abs(d3.doubleValue())) d2 = d3;
            double d = d1.doubleValue() - d2.doubleValue();
            vo[i].setNnosettlenum(new UFDouble(d));
          } else {
            vo[i].setNnosettlenum(new UFDouble(0));
          }
          d1 = vo[i].getNmoney();
          d2 = vo[i].getNaccumsettlemny();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            vo[i].setNnosettlemny(new UFDouble(d));
          }

          if (Math.abs(vo[i].getNnosettlenum().doubleValue()) <= VMIDMO.getDigitRMB(sUnitCode).doubleValue()) continue; vTemp.addElement(vo[i]);
        }
      }

      if (vTemp.size() > 0) {
        vo = new StockVO[vTemp.size()];
        vTemp.copyInto(vo);
      } else {
        vo = (StockVO[])null;
      }
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    return vo;
  }

  public void isUnauditableForSale(String csaleid) throws BusinessException {
    try {
      new SettleDMO().isUnauditableForSale(csaleid);
    } catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
  }

  public void estimateForPartSettle(StockVO[] VOs, EstimateVO[] estVO, String cOperator, UFDate dCurrDate, UFBoolean bZGYF)
    throws BusinessException
  {
    EstimateDMO dmo1 = null;
    ICreateCorpQueryService myService0 = null;
    PubImpl myService1 = null;
    ISysInitQry myService2 = null;
    try
    {
      dmo1 = new EstimateDMO();

      myService1 = new PubImpl();
      int[] digit = myService1.getDigitBatch(VOs[0].getPk_corp(), new String[] { "BD301", "BD505" });
      if ((digit == null) || (digit.length == 0))
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040503", "UPP40040503-000008"));
      myService2 = (ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName());
      String sCurrTypeID = myService2.getPkValue(VOs[0].getPk_corp(), "BD301");

      Vector vAllBodyVoB = new Vector();
      Vector vAllBodyVoBB3 = new Vector();
      for (int i = 0; i < VOs.length; i++) {
        GeneralHItemVO bodyVO = new GeneralHItemVO();
        bodyVO.setCgeneralbid(VOs[i].getCgeneralbid());
        bodyVO.setBzgflag(new UFBoolean(true));
        bodyVO.setDzgdate(dCurrDate);
        if (bZGYF.booleanValue()) bodyVO.setBzgyfflag(bZGYF);
        vAllBodyVoB.addElement(bodyVO);

        GeneralBb3VO bb3VO = new GeneralBb3VO();
        bb3VO.setCgeneralbid(VOs[i].getCgeneralbid());
        if (VOs[i].getNprice() != null) {
          double d = PubDMO.getRoundDouble(digit[1], VOs[i].getNprice().doubleValue());
          bb3VO.setNpprice(new UFDouble(d));
          if ((estVO != null) && (estVO.length > 0)) {
            d = PubDMO.getRoundDouble(digit[1], estVO[i].getNtaxprice().doubleValue());
            bb3VO.setNzygfprice(new UFDouble(d));
          }
        }
        if (VOs[i].getNmoney() != null) {
          double d = PubDMO.getRoundDouble(digit[0], VOs[i].getNmoney().doubleValue());
          bb3VO.setNpmoney(new UFDouble(d));
          if ((estVO != null) && (estVO.length > 0)) {
            d = PubDMO.getRoundDouble(digit[0], estVO[i].getNtotalmoney().doubleValue());
            bb3VO.setNzgyfmoney(new UFDouble(d));
          }
        }
        vAllBodyVoBB3.addElement(bb3VO);
      }

      if (vAllBodyVoB.size() > 0) {
        GeneralHItemVO[] bodyVOs = new GeneralHItemVO[vAllBodyVoB.size()];
        vAllBodyVoB.copyInto(bodyVOs);
        dmo1.updateBillBody(bodyVOs);
      }

      if (vAllBodyVoBB3.size() > 0) {
        GeneralBb3VO[] bb3VOs = new GeneralBb3VO[vAllBodyVoBB3.size()];
        vAllBodyVoBB3.copyInto(bb3VOs);
        dmo1.updateBillBb3(bb3VOs);
      }

      if ((VOs != null) && (VOs.length > 0)) {
        String unitCode = VOs[0].getPk_corp();
        myService0 = (ICreateCorpQueryService)NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());
        boolean bIAStartUp = myService0.isEnabled(unitCode, "IA");

        if (bIAStartUp)
        {
          saveBillFromOutterForPartSettle(VOs, cOperator, dCurrDate, new int[] { digit[0] });
        }

        if ((bZGYF.booleanValue()) && (myService0.isEnabled(unitCode, "AP")))
        {
          IEstimate myService3 = (IEstimate)NCLocator.getInstance().lookup(IEstimate.class.getName());
          ArrayList list = new ArrayList();
          list.add(sCurrTypeID);
          list.add(cOperator);
          list.add(dCurrDate);
          myService3.saveBillForARAP(estVO, list);
        }
      }
    } catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
  }

  public EstimateVO[] switchVOForZGYF(StockVO[] VOs, UFBoolean bZGYF)
    throws BusinessException
  {
    Vector vTemp = new Vector();
    for (int i = 0; i < VOs.length; i++) {
      EstimateVO tempVO = new EstimateVO();
      tempVO.setPk_corp(VOs[i].getPk_stockcorp());
      tempVO.setCprovidermangid(VOs[i].getCprovidermangid());
      tempVO.setCgeneralhid(VOs[i].getCgeneralhid());
      tempVO.setCbiztype(VOs[i].getCbiztype());
      tempVO.setDbilldate(VOs[i].getDbizdate());
      tempVO.setCgeneralbid(VOs[i].getCgeneralbid());
      tempVO.setCproviderbaseid(VOs[i].getCproviderbaseid());
      tempVO.setCbaseid(VOs[i].getCbaseid());
      tempVO.setCmangid(VOs[i].getCmangid());

      tempVO.setNinnum(VOs[i].getNinnum());
      tempVO.setNprice(VOs[i].getNprice());
      tempVO.setNmoney(VOs[i].getNmoney());
      tempVO.setNtaxprice(VOs[i].getNtaxprice());
      tempVO.setNtotalmoney(VOs[i].getNtotalmoney());

      tempVO.setCfirsttype(VOs[i].getCfirsttype());
      tempVO.setCfirstbillbid(VOs[i].getCfirstbillbid());
      tempVO.setCfirstbillhid(VOs[i].getCfirstbillhid());
      tempVO.setCbilltypecode(VOs[i].getCbilltype());

      tempVO.setVuserdefh1(VOs[i].getVuserdefh1());
      tempVO.setVuserdefh2(VOs[i].getVuserdefh2());
      tempVO.setVuserdefh3(VOs[i].getVuserdefh3());
      tempVO.setVuserdefh4(VOs[i].getVuserdefh4());
      tempVO.setVuserdefh5(VOs[i].getVuserdefh5());
      tempVO.setVuserdefh6(VOs[i].getVuserdefh6());
      tempVO.setVuserdefh7(VOs[i].getVuserdefh7());
      tempVO.setVuserdefh8(VOs[i].getVuserdefh8());
      tempVO.setVuserdefh9(VOs[i].getVuserdefh9());
      tempVO.setVuserdefh10(VOs[i].getVuserdefh10());

      tempVO.setVuserdefh11(VOs[i].getVuserdefh11());
      tempVO.setVuserdefh12(VOs[i].getVuserdefh12());
      tempVO.setVuserdefh13(VOs[i].getVuserdefh13());
      tempVO.setVuserdefh14(VOs[i].getVuserdefh14());
      tempVO.setVuserdefh15(VOs[i].getVuserdefh15());
      tempVO.setVuserdefh16(VOs[i].getVuserdefh16());
      tempVO.setVuserdefh17(VOs[i].getVuserdefh17());
      tempVO.setVuserdefh18(VOs[i].getVuserdefh18());
      tempVO.setVuserdefh19(VOs[i].getVuserdefh19());
      tempVO.setVuserdefh20(VOs[i].getVuserdefh20());

      tempVO.setPk_defdoch1(VOs[i].getPk_defdoch1());
      tempVO.setPk_defdoch2(VOs[i].getPk_defdoch2());
      tempVO.setPk_defdoch3(VOs[i].getPk_defdoch3());
      tempVO.setPk_defdoch4(VOs[i].getPk_defdoch4());
      tempVO.setPk_defdoch5(VOs[i].getPk_defdoch5());
      tempVO.setPk_defdoch6(VOs[i].getPk_defdoch6());
      tempVO.setPk_defdoch7(VOs[i].getPk_defdoch7());
      tempVO.setPk_defdoch8(VOs[i].getPk_defdoch8());
      tempVO.setPk_defdoch9(VOs[i].getPk_defdoch9());
      tempVO.setPk_defdoch10(VOs[i].getPk_defdoch10());

      tempVO.setPk_defdoch11(VOs[i].getPk_defdoch11());
      tempVO.setPk_defdoch12(VOs[i].getPk_defdoch12());
      tempVO.setPk_defdoch13(VOs[i].getPk_defdoch13());
      tempVO.setPk_defdoch14(VOs[i].getPk_defdoch14());
      tempVO.setPk_defdoch15(VOs[i].getPk_defdoch15());
      tempVO.setPk_defdoch16(VOs[i].getPk_defdoch16());
      tempVO.setPk_defdoch17(VOs[i].getPk_defdoch17());
      tempVO.setPk_defdoch18(VOs[i].getPk_defdoch18());
      tempVO.setPk_defdoch19(VOs[i].getPk_defdoch19());
      tempVO.setPk_defdoch20(VOs[i].getPk_defdoch20());

      tempVO.setVuserdef1(VOs[i].getVuserdefb1());
      tempVO.setVuserdef2(VOs[i].getVuserdefb2());
      tempVO.setVuserdef3(VOs[i].getVuserdefb3());
      tempVO.setVuserdef4(VOs[i].getVuserdefb4());
      tempVO.setVuserdef5(VOs[i].getVuserdefb5());
      tempVO.setVuserdef6(VOs[i].getVuserdefb6());
      tempVO.setVuserdef7(VOs[i].getVuserdefb7());
      tempVO.setVuserdef8(VOs[i].getVuserdefb8());
      tempVO.setVuserdef9(VOs[i].getVuserdefb9());
      tempVO.setVuserdef10(VOs[i].getVuserdefb10());

      tempVO.setVuserdef11(VOs[i].getVuserdefb11());
      tempVO.setVuserdef12(VOs[i].getVuserdefb12());
      tempVO.setVuserdef13(VOs[i].getVuserdefb13());
      tempVO.setVuserdef14(VOs[i].getVuserdefb14());
      tempVO.setVuserdef15(VOs[i].getVuserdefb15());
      tempVO.setVuserdef16(VOs[i].getVuserdefb16());
      tempVO.setVuserdef17(VOs[i].getVuserdefb17());
      tempVO.setVuserdef18(VOs[i].getVuserdefb18());
      tempVO.setVuserdef19(VOs[i].getVuserdefb19());
      tempVO.setVuserdef20(VOs[i].getVuserdefb20());

      tempVO.setPk_defdocb1(VOs[i].getPk_defdocb1());
      tempVO.setPk_defdocb2(VOs[i].getPk_defdocb2());
      tempVO.setPk_defdocb3(VOs[i].getPk_defdocb3());
      tempVO.setPk_defdocb4(VOs[i].getPk_defdocb4());
      tempVO.setPk_defdocb5(VOs[i].getPk_defdocb5());
      tempVO.setPk_defdocb6(VOs[i].getPk_defdocb6());
      tempVO.setPk_defdocb7(VOs[i].getPk_defdocb7());
      tempVO.setPk_defdocb8(VOs[i].getPk_defdocb8());
      tempVO.setPk_defdocb9(VOs[i].getPk_defdocb9());
      tempVO.setPk_defdocb10(VOs[i].getPk_defdocb10());

      tempVO.setPk_defdocb11(VOs[i].getPk_defdocb11());
      tempVO.setPk_defdocb12(VOs[i].getPk_defdocb12());
      tempVO.setPk_defdocb13(VOs[i].getPk_defdocb13());
      tempVO.setPk_defdocb14(VOs[i].getPk_defdocb14());
      tempVO.setPk_defdocb15(VOs[i].getPk_defdocb15());
      tempVO.setPk_defdocb16(VOs[i].getPk_defdocb16());
      tempVO.setPk_defdocb17(VOs[i].getPk_defdocb17());
      tempVO.setPk_defdocb18(VOs[i].getPk_defdocb18());
      tempVO.setPk_defdocb19(VOs[i].getPk_defdocb19());
      tempVO.setPk_defdocb20(VOs[i].getPk_defdocb20());

      tempVO.setCprojectid(VOs[i].getCprojectid());
      tempVO.setCprojectphaseid(VOs[i].getCprojectphaseid());
      tempVO.setCdptid(VOs[i].getCdeptid());
      tempVO.setCoperatorid(VOs[i].getCoperatorid());

      tempVO.setBMoney(VOs[i].getBMoney());
      tempVO.setNPricePolicy(VOs[i].getNPricePolicy());

      vTemp.addElement(tempVO);
    }

    EstimateVO[] estimates = new EstimateVO[vTemp.size()];
    vTemp.copyInto(estimates);

    vTemp = new Vector();
    Vector v1 = new Vector();
    for (int i = 0; i < estimates.length; i++) {
      if ((estimates[i].getCfirsttype() != null) && (estimates[i].getCfirsttype().equals("21"))) {
        if (vTemp.contains(estimates[i].getCfirstbillbid())) continue; vTemp.addElement(estimates[i].getCfirstbillbid());
      } else {
        if (v1.contains(estimates[i].getCbaseid())) continue; v1.addElement(estimates[i].getCbaseid());
      }
    }

    String[] sID = (String[])null;
    if (vTemp.size() > 0) {
      sID = new String[vTemp.size()];
      vTemp.copyInto(sID);
    }
    Hashtable hTemp = null; Hashtable h1 = null;
    try {
      if ((sID != null) && (sID.length > 0)) hTemp = new EstimateDMO().queryOrderTaxRate(sID);
      sID = (String[])null;
      if (v1.size() > 0) {
        sID = new String[v1.size()];
        v1.copyInto(sID);
      }
      if ((sID != null) && (sID.length > 0)) h1 = new EstimateDMO().queryInvTaxRate(sID); 
    }
    catch (Exception e) {
      throw new BusinessException(e.getMessage());
    }

    int[] descriptions = { 
      0, 1, 
      2, 
      7, 
      13, 
      8, 
      14, 
      11, 
      15, 
      9, 
      18, 
      12 };

    ISysInitQry myService = (ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName());
    String sPricePolicy = myService.getParaString(VOs[0].getPk_corp(), "PO28");
    int nPricePolicy = 6;
    if ((sPricePolicy != null) && (sPricePolicy.equals("含税价格优先"))) nPricePolicy = 5;

    String sChangedKey = "nprice";

    for (int i = 0; i < estimates.length; i++) {
      if ((estimates[i].getCfirsttype() != null) && 
        (estimates[i].getCfirsttype().equals("21")) && 
        (hTemp.get(estimates[i].getCfirstbillbid()) != null))
      {
        Object[] oTemp = (Object[])null;
        if ((hTemp != null) && (hTemp.size() > 0)) oTemp = (Object[])hTemp.get(estimates[i].getCfirstbillbid());
        UFDouble nTaxRate = new UFDouble(0);
        Integer iDiscountTaxType = new Integer(1);
        if ((oTemp != null) && (oTemp[0] != null)) nTaxRate = new UFDouble(oTemp[0].toString());
        if ((oTemp != null) && (oTemp[1] != null)) iDiscountTaxType = (Integer)oTemp[1];
        estimates[i].setNtaxrate(nTaxRate);
        estimates[i].setIdiscounttaxtype(iDiscountTaxType);
      }
      else
      {
        Object oTemp = null;
        if ((hTemp != null) && (hTemp.size() > 0)) oTemp = h1.get(estimates[i].getCbaseid());
        UFDouble nTaxRate = new UFDouble(0);
        Integer iDiscountTaxType = new Integer(1);
        if (oTemp != null) nTaxRate = new UFDouble(oTemp.toString());
        estimates[i].setNtaxrate(nTaxRate);
        estimates[i].setIdiscounttaxtype(iDiscountTaxType);
      }

      String s = "应税内含";
      if (estimates[i].getIdiscounttaxtype().intValue() == 1) s = "应税外加";
      if (estimates[i].getIdiscounttaxtype().intValue() == 2) s = "不计税";

      String[] keys = { 
        s, 
        "idiscounttaxtype", 
        "ninnum", 
        "nprice", 
        "nmoney", 
        "ntaxprice", 
        "ntotalmoney", 
        "ntaxrate", 
        "ndiscountrate", 
        "nnetprice", 
        "nnettaxprice", 
        "ntaxmoney" };

      if (estimates[i].getNPricePolicy() == 5) {
        if ((estimates[i].getBMoney() != null) && (estimates[i].getBMoney().booleanValue())) sChangedKey = "ntotalmoney"; else
          sChangedKey = "ntaxprice";
      }
      else if ((estimates[i].getBMoney() != null) && (estimates[i].getBMoney().booleanValue())) sChangedKey = "nmoney"; else {
        sChangedKey = "nprice";
      }

      SCMRelationsCal.calculate(estimates[i], new int[] { nPricePolicy }, sChangedKey, descriptions, keys);
    }

    return estimates;
  }

  private EstimateVO calculateTaxPriceForEstimateVO(EstimateVO VO, UFDouble nTaxRate, Integer iDiscountTaxType)
  {
    UFDouble nNum = VO.getNinnum();
    UFDouble nPrice = VO.getNprice();
    UFDouble nMoney = VO.getNmoney();

    UFDouble nTaxPrice = nPrice;
    UFDouble nTotalMoney = nMoney;

    if (iDiscountTaxType.intValue() == 0)
    {
      double d = nMoney.doubleValue() * nTaxRate.doubleValue() / 100.0D / (1.0D - nTaxRate.doubleValue() / 100.0D);
      nTotalMoney = new UFDouble(d);
      d /= nNum.doubleValue();
      nTaxPrice = new UFDouble(d);
    }
    else if (iDiscountTaxType.intValue() == 1)
    {
      double d = nMoney.doubleValue() * nTaxRate.doubleValue() / 100.0D + nMoney.doubleValue();
      nTotalMoney = new UFDouble(d);
      d /= nNum.doubleValue();
      nTaxPrice = new UFDouble(d);
    }

    VO.setNtaxprice(nTaxPrice);
    VO.setNtotalmoney(nTotalMoney);
    return VO;
  }

  private void saveBillFromOutterForPartSettle(StockVO[] VOs, String cOperator, UFDate dCurrDate, int[] nMnyDecimal)
    throws BusinessException
  {
    IIAToPUBill myService = null;
    try
    {
      myService = (IIAToPUBill)NCLocator.getInstance().lookup(IIAToPUBill.class.getName());
      BillVO[] billVOs = transferIADataForPartSettle(VOs, cOperator, dCurrDate, nMnyDecimal);

      Vector v = new Vector();
      Vector vTemp0 = new Vector();
      vTemp0.addElement(((IBillHeaderVO)billVOs[0].getParentVO()).getCbillid().trim());
      v.addElement(billVOs[0]);
      for (int i = 1; i < billVOs.length; i++) {
        String s1 = ((IBillHeaderVO)billVOs[i].getParentVO()).getCbillid().trim();
        if (!vTemp0.contains(s1)) {
          vTemp0.addElement(s1);
          v.addElement(billVOs[i]);
        }

      }

      Vector vv = new Vector();
      for (int i = 0; i < v.size(); i++) {
        BillVO tempVO = (BillVO)v.elementAt(i);
        String s1 = ((IBillHeaderVO)tempVO.getParentVO()).getCbillid().trim();
        Vector vTemp = new Vector();
        for (int j = 0; j < billVOs.length; j++) {
          String s2 = ((IBillHeaderVO)billVOs[j].getParentVO()).getCbillid().trim();
          if (s1.equals(s2)) {
            IBillItemVO[] tempItemVO = (IBillItemVO[])billVOs[j].getChildrenVO();
            for (int k = 0; k < tempItemVO.length; k++) {
              vTemp.addElement(tempItemVO[k]);
            }
          }
        }
        IBillItemVO[] tempItemVOs = new IBillItemVO[vTemp.size()];
        vTemp.copyInto(tempItemVOs);

        for (int j = 0; j < tempItemVOs.length; j++) {
          tempItemVOs[j].setIrownumber(new Integer(j));
        }
        tempVO.setChildrenVO(tempItemVOs);
        vv.addElement(tempVO);
      }

      billVOs = (BillVO[])null;
      billVOs = new BillVO[vv.size()];
      vv.copyInto(billVOs);

      if ((billVOs == null) || (billVOs.length == 0))
        return;
      String s1 = ((IBillHeaderVO)billVOs[0].getParentVO()).getCsourcemodulename();
      String s2 = ((IBillItemVO[])billVOs[0].getChildrenVO())[0].getCsourcebilltypecode();

      String sTime = new UFDateTime(new Date()).toString();
      for (int i = 0; i < billVOs.length; i++) {
        ((IBillHeaderVO)billVOs[i].getParentVO()).setVbillcode(null);
        ((IBillHeaderVO)billVOs[i].getParentVO()).setTmaketime(sTime);
        ((IBillHeaderVO)billVOs[i].getParentVO()).setTlastmaketime(sTime);
        ((IBillHeaderVO)billVOs[i].getParentVO()).setClastoperatorid(cOperator);
        IBillItemVO[] bodyVO = (IBillItemVO[])billVOs[i].getChildrenVO();
        if ((bodyVO != null) && (bodyVO.length > 0)) {
          for (int j = 0; j < bodyVO.length; j++) bodyVO[j].setVbillcode(null);
        }
      }

      myService.saveBillFromOutterArray(billVOs, s1, s2);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
  }

  private BillVO[] transferIADataForPartSettle(StockVO[] VOs, String cOperator, UFDate dCurrDate, int[] nMnyDecimal)
    throws BusinessException
  {
    if ((VOs == null) || (VOs.length == 0)) {
      SCMEnv.out("传入参数为空，直接返回NULL；调用堆栈如下：");
      SCMEnv.out(new Exception());
      return null;
    }
    BillVO[] billVO = (BillVO[])null;
    int iLen = VOs.length;
    try {
      if ((nMnyDecimal == null) || (nMnyDecimal.length == 0)) {
        nMnyDecimal = new int[1];
        nMnyDecimal[0] = 2;
      }

      IBillItemVO[] bodyVO = new IBillItemVO[iLen];
      for (int i = 0; i < bodyVO.length; i++) {
        bodyVO[i] = new IBillItemVO();

        bodyVO[i].setPk_corp(VOs[i].getPk_corp());
        bodyVO[i].setCbilltypecode("I2");
        bodyVO[i].setCbill_bid(VOs[i].getCgeneralbid());
        bodyVO[i].setCbillid(VOs[i].getCgeneralhid());

        bodyVO[i].setCsourcebillid(VOs[i].getCgeneralhid());
        bodyVO[i].setCsourcebillitemid(VOs[i].getCgeneralbid());
        bodyVO[i].setCsourcebilltypecode("45");
        bodyVO[i].setFpricemodeflag(null);

        bodyVO[i].setCicbilltype(VOs[i].getCbilltype());
        bodyVO[i].setCicbillcode(VOs[i].getVbillcode());
        bodyVO[i].setCicbillid(VOs[i].getCgeneralhid());
        bodyVO[i].setCicitemid(VOs[i].getCgeneralbid());

        bodyVO[i].setCinventoryid(VOs[i].getCmangid());
        bodyVO[i].setVbatch(VOs[i].getVbatchcode());
        bodyVO[i].setNnumber(VOs[i].getNinnum());

        UFDouble d1 = VOs[i].getNmoney();
        UFDouble d2 = null;
        if (d1 == null)
          d1 = new UFDouble(0.0D);
        if (d2 == null)
          d2 = new UFDouble(0.0D);
        double d = d1.doubleValue() + d2.doubleValue();
        bodyVO[i].setNmoney(new UFDouble(PubDMO.getRoundDouble(nMnyDecimal[0], d)));
        bodyVO[i].setNdrawsummny(d2);

        d1 = bodyVO[i].getNmoney();
        d2 = bodyVO[i].getNnumber();
        if ((d2 != null) && (d2.doubleValue() != 0.0D)) {
          d = d1.doubleValue() / d2.doubleValue();
          bodyVO[i].setNprice(new UFDouble(d));
        }
        bodyVO[i].setNplanedprice(null);
        bodyVO[i].setNplanedmny(null);

        d1 = bodyVO[i].getNmoney();
        d2 = bodyVO[i].getNplanedmny();
        if (d1 == null)
          d1 = new UFDouble(0.0D);
        if (d2 == null)
          d2 = new UFDouble(0.0D);
        d = d1.doubleValue() - d2.doubleValue();
        bodyVO[i].setNinvarymny(null);

        bodyVO[i].setCastunitid(VOs[i].getCastunitid());
        bodyVO[i].setNchangerate((bodyVO[i].getCastunitid() == null) || (bodyVO[i].getCastunitid().trim().equals("")) ? null : VOs[i].getHsl());

        bodyVO[i].setCfirstbillid(VOs[i].getCfirstbillhid());

        bodyVO[i].setCfirstbillitemid(VOs[i].getCfirstbillbid());
        bodyVO[i].setCfirstbilltypecode(VOs[i].getCfirsttype());
        bodyVO[i].setIrownumber(new Integer(i));
        bodyVO[i].setVbillcode(VOs[i].getVbillcode());
        bodyVO[i].setVsourcebillcode(VOs[i].getVbillcode());

        bodyVO[i].setVfree1(VOs[i].getVfree1());
        bodyVO[i].setVfree2(VOs[i].getVfree2());
        bodyVO[i].setVfree3(VOs[i].getVfree3());
        bodyVO[i].setVfree4(VOs[i].getVfree4());
        bodyVO[i].setVfree5(VOs[i].getVfree5());

        bodyVO[i].setCprojectid(VOs[i].getCprojectid());
        bodyVO[i].setCprojectphase(VOs[i].getCprojectphaseid());

        bodyVO[i].setVdef1(VOs[i].getVuserdefb1());
        bodyVO[i].setVdef2(VOs[i].getVuserdefb2());
        bodyVO[i].setVdef3(VOs[i].getVuserdefb3());
        bodyVO[i].setVdef4(VOs[i].getVuserdefb4());
        bodyVO[i].setVdef5(VOs[i].getVuserdefb5());
        bodyVO[i].setVdef6(VOs[i].getVuserdefb6());
        bodyVO[i].setVdef7(VOs[i].getVuserdefb7());
        bodyVO[i].setVdef8(VOs[i].getVuserdefb8());
        bodyVO[i].setVdef9(VOs[i].getVuserdefb9());
        bodyVO[i].setVdef10(VOs[i].getVuserdefb10());

        bodyVO[i].setVdef11(VOs[i].getVuserdefb11());
        bodyVO[i].setVdef12(VOs[i].getVuserdefb12());
        bodyVO[i].setVdef13(VOs[i].getVuserdefb13());
        bodyVO[i].setVdef14(VOs[i].getVuserdefb14());
        bodyVO[i].setVdef15(VOs[i].getVuserdefb15());
        bodyVO[i].setVdef16(VOs[i].getVuserdefb16());
        bodyVO[i].setVdef17(VOs[i].getVuserdefb17());
        bodyVO[i].setVdef18(VOs[i].getVuserdefb18());
        bodyVO[i].setVdef19(VOs[i].getVuserdefb19());
        bodyVO[i].setVdef20(VOs[i].getVuserdefb20());

        bodyVO[i].setPk_defdoc1(VOs[i].getPk_defdocb1());
        bodyVO[i].setPk_defdoc2(VOs[i].getPk_defdocb2());
        bodyVO[i].setPk_defdoc3(VOs[i].getPk_defdocb3());
        bodyVO[i].setPk_defdoc4(VOs[i].getPk_defdocb4());
        bodyVO[i].setPk_defdoc5(VOs[i].getPk_defdocb5());
        bodyVO[i].setPk_defdoc6(VOs[i].getPk_defdocb6());
        bodyVO[i].setPk_defdoc7(VOs[i].getPk_defdocb7());
        bodyVO[i].setPk_defdoc8(VOs[i].getPk_defdocb8());
        bodyVO[i].setPk_defdoc9(VOs[i].getPk_defdocb9());
        bodyVO[i].setPk_defdoc10(VOs[i].getPk_defdocb10());

        bodyVO[i].setPk_defdoc11(VOs[i].getPk_defdocb11());
        bodyVO[i].setPk_defdoc12(VOs[i].getPk_defdocb12());
        bodyVO[i].setPk_defdoc13(VOs[i].getPk_defdocb13());
        bodyVO[i].setPk_defdoc14(VOs[i].getPk_defdocb14());
        bodyVO[i].setPk_defdoc15(VOs[i].getPk_defdocb15());
        bodyVO[i].setPk_defdoc16(VOs[i].getPk_defdocb16());
        bodyVO[i].setPk_defdoc17(VOs[i].getPk_defdocb17());
        bodyVO[i].setPk_defdoc18(VOs[i].getPk_defdocb18());
        bodyVO[i].setPk_defdoc19(VOs[i].getPk_defdocb19());
        bodyVO[i].setPk_defdoc20(VOs[i].getPk_defdocb20());

        bodyVO[i].setDbizdate(VOs[i].getDbizdate());
        bodyVO[i].setCvendorid(VOs[i].getCvendorid());
      }

      Vector vDistVo = new Vector();
      HashMap hDistKey = new HashMap();
      String strHid = null;
      for (int i = 0; i < iLen; i++) {
        if (VOs[i] == null) {
          continue;
        }
        strHid = VOs[i].getCgeneralhid();
        if ((strHid == null) || (hDistKey.containsKey(strHid))) {
          continue;
        }
        vDistVo.addElement(VOs[i]);
        hDistKey.put(strHid, "");
      }
      hDistKey = null;
      int iRealCnt = vDistVo.size();
      IBillHeaderVO[] headVO = new IBillHeaderVO[iRealCnt];
      StockVO Vo = null;
      for (int i = 0; i < iRealCnt; i++) {
        headVO[i] = new IBillHeaderVO();
        Vo = (StockVO)vDistVo.elementAt(i);
        headVO[i].setPk_corp(Vo.getPk_corp());
        if (Vo.getCbilltype().trim().equals("47"))
          headVO[i].setCbilltypecode("ID");
        else {
          headVO[i].setCbilltypecode("I2");
        }
        headVO[i].setCbillid(Vo.getCgeneralhid());

        headVO[i].setDbilldate(dCurrDate);
        headVO[i].setCsourcemodulename("PO");
        headVO[i].setFdispatchflag(new Integer(0));
        String s = Vo.getCdispatcherid();
        if ((s != null) && (s.length() > 0))
          headVO[i].setCdispatchid(Vo.getCdispatcherid());
        else {
          headVO[i].setCdispatchid(null);
        }
        headVO[i].setCbiztypeid(Vo.getCbiztype());
        headVO[i].setCrdcenterid(Vo.getCstoreorganization());
        headVO[i].setCwarehouseid(Vo.getCwarehouseid());

        headVO[i].setCdeptid(Vo.getCdeptid());
        headVO[i].setCoperatorid(cOperator);
        headVO[i].setCcustomvendorid(Vo.getCprovidermangid());

        headVO[i].setVnote(Vo.getVnote());
        headVO[i].setBestimateflag(new UFBoolean(true));
        headVO[i].setCwarehousemanagerid(Vo.getCwhsmanagerid());
        headVO[i].setCemployeeid(Vo.getCbizid());

        headVO[i].setVbillcode(Vo.getVbillcode());
        headVO[i].setBwithdrawalflag(new UFBoolean(false));
        headVO[i].setBdisableflag(new UFBoolean(false));
        headVO[i].setBauditedflag(new UFBoolean(false));

        headVO[i].setVdef1(Vo.getVuserdefh1());
        headVO[i].setVdef2(Vo.getVuserdefh2());
        headVO[i].setVdef3(Vo.getVuserdefh3());
        headVO[i].setVdef4(Vo.getVuserdefh4());
        headVO[i].setVdef5(Vo.getVuserdefh5());
        headVO[i].setVdef6(Vo.getVuserdefh6());
        headVO[i].setVdef7(Vo.getVuserdefh7());
        headVO[i].setVdef8(Vo.getVuserdefh8());
        headVO[i].setVdef9(Vo.getVuserdefh9());
        headVO[i].setVdef10(Vo.getVuserdefh10());

        headVO[i].setVdef11(Vo.getVuserdefh11());
        headVO[i].setVdef12(Vo.getVuserdefh12());
        headVO[i].setVdef13(Vo.getVuserdefh13());
        headVO[i].setVdef14(Vo.getVuserdefh14());
        headVO[i].setVdef15(Vo.getVuserdefh15());
        headVO[i].setVdef16(Vo.getVuserdefh16());
        headVO[i].setVdef17(Vo.getVuserdefh17());
        headVO[i].setVdef18(Vo.getVuserdefh18());
        headVO[i].setVdef19(Vo.getVuserdefh19());
        headVO[i].setVdef20(Vo.getVuserdefh20());

        headVO[i].setPk_defdoc1(Vo.getPk_defdoch1());
        headVO[i].setPk_defdoc2(Vo.getPk_defdoch2());
        headVO[i].setPk_defdoc3(Vo.getPk_defdoch3());
        headVO[i].setPk_defdoc4(Vo.getPk_defdoch4());
        headVO[i].setPk_defdoc5(Vo.getPk_defdoch5());
        headVO[i].setPk_defdoc6(Vo.getPk_defdoch6());
        headVO[i].setPk_defdoc7(Vo.getPk_defdoch7());
        headVO[i].setPk_defdoc8(Vo.getPk_defdoch8());
        headVO[i].setPk_defdoc9(Vo.getPk_defdoch9());
        headVO[i].setPk_defdoc10(Vo.getPk_defdoch10());

        headVO[i].setPk_defdoc11(Vo.getPk_defdoch11());
        headVO[i].setPk_defdoc12(Vo.getPk_defdoch12());
        headVO[i].setPk_defdoc13(Vo.getPk_defdoch13());
        headVO[i].setPk_defdoc14(Vo.getPk_defdoch14());
        headVO[i].setPk_defdoc15(Vo.getPk_defdoch15());
        headVO[i].setPk_defdoc16(Vo.getPk_defdoch16());
        headVO[i].setPk_defdoc17(Vo.getPk_defdoch17());
        headVO[i].setPk_defdoc18(Vo.getPk_defdoch18());
        headVO[i].setPk_defdoc19(Vo.getPk_defdoch19());
        headVO[i].setPk_defdoc20(Vo.getPk_defdoch20());
      }

      billVO = new BillVO[headVO.length];
      for (int i = 0; i < billVO.length; i++) {
        billVO[i] = new BillVO();
        billVO[i].setParentVO(headVO[i]);

        Vector vTemp = new Vector();
        String s1 = headVO[i].getCbillid().trim();
        for (int j = 0; j < bodyVO.length; j++) {
          String s2 = bodyVO[j].getCbillid().trim();
          if (s1.equals(s2)) {
            bodyVO[j].setCbilltypecode(headVO[i].getCbilltypecode());
            if (headVO[i].getCbilltypecode().equals("ID")) {
              bodyVO[j].setCsourcebilltypecode("47");
            }
            vTemp.addElement(bodyVO[j]);
          }
        }
        IBillItemVO[] tempBodyVO = new IBillItemVO[vTemp.size()];
        vTemp.copyInto(tempBodyVO);

        billVO[i].setChildrenVO(tempBodyVO);
      }
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }

    return billVO;
  }

  public StockVO[] queryStock_yfn(String sUnitCode, ConditionVO[] conditionVOs)
    throws BusinessException
  {
    StockVO[] vo = (StockVO[])null;
    try {
      SettleDMO dmo = new SettleDMO();

      String sCondition = "";

      ArrayList listRet = dealCondVosForPower(conditionVOs);
      conditionVOs = (ConditionVO[])listRet.get(0);
      String strDataPowerSql = (String)listRet.get(1);

      int iLen = conditionVOs == null ? 0 : conditionVOs.length;
      for (int i = 0; i < iLen; i++) {
        String sName = conditionVOs[i].getFieldCode().trim();
        String sOpera = conditionVOs[i].getOperaCode().trim();
        String sValue = conditionVOs[i].getValue();
        String sSQL = conditionVOs[i].getSQLStr();
        String sReplace = null;

        if ((sName.equals("dbilldate")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "dbilldate " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("ddate")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "dbilldate " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("vbillcode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "vbillcode " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cbaseid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "B.cinvbasid in (select pk_invbasdoc from bd_invbasdoc where invcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cinvclassid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          try {
            EstimateDMO ddmo = new EstimateDMO();
            String[] sClassCode = ddmo.getSubInvClassCode(sValue);

            if ((sClassCode != null) && (sClassCode.length > 0)) {
              sValue = "(";
              for (int j = 0; j < sClassCode.length; j++) {
                if (j < sClassCode.length - 1)
                  sValue = sValue + "invclasscode " + sOpera + " '" + 
                    sClassCode[j] + "' or ";
                else
                  sValue = sValue + "invclasscode " + sOpera + " '" + 
                    sClassCode[j] + "')";
              }
              sReplace = "B.cinvbasid in (select pk_invbasdoc from bd_invbasdoc A,bd_invcl C where A.pk_invcl = C.pk_invcl and " + 
                sValue + ")";
            } else {
              sReplace = "B.cinvbasid in (select pk_invbasdoc from bd_invbasdoc A,bd_invcl C where A.pk_invcl = C.pk_invcl and invclasscode " + 
                sOpera + " '" + sValue + "')";
            }
            String s = getReplacedSQL(sSQL, sName, sReplace);
            sCondition = sCondition + s;
          }
          catch (Exception e) {
            SCMEnv.out(e);
            return null;
          }
        } else if ((sName.equals("cvendorbaseid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cproviderid in (select pk_cumandoc from bd_cumandoc A, bd_cubasdoc B where custcode " + 
            sOpera + 
            " '" + 
            sValue + 
            "'and A.pk_cubasdoc = B.pk_cubasdoc and (custflag = '1' or custflag = '3'))";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cdeptid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cdptid in (select pk_deptdoc from bd_deptdoc where deptcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cbiztype")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cbiztype in (select pk_busitype from bd_busitype where busicode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("vordercode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cfirstbillhid in (select corderid from po_order where vordercode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.indexOf("ic_general_b.vuserdef") >= 0) && 
          (sValue != null) && (sValue.length() > 0)) {
          sReplace = "B." + sName.substring(sName.indexOf(".") + 1) + 
            " " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.indexOf("vuserdef") >= 0) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "A." + sName + " " + sOpera + " '" + sValue + 
            "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if (sName.equalsIgnoreCase("ic_general_b.vnotebody")) {
          sReplace = "B." + sName.substring(sName.indexOf(".") + 1) + 
            " " + sOpera;
          if ("like".equalsIgnoreCase(sOpera))
            sReplace = sReplace + " '%" + sValue + "%'";
          else {
            sReplace = sReplace + " '" + sValue + "'";
          }
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.equals("vcontractcode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cfirstbillhid in (select corderid from po_order_b A, ct_manage B where ccontractid = pk_ct_manage and ct_code " + 
            sOpera + 
            " '" + 
            sValue + 
            "' and A.dr = 0 and B.dr = 0)";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.indexOf("vinvoicecode") >= 0) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "B.cgeneralbid in (select B.cupsourcebillrowid from po_invoice A, po_invoice_b B where A.cinvoiceid = B.cinvoiceid and vinvoicecode " + 
            sOpera + 
            " '" + 
            sValue + 
            "' and A.dr = 0 and B.dr = 0)";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.equals("cwarehouseid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cwarehouseid in (select pk_stordoc from bd_stordoc where storcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.equals("coperatorid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "A.coperatorid in (select cuserid from sm_user where user_code " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else {
          if ((!sName.equals("pk_stockcorp")) || (sValue == null) || 
            (sValue.length() <= 0)) continue;
          sReplace = "A.pk_corp in (select pk_corp from bd_corp where unitcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
      }

      sCondition = PubDMO.processFirst(sCondition);

      sCondition = sCondition + " and coalesce(flargess,'N') = 'N' ";

      sCondition = sCondition + " and (coalesce(S.iscalculatedinvcost,'N') = 'Y' or A.cwarehouseid is null) ";

      sCondition = sCondition + " and BT.verifyrule not in ('V','S','Z','N') ";

      if (strDataPowerSql != null) sCondition = sCondition + " and " + strDataPowerSql;

      vo = dmo.queryStock_yf(sUnitCode, sCondition);
      if ((vo != null) && (vo.length > 0))
      {
        vo = dmo.preDealStockVO(sUnitCode, vo);
        if ((vo == null) || (vo.length == 0)) return null;

        Vector vTemp = new Vector();
        for (int i = 0; i < vo.length; i++)
          vTemp.addElement(vo[i].getCprovidermangid());
        String[] sVendorMangID = new String[vTemp.size()];
        vTemp.copyInto(sVendorMangID);
        String[] sVendorBaseID = getVendorBaseKey(sVendorMangID);

        for (int i = 0; i < vo.length; i++) {
          vo[i].setCproviderbaseid(sVendorBaseID[i]);
          UFDouble d1 = vo[i].getNinnum();
          UFDouble d2 = vo[i].getNaccumsettlenum();

          if ((d2 != null) && (Math.abs(d2.doubleValue()) > 0.0D)) vo[i].setBsettled(new UFBoolean(true)); else {
            vo[i].setBsettled(new UFBoolean(false));
          }
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            vo[i].setNnosettlenum(new UFDouble(d1));
          }
          d1 = vo[i].getNmoney();
          d2 = vo[i].getNaccumsettlemny();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            vo[i].setNnosettlemny(new UFDouble(d1));
          }
        }
      }
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return vo;
  }

  public ArrayList queryAllInvoiceForManual_yfn(ArrayList listPara)
    throws BusinessException
  {
    if ((listPara == null) || (listPara.size() < 3)) {
      SCMEnv.out("程序BUG：查询发票（含费用发票和折扣发票）参数为NULL！");
      return null;
    }
    String sUnitCode = (String)listPara.get(0);
    ConditionVO[] conditionVOs = (ConditionVO[])listPara.get(1);
    UFBoolean bIsVMI = (UFBoolean)listPara.get(2);
    String strDataPower = (String)listPara.get(3);

    if ((sUnitCode == null) || (bIsVMI == null)) {
      SCMEnv.out("程序BUG：查询发票（含费用发票和折扣发票）参数存在NULL！");
      return null;
    }
    ArrayList list = new ArrayList();
    SettleDMO dmo = null;
    try {
      dmo = new SettleDMO();

      String sCondition = "";
      int iLen = conditionVOs == null ? 0 : conditionVOs.length;
      for (int i = 0; i < iLen; i++) {
        String sName = conditionVOs[i].getFieldCode().trim();
        String sOpera = conditionVOs[i].getOperaCode().trim();
        String sValue = conditionVOs[i].getValue();
        String sSQL = conditionVOs[i].getSQLStr();
        String sReplace = null;

        if ((sName.equals("dinvoicedate")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "dinvoicedate " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("ddate")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "dinvoicedate " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("vinvoicecode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "vinvoicecode " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cbaseid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "B.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B where A.pk_invbasdoc = B.pk_invbasdoc and invcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cinvclassid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          try {
            EstimateDMO ddmo = new EstimateDMO();
            String[] sClassCode = ddmo.getSubInvClassCode(sValue);

            if ((sClassCode != null) && (sClassCode.length > 0)) {
              sValue = "(";
              for (int j = 0; j < sClassCode.length; j++) {
                if (j < sClassCode.length - 1)
                  sValue = sValue + "invclasscode " + sOpera + " '" + 
                    sClassCode[j] + "' or ";
                else
                  sValue = sValue + "invclasscode " + sOpera + " '" + 
                    sClassCode[j] + "')";
              }
              sReplace = "B.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and " + 
                sValue + ")";
            } else {
              sReplace = "B.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and invclasscode " + 
                sOpera + " '" + sValue + "')";
            }
            String s = getReplacedSQL(sSQL, sName, sReplace);
            sCondition = sCondition + s;
          }
          catch (Exception e) {
            SCMEnv.out(e);
            return null;
          }
        } else if ((sName.equals("cvendorbaseid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cvendorbaseid in (select A.pk_cubasdoc from bd_cubasdoc A,bd_cumandoc B where A.pk_cubasdoc = B.pk_cubasdoc and custcode " + 
            sOpera + 
            " '" + 
            sValue + 
            "' and (custflag = '1' or custflag = '3'))";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cdeptid")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cdeptid in (select pk_deptdoc from bd_deptdoc where deptcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cbiztype")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "cbiztype in (select pk_busitype from bd_busitype where busicode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("coperator")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "coperator in (select cuserid from sm_user where user_code " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("vordercode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "corderid in (select corderid from po_order where vordercode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.indexOf("po_invoice_b.vdef") >= 0) && 
          (sValue != null) && (sValue.length() > 0)) {
          sReplace = "B." + sName.substring(sName.indexOf(".") + 1) + 
            " " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.indexOf("po_invoice.vdef") >= 0) && 
          (sValue != null) && (sValue.length() > 0)) {
          sReplace = "A." + sName.substring(sName.indexOf(".") + 1) + 
            " " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.equals("vcontractcode")) && (sValue != null) && 
          (sValue.length() > 0)) {
          sReplace = "corderid in (select corderid from po_order_b A, ct_manage B where pk_ct_manage = ccontractid and ct_code " + 
            sOpera + 
            " '" + 
            sValue + 
            "' and A.dr = 0 and B.dr = 0)";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else if ((sName.indexOf("po_invoice_b.vmemo") >= 0) && 
          (sValue != null) && (sValue.length() > 0)) {
          sReplace = "B." + sName.substring(sName.indexOf(".") + 1) + 
            " " + sOpera;
          if ("like".equalsIgnoreCase(sOpera))
            sReplace = sReplace + " '%" + sValue + "%'";
          else {
            sReplace = sReplace + " '" + sValue + "'";
          }
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
        else {
          if ((!sName.equals("cwarehouseid")) || (sValue == null) || 
            (sValue.length() <= 0)) continue;
          sReplace = "cwarehouseid in (select pk_stordoc from bd_stordoc where storcode " + 
            sOpera + " '" + sValue + "')";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
      }

      sCondition = PubDMO.processFirst(sCondition);

      sCondition = sCondition + " and abs(nmoney - naccumsettmny) >= " + 
        VMIDMO.getDigitRMB(sUnitCode);

      if (bIsVMI.booleanValue())
        sCondition = sCondition + " and E.verifyrule = 'V' ";
      else {
        sCondition = sCondition + " and E.verifyrule not in ('V','S') ";
      }

      if (strDataPower != null) sCondition = sCondition + " and " + strDataPower;

      ArrayList listPara1 = new ArrayList();
      listPara1.add(sUnitCode);
      listPara1.add(sCondition + " and abs(isnull(ninvoicenum,0)) > 0 ");
      listPara1.add(new UFBoolean(false));
      listPara1.add(new UFBoolean(true));
      listPara1.add(new UFBoolean(true));
      IinvoiceVO[] VOs = dmo.queryInvoice_yf(listPara1);
      FeeinvoiceVO[] discountVOs = (FeeinvoiceVO[])null;
      FeeinvoiceVO[] feeVOs = (FeeinvoiceVO[])null;

      if ((VOs != null) && (VOs.length > 0))
      {
        for (int i = 0; i < VOs.length; i++) {
          UFDouble d1 = VOs[i].getNinvoicenum();
          UFDouble d2 = VOs[i].getNaccumsettlenum();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            VOs[i].setNnosettlenum(new UFDouble(d));
          }
          d1 = VOs[i].getNmoney();
          d2 = VOs[i].getNaccumsettlemny();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            VOs[i].setNnosettlemny(new UFDouble(d));
          }

          d1 = VOs[i].getNinvoicenum();
          d2 = VOs[i].getNmoney();
          if ((d1 != null) && (d2 != null) && (d1.doubleValue() != 0.0D)) {
            double d = d2.doubleValue() / d1.doubleValue();
            VOs[i].setNprice(new UFDouble(d));
          }
        }
      }

      HashMap mapExistId = null;
      if ((discountVOs != null) && (discountVOs.length > 0)) {
        mapExistId = new HashMap();
        int mLen = discountVOs.length;

        for (int i = 0; i < mLen; i++) {
          mapExistId.put(discountVOs[i].getCinvoice_bid(), "");
          discountVOs[i].setVfactorname("折扣");
          UFDouble d1 = discountVOs[i].getNmny();
          UFDouble d2 = discountVOs[i].getNaccumsettlemny();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            discountVOs[i].setNnosettlemny(new UFDouble(d));
            discountVOs[i].setNsettlemny(new UFDouble(d));
          }
        }

      }

      if ((mapExistId != null) && (mapExistId.size() > 0) && 
        (feeVOs != null) && (feeVOs.length > 0)) {
        Vector vTmp = new Vector();
        int jLen = feeVOs.length;
        for (int i = 0; i < jLen; i++) {
          if (mapExistId.containsKey(feeVOs[i].getCinvoice_bid())) {
            continue;
          }
          vTmp.addElement(feeVOs[i]);
        }
        if (vTmp.size() > 0) {
          feeVOs = new FeeinvoiceVO[vTmp.size()];
          vTmp.copyInto(feeVOs);
        } else {
          feeVOs = (FeeinvoiceVO[])null;
        }

      }

      if ((feeVOs != null) && (feeVOs.length > 0)) {
        for (int i = 0; i < feeVOs.length; i++) {
          UFDouble d1 = feeVOs[i].getNmny();
          UFDouble d2 = feeVOs[i].getNaccumsettlemny();
          if ((d1 != null) && (d2 != null)) {
            double d = d1.doubleValue() - d2.doubleValue();
            feeVOs[i].setNnosettlemny(new UFDouble(d));
            feeVOs[i].setNsettlemny(new UFDouble(d));
          }
        }
      }

      list.add(VOs);
      list.add(feeVOs);
      list.add(discountVOs);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }

    return list;
  }

  public String doManualBalance_yfn(ArrayList listPara)
    throws BusinessException
  {
    if ((listPara == null) || (listPara.size() < 8)) {
      SCMEnv.out("传入参数为空或参数不足，直接返回 NULL！");
      return null;
    }
    Timer timer = new Timer();
    timer.start();

    SettlebillVO settlebillVO = (SettlebillVO)listPara.get(0);
    StockVO[] stockVOs = (StockVO[])listPara.get(1);
    IinvoiceVO[] iinvoiceVOs = (IinvoiceVO[])listPara.get(2);
    FeeinvoiceVO[] feeVOs = (FeeinvoiceVO[])listPara.get(3);
    FeeinvoiceVO[] discountVOs = (FeeinvoiceVO[])listPara.get(4);
    String sEstMode = (String)listPara.get(5);
    String sDiffMode = (String)listPara.get(6);
    String cOperator = (String)listPara.get(7);
    UFBoolean bIc2PiSettle = (UFBoolean)listPara.get(8);
    UFBoolean bZGYF = (UFBoolean)listPara.get(9);
    int nMnyDecimal = ((Integer)listPara.get(10)).intValue();
    UFBoolean bDifferSettle = (UFBoolean)listPara.get(11);

    UFDate ddate = (UFDate)listPara.get(12);

    if (bDifferSettle.booleanValue()) {
      if (bZGYF.booleanValue())
      {
        bIc2PiSettle = new UFBoolean(false);
      }
      bZGYF = new UFBoolean(false);
    }

    SettleDMO dmo = null;
    ICreateCorpQueryService myService0 = null;
    boolean bLock = false;
    String key = null;
    boolean bGetBillCodeSucc = false;

    String[] sKeys = getSettleLockKeys(stockVOs, iinvoiceVOs, feeVOs, 
      discountVOs, null, null, null);
    try {
      dmo = new SettleDMO();

      bLock = LockTool.setLockForPks(sKeys, cOperator);
      if (!bLock) {
        throw new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("40040502", 
          "UPP40040502-000009"));
      }

      timer.addExecutePhase(NCLangResOnserver.getInstance()
        .getStrByID("40040502", "UPP40040502-000010"));

      String sMessage = isTimeStampChangedForSettle(stockVOs, 
        iinvoiceVOs, feeVOs, discountVOs, null);
      if ((sMessage != null) && (sMessage.length() > 0)) {
        sMessage = sMessage + NCLangResOnserver.getInstance()
          .getStrByID("40040502", "UPP40040502-000002");

        throw new BusinessException(sMessage);
      }
      timer.addExecutePhase("判断时间戳是否改变");
      SettlebillHeaderVO head = settlebillVO.getHeadVO();
      head.setTmaketime(new UFDateTime(new Date()).toString());

      String vSettleCode = getSettleBillCode(head);
      head.setAttributeValue("vsettlebillcode", vSettleCode);
      bGetBillCodeSucc = true;

      timer.addExecutePhase("获得结算单号");

      SettlebillItemVO[] body = settlebillVO.getBodyVO();

      if ((bIc2PiSettle.booleanValue()) && (stockVOs != null) && (stockVOs.length > 0)) {
        Vector vTemp = new Vector();
        for (int i = 0; i < stockVOs.length; i++) {
          if ((stockVOs[i].getBzgflag().booleanValue()) || 
            (stockVOs[i].getBsettled().booleanValue()) || 
            (Math.abs(stockVOs[i].getNinnum().doubleValue() - stockVOs[i].getNaccumsettlenum().doubleValue()) <= 0.0D) || 
            (!stockVOs[i].getPk_corp().equals(stockVOs[i].getPk_stockcorp()))) continue;
          vTemp.addElement(stockVOs[i]);
        }
        if (vTemp.size() > 0) {
          StockVO[] zgStockVOs = new StockVO[vTemp.size()];
          vTemp.copyInto(zgStockVOs);

          zgStockVOs = dmo.replacePrice(zgStockVOs);

          for (int i = 0; i < zgStockVOs.length; i++) {
            zgStockVOs[i].setBzgflag(new UFBoolean(true));
            zgStockVOs[i].setDzgdate(head.getDsettledate());
          }

          EstimateVO[] estVO = switchVOForZGYF(zgStockVOs, bZGYF);

          for (int i = 0; i < stockVOs.length; i++) {
            double d3 = 0.0D;
            for (int j = 0; j < zgStockVOs.length; j++) {
              if (stockVOs[i].getCgeneralbid().equals(zgStockVOs[j].getCgeneralbid())) {
                if (estVO != null) {
                  zgStockVOs[j].setNprice(estVO[j].getNprice());
                  zgStockVOs[j].setNmoney(estVO[j].getNmoney());
                }
                stockVOs[i] = zgStockVOs[j];
                break;
              }
            }
            double d1 = stockVOs[i].getNprice().doubleValue();
            for (int j = 0; j < body.length; j++) {
              if ((body[j].getCstockrow() == null) || 
                (!body[j].getCstockrow().equals(stockVOs[i].getCgeneralbid()))) continue;
              double d2 = body[j].getNsettlenum().doubleValue();
              d3 += d2;
              body[j].setNgaugemny(new UFDouble(PubDMO.getRoundDouble(nMnyDecimal, d1 * d2)));
              break;
            }

            d3 *= stockVOs[i].getNprice().doubleValue();
            d3 = PubDMO.getRoundDouble(nMnyDecimal, d3);
            d3 += stockVOs[i].getNaccumsettlemny().doubleValue();
            stockVOs[i].setNaccumsettlemny(new UFDouble(d3));
          }

          estimateForPartSettle(zgStockVOs, estVO, cOperator, head.getDsettledate(), bZGYF);
        }

      }

      HashMap hm_csettlebill_inv = new HashMap();
      HashMap hm_csettlebill_mny = new HashMap();

      key = dmo.insertHead(head);

      String sqlupdate = "update po_settlebill set isyf='Y' where csettlebillid='" + key + "'";
      getBaseDAO().executeUpdate(sqlupdate);

      HashMap hm_stock_inv = new HashMap();
      for (int i = 0; i < stockVOs.length; i++) {
        String cbaseid = stockVOs[i].getCbaseid();
        hm_stock_inv.put(cbaseid, cbaseid);
      }

      String[] s = dmo.insertBody(body, key);
      for (int i = 0; i < body.length; i++) {
        body[i].setCsettlebill_bid(s[i]);
        body[i].setCsettlebillid(key);

        String cstockrow = body[i].getCstockrow();
        if ((cstockrow != null) && (!cstockrow.equals(""))) {
          hm_csettlebill_inv.put(cstockrow, s[i]);
          UFDouble ngaugemny = body[i].getNgaugemny() == null ? new UFDouble(0) : body[i].getNgaugemny();
          UFDouble nmoney = body[i].getNmoney() == null ? new UFDouble(0) : body[i].getNmoney();

          if ((ngaugemny.doubleValue() != 0.0D) && (ngaugemny.doubleValue() < nmoney.doubleValue())) {
            nmoney = ngaugemny;
          }
          hm_csettlebill_mny.put(cstockrow, Double.valueOf(PubDMO.getRoundDouble(nMnyDecimal, nmoney.doubleValue())));
        }

      }

      timer.addExecutePhase("插入结算单");

      if ((stockVOs != null) && (stockVOs.length > 0))
      {
        backICStatus(stockVOs, this.status[2], ddate.toString());
      }
      timer.addExecutePhase("更新入库单");

      if ((iinvoiceVOs != null) && (iinvoiceVOs.length > 0)) {
        dmo.updateInvoice(iinvoiceVOs);
      }
      timer.addExecutePhase("更新发票");
      
      //反编译后报错，edit by yqq  2016-07-18      
      if ((feeVOs != null) && (feeVOs.length > 0))
//      if (feeVOs != null) feeVOs.length;
      timer.addExecutePhase("更新费用发票");
      
      //反编译后报错，edit by yqq  2016-07-18     
      if ((discountVOs != null) && (discountVOs.length > 0))
 //   if (discountVOs != null) discountVOs.length;
      timer.addExecutePhase("更新折扣");

      if ((stockVOs != null) && (stockVOs.length > 0))
      {
        IArapBillPrivate iArapBillPrivate = (IArapBillPrivate)NCLocator.getInstance().lookup(IArapBillPrivate.class.getName());
        BaseDAO basedao = new BaseDAO();
        UFDouble df1 = new UFDouble(-1);

        for (int i = 0; i < stockVOs.length; i++) {
          String cgeneralhid = stockVOs[i].getCgeneralhid();
          String cgeneralbid = stockVOs[i].getCgeneralbid();

          StringBuffer sql = new StringBuffer();
          sql.append(" SELECT DISTINCT arap_djzb.vouchid, arap_djzb.djbh, arap_djzb.dwbm ")
            .append(" FROM arap_djzb, arap_djfb ")
            .append(" WHERE arap_djzb.vouchid = arap_djfb.vouchid ")
            .append(" and arap_djfb.ddlx = '" + cgeneralhid + "' ")
            .append(" and rtrim(arap_djfb.jsfsbm, ' ') = '45' ")
            .append(" and arap_djzb.djdl = 'yf' ")
            .append(" and nvl(arap_djzb.zyx29,'N') = 'Y' ")
            .append(" and arap_djzb.dr = 0 ")
            .append(" and arap_djfb.dr = 0 ");

          ArrayList list = (ArrayList)basedao.executeQuery(sql.toString(), new MapListProcessor());
          if ((list != null) && (list.size() > 0)) {
            for (int j = 0; j < list.size(); j++) {
              HashMap hm = (HashMap)list.get(j);
              String vouchid = hm.get("vouchid") == null ? "" : hm == null ? "" : hm.get("vouchid").toString();

              DJZBVO billvo = iArapBillPrivate.findBillByPK(vouchid);

              if (billvo != null) {
                DJZBHeaderVO hvo = (DJZBHeaderVO)billvo.getParentVO();

                DJZBItemVO[] bvos = (DJZBItemVO[])billvo.getChildrenVO();

                if ((hvo == null) || (bvos == null) || (bvos.length <= 0))
                {
                  continue;
                }
                DJZBHeaderVO newhvo = (DJZBHeaderVO)hvo.clone();

                newhvo.setPrimaryKey(null);
                newhvo.setVouchid(null);
                newhvo.setDjbh(null);
                newhvo.setDjrq(ddate);
                newhvo.setShr(null);
                newhvo.setShrq(null);
                newhvo.setShkjnd(null);
                newhvo.setShkjqj(null);

                ArrayList arrnewbvos = new ArrayList();

                UFDouble headmny = new UFDouble(0);
                for (int k = 0; k < bvos.length; k++)
                {
                  String ckdid = bvos[k].getCkdid();
                  if ((ckdid == null) || (!ckdid.equals(cgeneralbid)))
                  {
                    continue;
                  }

                  String csettbid = hm_csettlebill_inv.get(ckdid) == null ? "" : hm_csettlebill_inv.get(ckdid).toString();
                  if ((csettbid == null) || (csettbid.equals("")))
                  {
                    continue;
                  }

                  UFDouble nmoney = hm_csettlebill_mny.get(ckdid) == null ? new UFDouble(0) : new UFDouble(hm_csettlebill_mny.get(ckdid).toString());

                  headmny = headmny.add(nmoney);

                  DJZBItemVO newbvos = (DJZBItemVO)bvos[k].clone();

                  newbvos.setPrimaryKey(null);
                  newbvos.setVouchid(null);
                  newbvos.setFb_oid(null);
                  newbvos.setCkdh(null);
                  newbvos.setCkdid(null);
                  newbvos.setDdh(null);
                  newbvos.setDdhid(null);
                  newbvos.setDjbh(null);
                  newbvos.setJsfsbm("27");

                  newbvos.setDdlx(key);
                  newbvos.setDdhh(csettbid);

                  newbvos.setYbye(nmoney.multiply(df1));

                  newbvos.setBbye(nmoney.multiply(df1));

                  newbvos.setDfbbje(nmoney.multiply(df1));

                  newbvos.setDfbbwsje(nmoney.multiply(df1));

                  newbvos.setDfybje(nmoney.multiply(df1));

                  newbvos.setDfybwsje(nmoney.multiply(df1));

                  arrnewbvos.add(newbvos);
                }

                newhvo.setYbje(headmny.multiply(df1));

                newhvo.setBbje(headmny.multiply(df1));

                if ((arrnewbvos != null) && (arrnewbvos.size() > 0)) {
                  DJZBVO newbillvo = new DJZBVO();

                  newbillvo.setParentVO(newhvo);
                  newbillvo.setChildrenVO((DJZBItemVO[])arrnewbvos.toArray(new DJZBItemVO[0]));

                  IplatFormEntry iIplatFormEntry = (IplatFormEntry)NCLocator.getInstance().lookup(
                    IplatFormEntry.class.getName());

                  Object savevo = iIplatFormEntry.processAction("SAVE", ((DJZBHeaderVO)newbillvo.getParentVO()).getDjlxbm(), ddate.toString(), null, newbillvo, 
                    null, null);

                  ArrayList approvelist = (ArrayList)savevo;
                  DJZBVO approvevo = (DJZBVO)approvelist.get(1);

                  Object localObject1 = iIplatFormEntry.processAction("APPROVE", ((DJZBHeaderVO)newbillvo.getParentVO()).getDjlxbm(), ddate.toString(), null, approvevo, 
                    null, null);
                }

              }

            }

          }

        }

      }
      else if (bGetBillCodeSucc) {
        try {
          GetSysBillCode billCode = new GetSysBillCode();
          billCode.returnBillNo(settlebillVO);
        }
        catch (Exception ex)
        {
          PubDMO.throwBusinessException(ex);
        }
      }

      timer.addExecutePhase("向存货核算传送数据-saveBillFromOutter1-(总)");

      timer.showAllExecutePhase("手工结算-doManualBalance()-时间分布");
    }
    catch (Exception e)
    {
      if (bGetBillCodeSucc) {
        try {
          GetSysBillCode billCode = new GetSysBillCode();
          billCode.returnBillNo(settlebillVO);
        }
        catch (Exception ex)
        {
          PubDMO.throwBusinessException(e);
        }

      }

      PubDMO.throwBusinessException(e);
      
      //反编译后报错，edit by yqq  2016-07-18
/*      try
      {
        if (bLock)
        {
          LockTool.releaseLockForPks(sKeys, cOperator);
        }
      } catch (Exception e) {
        PubDMO.throwBusinessException(e);
      }*/
    }
    finally
    {
      try
      {
        if (bLock)
        {
          LockTool.releaseLockForPks(sKeys, cOperator);
        }
      } catch (Exception e) {
        PubDMO.throwBusinessException(e);
      }
    }
    return key;
  }

  public void backICStatus(StockVO[] VOs, String status, String date) throws BusinessException
  {
    BaseDAO basdao = new BaseDAO();
    if ((VOs != null) && (VOs.length > 0))
      for (int i = 0; i < VOs.length; i++) {
        String cgeneralbid = VOs[i].getCgeneralbid() == null ? "" : VOs[i].getCgeneralbid().toString();

        String sql = "update ic_general_b set vuserdef19='" + status + "',vuserdef20='" + date + "' where cgeneralbid='" + cgeneralbid + "'";
        basdao.executeUpdate(sql);
      }
  }

  public BaseDAO getBaseDAO()
  {
    if (this.basedao == null) {
      this.basedao = new BaseDAO();
    }
    return this.basedao;
  }

  public ArrayList onDiscard_yfn(ArrayList listPara)
    throws BusinessException
  {
    ICreateCorpQueryService myService0 = null;
    Timer timer = new Timer();
    timer.start();
    try
    {
      if ((listPara == null) || (listPara.size() < 3)) {
        SCMEnv.out("程序BUG，参数传入不正确，直接返回NULL!");
        return null;
      }
      SettlebillVO[] vos = (SettlebillVO[])listPara.get(0);
      String strPkCorp = (String)listPara.get(1);
      String strOprId = (String)listPara.get(2);
      CostfactorVO[] factors = 
        (CostfactorVO[])listPara
        .get(3);
      int iLen = vos.length;

      myService0 = (ICreateCorpQueryService)NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());
      boolean bAPStartUp = myService0.isEnabled(strPkCorp, "AP");
      GetSysBillCode billCode = new GetSysBillCode();
      for (int i = 0; i < vos.length; i++) {
        billCode.returnBillNo(vos[i]);

        SettlebillItemVO[] bodyVO = vos[i].getBodyVO();
        boolean bVirtual = false;
        Vector vTemp = new Vector();
        for (int j = 0; j < bodyVO.length; j++) {
          String s = bodyVO[j].getCinvoiceid();
          if ((s != null) && (s.trim().length() > 0) && (!vTemp.contains(s))) vTemp.addElement(s);

          String cstockrow = bodyVO[j].getCstockrow();
          if ((cstockrow != null) && (!cstockrow.equals(""))) {
            String sql = "select  vuserdef19 from ic_general_b  where cgeneralbid='" + cstockrow + "' and nvl(dr,0)=0 ";
            Object obj = getBaseDAO().executeQuery(sql, new ColumnProcessor());
            if ((obj != null) && (obj.toString().trim().equals("已结案"))) {
              throw new BusinessException("结算单<" + ((SettlebillHeaderVO)vos[i].getParentVO()).getVsettlebillcode() + ">对应入库单已结案，不能删除");
            }
          }
        }

        if (vTemp.size() > 0) {
          String[] cInvoiceID = new String[vTemp.size()];
          vTemp.copyInto(cInvoiceID);
          cInvoiceID = new SettleDMO().getVirtualInvoices(cInvoiceID);
          if ((cInvoiceID != null) && (cInvoiceID.length > 0)) 
        	  bVirtual = true;
        }

        boolean bIsFromVMI = isFromVMI(vos[i]);

        if ((!bIsFromVMI) && 
          (bAPStartUp))
        {
          String[] s = new String[bodyVO.length];
          for (int j = 0; j < s.length; j++) s[j] = bodyVO[j].getCinvoiceid();
          if (bVirtual) {
            deleteBillForARAP(s, strOprId);
          }

        }

        SettlebillHeaderVO hvo = (SettlebillHeaderVO)vos[i].getParentVO();
        SettlebillItemVO[] bvos = vos[i].getBodyVO();
        if ((hvo != null) && (bvos != null) && (bvos.length > 0))
        {
          Vector v1 = new Vector();

          for (int j = 0; j < bvos.length; j++) {
            String csettlebillid = hvo.getPrimaryKey();
            String csettlebill_bid = bvos[j].getCsettlebill_bid();
            if ((csettlebillid == null) || (csettlebillid.equals("")) || 
              (csettlebill_bid == null) || (csettlebill_bid.equals(""))) {
              continue;
            }
            DJZBItemVO yfbodyVO = new DJZBItemVO();
            yfbodyVO.setDdlx(csettlebillid);
            yfbodyVO.setDdhh(csettlebill_bid);

            DJZBVO tempVO = new DJZBVO();
            DJZBHeaderVO headvo = new DJZBHeaderVO();
            headvo.setZyx29("Y");
            tempVO.setParentVO(headvo);
            tempVO.setChildrenVO(new DJZBItemVO[] { yfbodyVO });

            v1.addElement(tempVO);
          }

          DJZBVO[] apVOs = new DJZBVO[v1.size()];
          v1.copyInto(apVOs);

          IArapForGYLPublic iArap = (IArapForGYLPublic)NCLocator.getInstance().lookup(IArapForGYLPublic.class.getName());
          for (int j = 0; j < apVOs.length; j++) {
            iArap.deleteEffForCG(apVOs[j]);
          }

        }

        if ((bvos != null) && (bvos.length > 0)) {
          for (int j = 0; j < bvos.length; j++) {
            String csettlebill_bid = bvos[j].getCsettlebill_bid();
            String cstockrow = bvos[j].getCstockrow();
            if ((csettlebill_bid == null) || (csettlebill_bid.equals("")) || 
              (cstockrow == null) || (cstockrow.equals("")))
              continue;
            StringBuffer sql = new StringBuffer();
            sql.append(" select psb.csettlebill_bid from po_settlebill ps ")
              .append(" inner join po_settlebill_b psb ")
              .append(" on ps.csettlebillid=psb.csettlebillid ")
              .append(" where nvl(ps.isyf,'N')='Y' ")
              .append(" and psb.cstockrow='" + cstockrow + "' ")
              .append(" and psb.csettlebill_bid<>'" + csettlebill_bid + "' ")
              .append(" and nvl(ps.dr,0)=0 ")
              .append(" and nvl(psb.dr,0)=0 ");

            ArrayList list = (ArrayList)getBaseDAO().executeQuery(sql.toString(), new MapListProcessor());
            if ((list == null) || (list.size() == 0)) {
              String sqlup = "update ic_general_b set vuserdef19='已暂估且未结算' where cgeneralbid='" + cstockrow + "'";
              getBaseDAO().executeUpdate(sqlup);
            }

          }

        }

      }

      ArrayList listHid = new ArrayList();
      ArrayList listHts = new ArrayList();
      String[] saHid = (String[])null;
      String[] saHts = (String[])null;
      for (int i = 0; i < iLen; i++) {
        if ((vos[i].getBodyVO() != null) && 
          (vos[i].getBodyVO().length > 0)) continue;
        listHid.add(vos[i].getHeadVO().getPrimaryKey());
        listHts.add(vos[i].getHeadVO().getTs());
      }

      if (listHid.size() > 0) {
        int iLenNullBody = listHid.size();
        saHid = new String[iLenNullBody];
        listHid.toArray(saHid);
        saHts = new String[iLenNullBody];
        listHts.toArray(saHts);
        ArrayList listAllBody = querySettleBodyBatch(strPkCorp, saHid, 
          saHts, new UFBoolean(false));
        if ((listAllBody == null) || (listAllBody.size() != iLenNullBody)) {
          throw new BusinessException(
            NCLangResOnserver.getInstance().getStrByID("40040502", 
            "UPP40040502-000044"));
        }

        int iPos = 0;
        for (int i = 0; i < iLen; i++) {
          if ((vos[i].getBodyVO() != null) && 
            (vos[i].getBodyVO().length > 0)) continue;
          vos[i].setChildrenVO(
            (SettlebillItemVO[])listAllBody
            .get(iPos));
          iPos++;
        }
      }

      timer.addExecutePhase("查询前台未加载过的表体数据");

      for (int i = 0; i < iLen; i++) {
        onDis_DoRegret_yf(vos[i], strPkCorp, strOprId, factors);
      }
      timer.addExecutePhase("作废操作");

      timer.showAllExecutePhase("作废结算单时间分布");
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return null;
  }

  private void onDis_DoRegret_yf(SettlebillVO vo, String strPkCorp, String strOprId, CostfactorVO[] factors)
    throws BusinessException
  {
    if ((vo == null) || (vo.getBodyVO() == null) || (vo.getBodyVO().length <= 0)) {
      SCMEnv.out("结算单或结算单表体为NULL，直接返回!");
      return;
    }

    Timer timer = new Timer();
    timer.start();

    SettlebillItemVO[] items = vo.getBodyVO();
    int iLen = items.length;

    String strTmp = null;
    Vector vStockKey = new Vector();
    for (int i = 0; i < iLen; i++) {
      strTmp = items[i].getCstockrow();
      if ((strTmp == null) || (strTmp.length() == 0) || 
        (vStockKey.contains(strTmp)))
        continue;
      vStockKey.addElement(strTmp);
    }

    timer.addExecutePhase("入库单行主键归类");

    Hashtable tFeeDiscount = null;
    if ((items != null) && (iLen > 0)) {
      Vector vTemp = new Vector();
      for (int i = 0; i < iLen; i++) {
        strTmp = items[i].getCinvoice_bid();
        if ((strTmp != null) && (strTmp.trim().length() > 0))
          vTemp.addElement(items[i].getCbaseid());
      }
      if (vTemp.size() > 0) {
        String[] saTmp = new String[vTemp.size()];
        vTemp.copyInto(saTmp);
        tFeeDiscount = isFeeDiscount(saTmp);
      }
    }
    timer.addExecutePhase("判断发票是否为费用/折扣");

    Vector vInvoiceKey = new Vector();
    Vector vFeeKey = new Vector();
    for (int i = 0; i < iLen; i++) {
      strTmp = items[i].getCinvoice_bid();
      if ((strTmp == null) || (strTmp.length() == 0)) {
        continue;
      }
      String cbaseid = items[i].getCbaseid();
      UFBoolean b = (UFBoolean)tFeeDiscount.get(cbaseid);
      if (b.booleanValue())
      {
        if (!vInvoiceKey.contains(strTmp)) {
          vInvoiceKey.addElement(strTmp);
        }
      }
      else if (!vInvoiceKey.contains(strTmp)) {
        vInvoiceKey.addElement(strTmp);
      }
    }

    timer.addExecutePhase("发票行主键归类（包括普通和费用、折扣两类）");

    ArrayList batchList = null;
    String[] stockBodyKey = new String[vStockKey.size()];
    vStockKey.copyInto(stockBodyKey);
    String[] invoiceBodyKey = new String[vInvoiceKey.size()];
    vInvoiceKey.copyInto(invoiceBodyKey);
    String[] feeBodyKey = new String[vFeeKey.size()];
    vFeeKey.copyInto(feeBodyKey);

    batchList = queryDetailBatchForDispose(strPkCorp, stockBodyKey, 
      invoiceBodyKey, feeBodyKey);

    timer.addExecutePhase("查询入库单、发票、费用发票");

    Vector v0 = new Vector();
    if ((batchList != null) && (batchList.size() == 3)) {
      ArrayList list = (ArrayList)batchList.get(2);
      if ((list != null) && (list.size() > 0)) {
        FeeinvoiceVO[] feeTempVOs = (FeeinvoiceVO[])list.get(0);
        if ((feeTempVOs != null) && (feeTempVOs.length > 0)) {
          for (int i = 0; i < feeTempVOs.length; i++) {
            v0.addElement(feeTempVOs[i]);
          }
        }
      }
    }
    FeeinvoiceVO[] feeTempVOs = new FeeinvoiceVO[v0.size()];
    v0.copyInto(feeTempVOs);

    timer.addExecutePhase("查询费用发票,确定结算单的费用发票是否进入成本");

    UFBoolean[] m_bIsentercost = (UFBoolean[])null;
    if ((feeTempVOs != null) && (feeTempVOs.length > 0))
    {
      Vector vBCost = new Vector();
      Vector vTemp = new Vector();
      vBCost.addElement(factors[0].getHeadVO().getBisentercost());
      vTemp.addElement(factors[0].getHeadVO().getCcostfactorid().trim());
      for (int i = 1; i < factors.length; i++) {
        String s1 = factors[i].getHeadVO().getCcostfactorid().trim();
        if (!vTemp.contains(s1)) {
          vTemp.addElement(s1);
          vBCost.addElement(factors[i].getHeadVO().getBisentercost());
        }
      }
      m_bIsentercost = new UFBoolean[vBCost.size()];
      vBCost.copyInto(m_bIsentercost);
    }

    timer.addExecutePhase("记录费用是否进入成本");

    StockVO[] stockVOs = (StockVO[])null;
    if ((batchList != null) && (batchList.size() == 3)) {
      Object oTemp = batchList.get(0);
      if ((oTemp != null) && (vStockKey != null) && (vStockKey.size() > 0)) {
        stockVOs = (StockVO[])oTemp;
        stockVOs = onDis_RestoreStock(vStockKey, items, stockVOs);
      }
    }

    timer.addExecutePhase("恢复入库单数据");

    IinvoiceVO[] iinvoiceVOs = (IinvoiceVO[])null;
    if ((batchList != null) && (batchList.size() == 3)) {
      Object oTemp = batchList.get(1);
      if ((oTemp != null) && (vInvoiceKey != null) && (vInvoiceKey.size() > 0)) {
        iinvoiceVOs = (IinvoiceVO[])oTemp;
        iinvoiceVOs = onDis_RestoreInvoice(vInvoiceKey, items, 
          iinvoiceVOs, m_bIsentercost);
      }
    }

    timer.addExecutePhase("恢复发票数据");

    FeeinvoiceVO[] feeVOs = (FeeinvoiceVO[])null;
    FeeinvoiceVO[] discountVOs = (FeeinvoiceVO[])null;
    FeeinvoiceVO[] specialVOs = (FeeinvoiceVO[])null;
    if ((vFeeKey != null) && (vFeeKey.size() > 0)) {
      Vector v = null;
      if ((batchList != null) && (batchList.size() == 3)) {
        Object oTemp = batchList.get(2);
        if (oTemp != null) {
          ArrayList list0 = (ArrayList)oTemp;
          v = onDis_RestoreFee(vFeeKey, items, list0);
        }
      }

      if ((v == null) || (v.size() < 3)) {
        throw new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("40040502", 
          "UPP40040502-000041"));
      }

      Vector v1 = (Vector)v.elementAt(0);
      Vector v2 = (Vector)v.elementAt(1);
      Vector v3 = (Vector)v.elementAt(2);
      feeVOs = new FeeinvoiceVO[v1.size()];
      v1.copyInto(feeVOs);
      discountVOs = new FeeinvoiceVO[v2.size()];
      v2.copyInto(discountVOs);
      specialVOs = new FeeinvoiceVO[v3.size()];
      v3.copyInto(specialVOs);
    }
    timer.addExecutePhase("恢复费用发票和折扣数据");

    ArrayList listPara = new ArrayList();
    listPara.add(vo);
    listPara.add(stockVOs);
    listPara.add(iinvoiceVOs);
    listPara.add(feeVOs);
    listPara.add(discountVOs);
    listPara.add(specialVOs);
    listPara.add(strOprId);
    listPara.add(strPkCorp);

    discardSettlebill_yf(listPara);

    timer.addExecutePhase("作废操作（总）");

    timer.showAllExecutePhase("结算单作废（核心算法）时间分布");
  }

  private void discardSettlebill_yf(ArrayList listPara)
    throws BusinessException
  {
    Timer timer = new Timer();
    timer.start();
    if ((listPara == null) || (listPara.size() < 8)) {
      SCMEnv.out("程序BUG:传入参数错误，直接返回！");
      return;
    }
    SettlebillVO settleVO = (SettlebillVO)listPara.get(0);
    StockVO[] stockVOs = (StockVO[])listPara.get(1);
    IinvoiceVO[] iinvoiceVOs = (IinvoiceVO[])listPara.get(2);
    FeeinvoiceVO[] feeVOs = (FeeinvoiceVO[])listPara.get(3);
    FeeinvoiceVO[] discountVOs = (FeeinvoiceVO[])listPara.get(4);
    FeeinvoiceVO[] specialVOs = (FeeinvoiceVO[])listPara.get(5);
    String strOperatorId = (String)listPara.get(6);
    String strCorpId = (String)listPara.get(7);

    SettleDMO dmo = null;
    InvoiceImpl myService1 = null;
    ICreateCorpQueryService myService0 = null;
    boolean bLock = false;
    boolean bVirtual = false;

    Hashtable hStock = new Hashtable();
    Hashtable hInvoice = new Hashtable();
    if ((stockVOs != null) && (stockVOs.length > 0)) {
      for (int i = 0; i < stockVOs.length; i++)
        hStock.put(stockVOs[i].getCgeneralbid(), stockVOs[i].getBzgyfflag());
    }
    if ((iinvoiceVOs != null) && (iinvoiceVOs.length > 0)) {
      for (int i = 0; i < iinvoiceVOs.length; i++) {
        hInvoice.put(iinvoiceVOs[i].getCinvoice_bid(), new Integer[] { iinvoiceVOs[i].getIInvoiceType(), iinvoiceVOs[i].getIBillStatus() });
      }

    }

    String[] sKeys = getSettleLockKeys(stockVOs, iinvoiceVOs, feeVOs, 
      discountVOs, specialVOs, null, null);

    timer.addExecutePhase("组合所有需要加锁的主键");

    Vector v = new Vector();
    for (int i = 0; i < sKeys.length; i++) v.addElement(sKeys[i]);
    SettlebillItemVO[] bodyVO = settleVO.getBodyVO();
    v.addElement(settleVO.getHeadVO().getCsettlebillid());

    for (int i = 0; i < bodyVO.length; i++) {
      if ((bodyVO[i].getCvmiid() == null) || (v.contains(bodyVO[i].getCvmiid()))) continue; v.addElement(bodyVO[i].getCvmiid());
    }

    sKeys = new String[v.size()];
    v.copyInto(sKeys);
    try
    {
      dmo = new SettleDMO();

      bLock = LockTool.setLockForPks(sKeys, strOperatorId);
      if (!bLock) {
        throw new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("40040502", 
          "UPP40040502-000009"));
      }

      timer.addExecutePhase("对单据加锁");

      String sMessage = isTimeStampChangedForDepose(stockVOs, 
        iinvoiceVOs, feeVOs, discountVOs, specialVOs, 
        new SettlebillVO[] { settleVO });
      if ((sMessage != null) && (sMessage.length() > 0)) {
        sMessage = sMessage + NCLangResOnserver.getInstance()
          .getStrByID("40040502", "UPP40040502-000002");

        throw new BusinessException(sMessage);
      }

      timer.addExecutePhase("判断时间戳是否改变(总)");

      if ((hStock.size() > 0) && (hInvoice.size() > 0) && (settleVO != null)) {
        for (int i = 0; i < bodyVO.length; i++) {
          String cstockrow = bodyVO[i].getCstockrow();
          String cinvoice_bid = bodyVO[i].getCinvoice_bid();
          if ((cstockrow != null) && (cinvoice_bid != null) && (hStock.get(cstockrow) != null) && (hInvoice.get(cinvoice_bid) != null)) {
            UFBoolean bZGYF = (UFBoolean)hStock.get(cstockrow);
            Integer[] n = (Integer[])hInvoice.get(cinvoice_bid);
            int ibillstatus = n[1].intValue();
            if ((bZGYF.booleanValue()) && ((ibillstatus == BillStatus.AUDITING.intValue()) || (ibillstatus == BillStatus.AUDITED.intValue()))) {
              throw new BusinessException(NCLangResOnserver.getInstance()
                .getStrByID("40040502", "UPP40040502-000224"));
            }
          }
        }

      }
      //反编译后报错，edit by yqq  2016-07-18    
      if ((stockVOs != null) && (stockVOs.length > 0)) 
 //   if (stockVOs != null) stockVOs.length;

      timer.addExecutePhase("恢复入库单");

      if ((iinvoiceVOs != null) && (iinvoiceVOs.length > 0)) {
        dmo.updateInvoice(iinvoiceVOs);
      }

      timer.addExecutePhase("恢复发票");

      if ((feeVOs != null) && (feeVOs.length > 0)) {
        dmo.updateFee(feeVOs);
      }

      timer.addExecutePhase("恢复费用发票");

      if ((discountVOs != null) && (discountVOs.length > 0)) {
        dmo.updateDiscount(discountVOs);
      }

      timer.addExecutePhase("恢复折扣");

      if ((specialVOs != null) && (specialVOs.length > 0)) {
        dmo.updateDiscount(specialVOs);
      }

      timer.addExecutePhase("恢复一般发票");

      if (settleVO != null) {
        bVirtual = discardVirtualInvoices(settleVO);
      }

      timer.addExecutePhase("作废虚拟发票");

      dmo.discardSettlebill(settleVO);

      timer.addExecutePhase("作废结算单");

      boolean bIsFrmSaleData = false;
      if ((bodyVO != null) && (bodyVO.length > 0) && 
        (bodyVO[0] != null) && 
        (bodyVO[0].getCsale_bid() != null) && 
        (bodyVO[0].getCsale_bid().trim().length() > 0)) {
        bIsFrmSaleData = true;
      }
      if (bIsFrmSaleData)
      {
        int iLen = bodyVO == null ? 0 : bodyVO.length;
        for (int i = 0; i < iLen; i++) {
          if (bodyVO[i].getNsettlenum() != null) {
            bodyVO[i].setNsettlenum(bodyVO[i].getNsettlenum()
              .multiply(-1.0D));
          }
        }
        dmo.updateSaleDataBatch(bodyVO, false);

        if ((iinvoiceVOs != null) && (iinvoiceVOs.length > 0)) dmo.updateAccumInvoiceNumForOrder(iinvoiceVOs);

        timer.addExecutePhase("受托代销结算单回写销售发票的累计结算数量");

        Vector vTemp = new Vector();
        String s1 = bodyVO[0].getCinvoiceid();
        if ((s1 != null) && (s1.trim().length() > 0))
          vTemp.addElement(s1.trim());
        for (int i = 1; i < bodyVO.length; i++) {
          s1 = bodyVO[i].getCinvoiceid();
          if ((s1 == null) || (s1.trim().length() <= 0) || 
            (vTemp.contains(s1.trim()))) continue;
          vTemp.addElement(s1.trim());
        }

        if (vTemp.size() > 0) {
          String[] s = new String[vTemp.size()];
          vTemp.copyInto(s);

          myService1 = new InvoiceImpl();
          myService1.discardInvoiceForSettle(s);
        }

        timer.addExecutePhase("受托代销结算单作废发票");
      }

      rewriteVMIAntiSettle(settleVO);

      timer.addExecutePhase("回写VMI明细");

      if (settleVO != null) {
        String unitCode = settleVO.getHeadVO().getPk_corp();
        myService0 = (ICreateCorpQueryService)
          NCLocator.getInstance().lookup(
          ICreateCorpQueryService.class.getName());

        boolean bIsFromVMI = isFromVMI(settleVO);

        boolean bIAStartUp = myService0.isEnabled(unitCode, "IA");
        if (bIAStartUp)
        {
          if (!bIsFromVMI) {
            String[] s = new String[1];
            s[0] = settleVO.getBodyVO()[0].getCsettlebillid();
            deleteBillFromOutters(s, strCorpId, strOperatorId);

            timer.addExecutePhase("结算单作废时向存货核算系统传送数据-非VMI");
          }
          else {
            deleteBillFromOutter(settleVO, strCorpId, strOperatorId);

            timer.addExecutePhase("结算单作废时向存货核算系统传送数据-VMI");
          }

        }

        if (!bIsFromVMI) {
          boolean bAPStartUp = myService0.isEnabled(unitCode, "AP");
          if (bAPStartUp)
          {
            if ((stockVOs != null) && (stockVOs.length > 0))
            {
              IArapForGYLPublic iArap = (IArapForGYLPublic)NCLocator.getInstance().lookup(IArapForGYLPublic.class.getName());
              iArap.unAdjuest(settleVO.getHeadVO().getCsettlebillid(), unitCode);
            }
          }
        }

      }

      timer.showAllExecutePhase("作废结算单(discardSettlebill(ArrayList))时间分布");
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
      //反编译后报错，edit by yqq  2016-07-18
/*      try
      {
        if (bLock)
        {
          LockTool.releaseLockForPks(sKeys, strOperatorId);
        }
      }
      catch (Exception e)
      {
        PubDMO.throwBusinessException(e);
      }*/
    }
    finally
    {
      try
      {
        if (bLock)
        {
          LockTool.releaseLockForPks(sKeys, strOperatorId);
        }
      }
      catch (Exception e)
      {
        PubDMO.throwBusinessException(e);
      }
    }
  } 
}