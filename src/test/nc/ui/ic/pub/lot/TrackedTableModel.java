package nc.ui.ic.pub.lot;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.table.ITableModel;
import nc.vo.ic.pub.lang.ResBase;
import nc.vo.ic.pub.lot.LotNumbRefVO;
import nc.vo.scm.pub.sort.SortMethod;

public class TrackedTableModel extends AbstractTableModel
  implements ITableModel
{
  private LotNumbRefVO[] m_voaAllData = null;

  private int m_iLastSortType = 1;

  public LotNumbRefVO[] getAllData()
  {
    return this.m_voaAllData;
  }
  public int getColumnCount() {
//    return 12;
    return 14;//edit by zwx 2014-12-26 
  }
  public String getColumnName(int col) {
    switch (col) {
    case 0:
      return NCLangRes.getInstance().getStrByID("40083010a", "UPT40083010a-000001");
    case 1:
      return NCLangRes.getInstance().getStrByID("common", "UC000-0002060");
    case 2:
      return ResBase.getQCLevel();
    case 3:
      return NCLangRes.getInstance().getStrByID("common", "UC000-0001402");
    case 4:
      return NCLangRes.getInstance().getStrByID("common", "UC000-0000794");
    case 5:
      return NCLangRes.getInstance().getStrByID("common", "UC000-0000807");
    case 6:
      return NCLangRes.getInstance().getStrByID("common", "UC000-0002282");
    case 7:
      return NCLangRes.getInstance().getStrByID("common", "UC000-0003975");
    case 8:
      return NCLangRes.getInstance().getStrByID("common", "UC000-0003971");
    case 9:
      return NCLangRes.getInstance().getStrByID("common", "UC000-0003327");
    case 10:
      return NCLangRes.getInstance().getStrByID("common", "UC000-0002161");
    case 11:
      return ResBase.getMaoZhong();
    case 12://add
        return "货位ID";
    case 13://add by zwx 2014-12-26 
    	return "货位名称";
    }
    return null;
  }

  public int getRowCount() {
    if (this.m_voaAllData != null) {
      return this.m_voaAllData.length;
    }
    return 0;
  }

  public Object getValueAt(int row, int col)
  {
    LotNumbRefVO vo = this.m_voaAllData[row];
    if (vo != null) {
      switch (col) { case 0:
        return vo.getOnhandnumType().intValue() == 0 ? NCLangRes.getInstance().getStrByID("4008ui", "UPP4008ui-000024") : NCLangRes.getInstance().getStrByID("4008ui", "UPP4008ui-000025");
      case 1:
        return vo.getVbatchcode() == null ? "" : vo.getVbatchcode();
      case 2:
        return vo.getCqualitylevelid() == null ? "" : vo.getCqualitylevelid();
      case 3:
        return vo.getDvalidate() == null ? "" : vo.getDvalidate().toString();
      case 4:
        return vo.getVbillcode() == null ? "" : vo.getVbillcode();
      case 5:
        return vo.getCorpid() == null ? "" : vo.getCorpid();
      case 6:
        return vo.getNinnum() == null ? "" : vo.getNinnum().toString();
      case 7:
        return vo.getCastunitname() == null ? "" : vo.getCastunitname();
      case 8:
        return vo.getNinassistnum() == null ? "" : vo.getNinassistnum().toString();
      case 9:
        return vo.getFreeVO() == null ? "" : vo.getFreeVO().getVfree0();
      case 10:
        return vo.getHsl() == null ? "" : vo.getHsl().toString();
      case 11:
        return vo.getNgrossnum() == null ? "" : vo.getNgrossnum().toString();
      case 12://add
          return vo.getCspaceid() == null ? "" : vo.getCspaceid().toString();
      case 13://add by zwx
          return vo.getCsname() == null ? "" : vo.getCsname().toString();
      }
      return null;
    }

    return "";
  }

  public boolean isCellEditable(int row, int col)
  {
    return false;
  }

  public void setAllData(LotNumbRefVO[] newAllData)
  {
    this.m_voaAllData = newAllData;
  }

  public void sortByColumn(int column, boolean ascending)
  {
    if (this.m_iLastSortType == 1)
      this.m_iLastSortType = 2;
    else
      this.m_iLastSortType = 1;
    ArrayList alSortedData = SortMethod.sortByKeys(new String[] { getColumnCode(column) }, new int[] { this.m_iLastSortType }, this.m_voaAllData);

    this.m_voaAllData = ((LotNumbRefVO[])(LotNumbRefVO[])alSortedData.get(1));
  }

  public String getColumnCode(int col)
  {
    switch (col) {
    case 0:
      return "ionhandnumtype";
    case 1:
      return "vbatchcode";
    case 2:
      return "cqualitylevelid";
    case 3:
      return "dvalidate";
    case 4:
      return "vbillcode";
    case 5:
      return "corpid";
    case 6:
      return "ninnum";
    case 7:
      return "castunitname";
    case 8:
      return "ninassistnum";
    case 9:
      return "vfree0";
    case 10:
      return "hsl";
    case 11:
      return "ngrossnum";
    case 12://add
        return "cspaceid";
    case 13://add by zwx
        return "csname";
    }
    return null;
  }
}