package nc.vo.cvm.log;


import java.io.Serializable;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

public class LogVO extends SuperVO implements Serializable{
	   private static final long serialVersionUID=-8371971655949083090L;
		private String pk_corp;//单位编码
		private String pk_log;//主键
		private Integer dr;//删除标识
		private UFDateTime ts;//时间戳
		private String  djlx;//订单类型
		private String lUser;//审核人
		private String djbx;//订单号
		private String lContext;//日志描述
		private String headBill_id;//主表id（表头）
		private String bodyBill_id;//子表id（表体）
		private UFDateTime  start_time;//开始时间
		private UFDateTime end_time;//结束时间
		
		public String getBodyBill_id() {
			return bodyBill_id;
		}
		public void setBodyBill_id(String bodyBill_id) {
			this.bodyBill_id = bodyBill_id;
		}
		public UFDateTime getStart_time() {
			return start_time;
		}
		public void setStart_time(UFDateTime start_time) {
			this.start_time = start_time;
		}
		public UFDateTime getEnd_time() {
			return end_time;
		}
		public void setEnd_time(UFDateTime end_time) {
			this.end_time = end_time;
		}
		public String getPk_corp() {
			return pk_corp;
		}
		public void setPk_corp(String pk_corp) {
			this.pk_corp = pk_corp;
		}
		public Integer getDr() {
			return dr;
		}
		public void setDr(Integer dr) {
			this.dr = dr;
		}
		public UFDateTime getTs() {
			return ts;
		}
		public void setTs(UFDateTime ts) {
			this.ts = ts;
		}
		public String getLUser() {
			return lUser;
		}
		public void setLUser(String user) {
			lUser = user;
		}
		public String getLContext() {
			return lContext;
		}
		public void setLContext(String context) {
			lContext = context;
		}
		
		//返回主键
		@Override
		public String getPKFieldName() {
			return "pk_log";
		}
		@Override
		public String getParentPKFieldName() {
			return null;
		}
		@Override
		public String getTableName() {
			return "itfLog";
		}
		public void setPk_log(String pk_log) {
			this.pk_log = pk_log;
		}
		public String getPk_log() {
			return pk_log;
		}
		public String getDjlx() {
			return djlx;
		}
		public void setDjlx(String djlx) {
			this.djlx = djlx;
		}
		public String getDjbx() {
			return djbx;
		}
		public void setDjbx(String djbx) {
			this.djbx = djbx;
		}
		public void setHeadBill_id(String headBill_id) {
			this.headBill_id = headBill_id;
		}
		public String getHeadBill_id() {
			return headBill_id;
		}
}
