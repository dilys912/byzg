package nc.vo.pub.querymodel;

import com.borland.dx.dataset.DataSetData;
import com.borland.dx.dataset.StorageDataSet;
import java.io.Serializable;
import java.util.Hashtable;
import nc.vo.iuforeport.businessquery.QueryBaseDef;

public class DataSetContainer
  implements Serializable
{
  private DataSetData m_data = null;

  QueryBaseDef m_qbd = null;

  private Hashtable m_hashParam = null;

  public DataSetData getDataSetData()
  {
    return this.m_data;
  }

  public void setDataSetData(DataSetData data)
  {
    this.m_data = data;
  }

  public StorageDataSet getDataSet()
  {
    StorageDataSet dataSet = null;
    if (this.m_data != null) {
      dataSet = new StorageDataSet();
      this.m_data.loadDataSet(dataSet);
    }
    return dataSet;
  }

  public void setDataSet(StorageDataSet dataSet)
  {
    this.m_data = (dataSet == null ? null : DataSetData.extractDataSet(dataSet));
  }

  public QueryBaseDef getQbd() {
    return this.m_qbd;
  }

  public void setQbd(QueryBaseDef qbd) {
    this.m_qbd = qbd;
  }

  public Hashtable getHashParam() {
    return this.m_hashParam;
  }

  public void setHashParam(Hashtable hashtable) {
    this.m_hashParam = hashtable;
  }
}