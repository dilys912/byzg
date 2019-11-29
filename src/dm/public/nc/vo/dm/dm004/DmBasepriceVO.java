// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 2012-8-9 10:03:30
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   DmBasepriceVO.java

package nc.vo.dm.dm004;

import java.util.Arrays;
import nc.vo.ia.bill.BillHeaderVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.bd.SmartVODataUtils;
import nc.vo.scm.pub.smart.SmartVO;

// Referenced classes of package nc.vo.dm.dm004:
//            DmBasepriceVOMeta

public class DmBasepriceVO extends SmartVO
{

    public DmBasepriceVO()
    {
        m_SVODataUtils = new SmartVODataUtils();
    }

    public Class getVOMetaClass()
    {
        return nc.vo.dm.dm004.DmBasepriceVOMeta.class;
    }

    public void setAttributeValue(String key, Object value)
    {
        super.setAttributeValue(key, value);
    }

    public boolean equals(Object other)
    {
        if(other == null)
            return false;
        boolean result = false;
        String sleft[] = getMapNames();
        String sright[] = ((BillHeaderVO)other).getMapNames();
        Arrays.sort(sleft);
        Arrays.sort(sright);
        if(sleft != null && sright != null && sleft.length == sright.length)
        {
            boolean temp = true;
            int i = 0;
            do
            {
                if(i >= sleft.length)
                    break;
                Object oleft = getAttributeValue(sleft[i]);
                Object oright = ((BillHeaderVO)other).getAttributeValue(sright[i]);
                if(oleft != null && oright != null && !oleft.toString().equals(oright.toString()))
                {
                    temp = false;
                    break;
                }
                if(oleft == null && oright != null || oleft != null && oright == null)
                {
                    temp = false;
                    break;
                }
                i++;
            } while(true);
            if(temp)
                result = true;
        } else
        if(sleft == null && sright == null)
            result = true;
        else
            result = false;
        return result;
    }

    public String getPrimaryKey()
    {
        return (String)getAttributeValue("pk_basicprice");
    }

    public void setPrimaryKey(String value)
    {
        setAttributeValue("pk_basicprice", value);
    }

    public UFDouble getNuplimitnum()
    {
        Object value = getAttributeValue("nuplimitnum");
        SmartVODataUtils _tmp = m_SVODataUtils;
        return SmartVODataUtils.getUFDouble(value);
    }

    public void setNuplimitnum(UFDouble value)
    {
        setAttributeValue("nuplimitnum", value);
    }

    public String getPkfromarea()
    {
        return (String)getAttributeValue("pkfromarea");
    }

    public void setPkfromarea(String value)
    {
        setAttributeValue("pkfromarea", value);
    }

    public UFBoolean getBsltfrmlevel()
    {
        Object value = getAttributeValue("bsltfrmlevel");
        SmartVODataUtils _tmp = m_SVODataUtils;
        return SmartVODataUtils.getUFBoolean(value);
    }

    public void setBsltfrmlevel(UFBoolean value)
    {
        setAttributeValue("bsltfrmlevel", value);
    }

    public String getPk_sendtype()
    {
        return (String)getAttributeValue("pk_sendtype");
    }

    public void setPk_sendtype(String value)
    {
        setAttributeValue("pk_sendtype", value);
    }

    public String getPkroute()
    {
        return (String)getAttributeValue("pkroute");
    }

    public void setPkroute(String value)
    {
        setAttributeValue("pkroute", value);
    }

    public String getPk_invclass()
    {
        return (String)getAttributeValue("pk_invclass");
    }

    public void setPk_invclass(String value)
    {
        setAttributeValue("pk_invclass", value);
    }

    public String getPk_vehicletype()
    {
        return (String)getAttributeValue("pk_vehicletype");
    }

    public void setPk_vehicletype(String value)
    {
        setAttributeValue("pk_vehicletype", value);
    }

    public String getPktoarea()
    {
        return (String)getAttributeValue("pktoarea");
    }

    public void setPktoarea(String value)
    {
        setAttributeValue("pktoarea", value);
    }

    public Integer getIpricetype()
    {
        Object value = getAttributeValue("ipricetype");
        SmartVODataUtils _tmp = m_SVODataUtils;
        return SmartVODataUtils.getInteger(value);
    }

    public void setIpricetype(Integer value)
    {
        setAttributeValue("ipricetype", value);
    }

    public String getPk_inventory()
    {
        return (String)getAttributeValue("pk_inventory");
    }

    public void setPk_inventory(String value)
    {
        setAttributeValue("pk_inventory", value);
    }

    public String getPkfromaddress()
    {
        return (String)getAttributeValue("pkfromaddress");
    }

    public void setPkfromaddress(String value)
    {
        setAttributeValue("pkfromaddress", value);
    }

    public String getPk_transcontainer()
    {
        return (String)getAttributeValue("pk_transcontainer");
    }

    public void setPk_transcontainer(String value)
    {
        setAttributeValue("pk_transcontainer", value);
    }

    public String getPk_transcust()
    {
        return (String)getAttributeValue("pk_transcust");
    }

    public void setPk_transcust(String value)
    {
        setAttributeValue("pk_transcust", value);
    }

    public Integer getIuplimittype()
    {
        Object value = getAttributeValue("iuplimittype");
        SmartVODataUtils _tmp = m_SVODataUtils;
        return SmartVODataUtils.getInteger(value);
    }

    public void setIuplimittype(Integer value)
    {
        setAttributeValue("iuplimittype", value);
    }

    public String getVpriceunit()
    {
        return (String)getAttributeValue("vpriceunit");
    }

    public void setVpriceunit(String value)
    {
        setAttributeValue("vpriceunit", value);
    }

    public String getPkpacksort()
    {
        return (String)getAttributeValue("pkpacksort");
    }

    public void setPkpacksort(String value)
    {
        setAttributeValue("pkpacksort", value);
    }

    public String getPktoaddress()
    {
        return (String)getAttributeValue("pktoaddress");
    }

    public void setPktoaddress(String value)
    {
        setAttributeValue("pktoaddress", value);
    }

    public String getPk_basicprice()
    {
        return (String)getAttributeValue("pk_basicprice");
    }

    public void setPk_basicprice(String value)
    {
        setAttributeValue("pk_basicprice", value);
    }

    public String getPkdelivorg()
    {
        return (String)getAttributeValue("pkdelivorg");
    }

    public void setPkdelivorg(String value)
    {
        setAttributeValue("pkdelivorg", value);
    }

    public UFDouble getNoveruplmtprice()
    {
        Object value = getAttributeValue("noveruplmtprice");
        SmartVODataUtils _tmp = m_SVODataUtils;
        return SmartVODataUtils.getUFDouble(value);
    }

    public void setNoveruplmtprice(UFDouble value)
    {
        setAttributeValue("noveruplmtprice", value);
    }

    public String getMemo()
    {
        return (String)getAttributeValue("memo");
    }

    public void setMemo(String value)
    {
        setAttributeValue("memo", value);
    }
    
    public UFDouble getDbaseprice()
    {
        Object value = getAttributeValue("dbaseprice");
        SmartVODataUtils _tmp = m_SVODataUtils;
        return SmartVODataUtils.getUFDouble(value);
    }

    public void setDbaseprice(UFDouble value)
    {
        setAttributeValue("dbaseprice", value);
    }
//eric
    
    public UFDouble getTaxprice()
    {
        Object value = getAttributeValue("taxprice");
        SmartVODataUtils _tmp = m_SVODataUtils;
        return SmartVODataUtils.getUFDouble(value);
    }

    public void setTaxprice(UFDouble value)
    {
        setAttributeValue("taxprice", value);
    }
    
    
    public UFDouble getRate()
    {
        Object value = getAttributeValue("rate");
        SmartVODataUtils _tmp = m_SVODataUtils;
        return SmartVODataUtils.getUFDouble(value);
    }

    public void setRate(UFDouble value)
    {
        setAttributeValue("rate", value);
    }
    
    public String getEffectdate()
    {
        return (String)getAttributeValue("effectdate");
    }

    public void setEffectdate(String value)
    {
        setAttributeValue("effectdate", value);
    }
    
    public String getExpirationdate()
    {
        return (String)getAttributeValue("expirationdate");
    }

    public void setExpirationdate(String value)
    {
        setAttributeValue("expirationdate", value);
    }
    
    
    public String getVdoname()
    {
        return (String)getAttributeValue("vdoname");
    }

    public void setVdoname(String value)
    {
        setAttributeValue("vdoname", value);
    }

    public String getVtranscustcode()
    {
        return (String)getAttributeValue("vtranscustcode");
    }

    public void setVtranscustcode(String value)
    {
        setAttributeValue("vtranscustcode", value);
    }

    public String getVtranscustname()
    {
        return (String)getAttributeValue("vtranscustname");
    }

    public void setVtranscustname(String value)
    {
        setAttributeValue("vtranscustname", value);
    }

    public String getVvhcltypecode()
    {
        return (String)getAttributeValue("vvhcltypecode");
    }

    public void setAttributeValue(String value)
    {
        setAttributeValue("vvhcltypecode", value);
    }

    public String getVvhcltypename()
    {
        return (String)getAttributeValue("vvhcltypename");
    }

    public void setVvhcltypename(String value)
    {
        setAttributeValue("vvhcltypename", value);
    }

    public String getVclasscode()
    {
        return (String)getAttributeValue("vclasscode");
    }

    public void setVclasscode(String value)
    {
        setAttributeValue("vclasscode", value);
    }

    public String getVclassname()
    {
        return (String)getAttributeValue("vclassname");
    }

    public void setVclassname(String value)
    {
        setAttributeValue("vclassname", value);
    }

    public String getVsendtypecode()
    {
        return (String)getAttributeValue("vsendtypecode");
    }

    public void setVsendtypecode(String value)
    {
        setAttributeValue("vsendtypecode", value);
    }

    public String getVsendtypename()
    {
        return (String)getAttributeValue("vsendtypename");
    }

    public void setVsendtypename(String value)
    {
        setAttributeValue("vsendtypename", value);
    }

    public String getVinvclasscode()
    {
        return (String)getAttributeValue("vinvclasscode");
    }

    public void setVinvclasscode(String value)
    {
        setAttributeValue("vinvclasscode", value);
    }

    public String getVinvclassname()
    {
        return (String)getAttributeValue("vinvclassname");
    }

    public void setVinvclassname(String value)
    {
        setAttributeValue("vinvclassname", value);
    }

    public String getVinvcode()
    {
        return (String)getAttributeValue("vinvcode");
    }

    public void setVinvcode(String value)
    {
        setAttributeValue("vinvcode", value);
    }

    public String getVinvname()
    {
        return (String)getAttributeValue("vinvname");
    }

    public void setVinvname(String value)
    {
        setAttributeValue("vinvname", value);
    }

    public String getPkcorp()
    {
        return (String)getAttributeValue("pkcorp");
    }

    public void setPkcorp(String value)
    {
        setAttributeValue("pkcorp", value);
    }

    public String getFromarea()
    {
        return (String)getAttributeValue("fromarea");
    }

    public void setFromarea(String value)
    {
        setAttributeValue("fromarea", value);
    }

    public String getToarea()
    {
        return (String)getAttributeValue("toarea");
    }

    public void setToarea(String value)
    {
        setAttributeValue("toarea", value);
    }

    public String getPacksort()
    {
        return (String)getAttributeValue("packsort");
    }

    public void setPacksort(String value)
    {
        setAttributeValue("packsort", value);
    }

    public String getTs()
    {
        return (String)getAttributeValue("ts");
    }

    public void setTs(String value)
    {
        setAttributeValue("ts", value);
    }

    private SmartVODataUtils m_SVODataUtils;
}
