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
 * @author chenzhongliang
 *
 */
@Table("ins_project_info")
@Comment("项目信息表")
public class Ins_ProjectInfo extends BaseModel implements Serializable{

	private static final long serialVersionUID = 703979280687410853L;
	
	@Id
	@Column
	private Integer id;
	
	
	@Comment("项目名称")
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String name;
	
	
	@Comment("项目编号")
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String code;
	
	
	@Comment("项目类型")
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String type;
	
	
	@Comment("项目简介")
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 300)
	private String detail;
	
	
	@Comment("添加时间")
	@Column
	@ColDefine(type = ColType.DATETIME)
	private Date createTime;
	
	
	@Comment("备用字段1")
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String str1;
	
	
	@Comment("备用字段2")
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String str2;


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getDetail() {
		return detail;
	}


	public void setDetail(String detail) {
		this.detail = detail;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
