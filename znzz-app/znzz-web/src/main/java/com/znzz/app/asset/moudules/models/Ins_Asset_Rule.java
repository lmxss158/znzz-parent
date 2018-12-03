package com.znzz.app.asset.moudules.models;

import java.io.Serializable;
import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

import com.znzz.framework.base.model.BaseModel;

/**
 * 规格型号实体类
 * @classname Ins_Asset_Rule.java
 * @author chenzhongliang
 * @date 2017年12月26日
 * @Description 
 */
@Table("ins_assets_version")
@Comment("资产型号规格")
@TableIndexes({@Index(name="INDEX_ASSETS_VERSION_DEVICEVERSIONORG",fields={"deviceVersionOrg"},unique=true)})
public class Ins_Asset_Rule extends BaseModel implements Serializable{

		private static final long serialVersionUID = -6651982948187678047L;

		@Id
		@Column
		@Comment("id")
		@ColDefine(type = ColType.INT, width = 10)
		private Integer id;

		
		@Column
		@Comment("型号")
		@ColDefine(type = ColType.VARCHAR, width = 60)
		private String deviceVersion;

		@Column
		@Comment("型号（自创）")
		@ColDefine(type = ColType.VARCHAR, width = 255)
		private String deviceVersionOrg;
		
		@Column
		@Comment("资产名称")
		@ColDefine(type = ColType.VARCHAR, width = 60)
		private String assetName;
		
		/*@Column
		@Comment("规格名称")
		@ColDefine(type = ColType.VARCHAR, width = 60)
		private String ggName;

		@Column
		@Comment("精度等级:（0特级1一级2二级）")
		@ColDefine(type = ColType.INT, width = 1)
		private Integer accuracyClass;

		@Column
		@Comment("国别厂家")
		@ColDefine(type = ColType.VARCHAR, width = 60)
		private String manufactor;

		@Column
		@Comment("原值")
		@ColDefine(type = ColType.INT, width = 60)
		private Integer originalValue;*/
		
		@Comment("添加时间")
		@Column
		@ColDefine(type = ColType.DATETIME)
		private Date createTime;
		
		@Comment("图片路径")
		@Column
		@ColDefine(type = ColType.VARCHAR, width = 300)
		private String urlImage;

	public String getDeviceVersionOrg() {
		return deviceVersionOrg;
	}

	public void setDeviceVersionOrg(String deviceVersionOrg) {
		this.deviceVersionOrg = deviceVersionOrg;
	}

	public String getUrlImage() {
			return urlImage;
		}


		public void setUrlImage(String urlImage) {
			this.urlImage = urlImage;
		}


		public Integer getId() {
			return id;
		}


		public void setId(Integer id) {
			this.id = id;
		}


		public String getDeviceVersion() {
			return deviceVersion;
		}


		public void setDeviceVersion(String deviceVersion) {
			this.deviceVersion = deviceVersion;
		}


		public String getAssetName() {
			return assetName;
		}


		public void setAssetName(String assetName) {
			this.assetName = assetName;
		}

		/*public String getGgName() {
			return ggName;
		}


		public void setGgName(String ggName) {
			this.ggName = ggName;
		}


		public Integer getAccuracyClass() {
			return accuracyClass;
		}


		public void setAccuracyClass(Integer accuracyClass) {
			this.accuracyClass = accuracyClass;
		}



		public String getManufactor() {
			return manufactor;
		}


		public void setManufactor(String manufactor) {
			this.manufactor = manufactor;
		}


		public Integer getOriginalValue() {
			return originalValue;
		}


		public void setOriginalValue(Integer originalValue) {
			this.originalValue = originalValue;
		}


*/

		public Date getCreateTime() {
			return createTime;
		}


		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
		
}
