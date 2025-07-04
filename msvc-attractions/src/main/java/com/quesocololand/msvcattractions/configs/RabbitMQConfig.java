package com.quesocololand.msvcattractions.configs;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
    private String queueName;
    @Value("${rabbitmq.exchange.visitor-counts}")
    private String exchangeName;
    @Value("${rabbitmq.routingkey.visitor-counts}")
    private String routingKey;

    //Beans of RabbitMQConfig
    @Bean
    public Queue visitorCountsQueue() {
        return new Queue(this.queueName);
    }

    @Bean
    public TopicExchange visitorCountsExchange() {
        return new TopicExchange(this.exchangeName);
    }

    @Bean
    public Binding visitorCountsBinding() {
        return BindingBuilder.bind(visitorCountsQueue())
                .to(visitorCountsExchange())
                .with(this.routingKey);
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
