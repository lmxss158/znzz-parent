package com.znzz.app.sys.modules.services;

import java.io.IOException;
import java.util.Date;

import com.znzz.app.sys.modules.models.Sys_api;
import com.znzz.framework.base.service.BaseService;


public interface SysApiService extends BaseService<Sys_api> {
    String generateToken(Date date, String appId)throws IOException, ClassNotFoundException;
    boolean verifyToken(String appId, String token);
}
