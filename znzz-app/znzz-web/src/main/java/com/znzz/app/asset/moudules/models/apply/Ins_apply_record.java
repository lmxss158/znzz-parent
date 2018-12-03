package com.znzz.app.asset.moudules.models.apply;

import com.znzz.framework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.Date;

@Table("ins_apply_record")
public class Ins_apply_record extends BaseModel implements Serializable {

    private static final long serialVersionUID = 8074544641481669971L;

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
    @Comment("归还日期")
    @ColDefine(type = ColType.DATETIME)
    private Date returnDate;

    @Column
    @Comment("单位")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String lenderUnit;

    @Column
    @Comment("申请人")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String proposer;

    @Column
    @Comment("审批人")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String approver;

    @Column
    @Comment("工号")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String entryNumber;

    @Column
    @Comment("联系电话")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String number;

    @Column
    @Comment("操作人")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String operator;

    @Column
    @Comment("操作状态:未领取/提出申请，已领取/确认领取，已取消/取消申请，已归还/确认归还，已延期/延期申请")
    @ColDefine(type = ColType.INT)
    private Integer operateState;

    @Column
    @Comment("操作时间")
    @ColDefine(type = ColType.DATETIME)
    private Date operateTime;

    @Column
    @Comment("附件")
    @ColDefine(type = ColType.VARCHAR, width = 200)
    private String accessary;

    @Column
    @Comment("备注")
    @ColDefine(type = ColType.VARCHAR, width = 200)
    private String remark;

    @Column
    @Comment("备用1")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String bak1;

    @Column
    @Comment("备用2")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String bak2;

    private Date forbidDate;//禁用日期

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

    public String getLenderUnit() {
        return lenderUnit;
    }

    public void setLenderUnit(String lenderUnit) {
        this.lenderUnit = lenderUnit;
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

    public String getEntryNumber() {
        return entryNumber;
    }

    public void setEntryNumber(String entryNumber) {
        this.entryNumber = entryNumber;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getOperateState() {
        return operateState;
    }

    public void setOperateState(Integer operateState) {
        this.operateState = operateState;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
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

    public Date getForbidDate() {
        return forbidDate;
    }

    public void setForbidDate(Date forbidDate) {
        this.forbidDate = forbidDate;
    }
}
