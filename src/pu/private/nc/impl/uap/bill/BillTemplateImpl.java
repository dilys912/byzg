package nc.impl.uap.bill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import nc.bp.impl.uap.template.TemplateBpImpl;
import nc.bp.itf.template.ITemplateBP;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.ComponentException;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.SuperDMO;
import nc.bs.pub.bill.BillTempletDAO;
import nc.bs.pub.bill.BillTypeQryBP;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uap.bd.refcheck.IReferenceCheck;
import nc.itf.uap.billtemplate.IBillTemplateBase;
import nc.itf.uap.billtemplate.IBillTemplateQry;
import nc.itf.uap.pf.IPFTemplate;
import nc.vo.pub.BusinessException;
import nc.vo.pub.bill.BillOperaterEnvVO;
import nc.vo.pub.bill.BillStructVO;
import nc.vo.pub.bill.BillTabVO;
import nc.vo.pub.bill.BillTemplateRuntimeException;
import nc.vo.pub.bill.BillTempletBodyVO;
import nc.vo.pub.bill.BillTempletHeadVO;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.pftemplate.SystemplateVO;
import nc.vo.uap.pf.TemplateParaVO;

public class BillTemplateImpl
  implements IBillTemplateBase, IBillTemplateQry
{
  private ITemplateBP tempBp = null;

  private void delete(String id)
    throws BusinessException
  {
    BillTempletDAO dao = new BillTempletDAO();
    try
    {
      dao.delete(id);
      SuperDMO sd = new SuperDMO();
      sd.deleteByWhereClause(BillTabVO.class, "pk_billtemplet='" + id + "'");
    } finally {
      dao.release();
    }
  }

  public String deleteWithValidate(String id)
    throws BusinessException
  {
    boolean isUsed = false;
    try
    {
      IPFTemplate iIPFTemplate = (IPFTemplate)NCLocator.getInstance().lookup(IPFTemplate.class.getName());

      isUsed = iIPFTemplate.isTemplateUsed(id, 0);
      if (isUsed) {
        return NCLangResOnserver.getInstance().getStrByID("_Bill", "UPP_Bill-000480") + "!";
      }

      IReferenceCheck iIReferenceCheck = (IReferenceCheck)NCLocator.getInstance().lookup(IReferenceCheck.class.getName());

      isUsed = iIReferenceCheck.isReferenced("pub_billtemplet", id);

      if (isUsed) {
        return NCLangResOnserver.getInstance().getStrByID("_Bill", "UPP_Bill-000481") + "!";
      }

    }
    catch (ComponentException e)
    {
      throw new BillTemplateRuntimeException(e.getMessage(), e);
    }

    delete(id);

    return null;
  }

  public BillTempletVO findCardTempletData(String id)
    throws BusinessException
  {
    BillTempletVO btVO = findCardTempletData(id, null);

    return btVO;
  }

  public BillTempletVO findCardTempletData(String id, String pkCorp)
    throws BusinessException
  {
    BillTempletVO btVO = null;
    BillTempletDAO dao = new BillTempletDAO();
    try
    {
      btVO = dao.findTempletData(id, BillTempletDAO.CARD, pkCorp);
    } finally {
      dao.release();
    }
    return btVO;
  }

  public BillTempletVO findDefaultCardTempletData(String typecode, String corp, String busitype, String operator)
    throws BusinessException
  {
    BillTempletVO btVO = null;
    BillTempletDAO dao = new BillTempletDAO();
    try
    {
      String id = findSystemplateID(typecode, corp, operator, busitype, null, null);

      if (id == null)
        id = dao.findDefaultTempletID(typecode, false);
      btVO = dao.findTempletData(id, BillTempletDAO.CARD, corp);
      BillTempletVO localBillTempletVO1 = btVO;
      return localBillTempletVO1;
    }
    finally
    {
      dao.release();
    }//throw localObject;
  }

  public BillTempletVO findListTempletData(String id)
    throws BusinessException
  {
    return findListTempletData(id, null);
  }

  public BillTempletVO findListTempletData(String id, String pkCorp)
    throws BusinessException
  {
    BillTempletVO btVO = null;
    BillTempletDAO dao = new BillTempletDAO();
    try
    {
      btVO = dao.findTempletData(id, BillTempletDAO.LIST, pkCorp);
    } finally {
      dao.release();
    }
    return btVO;
  }

  public BillTempletHeadVO[] findTemplet(String where)
    throws BusinessException
  {
    BillTempletHeadVO[] headVOs = null;
    BillTempletDAO dao = new BillTempletDAO();
    try
    {
      headVOs = dao.findTemplet(where);
    } finally {
      dao.release();
    }
    return headVOs;
  }

  public BillTempletVO findTempletData(String id)
    throws BusinessException
  {
    return findTempletData(id, null);
  }

  public BillTempletVO findTempletData(String id, String pkCorp)
    throws BusinessException
  {
    BillTempletVO btVO = null;
    BillTempletDAO dao = new BillTempletDAO();
    try
    {
      btVO = dao.findTempletData(id, null, pkCorp);
    } finally {
      dao.release();
    }
    return btVO;
  }

  public String[] findTempletIDWithTS(String typecode, String corp, String busitype, String operator)
    throws BusinessException
  {
    return findTempletIDWithTS(typecode, corp, busitype, operator, null);
  }

  private String[] findTempletIDWithTS(String typecode, String corp, String busitype, String operator, String nodeKey, String orgtype)
    throws BusinessException
  {
    String[] idts = null;
    BillTempletDAO dao = new BillTempletDAO();
    try
    {
      String id = findSystemplateID(typecode, corp, busitype, operator, nodeKey, orgtype);

      if (id == null)
        id = dao.findDefaultTempletID(typecode, false);
      if (id != null)
        idts = dao.findTempletIDWithTS(id);
      String[] arrayOfString1 = idts;
      return arrayOfString1;
    }
    finally
    {
      dao.release();
    }//throw localObject;
  }

  private String findSystemplateID(String typecode, String corp, String busitype, String operator, String nodeKey, String orgtype)
    throws BusinessException
  {
    String id = null;
    try
    {
      if (orgtype != null) {
        orgtype = "1";
      }

      String strNodeCode = findNodeCode(typecode);

      TemplateParaVO paraVo = new TemplateParaVO();
      paraVo.setTemplateType(0);
      paraVo.setPk_orgUnit(corp);
      paraVo.setOrgType(orgtype);
      paraVo.setFunNode(strNodeCode);
      paraVo.setOperator(operator);
      paraVo.setBusiType(busitype);
      paraVo.setNodeKey(nodeKey);

      id = getTempBp().queryTemplateId(paraVo);
    }
    catch (Exception e)
    {
      Logger.warn("没有找到默认模板!!!");
    }
    return id;
  }

  private String findNodeCode(String strBillTypeCode) throws BusinessException {
    if ((strBillTypeCode == null) || (strBillTypeCode.length() > 4)) {
      return strBillTypeCode;
    }
    String findNodeCode = strBillTypeCode;

    String strNodeCode = BillTypeQryBP.getNodeCode(strBillTypeCode);

    if (strNodeCode != null) {
      findNodeCode = strNodeCode;
    }
    return findNodeCode;
  }

  public String[] findTempletIDWithTS(String typecode, String corp, String busitype, String operator, String nodeKey) throws BusinessException
  {
    return findTempletIDWithTS(typecode, corp, busitype, operator, nodeKey, null);
  }

  public BillTempletHeadVO[] findTempletWithBillTypeElse(String where)
    throws BusinessException
  {
    return findTemplet(where);
  }

  public BillTempletHeadVO[] findTempletIDsByNodeCodeAndPkCorp(String nodeCode, String pkCorp)
    throws BusinessException
  {
    return findTempletIDsByNodeCodesAndPkCorp(new String[] { nodeCode }, pkCorp);
  }

  public BillTempletHeadVO[] findTempletIDsByNodeCodesAndPkCorp(String[] nodeCodes, String pkCorp)
    throws BusinessException
  {
    if ((nodeCodes == null) || (nodeCodes.length == 0) || (pkCorp == null) || ((pkCorp = pkCorp.trim()).length() == 0))
    {
      return null;
    }
    String where = null;

    BillTempletDAO dao = new BillTempletDAO();
    String[] billtypes = null;
    try {
      billtypes = dao.findTempletType(nodeCodes);
    } finally {
      dao.release();
    }

    if ((billtypes == null) || (billtypes.length == 0))
    {
      billtypes = BillTypeQryBP.getBillTypes(nodeCodes);
      if ((billtypes == null) || (billtypes.length == 0))
      {
        billtypes = nodeCodes;
      }
    }

    where = "pk_billtypecode in ('" + billtypes[0];

    for (int i = 1; i < billtypes.length; i++) {
      where = where + "','" + billtypes[i];
    }
    where = where + "') and pk_corp in ('0001','" + pkCorp + "')";

    BillTempletHeadVO[] headvo = findTemplet(where);
    return headvo;
  }

  public void overWriteBillTempletVO(BillTempletVO btVO)
    throws BusinessException
  {
    insertOrUpdateBillTempletVO(btVO, false, false);
  }

  public BilltypeVO[] queryAllBilltypes()
    throws BusinessException
  {
    BilltypeVO[] vos = null;
    BillTempletDAO dao = new BillTempletDAO();
    try
    {
      BilltypeVO[] vos1 = BillTypeQryBP.getAllBillTypeVOs();

      BilltypeVO[] vos2 = dao.queryBilltypeElse();
      BilltypeVO[] arrayOfBilltypeVO1;
      if (vos1 == null) {
        arrayOfBilltypeVO1 = vos2;
        return arrayOfBilltypeVO1;
      }
      if (vos2 == null) {
        arrayOfBilltypeVO1 = vos1;
        return arrayOfBilltypeVO1;
      }
      vos = new BilltypeVO[vos1.length + vos2.length];
      System.arraycopy(vos1, 0, vos, 0, vos1.length);
      System.arraycopy(vos2, 0, vos, vos1.length, vos2.length);

      Object c = new Comparator() {
        public int compare(BilltypeVO o1, BilltypeVO o2) {
          return o1.getPk_billtypecode().compareTo(o2.getPk_billtypecode());
        }

		public int compare(Object o1, Object o2) {
			// TODO Auto-generated method stub
			return 0;
		}
      };
      Arrays.sort(vos, (Comparator)c);
      BilltypeVO[] arrayOfBilltypeVO2 = vos;
      return arrayOfBilltypeVO2;
    }
    finally
    {
      dao.release();
    }//throw localObject1;
  }

  public String insertEx(BillTempletVO btVO)
    throws BusinessException
  {
    return insertOrUpdateBillTempletVO(btVO, true, false);
  }

  public BillTempletVO findBillTempletData(BillOperaterEnvVO envvo)
    throws BusinessException
  {
    if (envvo == null) {
      return null;
    }
    BillTempletVO btVO = null;
    BillTempletDAO dao = new BillTempletDAO();
    try
    {
      String billtype = envvo.getBilltype();
      String busitype = envvo.getBusitype();
      String operator = envvo.getOperator();
      String corp = envvo.getCorp();
      String nodekey = envvo.getNodekey();
      String orgtype = envvo.getOrgtype();
      String billID = envvo.getBilltemplateid();
      String ts = envvo.getTs();

      String newBillID = null;
      String newts = null;
      String[] idts = null;
      boolean isload = true;

      if (billtype != null)
        idts = findTempletIDWithTS(billtype, corp, busitype, operator, nodekey, orgtype);
      else {
        idts = dao.findTempletIDWithTS(billID);
      }

      if (idts != null) {
        newBillID = idts[0];
        newts = idts[1];
      }

      if ((newBillID != null) && 
        (newBillID.equals(billID)) && (newts.equals(ts))) {
        isload = false;
      }

      if ((newBillID != null) && (isload)) {
        int type = envvo.getType().intValue();
        if (type == 0)
          btVO = dao.findTempletData(newBillID, BillTempletDAO.CARD, corp);
        else if (type == 1)
          btVO = dao.findTempletData(newBillID, BillTempletDAO.LIST, corp);
        else
          btVO = dao.findTempletData(newBillID, null, corp);
      }
    } finally {
      dao.release();
    }
    return btVO;
  }

  private String insertOrUpdateBillTempletVO(BillTempletVO btVO, boolean isInsert, boolean insertIgnoreID)
    throws BusinessException
  {
    String id = "";
    BillTempletDAO dao = new BillTempletDAO();
    try
    {
      BillStructVO bsVO = btVO.getHeadVO().getStructvo();

      BillTabVO[] btvos = null;
      if (bsVO != null)
      {
        String cardStyle = bsVO.getCardStyle();
        if ((cardStyle != null) && ((cardStyle = cardStyle.trim()).equals("default")))
          bsVO.setCardStyle(null);
        btvos = bsVO.getBillTabVOs();
        bsVO.setBillTabVOs(null);

        btVO.getHeadVO().setStructvoAndClearOptions(bsVO);
      }
      if (isInsert)
      {
        id = dao.insert(btVO);
      }
      else {
        dao.overWriteBillTempletVO(btVO);
        id = btVO.getHeadVO().getPrimaryKey();
      }

      if (btvos != null) {
        for (int i = 0; i < btvos.length; i++) {
          btvos[i].setPk_billtemplet(id);
        }
      }
      SuperDMO sd = new SuperDMO();
      if (!isInsert) {
        sd.deleteByWhereClause(BillTabVO.class, "pk_billtemplet='" + id + "'");
      }
      if (btvos != null) {
        for (int i = 0; i < btvos.length; i++)
          btvos[i].setPrimaryKey(null);
        sd.insertArray(btvos);
      }
      //int i = id;
      return id;
    }
    finally
    {
      dao.release();
    }//throw localObject;
  }

  public String copySysTemplate(String billid)
    throws BusinessException
  {
    if ((billid == null) || (billid.trim().length() == 0)) {
      return null;
    }
    BillTempletDAO billdao = new BillTempletDAO();
    try
    {
      BillTempletVO bill = billdao.findTempletData(billid, BillTempletDAO.ALL, null);

      if ((bill != null) && (bill.getHeadVO().getBillTempletName().trim().equalsIgnoreCase("SYSTEM")) && (bill.getHeadVO().getPkCorp().equals("@@@@")))
      {
        SystemplateVO svo = new SystemplateVO();
        svo.setTemplateid(billid);
        svo.setTempstyle(new Integer(0));

        IUAPQueryBS iIUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());

        Collection col = iIUAPQueryBS.retrieve(svo, true);

        SystemplateVO[] sysVOs = (SystemplateVO[])(SystemplateVO[])col.toArray(new SystemplateVO[col.size()]);

        if ((sysVOs != null) && (sysVOs.length > 0)) {
          ArrayList list = new ArrayList();
          for (int i = 0; i < sysVOs.length; i++) {
            if (sysVOs[i].getPk_corp() == null)
              list.add(sysVOs[i]);
            else if (sysVOs[i].getPk_corp().equals("@@@@"))
              list.add(sysVOs[i]);
          }
          if (list.size() > 0)
            sysVOs = (SystemplateVO[])list.toArray(new SystemplateVO[list.size()]);
          else
            sysVOs = null;
        }
        if ((sysVOs != null) && (sysVOs.length > 0))
        {
          BillTempletHeadVO head = bill.getHeadVO();

          head.setBillTempletName(head.getBillTempletCaption());
          head.setPkCorp("0001");
          String id = head.getPrimaryKey();
          if (!"COPY".equalsIgnoreCase(id.substring(0, 4)))
            id = "COPY" + id.substring(4);
          else {
            id = "0001" + id.substring(4);
          }
          head.setPrimaryKey(id);

          BillStructVO bsvo = head.getStructvo();
          if (bsvo != null) {
            BillTabVO[] btvos = bsvo.getBillTabVOs();
            if (btvos != null) {
              for (int i = 0; i < btvos.length; i++) {
                btvos[i].setPk_billtemplet(id);
              }
            }
          }
          BillTempletBodyVO[] bodys = bill.getBodyVO();
          if (bodys != null) {
            for (int i = 0; i < bodys.length; i++) {
              bodys[i].setPk_billtemplet(id);
              bodys[i].setPrimaryKey(null);
            }
          }

          Logger.debug("$$$$$$$$$复制系统模板：" + billid);
          insertEx(bill);

          for (int i = 0; i < sysVOs.length; i++) {
            sysVOs[i].setTemplateid(id);
          }

          IVOPersistence iIUAPBService = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());

          iIUAPBService.updateVOArray(sysVOs);

          String str1 = id;
          return str1;
        }
      }
    }
    catch (ComponentException e)
    {
      throw new BillTemplateRuntimeException(e.getMessage(), e);
    } finally {
      billdao.release();
    }
    return null;
  }

  public BillTempletVO findTempletDataNew(String id)
    throws BusinessException
  {
    BillTempletVO btVO = null;
    BillTempletDAO dao = new BillTempletDAO();
    try
    {
      btVO = dao.findTempletData(id, BillTempletDAO.ALL, null);
    } finally {
      dao.release();
    }
    return btVO;
  }

  public ITemplateBP getTempBp() {
    if (this.tempBp == null)
      this.tempBp = new TemplateBpImpl();
    return this.tempBp;
  }

  public void setTempBp(ITemplateBP tempBp) {
    this.tempBp = tempBp;
  }
}