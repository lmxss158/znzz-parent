package com.znzz.app.instrument.modules.service;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface OffLineNoticeService {
	public String OffLineMessageNotice(@WebParam(name = "param", targetNamespace = "") String param);
}
