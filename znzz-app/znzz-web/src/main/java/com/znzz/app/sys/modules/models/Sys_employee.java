package com.znzz.app.sys.modules.models;

import java.io.Serializable;
import java.util.List;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.EL;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.ManyMany;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

import com.znzz.framework.base.model.BaseModel;
/**
 * 
 * @ClassName: Sys_employ
 * @Description: 职工信息
 * @author pengmantai
 * @date 2017年8月17日 下午3:51:10
 */
@Table("sys_employee")
public class Sys_employee  extends BaseModel implements Serializable{
	
	  private static final long serialVersionUID = 1L;
		@Id
		@Column
		@Comment("id")
		@ColDefine(type = ColType.INT, width = 10)
		private Integer id;
	    

	    @Column
	    @Comment("职工姓名")
	    @ColDefine(type = ColType.VARCHAR, width = 120)
	    private String name;

	    @Column
	    @Comment("职工工号")
	    @ColDefine(type = ColType.VARCHAR, width = 100)
	    private String jobNumber;// transient 修饰符可让此字段不在对象里显示 
	    
	    @Column
	    @Comment("职工状态：0所内正式职工1返聘职工2外聘职工3劳务派遣4外包人员5在读研究生6实习生")
	    @ColDefine(type = ColType.INT, width = 60)
	    private Integer  is_service;// 
	    
	    @Column
	    @Comment("身份证号")
	    @ColDefine(type = ColType.VARCHAR, width = 100)
	    private String idNumber;
	    
	    @Column
	    @Comment("出入证号")
	    @ColDefine(type = ColType.VARCHAR, width = 100)
	    private String entryNumber;
	     
	    @Column
	    @Comment("职工电话")
	    @ColDefine(type = ColType.VARCHAR, width = 100)
	    private String telephone;// transient 修饰符可让此字段不在对象里显示 
	    
	    @Column
	    @ColDefine(type = ColType.VARCHAR, width = 32)
	    private String unitid;
	    
	    private String unitName; //存储单位名称的容器
	    
	    @One(field = "unitid")
	    private Sys_unit unit;

	 /*   @ManyMany(from = "employeeId", relation = "sys_employee_unit", to = "unitId")
	    protected List<Sys_unit> units;*/
	
	    
	    
	    
		public String getUnitid() {
			return unitid;
		}

		

		public String getUnitName() {
			return unitName;
		}



		public void setUnitName(String unitName) {
			this.unitName = unitName;
		}



		public String getIdNumber() {
			return idNumber;
		}



		public void setIdNumber(String idNumber) {
			this.idNumber = idNumber;
		}


		public String getEntryNumber() {
			return entryNumber;
		}



		public void setEntryNumber(String entryNumber) {
			this.entryNumber = entryNumber;
		}



		public void setUnitid(String unitid) {
			this.unitid = unitid;
		}

		public Integer getIs_service() {
			return is_service;
		}

		public void setIs_service(Integer is_service) {
			this.is_service = is_service;
		}

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

	

		public String getJobNumber() {
			return jobNumber;
		}

		public void setJobNumber(String jobNumber) {
			this.jobNumber = jobNumber;
		}

		public String getTelephone() {
			return telephone;
		}

		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}

		public Sys_unit getUnit() {
			return unit;
		}

		public void setUnit(Sys_unit unit) {
			this.unit = unit;
		}
	    
	    
}
