package nc.ui.fa.fa20120301;

import nc.vo.fa.fa20120201.CardAllVO;
import nc.vo.pub.ValueObject;

public abstract interface INewCard
{
  public abstract void setCard(CardAllVO paramCardAllVO, ValueObject paramValueObject)
    throws Throwable;
}