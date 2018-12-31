package com.patroclos.knapp.jms;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@EnableJms
public class JmsConfig {

	@Value("${broker.url.1}")
	private String brokerVm;

	@Value("${broker.url.2}")
	private String brokerLocal;

	@Value("${broker.url.3}")
	private String brokerRmote;

	@Value("${spring.activemq.user}")
	private String user;

	@Value("${spring.activemq.password}")
	private String password;

	@Bean
	public ActiveMQConnectionFactory connectionFactory1() {
		if ("".equals(user)) {
			return new ActiveMQConnectionFactory(brokerVm);
		}
		return new ActiveMQConnectionFactory(user, password, brokerVm);
	}

	@Bean
	@Primary
	public ActiveMQConnectionFactory connectionFactory2() {
		if ("".equals(user)) {
			return new ActiveMQConnectionFactory(brokerLocal);
		}
		return new ActiveMQConnectionFactory(user, password, brokerLocal);
	}

	@Bean
	public ActiveMQConnectionFactory connectionFactory3() {
		if ("".equals(user)) {
			return new ActiveMQConnectionFactory(brokerRmote);
		}
		return new ActiveMQConnectionFactory(user, password, brokerRmote);
	}

	@Bean
	public JmsTemplate jmsTemplate1() {
		
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory1());
		jmsTemplate.setDefaultDestinationName(brokerVm);
		jmsTemplate.setPubSubDomain(false);
		return new JmsTemplate(connectionFactory1());
	}

	@Bean
	public JmsTemplate jmsTemplateTopic1() {
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory1());
		//jmsTemplate.setPubSubDomain(true);
		return jmsTemplate;
	}

	@Bean
	@Primary
	public JmsTemplate jmsTemplate2() {
		return new JmsTemplate(connectionFactory2());
	}

	@Bean
	public JmsTemplate jmsTemplateTopic2() {
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory2());
		jmsTemplate.setPubSubDomain(true);
		return jmsTemplate;
	}

	@Bean
	public JmsTemplate jmsTemplate3() {
		return new JmsTemplate(connectionFactory3());
	}

	@Bean
	public JmsTemplate jmsTemplateTopic3() {
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory3());
		jmsTemplate.setPubSubDomain(true);
		return jmsTemplate;
	}

	@Bean
	public JmsListenerContainerFactory<?> jmsListenerContainerFactory1(
			@Qualifier("connectionFactory1") ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		configurer.configure(factory, connectionFactory);
		return factory;
	}

	@Bean
	public JmsListenerContainerFactory<?> jmsListenerContainerFactory2(
			@Qualifier("connectionFactory2") ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		configurer.configure(factory, connectionFactory);
		return factory;
	}

	@Bean
	public JmsListenerContainerFactory<?> jmsListenerContainerFactory3(
			@Qualifier("connectionFactory3") ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		configurer.configure(factory, connectionFactory);
		return factory;
	}

}
