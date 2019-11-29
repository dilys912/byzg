package nc.vo.ia.pub;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

public class TransferException extends RuntimeException
{
  private static final long serialVersionUID = 1832358167833609276L;
  private String stackTrace = null;

  public TransferException(Throwable exception) {
    this.stackTrace = getStackTrace(exception);
  }

  private String getStackTrace(Throwable ex) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    ex.printStackTrace(pw);
    return sw.getBuffer().toString();
  }

  public void printStackTrace() {
    if (this.stackTrace != null) {
      printStackTrace(System.err);
    }
    else
      super.printStackTrace();
  }

  public void printStackTrace(PrintWriter s)
  {
    if (this.stackTrace != null) {
      synchronized (s) {
        s.print(this.stackTrace);
      }
    }
    else
      super.printStackTrace(s);
  }

  public void printStackTrace(PrintStream s)
  {
    if (this.stackTrace != null) {
      synchronized (s) {
        s.print(this.stackTrace);
      }
    }
    else
      super.printStackTrace(s);
  }

  public String getMessage()
  {
    return this.stackTrace;
  }
}