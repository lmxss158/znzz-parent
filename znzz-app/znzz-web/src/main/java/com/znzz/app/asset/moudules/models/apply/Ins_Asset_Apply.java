package com.znzz.app.asset.moudules.models.apply;

import com.znzz.framework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.Date;

@Table("ins_asset_apply")
public class Ins_Asset_Apply extends BaseModel implements Serializable {

    private static final long serialVersionUID = 8648658002841482908L;

    @Column
    @Name
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("申请编号")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String applyId;

    @Column
    @Comment("统一编号")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String assetCode;

    @Column
    @Comment("资产名称")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String assetName;

    @Column
    @Comment("资产型号")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String deviceVersion;

    @Column
    @Comment("出厂编号")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String serialNumber;

    @Column
    @Comment("规格")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String specifications;

    @Column
    @Comment("借用日期")
    @ColDefine(type = ColType.DATETIME)
    private Date lendDate;

    @Column
    @Comment("预约归还日期")
    @ColDefine(type = ColType.DATETIME)
    private Date returnDate;

    @Column
    @Comment("申请人")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String proposer;

    @Column
    @Comment("审批人")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String approver;

    @Column
    @Comment("剩余使用时间")
    @ColDefine(type = ColType.INT)
    private Integer deadline;

    @Column
    @Comment("联系方式")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String number;

    @Column
    @Comment("单位")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String lenderUnit;

    @Column
    @Comment("工号")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String entryNumber;

    @Column
    @Comment("申请状态")
    @ColDefine(type = ColType.INT)
    private Integer applyState;

    @Column
    @Comment("附件")
    @ColDefine(type = ColType.VARCHAR, width = 200)
    private String accessary;

    @Column
    @Comment("备注")
    @ColDefine(type = ColType.VARCHAR, width = 200)
    private String remark;

    @Column
    @Comment("生成时间")
    @ColDefine(type = ColType.DATETIME)
    private Date createTime;

    @Column
    @Comment("备用1")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String bak1;

    @Column
    @Comment("备用2")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String bak2;

    /**
     * 资产状态
     */
    private Integer assetState;

    /**
     * 离线日期
     */
    private Date powerOffTime;

    /**
     * 规格
     */
    private String ggName;

    private Integer manageStatus;

    private Integer repairState;

    private Integer scrapState;

    private String borrowDepart;

    private Integer isLend;

    public String getGgName() {
        return ggName;
    }

    public void setGgName(String ggName) {
        this.ggName = ggName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
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

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getProposer() {
        return proposer;
    }

    public void setProposer(String proposer) {
        this.proposer = proposer;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAccessary() {
        return accessary;
    }

    public void setAccessary(String accessary) {
        this.accessary = accessary;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBak1() {
        return bak1;
    }

    public void setBak1(String bak1) {
        this.bak1 = bak1;
    }

    public String getBak2() {
        return bak2;
    }

    public void setBak2(String bak2) {
        this.bak2 = bak2;
    }

    public Integer getAssetState() {
        return assetState;
    }

    public void setAssetState(Integer assetState) {
        this.assetState = assetState;
    }

    public String getLenderUnit() {
        return lenderUnit;
    }

    public void setLenderUnit(String lenderUnit) {
        this.lenderUnit = lenderUnit;
    }

    public Date getPowerOffTime() {
        return powerOffTime;
    }

    public void setPowerOffTime(Date powerOffTime) {
        this.powerOffTime = powerOffTime;
    }

    public Integer getManageStatus() {
        return manageStatus;
    }

    public void setManageStatus(Integer manageStatus) {
        this.manageStatus = manageStatus;
    }

    public Integer getRepairState() {
        return repairState;
    }

    public void setRepairState(Integer repairState) {
        this.repairState = repairState;
    }

    public Integer getScrapState() {
        return scrapState;
    }

    public void setScrapState(Integer scrapState) {
        this.scrapState = scrapState;
    }

    public String getBorrowDepart() {
        return borrowDepart;
    }

    public void setBorrowDepart(String borrowDepart) {
        this.borrowDepart = borrowDepart;
    }

    public Integer getIsLend() {
        return isLend;
    }

    public void setIsLend(Integer isLend) {
        this.isLend = isLend;
    }

    public Date getLendDate() {
        return lendDate;
    }

    public void setLendDate(Date lendDate) {
        this.lendDate = lendDate;
    }

    public String getEntryNumber() {
        return entryNumber;
    }

    public void setEntryNumber(String entryNumber) {
        this.entryNumber = entryNumber;
    }

    public Integer getApplyState() {
        return applyState;
    }

    public void setApplyState(Integer applyState) {
        this.applyState = applyState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
