package nc.vo.pi;

import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.IGetBusiDataForFlow;
import nc.vo.scm.field.pu.FieldDBValidate;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.IscmDefCheckVO;

public class InvoiceVO extends AggregatedValueObject
  implements IscmDefCheckVO, IGetBusiDataForFlow
{
  private InvoiceHeaderVO header = null;
  private InvoiceItemVO[] items = null;
  long currentTimestamp;
  long initialTimestamp;
  private UFBoolean m_UserConfirmFlag = null;
  public static final int FROM_HAND = 0;
  public static final int FROM_ORDER = 1;
  public static final int FROM_QUERY = 4;
  public static final int FROM_STO = 2;
//  public static final int FROM_VMI = 3;
  private int source;

  public InvoiceVO()
  {
    this.header = new InvoiceHeaderVO();
    this.items = null;
  }

  public CircularlyAccessibleValueObject[] getChildrenVO()
  {
    return this.items;
  }

  public CircularlyAccessibleValueObject getParentVO()
  {
    return this.header;
  }

  public void setParentVO(CircularlyAccessibleValueObject parent)
  {
    this.header = ((InvoiceHeaderVO)parent);
  }

  public void setChildrenVO(CircularlyAccessibleValueObject[] children)
  {
    this.items = ((InvoiceItemVO[])children);
  }

  public InvoiceVO(int bodyNum)
  {
    this.header = new InvoiceHeaderVO();
    this.items = new InvoiceItemVO[bodyNum];

    for (int i = 0; i < bodyNum; ++i)
      this.items[i] = new InvoiceItemVO();
  }

  public InvoiceItemVO[] getBodyVO()
  {
    return this.items;
  }

  public InvoiceHeaderVO getHeadVO()
  {
    return this.header;
  }

  public void validate()
    throws ValidationException, HintMessageException
  {
    getHeadVO().validate();

    if ((getBodyVO() == null) || (getBodyVO().length == 0))
      throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("40040101", "UPP40040101-000510"));
    for (int i = 0; i < getBodyVO().length; ++i) {
      getBodyVO()[i].setNShowRow(i + 1);
      getBodyVO()[i].validate();
    }

    FieldDBValidate.validate(this);
  }

  public Object clone()
  {
    InvoiceItemVO[] items = (InvoiceItemVO[])null;
    if (getBodyVO() != null) {
      items = new InvoiceItemVO[getBodyVO().length];
      for (int i = 0; i < getBodyVO().length; ++i) {
        items[i] = ((InvoiceItemVO)getBodyVO()[i].clone());

        items[i].setCinvoiceid(null);
        items[i].setCinvoice_bid(null);

        items[i].setCorderid(null);
        items[i].setCorder_bid(null);

        items[i].setCsourcebillid(null);
        items[i].setCsourcebillrowid(null);
        items[i].setCsourcebilltype(null);
        items[i].setCupsourcebillid(null);
        items[i].setCupsourcebillrowid(null);
        items[i].setCupsourcebilltype(null);

        items[i].setNaccumsettnum(null);
        items[i].setNaccumsettmny(null);
        items[i].setNassistsettmny(null);

        items[i].setNoriginalpaymentmny(null);
        items[i].setNpaymentmny(null);
        items[i].setNassistpaymny(null);

        items[i].setNShowRow(i + 1);

        items[i].setTs(null);
        items[i].setCupsourcehts(null);
        items[i].setCupsourcebts(null);
      }
    }

    InvoiceVO invoice = new InvoiceVO();
    invoice.setParentVO((InvoiceHeaderVO)getHeadVO().clone());
    invoice.setChildrenVO(items);

    invoice.setSource(0);

    invoice.getHeadVO().setCinvoiceid(null);

    invoice.getHeadVO().setVinvoicecode(null);

    invoice.getHeadVO().setCoperator(null);

    invoice.getHeadVO().setCauditpsn(null);
    invoice.getHeadVO().setDauditdate(null);
    invoice.getHeadVO().setCvendorphone(null);

    invoice.getHeadVO().setCvoucherid(null);

    invoice.getHeadVO().setIbillstatus(new Integer(0));

    invoice.getHeadVO().setTs(null);

    return invoice;
  }

  public String getPrimaryKey()
  {
    if (getHeadVO() == null) return null;
    return getHeadVO().getPrimaryKey();
  }

  public String toString()
  {
    return getHeadVO().getPrimaryKey();
  }

  public int getSource()
  {
    return this.source;
  }

  public UFBoolean getUserConfirmFlag() {
    return this.m_UserConfirmFlag;
  }

  public void setUserConfirmFlag(UFBoolean b) {
    this.m_UserConfirmFlag = b;
  }

  public void setSource(int newSource)
  {
    this.source = newSource;
  }

  public boolean isVirtual()
  {
    return (getHeadVO().getIinvoicetype().intValue() == 3);
  }

  public Object[] getBodyDefValues(int iserial)
  {
    if (getChildrenVO() == null) {
      return null;
    }
    Object[] oaRet = new Object[getChildrenVO().length];
    for (int i = 0; i < oaRet.length; ++i) {
      oaRet[i] = getChildrenVO()[i].getAttributeValue("vdef" + iserial);
    }
    return oaRet;
  }

  public String getCbilltypedef()
  {
    return "25";
  }

  public Object getHeadDefValue(int iserial)
  {
    if (getParentVO() == null) {
      return null;
    }
    return getParentVO().getAttributeValue("vdef" + iserial);
  }

  public String getPk_corp() {
    if (this.header != null) {
      return this.header.getPk_corp();
    }
    return null;
  }

  public int getBodyLen()
  {
    if (getBodyVO() == null) {
      return 0;
    }
    return getBodyVO().length;
  }

  public UFDouble getPfAssMoney()
  {
    return null;
  }

  public String getPfCurrency()
  {
    return null;
  }

  public UFDouble getPfLocalMoney()
  {
    if (getBodyLen() == 0) {
      return null;
    }
    UFDouble ufdSumNmny = new UFDouble(0.0D);
    for (int i = 0; i < getBodyLen(); ++i) {
      if (getBodyVO()[i] == null) {
        continue;
      }
      ufdSumNmny = ufdSumNmny.add(PuPubVO.getUFDouble_NullAsZero(getBodyVO()[i].getNmoney()));
    }
    return ufdSumNmny;
  }

  public UFDouble getPfMoney()
  {
    if (getBodyLen() == 0) {
      return null;
    }
    UFDouble ufdSumNmny = new UFDouble(0.0D);
    for (int i = 0; i < getBodyLen(); ++i) {
      if (getBodyVO()[i] == null) {
        continue;
      }
      ufdSumNmny = ufdSumNmny.add(PuPubVO.getUFDouble_NullAsZero(getBodyVO()[i].getNoriginalcurmny()));
    }
    return ufdSumNmny;
  }
}