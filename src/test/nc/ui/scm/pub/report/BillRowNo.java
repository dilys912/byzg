package nc.ui.scm.pub.report;

import java.awt.Container;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.BillRowNoVO;

public class BillRowNo
{
  private static int m_rowNoStart = 10;

  private static int m_rowNoStep = 10;

  public static void addLineRowNo(BillCardPanel pnlCard, String sBillType, String sRowNOKey)
  {
    addLineRowNos(pnlCard, sBillType, sRowNOKey, 1);
  }

  public static void addLineRowNo(BillCardPanel pnlCard, String sBillType, String sRowNOKey, int iGivenPreRow)
  {
    addLineRowNos(pnlCard, sBillType, sRowNOKey, iGivenPreRow, new int[] { pnlCard.getRowCount() - 1 });
  }

  public static void addLineRowNos(BillCardPanel pnlCard, String sBillType, String sRowNOKey, int iGivenPreRow, int[] iaGivenSetRow)
  {
    if ((pnlCard == null) || (sBillType == null) || (sRowNOKey == null) || (pnlCard.getBodyItem(sRowNOKey) == null) || (iaGivenSetRow == null))
    {
      System.out.println("行号公共方法传入参数不正确，请检查！");
      return;
    }

    int iSetLen = iaGivenSetRow.length;
    for (int i = 0; i < iSetLen; i++) {
      pnlCard.setBodyValueAt(null, iaGivenSetRow[i], sRowNOKey);
    }

    UFDouble dPreviousRowNO = getRowNoUFDoubleMax(pnlCard, sRowNOKey);
    for (int i = 0; i < iSetLen; i++)
    {
      UFDouble dCurRowNO = dPreviousRowNO.add(m_rowNoStart);

      pnlCard.setBodyValueAt(BillRowNoVO.getCorrectString(dCurRowNO), iaGivenSetRow[i], sRowNOKey);

      dPreviousRowNO = dCurRowNO;
    }
  }

  public static void addLineRowNos(BillCardPanel pnlCard, String sBillType, String sRowNOKey, int iSetCount)
  {
    int iTotalCount = pnlCard.getRowCount();

    int[] iaGivenRow = new int[iSetCount];
    int iPreRow = iTotalCount - iSetCount - 1;
    for (int i = 0; i < iSetCount; i++) {
      iaGivenRow[i] = (iPreRow + i + 1);
    }
    addLineRowNos(pnlCard, sBillType, sRowNOKey, iPreRow, iaGivenRow);
  }

  public static void addNewRowNo(BillCardPanel pnlCard, String sBillType, String sRowNOKey)
  {
    int iRowCount = pnlCard.getRowCount();
    if (iRowCount == 0) {
      return;
    }

    int[] iaAllRow = new int[iRowCount];
    for (int i = 0; i < iRowCount; i++) {
      iaAllRow[i] = i;
    }
    addNewRowNo(pnlCard, sBillType, sRowNOKey, iaAllRow);
  }

  public static void afterEditWhenRowNo(BillCardPanel pnlCard, BillEditEvent e, String sBillType)
  {
    if ((pnlCard == null) || (sBillType == null)) {
      System.out.println("行号公共方法传入参数不正确，请检查！");
      return;
    }

    String sRowNOKey = e.getKey();
    int iRow = e.getRow();
    String sValue = (String)e.getValue();

    if ((sValue == null) || (sValue.trim().equals(""))) {
      MessageDialog.showHintDlg(pnlCard, NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000270"), pnlCard.getBodyItem(sRowNOKey).getName() + NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000565"));
      pnlCard.setBodyValueAt(e.getOldValue(), iRow, sRowNOKey);
      return;
    }if (getRowNoUFDoubleAt(pnlCard, sRowNOKey, iRow).compareTo(BillRowNoVO.ZERO_UFDOUBLE) == 0) {
      MessageDialog.showHintDlg(pnlCard, NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000270"), pnlCard.getBodyItem(sRowNOKey).getName() + NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000566"));
      pnlCard.setBodyValueAt(e.getOldValue(), iRow, sRowNOKey);
      return;
    }

    pnlCard.setBodyValueAt(BillRowNoVO.getCorrectString(getRowNoUFDoubleAt(pnlCard, sRowNOKey, iRow)), iRow, sRowNOKey);
  }

  private static UFDouble getRowNoUFDoubleLessThan(BillCardPanel pnlCard, String sRowNOKey, int iRow)
  {
    int nRow = pnlCard.getBillTable().getRowCount();
    if (nRow == 1) {
      return BillRowNoVO.ZERO_UFDOUBLE;
    }

    UFDouble dKnownRowNo = getRowNoUFDoubleAt(pnlCard, sRowNOKey, iRow);

    UFDouble dLessFinal = BillRowNoVO.ZERO_UFDOUBLE;

    UFDouble dTemp = null;

    for (int i = 0; i < nRow; i++) {
      dTemp = getRowNoUFDoubleAt(pnlCard, sRowNOKey, i);
      if ((dTemp.compareTo(dKnownRowNo) < 0) && (dTemp.compareTo(dLessFinal) > 0)) {
        dLessFinal = dTemp;
      }
    }

    if (dLessFinal.compareTo(dKnownRowNo) == 0) {
      dLessFinal = BillRowNoVO.ZERO_UFDOUBLE;
    }

    return dLessFinal;
  }

  private static UFDouble getRowNoUFDoubleMax(BillCardPanel pnlCard, String sRowNOKey)
  {
    int nRow = pnlCard.getBillTable().getRowCount();
    if (nRow == 1) {
      return BillRowNoVO.ZERO_UFDOUBLE;
    }

    UFDouble dMaxValue = BillRowNoVO.ZERO_UFDOUBLE;
    UFDouble dEveryValue = null;
    for (int i = 0; i < nRow; i++) {
      dEveryValue = getRowNoUFDoubleAt(pnlCard, sRowNOKey, i);
      if (dMaxValue.compareTo(dEveryValue) < 0) {
        dMaxValue = dEveryValue;
      }
    }

    return dMaxValue;
  }

  private static String getDuplicatedRowNo(BillModel bm, String sRowNOKey)
  {
    int nRow = bm.getRowCount();
    Vector v1 = new Vector();
    Vector v2 = new Vector();

    Object oTemp = bm.getValueAt(0, sRowNOKey);
    if ((oTemp != null) && (oTemp.toString().trim().length() > 0)) {
      v1.addElement(oTemp.toString().trim());
    }

    for (int i = 1; i < nRow; i++) {
      oTemp = bm.getValueAt(i, sRowNOKey);
      if ((oTemp != null) && (oTemp.toString().trim().length() > 0)) {
        String s = oTemp.toString().trim();
        if (!v1.contains(s)) {
          v1.addElement(s);
        } else {
          if (v2.contains(s)) continue; v2.addElement(s);
        }
      }
    }

    if (v2.size() > 0) {
      String s = "";
      for (int i = 0; i < v2.size() - 1; i++) {
        s = s + v2.elementAt(i) + "、";
      }
      s = s + v2.elementAt(v2.size() - 1);

      return s;
    }

    return null;
  }

  private static String getRowNoAfterByRule(String sBillType, String sRowNo)
  {
    return BillRowNoVO.getRowNoAfterByRule(sRowNo, m_rowNoStep);
  }

  private static UFDouble getRowNoUFDoubleAt(BillCardPanel pnlCard, String sRowNoKey, int iRow)
  {
    if (iRow < 0) {
      return BillRowNoVO.ZERO_UFDOUBLE;
    }

    Object oValue = pnlCard.getBodyValueAt(iRow, sRowNoKey);
    return BillRowNoVO.getUFDouble(oValue);
  }

  private static int getPower(UFDouble dValue)
  {
    dValue.setTrimZero(true);
    int iPower = 0;
    String sValue = dValue.toString();
    if (sValue.indexOf(".") > 0) {
      iPower = sValue.length() - sValue.indexOf(".") - 1;
    }
    return iPower;
  }

  public static void insertLineRowNo(BillCardPanel pnlCard, String sBillType, String sRowNOKey)
  {
    insertLineRowNos(pnlCard, sBillType, sRowNOKey, pnlCard.getBillTable().getSelectedRow() + 1, 1);
  }

  public static void addNewRowNo(BillCardPanel pnlCard, String sBillType, String sRowNOKey, int[] iaGivenSetRow)
  {
    if ((pnlCard == null) || (sBillType == null) || (sRowNOKey == null) || (pnlCard.getBodyItem(sRowNOKey) == null) || (iaGivenSetRow == null))
    {
      System.out.println("行号公共方法传入参数不正确，请检查！");
      return;
    }

    String sValue = m_rowNoStart + "";
    int iLen = iaGivenSetRow.length;
    for (int i = 0; i < iLen; i++) {
      if (i != 0) {
        sValue = getRowNoAfterByRule(sBillType, sValue);
      }
      pnlCard.setBodyValueAt(BillRowNoVO.getCorrectString(BillRowNoVO.getUFDouble(sValue)), iaGivenSetRow[i], sRowNOKey);
    }
  }

  public static void insertLineRowNos(BillCardPanel pnlCard, String sBillType, String sRowNOKey, int iNextRow, int[] iaGivenSetRow)
  {
    if ((pnlCard == null) || (sBillType == null) || (sRowNOKey == null) || (pnlCard.getBodyItem(sRowNOKey) == null) || (iaGivenSetRow == null))
    {
      System.out.println("行号公共方法传入参数不正确，请检查！");
      return;
    }

    UFDouble dPreviousRowNO = getRowNoUFDoubleLessThan(pnlCard, sRowNOKey, iNextRow);
    UFDouble dNextRowNO = getRowNoUFDoubleAt(pnlCard, sRowNOKey, iNextRow);

    int iSetLen = iaGivenSetRow.length;
    UFDouble[] uaRowNo = new UFDouble[iSetLen];

    if (dPreviousRowNO.compareTo(dNextRowNO) == 0) {
      for (int i = 0; i < iSetLen; i++) {
        uaRowNo[i] = dPreviousRowNO;
      }

    }
    else
    {
      UFDouble dStep = dNextRowNO.sub(dPreviousRowNO).div(iSetLen + 1);
      for (int i = 0; i < iSetLen; i++) {
        uaRowNo[i] = dPreviousRowNO.add(dStep.multiply(i + 1.0D));
      }

      int iStepDgt = 8;
      UFDouble Zero = new UFDouble(0.0D);

      UFDouble[] uaRowNoDgt = new UFDouble[iSetLen];
      for (int i = 0; i < iSetLen; i++) {
        uaRowNoDgt[i] = uaRowNo[i];
      }
      while (true)
      {
        HashMap hmapRowNo = new HashMap();
        for (int i = 0; i < iSetLen; i++)
        {
          if (((uaRowNoDgt[i].compareTo(dPreviousRowNO) <= 0) && (uaRowNoDgt[i].compareTo(dNextRowNO) <= 0)) || ((uaRowNoDgt[i].compareTo(dPreviousRowNO) >= 0) && (uaRowNoDgt[i].compareTo(dNextRowNO) >= 0)) || (uaRowNoDgt[i].compareTo(Zero) == 0))
          {
            break;
          }

          hmapRowNo.put(BillRowNoVO.getCorrectString(uaRowNoDgt[i]), null);
        }
        hmapRowNo.put(BillRowNoVO.getCorrectString(dPreviousRowNO), null);
        hmapRowNo.put(BillRowNoVO.getCorrectString(dNextRowNO), null);
        if (hmapRowNo.size() != iSetLen + 2) {
          break;
        }
        for (int i = 0; i < iSetLen; i++) {
          uaRowNo[i] = uaRowNoDgt[i];
        }
        if (iStepDgt <= 0)
        {
          break;
        }

        iStepDgt--;
        for (int i = 0; i < iSetLen; i++) {
          uaRowNoDgt[i] = uaRowNoDgt[i].setScale(iStepDgt, 1);
        }

      }

    }

    for (int i = 0; i < iSetLen; i++)
    {
      pnlCard.setBodyValueAt(BillRowNoVO.getCorrectString(uaRowNo[i]), iaGivenSetRow[i], sRowNOKey);
    }
  }

  public static void insertLineRowNos(BillCardPanel pnlCard, String sBillType, String sRowNOKey, int iEndRow, int iSetCount)
  {
    int iPreviousRow = iEndRow - 1 - iSetCount;
    int[] iaGivenSetRow = new int[iSetCount];
    for (int i = 0; i < iSetCount; i++) {
      iaGivenSetRow[i] = (iPreviousRow + 1 + i);
    }

    insertLineRowNos(pnlCard, sBillType, sRowNOKey, iEndRow, iaGivenSetRow);
  }

  public static void loadRowNoItem(BillCardPanel pnlCard, String sRowNOKey)
  {
    if ((pnlCard == null) || (sRowNOKey == null) || (pnlCard.getBodyItem(sRowNOKey) == null)) {
      System.out.println("行号公共方法传入参数不正确，请检查！");
      return;
    }

    UIRefPane refRowNO = (UIRefPane)pnlCard.getBodyItem(sRowNOKey).getComponent();
    
  //2016-09-27反编译修改  yqq
    refRowNO.getUITextField().setDocument(new PositiveUFDoubleDocument());
 // refRowNO.getUITextField().setDocument(new PositiveUFDoubleDocument(null));
  }

  public static void main(String[] args)
  {
  }

  public static void pasteLineRowNo(BillCardPanel pnlCard, String sBillType, String sRowNOKey, int iNextRow, int[] iaGivenSetRow)
  {
    insertLineRowNos(pnlCard, sBillType, sRowNOKey, iNextRow, iaGivenSetRow);
  }

  public static void pasteLineRowNo(BillCardPanel pnlCard, String sBillType, String sRowNOKey, int iPasteCount)
  {
    insertLineRowNos(pnlCard, sBillType, sRowNOKey, pnlCard.getBillTable().getSelectedRow(), iPasteCount);
  }

  private static void setAverageLineRowNo(BillCardPanel pnlCard, String sBillType, String sRowNOKey, int iEndRow, int iSetCount)
  {
    int iPreviousRow = iEndRow - 1 - iSetCount;
    UFDouble dPreviousRowNO = getRowNoUFDoubleAt(pnlCard, sRowNOKey, iPreviousRow);
    UFDouble dNextRowNO = getRowNoUFDoubleAt(pnlCard, sRowNOKey, iEndRow);

    int iBeginSetRow = iPreviousRow + 1;
    int iEndSetRow = iEndRow - 1;

    UFDouble dStep = dNextRowNO.sub(dPreviousRowNO).div(iSetCount + 1);
    for (int i = iBeginSetRow; i <= iEndSetRow; i++)
    {
      UFDouble dCurRowNO = dPreviousRowNO.add(dStep.multiply(i - iBeginSetRow + 1));
      pnlCard.setBodyValueAt(BillRowNoVO.getCorrectString(dCurRowNO), i, sRowNOKey);
    }
  }

  public static void setRowNoOnNull(BillCardPanel pnlCard, String sBillType, String sRowNOKey)
  {
    if ((pnlCard == null) || (sBillType == null) || (sRowNOKey == null) || (pnlCard.getBodyItem(sRowNOKey) == null)) {
      return;
    }

    int iCount = pnlCard.getRowCount();
    String[] saRowNo = new String[iCount];
    for (int i = 0; i < iCount; i++) {
      saRowNo[i] = ((String)pnlCard.getBodyValueAt(i, sRowNOKey));
    }

    saRowNo = BillRowNoVO.getRowNosResetNull(m_rowNoStart, m_rowNoStep, saRowNo);

    for (int i = 0; i < iCount; i++)
      pnlCard.setBodyValueAt(saRowNo[i], i, sRowNOKey);
  }

  public static void setVORowNoByRule(AggregatedValueObject voAgg, String sBillType, String sRowNOKey)
  {
    if ((voAgg == null) || (sBillType == null) || (sRowNOKey == null)) {
      return;
    }

    setVOsRowNoByRule(new AggregatedValueObject[] { voAgg }, sBillType, sRowNOKey);
  }

  public static void setVORowNoByRule(CircularlyAccessibleValueObject[] voaCA, String sBillType, String sRowNOKey)
  {
    BillRowNoVO.setVOsRowNoByRule(voaCA, sBillType, sRowNOKey);
  }

  public static void setVOsRowNoByRule(AggregatedValueObject[] voaAgg, String sBillType, String sRowNOKey)
  {
    BillRowNoVO.setVOsRowNoByRule(voaAgg, sBillType, sRowNOKey);
  }

  public static boolean verifyRowNosCorrect(Container cont, BillModel bm, String sRowNOKey)
  {
    if ((cont == null) || (bm == null) || (sRowNOKey == null) || (bm.getItemByKey(sRowNOKey) == null)) {
      System.out.println("行号公共方法传入参数不正确，请检查！");
      return false;
    }

    int iBodyLen = bm.getRowCount();
    for (int i = 0; i < iBodyLen; i++) {
      Object oValue = bm.getValueAt(i, sRowNOKey);
      if ((oValue == null) || (oValue.toString().trim().equals(""))) {
        MessageDialog.showHintDlg(cont, NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000567") + bm.getItemByKey(sRowNOKey).getName() + "！");

        return false;
      }
    }

    String sDuplicateRowNO = getDuplicatedRowNo(bm, sRowNOKey);
    if (sDuplicateRowNO == null) {
      return true;
    }

    MessageDialog.showHintDlg(cont, NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000569") + bm.getItemByKey(sRowNOKey).getName() + "：\n" + sDuplicateRowNO + " ！");

    return false;
  }

  public static boolean verifyRowNosCorrect(BillCardPanel pnlCard, String sRowNOKey)
  {
    if ((pnlCard == null) || (sRowNOKey == null) || (pnlCard.getBodyItem(sRowNOKey) == null)) {
      System.out.println("行号公共方法传入参数不正确，请检查！");
      return false;
    }

    int iBodyLen = pnlCard.getRowCount();
    for (int i = 0; i < iBodyLen; i++) {
      Object oValue = pnlCard.getBodyValueAt(i, sRowNOKey);
      if ((oValue == null) || (oValue.toString().trim().equals(""))) {
        MessageDialog.showHintDlg(pnlCard, NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000567") + pnlCard.getBodyItem(sRowNOKey).getName() + "！");

        return false;
      }
    }

    String sDuplicateRowNO = getDuplicatedRowNo(pnlCard.getBillModel(), sRowNOKey);
    if (sDuplicateRowNO == null) {
      return true;
    }

    MessageDialog.showHintDlg(pnlCard, NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000569") + pnlCard.getBodyItem(sRowNOKey).getName() + "：\n" + sDuplicateRowNO + " ！");

    return false;
  }

  private static class PositiveUFDoubleDocument extends PlainDocument
  {
    public void insertString(int iOffset, String sInsert, AttributeSet attrSet)
      throws BadLocationException
    {
      if (sInsert == null) {
        return;
      }

      String sOld = getText(0, getLength());
      String sNew = sOld.substring(0, iOffset) + sInsert + sOld.substring(iOffset);

      if ((sNew.indexOf("e") > 0) || (sNew.length() > 20)) {
        return;
      }
      try
      {
        if ("-".equals(sNew)) {
          return;
        }

        int iDotIndex = sNew.indexOf(".");
        if (iDotIndex > 0) {
          int iLenAfterDot = sNew.length() - iDotIndex - 1;
          if (iLenAfterDot > 8) {
            return;
          }
        }
        UFDouble dValue = new UFDouble(sNew);
        super.insertString(iOffset, sInsert, attrSet);
      }
      catch (Exception e)
      {
      }
    }
  }
}