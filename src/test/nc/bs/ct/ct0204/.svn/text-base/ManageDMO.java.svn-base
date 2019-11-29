package nc.bs.ct.ct0204;

import javax.naming.NamingException;
import nc.bs.pub.SystemException;
import nc.impl.ct.pub.ContractDMO;
import nc.vo.ct.pub.ManageItemVO;
import nc.vo.ct.utility.CTUtility;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class ManageDMO extends ContractDMO
{
  public ManageDMO()
    throws NamingException, SystemException
  {
  }

  public ManageDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  public CircularlyAccessibleValueObject[] queryAllBodyData(String key)
    throws BusinessException
  {
    beforeCallMethod("nc.bs.ct.ct0204.ManageDMO", "queryAllBodyData", new Object[] { key });

    StringBuffer baseSQL = new StringBuffer(1260);
    ManageItemVO[] voaBody = null;
    baseSQL.append(" SELECT ct_b.pk_ct_manage_b, ct_b.pk_ct_manage, ct_b.pk_corp, ct_b.invid,ct_b.invclid, ct_b.delivdate, ct_b.amount,")
    .append(" ct_b.measid,ct_b.astmeasid,ct_b.astnum,ct_b.transrate, ct_b.oriprice,ct_b.oritaxprice, ct_b.oritaxmny,ct_b.oritaxsummny,ct_b.orisum,")
    .append(" ct_b.natiprice, ct_b.natitaxprice,ct_b.natitaxmny,ct_b.natitaxsummny,ct_b.natisum,  ct_b.taxration, ct_b.ordprice, ct_b.ordnum,")
    .append(" ct_b.astprice,ct_b.astsum,ct_b.asttaxprice,ct_b.asttaxmny,ct_b.asttaxsummny,ct_b.sopriceid,")
    .append(" ct_b.ordsum, ct_b.inoutnum, ct_b.inoutsum, ct_b.invoicnum, ct_b.invoicesum, ct_b.paysum, ct_b.earlydate, ct_b.def1,")
    .append(" ct_b.def2, ct_b.def3, ct_b.def4, ct_b.def5, ct_b.def6, ct_b.def7, ct_b.def8, ct_b.def9, ct_b.def10,ct_b.ts,ct_b.crowno, ")
    .append(" ct_b.def11, ct_b.def12, ct_b.def13, ct_b.def14, ct_b.def15, ct_b.def16, ct_b.def17, ct_b.def18, ct_b.def19, ct_b.def20, ")
    .append(" ct_b.pk_defdoc1, ct_b.pk_defdoc2, ct_b.pk_defdoc3, ct_b.pk_defdoc4, ct_b.pk_defdoc5, ct_b.pk_defdoc6, ct_b.pk_defdoc7, ct_b.pk_defdoc8, ct_b.pk_defdoc9, ct_b.pk_defdoc10,")
    .append(" ct_b.pk_defdoc11, ct_b.pk_defdoc12, ct_b.pk_defdoc13, ct_b.pk_defdoc14, ct_b.pk_defdoc15, ct_b.pk_defdoc16, ct_b.pk_defdoc17, ct_b.pk_defdoc18, ct_b.pk_defdoc19, ct_b.pk_defdoc20, ")
    .append(" ct_b.vfree1, ct_b.vfree2, ct_b.vfree3, ct_b.vfree4, ct_b.vfree5, ct_b.vbatchcode, ct_b.cqpbaseschemeid ")
    .append(" FROM ct_manage_b ct_b INNER JOIN ct_manage ct ON ct_b.pk_ct_manage = ct.pk_ct_manage ")
    .append(" INNER JOIN ct_type type ON ct.pk_ct_type = type.pk_ct_type ")
    .append(" LEFT OUTER JOIN bd_invmandoc inv ON ct_b.invid = inv.pk_invmandoc ")
    .append(" WHERE ct_b.dr = 0  and ")
    .append(" ct_b.pk_ct_manage = '")
    .append(key)
    .append("'")
    .append(" AND (inv.iscanpurchased = 'Y' or ct_b.invid is null) ");

    StringBuffer extendSQL = new StringBuffer(2000);
    extendSQL.append(baseSQL.toString()).append(CTUtility.getCtTypeControlCondition());
    try
    {
      DMDataVO[] dmdvos = super.query(extendSQL);

      voaBody = new ManageItemVO[dmdvos.length];
      for (int i = 0; i < dmdvos.length; i++) {
        voaBody[i] = new ManageItemVO();
        dmdvos[i].translateToOtherVO(voaBody[i]);
      }
    }
    catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e.getMessage());
    }

    afterCallMethod("nc.bs.ct.ct0204.ManageDMO", "queryAllBodyData", new Object[] { key });

    return voaBody;
  }

  public CircularlyAccessibleValueObject[] queryAllHeadData(String whereString)
    throws BusinessException
  {
    if ((null == whereString) || (whereString.trim().length() == 0)) {
      whereString = " 1=1 ";
    }

    StringBuffer querySQL = new StringBuffer(70);
    querySQL.append(" type.nbusitype= 0 and ct.ctflag = ");
    querySQL.append(2);
    querySQL.append(" and ct.ifearly='N' and ( ");
    querySQL.append(whereString);
    querySQL.append(" )");
    return super.queryAllHeadData(querySQL.toString());
  }
}