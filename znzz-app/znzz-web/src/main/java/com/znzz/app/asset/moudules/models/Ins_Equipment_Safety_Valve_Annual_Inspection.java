package com.znzz.app.asset.moudules.models;

import com.znzz.framework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;
import java.io.Serializable;
import java.util.Date;

@Table("Ins_Equipment_Safety_Valve_Annual_Inspection")
@Comment("安全阀设备年检信息表")
public class Ins_Equipment_Safety_Valve_Annual_Inspection extends BaseModel implements Serializable {
    @Id
    @Column
    @Comment("id")
    @ColDefine(type = ColType.INT, width = 10)
    private Integer id;

    @Column
    @Comment("安全阀设备统一编号")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String safetyValveCode;

    @Column
    @Comment("检测号")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String checkNo;

    @Column
    @Comment("出厂编号")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String serialNumber;

    @Column
    @Comment("楼号")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String floorNo;

    @Column
    @Comment("房间号")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String roomNo;

    @Column
    @Comment("安全阀位置")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String safetyValveSite;

    @Column
    @Comment("属性")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String attribute;

    @Column
    @Comment("介质")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String medium;
    @Column
    @Comment("口径")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String caliber;
    @Column
    @Comment("整定压力")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String setPressure;

    @Column
    @Comment("负责人")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String chargePerson;

    @Column
    @Comment("联系电话")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String telephone;

    @Column
    @Comment("年检日期")
    @ColDefine(type = ColType.DATETIME)
    private Date annualInspectionDate;

    @Column
    @Comment("下次年检日期")
    @ColDefine(type = ColType.DATETIME)
    private Date nextAnnualInspectionDate;

    @Column
    @Comment("提前提醒天数")
    @ColDefine(type = ColType.INT, width = 3)
    private Integer daysNotice;

    private String inspectionCycleBeginTime;
    private String inspectionCycleEndTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSafetyValveCode() {
        return safetyValveCode;
    }

    public void setSafetyValveCode(String safetyValveCode) {
        this.safetyValveCode = safetyValveCode;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(String floorNo) {
        this.floorNo = floorNo;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getSafetyValveSite() {
        return safetyValveSite;
    }

    public void setSafetyValveSite(String safetyValveSite) {
        this.safetyValveSite = safetyValveSite;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getCaliber() {
        return caliber;
    }

    public void setCaliber(String caliber) {
        this.caliber = caliber;
    }

    public String getSetPressure() {
        return setPressure;
    }

    public void setSetPressure(String setPressure) {
        this.setPressure = setPressure;
    }

    public String getChargePerson() {
        return chargePerson;
    }

    public void setChargePerson(String chargePerson) {
        this.chargePerson = chargePerson;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getAnnualInspectionDate() {
        return annualInspectionDate;
    }

    public void setAnnualInspectionDate(Date annualInspectionDate) {
        this.annualInspectionDate = annualInspectionDate;
    }

    public Date getNextAnnualInspectionDate() {
        return nextAnnualInspectionDate;
    }

    public void setNextAnnualInspectionDate(Date nextAnnualInspectionDate) {
        this.nextAnnualInspectionDate = nextAnnualInspectionDate;
    }

    public Integer getDaysNotice() {
        return daysNotice;
    }

    public void setDaysNotice(Integer daysNotice) {
        this.daysNotice = daysNotice;
    }

    public String getInspectionCycleBeginTime() {
        return inspectionCycleBeginTime;
    }

    public void setInspectionCycleBeginTime(String inspectionCycleBeginTime) {
        this.inspectionCycleBeginTime = inspectionCycleBeginTime;
    }

    public String getInspectionCycleEndTime() {
        return inspectionCycleEndTime;
    }

    public void setInspectionCycleEndTime(String inspectionCycleEndTime) {
        this.inspectionCycleEndTime = inspectionCycleEndTime;
    }
}
