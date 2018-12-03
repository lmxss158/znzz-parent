package com.znzz.app.web.modules.controllers.platform.asset.vo;

import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;

import java.io.Serializable;

public class AssetInventoryRecordImportForm implements Serializable, IExcelDataModel, IExcelModel  {
    private Integer id;
    private String assetCode;
    private String assetName;
    private String assetModel;
    private String serialNumber;
    private String useDepartment;
    private String chargePerson;
    private String assetOriginalSite;
    private String inventoryChecker;
    private String jobNumber;

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    private String inventoryDate;

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

    public String getInventoryChecker() {
        return inventoryChecker;
    }

    public void setInventoryChecker(String inventoryChecker) {
        this.inventoryChecker = inventoryChecker;
    }

    public String getInventoryDate() {
        return inventoryDate;
    }

    public void setInventoryDate(String inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    @Override
    public int getRowNum() {
        return 0;
    }

    @Override
    public void setRowNum(int rowNum) {

    }

    @Override
    public String getErrorMsg() {
        return null;
    }

    @Override
    public void setErrorMsg(String errorMsg) {

    }
}
