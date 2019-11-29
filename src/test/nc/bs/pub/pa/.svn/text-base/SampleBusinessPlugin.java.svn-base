package nc.bs.pub.pa;

import nc.bs.logging.Logger;
import nc.bs.pub.pa.html.IAlertMessage;
import nc.bs.pub.pa.html.SampleAlertMessage;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.pa.Key;
/**   
* 预警平台插件接口实现示范类。兼预警平台的测试类.。  
*    
*/
public class SampleBusinessPlugin
  implements IBusinessPlugin
{
  public int getImplmentsType()
  {
    return 3;
  }

//since v5.0 此接口方法没有用!返回null亦可。
  public Key[] getKeys()
  {
    return null;
  }

//since v5.0 此接口方法没有用!返回null亦可。
  public String getTypeDescription()
  {
    return "yqq";
  }

//since v5.0 此接口方法没有用!返回null亦可。
  public String getTypeName()
  {
    return "qq";
  }
  
  
//这里虽然实现了，但是由于返回的是格式化消息，所以等于没有起作用 
  public String implementReturnMessage(Key[] keys, String corpPK, UFDate clientLoginDate)
    throws BusinessException
  {
	  
	//业务实现。如果要返回格式化的HTML消息,请参考nc.bs.pub.pa.html.IAlertMessage
    double testValue = 10.0D;
    double lowStorageVolume = -1.0D; 
    double highStorageVolume = -1.0D;
    if ((keys != null) && (keys.length > 0)) {
      for (int i = 0; i < keys.length; i++) {
        if (keys[i].getName().equals("lowVolume"))
          lowStorageVolume = new Double(keys[i].getValue().toString()).doubleValue();
        else if (keys[i].getName().equals("highVolume")) {
          highStorageVolume = new Double(keys[i].getValue().toString()).doubleValue();
        }
      }
    }

    if ((lowStorageVolume == -1.0D) || (highStorageVolume == -1.0D)) {
      Logger.error("预警类型配置未完成");
      return null;
    }
    if (testValue < lowStorageVolume) return "预警平台测试示例:库存安全最低量超过限制";
    if (testValue > highStorageVolume) return "预警平台测试示例:库存安全最高量超过限制";

    return null;
  }

  public boolean implementWriteFile(Key[] keys, String fileName, String corpPK, UFDate clientLoginDate) throws BusinessException
  {
    return false;
  }

  public Object implementReturnObject(Key[] keys, String corpPK, UFDate clientLoginDate) throws BusinessException
  {
    return null;
  }

  public IAlertMessage implementReturnFormatMsg(Key[] keys, String corpPK, UFDate clientLoginDate)
    throws BusinessException
  {
    return new SampleAlertMessage();    // FIXME 直接返回 一个格式化文件   
  }
}


/*
*这个插件例子，是返回一个格式化的消息，直接new了一个格式化消息的实例类。而对于其中实现的方法*implementReturnMessage没有用途，这里只是个示范。
*/