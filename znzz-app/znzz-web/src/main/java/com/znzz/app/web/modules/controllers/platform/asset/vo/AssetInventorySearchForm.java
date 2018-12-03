package com.znzz.app.web.modules.controllers.platform.asset.vo;

import java.io.Serializable;

public class AssetInventorySearchForm implements Serializable {

    private String assetCode;
    private String assetName;
    private String assetModel;
    private String serialNumber;
    private String inventoryChecker;
    private String inventorySite;
    private String inventoryBeginTime;
    private String inventoryEndTime;

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

    public String getInventoryChecker() {
        return inventoryChecker;
    }

    public void setInventoryChecker(String inventoryChecker) {
        this.inventoryChecker = inventoryChecker;
    }

    public String getInventorySite() {
        return inventorySite;
    }

    public void setInventorySite(String inventorySite) {
        this.inventorySite = inventorySite;
    }

    public String getInventoryBeginTime() {
        return inventoryBeginTime;
    }

    public void setInventoryBeginTime(String inventoryBeginTime) {
        this.inventoryBeginTime = inventoryBeginTime;
    }

    public String getInventoryEndTime() {
        return inventoryEndTime;
    }

    public void setInventoryEndTime(String inventoryEndTime) {
        this.inventoryEndTime = inventoryEndTime;
    }
}
