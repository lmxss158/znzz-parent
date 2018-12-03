package com.znzz.app.web.commons.util;

import com.znzz.app.asset.moudules.models.Ins_Assets;
import com.znzz.app.asset.moudules.models.apply.Ins_Asset_Apply;
import com.znzz.app.asset.moudules.models.apply.Ins_apply_record;
import com.znzz.app.asset.moudules.services.apply.ApplyAssetService;
import com.znzz.app.instrument.modules.models.Ins_MessageInfo;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.app.web.modules.controllers.platform.instruments.formBean.MessageInfo;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import com.znzz.framework.util.StringUtil;
import org.nutz.dao.*;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ApplyUtils {

    private static final Log LOG = Logs.get();

    /**
     * 设置月历回显
     *
     * @param req
     * @param sdf
     * @param user
     * @param list
     * @param myApps
     * @param otherApps
     * @throws Exception
     */
    public static void forDateShow(HttpServletRequest req, SimpleDateFormat sdf, Sys_user user, List<Ins_Asset_Apply> list,
                                   StringBuilder myApps, StringBuilder otherApps, String id, Ins_Assets asset) throws Exception {
        // 资产的禁用情况
        Date forbidDate = asset.getForbidDate();

        if (list != null && !list.isEmpty()) {
            // 记录逾期未还
            StringBuilder delayApps = new StringBuilder();
            for (Ins_Asset_Apply apply : list) {
                // 已归还、已取消的申请不设置占用回显
                if ((apply.getApplyState() != 3) && (apply.getApplyState() != 4)) {
                    if (user.getId().equals(apply.getProposer())) {
                        myApps.append(getAllBetween(sdf.format(apply.getLendDate()), sdf.format(apply.getReturnDate())));
                    } else {
                        otherApps.append(getAllBetween(sdf.format(apply.getLendDate()), sdf.format(apply.getReturnDate())));
                    }
                    // 逾期未还的(只考虑已领取、已延期的，不考虑未领取、已归还、已取消)
                    if (apply.getApplyState() == 2 || apply.getApplyState() == 1) {
                        delayApps.append(getAllBetween(sdf.format(addNDays(apply.getReturnDate(), 1)), sdf.format(new Date())));
                    }

                    Date applyLendDate = apply.getLendDate();
                    Date appleyReturnDate = apply.getReturnDate();
                    //如果申请状态是已领取（1）或是已延期（2）的时候，判断禁止预约日期是否大于借用日期。目的是在日历显示时，借用未还的申请要正常表示。
                    if ((forbidDate != null) && (applyLendDate != null) && (appleyReturnDate != null) && ((apply.getApplyState() == 1) || (apply.getApplyState() == 2))) {
                        if ((forbidDate.getTime() >= applyLendDate.getTime()) && (forbidDate.getTime() <= appleyReturnDate.getTime())) {//如果禁止预约日期大于借用日期小于归还日期，即禁止预约时设备已借出还未归还
                            //设置禁止预约日期为申请归还日期的后一天
                            Calendar calendar = Calendar.getInstance();
                            calendar.clear();
                            calendar.setTime(appleyReturnDate);
                            calendar.add(Calendar.DATE, 1);
                            forbidDate = calendar.getTime();
                        }
                    }
                }
            }

            req.setAttribute("myApply", myApps);
            req.setAttribute("otherApply", otherApps);
            req.setAttribute("delayApply", delayApps);
        } else {
            req.setAttribute("myApply", 0);
            req.setAttribute("otherApply", 0);
            req.setAttribute("delayApply", 0);
        }
        // 资产的禁用情况
//        Date forbidDate = asset.getForbidDate();
        if (forbidDate != null) {
            // 禁用日期至年末
            String forbids = getAllBetween(sdf.format(forbidDate), sdf.format(getYearLast()));
            req.setAttribute("forbid", forbids);
        } else {
            req.setAttribute("forbid", 0);
        }
    }

    /**
     * 获取某年最后一天日期
     *
     * @return Date
     */
    public static Date getYearLast() {
        Calendar calendar = Calendar.getInstance();
        // 本年
        int currentYear = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, currentYear);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();

        return currYearLast;
    }

    /**
     * 获取两个时间之间的所有日期
     *
     * @param start
     * @param end
     * @return
     * @throws Exception
     */
    public static String getAllBetween(String start, String end) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(start));
        StringBuilder sb = new StringBuilder();
        for (long d = cal.getTimeInMillis(); d <= sdf.parse(end).getTime(); d = get_D_Plaus_1(cal)) {
            //System.out.println(sdf.format(d));
            sb.append(sdf.format(d));
            sb.append(",");
        }
        return sb.toString();
    }

    // 时间处理
    public static long get_D_Plaus_1(Calendar c) {
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
        return c.getTimeInMillis();
    }

    /**
     * 记录履历
     */
    public static void doRecord(Ins_apply_record record, Ins_Asset_Apply apply) {
        record.setApplyId(apply.getApplyId());
        record.setAssetCode(apply.getAssetCode());
        record.setAssetName(apply.getAssetName());
        record.setDeviceVersion(apply.getDeviceVersion());
        record.setSerialNumber(apply.getSerialNumber());
        record.setSpecifications(apply.getSpecifications());
        record.setLendDate(apply.getLendDate());
        record.setReturnDate(apply.getReturnDate());
        record.setLenderUnit(apply.getLenderUnit());
        record.setProposer(apply.getProposer());
        record.setApprover(apply.getApprover());
        record.setEntryNumber(apply.getEntryNumber());
        record.setNumber(apply.getNumber());
        record.setOperator(StringUtil.getUid());
        record.setOperateTime(new Date());
    }

    /**
     * date2比date1多的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) {
            //同一年
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    //闰年
                    timeDistance += 366;
                } else {
                    //不是闰年
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2 - day1);
        } else {
            //不同年
            //System.out.println("判断day2 - day1 : " + (day2 - day1));
            return day2 - day1;
        }
    }

    /**
     * 某天 + N天
     *
     * @param date
     * @param n
     * @return
     */
    public static Date addNDays(Date date, int n) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, n);
        return c.getTime();
    }

    /**
     * 列排序
     *
     * @param or
     */
    public static void orderByRow(DataTableOrder or, List<DataTableColumn> columns, Cnd cnd) {
        DataTableColumn col = columns.get(or.getColumn());
        String orderRow = col.getData();
        // 单列排序处理
        String orderStr = "";
        String append = "";
        String append2 = "";
        if ("applyid".equals(orderRow)) {
            orderStr = "a.applyId";
        } else if ("assetname".equals(orderRow)) {
            orderStr = "CONVERT( a.assetName USING gbk)";
        } else if ("deviceversion".equals(orderRow)) {
            orderStr = "a.deviceVersion";
        } else if ("serialnumber".equals(orderRow)) {
            orderStr = "a.serialNumber";
        } else if ("lenddate".equals(orderRow)) {
            orderStr = "a.lendDate";
        } else if ("returndate".equals(orderRow)) {
            orderStr = "a.returnDate";
        } else if ("deadline".equals(orderRow)) {
            orderStr = "CASE WHEN a.deadline IS NULL THEN 1 ELSE 0 END, a.deadline";
            append = "a.applyId";
        } else if ("approver".equals(orderRow)) {
            orderStr = "CONVERT( a.approver USING gbk)";
        } else if ("number".equals(orderRow)) {
            orderStr = "a.number";
        } else if ("applystate".equals(orderRow)) {
            orderStr = "a.applyState";
            append2 = "a.lendDate";
        } else if ("lenderunit".equals(orderRow)) {
            orderStr = "a.lenderUnit";
        } else if ("assetstate".equals(orderRow)) {
            orderStr = "assetState";
        } else if ("proposer".equals(orderRow)) {
            orderStr = "CONVERT( a.proposer USING gbk)";
        } else {
            // Nothing..
        }
        cnd.orderBy(Sqls.escapeSqlFieldValue(orderStr).toString(), or.getDir());
        if (!"".equals(append)) {
            cnd.desc(append);
        } else if (!"".equals(append2)) {
            cnd.asc(append2);
        }
    }

    /**
     * 取消禁用点往后的所有预约申请
     *
     * @param assetCode
     * @param forbidDate
     */
    public void cancelAccordForbidHandAsset(String assetCode, Date forbidDate, Dao dao, String text) {
        try {
            // 往后的所有申请
            List<Ins_Asset_Apply> list = dao.query(Ins_Asset_Apply.class, Cnd.where("assetCode", "=", assetCode).and("lendDate", ">=", forbidDate));
            String userId = "";
            if (list != null && !list.isEmpty()) {
                for (Ins_Asset_Apply apply : list) {
                    // 改状态
                    dao.update(Ins_Asset_Apply.class, Chain.make("applyState", 3), Cnd.where("applyId", "=", apply.getApplyId()));// 3:已取消
                    if (apply.getApplyState() == 0 ){//未领取
                        // 记履历
                        Ins_apply_record record = new Ins_apply_record();
                        doRecord(record, apply);
                        record.setOperateState(3);//取消申请
                        record.setRemark(text);
                        record.setOperator("10000");// 操作人都是系统
                        record.setId(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32));
                        dao.fastInsert(record);
                        insertMessageInfo(assetCode,dao,text,userId);
                    }

                }
            }
        } catch (Exception e) {
            LOG.error("Wrong when cancel of forbid..", e);
        }
    }

    public void cancelAccordForbid(String assetCode, Date forbidDate, Dao dao, String text) {
        try {
            // 往后的所有申请
            List<Ins_Asset_Apply> list = dao.query(Ins_Asset_Apply.class, Cnd.where("assetCode", "=", assetCode).and("lendDate", ">=", forbidDate));

            if (list != null && !list.isEmpty()) {
                for (Ins_Asset_Apply apply : list) {
                    // 改状态
                    dao.update(Ins_Asset_Apply.class, Chain.make("applyState", 3), Cnd.where("applyId", "=", apply.getApplyId()));// 3:已取消
                        // 记履历
                        Ins_apply_record record = new Ins_apply_record();
                        doRecord(record, apply);
                        record.setOperateState(3);//取消申请
                        record.setRemark(text);
                        record.setOperator("10000");// 操作人都是系统
                        record.setId(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32));
                        dao.fastInsert(record);
                }
            }
        } catch (Exception e) {
            LOG.error("Wrong when cancel of forbid..", e);
        }
    }

    /**
     * yyyy-MM-dd
     *
     * @return
     */
    public static Date pureDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    /**
     * 撤销了延期申请的情况（需要将原来的申请状态改回去1）
     *
     * @param vo
     * @param applyService
     */
    public static void backToState(Ins_apply_record vo, ApplyAssetService applyService) {
        if (vo.getApplyId().length() > 15) {
            String oldAapplyId = vo.getApplyId().substring(0, 15);
            Ins_Asset_Apply apply = applyService.fetch(Cnd.where("applyId", "=", oldAapplyId));
            //原来的申请已延期才能表示当前申请是延期申请
            if (apply.getApplyState() == 2) {
                // 修改回去
                applyService.update(Chain.make("applyState", 1), Cnd.where("applyId", "=", oldAapplyId));
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(differentDays(new Date(), new Date()));
    }


    /**
     * 取消禁用点往后的所有预约申请(针对大帐)
     * @param assetCode
     * @param dao
     * @param text
     */
    public void insertMessageInfo(String assetCode, Dao dao, String text,String userId) {
        try {

            //插入消息管理
            Ins_MessageInfo messageInfo = new Ins_MessageInfo();
            messageInfo.setMessageContext("统一编号为："+ assetCode + "该设备已" + text);  //内容
            messageInfo.setSendId("10000");     //系统
            messageInfo.setReceiveId(userId);
            messageInfo.setStatus(0);           //未读
            messageInfo.setSend_time(new Date());
            dao.insert(messageInfo.getClass(), Chain.from(messageInfo, FieldMatcher.make(null, null, true)));
        } catch (Exception e) {
            LOG.error("Wrong when cancel of forbid..", e);
        }
    }

}
