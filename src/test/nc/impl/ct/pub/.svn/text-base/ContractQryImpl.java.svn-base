package nc.impl.ct.pub;

import java.util.ArrayList;
import nc.itf.ct.pub.IContractBaseQuery;
import nc.vo.bd.b21.CurrinfoVO;
import nc.vo.ct.pub.ManageVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

public class ContractQryImpl
  implements IContractBaseQuery
{
  public UFBoolean canBSCModify(String sCorpPK, String pk_ct_manage, String bsc)
    throws BusinessException
  {
    ContractImpl ctBase = new ContractImpl();
    return ctBase.canBSCModify(sCorpPK, pk_ct_manage, bsc);
  }

  public String findCurrByCustid(String custPk) throws BusinessException {
    ContractImpl ctBase = new ContractImpl();
    return ctBase.findCurrByCustid(custPk);
  }

  public String[] getCtSysParaString(String corp, String[] param) throws BusinessException {
    ContractImpl ctBase = new ContractImpl();
    return ctBase.getCtSysParaString(corp, param);
  }

  public Integer qryCtStatus(String sPK) throws BusinessException {
    ContractImpl ctBase = new ContractImpl();
    return ctBase.qryCtStatus(sPK);
  }

  public String qryStatusTs(String pk_ct_manage) throws BusinessException {
    ContractImpl ctBase = new ContractImpl();
    return ctBase.qryStatusTs(pk_ct_manage);
  }

  public CurrinfoVO[] queryAllRateDigit(String pk_corp) throws BusinessException {
    ContractImpl ctBase = new ContractImpl();
    return ctBase.queryAllRateDigit(pk_corp);
  }

  public ManageVO[] queryBill(String pk_corp, String sWhere) throws BusinessException {
    ContractImpl ctBase = new ContractImpl();
    return ctBase.queryBill(pk_corp, sWhere);
  }

  public ManageVO[] queryChangedBills(ArrayList alCondition) throws BusinessException {
    ContractImpl ctBase = new ContractImpl();
    return ctBase.queryChangedBills(alCondition);
  }

  public String[] queryCustInfo(String sPk_Custmandoc) throws BusinessException {
    ContractImpl ctBase = new ContractImpl();
    return ctBase.queryCustInfo(sPk_Custmandoc);
  }

  public String[] queryCtCtrlScope(String pk_ct_manage) throws BusinessException {
    ContractImpl ctBase = new ContractImpl();
    return ctBase.queryCtCtrlScope(pk_ct_manage);
  }
}