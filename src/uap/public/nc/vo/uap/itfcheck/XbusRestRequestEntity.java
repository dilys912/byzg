package nc.vo.uap.itfcheck;

public class XbusRestRequestEntity {
	

	private java.lang.String url;
	private java.lang.String msgSendTime;
    private java.lang.String msgToken;
    private java.lang.String password;
    private java.lang.String serviceId;
    private java.lang.String sourceAppCode;
    private java.lang.String tempField;
    private java.lang.String version;
    private java.lang.String msgBody;
    private java.lang.String billcode;//来源单据
    
    public XbusRestRequestEntity() {
    }

    public XbusRestRequestEntity(
    		java.lang.String url,
           java.lang.String msgBody,
           java.lang.String msgSendTime,
           java.lang.String msgToken,
           java.lang.String password,
           java.lang.String serviceId,
           java.lang.String sourceAppCode,
           java.lang.String tempField,
           java.lang.String version,
           java.lang.String billcode) {
    	   this.url = url;
           this.msgBody = msgBody;
           this.msgSendTime = msgSendTime;
           this.msgToken = msgToken;
           this.password = password;
           this.serviceId = serviceId;
           this.sourceAppCode = sourceAppCode;
           this.tempField = tempField;
           this.version = version;
           this.billcode = billcode;
    }

    public java.lang.String getBillcode() {
		return billcode;
	}

	public void setBillcode(java.lang.String billcode) {
		this.billcode = billcode;
	}

	public java.lang.String getUrl() {
    	return url;
    }
    
    public void setUrl(java.lang.String url) {
    	this.url = url;
    }

    /**
     * Gets the msgBody value for this DefaultWebServiceRequest.
     * 
     * @return msgBody
     */
    public java.lang.String getMsgBody() {
        return msgBody;
    }


    /**
     * Sets the msgBody value for this DefaultWebServiceRequest.
     * 
     * @param msgBody
     */
    public void setMsgBody(java.lang.String msgBody) {
        this.msgBody = msgBody;
    }


    /**
     * Gets the msgSendTime value for this DefaultWebServiceRequest.
     * 
     * @return msgSendTime
     */
    public java.lang.String getMsgSendTime() {
        return msgSendTime;
    }


    /**
     * Sets the msgSendTime value for this DefaultWebServiceRequest.
     * 
     * @param msgSendTime
     */
    public void setMsgSendTime(java.lang.String msgSendTime) {
        this.msgSendTime = msgSendTime;
    }


    /**
     * Gets the msgToken value for this DefaultWebServiceRequest.
     * 
     * @return msgToken
     */
    public java.lang.String getMsgToken() {
        return msgToken;
    }


    /**
     * Sets the msgToken value for this DefaultWebServiceRequest.
     * 
     * @param msgToken
     */
    public void setMsgToken(java.lang.String msgToken) {
        this.msgToken = msgToken;
    }


    /**
     * Gets the password value for this DefaultWebServiceRequest.
     * 
     * @return password
     */
    public java.lang.String getPassword() {
        return password;
    }


    /**
     * Sets the password value for this DefaultWebServiceRequest.
     * 
     * @param password
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }


    /**
     * Gets the serviceId value for this DefaultWebServiceRequest.
     * 
     * @return serviceId
     */
    public java.lang.String getServiceId() {
        return serviceId;
    }


    /**
     * Sets the serviceId value for this DefaultWebServiceRequest.
     * 
     * @param serviceId
     */
    public void setServiceId(java.lang.String serviceId) {
        this.serviceId = serviceId;
    }


    /**
     * Gets the sourceAppCode value for this DefaultWebServiceRequest.
     * 
     * @return sourceAppCode
     */
    public java.lang.String getSourceAppCode() {
        return sourceAppCode;
    }


    /**
     * Sets the sourceAppCode value for this DefaultWebServiceRequest.
     * 
     * @param sourceAppCode
     */
    public void setSourceAppCode(java.lang.String sourceAppCode) {
        this.sourceAppCode = sourceAppCode;
    }


    /**
     * Gets the tempField value for this DefaultWebServiceRequest.
     * 
     * @return tempField
     */
    public java.lang.String getTempField() {
        return tempField;
    }


    /**
     * Sets the tempField value for this DefaultWebServiceRequest.
     * 
     * @param tempField
     */
    public void setTempField(java.lang.String tempField) {
        this.tempField = tempField;
    }


    /**
     * Gets the version value for this DefaultWebServiceRequest.
     * 
     * @return version
     */
    public java.lang.String getVersion() {
        return version;
    }


    /**
     * Sets the version value for this DefaultWebServiceRequest.
     * 
     * @param version
     */
    public void setVersion(java.lang.String version) {
        this.version = version;
    }
    
    
}
