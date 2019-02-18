package com.github.aaric.getui.service.entity;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * APP手机推送消息
 *
 * @author Aaric, created on 2019-02-28T16:26.
 * @since 0.1.1
 */
public class PushMsg {

    /**
     * 短信模板信息
     */
    public static Map<String, String> PUSH_TEMPLATE_INFO;

    /**
     * 模板编码-车辆报警提示
     */
    public static final String PUSH_TEMPLATE_CODE_LOGIN_EXCEPTION = "PUSH_TEMPLATE_CODE_LOGIN_EXCEPTION";

    static {
        // 添加推送模板信息
        PUSH_TEMPLATE_INFO = new HashMap<>();
        // 异常登录提示
        PUSH_TEMPLATE_INFO.put(PUSH_TEMPLATE_CODE_LOGIN_EXCEPTION, "您的账号${userName}（${phoneNumber}）存在异常登录行为：直接访问Web端（${systemUrl}）进行查看和处理，如果确认是您自己在登录，可忽略该消息。");
    }

    /**
     * 推送类型：给全部APP用户
     */
    public static final int PUSH_TYPE_ALL = 1;

    /**
     * 推送类型：给指定APP用户
     */
    public static final int PUSH_TYPE_REFID = 2;

    /**
     * 推送类型
     */
    private Integer pushType;

    /**
     * 推送ID
     */
    private Integer refId;

    /**
     * 消息类型
     */
    private Integer msgType;

    /**
     * 消息标题
     */
    private String msgTitle;

    /**
     * 消息模板编号
     */
    private String msgTemplateCode;

    /**
     * 消息模板参数信息
     */
    private Map<String, String> msgTemplateParams;

    /**
     * 消息业务ID
     */
    private Integer msgRefId;

    public PushMsg() {
    }

    /**
     * 构造函数
     *
     * @param pushType          推送类型
     * @param refId             推送ID
     * @param msgType           消息类型
     * @param msgTitle          消息标题
     * @param msgTemplateCode   消息模板编号
     * @param msgTemplateParams 消息模板参数信息
     * @param msgRefId          消息业务ID
     */
    public PushMsg(Integer pushType, Integer refId, Integer msgType, String msgTitle, String msgTemplateCode, Map<String, String> msgTemplateParams, Integer msgRefId) {
        this.pushType = pushType;
        this.refId = refId;
        this.msgType = msgType;
        this.msgTitle = msgTitle;
        this.msgTemplateCode = msgTemplateCode;
        this.msgTemplateParams = msgTemplateParams;
        this.msgRefId = msgRefId;
    }

    /**
     * 推送给全部APP用户
     *
     * @param msgType           消息类型
     * @param msgTitle          消息标题
     * @param msgTemplateCode   消息模板编号
     * @param msgTemplateParams 消息模板参数信息
     * @param msgRefId          消息业务ID
     */
    public PushMsg(Integer msgType, String msgTitle, String msgTemplateCode, Map<String, String> msgTemplateParams, Integer msgRefId) {
        this(PUSH_TYPE_ALL, null, msgType, msgTitle, msgTemplateCode, msgTemplateParams, msgRefId);
    }

    /**
     * 获得推送内容
     *
     * @return
     */
    public String getContent() {
        if (null != this.msgTemplateCode && null != this.msgTemplateParams) {
            String content = PUSH_TEMPLATE_INFO.get(this.msgTemplateCode);
            if (StringUtils.isNotBlank(content)) {
                Set<Map.Entry<String, String>> params = this.msgTemplateParams.entrySet();
                for (Map.Entry<String, String> object : params) {
                    content = content.replace("${" + object.getKey() + "}", object.getValue());
                }
                return content;
            }
        }
        return null;
    }

    public Integer getPushType() {
        return pushType;
    }

    public void setPushType(Integer pushType) {
        this.pushType = pushType;
    }

    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgTemplateCode() {
        return msgTemplateCode;
    }

    public void setMsgTemplateCode(String msgTemplateCode) {
        this.msgTemplateCode = msgTemplateCode;
    }

    public Map<String, String> getMsgTemplateParams() {
        return msgTemplateParams;
    }

    public void setMsgTemplateParams(Map<String, String> msgTemplateParams) {
        this.msgTemplateParams = msgTemplateParams;
    }

    public Integer getMsgRefId() {
        return msgRefId;
    }

    public void setMsgRefId(Integer msgRefId) {
        this.msgRefId = msgRefId;
    }
}
