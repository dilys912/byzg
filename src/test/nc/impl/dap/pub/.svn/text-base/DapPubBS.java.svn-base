package nc.impl.dap.pub;

import java.util.ArrayList;
import java.util.Vector;
import nc.bs.dap.accrule.InitGlOrgBookDMO;
import nc.bs.dap.out.DapBO;
import nc.bs.dap.out.DapDMO;
import nc.bs.dap.rtvouch.DapFinIndexDMO;
import nc.bs.dap.rtvouch.DapMsgDMO;
import nc.bs.dap.service.DapServiceBO;
import nc.bs.dap.vouchtemp.VouchtempBO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.dap.priv.IDap;
import nc.itf.dap.pub.IDapQueryMessage;
import nc.itf.dap.pub.IDapSendMessage;
import nc.itf.dap.pub.IDapService;
import nc.itf.uap.bd.corp.ICorpQry;
import nc.ui.bd.BDGLOrgBookAccessor;
import nc.vo.bd.b54.GlorgbookVO;
import nc.vo.dap.out.BillQueryVoucherVO;
import nc.vo.dap.out.DapMsgVO;
import nc.vo.dap.out.RetBillVo;
import nc.vo.dap.rtvouch.DapFactorVO;
import nc.vo.dap.rtvouch.DapFinMsgVO;
import nc.vo.dap.vouchtemp.RetVoucherVO;
import nc.vo.dap.vouchtemp.VoucherTemplateVO;
import nc.vo.dap.vouchtemp.VouchtempBVO;
import nc.vo.dap.vouchtemp.VouchtempVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

public class DapPubBS
  implements IDapSendMessage, IDapService, IDapQueryMessage
{
  public DapFactorVO[] getFactorArray(DapMsgVO dapMsgVo)
    throws BusinessException
  {
    try
    {
      VouchtempBO bo = new VouchtempBO();
      VouchtempVO queryVo = new VouchtempVO();
      queryVo.setPk_corp(dapMsgVo.getCorp());
      queryVo.setPk_sys(dapMsgVo.getSys());
      queryVo.setPk_proc(dapMsgVo.getProc());
      queryVo.setPk_busitype(dapMsgVo.getBusiType());
      Vector vRes = bo.queryTempVOSByCondVO(queryVo);
      String pkGrpInsubjClass = null;
      if ((vRes != null) && (vRes.size() > 0)) {
        pkGrpInsubjClass = "(";
        for (int i = 0; i < vRes.size(); i++) {
          VoucherTemplateVO resVO = (VoucherTemplateVO)vRes.elementAt(i);
          VouchtempBVO[] bvos = new VouchtempBVO[resVO.getTempDtl().size()];
          resVO.getTempDtl().toArray(bvos);
          for (int j = 0; j < bvos.length; j++) {
            pkGrpInsubjClass = pkGrpInsubjClass + "'" + bvos[j].getPk_insubjclass() + "',";
          }
        }
        if (pkGrpInsubjClass.length() > 4) {
          pkGrpInsubjClass = pkGrpInsubjClass.substring(0, pkGrpInsubjClass.length() - 1);
        }
        pkGrpInsubjClass = pkGrpInsubjClass + ")";
      } else {
        pkGrpInsubjClass = null;
      }
      return new DapDMO().queryFactorAttrByInsubjClassGrp(pkGrpInsubjClass, dapMsgVo.getSys(), dapMsgVo.getPkAccOrg(), dapMsgVo.getPkAccount()); } catch (Exception ex) {
    	    throw new BusinessException(ex);
    }
  }

  public DapFactorVO[] getBookRuleFactorArray(DapMsgVO dapMsgVo)
    throws BusinessException
  {
    try
    {
      VouchtempBO bo = new VouchtempBO();
      VouchtempVO queryVo = new VouchtempVO();
      queryVo.setPk_corp(dapMsgVo.getCorp());
      queryVo.setPk_sys(dapMsgVo.getSys());
      queryVo.setPk_proc(dapMsgVo.getProc());
      queryVo.setPk_busitype(dapMsgVo.getBusiType());
      Vector vRes = bo.queryTempVOSByCondVO(queryVo);
      String pkGrpInsubjClass = null;
      if ((vRes != null) && (vRes.size() > 0)) {
        pkGrpInsubjClass = "(";
        for (int i = 0; i < vRes.size(); i++) {
          VoucherTemplateVO resVO = (VoucherTemplateVO)vRes.elementAt(i);
          VouchtempBVO[] bvos = new VouchtempBVO[resVO.getTempDtl().size()];
          resVO.getTempDtl().toArray(bvos);
          for (int j = 0; j < bvos.length; j++) {
            pkGrpInsubjClass = pkGrpInsubjClass + "'" + bvos[j].getPk_insubjclass() + "',";
          }
        }
        if (pkGrpInsubjClass.length() > 4) {
          pkGrpInsubjClass = pkGrpInsubjClass.substring(0, pkGrpInsubjClass.length() - 1);
        }
        pkGrpInsubjClass = pkGrpInsubjClass + ")";
      } else {
        pkGrpInsubjClass = null;
      }
      return new DapDMO().queryFactorAttrByInsubjClassGrp(pkGrpInsubjClass, dapMsgVo.getSys(), dapMsgVo.getPkAccOrg(), dapMsgVo.getPkAccount());
    } catch (Exception ex) {
      Logger.error("取得入账规则定义的影响因素值出现错误！", ex);
      throw new BusinessException("取得入账规则定义的影响因素值出现错误！", ex);
    }
  }

  public RetBillVo[] getPeriodNotCompleteBill(String period, String pk_corp, String pk_proc, int k) throws BusinessException
  {
    try
    {
      return new DapDMO().getPeriodNoCompleteBill(period, pk_corp, pk_proc, k); } catch (Exception ex) {
    	    throw new BusinessException("查询数据出错！", ex);
    }
  }

  public VoucherTemplateVO[] getVouchtempArray(DapMsgVO[] dapMsgVo)
    throws BusinessException
  {
    Vector v = new Vector();
    VouchtempBO bo = new VouchtempBO();
    for (int i = 0; i < dapMsgVo.length; i++) {
      VouchtempVO queryVo = new VouchtempVO();
      queryVo.setPk_sys(dapMsgVo[i].getSys());
      queryVo.setPk_proc(dapMsgVo[i].getProc());
      queryVo.setPk_busitype(dapMsgVo[i].getBusiType());
      queryVo.setPk_corp(dapMsgVo[i].getCorp());
      queryVo.setPkAccOrg(dapMsgVo[i].getPkAccOrg());
      queryVo.setPkAccount(dapMsgVo[i].getPkAccount());
      Vector vRes = bo.queryTempVOSByCondVO(queryVo);
      if (vRes == null) continue; v.addAll(vRes);
    }
    if ((v != null) && (v.size() > 0)) {
      VoucherTemplateVO[] vRes = new VoucherTemplateVO[v.size()];
      v.copyInto(vRes);
      return vRes;
    }
    return null;
  }

  public BillQueryVoucherVO[] queryAllVouchers(BillQueryVoucherVO[] billDatas) throws BusinessException
  {
    return new DapServiceBO().queryVouchers(billDatas);
  }

  public boolean isEditBillTypeOrProc(String pkCorp, String pkSys, String pkProc, String pkBusiType, String procMsg) throws BusinessException {
    IDap dap = (IDap)NCLocator.getInstance().lookup(IDap.class.getName());
    return dap.isEditBillType_RequiresNew(pkCorp, pkSys, pkProc, pkBusiType, procMsg);
  }

  public boolean isEditBillType(String pkCorp, String pkSys, String pkProc, String pkBusiType, String procMsg) throws BusinessException {
    return isEditBillTypeOrProc(pkCorp, pkSys, pkProc, pkBusiType, procMsg);
  }

  public void queryMessageQueueAndProc() throws BusinessException
  {
    new DapBO().queryMessageQueueAndProc();
  }

  public RetVoucherVO sendMessage(DapMsgVO msgVo, AggregatedValueObject dataVo)
    throws BusinessException
  {
    return new DapBO().sendDapMessage(msgVo, dataVo);
  }

  public UFBoolean isHasRtVoucherByPeriod(String pk_corp, String year, String pk_glorg, String pk_glbook)
    throws BusinessException
  {
    boolean rst = new DapServiceBO().isHasRtVouchByCorpAndYear(pk_corp, year, pk_glorg, pk_glbook);
    return new UFBoolean(rst);
  }

  public UFBoolean isHasRtVouchByCorpAndYear(String pk_corp, String year, String pk_glorg, String pk_glbook)
    throws BusinessException
  {
    boolean rst = new DapServiceBO().isHasRtVouchByCorpAndYear(pk_corp, year, pk_glorg, pk_glbook);
    return new UFBoolean(rst);
  }

  public DapMsgVO[] queryNotFinishMessage(DapMsgVO dapMsgVo)
    throws BusinessException
  {
    try
    {
      DapFinMsgVO queryVo = DapFinMsgVO.changeFinIndexVO(dapMsgVo, null);
      queryVo.setBusiDate(null);
      queryVo.setQueryEndDate(dapMsgVo.getBusiDate());
      DapFinMsgVO[] msgVos = new DapMsgDMO().query(queryVo);
      String whereSql = "flag <4";
      DapFinMsgVO[] finVos = new DapFinIndexDMO().queryAllByVO(queryVo, whereSql);
      ArrayList resArray = new ArrayList();
      if (finVos != null) {
        for (int i = 0; i < finVos.length; i++) {
          resArray.add(DapFinMsgVO.changeMsgVO(finVos[i]));
        }
      }
      if (msgVos != null) {
        for (int i = 0; i < msgVos.length; i++) {
          resArray.add(DapFinMsgVO.changeMsgVO(msgVos[i]));
        }
      }
      DapMsgVO[] resVos = new DapMsgVO[resArray.size()];
      resArray.toArray(resVos);
      return resVos;
    } catch (Exception ex) {
      Logger.error(ex);
    }
    return null;
  }

  public void UpgradeNewBilltype(String[] pk_billtype)
    throws BusinessException
  {
    ICorpQry CorpQuery = (ICorpQry)NCLocator.getInstance().lookup(ICorpQry.class.getName());
    String[] pk_corps = CorpQuery.getAccountedCorpPKs();
    try {
      InitGlOrgBookDMO dmo = new InitGlOrgBookDMO();
      if (pk_corps != null)
        for (int i = 0; i < pk_corps.length; i++) {
          GlorgbookVO[] glorgbookvo = BDGLOrgBookAccessor.getGLOrgBookVOsByPk_Corp(pk_corps[i]);
          for (int j = 0; j < glorgbookvo.length; j++)
            dmo.initControlRule(pk_corps[i], glorgbookvo[j].getPk_glorg(), glorgbookvo[j].getPk_glbook(), pk_billtype);
        }
    }
    catch (Exception ex)
    {
      Logger.error(ex);
    }
  }
}