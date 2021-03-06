/*QUARTZ_01*/
DROP TABLE IF EXISTS SYS_QRTZ_FIRED_TRIGGERS;
/*QUARTZ_02*/
DROP TABLE IF EXISTS SYS_QRTZ_PAUSED_TRIGGER_GRPS;
/*QUARTZ_03*/
DROP TABLE IF EXISTS SYS_QRTZ_SCHEDULER_STATE;
/*QUARTZ_04*/
DROP TABLE IF EXISTS SYS_QRTZ_LOCKS;
/*QUARTZ_05*/
DROP TABLE IF EXISTS SYS_QRTZ_SIMPLE_TRIGGERS;
/*QUARTZ_06*/
DROP TABLE IF EXISTS SYS_QRTZ_SIMPROP_TRIGGERS;
/*QUARTZ_07*/
DROP TABLE IF EXISTS SYS_QRTZ_CRON_TRIGGERS;
/*QUARTZ_08*/
DROP TABLE IF EXISTS SYS_QRTZ_BLOB_TRIGGERS;
/*QUARTZ_09*/
DROP TABLE IF EXISTS SYS_QRTZ_TRIGGERS;
/*QUARTZ_10*/
DROP TABLE IF EXISTS SYS_QRTZ_JOB_DETAILS;
/*QUARTZ_11*/
DROP TABLE IF EXISTS SYS_QRTZ_CALENDARS;
/*QUARTZ_12*/
CREATE TABLE SYS_QRTZ_JOB_DETAILS(SCHED_NAME VARCHAR(50) NOT NULL,JOB_NAME VARCHAR(100) NOT NULL,JOB_GROUP VARCHAR(100) NOT NULL,DESCRIPTION VARCHAR(250) NULL,JOB_CLASS_NAME VARCHAR(250) NOT NULL,IS_DURABLE VARCHAR(1) NOT NULL,IS_NONCONCURRENT VARCHAR(1) NOT NULL,IS_UPDATE_DATA VARCHAR(1) NOT NULL,REQUESTS_RECOVERY VARCHAR(1) NOT NULL,JOB_DATA BLOB NULL,PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP))ENGINE=InnoDB;
/*QUARTZ_13*/
CREATE TABLE SYS_QRTZ_TRIGGERS(SCHED_NAME VARCHAR(50) NOT NULL,TRIGGER_NAME VARCHAR(100) NOT NULL,TRIGGER_GROUP VARCHAR(100) NOT NULL,JOB_NAME VARCHAR(100) NOT NULL,JOB_GROUP VARCHAR(100) NOT NULL,DESCRIPTION VARCHAR(250) NULL,NEXT_FIRE_TIME BIGINT(13) NULL,PREV_FIRE_TIME BIGINT(13) NULL,PRIORITY INTEGER NULL,TRIGGER_STATE VARCHAR(16) NOT NULL,TRIGGER_TYPE VARCHAR(8) NOT NULL,START_TIME BIGINT(13) NOT NULL,END_TIME BIGINT(13) NULL,CALENDAR_NAME VARCHAR(100) NULL,MISFIRE_INSTR SMALLINT(2) NULL,JOB_DATA BLOB NULL,PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)REFERENCES SYS_QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP))ENGINE=InnoDB;
/*QUARTZ_14*/
CREATE TABLE SYS_QRTZ_SIMPLE_TRIGGERS(SCHED_NAME VARCHAR(50) NOT NULL,TRIGGER_NAME VARCHAR(100) NOT NULL,TRIGGER_GROUP VARCHAR(100) NOT NULL,REPEAT_COUNT BIGINT(7) NOT NULL,REPEAT_INTERVAL BIGINT(12) NOT NULL,TIMES_TRIGGERED BIGINT(10) NOT NULL,PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)REFERENCES SYS_QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))ENGINE=InnoDB;
/*QUARTZ_15*/
CREATE TABLE SYS_QRTZ_CRON_TRIGGERS(SCHED_NAME VARCHAR(50) NOT NULL,TRIGGER_NAME VARCHAR(100) NOT NULL,TRIGGER_GROUP VARCHAR(100) NOT NULL,CRON_EXPRESSION VARCHAR(50) NOT NULL,TIME_ZONE_ID VARCHAR(50),PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)REFERENCES SYS_QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))ENGINE=InnoDB;
/*QUARTZ_16*/
CREATE TABLE SYS_QRTZ_SIMPROP_TRIGGERS(SCHED_NAME VARCHAR(50) NOT NULL,TRIGGER_NAME VARCHAR(100) NOT NULL,TRIGGER_GROUP VARCHAR(100) NOT NULL,STR_PROP_1 VARCHAR(512) NULL,STR_PROP_2 VARCHAR(512) NULL,STR_PROP_3 VARCHAR(512) NULL,INT_PROP_1 INT NULL,INT_PROP_2 INT NULL,LONG_PROP_1 BIGINT NULL,LONG_PROP_2 BIGINT NULL,DEC_PROP_1 NUMERIC(13,4) NULL,DEC_PROP_2 NUMERIC(13,4) NULL,BOOL_PROP_1 VARCHAR(1) NULL,BOOL_PROP_2 VARCHAR(1) NULL,PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP), FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) REFERENCES SYS_QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))ENGINE=InnoDB;
/*QUARTZ_17*/
CREATE TABLE SYS_QRTZ_BLOB_TRIGGERS (SCHED_NAME VARCHAR(50) NOT NULL,TRIGGER_NAME VARCHAR(100) NOT NULL,TRIGGER_GROUP VARCHAR(100) NOT NULL,BLOB_DATA BLOB NULL,PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),INDEX (SCHED_NAME,TRIGGER_NAME, TRIGGER_GROUP),FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)REFERENCES SYS_QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))ENGINE=InnoDB;
/*QUARTZ_18*/
CREATE TABLE SYS_QRTZ_CALENDARS (SCHED_NAME VARCHAR(50) NOT NULL,CALENDAR_NAME VARCHAR(100) NOT NULL,CALENDAR BLOB NOT NULL,PRIMARY KEY (SCHED_NAME,CALENDAR_NAME))ENGINE=InnoDB;
/*QUARTZ_19*/
CREATE TABLE SYS_QRTZ_PAUSED_TRIGGER_GRPS (SCHED_NAME VARCHAR(50) NOT NULL,TRIGGER_GROUP VARCHAR(100) NOT NULL,PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP))ENGINE=InnoDB;
/*QUARTZ_20*/
CREATE TABLE SYS_QRTZ_FIRED_TRIGGERS (SCHED_NAME VARCHAR(50) NOT NULL,ENTRY_ID VARCHAR(95) NOT NULL,TRIGGER_NAME VARCHAR(100) NOT NULL,TRIGGER_GROUP VARCHAR(100) NOT NULL,INSTANCE_NAME VARCHAR(100) NOT NULL,FIRED_TIME BIGINT(13) NOT NULL,SCHED_TIME BIGINT(13) NOT NULL,PRIORITY INTEGER NOT NULL,STATE VARCHAR(16) NOT NULL,JOB_NAME VARCHAR(100) NULL,JOB_GROUP VARCHAR(100) NULL,IS_NONCONCURRENT VARCHAR(1) NULL,REQUESTS_RECOVERY VARCHAR(1) NULL,PRIMARY KEY (SCHED_NAME,ENTRY_ID))ENGINE=InnoDB;
/*QUARTZ_21*/
CREATE TABLE SYS_QRTZ_SCHEDULER_STATE (SCHED_NAME VARCHAR(50) NOT NULL,INSTANCE_NAME VARCHAR(100) NOT NULL,LAST_CHECKIN_TIME BIGINT(13) NOT NULL,CHECKIN_INTERVAL BIGINT(13) NOT NULL,PRIMARY KEY (SCHED_NAME,INSTANCE_NAME))ENGINE=InnoDB;
/*QUARTZ_22*/
CREATE TABLE SYS_QRTZ_LOCKS (SCHED_NAME VARCHAR(50) NOT NULL,LOCK_NAME VARCHAR(40) NOT NULL,PRIMARY KEY (SCHED_NAME,LOCK_NAME))ENGINE=InnoDB;
/*QUARTZ_23*/
CREATE INDEX IDX_SYS_QRTZ_J_REQ_RECOVERY ON SYS_QRTZ_JOB_DETAILS(SCHED_NAME,REQUESTS_RECOVERY);
/*QUARTZ_24*/
CREATE INDEX IDX_SYS_QRTZ_J_GRP ON SYS_QRTZ_JOB_DETAILS(SCHED_NAME,JOB_GROUP);
/*QUARTZ_25*/
CREATE INDEX IDX_SYS_QRTZ_T_J ON SYS_QRTZ_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);
/*QUARTZ_26*/
CREATE INDEX IDX_SYS_QRTZ_T_JG ON SYS_QRTZ_TRIGGERS(SCHED_NAME,JOB_GROUP);
/*QUARTZ_27*/
CREATE INDEX IDX_SYS_QRTZ_T_C ON SYS_QRTZ_TRIGGERS(SCHED_NAME,CALENDAR_NAME);
/*QUARTZ_28*/
CREATE INDEX IDX_SYS_QRTZ_T_G ON SYS_QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);
/*QUARTZ_29*/
CREATE INDEX IDX_SYS_QRTZ_T_STATE ON SYS_QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_STATE);
/*QUARTZ_30*/
CREATE INDEX IDX_SYS_QRTZ_T_N_STATE ON SYS_QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_STATE);
/*QUARTZ_31*/
CREATE INDEX IDX_SYS_QRTZ_T_N_G_STATE ON SYS_QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_GROUP,TRIGGER_STATE);
/*QUARTZ_32*/
CREATE INDEX IDX_SYS_QRTZ_T_NEXT_FIRE_TIME ON SYS_QRTZ_TRIGGERS(SCHED_NAME,NEXT_FIRE_TIME);
/*QUARTZ_33*/
CREATE INDEX IDX_SYS_QRTZ_T_NFT_ST ON SYS_QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_STATE,NEXT_FIRE_TIME);
/*QUARTZ_34*/
CREATE INDEX IDX_SYS_QRTZ_T_NFT_MISFIRE ON SYS_QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME);
/*QUARTZ_35*/
CREATE INDEX IDX_SYS_QRTZ_T_NFT_ST_MISFIRE ON SYS_QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_STATE);
/*QUARTZ_36*/
CREATE INDEX IDX_SYS_QRTZ_T_NFT_ST_MISFIRE_GRP ON SYS_QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_GROUP,TRIGGER_STATE);
/*QUARTZ_37*/
CREATE INDEX IDX_SYS_QRTZ_FT_TRIG_INST_NAME ON SYS_QRTZ_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME);
/*QUARTZ_38*/
CREATE INDEX IDX_SYS_QRTZ_FT_INST_JOB_REQ_RCVRY ON SYS_QRTZ_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME,REQUESTS_RECOVERY);
/*QUARTZ_39*/
CREATE INDEX IDX_SYS_QRTZ_FT_J_G ON SYS_QRTZ_FIRED_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);
/*QUARTZ_40*/
CREATE INDEX IDX_SYS_QRTZ_FT_JG ON SYS_QRTZ_FIRED_TRIGGERS(SCHED_NAME,JOB_GROUP);
/*QUARTZ_41*/
CREATE INDEX IDX_SYS_QRTZ_FT_T_G ON SYS_QRTZ_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP);
/*QUARTZ_42*/
CREATE INDEX IDX_SYS_QRTZ_FT_TG ON SYS_QRTZ_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);
/*QUARTZ_43*/
INSERT INTO `sys_task` VALUES ('1c5942993f4c49cfbe0f1b08ac95dd11', '周检模块更新过期字段', 'com.znzz.app.web.commons.quartz.job.InsExamineJob', '根据时间更新是否过期字段', '0 0 23 * * ?', '', null, null, '0', '15729ed8e7d54060b09cb1105b34e562', '1503970497', '0');
/*QUARTZ_44*/
INSERT INTO `sys_task` VALUES ('e3e72b15336746b881c945147f76b745', '月度报告', 'com.znzz.app.web.commons.quartz.job.InsMonthReportJob', '每月25日00:02:00执行任务', '0 2 0 25 1/1 ? *', '', null, null, '0', '15729ed8e7d54060b09cb1105b34e562', '1506569162', '0');
/*QUARTZ_45*/
INSERT INTO `sys_task` VALUES ('d69a9827b93a422d99297b102503bb4c', '请求scard获取所有设备状态', 'com.znzz.app.web.commons.quartz.job.InsDeviceJob', '一分钟执行一次', '0 */1 * * * ?', '', null, null, '1', '15729ed8e7d54060b09cb1105b34e562', '1507855191', '0');
/*QUARTZ_46*/
INSERT INTO `sys_task` VALUES ('1e7788a631d24b998b966dbbdb9d0305', '月度使用时长', 'com.znzz.app.web.commons.quartz.job.InsDeviceFacilityJob', '每天零点10分执行', '0 10 0 * * ?', '', null, null, '1', '15729ed8e7d54060b09cb1105b34e562', '1507855802', '0');
/*QUARTZ_47*/
INSERT INTO `sys_task` VALUES ('a11729b8f7d441f6852529a72db79694', '设备利用率', 'com.znzz.app.web.commons.quartz.job.InsUtilizationRateJob', '每天23点55分执行', '0 55 23 * * ?', '', null, null, '1', '15729ed8e7d54060b09cb1105b34e562', '1507855830', '0');
/*QUARTZ_49*/
INSERT INTO `sys_task` VALUES ('056c8493e04d4135a84e92a27ea88725', '24小时设备概括图定时', 'com.znzz.app.web.commons.quartz.job.InsDeviceFacilityHistoryJob', '每小时执行一次', '0 0 * * * ?', '', null, null, '1', '15729ed8e7d54060b09cb1105b34e562', '1507856089', '0');
/*QUARTZ_48*/
INSERT INTO `sys_task` VALUES ('3056a403a6d9440ab962e328644e8a63', '功率辐射曲线定时', 'com.znzz.app.web.commons.quartz.job.InsDevicePowerJob', '5分钟执行一次', '0 */5 * * * ?', '', null, null, '1', '15729ed8e7d54060b09cb1105b34e562', '1507856162', '0');
/*QUARTZ_50*/
INSERT INTO `sys_task` VALUES ('3852c23d70c042ffa87a470632e820a2', '设备每天使用时长任务', 'com.znzz.app.web.commons.quartz.job.InsDeviceFacilityDayJob', '每天统计昨日的使用时长', '0 0 0 * * ?', '', null, null, '1', '15729ed8e7d54060b09cb1105b34e562', '1507858307', '0');
/*QUARTZ_51*/
INSERT INTO `sys_task` VALUES ('818e55b0a2034e72812d5e90e1edd284', 'scada实时数据上传云网', 'com.znzz.app.web.commons.quartz.job.InsUpload2CloudJob', '每一分钟执行一次', '0 */1 * * * ?', '', null, null, '0', '15729ed8e7d54060b09cb1105b34e562', '1508485575', '0');
/*QUARTZ_52*/
INSERT INTO `sys_task` VALUES ('d6078d5a2757464ba6f8f2a8695a4cc9', 'scada设备状态数据上传云网', 'com.znzz.app.web.commons.quartz.job.InsUpload2CloudJob2', '每五分钟上传一次', '0 */5 * * * ?', '', null, null, '0', '15729ed8e7d54060b09cb1105b34e562', '1508486415', '0');
/*QUARTZ_53*/
INSERT INTO `sys_task` VALUES ('709582ff21f6449d9e0ae178a5268c74', '24小时设备使用趋势任务', 'com.znzz.app.web.commons.quartz.job.InsDeviceUseTrendJob', '设备24小时使用趋势任务-每小时整点执行', '0 0 0 * * ?', '', NULL, NULL, '0', 'a88c7a1b30e346c885850f2d70747281', '1510111587', '0');

