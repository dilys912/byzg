package nc.ui.bd.pub;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import nc.ui.trade.businessaction.IBusinessController;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.voutils.IFilter;

public class FilterNullBodyVODecorator extends AbstractDecorator
  implements IFilter
{
  private HashSet exceptFields;
  private ArrayList toBeCheckedFields;

  public FilterNullBodyVODecorator(IBusinessController businessAction, String[] exceptionFields)
  {
    super(businessAction);
    this.exceptFields = new HashSet();
    if (exceptionFields == null)
      this.exceptFields = new HashSet();
    else
      this.exceptFields.addAll(Arrays.asList(exceptionFields));
  }

  private void doFilter(AggregatedValueObject billVO)
  {
    if (billVO == null)
      return;
    if ((billVO.getChildrenVO() != null) && (billVO.getChildrenVO().length != 0))
    {
      Class c = billVO.getChildrenVO()[0].getClass();

      SuperVO[] childrenvos = (SuperVO[])(SuperVO[])billVO.getChildrenVO();
      ArrayList al = new ArrayList();
      for (int i = 0; i < childrenvos.length; i++) {
        if (accept(childrenvos[i])) {
          al.add(childrenvos[i]);
        }
      }
      billVO.setChildrenVO((SuperVO[])(SuperVO[])al.toArray((SuperVO[])(SuperVO[])Array.newInstance(c, al.size())));
    }
  }

  public boolean accept(Object o)
  {
    if (o == null)
      return false;
    SuperVO vo = (SuperVO)o;

    this.toBeCheckedFields = getToBeCheckedFields(vo);

    for (int i = 0; i < this.toBeCheckedFields.size(); i++) {
      if (vo.getAttributeValue((String)this.toBeCheckedFields.get(i)) != null)
        return true;
    }
    return false;
  }

  private ArrayList initToBeCheckedFields(SuperVO vo)
  {
    String[] allfields = vo.getAttributeNames();
    ArrayList al = new ArrayList();

    for (int i = 0; i < allfields.length; i++) {
      if (!this.exceptFields.contains(allfields[i])) {
        al.add(allfields[i]);
      }
    }
    return al;
  }

  private void filterUFBooleanFields(SuperVO vo, ArrayList al)
  {
    ArrayList alNotUFboolean = new ArrayList();
    for (int i = 0; i < al.size(); i++) {
      Object o = vo.getAttributeValue((String)al.get(i));
      if ((o != null) && ((o instanceof UFBoolean)))
        continue;
      alNotUFboolean.add(al.get(i));
    }

    al = alNotUFboolean;
  }

  public ArrayList getToBeCheckedFields(SuperVO vo)
  {
    if (this.toBeCheckedFields == null) {
      this.toBeCheckedFields = initToBeCheckedFields(vo);
    }

    return this.toBeCheckedFields;
  }

  public AggregatedValueObject save(AggregatedValueObject billVO, String billType, String billDate, Object userObj, AggregatedValueObject checkVo)
    throws Exception
  {
    doFilter(billVO);

    doFilter(checkVo);

    return super.save(billVO, billType, billDate, userObj, checkVo);
  }
}