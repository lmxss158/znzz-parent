package com.znzz.app.sys.modules.models;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("sys_import_table")
public class Sys_import_table {

	/**
	 * 流水号
	 */
	@Id
	@Column
	@Comment("id")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer id;
	
	/**
	 * 报表编码
	 */
	@Column
    @Comment("报表编码")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String code;
	
	/**
	 * 导入列表的类型
	 */
	@Column
    @Comment("导入列表的类型")
    @ColDefine(type = ColType.VARCHAR, width = 200)
	private String type;
	
	/**
	 * 开始行数
	 */
	@Column
	@Comment("开始行数")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer startrows;
	
	/**
	 * 标题行数
	 */
	@Column
	@Comment("标题行数")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer titlerows;
	
	/**
	 * 表头行数
	 */
	@Column
	@Comment("表头行数")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer headrows;
	
	/**
	 * 主键列号
	 */
	@Column
	@Comment("主键列号")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer keyindex;
	
	/**
	 * 开始读取的sheet位置
	 */
	@Column
	@Comment("开始读取的sheet位置")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer startsheetindex;
	
	/**
	 * 上传表格需要读取的sheet数量
	 */
	@Column
	@Comment("上传表格需要读取的sheet数量")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer sheetnum;
	
	/**
	 * 是否保存上传的excel
	 */
	@Column
    @Comment("是否保存上传的excel")
    @ColDefine(type = ColType.VARCHAR, width = 1)
	private String needsave;
	
	/**
	 * 检验组类名，逗号分隔
	 */
	@Column
    @Comment("检验组类名，逗号分隔")
    @ColDefine(type = ColType.VARCHAR, width = 2000)
	private String verifygroup;
	
	/**
	 * 保存上传的excel目录
	 */
	@Column
    @Comment("保存上传的excel目录")
    @ColDefine(type = ColType.VARCHAR, width = 200)
	private String saveurl;
	
	/**
	 * 是否需要验证
	 */
	@Column
    @Comment("是否需要验证")
    @ColDefine(type = ColType.VARCHAR, width = 1)
	private String needverify;
	
	/**
	 * 校验处理接口
	 */
	@Column
    @Comment("校验处理接口")
    @ColDefine(type = ColType.VARCHAR, width = 200)
	private String verifyhandler;
	
	/**
	 * 最后的无效行数
	 */
	@Column
    @Comment("最后的无效行数")
    @ColDefine(type = ColType.INT, width = 10)
	private Integer lastofinvalirow;
	
	/**
	 * 手动控制读取的行数
	 */
	@Column
    @Comment("手动控制读取的行数")
    @ColDefine(type = ColType.INT, width = 10)
	private Integer readrows;
	
	/**
	 * 导入时校验数据模板，是不是正确的excel
	 */
	@Column
    @Comment("导入时校验数据模板，是不是正确的excel")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String importfields;
	
	/**
	 * 导入时校验excel的标题列顺序，依赖于importFields的配置顺序
	 */
	@Column
    @Comment("导入时校验excel的标题列顺序，依赖于importFields的配置顺序")
    @ColDefine(type = ColType.VARCHAR, width = 2000)
	private String needcheckorder;
	
	/**
	 * 数据处理类名
	 */
	@Column
    @Comment("数据处理类名")
    @ColDefine(type = ColType.VARCHAR, width = 200)
	private String data_handler;
	
	/**
	 * 字典处理类名
	 */
	@Column
    @Comment("字典处理类名")
    @ColDefine(type = ColType.VARCHAR, width = 200)
	private String dic_handler;
	
	/**
	 * key-value读取标记，已这个为key，后面的cell为value，多个则为list
	 */
	@Column
    @Comment("key-value读取标记，已这个为key，后面的cell为value，多个则为list")
    @ColDefine(type = ColType.VARCHAR, width = 200)
	private String keymark;
	
	/**
	 * 按照key-value规则读取全局扫描excel
	 */
	@Column
    @Comment("按照key-value规则读取全局扫描excel")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String readsinglecell;
	
	/**
	 * 备注
	 */
	@Column
    @Comment("备注")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String remark;
	
	/**
	 * 是否已删除
	 */
	@Column
    @Comment("是否已删除")
    @ColDefine(type = ColType.INT, width = 1)
	private Integer is_del;
	
	/**
	 * 创建时间
	 */
	@Column
	@Comment("创建时间")
	@ColDefine(type = ColType.DATETIME)
	private Date create_time;
	
	/**
	 * 创建人ID
	 */
	@Column
    @Comment("创建人ID")
    @ColDefine(type = ColType.INT, width = 10)
	private Integer create_by;
	
	/**
	 * 修改时间
	 */
	@Column
	@Comment("修改时间")
	@ColDefine(type = ColType.DATETIME)
	private Date update_time;
	
	/**
	 * 修改人ID
	 */
	@Column
    @Comment("修改人ID")
    @ColDefine(type = ColType.INT, width = 10)
	private Integer update_by;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getStartrows() {
		return startrows;
	}

	public void setStartrows(Integer startrows) {
		this.startrows = startrows;
	}

	public Integer getTitlerows() {
		return titlerows;
	}

	public void setTitlerows(Integer titlerows) {
		this.titlerows = titlerows;
	}

	public Integer getHeadrows() {
		return headrows;
	}

	public void setHeadrows(Integer headrows) {
		this.headrows = headrows;
	}

	public Integer getKeyindex() {
		return keyindex;
	}

	public void setKeyindex(Integer keyindex) {
		this.keyindex = keyindex;
	}

	public Integer getStartsheetindex() {
		return startsheetindex;
	}

	public void setStartsheetindex(Integer startsheetindex) {
		this.startsheetindex = startsheetindex;
	}

	public Integer getSheetnum() {
		return sheetnum;
	}

	public void setSheetnum(Integer sheetnum) {
		this.sheetnum = sheetnum;
	}

	public String getNeedsave() {
		return needsave;
	}

	public void setNeedsave(String needsave) {
		this.needsave = needsave;
	}

	public String getVerifygroup() {
		return verifygroup;
	}

	public void setVerifygroup(String verifygroup) {
		this.verifygroup = verifygroup;
	}

	public String getSaveurl() {
		return saveurl;
	}

	public void setSaveurl(String saveurl) {
		this.saveurl = saveurl;
	}

	public String getNeedverify() {
		return needverify;
	}

	public void setNeedverify(String needverify) {
		this.needverify = needverify;
	}

	public String getVerifyhandler() {
		return verifyhandler;
	}

	public void setVerifyhandler(String verifyhandler) {
		this.verifyhandler = verifyhandler;
	}

	public Integer getLastofinvalirow() {
		return lastofinvalirow;
	}

	public void setLastofinvalirow(Integer lastofinvalirow) {
		this.lastofinvalirow = lastofinvalirow;
	}

	public Integer getReadrows() {
		return readrows;
	}

	public void setReadrows(Integer readrows) {
		this.readrows = readrows;
	}

	public String getImportfields() {
		return importfields;
	}

	public void setImportfields(String importfields) {
		this.importfields = importfields;
	}

	public String getNeedcheckorder() {
		return needcheckorder;
	}

	public void setNeedcheckorder(String needcheckorder) {
		this.needcheckorder = needcheckorder;
	}

	public String getData_handler() {
		return data_handler;
	}

	public void setData_handler(String data_handler) {
		this.data_handler = data_handler;
	}

	public String getDic_handler() {
		return dic_handler;
	}

	public void setDic_handler(String dic_handler) {
		this.dic_handler = dic_handler;
	}

	public String getKeymark() {
		return keymark;
	}

	public void setKeymark(String keymark) {
		this.keymark = keymark;
	}

	public String getReadsinglecell() {
		return readsinglecell;
	}

	public void setReadsinglecell(String readsinglecell) {
		this.readsinglecell = readsinglecell;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIs_del() {
		return is_del;
	}

	public void setIs_del(Integer is_del) {
		this.is_del = is_del;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Integer getCreate_by() {
		return create_by;
	}

	public void setCreate_by(Integer create_by) {
		this.create_by = create_by;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public Integer getUpdate_by() {
		return update_by;
	}

	public void setUpdate_by(Integer update_by) {
		this.update_by = update_by;
	}
	
	
}
