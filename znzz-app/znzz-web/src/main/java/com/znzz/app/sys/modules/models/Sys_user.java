package com.znzz.app.sys.modules.models;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.EL;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.ManyMany;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

import com.znzz.framework.base.model.BaseModel;

/**
 * 用户
 */
@Table("sys_user")
@TableIndexes({ @Index(name = "INDEX_SYS_USER_LOGINNAMAE", fields = { "loginname" }, unique = true) })
public class Sys_user extends BaseModel implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column
	@Name
	@Comment("ID")
	@ColDefine(type = ColType.VARCHAR, width = 32)
	@Prev(els = { @EL("uuid()") })
	private String id;

	@Column
	@Comment("用户名")
	@ColDefine(type = ColType.VARCHAR, width = 120)
	private String loginname;

	@Column
	@Comment("密码")
	@ColDefine(type = ColType.VARCHAR, width = 100)
	private String password;// transient 修饰符可让此字段不在对象里显示

	@Column
	@Comment("密码盐")
	@ColDefine(type = ColType.VARCHAR, width = 50)
	private String salt;

	@Column
	@Comment("用户名")
	@ColDefine(type = ColType.VARCHAR, width = 100)
	private String username;

	@Column
	@Comment("是否在线")
	@ColDefine(type = ColType.BOOLEAN)
	private boolean isOnline;

	@Column
	@Comment("是否禁用")
	@ColDefine(type = ColType.BOOLEAN)
	private boolean disabled;

	@Column
	@ColDefine(type = ColType.VARCHAR, width = 255)
	private String email;

	@Column
	@Comment("登陆时间")
	@ColDefine(type = ColType.INT)
	private Integer loginAt;

	@Column
	@Comment("登陆IP")
	@ColDefine(type = ColType.VARCHAR, width = 255)
	private String loginIp;

	@Column
	@Comment("登陆次数")
	@ColDefine(type = ColType.INT)
	private Integer loginCount;

	@Column
	@Comment("常用菜单")
	@ColDefine(type = ColType.VARCHAR, width = 255)
	private String customMenu;

	@Column
	@Comment("皮肤样式")
	@ColDefine(type = ColType.VARCHAR, width = 100)
	private String loginTheme;

	@Column
	@Comment("昵称")
	@ColDefine(type = ColType.VARCHAR, width = 120)
	private String nickName;

	@Column
	@Comment("职工状态：0所内正式职工1返聘职工2外聘职工3劳务派遣4外包人员5在读研究生6实习生")
	@ColDefine(type = ColType.INT)
	private Integer is_service;//

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
	@Comment("用户状态：超级管理员1,普通用户2，职工3,离职4，未知5")
	@ColDefine(type = ColType.INT)
	private Integer userStatus;

	@Column
	private boolean loginSidebar;

	@Column
	private boolean loginBoxed;

	@Column
	private boolean loginScroll;

	@Column
	private boolean loginPjax;

	/*
	 * @Column
	 * 
	 * @ColDefine(type = ColType.INT, width = 10) private Integer employeeid;
	 * 
	 * @One(field = "employeeid") private Sys_employee employee;
	 */

	@Column
	@ColDefine(type = ColType.VARCHAR, width = 32)
	private String unitid;

	@One(field = "unitid")
	private Sys_unit unit;

	@ManyMany(from = "userId", relation = "sys_user_role", to = "roleId")
	private List<Sys_role> roles;

	@ManyMany(from = "userId", relation = "sys_user_unit", to = "unitId")
	protected List<Sys_unit> units;

	protected List<Sys_menu> menus;

	protected List<Sys_menu> firstMenus;

	protected Map<String, List<Sys_menu>> secondMenus;

	private List<Sys_menu> customMenus;

	private String unitName; // 存储单位名称的容器

	private List<String> userPermissionData;
	// private String statusName; //存储职工状态名称的容器

	public List<String> getUserPermissionData() {
		return userPermissionData;
	}

	public void setUserPermissionData(List<String> userPermissionData) {
		this.userPermissionData = userPermissionData;
	}

	public List<Sys_menu> userFastIntoMenu ;//存储用户的快捷菜单

	public List<Sys_menu> getUserFastIntoMenu() {
		return userFastIntoMenu;
	}

	public void setUserFastIntoMenu(List<Sys_menu> userFastIntoMenu) {
		this.userFastIntoMenu = userFastIntoMenu;
	}

	@Column
	@Comment("记录插入顺序")
	@ColDefine(type = ColType.INT, width = 32)
	@Prev(@SQL(value = "SELECT CASE WHEN max(orderby) IS NULL then 0 ELSE max(orderby) + 1 END FROM sys_user"))
	private Integer orderby;

	private String assetCodeArray;

	public String getAssetCodeArray() {
		return assetCodeArray;
	}

	public void setAssetCodeArray(String assetCodeArray) {
		this.assetCodeArray = assetCodeArray;
	}

	public Integer getOrderby() {
		return orderby;
	}

	public void setOrderby(Integer orderby) {
		this.orderby = orderby;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginname() {
		return loginname;
	}

	/*
	 * public String getStatusName() { return statusName; }
	 * 
	 * public void setStatusName(String statusName) { this.statusName =
	 * statusName; }
	 */
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getIs_service() {
		return is_service;
	}

	public void setIs_service(Integer is_service) {
		this.is_service = is_service;
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

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Integer getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setIsOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getLoginAt() {
		return loginAt;
	}

	public void setLoginAt(Integer loginAt) {
		this.loginAt = loginAt;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Integer getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}

	public String getCustomMenu() {
		return customMenu;
	}

	public void setCustomMenu(String customMenu) {
		this.customMenu = customMenu;
	}

	public String getLoginTheme() {
		return loginTheme;
	}

	public void setLoginTheme(String loginTheme) {
		this.loginTheme = loginTheme;
	}

	public boolean isLoginSidebar() {
		return loginSidebar;
	}

	public void setLoginSidebar(boolean loginSidebar) {
		this.loginSidebar = loginSidebar;
	}

	public boolean isLoginBoxed() {
		return loginBoxed;
	}

	public void setLoginBoxed(boolean loginBoxed) {
		this.loginBoxed = loginBoxed;
	}

	public boolean isLoginScroll() {
		return loginScroll;
	}

	public void setLoginScroll(boolean loginScroll) {
		this.loginScroll = loginScroll;
	}

	public boolean isLoginPjax() {
		return loginPjax;
	}

	public void setLoginPjax(boolean loginPjax) {
		this.loginPjax = loginPjax;
	}

	public String getUnitid() {
		return unitid;
	}

	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}

	public Sys_unit getUnit() {
		return unit;
	}

	public void setUnit(Sys_unit unit) {
		this.unit = unit;
	}

	public List<Sys_role> getRoles() {
		return roles;
	}

	public void setRoles(List<Sys_role> roles) {
		this.roles = roles;
	}

	public List<Sys_unit> getUnits() {
		return units;
	}

	public void setUnits(List<Sys_unit> units) {
		this.units = units;
	}

	public List<Sys_menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Sys_menu> menus) {
		this.menus = menus;
	}

	public List<Sys_menu> getFirstMenus() {
		return firstMenus;
	}

	public void setFirstMenus(List<Sys_menu> firstMenus) {
		this.firstMenus = firstMenus;
	}

	public Map<String, List<Sys_menu>> getSecondMenus() {
		return secondMenus;
	}

	public void setSecondMenus(Map<String, List<Sys_menu>> secondMenus) {
		this.secondMenus = secondMenus;
	}

	public List<Sys_menu> getCustomMenus() {
		return customMenus;
	}

	public void setCustomMenus(List<Sys_menu> customMenus) {
		this.customMenus = customMenus;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
}
