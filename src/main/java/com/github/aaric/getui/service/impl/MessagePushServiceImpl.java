package com.github.aaric.getui.service.impl;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import com.github.aaric.getui.service.MessagePushService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 消息推送Service接口实现
 *
 * @author Aaric, created on 2018-07-27T11:02.
 * @since 0.1.0
 */
@Service
public class MessagePushServiceImpl implements MessagePushService {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(MessagePushService.class);

    /**
     * 默认离线有效时间，单位为毫秒
     */
    private static long DEFAULT_OFFLINE_EXPIRE_TIME = 24 * 3600 * 1000;

    /**
     * 默认网络环境：1为wifi，0为不限制网络环境
     */
    private static int DEFAULT_PUSH_NETWORK_TYPE = 0;

    /**
     * 个推-AppID
     */
    @Value("${getui.appId}")
    private String appId;

    /**
     * 个推-AppKey
     */
    @Value("${getui.appKey}")
    private String appKey;

    /**
     * 个推-AppSecret
     */
    @Value("${getui.masterSecret}")
    private String masterSecret;

    /**
     * 个推-SDK地址
     */
    @Value("${getui.sdkUrl}")
    private String sdkUrl;

    /**
     * 通知栏显示logo地址
     */
    @Value("${getui.logoUrl}")
    private String logoUrl;

    @Override
    public String pushToSingleToApple(String msgTitle, String msgText, String transContent, String clientId) {
        // 检查数据合法性
        if (StringUtils.isBlank(transContent) || StringUtils.isBlank(clientId)) {
            throw new IllegalArgumentException("the required params can't be empty");
        }

        // 推送给单个用户
        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        message.setOfflineExpireTime(DEFAULT_OFFLINE_EXPIRE_TIME); //离线有效时间，单位为毫秒，可选
        message.setPushNetWorkType(DEFAULT_PUSH_NETWORK_TYPE); //网络环境：1为wifi，0为不限制网络环境
        message.setData(getDefaultTransmissionAPNTemplate(msgTitle, msgText, transContent)); //设置ANPS内容模板

        // 指定对象
        Target target = new Target();
        target.setAppId(appId);
        target.setClientId(clientId);

        // 返回taskId信息
        IGtPush gtPush = new IGtPush(sdkUrl, appKey, masterSecret);
        IPushResult result = null;
        int count = 0;
        return sendSingleMsg(message, target, gtPush, result, count, "test");
    }

    @Override
    public String pushToSingleToAndroid(String msgTitle, String msgText, String transContent, String clientId) {
        // 检查数据合法性
        if (StringUtils.isBlank(msgTitle) || StringUtils.isBlank(msgText) || StringUtils.isBlank(transContent) || StringUtils.isBlank(clientId)) {
            throw new IllegalArgumentException("the required params can't be empty");
        }

        // 推送给单个用户
        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        message.setOfflineExpireTime(DEFAULT_OFFLINE_EXPIRE_TIME); //离线有效时间，单位为毫秒，可选
        message.setPushNetWorkType(DEFAULT_PUSH_NETWORK_TYPE); //网络环境：1为wifi，0为不限制网络环境
        message.setData(getDefaultNotificationTemplate(msgTitle, msgText, transContent)); // 设置内容模板

        // 指定对象
        Target target = new Target();
        target.setAppId(appId);
        target.setClientId(clientId);

        // 返回taskId信息
        IGtPush gtPush = new IGtPush(sdkUrl, appKey, masterSecret);
        IPushResult result = null;
        int count = 0;
        return sendSingleMsg(message, target, gtPush, result, count, msgTitle);
    }

    //失败情况重复发送三次消息
    private String sendSingleMsg(SingleMessage message, Target target, IGtPush gtPush, IPushResult result, int count, String msgTitle) {
        String msg = null;
        boolean flag = false;
        if (count >= 3) {
            return msg;
        }
        count++;
        try {
            result = gtPush.pushMessageToSingle(message, target);
            if (null != result) {
                Map<String, Object> response = result.getResponse();
                if (null != response) {
                    if ("ok".equals(response.get("result"))) {
                        return response.get("taskId").toString();
                    } else {
                        logger.error("{} - pushSingle failure Id:{}, cause: {}", msgTitle, target.getClientId(), null == response.get("taskId") ? null : response.get("taskId").toString());
                    }
                } else {
                    flag = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = true;
        }
        if (flag) {
            logger.error("{} - push failure: {}", msgTitle, target.getClientId());
            msg = sendSingleMsg(message, target, gtPush, result, count, msgTitle);
        }
        return msg;
    }

    @Override
    public String pushToList(String msgTitle, String msgText, String transContent, List<String> clientIdList) {
        // 检查数据合法性
        if (StringUtils.isBlank(msgTitle) || StringUtils.isBlank(msgText) || StringUtils.isBlank(transContent) || null == clientIdList || 0 == clientIdList.size()) {
            throw new IllegalArgumentException("the required params can't be empty");
        }

        // 推送给一个用户集合
        ListMessage message = new ListMessage();
        message.setOffline(true);
        message.setOfflineExpireTime(DEFAULT_OFFLINE_EXPIRE_TIME); //离线有效时间，单位为毫秒，可选
        message.setPushNetWorkType(DEFAULT_PUSH_NETWORK_TYPE); //网络环境：1为wifi，0为不限制网络环境
        message.setData(getDefaultNotificationTemplate(msgTitle, msgText, transContent)); // 设置内容模板

        // 指定对象集合
        Target target;
        List<Target> targetList = new ArrayList<>();
        for (String clientId : clientIdList) {
            target = new Target();
            target.setAppId(appId);
            target.setClientId(clientId);
            targetList.add(target);
        }

        // 返回taskId信息
        IGtPush gtPush = new IGtPush(sdkUrl, appKey, masterSecret);
        IPushResult result = gtPush.pushMessageToList(gtPush.getContentId(message), targetList);
        if (null != result) {
            Map<String, Object> response = result.getResponse();
            if (null != response && "ok".equals(response.get("result"))) {
                return response.get("contentId").toString();
            }
        }

        return null;
    }

    @Override
    public String pushToAll(String msgTitle, String msgText, String transContent) {
        // 检查数据合法性
        if (StringUtils.isBlank(msgTitle) || StringUtils.isBlank(msgText) || StringUtils.isBlank(transContent)) {
            throw new IllegalArgumentException("the required params can't be empty");
        }

        // 推送给所有APP用户
        AppMessage message = new AppMessage();
        message.setOffline(true);
        message.setOfflineExpireTime(DEFAULT_OFFLINE_EXPIRE_TIME); //离线有效时间，单位为毫秒，可选
        message.setPushNetWorkType(DEFAULT_PUSH_NETWORK_TYPE); //网络环境：1为wifi，0为不限制网络环境
        message.setData(getDefaultNotificationTemplate(msgTitle, msgText, transContent)); // 设置内容模板

        // 推送给App的目标用户需要满足的条件
        List<String> appIdList = new ArrayList<>();
        appIdList.add(appId);
        message.setAppIdList(appIdList);

        // 返回taskId信息
        IGtPush gtPush = new IGtPush(sdkUrl, appKey, masterSecret);
        IPushResult result = null;
        int count = 0;
        return sendAllMsg(message, gtPush, result, count, msgTitle);
    }

    //失败情况重复发送三次消息（所有人）
    private String sendAllMsg(AppMessage message, IGtPush gtPush, IPushResult result, int count, String msgTitle) {
        String msg = null;
        boolean flag = false;
        if (count >= 3) {
            return msg;
        }
        count++;
        try {
            result = gtPush.pushMessageToApp(message);
            if (null != result) {
                Map<String, Object> response = result.getResponse();
                if (null != response && "ok".equals(response.get("result"))) {
                    return response.get("contentId").toString();
                } else {
                    logger.error("{} - push All failure, cause: {}", msgTitle, response.get("result").toString());
                }
            } else {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = true;
        }
        if (flag) {
            logger.error("{} - push All failure: count:{}", msgTitle, count);
            msg = sendAllMsg(message, gtPush, result, count, msgTitle);
        }
        return msg;
    }

    @Override
    public String pushLinkToAll(String msgTitle, String msgText, String skipToUrl) {
        // 检查数据合法性
        if (StringUtils.isBlank(msgTitle) || StringUtils.isBlank(msgText) || StringUtils.isBlank(skipToUrl)) {
            throw new IllegalArgumentException("the required params can't be empty");
        }

        // 推送给所有APP用户
        AppMessage message = new AppMessage();
        message.setOffline(true);
        message.setOfflineExpireTime(DEFAULT_OFFLINE_EXPIRE_TIME); //离线有效时间，单位为毫秒，可选
        message.setPushNetWorkType(DEFAULT_PUSH_NETWORK_TYPE); //网络环境：1为wifi，0为不限制网络环境
        message.setData(getDefaultLinkTemplate(msgTitle, msgText, skipToUrl)); // 设置内容模板

        // 推送给App的目标用户需要满足的条件
        List<String> appIdList = new ArrayList<>();
        appIdList.add(appId);
        message.setAppIdList(appIdList);

        // 返回taskId信息
        IGtPush gtPush = new IGtPush(sdkUrl, appKey, masterSecret);
        IPushResult result = gtPush.pushMessageToApp(message);
        if (null != result) {
            Map<String, Object> response = result.getResponse();
            if (null != response && "ok".equals(response.get("result"))) {
                return response.get("contentId").toString();
            }
        }

        return null;
    }

    @Override
    public boolean cancelPushTask(String taskId) {
        IGtPush gtPush = new IGtPush(sdkUrl, appKey, masterSecret);
        return gtPush.cancelContentId(taskId);
    }

    /**
     * 获得默认透传消息模板
     *
     * @param msgTitle     消息标题
     * @param msgText      消息内容
     * @param transContent 透传内容
     * @return
     */
    public NotificationTemplate getDefaultNotificationTemplate(String msgTitle, String msgText, String transContent) {
        // 初始化模板对象
        NotificationTemplate template = new NotificationTemplate();
        template.setAppId(appId); //设置AppID与AppKey
        template.setAppkey(appKey);

        // 定义推送风格
        Style0 style = new Style0();
        style.setTitle(msgTitle);  //设置通知栏标题与内容
        style.setText(msgText);
        style.setLogo("icon.png"); //配置通知栏图标
        style.setLogoUrl(logoUrl); //配置通知栏网络图标
        style.setRing(true); //设置通知是否响铃，震动，或者可清除
        style.setVibrate(true);
        style.setClearable(true);
        template.setStyle(style);

        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(2);
        template.setTransmissionContent(transContent);
        return template;
    }

    /**
     * 获得默认链接消息模板
     *
     * @param msgTitle  消息标题
     * @param msgText   消息内容
     * @param skipToUrl 跳转地址
     * @return
     */
    private LinkTemplate getDefaultLinkTemplate(String msgTitle, String msgText, String skipToUrl) {
        // 初始化模板对象
        LinkTemplate template = new LinkTemplate();
        template.setAppId(appId); //设置AppID与AppKey
        template.setAppkey(appKey);

        // 定义推送风格
        Style0 style = new Style0();
        style.setTitle(msgTitle); //设置通知栏标题与内容
        style.setText(msgText);
        style.setLogo("icon.png"); //配置通知栏图标
        style.setLogoUrl(logoUrl); // 配置通知栏网络图标
        style.setRing(true); //设置通知是否响铃，震动，或者可清除
        style.setVibrate(true);
        style.setClearable(true);
        template.setStyle(style);

        // 设置打开的网址地址
        template.setUrl(skipToUrl);
        return template;
    }

    /**
     * 获得默认透传消息模板（支持iOS系统APN透传）
     *
     * @param msgTitle     消息标题，可选
     * @param msgText      消息内容，可选
     * @param transContent 透传内容，必须
     * @return
     */
    private TransmissionTemplate getDefaultTransmissionAPNTemplate(String msgTitle, String msgText, String transContent) {
        // 通知文本消息字符串
        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
        alertMsg.setTitle(msgTitle); //消息标题，iOS8.2以上版本支持
        alertMsg.setBody(msgText); //消息内容
        alertMsg.setActionLocKey("ActionLockey");
        alertMsg.setLocKey("LocKey");
        alertMsg.addLocArg("loc-args");
        alertMsg.setLaunchImage("launch-image");
        alertMsg.setTitleLocKey("TitleLocKey");
        alertMsg.addTitleLocArg("TitleLocArg");

        // 创建payload对象
        APNPayload payload = new APNPayload();
        payload.setAutoBadge("+1"); //在已有数字基础上加1显示
        payload.setContentAvailable(1);
        payload.setSound("default");
        payload.setCategory("default"); //由客户端定义
        payload.addCustomMsg("payload", "payload");
        payload.setAlertMsg(alertMsg);

        // 创建透传模板
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTransmissionContent(transContent);
        template.setTransmissionType(2);
        template.setAPNInfo(payload);

        return template;
    }
}
