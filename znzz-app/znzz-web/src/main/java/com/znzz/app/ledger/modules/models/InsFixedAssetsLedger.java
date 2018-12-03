package com.znzz.app.ledger.modules.models;

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

@Table("ins_fixed_assets_ledger")
@Comment("固定资产台账信息表")
@TableIndexes({@Index(name="INDEX_FIXED_ASSETS_SEQUENCE",fields={"sequence"},unique = true)})
public class InsFixedAssetsLedger extends BaseModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2074465002112707253L;

	@Id
	@Comment("主键ID")
	@Column
	@ColDefine(type = ColType.INT)
	private Integer id;
	
	@Column
	@Comment("统一编码")
	@ColDefine(type = ColType.VARCHAR, width = 100)
	private String assetCode;
	
	@Column
	@Comment("序号")
	@ColDefine(type = ColType.VARCHAR, width = 100)
	private String sequence;

	@Column
	@Comment("大类别")
	@ColDefine(type = ColType.VARCHAR, width = 10)
	private String largeCategory;
	
	@Column
	@Comment("中类别")
	@ColDefine(type = ColType.VARCHAR, width = 10)
	private String mediumCategory;
	
	@Column
	@Comment("小类别")
	@ColDefine(type = ColType.VARCHAR, width = 10)
	private String smallCategory;
	
	@Column
	@Comment("检测设备")
	@ColDefine(type = ColType.VARCHAR,width = 60)
	private String checkDevice;
	
	@Column
	@Comment("军工专用")
	@ColDefine(type = ColType.VARCHAR,width = 60)	
	private String militarySpecial;
	
	@Column
	@Comment("未移交")
	@ColDefine(type = ColType.VARCHAR,width = 60)
	private String noHandOver;
	
	@Column
	@Comment("办理单位")
	@ColDefine(type = ColType.VARCHAR,width = 60)
	private String handleUnit;
	
	@Column
	@Comment("费用来源")
	@ColDefine(type = ColType.VARCHAR,width = 60)
	private String costSource;
	
	@Column
	@Comment("项目名称")
	@ColDefine(type = ColType.VARCHAR,width = 60)
	private String projectName;
	
	@Column
	@Comment("2017年保险")
	@ColDefine(type = ColType.VARCHAR,width = 60)
	private String insurance_2017;
	
	@Column
	@Comment("2017年测调设备")
	@ColDefine(type = ColType.VARCHAR,width = 60)
	private String testDevice_2017;
	
	@Column
	@Comment("固定资产名称")
	@ColDefine(type = ColType.VARCHAR,width = 60)
	private String fixedAssetsName;
	
	@Column
	@Comment("规格型号")
	@ColDefine(type = ColType.VARCHAR,width = 60)
	private String ruleVersion;
	
	@Column
	@Comment("技术指标")
	@ColDefine(type = ColType.VARCHAR,width = 60)
	private String technicalIndicator;
	
	@Column
	@Comment("制造厂")
	@ColDefine(type = ColType.VARCHAR,width = 60)
	private String factory;
	
	@Column
	@Comment("出厂编号")
	@ColDefine(type = ColType.VARCHAR,width = 60)
	private String factoryNumber;
	
	@Column
	@Comment("销售商")
	@ColDefine(type = ColType.VARCHAR,width = 60)
	private String salePerson;
	
	@Column
	@Comment("购进日期")
	@ColDefine(type = ColType.DATE)
	private Date purchaseDate;
	
	
	@Column
	@Comment("使用日期")
	@ColDefine(type = ColType.DATE)
	private Date useDate;
	
	@Column
	@Comment("购置年代")
	@ColDefine(type = ColType.VARCHAR)
	private String purchaseYear;

	@Column
	@Comment("使用单位")
	@ColDefine(type = ColType.VARCHAR,width = 60)
	private String borrowDepart;

	@Column
	@Comment("责任人")
	@ColDefine(type = ColType.VARCHAR,width = 60)
	private String chargePerson;
	
	
	@Column
	@Comment("安装地点")
	@ColDefine(type = ColType.VARCHAR,width = 60)
	private String installationSite;
	
	@Column
	@Comment("购入原值（元）")
	@ColDefine(customType="double",width=30,precision=2)
	private Double buyOriginal;
	
	@Column
	@Comment("增值（元）")
	@ColDefine(customType="double",width=30,precision=2)
	private Double increasePrice;
	
	@Column
	@Comment("财务原值（元）")
	@ColDefine(customType="double",width=30,precision=2)
	private Double accountOriginal;
	
	@Column
	@Comment("备注")
	@ColDefine(type = ColType.VARCHAR,width = 100)
	private String remark;
	
	@Column
	@Comment("退库或其他")
	@ColDefine(type = ColType.VARCHAR,width = 60)
	private String returnTreasuryOrOthers;
	
	
	@Column
	@Comment("退库（准备销毁）")
	@ColDefine(type = ColType.VARCHAR,width = 60)
	private String returnTreasuryForDestory;


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getSequence() {
		return sequence;
	}


	public void setSequence(String sequence) {
		this.sequence = sequence;
	}


	public String getLargeCategory() {
		return largeCategory;
	}


	public void setLargeCategory(String largeCategory) {
		this.largeCategory = largeCategory;
	}


	public String getMediumCategory() {
		return mediumCategory;
	}


	public void setMediumCategory(String mediumCategory) {
		this.mediumCategory = mediumCategory;
	}


	public String getSmallCategory() {
		return smallCategory;
	}


	public void setSmallCategory(String smallCategory) {
		this.smallCategory = smallCategory;
	}


	public String getCheckDevice() {
		return checkDevice;
	}


	public void setCheckDevice(String checkDevice) {
		this.checkDevice = checkDevice;
	}


	public String getMilitarySpecial() {
		return militarySpecial;
	}


	public void setMilitarySpecial(String militarySpecial) {
		this.militarySpecial = militarySpecial;
	}


	public String getNoHandOver() {
		return noHandOver;
	}


	public void setNoHandOver(String noHandOver) {
		this.noHandOver = noHandOver;
	}


	public String getHandleUnit() {
		return handleUnit;
	}


	public void setHandleUnit(String handleUnit) {
		this.handleUnit = handleUnit;
	}


	public String getCostSource() {
		return costSource;
	}


	public void setCostSource(String costSource) {
		this.costSource = costSource;
	}


	public String getProjectName() {
		return projectName;
	}


	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}


	public String getAssetCode() {
		return assetCode;
	}


	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}


	public String getInsurance_2017() {
		return insurance_2017;
	}


	public void setInsurance_2017(String insurance_2017) {
		this.insurance_2017 = insurance_2017;
	}


	public String getTestDevice_2017() {
		return testDevice_2017;
	}


	public void setTestDevice_2017(String testDevice_2017) {
		this.testDevice_2017 = testDevice_2017;
	}


	public String getFixedAssetsName() {
		return fixedAssetsName;
	}


	public void setFixedAssetsName(String fixedAssetsName) {
		this.fixedAssetsName = fixedAssetsName;
	}


	public String getRuleVersion() {
		return ruleVersion;
	}


	public void setRuleVersion(String ruleVersion) {
		this.ruleVersion = ruleVersion;
	}


	public String getTechnicalIndicator() {
		return technicalIndicator;
	}


	public void setTechnicalIndicator(String technicalIndicator) {
		this.technicalIndicator = technicalIndicator;
	}


	public String getFactory() {
		return factory;
	}


	public void setFactory(String factory) {
		this.factory = factory;
	}


	public String getFactoryNumber() {
		return factoryNumber;
	}


	public void setFactoryNumber(String factoryNumber) {
		this.factoryNumber = factoryNumber;
	}


	public String getSalePerson() {
		return salePerson;
	}


	public void setSalePerson(String salePerson) {
		this.salePerson = salePerson;
	}


	public Date getPurchaseDate() {
		return purchaseDate;
	}


	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}


	public Date getUseDate() {
		return useDate;
	}


	public void setUseDate(Date useDate) {
		this.useDate = useDate;
	}


	public String getPurchaseYear() {
		return purchaseYear;
	}


	public void setPurchaseYear(String purchaseYear) {
		this.purchaseYear = purchaseYear;
	}


	public String getBorrowDepart() {
		return borrowDepart;
	}


	public void setBorrowDepart(String borrowDepart) {
		this.borrowDepart = borrowDepart;
	}


	public String getChargePerson() {
		return chargePerson;
	}


	public void setChargePerson(String chargePerson) {
		this.chargePerson = chargePerson;
	}


	public String getInstallationSite() {
		return installationSite;
	}


	public void setInstallationSite(String installationSite) {
		this.installationSite = installationSite;
	}


	public Double getBuyOriginal() {
		return buyOriginal;
	}


	public void setBuyOriginal(Double buyOriginal) {
		this.buyOriginal = buyOriginal;
	}


	public Double getIncreasePrice() {
		return increasePrice;
	}


	public void setIncreasePrice(Double increasePrice) {
		this.increasePrice = increasePrice;
	}


	public Double getAccountOriginal() {
		return accountOriginal;
	}


	public void setAccountOriginal(Double accountOriginal) {
		this.accountOriginal = accountOriginal;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getReturnTreasuryOrOthers() {
		return returnTreasuryOrOthers;
	}


	public void setReturnTreasuryOrOthers(String returnTreasuryOrOthers) {
		this.returnTreasuryOrOthers = returnTreasuryOrOthers;
	}


	public String getReturnTreasuryForDestory() {
		return returnTreasuryForDestory;
	}


	public void setReturnTreasuryForDestory(String returnTreasuryForDestory) {
		this.returnTreasuryForDestory = returnTreasuryForDestory;
	}

 
}
