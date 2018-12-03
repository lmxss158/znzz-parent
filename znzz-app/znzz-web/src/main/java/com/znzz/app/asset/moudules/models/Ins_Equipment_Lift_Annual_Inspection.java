package com.znzz.app.asset.moudules.models;

import com.znzz.framework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;
import java.io.Serializable;
import java.util.Date;


@Table("Ins_Equipment_Lift_Annual_Inspection")
@Comment("电梯设备年检信息表")
public class Ins_Equipment_Lift_Annual_Inspection extends BaseModel implements Serializable {

    @Id
    @Column
    @Comment("id")
    @ColDefine(type = ColType.INT, width = 10)
    private Integer id;

    @Column
    @Comment("电梯资产编号")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String liftCode;

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
    @Comment("电梯号")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String liftNo;

    @Column
    @Comment("电梯位置")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String liftSite;

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

    public String getLiftCode() {
        return liftCode;
    }

    public void setLiftCode(String liftCode) {
        this.liftCode = liftCode;
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

    public String getLiftNo() {
        return liftNo;
    }

    public void setLiftNo(String liftNo) {
        this.liftNo = liftNo;
    }

    public String getLiftSite() {
        return liftSite;
    }

    public void setLiftSite(String liftSite) {
        this.liftSite = liftSite;
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
