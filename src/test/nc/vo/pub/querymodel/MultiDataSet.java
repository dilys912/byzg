package nc.vo.pub.querymodel;

import com.borland.dx.dataset.StorageDataSet;
import java.util.Hashtable;
import nc.vo.pub.ValidationException;
import nc.vo.pub.ValueObject;

public class MultiDataSet extends ValueObject
{
  private StorageDataSet m_ds;
  private StorageDataSet[] m_dsArray;
  private StorageDataSet m_dsBeforeRotate = null;

  private Hashtable hashParam = null;

  public StorageDataSet[] getAssisDataSets()
  {
    return this.m_dsArray;
  }

  public StorageDataSet getDataSet()
  {
    return this.m_ds;
  }

  public StorageDataSet getDsBeforeRotate()
  {
    return this.m_dsBeforeRotate;
  }

  public String getEntityName()
  {
    return "DataSetDataWithColumn";
  }

  public Hashtable getHashParam()
  {
    return this.hashParam;
  }

  public void setAssisDataSets(StorageDataSet[] dsArray)
  {
    this.m_dsArray = dsArray;
  }

  public void setDataSet(StorageDataSet ds)
  {
    this.m_ds = ds;
  }

  public void setDsBeforeRotate(StorageDataSet newDsBeforeRotate)
  {
    this.m_dsBeforeRotate = newDsBeforeRotate;
  }

  public void setHashParam(Hashtable newHashParam)
  {
    this.hashParam = newHashParam;
  }

  public void validate()
    throws ValidationException
  {
  }
}