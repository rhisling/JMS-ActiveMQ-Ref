package me.aravindh.messqgingqueues.activemqref;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

@EnableJms
@SpringBootApplication
public class ActivemqrefApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ActivemqrefApplication.class, args);
        Sender sender = context.getBean(Sender.class);
        System.out.println("Sending a message ...");
        sender.sendMessage("order-queue", "Hello!");
    }

    @Bean
    public JmsListenerContainerFactory warehouseFactory(@Qualifier("connectionFactory") ConnectionFactory factory,
                                                        DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory containerFactory = new DefaultJmsListenerContainerFactory();
        configurer.configure(containerFactory, factory);
        return containerFactory;
    }

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory("admin", "admin", "tcp://localhost:61616");
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        return new JmsTemplate(connectionFactory());
    }


    @Bean
    public DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrency("1-1");
        return factory;
    }

}
