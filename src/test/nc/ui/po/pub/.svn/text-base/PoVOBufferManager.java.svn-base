package nc.ui.po.pub;

import java.awt.Container;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import nc.ui.pub.bill.IBillRelaSortListener;
import nc.vo.po.OrderHeaderVO;
import nc.vo.po.OrderVO;

public class PoVOBufferManager
  implements Serializable, IBillRelaSortListener
{
  private ArrayList m_listOrder = new ArrayList();

  private int m_nPos = 0;
  private int m_nPreviousPos = -1;

  public void addVO(OrderVO voNew)
  {
    this.m_listOrder.add(voNew);
  }

  public void addVOs(OrderVO[] voaNew)
  {
    if (voaNew == null) {
      return;
    }

    int iLen = voaNew.length;
    for (int i = 0; i < iLen; i++)
      this.m_listOrder.add(voaNew[i]);
  }

  public void clear()
  {
    this.m_listOrder.clear();
  }

  private int getCorrectVOPos(int iPos)
  {
    if ((iPos < 0) || (getLength() == 0))
      return 0;
    if (iPos > getLength() - 1) {
      return getLength() - 1;
    }
    return iPos;
  }

  public OrderHeaderVO getHeadVOAt(int iPos)
  {
    if (!isCorrectVOPos(iPos)) {
      return null;
    }

    return ((OrderVO)this.m_listOrder.get(getCorrectVOPos(iPos))).getHeadVO();
  }

  public int getLength()
  {
    return this.m_listOrder.size();
  }

  public int getPreviousVOPos()
  {
    return getCorrectVOPos(this.m_nPreviousPos);
  }

  public OrderVO getVOAt_LoadItemNo(int iPos)
  {
    if (!isCorrectVOPos(iPos)) {
      return null;
    }

    return (OrderVO)this.m_listOrder.get(getCorrectVOPos(iPos));
  }

  public OrderVO getVOAt_LoadItemYes(Container cont, int iPos)
  {
    if (!isCorrectVOPos(iPos)) {
      return null;
    }

    OrderVO voOrder = getVOAt_LoadItemNo(iPos);

    PoLoadDataTool.loadItemsForOrderVOs(cont, new OrderVO[] { voOrder });

    return getVOAt_LoadItemNo(iPos);
  }

  public int getVOPos()
  {
    return getCorrectVOPos(this.m_nPos);
  }

  private boolean isCorrectVOPos(int iPos)
  {
    return (iPos >= 0) && (iPos <= getLength() - 1);
  }

  public void removeAt(int iPos)
  {
    if (!isCorrectVOPos(iPos)) {
      return;
    }

    this.m_listOrder.remove(iPos);
  }

  public void resetVOs(OrderVO[] voaNew)
  {
    clear();

    if (voaNew == null) {
      return;
    }

    int iLen = voaNew.length;
    for (int i = 0; i < iLen; i++)
      this.m_listOrder.add(voaNew[i]);
  }

  public void resetVOs(PoVOBufferManager voNewBuf)
  {
    clear();

    if ((voNewBuf == null) || (voNewBuf.getLength() == 0)) {
      return;
    }

    int iLen = voNewBuf.getLength();
    for (int i = 0; i < iLen; i++)
      this.m_listOrder.add(voNewBuf.getVOAt_LoadItemNo(i));
  }

  public void setPreviousVOPos(int iPreviousPos)
  {
    this.m_nPreviousPos = getCorrectVOPos(iPreviousPos);
  }

  public void setVOAt(int iPos, OrderVO voNew)
  {
    if (getLength() == 0) {
      return;
    }

    this.m_listOrder.set(iPos, voNew);
  }

  public void setVOPos(int iPos)
  {
    this.m_nPos = getCorrectVOPos(iPos);
  }

  public List getRelaSortObject()
  {
    return this.m_listOrder;
  }
}