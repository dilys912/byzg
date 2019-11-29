package nc.bs.ia.bill.outersave;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import nc.bs.ia.boundary.pu.PUForIA;
import nc.bs.ia.pub.DataAccessUtils;
import nc.bs.ml.NCLangResOnserver;
import nc.impl.ia.bill.BillTool;
import nc.vo.ia.bill.BillHeaderVO;
import nc.vo.ia.bill.BillItemVO;
import nc.vo.ia.bill.BillVO;
import nc.vo.ia.pub.RowSet;
import nc.vo.ia.pub.SqlBuilder;
import nc.vo.ia.pub.SystemAccessException;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

public class ProcessJICAI
{
  private Vector multiCorpVec = new Vector();
  private Vector singleCorpVec = new Vector();

  public ArrayList execute(AggregatedValueObject[] vos, String cSourceModule)
    throws BusinessException
  {
    ArrayList retList = new ArrayList();
    boolean isPOStart = BillSaveUtil.isIAModuleStart((BillVO)vos[0], "4004");

    if ((!isPOStart) || (!cSourceModule.equals("IC"))) {
      retList.add(vos);
      return retList;
    }

    for (int i = 0; i < vos.length; i++) {
      BillHeaderVO header = (BillHeaderVO)vos[i].getParentVO();

      if (!header.getCbilltypecode().equals("I2")) {
        this.singleCorpVec.addElement(vos[i]);
      }
      else {
        BillItemVO[] items = (BillItemVO[])(BillItemVO[])vos[i].getChildrenVO();

        String pk_corp = header.getPk_corp();
        Vector tempV1 = new Vector();
        Vector tempV2 = new Vector();
        for (int j = 0; j < items.length; j++)
        {
          if ((items[j].getBlargessflag() != null) && (items[j].getBlargessflag().booleanValue()))
          {
            if ((!items[j].getPk_invoicecorp().equals(pk_corp)) && ((items[j].getPk_reqcorp() == null) || (pk_corp.equals(items[j].getPk_reqcorp()))))
            {
              tempV2.addElement(items[j]);
            }
            else tempV1.addElement(items[j]);
          }
          else {
            tempV1.addElement(items[j]);
          }
        }
        if (tempV1.size() > 0) {
          BillVO bill = new BillVO();
          bill.setParentVO((BillHeaderVO)header.clone());
          BillItemVO[] body = new BillItemVO[tempV1.size()];
          tempV1.copyInto(body);
          bill.setChildrenVO(body);
          this.singleCorpVec.addElement(bill);
        }
        if (tempV2.size() > 0) {
          BillVO bill = new BillVO();
          bill.setParentVO((BillHeaderVO)header.clone());
          BillItemVO[] body = new BillItemVO[tempV2.size()];
          tempV2.copyInto(body);
          bill.setChildrenVO(body);
          this.multiCorpVec.addElement(bill);
        }
      }
    }
    if (this.multiCorpVec.size() > 0) {
      BillVO[] multiCorpVOs = new BillVO[this.multiCorpVec.size()];
      this.multiCorpVec.copyInto(multiCorpVOs);
      this.multiCorpVec = processMultiCorpBills(multiCorpVOs);
    }
    retList = splitBills(this.singleCorpVec, this.multiCorpVec);
    return retList;
  }

  private Vector processMultiCorpBills(BillVO[] bills)
    throws BusinessException
  {
    Vector retVec = new Vector();
    for (int i = 0; i < bills.length; i++) {
      BillVO oldBill;
      try { oldBill = (BillVO)bills[i].clone();
      } catch (Exception e) {
        throw new SystemAccessException(e);
      }
      processHeadInfo(bills[i]);
      BillItemVO[] items = (BillItemVO[])(BillItemVO[])bills[i].getChildrenVO();
      if ((items != null) && (items.length != 0)) {
        BillTool.mapDocsBetweenCorps(bills[i], items[0].getPk_corp(), items[0].getPk_invoicecorp());
      }

      retVec.addElement(bills[i]);
      retVec.addElement(processTOInFromCGR(bills[i], oldBill));
      retVec.addElement(processTOOutFromCGR(bills[i], oldBill));
    }
    return retVec;
  }

  private void processHeadInfo(BillVO bill)
    throws BusinessException
  {
    BillItemVO[] items = (BillItemVO[])(BillItemVO[])bill.getChildrenVO();
    BillHeaderVO head = (BillHeaderVO)bill.getParentVO();

    head.setPk_corp(items[0].getPk_invoicecorp());

    String cordid = items[0].getCfirstbillid();
    String[] calbodyids = new PUForIA().getJSCalbodys(new String[] { cordid });

    if ((calbodyids == null) || (calbodyids.length == 0)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("20143010", "UPP20143010-000304"));
    }

    head.setCstockrdcenterid(calbodyids[0]);

    SqlBuilder sql = new SqlBuilder();
    sql.append("select pk_stordoc from bd_stordoc where isdirectstore = 'Y' and ");
    sql.append("pk_calbody", calbodyids[0]);
    RowSet set = (RowSet)DataAccessUtils.query(sql.toString());
    if (set.next()) {
      String store = set.getString(0);
      if ((store == null) || (store.trim().length() == 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("20143010", "UPP20143010-000305"));
      }

      head.setCwarehouseid(store);
    }

    head.setCdeptid(null);
    head.setCemployeeid(null);
    head.setCwarehousemanagerid(null);
    bill.setParentVO(head);
  }

  private BillVO processTOInFromCGR(BillVO newBill, BillVO oldBill)
  {
    BillVO retVO;
    try
    {
      retVO = (BillVO)oldBill.clone();
    } catch (Exception e) {
      throw new SystemAccessException(e);
    }

    BillHeaderVO head = (BillHeaderVO)retVO.getParentVO();
    BillHeaderVO newHead = (BillHeaderVO)newBill.getParentVO();
    head.setCbilltypecode("II");
    head.setCothercorpid(newHead.getPk_corp());
    head.setCothercalbodyid(newHead.getCstockrdcenterid());
    head.setCoutcorpid(newHead.getPk_corp());
    head.setCoutcalbodyid(newHead.getCstockrdcenterid());
    head.setBestimateflag(UFBoolean.FALSE);
    head.setFallocflag(new Integer(2));
    ArrayList items = new ArrayList();
    for (int i = 0; i < retVO.getChildrenVO().length; i++) {
      BillItemVO item = (BillItemVO)retVO.getChildrenVO()[i];
      if (item.getPk_invoicecorp().equals(item.getPk_reqcorp())) {
        continue;
      }
      if ((item.getPk_reqcorp() != null) && (!head.getPk_corp().equals(item.getPk_reqcorp())))
        continue;
      item.setNprice(null);
      item.setNmoney(null);
      item.setCbilltypecode("II");
      item.setBlargessflag(UFBoolean.TRUE);
      items.add(item);
    }

    BillItemVO[] body = new BillItemVO[items.size()];
    body = (BillItemVO[])(BillItemVO[])items.toArray(body);
    retVO.setChildrenVO(body);
    return retVO;
  }

  private BillVO processTOOutFromCGR(BillVO newBill, BillVO oldBill)
  {
    BillVO retVO;
    try
    {
      retVO = (BillVO)newBill.clone();
    } catch (Exception e) {
      throw new SystemAccessException(e);
    }
    BillHeaderVO head = (BillHeaderVO)retVO.getParentVO();
    BillHeaderVO oldHead = (BillHeaderVO)oldBill.getParentVO();
    head.setCbilltypecode("IJ");
    head.setCothercorpid(oldHead.getPk_corp());
    head.setCothercalbodyid(oldHead.getCstockrdcenterid());
    head.setCoutcorpid(head.getPk_corp());
    head.setCoutcalbodyid(head.getCstockrdcenterid());
    head.setBestimateflag(new UFBoolean(false));
    head.setFallocflag(new Integer(2));
    head.setFdispatchflag(new Integer(1));
    for (int i = 0; i < retVO.getChildrenVO().length; i++) {
      BillItemVO item = (BillItemVO)retVO.getChildrenVO()[i];
      item.setNprice(null);
      item.setNmoney(null);
      item.setCbilltypecode("IJ");
      item.setBlargessflag(new UFBoolean(true));
    }
    return retVO;
  }

  private ArrayList splitBills(Vector singleCorpBills, Vector multiCorpBills)
  {
    ArrayList retList = new ArrayList();
    if (multiCorpBills.size() == 0) {
      BillVO[] bills = new BillVO[singleCorpBills.size()];
      singleCorpBills.copyInto(bills);
      retList.add(bills);
      return retList;
    }
    Hashtable ht = new Hashtable();
    if (singleCorpBills.size() > 0) {
      String pk_corp = ((BillVO)singleCorpBills.get(0)).getPk_corp();
      ht.put(pk_corp, singleCorpBills);
    }
    if (multiCorpBills.size() > 0) {
      for (int i = 0; i < multiCorpBills.size(); i++) {
        BillVO bill = (BillVO)multiCorpBills.get(i);
        String pk_corp = bill.getPk_corp();
        if (ht.containsKey(pk_corp)) {
          ((Vector)ht.get(pk_corp)).addElement(bill);
        } else {
          Vector tempV = new Vector();
          tempV.addElement(bill);
          ht.put(pk_corp, tempV);
        }
      }
    }
    Enumeration enu = ht.keys();
    while (enu.hasMoreElements()) {
      String key = enu.nextElement().toString();
      Vector tempV = (Vector)ht.get(key);
      BillVO[] bills = new BillVO[tempV.size()];
      tempV.copyInto(bills);
      retList.add(bills);
    }
    return retList;
  }
}