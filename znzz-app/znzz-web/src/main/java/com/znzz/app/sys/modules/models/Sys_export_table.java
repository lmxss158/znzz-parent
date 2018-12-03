package com.znzz.app.sys.modules.models;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
@Table("sys_export_table")
public class Sys_export_table {
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
	 * 导出文件类型xlsx,xls,pdf,docx
	 */
	@Column
    @Comment("导出文件的类型")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String type;
	
	/**
	 * 文件名
	 */
	@Column
    @Comment("文件名")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String file_name;
	
	/**
	 * sheet名
	 */
	@Column
    @Comment("sheet名")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String sheet_name;
	
	/**
	 * 标题
	 */
	@Column
    @Comment("标题")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String title;
	
	/**
	 * 标题高度
	 */
	@Column
    @Comment("标题高度")
    @ColDefine(type = ColType.INT, width = 10)
	private Integer title_height;
	
	/**
	 * 第二标题
	 */
	@Column
    @Comment("第二标题")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String second_title;
	
	/**
	 * 第二标题高度
	 */
	@Column
    @Comment("第二标题高度")
    @ColDefine(type = ColType.INT, width = 10)
	private Integer second_title_height;
	
	/**
	 * 排除列号,逗号分隔
	 */
	@Column
    @Comment("排除列号,逗号分隔")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String exclusions;
	
	/**
	 * 是否需要序列号
	 */
	@Column
    @Comment("是否需要序列号")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String addindex;
	
	/**
	 * 序列号名
	 */
	@Column
    @Comment("序列号名")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String indexname;
	
	/**
	 * 冻结列号
	 */
	@Column
    @Comment("冻结列号")
    @ColDefine(type = ColType.INT, width = 10)
	private Integer freezecol;
	
	/**
	 * 标题颜色
	 */
	@Column
    @Comment("标题颜色")
    @ColDefine(type = ColType.INT, width = 10)
	private Integer color;
	
	/**
	 * 第二标题颜色
	 */
	@Column
    @Comment("第二标题颜色")
    @ColDefine(type = ColType.INT, width = 10)
	private Integer second_color;
	
	/**
	 * 表头颜色
	 */
	@Column
    @Comment("表头颜色")
    @ColDefine(type = ColType.INT, width = 10)
	private Integer header_color;
	
	/**
	 * 表格格式类名
	 */
	@Column
    @Comment("表格格式类名")
    @ColDefine(type = ColType.VARCHAR, width = 200)
	private String style;
	
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
	 * 表头高度
	 */
	@Column
    @Comment("表头高度")
    @ColDefine(type = ColType.INT, width = 10)
	private Integer header_height;
	
	/**
	 * 是否创建表头
	 */
	@Column
    @Comment("是否创建表头")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String is_create_header;
	
	/**
	 * 表格高度
	 */
	@Column
    @Comment("表格高度")
    @ColDefine(type = ColType.INT, width = 10)
	private Integer height;
	
	/**
	 * 是否模板导出
	 */
	@Column
    @Comment("是否模板导出")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String is_template;
	
	/**
	 * 模板地址
	 */
	@Column
    @Comment("模板地址")
    @ColDefine(type = ColType.VARCHAR, width = 200)
	private String template_url;
	
	/**
	 * 先循环处理列
	 */
	@Column
    @Comment("先循环处理列")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String is_column_foreach;
	
	/**
	 * 导出sheet号
	 */
	@Column
    @Comment("导出sheet号")
    @ColDefine(type = ColType.INT, width = 10)
	private Integer sheet_num;
	
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
    @Comment("导出sheet号")
    @ColDefine(type = ColType.INT, width = 10)
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
	 * 更新时间
	 */
	@Column
	@Comment("更新时间")
	@ColDefine(type = ColType.DATETIME)
	private Date update_time;
	
	/**
	 * 更新人ID
	 */
	@Column
    @Comment("更新人ID")
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

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getSheet_name() {
		return sheet_name;
	}

	public void setSheet_name(String sheet_name) {
		this.sheet_name = sheet_name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getTitle_height() {
		return title_height;
	}

	public void setTitle_height(Integer title_height) {
		this.title_height = title_height;
	}

	public String getSecond_title() {
		return second_title;
	}

	public void setSecond_title(String second_title) {
		this.second_title = second_title;
	}

	public Integer getSecond_title_height() {
		return second_title_height;
	}

	public void setSecond_title_height(Integer second_title_height) {
		this.second_title_height = second_title_height;
	}

	public String getExclusions() {
		return exclusions;
	}

	public void setExclusions(String exclusions) {
		this.exclusions = exclusions;
	}

	public String getAddindex() {
		return addindex;
	}

	public void setAddindex(String addindex) {
		this.addindex = addindex;
	}

	public String getIndexname() {
		return indexname;
	}

	public void setIndexname(String indexname) {
		this.indexname = indexname;
	}

	public Integer getFreezecol() {
		return freezecol;
	}

	public void setFreezecol(Integer freezecol) {
		this.freezecol = freezecol;
	}

	public Integer getColor() {
		return color;
	}

	public void setColor(Integer color) {
		this.color = color;
	}

	public Integer getSecond_color() {
		return second_color;
	}

	public void setSecond_color(Integer second_color) {
		this.second_color = second_color;
	}

	public Integer getHeader_color() {
		return header_color;
	}

	public void setHeader_color(Integer header_color) {
		this.header_color = header_color;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
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

	public Integer getHeader_height() {
		return header_height;
	}

	public void setHeader_height(Integer header_height) {
		this.header_height = header_height;
	}

	public String getIs_create_header() {
		return is_create_header;
	}

	public void setIs_create_header(String is_create_header) {
		this.is_create_header = is_create_header;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getIs_template() {
		return is_template;
	}

	public void setIs_template(String is_template) {
		this.is_template = is_template;
	}

	public String getTemplate_url() {
		return template_url;
	}

	public void setTemplate_url(String template_url) {
		this.template_url = template_url;
	}

	public String getIs_column_foreach() {
		return is_column_foreach;
	}

	public void setIs_column_foreach(String is_column_foreach) {
		this.is_column_foreach = is_column_foreach;
	}

	public Integer getSheet_num() {
		return sheet_num;
	}

	public void setSheet_num(Integer sheet_num) {
		this.sheet_num = sheet_num;
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
