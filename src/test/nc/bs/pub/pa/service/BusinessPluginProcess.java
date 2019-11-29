package nc.bs.pub.pa.service;

import java.io.File;
import nc.bs.framework.server.util.NewObjectService;
import nc.bs.logging.Logger;
import nc.bs.pub.pa.IBusinessPlugin;
import nc.bs.pub.pa.IBusinessPlugin2;
import nc.bs.pub.pa.IBusinessPlugin3;
import nc.bs.pub.pa.PaConstant;
import nc.bs.pub.pa.html.HtmlCreater;
import nc.bs.pub.pa.html.IAlertMessage;
import nc.bs.pub.pa.html.ObjectFile;
import nc.bs.pub.pa.html.TextFile;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.pa.AlertregistryVO;
import nc.vo.pub.pa.AlerttypeVO;
import nc.vo.pub.pa.CurrEnvVO;
import nc.vo.pub.pa.PreAlertException;

public class BusinessPluginProcess
{
  private final String ERROR_HINT = "生成预警信息文件时出现错误!";

  private final String HINT_STRING_RETURN = "业务插件以字符串方式返回!";

  private final String HINT_OBJECT_RETURN = "业务插件以对象方式返回!";

  private final String HINT_FORMATMSG_RETURN = "业务插件实现格式化字符串方式返回!";

  private final String HINT_WRITEFILE_RETURN = "业务插件实现写入文件方式返回!";

  public int callBusinessPlugin(AlerttypeVO type, AlertregistryVO info, String fileName, UFDate loginDate, CurrEnvVO env)
  {
    String pluginName = type.getBusi_plugin();
    Logger.info("[预警平台]开始执行条目" + info.getAlertname() + "......对应业务插件为" + pluginName + "----");

    Object newObject = null;
    try {
      newObject = NewObjectService.newInstance(type.getBelong_system().toLowerCase(), pluginName);
    } catch (Exception e) {
      Logger.error("[ERROR][预警平台]执行条目:" + info.getAlertname() + "时, 系统中实例化插件类错误: " + pluginName, e);
      return -1;
    }

    File dictionary = new File(PaConstant.FILE_BASE_ABSOLUTE_PATH);
    if (!dictionary.exists()) {
      dictionary.mkdirs();
    }
    if ((newObject instanceof IBusinessPlugin2))
    {
      if (env != null) {
        Logger.debug("插件实现了接口IBusinessPlugin2");
        return callBusinessPlugin2(type, info, fileName, loginDate, env, (IBusinessPlugin2)newObject);
      }

      Logger.warn("条目:" + info.getAlertname() + ".插件实现IBusinessPlugin2.但得到的客户端环境变量为空，故仍按接口1处理");
      return callBusinessPlugin1(type, info, fileName, loginDate, (IBusinessPlugin)newObject);
    }
    if ((newObject instanceof IBusinessPlugin3)) {
      Logger.debug(" 插件实现接口IBusinessPlugin3");
      return callBusinessPlugin3(type, info, fileName, loginDate, (IBusinessPlugin3)newObject);
    }if ((newObject instanceof IBusinessPlugin)) {
      Logger.debug(" 插件实现接口IBusinessPlugin");
      return callBusinessPlugin1(type, info, fileName, loginDate, (IBusinessPlugin)newObject);
    }
    Logger.error("插件没有实现指定的接口");

    return -1;
  }

  private int callBusinessPlugin1(AlerttypeVO type, AlertregistryVO info, String fileName, UFDate loginDate, IBusinessPlugin plugin)
  {
    try
    {
      boolean bSucceed = false;
      if (plugin.getImplmentsType() == 0) {
        Logger.debug("业务插件以字符串方式返回!");
        String msgContent = plugin.implementReturnMessage(PaConstant.transVO2Key(type.getAlertVariables()), info.getPk_corp(), loginDate);

        if (msgContent != null)
        {
          try
          {
            String htmlContent = HtmlCreater.getStringAlertMessageHTML(msgContent);
            String htmlFileNameString = PaConstant.FILE_BASE_ABSOLUTE_PATH + fileName + ".html";
            bSucceed = TextFile.write(htmlContent, htmlFileNameString, "UTF-8");
          } catch (Exception e) {
            doLogErrorCreateFile(info, e);
            throw new PreAlertException("生成预警信息文件时出现错误!", e);
          }
        } else {
          doLogWarnNull(info);
          return -1;
        }
      } else if (plugin.getImplmentsType() == 1) {
        Logger.debug("业务插件以对象方式返回!");
        Object objContent = plugin.implementReturnObject(PaConstant.transVO2Key(type.getAlertVariables()), info.getPk_corp(), loginDate);

        if (objContent != null) {
          try {
            String objectfilename = PaConstant.FILE_BASE_ABSOLUTE_PATH + fileName;
            bSucceed = ObjectFile.write(objContent, objectfilename);
          } catch (Exception e) {
            doLogErrorCreateFile(info, e);
            throw new PreAlertException("生成预警信息文件时出现错误!", e);
          }
        }
        else
          return -1;
      }
      else if (plugin.getImplmentsType() == 2) {
        Logger.debug("业务插件实现写入文件方式。");
        String writeFilename = PaConstant.FILE_BASE_ABSOLUTE_PATH + fileName;
        bSucceed = plugin.implementWriteFile(PaConstant.transVO2Key(type.getAlertVariables()), writeFilename, info.getPk_corp(), loginDate);

        if (!bSucceed) {
          doLogWarnNull(info);
          return -1;
        }
      } else if (plugin.getImplmentsType() == 3) {
        Logger.debug("业务插件实现格式化字符串方式返回!");
        IAlertMessage msgContent = plugin.implementReturnFormatMsg(PaConstant.transVO2Key(type.getAlertVariables()), info.getPk_corp(), loginDate);

        if (msgContent != null)
        {
          try
          {
            String htmlContent = HtmlCreater.getFormattedAlertMessageHTML(msgContent, info);
            String htmlFileName = PaConstant.FILE_BASE_ABSOLUTE_PATH + fileName + ".html";
            bSucceed = TextFile.write(htmlContent, htmlFileName, "UTF-8");
          } catch (Exception e) {
            doLogErrorCreateFile(info, e);
            throw new PreAlertException(e.getMessage() + "生成预警信息文件时出现错误!", e);
          }
        } else {
          doLogWarnNull(info);
          return -1;
        }
      }

      if (bSucceed == true) {
        Logger.info("-------调用插件--" + type.getBusi_plugin() + "---成功!");
        return plugin.getImplmentsType();
      }
      Logger.warn("-------调用插件----" + type.getBusi_plugin() + "---失败!");
      return -1;
    }
    catch (Exception e) {
      Logger.error("[预警平台] 调用业务插件 " + type.getBusi_plugin() + " 生成预警信息时发生错误!", e);
    }return -1;
  }

  private int callBusinessPlugin2(AlerttypeVO type, AlertregistryVO info, String fileName, UFDate loginDate, CurrEnvVO env, IBusinessPlugin2 plugin)
  {
    try
    {
      boolean bSucceed = false;
      if (plugin.getImplmentsType() == 0) {
        Logger.debug("业务插件以字符串方式返回!");
        String[] msgContent = plugin.implementReturnMessage(PaConstant.transVO2Key(type.getAlertVariables()), env, loginDate);

        if (msgContent != null)
          try {
            String contentString = HtmlCreater.getStringAlertMessageHTML2(msgContent);
            String htmlFileNameString = PaConstant.FILE_BASE_ABSOLUTE_PATH + fileName + ".html";
            bSucceed = TextFile.write(contentString, htmlFileNameString, "UTF-8");
          } catch (Exception e) {
            doLogErrorCreateFile(info, e);
            throw new PreAlertException("生成预警信息文件时出现错误!", e);
          }
        else
          doLogWarnNull(info);
      }
      else if (plugin.getImplmentsType() == 1) {
        Logger.debug("业务插件以对象方式返回!");
        Object msgContent = plugin.implementReturnObject(PaConstant.transVO2Key(type.getAlertVariables()), env, loginDate);

        if (msgContent != null)
          try {
            String objecfilename = PaConstant.FILE_BASE_ABSOLUTE_PATH + fileName;
            bSucceed = ObjectFile.write(msgContent, objecfilename);
          } catch (Exception e) {
            throw new PreAlertException("生成预警信息文件时出现错误!", e);
          }
        else
          doLogWarnNull(info);
      }
      else if (plugin.getImplmentsType() == 2) {
        String writeFilename = PaConstant.FILE_BASE_ABSOLUTE_PATH + fileName;
        bSucceed = plugin.implementWriteFile(PaConstant.transVO2Key(type.getAlertVariables()), writeFilename, env, loginDate);

        if (!bSucceed)
          doLogWarnNull(info);
      }
      else if (plugin.getImplmentsType() == 3) {
        Logger.debug("业务插件实现格式化字符串方式返回!");
        IAlertMessage[] msgContent = plugin.implementReturnFormatMsg(PaConstant.transVO2Key(type.getAlertVariables()), env, loginDate);

        if (msgContent != null)
        {
          try
          {
            String content = HtmlCreater.getFormattedalertMessageHTMs(msgContent);
            String htmlFileName = PaConstant.FILE_BASE_ABSOLUTE_PATH + fileName + ".html";
            bSucceed = TextFile.write(content, htmlFileName, "UTF-8");
          } catch (Exception e) {
            doLogErrorCreateFile(info, e);
            throw new PreAlertException("生成预警信息文件时出现错误!" + e.getMessage(), e);
          }
        }
        else doLogWarnNull(info);
      }

      if (bSucceed == true) {
        Logger.info("-------调用插件--------" + type.getBusi_plugin() + "---成功!");
        return plugin.getImplmentsType();
      }
      Logger.warn("-------调用插件--------" + type.getBusi_plugin() + "---失败!");
      return -1;
    }
    catch (Exception e) {
      doLogErrorCreateFile(info, e);
    }return -1;
  }

  private int callBusinessPlugin3(AlerttypeVO type, AlertregistryVO info, String fileName, UFDate loginDate, IBusinessPlugin3 bp)
  {
    String accountPk = info.getAccountpk();
    try {
      boolean bSucceed = false;
      if (bp.getImplmentsType() == 0) {
        Logger.debug("业务插件以字符串方式返回!");
        String msgContent = bp.implementReturnMessage(PaConstant.transVO2Key(type.getAlertVariables()), info.getPk_corp(), accountPk, loginDate);

        if (msgContent != null)
        {
          try
          {
            String contentString = HtmlCreater.getStringAlertMessageHTML(msgContent);
            String htmlFileNameString = PaConstant.FILE_BASE_ABSOLUTE_PATH + fileName + ".html";
            bSucceed = TextFile.write(contentString, htmlFileNameString, "UTF-8");
          } catch (Exception e) {
            doLogErrorCreateFile(info, e);
            throw new PreAlertException("生成预警信息文件时出现错误!" + e.getMessage(), e);
          }
        }
        else doLogWarnNull(info);
      }
      else if (bp.getImplmentsType() == 1) {
        Logger.debug("业务插件以对象方式返回!");
        Object msgContent = bp.implementReturnObject(PaConstant.transVO2Key(type.getAlertVariables()), info.getPk_corp(), accountPk, loginDate);

        if (msgContent != null)
          try {
            String objectfilename = PaConstant.FILE_BASE_ABSOLUTE_PATH + fileName;
            bSucceed = ObjectFile.write(msgContent, objectfilename);
          } catch (Exception e) {
            doLogErrorCreateFile(info, e);
            throw new PreAlertException("生成预警信息文件时出现错误!" + e.getMessage(), e);
          }
        else
          doLogWarnNull(info);
      }
      else if (bp.getImplmentsType() == 2) {
        Logger.debug("业务插件实现写入文件方式返回!");
        String writeFilename = PaConstant.FILE_BASE_ABSOLUTE_PATH + fileName;
        bSucceed = bp.implementWriteFile(PaConstant.transVO2Key(type.getAlertVariables()), writeFilename, info.getPk_corp(), accountPk, loginDate);

        if (!bSucceed)
          doLogWarnNull(info);
      }
      else if (bp.getImplmentsType() == 3) {
        Logger.debug("业务插件实现格式化字符串方式返回!");
        IAlertMessage msgContent = bp.implementReturnFormatMsg(PaConstant.transVO2Key(type.getAlertVariables()), info.getPk_corp(), accountPk, loginDate);

        if (msgContent != null)
        {
          try
          {
            String content = HtmlCreater.getFormattedAlertMessageHTML(msgContent, info);
            String htmlFileName = PaConstant.FILE_BASE_ABSOLUTE_PATH + fileName + ".html";
            bSucceed = TextFile.write(content, htmlFileName, "UTF-8");
          } catch (Exception e) {
            doLogErrorCreateFile(info, e);
            throw new PreAlertException("生成预警信息文件时出现错误!" + e.getMessage(), e);
          }
        }
        else doLogWarnNull(info);
      }

      if (bSucceed == true) {
        Logger.info("-------调用插件--------" + type.getBusi_plugin() + "---成功!");
        return bp.getImplmentsType();
      }
      Logger.warn("-------调用插件--------" + type.getBusi_plugin() + "---失败!");
      return -1;
    }
    catch (Exception e) {
      doLogErrorCreateFile(info, e);
    }return -1;
  }

  private void doLogWarnNull(AlertregistryVO info)
  {
    Logger.warn("[Warning]条目:+" + info.getAlertname() + "...pk=" + info.getPrimaryKey() + "******没有产生预警信息文件,调用插件的返回值为空!****");
  }

  private void doLogErrorCreateFile(AlertregistryVO info, Exception e)
  {
    Logger.error("[ERROR][预警平台]条目:" + info.getAlertname() + "执行时,调用业务插件+" + info.getAlertTypeVo().getBusi_plugin() + "生成预警信息文件时出现错误!", e);
  }
}