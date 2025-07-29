package com.quesocololand.msvcattractionsalerting.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    //Fields of RabbitMQConfig
    @Value("${rabbitmq.queue.visitor-counts}")
    public String visitorCountsQueueName;
    @Value("${rabbitmq.queue.visitor-counts-alert}")
    public String visitorCountsAlertQueueName;

    @Value("${rabbitmq.exchange.visitor-counts}")
    public String exchangeName;

    @Value("${rabbitmq.routingkey.visitor-counts}")
    public String visitorCountsRoutingKey;
    @Value("${rabbitmq.routingkey.visitor-counts-alert}")
    public String visitorCountsAlertRoutingKey;

    //Beans of RabbitMQConfig
    @Bean
    public Queue visitorCountsQueue() {
        return new Queue(this.visitorCountsQueueName);
    }
    @Bean
    public Queue visitorCountsAlertQueue() {
        // The queue is created as durable (true) so it survives to broker restarts
        // and exclusive (false) so it can be accessed by multiple consumers
        // autoDelete: (false) means that the queue will not be deleted when the last consumer gets disconnected
        return new Queue(this.visitorCountsAlertQueueName,
            true,
            false,
            false
        );
    }

    @Bean
    public TopicExchange visitorCountsExchange() {
        return new TopicExchange(this.exchangeName);
    }

    @Bean
    public Binding visitorCountsBinding() {
        return BindingBuilder.bind(visitorCountsQueue())
                .to(visitorCountsExchange())
                .with(this.visitorCountsRoutingKey);
    }

    @Bean
    public Binding visitorCountsAlertBinding() {
        return BindingBuilder.bind(visitorCountsAlertQueue())
                .to(visitorCountsExchange())
                .with(this.visitorCountsAlertRoutingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
