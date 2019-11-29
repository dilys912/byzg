package nc.bs.trade.business;

import java.util.ArrayList;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.ComponentException;
import nc.bs.logging.Logger;
import nc.itf.uif.pub.IUifService;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.trade.billsource.LightBillVO;

public class HYPubBO
  implements IUifService
{
  private IUifService service;

  public IUifService getService()
    throws UifException
  {
    if (this.service == null) {
      try {
        this.service = ((IUifService)NCLocator.getInstance().lookup(IUifService.class.getName()));
      }
      catch (ComponentException e) {
        Logger.warn("can't find " + e.getComponentName(), e);
        throw new UifException();
      }
    }
    return this.service;
  }

  public void checkConsistence(AggregatedValueObject billVo)
    throws UifException
  {
    getService().checkConsistence(billVo);
  }

  public ArrayList commitBill(AggregatedValueObject billVo)
    throws UifException
  {
    return getService().commitBill(billVo);
  }

  public void delete(SuperVO vo)
    throws UifException
  {
    getService().delete(vo);
  }

  public AggregatedValueObject deleteBD(AggregatedValueObject billVo, Object userObj)
    throws UifException
  {
    return getService().deleteBD(billVo, userObj);
  }

  public AggregatedValueObject deleteBill(AggregatedValueObject billVo)
    throws UifException
  {
    return getService().deleteBill(billVo);
  }

  public void deleteByWhereClause(Class c, String where)
    throws UifException
  {
    getService().deleteByWhereClause(c, where);
  }

  public Object findColValue(String tablename, String fieldname, String strwhere)
    throws UifException
  {
    return getService().findColValue(tablename, fieldname, strwhere);
  }

  public String[] getAllPeriodByQuarter(String year_quarter)
    throws UifException
  {
    return getService().getAllPeriodByQuarter(year_quarter);
  }

  public String[] getAllPeriodByQuarter(String begin_year_quarter, String end_year_quarter)
    throws UifException
  {
    return getService().getAllPeriodByQuarter(begin_year_quarter, end_year_quarter);
  }

  public String[] getAllPeriodByYear(String year_quarter)
    throws UifException
  {
    return getService().getAllPeriodByYear(year_quarter);
  }

  public String[] getAllPeriodByYear(String beginYear, String endYear)
    throws UifException
  {
    return getService().getAllPeriodByYear(beginYear, endYear);
  }

  public String[] getAllQuarterByYear(String year)
    throws UifException
  {
    return getService().getAllQuarterByYear(year);
  }

  public String[] getAllQuarterByYear(String beginYear, String endYear)
    throws UifException
  {
    return getService().getAllQuarterByYear(beginYear, endYear);
  }

  public String[] getBatchBillNo(String billtype, String pk_corp, BillCodeObjValueVO vo, int intNum)
    throws UifException
  {
    return getService().getBatchBillNo(billtype, pk_corp, vo, intNum);
  }

  public String getBillNo(String billtype, String pk_corp, String custBillCode, BillCodeObjValueVO vo)
    throws UifException
  {
    return getService().getBillNo(billtype, pk_corp, custBillCode, vo);
  }

  public String[] getMiddlePeriod(String beginPeriod, String endPeriod)
    throws UifException
  {
    return getService().getMiddlePeriod(beginPeriod, endPeriod);
  }

  public String[] getMiddleQuarter(String beginQuarter, String endQuarter)
    throws UifException
  {
    return getService().getMiddleQuarter(beginQuarter, endQuarter);
  }

  public String[] getMiddleYear(String beginYear, String endYear)
    throws UifException
  {
    return getService().getMiddleYear(beginYear, endYear);
  }

  public String[] getPreMonth(String year, String month)
    throws UifException
  {
    return getService().getPreMonth(year, month);
  }

  public String getQuarterByPeriod(String year_period)
    throws UifException
  {
    return getService().getQuarterByPeriod(year_period);
  }

  public String insert(SuperVO vo)
    throws UifException
  {
    return getService().insert(vo);
  }

  public String[] insertAry(SuperVO[] vo)
    throws UifException
  {
    return getService().insertAry(vo);
  }

  public SuperVO[] queryAll(Class voClass)
    throws UifException
  {
    return getService().queryAll(voClass);
  }

  public CircularlyAccessibleValueObject[] queryAllBodyData(String strBillType, Class c, String key, String strWhere)
    throws UifException
  {
    return getService().queryAllBodyData(strBillType, c, key, strWhere);
  }

  public LightBillVO queryBillGraph(String billFinderClassName, String id, String type)
    throws UifException
  {
    return getService().queryBillGraph(billFinderClassName, id, type);
  }

  public AggregatedValueObject[] queryBillVOByCondition(Object userObj, String strWhere)
    throws UifException
  {
    return getService().queryBillVOByCondition(userObj, strWhere);
  }

  public AggregatedValueObject queryBillVOByPrimaryKey(Object userObj, String parentPK)
    throws UifException
  {
    return getService().queryBillVOByPrimaryKey(userObj, parentPK);
  }

  public SuperVO[] queryByCondition(Class voClass, String strWhere)
    throws UifException
  {
    return getService().queryByCondition(voClass, strWhere);
  }

  public SuperVO queryByPrimaryKey(Class voClass, String pk)
    throws UifException
  {
    return getService().queryByPrimaryKey(voClass, pk);
  }

  public ArrayList queryDefVO(String[] objNames, String pk_corp)
    throws UifException
  {
    return getService().queryDefVO(objNames, pk_corp);
  }

  public AggregatedValueObject saveBD(AggregatedValueObject billVo, Object userObj)
    throws UifException
  {
    return getService().saveBD(billVo, userObj);
  }

  public AggregatedValueObject[] saveBDs(AggregatedValueObject[] billVos, Object userObj)
    throws UifException
  {
    return getService().saveBDs(billVos, userObj);
  }

  public AggregatedValueObject saveBill(AggregatedValueObject billVo)
    throws UifException
  {
    return getService().saveBill(billVo);
  }

  public AggregatedValueObject setBillTs(AggregatedValueObject billVo)
    throws UifException
  {
    return getService().setBillTs(billVo);
  }

  public void update(SuperVO vo)
    throws UifException
  {
    getService().update(vo);
  }

  public void updateAry(SuperVO[] vo)
    throws UifException
  {
    getService().updateAry(vo);
  }
}