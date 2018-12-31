package com.patroclos.knapp.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PatroclosIntegration {

	@Autowired
	private MockMsg mockMsg;
	
	@Autowired
	@Qualifier("jmsTemplate1")
	private JmsTemplate jmsTemplate1;

	@Value("${queue.queue.broker1}")
	private String queueVm;

	@Value("${queue.topic.broker1}")
	private String topicVm;

	
	@Test
    public  void integationMQtest() {
        this.jmsTemplate1.convertAndSend(queueVm, mockMsg.getMsgExpectIn());
        
        assertThat(this.jmsTemplate1.receiveAndConvert(queueVm)).isEqualTo( mockMsg.getMsgExpectIn());
    }

}