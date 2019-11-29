package nc.ui.bd.deptdoc;

import nc.ui.sm.createcorp.CreatecorpBO_Client;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.lang.UFBoolean;

public class DeptDocUIExt extends nc.ui.bd.deptdoc.DeptDocUI{
	private UFBoolean isHRModuleUsed;
	@Override
	protected CardEventHandler createEventHandler() {
		return new DeptDocEHDExt(this, getUIControl(), isHRModuleUsed(), getFaterButtonVO());
		
	}

	private boolean isHRModuleUsed()
    {
        if(isHRModuleUsed == null)
            try
            {
                isHRModuleUsed = new UFBoolean(CreatecorpBO_Client.isEnabled(getPk_corp(), "HR"));
            }
            catch(Exception e)
            {
                e.printStackTrace();
                isHRModuleUsed = new UFBoolean(false);
            }
        return isHRModuleUsed.booleanValue();
    }
	
    private String getPk_corp()
    {
        String pk_corp = getClientEnvironment().getCorporation().getPrimaryKey();
        return pk_corp;
    }
}
