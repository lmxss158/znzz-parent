/*
Navicat MySQL Data Transfer

Source Server         : newConn
Source Server Version : 50718
Source Host           : localhost:3306
Source Database       : nutzwk

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2017-08-14 08:57:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for cms_article
-- ----------------------------
DROP TABLE IF EXISTS `cms_article`;
CREATE TABLE `cms_article` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `shopid` varchar(32) DEFAULT NULL COMMENT '预留商城ID',
  `title` varchar(120) DEFAULT NULL COMMENT '文章标题',
  `info` varchar(500) DEFAULT NULL COMMENT '文章简介',
  `author` varchar(50) DEFAULT NULL COMMENT '文章作者',
  `picurl` varchar(255) DEFAULT NULL COMMENT '标题图',
  `content` text COMMENT '文章内容',
  `disabled` tinyint(1) DEFAULT NULL COMMENT '是否禁用',
  `publishAt` int(32) DEFAULT NULL COMMENT '发布时间',
  `location` int(32) DEFAULT NULL COMMENT '排序字段',
  `channelId` varchar(32) DEFAULT NULL,
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cms_article
-- ----------------------------

-- ----------------------------
-- Table structure for cms_channel
-- ----------------------------
DROP TABLE IF EXISTS `cms_channel`;
CREATE TABLE `cms_channel` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `shopid` varchar(32) DEFAULT NULL COMMENT '预留商城ID',
  `parentId` varchar(32) DEFAULT NULL COMMENT '父级ID',
  `path` varchar(100) DEFAULT NULL COMMENT '树路径',
  `name` varchar(100) DEFAULT NULL COMMENT '栏目名称',
  `type` varchar(20) DEFAULT NULL COMMENT '栏目类型',
  `url` varchar(255) DEFAULT NULL COMMENT '链接地址',
  `target` varchar(20) DEFAULT NULL COMMENT '打开方式',
  `isShow` tinyint(1) DEFAULT NULL COMMENT '是否显示',
  `disabled` tinyint(1) DEFAULT NULL COMMENT '是否禁用',
  `location` int(32) DEFAULT NULL COMMENT '排序字段',
  `hasChildren` tinyint(1) DEFAULT NULL COMMENT '有子节点',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cms_channel
-- ----------------------------

-- ----------------------------
-- Table structure for cms_class_link
-- ----------------------------
DROP TABLE IF EXISTS `cms_class_link`;
CREATE TABLE `cms_class_link` (
  `classId` varchar(32) DEFAULT NULL,
  `linkId` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cms_class_link
-- ----------------------------

-- ----------------------------
-- Table structure for cms_link
-- ----------------------------
DROP TABLE IF EXISTS `cms_link`;
CREATE TABLE `cms_link` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `name` varchar(120) DEFAULT NULL COMMENT '链接名称',
  `type` varchar(20) DEFAULT NULL COMMENT '链接类型',
  `picurl` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `url` varchar(255) DEFAULT NULL COMMENT '链接地址',
  `target` varchar(20) DEFAULT NULL COMMENT '打开方式',
  `classId` varchar(32) DEFAULT NULL,
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cms_link
-- ----------------------------

-- ----------------------------
-- Table structure for cms_link_class
-- ----------------------------
DROP TABLE IF EXISTS `cms_link_class`;
CREATE TABLE `cms_link_class` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `name` varchar(120) DEFAULT NULL COMMENT '分类名称',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cms_link_class
-- ----------------------------

-- ----------------------------
-- Table structure for cms_site
-- ----------------------------
DROP TABLE IF EXISTS `cms_site`;
CREATE TABLE `cms_site` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `shopid` varchar(32) DEFAULT NULL COMMENT '预留商城ID',
  `site_name` varchar(120) DEFAULT NULL COMMENT '名称',
  `site_domain` varchar(120) DEFAULT NULL COMMENT '域名',
  `site_icp` varchar(120) DEFAULT NULL COMMENT 'ICP',
  `site_logo` varchar(255) DEFAULT NULL COMMENT 'LOGO',
  `site_wap_logo` varchar(255) DEFAULT NULL COMMENT 'WAPLOGO',
  `site_qq` varchar(20) DEFAULT NULL COMMENT '客服QQ',
  `site_email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `site_tel` varchar(20) DEFAULT NULL COMMENT '电话',
  `weibo_name` varchar(50) DEFAULT NULL COMMENT '微博',
  `weibo_url` varchar(255) DEFAULT NULL COMMENT '微博地址',
  `weibo_qrcode` varchar(255) DEFAULT NULL COMMENT '微博二维码',
  `wechat_name` varchar(50) DEFAULT NULL COMMENT '微信名称',
  `wechat_id` varchar(50) DEFAULT NULL COMMENT '微信ID',
  `wechat_qrcode` varchar(255) DEFAULT NULL COMMENT '微信二维码',
  `seo_keywords` varchar(255) DEFAULT NULL COMMENT '关键词',
  `seo_description` varchar(255) DEFAULT NULL COMMENT '描述',
  `footer_content` text COMMENT '底部版权',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cms_site
-- ----------------------------

-- ----------------------------
-- Table structure for ins_assets_info
-- ----------------------------
DROP TABLE IF EXISTS `ins_assets_info`;
CREATE TABLE `ins_assets_info` (
  `id` bigint(40) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `assetCode` varchar(60) DEFAULT NULL COMMENT '资产统一编号',
  `assetName` varchar(60) DEFAULT NULL COMMENT '资产名称',
  `assetType` tinyint(4) DEFAULT NULL COMMENT '资产种类:（0全部1设备2仪器）',
  `assetRule` varchar(60) DEFAULT NULL COMMENT '资产规格',
  `factoryTime` int(32) DEFAULT NULL COMMENT '出厂时间',
  `enableTime` int(32) DEFAULT NULL COMMENT '启用时间',
  `serialNumber` varchar(60) DEFAULT NULL COMMENT '出厂编号',
  `isLend` tinyint(4) DEFAULT NULL COMMENT '是否借出:0是1否',
  `borrowDepart` varchar(60) DEFAULT NULL COMMENT '使用单位',
  `chargePerson` varchar(60) DEFAULT NULL COMMENT '责任人',
  `lendDate` int(32) DEFAULT NULL COMMENT '借用日期',
  `returnDate` int(32) DEFAULT NULL COMMENT '归还日期',
  `checker` varchar(60) DEFAULT NULL COMMENT '验收人',
  `imagePath` varchar(60) DEFAULT NULL COMMENT '图片路径',
  `manageStatus` tinyint(4) DEFAULT NULL COMMENT '管理状态:0在用/1封存/2闲置/3禁用',
  `manageLevel` tinyint(4) DEFAULT NULL COMMENT '管理级别:0厂(所)管/1院管/2总公司部管',
  `completeStatus` tinyint(4) DEFAULT NULL COMMENT '完好状态:0完好/1不完好',
  `assetCategory` tinyint(4) DEFAULT NULL COMMENT '资产类别:0固定资产/1低值/2在建/3附件/4零值',
  `instrumentCategory` tinyint(4) DEFAULT NULL COMMENT '仪器类别:普通/精大贵希/进口普通/进口精大贵希',
  `fundSources` tinyint(4) DEFAULT NULL COMMENT '基金来源:0拨款/1自购',
  `projectName` varchar(60) DEFAULT NULL COMMENT '项目名称',
  `contractCode` varchar(60) DEFAULT NULL COMMENT '合同号',
  `batch_code` varchar(60) DEFAULT NULL COMMENT '批件序号',
  `power` varchar(60) DEFAULT NULL COMMENT '功率',
  `warrantyPeriod` varchar(60) DEFAULT NULL COMMENT '保修期',
  `remark` varchar(60) DEFAULT NULL COMMENT '备注',
  `scrapState` tinyint(4) DEFAULT NULL COMMENT '报废状态',
  `repairState` tinyint(4) DEFAULT NULL COMMENT '维修状态',
  `locationInfo` varchar(60) DEFAULT NULL COMMENT '位置信息',
  `macHour` varchar(60) DEFAULT NULL COMMENT '台时信息',
  `topicNo` varchar(60) DEFAULT NULL COMMENT '课题号',
  `supplier` varchar(60) DEFAULT NULL COMMENT '供货单位',
  `unpackingDate` varchar(60) DEFAULT NULL COMMENT '开箱日期',
  `depreciationYear` varchar(60) DEFAULT NULL COMMENT '折旧年限',
  `property` varchar(60) DEFAULT NULL COMMENT '属性',
  `technicalIndex` varchar(60) DEFAULT NULL COMMENT '技术指标',
  `annexPath` varchar(60) DEFAULT NULL COMMENT '附件',
  `is_military` tinyint(4) DEFAULT NULL COMMENT '军工关键设备:0是/1否',
  `use_type` tinyint(4) DEFAULT NULL COMMENT '用途:0科研类、1生产类、2测量类 ',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资产信息表';

-- ----------------------------
-- Records of ins_assets_info
-- ----------------------------

-- ----------------------------
-- Table structure for ins_collect
-- ----------------------------
DROP TABLE IF EXISTS `ins_collect`;
CREATE TABLE `ins_collect` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `collectCode` varchar(60) DEFAULT NULL COMMENT '采集器编号',
  `collectName` varchar(60) DEFAULT NULL COMMENT '采集器名称',
  `assetCode` varchar(60) DEFAULT NULL COMMENT '统一编号',
  `createTime` int(32) DEFAULT NULL COMMENT '录入时间',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='采集器表';

-- ----------------------------
-- Records of ins_collect
-- ----------------------------

-- ----------------------------
-- Table structure for ins_gateway
-- ----------------------------
DROP TABLE IF EXISTS `ins_gateway`;
CREATE TABLE `ins_gateway` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `gatewayCode` varchar(60) DEFAULT NULL COMMENT '网关编号',
  `gatewayName` varchar(60) DEFAULT NULL COMMENT '网关名称',
  `gatewayLocation` varchar(60) DEFAULT NULL COMMENT '位置',
  `ip` varchar(60) DEFAULT NULL COMMENT 'ip',
  `remark` varchar(60) DEFAULT NULL COMMENT '描述',
  `createTime` datetime DEFAULT NULL COMMENT '录入时间',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='网关表-A卡';

-- ----------------------------
-- Records of ins_gateway
-- ----------------------------
INSERT INTO `ins_gateway` VALUES ('2', 'FD001', '思科路由器', '北京海淀', '127.0.0.2', '123213', '2017-08-10 00:00:00', 'ba2129e64efe4c18963b95c190e38ad2', '1502594270', '0');
INSERT INTO `ins_gateway` VALUES ('6', 'FD001', '思科路由器', '北京海淀', '127.0.0.2', '数据', '2017-08-10 00:00:00', 'ba2129e64efe4c18963b95c190e38ad2', '1502596868', '0');
INSERT INTO `ins_gateway` VALUES ('7', 'FD001', '思科路由器', '北京通州', '127.0.0.2', 'qew', '2017-08-10 00:00:00', 'ba2129e64efe4c18963b95c190e38ad2', '1502597447', '0');
INSERT INTO `ins_gateway` VALUES ('8', 'FD001', '思科路由器', '北京海淀', '127.0.0.2', 'werer', '2017-08-10 05:00:00', 'ba2129e64efe4c18963b95c190e38ad2', '1502597452', '0');
INSERT INTO `ins_gateway` VALUES ('9', 'FD003', '思科路由器', '北京海淀', '127.0.0.2', '这些', '2017-08-13 11:59:00', 'ba2129e64efe4c18963b95c190e38ad2', '1502596877', '0');
INSERT INTO `ins_gateway` VALUES ('10', 'FD001', '思科路由器', '北京海淀', '127.0.0.2', '按时', '2017-08-10 00:00:00', 'ba2129e64efe4c18963b95c190e38ad2', '1502596789', '0');
INSERT INTO `ins_gateway` VALUES ('11', 'FD001', '思科路由器', '北京海淀', '127.0.0.2', '水电费', '2017-08-10 00:00:00', 'ba2129e64efe4c18963b95c190e38ad2', '1502596803', '0');
INSERT INTO `ins_gateway` VALUES ('12', 'FD001', '思科路由器', '北京海淀', '127.0.0.2', '切勿', '2017-08-10 05:00:00', 'ba2129e64efe4c18963b95c190e38ad2', '1502596817', '0');
INSERT INTO `ins_gateway` VALUES ('13', 'FD001', '思科路由器', '北京海淀', '127.0.0.2', 'wrerw', '2017-08-10 05:00:00', 'ba2129e64efe4c18963b95c190e38ad2', '1502597458', '0');

-- ----------------------------
-- Table structure for sys_api
-- ----------------------------
DROP TABLE IF EXISTS `sys_api`;
CREATE TABLE `sys_api` (
  `id` varchar(32) NOT NULL,
  `appName` varchar(20) DEFAULT NULL COMMENT 'appName',
  `appId` varchar(255) DEFAULT NULL COMMENT 'appId',
  `appSecret` varchar(255) DEFAULT NULL COMMENT 'appSecret',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_api
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `configKey` varchar(100) NOT NULL,
  `configValue` varchar(100) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`configKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES ('AppDomain', '127.0.0.1', '系统域名', '', '1502590811', '0');
INSERT INTO `sys_config` VALUES ('AppName', '25所仪器设备管理系统', '系统名称', '', '1502590810', '0');
INSERT INTO `sys_config` VALUES ('AppShrotName', 'znly', '系统短名称', '', '1502590811', '0');
INSERT INTO `sys_config` VALUES ('AppUploadPath', '/upload', '文件上传文件夹', '', '1502590811', '0');

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `parentId` varchar(32) DEFAULT NULL COMMENT '父级ID',
  `path` varchar(100) DEFAULT NULL COMMENT '树路径',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `code` varchar(20) DEFAULT NULL COMMENT '机构编码',
  `location` int(32) DEFAULT NULL COMMENT '排序字段',
  `hasChildren` tinyint(1) DEFAULT NULL COMMENT '有子节点',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `INDEX_SYS_DICT_PATH` (`path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL COMMENT '创建昵称',
  `type` varchar(20) DEFAULT NULL COMMENT '日志类型',
  `tag` varchar(50) DEFAULT NULL COMMENT '日志标识',
  `src` varchar(255) DEFAULT NULL COMMENT '执行类',
  `ip` varchar(255) DEFAULT NULL COMMENT '来源IP',
  `msg` text COMMENT '日志内容',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES ('1', '超级管理员', 'info', '用户登陆', 'com.znly.app.web.modules.controllers.platform.sys.SysLoginController#doLogin', '0:0:0:0:0:0:0:1', '成功登录系统！', 'ba2129e64efe4c18963b95c190e38ad2', '1502591062', null);
INSERT INTO `sys_log` VALUES ('2', '超级管理员', 'info', '用户登陆', 'com.znzz.app.web.modules.controllers.platform.sys.SysLoginController#doLogin', '0:0:0:0:0:0:0:1', '成功登录系统！', 'ba2129e64efe4c18963b95c190e38ad2', '1502592904', null);
INSERT INTO `sys_log` VALUES ('3', '超级管理员', 'aop.after', '新建菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysMenuController#addDo', '0:0:0:0:0:0:0:1', '菜单名称:监控管理', 'ba2129e64efe4c18963b95c190e38ad2', '1502593155', null);
INSERT INTO `sys_log` VALUES ('4', '超级管理员', 'aop.after', '新建菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysMenuController#addDo', '0:0:0:0:0:0:0:1', '菜单名称:采集器管理', 'ba2129e64efe4c18963b95c190e38ad2', '1502593192', null);
INSERT INTO `sys_log` VALUES ('5', '超级管理员', 'aop.after', '新建菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysMenuController#addDo', '0:0:0:0:0:0:0:1', '菜单名称:网关管理', 'ba2129e64efe4c18963b95c190e38ad2', '1502593223', null);
INSERT INTO `sys_log` VALUES ('6', '超级管理员', 'aop.after', '修改角色菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysRoleController#editMenuDo', '0:0:0:0:0:0:0:1', '角色名称:系统管理员', 'ba2129e64efe4c18963b95c190e38ad2', '1502593278', null);
INSERT INTO `sys_log` VALUES ('7', '超级管理员', 'info', '用户登出', 'com.znzz.app.web.modules.controllers.platform.sys.SysLoginController#logout', '0:0:0:0:0:0:0:1', '成功退出系统！', 'ba2129e64efe4c18963b95c190e38ad2', '1502593282', null);
INSERT INTO `sys_log` VALUES ('8', '超级管理员', 'info', '用户登陆', 'com.znzz.app.web.modules.controllers.platform.sys.SysLoginController#doLogin', '0:0:0:0:0:0:0:1', '成功登录系统！', 'ba2129e64efe4c18963b95c190e38ad2', '1502593288', null);
INSERT INTO `sys_log` VALUES ('9', '超级管理员', 'info', '用户登陆', 'com.znzz.app.web.modules.controllers.platform.sys.SysLoginController#doLogin', '0:0:0:0:0:0:0:1', '成功登录系统！', 'ba2129e64efe4c18963b95c190e38ad2', '1502593369', null);
INSERT INTO `sys_log` VALUES ('10', '超级管理员', 'aop.after', '修改角色菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysRoleController#editMenuDo', '0:0:0:0:0:0:0:1', '角色名称:系统管理员', 'ba2129e64efe4c18963b95c190e38ad2', '1502593422', null);
INSERT INTO `sys_log` VALUES ('11', '超级管理员', 'aop.after', '新建菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysMenuController#addDo', '0:0:0:0:0:0:0:1', '菜单名称:采集器管理', 'ba2129e64efe4c18963b95c190e38ad2', '1502593466', null);
INSERT INTO `sys_log` VALUES ('12', '超级管理员', 'aop.after', '删除菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysMenuController#delete', '0:0:0:0:0:0:0:1', '菜单名称:采集器管理', 'ba2129e64efe4c18963b95c190e38ad2', '1502593494', null);
INSERT INTO `sys_log` VALUES ('13', '超级管理员', 'aop.after', '删除菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysMenuController#delete', '0:0:0:0:0:0:0:1', '菜单名称:网关管理', 'ba2129e64efe4c18963b95c190e38ad2', '1502593596', null);
INSERT INTO `sys_log` VALUES ('14', '超级管理员', 'aop.after', '新建菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysMenuController#addDo', '0:0:0:0:0:0:0:1', '菜单名称:网关管理', 'ba2129e64efe4c18963b95c190e38ad2', '1502593632', null);
INSERT INTO `sys_log` VALUES ('15', '超级管理员', 'aop.after', '编辑菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysMenuController#editDo', '0:0:0:0:0:0:0:1', '菜单名称:采集器管理', 'ba2129e64efe4c18963b95c190e38ad2', '1502593651', null);
INSERT INTO `sys_log` VALUES ('16', '超级管理员', 'aop.after', '修改角色菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysRoleController#editMenuDo', '0:0:0:0:0:0:0:1', '角色名称:系统管理员', 'ba2129e64efe4c18963b95c190e38ad2', '1502593669', null);
INSERT INTO `sys_log` VALUES ('17', '超级管理员', 'aop.after', '新建用户', 'com.znzz.app.web.modules.controllers.platform.sys.SysUserController#addDo', '0:0:0:0:0:0:0:1', '用户名:admin', 'ba2129e64efe4c18963b95c190e38ad2', '1502593691', null);
INSERT INTO `sys_log` VALUES ('18', '超级管理员', 'info', '用户登出', 'com.znzz.app.web.modules.controllers.platform.sys.SysLoginController#logout', '0:0:0:0:0:0:0:1', '成功退出系统！', 'ba2129e64efe4c18963b95c190e38ad2', '1502593707', null);
INSERT INTO `sys_log` VALUES ('19', '超级管理员', 'info', '用户登陆', 'com.znzz.app.web.modules.controllers.platform.sys.SysLoginController#doLogin', '0:0:0:0:0:0:0:1', '成功登录系统！', 'ba2129e64efe4c18963b95c190e38ad2', '1502593713', null);
INSERT INTO `sys_log` VALUES ('20', '超级管理员', 'aop.after', '新建菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysMenuController#addDo', '0:0:0:0:0:0:0:1', '菜单名称:添加采集器', 'ba2129e64efe4c18963b95c190e38ad2', '1502593853', null);
INSERT INTO `sys_log` VALUES ('21', '超级管理员', 'aop.after', '编辑菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysMenuController#editDo', '0:0:0:0:0:0:0:1', '菜单名称:添加采集器', 'ba2129e64efe4c18963b95c190e38ad2', '1502593868', null);
INSERT INTO `sys_log` VALUES ('22', '超级管理员', 'aop.after', '新建菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysMenuController#addDo', '0:0:0:0:0:0:0:1', '菜单名称:修改采集器', 'ba2129e64efe4c18963b95c190e38ad2', '1502593913', null);
INSERT INTO `sys_log` VALUES ('23', '超级管理员', 'aop.after', '编辑菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysMenuController#editDo', '0:0:0:0:0:0:0:1', '菜单名称:修改采集器', 'ba2129e64efe4c18963b95c190e38ad2', '1502593933', null);
INSERT INTO `sys_log` VALUES ('24', '超级管理员', 'aop.after', '新建菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysMenuController#addDo', '0:0:0:0:0:0:0:1', '菜单名称:删除采集器', 'ba2129e64efe4c18963b95c190e38ad2', '1502593989', null);
INSERT INTO `sys_log` VALUES ('25', '超级管理员', 'aop.after', '编辑菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysMenuController#editDo', '0:0:0:0:0:0:0:1', '菜单名称:删除采集器', 'ba2129e64efe4c18963b95c190e38ad2', '1502593999', null);
INSERT INTO `sys_log` VALUES ('26', '超级管理员', 'aop.after', '新建菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysMenuController#addDo', '0:0:0:0:0:0:0:1', '菜单名称:添加网关', 'ba2129e64efe4c18963b95c190e38ad2', '1502594053', null);
INSERT INTO `sys_log` VALUES ('27', '超级管理员', 'aop.after', '编辑菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysMenuController#editDo', '0:0:0:0:0:0:0:1', '菜单名称:添加网关', 'ba2129e64efe4c18963b95c190e38ad2', '1502594068', null);
INSERT INTO `sys_log` VALUES ('28', '超级管理员', 'aop.after', '新建菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysMenuController#addDo', '0:0:0:0:0:0:0:1', '菜单名称:删除网关', 'ba2129e64efe4c18963b95c190e38ad2', '1502594107', null);
INSERT INTO `sys_log` VALUES ('29', '超级管理员', 'aop.after', '编辑菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysMenuController#editDo', '0:0:0:0:0:0:0:1', '菜单名称:删除网关', 'ba2129e64efe4c18963b95c190e38ad2', '1502594114', null);
INSERT INTO `sys_log` VALUES ('30', '超级管理员', 'aop.after', '新建菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysMenuController#addDo', '0:0:0:0:0:0:0:1', '菜单名称:编辑网关', 'ba2129e64efe4c18963b95c190e38ad2', '1502594154', null);
INSERT INTO `sys_log` VALUES ('31', '超级管理员', 'aop.after', '编辑菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysMenuController#editDo', '0:0:0:0:0:0:0:1', '菜单名称:编辑网关', 'ba2129e64efe4c18963b95c190e38ad2', '1502594161', null);
INSERT INTO `sys_log` VALUES ('32', '超级管理员', 'info', '用户登出', 'com.znzz.app.web.modules.controllers.platform.sys.SysLoginController#logout', '0:0:0:0:0:0:0:1', '成功退出系统！', 'ba2129e64efe4c18963b95c190e38ad2', '1502594165', null);
INSERT INTO `sys_log` VALUES ('33', '超级管理员', 'info', '用户登陆', 'com.znzz.app.web.modules.controllers.platform.sys.SysLoginController#doLogin', '0:0:0:0:0:0:0:1', '成功登录系统！', 'ba2129e64efe4c18963b95c190e38ad2', '1502594173', null);
INSERT INTO `sys_log` VALUES ('34', '超级管理员', 'aop.after', '修改角色菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysRoleController#editMenuDo', '0:0:0:0:0:0:0:1', '角色名称:系统管理员', 'ba2129e64efe4c18963b95c190e38ad2', '1502594195', null);
INSERT INTO `sys_log` VALUES ('35', '超级管理员', 'info', '用户登出', 'com.znzz.app.web.modules.controllers.platform.sys.SysLoginController#logout', '0:0:0:0:0:0:0:1', '成功退出系统！', 'ba2129e64efe4c18963b95c190e38ad2', '1502594200', null);
INSERT INTO `sys_log` VALUES ('36', '', 'aop.after', '修改角色菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysRoleController#editMenuDo', '0:0:0:0:0:0:0:1', '角色名称:系统管理员', '', '1502594200', null);
INSERT INTO `sys_log` VALUES ('37', '超级管理员', 'info', '用户登陆', 'com.znzz.app.web.modules.controllers.platform.sys.SysLoginController#doLogin', '0:0:0:0:0:0:0:1', '成功登录系统！', 'ba2129e64efe4c18963b95c190e38ad2', '1502594206', null);
INSERT INTO `sys_log` VALUES ('38', '超级管理员', 'aop.after', '新建网关', 'com.znzz.app.web.modules.controllers.platform.instruments.InsGatewayController#addDo', '0:0:0:0:0:0:0:1', '网关名称:思科路由器', 'ba2129e64efe4c18963b95c190e38ad2', '1502594250', null);
INSERT INTO `sys_log` VALUES ('39', '超级管理员', 'aop.after', '新建网关', 'com.znzz.app.web.modules.controllers.platform.instruments.InsGatewayController#addDo', '0:0:0:0:0:0:0:1', '网关名称:思科路由器', 'ba2129e64efe4c18963b95c190e38ad2', '1502594270', null);
INSERT INTO `sys_log` VALUES ('40', '超级管理员', 'aop.after', '新建网关', 'com.znzz.app.web.modules.controllers.platform.instruments.InsGatewayController#addDo', '0:0:0:0:0:0:0:1', '网关名称:思科路由器', 'ba2129e64efe4c18963b95c190e38ad2', '1502596337', null);
INSERT INTO `sys_log` VALUES ('41', '超级管理员', 'aop.after', '新建网关', 'com.znzz.app.web.modules.controllers.platform.instruments.InsGatewayController#addDo', '0:0:0:0:0:0:0:1', '网关名称:思科路由器', 'ba2129e64efe4c18963b95c190e38ad2', '1502596510', null);
INSERT INTO `sys_log` VALUES ('42', '超级管理员', 'aop.after', '新建网关', 'com.znzz.app.web.modules.controllers.platform.instruments.InsGatewayController#addDo', '0:0:0:0:0:0:0:1', '网关名称:思科路由器', 'ba2129e64efe4c18963b95c190e38ad2', '1502596583', null);
INSERT INTO `sys_log` VALUES ('43', '超级管理员', 'aop.after', '新建网关', 'com.znzz.app.web.modules.controllers.platform.instruments.InsGatewayController#addDo', '0:0:0:0:0:0:0:1', '网关名称:思科路由器', 'ba2129e64efe4c18963b95c190e38ad2', '1502596607', null);
INSERT INTO `sys_log` VALUES ('44', '超级管理员', 'info', '用户登陆', 'com.znzz.app.web.modules.controllers.platform.sys.SysLoginController#doLogin', '0:0:0:0:0:0:0:1', '成功登录系统！', 'ba2129e64efe4c18963b95c190e38ad2', '1502596647', null);
INSERT INTO `sys_log` VALUES ('45', '超级管理员', 'aop.after', '新建网关', 'com.znzz.app.web.modules.controllers.platform.instruments.InsGatewayController#addDo', '0:0:0:0:0:0:0:1', '网关名称:思科路由器', 'ba2129e64efe4c18963b95c190e38ad2', '1502596665', null);
INSERT INTO `sys_log` VALUES ('46', '超级管理员', 'aop.after', '新建网关', 'com.znzz.app.web.modules.controllers.platform.instruments.InsGatewayController#addDo', '0:0:0:0:0:0:0:1', '网关名称:思科路由器', 'ba2129e64efe4c18963b95c190e38ad2', '1502596736', null);
INSERT INTO `sys_log` VALUES ('47', '超级管理员', 'aop.after', '新建网关', 'com.znzz.app.web.modules.controllers.platform.instruments.InsGatewayController#addDo', '0:0:0:0:0:0:0:1', '网关名称:思科路由器', 'ba2129e64efe4c18963b95c190e38ad2', '1502596774', null);
INSERT INTO `sys_log` VALUES ('48', '超级管理员', 'aop.after', '新建网关', 'com.znzz.app.web.modules.controllers.platform.instruments.InsGatewayController#addDo', '0:0:0:0:0:0:0:1', '网关名称:思科路由器', 'ba2129e64efe4c18963b95c190e38ad2', '1502596789', null);
INSERT INTO `sys_log` VALUES ('49', '超级管理员', 'aop.after', '新建网关', 'com.znzz.app.web.modules.controllers.platform.instruments.InsGatewayController#addDo', '0:0:0:0:0:0:0:1', '网关名称:思科路由器', 'ba2129e64efe4c18963b95c190e38ad2', '1502596803', null);
INSERT INTO `sys_log` VALUES ('50', '超级管理员', 'aop.after', '新建网关', 'com.znzz.app.web.modules.controllers.platform.instruments.InsGatewayController#addDo', '0:0:0:0:0:0:0:1', '网关名称:思科路由器', 'ba2129e64efe4c18963b95c190e38ad2', '1502596817', null);
INSERT INTO `sys_log` VALUES ('51', '超级管理员', 'aop.after', '编辑网关', 'com.znzz.app.web.modules.controllers.platform.instruments.InsGatewayController#editDo', '0:0:0:0:0:0:0:1', '网关名称:思科路由器', 'ba2129e64efe4c18963b95c190e38ad2', '1502596869', null);
INSERT INTO `sys_log` VALUES ('52', '超级管理员', 'aop.after', '编辑网关', 'com.znzz.app.web.modules.controllers.platform.instruments.InsGatewayController#editDo', '0:0:0:0:0:0:0:1', '网关名称:思科路由器', 'ba2129e64efe4c18963b95c190e38ad2', '1502596877', null);
INSERT INTO `sys_log` VALUES ('53', '超级管理员', 'aop.after', '新建网关', 'com.znzz.app.web.modules.controllers.platform.instruments.InsGatewayController#addDo', '0:0:0:0:0:0:0:1', '网关名称:思科路由器', 'ba2129e64efe4c18963b95c190e38ad2', '1502596896', null);
INSERT INTO `sys_log` VALUES ('54', '超级管理员', 'aop.after', '修改角色菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysRoleController#editMenuDo', '0:0:0:0:0:0:0:1', '角色名称:系统管理员', 'ba2129e64efe4c18963b95c190e38ad2', '1502596967', null);
INSERT INTO `sys_log` VALUES ('55', '超级管理员', 'aop.after', '编辑菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysMenuController#editDo', '0:0:0:0:0:0:0:1', '菜单名称:采集器管理', 'ba2129e64efe4c18963b95c190e38ad2', '1502597027', null);
INSERT INTO `sys_log` VALUES ('56', '超级管理员', 'aop.after', '编辑菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysMenuController#editDo', '0:0:0:0:0:0:0:1', '菜单名称:监控管理', 'ba2129e64efe4c18963b95c190e38ad2', '1502597042', null);
INSERT INTO `sys_log` VALUES ('57', '超级管理员', 'aop.after', '编辑菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysMenuController#editDo', '0:0:0:0:0:0:0:1', '菜单名称:监控管理', 'ba2129e64efe4c18963b95c190e38ad2', '1502597063', null);
INSERT INTO `sys_log` VALUES ('58', '超级管理员', 'aop.after', '修改角色菜单', 'com.znzz.app.web.modules.controllers.platform.sys.SysRoleController#editMenuDo', '0:0:0:0:0:0:0:1', '角色名称:系统管理员', 'ba2129e64efe4c18963b95c190e38ad2', '1502597106', null);
INSERT INTO `sys_log` VALUES ('59', '超级管理员', 'info', '用户登出', 'com.znzz.app.web.modules.controllers.platform.sys.SysLoginController#logout', '0:0:0:0:0:0:0:1', '成功退出系统！', 'ba2129e64efe4c18963b95c190e38ad2', '1502597106', null);
INSERT INTO `sys_log` VALUES ('60', '超级管理员', 'info', '用户登陆', 'com.znzz.app.web.modules.controllers.platform.sys.SysLoginController#doLogin', '0:0:0:0:0:0:0:1', '成功登录系统！', 'ba2129e64efe4c18963b95c190e38ad2', '1502597113', null);
INSERT INTO `sys_log` VALUES ('61', '超级管理员', 'info', '用户登陆', 'com.znzz.app.web.modules.controllers.platform.sys.SysLoginController#doLogin', '0:0:0:0:0:0:0:1', '成功登录系统！', 'ba2129e64efe4c18963b95c190e38ad2', '1502597398', null);
INSERT INTO `sys_log` VALUES ('62', '超级管理员', 'aop.after', '编辑网关', 'com.znzz.app.web.modules.controllers.platform.instruments.InsGatewayController#editDo', '0:0:0:0:0:0:0:1', '网关名称:思科路由器', 'ba2129e64efe4c18963b95c190e38ad2', '1502597447', null);
INSERT INTO `sys_log` VALUES ('63', '超级管理员', 'aop.after', '编辑网关', 'com.znzz.app.web.modules.controllers.platform.instruments.InsGatewayController#editDo', '0:0:0:0:0:0:0:1', '网关名称:思科路由器', 'ba2129e64efe4c18963b95c190e38ad2', '1502597452', null);
INSERT INTO `sys_log` VALUES ('64', '超级管理员', 'aop.after', '编辑网关', 'com.znzz.app.web.modules.controllers.platform.instruments.InsGatewayController#editDo', '0:0:0:0:0:0:0:1', '网关名称:思科路由器', 'ba2129e64efe4c18963b95c190e38ad2', '1502597458', null);
INSERT INTO `sys_log` VALUES ('65', '超级管理员', 'info', '用户登陆', 'com.znzz.app.web.modules.controllers.platform.sys.SysLoginController#doLogin', '0:0:0:0:0:0:0:1', '成功登录系统！', 'ba2129e64efe4c18963b95c190e38ad2', '1502601257', null);
INSERT INTO `sys_log` VALUES ('66', '超级管理员', 'info', '用户登陆', 'com.znzz.app.web.modules.controllers.platform.sys.SysLoginController#doLogin', '0:0:0:0:0:0:0:1', '成功登录系统！', 'ba2129e64efe4c18963b95c190e38ad2', '1502609341', null);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `parentId` varchar(32) DEFAULT NULL COMMENT '父级ID',
  `path` varchar(100) DEFAULT NULL COMMENT '树路径',
  `name` varchar(100) DEFAULT NULL COMMENT '菜单名称',
  `aliasName` varchar(100) DEFAULT NULL COMMENT '菜单别名',
  `type` varchar(10) DEFAULT NULL COMMENT '资源类型',
  `href` varchar(255) DEFAULT NULL COMMENT '菜单链接',
  `target` varchar(50) DEFAULT NULL COMMENT '打开方式',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `isShow` tinyint(1) DEFAULT NULL COMMENT '是否显示',
  `disabled` tinyint(1) DEFAULT NULL COMMENT '是否禁用',
  `permission` varchar(255) DEFAULT NULL COMMENT '权限标识',
  `note` varchar(255) DEFAULT NULL COMMENT '菜单介绍',
  `location` int(32) DEFAULT NULL COMMENT '排序字段',
  `hasChildren` tinyint(1) DEFAULT NULL COMMENT '有子节点',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `INDEX_SYS_MENU_PATH` (`path`),
  UNIQUE KEY `INDEX_SYS_MENU_PREM` (`permission`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('02e86a61e99746bea34236ea73dd52a5', '', '0003', 'CMS', 'CMS', 'menu', '', '', '', '1', '0', 'cms', null, '9', '1', '1a19ef09b12344b4a797d6e6dfe7fb29', '1468895671', '0');
INSERT INTO `sys_menu` VALUES ('054995ef6e714486aa72781ac4f9ad27', '88584eb0c6094c078ee1132e8e476c62', '0001000100020001', '添加用户', 'Add', 'data', null, null, null, '0', '0', 'sys.manager.user.add', null, '8', '0', '', '1502590815', '0');
INSERT INTO `sys_menu` VALUES ('05aac4cdf6c243a1bca5a9452cfebeee', '5a4a1595877a4dd484815a279394ea66', '0001000100110002', '启用禁用', 'Update', 'data', null, null, null, '0', '0', 'sys.manager.plugin.update', null, '45', '0', '', '1502590825', '0');
INSERT INTO `sys_menu` VALUES ('060488c243694a38b426bb81fed4a9d7', 'a4b00e3d95314007a2de8ef6629dd50d', '0001000100010001', '添加单位', 'Add', 'data', null, null, null, '0', '0', 'sys.manager.unit.add', null, '4', '0', '', '1502590814', '0');
INSERT INTO `sys_menu` VALUES ('0706112ff5dc46e388064a99bcdb0561', '4cd8e4e9519e4cff95465194fdcc8d88', '000200030004', '关键词回复', 'Keyword', 'menu', '/platform/wx/reply/conf/keyword', 'data-pjax', '', '1', '0', 'wx.reply.keyword', null, '10', '0', '', '1467472362', '0');
INSERT INTO `sys_menu` VALUES ('077cb6be4c7c41cc8955ee045a4f0286', '68cdbf694f71445c8587a20234d6fe31', '0003000300020001', '添加链接', 'Add', 'data', '', '', '', '0', '0', 'cms.link.link.add', null, '47', '0', '1a19ef09b12344b4a797d6e6dfe7fb29', '1468897043', '0');
INSERT INTO `sys_menu` VALUES ('0a1b80634751492f839446419ec2a8b5', 'a59cb8a80af14dc4a05a2f7113ff6c8a', '0001000100040001', '添加菜单', 'Add', 'data', null, null, null, '0', '0', 'sys.manager.menu.add', null, '18', '0', '', '1502590818', '0');
INSERT INTO `sys_menu` VALUES ('0a43d291e0c94ad88c8b690009279e34', '2fab774f8b6d40cb9d7e187babab2d91', '0002000400020004', '保存排序', 'Save', 'data', '', '', '', '0', '0', 'wx.conf.menu.sort', null, '0', '0', '', '1467474314', '0');
INSERT INTO `sys_menu` VALUES ('0a972ce655cb4c84809d58668b655900', '17e1ee23ca1443f1bc886c2f5eb7c24b', '0002000300020002', '修改图文', 'Edit', 'data', '', '', '', '0', '0', 'wx.reply.news.edit', null, '0', '0', '', '1467473596', '0');
INSERT INTO `sys_menu` VALUES ('1385ae887e5c4b8aa33fbf228be7f907', '6afc5075913d4df4b44a6476080e35a0', '000200050001', '模板编号', 'Id', 'menu', '/platform/wx/tpl/id', 'data-pjax', '', '1', '0', 'wx.tpl.id', null, '51', '0', '', '1470406854', '0');
INSERT INTO `sys_menu` VALUES ('1734e586e96941268a4c5248b593cef9', 'f426468abf714b1599729f8c36ebbb0d', '0002000200010001', '回复消息', 'Reply', 'data', '', '', '', '0', '0', 'wx.msg.user.reply', null, '0', '0', '', '1467473127', '0');
INSERT INTO `sys_menu` VALUES ('17500ef3a9e44b4fabb240162a164fcb', '6075fc0cf0ef441b9d93cc3cab3445bf', '0003000200020003', '删除文章', 'Delete', 'data', '', '', '', '0', '0', 'cms.content.article.delete', null, '40', '0', '1a19ef09b12344b4a797d6e6dfe7fb29', '1468896170', '0');
INSERT INTO `sys_menu` VALUES ('17be000f92594e7ab543192aaa1c62ad', 'a59cb8a80af14dc4a05a2f7113ff6c8a', '0001000100040002', '修改菜单', 'Edit', 'data', null, null, null, '0', '0', 'sys.manager.menu.edit', null, '19', '0', '', '1502590818', '0');
INSERT INTO `sys_menu` VALUES ('17e1ee23ca1443f1bc886c2f5eb7c24b', '4cd8e4e9519e4cff95465194fdcc8d88', '000200030002', '图文内容', 'News', 'menu', '/platform/wx/reply/news', 'data-pjax', '', '1', '0', 'wx.reply.news', null, '8', '0', '', '1467471926', '0');
INSERT INTO `sys_menu` VALUES ('19ffba700b974329b3f578cd47b95a86', 'c707f5a5491b4ceb8a52bb6172e6e21e', '0001000100080001', '添加路由', 'Add', 'data', null, null, null, '0', '0', 'sys.manager.route.add', null, '32', '0', '', '1502590821', '0');
INSERT INTO `sys_menu` VALUES ('21b27349171b4db5bdab9a9a573b9028', '46cd7ed6c1ae4a3286895ca56a4ae948', '0001000100050003', '删除参数', 'Delete', 'data', null, null, null, '0', '0', 'sys.manager.conf.delete', null, '24', '0', '', '1502590819', '0');
INSERT INTO `sys_menu` VALUES ('2275cb125710414e91b617dd7c62f12c', '17e1ee23ca1443f1bc886c2f5eb7c24b', '0002000300020001', '添加图文', 'add', 'data', '', '', '', '0', '0', 'wx.reply.news.add', null, '0', '0', '', '1467473585', '0');
INSERT INTO `sys_menu` VALUES ('234f8ec3c2bc42bf9f6202aecae36fd6', '4cd8e4e9519e4cff95465194fdcc8d88', '000200030001', '文本内容', 'Txt', 'menu', '/platform/wx/reply/txt', 'data-pjax', '', '1', '0', 'wx.reply.txt', null, '7', '0', '', '1467471884', '0');
INSERT INTO `sys_menu` VALUES ('2a63040409094f1e9dc535dd78ce15b7', '2cb327ad59b140828fd26eb2a46cb948', '0002000300030003', '删除绑定', 'Delete', 'data', '', '', '', '0', '0', 'wx.reply.follow.delete', null, '0', '0', '', '1467474080', '0');
INSERT INTO `sys_menu` VALUES ('2aabf0eae04941a6bef26e92f4dc3997', '6e58b1baaacb4c30b62244f4b4dd53fe', '0001000100100001', '添加字典', 'Add', 'data', null, null, null, '0', '0', 'sys.manager.dict.add', null, '40', '0', '', '1502590824', '0');
INSERT INTO `sys_menu` VALUES ('2bcbb53ed0f94b0da1a500945d8ec77c', '6c4be7ddf6b940c5803fcc36f3c01dda', '000400010003', '删除采集器', null, 'data', '', '', '', '1', '0', 'instrument.monitor.collect.delete', null, '62', '0', 'ba2129e64efe4c18963b95c190e38ad2', '1502593999', '0');
INSERT INTO `sys_menu` VALUES ('2cb327ad59b140828fd26eb2a46cb948', '4cd8e4e9519e4cff95465194fdcc8d88', '000200030003', '关注自动回复', 'Follow', 'menu', '/platform/wx/reply/conf/follow', 'data-pjax', '', '1', '0', 'wx.reply.follow', null, '9', '0', '', '1467472280', '0');
INSERT INTO `sys_menu` VALUES ('2fab774f8b6d40cb9d7e187babab2d91', 'bcf64d623fdd4519ae345b7a08c071a1', '000200040002', '菜单配置', 'Menu', 'menu', '/platform/wx/conf/menu', 'data-pjax', '', '1', '0', 'wx.conf.menu', null, '13', '0', '', '1467472649', '0');
INSERT INTO `sys_menu` VALUES ('304c7e6c18424d09b6304659135d5f73', '83bfec820d1647d998fbc6f55d61077c', '000400020001', '添加网关', null, 'data', '', '', '', '1', '0', 'instrument.monitor.gateway.add', null, '63', '0', 'ba2129e64efe4c18963b95c190e38ad2', '1502594068', '0');
INSERT INTO `sys_menu` VALUES ('3099f497480c4b1987bce3f3a26c3fb4', '6bb17a41f6394ed0a8a6faf5ff781354', '0002000200020003', '群发消息', 'Push', 'data', '', '', '', '0', '0', 'wx.msg.mass.pushNews', null, '0', '0', '', '1467473400', '0');
INSERT INTO `sys_menu` VALUES ('309dc29ad3c34408a68df8f867a5c9ff', '66cc21d7ce104dd6877cbce114c59fb3', '0002000400010001', '添加帐号', 'Add', 'data', '', '', '', '0', '0', 'wx.conf.account.add', null, '0', '0', '', '1467474187', '0');
INSERT INTO `sys_menu` VALUES ('30a5e70a1456447ebf90b5546e9bc321', '2cb327ad59b140828fd26eb2a46cb948', '0002000300030002', '修改绑定', 'Edit', 'data', '', '', '', '0', '0', 'wx.reply.follow.edit', null, '0', '0', '', '1467474056', '0');
INSERT INTO `sys_menu` VALUES ('31ed2243077c44448cce26abfd5ae574', '9822bafbe3454dfd8e8b974ebc304d03', '0003000300010002', '修改分类', 'Edit', 'data', '', '', '', '0', '0', 'cms.link.class.edit', null, '44', '0', '1a19ef09b12344b4a797d6e6dfe7fb29', '1468896957', '0');
INSERT INTO `sys_menu` VALUES ('33aed9298643424783116e0cf0f7fcbe', '6075fc0cf0ef441b9d93cc3cab3445bf', '0003000200020001', '添加文章', 'Add', 'data', '', '', '', '0', '0', 'cms.content.article.add', null, '38', '0', '1a19ef09b12344b4a797d6e6dfe7fb29', '1468896151', '0');
INSERT INTO `sys_menu` VALUES ('35a696a6f59147fcb7cef99a5def9782', 'b478c721f38c40a3900ca8d4b0784bcd', '000100010007', '定时任务', 'Task', 'menu', '/platform/sys/task', 'data-pjax', null, '1', '0', 'sys.manager.task', null, '27', '0', '', '1502590820', '0');
INSERT INTO `sys_menu` VALUES ('36e0faf5062b4f6b95d4167cbb1f8fea', '68cdbf694f71445c8587a20234d6fe31', '0003000300020002', '修改链接', 'Edit', 'data', '', '', '', '0', '0', 'cms.link.link.edit', null, '48', '0', '1a19ef09b12344b4a797d6e6dfe7fb29', '1468897051', '0');
INSERT INTO `sys_menu` VALUES ('3888f05aa4064f788ba7ec51c495ce7c', '1385ae887e5c4b8aa33fbf228be7f907', '0002000500010002', '删除编号', 'Delete', 'data', '', '', '', '0', '0', 'wx.tpl.id.delete', null, '55', '0', '', '1470407068', '0');
INSERT INTO `sys_menu` VALUES ('3c24111091ad4a70ad2d9cc361311d2f', '68cdbf694f71445c8587a20234d6fe31', '0003000300020003', '删除链接', 'Delete', 'data', '', '', '', '0', '0', 'cms.link.link.delete', null, '49', '0', '1a19ef09b12344b4a797d6e6dfe7fb29', '1468897060', '0');
INSERT INTO `sys_menu` VALUES ('3f330d729ca34dc9825c46122be1bfae', '02e86a61e99746bea34236ea73dd52a5', '00030003', '广告链接', 'AD', 'menu', '', '', 'ti-link', '1', '0', 'cms.link', null, '41', '1', '1a19ef09b12344b4a797d6e6dfe7fb29', '1468896230', '0');
INSERT INTO `sys_menu` VALUES ('419bcac4d44f428ea2917e7007c9e882', 'a59cb8a80af14dc4a05a2f7113ff6c8a', '0001000100040003', '删除菜单', 'Delete', 'data', null, null, null, '0', '0', 'sys.manager.menu.delete', null, '20', '0', '', '1502590818', '0');
INSERT INTO `sys_menu` VALUES ('44da90bc76a5419a841f4924333f7a66', '2fab774f8b6d40cb9d7e187babab2d91', '0002000400020002', '修改菜单', 'Edit', 'data', '', '', '', '0', '0', 'wx.conf.menu.edit', null, '0', '0', '', '1467474294', '0');
INSERT INTO `sys_menu` VALUES ('45d958ca78304f25b51f6c71cf66f6d8', '2fab774f8b6d40cb9d7e187babab2d91', '0002000400020001', '添加菜单', 'Add', 'data', '', '', '', '0', '0', 'wx.conf.menu.add', null, '0', '0', '', '1467474283', '0');
INSERT INTO `sys_menu` VALUES ('46cd7ed6c1ae4a3286895ca56a4ae948', 'b478c721f38c40a3900ca8d4b0784bcd', '000100010005', '系统参数', 'Param', 'menu', '/platform/sys/conf', 'data-pjax', null, '1', '0', 'sys.manager.conf', null, '21', '0', '', '1502590819', '0');
INSERT INTO `sys_menu` VALUES ('4781372b00bb4d52b429b58e72b80c68', 'b2631bbdbf824cc4b74d819c87962c0d', '0003000200010001', '添加栏目', 'Add', 'data', '', '', '', '0', '0', 'cms.content.channel.add', null, '33', '0', '1a19ef09b12344b4a797d6e6dfe7fb29', '1468896049', '0');
INSERT INTO `sys_menu` VALUES ('47fc6780bbd94db882a9a8750aec52fb', '35a696a6f59147fcb7cef99a5def9782', '0001000100070003', '删除任务', 'Delete', 'data', null, null, null, '0', '0', 'sys.manager.task.delete', null, '30', '0', '', '1502590821', '0');
INSERT INTO `sys_menu` VALUES ('4cd8e4e9519e4cff95465194fdcc8d88', 'b0edc6861a494b79b97990dc05f0a524', '00020003', '自动回复', 'AutoReply', 'menu', '', '', 'ti-back-left', '1', '0', 'wx.reply', null, '6', '1', '', '1467471610', '0');
INSERT INTO `sys_menu` VALUES ('4dc997fef71e4862b9db22de8e99a618', 'b19b23b0459a4754bf1fb8cb234450f2', '0002000100010001', '同步会员信息', 'Sync', 'data', '', '', '', '0', '0', 'wx.user.list.sync', null, '0', '0', '', '1467473044', '0');
INSERT INTO `sys_menu` VALUES ('50ba60ee650e4c739e6abc3ab71e4960', 'b2631bbdbf824cc4b74d819c87962c0d', '0003000200010004', '栏目排序', 'Sort', 'data', '', '', '', '0', '0', 'cms.content.channel.sort', null, '36', '0', '1a19ef09b12344b4a797d6e6dfe7fb29', '1468896092', '0');
INSERT INTO `sys_menu` VALUES ('5244f5c38eb24b918e9ad64d456daa38', '2fab774f8b6d40cb9d7e187babab2d91', '0002000400020005', '推送到微信', 'Push', 'data', '', '', '', '0', '0', 'wx.conf.menu.push', null, '0', '0', '', '1467474330', '0');
INSERT INTO `sys_menu` VALUES ('531e5053a1e041f2938713df16378da7', '35a696a6f59147fcb7cef99a5def9782', '0001000100070001', '添加任务', 'Add', 'data', null, null, null, '0', '0', 'sys.manager.task.add', null, '28', '0', '', '1502590820', '0');
INSERT INTO `sys_menu` VALUES ('555cc8f9c5554ef38295602c8f13908a', '6c4be7ddf6b940c5803fcc36f3c01dda', '000400010002', '修改采集器', null, 'data', '', '', '', '1', '0', 'instrument.monitor.collect.edit', null, '61', '0', 'ba2129e64efe4c18963b95c190e38ad2', '1502593933', '0');
INSERT INTO `sys_menu` VALUES ('56284b53c8a7479c9e8fc73651a057e6', 'a4b00e3d95314007a2de8ef6629dd50d', '0001000100010003', '删除单位', 'Delete', 'data', null, null, null, '0', '0', 'sys.manager.unit.delete', null, '6', '0', '', '1502590815', '0');
INSERT INTO `sys_menu` VALUES ('56d0658c5a8848818ac05e8ffa5c0570', '6bb17a41f6394ed0a8a6faf5ff781354', '0002000200020001', '添加图文', 'Add', 'data', '', '', '', '0', '0', 'wx.msg.mass.addNews', null, '0', '0', '', '1467473338', '0');
INSERT INTO `sys_menu` VALUES ('5a4a1595877a4dd484815a279394ea66', 'b478c721f38c40a3900ca8d4b0784bcd', '000100010011', '插件管理', 'Plugin', 'menu', '/platform/sys/plugin', 'data-pjax', null, '1', '0', 'sys.manager.plugin', null, '43', '0', '', '1502590824', '0');
INSERT INTO `sys_menu` VALUES ('6075fc0cf0ef441b9d93cc3cab3445bf', '6b6de8c720c645a1808e1c3e9ccbfc90', '000300020002', '文章管理', 'Article', 'menu', '/platform/cms/article', 'data-pjax', '', '1', '0', 'cms.content.article', null, '37', '0', '1a19ef09b12344b4a797d6e6dfe7fb29', '1468896141', '0');
INSERT INTO `sys_menu` VALUES ('6120521824ed4ac992c95a76bbf31714', '6e58b1baaacb4c30b62244f4b4dd53fe', '0001000100100002', '修改字典', 'Edit', 'data', null, null, null, '0', '0', 'sys.manager.dict.edit', null, '41', '0', '', '1502590824', '0');
INSERT INTO `sys_menu` VALUES ('66cc21d7ce104dd6877cbce114c59fb3', 'bcf64d623fdd4519ae345b7a08c071a1', '000200040001', '帐号配置', 'Account', 'menu', '/platform/wx/conf/account', 'data-pjax', '', '1', '0', 'wx.conf.account', null, '12', '0', '', '1467472624', '0');
INSERT INTO `sys_menu` VALUES ('686406c7e166440b8861aee3faa3f0a1', '88584eb0c6094c078ee1132e8e476c62', '0001000100020002', '修改用户', 'Edit', 'data', null, null, null, '0', '0', 'sys.manager.user.edit', null, '9', '0', '', '1502590815', '0');
INSERT INTO `sys_menu` VALUES ('68cdbf694f71445c8587a20234d6fe31', '3f330d729ca34dc9825c46122be1bfae', '000300030002', '链接管理', 'Link', 'menu', '/platform/cms/link/link', 'data-pjax', '', '1', '0', 'cms.link.link', null, '46', '0', '1a19ef09b12344b4a797d6e6dfe7fb29', '1468897031', '0');
INSERT INTO `sys_menu` VALUES ('6afc5075913d4df4b44a6476080e35a0', 'b0edc6861a494b79b97990dc05f0a524', '00020005', '模板消息', 'Template', 'menu', '', '', 'ti-notepad', '1', '0', 'wx.tpl', null, '50', '1', '', '1470406797', '0');
INSERT INTO `sys_menu` VALUES ('6b6de8c720c645a1808e1c3e9ccbfc90', '02e86a61e99746bea34236ea73dd52a5', '00030002', '内容管理', 'Content', 'menu', '', '', 'ti-pencil-alt', '1', '0', 'cms.content', null, '31', '1', '1a19ef09b12344b4a797d6e6dfe7fb29', '1468895990', '0');
INSERT INTO `sys_menu` VALUES ('6bb17a41f6394ed0a8a6faf5ff781354', '9f20a757a6bc40ddbb650c70debbf660', '000200020002', '群发消息', 'Mass', 'menu', '/platform/wx/msg/mass', 'data-pjax', '', '1', '0', 'wx.msg.mass', null, '5', '0', '', '1467471561', '0');
INSERT INTO `sys_menu` VALUES ('6c4be7ddf6b940c5803fcc36f3c01dda', 'b630cdab99d1419485c6281199610795', '00040001', '采集器管理', null, 'menu', '/instrument/monitor/collect', '', '', '1', '0', 'instrument.monitor.collect', null, '58', '1', 'ba2129e64efe4c18963b95c190e38ad2', '1502597027', '0');
INSERT INTO `sys_menu` VALUES ('6e58b1baaacb4c30b62244f4b4dd53fe', 'b478c721f38c40a3900ca8d4b0784bcd', '000100010010', '数据字典', 'Dict', 'menu', '/platform/sys/dict', 'data-pjax', null, '1', '0', 'sys.manager.dict', null, '39', '0', '', '1502590823', '0');
INSERT INTO `sys_menu` VALUES ('7125a72beee34b21ab3df9bf01b7bce6', '9822bafbe3454dfd8e8b974ebc304d03', '0003000300010003', '删除分类', 'Delete', 'data', '', '', '', '0', '0', 'cms.link.class.delete', null, '45', '0', '1a19ef09b12344b4a797d6e6dfe7fb29', '1468896968', '0');
INSERT INTO `sys_menu` VALUES ('717c21dee18e4d5c902ac024d2e05273', '6c4be7ddf6b940c5803fcc36f3c01dda', '000400010001', '添加采集器', null, 'data', '', '', '', '1', '0', 'instrument.monitor.collect.add', null, '60', '0', 'ba2129e64efe4c18963b95c190e38ad2', '1502593868', '0');
INSERT INTO `sys_menu` VALUES ('72c56f550bac4536a775e1080f9bfa7a', 'c194a28ec7924d8799306db216b59666', '0001000100090003', '删除应用', 'Delete', 'data', null, null, null, '0', '0', 'sys.manager.api.delete', null, '38', '0', '', '1502590823', '0');
INSERT INTO `sys_menu` VALUES ('733d3f35d49f45af99ca9220048583ba', '0706112ff5dc46e388064a99bcdb0561', '0002000300040003', '删除绑定', 'Delete', 'data', '', '', '', '0', '0', 'wx.reply.keyword.delete', null, '0', '0', '', '1467474136', '0');
INSERT INTO `sys_menu` VALUES ('73a29d3f99224426b5a87c92da122275', 'd1e991ad38a8424daf9f7eb000ee27f4', '0003000100010001', '保存配置', 'Save', 'data', '', '', '', '0', '0', 'cms.site.settings.save', null, '30', '0', '1a19ef09b12344b4a797d6e6dfe7fb29', '1468895899', '0');
INSERT INTO `sys_menu` VALUES ('7c040dfd8db347e5956a3bc1764653dc', '234f8ec3c2bc42bf9f6202aecae36fd6', '0002000300010003', '删除文本', 'Delete', 'data', '', '', '', '0', '0', 'wx.reply.txt.delete', null, '0', '0', '', '1467473540', '0');
INSERT INTO `sys_menu` VALUES ('7db6207d0dab4d6e95a7eee4f2efe875', '9822bafbe3454dfd8e8b974ebc304d03', '0003000300010001', '添加分类', 'Add', 'data', '', '', '', '0', '0', 'cms.link.class.add', null, '43', '0', '1a19ef09b12344b4a797d6e6dfe7fb29', '1468896947', '0');
INSERT INTO `sys_menu` VALUES ('7ea0d4021bc3452b93582e3105fc68da', 'c707f5a5491b4ceb8a52bb6172e6e21e', '0001000100080002', '修改路由', 'Edit', 'data', null, null, null, '0', '0', 'sys.manager.route.edit', null, '33', '0', '', '1502590822', '0');
INSERT INTO `sys_menu` VALUES ('83bfec820d1647d998fbc6f55d61077c', 'b630cdab99d1419485c6281199610795', '00040002', '网关管理', null, 'menu', '/instrument/monitor/gateway', '', '', '1', '0', 'instrument.monitor.gateway', null, '59', '1', 'ba2129e64efe4c18963b95c190e38ad2', '1502593632', '0');
INSERT INTO `sys_menu` VALUES ('88584eb0c6094c078ee1132e8e476c62', 'b478c721f38c40a3900ca8d4b0784bcd', '000100010002', '用户管理', 'User', 'menu', '/platform/sys/user', 'data-pjax', null, '1', '0', 'sys.manager.user', null, '7', '0', '', '1502590815', '0');
INSERT INTO `sys_menu` VALUES ('8ef0deb15e454e07abd44c4de713744f', 'c707f5a5491b4ceb8a52bb6172e6e21e', '0001000100080003', '删除路由', 'Delete', 'data', null, null, null, '0', '0', 'sys.manager.route.delete', null, '34', '0', '', '1502590822', '0');
INSERT INTO `sys_menu` VALUES ('96554b09a2dd4f82bab7546fa59acd35', '66cc21d7ce104dd6877cbce114c59fb3', '0002000400010002', '修改帐号', 'Edit', 'data', '', '', '', '0', '0', 'wx.conf.account.edit', null, '0', '0', '', '1467474197', '0');
INSERT INTO `sys_menu` VALUES ('9753f82f45464b7794e758291dc5cda7', 'c194a28ec7924d8799306db216b59666', '0001000100090002', '修改应用', 'Edit', 'data', null, null, null, '0', '0', 'sys.manager.api.edit', null, '37', '0', '', '1502590823', '0');
INSERT INTO `sys_menu` VALUES ('9822bafbe3454dfd8e8b974ebc304d03', '3f330d729ca34dc9825c46122be1bfae', '000300030001', '链接分类', 'Class', 'menu', '/platform/cms/link/class', 'data-pjax', '', '1', '0', 'cms.link.class', null, '42', '0', '1a19ef09b12344b4a797d6e6dfe7fb29', '1468896932', '0');
INSERT INTO `sys_menu` VALUES ('98ed60ca5e454b258ae9f81685a675bb', '9996aa416428493fbc79d84e9a208603', '0001000100030004', '分配菜单', 'SetMenu', 'data', null, null, null, '0', '0', 'sys.manager.role.menu', null, '15', '0', '', '1502590817', '0');
INSERT INTO `sys_menu` VALUES ('9996aa416428493fbc79d84e9a208603', 'b478c721f38c40a3900ca8d4b0784bcd', '000100010003', '角色管理', 'Role', 'menu', '/platform/sys/role', 'data-pjax', null, '1', '0', 'sys.manager.role', null, '11', '0', '', '1502590816', '0');
INSERT INTO `sys_menu` VALUES ('9a9557177d334c209cf73c3817fe3b63', '2fab774f8b6d40cb9d7e187babab2d91', '0002000400020003', '删除菜单', 'Delete', 'data', '', '', '', '0', '0', 'wx.conf.menu.delete', null, '0', '0', '', '1467474304', '0');
INSERT INTO `sys_menu` VALUES ('9c75db1e34fa42fea8c2770d6375cc66', '5a4a1595877a4dd484815a279394ea66', '0001000100110003', '删除插件', 'Delete', 'data', null, null, null, '0', '0', 'sys.manager.plugin.delete', null, '46', '0', '', '1502590825', '0');
INSERT INTO `sys_menu` VALUES ('9f20a757a6bc40ddbb650c70debbf660', 'b0edc6861a494b79b97990dc05f0a524', '00020002', '消息管理', 'Message', 'menu', '', '', 'ti-pencil-alt', '1', '0', 'wx.msg', null, '3', '1', '', '1467471415', '0');
INSERT INTO `sys_menu` VALUES ('a0e4fceed5a94576ba93f89f3607dfac', '9996aa416428493fbc79d84e9a208603', '0001000100030001', '添加角色', 'Add', 'data', null, null, null, '0', '0', 'sys.manager.role.add', null, '12', '0', '', '1502590816', '0');
INSERT INTO `sys_menu` VALUES ('a0fbfc5d60834ea482acc1336d58f706', '83bfec820d1647d998fbc6f55d61077c', '000400020003', '编辑网关', null, 'data', '', '', '', '1', '0', 'instrument.monitor.gateway.edit', null, '65', '0', 'ba2129e64efe4c18963b95c190e38ad2', '1502594161', '0');
INSERT INTO `sys_menu` VALUES ('a11163584dfe456cbfd6fb2d4b74391b', 'cabbe834a7474675b899e8442b5c2604', '0002000500020001', '获取列表', 'Get', 'data', '', '', '', '0', '0', 'wx.tpl.list.get', null, '56', '0', '', '1470407390', '0');
INSERT INTO `sys_menu` VALUES ('a1e2de0e0a654421b8230d8df6d58100', '88584eb0c6094c078ee1132e8e476c62', '0001000100020003', '删除用户', 'Delete', 'data', null, null, null, '0', '0', 'sys.manager.user.delete', null, '10', '0', '', '1502590816', '0');
INSERT INTO `sys_menu` VALUES ('a2bcb2d3f78445cab70e469b15f666dc', '9996aa416428493fbc79d84e9a208603', '0001000100030005', '分配用户', 'SetUser', 'data', null, null, null, '0', '0', 'sys.manager.role.user', null, '16', '0', '', '1502590817', '0');
INSERT INTO `sys_menu` VALUES ('a4b00e3d95314007a2de8ef6629dd50d', 'b478c721f38c40a3900ca8d4b0784bcd', '000100010001', '单位管理', 'Unit', 'menu', '/platform/sys/unit', 'data-pjax', null, '1', '0', 'sys.manager.unit', null, '3', '0', '', '1502590814', '0');
INSERT INTO `sys_menu` VALUES ('a53eeaece2144592a24ee3f1926eb1f4', '46cd7ed6c1ae4a3286895ca56a4ae948', '0001000100050001', '添加参数', 'Add', 'data', null, null, null, '0', '0', 'sys.manager.conf.add', null, '22', '0', '', '1502590819', '0');
INSERT INTO `sys_menu` VALUES ('a59cb8a80af14dc4a05a2f7113ff6c8a', 'b478c721f38c40a3900ca8d4b0784bcd', '000100010004', '菜单管理', 'Menu', 'menu', '/platform/sys/menu', 'data-pjax', null, '1', '0', 'sys.manager.menu', null, '17', '0', '', '1502590817', '0');
INSERT INTO `sys_menu` VALUES ('aa3db2c76b2c4fde8d452b28646c9f18', '9996aa416428493fbc79d84e9a208603', '0001000100030003', '删除角色', 'Delete', 'data', null, null, null, '0', '0', 'sys.manager.role.delete', null, '14', '0', '', '1502590817', '0');
INSERT INTO `sys_menu` VALUES ('b0edc6861a494b79b97990dc05f0a524', '', '0002', '微信', 'Wechat', 'menu', '', '', '', '1', '0', 'wx', null, '8', '1', '', '1467471229', '0');
INSERT INTO `sys_menu` VALUES ('b19b23b0459a4754bf1fb8cb234450f2', 'e4256d7b0ffc4a02906cf900322b6213', '000200010001', '会员列表', 'List', 'menu', '/platform/wx/user/index', 'data-pjax', '', '1', '0', 'wx.user.list', null, '2', '0', '', '1467471357', '0');
INSERT INTO `sys_menu` VALUES ('b2631bbdbf824cc4b74d819c87962c0d', '6b6de8c720c645a1808e1c3e9ccbfc90', '000300020001', '栏目管理', 'Channel', 'menu', '/platform/cms/channel', 'data-pjax', '', '1', '0', 'cms.content.channel', null, '32', '0', '1a19ef09b12344b4a797d6e6dfe7fb29', '1468896018', '0');
INSERT INTO `sys_menu` VALUES ('b478c721f38c40a3900ca8d4b0784bcd', 'c1c4c472af8d4a3689e137231ac99de9', '00010001', '系统管理', 'Manager', 'menu', '', '', 'ti-settings', '1', '0', 'sys.manager', '系统管理', '2', '1', '', '1502590814', '0');
INSERT INTO `sys_menu` VALUES ('b630cdab99d1419485c6281199610795', '', '0004', '监控管理', null, 'menu', '', '', '', '1', '0', 'instrument.monitor', null, '57', '1', 'ba2129e64efe4c18963b95c190e38ad2', '1502597063', '0');
INSERT INTO `sys_menu` VALUES ('bcf64d623fdd4519ae345b7a08c071a1', 'b0edc6861a494b79b97990dc05f0a524', '00020004', '微信配置', 'Config', 'menu', '', '', 'fa fa-weixin', '1', '0', 'wx.conf', null, '11', '1', '', '1467472498', '0');
INSERT INTO `sys_menu` VALUES ('bdc09ea2af5e4831a0e1fc594d8bb3b1', 'c194a28ec7924d8799306db216b59666', '0001000100090001', '添加应用', 'Add', 'data', null, null, null, '0', '0', 'sys.manager.api.add', null, '36', '0', '', '1502590823', '0');
INSERT INTO `sys_menu` VALUES ('c194a28ec7924d8799306db216b59666', 'b478c721f38c40a3900ca8d4b0784bcd', '000100010009', '应用管理', 'App', 'menu', '/platform/sys/api', 'data-pjax', null, '1', '0', 'sys.manager.api', null, '35', '0', '', '1502590822', '0');
INSERT INTO `sys_menu` VALUES ('c1c4c472af8d4a3689e137231ac99de9', '', '0001', '系统', 'System', 'menu', '', '', '', '1', '0', 'sys', '系统', '10', '1', '', '1502590813', '0');
INSERT INTO `sys_menu` VALUES ('c3a44b478d3241b899b9c3f4611bc2b6', '234f8ec3c2bc42bf9f6202aecae36fd6', '0002000300010001', '添加文本', 'Add', 'data', '', '', '', '0', '0', 'wx.reply.txt.add', null, '0', '0', '', '1467473460', '0');
INSERT INTO `sys_menu` VALUES ('c707f5a5491b4ceb8a52bb6172e6e21e', 'b478c721f38c40a3900ca8d4b0784bcd', '000100010008', '自定义路由', 'Route', 'menu', '/platform/sys/route', 'data-pjax', null, '1', '0', 'sys.manager.route', null, '31', '0', '', '1502590821', '0');
INSERT INTO `sys_menu` VALUES ('c76a84f871d047db955dd1465c845ac1', '6afc5075913d4df4b44a6476080e35a0', '000200050003', '发送记录', 'Log', 'menu', '/platform/wx/tpl/log', 'data-pjax', '', '1', '0', 'wx.tpl.log', null, '53', '0', '', '1470406926', '0');
INSERT INTO `sys_menu` VALUES ('cabbe834a7474675b899e8442b5c2604', '6afc5075913d4df4b44a6476080e35a0', '000200050002', '模板列表', 'List', 'menu', '/platform/wx/tpl/list', 'data-pjax', '', '1', '0', 'wx.tpl.list', null, '52', '0', '', '1470406883', '0');
INSERT INTO `sys_menu` VALUES ('cac2263ba3674e5f917fd05e0a4081d1', '46cd7ed6c1ae4a3286895ca56a4ae948', '0001000100050002', '修改参数', 'Edit', 'data', null, null, null, '0', '0', 'sys.manager.conf.edit', null, '23', '0', '', '1502590819', '0');
INSERT INTO `sys_menu` VALUES ('cbfcf74060164feca9edb4f088811dfe', 'b478c721f38c40a3900ca8d4b0784bcd', '000100010006', '日志管理', 'Log', 'menu', '/platform/sys/log', 'data-pjax', null, '1', '0', 'sys.manager.log', null, '25', '0', '', '1502590820', '0');
INSERT INTO `sys_menu` VALUES ('ce709456e867425297955b3c40406d7e', '6bb17a41f6394ed0a8a6faf5ff781354', '0002000200020002', '删除图文', 'Delete', 'data', '', '', '', '0', '0', 'wx.msg.mass.delNews', null, '0', '0', '', '1467473363', '0');
INSERT INTO `sys_menu` VALUES ('d1e991ad38a8424daf9f7eb000ee27f4', 'd920314e925c451da6d881e7a29743b7', '000300010001', '网站配置', 'Settings', 'menu', '/platform/cms/site', 'data-pjax', '', '1', '0', 'cms.site.settings', null, '29', '0', '1a19ef09b12344b4a797d6e6dfe7fb29', '1468895881', '0');
INSERT INTO `sys_menu` VALUES ('d568f4c2b687404e8aec7b9edcae5767', '66cc21d7ce104dd6877cbce114c59fb3', '0002000400010003', '删除帐号', 'Delete', 'data', '', '', '', '0', '0', 'wx.conf.account.delete', null, '0', '0', '', '1467474209', '0');
INSERT INTO `sys_menu` VALUES ('d920314e925c451da6d881e7a29743b7', '02e86a61e99746bea34236ea73dd52a5', '00030001', '站点管理', 'Site', 'menu', '', '', 'ti-world', '1', '0', 'cms.site', null, '28', '1', '1a19ef09b12344b4a797d6e6dfe7fb29', '1468895821', '0');
INSERT INTO `sys_menu` VALUES ('dc672f10dd9b47a7aa1dd6cbb8a74288', '83bfec820d1647d998fbc6f55d61077c', '000400020002', '删除网关', null, 'data', '', '', '', '1', '0', 'instrument.monitor.gateway.delete', null, '64', '0', 'ba2129e64efe4c18963b95c190e38ad2', '1502594114', '0');
INSERT INTO `sys_menu` VALUES ('dd965b2c1dfd493fb5efc7e4bcac99d4', '2cb327ad59b140828fd26eb2a46cb948', '0002000300030001', '添加绑定', 'Add', 'data', '', '', '', '0', '0', 'wx.reply.follow.add', null, '0', '0', '', '1467474026', '0');
INSERT INTO `sys_menu` VALUES ('dee3fbf7ea5d4cbd86e549db000739ab', '5a4a1595877a4dd484815a279394ea66', '0001000100110001', '添加插件', 'Add', 'data', null, null, null, '0', '0', 'sys.manager.plugin.add', null, '44', '0', '', '1502590825', '0');
INSERT INTO `sys_menu` VALUES ('e4256d7b0ffc4a02906cf900322b6213', 'b0edc6861a494b79b97990dc05f0a524', '00020001', '微信会员', 'Member', 'menu', '', '', 'fa fa-user', '1', '0', 'wx.user', null, '1', '1', '', '1467471292', '0');
INSERT INTO `sys_menu` VALUES ('e461c62a1d5441619cd35612f3b40691', 'b2631bbdbf824cc4b74d819c87962c0d', '0003000200010002', '修改栏目', 'Edit', 'data', '', '', '', '0', '0', 'cms.content.channel.edit', null, '34', '0', '1a19ef09b12344b4a797d6e6dfe7fb29', '1468896060', '0');
INSERT INTO `sys_menu` VALUES ('e6b6224617b04090a76e46a4b048fb96', '1385ae887e5c4b8aa33fbf228be7f907', '0002000500010001', '添加编号', 'Add', 'data', '', '', '', '0', '0', 'wx.tpl.id.add', null, '54', '0', '', '1470407055', '0');
INSERT INTO `sys_menu` VALUES ('e864c78aba63448892cbcb6a3a7f4da7', '0706112ff5dc46e388064a99bcdb0561', '0002000300040001', '添加绑定', 'Add', 'data', '', '', '', '0', '0', 'wx.reply.keyword.add', null, '0', '0', '', '1467474113', '0');
INSERT INTO `sys_menu` VALUES ('eed1c9d214f44bbe82edf30a7556a043', '35a696a6f59147fcb7cef99a5def9782', '0001000100070002', '修改任务', 'Edit', 'data', null, null, null, '0', '0', 'sys.manager.task.edit', null, '29', '0', '', '1502590821', '0');
INSERT INTO `sys_menu` VALUES ('ef9f436c61654ec09efbfa79a40061cf', '6075fc0cf0ef441b9d93cc3cab3445bf', '0003000200020002', '修改文章', 'Edit', 'data', '', '', '', '0', '0', 'cms.content.article.edit', null, '39', '0', '1a19ef09b12344b4a797d6e6dfe7fb29', '1468896159', '0');
INSERT INTO `sys_menu` VALUES ('f2bff7d575614689ba9c502d02691732', 'a4b00e3d95314007a2de8ef6629dd50d', '0001000100010002', '修改单位', 'Edit', 'data', null, null, null, '0', '0', 'sys.manager.unit.edit', null, '5', '0', '', '1502590814', '0');
INSERT INTO `sys_menu` VALUES ('f426468abf714b1599729f8c36ebbb0d', '9f20a757a6bc40ddbb650c70debbf660', '000200020001', '会员消息', 'Msg', 'menu', '/platform/wx/msg/user', 'data-pjax', '', '1', '0', 'wx.msg.user', null, '4', '1', '', '1467471478', '0');
INSERT INTO `sys_menu` VALUES ('f6fba69c3b704d79834b8bd2cc753729', 'b2631bbdbf824cc4b74d819c87962c0d', '0003000200010003', '删除栏目', 'Delete', 'data', '', '', '', '0', '0', 'cms.content.channel.delete', null, '35', '0', '1a19ef09b12344b4a797d6e6dfe7fb29', '1468896072', '0');
INSERT INTO `sys_menu` VALUES ('f75397c77f7345d28e9c9682f72e2fb3', 'cbfcf74060164feca9edb4f088811dfe', '0001000100060001', '清除日志', 'Delete', 'data', null, null, null, '0', '0', 'sys.manager.log.delete', null, '26', '0', '', '1502590820', '0');
INSERT INTO `sys_menu` VALUES ('f8455154e1124e778fbdb983e5f84ab8', '9996aa416428493fbc79d84e9a208603', '0001000100030002', '修改角色', 'Edit', 'data', null, null, null, '0', '0', 'sys.manager.role.edit', null, '13', '0', '', '1502590816', '0');
INSERT INTO `sys_menu` VALUES ('fa764a513b8042f9b5079ca47c69e5c4', '6e58b1baaacb4c30b62244f4b4dd53fe', '0001000100100003', '删除字典', 'Delete', 'data', null, null, null, '0', '0', 'sys.manager.dict.delete', null, '42', '0', '', '1502590824', '0');
INSERT INTO `sys_menu` VALUES ('fc52d5284b8f4522802383c1ef732242', '17e1ee23ca1443f1bc886c2f5eb7c24b', '0002000300020003', '删除图文', 'Delete', 'data', '', '', '', '0', '0', 'wx.reply.news.delete', null, '0', '0', '', '1467473606', '0');
INSERT INTO `sys_menu` VALUES ('fd63a8e389e04ff3a86c3cea53a3b9d5', '234f8ec3c2bc42bf9f6202aecae36fd6', '0002000300010002', '修改文本', 'Edit', 'data', '', '', '', '0', '0', 'wx.reply.txt.edit', null, '0', '0', '', '1467473519', '0');
INSERT INTO `sys_menu` VALUES ('ff6cd243a77c4ae98dacf6149c816c75', '0706112ff5dc46e388064a99bcdb0561', '0002000300040002', '修改绑定', 'Edit', 'data', '', '', '', '0', '0', 'wx.reply.keyword.edit', null, '0', '0', '', '1467474125', '0');

-- ----------------------------
-- Table structure for sys_plugin
-- ----------------------------
DROP TABLE IF EXISTS `sys_plugin`;
CREATE TABLE `sys_plugin` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `code` varchar(255) DEFAULT NULL COMMENT '唯一标识',
  `className` varchar(255) DEFAULT NULL COMMENT '类名',
  `args` varchar(255) DEFAULT NULL COMMENT '执行参数',
  `path` varchar(255) DEFAULT NULL COMMENT '文件路径',
  `disabled` tinyint(1) DEFAULT NULL COMMENT '是否禁用',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `INDEX_SYS_PLUGIN` (`code`),
  UNIQUE KEY `INDEX_SYS_CLASSNAME` (`className`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_plugin
-- ----------------------------

-- ----------------------------
-- Table structure for sys_qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `sys_qrtz_blob_triggers`;
CREATE TABLE `sys_qrtz_blob_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `sys_qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `sys_qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for sys_qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `sys_qrtz_calendars`;
CREATE TABLE `sys_qrtz_calendars` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for sys_qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `sys_qrtz_cron_triggers`;
CREATE TABLE `sys_qrtz_cron_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(120) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `sys_qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `sys_qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_qrtz_cron_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for sys_qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `sys_qrtz_fired_triggers`;
CREATE TABLE `sys_qrtz_fired_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`),
  KEY `IDX_SYS_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`,`INSTANCE_NAME`),
  KEY `IDX_SYS_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_SYS_QRTZ_FT_J_G` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_SYS_QRTZ_FT_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_SYS_QRTZ_FT_T_G` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_SYS_QRTZ_FT_TG` (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for sys_qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `sys_qrtz_job_details`;
CREATE TABLE `sys_qrtz_job_details` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_SYS_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_SYS_QRTZ_J_GRP` (`SCHED_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_qrtz_job_details
-- ----------------------------

-- ----------------------------
-- Table structure for sys_qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `sys_qrtz_locks`;
CREATE TABLE `sys_qrtz_locks` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_qrtz_locks
-- ----------------------------
INSERT INTO `sys_qrtz_locks` VALUES ('defaultScheduler', 'STATE_ACCESS');
INSERT INTO `sys_qrtz_locks` VALUES ('defaultScheduler', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for sys_qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `sys_qrtz_paused_trigger_grps`;
CREATE TABLE `sys_qrtz_paused_trigger_grps` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for sys_qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `sys_qrtz_scheduler_state`;
CREATE TABLE `sys_qrtz_scheduler_state` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_qrtz_scheduler_state
-- ----------------------------
INSERT INTO `sys_qrtz_scheduler_state` VALUES ('defaultScheduler', 'changzheng-nb1502601209466', '1502616310783', '20000');

-- ----------------------------
-- Table structure for sys_qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `sys_qrtz_simple_triggers`;
CREATE TABLE `sys_qrtz_simple_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `sys_qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `sys_qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for sys_qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `sys_qrtz_simprop_triggers`;
CREATE TABLE `sys_qrtz_simprop_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `sys_qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `sys_qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for sys_qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `sys_qrtz_triggers`;
CREATE TABLE `sys_qrtz_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_SYS_QRTZ_T_J` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_SYS_QRTZ_T_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_SYS_QRTZ_T_C` (`SCHED_NAME`,`CALENDAR_NAME`),
  KEY `IDX_SYS_QRTZ_T_G` (`SCHED_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_SYS_QRTZ_T_STATE` (`SCHED_NAME`,`TRIGGER_STATE`),
  KEY `IDX_SYS_QRTZ_T_N_STATE` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_SYS_QRTZ_T_N_G_STATE` (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_SYS_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`,`NEXT_FIRE_TIME`),
  KEY `IDX_SYS_QRTZ_T_NFT_ST` (`SCHED_NAME`,`TRIGGER_STATE`,`NEXT_FIRE_TIME`),
  KEY `IDX_SYS_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`),
  KEY `IDX_SYS_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_STATE`),
  KEY `IDX_SYS_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  CONSTRAINT `sys_qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `sys_qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_qrtz_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` varchar(32) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `aliasName` varchar(50) DEFAULT NULL,
  `disabled` tinyint(1) DEFAULT NULL,
  `unitid` varchar(32) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `INDEX_SYS_ROLE_CODE` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('2c328e431f524f898161094ac669406c', '公共角色', 'public', 'Public', '0', '', 'All user has role', '', '1502590826', '0');
INSERT INTO `sys_role` VALUES ('f8341538cd4540699fbc54e8c7de315f', '系统管理员', 'sysadmin', 'Sysadmin', '0', '', 'System Admin', '', '1502590826', '0');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `roleId` varchar(32) DEFAULT NULL,
  `menuId` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '4dc997fef71e4862b9db22de8e99a618');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '1734e586e96941268a4c5248b593cef9');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '56d0658c5a8848818ac05e8ffa5c0570');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'ce709456e867425297955b3c40406d7e');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '3099f497480c4b1987bce3f3a26c3fb4');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'c3a44b478d3241b899b9c3f4611bc2b6');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'fd63a8e389e04ff3a86c3cea53a3b9d5');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '7c040dfd8db347e5956a3bc1764653dc');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '2275cb125710414e91b617dd7c62f12c');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '0a972ce655cb4c84809d58668b655900');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'fc52d5284b8f4522802383c1ef732242');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'dd965b2c1dfd493fb5efc7e4bcac99d4');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '30a5e70a1456447ebf90b5546e9bc321');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '2a63040409094f1e9dc535dd78ce15b7');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'e864c78aba63448892cbcb6a3a7f4da7');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'ff6cd243a77c4ae98dacf6149c816c75');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '733d3f35d49f45af99ca9220048583ba');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '309dc29ad3c34408a68df8f867a5c9ff');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '96554b09a2dd4f82bab7546fa59acd35');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'd568f4c2b687404e8aec7b9edcae5767');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '45d958ca78304f25b51f6c71cf66f6d8');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '44da90bc76a5419a841f4924333f7a66');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '9a9557177d334c209cf73c3817fe3b63');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '0a43d291e0c94ad88c8b690009279e34');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '5244f5c38eb24b918e9ad64d456daa38');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'e6b6224617b04090a76e46a4b048fb96');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '3888f05aa4064f788ba7ec51c495ce7c');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'a11163584dfe456cbfd6fb2d4b74391b');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'c76a84f871d047db955dd1465c845ac1');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '73a29d3f99224426b5a87c92da122275');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '4781372b00bb4d52b429b58e72b80c68');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'e461c62a1d5441619cd35612f3b40691');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'f6fba69c3b704d79834b8bd2cc753729');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '50ba60ee650e4c739e6abc3ab71e4960');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '33aed9298643424783116e0cf0f7fcbe');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'ef9f436c61654ec09efbfa79a40061cf');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '17500ef3a9e44b4fabb240162a164fcb');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '7db6207d0dab4d6e95a7eee4f2efe875');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '31ed2243077c44448cce26abfd5ae574');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '7125a72beee34b21ab3df9bf01b7bce6');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '077cb6be4c7c41cc8955ee045a4f0286');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '36e0faf5062b4f6b95d4167cbb1f8fea');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '3c24111091ad4a70ad2d9cc361311d2f');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '060488c243694a38b426bb81fed4a9d7');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'f2bff7d575614689ba9c502d02691732');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '56284b53c8a7479c9e8fc73651a057e6');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '054995ef6e714486aa72781ac4f9ad27');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '686406c7e166440b8861aee3faa3f0a1');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'a1e2de0e0a654421b8230d8df6d58100');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'a0e4fceed5a94576ba93f89f3607dfac');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'f8455154e1124e778fbdb983e5f84ab8');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'aa3db2c76b2c4fde8d452b28646c9f18');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '98ed60ca5e454b258ae9f81685a675bb');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'a2bcb2d3f78445cab70e469b15f666dc');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '0a1b80634751492f839446419ec2a8b5');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '17be000f92594e7ab543192aaa1c62ad');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '419bcac4d44f428ea2917e7007c9e882');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'a53eeaece2144592a24ee3f1926eb1f4');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'cac2263ba3674e5f917fd05e0a4081d1');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '21b27349171b4db5bdab9a9a573b9028');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'f75397c77f7345d28e9c9682f72e2fb3');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '531e5053a1e041f2938713df16378da7');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'eed1c9d214f44bbe82edf30a7556a043');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '47fc6780bbd94db882a9a8750aec52fb');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '19ffba700b974329b3f578cd47b95a86');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '7ea0d4021bc3452b93582e3105fc68da');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '8ef0deb15e454e07abd44c4de713744f');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'bdc09ea2af5e4831a0e1fc594d8bb3b1');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '9753f82f45464b7794e758291dc5cda7');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '72c56f550bac4536a775e1080f9bfa7a');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '2aabf0eae04941a6bef26e92f4dc3997');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '6120521824ed4ac992c95a76bbf31714');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'fa764a513b8042f9b5079ca47c69e5c4');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'dee3fbf7ea5d4cbd86e549db000739ab');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '05aac4cdf6c243a1bca5a9452cfebeee');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '9c75db1e34fa42fea8c2770d6375cc66');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '717c21dee18e4d5c902ac024d2e05273');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '555cc8f9c5554ef38295602c8f13908a');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '2bcbb53ed0f94b0da1a500945d8ec77c');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '304c7e6c18424d09b6304659135d5f73');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'dc672f10dd9b47a7aa1dd6cbb8a74288');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'a0fbfc5d60834ea482acc1336d58f706');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'b19b23b0459a4754bf1fb8cb234450f2');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'e4256d7b0ffc4a02906cf900322b6213');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'b0edc6861a494b79b97990dc05f0a524');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'f426468abf714b1599729f8c36ebbb0d');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '9f20a757a6bc40ddbb650c70debbf660');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '6bb17a41f6394ed0a8a6faf5ff781354');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '234f8ec3c2bc42bf9f6202aecae36fd6');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '4cd8e4e9519e4cff95465194fdcc8d88');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '17e1ee23ca1443f1bc886c2f5eb7c24b');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '2cb327ad59b140828fd26eb2a46cb948');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '0706112ff5dc46e388064a99bcdb0561');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '66cc21d7ce104dd6877cbce114c59fb3');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'bcf64d623fdd4519ae345b7a08c071a1');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '2fab774f8b6d40cb9d7e187babab2d91');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '1385ae887e5c4b8aa33fbf228be7f907');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '6afc5075913d4df4b44a6476080e35a0');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'cabbe834a7474675b899e8442b5c2604');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'd1e991ad38a8424daf9f7eb000ee27f4');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'd920314e925c451da6d881e7a29743b7');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '02e86a61e99746bea34236ea73dd52a5');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'b2631bbdbf824cc4b74d819c87962c0d');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '6b6de8c720c645a1808e1c3e9ccbfc90');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '6075fc0cf0ef441b9d93cc3cab3445bf');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '9822bafbe3454dfd8e8b974ebc304d03');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '3f330d729ca34dc9825c46122be1bfae');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '68cdbf694f71445c8587a20234d6fe31');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'a4b00e3d95314007a2de8ef6629dd50d');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'b478c721f38c40a3900ca8d4b0784bcd');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'c1c4c472af8d4a3689e137231ac99de9');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '88584eb0c6094c078ee1132e8e476c62');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '9996aa416428493fbc79d84e9a208603');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'a59cb8a80af14dc4a05a2f7113ff6c8a');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '46cd7ed6c1ae4a3286895ca56a4ae948');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'cbfcf74060164feca9edb4f088811dfe');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '35a696a6f59147fcb7cef99a5def9782');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'c707f5a5491b4ceb8a52bb6172e6e21e');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'c194a28ec7924d8799306db216b59666');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '6e58b1baaacb4c30b62244f4b4dd53fe');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '5a4a1595877a4dd484815a279394ea66');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '6c4be7ddf6b940c5803fcc36f3c01dda');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', 'b630cdab99d1419485c6281199610795');
INSERT INTO `sys_role_menu` VALUES ('f8341538cd4540699fbc54e8c7de315f', '83bfec820d1647d998fbc6f55d61077c');

-- ----------------------------
-- Table structure for sys_route
-- ----------------------------
DROP TABLE IF EXISTS `sys_route`;
CREATE TABLE `sys_route` (
  `id` varchar(32) NOT NULL,
  `url` varchar(255) DEFAULT NULL COMMENT '原始路径',
  `toUrl` varchar(255) DEFAULT NULL COMMENT '跳转路径',
  `type` varchar(10) DEFAULT NULL COMMENT '转发类型',
  `disabled` tinyint(1) DEFAULT NULL COMMENT '是否禁用',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_route
-- ----------------------------
INSERT INTO `sys_route` VALUES ('0bc4e562c6fa41e0b20e65ab4c35dcdf', '/sysadmin', '/platform/login', 'hide', '0', '', '1502590863', '0');

-- ----------------------------
-- Table structure for sys_task
-- ----------------------------
DROP TABLE IF EXISTS `sys_task`;
CREATE TABLE `sys_task` (
  `id` varchar(32) NOT NULL,
  `name` varchar(50) DEFAULT NULL COMMENT '任务名',
  `jobClass` varchar(255) DEFAULT NULL COMMENT '执行类',
  `note` varchar(255) DEFAULT NULL COMMENT '任务说明',
  `cron` varchar(50) DEFAULT NULL COMMENT '定时规则',
  `data` text COMMENT '执行参数',
  `exeAt` int(32) DEFAULT NULL COMMENT '执行时间',
  `exeResult` text COMMENT '执行结果',
  `disabled` tinyint(1) DEFAULT NULL COMMENT '是否禁用',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_task
-- ----------------------------
INSERT INTO `sys_task` VALUES ('23c0bf1e2dc944e68b845ec6f0d8c0d2', '测试任务', 'cn.wizzer.app.web.commons.quartz.job.TestJob', '微信号：wizzer | 欢迎发送红包以示支持，多谢。。', '*/5 * * * * ?', '{\"hi\":\"Wechat:wizzer | send red packets of support,thank u\"}', null, null, '1', '', '1502590855', '0');

-- ----------------------------
-- Table structure for sys_unit
-- ----------------------------
DROP TABLE IF EXISTS `sys_unit`;
CREATE TABLE `sys_unit` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `parentId` varchar(32) DEFAULT NULL COMMENT '父级ID',
  `path` varchar(100) DEFAULT NULL COMMENT '树路径',
  `name` varchar(100) DEFAULT NULL COMMENT '单位名称',
  `aliasName` varchar(100) DEFAULT NULL COMMENT '单位别名',
  `unitcode` varchar(20) DEFAULT NULL COMMENT '机构编码',
  `note` varchar(255) DEFAULT NULL COMMENT '单位介绍',
  `address` varchar(100) DEFAULT NULL COMMENT '单位地址',
  `telephone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) DEFAULT NULL COMMENT '单位邮箱',
  `website` varchar(100) DEFAULT NULL COMMENT '单位网站',
  `location` int(32) DEFAULT NULL COMMENT '排序字段',
  `hasChildren` tinyint(1) DEFAULT NULL COMMENT '有子节点',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `INDEX_SYS_UNIT_PATH` (`path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_unit
-- ----------------------------
INSERT INTO `sys_unit` VALUES ('dded7b30d4c14cc8b5292d5e967dde7e', '', '0001', '系统管理', 'System', null, null, '银河-太阳系-地球', '', 'wizzer@qq.com', 'http://www.wizzer.cn', '1', '0', '', '1502590812', '0');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `loginname` varchar(120) DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `salt` varchar(50) DEFAULT NULL COMMENT '密码盐',
  `username` varchar(100) DEFAULT NULL COMMENT '用户名',
  `isOnline` tinyint(1) DEFAULT NULL COMMENT '是否在线',
  `disabled` tinyint(1) DEFAULT NULL COMMENT '是否禁用',
  `email` varchar(255) DEFAULT NULL,
  `loginAt` int(32) DEFAULT NULL COMMENT '登陆时间',
  `loginIp` varchar(255) DEFAULT NULL COMMENT '登陆IP',
  `loginCount` int(32) DEFAULT NULL COMMENT '登陆次数',
  `customMenu` varchar(255) DEFAULT NULL COMMENT '常用菜单',
  `loginTheme` varchar(100) DEFAULT NULL COMMENT '皮肤样式',
  `loginSidebar` tinyint(1) DEFAULT NULL,
  `loginBoxed` tinyint(1) DEFAULT NULL,
  `loginScroll` tinyint(1) DEFAULT NULL,
  `loginPjax` tinyint(1) DEFAULT NULL,
  `unitid` varchar(32) DEFAULT NULL,
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `INDEX_SYS_USER_LOGINNAMAE` (`loginname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('ad271cece8b54f71bdfe5a0241d3bbc8', 'admin', 'EYcUdAjzwEn6AV+hpeazGyWS+UaWWJw0sl2A4FFSv5s=', 'Ygokvy38XOjDkClN1zMkZw==', 'admin', '0', '0', '123@qq.com', '0', null, '0', null, null, '0', '0', '0', '1', 'dded7b30d4c14cc8b5292d5e967dde7e', 'ba2129e64efe4c18963b95c190e38ad2', '1502593691', '0');
INSERT INTO `sys_user` VALUES ('ba2129e64efe4c18963b95c190e38ad2', 'superadmin', 'kyZSOnhh2Fa0hoqBWRuzLt0dIV7FxvIAvl4E5kDAFyg=', 'FqRL557DLHcVOW3UPQVWPA==', '超级管理员', '1', '0', 'wizzer@qq.com', '1502609341', '127.0.0.1', '12', null, 'palette.css', '0', '0', '0', '1', 'dded7b30d4c14cc8b5292d5e967dde7e', '', '1502590833', '0');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `userId` varchar(32) DEFAULT NULL,
  `roleId` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('ba2129e64efe4c18963b95c190e38ad2', 'f8341538cd4540699fbc54e8c7de315f');

-- ----------------------------
-- Table structure for sys_user_unit
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_unit`;
CREATE TABLE `sys_user_unit` (
  `userId` varchar(32) DEFAULT NULL,
  `unitId` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_unit
-- ----------------------------
INSERT INTO `sys_user_unit` VALUES ('ba2129e64efe4c18963b95c190e38ad2', 'dded7b30d4c14cc8b5292d5e967dde7e');

-- ----------------------------
-- Table structure for wx_config
-- ----------------------------
DROP TABLE IF EXISTS `wx_config`;
CREATE TABLE `wx_config` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `appname` varchar(120) DEFAULT NULL COMMENT '公众号名称',
  `ghid` varchar(100) DEFAULT NULL COMMENT '原始ID',
  `appid` varchar(50) DEFAULT NULL COMMENT 'Appid',
  `appsecret` varchar(50) DEFAULT NULL COMMENT 'Appsecret',
  `encodingAESKey` varchar(100) DEFAULT NULL COMMENT 'EncodingAESKey',
  `token` varchar(100) DEFAULT NULL COMMENT 'Token',
  `access_token` varchar(255) DEFAULT NULL COMMENT 'access_token',
  `access_token_expires` int(32) DEFAULT NULL COMMENT 'access_token_expires',
  `access_token_lastat` varchar(50) DEFAULT NULL COMMENT 'access_token_lastat',
  `payEnabled` tinyint(1) DEFAULT NULL COMMENT '禁用支付',
  `payInfo` text COMMENT '支付信息',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_config
-- ----------------------------

-- ----------------------------
-- Table structure for wx_mass
-- ----------------------------
DROP TABLE IF EXISTS `wx_mass`;
CREATE TABLE `wx_mass` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `name` varchar(255) DEFAULT NULL COMMENT '群发名称',
  `type` varchar(20) DEFAULT NULL COMMENT '群发类型',
  `media_id` varchar(255) DEFAULT NULL COMMENT '媒体文件ID',
  `scope` varchar(20) DEFAULT NULL COMMENT 'Scope',
  `content` text COMMENT 'Content',
  `status` int(32) DEFAULT NULL COMMENT '发送状态',
  `wxid` varchar(32) DEFAULT NULL COMMENT '微信ID',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_mass
-- ----------------------------

-- ----------------------------
-- Table structure for wx_mass_news
-- ----------------------------
DROP TABLE IF EXISTS `wx_mass_news`;
CREATE TABLE `wx_mass_news` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `thumb_media_id` varchar(255) DEFAULT NULL COMMENT '缩略图',
  `author` varchar(120) DEFAULT NULL COMMENT '作者',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `content_source_url` varchar(255) DEFAULT NULL COMMENT '原地址',
  `content` text COMMENT '图文内容',
  `digest` text COMMENT '摘要',
  `show_cover_pic` int(32) DEFAULT NULL COMMENT '显示封面',
  `location` int(32) DEFAULT NULL COMMENT '排序字段',
  `wxid` varchar(32) DEFAULT NULL COMMENT '微信ID',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_mass_news
-- ----------------------------

-- ----------------------------
-- Table structure for wx_mass_send
-- ----------------------------
DROP TABLE IF EXISTS `wx_mass_send`;
CREATE TABLE `wx_mass_send` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `massId` varchar(32) DEFAULT NULL COMMENT '群发ID',
  `receivers` text COMMENT 'Openid列表',
  `status` int(32) DEFAULT NULL COMMENT '发送状态',
  `msgId` varchar(32) DEFAULT NULL COMMENT 'msgId',
  `errCode` varchar(32) DEFAULT NULL COMMENT 'errCode',
  `errMsg` varchar(255) DEFAULT NULL COMMENT 'errMsg',
  `wxid` varchar(32) DEFAULT NULL COMMENT '微信ID',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_mass_send
-- ----------------------------

-- ----------------------------
-- Table structure for wx_menu
-- ----------------------------
DROP TABLE IF EXISTS `wx_menu`;
CREATE TABLE `wx_menu` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `parentId` varchar(32) DEFAULT NULL COMMENT '父ID',
  `path` varchar(100) DEFAULT NULL COMMENT '树路径',
  `menuName` varchar(20) DEFAULT NULL COMMENT '菜单名称',
  `menuType` varchar(20) DEFAULT NULL COMMENT '菜单类型',
  `menuKey` varchar(20) DEFAULT NULL COMMENT '关键词',
  `url` varchar(255) DEFAULT NULL COMMENT '网址',
  `appid` varchar(255) DEFAULT NULL COMMENT '小程序appid',
  `pagepath` varchar(255) DEFAULT NULL COMMENT '小程序入口页',
  `location` int(32) DEFAULT NULL COMMENT '排序字段',
  `hasChildren` tinyint(1) DEFAULT NULL COMMENT '有子节点',
  `wxid` varchar(32) DEFAULT NULL COMMENT '微信ID',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_menu
-- ----------------------------

-- ----------------------------
-- Table structure for wx_msg
-- ----------------------------
DROP TABLE IF EXISTS `wx_msg`;
CREATE TABLE `wx_msg` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `openid` varchar(50) DEFAULT NULL COMMENT 'openid',
  `nickname` varchar(255) DEFAULT NULL COMMENT '微信昵称',
  `type` varchar(20) DEFAULT NULL COMMENT '信息类型',
  `content` text COMMENT '信息内容',
  `replyId` varchar(32) DEFAULT NULL COMMENT '回复ID',
  `wxid` varchar(32) DEFAULT NULL COMMENT '微信ID',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_msg
-- ----------------------------

-- ----------------------------
-- Table structure for wx_msg_reply
-- ----------------------------
DROP TABLE IF EXISTS `wx_msg_reply`;
CREATE TABLE `wx_msg_reply` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `msgid` varchar(32) DEFAULT NULL COMMENT 'msgid',
  `openid` varchar(50) DEFAULT NULL COMMENT 'openid',
  `type` varchar(20) DEFAULT NULL COMMENT '信息类型',
  `content` text COMMENT '信息内容',
  `wxid` varchar(32) DEFAULT NULL COMMENT '微信ID',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_msg_reply
-- ----------------------------

-- ----------------------------
-- Table structure for wx_reply
-- ----------------------------
DROP TABLE IF EXISTS `wx_reply`;
CREATE TABLE `wx_reply` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `type` varchar(20) DEFAULT NULL COMMENT '回复类型',
  `msgType` varchar(20) DEFAULT NULL COMMENT '消息类型',
  `keyword` varchar(50) DEFAULT NULL COMMENT '关键词',
  `content` text COMMENT '回复内容',
  `wxid` varchar(32) DEFAULT NULL COMMENT '微信ID',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_reply
-- ----------------------------

-- ----------------------------
-- Table structure for wx_reply_news
-- ----------------------------
DROP TABLE IF EXISTS `wx_reply_news`;
CREATE TABLE `wx_reply_news` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `description` varchar(255) DEFAULT NULL COMMENT '摘要',
  `picUrl` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `url` varchar(255) DEFAULT NULL COMMENT '文章路径',
  `location` int(32) DEFAULT NULL COMMENT '排序字段',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_reply_news
-- ----------------------------

-- ----------------------------
-- Table structure for wx_reply_txt
-- ----------------------------
DROP TABLE IF EXISTS `wx_reply_txt`;
CREATE TABLE `wx_reply_txt` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `content` text COMMENT '内容',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_reply_txt
-- ----------------------------

-- ----------------------------
-- Table structure for wx_tpl_id
-- ----------------------------
DROP TABLE IF EXISTS `wx_tpl_id`;
CREATE TABLE `wx_tpl_id` (
  `id` varchar(32) NOT NULL COMMENT '模板编号',
  `template_id` varchar(255) DEFAULT NULL COMMENT '模板ID',
  `wxid` varchar(32) DEFAULT NULL COMMENT '微信ID',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_tpl_id
-- ----------------------------

-- ----------------------------
-- Table structure for wx_tpl_list
-- ----------------------------
DROP TABLE IF EXISTS `wx_tpl_list`;
CREATE TABLE `wx_tpl_list` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `template_id` varchar(100) DEFAULT NULL COMMENT '模板ID',
  `title` varchar(255) DEFAULT NULL COMMENT '模板标题',
  `primary_industry` varchar(100) DEFAULT NULL COMMENT '主营行业',
  `deputy_industry` varchar(100) DEFAULT NULL COMMENT '副营行业',
  `content` varchar(300) DEFAULT NULL COMMENT '模板内容',
  `example` varchar(300) DEFAULT NULL COMMENT '模板示例',
  `wxid` varchar(32) DEFAULT NULL COMMENT '微信ID',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `INDEX_WX_TPL_LIST` (`template_id`,`wxid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_tpl_list
-- ----------------------------

-- ----------------------------
-- Table structure for wx_tpl_log
-- ----------------------------
DROP TABLE IF EXISTS `wx_tpl_log`;
CREATE TABLE `wx_tpl_log` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `openid` varchar(50) DEFAULT NULL COMMENT 'openid',
  `nickname` varchar(255) DEFAULT NULL COMMENT '微信昵称',
  `content` text COMMENT '发送内容',
  `status` int(32) DEFAULT NULL COMMENT '发送状态',
  `result` text COMMENT '发送结果',
  `wxid` varchar(32) DEFAULT NULL COMMENT '微信ID',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_tpl_log
-- ----------------------------

-- ----------------------------
-- Table structure for wx_user
-- ----------------------------
DROP TABLE IF EXISTS `wx_user`;
CREATE TABLE `wx_user` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `openid` varchar(50) DEFAULT NULL COMMENT 'openid',
  `unionid` varchar(50) DEFAULT NULL COMMENT 'unionid',
  `nickname` varchar(255) DEFAULT NULL COMMENT '微信昵称',
  `subscribe` tinyint(1) DEFAULT NULL COMMENT '是否关注',
  `subscribeAt` int(32) DEFAULT NULL COMMENT '关注时间',
  `sex` int(32) DEFAULT NULL COMMENT '性别',
  `country` varchar(50) DEFAULT NULL COMMENT '国家',
  `province` varchar(50) DEFAULT NULL COMMENT '省份',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `headimgurl` varchar(255) DEFAULT NULL COMMENT '头像',
  `wxid` varchar(32) DEFAULT NULL COMMENT '微信ID',
  `opBy` varchar(32) DEFAULT NULL COMMENT '操作人',
  `opAt` int(32) DEFAULT NULL COMMENT '操作时间',
  `delFlag` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `INDEX_WX_USER_OPENID` (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_user
-- ----------------------------
