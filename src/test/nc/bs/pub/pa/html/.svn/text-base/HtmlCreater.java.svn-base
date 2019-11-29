package nc.bs.pub.pa.html;

import java.text.NumberFormat;
import java.util.Date;
import nc.bs.logging.Logger;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pa.AlertregistryVO;

public class HtmlCreater extends Thread
{
  public HtmlCreater()
  {
  }

  public HtmlCreater(Runnable target)
  {
    super(target);
  }

  public HtmlCreater(Runnable target, String name)
  {
    super(target, name);
  }

  public HtmlCreater(String name)
  {
    super(name);
  }

  public HtmlCreater(ThreadGroup group, Runnable target)
  {
    super(group, target);
  }

  public HtmlCreater(ThreadGroup group, Runnable target, String name)
  {
    super(group, target, name);
  }

  public HtmlCreater(ThreadGroup group, String name)
  {
    super(group, name);
  }

  public static String getFormattedAlertMessageHTML(IAlertMessage alertMessage)
  {
    StringBuffer buffer = fillHeader(alertMessage.getTitle());
    fillContent(alertMessage, buffer);
    buffer.append("\n</body>");
    buffer.append("\n</html>");
    return buffer.toString();
  }

  public static String getFormattedAlertMessageHTML(IAlertMessage alertMessage, AlertregistryVO info)
  {
    StringBuffer buffer = fillHeader(alertMessage.getTitle(), info.getAlertname());
    fillContent(alertMessage, buffer, info.getAlertname());
    buffer.append("\n</body>");
    buffer.append("\n</html>");
    return buffer.toString();
  }

  private static void fillContent(IAlertMessage alertMessage, StringBuffer buffer, String alertname)
  {
    if (alertMessage.getTitle() != null) {
      buffer.append("<h2 align=\"center\">" + alertname + "--[" + alertMessage.getTitle() + "]</h2>");
    }

    if (alertMessage.getTop() != null) {
      StringBuffer top = new StringBuffer();
      for (int i = 0; i < alertMessage.getTop().length; i++) {
        top.append(alertMessage.getTop()[i] + " ");
      }
      buffer.append("<p align=\"justify\">" + top + "</p> ");
    }
    Logger.debug("Parsing table.");
    buffer.append(getTableAlertMessage(alertMessage));

    if (alertMessage.getBottom() != null) {
      StringBuffer bottom = new StringBuffer();
      for (int i = 0; i < alertMessage.getBottom().length; i++) {
        bottom.append(alertMessage.getBottom()[i] + " ");
      }
      buffer.append("<p align=\"justify\"> " + bottom.toString() + " </p>");
    }
  }

  private static StringBuffer fillHeader(String msgTilte) {
    StringBuffer buffer = new StringBuffer();

    buffer.append("<html>\n<title>");
    buffer.append(msgTilte);
    buffer.append("</title>\n<head>");
    buffer.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" >");
    buffer.append("</head>\n");
    buffer.append("<body>\n");
    return buffer;
  }

  private static StringBuffer fillHeader(String title, String alertname)
  {
    StringBuffer buffer = new StringBuffer();

    buffer.append("<html>\n<title>[");
    buffer.append(title + "]--" + alertname);
    buffer.append("</title>\n<head>");
    buffer.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" >");
    buffer.append("</head>\n");
    buffer.append("<body>\n");
    return buffer;
  }

  public static String getFormattedalertMessageHTMs(IAlertMessage[] alertMessage)
  {
    Logger.debug("Entering HtmlCreater.getFormattedalertMessageHTML2()");
    StringBuffer buffer = fillHeader(alertMessage[0].getTitle());
    for (int k = 0; k < alertMessage.length; k++) {
      Logger.debug("Formatted Messages start: " + k);
      if (alertMessage[k] == null)
        continue;
      fillContent(alertMessage[k], buffer);
    }
    buffer.append("\n</body>");
    buffer.append("\n</html>");
    Logger.debug("Leaving HtmlCreater.getFormattedalertMessageHTML2()");
    return buffer.toString();
  }

  private static void fillContent(IAlertMessage alertMessage, StringBuffer buffer)
  {
    if (alertMessage.getTitle() != null) {
      buffer.append("<h2 align=\"center\">" + alertMessage.getTitle() + "</h2>");
    }

    if (alertMessage.getTop() != null) {
      StringBuffer top = new StringBuffer();
      for (int i = 0; i < alertMessage.getTop().length; i++) {
        top.append(alertMessage.getTop()[i] + " ");
      }
      buffer.append("<p align=\"justify\">" + top + "</p> ");
    }
    Logger.debug("Parsing table.");
    buffer.append(getTableAlertMessage(alertMessage));

    if (alertMessage.getBottom() != null) {
      StringBuffer bottom = new StringBuffer();
      for (int i = 0; i < alertMessage.getBottom().length; i++) {
        bottom.append(alertMessage.getBottom()[i] + " ");
      }
      buffer.append("<p align=\"justify\"> " + bottom.toString() + " </p>");
    }
  }

  public static String getStringAlertMessageHTML(String message)
  {
    StringBuffer buffer = fillHeader("alert message");

    if (message != null) {
      buffer.append("<h1 align=\"center\">" + message + "</h1>");
    }
    buffer.append("\n</body>");
    buffer.append("\n</html>");
    return buffer.toString();
  }

  public static String getStringAlertMessageHTML2(String[] message)
  {
    StringBuffer buffer = fillHeader("alert message");
    for (int i = 0; i < message.length; i++)
    {
      if (message[i] != null) {
        buffer.append("<p> <h1 align=\"center\">" + message[i] + "</h1></p>");
      }
    }
    buffer.append("\n</body>");
    buffer.append("\n</html>");
    return buffer.toString();
  }

  public static String getTableAlertMessage(IAlertMessage alertMessage)
  {
    Logger.debug("Entering HtmlCreater.getTableAlertMessage()");
    String width = "100";
    if ((alertMessage instanceof IAlertMessage1)) {
      width = ((IAlertMessage1)alertMessage).getTableWidth();
      try
      {
        Integer.valueOf(width);
      } catch (Exception e) {
        Logger.warn("Illegal character string representing the width.and use the defautl value 100");

        width = "100";
      }
    }
    float[] columnWidth = transvertColumnWidth(alertMessage);
    StringBuffer buffer = new StringBuffer();

    if (alertMessage.getBodyFields() != null) {
      buffer.append("<table border=\"1\" width=\"" + width + "%\" cellspacing=\"0\" cellpadding=\"0\" style=\"border-collapse: collapse\" bordercolor=\"#000000\">");

      buffer.append("<tr>");
      for (int i = 0; i < alertMessage.getBodyFields().length; i++) {
        buffer.append("<td width=\"" + columnWidth[i] * 100.0F + "%\" align=\"center\" >" + alertMessage.getBodyFields()[i] + "</td>");
      }

      for (int i = alertMessage.getBodyFields().length; i < columnWidth.length; i++) {
        buffer.append("<td width=\"" + columnWidth[i] * 100.0F + "%\" align=\"center\" >" + "" + "</td>");
      }

      buffer.append("</tr>");
      if (alertMessage.getBodyValue() != null)
      {
        if ((alertMessage instanceof IAlertMessage2))
          renderTableBody2((IAlertMessage2)alertMessage, columnWidth, buffer);
        else
          renderTableBody(alertMessage, columnWidth, buffer);
      }
      buffer.append("</table>");
    }
    Logger.debug("leaving HtmlCreater.getTableAlertMessage()");
    return buffer.toString();
  }

  private static void renderTableBody(IAlertMessage alertMessage, float[] columnWidth, StringBuffer buffer)
  {
    for (int i = 0; i < alertMessage.getBodyValue().length; i++) {
      buffer.append("<tr>");
      if (alertMessage.getBodyValue()[i] != null) {
        for (int j = 0; j < alertMessage.getBodyValue()[i].length; j++) {
          Object value = alertMessage.getBodyValue()[i][j];
          String columnAlignTag = getAlignByType(getValueType(value));
          buffer.append("<td width=\"" + columnWidth[j] * 100.0F + "%\" " + columnAlignTag + " >" + (value != null ? value.toString() : "") + "</td>");
        }
      }

      for (int j = alertMessage.getBodyValue()[i].length; j < columnWidth.length; j++) {
        buffer.append("<td width=\"" + columnWidth[j] * 100.0F + "%\" align=\"center\" >" + "" + "</td>");
      }

      buffer.append("</tr>");
    }
  }

  private static void renderTableBody2(IAlertMessage2 alertMessage, float[] columnWidth, StringBuffer buffer)
  {
    String[] columnProperty = new String[columnWidth.length];
    String strNull = alertMessage.getNullPresent();
    String strOmit = alertMessage.getOmitPresent();
    for (int i = 0; i < alertMessage.getBodyValue().length; i++) {
      buffer.append("<tr>");
      if (alertMessage.getBodyValue()[i] != null) {
        for (int j = 0; j < alertMessage.getBodyValue()[i].length; j++) {
          Object value = alertMessage.getBodyValue()[i][j];
          int type = alertMessage.getBodyColumnType()[j];
          if (i == 0) {
            columnProperty = initColumnProperty(alertMessage);
          }
          String columnAlignTag = null == columnProperty[j] ? " align=\"center\" " : columnProperty[j];

          buffer.append("<td width=\"" + columnWidth[j] * 100.0F + "%\" " + columnAlignTag + " >" + (value == null ? "" : strNull != null ? strNull : wrapValue(value, type)) + "</td>");
        }

      }

      for (int j = alertMessage.getBodyValue()[i].length; j < columnWidth.length; j++) {
        buffer.append("<td width=\"" + columnWidth[j] * 100.0F + "%\" align=\"center\" >" + (strOmit == null ? "" : strOmit) + "</td>");
      }

      buffer.append("</tr>");
    }
  }

  protected static String wrapValue(Object value, int type)
  {
    String res = "";
    switch (type) {
    case 5:
      res = getInstanceOfNumberFormat().format(value);
      break;
    case 6:
      res = NumberFormat.getCurrencyInstance().format(value);

      break;
    default:
      res = value.toString();
    }

    return res;
  }

  private static NumberFormat getInstanceOfNumberFormat() {
    NumberFormat format = NumberFormat.getInstance();

    format.setMaximumFractionDigits(18);
    return format;
  }

  private static String[] initColumnProperty(IAlertMessage2 alertMessage2)
  {
    String[] columnprop = new String[alertMessage2.getBodyFields().length];
    if (alertMessage2 != null) {
      int[] types = alertMessage2.getBodyColumnType();
      for (int i = 0; i < types.length; i++) {
        columnprop[i] = getAlignByType(types[i]);
      }
    }
    return columnprop;
  }

  private static String getAlignByType(int type) {
    String alignMethod = "";
    switch (type) {
    case 1:
    case 2:
      alignMethod = " align=\"center\" ";
      break;
    case 3:
    case 4:
    case 5:
    case 6:
      alignMethod = " align=\"right\" ";
      break;
    case 0:
      alignMethod = " align=\"left\" ";
      break;
    case -1:
      alignMethod = " align=\"center\" ";
      break;
    default:
      alignMethod = " align=\"center\" ";
    }

    return alignMethod;
  }

  private static byte getValueType(Object value)
  {
    if ((value instanceof String))
      return 0;
    if (((value instanceof UFBoolean)) || ((value instanceof Boolean)))
      return 2;
    if (((value instanceof Integer)) || ((value instanceof Long)) || ((value instanceof Short)) || ((value instanceof Byte)))
    {
      return 3;
    }if (((value instanceof UFDouble)) || ((value instanceof Double)) || ((value instanceof Float)))
    {
      return 4;
    }if (((value instanceof UFDate)) || ((value instanceof UFDateTime)) || ((value instanceof Date)))
      return 1;
    return -1;
  }

  private static float[] transvertColumnWidth(IAlertMessage alertMessage)
  {
    Logger.debug("Entering HtmlCreater.transvertColumnWidth()");
    int columnCount = 0;
    if (alertMessage.getBodyFields() != null) {
      columnCount = alertMessage.getBodyFields().length;
    }
    if (alertMessage.getBodyValue() != null) {
      for (int i = 0; i < alertMessage.getBodyValue().length; i++) {
        columnCount = Math.max(columnCount, alertMessage.getBodyValue()[i].length);
      }
    }
    float[] result = new float[columnCount];
    if (alertMessage.getBodyWidths() != null) {
      if (alertMessage.getBodyWidths().length < columnCount) {
        for (int i = 0; i < alertMessage.getBodyWidths().length; i++) {
          result[i] = (alertMessage.getBodyWidths()[i] * (alertMessage.getBodyWidths().length / columnCount));
        }

        for (int i = alertMessage.getBodyWidths().length; i < columnCount; i++)
          result[i] = (alertMessage.getBodyWidths().length / columnCount);
      }
      else if (alertMessage.getBodyWidths().length > columnCount) {
        for (int i = 0; i < columnCount; i++)
          result[i] = (alertMessage.getBodyWidths()[i] * (columnCount / alertMessage.getBodyWidths().length));
      }
      else
      {
        result = alertMessage.getBodyWidths();
      }
    }
    else for (int i = 0; i < columnCount; i++) {
        result[i] = (1 / columnCount);
      }

    Logger.debug("leaving HtmlCreater.transvertColumnWidth()");
    return result;
  }
}