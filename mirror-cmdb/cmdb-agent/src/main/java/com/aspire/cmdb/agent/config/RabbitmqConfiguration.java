package com.aspire.cmdb.agent.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbitmq配置
 *
 * @author jiangxuwen
 * @date 2020/5/20 10:58
 */
@Configuration
@ConditionalOnProperty(name = "cmdb.enable.syncOsaFlag", havingValue = "true")
public class RabbitmqConfiguration {

    /** 业务线同步配置 start. */
    @Value("${businessLine.mq.exchange.name}")
    private String businessExchangeName;

    @Value("${businessLine.mq.queue.name}")
    private String businessQuqueName;

    @Bean
    public Queue businessQueue() {
        return new Queue(businessQuqueName, true, false, false);
    }

    @Bean
    public FanoutExchange businessExchange() {
        return new FanoutExchange(businessExchangeName);
    }

    @Bean
    public Binding bindingBusinessExchange() {
        return BindingBuilder.bind(businessQueue()).to(businessExchange());
    }

    /** 业务线同步配置 end. */

    /** 部门机构 同步配置 start. */
    @Value("${organization.mq.exchange.name}")
    private String orgExchangeName;

    @Value("${organization.mq.queue.name}")
    private String orgQuqueName;

    @Bean
    public Queue orgQueue() {
        return new Queue(orgQuqueName, true, false, false);
    }

    @Bean
    public FanoutExchange orgExchange() {
        return new FanoutExchange(orgExchangeName);
    }

    @Bean
    public Binding bindingOrgExchange() {
        return BindingBuilder.bind(orgQueue()).to(orgExchange());
    }

    /** 部门机构 同步配置 end. */

    /** 人员账号 同步配置 start. */
    @Value("${account.mq.exchange.name}")
    private String accountExchangeName;

    @Value("${account.mq.queue.name}")
    private String accountQuqueName;

    @Bean
    public Queue accountQueue() {
        return new Queue(accountQuqueName, true, false, false);
    }

    @Bean
    public FanoutExchange accountExchange() {
        return new FanoutExchange(accountExchangeName);
    }

    @Bean
    public Binding bindingAccountExchange() {
        return BindingBuilder.bind(accountQueue()).to(accountExchange());
    }

    /** 人员账号 同步配置 end. */

    /** 部门-人员账号 同步配置 start. */
    @Value("${orgAccount.mq.exchange.name}")
    private String orgAccountExchangeName;

    @Value("${orgAccount.mq.queue.name}")
    private String orgAccountQuqueName;

    @Bean
    public Queue orgAccountQueue() {
        return new Queue(orgAccountQuqueName, true, false, false);
    }

    @Bean
    public FanoutExchange orgAccountExchange() {
        return new FanoutExchange(orgAccountExchangeName);
    }

    @Bean
    public Binding bindingOrgAccountExchange() {
        return BindingBuilder.bind(orgAccountQueue()).to(orgAccountExchange());
    }

    /** 部门-人员账号 同步配置 end. */
}
