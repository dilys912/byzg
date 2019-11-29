package nc.impl.ws.baoyin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.vo.framework.rsa.Encode;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.rino.pda.BasicdocVO;
import nc.vo.sm.UserVO;

/**
 * 基础数据服务类
 * @author Administrator
 * @date Mar 31, 2014 3:38:04 PM
 * @type nc.ws.baoyin.BDService
 * @corporation 上海锐鸟软件有限公司
 * @website www.rino123.com
 * @mail zap_168@163.com
 */
@SuppressWarnings({
	"unchecked"
})
public class BDService extends BaseService {

	// 登录校验,登录成功后获取未同步的基础档案
	public String login(Map<String, Object> root) throws Exception {
		IUserManageQuery userDMO = (IUserManageQuery) NCLocator.getInstance().lookup(IUserManageQuery.class.getName());
		Map<String, Object> data = (Map<String, Object>) root.get("data");
		String sender = (String) root.get("sender");
		String unitcode = (String) data.get("unitcode");
		String usercode = (String) data.get("usercode");
		String userpwd = (String) data.get("userpwd");
		String pk_corp = (String) getJdbc().getObject("select pk_corp from bd_corp where unitcode='" + unitcode + "'");
		String unitname = (String) getJdbc().getObject("select unitname from bd_corp where pk_corp='" + pk_corp + "'");
		String tarpwd = new Encode().encode(userpwd);
		UserVO userVO = userDMO.findUser(pk_corp, usercode, tarpwd);
		if (userVO == null) throw new Exception("用户名或密码错误");
		StringBuilder ret = new StringBuilder();
		ret.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		ret.append("<root>");
		ret.append("<actiontype>").append(root.get("actiontype")).append("</actiontype>");
		ret.append("<sender>").append(sender).append("</sender>");
		ret.append("<data>");
		ret.append("<userid>").append(userVO.getPrimaryKey()).append("</userid>");
		ret.append("<pk_corp>").append(pk_corp).append("</pk_corp>");
		ret.append("<unitcode>").append(unitcode).append("</unitcode>");
		ret.append("<unitname>").append(unitname).append("</unitname>");
		ret.append("<usercode>").append(usercode).append("</usercode>");
		ret.append("<username>").append(userVO.getUserName()).append("</username>");
		ret.append("</data>");
		// 检查基础数据是否需要更新
		// edit by zip: 2014/8/10
		// 禁用货位同步
		List<BasicdocVO> docList = (List<BasicdocVO>) getJdbc().getBeanList("select * from pda_basicdoc where bdtype<>'HW' and sysflag='Y' and nvl(dr,0)=0", BasicdocVO.class);
		if (docList != null && docList.size() > 0) {
			List<BasicdocVO> updateList = new ArrayList<BasicdocVO>();
			for (BasicdocVO docVO : docList) {
				Object obj = getJdbc().getObject("select tid from pda_basicdoc where sysflag='N' and handledevice='" + sender + "' and bdid='" + docVO.getBdid() + "' and nvl(dr,0)=0");
				if (obj == null) {
					String currTime = new UFDateTime(System.currentTimeMillis()).toString();
					BasicdocVO vo = (BasicdocVO) docVO.clone();
					vo.setPrimaryKey(null);
					vo.setSysflag("N");
					vo.setHandledevice(sender);
					vo.setHandletime(currTime);
					vo.setTs(null);
					getJdbc().insert(vo);
					updateList.add(vo);
				}
			}
			if (updateList.size() > 0) {
				ret.append("<docs>");
				for (BasicdocVO docVO : updateList) {
					ret.append("<bd>");
					ret.append("<bdtype>").append(docVO.getBdtype()).append("</bdtype>");
					ret.append("<proctype>").append(docVO.getProctype()).append("</proctype>");
					ret.append("<bdid>").append(docVO.getBdid()).append("</bdid>");
					ret.append("<bdname>").append(docVO.getBdname()).append("</bdname>");
					ret.append("<def1>").append(docVO.getDef1()).append("</def1>");
					ret.append("<def2>").append(docVO.getDef2()).append("</def2>");
					ret.append("</bd>");
				}
				ret.append("</docs>");
			}
		}
		ret.append("</root>");
		return ret.toString();
	}

	// 获取未同步的基础档案
	public String updateBd(Map<String, Object> root) throws Exception {
		String bdtype = (String) root.get("bdtype");
		String sender = (String) root.get("sender");
		List<BasicdocVO> docList = (List<BasicdocVO>) getJdbc().getBeanList("select * from pda_basicdoc where sysflag='Y' and bdtype='" + bdtype + "' and nvl(dr,0)=0", BasicdocVO.class);
		if (docList == null || docList.size() <= 0) return null;
		StringBuilder ret = new StringBuilder();
		ret.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		ret.append("<root>");
		ret.append("<actiontype>").append(root.get("actiontype")).append("</actiontype>");
		ret.append("<sender>").append(sender).append("</sender>");
		List<BasicdocVO> updateList = new ArrayList<BasicdocVO>();
		for (BasicdocVO docVO : docList) {
			Object obj = getJdbc().getObject("select tid from pda_basicdoc where sysflag='N' and handledevice='" + sender + "' and bdid='" + docVO.getBdid() + "' and bdtype='" + bdtype + "' and nvl(dr,0)=0");
			if (obj == null) {
				String currTime = new UFDateTime(System.currentTimeMillis()).toString();
				BasicdocVO vo = (BasicdocVO) docVO.clone();
				vo.setPrimaryKey(null);
				vo.setSysflag("N");
				vo.setHandledevice(sender);
				vo.setHandletime(currTime);
				vo.setTs(null);
				getJdbc().insert(vo);
				updateList.add(vo);
			}
		}
		if (updateList.size() > 0) {
			ret.append("<bodys>");
			for (BasicdocVO docVO : updateList) {
				ret.append("<body>");
				ret.append("<bdtype>").append(docVO.getBdtype()).append("</bdtype>");
				ret.append("<proctype>").append(docVO.getProctype()).append("</proctype>");
				ret.append("<bdid>").append(docVO.getBdid()).append("</bdid>");
				ret.append("<bdname>").append(docVO.getBdname()).append("</bdname>");
				ret.append("<def1>").append(docVO.getDef1()).append("</def1>");
				ret.append("<def2>").append(docVO.getDef2()).append("</def2>");
				ret.append("</body>");
			}
			ret.append("</bodys>");
		}
		ret.append("</root>");
		return ret.toString();
	}
}
