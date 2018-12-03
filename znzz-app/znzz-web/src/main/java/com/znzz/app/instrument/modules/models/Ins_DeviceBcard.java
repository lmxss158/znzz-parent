package com.znzz.app.instrument.modules.models;

import java.io.Serializable;
import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import com.znzz.framework.base.model.BaseModel;
/**
 * 
 * @ClassName: Ins_DeviceBcard
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author pengmantai
 * @date 2017年9月18日 上午10:49:58
 */
@Table("ins_device_bcard")
@Comment("采集器表")
public class Ins_DeviceBcard extends BaseModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@Comment("主键id")
	@Column
	@ColDefine(type = ColType.INT)
	private Integer id;

	@Column
	@Comment("b卡编号")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String bcardCode;

	@Column
	@Comment("生产编号")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String orignCode;
	@Column
	@Comment("操作时间") 
	@ColDefine(type = ColType.DATETIME)
	private Date operateTime;
	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBcardCode() {
		return bcardCode;
	}

	public void setBcardCode(String bcardCode) {
		this.bcardCode = bcardCode;
	}

	public String getOrignCode() {
		return orignCode;
	}

	public void setOrignCode(String orignCode) {
		this.orignCode = orignCode;
	}


	
	
}
