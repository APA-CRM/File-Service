package com.crm.file.config;

import com.crm.sharedlib.messaging.config.BaseRabbitMQConfig;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.crm.sharedlib.messaging.constants.RabbitMQConstants.*;

@Configuration
@EnableRabbit
public class RabbitMQConfig extends BaseRabbitMQConfig {

    private static final int DEFAULT_DELIVERY_LIMIT = 5;

    @Bean
    public TopicExchange fileServiceTopicExchanger() {
        return ExchangeBuilder
                .topicExchange(FILE_SERVICE_EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    @Bean
    public Queue createRootDirQueue() {
        return QueueBuilder
                .durable(CREATE_ROOT_DIR_QUEUE)
                .deliveryLimit(5)
                .build();
    }

    @Bean
    public Queue orgRootDirCreatedQueue() {
        return QueueBuilder
                .durable(ROOT_DIR_CREATED_REPLY_QUEUE)
                .deliveryLimit(DEFAULT_DELIVERY_LIMIT)
                .build();
    }

    @Bean
    public Binding orgRootDirCreatedBinding() {
        return BindingBuilder
                .bind(orgRootDirCreatedQueue())
                .to(fileServiceTopicExchanger())
                .with(ORGANIZATION_ROOT_DIR_CREATE_ROUTING_KEY);
    }

}
