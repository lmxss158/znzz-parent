package com.znzz.app.web.commons.core;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.FileSqlManager;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.Daos;
import org.nutz.el.opt.custom.CustomMake;
import org.nutz.integration.jedis.JedisAgent;
import org.nutz.integration.jedis.RedisService;
import org.nutz.integration.quartz.QuartzJob;
import org.nutz.integration.quartz.QuartzManager;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.json.Json;
import org.nutz.lang.Encoding;
import org.nutz.lang.Files;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.NutConfig;
import org.quartz.Scheduler;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.znzz.app.instrument.modules.models.Ins_DeviceFacilityHour;
import com.znzz.app.sys.modules.models.Sys_config;
import com.znzz.app.sys.modules.models.Sys_dict;
import com.znzz.app.sys.modules.models.Sys_plugin;
import com.znzz.app.sys.modules.models.Sys_role;
import com.znzz.app.sys.modules.models.Sys_route;
import com.znzz.app.sys.modules.models.Sys_task;
import com.znzz.app.sys.modules.models.Sys_unit;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.app.sys.modules.services.impl.SysDictServiceImpl;
import com.znzz.app.web.commons.base.Globals;
import com.znzz.app.web.commons.plugin.IPlugin;
import com.znzz.app.web.commons.plugin.PluginMaster;
import com.znzz.framework.ig.RedisIdGenerator;

import net.sf.ehcache.CacheManager;
import redis.clients.jedis.Jedis;

/**
 * Created by wizzer on 2016/6/21.
 */
public class Setup implements org.nutz.mvc.Setup {
    private static final Log log = Logs.get();
    public void init(NutConfig config) {
        try {
            // 环境检查
            if (!Charset.defaultCharset().name().equalsIgnoreCase(Encoding.UTF8)) {
                log.warn("This project must run in UTF-8, pls add -Dfile.encoding=UTF-8 to JAVA_OPTS");
            }
            Ioc ioc = config.getIoc();
            Dao dao = ioc.get(Dao.class);
            // 初始化redis实现的id生成器
            CustomMake.me().register("ig", ioc.get(RedisIdGenerator.class));
            // 初始化数据表
            initSysData(config, dao);
            // 初始化系统变量
            initSysSetting(config, dao);
            // 初始化定时任务
            initSysTask(config, dao);
            // 初始化自定义路由
            initSysRoute(config, dao);
            // 初始化热插拔插件
            initSysPlugin(config, dao);
            // 初始化redis相关key
            initRedisKey(ioc) ;
            // 初始化rabbitmq
            //initRabbit(config, dao);
            // 初始化数据字典
            initSysDict(ioc,dao);
            // 初始化ig缓存
            //initRedisIg(ioc.get(JedisAgent.class), ioc.get(PropertiesProxy.class, "conf"), dao);
            // 检查一下Ehcache CacheManager 是否正常
            CacheManager cacheManager = ioc.get(CacheManager.class);
            log.debug("Ehcache CacheManager = " + cacheManager);
            log.info("启动成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


	/**
     * 当项目启动的时候把表主键加载到redis缓存中
     */
    private void initRedisIg(JedisAgent jedisAgent, PropertiesProxy conf, Dao dao) {
        long a = System.currentTimeMillis();
        try (Jedis jedis = jedisAgent.getResource()) {
            Sql sql;
            if ("mysql".equalsIgnoreCase(dao.getJdbcExpert().getDatabaseType())) {
                sql = Sqls.create("SELECT table_name FROM information_schema.columns WHERE table_schema='" + conf.get("db.name", "") + "' AND column_name='id'");
            } else {
                //oracle mssql该怎么写呢,等你来添加...
                log.info("wait for you ...");
                return;
            }
            sql.setCallback(Sqls.callback.strs());
            dao.execute(sql);
            List<String> tableNameList = sql.getList(String.class);
            for (String tableName : tableNameList) {
                List<Record> list = dao.query(tableName, Cnd.NEW().desc("id"), new Pager().setPageSize(1).setPageNumber(1));
                if (list.size() > 0) {
                    String id = list.get(0).getString("id");
                    if (Strings.isMatch(Pattern.compile("^.*[\\d]{16}$"), id)) {
                        String ym = id.substring(id.length() - 16, id.length() - 10);
                        if (Strings.isBlank(jedis.get("ig:" + tableName.toUpperCase() + ym))) {
                            jedis.set("ig:" + tableName.toUpperCase() + ym, String.valueOf(NumberUtils.toLong(id.substring(id.length() - 10, id.length()), 1)));
                        }
                    }
                }
            }
        }
        long b = System.currentTimeMillis();
        log.info("init redis ig time::" + (b - a) + "ms");
    }

    /**
     * 初始化队列,用于集群部署时的数据更新
     */
    private void initRabbit(NutConfig config, Dao dao) {
        try {
            String queue = R.UU32(), topicQueue = "topicQueue";
            ConnectionFactory factory = config.getIoc().get(ConnectionFactory.class, "rabbitmq_cf");
            log.debug("RabbitMQ :::" + factory.getHost());
            Connection conn = factory.newConnection();
            Channel channel = conn.createChannel();
            channel.queueDeclare(queue, true, true, false, null);
            channel.queueDeclare(topicQueue, true, false, false, null);
            channel.exchangeDeclare("topicExchange", BuiltinExchangeType.TOPIC, true);
            channel.exchangeDeclare("fanoutExchange", BuiltinExchangeType.FANOUT, true);
            channel.queueBind(queue, "fanoutExchange", "");
            channel.queueBind(queue, "topicExchange", "topic.*");
            //添加一个消费者,当系统参数/自定义路由修改时,重新初始化每个tomcat或jetty实例里的全局变量
            channel.basicConsume(queue, false, "myConsumerTag",
                    new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag,
                                                   Envelope envelope,
                                                   AMQP.BasicProperties properties,
                                                   byte[] body)
                                throws IOException {
                            String routingKey = envelope.getRoutingKey();
                            String exchange = envelope.getExchange();
                            NutMap params = Lang.fromBytes(body, NutMap.class);
                            log.debug("RabbitMQ exchange=" + exchange + ",routingKey=" + routingKey + ",params=" + Json.toJson(params));
                            long deliveryTag = envelope.getDeliveryTag();
                            switch (exchange) {
                                case "topicExchange"://主题模式,只需一个消费者消费
                                    break;
                                case "fanoutExchange"://广播模式,每个消费者都会消费
                                    switch (routingKey) {
                                        case "sysconfig":
                                            Globals.initSysConfig(dao);
                                            break;
                                        case "sysroute":
                                            Globals.initRoute(dao);
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                            }
                            // (process the message components here ...)
                            channel.basicAck(deliveryTag, false);
                        }
                    });
            Globals.RabbitMQEnabled = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化热插拔插件
     *
     * @param config
     * @param dao
     */
    private void initSysPlugin(NutConfig config, Dao dao) {
        try {
            PluginMaster pluginMaster = config.getIoc().get(PluginMaster.class);
            List<Sys_plugin> list = dao.query(Sys_plugin.class, Cnd.where("disabled", "=", false));
            for (Sys_plugin sysPlugin : list) {
                String name = sysPlugin.getPath().substring(sysPlugin.getPath().indexOf(".")).toLowerCase();
                File file = new File(Globals.AppRoot + sysPlugin.getPath());
                String[] p = new String[]{};
                IPlugin plugin;
                if (".jar".equals(name)) {
                    plugin = pluginMaster.buildFromJar(file, sysPlugin.getClassName());
                } else {
                    byte[] buf = Files.readBytes(file);
                    plugin = pluginMaster.build(sysPlugin.getClassName(), buf);
                }
                if (!Strings.isBlank(sysPlugin.getArgs())) {
                    p = org.apache.commons.lang3.StringUtils.split(sysPlugin.getArgs(), ",");
                }
                pluginMaster.register(sysPlugin.getCode(), plugin, p);
            }
        } catch (Exception e) {
            log.debug("plugin load error", e);
        }
    }

    /**
     * 初始化自定义路由
     *
     * @param config
     * @param dao
     */
    private void initSysRoute(NutConfig config, Dao dao) {
        if (0 == dao.count(Sys_route.class)) {
            //路由示例
            Sys_route route = new Sys_route();
            route.setDisabled(false);
            route.setUrl("/sysadmin");
            route.setToUrl("/platform/login");
            route.setType("hide");
            dao.insert(route);
        }
        Globals.initRoute(dao);
    }

    /**
     * 初始化定时任务
     *
     * @param config
     * @param dao
     */
    private void initSysTask(NutConfig config, Dao dao) {
        if (0 == dao.count(Sys_task.class)) {
            //执行Quartz SQL脚本
            String dbType = dao.getJdbcExpert().getDatabaseType();
            log.debug("dbType:::" + dbType);
            FileSqlManager fmq = new FileSqlManager("quartz/" + dbType.toLowerCase() + ".sql");
            List<Sql> sqlListq = fmq.createCombo(fmq.keys());
            Sql[] sqlsq = sqlListq.toArray(new Sql[sqlListq.size()]);
            for (Sql sql : sqlsq) {
                dao.execute(sql);
            }
            //定时任务示例
            Sys_task task = new Sys_task();
            task.setDisabled(true);
            task.setName("测试任务");
            task.setJobClass("cn.wizzer.app.web.commons.quartz.job.TestJob");
            task.setCron("*/5 * * * * ?");
            task.setData("{\"hi\":\"Wechat:wizzer | send red packets of support,thank u\"}");
            task.setNote("微信号：wizzer | 欢迎发送红包以示支持，多谢。。");
            dao.insert(task);
        }
        QuartzManager quartzManager = config.getIoc().get(QuartzManager.class);
        quartzManager.clear();//启动时清除任务(不影响集群任务)
        List<Sys_task> taskList = dao.query(Sys_task.class, Cnd.where("disabled", "=", false));
        for (Sys_task sysTask : taskList) {
            try {
                QuartzJob qj = new QuartzJob();
                qj.setJobName(sysTask.getId());
                qj.setJobGroup(sysTask.getId());
                qj.setClassName(sysTask.getJobClass());
                qj.setCron(sysTask.getCron());
                qj.setComment(sysTask.getNote());
                qj.setDataMap(sysTask.getData());
                quartzManager.add(qj);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    
    
    /**
     * 初始化数据库
     *
     * @param config
     * @param dao
     */
    private void initSysData(NutConfig config, Dao dao) {
        Daos.createTablesInPackage(dao, "com.znzz.app", false);
       // Daos.migration(dao, Ins_Asset_Repair.class, true, false);
       // Daos.migration(dao, Ins_DeviceState.class, false, false, true);
        //初始化24小时监控的数据
        if(0== dao.count(Ins_DeviceFacilityHour.class)){
        	for(int i=0;i<24;i++){
        		Ins_DeviceFacilityHour facilityHour = new Ins_DeviceFacilityHour() ;
        		facilityHour.setDateHour(i);
        		facilityHour.setPowerOnNum(0);
        		facilityHour.setCollectNum(0);
        		dao.insert(facilityHour);
        	}
        }
        
        
        // 若必要的数据表不存在，则初始化数据库
        if (0 == dao.count(Sys_user.class)) {
            //初始化配置表
            Sys_config conf = new Sys_config();
            conf.setConfigKey("AppName");
            conf.setConfigValue("仪器设备管理系统");
            conf.setNote("系统名称");
            // 插入一条系统配置信息
            dao.insert(conf);
            
            // 每次创建都要重新实例化
            conf = new Sys_config();
            conf.setConfigKey("AppShrotName");
            conf.setConfigValue("znzz");
            conf.setNote("系统短名称");
            dao.insert(conf);
            
            conf = new Sys_config();
            conf.setConfigKey("AppDomain");
            conf.setConfigValue("127.0.0.1");
            conf.setNote("系统域名");
            dao.insert(conf);
            
            conf = new Sys_config();
            conf.setConfigKey("AppUploadPath");
            conf.setConfigValue("/upload");
            conf.setNote("文件上传文件夹");
            dao.insert(conf);
            
            //初始化单位
            Sys_unit unit = new Sys_unit();
            unit.setId("100000");
            unit.setPath("0001");
            unit.setUnitcode("100000");
            unit.setName("航天二院25所");
            unit.setAliasName("System");
            unit.setLocation(0);
            unit.setAddress("银河-太阳系-地球");
            unit.setEmail("wizzer@qq.com");
            unit.setTelephone("");
            unit.setHasChildren(true);
            unit.setParentId("");
            unit.setWebsite("http://www.wizzer.cn");
            Sys_unit dbunit = dao.insert(unit);
            
            Sys_unit unit1 = new Sys_unit();
            unit1.setId("100001");
            unit1.setPath("00010001");
            unit1.setUnitcode("100001");
            unit1.setName("库房");
            unit1.setAliasName("System");
            unit1.setLocation(0);
            unit1.setAddress("25所仪器室默认库房");
            unit1.setEmail("wizzer@qq.com");
            unit1.setTelephone("");
            unit1.setHasChildren(false);
            unit1.setParentId("100000");
            unit1.setWebsite("http://www.wizzer.cn");
            
            dao.insert(unit1);
            

            
            //初始化角色
            Sys_role role = new Sys_role();
            role.setName("公共角色");
            role.setCode("public");
            role.setAliasName("Public");
            role.setNote("All user has role");
            role.setUnitid("");
            role.setDisabled(false);
            dao.insert(role);
            
            role = new Sys_role();
            role.setName("系统超级管理员");
            role.setCode("sysadmin");
            role.setAliasName("Sysadmin");
            role.setNote("System Admin");
            role.setUnitid("");
            //role.setMenus(menuList);
            role.setDisabled(false);
            Sys_role dbrole = dao.insert(role);
            
            role = new Sys_role();
            role.setName("系统管理员");
            role.setCode("admin");
            role.setAliasName("admin");
            role.setNote("tem Admin");
            role.setUnitid("");
            //role.setMenus(menuList);
            role.setDisabled(false);
            Sys_role dbroleadmin = dao.insert(role);
            
            //超级用户
            Sys_user user = new Sys_user();
            user.setLoginname("superadmin");
            user.setUsername("超级管理员");
            user.setUserStatus(1);
            user.setOrderby(1);
            user.setOpAt(1466571305);
            RandomNumberGenerator rng = new SecureRandomNumberGenerator();
            String salt = rng.nextBytes().toBase64();
            String hashedPasswordBase64 = new Sha256Hash("1", salt, 1024).toBase64();
            user.setSalt(salt);
            user.setPassword(hashedPasswordBase64);
            user.setLoginIp("127.0.0.1");
            user.setLoginAt(0);
            user.setLoginCount(0);
            user.setEmail("wizzer@qq.com");
            user.setLoginTheme("palette.css");
            user.setLoginBoxed(false);
            user.setLoginScroll(false);
            user.setLoginSidebar(false);
            user.setLoginPjax(true);
            user.setUnitid(dbunit.getId());
            Sys_user dbuser = dao.insert(user);
            
            //下一级用户
            Sys_user useradmin = new Sys_user();
            useradmin.setLoginname("admin");
            useradmin.setUsername("管理员");
            user.setUserStatus(1);
            useradmin.setOpAt(1466571305);
            RandomNumberGenerator rng1 = new SecureRandomNumberGenerator();
            String salt1 = rng1.nextBytes().toBase64();
            String hashedPasswordBase641 = new Sha256Hash("1", salt1, 1024).toBase64();
            useradmin.setSalt(salt1);
            useradmin.setPassword(hashedPasswordBase641);
            useradmin.setLoginIp("127.0.0.1");
            useradmin.setLoginAt(0);
            useradmin.setLoginCount(0);
            useradmin.setEmail("wizzer@qq.com");
            useradmin.setLoginTheme("palette.css");
            useradmin.setLoginBoxed(false);
            useradmin.setLoginScroll(false);
            useradmin.setLoginSidebar(false);
            useradmin.setLoginPjax(true);
            useradmin.setUnitid(dbunit.getId());
            Sys_user dbadminuser = dao.insert(useradmin);
            
            //不同的插入数据方式(安全)
            dao.insert("sys_user_unit", Chain.make("userId", dbuser.getId()).add("unitId", dbunit.getId()));
            dao.insert("sys_user_role", Chain.make("userId", dbuser.getId()).add("roleId", dbrole.getId()));
            
            
            dao.insert("sys_user_unit", Chain.make("userId", dbadminuser.getId()).add("unitId", dbunit.getId()));
            dao.insert("sys_user_role", Chain.make("userId", dbadminuser.getId()).add("roleId", dbroleadmin.getId()));
            //执行SQL脚本
            FileSqlManager fm = new FileSqlManager("db/");
            List<Sql> sqlList = fm.createCombo(fm.keys());
            Sql[] sqls = sqlList.toArray(new Sql[sqlList.size()]);
            for (Sql sql : sqls) {
                dao.execute(sql);
            }
         
            
            //菜单关联到角色
           dao.execute(Sqls.create("INSERT INTO sys_role_menu(roleId,menuId) SELECT @roleId,id FROM sys_menu").setParam("roleId", dbrole.getId()));
        
			dao.execute(Sqls.create("INSERT INTO sys_role_menu(roleId,menuId) SELECT @roleId,id FROM sys_menu where parentId != 'a9a8d95cd0f243b4938140f09b0affa1' and parentId != '620d11b448184980a9132ffcedcbad80' and id!='a9a8d95cd0f243b4938140f09b0affa1'").setParam("roleId", dbroleadmin.getId()));

        }
    }

    /**
     * 初始化系统变量
     *
     * @param config
     * @param dao
     */
    private void initSysSetting(NutConfig config, Dao dao) {
        Globals.AppRoot = Strings.sNull(config.getAppRoot());//项目路径
        Globals.AppBase = Strings.sNull(config.getServletContext().getContextPath());//部署名
        Globals.initSysConfig(dao);
    }
    
    private void initSysDict(Ioc ioc , Dao dao) {
    	SysDictServiceImpl sysDictServiceImpl = ioc.get(SysDictServiceImpl.class);
    	RedisService redisService = ioc.get(RedisService.class);
    	//清空可能存在的字典缓存
    	Set<String> keys = redisService.keys(Globals.DICTKEY) ;
    	if(keys.size()>0){
			String[] array = keys.toArray(new String[]{}) ;
			redisService.del(array) ;
		}
    	
    	//顶级字典list
        List<Sys_dict> dictList = dao.query(Sys_dict.class, Cnd.where("parentId", "=", "").or("parentId", "is", null));
        for (Sys_dict dict : dictList) {
        	  String key = Globals.DICTKEY+dict.getCode();
        	  List<Sys_dict> list = sysDictServiceImpl.getSubListByCode(dict.getCode());
        	  redisService.set(key, Json.toJson(list));
        }
		
	}
    
    public void destroy(NutConfig config) {
        // 解决quartz有时候无法停止的问题
        try {
            config.getIoc().get(Scheduler.class).shutdown(true);
        } catch (Exception e) {
        }
    }
    /**
     * 系统重启处理相关数据的key
     */
    public void initRedisKey(Ioc ioc){
    	try {
    		RedisService redisService = ioc.get(RedisService.class);
    		String keyModel=Globals.KEYMODEL ;
    		Set<String> keys = redisService.keys(keyModel) ;
    		if(keys.size()>0){
    			String[] array = keys.toArray(new String[]{}) ;
    			redisService.del(array) ;
    		}
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
    
    
}
