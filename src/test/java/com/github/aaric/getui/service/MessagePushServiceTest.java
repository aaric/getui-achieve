package com.github.aaric.getui.service;

import com.github.aaric.getui.service.entity.PushMsg;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * MessagePushServiceTest
 *
 * @author Aaric, created on 2018-07-27T11:01.
 * @since 0.1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MessagePushServiceTest {

    @Autowired
    private MessagePushService messagePushService;

    @Test
    @Ignore
    public void pushToSingleToApple() {
        String content = MessageFormat.format("{0,number,000000}", new Random().nextInt(999999));
        String taskId = messagePushService.pushToSingleToApple("GMMC-TEST", "广汽三菱TSP平台测试-" + content, content, "9514972f3a7cbdac08c36348b29a693c");
        System.out.println(taskId);
        Assert.assertNotNull(taskId);
    }

    @Test
    @Ignore
    public void pushToSingleToAndroid() {
        String content = MessageFormat.format("{0,number,000000}", new Random().nextInt(999999));
        String taskId = messagePushService.pushToSingleToAndroid("GMMC-TEST", "广汽三菱TSP平台测试-" + content, content, "8ecd37c4af1eedb99dbd85dff59b0c2b");
        System.out.println(taskId);
        Assert.assertNotNull(taskId);
    }

    @Test
    @Ignore
    public void testPushToList() {
        String random = MessageFormat.format("{0,number,000000}", new Random().nextInt(999999));
        String taskId = messagePushService.pushToList("Hello World!", "Test Content-" + random, random, Arrays.asList("e206f8326cdc4410cde6b137c1ac77aa"));
        System.out.println(taskId);
        Assert.assertNotNull(taskId);
    }

    @Test
    @Ignore
    public void testPushToAll() {
        String random = MessageFormat.format("{0,number,000000}", new Random().nextInt(999999));
        String taskId = messagePushService.pushToAll("Hello World!", "Test Content-" + random, random);
        System.out.println(taskId);
        Assert.assertNotNull(taskId);
    }

    @Test
    @Ignore
    public void testPushLinkToAll() {
        String random = MessageFormat.format("{0,number,000000}", new Random().nextInt(999999));
        String taskId = messagePushService.pushLinkToAll("Hello World!", "Test Content-" + random, "http://www.baidu.com");
        System.out.println(taskId);
        Assert.assertNotNull(taskId);
    }

    @Test
    @Ignore
    public void testCancelPushTask() {
        Assert.assertTrue(messagePushService.cancelPushTask("OSL-0725_dWo6k9WbfL7AXvMhvNOAX8"));
    }

    @Test
    @Ignore
    public void testPushMsg() {
        Map<String, String> msgTemplateParams = new HashMap<>();
        msgTemplateParams.put("userName", "aaric");
        msgTemplateParams.put("phoneNumber", "134****6688");
        msgTemplateParams.put("systemUrl", "http://t.cn/AABBCC");

        PushMsg pushMsg = new PushMsg(PushMsg.PUSH_TYPE_REFID, 1, 1, "hello", PushMsg.PUSH_TEMPLATE_CODE_LOGIN_EXCEPTION, msgTemplateParams, 1);

        String taskId;
        taskId = messagePushService.pushToSingleToApple("异常登录提醒", pushMsg.getContent(), "{\"refid\": 1}", "9514972f3a7cbdac08c36348b29a693c");
        //taskId = messagePushService.pushToSingleToAndroid("异常登录提醒", pushMsg.getContent(), "{\"refid\": 1}", "8ecd37c4af1eedb99dbd85dff59b0c2b");
        System.out.println(taskId);
        Assert.assertNotNull(taskId);
    }
}
