package nc.ui.scm.pub.def;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import nc.itf.uap.bd.def.IDef;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.RefCall;
import nc.ui.bd.service.BDDef;
import nc.ui.dbcache.DBCacheFacade;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.scm.service.LocalCallService;
import nc.vo.bd.def.DefVO;
import nc.vo.bd.def.DefdefVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.query.QueryConditionVO;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.datatype.DataTypeTool;
import nc.vo.scm.pub.ScmDataSet;
import nc.vo.scm.service.ServcallVO;

public class DefSetTool
{
  private static final String strPre = "[";
  private static final String strEnd = "]";
  private static final String OBJCODE_BATCHCODE = "scm_vbatchcode";
  public static final int _STYLE_IC = 0;
  public static final int _STYLE_NORMAL = -999;
  public static final int _POS_NORMAL = 0;
  public static final int _POS_HEAD = 1;
  public static final int _POS_BODY = 2;
  private static Object[] m_objDefVos = null;

  private static HashMap m_hmDefHead = new HashMap();

  private static HashMap m_hmDefBody = new HashMap();

  private static HashMap m_hmDefName = new HashMap();
  private static final String KEY_NOT_MODIFIED_1 = "UDC";
  private static final String KEY_NOT_MODIFIED_2 = "自定义项";

  public static DefVO[] getDefHead(String pk_corp, String cbilltypecode)
  {
    if (pk_corp == null) {
      return null;
    }
    if (!m_hmDefHead.containsKey(pk_corp + cbilltypecode)) {
      Object[] objs = getDefVOBatch(pk_corp, cbilltypecode);
      if (objs == null) {
        return null;
      }
      if (objs[1] != null) {
        m_hmDefBody.put(pk_corp + cbilltypecode, (DefVO[])(DefVO[])objs[1]);
      }
      if (objs[0] == null) {
        return null;
      }
      m_hmDefHead.put(pk_corp + cbilltypecode, (DefVO[])(DefVO[])objs[0]);
    }

    return (DefVO[])(DefVO[])m_hmDefHead.get(pk_corp + cbilltypecode);
  }

  public static DefVO[] getDefBody(String pk_corp, String cbilltypecode)
  {
    if (pk_corp == null) {
      return null;
    }
    if (!m_hmDefBody.containsKey(pk_corp + cbilltypecode)) {
      Object[] objs = getDefVOBatch(pk_corp, cbilltypecode);
      if (objs == null) {
        return null;
      }
      if (objs[0] != null) {
        m_hmDefHead.put(pk_corp + cbilltypecode, (DefVO[])(DefVO[])objs[0]);
      }
      if (objs[1] == null) {
        return null;
      }
      m_hmDefBody.put(pk_corp + cbilltypecode, (DefVO[])(DefVO[])objs[1]);
    }

    return (DefVO[])(DefVO[])m_hmDefBody.get(pk_corp + cbilltypecode);
  }

  public static DefVO[] getBatchcodeDef(String pk_corp)
  {
    if (pk_corp == null) {
      return null;
    }
    if (!m_hmDefBody.containsKey(pk_corp + "scm_vbatchcode")) {
      Object[] objs = getDefVOBatch(pk_corp, new String[] { getObjName(pk_corp, "scm_vbatchcode") });
      if (objs == null) {
        return null;
      }

      if (objs[0] == null) {
        return null;
      }
      m_hmDefBody.put(pk_corp + "scm_vbatchcode", (DefVO[])(DefVO[])objs[0]);
    }

    return (DefVO[])(DefVO[])m_hmDefBody.get(pk_corp + "scm_vbatchcode");
  }

  public static void afterEditBody(BillModel billModel, int iRow, String sVdefValueKey, String sVdefPkKey)
  {
    if ((billModel == null) || (sVdefPkKey == null) || (sVdefValueKey == null) || (billModel.getItemByKey(sVdefPkKey) == null) || (billModel.getItemByKey(sVdefValueKey) == null))
    {
      return;
    }

    BillItem item = billModel.getItemByKey(sVdefValueKey);

    if (item.getDataType() == 7) {
      UIRefPane refpane = (UIRefPane)item.getComponent();
      String sPk_defdoc = DataTypeTool.getString_Trim0LenAsNull(refpane.getRefPK());

      if ((sPk_defdoc == null) && (refpane.getUITextField().getText() != null) && (refpane.getUITextField().getText().trim().length() > 0))
        return;
      billModel.setValueAt(sPk_defdoc, iRow, sVdefPkKey);
    } else {
      billModel.setValueAt(null, iRow, sVdefPkKey);
    }
  }

  public static void afterEditHead(BillModel billModel, int iRow, String sVdefValueKey, String sVdefPkKey)
  {
    afterEditBody(billModel, iRow, sVdefValueKey, sVdefPkKey);
  }

  public static void afterEditHead(BillData bdata, String sVdefValueKey, String sVdefPkKey)
  {
    if ((bdata == null) || (sVdefPkKey == null) || (sVdefValueKey == null) || (bdata.getHeadItem(sVdefPkKey) == null) || (bdata.getHeadItem(sVdefValueKey) == null))
    {
      return;
    }

    BillItem item = bdata.getHeadItem(sVdefValueKey);

    if (item.getDataType() == 7) {
      UIRefPane refpane = (UIRefPane)item.getComponent();
      String sPk_defdoc = DataTypeTool.getString_Trim0LenAsNull(refpane.getRefPK());

      if ((sPk_defdoc == null) && (refpane.getUITextField().getText() != null) && (refpane.getUITextField().getText().trim().length() > 0))
        return;
      bdata.setHeadItem(sVdefPkKey, sPk_defdoc);
    } else {
      bdata.setHeadItem(sVdefPkKey, null);
    }
  }

  private static Object[] getDefVOBatch(String pk_corp, String cbilltypecode)
  {
    if ((m_objDefVos != null) && (m_objDefVos.length == 2)) {
      return m_objDefVos;
    }

    Object[] objs = null;
    try
    {
      objs = LocalCallService.callService(getTwoSCDs(pk_corp, cbilltypecode));
    } catch (Exception e) {
      System.out.println("批量执行自定义项VO获取方法异常!详细信息如下：");
      e.printStackTrace();
    }
    return objs;
  }

  private static Object[] getDefVOBatch(String pk_corp, String[] saDefType)
  {
    if ((pk_corp == null) || (saDefType == null)) {
      return null;
    }
    int iLen = saDefType.length;
    Object[] objsRet = new Object[iLen];
    ServcallVO scd = null;
    ArrayList listScd = new ArrayList();
    for (int i = 0; i < iLen; i++)
      if ((saDefType[i] == null) || (saDefType[i].trim().length() == 0)) {
        objsRet[i] = null;
      }
      else {
        scd = new ServcallVO();

        scd.setBeanName(IDef.class.getName());
        scd.setMethodName("queryDefVO");
        scd.setParameter(new Object[] { saDefType[i], pk_corp });
        scd.setParameterTypes(new Class[] { String.class, String.class });
        listScd.add(scd);
      }
    if (listScd.size() == 0) {
      return objsRet;
    }
    Object[] objs = null;
    try {
      objs = LocalCallService.callService((ServcallVO[])(ServcallVO[])listScd.toArray(new ServcallVO[listScd.size()]));
    }
    catch (Exception e) {
      System.out.println("批量执行自定义项VO获取方法异常!详细信息如下：");
      e.printStackTrace();
    }

    int iPos = 0;
    for (int i = 0; i < iLen; i++) {
      if ((saDefType[i] != null) && (saDefType[i].trim().length() > 0)) {
        objsRet[i] = objs[iPos];
        iPos++;
      }
    }
    return objsRet;
  }

  private static int getIndex(String strSrc, String strBgn, String strEnd, int iAddNum)
  {
    int iIndex = -1;
    try {
      int iBgnLen = strBgn.length();
      String strTmp = strSrc.substring(iBgnLen);
      if ((strEnd != null) && (strEnd.length() != 0)) {
        strTmp = StringUtil.replaceAllString(strTmp, strEnd, "");
      }

      iIndex = new Integer(strTmp.trim()).intValue();
      iIndex--;
      //edit by zwx 2016-03
//      iIndex += iAddNum;
    } catch (Exception e) {
      System.out.println("从给定串中解析索引：源串：" + strSrc + "前缀：" + strBgn + "、后缀：" + strEnd + "。");

      e.printStackTrace();
    }

    return iIndex;
  }

  public static ServcallVO[] getTwoSCDs(String pk_corp, String cbilltypecode)
  {
    String objheadname = getObjName(pk_corp, ScmConst.getHeadObjCode(cbilltypecode));
    String objbodyname = getObjName(pk_corp, ScmConst.getBodyObjCode(cbilltypecode));

    if ((objheadname == null) && (objbodyname == null)) {
      return null;
    }
    ServcallVO[] scds = new ServcallVO[2];

    scds[0] = new ServcallVO();
    scds[0].setBeanName(IDef.class.getName());
    scds[0].setMethodName("queryDefVO");
    scds[0].setParameter(new Object[] { objheadname, pk_corp });
    scds[0].setParameterTypes(new Class[] { String.class, String.class });

    scds[1] = new ServcallVO();
    scds[1].setBeanName(IDef.class.getName());
    scds[1].setMethodName("queryDefVO");
    scds[1].setParameter(new Object[] { objbodyname, pk_corp });
    scds[1].setParameterTypes(new Class[] { String.class, String.class });

    return scds;
  }

  private static String getObjName(String pk_corp, String objcode)
  {
    if (!m_hmDefName.containsKey(pk_corp + objcode)) {
      ScmDataSet data = new ScmDataSet();
      Object ohead = DBCacheFacade.runQuery(" select objname from bd_defused where  ( objcode= '" + objcode + "')", data);
      data = (ScmDataSet)ohead;
      String objname = null;
      if ((data != null) && (data.getRowCount() > 0)) {
        objname = (String)data.getValueAt(0, 0);
      }

      m_hmDefName.put(pk_corp + objcode, objname);
    }

    return (String)m_hmDefName.get(pk_corp + objcode);
  }

  private static boolean isAddObjName(Object obj)
  {
    if (obj == null)
      return false;
    return obj instanceof ReportBaseClass;
  }

  private static boolean isBody(String fieldCode, String bPrefix, String bEndfix, int iStyle)
  {
    if (bPrefix == null)
      return false;
    return fieldCode.startsWith(bPrefix);
  }

  private static boolean isHead(String fieldCode, String hPrefix, String hEndfix, int iStyle)
  {
    boolean bIsFromIc = iStyle == 0;

    if (bIsFromIc) {
      if (fieldCode.startsWith(hPrefix));
      return fieldCode.endsWith(hEndfix == null ? "" : hEndfix);
    }

    return fieldCode.startsWith(hPrefix);
  }

  public static void setTwoOBJs(Object[] objDefVos)
  {
    m_objDefVos = objDefVos;
  }

  public static void updateBillCardPanelUserDef(BillCardPanel panel, String pk_corp, String cbilltypecode, String hPrefix, String bPrefix)
  {
    if ((panel == null) || (panel.getBillData() == null))
      return;
    updateBillCardPanelUserDef(panel, pk_corp, cbilltypecode, hPrefix, null, 0, bPrefix, null, 0);
  }

  public static void updateBillCardPanelUserDef(BillCardPanel panel, String pk_corp, String cbilltypecode, String hPrefix, String hEndfix, int hAddNum, String bPrefix, String bEndfix, int bAddNum)
  {
    try
    {
      if (panel == null) {
        System.out.println("--> BillCardPanel is null.");
        return;
      }
      BillData billData = panel.getBillData();
      boolean bIsAddObjName = isAddObjName(panel);
      if (bIsAddObjName) {
        System.out.println("待设自定义项控件是报表模板，要拼接自定义项对象名称");
      }
      if (billData == null) {
        System.out.println("--> BillData is null.");
        return;
      }
      DefVO[] defs = null;

      defs = getDefHead(pk_corp, cbilltypecode);
      String strHKey = hPrefix + (hEndfix == null ? "" : hEndfix);
      String strBKey = bPrefix + (bEndfix == null ? "" : bEndfix);
      if (defs != null) {
        updateItemByDef(billData, defs, hPrefix, hEndfix, hAddNum, true, bIsAddObjName);

        if (!strHKey.equals(strBKey)) {
          updateItemByDef(billData, defs, hPrefix, hEndfix, hAddNum, false, bIsAddObjName);
        }

      }

      defs = getDefBody(pk_corp, cbilltypecode);
      if (defs != null) {
        updateItemByDef(billData, defs, bPrefix, bEndfix, bAddNum, false, bIsAddObjName);
      }

      panel.setBillData(billData);
    }
    catch (Exception e) {
      System.out.println("加载单据模板(卡片)自定义项时出现异常：");
      e.printStackTrace();
    }
  }

  public static void updateBillListPanelUserDef(BillListPanel panel, String pk_corp, String cbilltypecode, String hPrefix, String bPrefix)
  {
    if (panel == null)
      return;
    updateBillListPanelUserDef(panel, pk_corp, cbilltypecode, hPrefix, null, 0, bPrefix, null, 0);
  }

  public static void updateBillListPanelUserDef(BillListPanel panel, String pk_corp, String cbilltypecode, String hPrefix, String hEndfix, int hAddNum, String bPrefix, String bEndfix, int bAddNum)
  {
    try
    {
      DefVO[] defs = null;
      BillListData bd = panel.getBillListData();
      if (bd == null) {
        System.out.println("--> billdata null.");
        return;
      }

      defs = getDefHead(pk_corp, cbilltypecode);

      String strHKey = hPrefix + (hEndfix == null ? "" : hEndfix);
      String strBKey = bPrefix + (bEndfix == null ? "" : bEndfix);
      if (defs != null) {
        updateItemByDef(bd, defs, hPrefix, hEndfix, hAddNum, true);
        if (!strHKey.equals(strBKey)) {
          updateItemByDef(bd, defs, hPrefix, hEndfix, hAddNum, false);
        }

      }

      defs = getDefBody(pk_corp, cbilltypecode);
      if (defs != null) {
        updateItemByDef(bd, defs, bPrefix, bEndfix, bAddNum, false);
      }
      panel.setListData(bd);
    }
    catch (Exception e) {
      System.out.println("加载单据模板(列表)自定义项时出现异常：");
      e.printStackTrace();
    }
  }

  public static void updateBillListPanelUserDef(BillListPanel panel, String pk_corp, String cbilltypecode, String hPrefix, String hEndfix, int hAddNum, String bPrefix, String bEndfix, int bAddNum, int iPos)
  {
    if (iPos == 0) {
      updateBillListPanelUserDef(panel, pk_corp, cbilltypecode, hPrefix, hEndfix, hAddNum, bPrefix, bEndfix, bAddNum);

      return;
    }
    boolean bHead = false;
    if (iPos == 1) {
      bHead = true;
    }
    try
    {
      DefVO[] defs = null;
      BillListData bd = panel.getBillListData();
      if (bd == null) {
        System.out.println("--> billdata null.");
        return;
      }

      defs = getDefHead(pk_corp, cbilltypecode);
      String strHKey = hPrefix + (hEndfix == null ? "" : hEndfix);
      String strBKey = bPrefix + (bEndfix == null ? "" : bEndfix);
      if (defs != null) {
        updateItemByDef(bd, defs, hPrefix, hEndfix, hAddNum, bHead);
      }

      defs = getDefBody(pk_corp, cbilltypecode);
      if (defs != null) {
        updateItemByDef(bd, defs, bPrefix, bEndfix, bAddNum, bHead);
      }
      panel.setListData(bd);
    }
    catch (Exception e) {
      System.out.println("加载单据模板(列表)自定义项时出现异常：");
      e.printStackTrace();
    }
  }

  private static void updateItemByDef(BillData bd, DefVO[] defVOs, String fieldPrefix, String fieldEndfix, int iAddNum, boolean isHead, boolean isAddNumObjName)
  {
    if (defVOs == null)
      return;
    Integer iOldIndex = null;
    for (int i = 0; i < defVOs.length; i++) {
      DefVO defVO = defVOs[i];
      if (defVOs[i] == null) {
        continue;
      }
      String itemkey = defVO.getFieldName();
      if (fieldEndfix != null) {
        itemkey = itemkey + fieldEndfix;
      }

      BillItem item = null;

      if (isHead) {
        item = bd.getHeadItem(itemkey);
        if (item == null)
          item = bd.getTailItem(itemkey);
        if (item == null)
          item = bd.getBodyItem(itemkey);
      } else {
        item = bd.getBodyItem(itemkey);
      }
      if (item == null) {
        continue;
      }
      int iDataType = item.getDataType();

      if (defVO == null)
        continue;
      String itemKey = item.getKey();

      String fieldName = item.getName();
      if ((fieldName == null) || (fieldName.trim().equals("")) || (fieldName.indexOf("UDC") >= 0) || (fieldName.indexOf("自定义项") >= 0))
      {
        if (itemKey.indexOf(fieldPrefix) >= 0) {
          if (isAddNumObjName) {
            String resID = "D" + defVO.getDefcode();
            String multiLangStr = NCLangRes.getInstance().getStrByID("bd_defused", resID);

            item.setName("[" + transRes(defVO.getObjName()) + "]" + defVO.getDefname());
          }
          else
          {
            item.setName(defVO.getDefname());
          }
        }
      }

      int inputlength = defVO.getLengthnum().intValue();
      item.setLength(inputlength);

      String iTypeDef = defVO.getType();
      int datatype = 0;
      if (iTypeDef.equals("备注")) {
        datatype = 0;
      } else if (iTypeDef.equals("日期")) {
        datatype = 3;
      } else if (iTypeDef.equals("数字")) {
        datatype = 1;
        if ((defVO.getDigitnum() != null) && (defVO.getDigitnum().intValue() > 0))
        {
          datatype = 2;
          item.setDecimalDigits(defVO.getDigitnum().intValue());
        }
      }

      String strDefDocReftype = defVO.getDefdef().getPk_bdinfo();
      if ((iTypeDef.equals("统计")) && (strDefDocReftype != null)) {
        datatype = 7;
        item.setRefType(strDefDocReftype);
      }
      item.setDataType(datatype);
      item.reCreateComponent();

      if ((item.getDataType() == 5) || (item.getDataType() == 7)) {
        UIRefPane ref = (UIRefPane)item.getComponent();
        ref.setAutoCheck(false);
      }
    }
  }

  private static void updateItemByDef(BillListData bd, DefVO[] defVOs, String fieldPrefix, String fieldEndfix, int iAddNum, boolean isHead)
  {
    if (defVOs == null) {
      return;
    }
    Integer iOldIndex = null;
    for (int i = 0; i < defVOs.length; i++) {
      DefVO defVO = defVOs[i];
      if (defVOs[i] == null)
      {
        continue;
      }
      String itemkey = defVO.getFieldName();
      if (fieldEndfix != null) {
        itemkey = itemkey + fieldEndfix;
      }

      BillItem item = null;

      if (isHead)
        item = bd.getHeadItem(itemkey);
      else {
        item = bd.getBodyItem(itemkey);
      }
      if ((item == null) || 
        (defVO == null))
        continue;
      String itemKey = item.getKey();

      String fieldName = item.getName();
      if ((fieldName == null) || (fieldName.trim().equals("")) || (fieldName.indexOf("UDC") >= 0) || (fieldName.indexOf("自定义项") >= 0))
      {
        if (itemKey.indexOf(fieldPrefix) >= 0) {
          item.setName(defVO.getDefname());
        }
      }

      int inputlength = defVO.getLengthnum().intValue();
      item.setLength(inputlength);

      String type = defVO.getType();
      int datatype = 0;
      if (type.equals("备注")) {
        datatype = 0;
      } else if (type.equals("日期")) {
        datatype = 3;
      } else if (type.equals("数字")) {
        datatype = 1;
        if ((defVO.getDigitnum() != null) && (defVO.getDigitnum().intValue() > 0))
        {
          datatype = 2;
          item.setDecimalDigits(defVO.getDigitnum().intValue());
        }
      }

      if (type.equals("统计"))
        datatype = 7;
      item.setDataType(datatype);

      String reftype = defVO.getDefdef().getPk_bdinfo();

      if (type.equals("统计")) {
        item.setRefType(reftype);
      }

      item.reCreateComponent();
    }
  }

  private static void updateQueryCondition(QueryConditionClient client, String pk_corp, String docName, String prefix)
  {
    int iMaxLenOfDef = 20;

    if ((client == null) || (prefix == null) || (prefix.length() <= 0))
    {
      return;
    }

    DefVO[] defs = BDDef.queryDefVO(docName, pk_corp);

    updateQueryConditionByDefVOs(client, pk_corp, prefix, defs);
  }

  private static void updateQueryConditionByDefVOs(QueryConditionClient client, String pk_corp, String prefix, DefVO[] defs)
  {
    if (defs == null) {
      System.out.println("传入参数：自定义项VO为空，直接返回!");
      return;
    }

    int iMaxLenOfDef = 20;

    if ((client == null) || (prefix == null) || (prefix.length() <= 0))
    {
      return;
    }

    Vector v = new Vector();
    QueryConditionVO[] cs = client.getConditionDatas();
    for (int i = 0; (cs != null) && (i < cs.length); i++) {
      String fieldCode = cs[i].getFieldCode();
      String fieldName = cs[i].getFieldName();
      DefVO vo = null;
      if (fieldCode == null)
      {
        continue;
      }
      if ((fieldCode.startsWith(prefix)) && (defs != null)) {
        int iIndex = -1;
        try {
          int len = prefix.length();
          iIndex = new Integer(fieldCode.substring(len)).intValue();
          iIndex--;
        }
        catch (Exception e) {
          System.out.println("键值解释索引异常");
        }
        if ((iIndex >= 0) && (iIndex < defs.length) && (defs[iIndex] != null))
        {
          if ((fieldName == null) || (fieldName.trim().equals("")) || (fieldName.indexOf("UDC") >= 0) || (fieldName.indexOf("自定义项") >= 0))
          {
            client.setFieldName(fieldCode, "[" + transRes(defs[iIndex].getObjName()) + "]" + defs[iIndex].getDefname());
          }

          vo = defs[iIndex];
        }
        if (vo != null) {
          cs[i].setDispType(new Integer(1));
          cs[i].setReturnType(new Integer(1));
          v.add(cs[i]);
        }
      } else {
        v.add(cs[i]);
      }

      if (vo != null) {
        String type = vo.getType();
        UIRefPane refPane = new UIRefPane();
        if (type.equals("统计"))
        {
          if ((vo.getDefdef() != null) && (vo.getDefdef().getPk_defdoclist() == null))
          {
            refPane = RefCall.getUIRefPane(vo.getDefdef().getPk_bdinfo(), pk_corp);
          }
          else
          {
            String sWhereString = " and ";
            sWhereString = sWhereString + "pk_defdef = (";
            sWhereString = sWhereString + "select a.pk_defdef ";
            sWhereString = sWhereString + "from bd_defquote a,bd_defused b ";

            sWhereString = sWhereString + "where a.pk_defused = b.pk_defused ";

            sWhereString = sWhereString + "and b.objname = '" + vo.getObjName() + "' ";

            sWhereString = sWhereString + "and a.fieldname = '" + vo.getFieldName() + "'";

            sWhereString = sWhereString + ") ";
            refPane.setRefNodeName("自定义项档案");
            refPane.getRefModel().addWherePart(sWhereString);
            refPane.setReturnCode(false);
          }

          if ((refPane != null) && (refPane.getRefModel() != null))
            refPane.getRefModel().setSealedDataShow(true);
        }
        else if (type.equals("日期")) {
          refPane.setRefNodeName("日历");
        } else if (type.equals("备注")) {
          refPane.setMaxLength(20);
          refPane.setButtonVisible(false);
        } else if (type.equals("数字")) {
          refPane.setTextType("TextDbl");
          refPane.setButtonVisible(false);
          refPane.setMaxLength(20);
          if (vo.getDigitnum() == null)
            refPane.setNumPoint(0);
          else {
            refPane.setNumPoint(vo.getDigitnum().intValue());
          }
        }
        client.setValueRef(fieldCode, refPane);
      }
    }
    QueryConditionVO[] vos = null;
    if (v.size() > 0) {
      vos = new QueryConditionVO[v.size()];
      v.copyInto(vos);
    }
    client.setConditionDatas(vos);
  }

  public static void updateQueryConditionClientUserDef(QueryConditionClient client, String pk_corp, String cbilltypecode, String hPrefix, String bPrefix)
  {
    updateQueryConditionClientUserDef(client, pk_corp, cbilltypecode, hPrefix, null, 0, bPrefix, null, 0);
  }

  public static void updateQueryConditionClientUserDef(QueryConditionClient client, String pk_corp, String cbilltypecode, String hPrefix, String hEndfix, int hAddNum, String bPrefix, String bEndfix, int bAddNum)
  {
    updateQueryConditionClientUserDef(client, pk_corp, cbilltypecode, hPrefix, hEndfix, hAddNum, bPrefix, bEndfix, bAddNum, -999);
  }

  public static void updateQueryConditionClientUserDef(QueryConditionClient client, String pk_corp, String cbilltypecode, String hPrefix, String hEndfix, int hAddNum, String bPrefix, String bEndfix, int bAddNum, int iStyle)
  {
    try
    {
      int iMaxLenOfDef = 20;

      if ((client == null) || (hPrefix == null) || (hPrefix.length() <= 0)) {
        return;
      }
      Object[] objs = null;
      DefVO[] hDefs = null;
      DefVO[] bDefs = null;
      if (bPrefix != null)
      {
        hDefs = getDefHead(pk_corp, cbilltypecode);
        bDefs = getDefBody(pk_corp, cbilltypecode);
      } else {
        hDefs = BDDef.queryDefVO("供应链/ARAP单据头", pk_corp);
      }

      if ((bEndfix != null) && (bEndfix.indexOf("_b_") != -1)) {
        updateQueryConditionDelivClientUserDef(client, pk_corp, hPrefix, hEndfix, hAddNum, bPrefix, bEndfix, bAddNum, iStyle, hDefs, bDefs);
      }
      else
      {
        updateQueryConditionClientUserDef(client, pk_corp, hPrefix, hEndfix, hAddNum, bPrefix, bEndfix, bAddNum, iStyle, hDefs, bDefs);
      }
    }
    catch (Exception e)
    {
      System.out.println("加载查询模板自定义项时出现异常：");
      e.printStackTrace();
    }
  }

  private static void updateQueryConditionClientUserDef(QueryConditionClient client, String pk_corp, String hPrefix, String hEndfix, int hAddNum, String bPrefix, String bEndfix, int bAddNum, int iStyle, DefVO[] hDefs, DefVO[] bDefs)
  {
    try
    {
      int iMaxLenOfDef = 20;

      if ((client == null) || (hPrefix == null) || (hPrefix.length() <= 0))
        return;
      QueryConditionVO[] cs = client.getConditionDatas();
      Vector v = new Vector();
      for (int i = 0; (cs != null) && (i < cs.length); i++) {
        String fieldCode = cs[i].getFieldCode();
        String fieldName = cs[i].getFieldName();
        DefVO vo = null;
        if (fieldCode == null)
        {
          continue;
        }

        boolean bIsHead = isHead(fieldCode, hPrefix, hEndfix, iStyle);
        boolean bIsBody = isBody(fieldCode, bPrefix, bEndfix, iStyle);

        if ((bIsHead) && (hDefs != null)) {
          int iIndex = -1;
          iIndex = getIndex(fieldCode, hPrefix, hEndfix, hAddNum);
          if ((iIndex >= 0) && (iIndex < hDefs.length) && (hDefs[iIndex] != null))
          {
            if ((fieldName == null) || (fieldName.trim().equals("")) || (fieldName.indexOf("UDC") >= 0) || (fieldName.indexOf("自定义项") >= 0))
            {
              client.setFieldName(fieldCode, "[" + transRes(hDefs[iIndex].getObjName()) + "]" + hDefs[iIndex].getDefname());
            }

            vo = hDefs[iIndex];
          }
          if (vo != null) {
            cs[i].setDispType(new Integer(1));
            cs[i].setReturnType(new Integer(1));
            v.add(cs[i]);
          }

        }
        else if ((bIsBody) && (bDefs != null)) {
          int iIndex = -1;
          iIndex = getIndex(fieldCode, bPrefix, bEndfix, bAddNum);
          if ((iIndex >= 0) && (iIndex < bDefs.length) && (bDefs[iIndex] != null))
          {
            if ((fieldName == null) || (fieldName.trim().equals("")) || (fieldName.indexOf("UDC") >= 0) || (fieldName.indexOf("自定义项") >= 0))
            {
              client.setFieldName(fieldCode, "[" + transRes(bDefs[iIndex].getObjName()) + "]" + bDefs[iIndex].getDefname());
            }

            vo = bDefs[iIndex];
          }
          if (vo != null) {
            cs[i].setDispType(new Integer(1));
            cs[i].setReturnType(new Integer(1));
            v.add(cs[i]);
          }
        } else {
          v.add(cs[i]);
        }
        if (vo != null) {
          String type = vo.getType();
          UIRefPane refPane = new UIRefPane();
          if (type.equals("统计"))
          {
            if ((vo.getDefdef() != null) && (vo.getDefdef().getPk_defdoclist() == null))
            {
              refPane = RefCall.getUIRefPane(vo.getDefdef().getPk_bdinfo(), pk_corp);
            }
            else
            {
              String sWhereString = " and ";
              sWhereString = sWhereString + "pk_defdef = (";
              sWhereString = sWhereString + "select a.pk_defdef ";
              sWhereString = sWhereString + "from bd_defquote a,bd_defused b ";

              sWhereString = sWhereString + "where a.pk_defused = b.pk_defused ";

              sWhereString = sWhereString + "and b.objname = '" + vo.getObjName() + "' ";

              sWhereString = sWhereString + "and a.fieldname = '" + vo.getFieldName() + "'";

              sWhereString = sWhereString + ") ";
              refPane.setRefNodeName("自定义项档案");
              refPane.getRefModel().addWherePart(sWhereString);
            }

            if ((refPane != null) && (refPane.getRefModel() != null))
              refPane.getRefModel().setSealedDataShow(true);
          }
          else if (type.equals("日期")) {
            refPane.setRefNodeName("日历");
          } else if (type.equals("备注")) {
            refPane.setMaxLength(20);
            refPane.setButtonVisible(false);
          } else if (type.equals("数字")) {
            refPane.setTextType("TextDbl");
            refPane.setButtonVisible(false);
            refPane.setMaxLength(20);
            int iDigt = 0;
            if (vo.getDigitnum() != null)
              iDigt = vo.getDigitnum().intValue();
            refPane.setNumPoint(iDigt);
          }

          client.setValueRef(fieldCode, refPane);
        }
      }
      QueryConditionVO[] vos = null;
      if (v.size() > 0) {
        vos = new QueryConditionVO[v.size()];
        v.copyInto(vos);
      }
      if (vos == null) {
        QueryConditionVO voError = new QueryConditionVO();
        voError.setDataType(new Integer(0));
        voError.setDirty(true);
        voError.setConsultCode("公司目录");
        voError.setDispSequence(new Integer(0));
        voError.setDispType(new Integer(1));
        voError.setDispValue("Error Value");
        voError.setDr(new Integer(1));
        voError.setFieldCode("nothiscode");
        voError.setFieldName("ErrorName");
        voError.setId("dddddd");
        voError.setId("Error00000012");
        voError.setIfAutocheck(UFBoolean.FALSE);
        voError.setIfDataPower(UFBoolean.FALSE);
        voError.setIfDefault(UFBoolean.FALSE);
        voError.setIfDesc(UFBoolean.FALSE);
        voError.setIfGroup(UFBoolean.FALSE);
        voError.setIfImmobility(UFBoolean.FALSE);
        voError.setIfMust(UFBoolean.FALSE);
        voError.setIfOrder(UFBoolean.FALSE);
        voError.setIfSum(UFBoolean.FALSE);
        voError.setIfUsed(UFBoolean.FALSE);
        voError.setIsUserDef(UFBoolean.TRUE);
        voError.setNodecode("2014XXXX");
        voError.setOperaCode("=");
        voError.setOperaName("Equals");
        voError.setOrderSequence(new Integer(0));
        voError.setPkCorp("1001");
        voError.setPkTemplet("2014XXXXYYYYXXXXYYYY");
        voError.setPrimaryKey("2014XXXXYYYYXXXX0001");
        voError.setResid("ddd");
        voError.setReturnType(new Integer(0));
        voError.setStatus(0);
        voError.setTableCode("ddcode");
        voError.setTableName("ddname");
        voError.setTs(new UFDateTime(System.currentTimeMillis()));
        voError.setValue("error Value");
        vos = new QueryConditionVO[] { voError };
      }
      client.setConditionDatas(vos);
    } catch (Exception e) {
      System.out.println("加载查询模板自定义项时出现异常：");
      e.printStackTrace();
    }
  }

  private static void updateQueryConditionDelivClientUserDef(QueryConditionClient client, String pk_corp, String hPrefix, String hEndfix, int hAddNum, String bPrefix, String bEndfix, int bAddNum, int iStyle, DefVO[] hDefs, DefVO[] bDefs)
  {
    try
    {
      int iMaxLenOfDef = 20;

      if ((client == null) || (hPrefix == null) || (hPrefix.length() <= 0))
        return;
      QueryConditionVO[] cs = client.getConditionDatas();
      Vector v = new Vector();
      for (int i = 0; (cs != null) && (i < cs.length); i++) {
        String fieldCode = cs[i].getFieldCode();
        String fieldName = cs[i].getFieldName();
        DefVO vo = null;
        if (fieldCode == null)
        {
          continue;
        }

        boolean bIsHead = isDelivHead(fieldName);
        boolean bIsBody = isBody(fieldCode, bPrefix, bEndfix, iStyle);

        if ((bIsHead) && (hDefs != null)) {
          int iIndex = -1;
          iIndex = getIndex(fieldCode, hPrefix, hEndfix, hAddNum);
          if ((iIndex >= 0) && (iIndex < hDefs.length) && (hDefs[iIndex] != null))
          {
            if ((fieldName == null) || (fieldName.trim().equals("")) || (fieldName.indexOf("UDC") >= 0) || (fieldName.indexOf("自定义项") >= 0))
            {
              client.setFieldName(fieldCode, "[" + transRes(hDefs[iIndex].getObjName()) + "]" + hDefs[iIndex].getDefname());
            }

            vo = hDefs[iIndex];
          }
          if (vo != null) {
            cs[i].setDispType(new Integer(1));
            cs[i].setReturnType(new Integer(1));
            v.add(cs[i]);
          }

        }
        else if ((bIsBody) && (bDefs != null)) {
          int iIndex = -1;
          iIndex = getIndex(fieldCode, bPrefix, bEndfix, bAddNum);
          if ((iIndex >= 0) && (iIndex < bDefs.length) && (bDefs[iIndex] != null))
          {
            if ((fieldName == null) || (fieldName.trim().equals("")) || (fieldName.indexOf("UDC") >= 0) || (fieldName.indexOf("自定义项") >= 0))
            {
              client.setFieldName(fieldCode, "[" + transRes(bDefs[iIndex].getObjName()) + "]" + bDefs[iIndex].getDefname());
            }

            vo = bDefs[iIndex];
          }
          if (vo != null) {
            cs[i].setDispType(new Integer(1));
            cs[i].setReturnType(new Integer(1));
            v.add(cs[i]);
          }
        } else {
          v.add(cs[i]);
        }
        if (vo != null) {
          String type = vo.getType();
          UIRefPane refPane = new UIRefPane();
          if (type.equals("统计"))
          {
            if ((vo.getDefdef() != null) && (vo.getDefdef().getPk_defdoclist() == null))
            {
              refPane = RefCall.getUIRefPane(vo.getDefdef().getPk_bdinfo(), pk_corp);
            }
            else
            {
              String sWhereString = " and ";
              sWhereString = sWhereString + "pk_defdef = (";
              sWhereString = sWhereString + "select a.pk_defdef ";
              sWhereString = sWhereString + "from bd_defquote a,bd_defused b ";

              sWhereString = sWhereString + "where a.pk_defused = b.pk_defused ";

              sWhereString = sWhereString + "and b.objname = '" + vo.getObjName() + "' ";

              sWhereString = sWhereString + "and a.fieldname = '" + vo.getFieldName() + "'";

              sWhereString = sWhereString + ") ";
              refPane.setRefNodeName("自定义项档案");
              refPane.getRefModel().addWherePart(sWhereString);
            }

            if ((refPane != null) && (refPane.getRefModel() != null))
              refPane.getRefModel().setSealedDataShow(true);
          }
          else if (type.equals("日期")) {
            refPane.setRefNodeName("日历");
          } else if (type.equals("备注")) {
            refPane.setMaxLength(20);
            refPane.setButtonVisible(false);
          } else if (type.equals("数字")) {
            refPane.setTextType("TextDbl");
            refPane.setButtonVisible(false);
            refPane.setMaxLength(20);
            int iDigt = 0;
            if (vo.getDigitnum() != null)
              iDigt = vo.getDigitnum().intValue();
            refPane.setNumPoint(iDigt);
          }

          client.setValueRef(fieldCode, refPane);
        }
      }
      QueryConditionVO[] vos = null;
      if (v.size() > 0) {
        vos = new QueryConditionVO[v.size()];
        v.copyInto(vos);
      }
      if (vos == null) {
        QueryConditionVO voError = new QueryConditionVO();
        voError.setDataType(new Integer(0));
        voError.setDirty(true);
        voError.setConsultCode("公司目录");
        voError.setDispSequence(new Integer(0));
        voError.setDispType(new Integer(1));
        voError.setDispValue("Error Value");
        voError.setDr(new Integer(1));
        voError.setFieldCode("nothiscode");
        voError.setFieldName("ErrorName");
        voError.setId("dddddd");
        voError.setId("Error00000012");
        voError.setIfAutocheck(UFBoolean.FALSE);
        voError.setIfDataPower(UFBoolean.FALSE);
        voError.setIfDefault(UFBoolean.FALSE);
        voError.setIfDesc(UFBoolean.FALSE);
        voError.setIfGroup(UFBoolean.FALSE);
        voError.setIfImmobility(UFBoolean.FALSE);
        voError.setIfMust(UFBoolean.FALSE);
        voError.setIfOrder(UFBoolean.FALSE);
        voError.setIfSum(UFBoolean.FALSE);
        voError.setIfUsed(UFBoolean.FALSE);
        voError.setIsUserDef(UFBoolean.TRUE);
        voError.setNodecode("2014XXXX");
        voError.setOperaCode("=");
        voError.setOperaName("Equals");
        voError.setOrderSequence(new Integer(0));
        voError.setPkCorp("1001");
        voError.setPkTemplet("2014XXXXYYYYXXXXYYYY");
        voError.setPrimaryKey("2014XXXXYYYYXXXX0001");
        voError.setResid("ddd");
        voError.setReturnType(new Integer(0));
        voError.setStatus(0);
        voError.setTableCode("ddcode");
        voError.setTableName("ddname");
        voError.setTs(new UFDateTime(System.currentTimeMillis()));
        voError.setValue("error Value");
        vos = new QueryConditionVO[] { voError };
      }
      client.setConditionDatas(vos);
    } catch (Exception e) {
      System.out.println("加载查询模板自定义项时出现异常：");
      e.printStackTrace();
    }
  }

  public static boolean isDelivHead(String fieldName)
  {
    return fieldName.startsWith("H-UDC");
  }

  public static void updateQueryConditionClientUserDef(QueryConditionClient client, String pk_corp, String cbilltypecode, String hPrefix, String hEndfix, int hAddNum, String bPrefix, String bEndfix, int bAddNum, String prefixCumandoc, String prefixCubasdoc, String prefixInvmandoc, String prefixInvbasdoc)
  {
    updateQueryConditionClientUserDefAll(client, pk_corp, cbilltypecode, hPrefix, hEndfix, hAddNum, bPrefix, bEndfix, bAddNum, -999, prefixCumandoc, prefixCubasdoc, prefixInvmandoc, prefixInvbasdoc);
  }

  public static void updateQueryConditionClientUserDefAll(QueryConditionClient client, String pk_corp, String cbilltypecode, String hPrefix, String hEndfix, int hAddNum, String bPrefix, String bEndfix, int bAddNum, int iStyle, String prefixCumandoc, String prefixCubasdoc, String prefixInvmandoc, String prefixInvbasdoc)
  {
    try
    {
      int iMaxLenOfDef = 20;

      if ((client == null) || (hPrefix == null) || (hPrefix.length() <= 0)) {
        return;
      }
      String[] saDocName = { "客商管理档案", "客商档案", "存货管理档案", "存货档案" };

      if ((prefixCumandoc == null) || (prefixCumandoc.trim().length() == 0)) {
        saDocName[0] = null;
      }
      if ((prefixCubasdoc == null) || (prefixCubasdoc.trim().length() == 0)) {
        saDocName[1] = null;
      }
      if ((prefixInvmandoc == null) || (prefixInvmandoc.trim().length() == 0)) {
        saDocName[2] = null;
      }
      if ((prefixInvbasdoc == null) || (prefixInvbasdoc.trim().length() == 0)) {
        saDocName[3] = null;
      }
      Object[] objs = getDefVOBatch(pk_corp, saDocName);
      if ((objs == null) || (objs.length != 4)) {
        System.out.println("批量远程调用出现异常，查询模板自定义项设置异常返回!");
        return;
      }
      DefVO[] hDefs = getDefHead(pk_corp, cbilltypecode);
      DefVO[] bDefs = null;
      if (bPrefix != null) {
        bDefs = getDefBody(pk_corp, cbilltypecode);
      }
      updateQueryConditionClientUserDef(client, pk_corp, hPrefix, hEndfix, hAddNum, bPrefix, bEndfix, bAddNum, iStyle, hDefs, bDefs);

      if ((prefixCumandoc != null) && (prefixCumandoc.trim().length() > 0)) {
        updateQueryConditionByDefVOs(client, pk_corp, prefixCumandoc, (DefVO[])(DefVO[])objs[2]);
      }

      if ((prefixCubasdoc != null) && (prefixCubasdoc.trim().length() > 0)) {
        updateQueryConditionByDefVOs(client, pk_corp, prefixCubasdoc, (DefVO[])(DefVO[])objs[3]);
      }

      if ((prefixInvmandoc != null) && (prefixInvmandoc.trim().length() > 0)) {
        updateQueryConditionByDefVOs(client, pk_corp, prefixInvmandoc, (DefVO[])(DefVO[])objs[4]);
      }

      if ((prefixInvbasdoc != null) && (prefixInvbasdoc.trim().length() > 0))
        updateQueryConditionByDefVOs(client, pk_corp, prefixInvbasdoc, (DefVO[])(DefVO[])objs[5]);
    }
    catch (Exception e)
    {
      System.out.println("加载查询模板自定义项时出现异常：");
      e.printStackTrace();
    }
  }

  public static void updateQueryConditionForCubasdoc(QueryConditionClient client, String pk_corp, String prefix)
  {
    updateQueryCondition(client, pk_corp, "客商档案", prefix);
  }

  public static void updateQueryConditionForCumandoc(QueryConditionClient client, String pk_corp, String prefix)
  {
    updateQueryCondition(client, pk_corp, "客商管理档案", prefix);
  }

  public static void updateQueryConditionForInvbasdoc(QueryConditionClient client, String pk_corp, String prefix)
  {
    updateQueryCondition(client, pk_corp, "存货档案", prefix);
  }

  public static void updateQueryConditionForInvmandoc(QueryConditionClient client, String pk_corp, String prefix)
  {
    updateQueryCondition(client, pk_corp, "存货管理档案", prefix);
  }

  private static String transRes(String strSrc)
  {
    String strDst = strSrc;
    if ((strSrc != null) && ((strSrc.equals("供应链/ARAP单据头")) || (strSrc.equals("供应链/ARAP单据体"))))
    {
      String strResId = "D14";
      if (strSrc.indexOf("体") >= 0) {
        strResId = "D15";
      }
      strDst = NCLangRes.getInstance().getStrByID("bd_defused", strResId);
    }

    return strDst;
  }

  public static String[] updateFieldUserDef(String[] sFieldNames, String pk_corp, String cbilltypecode, DefVO[] Defs, boolean isHead)
  {
    if ((sFieldNames == null) || (sFieldNames.length <= 0)) {
      return sFieldNames;
    }
    if ((Defs == null) && (pk_corp != null))
    {
      if (isHead)
        Defs = getDefHead(pk_corp, cbilltypecode);
      else {
        Defs = getDefBody(pk_corp, cbilltypecode);
      }
    }

    if ((Defs == null) || (Defs.length < sFieldNames.length)) {
      return sFieldNames;
    }
    String[] sNewFieldNames = new String[sFieldNames.length];

    for (int i = 0; i < sFieldNames.length; i++) {
      sNewFieldNames[i] = sFieldNames[i];
      if (Defs[i] != null) {
        sNewFieldNames[i] = Defs[i].getDefname();
      }

    }

    return sNewFieldNames;
  }
}