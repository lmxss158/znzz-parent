package com.znzz.app.instrument.modules.models;

import java.io.Serializable;
import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import com.znzz.framework.base.model.BaseModel;

/**
 * @author chenzhongliang
 * 消息管理的实体类
 */
@Table("ins_message_info")
@Comment("消息管理表")
public class Ins_MessageInfo extends BaseModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column
	private Integer id;
	
	@Comment("发送者编号")
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 32)
	private String sendId;
	
	@Comment("接收者编号")
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 32)
	private String receiveId;
	
	@Comment("站内信内容")
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 2000)
	private String messageContext;
	
	@Comment("站内信查看状态 0未读1已读")
	@Column
	@ColDefine(type = ColType.INT, width = 1)
	@Default("0")
	private Integer status;
	
	@Comment("站内信发送时间")
	@Column
	@ColDefine(type = ColType.DATETIME)
	private Date send_time;
	
	@Comment("备用字段1")
	@Column
	@ColDefine(type = ColType.VARCHAR, width= 60)
	private String str1;
	
	@Comment("备用字段2")
	@Column
	@ColDefine(type = ColType.VARCHAR, width= 60)
	private String str2;

	
	private String uname;//单位名称
	
	private String unitid;//单位id
	
	private String powerOnRate;//设备使用率
	
	private String unitDeviceNum;//单位下受监控设备数量
	
	
	
	public String getUnitDeviceNum() {
		return unitDeviceNum;
	}

	public void setUnitDeviceNum(String unitDeviceNum) {
		this.unitDeviceNum = unitDeviceNum;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getUnitid() {
		return unitid;
	}

	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}

	public String getPowerOnRate() {
		return powerOnRate;
	}

	public void setPowerOnRate(String powerOnRate) {
		this.powerOnRate = powerOnRate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSendId() {
		return sendId;
	}

	public void setSendId(String sendId) {
		this.sendId = sendId;
	}

	public String getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}

	public String getMessageContext() {
		return messageContext;
	}

	public void setMessageContext(String messageContext) {
		this.messageContext = messageContext;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getSend_time() {
		return send_time;
	}

	public void setSend_time(Date send_time) {
		this.send_time = send_time;
	}

	public String getStr1() {
		return str1;
	}

	public void setStr1(String str1) {
		this.str1 = str1;
	}

	public String getStr2() {
		return str2;
	}

	public void setStr2(String str2) {
		this.str2 = str2;
	}
	
	
	
}
