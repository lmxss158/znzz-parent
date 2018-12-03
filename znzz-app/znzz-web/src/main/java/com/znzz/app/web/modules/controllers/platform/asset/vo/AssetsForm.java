package com.znzz.app.web.modules.controllers.platform.asset.vo;

import java.io.Serializable;
import java.util.Date;

import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;

public class AssetsForm implements Serializable,IExcelDataModel,IExcelModel{


	private Integer id;

	private String ids;

	private String assetCode;
	private String ggName;

	private Date createTime;

	private String country;

	private String manufactor;

	private Double originalValue;

	private Integer assetType;

	private String deviceVersion;

	private Date factoryTime;

	private Date enableTime;

	private String serialNumber;

	private Integer isLend;

	private String borrowDepart;

	private String chargePerson;
	private Date lendDate;

	private Date returnDate;

	private String returnPerson;
	private String handlePerson;

	private String checker;

	private String imagePath;

	private Integer manageStatus;

	private Integer manageLevel;

	private Integer completeStatus;

	private Integer assetCategory;

	private Integer instrumentCategory;
	private Integer fundSources;

	private String projectName;

	private String contractCode;

	private String batchCode;

	private String power;

	private String warrantyPeriod;
	private Integer scrapState;

	private Integer repairState;

	private String locationInfo;

	private String macHour;

	private String topicNo;

	private String supplier;
	private Date unpackingDate;
	private String depreciationYear;
	private Integer isConnectCloud;

	private String remark;

	private String technicalIndex;

	private Double costValue;

	private Integer isMilitary;

	private Integer useType;
	private String depreciationRate;

	private Integer isOverdue;
	private Integer isOrder;
	private Date examineDate;

	private Date dueDate;

	private Integer isMeasure;

	private Integer editState;

	private String assetName;

	private String urlImage;

	private Double originalValue1;

	private Double originalValue2;

	private String factoryTimeRange;
	private String returnDateRange;
	private String lendDateRange;
	private String enableTimeRange;
	private String unpackingDateRange;
	private String dueDateRange;
	private String examineDateRange;
	private String  telephone;
	private String exportScrapState;
	private String barCodeImage;

	private String weight;
	private Double weight1;
	private Double weight2;
	private String installedCapacity;

	public Double getWeight1() {
		return weight1;
	}

	public void setWeight1(Double weight1) {
		this.weight1 = weight1;
	}

	public Double getWeight2() {
		return weight2;
	}

	public void setWeight2(Double weight2) {
		this.weight2 = weight2;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getInstalledCapacity() {
		return installedCapacity;
	}

	public void setInstalledCapacity(String installedCapacity) {
		this.installedCapacity = installedCapacity;
	}

	public String getExportScrapState() {
		return exportScrapState;
	}

	public void setExportScrapState(String exportScrapState) {
		this.exportScrapState = exportScrapState;
	}

	public Integer getIsOrder() {
		return isOrder;
	}

	public void setIsOrder(Integer isOrder) {
		this.isOrder = isOrder;
	}

	public String getBarCodeImage() {
		return barCodeImage;
	}

	public void setBarCodeImage(String barCodeImage) {
		this.barCodeImage = barCodeImage;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public String getGgName() {
		return ggName;
	}

	public void setGgName(String ggName) {
		this.ggName = ggName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getManufactor() {
		return manufactor;
	}

	public void setManufactor(String manufactor) {
		this.manufactor = manufactor;
	}

	public Double getOriginalValue() {
		return originalValue;
	}

	public void setOriginalValue(Double originalValue) {
		this.originalValue = originalValue;
	}

	public Integer getAssetType() {
		return assetType;
	}

	public void setAssetType(Integer assetType) {
		this.assetType = assetType;
	}

	public String getDeviceVersion() {
		return deviceVersion;
	}

	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
	}

	public Date getFactoryTime() {
		return factoryTime;
	}

	public void setFactoryTime(Date factoryTime) {
		this.factoryTime = factoryTime;
	}

	public Date getEnableTime() {
		return enableTime;
	}

	public void setEnableTime(Date enableTime) {
		this.enableTime = enableTime;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Integer getIsLend() {
		return isLend;
	}

	public void setIsLend(Integer isLend) {
		this.isLend = isLend;
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

	public Date getLendDate() {
		return lendDate;
	}

	public void setLendDate(Date lendDate) {
		this.lendDate = lendDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getReturnPerson() {
		return returnPerson;
	}

	public void setReturnPerson(String returnPerson) {
		this.returnPerson = returnPerson;
	}

	public String getHandlePerson() {
		return handlePerson;
	}

	public void setHandlePerson(String handlePerson) {
		this.handlePerson = handlePerson;
	}

	public String getChecker() {
		return checker;
	}

	public void setChecker(String checker) {
		this.checker = checker;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Integer getManageStatus() {
		return manageStatus;
	}

	public void setManageStatus(Integer manageStatus) {
		this.manageStatus = manageStatus;
	}

	public Integer getManageLevel() {
		return manageLevel;
	}

	public void setManageLevel(Integer manageLevel) {
		this.manageLevel = manageLevel;
	}

	public Integer getCompleteStatus() {
		return completeStatus;
	}

	public void setCompleteStatus(Integer completeStatus) {
		this.completeStatus = completeStatus;
	}

	public Integer getAssetCategory() {
		return assetCategory;
	}

	public void setAssetCategory(Integer assetCategory) {
		this.assetCategory = assetCategory;
	}

	public Integer getInstrumentCategory() {
		return instrumentCategory;
	}

	public void setInstrumentCategory(Integer instrumentCategory) {
		this.instrumentCategory = instrumentCategory;
	}

	public Integer getFundSources() {
		return fundSources;
	}

	public void setFundSources(Integer fundSources) {
		this.fundSources = fundSources;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public String getPower() {
		return power;
	}

	public void setPower(String power) {
		this.power = power;
	}

	public String getWarrantyPeriod() {
		return warrantyPeriod;
	}

	public void setWarrantyPeriod(String warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}

	public Integer getScrapState() {
		return scrapState;
	}

	public void setScrapState(Integer scrapState) {
		this.scrapState = scrapState;
	}

	public Integer getRepairState() {
		return repairState;
	}

	public void setRepairState(Integer repairState) {
		this.repairState = repairState;
	}

	public String getLocationInfo() {
		return locationInfo;
	}

	public void setLocationInfo(String locationInfo) {
		this.locationInfo = locationInfo;
	}

	public String getMacHour() {
		return macHour;
	}

	public void setMacHour(String macHour) {
		this.macHour = macHour;
	}

	public String getTopicNo() {
		return topicNo;
	}

	public void setTopicNo(String topicNo) {
		this.topicNo = topicNo;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public Date getUnpackingDate() {
		return unpackingDate;
	}

	public void setUnpackingDate(Date unpackingDate) {
		this.unpackingDate = unpackingDate;
	}

	public String getDepreciationYear() {
		return depreciationYear;
	}

	public void setDepreciationYear(String depreciationYear) {
		this.depreciationYear = depreciationYear;
	}

	public Integer getIsConnectCloud() {
		return isConnectCloud;
	}

	public void setIsConnectCloud(Integer isConnectCloud) {
		this.isConnectCloud = isConnectCloud;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTechnicalIndex() {
		return technicalIndex;
	}

	public void setTechnicalIndex(String technicalIndex) {
		this.technicalIndex = technicalIndex;
	}

	public Double getCostValue() {
		return costValue;
	}

	public void setCostValue(Double costValue) {
		this.costValue = costValue;
	}

	public Integer getIsMilitary() {
		return isMilitary;
	}

	public void setIsMilitary(Integer isMilitary) {
		this.isMilitary = isMilitary;
	}

	public Integer getUseType() {
		return useType;
	}

	public void setUseType(Integer useType) {
		this.useType = useType;
	}

	public String getDepreciationRate() {
		return depreciationRate;
	}

	public void setDepreciationRate(String depreciationRate) {
		this.depreciationRate = depreciationRate;
	}

	public Integer getIsOverdue() {
		return isOverdue;
	}

	public void setIsOverdue(Integer isOverdue) {
		this.isOverdue = isOverdue;
	}

	public Date getExamineDate() {
		return examineDate;
	}

	public void setExamineDate(Date examineDate) {
		this.examineDate = examineDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Integer getIsMeasure() {
		return isMeasure;
	}

	public void setIsMeasure(Integer isMeasure) {
		this.isMeasure = isMeasure;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

	public String getFactoryTimeRange() {
		return factoryTimeRange;
	}

	public void setFactoryTimeRange(String factoryTimeRange) {
		this.factoryTimeRange = factoryTimeRange;
	}

	public String getReturnDateRange() {
		return returnDateRange;
	}

	public void setReturnDateRange(String returnDateRange) {
		this.returnDateRange = returnDateRange;
	}

	public String getLendDateRange() {
		return lendDateRange;
	}

	public void setLendDateRange(String lendDateRange) {
		this.lendDateRange = lendDateRange;
	}

	public String getEnableTimeRange() {
		return enableTimeRange;
	}

	public void setEnableTimeRange(String enableTimeRange) {
		this.enableTimeRange = enableTimeRange;
	}

	public String getUnpackingDateRange() {
		return unpackingDateRange;
	}

	public void setUnpackingDateRange(String unpackingDateRange) {
		this.unpackingDateRange = unpackingDateRange;
	}

	public String getDueDateRange() {
		return dueDateRange;
	}

	public void setDueDateRange(String dueDateRange) {
		this.dueDateRange = dueDateRange;
	}

	public String getExamineDateRange() {
		return examineDateRange;
	}

	public void setExamineDateRange(String examineDateRange) {
		this.examineDateRange = examineDateRange;
	}

	public String getErroeMsg() {
		return erroeMsg;
	}

	public void setErroeMsg(String erroeMsg) {
		this.erroeMsg = erroeMsg;
	}

	public Double getOriginalValue1() {
		return originalValue1;
	}

	public void setOriginalValue1(Double originalValue1) {
		this.originalValue1 = originalValue1;
	}

	public Double getOriginalValue2() {
		return originalValue2;
	}

	public void setOriginalValue2(Double originalValue2) {
		this.originalValue2 = originalValue2;
	}

	public Integer getEditState() {
		return editState;
	}

	public void setEditState(Integer editState) {
		this.editState = editState;
	}


	private String erroeMsg ;

	@Override
	public String getErrorMsg() {
		return erroeMsg;
	}

	@Override
	public void setErrorMsg(String errorMsg) {
		this.erroeMsg = errorMsg ; 
		
	}
	
	private int rowNum ; 
	@Override
	public int getRowNum() {
		return rowNum;
	}

	@Override
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum ;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
}
