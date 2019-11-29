
package nc.vo.bd.operationinfo;

import java.util.ArrayList;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.*;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

public class SendtypeVO extends SuperVO
{

    public String getAccepttedtype()
    {
        return accepttedtype;
    }

    public Integer getDr()
    {
        return dr;
    }

    public UFBoolean getIsacceptted()
    {
        return isacceptted;
    }

    public UFBoolean getIssendarranged()
    {
        return issendarranged;
    }

    public String getPk_corp()
    {
        return pk_corp;
    }

    public String getSendcode()
    {
        return sendcode;
    }

    public String getSendname()
    {
        return sendname;
    }

    public Integer getTransporttype()
    {
        return transporttype;
    }

    public UFDateTime getTs()
    {
        return ts;
    }

    public void setAccepttedtype(String newAccepttedtype)
    {
        accepttedtype = newAccepttedtype;
    }

    public void setDr(Integer newDr)
    {
        dr = newDr;
    }

    public void setIsacceptted(UFBoolean newIsacceptted)
    {
        isacceptted = newIsacceptted;
    }

    public void setIssendarranged(UFBoolean newIssendarranged)
    {
        issendarranged = newIssendarranged;
    }

    public void setPk_corp(String newPk_corp)
    {
        pk_corp = newPk_corp;
    }

    public void setSendcode(String newSendcode)
    {
        sendcode = newSendcode;
    }

    public void setSendname(String newSendname)
    {
        sendname = newSendname;
    }

    public void setTransporttype(Integer newTransporttype)
    {
        transporttype = newTransporttype;
    }

    public void setTs(UFDateTime newTs)
    {
        ts = newTs;
    }

    public void validate()
        throws ValidationException
    {
        ArrayList errFields = new ArrayList();
        if(isacceptted == null)
            errFields.add(new String("isacceptted"));
        if(issendarranged == null)
            errFields.add(new String("issendarranged"));
        if(pk_corp == null)
            errFields.add(new String("pk_corp"));
        if(pk_sendtype == null)
            errFields.add(new String("pk_sendtype"));
        if(sendcode == null)
            errFields.add(new String("sendcode"));
        if(sendname == null)
            errFields.add(new String("sendname"));
        StringBuffer message = new StringBuffer();
        message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("10082202", "UPP10082202-000018"));
        if(errFields.size() > 0)
        {
            String temp[] = (String[])(String[])errFields.toArray(new String[0]);
            message.append(temp[0]);
            for(int i = 1; i < temp.length; i++)
            {
                message.append(",");
                message.append(temp[i]);
            }

            throw new NullFieldException(message.toString());
        } else
        {
            return;
        }
    }

    public String getParentPKFieldName()
    {
        return null;
    }

    public String getPKFieldName()
    {
        return "pk_sendtype";
    }

    public String getTableName()
    {
        return "bd_sendtype";
    }

    public SendtypeVO()
    {
    }

    public SendtypeVO(String newPk_sendtype)
    {
        pk_sendtype = newPk_sendtype;
    }

    public String getPrimaryKey()
    {
        return pk_sendtype;
    }

    public void setPrimaryKey(String newPk_sendtype)
    {
        pk_sendtype = newPk_sendtype;
    }

    public String getEntityName()
    {
        return "Sendtype";
    }

    public Integer getFreighttype()
    {
        return freighttype;
    }

    public void setFreighttype(Integer freighttype)
    {
        this.freighttype = freighttype;
    }
    
    public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	private static final long serialVersionUID = 7806102407713435634L;
    public String pk_sendtype;
    public String accepttedtype;
    public Integer dr;
    public UFBoolean isacceptted;
    public UFBoolean issendarranged;
    public String pk_corp;
    public String sendcode;
    public String sendname;
    public Integer transporttype;
    public Integer freighttype;
    public UFDateTime ts;
    private String memo;
    
}

