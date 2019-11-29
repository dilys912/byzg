package nc.vo.ic.kl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ic.bill.FreeVO;

public class KlVO extends CircularlyAccessibleValueObject
{
  public static String SPREV_dynamic_num = "numberinday";
  public static String SPREV_dynamic_mny = "nmny000";
  public static String SPREV_dynamic_planmny = "nplanmny000";
  public String m_pk_tempb;
  public String m_cwarehousecode;
  public String m_cwarehousename;
  public String m_cinventoryid;
  public String m_cinventorycode;
  public String m_invname;
  public String m_invspec;
  public String m_invtype;
  public String m_measdocname;
  public String m_castunitname;
  public String m_vbatchcode;
  public UFDouble m_xcl;
  public UFDouble m_averageinnum7days;
  public UFDouble m_averageinnum30days;
  public UFDouble m_warnoutdays;
  public String m_cwarehouseclassid;
  public FreeVO m_freevo;
  public Hashtable m_htnumberindays = new Hashtable();
  public String m_pk_corp;
  public String custcode;
  public String custname;
  public String cvendorid;

  public KlVO()
  {
  }

  public KlVO(String newPk_tempb)
  {
    this.m_pk_tempb = newPk_tempb;
  }

  public Object clone()
  {
    Object o = null;
    try {
      o = super.clone(); } catch (Exception e) {
    }
    KlVO klfxB = (KlVO)o;

    return klfxB;
  }

  public String getEntityName()
  {
    return "KlfxB";
  }
  
  

	public String getCustcode() {
		return custcode;
	}
	
	public void setCustcode(String custcode) {
		this.custcode = custcode;
	}
	
	public String getCustname() {
		return custname;
	}
	
	public void setCustname(String custname) {
		this.custname = custname;
	}
	
	public String getCvendorid() {
		return cvendorid;
	}
	
	public void setCvendorid(String cvendorid) {
		this.cvendorid = cvendorid;
	}

public String getPrimaryKey()
  {
    return this.m_pk_tempb;
  }

  public void setPrimaryKey(String newPk_tempb)
  {
    this.m_pk_tempb = newPk_tempb;
  }

  public String getPk_tempb()
  {
    return this.m_pk_tempb;
  }

  public String getCwarehousecode()
  {
    return this.m_cwarehousecode;
  }

  public String getCwarehousename()
  {
    return this.m_cwarehousename;
  }

  public String getCinventoryid()
  {
    return this.m_cinventoryid;
  }

  public String getCinventorycode()
  {
    return this.m_cinventorycode;
  }

  public String getInvname()
  {
    return this.m_invname;
  }

  public String getInvspec()
  {
    return this.m_invspec;
  }

  public String getInvtype()
  {
    return this.m_invtype;
  }

  public String getMeasdocname()
  {
    return this.m_measdocname;
  }

  public String getCastunitname()
  {
    return this.m_castunitname;
  }

  public String getVbatchcode()
  {
    return this.m_vbatchcode;
  }

  public UFDouble getXcl()
  {
    return this.m_xcl;
  }

  public UFDouble getAverageinnum7days()
  {
    return this.m_averageinnum7days;
  }

  public UFDouble getAverageinnum30days()
  {
    return this.m_averageinnum30days;
  }

  public UFDouble getWarnoutdays()
  {
    return this.m_warnoutdays;
  }

  public void setPk_tempb(String newPk_tempb)
  {
    this.m_pk_tempb = newPk_tempb;
  }

  public void setCwarehousecode(String newCwarehousecode)
  {
    this.m_cwarehousecode = newCwarehousecode;
  }

  public void setCwarehousename(String newCwarehousename)
  {
    this.m_cwarehousename = newCwarehousename;
  }

  public void setCinventoryid(String newCinventoryid)
  {
    this.m_cinventoryid = newCinventoryid;
  }

  public void setCinventorycode(String newCinventorycode)
  {
    this.m_cinventorycode = newCinventorycode;
  }

  public void setInvname(String newInvname)
  {
    this.m_invname = newInvname;
  }

  public void setInvspec(String newInvspec)
  {
    this.m_invspec = newInvspec;
  }

  public void setInvtype(String newInvtype)
  {
    this.m_invtype = newInvtype;
  }

  public void setMeasdocname(String newMeasdocname)
  {
    this.m_measdocname = newMeasdocname;
  }

  public void setCastunitname(String newCastunitname)
  {
    this.m_castunitname = newCastunitname;
  }

  public void setVbatchcode(String newVbatchcode)
  {
    this.m_vbatchcode = newVbatchcode;
  }

  public void setXcl(UFDouble newXcl)
  {
    this.m_xcl = newXcl;
  }

  public void setAverageinnum7days(UFDouble newAverageinnum7days)
  {
    this.m_averageinnum7days = newAverageinnum7days;
  }

  public void setAverageinnum30days(UFDouble newAverageinnum30days)
  {
    this.m_averageinnum30days = newAverageinnum30days;
  }

  public void setWarnoutdays(UFDouble newWarnoutdays)
  {
    this.m_warnoutdays = newWarnoutdays;
  }

  public void validate()
    throws ValidationException
  {
    ArrayList errFields = new ArrayList();

    if (this.m_pk_tempb == null) {
      errFields.add(new String("m_pk_tempb"));
    }

    StringBuffer message = new StringBuffer();
    message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008report", "UPP4008report-000007"));
    if (errFields.size() > 0) {
      String[] temp = (String[])errFields.toArray(new String[0]);
      message.append(temp[0]);
      for (int i = 1; i < temp.length; i++) {
        message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON", "UPPSCMCommon-000000"));
        message.append(temp[i]);
      }

      throw new NullFieldException(message.toString());
    }
  }

  public String[] getAttributeNames()
  {
    Vector vName = new Vector();
    vName.add("pk_corp");
    vName.add("cwarehouseclassid");
    vName.add("cwarehousecode");
    vName.add("cwarehousename");
    vName.add("cinventoryid");
    vName.add("cinventorycode");
    vName.add("invname");
    vName.add("invspec");
    vName.add("invtype");
    vName.add("measdocname");
    vName.add("castunitname");
    vName.add("vfree0");
    vName.add("vfree1");
    vName.add("vfree2");
    vName.add("vfree3");
    vName.add("vfree4");
    vName.add("vfree5");
    vName.add("vfree6");
    vName.add("vfree7");
    vName.add("vfree8");
    vName.add("vfree9");
    vName.add("vfree10");
    vName.add("vbatchcode");
    vName.add("xcl");
    vName.add("averageinnum7days");
    vName.add("averageinnum30days");
    vName.add("warnoutdays");
    vName.add("custcode");
    vName.add("custname");
    vName.add("cvendorid");

    Enumeration enumKey = this.m_htnumberindays.keys();

    while (enumKey.hasMoreElements()) {
      Object obj = enumKey.nextElement().toString().trim();
      vName.add(obj);
    }

    String[] sNames = new String[vName.size()];
    vName.copyInto(sNames);

    return sNames;
  }

  public Object getAttributeValue(String attributeName)
  {
    if (attributeName.equals("pk_tempb"))
      return this.m_pk_tempb;
    if (attributeName.equals("pk_corp"))
      return this.m_pk_corp;
    if (attributeName.equals("cwarehouseclassid"))
      return this.m_cwarehouseclassid;
    if (attributeName.equals("cwarehousecode"))
      return this.m_cwarehousecode;
    if (attributeName.equals("cwarehousename"))
      return this.m_cwarehousename;
    if (attributeName.equals("cinventoryid"))
      return this.m_cinventoryid;
    if (attributeName.equals("cinventorycode"))
      return this.m_cinventorycode;
    if (attributeName.equals("invname"))
      return this.m_invname;
    if (attributeName.equals("invspec"))
      return this.m_invspec;
    if (attributeName.equals("invtype"))
      return this.m_invtype;
    if (attributeName.equals("measdocname"))
      return this.m_measdocname;
    if (attributeName.equals("castunitname")) {
      return this.m_castunitname;
    }
    //by  src
    if (attributeName.equals("custcode")) {
        return this.custcode;
      }
    if (attributeName.equals("custname")) {
        return this.custname;
      }
    if (attributeName.equals("cvendorid")) {
        return this.cvendorid;
      }
    if (attributeName.equals("vbatchcode"))
      return this.m_vbatchcode;
    if (attributeName.equals("xcl"))
      return this.m_xcl;
    if (attributeName.equals("averageinnum7days"))
      return this.m_averageinnum7days;
    if (attributeName.equals("averageinnum30days"))
      return this.m_averageinnum30days;
    if (attributeName.equals("warnoutdays"))
      return this.m_warnoutdays;
    if ((attributeName.startsWith("vfree")) && (this.m_freevo != null)) {
      return this.m_freevo.getAttributeValue(attributeName);
    }
    if ((this.m_htnumberindays != null) && (this.m_htnumberindays.containsKey(attributeName)))
    {
      return this.m_htnumberindays.get(attributeName);
    }

    return null;
  }

  public void setAttributeValue(String name, Object value)
  {
    try
    {
      if (name.equals("pk_tempb")) {
        this.m_pk_tempb = ((String)value);
      } else if (name.equals("pk_corp")) {
        this.m_pk_corp = ((String)value);
      } else if (name.equals("cwarehouseclassid")) {
        this.m_cwarehouseclassid = ((String)value);
      } else if (name.equals("cwarehousecode")) {
        this.m_cwarehousecode = ((String)value);
      } else if (name.equals("cwarehousename")) {
        this.m_cwarehousename = ((String)value);
      } else if (name.equals("cinventoryid")) {
        this.m_cinventoryid = ((String)value);
      } else if (name.equals("cinventorycode")) {
        this.m_cinventorycode = ((String)value);
      } else if (name.equals("invname")) {
        this.m_invname = ((String)value);
      } else if (name.equals("invspec")) {
        this.m_invspec = ((String)value);
      } else if (name.equals("invtype")) {
        this.m_invtype = ((String)value);
      } else if (name.equals("measdocname")) {
        this.m_measdocname = ((String)value);
      } else if (name.equals("castunitname")) {
        this.m_castunitname = ((String)value);
      } else if (name.equals("custcode")) {//add by src
        this.custcode = ((String)value);
      } else if (name.equals("custname")) {
        this.custname = ((String)value);
      } else if (name.equals("cvendorid")) {
        this.cvendorid = ((String)value);
      }
      else if (name.equals("vbatchcode")) {
        this.m_vbatchcode = ((String)value);
      } else if (name.equals("xcl")) {
        this.m_xcl = (value == null ? null : new UFDouble(value.toString()));
      } else if (name.equals("averageinnum7days")) {
        this.m_averageinnum7days = (value == null ? null : new UFDouble(value.toString()));
      } else if (name.equals("averageinnum30days")) {
        this.m_averageinnum30days = (value == null ? null : new UFDouble(value.toString()));
      } else if (name.equals("warnoutdays")) {
        this.m_warnoutdays = (value == null ? null : new UFDouble(value.toString()));
      } else if ((name.startsWith("vfree")) && (this.m_freevo != null)) {
        this.m_freevo.setAttributeValue(name, value);
      } else if ((name.startsWith("vfree")) && (this.m_freevo == null)) {
        this.m_freevo = new FreeVO();
        this.m_freevo.setAttributeValue(name, value);
      }
      else if (null != value) {
        this.m_htnumberindays.put(name, value);
      }
    }
    catch (ClassCastException e) {
      throw new ClassCastException(NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON", "UPPSCMCommon-000005", null, new String[] { name, value.toString() }));
    }
  }

  public String getCwarehouseclassid()
  {
    return this.m_cwarehouseclassid;
  }

  public FreeVO getFreevo()
  {
    return this.m_freevo;
  }

  public String getPk_corp()
  {
    return this.m_pk_corp;
  }

  public void setCwarehouseclassid(String newCwarehousecode)
  {
    this.m_cwarehouseclassid = newCwarehousecode;
  }

  public void setFreevo(FreeVO newVfree0)
  {
    this.m_freevo = newVfree0;
  }

  public void setPk_corp(String newCinventoryid)
  {
    this.m_pk_corp = newCinventoryid;
  }
}