package com.znzz.app.web.modules.controllers.platform.instruments.formBean;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息信息的vo对象
 */
public class MessageInfo implements Serializable{

    private String messageContext;          //内容
    private Integer status;                 //状态
    private String send_time;                 //时间

    public String getMessageContext() {
        return messageContext;
    }

    public void setMessageContext(String messageContext) {
        this.messageContext = messageContext;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }
}
