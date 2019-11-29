package nc.bs.ia.boundary.sc;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.ComponentException;
import nc.bs.ia.pub.DataAccessUtils;
import nc.bs.ia.pub.IAContext;
import nc.bs.ia.pub.IATool;
import nc.itf.sc.inter.IScToIa_MaterialledgerDMO;
import nc.itf.sc.inter.IScToIa_Materialledger_QueryDMO;
import nc.vo.ia.bill.BillHeaderVO;
import nc.vo.ia.bill.BillItemVO;
import nc.vo.ia.bill.BillVO;
import nc.vo.ia.ia501.IaInoutledgerVO;
import nc.vo.ia.pub.ExceptionUtils;
import nc.vo.ia.pub.IRowSet;
import nc.vo.ia.pub.SqlBuilder;
import nc.vo.ia.pub.SystemAccessException;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sc.init.MaterialledgerVO;
import nc.vo.sc.pub.CheckDataParaVO1;
import nc.vo.sc.pub.CheckDataParaVO2;

public class SCForIA
{
  public void addSCAccount(BillVO[] bills)
  {
    MaterialledgerVO[] vos = construct(bills);
    if (vos.length == 0) {
      return;
    }
    IScToIa_MaterialledgerDMO service = null;
    try {
      service = (IScToIa_MaterialledgerDMO)NCLocator.getInstance().lookup(IScToIa_MaterialledgerDMO.class.getName());

      service.updateSCForICAudit(vos);
    }
    catch (ComponentException ex) {
      ExceptionUtils.wrappException(ex);
    }
    catch (BusinessException ex) {
      ExceptionUtils.wrappException(ex);
    }
  }

  private MaterialledgerVO[] construct(BillVO[] bills)
  {
    List list = new ArrayList();
    int length = bills.length;
    for (int i = 0; i < length; ++i) {
      BillHeaderVO header = (BillHeaderVO)bills[i].getParentVO();
      BillItemVO[] items = (BillItemVO[])(BillItemVO[])bills[i].getChildrenVO();
      int size = items.length;
      for (int j = 0; j < size; ++j) {
        MaterialledgerVO vo = construct(header, items[j]);
        list.add(vo);
      }
    }
    length = list.size();
    MaterialledgerVO[] vos = new MaterialledgerVO[length];
    vos = (MaterialledgerVO[])(MaterialledgerVO[])list.toArray(vos);
    return vos;
  }

  private MaterialledgerVO construct(BillHeaderVO header, BillItemVO item)
  {
    MaterialledgerVO vo = new MaterialledgerVO();

    vo.setPk_corp(header.getPk_corp());

    vo.setCvendormangid(header.getCcustomvendorid());

    vo.setCbillid(header.getCbillid());

    vo.setDbilldate(header.getDbilldate());

    vo.setVmemo(header.getVnote());

    vo.setCorderid(item.getCfirstbillid());

    vo.setCorder_bid(item.getCfirstbillitemid());

    vo.setCprocessmangid(item.getVbomcode());

    vo.setCmaterialmangid(item.getCinventoryid());

    vo.setCbill_bid(item.getCbill_bid());

    vo.setNnum(item.getNnumber());

    vo.setVbatch(item.getVbatch());

    vo.setVfree1(item.getVfree1());
    vo.setVfree2(item.getVfree2());
    vo.setVfree3(item.getVfree3());
    vo.setVfree4(item.getVfree4());
    vo.setVfree5(item.getVfree5());
    return vo;
  }

  public void removeSCAccount(BillVO[] bills)
  {
    MaterialledgerVO[] vos = construct(bills);
    if (vos.length == 0)
      return;
    try
    {
      IScToIa_Materialledger_QueryDMO service = (IScToIa_Materialledger_QueryDMO)NCLocator.getInstance().lookup(IScToIa_Materialledger_QueryDMO.class.getName());

      IScToIa_MaterialledgerDMO service2 = (IScToIa_MaterialledgerDMO)NCLocator.getInstance().lookup(IScToIa_MaterialledgerDMO.class.getName());

      for (int i = 0; i < vos.length; ++i) {
        MaterialledgerVO[] delVOs = new MaterialledgerVO[1];
        delVOs[0] = vos[i];
        boolean flag = service.isCanDelBillForIA(vos[i].getCbillid());
        if (!(flag)) {
          throw new BusinessException("不能删除存货单据,可能后续存在核销的委外加工收货单");
        }
        service2.updateSCForICUnAudit(delVOs);
      }
    }
    catch (ComponentException ex) {
      ExceptionUtils.wrappException(ex);
    }
    catch (BusinessException ex) {
      ExceptionUtils.wrappException(ex);
    }
  }

  public void updateSCForIAOutAccount(IaInoutledgerVO[] vos)
  {
    CheckDataParaVO1 vo = contructOut(vos);
    try {
      IScToIa_Materialledger_QueryDMO bo = (IScToIa_Materialledger_QueryDMO)NCLocator.getInstance().lookup(IScToIa_Materialledger_QueryDMO.class.getName());

      MaterialledgerVO[] mvos = bo.check_updateSCForIACKCBJS(vo);

      IScToIa_MaterialledgerDMO service = (IScToIa_MaterialledgerDMO)NCLocator.getInstance().lookup(IScToIa_MaterialledgerDMO.class.getName());

      service.updateSCForIACKCBJS(vo, mvos);
    }
    catch (ComponentException ex) {
      ExceptionUtils.wrappException(ex);
    }
    catch (BusinessException ex) {
      ExceptionUtils.wrappException(ex);
    }
  }

  public void updateSCForIAOutUnAccount(IaInoutledgerVO[] vos)
  {
    isCanUnCKCBJSForIA(vos);
    CheckDataParaVO1 vo = contructOut(vos);
    try {
      IScToIa_Materialledger_QueryDMO bo = (IScToIa_Materialledger_QueryDMO)NCLocator.getInstance().lookup(IScToIa_Materialledger_QueryDMO.class.getName());

      MaterialledgerVO[] mvos = bo.check_updateSCForIACKUnCBJS(vo);

      IScToIa_MaterialledgerDMO service = (IScToIa_MaterialledgerDMO)NCLocator.getInstance().lookup(IScToIa_MaterialledgerDMO.class.getName());

      service.updateSCForIACKUnCBJS(vo, mvos);
    }
    catch (ComponentException ex) {
      ExceptionUtils.wrappException(ex);
    }
    catch (BusinessException ex) {
      ExceptionUtils.wrappException(ex);
    }
  }

  private void isCanUnCKCBJSForIA(IaInoutledgerVO[] vos)
  {
    int length = vos.length;
    List list = new ArrayList();
    for (int i = 0; i < length; ++i) {
      list.add(vos[i].getCbill_bid());
    }
    String[] cbill_bids = new String[length];
    cbill_bids = (String[])(String[])list.toArray(cbill_bids);
    UFBoolean[] flag = null;
    try {
      IScToIa_Materialledger_QueryDMO service = (IScToIa_Materialledger_QueryDMO)NCLocator.getInstance().lookup(IScToIa_Materialledger_QueryDMO.class.getName());

      flag = service.isCanUnCKCBJSForIA(cbill_bids);
    }
    catch (ComponentException ex) {
      ExceptionUtils.wrappException(ex);
    }
    catch (BusinessException ex) {
      ExceptionUtils.wrappException(ex);
    }
    length = flag.length;
    for (int i = 0; i < length; ++i)
      if (!(flag[i].booleanValue())) {
        String cbill_bid = cbill_bids[i];
        String vbillcode = findBill(cbill_bid);
        String message = vbillcode + "单据不能取消成本计算，后续可能有委外加工收货单已经成本计算";
        ExceptionUtils.wrappBusinessException(message);
      }
  }

  private String findBill(String cbill_bid)
  {
    String vbillcode = null;
    SqlBuilder sql = new SqlBuilder();
    sql.append("select vbillcode from ia_bill_b where ");
    sql.append(" cbill_bid", cbill_bid);
    sql.append(" and dr=0 ");
    IRowSet rowset = DataAccessUtils.query(sql.toString());
    if (rowset.next()) {
      vbillcode = rowset.getString(0);
    }
    return vbillcode;
  }

  private CheckDataParaVO1 contructOut(IaInoutledgerVO[] vos)
  {
    CheckDataParaVO1 vo = new CheckDataParaVO1();
    int length = vos.length;
    List mnyList = new ArrayList();
    List priceList = new ArrayList();
    List billIDList = new ArrayList();
    List billItemIDList = new ArrayList();
    String pk_corp = null;
    for (int i = 0; i < length; ++i) {
      pk_corp = vos[i].getPk_corp();
      mnyList.add(vos[i].getNmoney());
      priceList.add(vos[i].getNprice());
      billIDList.add(vos[i].getCbillid());
      billItemIDList.add(vos[i].getCbill_bid());
    }
    UFDouble[] mnys = new UFDouble[length];
    mnys = (UFDouble[])(UFDouble[])mnyList.toArray(mnys);

    UFDouble[] prices = new UFDouble[length];
    prices = (UFDouble[])(UFDouble[])priceList.toArray(prices);

    String[] billIDs = new String[length];
    billIDs = (String[])(String[])billIDList.toArray(billIDs);

    String[] billItemIDs = new String[length];
    billItemIDs = (String[])(String[])billItemIDList.toArray(billItemIDs);

    vo.setPk_corp(pk_corp);
    vo.setMnys(mnys);
    vo.setPrices(prices);
    vo.setBillIDs(billIDs);
    vo.setBillRowIDs(billItemIDs);
    return vo;
  }

  public void updateSCForIAInAccount(IaInoutledgerVO[] vos, Object obj)
  {
    if (obj == null) {
      return;
    }
    CheckDataParaVO2 vo = (CheckDataParaVO2)obj;
    try {
      IScToIa_Materialledger_QueryDMO bo = (IScToIa_Materialledger_QueryDMO)NCLocator.getInstance().lookup(IScToIa_Materialledger_QueryDMO.class.getName());

      MaterialledgerVO[] mvos = bo.check_updateSCForIARKCBJS(vo);

      IScToIa_MaterialledgerDMO service = (IScToIa_MaterialledgerDMO)NCLocator.getInstance().lookup(IScToIa_MaterialledgerDMO.class.getName());

      service.updateSCForIARKCBJS(vo, mvos);
    }
    catch (ComponentException ex) {
      throw new SystemAccessException(ex);
    }
    catch (BusinessException ex) {
      throw new SystemAccessException(ex);
    }
  }

  public void updateSCForIAInUnAccount(IaInoutledgerVO[] vos)
  {
    CheckDataParaVO2 vo = new CheckDataParaVO2();
    contructIn(vos, vo);
    IScToIa_MaterialledgerDMO service = null;
    try {
      IScToIa_Materialledger_QueryDMO bo = (IScToIa_Materialledger_QueryDMO)NCLocator.getInstance().lookup(IScToIa_Materialledger_QueryDMO.class.getName());

      Vector data = bo.check_updateSCForIARKUnCBJS(vo);

      service = (IScToIa_MaterialledgerDMO)NCLocator.getInstance().lookup(IScToIa_MaterialledgerDMO.class.getName());

      service.updateSCForIARKUnCBJS(vo, data);
    }
    catch (ComponentException ex) {
      ExceptionUtils.wrappException(ex);
    }
    catch (BusinessException ex) {
      ExceptionUtils.wrappException(ex);
    }
  }

  private void contructIn(IaInoutledgerVO[] vos, CheckDataParaVO2 vo)
  {
    int length = vos.length;
    List billIDList = new ArrayList();
    List billItemIDList = new ArrayList();
    String pk_corp = null;
    for (int i = 0; i < length; ++i) {
      pk_corp = vos[i].getPk_corp();
      billIDList.add(vos[i].getCsourcebillid());
      billItemIDList.add(vos[i].getCsourcebillitemid());
    }
    String[] billIDs = new String[length];
    billIDs = (String[])(String[])billIDList.toArray(billIDs);

    String[] billItemIDs = new String[length];
    billItemIDs = (String[])(String[])billItemIDList.toArray(billItemIDs);

    vo.setPk_corp(pk_corp);
    vo.setBillIDs(billIDs);
    vo.setBillRowIDs(billItemIDs);
  }

  public Object getCaiLiao(IaInoutledgerVO ledgerVO)
  {
    CheckDataParaVO2 data = null;
    String pk_corp = ledgerVO.getPk_corp();
    String csourcebillid = ledgerVO.getCsourcebillid();
    String csourcebillitemid = ledgerVO.getCsourcebillitemid();
    try {
      IScToIa_Materialledger_QueryDMO service = (IScToIa_Materialledger_QueryDMO)NCLocator.getInstance().lookup(IScToIa_Materialledger_QueryDMO.class.getName());

      data = service.getSCPriceForIARKCBJS(pk_corp, csourcebillid, csourcebillitemid);
    }
    catch (ComponentException ex)
    {
      ExceptionUtils.wrappException(ex);
    }
    catch (BusinessException ex) {
      ExceptionUtils.wrappException(ex);
    }
    return data;
  }

  public void calculate(Object obj, IAContext context, IaInoutledgerVO ledgerVO)
  {
    CheckDataParaVO2 vo = (CheckDataParaVO2)obj;
    UFDouble[] mnys = vo.getMnys();
    UFDouble[] numbers = vo.getNums();
    UFDouble[] prices = vo.getPrices();
    UFDouble[] cailiaoNums = vo.getVerifyNums();
    if ((mnys == null) || (numbers == null) || (mnys.length != numbers.length) || (mnys.length == 0))
    {
      throw new IllegalArgumentException("mny or number is null");
    }
    int length = mnys.length;
    UFDouble price = null;
    UFDouble nmoney = null;

    for (int i = 0; i < length; ++i) {
      UFDouble nabmny = mnys[i];
      UFDouble nabnum = numbers[i];
      UFDouble number = cailiaoNums[i];
      UFDouble[] data = calculate(context, nabmny, nabnum, number);
      mnys[i] = data[0];
      prices[i] = data[1];
      nmoney = IATool.getInstance().add(nmoney, data[0]);
    }

    UFDouble nnumber = ledgerVO.getNnumber();
    ledgerVO.setNdrawsummny(nmoney);

    nmoney = IATool.getInstance().add(nmoney, ledgerVO.getNmoney());
    price = nmoney.div(nnumber);
    price = context.adjustPrice(price);

    ledgerVO.setNmoney(nmoney);
    ledgerVO.setNprice(price);
  }

  private UFDouble[] calculate(IAContext context, UFDouble nabmny, UFDouble nabnum, UFDouble number)
  {
    UFDouble[] data = new UFDouble[2];
    UFDouble nmoney = null;
    UFDouble nprice = null;

    if (number.doubleValue() == 0.0D) {
      nmoney = new UFDouble(0);
      nprice = new UFDouble(0);
    }
    else if (nabnum.doubleValue() == number.doubleValue()) {
      nmoney = nabmny;
      nprice = nmoney.div(number);
      nprice = context.adjustPrice(nprice);
    }
    else if (nabnum.doubleValue() == 0.0D) {
      String message = "委外明细帐上结存数量为0";
      ExceptionUtils.wrappBusinessException(message);
    }
    else {
      nprice = nabmny.div(nabnum);
      nprice = context.adjustPrice(nprice);
      nmoney = number.multiply(nprice);
      nmoney = context.adjustMoney(nmoney);
    }
    data[0] = nmoney;
    data[1] = nprice;
    return data;
  }
}