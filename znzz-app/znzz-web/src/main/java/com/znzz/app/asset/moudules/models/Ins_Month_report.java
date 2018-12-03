package com.znzz.app.asset.moudules.models;

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
 * @ClassName: Ins_Month_report   
 * @Description: TODO(月度报告)
 * @author fengguoxin
 * @date 2017年9月12日 上午11:16:36   
 */
@Table("ins_month_report")
@Comment("月度报告")
public class  Ins_Month_report extends BaseModel implements Serializable{

		private static final long serialVersionUID = -4244372815491966552L;


		@Id
		@Column
		@Comment("id")
		@ColDefine(type = ColType.INT, width = 10)
		private Integer id;
		
		
		@Column
		@Comment("文件名称")
		@ColDefine(type = ColType.VARCHAR, width = 60)
		private String fileName;
		
		@Comment("月度")
		@Column
		@ColDefine(type = ColType.DATETIME)
		private Date month;
		
		@Column
		@Comment("excle保存路径")
		@ColDefine(type = ColType.VARCHAR, width = 200)
		private String filePath;
		

		public String getFilePath() {
			return filePath;
		}


		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}


		public String getFileName() {
			return fileName;
		}


		public void setFileName(String fileName) {
			this.fileName = fileName;
		}


		public Integer getId() {
			return id;
		}


		public void setId(Integer id) {
			this.id = id;
		}


		public Date getMonth() {
			return month;
		}


		public void setMonth(Date month) {
			this.month = month;
		}

		

		
}
