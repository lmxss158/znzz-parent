package com.znzz.app.asset.moudules.models;

import com.znzz.framework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.Date;

@Table("ins_asset_inventory_record")
@Comment("盘点履历表")
public class Ins_Asset_Inventory_Record extends BaseModel implements Serializable {

    @Id
    @Column
    @Comment("id")
    private Integer id;

    @Column
    @Comment("统一编号")
    @ColDefine(type = ColType.VARCHAR, width = 60, notNull=true)
    private String assetCode;

    @Column
    @Comment("盘点位置")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String inventorySite;

    @Column
    @Comment("工号")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String jobNumber;

    @Column
    @Comment("盘点时间")
    @ColDefine(type = ColType.DATETIME)
    private  Date inventoryDate;

    private String assetType;         //资产类别
    private String assetName;        //资产名称
    private String assetModel;       //资产型号
    private String country;          //国别
    private String manufactor;       //厂家
    private Date   factoryTime;      //出厂日期
    private Date   lendDate;         //借出日期
    private Date   returnDate;       //归还日期
    private String serialNumber;     //出厂编号
    private String useDepartment;    //使用单位
    private String chargePerson;     //责任人
    private String assetOriginalSite;//资产原位置
    private String inventoryCheckerDepartment; //盘点人单位
    private String inventoryChecker;  //盘点人
    private Date inventoryBeginTime; //盘点开始时间
    private Date inventoryEndTime;   //盘点结束时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getInventorySite() {
        return inventorySite;
    }

    public void setInventorySite(String inventorySite) {
        this.inventorySite = inventorySite;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getInventoryChecker() {
        return inventoryChecker;
    }

    public void setInventoryChecker(String inventoryChecker) {
        this.inventoryChecker = inventoryChecker;
    }

    public String getInventoryCheckerDepartment() {
        return inventoryCheckerDepartment;
    }

    public void setInventoryCheckerDepartment(String inventoryCheckerDepartment) {
        this.inventoryCheckerDepartment = inventoryCheckerDepartment;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public Date getInventoryDate() {
        return inventoryDate;
    }

    public void setInventoryDate(Date inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetModel() {
        return assetModel;
    }

    public void setAssetModel(String assetModel) {
        this.assetModel = assetModel;
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

    public Date getFactoryTime() {
        return factoryTime;
    }

    public void setFactoryTime(Date factoryTime) {
        this.factoryTime = factoryTime;
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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getUseDepartment() {
        return useDepartment;
    }

    public void setUseDepartment(String useDepartment) {
        this.useDepartment = useDepartment;
    }

    public String getChargePerson() {
        return chargePerson;
    }

    public void setChargePerson(String chargePerson) {
        this.chargePerson = chargePerson;
    }

    public String getAssetOriginalSite() {
        return assetOriginalSite;
    }

    public void setAssetOriginalSite(String assetOriginalSite) {
        this.assetOriginalSite = assetOriginalSite;
    }

    public Date getInventoryBeginTime() {
        return inventoryBeginTime;
    }

    public void setInventoryBeginTime(Date inventoryBeginTime) {
        this.inventoryBeginTime = inventoryBeginTime;
    }

    public Date getInventoryEndTime() {
        return inventoryEndTime;
    }

    public void setInventoryEndTime(Date inventoryEndTime) {
        this.inventoryEndTime = inventoryEndTime;
    }
}
