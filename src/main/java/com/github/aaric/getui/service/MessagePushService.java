package com.github.aaric.getui.service;

import java.util.List;

/**
 * 消息推送Service接口
 *
 * @author Aaric, created on 2018-07-27T11:01.
 * @since 0.1.0
 */
public interface MessagePushService {

    /**
     * 推送消息给某个用户
     *
     * @param msgTitle     消息标题，可选
     * @param msgText      消息内容，可选
     * @param transContent 透传内容，必须
     * @param clientId     客户端ID
     * @return 任务ID
     */
    String pushToSingle(String msgTitle, String msgText, String transContent, String clientId);

    /**
     * 推送消息给一个用户集合
     *
     * @param msgTitle     消息标题，可选
     * @param msgText      消息内容，可选
     * @param transContent 透传内容，必须
     * @param clientIdList 客户端ID集合
     * @return
     */
    String pushToList(String msgTitle, String msgText, String transContent, List<String> clientIdList);

    /**
     * 推送消息给所有APP用户
     *
     * @param msgTitle     消息标题，可选
     * @param msgText      消息内容，可选
     * @param transContent 透传内容，必须
     * @return
     */
    String pushToAll(String msgTitle, String msgText, String transContent);

    /**
     * 推送链接地址消息给所有APP用户
     *
     * @param msgTitle  消息标题，可选
     * @param msgText   消息内容，可选
     * @param skipToUrl 跳转地址
     * @return
     */
    String pushLinkToAll(String msgTitle, String msgText, String skipToUrl);

    /**
     * 取消推送任务
     *
     * @param taskId 任务ID
     * @return
     */
    boolean cancelPushTask(String taskId);
}
