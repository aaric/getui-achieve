package com.github.aaric.getui.service;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.MessageFormat;
import java.util.Arrays;
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
    public void testPushToSingle() {
        System.out.println(messagePushService);
        String random = MessageFormat.format("{0,number,000000}", new Random().nextInt(999999));
        String taskId = messagePushService.pushToSingle("Hello World!", "Test Content-" + random, random, "e206f8326cdc4410cde6b137c1ac77aa");
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

}
