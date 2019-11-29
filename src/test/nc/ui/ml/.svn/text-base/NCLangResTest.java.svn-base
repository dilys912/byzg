package nc.ui.ml;

import java.io.PrintStream;
import nc.vo.ml.Language;

public class NCLangResTest
{
  public static void main(String[] args)
  {
    Language lang = new Language();
    lang.setDisplayName("Ó¢ÎÄ");
    lang.setCode("sis");

    lang.setTranslatorClassName("nc.vo.ml.translator.ForeignLanguageTranslator");
    NCLangRes.getInstance().setCurrLanguage(lang);

    String s = NCLangRes.getInstance().getString("uap", "ÖÐÎÄ", null);
    System.out.println(s);
  }

  public static String uniCode2Gb(String src)
  {
    byte[] b = src.getBytes();
    int n = b.length;
    char[] c = new char[n];
    for (int i = 0; i < n; i++)
      c[i] = (char)((short)b[i] & 0xFF);
    return new String(c);
  }
}