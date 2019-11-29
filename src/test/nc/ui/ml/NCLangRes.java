package nc.ui.ml;

import java.util.Locale;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.ml.IMultiLanguageService;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.Language;

public class NCLangRes extends AbstractNCLangRes
{
  private static NCLangRes instance = null;

  private Language currLang = null;

  public static NCLangRes getInstance()
  {
    if (instance == null) {
      instance = new NCLangRes();
    }
    return instance;
  }

  public Language[] getAllLanguages()
  {
    if (this.allLanguages == null) {
      try {
        this.allLanguages = ((IMultiLanguageService)NCLocator.getInstance().lookup(IMultiLanguageService.class.getName())).getAllLanguages();
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    return this.allLanguages;
  }

  public Language getCurrLanguage()
  {
    return this.currLang;
  }

  public void setCurrLanguage(Language lang) {
    this.currLang = lang;
    Locale.setDefault(lang.getLocal());
  }
}