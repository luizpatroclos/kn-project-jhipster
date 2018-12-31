package com.patroclos.knapp.jms;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsSender {

	public void senderTopic(JmsTemplate jms, String queue, String msg) {

		jms.convertAndSend(queue, msg);

	}

	public void senderQueue(JmsTemplate jms, String queue, String msg) {

		jms.convertAndSend(queue, msg);

	}

}
