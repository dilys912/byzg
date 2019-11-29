package nc.vo.by.invapp.pub;

/**
 * 说明:状态机专用VO
 */
public class StateVO extends nc.vo.pub.ValueObject {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 单据来源
    private int m_iBillSource;
    // 单据状态。
    private int m_iBillStatus;
    
    public StateVO() {
        super();
    }

    public StateVO(int iBillStatus, int iBillSource) {
        super();
        m_iBillSource = iBillSource;
        m_iBillStatus = iBillStatus;
    }


    public int getBillSource() {
        return m_iBillSource;
    }

    public int getBillStatus() {
        return m_iBillStatus;
    }

    public String getEntityName() {
        return null;
    }

    public void validate() throws nc.vo.pub.ValidationException {
    }
}
