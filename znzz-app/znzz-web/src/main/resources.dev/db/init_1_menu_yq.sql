/* updateSysMenu */
INSERT INTO `sys_menu` VALUES ('a9a8d95cd0f243b4938140f09b0affa1', '', '0001', '系统', 'System', 'menu', '', 'data-pjax', 'ti-menu-alt', '1', '0', 'sys', '系统', '1', '1', '15729ed8e7d54060b09cb1105b34e562', '1506325377', '0');
/* updateSysMenu1*/
INSERT INTO `sys_menu` VALUES ('620d11b448184980a9132ffcedcbad80', 'a9a8d95cd0f243b4938140f09b0affa1', '00010001', '系统管理', 'Manager', 'menu', '', '', 'ti-settings', '1', '0', 'sys.manager', '系统管理', '2', '1', '', '1502677635', '0');
/* updateSysMenu2*/
INSERT INTO `sys_menu` VALUES ('45c45b7d2bb447bc9481ce1514c33680', '620d11b448184980a9132ffcedcbad80', '000100010001', '单位管理', 'Unit', 'menu', '/platform/sys/unit', 'data-pjax', null, '1', '0', 'sys.manager.unit', null, '10', '0', '', '1502677635', '0');
/* updateSysMenu3 */
INSERT INTO `sys_menu` VALUES ('8d54b679aa494fe7be104fcc39d77d8c', '45c45b7d2bb447bc9481ce1514c33680', '0001000100010001', '添加单位', 'Add', 'menu', '', '', '', '0', '0', 'sys.manager.unit.add', null, '0', '0', '15729ed8e7d54060b09cb1105b34e562', '1503890186', '0');
/* updateSysMenu4 */
INSERT INTO `sys_menu` VALUES ('ee894e06a8e3463093557693f62d23e3', '45c45b7d2bb447bc9481ce1514c33680', '0001000100010002', '修改单位', 'Edit', 'data', null, null, null, '0', '0', 'sys.manager.unit.edit', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu5*/
INSERT INTO `sys_menu` VALUES ('bd61fca12cdb45c2b93e4fd72bbf7663', '45c45b7d2bb447bc9481ce1514c33680', '0001000100010003', '删除单位', 'Delete', 'data', null, null, null, '0', '0', 'sys.manager.unit.delete', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu6*/
INSERT INTO `sys_menu` VALUES ('82a9cf046114406cb0e6a829afea6b0a', '620d11b448184980a9132ffcedcbad80', '000100010002', '用户管理', 'User', 'menu', '/platform/sys/user', 'data-pjax', null, '1', '0', 'sys.manager.user', null, '11', '0', '', '1502677635', '0');
/* updateSysMenu7*/
INSERT INTO `sys_menu` VALUES ('053bb74d06a84177a083be227b613e04', '82a9cf046114406cb0e6a829afea6b0a', '0001000100020001', '添加用户', 'Add', 'data', null, null, null, '0', '0', 'sys.manager.user.add', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu 8*/
INSERT INTO `sys_menu` VALUES ('45eb1113b74948b78771e87dbc8868f2', '82a9cf046114406cb0e6a829afea6b0a', '0001000100020002', '修改用户', 'Edit', 'data', null, null, null, '0', '0', 'sys.manager.user.edit', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu9 */
INSERT INTO `sys_menu` VALUES ('bf8d6ce6b72643049f3222ccec91cd98', '82a9cf046114406cb0e6a829afea6b0a', '0001000100020003', '删除用户', 'Delete', 'data', null, null, null, '0', '0', 'sys.manager.user.delete', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu10*/
INSERT INTO `sys_menu` VALUES ('c8533a00eed74e85865adadb461dd42f', '620d11b448184980a9132ffcedcbad80', '000100010003', '角色管理', 'Role', 'menu', '/platform/sys/role', 'data-pjax', null, '1', '0', 'sys.manager.role', null, '12', '0', '', '1502677635', '0');
/* updateSysMenu11 */
INSERT INTO `sys_menu` VALUES ('ad38470fb882440681aed5b60124439b', 'c8533a00eed74e85865adadb461dd42f', '0001000100030001', '添加角色', 'Add', 'data', null, null, null, '0', '0', 'sys.manager.role.add', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu12*/
INSERT INTO `sys_menu` VALUES ('39912196b12d458eba23cff669328ea9', 'c8533a00eed74e85865adadb461dd42f', '0001000100030002', '修改角色', 'Edit', 'data', null, null, null, '0', '0', 'sys.manager.role.edit', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu13 */
INSERT INTO `sys_menu` VALUES ('7c8b202a143b4f079705f9210ba25fa5', 'c8533a00eed74e85865adadb461dd42f', '0001000100030003', '删除角色', 'Delete', 'data', null, null, null, '0', '0', 'sys.manager.role.delete', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu14 */
INSERT INTO `sys_menu` VALUES ('30e6354fc02c424db5ba8ca0d4b40bee', 'c8533a00eed74e85865adadb461dd42f', '0001000100030004', '分配菜单', 'SetMenu', 'data', null, null, null, '0', '0', 'sys.manager.role.menu', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu15 */
INSERT INTO `sys_menu` VALUES ('48540178b1db44ce9fd5d89ac83b7a94', 'c8533a00eed74e85865adadb461dd42f', '0001000100030005', '分配用户', 'SetUser', 'data', null, null, null, '0', '0', 'sys.manager.role.user', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu16 */
INSERT INTO `sys_menu` VALUES ('0c2b49576374400bb88b629067132a09', '620d11b448184980a9132ffcedcbad80', '000100010004', '菜单管理', 'Menu', 'menu', '/platform/sys/menu', 'data-pjax', null, '1', '0', 'sys.manager.menu', null, '13', '0', '', '1502677635', '0');
/* updateSysMenu17 */
INSERT INTO `sys_menu` VALUES ('f12e07dd69834efba248a666abd4c096', '0c2b49576374400bb88b629067132a09', '0001000100040001', '添加菜单', 'Add', 'data', null, null, null, '0', '0', 'sys.manager.menu.add', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu18 */
INSERT INTO `sys_menu` VALUES ('d32b98129b664586a3ab51e2cbfaea8f', '0c2b49576374400bb88b629067132a09', '0001000100040002', '修改菜单', 'Edit', 'data', null, null, null, '0', '0', 'sys.manager.menu.edit', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu19 */
INSERT INTO `sys_menu` VALUES ('b80c72152f4044aa8b36a381b8356c17', '0c2b49576374400bb88b629067132a09', '0001000100040003', '删除菜单', 'Delete', 'data', null, null, null, '0', '0', 'sys.manager.menu.delete', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu20 */
INSERT INTO `sys_menu` VALUES ('d40b4b76256b49d08d271864a764df68', '620d11b448184980a9132ffcedcbad80', '000100010005', '系统参数', 'Param', 'menu', '/platform/sys/conf', 'data-pjax', null, '1', '0', 'sys.manager.conf', null, '6', '0', '', '1502677635', '0');
/* updateSysMenu21 */
INSERT INTO `sys_menu` VALUES ('8bf9ee2ffdca4d1a9673956a64c3ccef', 'd40b4b76256b49d08d271864a764df68', '0001000100050001', '添加参数', 'Add', 'data', null, null, null, '0', '0', 'sys.manager.conf.add', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu22  */
INSERT INTO `sys_menu` VALUES ('6e1ba7c15e7e443a95f707372e8a5db0', 'd40b4b76256b49d08d271864a764df68', '0001000100050002', '修改参数', 'Edit', 'data', null, null, null, '0', '0', 'sys.manager.conf.edit', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu23 */
INSERT INTO `sys_menu` VALUES ('6d76b42c2ea648ac94052113706c369d', 'd40b4b76256b49d08d271864a764df68', '0001000100050003', '删除参数', 'Delete', 'data', null, null, null, '0', '0', 'sys.manager.conf.delete', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu24  */
INSERT INTO `sys_menu` VALUES ('611144b762c842d1a69b58378e9fc620', '620d11b448184980a9132ffcedcbad80', '000100010006', '日志管理', 'Log', 'menu', '/platform/sys/log', 'data-pjax', null, '1', '0', 'sys.manager.log', null, '15', '0', '', '1502677635', '0');
/* updateSysMenu25  */
INSERT INTO `sys_menu` VALUES ('f400f3386e734b61b5cb3435ff160c25', '611144b762c842d1a69b58378e9fc620', '0001000100060001', '清除日志', 'Delete', 'data', null, null, null, '0', '0', 'sys.manager.log.delete', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu26  */
INSERT INTO `sys_menu` VALUES ('9c87679cc16d4c46a2935392696f229a', '620d11b448184980a9132ffcedcbad80', '000100010007', '定时任务', 'Task', 'menu', '/platform/sys/task', 'data-pjax', null, '1', '0', 'sys.manager.task', null, '7', '0', '', '1502677635', '0');
/* updateSysMenu27  */
INSERT INTO `sys_menu` VALUES ('c6f57b31564a4b06a32116b83f92dd81', '9c87679cc16d4c46a2935392696f229a', '0001000100070001', '添加任务', 'Add', 'data', null, null, null, '0', '0', 'sys.manager.task.add', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu28  */
INSERT INTO `sys_menu` VALUES ('8075582d5a4c48548d72187628148b16', '9c87679cc16d4c46a2935392696f229a', '0001000100070002', '修改任务', 'Edit', 'data', null, null, null, '0', '0', 'sys.manager.task.edit', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu29  */
INSERT INTO `sys_menu` VALUES ('4e01e3113eb04aeda2a212444ed0324c', '9c87679cc16d4c46a2935392696f229a', '0001000100070003', '删除任务', 'Delete', 'data', null, null, null, '0', '0', 'sys.manager.task.delete', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu30  */
INSERT INTO `sys_menu` VALUES ('a11f774c05e64d13ad8992216922220c', '620d11b448184980a9132ffcedcbad80', '000100010008', '自定义路由', 'Route', 'menu', '/platform/sys/route', 'data-pjax', null, '1', '0', 'sys.manager.route', null, '3', '0', '', '1502677635', '0');
/* updateSysMenu31  */
INSERT INTO `sys_menu` VALUES ('eb9c58758c2947c6a89dee57b3842716', 'a11f774c05e64d13ad8992216922220c', '0001000100080001', '添加路由', 'Add', 'data', null, null, null, '0', '0', 'sys.manager.route.add', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu32  */
INSERT INTO `sys_menu` VALUES ('14450b5f87eb41228d3529a40914f0a4', 'a11f774c05e64d13ad8992216922220c', '0001000100080002', '修改路由', 'Edit', 'data', null, null, null, '0', '0', 'sys.manager.route.edit', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu33  */
INSERT INTO `sys_menu` VALUES ('5f82ccc618634794a9d968198df9463c', 'a11f774c05e64d13ad8992216922220c', '0001000100080003', '删除路由', 'Delete', 'data', null, null, null, '0', '0', 'sys.manager.route.delete', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu34  */
INSERT INTO `sys_menu` VALUES ('acbb1ae1c9dd48e19fb7c93bbfe12c1b', '620d11b448184980a9132ffcedcbad80', '000100010009', '应用管理', 'App', 'menu', '/platform/sys/api', 'data-pjax', null, '1', '0', 'sys.manager.api', null, '4', '0', '', '1502677635', '0');
/* updateSysMenu35  */
INSERT INTO `sys_menu` VALUES ('a125fa7902c642d48daba3027da5f4bd', 'acbb1ae1c9dd48e19fb7c93bbfe12c1b', '0001000100090001', '添加应用', 'Add', 'data', null, null, null, '0', '0', 'sys.manager.api.add', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu36  */
INSERT INTO `sys_menu` VALUES ('0ff6a94245624df18c252d5d03dc5764', 'acbb1ae1c9dd48e19fb7c93bbfe12c1b', '0001000100090002', '修改应用', 'Edit', 'data', null, null, null, '0', '0', 'sys.manager.api.edit', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu37  */
INSERT INTO `sys_menu` VALUES ('2e358e68a24342c29ee6f54265ccf30f', 'acbb1ae1c9dd48e19fb7c93bbfe12c1b', '0001000100090003', '删除应用', 'Delete', 'data', null, null, null, '0', '0', 'sys.manager.api.delete', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu38  */
INSERT INTO `sys_menu` VALUES ('288e813be70d4980b54e32f6bc8b2d53', '620d11b448184980a9132ffcedcbad80', '000100010010', '数据字典', 'Dict', 'menu', '/platform/sys/dict', 'data-pjax', null, '1', '0', 'sys.manager.dict', null, '5', '0', '', '1502677635', '0');
/* updateSysMenu39  */
INSERT INTO `sys_menu` VALUES ('83e56e0d10664ea5942cbee619ee425d', '288e813be70d4980b54e32f6bc8b2d53', '0001000100100001', '添加字典', 'Add', 'data', null, null, null, '0', '0', 'sys.manager.dict.add', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu40 */
INSERT INTO `sys_menu` VALUES ('3ceefdba12cc40eabe5b6375c6ce94fa', '288e813be70d4980b54e32f6bc8b2d53', '0001000100100002', '修改字典', 'Edit', 'data', null, null, null, '0', '0', 'sys.manager.dict.edit', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu41  */
INSERT INTO `sys_menu` VALUES ('7e2d7cee693645678412c44f1c36360b', '288e813be70d4980b54e32f6bc8b2d53', '0001000100100003', '删除字典', 'Delete', 'data', null, null, null, '0', '0', 'sys.manager.dict.delete', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu42  */
INSERT INTO `sys_menu` VALUES ('7eff5d73021a4002acab3aafb53f424c', '620d11b448184980a9132ffcedcbad80', '000100010011', '插件管理', 'Plugin', 'menu', '/platform/sys/plugin', 'data-pjax', null, '1', '0', 'sys.manager.plugin', null, '8', '0', '', '1502677635', '0');
/* updateSysMenu43  */
INSERT INTO `sys_menu` VALUES ('8287574a8cbe40908514688b658d2954', '7eff5d73021a4002acab3aafb53f424c', '0001000100110001', '添加插件', 'Add', 'data', null, null, null, '0', '0', 'sys.manager.plugin.add', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu44  */
INSERT INTO `sys_menu` VALUES ('11c42c19ef6440839f44a7dd11ad559d', '7eff5d73021a4002acab3aafb53f424c', '0001000100110002', '启用禁用', 'Update', 'data', null, null, null, '0', '0', 'sys.manager.plugin.update', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu45  */
INSERT INTO `sys_menu` VALUES ('4f1e8695e9b840a78a4e116b3376573d', '7eff5d73021a4002acab3aafb53f424c', '0001000100110003', '删除插件', 'Delete', 'data', null, null, null, '0', '0', 'sys.manager.plugin.delete', null, '0', '0', '', '1502677635', '0');
/* updateSysMenu46  */
INSERT INTO `sys_menu` VALUES ('a5b43a4ea59b48f79d23afb0a9a7469c', '', '0012', '系统管理', null, 'menu', 'sys', '', 'ti-settings', '1', '0', 'sys.manager_bak', null, '82', '1', '15729ed8e7d54060b09cb1105b34e562', '1507704196', '0');
/* updateSysMenu47  */
INSERT INTO `sys_menu` VALUES ('4e1e4a07b5de49b59b1e5aa862c92491', 'a5b43a4ea59b48f79d23afb0a9a7469c', '00120001', '单位管理', null, 'menu', '/platform/sys/unit', 'data-pjax', '', '1', '0', 'sys.manager_bak.unit', null, '83', '1', '15729ed8e7d54060b09cb1105b34e562', '1507706164', '0');
/* updateSysMenu48  */
INSERT INTO `sys_menu` VALUES ('ef334a88024e44918b52f9fa567c8bac', '4e1e4a07b5de49b59b1e5aa862c92491', '001200010001', '添加单位 ', null, 'data', '', '', '', '1', '0', 'sys.manager_bak.unit.add', null, '84', '0', '15729ed8e7d54060b09cb1105b34e562', '1507704249', '0');
/* updateSysMenu49  */
INSERT INTO `sys_menu` VALUES ('5ecd3dd5e3384fe687889c244c369524', '4e1e4a07b5de49b59b1e5aa862c92491', '001200010002', '修改单位 ', null, 'data', '', '', '', '1', '0', 'sys.manager_bak.unit.edit', null, '85', '0', '15729ed8e7d54060b09cb1105b34e562', '1507704307', '0');
/* updateSysMenu50  */
INSERT INTO `sys_menu` VALUES ('96b7706820cc45cf9c3ab0c6e2a16767', '4e1e4a07b5de49b59b1e5aa862c92491', '001200010003', '删除单位 ', null, 'data', '', '', '', '1', '0', 'sys.manager_bak.unit.delete', null, '86', '0', '15729ed8e7d54060b09cb1105b34e562', '1507704359', '0');
/* updateSysMenu51  */
INSERT INTO `sys_menu` VALUES ('fa8d2b15295341b3a7c79adaf0546216', 'a5b43a4ea59b48f79d23afb0a9a7469c', '00120002', ' 用户管理 ', null, 'menu', '/platform/sys/user', 'data-pjax', '', '1', '0', 'sys.manager_bak.user', null, '87', '1', '15729ed8e7d54060b09cb1105b34e562', '1507706183', '0');
/* updateSysMenu52 */
INSERT INTO `sys_menu` VALUES ('e423a65ff39349a7b2f85494a5deaf83', 'fa8d2b15295341b3a7c79adaf0546216', '001200020001', '添加用户 ', null, 'data', '', '', '', '1', '0', 'sys.manager_bak.user.add', null, '88', '0', '15729ed8e7d54060b09cb1105b34e562', '1507704492', '0');
/* updateSysMenu53*/
INSERT INTO `sys_menu` VALUES ('0619d8ee31c44cf1a5be257a451d5866', 'fa8d2b15295341b3a7c79adaf0546216', '001200020002', ' 修改用户', null, 'data', '', '', '', '1', '0', 'sys.manager_bak.user.edit', null, '89', '0', '15729ed8e7d54060b09cb1105b34e562', '1507704536', '0');
/* updateSysMenu54  */
INSERT INTO `sys_menu` VALUES ('2792d1cff5154b31b4a3b0c037bba093', 'fa8d2b15295341b3a7c79adaf0546216', '001200020003', '删除用户 ', null, 'data', '', '', '', '1', '0', 'sys.manager_bak.user.delete', null, '90', '0', '15729ed8e7d54060b09cb1105b34e562', '1507704570', '0');
/* updateSysMenu55  */
INSERT INTO `sys_menu` VALUES ('43a2c6333ace499585668402b829f176', 'a5b43a4ea59b48f79d23afb0a9a7469c', '00120006', '职工管理', null, 'menu', '/platform/sys/employee', 'data-pjax', '', '1', '0', 'sys.manager_bak.employee', null, '95', '0', '15729ed8e7d54060b09cb1105b34e562', '1507707594', '0');
/* updateSysMenu56  */
INSERT INTO `sys_menu` VALUES ('c966d0b0d9db49ae9c6c55741ae5806d', 'a5b43a4ea59b48f79d23afb0a9a7469c', '00120004', '角色管理 ', null, 'menu', '/platform/sys/role', 'data-pjax', '', '1', '0', 'sys.manager_bak.role', null, '92', '0', '15729ed8e7d54060b09cb1105b34e562', '1507706315', '0');
/* updateSysMenu57  */
INSERT INTO `sys_menu` VALUES ('00f03788a1ea4d01887305926240c9c6', 'a5b43a4ea59b48f79d23afb0a9a7469c', '00120005', '菜单管理 ', null, 'menu', '/platform/sys/menu', 'data-pjax', '', '1', '0', 'sys.manager_bak.menu', null, '93', '0', '15729ed8e7d54060b09cb1105b34e562', '1507706292', '0');

/* updateSysMenu59  */
INSERT INTO `sys_menu` VALUES ('298eb2fa23f142ad9f33b016ab37f210', '', '0004', '监控管理', null, 'menu', '', '', 'ti-desktop', '1', '0', 'monitor', null, '14', '1', '15729ed8e7d54060b09cb1105b34e562', '1503291955', '0');
/* updateSysMenu60  */
INSERT INTO `sys_menu` VALUES ('594736fbf5fc48098591ffbd654a2d00', '298eb2fa23f142ad9f33b016ab37f210', '00040001', '设备绑定', null, 'menu', '/instrument/monitor/collect', 'data-pjax', 'ti-wand', '1', '0', 'instrument.monitor.collect', null, '25', '1', '15729ed8e7d54060b09cb1105b34e562', '1505700453', '0');
/* updateSysMenu61  */
INSERT INTO `sys_menu` VALUES ('c38c4d7b5cbc41f7b6db8b3381029f0d', '298eb2fa23f142ad9f33b016ab37f210', '00040002', '收集器管理', null, 'menu', '/instrument/monitor/gateway', 'data-pjax', 'ti-signal', '1', '0', 'instrument.monitor.gateway', null, '21', '1', '15729ed8e7d54060b09cb1105b34e562', '1506042729', '0');
/* updateSysMenu62 */
INSERT INTO `sys_menu` VALUES ('6c432e32b84b46488d220595ce46361d', '594736fbf5fc48098591ffbd654a2d00', '000400010001', '添加采集器', null, 'data', '', '', '', '1', '0', 'instrument.monitor.collect.add', null, '26', '0', '15729ed8e7d54060b09cb1105b34e562', '1502679136', '0');
/* updateSysMenu63 */
INSERT INTO `sys_menu` VALUES ('bfccba8e71e644beb36442beacc99db5', '594736fbf5fc48098591ffbd654a2d00', '000400010002', '修改采集器', null, 'data', '', '', '', '1', '0', 'instrument.monitor.collect.edit', null, '27', '0', '15729ed8e7d54060b09cb1105b34e562', '1502679146', '0');
/* updateSysMenu64  */
INSERT INTO `sys_menu` VALUES ('994fa1ab8fea46409f89435b5dc26a52', '594736fbf5fc48098591ffbd654a2d00', '000400010003', '删除采集器', null, 'data', '', '', '', '1', '0', 'instrument.monitor.collect.delete', null, '28', '0', '15729ed8e7d54060b09cb1105b34e562', '1502679157', '0');
/* updateSysMenu65 */
INSERT INTO `sys_menu` VALUES ('20106303852a4201b40729e81d01d34e', 'c38c4d7b5cbc41f7b6db8b3381029f0d', '000400020001', '添加网关', null, 'data', '', '', '', '1', '0', 'instrument.monitor.gateway.add', null, '22', '0', '15729ed8e7d54060b09cb1105b34e562', '1502938212', '0');
/* updateSysMenu66  */
INSERT INTO `sys_menu` VALUES ('a7cb8be665544ac79c981d002b7bf0bc', 'c38c4d7b5cbc41f7b6db8b3381029f0d', '000400020002', '修改网关', null, 'data', '', '', '', '1', '0', 'instrument.monitor.gateway.edit', null, '23', '0', '15729ed8e7d54060b09cb1105b34e562', '1502679301', '0');
/* updateSysMenu67  */
INSERT INTO `sys_menu` VALUES ('6658ceecf93545c18fe4e900054c0e95', 'c38c4d7b5cbc41f7b6db8b3381029f0d', '000400020003', '删除网关', null, 'data', '', '', '', '1', '0', 'instrument.monitor.gateway.delete', null, '24', '0', '15729ed8e7d54060b09cb1105b34e562', '1502679310', '0');
/* updateSysMenu68  */
INSERT INTO `sys_menu` VALUES ('d01804ba4e5d496e9dd9932522b9e763', '', '0005', '资产管理', null, 'menu', '/asset/', 'data-pjax', 'ti-credit-card', '1', '0', 'asset.auth', null, '45', '1', '15729ed8e7d54060b09cb1105b34e562', '1503292289', '0');
/* updateSysMenu69  */
INSERT INTO `sys_menu` VALUES ('011bfb74a8fc48fbb0c15df42dcb2163', 'd01804ba4e5d496e9dd9932522b9e763', '00050001', '资产信息', null, 'menu', '/asset/info', 'data-pjax', 'ti-bag', '1', '0', 'asset.auth.info', null, '46', '1', '15729ed8e7d54060b09cb1105b34e562', '1503292677', '0');
/* updateSysMenu70  */
INSERT INTO `sys_menu` VALUES ('f1ca7220137b4da394862ab97a61ecfd', 'd01804ba4e5d496e9dd9932522b9e763', '00050002', '规格型号', null, 'menu', '/asset/rule', 'data-pjax', 'ti-ruler-alt', '1', '0', 'asset.auth.rule', null, '57', '1', '15729ed8e7d54060b09cb1105b34e562', '1503292718', '0');
/* updateSysMenu71  */
INSERT INTO `sys_menu` VALUES ('c6a3316af4e64dacb1a5cb02b9ffc5ba', 'd01804ba4e5d496e9dd9932522b9e763', '00050003', '技改项目', null, 'menu', '/instrument/monitor/jgprojectinfo', 'data-pjax', 'ti-palette', '1', '0', 'instrument.monitor.jgprojectinfo', null, '61', '1', '15729ed8e7d54060b09cb1105b34e562', '1505798838', '0');
/* updateSysMenu72  */
INSERT INTO `sys_menu` VALUES ('43b9cc809aab4d07a2105fe7a2b4dc66', 'd01804ba4e5d496e9dd9932522b9e763', '00050004', '月度报告', null, 'menu', '/asset/report', 'data-pjax', 'ti-timer', '1', '0', 'asset.auth.report', null, '63', '0', '15729ed8e7d54060b09cb1105b34e562', '1506299935', '0');
/* updateSysMenu73  */
INSERT INTO `sys_menu` VALUES ('7eafe8fa99fc43ceac6169fef9284dca', '298eb2fa23f142ad9f33b016ab37f210', '00040003', '设备监控', null, 'menu', '/instrument/monitor/device', 'data-pjax', 'ti-save-alt', '1', '0', 'instrument.monitor.device', null, '29', '1', '15729ed8e7d54060b09cb1105b34e562', '1505372983', '0');
/* updateSysMenu74  */
INSERT INTO `sys_menu` VALUES ('cb910e6eaa80489f99c0469c5f079617', '7eafe8fa99fc43ceac6169fef9284dca', '000400030001', '添加设备', null, 'data', '', '', '', '1', '0', 'instrument.monitor.device.add', null, '30', '0', '15729ed8e7d54060b09cb1105b34e562', '1503017910', '0');
/* updateSysMenu75  */
INSERT INTO `sys_menu` VALUES ('02c53f666b384798a4e1f93d8d8b001d', '7eafe8fa99fc43ceac6169fef9284dca', '000400030002', '修改设备信息', null, 'data', '', '', '', '1', '0', 'instrument.monitor.device.edit', null, '31', '0', '15729ed8e7d54060b09cb1105b34e562', '1502867023', '0');
/* updateSysMenu76  */
INSERT INTO `sys_menu` VALUES ('ece550bef898476c8850830f4b0a54a5', '7eafe8fa99fc43ceac6169fef9284dca', '000400030003', '删除设备', null, 'data', '', '', '', '1', '0', 'instrument.monitor.device.delete', null, '32', '0', '15729ed8e7d54060b09cb1105b34e562', '1502867035', '0');
/* updateSysMenu77  */
INSERT INTO `sys_menu` VALUES ('1fe78e306edd4f51a2675f9610cd9af0', '', '0006', '使用情况', null, 'menu', '', 'data-pjax', 'ti-zoom-in', '1', '0', 'spend', null, '34', '1', '15729ed8e7d54060b09cb1105b34e562', '1503292348', '0');
/* updateSysMenu78  */
INSERT INTO `sys_menu` VALUES ('5d9ade6043ad4f83befcf275da6a6e45', '298eb2fa23f142ad9f33b016ab37f210', '00040004', '设备履历', null, 'menu', '/instrument/monitor/switchingFlow', 'data-pjax', 'ti-bookmark-alt', '1', '0', 'instrument.monitor.switch', null, '33', '0', '15729ed8e7d54060b09cb1105b34e562', '1505372851', '0');
/* updateSysMenu79  */
INSERT INTO `sys_menu` VALUES ('360aa1b6f8734f0d969b27127919bdb4', 'afa48b4ae412403f88ba1defd244820f', '000600010003', '删除项目', null, 'data', '', '', '', '1', '0', 'instrument.monitor.projectinfo.delete', null, '38', '0', '15729ed8e7d54060b09cb1105b34e562', '1503276716', '0');
/* updateSysMenu80 */
INSERT INTO `sys_menu` VALUES ('af1d06ffae1d4794bef484fea1e6834a', '8e630fdfe6644b3f81532945461e5002', '00070001', '配置信息', null, 'menu', '/yun/configure/yungateway', 'data-pjax', 'ti-settings', '1', '0', 'yun.configure.yungateway', null, '41', '1', '15729ed8e7d54060b09cb1105b34e562', '1503292412', '0');
/* updateSysMenu81  */
INSERT INTO `sys_menu` VALUES ('013bec6bb7e24179908ba90be2e3e1dc', 'af1d06ffae1d4794bef484fea1e6834a', '000700010001', '添加云网', null, 'data', '', '', '', '1', '0', 'yun.configure.yungateway.add', null, '42', '0', '15729ed8e7d54060b09cb1105b34e562', '1503280628', '0');
/* updateSysMenu82  */
INSERT INTO `sys_menu` VALUES ('8e630fdfe6644b3f81532945461e5002', '', '0007', '云网管理', null, 'menu', '', 'data-pjax', 'ti-rss-alt', '1', '0', 'yun', null, '40', '1', '15729ed8e7d54060b09cb1105b34e562', '1503292362', '0');
/* updateSysMenu83  */
INSERT INTO `sys_menu` VALUES ('61b7a0e562a44a6b8cf350e4493d54d2', '620d11b448184980a9132ffcedcbad80', '000100010012', '职工管理', null, 'menu', '/platform/sys/employee', 'data-pjax', '', '1', '0', 'sys.manager.employee', null, '9', '0', '15729ed8e7d54060b09cb1105b34e562', '1503276434', '0');
/* updateSysMenu84  */
INSERT INTO `sys_menu` VALUES ('9c0cb1e43d77436d8b8d04e50d3ca9c5', 'afa48b4ae412403f88ba1defd244820f', '000600010002', '编辑项目', null, 'data', '', '', '', '1', '0', 'instrument.monitor.projectinfo.edit', null, '37', '0', '15729ed8e7d54060b09cb1105b34e562', '1503276679', '0');
/* updateSysMenu85  */
INSERT INTO `sys_menu` VALUES ('65331a99679642b18b47df7623e877b5', 'afa48b4ae412403f88ba1defd244820f', '000600010001', '添加项目', null, 'data', '', '', '', '1', '0', 'instrument.monitor.projectinfo.add', null, '36', '0', '15729ed8e7d54060b09cb1105b34e562', '1503276622', '0');
/* updateSysMenu86  */
INSERT INTO `sys_menu` VALUES ('afa48b4ae412403f88ba1defd244820f', '1fe78e306edd4f51a2675f9610cd9af0', '00060001', '项目管理', null, 'menu', '/instrument/monitor/projectinfo', 'data-pjax', 'ti-layers-alt', '1', '0', 'instrument.monitor.projectinfo', null, '35', '1', '15729ed8e7d54060b09cb1105b34e562', '1505701644', '0');
/* updateSysMenu87  */
INSERT INTO `sys_menu` VALUES ('7032eac97f8b4ae08c453ab63aec61e5', 'af1d06ffae1d4794bef484fea1e6834a', '000700010002', '编辑云网', null, 'data', '', '', '', '1', '0', 'yun.configure.yungateway.edit', null, '43', '0', '15729ed8e7d54060b09cb1105b34e562', '1503280637', '0');
/* updateSysMenu88  */
INSERT INTO `sys_menu` VALUES ('6f91cef2d6274974b202cc84638d3dad', 'af1d06ffae1d4794bef484fea1e6834a', '000700010003', '删除云网', null, 'data', '', '', '', '1', '0', 'yun.configure.yungateway.delete', null, '44', '0', '15729ed8e7d54060b09cb1105b34e562', '1503280645', '0');
/* updateSysMenu89 */
INSERT INTO `sys_menu` VALUES ('2a7a6d7875004ecb979d1763f01797f8', '1fe78e306edd4f51a2675f9610cd9af0', '00060002', '设备状况', null, 'menu', '/instrument/monitor/devicefacility ', 'data-pjax', 'ti-ruler-alt', '1', '0', 'instrument.monitor.devicefacility ', null, '39', '0', '15729ed8e7d54060b09cb1105b34e562', '1503394213', '0');
/* updateSysMenu90  */
INSERT INTO `sys_menu` VALUES ('72412003266c4b93998c42ecdd148687', 'd01804ba4e5d496e9dd9932522b9e763', '00050005', '仪器室设备借调记录', null, 'menu', '/asset/recode', 'data-pjax', 'ti-credit-card', '1', '1', 'asset.auth.recode', null, '66', '1', '15729ed8e7d54060b09cb1105b34e562', '1503475150', '0');
/* updateSysMenu91  */
INSERT INTO `sys_menu` VALUES ('7b710f4e95b74e82bbf860c3a0a3f3b3', 'f1ca7220137b4da394862ab97a61ecfd', '000500020001', '添加型号', null, 'data', '', '', '', '1', '0', 'asset.auth.rule.add', null, '58', '0', '15729ed8e7d54060b09cb1105b34e562', '1503475730', '0');
/* updateSysMenu92  */
INSERT INTO `sys_menu` VALUES ('50f048e5ec1c4947bb97cc2168599b26', '011bfb74a8fc48fbb0c15df42dcb2163', '000500010001', '添加资产信息', null, 'data', '', '', '', '1', '0', 'asset.auth.info.add', null, '47', '0', '15729ed8e7d54060b09cb1105b34e562', '1503538504', '0');
/* updateSysMenu93  */
INSERT INTO `sys_menu` VALUES ('3061b2b4b64c4bf68c7ae9e7591d179d', '011bfb74a8fc48fbb0c15df42dcb2163', '000500010002', '编辑资产信息', null, 'data', '', '', '', '1', '0', 'asset.auth.info.edit', null, '48', '0', '15729ed8e7d54060b09cb1105b34e562', '1503538575', '0');
/* updateSysMenu94  */
INSERT INTO `sys_menu` VALUES ('a7e82c75b0ea48e2a3d26e81bea706d9', '011bfb74a8fc48fbb0c15df42dcb2163', '000500010003', '删除资产信息', null, 'data', '', '', '', '1', '0', 'asset.auth.info.delete', null, '49', '0', '15729ed8e7d54060b09cb1105b34e562', '1503542849', '0');
/* updateSysMenu95  */
INSERT INTO `sys_menu` VALUES ('1454475863b9458592cf53a41c9465d3', 'd01804ba4e5d496e9dd9932522b9e763', '00050006', '周期检定', null, 'menu', '/cycle/examine', 'data-pjax', '', '1', '0', 'cycle.examine', null, '64', '1', '15729ed8e7d54060b09cb1105b34e562', '1504056823', '0');
/* updateSysMenu96  */
INSERT INTO `sys_menu` VALUES ('05159a94d1d947378823ba62e0b4794d', 'e107971c77b548a6a2f5d0b3d134ec31', '0009000200010001', '维修审批', null, 'menu', '/repair/approve?type=1&opType=0', 'data-pjax', 'ti-slice', '1', '0', 'repair.manager.approve', null, '0', '0', '15729ed8e7d54060b09cb1105b34e562', '1504082339', '0');
/* updateSysMenu97 */
INSERT INTO `sys_menu` VALUES ('7691cce60aec491bb731d89f2643e223', '72412003266c4b93998c42ecdd148687', '000500050001', '添加记录', null, 'data', '', '', '', '1', '0', 'asset.auth.recode.add', null, '67', '0', '15729ed8e7d54060b09cb1105b34e562', '1503544537', '0');
/* updateSysMenu98 */
INSERT INTO `sys_menu` VALUES ('b8202b346b394259a8fd4e8a7792c08a', 'f1ca7220137b4da394862ab97a61ecfd', '000500020002', '删除型号', null, 'data', '', '', '', '1', '0', 'asset.auth.rule.delete', null, '59', '0', '15729ed8e7d54060b09cb1105b34e562', '1503554217', '0');
/* updateSysMenu99  */
INSERT INTO `sys_menu` VALUES ('3f56e388c9b14264830c3daa21f29f2b', 'f1ca7220137b4da394862ab97a61ecfd', '000500020003', '修改型号', null, 'data', '', '', '', '1', '0', 'asset.auth.rule.edit', null, '60', '0', '15729ed8e7d54060b09cb1105b34e562', '1503554282', '0');
/* updateSysMenu100  */
INSERT INTO `sys_menu` VALUES ('94657258b6414e51b1e9b7ca16e63fd7', '72412003266c4b93998c42ecdd148687', '000500050002', '修改记录', null, 'data', '', '', '', '1', '0', 'asset.auth.recode.edit', null, '68', '0', '15729ed8e7d54060b09cb1105b34e562', '1503624210', '0');
/* updateSysMenu101  */
INSERT INTO `sys_menu` VALUES ('878a6f60bae44c22a4e06cc163893546', '72412003266c4b93998c42ecdd148687', '000500050003', '删除记录', null, 'data', '', '', '', '1', '0', 'asset.auth.recode.delete', null, '69', '0', '15729ed8e7d54060b09cb1105b34e562', '1503564973', '0');
/* updateSysMenu102 */
INSERT INTO `sys_menu` VALUES ('43341d8ec86a492b9fda554e29eae275', '1454475863b9458592cf53a41c9465d3', '000500060001', '检定', null, 'data', '', '', '', '1', '0', 'cycle.examine.test', null, '65', '0', '15729ed8e7d54060b09cb1105b34e562', '1504056879', '0');
/* updateSysMenu103  */
INSERT INTO `sys_menu` VALUES ('25db0a3d2ca04409a54833443568e828', '', '0009', '维修管理', null, 'menu', ' ', '', 'ti-hummer', '1', '0', 'repair.manager', null, '70', '1', '15729ed8e7d54060b09cb1105b34e562', '1505196618', '0');
/* updateSysMenu104  */
INSERT INTO `sys_menu` VALUES ('2f5e4ce5d23643c899c96ca354a4a776', '25db0a3d2ca04409a54833443568e828', '00090001', '维修申请', null, 'menu', '/repair/add', 'data-pjax', 'ti-notepad', '1', '0', 'repair.manager.add', null, '71', '0', '15729ed8e7d54060b09cb1105b34e562', '1503974480', '0');
/* updateSysMenu105  */
INSERT INTO `sys_menu` VALUES ('5bae14b717594eaa86b085ba4cb265c2', '25db0a3d2ca04409a54833443568e828', '00090002', '处理中', null, 'menu', ' #', 'data-pjax', 'ti-car', '1', '0', 'repair.manager.processing', null, '72', '1', '15729ed8e7d54060b09cb1105b34e562', '1503974555', '0');
/* updateSysMenu106  */
INSERT INTO `sys_menu` VALUES ('e107971c77b548a6a2f5d0b3d134ec31', '5bae14b717594eaa86b085ba4cb265c2', '000900020001', '设备', null, 'menu', '#', 'data-pjax', 'ti-layout-slider-alt', '1', '0', 'repair.manager.device', null, '73', '1', '15729ed8e7d54060b09cb1105b34e562', '1503975320', '0');
/* updateSysMenu107  */
INSERT INTO `sys_menu` VALUES ('b8a1da5877e14c199df5840dbe1f0bec', '5bae14b717594eaa86b085ba4cb265c2', '000900020002', '仪器', null, 'menu', '#', 'data-pjax', 'ti-layout-media-center-alt', '1', '0', 'repair.manager.asset', null, '74', '1', '15729ed8e7d54060b09cb1105b34e562', '1504062465', '0');
/* updateSysMenu108  */
INSERT INTO `sys_menu` VALUES ('47ca526b8143492ea446045a80a24c2e', 'e107971c77b548a6a2f5d0b3d134ec31', '0009000200010002', '等待维修', null, 'menu', '/repair/approve?type=1&opType=1', 'data-pjax', 'ti-files', '1', '0', 'repair.manager.repairwait', null, '0', '0', '15729ed8e7d54060b09cb1105b34e562', '1504082416', '0');
/* updateSysMenu109  */
INSERT INTO `sys_menu` VALUES ('60336340548045eb9cd674eef8f42d09', 'e107971c77b548a6a2f5d0b3d134ec31', '0009000200010003', '维修中', null, 'menu', '/repair/approve?type=1&opType=2', 'data-pjax', 'ti-paint-roller', '1', '0', 'repair.manager.repairing', null, '0', '0', '15729ed8e7d54060b09cb1105b34e562', '1504072580', '0');
/* updateSysMenu110 */
INSERT INTO `sys_menu` VALUES ('0ea2fab3fa1941fb930f3bc17197e271', '25db0a3d2ca04409a54833443568e828', '00090003', '维修记录', null, 'menu', '/repair/approve?view=1', 'data-pjax', 'ti-search', '1', '0', 'repair.manager.videw', null, '76', '0', '15729ed8e7d54060b09cb1105b34e562', '1504067988', '0');
/* updateSysMenu111  */
INSERT INTO `sys_menu` VALUES ('8b56bac268b148c088f9da85bf7c7092', 'e107971c77b548a6a2f5d0b3d134ec31', '0009000200010004', '待验收', null, 'menu', '/repair/approve?type=1&opType=3', 'data-pjax', 'ti-face-smile', '1', '0', 'repair.manager.check', null, '0', '0', '15729ed8e7d54060b09cb1105b34e562', '1504072708', '0');
/* updateSysMenu112  */
INSERT INTO `sys_menu` VALUES ('3306324e64284dd98c027912f9d57b8c', 'b8a1da5877e14c199df5840dbe1f0bec', '0009000200020001', '维修审批', null, 'menu', '/repair/approve?type=2&opType=0', 'data-pjax', 'ti-slice', '1', '0', 'repair.manager.approve2', null, '0', '0', '15729ed8e7d54060b09cb1105b34e562', '1504082354', '0');
/* updateSysMenu113  */
INSERT INTO `sys_menu` VALUES ('75d2a796e32a4046b6ed0ea493f5da0c', 'b8a1da5877e14c199df5840dbe1f0bec', '0009000200020002', '等待维修', null, 'menu', '/repair/approve?type=2&opType=1', 'data-pjax', 'ti-files', '1', '0', 'repair.manager.repairwait2', null, '0', '0', '15729ed8e7d54060b09cb1105b34e562', '1504082452', '0');
/* updateSysMenu114  */
INSERT INTO `sys_menu` VALUES ('23bd718564d54abe86ac9c896e6e3784', 'b8a1da5877e14c199df5840dbe1f0bec', '0009000200020003', '维修中', null, 'menu', '/repair/approve?type=2&opType=2', 'data-pjax', '', '1', '0', 'repair.manager.repairing2', null, '0', '0', '15729ed8e7d54060b09cb1105b34e562', '1504073835', '0');
/* updateSysMenu115  */
INSERT INTO `sys_menu` VALUES ('812201028e87438f96f38f4b27ce0e10', 'b8a1da5877e14c199df5840dbe1f0bec', '0009000200020004', '计量中', null, 'menu', '/repair/approve?type=2&opType=4', 'data-pjax', '', '1', '0', 'repair.manager.metering', null, '0', '0', '15729ed8e7d54060b09cb1105b34e562', '1504074061', '0');
/* updateSysMenu116  */
INSERT INTO `sys_menu` VALUES ('6161796582cc4665aa92bf3bb2502dd9', 'b8a1da5877e14c199df5840dbe1f0bec', '0009000200020005', '待领取', null, 'menu', '/repair/approve?type=2&opType=5', 'data-pjax', '', '1', '0', 'repair.manager.receive', null, '0', '0', '15729ed8e7d54060b09cb1105b34e562', '1504074141', '0');
/* updateSysMenu117  */
INSERT INTO `sys_menu` VALUES ('37071dcf523c4c15be2ce3d651d2b431', '25db0a3d2ca04409a54833443568e828', '00090004', '完结', null, 'menu', '/repair/approve?opType=8&view=1', 'data-pjax', 'ti-face-smile', '1', '1', 'repair.manager.finish', null, '75', '0', '4205786fa5284d15af7575a6b1705a3a', '1504078289', '0');
/* updateSysMenu118  */
INSERT INTO `sys_menu` VALUES ('137e3c5270a548c481c30702afd8964c', '011bfb74a8fc48fbb0c15df42dcb2163', '000500010005', '跳转归还', null, 'data', '', '', '', '1', '0', 'asset.auth.recode.return', null, '51', '0', '15729ed8e7d54060b09cb1105b34e562', '1505786551', '0');
/* updateSysMenu119  */
INSERT INTO `sys_menu` VALUES ('80526ea52cbe4db3b3b2f18583c92453', '011bfb74a8fc48fbb0c15df42dcb2163', '000500010004', '查询仪器借调记录', null, 'data', '', '', '', '1', '0', 'asset.auth.recode.show', null, '50', '0', '15729ed8e7d54060b09cb1105b34e562', '1504076412', '0');
/* updateSysMenu120 */
INSERT INTO `sys_menu` VALUES ('6ac76e941c6240c4aacab6b076a37a77', '', '0010', '科室借调', null, 'menu', '#', 'data-pjax', 'ti-wallet', '1', '0', 'asset.unit', null, '77', '1', '15729ed8e7d54060b09cb1105b34e562', '1505903622', '0');
/* updateSysMenu121  */
INSERT INTO `sys_menu` VALUES ('c535497b65a64552862521e4ae939d3f', '6ac76e941c6240c4aacab6b076a37a77', '00100001', '资产信息', null, 'menu', '/assetUnit/index', 'data-pjax', 'ti-bag', '1', '0', 'asset.unit.info', null, '78', '0', '15729ed8e7d54060b09cb1105b34e562', '1504233409', '0');
/* updateSysMenu122  */
INSERT INTO `sys_menu` VALUES ('4d486a49633a41459e70f4dfffbaa4dd', '6ac76e941c6240c4aacab6b076a37a77', '00100002', '审批', null, 'menu', '/assetUnit/approveHtml', 'data-pjax', 'ti-marker-alt', '1', '0', 'asset.unit.approve', null, '79', '0', '15729ed8e7d54060b09cb1105b34e562', '1504490236', '0');
/* updateSysMenu123  */
INSERT INTO `sys_menu` VALUES ('a096da5b9338417e809341d645813283', '6ac76e941c6240c4aacab6b076a37a77', '00100003', '申请记录', null, 'menu', '/assetUnit/applyRecordHtml', 'data-pjax', 'ti-book', '1', '0', 'asset.unit.applyRecord', null, '80', '0', '15729ed8e7d54060b09cb1105b34e562', '1504514663', '0');
/* updateSysMenu124  */
INSERT INTO `sys_menu` VALUES ('12241d73d7e2472a93abb5316a365fa6', '6ac76e941c6240c4aacab6b076a37a77', '00100004', '审批记录', null, 'menu', '/assetUnit/approveRecordHtml', 'data-pjax', ' ti-write ', '1', '0', 'asset.unit.approveRecord', null, '81', '0', '15729ed8e7d54060b09cb1105b34e562', '1504514682', '0');
/* updateSysMenu125  */
INSERT INTO `sys_menu` VALUES ('7b923231dddc492eae00542b28e94817', '011bfb74a8fc48fbb0c15df42dcb2163', '000500010006', '封存', null, 'data', '', '', '', '1', '0', 'asset.auth.sealed', null, '52', '0', '15729ed8e7d54060b09cb1105b34e562', '1504597520', '0');
/* updateSysMenu126  */
INSERT INTO `sys_menu` VALUES ('ac3b9a6d7ac546968c4f198e6b95ea32', '011bfb74a8fc48fbb0c15df42dcb2163', '000500010007', '解封', null, 'data', '', '', '', '1', '0', 'asset.auth.unsealed', null, '53', '0', '15729ed8e7d54060b09cb1105b34e562', '1504601635', '0');
/* updateSysMenu126  */
INSERT INTO `sys_menu` VALUES ('73aaeb8d751d4240b6f4716207cf9b9c', '011bfb74a8fc48fbb0c15df42dcb2163', '000500010008', '导出资产信息', null, 'data', '', '', '', '1', '0', 'asset.auth.recode.export', null, '54', '0', '15729ed8e7d54060b09cb1105b34e562', '1504610492', '0');
/* updateSysMenu128  */
INSERT INTO `sys_menu` VALUES ('6ea5607687b0477baa514ace8d70426f', '011bfb74a8fc48fbb0c15df42dcb2163', '000500010009', '报废', null, 'data', '', '', '', '1', '0', 'asset.auth.scrap', null, '55', '0', '15729ed8e7d54060b09cb1105b34e562', '1504768642', '0');
/* updateSysMenu129  */
INSERT INTO `sys_menu` VALUES ('104146cd371c484d9ef9a850034bf5f8', '011bfb74a8fc48fbb0c15df42dcb2163', '000500010010', '确认报废', null, 'data', '', '', '', '1', '0', 'asset.auth.confirmScrap', null, '56', '0', '15729ed8e7d54060b09cb1105b34e562', '1504768655', '0');
/* updateSysMenu130  */
INSERT INTO `sys_menu` VALUES ('9a87def724ab44b283d6112cd72a4352', '', '0011', '综合监控', null, 'menu', '/platform/account', 'data-pjax', 'ti-menu-alt', '1', '1', 'sys.manager.account', null, '0', '0', '15729ed8e7d54060b09cb1105b34e562', '1507604418', '0');
/* updateSysMenu131 */
INSERT INTO `sys_menu` VALUES ('13cf4b29d3064a9e9b6692e4106de187', '298eb2fa23f142ad9f33b016ab37f210', '00040005', '采集器管理', null, 'menu', '/instrument/monitor/bCard', 'data-pjax', 'ti-map', '1', '0', 'instrument.monitor.bCard', null, '16', '1', '15729ed8e7d54060b09cb1105b34e562', '1508206555', '0');
/* updateSysMenu132  */
INSERT INTO `sys_menu` VALUES ('6367d76088414799ba9534bba2fbd418', '13cf4b29d3064a9e9b6692e4106de187', '000400050001', '导入', null, 'data', '', '', '', '1', '0', 'instrument.monitor.bCard.importExcel', null, '17', '0', '15729ed8e7d54060b09cb1105b34e562', '1505701214', '0');
/* updateSysMenu133  */
INSERT INTO `sys_menu` VALUES ('91191e9fcb8a4c6aa87357dfd31983ab', '13cf4b29d3064a9e9b6692e4106de187', '000400050002', '删除', null, 'data', '', '', '', '1', '0', 'instrument.monitor.bCard.del', null, '18', '0', '15729ed8e7d54060b09cb1105b34e562', '1505701380', '0');
/* updateSysMenu134  */
INSERT INTO `sys_menu` VALUES ('22dcf2f3b1a84271b345613c2b7e6713', '13cf4b29d3064a9e9b6692e4106de187', '000400050003', '修改', null, 'data', '', '', '', '1', '0', 'instrument.monitor.bCard.modify', null, '19', '0', '15729ed8e7d54060b09cb1105b34e562', '1505701537', '0');
/* updateSysMenu135  */
INSERT INTO `sys_menu` VALUES ('c3f3917d7f814ddeac925ea2b4bbf2c1', 'c6a3316af4e64dacb1a5cb02b9ffc5ba', '000500030001', '添加技改项目', null, 'data', '', '', '', '1', '0', 'instrument.monitor.jgprojectinfo.add', null, '62', '0', '15729ed8e7d54060b09cb1105b34e562', '1505800983', '0');
/* updateSysMenu136  */
INSERT INTO `sys_menu` VALUES ('4900ff5657cb44fc866409973858b844', '13cf4b29d3064a9e9b6692e4106de187', '000400050004', '增加', null, 'data', '', '', '', '1', '0', 'instrument.monitor.bCard.add', null, '20', '0', '15729ed8e7d54060b09cb1105b34e562', '1505975122', '0');
