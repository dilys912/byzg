package nc.ui.pub.bill;

import java.util.ArrayList;

import nc.bs.logging.Logger;
import nc.vo.pub.bill.BillOperaterEnvVO;
import nc.vo.pub.bill.BillTempletBodyVO;
import nc.vo.pub.bill.BillTempletHeadVO;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.templet.translator.BillTranslator;
import nc.vo.pub.util1.TempletVOWithTS;

final public class BillUIUtil
{
	public static BillTempletVO getDefaultTempletStatic(String strBillType, String strBusiType, String strOperator, String strCorp, String nodeKey, String billPrefix, String orgtype)
  {
    return getDefaultTempletStatic(strBillType, strBusiType, strOperator, strCorp, nodeKey, null, billPrefix, orgtype);
  }

  public static BillTempletVO getDefaultTempletStatic(String strBillType, String strBusiType, String strOperator, String strCorp, String nodeKey, String billPrefix)
  {
    return getDefaultTempletStatic(strBillType, strBusiType, strOperator, strCorp, nodeKey, billPrefix, null);
  }

  private static BillTempletVO getDefaultTempletStatic(String strBillType, String strBusiType, String strOperator, String strCorp, String nodeKey, String billTempletID, String billPrefix, String orgtype)
  {
    if ((billTempletID == null) && (strBillType == null)) {
      return null;
    }
    boolean isCard = billPrefix == "TBC_";
    BillTempletVO resultVO = null;
    try {
      boolean isFindinCache = false;
      String ceKey;
      if (billTempletID == null)
        ceKey = billPrefix + "." + strBillType + "." + nodeKey + "." + strBusiType + "." + strOperator + "." + strCorp;
      else {
        ceKey = billPrefix + "." + billTempletID;
      }
      BillOperaterEnvVO envvo = new BillOperaterEnvVO();
      envvo.setCorp(strCorp);
      envvo.setType(Integer.valueOf(2));
      BillTempletVO cardListVO = null;
      envvo.setBilltype(strBillType);
      envvo.setBusitype(strBusiType);
      envvo.setNodekey(nodeKey);
      envvo.setOperator(strOperator);
      envvo.setOrgtype(orgtype);
      envvo.setBilltemplateid(billTempletID);

      TempletVOWithTS templatevo = BillTemplateCache.getTemplet(ceKey);

      if (templatevo != null) {
        resultVO = (BillTempletVO)templatevo.getTempVO();
        envvo.setTs(templatevo.getTS());
        if (resultVO != null) {
          envvo.setBilltemplateid(resultVO.getPrimaryKey());
          Logger.debug("Found Template in Cache, KEY:" + ceKey + " ID:" + resultVO.getPrimaryKey() + " TS:" + templatevo.getTS());
        }

      }

      cardListVO = BillTemplateHelper.findBillTempletData(envvo);

      BillTempletVO cardvo = null;
      BillTempletVO listvo = null;

      if (cardListVO == null) {
        isFindinCache = true;
      } else {
        if (resultVO != null) {
          Logger.debug("Found TemplateVersion Change!");
        }
        Logger.debug("Found Template in Database,KEY:" + ceKey + " ID:" + cardListVO.getPrimaryKey() + " Last TS:" + cardListVO.getHeadVO().getTs());
        BillTempletVO[] billtempletvo = getCardListBillTempletVO(cardListVO);
        cardvo = billtempletvo[0];
        listvo = billtempletvo[1];
        resultVO = isCard ? cardvo : listvo;
      }

      if (resultVO == null) {
        Logger.info(isCard ? "未找到可用卡片模板!" : "未找到可用列表模板!");
        return null;
      }
      String ceKeyList;
      String ceKeyCard;
      if (isCard) {
        ceKeyCard = ceKey;
        ceKeyList = "TBL_" + ceKey.substring("TBC_".length());
      } else {
        ceKeyList = ceKey;
        ceKeyCard = "TBC_" + ceKey.substring("TBL_".length());
      }
      if (!isFindinCache) {
        cacheBillTempletVO(cardvo, ceKeyCard);
        cacheBillTempletVO(listvo, ceKeyList);
      }
      Logger.info(isCard ? "卡片模板加载成功!" : "列表模板加载成功!");
    } catch (Exception e) {
      Logger.error(isCard ? "卡片模板加载失败!" : "列表模板加载失败!", e);
    }

    if (resultVO != null) {
      BillTranslator.translate(resultVO);
    }

    return resultVO;
  }

  public static BillTempletVO getDefaultTempletStatic(String billTempletID, String billPrefix, String corp)
  {
    return getDefaultTempletStatic(null, null, null, corp, null, billTempletID, billPrefix, null);
  }

  @SuppressWarnings("unchecked")
private static BillTempletVO[] getCardListBillTempletVO(BillTempletVO vo)
  {
    BillTempletVO[] billtemplet = new BillTempletVO[2];

    if (vo == null) {
      return billtemplet;
    }
    ArrayList card = new ArrayList();
    ArrayList list = new ArrayList();

    BillTempletHeadVO headvo = vo.getHeadVO();

    BillTempletBodyVO[] vos = vo.getBodyVO();

    boolean iscard = false;

    if (vos != null) {
      for (int i = 0; i < vos.length; i++) {
        iscard = false;
        if ((vos[i].getCardflag() != null) && (vos[i].getCardflag().booleanValue())) {
          card.add(vos[i]);
          iscard = true;
        }
        if ((vos[i].getListflag() != null) && (vos[i].getListflag().booleanValue())) {
          if (iscard) {
            BillTempletBodyVO bodyvo = (BillTempletBodyVO)vos[i].clone();
            bodyvo.setList(true);
            list.add(bodyvo);
          } else {
            vos[i].setList(true);
            list.add(vos[i]);
          }
        }

      }

      if (card.size() > 0) {
        BillTempletBodyVO[] bodys = (BillTempletBodyVO[])card.toArray(new BillTempletBodyVO[card.size()]);
        billtemplet[0] = new BillTempletVO((BillTempletHeadVO)headvo.clone(), bodys);
      }
      if (list.size() > 0) {
        BillTempletBodyVO[] bodys = (BillTempletBodyVO[])list.toArray(new BillTempletBodyVO[list.size()]);
        billtemplet[1] = new BillTempletVO((BillTempletHeadVO)headvo.clone(), bodys);
      }
    }
    return billtemplet;
  }

  private static void cacheBillTempletVO(BillTempletVO resultVO, String ceKey)
    throws Exception
  {
    if (resultVO == null) {
      return;
    }
    TempletVOWithTS btVO = new TempletVOWithTS();
    btVO.setTempVO(resultVO);
    String ts = resultVO.getHeadVO().getTs().toString();
    if ((ts != null) && ((ts = ts.trim()).length() > 0)) {
      btVO.setTS(ts);
    }
    else
      btVO.setTS("CE00");
    BillTemplateCache.saveTemplet(ceKey, btVO);
  }
}