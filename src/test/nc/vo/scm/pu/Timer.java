package nc.vo.scm.pu;

import java.io.PrintStream;
import java.util.Vector;
import nc.vo.pub.lang.UFDouble;

public class Timer
{
  private long startTime;
  private long endTime;
  private Vector vecPhaseName = new Vector();
  private Vector vecPhaseTime = new Vector();

  public void addExecutePhase(String sTaskHint)
  {
    stop();

    this.vecPhaseName.add(sTaskHint);
    this.vecPhaseTime.add(new Long(getTime()));

    start();
  }

  public long getTime()
  {
    return this.endTime - this.startTime;
  }

  public void showAllExecutePhase(String sTaskHint)
  {
    stop();

    if (PuPubVO.DEBUG) {
      int iSize = this.vecPhaseTime.size();
      if (iSize == 0) {
        return;
      }

      long lAllTime = 0L;
      for (int i = 0; i < iSize; i++) {
        lAllTime += ((Long)this.vecPhaseTime.get(i)).longValue();
      }

      System.out.println("\n" + sTaskHint + "总时间：" + lAllTime);
      for (int i = 0; i < iSize; i++) {
        System.out.println(this.vecPhaseName.get(i) + " 消耗时间：" + this.vecPhaseTime.get(i) + " 占总时间：" + new UFDouble(((Long)this.vecPhaseTime.get(i)).longValue() * 100.0D / (lAllTime * 1.0D), 2) + "%");
      }

      System.out.println("\n");
    }
  }

  public void showExecuteTime(String sTaskHint)
  {
    stop();
    showTime(sTaskHint);
    start();
  }

  public void showTime(String sTaskHint)
  {
    long lTime = getTime();
    if (PuPubVO.DEBUG)
      System.out.println("NCMMOUT@@:-）执行<" + sTaskHint + ">消耗的时间为：" + lTime / 60000L + "分" + lTime / 1000L % 60L + "秒" + lTime % 1000L + "毫秒");
  }

  public void start()
  {
    this.startTime = System.currentTimeMillis();
  }

  public void start(String sTaskHint)
  {
    start();
    if (PuPubVO.DEBUG)
      System.out.println("NCMMOUT@@:-）开始执行" + sTaskHint);
  }

  public void stop()
  {
    this.endTime = System.currentTimeMillis();
  }

  public void stopAndShow(String sTaskHint)
  {
    stop();
    showTime(sTaskHint);
  }
}