package nc.ui.scm.pub.print;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.print.PrintDirectEntry;
import nc.ui.pub.print.datastruct.CellRange;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.pub.report.ReportItem;
import nc.ui.scm.pub.simplecard.SimpleCard;
import nc.vo.bd.CorpVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.cquery.FldgroupVO;
import nc.vo.sm.UserVO;

public class SCMPrintDirect
{
  private static ArrayList getMultiHeadInfo(FldgroupVO[] voGroups)
    throws BusinessException
  {
    ArrayList alRes = new ArrayList();
    int tmprow = 1;

    if (voGroups == null) {
      alRes.add(new Integer(1));
      alRes.add(new Hashtable());
      return alRes;
    }

    Hashtable htGroup = new Hashtable();
    ArrayList alTop = new ArrayList();
    int rowcount = 1;
    boolean bIsTop = false;
    for (int i = 0; i < voGroups.length; i++) {
      String groupname = voGroups[i].getGroupname();
      Integer istart = null;
      Integer iend = null;
      int itype = Integer.parseInt(voGroups[i].getGrouptype());

      CellRange cell = null;
      CellRange cell1 = null;
      CellRange cell2 = null;
      String subGroup = null;
      switch (itype) {
      case 0:
        try {
          istart = new Integer(voGroups[i].getItem1());
          iend = new Integer(voGroups[i].getItem2());
        } catch (Exception e) {
          throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000555"));
        }
        if (iend.intValue() < istart.intValue()) {
          Integer tmp = new Integer(istart.intValue());
          istart = iend;
          iend = tmp;
        }

        tmprow = 1;
        cell1 = new CellRange(1, istart.intValue(), tmprow, istart.intValue());
        htGroup.put(istart.toString(), cell1);
        cell2 = new CellRange(1, iend.intValue(), tmprow, iend.intValue());
        htGroup.put(iend.toString(), cell2);

        tmprow = 2;
        if (rowcount < 2)
        {
          rowcount = 2;
        }
        cell = new CellRange(2, istart.intValue(), tmprow, iend.intValue());
        htGroup.put(groupname, cell);
        if ((voGroups[i].getToplevelflag() == null) || (!voGroups[i].getToplevelflag().equals("Y")))
          continue;
        alTop.add(groupname); break;
      case 1:
      case 2:
        try
        {
          istart = new Integer(voGroups[i].getItem1());
          subGroup = voGroups[i].getItem2();
        }
        catch (Exception e)
        {
          iend = new Integer(voGroups[i].getItem2());
          subGroup = voGroups[i].getItem1();
        }

        if (iend == null) {
          iend = istart;
        }

        cell1 = new CellRange(1, iend.intValue(), tmprow, iend.intValue());
        htGroup.put(iend.toString(), cell1);

        if (!groupname.equals(subGroup)) {
          tmprow++;
          if (tmprow > rowcount) {
            rowcount++;
          }
        }
        if (!htGroup.containsKey(groupname)) {
          if (htGroup.containsKey(subGroup)) {
            CellRange cellSub = (CellRange)htGroup.get(subGroup);
            int st2 = Math.min(cellSub.start_col, iend.intValue());
            int ed2 = Math.max(cellSub.end_col, iend.intValue());
            cell = new CellRange(cellSub.end_row, st2, tmprow, ed2);
          } else {
            cell = new CellRange(1, iend.intValue(), tmprow, iend.intValue());
          }

          htGroup.put(groupname, cell);
        } else {
          cell = (CellRange)htGroup.get(groupname);
          cell.start_col = Math.min(cell.start_col, iend.intValue());
          cell.end_col = Math.max(cell.end_col, iend.intValue());
        }

        if ((voGroups[i].getToplevelflag() == null) || (!voGroups[i].getToplevelflag().equals("Y")))
          continue;
        alTop.add(groupname); break;
      case 3:
        int st_row = 0;
        int ed_row = 0;
        int st_col = 0;
        int ed_col = 0;

        String subGrp1 = voGroups[i].getItem1();
        String subGrp2 = voGroups[i].getItem2();
        cell1 = (CellRange)htGroup.get(subGrp1);
        cell2 = (CellRange)htGroup.get(subGrp2);

        if ((!groupname.equals(subGrp1)) && (!groupname.equals(subGrp2))) {
          tmprow++;
          if (tmprow > rowcount)
            rowcount++;
          st_row = tmprow;
          ed_row = tmprow;
        }
        else {
          st_row = Math.max(cell1.start_row, cell2.start_row);
          ed_row = Math.max(cell1.end_row, cell2.end_row);
        }

        if (htGroup.containsKey(subGrp1))
        {
          st_col = Math.min(cell1.start_col, cell2.start_col);
          ed_col = Math.max(cell1.end_col, cell2.end_col);

          cell = new CellRange(st_row, st_col, ed_row, ed_col);
          htGroup.put(groupname, cell);
        }

        if ((voGroups[i].getToplevelflag() == null) || (!voGroups[i].getToplevelflag().equals("Y")))
          continue;
        alTop.add(groupname);
      }

    }

    alRes.add(new Integer(rowcount));
    alRes.add(htGroup);
    for (int i = 0; i < alTop.size(); i++) {
      String group = (String)alTop.get(i);
      if (htGroup.containsKey(group)) {
        CellRange celltop = (CellRange)htGroup.get(group);
        celltop.end_row = rowcount;
      }
    }

    return alRes;
  }

  private static String getTopStr(BillCardPanel report)
  {
    int colCount = report.getHeadItems() == null ? -1 : report.getHeadItems().length;

    int rowCount = report.getRowCount();
    Vector v = new Vector();

    Hashtable htSort = new Hashtable();
    for (int i = 0; i < colCount; i++) {
      BillItem ri = report.getHeadItems()[i];
      if (ri.isShow()) {
        int showorder = ri.getShowOrder();

        String[] shead = new String[2];
        shead[0] = ri.getName();
        Object o = null;
        if (ri.getDataType() == 5) {
          UIRefPane ref = (UIRefPane)ri.getComponent();
          if (ref.isReturnCode() == true)
            o = ref.getRefCode();
          else {
            o = ref.getRefName();
          }
        }
        else if (ri.getDataType() == 4) {
          if (ri.getDataType() == 4) {
            o = ri.getValue();
            if ((o != null) && ((o instanceof Boolean))) {
              Boolean value = (Boolean)o;
              if (value.booleanValue()) {
                o = "ÊÇ";
              }
              else {
                o = "·ñ";
              }
            }
          }
        }
        else if (ri.getDataType() == 6) {
          o = ri.getValue();
          if ((ri.getComponent() instanceof UIComboBox)) {
            UIComboBox combox = (UIComboBox)ri.getComponent();
            int index = combox.getSelectedIndex();
            o = combox.getItemNameAt(index);
          }
        }
        else {
          o = ri.getValue();
        }
        if (o != null)
          shead[1] = o.toString();
        v.addElement(shead);
      }
    }

    StringBuffer str = new StringBuffer();
    for (int i = 0; i < v.size(); i++) {
      String[] strs = (String[])(String[])v.get(i);
      str.append(strs[0] + " : ");
      if ((strs[1] == null) || (strs[1].trim().length() == 0))
        str.append("      ");
      else
        str.append(strs[1]);
      str.append("      ");
    }

    return str.toString();
  }

  private static String getTopStr(ReportBaseClass report)
  {
    int colCount = report.getHead_Items() == null ? -1 : report.getHead_Items().length;

    int rowCount = report.getRowCount();
    Vector v = new Vector();

    Hashtable htSort = new Hashtable();
    for (int i = 0; i < colCount; i++)
    {
      ReportItem ri = report.getHead_Items()[i];
      if (!ri.isShow())
        continue;
      int showorder = ri.getShowOrder();

      String[] shead = new String[2];
      shead[0] = ri.getName();

      Object o = null;
      if (ri.getDataType() == 5)
      {
        UIRefPane ref = (UIRefPane)ri.getComponent();
        if (ref.isReturnCode() == true)
        {
          o = ref.getRefCode();
        }
        else
        {
          o = ref.getRefName();
        }
      }
      else
      {
        o = ri.getValue();
      }

      if (o != null)
        shead[1] = o.toString();
      v.addElement(shead);
    }

    StringBuffer str = new StringBuffer();
    for (int i = 0; i < v.size(); i++)
    {
      String[] strs = (String[])(String[])v.get(i);
      str.append(strs[0] + " : ");
      if ((strs[1] == null) || (strs[1].trim().length() == 0))
        str.append("      ");
      else
        str.append(strs[1]);
      str.append("      ");
    }

    return str.toString();
  }

  private static String getTopStr(SimpleCard report)
  {
    int colCount = report.getItems() == null ? -1 : report.getItems().length;

    int Index = 0;
    for (int i = 0; i < colCount; i++)
    {
      if (report.getItems()[i].getIDColName().endsWith("-h"))
        Index++;
    }
    colCount = Index;
    Vector v = new Vector();

    Hashtable htSort = new Hashtable();
    for (int i = 0; i < colCount; i++)
    {
      BillItem ri = report.getItems()[i];
      if (!ri.getIDColName().endsWith("-h"))
        continue;
      if (!ri.isShow())
        continue;
      int showorder = ri.getShowOrder();

      String[] shead = new String[2];
      shead[0] = ri.getName();

      Object o = null;
      if (ri.getDataType() == 5)
      {
        UIRefPane ref = (UIRefPane)ri.getComponent();
        if (ref.isReturnCode() == true)
        {
          o = ref.getRefCode();
        }
        else
        {
          o = ref.getRefName();
        }
      }
      else
      {
        o = ri.getValue();
      }
      if (o != null)
        shead[1] = o.toString();
      v.addElement(shead);
    }

    StringBuffer str = new StringBuffer();
    for (int i = 0; i < v.size(); i++)
    {
      String[] strs = (String[])(String[])v.get(i);
      str.append(strs[0] + " : ");
      if ((strs[1] == null) || (strs[1].trim().length() == 0))
        str.append("      ");
      else
        str.append(strs[1]);
      str.append("      ");
    }

    return str.toString();
  }

  private static void modifyCol(String grpname, Hashtable htMem)
  {
    if (htMem.containsKey(grpname)) {
      CellRange cell = (CellRange)htMem.get(grpname);
      Integer istart = new Integer(cell.start_col);
      Integer iend = new Integer(cell.end_col);

      if (htMem.containsKey(istart.toString())) {
        CellRange cell1 = (CellRange)htMem.get(istart.toString());
        cell.start_col = cell1.start_col;
      }
      if (htMem.containsKey(iend.toString())) {
        CellRange cell2 = (CellRange)htMem.get(iend.toString());
        cell.end_col = cell2.end_col;
      }
    }
  }

  public static void preview(SimpleCard[] report, String title, boolean bIspreview)
    throws BusinessException
  {
    if ((report == null) || (report.length == 0))
      return;
    for (int i = 0; i < report.length; i++)
    {
      if (report[i].getItemValue("billscrollpane") == null)
      {
        throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000556"));
      }
      BillScrollPane bsp = (BillScrollPane)report[i].getItemValue("billscrollpane").getComponent();
      if (bsp.getTableModel().getBodyItems() == null) {
        return;
      }
      int colCount = bsp.getTableModel().getBodyItems().length;
      int rowCount = bsp.getTableModel().getRowCount();
      Vector vColKey = new Vector();

      BillItem item = null;
      for (int j = 0; j < colCount; j++)
      {
        item = bsp.getTableModel().getBodyItems()[j];
        if (item.isShow())
          vColKey.add(item.getKey());
      }
      colCount = vColKey.size();
      if (colCount <= 0) {
        return;
      }
      int[] index = new int[colCount];
      String[][] colname = (String[][])null;
      int[] colwidth = new int[colCount];
      int[] alignflag = new int[colCount];

      colname = new String[1][colCount];
      for (int j = 0; j < colCount; j++)
      {
        item = bsp.getTableModel().getBodyItems()[bsp.getTableModel().getBodyColByKey((String)vColKey.get(j))];
        colname[0][j] = item.getName();
        colwidth[j] = item.getWidth();

        if (item.getDataType() == 0)
          alignflag[j] = 0;
        else if ((item.getDataType() == 2) || (item.getDataType() == 1))
          alignflag[j] = 2;
        else {
          alignflag[j] = 1;
        }

      }

      Object[][] data = new Object[rowCount][colCount];

      for (int j = 0; j < rowCount; j++)
      {
        for (int k = 0; k < colCount; k++) {
          data[j][k] = bsp.getTableModel().getValueAt(j, (String)vColKey.get(k));
        }
      }

      Font font = new Font("dialog", 1, 18);
      Font font1 = new Font("dialog", 0, 12);
      String topstr = null;
      String botstr = null;
      try
      {
        topstr = NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000557") + ClientEnvironment.getInstance().getCorporation().getUnitname();
        botstr = NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000325") + ClientEnvironment.getInstance().getUser().getUserName() + NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000558") + ClientEnvironment.getInstance().getDate();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }

      PrintDirectEntry print = new PrintDirectEntry(report[i].getParent());

      print.setTitle(title);

      print.setTitleFont(font);

      print.setContentFont(font1);

      print.setTopStr(getTopStr(report[i]));

      print.setBottomStr(botstr);
      print.setPageNumDisp(true);
      print.setPageNumFont(font1);

      print.setPageNumAlign(2);

      print.setPageNumPos(2);
      print.setPageNumTotalDisp(true);

      print.setFixedRows(1);

      print.setColNames(colname);

      print.setData(data);

      print.setColWidth(colwidth);

      print.setAlignFlag(alignflag);

      if (bIspreview)
        print.preview();
    }
  }

  public static void preview(BillCardPanel report, String title)
    throws BusinessException
  {
    if (report.getRowCount() == 0) {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000556"));
    }

    if (report.getBodyItems() == null) {
      return;
    }
    int colCount = report.getBodyItems().length;
    int rowCount = report.getRowCount();
    Vector vColKey = new Vector();

    BillItem item = null;
    for (int i = 0; i < colCount; i++) {
      item = report.getBodyItems()[i];
      if (item.isShow())
        vColKey.add(item.getKey());
    }
    colCount = vColKey.size();
    if (colCount <= 0) {
      return;
    }
    int[] index = new int[colCount];
    String[][] colname = (String[][])null;
    int[] colwidth = new int[colCount];
    int[] alignflag = new int[colCount];

    colname = new String[1][colCount];
    for (int i = 0; i < colCount; i++) {
      item = report.getBodyItem((String)vColKey.get(i));
      colname[0][i] = item.getName();
      colwidth[i] = item.getWidth();

      if (item.getDataType() == 0)
        alignflag[i] = 0;
      else if ((item.getDataType() == 2) || (item.getDataType() == 1))
      {
        alignflag[i] = 2;
      }
      else alignflag[i] = 1;

    }

    Object[][] data = new Object[rowCount][colCount];

    for (int i = 0; i < rowCount; i++)
    {
      for (int j = 0; j < colCount; j++) {
        data[i][j] = report.getBillModel().getValueAt(i, (String)vColKey.get(j));
        item = report.getBodyItem((String)vColKey.get(j));
        if ((item.getDataType() != 4) || 
          (data[i][j] == null) || (!(data[i][j] instanceof Boolean))) continue;
        Boolean value = (Boolean)data[i][j];
        if (value.booleanValue()) {
          data[i][j] = "ÊÇ";
        }
        else {
          data[i][j] = "·ñ";
        }

      }

    }

    Font font = new Font("dialog", 1, 18);
    Font font1 = new Font("dialog", 0, 12);
    String topstr = NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000557") + ClientEnvironment.getInstance().getCorporation().getUnitname();

    String botstr = NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000325") + ClientEnvironment.getInstance().getUser().getUserName() + NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000558") + ClientEnvironment.getInstance().getDate();

    PrintDirectEntry print = new PrintDirectEntry();
  //  print.
    print.setTitle(title);

    print.setTitleFont(font);

    print.setContentFont(font1);

    print.setTopStr(getTopStr(report));

    print.setBottomStr(botstr);
    print.setPageNumDisp(true);
    print.setPageNumFont(font1);

    print.setPageNumAlign(2);

    print.setPageNumPos(2);
    print.setPageNumTotalDisp(true);

    print.setFixedRows(1);

    print.setColNames(colname);

    print.setData(data);

    print.setColWidth(colwidth);

    print.setAlignFlag(alignflag);
  
    print.preview();
  }

  public static void preview(BillCardPanel report, String title, String tableCode)
    throws BusinessException
  {
    if ((tableCode == null) || (tableCode.trim().length() == 0))
      return;
    BillItem[] items = report.getBillModel(tableCode).getBodyItems();
    if ((items == null) || (items.length == 0))
      return;
    if (report.getRowCount() == 0) {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000556"));
    }

    int colCount = items.length;
    int rowCount = report.getRowCount();
    Vector vColKey = new Vector();

    BillItem item = null;
    for (int i = 0; i < colCount; i++) {
      item = items[i];
      if (item.isShow())
        vColKey.add(item.getKey());
    }
    colCount = vColKey.size();
    if (colCount <= 0) {
      return;
    }
    int[] index = new int[colCount];
    String[][] colname = (String[][])null;
    int[] colwidth = new int[colCount];
    int[] alignflag = new int[colCount];

    colname = new String[1][colCount];
    for (int i = 0; i < colCount; i++) {
      item = report.getBodyItem((String)vColKey.get(i));
      colname[0][i] = item.getName();
      colwidth[i] = item.getWidth();

      if (item.getDataType() == 0) {
        alignflag[i] = 0;
      }
      else if ((item.getDataType() == 2) || (item.getDataType() == 1))
      {
        alignflag[i] = 2;
      }
      else alignflag[i] = 1;

    }

    Object[][] data = new Object[rowCount][colCount];

    for (int i = 0; i < rowCount; i++)
    {
      for (int j = 0; j < colCount; j++) {
        data[i][j] = report.getBillModel(tableCode).getValueAt(i, (String)vColKey.get(j));
      }
    }

    Font font = new Font("dialog", 1, 18);
    Font font1 = new Font("dialog", 0, 12);
    String topstr = NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000557") + ClientEnvironment.getInstance().getCorporation().getUnitname();

    String botstr = NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000325") + ClientEnvironment.getInstance().getUser().getUserName() + NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000558") + ClientEnvironment.getInstance().getDate();

    PrintDirectEntry print = new PrintDirectEntry();

    print.setTitle(title);

    print.setTitleFont(font);

    print.setContentFont(font1);

    print.setTopStr(getTopStr(report));

    print.setBottomStr(botstr);
    print.setPageNumDisp(true);
    print.setPageNumFont(font1);

    print.setPageNumAlign(2);

    print.setPageNumPos(2);
    print.setPageNumTotalDisp(true);

    print.setFixedRows(1);

    print.setColNames(colname);

    print.setData(data);

    print.setColWidth(colwidth);

    print.setAlignFlag(alignflag);

    print.preview();
  }

  public static void preview(BillCardPanel report, String title, boolean isPrintTail)
    throws BusinessException
  {
    if (report.getRowCount() == 0) {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000556"));
    }

    if (report.getBodyItems() == null) {
      return;
    }
    int colCount = report.getBodyItems().length;
    int rowCount = report.getRowCount();
    Vector vColKey = new Vector();

    BillItem item = null;
    for (int i = 0; i < colCount; i++) {
      item = report.getBodyItems()[i];
      if (item.isShow())
        vColKey.add(item.getKey());
    }
    colCount = vColKey.size();
    if (colCount <= 0) {
      return;
    }
    int[] index = new int[colCount];
    String[][] colname = (String[][])null;
    int[] colwidth = new int[colCount];
    int[] alignflag = new int[colCount];

    colname = new String[1][colCount];
    for (int i = 0; i < colCount; i++) {
      item = report.getBodyItem((String)vColKey.get(i));
      colname[0][i] = item.getName();
      colwidth[i] = item.getWidth();

      if (item.getDataType() == 0)
        alignflag[i] = 0;
      else if ((item.getDataType() == 2) || (item.getDataType() == 1))
      {
        alignflag[i] = 2;
      }
      else alignflag[i] = 1;

    }

    Object[][] data = new Object[rowCount][colCount];

    for (int i = 0; i < rowCount; i++)
    {
      for (int j = 0; j < colCount; j++) {
        data[i][j] = report.getBillModel().getValueAt(i, (String)vColKey.get(j));
      }

    }

    Font font = new Font("dialog", 1, 18);
    Font font1 = new Font("dialog", 0, 12);
    String topstr = NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000557") + ClientEnvironment.getInstance().getCorporation().getUnitname();

    String botstr = "";
    if (isPrintTail) {
      botstr = NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000325") + ClientEnvironment.getInstance().getUser().getUserName() + NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000558") + ClientEnvironment.getInstance().getDate();
    }

    PrintDirectEntry print = new PrintDirectEntry();

    print.setTitle(title);

    print.setTitleFont(font);

    print.setContentFont(font1);

    print.setTopStr(getTopStr(report));

    print.setBottomStr(botstr);
    print.setPageNumDisp(true);
    print.setPageNumFont(font1);

    print.setPageNumAlign(2);

    print.setPageNumPos(2);
    print.setPageNumTotalDisp(true);

    print.setFixedRows(1);

    print.setColNames(colname);

    print.setData(data);

    print.setColWidth(colwidth);

    print.setAlignFlag(alignflag);

    print.preview();
  }

  public static void preview(BillListPanel report, String title)
    throws BusinessException
  {
    if (report.getHeadTable() == null) {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000556"));
    }
    BillModel bsp = report.getHeadBillModel();
    if (bsp.getBodyItems() == null) {
      return;
    }
    int colCount = bsp.getBodyItems().length;
    int rowCount = bsp.getRowCount();
    Vector vColKey = new Vector();

    BillItem item = null;
    for (int i = 0; i < colCount; i++) {
      item = bsp.getBodyItems()[i];
      if (item.isShow())
        vColKey.add(item.getKey());
    }
    colCount = vColKey.size();
    if (colCount <= 0) {
      return;
    }
    int[] index = new int[colCount];
    String[][] colname = (String[][])null;
    int[] colwidth = new int[colCount];
    int[] alignflag = new int[colCount];

    colname = new String[1][colCount];
    for (int i = 0; i < colCount; i++) {
      item = bsp.getBodyItems()[bsp.getBodyColByKey((String)vColKey.get(i))];
      colname[0][i] = item.getName();
      colwidth[i] = item.getWidth();

      if (item.getDataType() == 0)
        alignflag[i] = 0;
      else if ((item.getDataType() == 2) || (item.getDataType() == 1))
      {
        alignflag[i] = 2;
      }
      else alignflag[i] = 1;

    }

    Object[][] data = new Object[rowCount][colCount];

    for (int i = 0; i < rowCount; i++)
    {
      for (int j = 0; j < colCount; j++) {
        data[i][j] = bsp.getValueAt(i, (String)vColKey.get(j));
      }
    }

    Font font = new Font("dialog", 1, 18);
    Font font1 = new Font("dialog", 0, 12);
    String topstr = null;
    String botstr = null;
    try {
      topstr = NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000557") + ClientEnvironment.getInstance().getCorporation().getUnitname();

      botstr = NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000325") + ClientEnvironment.getInstance().getUser().getUserName() + NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000558") + ClientEnvironment.getInstance().getDate();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    PrintDirectEntry print = new PrintDirectEntry(report.getParent());

    print.setTitle(title);

    print.setTitleFont(font);

    print.setContentFont(font1);

    print.setTopStr("");

    print.setBottomStr(botstr);
    print.setPageNumDisp(true);
    print.setPageNumFont(font1);

    print.setPageNumAlign(2);

    print.setPageNumPos(2);
    print.setPageNumTotalDisp(true);

    print.setFixedRows(1);

    print.setColNames(colname);

    print.setData(data);

    print.setColWidth(colwidth);

    print.setAlignFlag(alignflag);

    print.preview();
  }

  public static void preview(ReportBaseClass report, String title)
    throws BusinessException
  {
    if ((report == null) || (report.getBodyDataVO() == null) || (report.getBodyDataVO().length == 0))
    {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000556"));
    }

    if (report.getBody_Items() == null) {
      return;
    }
    int colCount = report.getBody_Items().length;
    int rowCount = report.getRowCount();

    Hashtable htHiddenCol = new Hashtable();
    for (int i = 0; i < colCount; i++) {
      ReportItem ri = report.getBody_Items()[i];
      if (!ri.isShow()) {
        htHiddenCol.put(new Integer(i), new Integer(i));
      }
    }
    int[] index = new int[colCount - htHiddenCol.size()];
    String[][] colname = (String[][])null;
    int[] colwidth = new int[colCount - htHiddenCol.size()];
    int[] alignflag = new int[colCount - htHiddenCol.size()];

    FldgroupVO[] voFldGroups = report.getFieldGroup();
    ArrayList alheadinfo = null;
    try {
      alheadinfo = getMultiHeadInfo(voFldGroups);
    } catch (Exception e) {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000559") + e.getMessage());
    }

    int headrowcount = ((Integer)alheadinfo.get(0)).intValue();
    Hashtable htGroup = (Hashtable)alheadinfo.get(1);

    colname = new String[headrowcount][colCount - htHiddenCol.size()];
    int count = 0;
    for (int i = 0; i < colCount; i++) {
      if (!htHiddenCol.containsKey(new Integer(i))) {
        ReportItem item = report.getBody_Items()[i];
        for (int j = 0; j < headrowcount; j++)
        {
          colname[j][count] = item.getName();
        }

        colwidth[count] = item.getWidth();
        if (item.getDataType() == 0)
          alignflag[count] = 0;
        else if ((item.getDataType() == 2) || (item.getDataType() == 1))
          alignflag[count] = 2;
        else
          alignflag[count] = 1;
        count++;
      }

    }

    Object[][] data = new Object[rowCount][colCount - htHiddenCol.size()];

    for (int i = 0; i < rowCount; i++) {
      count = 0;
      for (int j = 0; j < colCount; j++) {
        if (!htHiddenCol.containsKey(new Integer(j))) {
          data[i][count] = report.getBillModel().getValueAt(i, j);
          ReportItem item = report.getBody_Items()[j];
          if (item.getDataType() == 4) {
            if ((data[i][count] != null) && ((data[i][count].toString().equals("true")) || (data[i][count].toString().equals("Y"))))
              data[i][count] = NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000244");
            else
              data[i][count] = NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000108");
          }
          count++;
        }
      }

    }

    Font font = new Font("dialog", 1, 18);
    Font font1 = new Font("dialog", 0, 12);
    String topstr = NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000557") + ClientEnvironment.getInstance().getCorporation().getUnitname();

    String botstr = NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000325") + ClientEnvironment.getInstance().getUser().getUserName() + NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000558") + ClientEnvironment.getInstance().getDate();

    PrintDirectEntry print = new PrintDirectEntry();

    print.setTitle(title);

    print.setTitleFont(font);

    print.setContentFont(font1);

    print.setTopStr(getTopStr(report));

    print.setBottomStr(botstr);
    print.setPageNumDisp(true);
    print.setPageNumFont(font1);

    print.setPageNumAlign(2);

    print.setPageNumPos(2);
    print.setPageNumTotalDisp(true);

    print.setFixedRows(headrowcount);

    print.setColNames(colname);

    print.setData(data);

    print.setColWidth(colwidth);

    print.setAlignFlag(alignflag);

    Vector v = new Vector();
    int start_row = 0;
    int start_col = 0;
    int end_row = 0;
    int end_col = 0;
    int hiddenCount = 0;

    for (int i = 0; i < colCount; i++) {
      Integer iCur = new Integer(i);
      if (htHiddenCol.containsValue(iCur)) {
        hiddenCount++;
      }

      if ((hiddenCount != 0) && (htGroup != null) && (htGroup.containsKey(iCur.toString())))
      {
        CellRange tmpCell = (CellRange)htGroup.get(iCur.toString());
        tmpCell.start_col -= hiddenCount;
        tmpCell.end_col -= hiddenCount;
      }

      if ((htGroup != null) && (!htGroup.containsKey(iCur.toString())) && (!htHiddenCol.containsKey(iCur))) {
        start_row = 0;
        start_col = i - hiddenCount;
        end_row = headrowcount - 1;
        end_col = i - hiddenCount;

        colname[0][end_col] = report.getBody_Items()[i].getName();
        CellRange cell = new CellRange(start_row, start_col, end_row, end_col);
        v.addElement(cell);
      }
    }

    if (voFldGroups != null) {
      for (int i = 0; i < voFldGroups.length; i++) {
        String curname = voFldGroups[i].getGroupname();

        CellRange cell = null;
        if (!htGroup.containsKey(curname))
          continue;
        modifyCol(curname, htGroup);
        cell = (CellRange)htGroup.get(curname);
        int ts = cell.start_row;
        int td = cell.end_row;
        cell.start_row = (headrowcount - td);
        cell.end_row = (headrowcount - ts);

        colname[cell.start_row][cell.start_col] = curname;
        htGroup.remove(curname);
        v.addElement(cell);
      }

      CellRange[] cells = null;
      if (v.size() > 0) {
        cells = new CellRange[v.size()];
        v.copyInto(cells);
      }
      if (cells != null) {
        print.setCombinCellRange(cells);
      }
    }
    print.preview();
  }

  public static void preview(SimpleCard report, String title)
    throws BusinessException
  {
    if (report.getItemValue("billscrollpane") == null) {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000556"));
    }
    BillScrollPane bsp = (BillScrollPane)report.getItemValue("billscrollpane").getComponent();

    if (bsp.getTableModel().getBodyItems() == null) {
      return;
    }
    int colCount = bsp.getTableModel().getBodyItems().length;
    int rowCount = bsp.getTableModel().getRowCount();
    Vector vColKey = new Vector();

    BillItem item = null;
    for (int i = 0; i < colCount; i++) {
      item = bsp.getTableModel().getBodyItems()[i];
      if (item.isShow())
        vColKey.add(item.getKey());
    }
    colCount = vColKey.size();
    if (colCount <= 0) {
      return;
    }
    int[] index = new int[colCount];
    String[][] colname = (String[][])null;
    int[] colwidth = new int[colCount];
    int[] alignflag = new int[colCount];

    colname = new String[1][colCount];
    for (int i = 0; i < colCount; i++) {
      item = bsp.getTableModel().getBodyItems()[bsp.getTableModel().getBodyColByKey((String)vColKey.get(i))];

      colname[0][i] = item.getName();
      colwidth[i] = item.getWidth();

      if (item.getDataType() == 0)
        alignflag[i] = 0;
      else if ((item.getDataType() == 2) || (item.getDataType() == 1))
      {
        alignflag[i] = 2;
      }
      else alignflag[i] = 1;

    }

    Object[][] data = new Object[rowCount][colCount];

    for (int i = 0; i < rowCount; i++)
    {
      for (int j = 0; j < colCount; j++) {
        data[i][j] = bsp.getTableModel().getValueAt(i, (String)vColKey.get(j));
      }
    }

    Font font = new Font("dialog", 1, 18);
    Font font1 = new Font("dialog", 0, 12);
    String topstr = null;
    String botstr = null;
    try {
      topstr = NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000557") + ClientEnvironment.getInstance().getCorporation().getUnitname();

      botstr = NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000325") + ClientEnvironment.getInstance().getUser().getUserName() + NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000558") + ClientEnvironment.getInstance().getDate();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    PrintDirectEntry print = new PrintDirectEntry(report.getParent());

    print.setTitle(title);

    print.setTitleFont(font);

    print.setContentFont(font1);

    print.setTopStr(getTopStr(report));

    print.setBottomStr(botstr);
    print.setPageNumDisp(true);
    print.setPageNumFont(font1);

    print.setPageNumAlign(2);

    print.setPageNumPos(2);
    print.setPageNumTotalDisp(true);

    print.setFixedRows(1);

    print.setColNames(colname);

    print.setData(data);

    print.setColWidth(colwidth);

    print.setAlignFlag(alignflag);

    print.preview();
  }

  public static void preview(SimpleCard report, String title, boolean bIsPreview)
    throws BusinessException
  {
    if (report.getItemValue("billscrollpane") == null) {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000556"));
    }
    BillScrollPane bsp = (BillScrollPane)report.getItemValue("billscrollpane").getComponent();

    if (bsp.getTableModel().getBodyItems() == null) {
      return;
    }
    int colCount = bsp.getTableModel().getBodyItems().length;
    int rowCount = bsp.getTableModel().getRowCount();
    Vector vColKey = new Vector();

    BillItem item = null;
    for (int i = 0; i < colCount; i++) {
      item = bsp.getTableModel().getBodyItems()[i];
      if (item.isShow())
        vColKey.add(item.getKey());
    }
    colCount = vColKey.size();
    if (colCount <= 0) {
      return;
    }
    int[] index = new int[colCount];
    String[][] colname = (String[][])null;
    int[] colwidth = new int[colCount];
    int[] alignflag = new int[colCount];

    colname = new String[1][colCount];
    for (int i = 0; i < colCount; i++) {
      item = bsp.getTableModel().getBodyItems()[bsp.getTableModel().getBodyColByKey((String)vColKey.get(i))];

      colname[0][i] = item.getName();
      colwidth[i] = item.getWidth();

      if (item.getDataType() == 0)
        alignflag[i] = 0;
      else if ((item.getDataType() == 2) || (item.getDataType() == 1))
      {
        alignflag[i] = 2;
      }
      else alignflag[i] = 1;

    }

    Object[][] data = new Object[rowCount][colCount];

    for (int i = 0; i < rowCount; i++)
    {
      for (int j = 0; j < colCount; j++) {
        data[i][j] = bsp.getTableModel().getValueAt(i, (String)vColKey.get(j));
      }
    }

    Font font = new Font("dialog", 1, 18);
    Font font1 = new Font("dialog", 0, 12);
    String topstr = null;
    String botstr = null;
    try {
      topstr = NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000557") + ClientEnvironment.getInstance().getCorporation().getUnitname();

      botstr = NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000325") + ClientEnvironment.getInstance().getUser().getUserName() + NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000558") + ClientEnvironment.getInstance().getDate();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    PrintDirectEntry print = new PrintDirectEntry(report.getParent());

    print.setTitle(title);

    print.setTitleFont(font);

    print.setContentFont(font1);

    print.setTopStr(getTopStr(report));

    print.setBottomStr(botstr);
    print.setPageNumDisp(true);
    print.setPageNumFont(font1);

    print.setPageNumAlign(2);

    print.setPageNumPos(2);
    print.setPageNumTotalDisp(true);

    print.setFixedRows(1);

    print.setColNames(colname);

    print.setData(data);

    print.setColWidth(colwidth);

    print.setAlignFlag(alignflag);

    if (bIsPreview)
    {
      print.preview();
    }
  }

  public static void printDirect(SimpleCard[] report, String title, boolean bIspreview)
    throws BusinessException
  {
    if ((report == null) || (report.length == 0))
      return;
    for (int i = 0; i < report.length; i++)
    {
      if (report[i].getItemValue("billscrollpane") == null)
      {
        throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000556"));
      }
      BillScrollPane bsp = (BillScrollPane)report[i].getItemValue("billscrollpane").getComponent();
      if (bsp.getTableModel().getBodyItems() == null) {
        return;
      }
      int colCount = bsp.getTableModel().getBodyItems().length;
      int rowCount = bsp.getTableModel().getRowCount();
      Vector vColKey = new Vector();

      BillItem item = null;
      for (int j = 0; j < colCount; j++)
      {
        item = bsp.getTableModel().getBodyItems()[j];
        if (item.isShow())
          vColKey.add(item.getKey());
      }
      colCount = vColKey.size();
      if (colCount <= 0) {
        return;
      }
      int[] index = new int[colCount];
      String[][] colname = (String[][])null;
      int[] colwidth = new int[colCount];
      int[] alignflag = new int[colCount];

      colname = new String[1][colCount];
      for (int j = 0; j < colCount; j++)
      {
        item = bsp.getTableModel().getBodyItems()[bsp.getTableModel().getBodyColByKey((String)vColKey.get(j))];
        colname[0][j] = item.getName();
        colwidth[j] = item.getWidth();

        if (item.getDataType() == 0)
          alignflag[j] = 0;
        else if ((item.getDataType() == 2) || (item.getDataType() == 1))
          alignflag[j] = 2;
        else {
          alignflag[j] = 1;
        }

      }

      Object[][] data = new Object[rowCount][colCount];

      for (int j = 0; j < rowCount; j++)
      {
        for (int k = 0; k < colCount; k++) {
          data[j][k] = bsp.getTableModel().getValueAt(j, (String)vColKey.get(k));
        }
      }

      Font font = new Font("dialog", 1, 18);
      Font font1 = new Font("dialog", 0, 12);
      String topstr = null;
      String botstr = null;
      try
      {
        topstr = NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000557") + ClientEnvironment.getInstance().getCorporation().getUnitname();
        botstr = NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000325") + ClientEnvironment.getInstance().getUser().getUserName() + NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000558") + ClientEnvironment.getInstance().getDate();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }

      PrintDirectEntry print = new PrintDirectEntry(report[i].getParent());

      print.setTitle(title);

      print.setTitleFont(font);

      print.setContentFont(font1);

      print.setTopStr(getTopStr(report[i]));

      print.setBottomStr(botstr);
      print.setPageNumDisp(true);
      print.setPageNumFont(font1);

      print.setPageNumAlign(2);

      print.setPageNumPos(2);
      print.setPageNumTotalDisp(true);

      print.setFixedRows(1);

      print.setColNames(colname);

      print.setData(data);

      print.setColWidth(colwidth);

      print.setAlignFlag(alignflag);

      if (bIspreview)
        print.preview();
      else
        print.print2();
    }
  }

  public static void printDirect(BillCardPanel report, String title)
    throws BusinessException
  {
    if (report.getRowCount() == 0) {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000556"));
    }

    if (report.getBodyItems() == null) {
      return;
    }
    int colCount = report.getBodyItems().length;
    int rowCount = report.getRowCount();
    Vector vColKey = new Vector();

    BillItem item = null;
    for (int i = 0; i < colCount; i++) {
      item = report.getBodyItems()[i];
      if (item.isShow())
        vColKey.add(item.getKey());
    }
    colCount = vColKey.size();
    if (colCount <= 0) {
      return;
    }
    int[] index = new int[colCount];
    String[][] colname = (String[][])null;
    int[] colwidth = new int[colCount];
    int[] alignflag = new int[colCount];

    colname = new String[1][colCount];
    for (int i = 0; i < colCount; i++) {
      item = report.getBodyItem((String)vColKey.get(i));
      colname[0][i] = item.getName();
      colwidth[i] = item.getWidth();

      if (item.getDataType() == 0)
        alignflag[i] = 0;
      else if ((item.getDataType() == 2) || (item.getDataType() == 1))
      {
        alignflag[i] = 2;
      }
      else alignflag[i] = 1;

    }

    Object[][] data = new Object[rowCount][colCount];

    for (int i = 0; i < rowCount; i++)
    {
      for (int j = 0; j < colCount; j++) {
        data[i][j] = report.getBillModel().getValueAt(i, (String)vColKey.get(j));
        item = report.getBodyItem((String)vColKey.get(j));
        if ((item.getDataType() != 4) || 
          (data[i][j] == null) || (!(data[i][j] instanceof Boolean))) continue;
        Boolean value = (Boolean)data[i][j];
        if (value.booleanValue()) {
          data[i][j] = "ÊÇ";
        }
        else {
          data[i][j] = "·ñ";
        }

      }

    }

    Font font = new Font("dialog", 1, 18);
    Font font1 = new Font("dialog", 0, 12);
    String topstr = NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000557") + ClientEnvironment.getInstance().getCorporation().getUnitname();

    String botstr = NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000325") + ClientEnvironment.getInstance().getUser().getUserName() + NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000558") + ClientEnvironment.getInstance().getDate();

    PrintDirectEntry print = new PrintDirectEntry();

    print.setTitle(title);

    print.setTitleFont(font);

    print.setContentFont(font1);

    print.setTopStr(getTopStr(report));

    print.setBottomStr(botstr);
    print.setPageNumDisp(true);
    print.setPageNumFont(font1);

    print.setPageNumAlign(2);

    print.setPageNumPos(2);
    print.setPageNumTotalDisp(true);

    print.setFixedRows(1);

    print.setColNames(colname);

    print.setData(data);

    print.setColWidth(colwidth);

    print.setAlignFlag(alignflag);

    print.print2();
  }

  public static void printDirect(BillCardPanel report, String title, String tableCode)
    throws BusinessException
  {
    if (report.getRowCount() == 0) {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000556"));
    }
    if ((tableCode == null) || (tableCode.trim().length() == 0))
      return;
    BillItem[] items = report.getBillModel(tableCode).getBodyItems();
    if ((items == null) || (items.length == 0)) {
      return;
    }

    int colCount = items.length;
    int rowCount = report.getRowCount();
    Vector vColKey = new Vector();

    BillItem item = null;
    for (int i = 0; i < colCount; i++) {
      item = items[i];
      if (item.isShow())
        vColKey.add(item.getKey());
    }
    colCount = vColKey.size();
    if (colCount <= 0) {
      return;
    }
    int[] index = new int[colCount];
    String[][] colname = (String[][])null;
    int[] colwidth = new int[colCount];
    int[] alignflag = new int[colCount];

    colname = new String[1][colCount];
    for (int i = 0; i < colCount; i++) {
      item = report.getBodyItem((String)vColKey.get(i));
      colname[0][i] = item.getName();
      colwidth[i] = item.getWidth();

      if (item.getDataType() == 0) {
        alignflag[i] = 0;
      }
      else if ((item.getDataType() == 2) || (item.getDataType() == 1))
      {
        alignflag[i] = 2;
      }
      else alignflag[i] = 1;

    }

    Object[][] data = new Object[rowCount][colCount];

    for (int i = 0; i < rowCount; i++)
    {
      for (int j = 0; j < colCount; j++) {
        data[i][j] = report.getBillModel(tableCode).getValueAt(i, (String)vColKey.get(j));
      }

    }

    Font font = new Font("dialog", 1, 18);
    Font font1 = new Font("dialog", 0, 12);
    String topstr = NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000557") + ClientEnvironment.getInstance().getCorporation().getUnitname();

    String botstr = NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000325") + ClientEnvironment.getInstance().getUser().getUserName() + NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000558") + ClientEnvironment.getInstance().getDate();

    PrintDirectEntry print = new PrintDirectEntry();

    print.setTitle(title);

    print.setTitleFont(font);

    print.setContentFont(font1);

    print.setTopStr(getTopStr(report));

    print.setBottomStr(botstr);
    print.setPageNumDisp(true);
    print.setPageNumFont(font1);

    print.setPageNumAlign(2);

    print.setPageNumPos(2);
    print.setPageNumTotalDisp(true);

    print.setFixedRows(1);

    print.setColNames(colname);

    print.setData(data);

    print.setColWidth(colwidth);

    print.setAlignFlag(alignflag);

    print.print2();
  }

  public static void printDirect(BillListPanel report, String title)
    throws BusinessException
  {
    if (report.getHeadTable() == null) {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000556"));
    }
    BillModel bsp = report.getHeadBillModel();
    if (bsp.getBodyItems() == null) {
      return;
    }
    int colCount = bsp.getBodyItems().length;
    int rowCount = bsp.getRowCount();
    Vector vColKey = new Vector();

    BillItem item = null;
    for (int i = 0; i < colCount; i++) {
      item = bsp.getBodyItems()[i];
      if (item.isShow())
        vColKey.add(item.getKey());
    }
    colCount = vColKey.size();
    if (colCount <= 0) {
      return;
    }
    int[] index = new int[colCount];
    String[][] colname = (String[][])null;
    int[] colwidth = new int[colCount];
    int[] alignflag = new int[colCount];

    colname = new String[1][colCount];
    for (int i = 0; i < colCount; i++) {
      item = bsp.getBodyItems()[bsp.getBodyColByKey((String)vColKey.get(i))];
      colname[0][i] = item.getName();
      colwidth[i] = item.getWidth();

      if (item.getDataType() == 0)
        alignflag[i] = 0;
      else if ((item.getDataType() == 2) || (item.getDataType() == 1))
      {
        alignflag[i] = 2;
      }
      else alignflag[i] = 1;

    }

    Object[][] data = new Object[rowCount][colCount];

    for (int i = 0; i < rowCount; i++)
    {
      for (int j = 0; j < colCount; j++) {
        data[i][j] = bsp.getValueAt(i, (String)vColKey.get(j));
      }
    }

    Font font = new Font("dialog", 1, 18);
    Font font1 = new Font("dialog", 0, 12);
    String topstr = null;
    String botstr = null;
    try {
      topstr = NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000557") + ClientEnvironment.getInstance().getCorporation().getUnitname();

      botstr = NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000325") + ClientEnvironment.getInstance().getUser().getUserName() + NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000558") + ClientEnvironment.getInstance().getDate();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    PrintDirectEntry print = new PrintDirectEntry(report.getParent());

    print.setTitle(title);

    print.setTitleFont(font);

    print.setContentFont(font1);

    print.setTopStr("");

    print.setBottomStr(botstr);
    print.setPageNumDisp(true);
    print.setPageNumFont(font1);

    print.setPageNumAlign(2);

    print.setPageNumPos(2);
    print.setPageNumTotalDisp(true);

    print.setFixedRows(1);

    print.setColNames(colname);

    print.setData(data);

    print.setColWidth(colwidth);

    print.setAlignFlag(alignflag);

    print.print2();
  }

  public static void printDirect(ReportBaseClass report, String title)
    throws BusinessException
  {
    if ((report == null) || (report.getBodyDataVO() == null) || (report.getBodyDataVO().length == 0))
    {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000556"));
    }

    if (report.getBody_Items() == null) {
      return;
    }
    int colCount = report.getBody_Items().length;
    int rowCount = report.getRowCount();

    Hashtable htHiddenCol = new Hashtable();
    for (int i = 0; i < colCount; i++) {
      ReportItem ri = report.getBody_Items()[i];
      if (!ri.isShow()) {
        htHiddenCol.put(new Integer(i), new Integer(i));
      }
    }
    int[] index = new int[colCount - htHiddenCol.size()];
    String[][] colname = (String[][])null;
    int[] colwidth = new int[colCount - htHiddenCol.size()];
    int[] alignflag = new int[colCount - htHiddenCol.size()];

    FldgroupVO[] voFldGroups = report.getFieldGroup();
    ArrayList alheadinfo = null;
    try {
      alheadinfo = getMultiHeadInfo(voFldGroups);
    } catch (Exception e) {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000559") + e.getMessage());
    }

    int headrowcount = ((Integer)alheadinfo.get(0)).intValue();
    Hashtable htGroup = (Hashtable)alheadinfo.get(1);

    colname = new String[headrowcount][colCount - htHiddenCol.size()];
    int count = 0;
    for (int i = 0; i < colCount; i++) {
      if (!htHiddenCol.containsKey(new Integer(i))) {
        ReportItem item = report.getBody_Items()[i];
        for (int j = 0; j < headrowcount; j++)
        {
          colname[j][count] = item.getName();
        }

        colwidth[count] = item.getWidth();
        if (item.getDataType() == 0)
          alignflag[count] = 0;
        else if ((item.getDataType() == 2) || (item.getDataType() == 1))
          alignflag[count] = 2;
        else
          alignflag[count] = 1;
        count++;
      }

    }

    Object[][] data = new Object[rowCount][colCount - htHiddenCol.size()];

    for (int i = 0; i < rowCount; i++) {
      count = 0;
      for (int j = 0; j < colCount; j++) {
        if (!htHiddenCol.containsKey(new Integer(j))) {
          data[i][count] = report.getBillModel().getValueAt(i, j);
          ReportItem item = report.getBody_Items()[j];
          if (item.getDataType() == 4) {
            if ((data[i][count] != null) && ((data[i][count].toString().equals("true")) || (data[i][count].toString().equals("Y"))))
              data[i][count] = NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000244");
            else
              data[i][count] = NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000108");
          }
          count++;
        }
      }

    }

    Font font = new Font("dialog", 1, 18);
    Font font1 = new Font("dialog", 0, 12);
    String topstr = NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000557") + ClientEnvironment.getInstance().getCorporation().getUnitname();

    String botstr = NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000325") + ClientEnvironment.getInstance().getUser().getUserName() + NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000558") + ClientEnvironment.getInstance().getDate();

    PrintDirectEntry print = new PrintDirectEntry();

    print.setTitle(title);

    print.setTitleFont(font);

    print.setContentFont(font1);

    print.setTopStr(getTopStr(report));

    print.setBottomStr(botstr);
    print.setPageNumDisp(true);
    print.setPageNumFont(font1);

    print.setPageNumAlign(2);

    print.setPageNumPos(2);
    print.setPageNumTotalDisp(true);

    print.setFixedRows(headrowcount);

    print.setColNames(colname);

    print.setData(data);

    print.setColWidth(colwidth);

    print.setAlignFlag(alignflag);

    Vector v = new Vector();
    int start_row = 0;
    int start_col = 0;
    int end_row = 0;
    int end_col = 0;
    int hiddenCount = 0;

    for (int i = 0; i < colCount; i++) {
      Integer iCur = new Integer(i);
      if (htHiddenCol.containsValue(iCur)) {
        hiddenCount++;
      }

      if ((hiddenCount != 0) && (htGroup != null) && (htGroup.containsKey(iCur.toString())))
      {
        CellRange tmpCell = (CellRange)htGroup.get(iCur.toString());
        tmpCell.start_col -= hiddenCount;
        tmpCell.end_col -= hiddenCount;
      }

      if ((htGroup != null) && (!htGroup.containsKey(iCur.toString())) && (!htHiddenCol.containsKey(iCur))) {
        start_row = 0;
        start_col = i - hiddenCount;
        end_row = headrowcount - 1;
        end_col = i - hiddenCount;

        colname[0][end_col] = report.getBody_Items()[i].getName();
        CellRange cell = new CellRange(start_row, start_col, end_row, end_col);
        v.addElement(cell);
      }
    }

    if (voFldGroups != null) {
      for (int i = 0; i < voFldGroups.length; i++) {
        String curname = voFldGroups[i].getGroupname();

        CellRange cell = null;
        if (!htGroup.containsKey(curname))
          continue;
        modifyCol(curname, htGroup);
        cell = (CellRange)htGroup.get(curname);
        int ts = cell.start_row;
        int td = cell.end_row;
        cell.start_row = (headrowcount - td);
        cell.end_row = (headrowcount - ts);

        colname[cell.start_row][cell.start_col] = curname;
        htGroup.remove(curname);
        v.addElement(cell);
      }

      CellRange[] cells = null;
      if (v.size() > 0) {
        cells = new CellRange[v.size()];
        v.copyInto(cells);
      }
      if (cells != null) {
        print.setCombinCellRange(cells);
      }
    }
    print.print2();
  }

  public static void printDirect(SimpleCard report, String title)
    throws BusinessException
  {
    if (report.getItemValue("billscrollpane") == null) {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000556"));
    }
    BillScrollPane bsp = (BillScrollPane)report.getItemValue("billscrollpane").getComponent();

    if (bsp.getTableModel().getBodyItems() == null) {
      return;
    }
    int colCount = bsp.getTableModel().getBodyItems().length;
    int rowCount = bsp.getTableModel().getRowCount();
    Vector vColKey = new Vector();

    BillItem item = null;
    for (int i = 0; i < colCount; i++) {
      item = bsp.getTableModel().getBodyItems()[i];
      if (item.isShow())
        vColKey.add(item.getKey());
    }
    colCount = vColKey.size();
    if (colCount <= 0) {
      return;
    }
    int[] index = new int[colCount];
    String[][] colname = (String[][])null;
    int[] colwidth = new int[colCount];
    int[] alignflag = new int[colCount];

    colname = new String[1][colCount];
    for (int i = 0; i < colCount; i++) {
      item = bsp.getTableModel().getBodyItems()[bsp.getTableModel().getBodyColByKey((String)vColKey.get(i))];

      colname[0][i] = item.getName();
      colwidth[i] = item.getWidth();

      if (item.getDataType() == 0)
        alignflag[i] = 0;
      else if ((item.getDataType() == 2) || (item.getDataType() == 1))
      {
        alignflag[i] = 2;
      }
      else alignflag[i] = 1;

    }

    Object[][] data = new Object[rowCount][colCount];

    for (int i = 0; i < rowCount; i++)
    {
      for (int j = 0; j < colCount; j++) {
        data[i][j] = bsp.getTableModel().getValueAt(i, (String)vColKey.get(j));
      }
    }

    Font font = new Font("dialog", 1, 18);
    Font font1 = new Font("dialog", 0, 12);
    String topstr = null;
    String botstr = null;
    try {
      topstr = NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000557") + ClientEnvironment.getInstance().getCorporation().getUnitname();

      botstr = NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000325") + ClientEnvironment.getInstance().getUser().getUserName() + NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000558") + ClientEnvironment.getInstance().getDate();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    PrintDirectEntry print = new PrintDirectEntry(report.getParent());

    print.setTitle(title);

    print.setTitleFont(font);

    print.setContentFont(font1);

    print.setTopStr(getTopStr(report));

    print.setBottomStr(botstr);
    print.setPageNumDisp(true);
    print.setPageNumFont(font1);

    print.setPageNumAlign(2);

    print.setPageNumPos(2);
    print.setPageNumTotalDisp(true);

    print.setFixedRows(1);

    print.setColNames(colname);

    print.setData(data);

    print.setColWidth(colwidth);

    print.setAlignFlag(alignflag);

    print.print2();
  }

  public static void printDirect(SimpleCard report, String title, boolean bIsPreview)
    throws BusinessException
  {
    if (report.getItemValue("billscrollpane") == null) {
      throw new BusinessException(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000556"));
    }
    BillScrollPane bsp = (BillScrollPane)report.getItemValue("billscrollpane").getComponent();

    if (bsp.getTableModel().getBodyItems() == null) {
      return;
    }
    int colCount = bsp.getTableModel().getBodyItems().length;
    int rowCount = bsp.getTableModel().getRowCount();
    Vector vColKey = new Vector();

    BillItem item = null;
    for (int i = 0; i < colCount; i++) {
      item = bsp.getTableModel().getBodyItems()[i];
      if (item.isShow())
        vColKey.add(item.getKey());
    }
    colCount = vColKey.size();
    if (colCount <= 0) {
      return;
    }
    int[] index = new int[colCount];
    String[][] colname = (String[][])null;
    int[] colwidth = new int[colCount];
    int[] alignflag = new int[colCount];

    colname = new String[1][colCount];
    for (int i = 0; i < colCount; i++) {
      item = bsp.getTableModel().getBodyItems()[bsp.getTableModel().getBodyColByKey((String)vColKey.get(i))];

      colname[0][i] = item.getName();
      colwidth[i] = item.getWidth();

      if (item.getDataType() == 0)
        alignflag[i] = 0;
      else if ((item.getDataType() == 2) || (item.getDataType() == 1))
      {
        alignflag[i] = 2;
      }
      else alignflag[i] = 1;

    }

    Object[][] data = new Object[rowCount][colCount];

    for (int i = 0; i < rowCount; i++)
    {
      for (int j = 0; j < colCount; j++) {
        data[i][j] = bsp.getTableModel().getValueAt(i, (String)vColKey.get(j));
      }
    }

    Font font = new Font("dialog", 1, 18);
    Font font1 = new Font("dialog", 0, 12);
    String topstr = null;
    String botstr = null;
    try {
      topstr = NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000557") + ClientEnvironment.getInstance().getCorporation().getUnitname();

      botstr = NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000325") + ClientEnvironment.getInstance().getUser().getUserName() + NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000558") + ClientEnvironment.getInstance().getDate();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    PrintDirectEntry print = new PrintDirectEntry(report.getParent());

    print.setTitle(title);

    print.setTitleFont(font);

    print.setContentFont(font1);

    print.setTopStr(getTopStr(report));

    print.setBottomStr(botstr);
    print.setPageNumDisp(true);
    print.setPageNumFont(font1);

    print.setPageNumAlign(2);

    print.setPageNumPos(2);
    print.setPageNumTotalDisp(true);

    print.setFixedRows(1);

    print.setColNames(colname);

    print.setData(data);

    print.setColWidth(colwidth);

    print.setAlignFlag(alignflag);

    if (bIsPreview)
    {
      print.preview();
    }
    else
      print.print2();
  }
}