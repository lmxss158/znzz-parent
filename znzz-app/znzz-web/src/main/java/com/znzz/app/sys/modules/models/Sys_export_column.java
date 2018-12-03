package com.znzz.app.sys.modules.models;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
@Table("sys_export_column")
public class Sys_export_column {
	/**
	 * 流水号
	 */
	@Id
	@Column
	@Comment("id")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer id;
	
	/**
	 * table_id
	 */
	@Column
	@Comment("关联字段")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer table_id;
	
	/**
	 * 数据是map时的key
	 */
	@Column
    @Comment("数据是map时的key")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String map_key;
	
	/**
	 * 宽度
	 */
	@Column
	@Comment("宽度")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer width;
	
	/**
	 * 图片类型，1:文件 2:数据库
	 */
	@Column
    @Comment("图片类型，1:文件 2:数据库")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String image_type;
	
	/**
	 * 排序号
	 */
	@Column
	@Comment("排序号")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer order_num;
	
	/**
	 * 是否可以换行
	 */
	@Column
    @Comment("是否可以换行")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String is_wrap;
	
	/**
	 * 是否需要合并
	 */
	@Column
    @Comment("是否需要合并")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String need_merge;
	
	/**
	 * 相同内容合并
	 */
	@Column
    @Comment("相同内容合并")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String merge_vertical;
	
	/**
	 * 合并关联列号，逗号分隔
	 */
	@Column
    @Comment("合并关联列号，逗号分隔")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String merge_rely;
	
	/**
	 * 后缀
	 */
	@Column
    @Comment("后缀")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String suffix;
	
	/**
	 * 是否输出统计行
	 */
	@Column
    @Comment("是否输出统计行")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String is_statistics;
	
	/**
	 * 数字格式
	 */
	@Column
    @Comment("数字格式")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String num_format;
	
	/**
	 * 是否隐藏
	 */
	@Column
    @Comment("是否隐藏")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String is_column_hidden;
	
	/**
	 * 枚举字段
	 */
	@Column
    @Comment("枚举字段")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String enum_field;
	
	/**
	 * 父列表ID
	 */
	@Column
    @Comment("父列表ID")
    @ColDefine(type = ColType.INT, width = 10)
	private Integer parent_id;
	
	/**
	 * 列名
	 */
	@Column
    @Comment("列名")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String col_name;
	
	/**
	 * 列组名
	 */
	@Column
    @Comment("列组名")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String group_name;
	
	/**
	 * 列类型，1:字符串 3:图片 10:浮点数
	 */
	@Column
    @Comment("列类型，1:字符串 3:图片 10:浮点数")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String type;
	
	/**
	 * 数据库日期格式
	 */
	@Column
    @Comment("数据库日期格式")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String database_date_format;
	
	/**
	 * 导出日期格式
	 */
	@Column
    @Comment("数据库日期格式")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String date_format;
	
	/**
	 * 导入导出内容/值转换，逗号分隔
	 */
	@Column
    @Comment("导入导出内容/值转换，逗号分隔")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String export_replace;
	
	/**
	 * 字典名称
	 */
	@Column
    @Comment("字典名称")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String dict;
	
	/**
	 * 是否超链接
	 */
	@Column
    @Comment("是否超链接")
    @ColDefine(type = ColType.VARCHAR, width = 60)
	private String hyper_link;
	
	/**
	 * 导入列号
	 */
	@Column
    @Comment("导入列号")
    @ColDefine(type = ColType.INT, width = 60)
	private Integer fixed_index;
	
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

	public Integer getTable_id() {
		return table_id;
	}

	public void setTable_id(Integer table_id) {
		this.table_id = table_id;
	}

	public String getMap_key() {
		return map_key;
	}

	public void setMap_key(String map_key) {
		this.map_key = map_key;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public String getImage_type() {
		return image_type;
	}

	public void setImage_type(String image_type) {
		this.image_type = image_type;
	}

	public Integer getOrder_num() {
		return order_num;
	}

	public void setOrder_num(Integer order_num) {
		this.order_num = order_num;
	}

	public String getIs_wrap() {
		return is_wrap;
	}

	public void setIs_wrap(String is_wrap) {
		this.is_wrap = is_wrap;
	}

	public String getNeed_merge() {
		return need_merge;
	}

	public void setNeed_merge(String need_merge) {
		this.need_merge = need_merge;
	}

	public String getMerge_vertical() {
		return merge_vertical;
	}

	public void setMerge_vertical(String merge_vertical) {
		this.merge_vertical = merge_vertical;
	}

	public String getMerge_rely() {
		return merge_rely;
	}

	public void setMerge_rely(String merge_rely) {
		this.merge_rely = merge_rely;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getIs_statistics() {
		return is_statistics;
	}

	public void setIs_statistics(String is_statistics) {
		this.is_statistics = is_statistics;
	}

	public String getNum_format() {
		return num_format;
	}

	public void setNum_format(String num_format) {
		this.num_format = num_format;
	}

	public String getIs_column_hidden() {
		return is_column_hidden;
	}

	public void setIs_column_hidden(String is_column_hidden) {
		this.is_column_hidden = is_column_hidden;
	}

	public String getEnum_field() {
		return enum_field;
	}

	public void setEnum_field(String enum_field) {
		this.enum_field = enum_field;
	}

	public Integer getParent_id() {
		return parent_id;
	}

	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}

	public String getCol_name() {
		return col_name;
	}

	public void setCol_name(String col_name) {
		this.col_name = col_name;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDatabase_date_format() {
		return database_date_format;
	}

	public void setDatabase_date_format(String database_date_format) {
		this.database_date_format = database_date_format;
	}

	public String getDate_format() {
		return date_format;
	}

	public void setDate_format(String date_format) {
		this.date_format = date_format;
	}

	public String getExport_replace() {
		return export_replace;
	}

	public void setExport_replace(String export_replace) {
		this.export_replace = export_replace;
	}

	public String getDict() {
		return dict;
	}

	public void setDict(String dict) {
		this.dict = dict;
	}

	public String getHyper_link() {
		return hyper_link;
	}

	public void setHyper_link(String hyper_link) {
		this.hyper_link = hyper_link;
	}

	public Integer getFixed_index() {
		return fixed_index;
	}

	public void setFixed_index(Integer fixed_index) {
		this.fixed_index = fixed_index;
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
