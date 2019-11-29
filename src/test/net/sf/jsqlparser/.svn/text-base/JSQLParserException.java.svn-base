package net.sf.jsqlparser;

import java.io.PrintStream;
import java.io.PrintWriter;

public class JSQLParserException extends Exception
{
  private Throwable cause = null;

  public JSQLParserException()
  {
  }

  public JSQLParserException(String arg0) {
    super(arg0);
  }

  public JSQLParserException(Throwable arg0) {
    this.cause = arg0;
  }

  public JSQLParserException(String arg0, Throwable arg1) {
    super(arg0);
    this.cause = arg1;
  }

  public Throwable getCause() {
    return this.cause;
  }

  public void printStackTrace() {
    printStackTrace(System.err);
  }

  public void printStackTrace(PrintWriter pw) {
    super.printStackTrace(pw);
    if (this.cause != null) {
      pw.println("Caused by:");
      this.cause.printStackTrace(pw);
    }
  }

  public void printStackTrace(PrintStream ps) {
    super.printStackTrace(ps);
    if (this.cause != null) {
      ps.println("Caused by:");
      this.cause.printStackTrace(ps);
    }
  }
}