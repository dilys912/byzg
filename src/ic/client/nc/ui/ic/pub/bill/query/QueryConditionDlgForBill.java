package nc.ui.ic.pub.bill.query;

import java.awt.Container;
import java.awt.Frame;
import nc.ui.bd.datapower.DataPowerServ;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.UFRefManage;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.query.EditComponentFacotry;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.QueryConditionVO;
import nc.vo.scm.pub.SCMEnv;

public class QueryConditionDlgForBill extends QueryConditionDlg
{
  private int iInOutFlag = 1;

  public QueryConditionDlgForBill()
  {
  }

  public QueryConditionDlgForBill(Container parent)
  {
    super(parent);
  }

  protected void addNullContionDataPower(String refTableName, String sFieldCode, String pk_Corp)
  {
    try
    {
      String dpTableName = DataPowerServ.getRefTableName(refTableName);
      if (DataPowerServ.isUsedDataPower(dpTableName, refTableName, pk_Corp))
      {
        String insql = DataPowerServ.getSubSql(dpTableName, refTableName, pk_Corp);

        ConditionVO[] cvos = getConditionVOsByFieldCode(sFieldCode);
        if (cvos != null)
        {
          for (int i = 0; i < cvos.length; i++) {
            cvos[i].setOperaCode(null);
            cvos[i].setValue(null);

            cvos[i].setFieldCode(sFieldCode + " is null or " + sFieldCode);

            cvos[i].setOperaCode("in");
            cvos[i].setValue("(" + insql + ")");
          }
        }
      }

    }
    catch (Exception e)
    {
      SCMEnv.error(e);
    }
  }

  public QueryConditionDlgForBill(Container parent, String title)
  {
    super(parent, title);
  }

  public QueryConditionDlgForBill(Frame parent)
  {
    super(parent);
  }

  public QueryConditionDlgForBill(Frame parent, String title)
  {
    super(parent, title);
  }

  private UIRefPane getRefPaneByCode(String sFieldCode) {
    QueryConditionVO[] voaConData = getConditionDatas();

    if ((voaConData == null) || (voaConData.length == 0)) {
      return null;
    }

    UIRefPane ref = null;
    for (int i = 0; i < voaConData.length; i++) {
      if (sFieldCode.equals(voaConData[i].getFieldCode())) {
        if (voaConData[i].getDataType().intValue() != 5)
          break;
        EditComponentFacotry factry = new EditComponentFacotry(voaConData[i]);
        ref = (UIRefPane)factry.getEditComponent(null);

        break;
      }

    }

    return ref;
  }

  public int getInOutFlag() {
    return this.iInOutFlag;
  }

  public String getWhereSQL()
  {
    String sWhereClause = super.getWhereSQL();
    sWhereClause = (sWhereClause == null) || (sWhereClause.trim().length() == 0) ? "1=1" : sWhereClause;

    if ((getCorpFieldCode().length() != 0) && (getDefaultCorpID().length() != 0))
    {
      sWhereClause = " (" + getCorpFieldCode() + "='" + getDefaultCorpID().trim() + "') and (" + sWhereClause + ")";
    }

    return sWhereClause;
  }

  public String getWhereSQL(ConditionVO[] cvos)
  {
    String sWhereClause = super.getWhereSQL(cvos);
    sWhereClause = (sWhereClause == null) || (sWhereClause.trim().length() == 0) ? "1=1" : sWhereClause;

    if ((getCorpFieldCode().length() != 0) && (getDefaultCorpID().length() != 0))
    {
      sWhereClause = " (" + getCorpFieldCode() + "='" + getDefaultCorpID().trim() + "') and (" + sWhereClause + ")";
    }

    return sWhereClause;
  }

  public void initQueryDlgRef()
  {
    super.initQueryDlgRef();
    setRefInitWhereClause("head.pk_calbody", "库存组织", " property in (0,1) and pk_corp=", "pk_corp");

    setRefInitWhereClause("head.cwastewarehouseid", "仓库档案", "gubflag='Y'   and pk_corp=", "pk_corp");
  }

  public void setInOutFlag(int newInOutFlag)
  {
    this.iInOutFlag = newInOutFlag;
  }

  protected void setValueRefEditor(int iRow, int iCol)
  {
    int index = getIndexes(iRow - getImmobilityRows());

    if ((index < 0) || (index >= getConditionDatas().length)) {
      SCMEnv.out("qry cond err.");
      return;
    }

    QueryConditionVO qcvo = getConditionDatas()[index];

    if (qcvo.getFieldCode().trim().equals("head.cbiztype")) {
      UIRefPane uirp = new UIRefPane();
      uirp.setRefNodeName("业务类型");

      changeValueRef("head.cbiztype", uirp);
    }

    if (qcvo.getFieldCode().trim().equals("body.ccostobject")) {
      UIRefPane uirp = new UIRefPane();
      uirp.setRefNodeName("存货档案");
      uirp.setReturnCode(false);
      uirp.setStrPatch(" DISTINCT ");
      AbstractRefModel refmodel = uirp.getRef().getRefModel();

      String sTableName = refmodel.getTableName();

      if ((sTableName != null) && (sTableName.indexOf("bd_produce") < 0)) {
        sTableName = sTableName + ",bd_produce";
        refmodel.setTableName(sTableName);
      }
      String sPK = getDefaultCorpID().trim();
      String sOldBomCondition = refmodel.getWherePart();
      sOldBomCondition = sOldBomCondition + " and bd_produce.pk_invmandoc = bd_invmandoc.pk_invmandoc and bd_produce.sfcbdx = 'Y' and bd_invmandoc.pk_corp = '" + sPK + "' ";

      refmodel.setWherePart(sOldBomCondition);

      changeValueRef("body.ccostobject", uirp);
    }

    super.setValueRefEditor(iRow, iCol);
  }
}