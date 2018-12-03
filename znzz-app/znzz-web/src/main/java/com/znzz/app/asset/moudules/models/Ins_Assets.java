package com.znzz.app.asset.moudules.models;

import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.znzz.framework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author pengmantai
 * @ClassName: Ins_Assets
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2017年8月17日 上午10:56:22
 */
@Table("ins_assets_info")
@Comment("资产信息表")
@TableIndexes({@Index(name = "INDEX_ASSETS_INFO_ASSETCODE", fields = {"assetCode"}, unique = true)})
public class Ins_Assets extends BaseModel implements Serializable, IExcelDataModel, IExcelModel {

    private static final long serialVersionUID = -6651982948187678047L;

    @Id
    @Column
    @Comment("id")
    @ColDefine(type = ColType.INT, width = 10)
    private Integer id;

    @Column
    @Comment("统一编号")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String assetCode;

    @Column
    @Comment("规格名称")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String ggName;

    @Column
    @Comment("精度等级:（0特级1一级2二级）")
    @ColDefine(type = ColType.INT, width = 1)
    @Deprecated
    private Integer accuracyClass;

    @Column
    @Comment("国别")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String country;

    @Column
    @Comment("厂家")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String manufactor;

    @Column
    @Comment("原值")
    @ColDefine(customType = "double", width = 10, precision = 2)
    private Double originalValue;

    @Column
    @Comment("资产种类:（0全部1设备2仪器3工量具）")
    @ColDefine(type = ColType.INT, width = 1)
    private Integer assetType;

    @Column
    @Comment("资产规格/型号")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String deviceVersion;

    @Column
    @Comment("出厂日期")
    @ColDefine(type = ColType.DATETIME)
    private Date factoryTime;

    @Column
    @Comment("启用时间")
    @ColDefine(type = ColType.DATETIME)
    private Date enableTime;

    @Column
    @Comment("录入时间")
    @ColDefine(type = ColType.DATETIME)
    private Date createTime;

    @Column
    @Comment("出厂编号")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String serialNumber;

    @Column
    @Comment("是否借出:0是1否")
    @ColDefine(type = ColType.INT, width = 1)
    private Integer isLend;

    @Column
    @Comment("使用单位")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String borrowDepart;

    @Column
    @Comment("责任人")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String chargePerson;

    @Column
    @Comment("借用日期")
    @ColDefine(type = ColType.DATETIME)
    private Date lendDate;

    @Column
    @Comment("归还日期")
    @ColDefine(type = ColType.DATETIME)
    private Date returnDate;

    @Column
    @Comment("转账日期")
    @ColDefine(type = ColType.DATETIME)
    private Date transferAccountsDate;

    @Column
    @Comment("归还人")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String returnPerson;
    @Column
    @Comment("办理人")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String handlePerson;

    @Column
    @Comment("验收人")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String checker;

    @Column
    @Comment("图片路径")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String imagePath;

    @Column
    @Comment("管理状态:0在用/1封存/2闲置/3禁用")
    @ColDefine(type = ColType.INT, width = 1)
    private Integer manageStatus;

    @Column
    @Comment("管理级别:0厂(所)管/1院管/2总公司部管")
    @ColDefine(type = ColType.INT, width = 1)
    private Integer manageLevel;

    @Column
    @Comment("完好状态:0完好/1不完好")
    @ColDefine(type = ColType.INT, width = 1)
    private Integer completeStatus;

    @Column
    @Comment("预约是否可用(1被占用 2_可借)")
    @ColDefine(type = ColType.INT, width = 4)
    private Integer isOrder;


    @Column
    @Comment("资产类别:0固定资产/1低值/2在建/3附件/4零值")
    @ColDefine(type = ColType.INT, width = 1)
    private Integer assetCategory;

    @Column
    @Comment("仪器类别:普通/精大贵希/进口普通/进口精大贵希")
    @ColDefine(type = ColType.INT, width = 1)
    private Integer instrumentCategory;

    @Column
    @Comment("资金来源:0拨款、1自购、2科研费、3馈赠或无偿调入")
    @ColDefine(type = ColType.INT, width = 1)
    private Integer fundSources;

    @Column
    @Comment("项目名称")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String projectName;

    @Column
    @Comment("合同号")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String contractCode;

    @Column
    @Comment("批件序号")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String batchCode;

    @Column
    @Comment("功率")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String power;

    @Column
    @Comment("保修期")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String warrantyPeriod;


    @Column
    @Comment("报废状态")
    @ColDefine(type = ColType.INT, width = 1)
    private Integer scrapState;

    @Column
    @Comment("维修状态")
    @ColDefine(type = ColType.INT, width = 1)
    private Integer repairState;

    @Column
    @Comment("位置信息")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String locationInfo;

    @Column
    @Comment("台时信息")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String macHour;

    @Column
    @Comment("课题号")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String topicNo;

    @Column
    @Comment("供货单位")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String supplier;

    @Column
    @Comment("开箱日期")
    @ColDefine(type = ColType.DATETIME)
    private Date unpackingDate;

    @Column
    @Comment("折旧年限")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String depreciationYear;

    @Column
    @Comment("是否连接云网")
    @ColDefine(type = ColType.INT, width = 10)
    private Integer isConnectCloud;

    @Column
    @Comment("备注")
    @ColDefine(type = ColType.VARCHAR, width = 200)
    private String remark;
    @Column
    @Comment("技术指标")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String technicalIndex;
    @Column
    @Comment("成本值")
    @ColDefine(customType = "double", width = 10, precision = 2)
    private Double costValue;
    @Column
    @Comment("军工关键设备:0是/1否")
    @ColDefine(type = ColType.INT, width = 1)
    private Integer isMilitary;
    @Column
    @Comment("用途:0科研类、1生产类、2测量类、3办公类")
    @ColDefine(type = ColType.INT, width = 1)
    private Integer useType;

    @Column
    @Comment("年折旧率 ")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String depreciationRate;

    @Column
    @Comment("条形码图片路径")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String barCodeImage;

    @Column
    @Comment("是否过期")
    @ColDefine(type = ColType.INT, width = 1)
    private Integer isOverdue;

    @Column
    @Comment("检定日期")
    @ColDefine(type = ColType.DATE)
    private Date examineDate;

    @Column
    @Comment("到期检定日期")
    @ColDefine(type = ColType.DATE)
    private Date dueDate;

    @Column
    @Comment("是否需要计量")
    @ColDefine(type = ColType.INT, width = 1)
    private Integer isMeasure;

    @Column
    @Comment("禁用时间")
    @ColDefine(type = ColType.DATETIME)
    private Date forbidDate;

    @Column
    @Comment("重量")
    @ColDefine(type = ColType.FLOAT, width = 10, precision = 2)
    private Double weight;

    @Column
    @Comment("装机容量")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String installedCapacity;

    private Integer editState;

    private String assetName;

    private String urlImage;

    private Double originalValue1;

    private Double originalValue2;

    private Double weight1;
    private Double weight2;

    private String factoryTimeRange;
    private String returnDateRange;
    private String lendDateRange;
    private String enableTimeRange;
    private String unpackingDateRange;
    private String dueDateRange;
    private String examineDateRange;

    private int rowNum;
    private String errorMsg;

    private String unit;
    private String userName;
    private String userId;

    private String oldCode;//原本的assetCode

    public String getOldCode() {
        return oldCode;
    }

    public void setOldCode(String oldCode) {
        this.oldCode = oldCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getEditState() {
        return editState;
    }

    public void setEditState(Integer editState) {
        this.editState = editState;
    }

    public Integer getIsMeasure() {
        return isMeasure;
    }

    public void setIsMeasure(Integer isMeasure) {
        this.isMeasure = isMeasure;
    }

    public Double getCostValue() {
        return costValue;
    }

    public void setCostValue(Double costValue) {
        this.costValue = costValue;
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

    public void setOriginalValue(Double originalValue) {
        this.originalValue = originalValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getIsConnectCloud() {
        return isConnectCloud;
    }

    public void setIsConnectCloud(Integer isConnectCloud) {
        this.isConnectCloud = isConnectCloud;
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

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
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

    public String getDepreciationRate() {
        return depreciationRate;
    }

    public void setDepreciationRate(String depreciationRate) {
        this.depreciationRate = depreciationRate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReturnPerson() {
        return returnPerson;
    }

    public void setReturnPerson(String returnPerson) {
        this.returnPerson = returnPerson;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
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

    public Double getOriginalValue() {
        return originalValue;
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

    public Integer getIsMilitary() {
        return isMilitary;
    }

    public void setIsMilitary(Integer isMilitary) {
        this.isMilitary = isMilitary;
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

    public String getTechnicalIndex() {
        return technicalIndex;
    }

    public void setTechnicalIndex(String technicalIndex) {
        this.technicalIndex = technicalIndex;
    }

    public Integer getUseType() {
        return useType;
    }

    public void setUseType(Integer useType) {
        this.useType = useType;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getHandlePerson() {
        return handlePerson;
    }

    public void setHandlePerson(String handlePerson) {
        this.handlePerson = handlePerson;
    }

    public String getGgName() {
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

    public Date getForbidDate() {
        return forbidDate;
    }

    public void setForbidDate(Date forbidDate) {
        this.forbidDate = forbidDate;
    }

    @Override
    public int getRowNum() {
        return rowNum;
    }

    @Override
    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getTransferAccountsDate() {
        return transferAccountsDate;
    }

    public void setTransferAccountsDate(Date transferAccountsDate) {

        this.transferAccountsDate = transferAccountsDate;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getInstalledCapacity() {
        return installedCapacity;
    }

    public void setInstalledCapacity(String installedCapacity) {

        this.installedCapacity = installedCapacity;
    }

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
}
