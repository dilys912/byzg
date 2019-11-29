package nc.ui.scm.pub.query;

import java.awt.Container;
import java.awt.Frame;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import nc.ui.bd.datapower.DataPowerServ;
import nc.ui.bd.manage.RefCellRender;
import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.UFRefManage;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.query.EditComponentFacotry;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.query.QueryTabModel;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.ic.measurerate.InvMeasRate;
import nc.ui.scm.inter.ic.ILocatorRefPane;
import nc.ui.scm.inter.ic.ILotQueryRef;
import nc.ui.scm.inter.ic.IStatbRefPane;
import nc.ui.scm.pub.CommonDataHelper;
import nc.vo.bd.CorpVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.QueryConditionVO;
import nc.vo.pub.query.RefResultVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.service.ServcallVO;
import nc.vo.sm.UserVO;

public class SCMQueryConditionDlg extends QueryConditionClient
{
  private static final long serialVersionUID = 1L;
  private UIPanel ivjUIPanel = null;

  private JTextField ivjtfUnitCode = null;

  ClientEnvironment env = null;

  private Hashtable m_htFreeItemInv = new Hashtable();

  private Hashtable<String, InvVO> m_htInvVO = new Hashtable();

  private Hashtable m_htAutoClear = new Hashtable();

  private Hashtable<String, WhVO> m_htWhVO = new Hashtable();

  private InvMeasRate m_voInvMeas = new InvMeasRate();

  private Hashtable m_htAstCorpInv = new Hashtable();

  private HashMap m_htCorpRef = new HashMap();

  private ArrayList m_alpower = new ArrayList();

  private HashMap m_hmRefCorp = new HashMap();

  private ConditionVO[] m_dataPowerScmVOs = null;

  private HashMap m_htRefField = null;

  private ArrayList m_alRefInitWhereClause = new ArrayList();

  private final int RI_FIELD_CODE = 0;

  private final int RI_REF_NAME = 1;

  private final int RI_WHERE_CLAUSE = 2;

  private final int RI_CHECK_FIELD_CODE = 3;

  private Hashtable m_htStatBtoH = new Hashtable();

  private Hashtable m_htStatHtoCorp = new Hashtable();

  private Hashtable m_htLotInv = new Hashtable();

  private ArrayList m_alComboxToRef = new ArrayList();

  private ArrayList m_alCorpRef = new ArrayList();

  private HashMap m_hmCorpData = new HashMap();

  private String[] m_sdefaultcorps = null;

  private ArrayList m_alLocatorRef = new ArrayList();

  private String m_sDefaultCorpID = "";

  private String m_sCorpFieldCode = "";

  private Hashtable m_htInitMultiRef = new Hashtable();
  private QryPrintStatusPanel m_QryPrintStatusPanel;
  private boolean m_isShowPrintStatusPanel = false;

  private boolean m_isDataPowerSqlReturned = true;

  private ConditionVO[] m_cCtrTmpDataPowerVOs = null;

  private static HashMap<String, UFBoolean> m_mapPowerEnableCache = new HashMap();

  private static HashMap<String, String> m_mapPowerSubSqlCache = new HashMap();

  public SCMQueryConditionDlg()
  {
    setIsWarningWithNoInput(true);
    setSealedDataShow(true);
  }

  public SCMQueryConditionDlg(boolean isFixedSet)
  {
    super(isFixedSet);
    setIsWarningWithNoInput(true);
    setSealedDataShow(true);
  }

  public SCMQueryConditionDlg(Container parent)
  {
    super(parent);
    setIsWarningWithNoInput(true);
    setSealedDataShow(true);
    hideNormal();
  }

  public SCMQueryConditionDlg(Container parent, String title)
  {
    super(parent, title);
    setIsWarningWithNoInput(true);
    setSealedDataShow(true);
  }

  public SCMQueryConditionDlg(Frame parent)
  {
    super(parent);
    setIsWarningWithNoInput(true);
    setSealedDataShow(true);
  }

  public SCMQueryConditionDlg(Frame parent, String title)
  {
    super(parent, title);
    setIsWarningWithNoInput(true);
    setSealedDataShow(true);
  }

  public void afterEdit(TableCellEditor editor, int row, int col)
  {
    int index1 = getIndexes(row - getImmobilityRows());
    int index = row - getImmobilityRows();

    if ((index1 < 0) || (index1 >= getConditionDatas().length)) {
      System.out.println("qry cond err.");
      return;
    }

    QueryConditionVO qcvo = getConditionDatas()[index1];
    if ((col == this.COLVALUE) && ((editor instanceof UIFreeItemCellEditor)))
    {
      Object temp = ((UIFreeItemCellEditor)editor).getComponent();
      if ((temp instanceof FreeItemRefPane)) {
        FreeItemRefPane pane = (FreeItemRefPane)temp;
        FreeVO fvo = pane.getFreeVO();

        RefResultVO[] rrvo = getValueRefResults();
        if (rrvo[index] == null)
          rrvo[index] = new RefResultVO();
        rrvo[index].setRefCode(fvo.getVfree0().trim());
        rrvo[index].setRefName(fvo.getVfree0().trim());
        rrvo[index].setRefPK(fvo.getVfree0().trim());
        rrvo[index].setRefObj(fvo);
        setValueRefResults(rrvo);

        getUITabInput().setValueAt(fvo.getVfree0().trim(), row, col);

        String sFreeItemCode = qcvo.getFieldCode().trim();
        if (this.m_htFreeItemInv.containsKey(sFreeItemCode)) {
          String sInvIDCode = this.m_htFreeItemInv.get(sFreeItemCode).toString().trim();

          String sInvID = "";
          sInvID = getPKbyFieldCode(sInvIDCode);
          if (this.m_htInvVO.containsKey(sInvID)) {
            InvVO ivo = (InvVO)this.m_htInvVO.get(sInvID);
            ivo.setFreeItemVO(fvo);
            this.m_htInvVO.put(sInvID, ivo);
          }
        }
      }
    }
    else if ((col == this.COLVALUE) && ((editor instanceof UIComboBoxCellEditor)))
    {
      Object temp = ((UIComboBoxCellEditor)editor).getComponent();
      if ((temp instanceof UIComboBox)) {
        UIComboBox pane = (UIComboBox)temp;
        DataObject doValue = (DataObject)pane.getItemAt(pane.getSelectedIndex());

        RefResultVO[] rrvo = getValueRefResults();
        if (rrvo[index] == null)
          rrvo[index] = new RefResultVO();
        rrvo[index].setRefCode(doValue.getID().toString().trim());
        rrvo[index].setRefName(doValue.getName().toString().trim());
        rrvo[index].setRefPK(doValue.getID().toString().trim());

        setValueRefResults(rrvo);

        getUITabInput().setValueAt(doValue.getName().toString().trim(), row, col);
      }

    }
    else if ((col == this.COLVALUE) && ((editor instanceof UIRefCellEditor))) {
      super.afterEdit(editor, row, col);
      Object temp = ((UIRefCellEditor)editor).getComponent();
      if ((temp instanceof UIRefPane)) {
        UIRefPane pane = (UIRefPane)temp;
        String sRefNodeName = pane.getRefNodeName().trim();

        if (sRefNodeName.equals("公司目录")) {
          if ((qcvo.getFieldCode() != null) && 
            (this.m_htCorpRef.containsKey(qcvo.getFieldCode()))) {
            String[] sRefs = (String[])(String[])this.m_htCorpRef.get(qcvo.getFieldCode());

            for (int kk = 0; kk < sRefs.length; kk++) {
              Object oRef = getValueRefObjectByFieldCode(sRefs[kk]);
              if ((oRef == null) || (!(oRef instanceof UIRefPane))) {
                continue;
              }
              ((UIRefPane)oRef).getRefModel().setPk_corp(pane.getRefPK());
            }

          }

        }
        else if ((sRefNodeName.equals("存货档案")) || (sRefNodeName.equals("成套件")))
        {
          if (pane.getRefPK() != null) {
            String sTempID1 = pane.getRefPK().trim();
            String sTempID2 = null;
            ArrayList alIDs = new ArrayList();
            alIDs.add(sTempID2);
            alIDs.add(sTempID1);
            alIDs.add(getCurUserID());
            alIDs.add(null);
            try
            {
              ServcallVO scd = new ServcallVO();
              scd.setBeanName("nc.bs.ic.pub.bill.GeneralBillImpl");
              scd.setMethodName("queryInfo");
              scd.setParameter(new Object[] { new Integer(0), alIDs });

              scd.setParameterTypes(new Class[] { Integer.class, ArrayList.class });

              InvVO voInv = (InvVO)(InvVO)nc.ui.scm.service.LocalCallService.callService("ic", new ServcallVO[] { scd })[0];

              this.m_htInvVO.put(sTempID1, voInv);
            } catch (Exception e) {
            }
          }
        } else if (sRefNodeName.equals("会计期间")) {
          if (pane.getRefPK() != null)
          {
            RefResultVO[] rrvo = getValueRefResults();
            if (rrvo[index] == null)
              rrvo[index] = new RefResultVO();
            rrvo[index].setRefCode(pane.getRefCode().trim());
            rrvo[index].setRefName(pane.getRefName().trim());

            rrvo[index].setRefPK(pane.getRefPK().trim());
            rrvo[index].setRefObj(pane.getRefValue("bd_accperiodmonth.begindate"));

            setValueRefResults(rrvo);

            getUITabInput().setValueAt(pane.getRefName().trim(), row, col);
          }

        }
        else if ((sRefNodeName.equals("仓库档案")) && 
          (pane.getRefPK() != null)) {
          String sTempID1 = pane.getRefPK().trim();
          String sTempID2 = null;
          ArrayList alIDs = new ArrayList();
          alIDs.add(sTempID2);
          alIDs.add(sTempID1);
          alIDs.add(getCurUserID());
          alIDs.add(null);
          try
          {
            ServcallVO scd = new ServcallVO();
            scd.setBeanName("nc.bs.ic.ic221.SpecialHBO");
            scd.setMethodName("queryInfo");
            scd.setParameter(new Object[] { new Integer(1), sTempID1 });

            scd.setParameterTypes(new Class[] { Integer.class, String.class });

            WhVO voWh = (WhVO)(WhVO)nc.ui.scm.service.LocalCallService.callService("ic", new ServcallVO[] { scd })[0];

            this.m_htWhVO.put(sTempID1, voWh);
          }
          catch (Exception e) {
          }
        }
      }
      if (qcvo.getDataType().intValue() != 300)
      {
        if (qcvo.getDataType().intValue() == 350)
        {
          IStatbRefPane pane = (IStatbRefPane)temp;

          RefResultVO[] rrvo = getValueRefResults();
          if (rrvo[index] == null)
            rrvo[index] = new RefResultVO();
          rrvo[index].setRefCode(pane.getStatcode().trim());
          rrvo[index].setRefName(pane.getStatcode().trim());
          rrvo[index].setRefPK(pane.getStatbid().trim());

          setValueRefResults(rrvo);

          getUITabInput().setValueAt(pane.getStatcode().toString().trim(), row, col);
        }
        else if (qcvo.getDataType().intValue() == 400)
        {
          UIRefPane pane = (UIRefPane)temp;

          RefResultVO[] rrvo = getValueRefResults();
          if (rrvo[index] == null)
            rrvo[index] = new RefResultVO();
          rrvo[index].setRefCode(pane.getText().trim());
          rrvo[index].setRefName(pane.getText().trim());
          rrvo[index].setRefPK(pane.getText().trim());

          setValueRefResults(rrvo);

          getUITabInput().setValueAt(pane.getText().trim(), row, col);
        }
      }
    } else {
      super.afterEdit(editor, row, col);
    }

    if (col == this.COLVALUE) {
      String sFieldCode = qcvo.getFieldCode().trim();
      autoClear(sFieldCode, row, col);
    }
  }

  protected void autoClear(String sKey, int row, int col)
  {
    if (this.m_htAutoClear.containsKey(sKey)) {
      String[] sOtherFieldCodes = (String[])(String[])this.m_htAutoClear.get(sKey);
      RefResultVO[] rrvo = getValueRefResults();
      QueryConditionVO[] qcvos = getConditionDatas();

      for (int i = 0; i < sOtherFieldCodes.length; i++) {
        String sFieldCodeClear = sOtherFieldCodes[i].trim();
        for (int j = 0; j < getUITabInput().getRowCount(); j++) {
          int index = getIndexes(j - getImmobilityRows());

          if ((index < 0) || (index >= qcvos.length)) {
            System.out.println("qry cond err.");
          }
          else {
            if (!qcvos[index].getFieldCode().trim().equals(sFieldCodeClear)) {
              continue;
            }
            if (rrvo[j] != null) {
              rrvo[j].setRefCode("");
              rrvo[j].setRefName("");
              rrvo[j].setRefPK("");
            }

            getUITabInput().setValueAt(null, j, col);
            break;
          }
        }
      }
    }
  }

  protected void addNullContionDataPower(String refTableName, String sFieldCode, String pk_Corp)
  {
  }

  public void calculateDataPowerVOs()
  {
    if ((this.m_htCorpRef.size() == 0) && ((this.m_cCtrTmpDataPowerVOs == null) || (this.m_cCtrTmpDataPowerVOs.length == 0)))
    {
      return;
    }

    HashMap hmRefCorp = new HashMap();

    for (int i = 0; i < this.m_alpower.size(); i++)
    {
      String corpfieldcode = (String)this.m_hmRefCorp.get(this.m_alpower.get(i));
      if (corpfieldcode != null) {
        if (this.m_hmCorpData.get(corpfieldcode) != null) {
          ArrayList alcorpvalue = (ArrayList)this.m_hmCorpData.get(corpfieldcode);

          String[] scorpvalue = new String[alcorpvalue.size()];
          alcorpvalue.toArray(scorpvalue);

          hmRefCorp.put(this.m_alpower.get(i), scorpvalue);
        }
        else {
          hmRefCorp.put(this.m_alpower.get(i), this.m_sdefaultcorps);
        }
      }

    }

    HashMap hmPoweredConditionVO = new HashMap();

    ArrayList alNullValueCondVO = new ArrayList();

    this.m_dataPowerScmVOs = null;

    String strDef = null;

    for (int i = 0; i < getConditionDatas().length; i++) {
      QueryConditionVO vo = getConditionDatas()[i];
      if (!isDataPwr(vo))
      {
        continue;
      }
      strDef = getFldCodeByPower(vo);

      hmPoweredConditionVO.put(strDef, vo);
      hmPoweredConditionVO.put(getFldCodeByPower(vo), vo);
    }

    ConditionVO[] vos = getConditionVO();
    if (vos != null) {
      String strUsed = null;
      HashMap htUsed = new HashMap();
      ConditionVO usedVO = null;
      int iLen = vos.length;
      for (int i = 0; i < iLen; i++) {
        usedVO = vos[i];
        strUsed = usedVO.getFieldCode();
        QueryConditionVO voQuery = (QueryConditionVO)hmPoweredConditionVO.get(strUsed);

        if ((voQuery != null) && (voQuery.getIfAutoCheck() != null) && (voQuery.getIfAutoCheck().booleanValue())) {
          htUsed.put(strUsed, usedVO);
        }

        if (this.m_htCorpRef.containsKey(strUsed)) {
          String[] sps = (String[])(String[])this.m_htCorpRef.get(strUsed);
          for (int j = 0; j < sps.length; j++) {
            hmRefCorp.put(sps[j], new String[] { usedVO.getRefResult().getRefPK() });
          }
        }

      }

      for (int i = 0; i < this.m_alpower.size(); i++) {
        strUsed = (String)this.m_alpower.get(i);

        if (!htUsed.containsKey(strUsed)) {
          alNullValueCondVO.add(strUsed);
        }
      }

    }

    if (alNullValueCondVO.size() > 0)
    {
      Vector vecVO = new Vector();
      for (int i = 0; i < alNullValueCondVO.size(); i++) {
        String fieldcode = (String)alNullValueCondVO.get(i);

        QueryConditionVO voQuery = (QueryConditionVO)hmPoweredConditionVO.get(fieldcode);

        if (voQuery == null) {
          System.out.println("@@@@没有QueryConditionVO::" + fieldcode);
        }
        else
        {
          String[] corpValues = (String[])(String[])hmRefCorp.get(getFldCodeByPower(voQuery));
          appendPowerCons(vecVO, voQuery, corpValues);
        }

      }

      if (vecVO.size() > 0) {
        this.m_dataPowerScmVOs = new ConditionVO[vecVO.size()];
        vecVO.copyInto(this.m_dataPowerScmVOs);
      }
    }
  }

  public void checkAllHaveOrNot(ConditionVO[] voCons, String[] strKeys)
    throws BusinessException
  {
    if (strKeys == null) {
      return;
    }
    if ((voCons == null) || (voCons.length < 1)) {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000162"));
    }

    Hashtable htKey = new Hashtable();
    StringBuffer sMsg = new StringBuffer();
    for (int i = 0; i < strKeys.length; i++) {
      if (!htKey.containsKey(strKeys[i])) {
        htKey.put(strKeys[i], strKeys[i]);
        sMsg.append(getNamebyFieldCode(strKeys[i]) + ",");
      }

    }

    for (int i = 0; i < voCons.length; i++) {
      String sFieldCode = voCons[i].getFieldCode();
      if (htKey.containsKey(sFieldCode)) {
        htKey.remove(sFieldCode);
      }

    }

    if ((htKey.size() != 0) && (htKey.size() != strKeys.length))
      throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000163") + sMsg);
  }

  public void checkBracket(ConditionVO[] conditions)
    throws BusinessException
  {
    int left = 0;
    int right = 0;
    String sMsg = "";

    for (int i = 0; i < conditions.length; i++) {
      if ((conditions[i].getValue() == null) || (conditions[i].getValue().toString().trim().length() <= 0)) {
        continue;
      }
      if (!conditions[i].getNoLeft())
        left++;
      if (!conditions[i].getNoRight()) {
        right++;
      }
    }
    if (left != right) {
      sMsg = NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000164");

      throw new BusinessException(sMsg);
    }
  }

  public String checkCondition(ConditionVO[] conditions)
  {
    String sReturnError = super.checkCondition(conditions);
    if ((sReturnError != null) && (sReturnError.trim().length() != 0)) {
      return sReturnError;
    }

    if ((conditions != null) && (conditions.length > 1)) {
      HashMap hm = new HashMap();
      ConditionVO voPre = null;
      for (int i = 1; i < conditions.length; i++)
      {
        if (conditions[i].getLogic())
          continue;
        if (!hm.containsKey(conditions[i].getFieldCode())) {
          voPre = conditions[(i - 1)];
          hm.put(voPre.getFieldCode(), voPre);
        } else {
          voPre = (ConditionVO)hm.get(conditions[i].getFieldCode());
        }

        if (!conditions[(i - 1)].getFieldCode().equals(conditions[i].getFieldCode()))
        {
          sReturnError = "只有相同的查询项目可以使用'或者'!";
          break;
        }

        if ((voPre.getNoLeft()) || (!voPre.getNoRight())) {
          sReturnError = "请在第一个'" + voPre.getFieldName() + "'的条件外层加左括号,并且去掉右括号!";

          break;
        }

      }

    }

    return sReturnError;
  }

  public void checkNotField(ConditionVO[] voCons, String[] strKeys)
    throws BusinessException
  {
    if (strKeys == null) {
      return;
    }
    if ((voCons == null) || (voCons.length < 1)) {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000162"));
    }

    Hashtable htKey = new Hashtable();
    StringBuffer sMsg = new StringBuffer();

    for (int i = 0; i < strKeys.length; i++) {
      if (!htKey.containsKey(strKeys[i])) {
        htKey.put(strKeys[i], strKeys[i]);
        sMsg.append("[" + getNamebyFieldCode(strKeys[i]) + "]");
      }

    }

    int count = -1;
    int i = 0;
    for (i = 0; i < voCons.length; i++)
    {
      String sFieldCode = voCons[i].getFieldCode();
      if (!htKey.containsKey(sFieldCode))
        continue;
      if ((!voCons[i].getNoLeft()) || (!voCons[i].getNoRight())) {
        throw new BusinessException(sMsg + NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000165"));
      }

      if (count == -1) {
        count = i;
      }
      else if (i == count + 1)
        count = i;
      else {
        throw new BusinessException(sMsg + NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000166"));
      }

    }

    if ((count != -1) && (i != count + 1))
      throw new BusinessException(sMsg + NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000166"));
  }

  public void checkOncetime(ConditionVO[] voCons, String[] strKeys)
    throws BusinessException
  {
    if (strKeys == null) {
      return;
    }
    if ((voCons == null) || (voCons.length < 1)) {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000162"));
    }

    Hashtable htKey = new Hashtable();
    Hashtable htField = new Hashtable();

    StringBuffer sMsg = new StringBuffer();

    for (int i = 0; i < strKeys.length; i++) {
      if (!htKey.containsKey(strKeys[i])) {
        htKey.put(strKeys[i], strKeys[i]);
        sMsg.append("[" + getNamebyFieldCode(strKeys[i]) + "]");
      }

    }

    for (int i = 0; i < voCons.length; i++) {
      String sFieldCode = voCons[i].getFieldCode();
      if (!htKey.containsKey(sFieldCode))
        continue;
      if (htField.containsKey(sFieldCode)) {
        throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000167") + sMsg);
      }

      htField.put(sFieldCode, sFieldCode);
    }
  }

  public void checkOneNotOther(ConditionVO[] voCons, String[] strKeys, boolean bmust)
    throws BusinessException
  {
    if (strKeys == null) {
      return;
    }
    if ((voCons == null) || (voCons.length < 1)) {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000162"));
    }

    int count = 0;
    Hashtable htKey = new Hashtable();
    Hashtable htField = new Hashtable();
    StringBuffer sMsg = new StringBuffer();

    for (int i = 0; i < strKeys.length; i++) {
      if (!htKey.containsKey(strKeys[i])) {
        htKey.put(strKeys[i], strKeys[i]);
        sMsg.append("[" + getNamebyFieldCode(strKeys[i]) + "]");
      }

    }

    for (int i = 0; i < voCons.length; i++) {
      String sFieldCode = voCons[i].getFieldCode();

      if ((htKey.containsKey(sFieldCode)) && (!htField.containsKey(sFieldCode)))
      {
        count++;
      }
      if (!htField.containsKey(sFieldCode)) {
        htField.put(sFieldCode, sFieldCode);
      }
    }

    if (bmust) {
      if (count != 1) {
        throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000168") + sMsg);
      }

    }
    else if (count > 1)
      throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000169") + sMsg);
  }

  public void checkOneNotOther(ConditionVO[] voCons, ArrayList alKeys, boolean bmust)
    throws BusinessException
  {
    if (alKeys == null) {
      return;
    }
    if ((voCons == null) || (voCons.length < 1)) {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000162"));
    }

    int count = 0;
    Hashtable htKey = new Hashtable();
    Hashtable htField = new Hashtable();

    StringBuffer sMsg = new StringBuffer();

    for (int j = 0; j < alKeys.size(); j++) {
      String[] strKeys = (String[])(String[])alKeys.get(j);
      if (strKeys == null)
        continue;
      sMsg.append("[");
      for (int i = 0; i < strKeys.length; i++) {
        if (!htKey.containsKey(strKeys[i])) {
          htKey.put(strKeys[i], new Integer(j));
          sMsg.append(getNamebyFieldCode(strKeys[i]) + ",");
        }
      }

      sMsg.append("]");
    }

    for (int i = 0; i < voCons.length; i++) {
      String sFieldCode = voCons[i].getFieldCode();

      if (htKey.containsKey(sFieldCode)) {
        Integer iGroup = (Integer)htKey.get(sFieldCode);
        if (!htField.containsKey(iGroup)) {
          String[] strKeys = (String[])(String[])alKeys.get(iGroup.intValue());

          htField.put(iGroup, iGroup);
        }

      }

    }

    if (bmust) {
      if (htField.size() != 1) {
        throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000170") + sMsg);
      }

    }
    else if (htField.size() > 1)
      throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000171") + sMsg);
  }

  protected ClientEnvironment getClientEnvironment()
  {
    if (this.env == null) {
      this.env = ClientEnvironment.getInstance();
    }
    return this.env;
  }

  public void checkOneTrueAnotherMustFillin(ConditionVO[] voCons, String sFlagField, String sFieldMustFillIn, String sFieldMustFillInName)
    throws BusinessException
  {
    if ((sFlagField == null) || (sFieldMustFillIn == null) || (sFieldMustFillInName == null) || (voCons == null) || (voCons.length < 1))
    {
      System.out.println("检查必填查询条件错误！");
      return;
    }

    String sFieldCode = null;
    String sFieldValue = null;
    String sFlagFieldValue = null;
    String sFlagFieldName = null;
    String sFieldMustFillInValue = null;
    for (int i = 0; i < voCons.length; i++) {
      if (voCons[i] != null) {
        sFieldCode = voCons[i].getFieldCode();
        sFieldValue = voCons[i].getValue();

        if (sFlagField.equalsIgnoreCase(sFieldCode)) {
          sFlagFieldValue = sFieldValue;
          sFlagFieldName = voCons[i].getFieldName();
        } else {
          if (!sFieldMustFillIn.equalsIgnoreCase(sFieldCode))
            continue;
          sFieldMustFillInValue = sFieldValue;
        }
      }
    }
    UFBoolean flag = new UFBoolean(sFlagFieldValue);
    if ((flag.booleanValue()) && ((sFieldMustFillInValue == null) || (sFieldMustFillInValue.trim().length() == 0)))
    {
      throw new BusinessException("‘" + sFlagFieldName + "’值为true时,\n‘" + sFieldMustFillInName + "’不能为空！");
    }
  }

  protected String[] getConditionbyFieldCode(String sFieldCode)
  {
    String[] sReturnString = null;
    ConditionVO[] cvos = getConditionVOsByFieldCode(sFieldCode);
    if ((null != cvos) && (cvos.length > 0)) {
      sReturnString = new String[cvos.length];
      for (int i = 0; i < sReturnString.length; i++)
        sReturnString[i] = cvos[i].getOperaCode();
    }
    else {
      sReturnString = new String[0];
    }
    return sReturnString;
  }

  public ConditionVO[] getConditionVO()
  {
    ConditionVO[] voaCond = super.getConditionVO();

    voaCond = setCVO4MulCoPower(voaCond);

    ConditionVO[] voaCondpower = null;

    if (this.m_isDataPowerSqlReturned) {
      voaCondpower = getDataPowerScmVOs();
    }

    if ((voaCondpower != null) && (voaCondpower.length > 0)) {
      ConditionVO[] vocons = new ConditionVO[voaCond.length + voaCondpower.length];

      int count = 0;
      for (int i = 0; i < voaCond.length; i++) {
        vocons[i] = voaCond[i];
        count++;
      }
      for (int i = 0; i < voaCondpower.length; i++) {
        vocons[count] = voaCondpower[i];
        count++;
      }
      voaCond = vocons;
    }

    int iEleCount = 0;

    if (voaCond != null) {
      iEleCount = voaCond.length;
    }
    ConditionVO voPrintStatus = getPrintStatusCondVO();
    System.out.println("try PrintCount.");
    if (voPrintStatus != null) {
      System.out.println("try PrintCount." + voPrintStatus.getFieldCode() + "," + voPrintStatus.getValue());

      ConditionVO[] voaNewCond = new ConditionVO[iEleCount + 1];
      for (int i = 0; i < iEleCount; i++) {
        voaNewCond[i] = voaCond[i];
      }
      voaNewCond[iEleCount] = voPrintStatus;
      return voaNewCond;
    }

    return voaCond;
  }

  public String getCorpFieldCode()
  {
    return this.m_sCorpFieldCode;
  }

  public ConditionVO[] getDataPowerScmVOs()
  {
    return this.m_dataPowerScmVOs;
  }

  public String getDefaultCorpID()
  {
    if ((this.m_sDefaultCorpID == null) || (this.m_sDefaultCorpID.equals(""))) {
      this.m_sDefaultCorpID = getClientEnvironment().getCorporation().getPk_corp();
    }
    return this.m_sDefaultCorpID;
  }

  public QueryConditionVO getDefaultQueryCndVO(String sFieldCode, String sFieldName)
  {
    if ((sFieldCode == null) || (sFieldName == null)) {
      return null;
    }
    UFBoolean bTrue = new UFBoolean(true);
    UFBoolean bFalse = new UFBoolean(false);
    Integer intZero = new Integer(0);
    Integer intOne = new Integer(1);
    QueryConditionVO voQuery = new QueryConditionVO();
    voQuery.setFieldCode(sFieldCode);
    voQuery.setFieldName(sFieldName);
    voQuery.setOperaCode("=@");
    voQuery.setOperaName("等于@");

    voQuery.setDataType(intZero);
    voQuery.setNewMaxLength(new Integer(20));
    voQuery.setReturnType(intOne);
    voQuery.setDispType(intOne);
    voQuery.setPkTemplet("");
    voQuery.setIfAutocheck(bTrue);
    voQuery.setIfMust(bFalse);
    voQuery.setIfDefault(bTrue);
    voQuery.setIfUsed(bTrue);
    voQuery.setIfOrder(bFalse);
    voQuery.setIfGroup(bFalse);
    voQuery.setIfSum(bFalse);

    return voQuery;
  }

  public ConditionVO[] getExpandVOs(ConditionVO[] cvo)
  {
    ConditionVO[] cvoNew = null;
    Vector vCvoNew = new Vector(1, 1);

    boolean bHasPrintCount = false;

    for (int i = 0; i < cvo.length; i++) {
      if ((!bHasPrintCount) && (cvo[i].getFieldCode().indexOf("iprintcount") > 0))
      {
        bHasPrintCount = true;
      }
      if (cvo[i].getDataType() == 100) {
        String sFieldCode = cvo[i].getFieldCode().trim();
        String sAfterVfree0 = sFieldCode.substring(sFieldCode.indexOf("vfree0") + 6);

        FreeVO fvo = (FreeVO)cvo[i].getRefResult().getRefObj();

        for (int j = 1; j <= 10; j++) {
          ConditionVO cvoAddVO = new ConditionVO();
          String sValueName = "vfree" + Integer.toString(j).trim();
          if (fvo.getAttributeValue(sValueName) == null) {
            cvoAddVO.setOperaCode("IS");
            cvoAddVO.setValue("NULL");
            cvoAddVO.setDataType(1);
          } else {
            cvoAddVO.setOperaCode("=");
            cvoAddVO.setValue(fvo.getAttributeValue(sValueName).toString().trim());

            cvoAddVO.setDataType(0);
          }
          cvoAddVO.setFieldCode(sValueName + sAfterVfree0);
          cvoAddVO.setFieldName(sValueName + sAfterVfree0);
          cvoAddVO.setLogic(true);
          vCvoNew.addElement((ConditionVO)cvoAddVO.clone());
        }

      }
      else if (cvo[i].getDataType() == 200) {
        ConditionVO cvoAddVO = cvo[i];
        Object oValue = null;
        if ((cvoAddVO.getRefResult() != null) && (cvoAddVO.getRefResult().getRefObj() != null))
        {
          oValue = ((DataObject)cvoAddVO.getRefResult().getRefObj()).getID();

          cvoAddVO.setValue(oValue.toString());
        }
        else {
          oValue = cvoAddVO.getValue();
          cvoAddVO.setValue(oValue.toString());
        }
        if ((oValue instanceof Integer))
          cvoAddVO.setDataType(1);
        else if ((oValue instanceof UFDouble))
          cvoAddVO.setDataType(2);
        else if ((oValue instanceof String)) {
          cvoAddVO.setDataType(0);
        }
        vCvoNew.addElement((ConditionVO)cvoAddVO.clone());
      } else {
        vCvoNew.addElement(cvo[i]);
      }
    }

    int iEleCount = vCvoNew.size();
    ConditionVO voPrintStatus = null;
    if (!bHasPrintCount)
    {
      voPrintStatus = getPrintStatusCondVO();
      System.out.println("try PrintCount. ex.");
      if (voPrintStatus != null)
        iEleCount++;
    }
    cvoNew = new ConditionVO[iEleCount];
    vCvoNew.copyInto(cvoNew);

    if ((!bHasPrintCount) && (voPrintStatus != null)) {
      System.out.println("try PrintCount.ex." + voPrintStatus.getFieldCode() + "," + voPrintStatus.getValue());

      cvoNew[(iEleCount - 1)] = voPrintStatus;
    }

    return cvoNew;
  }

  public ConditionVO[] getExpandVOs(ConditionVO[] cvo, String sFreeAlianame)
  {
    if (sFreeAlianame == null)
      sFreeAlianame = "";
    else {
      sFreeAlianame = sFreeAlianame.trim() + '.';
    }
    ConditionVO[] cvoNew = null;
    Vector vCvoNew = new Vector(1, 1);

    boolean bHasPrintCount = false;

    for (int i = 0; i < cvo.length; i++) {
      if ((!bHasPrintCount) && (cvo[i].getFieldCode().indexOf("iprintcount") > 0))
      {
        bHasPrintCount = true;
      }
      if (cvo[i].getDataType() == 100) {
        String sFieldCode = cvo[i].getFieldCode().trim();
        String sAfterVfree0 = sFieldCode.substring(sFieldCode.indexOf("vfree0") + 6);

        FreeVO fvo = (FreeVO)cvo[i].getRefResult().getRefObj();

        for (int j = 1; j <= 10; j++) {
          ConditionVO cvoAddVO = new ConditionVO();
          String sValueName = "vfree" + Integer.toString(j).trim();
          if (fvo.getAttributeValue(sValueName) == null) {
            cvoAddVO.setOperaCode("IS");
            cvoAddVO.setValue("NULL");
            cvoAddVO.setDataType(1);
          } else {
            cvoAddVO.setOperaCode("=");
            cvoAddVO.setValue(fvo.getAttributeValue(sValueName).toString().trim());

            cvoAddVO.setDataType(0);
          }
          cvoAddVO.setFieldCode(sFreeAlianame + sValueName + sAfterVfree0);

          cvoAddVO.setFieldName(sFreeAlianame + sValueName + sAfterVfree0);

          cvoAddVO.setLogic(true);
          vCvoNew.addElement((ConditionVO)cvoAddVO.clone());
        }

      }
      else if (cvo[i].getDataType() == 200) {
        ConditionVO cvoAddVO = cvo[i];
        Object oValue = null;
        if ((cvoAddVO.getRefResult() != null) && (cvoAddVO.getRefResult().getRefObj() != null))
        {
          oValue = ((DataObject)cvoAddVO.getRefResult().getRefObj()).getID();

          cvoAddVO.setValue(oValue.toString());
        }
        else {
          oValue = cvoAddVO.getValue();
          cvoAddVO.setValue(oValue.toString());
        }
        if ((oValue instanceof Integer))
          cvoAddVO.setDataType(1);
        else if ((oValue instanceof UFDouble))
          cvoAddVO.setDataType(2);
        else if ((oValue instanceof String)) {
          cvoAddVO.setDataType(0);
        }
        vCvoNew.addElement((ConditionVO)cvoAddVO.clone());
      } else {
        vCvoNew.addElement(cvo[i]);
      }
    }

    int iEleCount = vCvoNew.size();
    ConditionVO voPrintStatus = null;
    if (!bHasPrintCount)
    {
      voPrintStatus = getPrintStatusCondVO();
      System.out.println("try PrintCount. ex2.");
      if (voPrintStatus != null)
        iEleCount++;
    }
    cvoNew = new ConditionVO[iEleCount];
    vCvoNew.copyInto(cvoNew);

    if ((!bHasPrintCount) && (voPrintStatus != null)) {
      System.out.println("try PrintCount.ex2." + voPrintStatus.getFieldCode() + "," + voPrintStatus.getValue());

      cvoNew[(iEleCount - 1)] = voPrintStatus;
    }

    return cvoNew;
  }

  protected int getListRow(int iIndexRow)
  {
    for (int j = 0; j < getValueRefResults().length; j++) {
      if (getIndexes(j - getImmobilityRows()) == iIndexRow) {
        return j;
      }
    }
    return -1;
  }

  protected String getNamebyFieldCode(String sFieldCode)
  {
    String sReturnString = "";
    for (int i = 0; i < getConditionDatas().length; i++) {
      if (getConditionDatas()[i].getFieldCode().trim().equals(sFieldCode)) {
        sReturnString = getConditionDatas()[i].getFieldName().trim();
        break;
      }
    }
    return sReturnString;
  }

  protected String getPKbyFieldCode(String sFieldCode)
  {
    String sReturnString = "";
    for (int i = 0; i < getConditionDatas().length; i++) {
      if (getConditionDatas()[i].getFieldCode().trim().equals(sFieldCode)) {
        int iListRow = getListRow(i);
        if (iListRow == -1) break;
        if ((getValueRefResults()[iListRow] != null) && (getValueRefResults()[iListRow].getRefPK() != null))
        {
          sReturnString = getValueRefResults()[iListRow].getRefPK().trim(); break;
        }

        sReturnString = "";
        break;
      }
    }

    return sReturnString;
  }

  protected String getRefField(String sRefNodeName, int flag)
  {
    if (this.m_htRefField == null) {
      this.m_htRefField = new HashMap();
      this.m_htRefField.put("库存组织" + String.valueOf(0), "bodycode");
      this.m_htRefField.put("库存组织" + String.valueOf(1), "bodyname");
      this.m_htRefField.put("库存组织" + String.valueOf(2), "pk_calbody");
      this.m_htRefField.put("库存组织" + String.valueOf(-1), " from bd_calbody where 0=0 ");
      this.m_htRefField.put("仓库档案" + String.valueOf(0), "storcode");
      this.m_htRefField.put("仓库档案" + String.valueOf(1), "storname");
      this.m_htRefField.put("仓库档案" + String.valueOf(2), "pk_stordoc");
      this.m_htRefField.put("仓库档案" + String.valueOf(-1), " from bd_stordoc where 0=0 ");
      this.m_htRefField.put("客商档案" + String.valueOf(0), "custcode");
      this.m_htRefField.put("客商档案" + String.valueOf(1), "custname");
      this.m_htRefField.put("客商档案" + String.valueOf(2), "pk_cumandoc");
      this.m_htRefField.put("客商档案" + String.valueOf(-1), " from bd_cubasdoc b ,bd_cumandoc m where b.pk_cubasdoc=m.pk_cubasdoc ");
      this.m_htRefField.put("客户档案" + String.valueOf(0), "custcode");
      this.m_htRefField.put("客户档案" + String.valueOf(1), "custname");
      this.m_htRefField.put("客户档案" + String.valueOf(2), "pk_cumandoc");
      this.m_htRefField.put("客户档案" + String.valueOf(-1), " from bd_cubasdoc b ,bd_cumandoc m where b.pk_cubasdoc=m.pk_cubasdoc ");
      this.m_htRefField.put("供应商档案" + String.valueOf(0), "custcode");
      this.m_htRefField.put("供应商档案" + String.valueOf(1), "custname");
      this.m_htRefField.put("供应商档案" + String.valueOf(2), "pk_cumandoc");
      this.m_htRefField.put("供应商档案" + String.valueOf(-1), " from bd_cubasdoc b ,bd_cumandoc m where b.pk_cubasdoc=m.pk_cubasdoc ");
      this.m_htRefField.put("部门档案" + String.valueOf(0), "deptcode");
      this.m_htRefField.put("部门档案" + String.valueOf(1), "deptname");
      this.m_htRefField.put("部门档案" + String.valueOf(2), "pk_deptdoc");
      this.m_htRefField.put("部门档案" + String.valueOf(-1), " from bd_deptdoc where 0=0 ");
      this.m_htRefField.put("人员档案" + String.valueOf(0), "psncode");
      this.m_htRefField.put("人员档案" + String.valueOf(1), "psnname");
      this.m_htRefField.put("人员档案" + String.valueOf(2), "pk_psndoc");
      this.m_htRefField.put("人员档案" + String.valueOf(-1), " from bd_psndoc where 0=0 ");

      this.m_htRefField.put("采购组织" + String.valueOf(0), "code");
      this.m_htRefField.put("采购组织" + String.valueOf(1), "name");
      this.m_htRefField.put("采购组织" + String.valueOf(2), "pk_purorg");
      this.m_htRefField.put("采购组织" + String.valueOf(-1), " from bd_purorg where 0=0 ");

      this.m_htRefField.put("存货档案" + String.valueOf(0), "invcode");
      this.m_htRefField.put("存货档案" + String.valueOf(1), "invname");
      this.m_htRefField.put("存货档案" + String.valueOf(2), "pk_invmandoc");
      this.m_htRefField.put("存货档案" + String.valueOf(-1), " from bd_invbasdoc b, bd_invmandoc m where b.pk_invbasdoc = m.pk_invbasdoc ");
      this.m_htRefField.put("存货分类" + String.valueOf(0), "invclasscode");
      this.m_htRefField.put("存货分类" + String.valueOf(1), "invclassname");
      this.m_htRefField.put("存货分类" + String.valueOf(2), "pk_invcl");
      this.m_htRefField.put("存货分类" + String.valueOf(-1), " from bd_invcl where 0=0 ");

      this.m_htRefField.put("项目管理档案" + String.valueOf(0), "jobcode");
      this.m_htRefField.put("项目管理档案" + String.valueOf(1), "jobname");
      this.m_htRefField.put("项目管理档案" + String.valueOf(2), "pk_jobmngfil");
      this.m_htRefField.put("项目管理档案" + String.valueOf(-1), " from bd_jobbasfil b, bd_jobmngfil m where b.pk_jobbasfil = m.pk_jobbasfil ");

      this.m_htRefField.put("地区分类" + String.valueOf(0), "areaclcode");
      this.m_htRefField.put("地区分类" + String.valueOf(1), "areaclname");
      this.m_htRefField.put("地区分类" + String.valueOf(2), "pk_areacl");
      this.m_htRefField.put("地区分类" + String.valueOf(-1), " from bd_areacl where 0=0 ");

      this.m_htRefField.put("销售组织" + String.valueOf(0), "vsalestrucode");
      this.m_htRefField.put("销售组织" + String.valueOf(1), "vsalestruname");
      this.m_htRefField.put("销售组织" + String.valueOf(2), "csalestruid");
      this.m_htRefField.put("销售组织" + String.valueOf(-1), " from bd_salestru where 0=0 ");
    }

    return (String)this.m_htRefField.get(sRefNodeName + String.valueOf(flag));
  }

  private UIRefPane getRefPaneByCode(String sFieldCode)
  {
    QueryConditionVO[] voaConData = getConditionDatas();
    if ((voaConData == null) || (voaConData.length == 0)) {
      return null;
    }

    UIRefPane ref = null;
    for (int i = 0; i < voaConData.length; i++) {
      if (sFieldCode.equals(voaConData[i].getFieldCode())) {
        if (voaConData[i].getDataType().intValue() != 5) break;
        EditComponentFacotry factry = new EditComponentFacotry(voaConData[i]);

        ref = (UIRefPane)factry.getEditComponent(null);

        break;
      }

    }

    return ref;
  }

  public UIRefPane getRefPaneByNodeCode(String sFieldCode)
  {
    QueryConditionVO[] voaConData = getConditionDatas();
    if ((voaConData == null) || (voaConData.length == 0)) {
      return null;
    }

    UIRefPane ref = null;
    for (int i = 0; i < voaConData.length; i++) {
      if (sFieldCode.equals(voaConData[i].getNodecode())) {
        if (voaConData[i].getDataType().intValue() != 5) break;
        EditComponentFacotry factry = new EditComponentFacotry(voaConData[i]);

        ref = (UIRefPane)factry.getEditComponent(null);

        break;
      }

    }

    return ref;
  }

  private JTextField gettfUnitCode()
  {
    if (this.ivjtfUnitCode == null) {
      try {
        this.ivjtfUnitCode = new JTextField();
        this.ivjtfUnitCode.setName("tfUnitCode");
        this.ivjtfUnitCode.setBounds(104, 26, 77, 20);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjtfUnitCode;
  }

  public UIPanel getUIPanel()
  {
    if (this.ivjUIPanel == null)
    {
      try
      {
        this.ivjUIPanel = new UIPanel();
        this.ivjUIPanel.setName("yudaying");
        this.ivjUIPanel.setLayout(null);

        System.out.println("call getUIPanel");
        this.ivjUIPanel.add(new UITextField("dddd"));
        this.ivjUIPanel.add(gettfUnitCode());
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIPanel;
  }

  public String getUnitCode()
  {
    return gettfUnitCode().getText();
  }

  protected String getValuebyFieldCode(String sFieldCode)
  {
    String sReturnString = "";
    for (int i = 0; i < getConditionVO().length; i++) {
      if (getConditionVO()[i].getFieldCode().trim().equals(sFieldCode)) {
        sReturnString = getConditionVO()[i].getValue().trim();
        break;
      }

    }

    return sReturnString;
  }

  protected String[] getValuesbyFieldCode(String sFieldCode)
  {
    String[] sReturnString = new String[0];
    ArrayList al = new ArrayList();
    for (int i = 0; i < getConditionVO().length; i++) {
      if (getConditionVO()[i].getFieldCode().trim().equals(sFieldCode)) {
        if (getConditionVO()[i].getDataType() == 5) {
          al.add(getConditionVO()[i].getRefResult().getRefPK().trim());
        }
        else
        {
          al.add(getConditionVO()[i].getValue().trim());
        }

      }

    }

    sReturnString = (String[])(String[])al.toArray(sReturnString);
    return sReturnString;
  }

  public String getWhereSQL(ConditionVO[] cvo)
  {
    return super.getWhereSQL(getExpandVOs(cvo));
  }

  public String getWhereSQL(ConditionVO[] cvo, String sFreeAlianame)
  {
    return super.getWhereSQL(getExpandVOs(cvo, sFreeAlianame));
  }

  public void initCorpRef(String sCorpFieldCode, String sShowCorpID, ArrayList alAllCorpIDs)
  {
    ArrayList altemp = new ArrayList();
    altemp.add(sCorpFieldCode);
    altemp.add(sShowCorpID);
    this.m_sDefaultCorpID = sShowCorpID;
    this.m_sCorpFieldCode = sCorpFieldCode;
    if (null == alAllCorpIDs)
      altemp.add(new ArrayList());
    else {
      altemp.add(alAllCorpIDs);
    }
    this.m_alCorpRef.add(altemp);
    if (alAllCorpIDs != null) {
      if (this.m_sdefaultcorps == null) {
        this.m_sdefaultcorps = new String[alAllCorpIDs.size()];
        alAllCorpIDs.toArray(this.m_sdefaultcorps);
      }

      this.m_hmCorpData.put(sCorpFieldCode, alAllCorpIDs);
    }

    setDefaultValue(sCorpFieldCode.trim(), sShowCorpID, null);
  }

  public void initLocatorRef(String sLocatorFieldCode, String sInvFieldCode, String sWhFieldCode)
  {
    ArrayList altemp = new ArrayList();
    altemp.add(sLocatorFieldCode);
    altemp.add(sInvFieldCode);
    altemp.add(sWhFieldCode);
    this.m_alLocatorRef.add(altemp);
  }

  public void initQueryDlgRef()
  {
    setRefInitWhereClause("head.cwarehouseid", "仓库档案", "gubflag='N'  and pk_corp=", "pk_corp");

    setRefInitWhereClause("cinwarehouseid", "仓库档案", "gubflag='N'  and pk_corp=", "pk_corp");

    setRefInitWhereClause("coutwarehouseid", "仓库档案", "gubflag='N'  and pk_corp=", "pk_corp");

    setRefInitWhereClause("cwastewarehouseid", "仓库档案", "gubflag='Y'  and pk_corp=", "pk_corp");

    setRefInitWhereClause("head.ccustomerid", "客户档案", "(custflag ='0' or custflag ='2') and  bd_cumandoc.pk_corp=", "pk_corp");

    setRefInitWhereClause("head.cproviderid", "供应商档案", "(custflag ='1' or custflag ='3') and  bd_cumandoc.pk_corp=", "pk_corp");

    setRefInitWhereClause("cvendorid", "供应商档案", "(custflag ='1' or custflag ='3')  and bd_cumandoc.pk_corp=", "pk_corp");

    setRefInitWhereClause("cinventorycode", "存货档案", " bd_invbasdoc.discountflag='N' and bd_invbasdoc.laborflag='N'   and bd_invmandoc.pk_corp=", "pk_corp");

    setRefInitWhereClause("body.cinventoryid", "存货档案", " bd_invbasdoc.discountflag='N' and bd_invbasdoc.laborflag='N'   and bd_invmandoc.pk_corp=", "pk_corp");

    setRefInitWhereClause("ccostobject", "物料档案", "sfcbdx='Y' and bd_produce.pk_corp=", "pk_corp");

    setRefInitWhereClause("cdispatcherid", "收发类别", " pk_corp='0001' or pk_corp=", "pk_corp");
  }

  public boolean isCloseOK()
  {
    return getResult() == 1;
  }

  public boolean setAstUnit(String sAstUnit, String[] sInitParam)
  {
    if ((sAstUnit == null) || (sAstUnit.trim().length() == 0) || (sInitParam == null) || (sInitParam.length != 2))
    {
      return false;
    }this.m_htAstCorpInv.put(sAstUnit.trim(), sInitParam);
    return true;
  }

  public void setAutoClear(String sWhenChanged, String[] sThenClears)
  {
    this.m_htAutoClear.put(sWhenChanged, sThenClears);
  }

  public boolean setCombox(String sFieldCode, Object[][] Values)
  {
    try
    {
      UIComboBox uicbUIComboBox = new UIComboBox();
      for (int i = 0; i < Values.length; i++) {
        DataObject doValue = new DataObject(Values[i][0], Values[i][1]);
        uicbUIComboBox.addItem(doValue);
      }

      UIComboBoxCellEditor editor = new UIComboBoxCellEditor(uicbUIComboBox);

      setValueRef(sFieldCode, editor);
      DataObject doValue = new DataObject(Values[0][0], Values[0][1]);
      setDefaultValue(sFieldCode, null, doValue.toString());

      RefResultVO rrvo = new RefResultVO();
      rrvo.setRefCode(doValue.getID().toString().trim());
      rrvo.setRefName(doValue.getName().toString().trim());
      rrvo.setRefPK(doValue.getID().toString().trim());

      setDefaultResultVO(sFieldCode, rrvo);
      return true; } catch (Exception e) {
    }
    return false;
  }

  public boolean setCorpRefs(String sCorpFieldCode, String[] sRefFieldCodes)
  {
    try
    {
      if ((sCorpFieldCode != null) && (sRefFieldCodes != null) && (sRefFieldCodes.length > 0))
      {
        this.m_htCorpRef.put(sCorpFieldCode, sRefFieldCodes);
        for (int i = 0; i < sRefFieldCodes.length; i++) {
          int index = this.m_alpower.indexOf(sRefFieldCodes[i]);
          if (index >= 0)
            continue;
          this.m_alpower.add(sRefFieldCodes[i]);
          this.m_hmRefCorp.put(sRefFieldCodes[i], sCorpFieldCode);
          UIRefPane oRef = getRefPaneByCode(sRefFieldCodes[i]);
          if (oRef != null) {
            oRef.getRefModel().setUseDataPower(true);
            setValueRef(sRefFieldCodes[i], oRef);
          }
        }

      }

      return true;
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }return false;
  }

  public void setDefaultCorps(String[] sNewCorps)
  {
    this.m_sdefaultcorps = sNewCorps;
  }

  public boolean setDifferenceRef(String sSourceFieldCode, String sDestinationFieldCode, Object[][] oRefs)
  {
    if ((null == sSourceFieldCode) || (null == sDestinationFieldCode) || (null == oRefs))
    {
      return false;
    }
    sSourceFieldCode = sSourceFieldCode.trim();
    sDestinationFieldCode = sDestinationFieldCode.trim();

    boolean bFounded = false;
    for (int i = 0; i < this.m_alComboxToRef.size(); i++) {
      ArrayList alRow = (ArrayList)(ArrayList)this.m_alComboxToRef.get(i);
      if ((!alRow.get(0).toString().trim().equals(sSourceFieldCode)) || (!alRow.get(1).toString().trim().equals(sDestinationFieldCode))) {
        continue;
      }
      alRow.set(2, oRefs);
      this.m_alComboxToRef.set(i, alRow);
      bFounded = true;
      break;
    }

    if (!bFounded) {
      ArrayList alRow = new ArrayList();
      alRow.add(sSourceFieldCode);
      alRow.add(sDestinationFieldCode);
      alRow.add(oRefs);
      this.m_alComboxToRef.add(alRow);
    }

    return true;
  }

  public boolean setFreeItem(String sFieldCode, String sInvFieldCode)
  {
    try
    {
      FreeItemRefPane firpFreeItemRefPane = new FreeItemRefPane();

      UIFreeItemCellEditor editor = new UIFreeItemCellEditor(firpFreeItemRefPane);

      setValueRef(sFieldCode, editor);

      this.m_htFreeItemInv.put(sFieldCode.trim(), sInvFieldCode.trim());

      return true; } catch (Exception e) {
    }
    return false;
  }

  public void setInitDate(String sFieldCode, String ufdSourceDate)
  {
    setDefaultValue(sFieldCode, null, ufdSourceDate);
  }

  public void setInitDate(String sFieldCode, String ufdSourceDate, boolean bIsFirstDateOrNot)
  {
    UFDate ufdValueDate = new UFDate("2001-01-01");
    if (bIsFirstDateOrNot) {
      ufdValueDate = new UFDate(new String(ufdSourceDate.substring(0, 8) + "01"));
    }
    else {
      ufdValueDate = new UFDate(new String(ufdSourceDate.substring(0, 8) + new Integer(new UFDate(ufdSourceDate).getDaysMonth())));
    }

    setDefaultValue(sFieldCode, null, ufdValueDate.toString());
  }

  public boolean setLot(String sFieldLotCode, String sFieldInvCode)
  {
    try
    {
      UIRefPane lqrLotQueryRef = (UIRefPane)Class.forName("nc.ui.ic.pub.lotquery.LotQueryRef").newInstance();

      UIRefCellEditor editor = new UIRefCellEditor(lqrLotQueryRef);
      setValueRef(sFieldLotCode, editor);

      this.m_htLotInv.put(sFieldLotCode.trim(), sFieldInvCode.trim());

      return true; } catch (Exception e) {
    }
    return false;
  }

  public boolean setRefInitWhereClause(String sFieldCode, String sRefName, String sWhereClause, String sCheckFieldCode)
  {
    try
    {
      String sMyFieldCode = null;

      for (int i = this.m_alRefInitWhereClause.size() - 1; i >= 0; i--) {
        sMyFieldCode = ((ArrayList)this.m_alRefInitWhereClause.get(i)).get(0).toString();

        if (!sMyFieldCode.trim().equals(sFieldCode.trim()))
          continue;
        this.m_alRefInitWhereClause.remove(i);
      }

      ArrayList alrow = new ArrayList();
      alrow.add(sFieldCode.trim());
      alrow.add(sRefName);
      alrow.add(sWhereClause.trim());
      alrow.add(sCheckFieldCode);
      this.m_alRefInitWhereClause.add(alrow);
      UIRefPane oRef = getRefPaneByCode(sFieldCode);
      if (oRef != null)
      {
        oRef.getRefModel().setPk_corp(getDefaultCorpID());
        setValueRef(sFieldCode, oRef);
      }

      return true;
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }return false;
  }

  public boolean setRefMultiInit(String sFieldCode, String sBeforeWhereClause, String sAfterWhereClause, String[][] sCheckFieldCodes, boolean bIsNullUsed)
  {
    try
    {
      ArrayList alrow = new ArrayList();
      alrow.add(sBeforeWhereClause.trim());
      alrow.add(sAfterWhereClause.trim());
      alrow.add(sCheckFieldCodes);
      alrow.add(new UFBoolean(bIsNullUsed));

      this.m_htInitMultiRef.put(sFieldCode, alrow);
      return true;
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }return false;
  }

  public boolean setStat(String sFieldStateBCode, String sFieldStateHCode, String sFieldCorpcode)
  {
    try
    {
      UIRefPane srpStathRefPane1 = (UIRefPane)Class.forName("nc.ui.ic.pub.stat.StathRefPane").newInstance();

      srpStathRefPane1.setWhereString("bicusedflag='Y'");
      UIRefPane srpStatbRefPane2 = (UIRefPane)Class.forName("nc.ui.ic.pub.stat.StathRefPane").newInstance();

      UIRefCellEditor editorh = new UIRefCellEditor(srpStathRefPane1);

      setValueRef(sFieldStateHCode, editorh);

      UIRefCellEditor editorb = new UIRefCellEditor(srpStatbRefPane2);

      setValueRef(sFieldStateBCode, editorb);

      this.m_htStatBtoH.put(sFieldStateBCode.trim(), sFieldStateHCode.trim());

      this.m_htStatHtoCorp.put(sFieldStateHCode.trim(), sFieldCorpcode.trim());

      return true; } catch (Exception e) {
    }
    return false;
  }

  protected void setValueRefEditor(int iRow, int iCol)
  {
    int index = getIndexes(iRow - getImmobilityRows());

    if ((index < 0) || (index >= getConditionDatas().length)) {
      System.out.println("qry cond err.");
      return;
    }
    QueryConditionVO qcvo = getConditionDatas()[index];
    String sFieldCode = qcvo.getFieldCode().trim();
    ArrayList alRefRow = null;

    for (int i = 0; i < this.m_alRefInitWhereClause.size(); i++)
    {
      alRefRow = (ArrayList)this.m_alRefInitWhereClause.get(i);
      String refname = alRefRow.get(1) == null ? null : alRefRow.get(1).toString().trim();
      String refcode = alRefRow.get(0).toString().trim();
      String refwhere = alRefRow.get(2).toString().trim();
      Object refcheck = alRefRow.get(3);
      if (refcode.equals(qcvo.getFieldCode().trim())) {
        UIRefPane uirp = (UIRefPane)getValueRefObjectByFieldCode(qcvo.getFieldCode());

        if (refname == null) {
          refname = uirp.getRefNodeName();
        }
        uirp.getRefModel().setSealedDataShow(true);
        String sPK = "";
        StringBuffer swhere = new StringBuffer();
        if (refwhere != null)
          swhere.append(refwhere);
        if (refcheck != null)
          swhere.append("''");
        if ("仓库档案".equals(refname))
          uirp.getRefModel().addWherePart(" and " + swhere.toString());
        else
          uirp.setWhereString(swhere.toString());
        changeValueRef(refname, uirp);

        for (int j = 0; j < getUITabInput().getRowCount(); j++) {
          int indexNow = getIndexes(j - getImmobilityRows());

          if ((indexNow < 0) || (indexNow >= getConditionDatas().length))
          {
            continue;
          }

          QueryConditionVO qcvoNow = getConditionDatas()[indexNow];
          if (qcvoNow.getFieldCode().trim().equals(refcheck)) {
            RefResultVO tempValue = getValueRefResults()[(j - getImmobilityRows())];

            if ((tempValue == null) || (tempValue.getRefPK() == null)) {
              break;
            }
            sPK = getValueRefResults()[(j - getImmobilityRows())].getRefPK().trim();

            System.out.println("wh sql=" + alRefRow.get(2).toString().trim() + "'" + sPK + "'");

            swhere = new StringBuffer();
            if (refwhere != null)
              swhere.append(refwhere);
            if (refcheck != null) {
              swhere.append("'" + sPK + "'");
            }
            if ("仓库档案".equals(refname))
              uirp.getRefModel().addWherePart(" and " + swhere.toString());
            else
              uirp.setWhereString(swhere.toString());
            changeValueRef(refname, uirp);
            break;
          }
        }
        if ("客商档案".equals(refname)) {
          uirp.setStrPatch(" DISTINCT ");
        }
        if (refcode.indexOf("costobject") >= 0)
        {
          AbstractRefModel refmodel = uirp.getRef().getRefModel();

          String sTableName = refmodel.getTableName();
          if (sTableName.indexOf("bd_produce") < 0) {
            sTableName = sTableName + ",bd_produce";
            refmodel.setTableName(sTableName);
            uirp.setStrPatch(" DISTINCT ");
          }

        }

        if ((!sPK.equals("")) || (refcheck == null) || (!refcheck.toString().endsWith("pk_corp"))) break;
        swhere = new StringBuffer();
        swhere.append(refwhere);
        swhere.append("'" + this.m_sDefaultCorpID + "'");

        if ("仓库档案".equals(refname))
          uirp.getRefModel().addWherePart(" and " + swhere.toString());
        else
          uirp.setWhereString(swhere.toString());
        changeValueRef(refname, uirp);

        break;
      }
    }

    for (int i = 0; i < this.m_alCorpRef.size(); i++) {
      alRefRow = (ArrayList)this.m_alCorpRef.get(i);
      if (!alRefRow.get(0).toString().trim().equals(qcvo.getFieldCode().trim()))
        continue;
      UIRefPane uirp = new UIRefPane();
      uirp.setRefNodeName("公司目录");
      uirp.getRefModel().setSealedDataShow(true);
      String sPK = alRefRow.get(1).toString().trim();
      uirp.setPK(sPK);
      String sPKs = "'" + sPK + "' ";
      ArrayList alPKs = (ArrayList)alRefRow.get(2);
      for (int j = 0; j < alPKs.size(); j++) {
        sPKs = sPKs + ",'" + (String)alPKs.get(j) + "'";
      }
      uirp.getRefModel().addWherePart(" and pk_corp in (" + sPKs + ")");

      changeValueRef(alRefRow.get(0).toString().trim(), uirp);
      if (qcvo.getReturnType().intValue() == 1) {
        setDefaultValue(alRefRow.get(0).toString().trim(), sPK, uirp.getRefName());

        break;
      }setDefaultValue(alRefRow.get(0).toString().trim(), sPK, uirp.getRefCode());

      break;
    }

    for (int i = 0; i < this.m_alLocatorRef.size(); i++) {
      alRefRow = (ArrayList)this.m_alLocatorRef.get(i);
      if (!alRefRow.get(0).toString().trim().equals(qcvo.getFieldCode().trim()))
        continue;
      String sNowFieldCode = alRefRow.get(0).toString().trim();
      String sInvFieldCode = alRefRow.get(1).toString().trim();
      String sWhFieldCode = alRefRow.get(2).toString().trim();
      String sInvID = getPKbyFieldCode(sInvFieldCode);
      InvVO invvo = null;
      if (this.m_htInvVO.containsKey(sInvID)) {
        invvo = (InvVO)this.m_htInvVO.get(sInvID);
      }
      String sWhID = getPKbyFieldCode(sWhFieldCode);
      WhVO voWh = null;
      if (this.m_htWhVO.containsKey(sWhID)) {
        voWh = (WhVO)this.m_htWhVO.get(sWhID);
      }
      String sName = getValuebyFieldCode(sNowFieldCode);
      String spk = getPKbyFieldCode(sNowFieldCode);

      ILocatorRefPane uirp = null;
      try {
        uirp = (ILocatorRefPane)Class.forName("nc.bs.ic.pub.locatorref.LocatorRefPane").newInstance();
      }
      catch (Exception e)
      {
        System.out.println(e);
        changeValueRef(alRefRow.get(0).toString().trim(), null);
        break;
      }

      uirp.setInOutFlag(-1);
      uirp.setOldValue(sName, null, spk);
      uirp.setParam(voWh, invvo);
      changeValueRef(alRefRow.get(0).toString().trim(), uirp);

      break;
    }

    if (this.m_htInitMultiRef.containsKey(sFieldCode)) {
      Object oRef = getValueRefObjectByFieldCode(sFieldCode);
      if ((oRef instanceof UIRefCellEditor))
        oRef = ((UIRefCellEditor)oRef).getComponent();
      if ((oRef != null) && ((oRef instanceof UIRefPane))) {
        UIRefPane uiRef = (UIRefPane)oRef;
        ArrayList alObj = (ArrayList)this.m_htInitMultiRef.get(sFieldCode);
        String sBeforeWhereClause = (String)alObj.get(0);
        String sAfterWhereClause = (String)alObj.get(1);
        String[][] sKeys = (String[][])(String[][])alObj.get(2);
        boolean bIsNullUsed = ((UFBoolean)alObj.get(3)).booleanValue();
        StringBuffer sb = new StringBuffer();

        uiRef.getRefModel().setWherePart(uiRef.getRefModel().getOriginWherePart());

        if ((sBeforeWhereClause != null) && (sBeforeWhereClause.trim().length() > 0))
        {
          sb.append(sBeforeWhereClause);
        }
        boolean bFirstAddAnd = true;
        for (int i = 0; i < sKeys.length; i++) {
          String[] sValues = getValuesbyFieldCode(sKeys[i][0]);
          for (int j = 0; j < sValues.length; j++) {
            Object oValue = sValues[j];
            if ((null != oValue) && (oValue.toString().trim().length() != 0))
            {
              if (bFirstAddAnd) {
                sb.append(" 1=1 ");
                bFirstAddAnd = false;
              }
              sb.append(" and ").append(sKeys[i][1]).append(getConditionbyFieldCode(sKeys[i][0])[j]);

              sb.append("'").append(oValue).append("'");
            }
            else if (bIsNullUsed) {
              if (bFirstAddAnd) {
                sb.append(" 1=1 ");
                bFirstAddAnd = false;
              }
              sb.append(" and (").append(sKeys[i][1]).append("is null or len(ltrim(").append(sKeys[i][1]).append("))=0)");

              bIsNullUsed = false;
            }
          }
        }

        if ((sAfterWhereClause != null) && (sAfterWhereClause.trim().length() > 0))
        {
          if (sb.toString().trim().length() == 0)
            sb.append(" 1=1 " + sAfterWhereClause);
          else {
            sb.append(sAfterWhereClause);
          }
        }

        if ((null != sb) && (sb.toString().trim().length() > 0)) {
          uiRef.getRefModel().addWherePart(" and " + sb.toString());
        }
        changeValueRef(sFieldCode, uiRef);
      }
    }

    super.setValueRefEditor(iRow, iCol);
    if (qcvo.getDataType().intValue() == 100) {
      FreeItemRefPane firpFreeItemRefPane = new FreeItemRefPane();
      String sFreeItemCode = qcvo.getFieldCode().trim();
      if (this.m_htFreeItemInv.containsKey(sFreeItemCode)) {
        String sInvIDCode = this.m_htFreeItemInv.get(sFreeItemCode).toString().trim();

        String sInvID = "";
        sInvID = getPKbyFieldCode(sInvIDCode);
        if (this.m_htInvVO.containsKey(sInvID)) {
          InvVO ivo = (InvVO)this.m_htInvVO.get(sInvID);
          firpFreeItemRefPane.setFreeItemParam(ivo);
        }
      }
      UIFreeItemCellEditor editor = new UIFreeItemCellEditor(firpFreeItemRefPane);

      getUITabInput().getColumnModel().getColumn(iCol).setCellEditor(editor);
    }
    else if (qcvo.getDataType().intValue() != 200)
    {
      if (qcvo.getDataType().intValue() == 300)
      {
        UIRefPane srpStathRefPane = null;
        try {
          srpStathRefPane = (UIRefPane)Class.forName("nc.ui.ic.pub.stat.StathRefPane").newInstance();
        }
        catch (Exception e) {
          System.out.println(e);
        }

        String sStatH = qcvo.getFieldCode().trim();
        if (this.m_htStatHtoCorp.containsKey(sStatH)) {
          String sCorpField = this.m_htStatHtoCorp.get(sStatH).toString().trim();

          String sCorpValue = "";
          sCorpValue = getPKbyFieldCode(sCorpField);
          if ((sCorpValue == null) || (sCorpValue.trim().length() == 0)) {
            srpStathRefPane.setWhereString("bicusedflag='Y'");
          }
          else {
            srpStathRefPane.setWhereString("bicusedflag='Y' and pk_corp='" + sCorpValue + "'");
          }

        }

        UIRefCellEditor editorh = new UIRefCellEditor(srpStathRefPane);

        getUITabInput().getColumnModel().getColumn(iCol).setCellEditor(editorh);
      }
      else if (qcvo.getDataType().intValue() == 350) {
        IStatbRefPane srpStatbRefPane = null;
        try
        {
          srpStatbRefPane = (IStatbRefPane)Class.forName("nc.ui.ic.pub.stat.StatbRefPane").newInstance();
        }
        catch (Exception e)
        {
          System.out.println(e);
        }

        String sStatB = qcvo.getFieldCode().trim();
        if (this.m_htStatBtoH.containsKey(sStatB)) {
          String sStatHCode = this.m_htStatBtoH.get(sStatB).toString().trim();
          String sStatH = "";
          sStatH = getPKbyFieldCode(sStatHCode);
          srpStatbRefPane.setStathid(sStatH);
        }
        UIRefCellEditor editorb = new UIRefCellEditor((UIRefPane)srpStatbRefPane);

        getUITabInput().getColumnModel().getColumn(iCol).setCellEditor(editorb);
      }
      else if (qcvo.getDataType().intValue() == 400) {
        UIRefPane lqrLotQueryRef = null;
        try {
          lqrLotQueryRef = (UIRefPane)Class.forName("nc.ui.ic.pub.lotquery.LotQueryRef").newInstance();
        }
        catch (Exception e) {
          System.out.println(e);
        }

        String sLot = qcvo.getFieldCode().trim();
        Object sLotValue = getUITabInput().getValueAt(iRow, iCol);
        if (this.m_htLotInv.containsKey(sLot)) {
          String sInvCode = this.m_htLotInv.get(sLot).toString().trim();
          String sInv = "";
          sInv = getPKbyFieldCode(sInvCode);
          String[] sInput = { sInv };
          ((ILotQueryRef)lqrLotQueryRef).setParams(sInput);
          lqrLotQueryRef.setText((String)sLotValue);
          String sPK = "";
          for (int j = 0; j < getUITabInput().getRowCount(); j++) {
            int indexNow = getIndexes(j - getImmobilityRows());
            QueryConditionVO qcvoNow = getConditionDatas()[indexNow];
            if (qcvoNow.getFieldCode().trim().equals("pk_corp")) {
              sPK = getValueRefResults()[j] == null ? "" : getValueRefResults()[j].getRefPK().trim();

              lqrLotQueryRef.setWhereString(" bd_invbasdoc.discountflag='N' and bd_invbasdoc.laborflag='N' and bd_invmandoc.wholemanaflag='Y' and bd_invmandoc.sealflag ='N' and bd_invmandoc.pk_corp='" + sPK + "'");

              break;
            }
          }
        }
        UIRefCellEditor editor = new UIRefCellEditor(lqrLotQueryRef);
        getUITabInput().getColumnModel().getColumn(iCol).setCellEditor(editor);

        getUITabInput().setValueAt(sLotValue, iRow, iCol);
      } else if ((qcvo.getDataType().intValue() == 5) && 
        (this.m_htAstCorpInv.containsKey(sFieldCode)))
      {
        String[] sCorpInv = (String[])(String[])this.m_htAstCorpInv.get(sFieldCode);
        String sCorp = sCorpInv[0];
        String sCorpID = "";
        String sInv = sCorpInv[1];
        String sInvID = "";
        sCorpID = getPKbyFieldCode(sCorp);
        sInvID = getPKbyFieldCode(sInv);
        UIRefPane refCast = new UIRefPane();
        refCast.setRefNodeName("计量档案");
        refCast.getRefModel().setSealedDataShow(true);
        this.m_voInvMeas.filterMeas(sCorpID, sInvID, refCast);
        UIRefCellEditor editor = new UIRefCellEditor(refCast);
        getUITabInput().getColumnModel().getColumn(iCol).setCellEditor(editor);
      }
    }

    boolean bFounded = false;
    boolean bActualize = false;
    Object Ref = null;
    for (int i = 0; i < this.m_alComboxToRef.size(); i++) {
      alRefRow = (ArrayList)(ArrayList)this.m_alComboxToRef.get(i);
      if (!alRefRow.get(1).toString().trim().equals(qcvo.getFieldCode().trim()))
        continue;
      String sSourceFieldCode = alRefRow.get(0).toString().trim();
      for (int j = 0; j < getConditionDatas().length; j++) {
        if (!getConditionDatas()[j].getFieldCode().trim().equals(sSourceFieldCode))
          continue;
        bFounded = true;
        Object oValue = getTabModelInput().getValueAt(getListRow(j), 4);

        Object[][] oRef = (Object[][])(Object[][])alRefRow.get(2);
        for (int k = 0; k < oRef.length; k++) {
          if (oRef[k][0].equals(oValue)) {
            Ref = oRef[k][1];
            if ((Ref instanceof UIRefCellEditor)) {
              getUITabInput().getColumnModel().getColumn(iCol).setCellEditor((UIRefCellEditor)Ref);
            }
            else if ((Ref instanceof JCheckBox)) {
              getUITabInput().getColumnModel().getColumn(iCol).setCellEditor(new DefaultCellEditor((JCheckBox)Ref));
            }
            else if ((Ref instanceof JComboBox)) {
              getUITabInput().getColumnModel().getColumn(iCol).setCellEditor(new DefaultCellEditor((JComboBox)Ref));
            }
            else if ((Ref instanceof JTextField)) {
              getUITabInput().getColumnModel().getColumn(iCol).setCellEditor(new DefaultCellEditor((JTextField)Ref));
            }
            else if ((Ref instanceof RefCellRender)) {
              getUITabInput().getColumnModel().getColumn(iCol).setCellEditor(new UIRefCellEditor((RefCellRender)Ref));
            }
            else if ((Ref instanceof UIRefPane)) {
              getUITabInput().getColumnModel().getColumn(iCol).setCellEditor(new UIRefCellEditor((UIRefPane)Ref));
            }
            else
            {
              getUITabInput().getColumnModel().getColumn(iCol).setCellEditor(null);
            }

            bActualize = true;
            break;
          }
        }
        break;
      }

    }

    if ((bFounded) && (!bActualize))
      getUITabInput().getColumnModel().getColumn(iCol).setCellEditor(null);
  }

  public void showBillDefCondition(String sHeadDefAliasName, String sBodyDefAliasName)
  {
    Hashtable htbInvDef = new Hashtable();
    htbInvDef.put("供应链/ARAP单据头", sHeadDefAliasName);
    htbInvDef.put("供应链/ARAP单据体", sBodyDefAliasName);
    showDefCondition(htbInvDef);
  }

  public void showDefCondition(String sDefInvBasAliasName, String sDefInvManAliasName)
  {
    Hashtable htbInvDef = new Hashtable();
    htbInvDef.put("存货档案", sDefInvBasAliasName);
    htbInvDef.put("存货管理档案", sDefInvManAliasName);
    showDefCondition(htbInvDef);
  }

  public void showDefCondition(Hashtable htbDocumentRef)
  {
    UIRefPane[] paneDefs = null;
    try {
      StringBuffer sbSQL = new StringBuffer(" ");

      StringBuffer sbSQLDefault = new StringBuffer(" select ");
      sbSQLDefault.append(" a.fieldname,c.defname,b.objname,c.type,c.lengthnum,c.digitnum \n");

      sbSQLDefault.append(" from bd_defquote a,bd_defused b,bd_defdef c \n");

      sbSQLDefault.append(" where ");
      sbSQLDefault.append(" a.pk_defused = b.pk_defused \n");
      sbSQLDefault.append(" and ");
      sbSQLDefault.append(" c.pk_defdef = a.pk_defdef ");

      sbSQL.append(sbSQLDefault);
      sbSQL.append(" and (");
      Enumeration en = htbDocumentRef.keys();
      int n = 0;
      while (en.hasMoreElements()) {
        if (n > 0)
          sbSQL.append(" or ");
        sbSQL.append(" b.objname = '" + en.nextElement().toString() + "' \n");

        n++;
      }
      sbSQL.append(" ) ");
      sbSQL.append(" order by b.objname,a.fieldname ");

      String[][] sResult = CommonDataHelper.queryData(sbSQL.toString());

      Hashtable htDef = new Hashtable();
      Vector vAddDef = new Vector();
      if (sResult != null) {
        paneDefs = new UIRefPane[sResult.length];
        for (int i = 0; i < sResult.length; i++) {
          String[] sTemp = sResult[i];
          if (sTemp.length <= 1)
            continue;
          String sFieldName = sTemp[0];
          String sDefName = sTemp[1];
          String sObjName = sTemp[2];
          String sDefType = sTemp[3];
          String sDigitNum = sTemp[4];
          int iLengthNum = 20;
          int iNum = 0;
          if ((sTemp[4] != null) && (sTemp[4].length() != 0)) {
            iLengthNum = new Integer(sDigitNum).intValue();
          }

          if ((sTemp[5] != null) && (sTemp[5].length() != 0)) {
            iNum = new Integer(sTemp[5]).intValue();
          }

          String sFieldCode = "";
          String sAlaisTable = null;

          if ((sDefName != null) && (sDefName.length() > 0)) {
            sAlaisTable = (String)htbDocumentRef.get(sObjName);
            sDefName = sObjName + "-" + sDefName;
            if ((sAlaisTable != null) && (sAlaisTable.length() >= 0))
            {
              if ((sObjName.equalsIgnoreCase("存货档案")) || (sObjName.equalsIgnoreCase("存货管理档案")))
              {
                sFieldCode = htbDocumentRef.get(sObjName) + "." + sFieldName;
              }
              else if (sObjName.equalsIgnoreCase("供应链/ARAP单据头"))
              {
                sFieldCode = "vuser" + sFieldName + htbDocumentRef.get(sObjName);
              }
              else if (sObjName.equalsIgnoreCase("供应链/ARAP单据体"))
              {
                sFieldCode = "vuser" + sFieldName;
              }
              else sFieldCode = sFieldName;
            }

            vAddDef.addElement(getDefaultQueryCndVO(sFieldCode, sDefName));

            if (sDefType.equals("统计"))
            {
              String sWhereString = " and ";
              sWhereString = sWhereString + " pk_defdef = ";
              sWhereString = sWhereString + "(";
              sWhereString = sWhereString + " select ";
              sWhereString = sWhereString + " a.pk_defdef ";
              sWhereString = sWhereString + " from ";
              sWhereString = sWhereString + " bd_defquote a,bd_defused b ";

              sWhereString = sWhereString + " where ";
              sWhereString = sWhereString + " a.pk_defused = b.pk_defused ";

              sWhereString = sWhereString + " and ";
              sWhereString = sWhereString + " b.objname = '" + sObjName + "'";

              sWhereString = sWhereString + " and ";
              sWhereString = sWhereString + " a.fieldname = '" + sFieldName + "'";

              sWhereString = sWhereString + " ) ";
              paneDefs[i] = new UIRefPane();
              paneDefs[i].setRefNodeName("自定义项档案");
              paneDefs[i].getRef().getRefModel().addWherePart(sWhereString);
            }
            else if (sDefType.equals("日期")) {
              paneDefs[i] = new UIRefPane();
              paneDefs[i].setRefNodeName("日历");
            } else if (sDefType.equals("备注")) {
              paneDefs[i] = new UIRefPane();
              paneDefs[i].setMaxLength(iLengthNum);
              paneDefs[i].setButtonVisible(false);
            } else if (sDefType.equals("数字")) {
              paneDefs[i] = new UIRefPane();
              paneDefs[i].setTextType("TextDbl");
              paneDefs[i].setButtonVisible(false);
              paneDefs[i].setMaxLength(iLengthNum);
              paneDefs[i].setNumPoint(iNum);
            }
            htDef.put(sFieldCode, paneDefs[i]);
          }
        }

      }

      QueryConditionVO[] vos = getConditionDatas();
      Vector vTemp = new Vector();
      for (int i = 0; i < vos.length; i++) {
        String sCode = vos[i].getFieldCode();
        if (sCode.indexOf("def") < 0) {
          vTemp.addElement(vos[i]);
        }
        else
        {
          vTemp.addElement(vos[i]);
        }
      }
      for (int j = 0; j < vAddDef.size(); j++) {
        vTemp.addElement((QueryConditionVO)vAddDef.elementAt(j));
      }
      QueryConditionVO[] vosResult = new QueryConditionVO[vTemp.size()];
      vTemp.copyInto(vosResult);

      setConditionDatas(vosResult);

      Enumeration e = htDef.keys();
      while (e.hasMoreElements()) {
        String sKey = (String)e.nextElement();
        setValueRef(sKey, (UIRefPane)htDef.get(sKey));
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void showInvDefCondition(String sDefInvBasAliasName, String sDefInvManAliasName)
  {
    Hashtable htbInvDef = new Hashtable();
    htbInvDef.put("存货档案", sDefInvBasAliasName);
    htbInvDef.put("存货管理档案", sDefInvManAliasName);
    showDefCondition(htbInvDef);
  }

  public void setShowPrintStatusPanel(boolean isShowPrintStatusPanel)
  {
    if (isShowPrintStatusPanel) {
      getUITabbedPane().insertTab(getUIPanelPrintStatus().getName(), null, getUIPanelPrintStatus(), null, getUITabbedPane().getTabCount());
    }
    else if (this.m_isShowPrintStatusPanel) {
      getUITabbedPane().removeTabAt(getUITabbedPane().getTabCount() - 1);
    }

    this.m_isShowPrintStatusPanel = isShowPrintStatusPanel;
  }

  private boolean isShowPrintStatusPanel()
  {
    return this.m_isShowPrintStatusPanel;
  }

  private QryPrintStatusPanel getUIPanelPrintStatus()
  {
    if (this.m_QryPrintStatusPanel == null) {
      try {
        this.m_QryPrintStatusPanel = new QryPrintStatusPanel();
        this.m_QryPrintStatusPanel.setName(NCLangRes.getInstance().getStrByID("common", "UC000-0001993"));

        this.m_QryPrintStatusPanel.setLayout(null);

        this.m_QryPrintStatusPanel.setVisible(false);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.m_QryPrintStatusPanel;
  }

  public int getPrintStatus()
  {
    return getUIPanelPrintStatus().getStatus();
  }

  public String getPrintResult(String sTableAlias)
  {
    int iSelRslt = getPrintStatus();

    if (iSelRslt == -1) {
      return null;
    }

    String sResSql = "";

    if (sTableAlias == null) {
      sTableAlias = "";
    }
    else {
      sTableAlias = sTableAlias.trim() + ".";
    }

    if (iSelRslt == 1) {
      sResSql = sResSql + sTableAlias + "iprintcount" + ">0 ";
    }
    else if (iSelRslt == 0) {
      sResSql = sResSql + " (" + sTableAlias + "iprintcount" + "<=0 OR " + sTableAlias + "iprintcount" + " IS NULL ) ";
    }

    return sResSql;
  }

  private ConditionVO getPrintStatusCondVO()
  {
    if (!isShowPrintStatusPanel()) {
      return null;
    }
    int iStatus = getUIPanelPrintStatus().getStatus();

    if ((iStatus != 1) && (iStatus != 0))
    {
      System.out.println("PRINTCOUNT IGNORED");
      return null;
    }

    ConditionVO voCond = new ConditionVO();
    voCond.setFieldCode("iprintcount");
    voCond.setDataType(1);
    if (iStatus == 1) {
      voCond.setOperaCode(">");
      voCond.setValue("0");
      System.out.println("PRINTCOUNT>0");
    } else {
      voCond.setFieldCode("ISNULL(iprintcount,0)");

      voCond.setOperaCode("=");
      voCond.setValue("0");
      System.out.println("PRINTCOUNT=0");
    }
    return voCond;
  }

  public boolean isDataPowerSqlReturned()
  {
    return this.m_isDataPowerSqlReturned;
  }

  public boolean isExistFieldCode(String strFieldCode)
  {
    if ((strFieldCode == null) || (strFieldCode.trim().length() == 0)) {
      return false;
    }
    QueryConditionVO[] voaAllTempateDatas = getAllTempletDatas();
    int iLen = voaAllTempateDatas.length;
    for (int i = 0; i < iLen; i++) {
      if ((voaAllTempateDatas[i] == null) || (voaAllTempateDatas[i].getFieldCode() == null)) {
        continue;
      }
      if (strFieldCode.trim().equalsIgnoreCase(voaAllTempateDatas[i].getFieldCode().trim())) {
        return true;
      }
    }
    return false;
  }

  public boolean isUsedDataPower(String dpTableName, String sRefName, String pk_corp)
  {
    if ((dpTableName == null) || (sRefName == null) || (pk_corp == null))
    {
      return false;
    }
    String strKey = dpTableName + sRefName + pk_corp;
    if (m_mapPowerEnableCache.containsKey(strKey)) {
      return ((UFBoolean)m_mapPowerEnableCache.get(strKey)).booleanValue();
    }
    boolean bEnable = false;
    try
    {
      bEnable = DataPowerServ.isUsedDataPower(dpTableName, sRefName, pk_corp);
    } catch (Exception e) {
      SCMEnv.out(e);
      return false;
    }

    m_mapPowerEnableCache.put(strKey, new UFBoolean(bEnable));

    return bEnable;
  }

  public String getPowerSubSql(String dpTableName, String sRefName, String sCurUser, String[] pk_corps)
  {
    if ((dpTableName == null) || (sRefName == null) || (sCurUser == null) || (pk_corps == null) || (pk_corps.length == 0))
    {
      return null;
    }
    String strCorps = pk_corps[0];
    for (int i = 1; i < pk_corps.length; i++) {
      strCorps = strCorps + pk_corps[i];
    }
    String strKey = dpTableName + sRefName + sCurUser + strCorps;
    if (m_mapPowerSubSqlCache.containsKey(strKey)) {
      return PuPubVO.getString_TrimZeroLenAsNull(m_mapPowerSubSqlCache.get(strKey));
    }
    DataPowerServ DPServ = DataPowerServ.getInstance();

    String strPowerSubSql = DPServ.getSubSqlForMutilCorp(dpTableName, sRefName, sCurUser, pk_corps);

    m_mapPowerSubSqlCache.put(strKey, strPowerSubSql == null ? "" : strPowerSubSql);

    return strPowerSubSql;
  }

  public void setRefsDataPowerConVOs(String sCurUser, String[] pk_corps, String[] sRefNames, String[] sRefSqlFieldCodes, int[] iReturntypes)
  {
    try
    {
      ConditionVO[] cVos = null;
      if ((pk_corps != null) && (pk_corps.length > 0) && (sRefNames != null) && (sRefNames.length > 0) && (sRefSqlFieldCodes != null) && (sRefSqlFieldCodes.length > 0) && (iReturntypes != null) && (iReturntypes.length > 0) && (sRefNames.length == sRefSqlFieldCodes.length) && (sRefSqlFieldCodes.length == iReturntypes.length))
      {
        Hashtable htCtrRefDataPower = new Hashtable();
        QueryConditionVO[] voaConData = getConditionDatas();
        for (int i = 0; i < voaConData.length; i++)
        {
          if ((voaConData[i].getIfDataPower() == null) || (!voaConData[i].getIfDataPower().booleanValue()))
          {
            continue;
          }
          htCtrRefDataPower.put(voaConData[i].getFieldCode(), voaConData[i]);
        }

        if (htCtrRefDataPower.size() > 0) {
          Vector vecVO = new Vector();
          for (int i = 0; i < sRefNames.length; i++)
          {
            if (!isExistFieldCode(sRefSqlFieldCodes[i])) {
              continue;
            }
            if (htCtrRefDataPower.get(sRefSqlFieldCodes[i]) != null) {
              String dpTableName = DataPowerServ.getRefTableName(sRefNames[i]);

              if (!isUsedDataPower(dpTableName, sRefNames[i], pk_corps[0])) {
                continue;
              }
              String strPowerSubSql = getPowerSubSql(dpTableName, sRefNames[i], sCurUser, pk_corps);
              if (strPowerSubSql == null)
              {
                continue;
              }
              String sField = getRefField(sRefNames[i], iReturntypes[i]);
              if (sField == null)
              {
                continue;
              }

              QueryConditionVO cvo = (QueryConditionVO)htCtrRefDataPower.get(sRefSqlFieldCodes[i]);
              RefResultVO ref = new RefResultVO();
              ref.setRefCode(sRefNames[i]);
              ref.setRefName(sRefNames[i]);

              ConditionVO vo1 = new ConditionVO();
              vo1.setFieldCode(sRefSqlFieldCodes[i]);
              vo1.setDataType(1);
              vo1.setOperaCode(" is ");
              vo1.setValue(" NULL ");
              vo1.setNoLeft(false);

              vo1.setTableCode(cvo.getTableCode());
              vo1.setTableName(cvo.getTableName());
              vo1.setRefResult(ref);

              ConditionVO vo = new ConditionVO();
              vo.setFieldCode(sRefSqlFieldCodes[i]);
              vo.setDataType(1);
              vo.setOperaCode("in");

              vo.setTableCode(cvo.getTableCode());
              vo.setTableName(cvo.getTableName());
              vo.setRefResult(ref);
              if (iReturntypes[i] == 2) {
                vo.setValue(strPowerSubSql);
              } else {
                StringBuffer subSql = new StringBuffer(" (select ");
                subSql.append(sField).append(getRefField(sRefNames[i], -1)).append(" and " + getRefField(sRefNames[i], 2)).append(" in ").append(strPowerSubSql).append(")");
                vo.setValue(subSql.toString());
              }
              vo.setNoRight(false);
              vo.setLogic(false);
              vecVO.addElement(vo1);
              vecVO.addElement(vo);
            }
          }
          if ((vecVO != null) && (vecVO.size() > 0)) {
            cVos = new ConditionVO[vecVO.size()];
            vecVO.toArray(cVos);
          }
        }
      }

      this.m_cCtrTmpDataPowerVOs = cVos;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      this.m_cCtrTmpDataPowerVOs = null;
    }
  }

  public void setCtrTmpDataPowerVOs(ConditionVO[] cCtrTmpDataPowerVOs) {
    this.m_cCtrTmpDataPowerVOs = cCtrTmpDataPowerVOs;
  }

  public ConditionVO[] getCtrTmpDataPowerVOs() {
    return this.m_cCtrTmpDataPowerVOs;
  }

  private ConditionVO[] setCVO4MulCoPower(ConditionVO[] voaCond)
  {
    if ((this.m_cCtrTmpDataPowerVOs != null) && (this.m_cCtrTmpDataPowerVOs.length > 0))
    {
      Vector v = new Vector();
      for (int i = 0; i < voaCond.length; i++) {
        ConditionVO newcon = voaCond[i];
        if (newcon != null) {
          String strtemp = newcon.getValue();
          if ((strtemp == null) || (strtemp.toLowerCase().startsWith("(select distinct power.resource_data_id")))
          {
            continue;
          }
          v.add(newcon);
        }
      }

      for (int i = 0; i < this.m_cCtrTmpDataPowerVOs.length; i++) {
        v.add(this.m_cCtrTmpDataPowerVOs[i]);
      }
      ConditionVO[] newCondition = new ConditionVO[v.size()];
      v.copyInto(newCondition);
      return newCondition;
    }
    return voaCond;
  }

  public ConditionVO[] getDataPowerConVOs(String corpValue, String[] refcodes)
  {
    if (corpValue == null) {
      return null;
    }
    if ((refcodes == null) && 
      (this.m_alpower != null) && (this.m_alpower.size() > 0)) {
      refcodes = new String[this.m_alpower.size()];
      this.m_alpower.toArray(refcodes);
    }

    if (refcodes == null) {
      return null;
    }
    HashMap hmRef = new HashMap();

    for (int i = 0; i < refcodes.length; i++) {
      hmRef.put(refcodes[i], refcodes[i]);
    }

    HashMap hmPoweredConditionVO = new HashMap();

    String strDef = null;

    for (int i = 0; i < getConditionDatas().length; i++) {
      QueryConditionVO vo = getConditionDatas()[i];
      if (!hmRef.containsKey(vo.getFieldCode()))
        continue;
      if (isDataPwr(vo)) {
        hmPoweredConditionVO.put(getFldCodeByPower(vo), vo);
      }
    }

    Vector vecVO = new Vector();
    for (int i = 0; i < refcodes.length; i++) {
      String fieldcode = refcodes[i];
      QueryConditionVO voQuery = (QueryConditionVO)hmPoweredConditionVO.get(fieldcode);

      if (voQuery == null) {
        System.out.println("@@@@没有QueryConditionVO::" + fieldcode);
      }
      else
      {
        appendPowerCons(vecVO, voQuery, new String[] { corpValue });
      }

    }

    if (vecVO.size() > 0) {
      ConditionVO[] voRets = new ConditionVO[vecVO.size()];
      vecVO.copyInto(voRets);
      return voRets;
    }

    return null;
  }

  private void appendPowerCons(Vector vecVO, QueryConditionVO voQuery, String[] corpValues)
  {
    String insql = getMultiCorpsPowerSql(voQuery, corpValues);

    if (insql == null) {
      return;
    }

    if (insql.indexOf("(select distinct power.resource_data_id") < 0) {
      return;
    }

    String fieldcode = voQuery.getFieldCode();

    ConditionVO vo1 = new ConditionVO();
    vo1.setFieldCode(fieldcode);
    vo1.setDataType(1);
    vo1.setOperaCode(" is ");
    vo1.setValue(" NULL ");
    vo1.setNoLeft(false);
    vecVO.addElement(vo1);

    ConditionVO vo = new ConditionVO();
    vo.setFieldCode(fieldcode);
    vo.setDataType(1);
    vo.setOperaCode("in");
    if (voQuery.getReturnType().intValue() == 2) {
      vo.setValue(insql);
    } else {
      String sRefNodeName = voQuery.getConsultCode();
      StringBuffer subSql = new StringBuffer(" (select ");
      String sField = getRefField(sRefNodeName, voQuery.getReturnType().intValue());

      subSql.append(sField).append(getRefField(sRefNodeName, -1)).append(" and " + getRefField(sRefNodeName, 2)).append(" in ").append(insql).append(")");

      vo.setValue(subSql.toString());
    }

    vo.setNoRight(false);
    vo.setLogic(false);

    vecVO.addElement(vo);
  }

  private String getFldCodeByPower(QueryConditionVO vo)
  {
    String strDef = vo.getFieldCode();

    if (this.m_alpower.contains(strDef)) {
      return strDef;
    }

    strDef = vo.getTableName() + "." + vo.getTableCode();
    if (this.m_alpower.contains(strDef)) {
      return strDef;
    }

    return null;
  }

  private String getMultiCorpsPowerSql(QueryConditionVO voQuery, String[] corps) {
    DataPowerServ DPServ = DataPowerServ.getInstance();

    String sCurUser = getClientEnvironment().getUser().getPrimaryKey();
    String sRefNodeName = voQuery.getConsultCode();

    String dpTableName = null;
    try {
      dpTableName = DataPowerServ.getRefTableName(sRefNodeName);
    }
    catch (Exception e)
    {
    }

    if (dpTableName == null) {
      return null;
    }
    ArrayList alnewCorp = new ArrayList();
    if (corps != null)
    {
      boolean ispower = false;
      for (int m = 0; m < corps.length; m++) {
        if (isUsedDataPower(dpTableName, sRefNodeName, corps[m])) {
          ispower = true;
          alnewCorp.add(corps[m]);
        }

      }

      if (!ispower) {
        return null;
      }
    }

    String insql = DPServ.getSubSqlForMutilCorp(dpTableName, sRefNodeName, sCurUser, corps);

    return insql;
  }

  private boolean isDataPwr(QueryConditionVO vo)
  {
    return (vo.getIfUsed().booleanValue()) && ((isDataPower()) || (vo.getIfDataPower().booleanValue())) && (vo.getDataType().intValue() == 5) && (vo.getConsultCode() != null) && (!vo.getConsultCode().trim().equals("-99")) && (!vo.getConsultCode().startsWith("<")) && (!vo.getConsultCode().startsWith("{"));
  }
}