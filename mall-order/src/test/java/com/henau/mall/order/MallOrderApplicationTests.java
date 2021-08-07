package com.henau.mall.order;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MallOrderApplicationTests {

    @Autowired
    AmqpAdmin amqpAdmin;

    /**
     * 1、如何创建Exchange、Queue、Binding
     *      1)、使用AmqpAdmin进行创建
     * 2、如何收发消息
     */

    @Test
    public void createExchange() {
        //amqpAdmin
        //Exchange
        DirectExchange directExchange = new DirectExchange("hello-java-exchange", true, false);
        amqpAdmin.declareExchange(directExchange);
        log.info("Exchange[{}]创建成功", "hello-java-exchange");
    }

    @Test
    public void createQueue() {

        Queue queue = new Queue();
        amqpAdmin.declareQueue(queue);
        log.info("Queue[{]}创建成功", "hello-java-queue");
    }

}
