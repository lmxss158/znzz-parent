package com.znzz.app.asset.moudules.models;

import com.znzz.framework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;
import java.io.Serializable;
import java.util.Date;

@Table("Ins_Equipment_Container_Complete_Inspection")
@Comment("容器设备全检信息表")
public class Ins_Equipment_Container_Complete_Inspection extends BaseModel implements Serializable {

    @Id
    @Column
    @Comment("id")
    @ColDefine(type = ColType.INT, width = 10)
    private Integer id;

    @Column
    @Comment("容器设备统一编号")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String containerCode;

    @Column
    @Comment("检测号")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String checkNo;

    @Column
    @Comment("出厂编号")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String serialNumber;

    @Column
    @Comment("负责人")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String chargePerson;

    @Column
    @Comment("楼号")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String floorNo;

    @Column
    @Comment("房间号")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String roomNo;

    @Column
    @Comment("容器位置")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String containerSite;

    @Column
    @Comment("压力")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String pressure;

    @Column
    @Comment("介质")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String medium;

    @Column
    @Comment("罐体大小")
    @ColDefine(type = ColType.VARCHAR, width = 60)
    private String tankSize;

    @Column
    @Comment("联系电话")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String telephone;

    @Column
    @Comment("全检日期")
    @ColDefine(type = ColType.DATETIME)
    private Date completeInspectionDate;

    @Column
    @Comment("下次全检日期")
    @ColDefine(type = ColType.DATETIME)
    private Date nextCompleteInspectionDate;

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

    public String getContainerCode() {
        return containerCode;
    }

    public void setContainerCode(String containerCode) {
        this.containerCode = containerCode;
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

    public String getChargePerson() {
        return chargePerson;
    }

    public void setChargePerson(String chargePerson) {
        this.chargePerson = chargePerson;
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

    public String getContainerSite() {
        return containerSite;
    }

    public void setContainerSite(String containerSite) {
        this.containerSite = containerSite;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getTankSize() {
        return tankSize;
    }

    public void setTankSize(String tankSize) {
        this.tankSize = tankSize;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getCompleteInspectionDate() {
        return completeInspectionDate;
    }

    public void setCompleteInspectionDate(Date completeInspectionDate) {
        this.completeInspectionDate = completeInspectionDate;
    }

    public Date getNextCompleteInspectionDate() {
        return nextCompleteInspectionDate;
    }

    public void setNextCompleteInspectionDate(Date nextCompleteInspectionDate) {
        this.nextCompleteInspectionDate = nextCompleteInspectionDate;
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
