package com.patroclos.knapp.jms;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.patroclos.knapp.util.MsgManipulate;

@Component
public class JmsConsumer {

   private static Logger log = LoggerFactory.getLogger(JmsConsumer.class);

	
	@Autowired
	private JmsSender sender;
	
	@Autowired
	private MsgManipulate extractInf;
	
	@Value("${queue.topic.broker1}")
	private String topicVm;
	
	@Value("${queue.topic.broker2}")
	private String topic;

	@Autowired
	@Qualifier("jmsTemplateTopic2")
	private JmsTemplate jmsTemplateTopic2;

	@Autowired
	@Qualifier("jmsTemplateTopic1")
	private JmsTemplate jmsTemplateTopic1;

	private String resultMsg = null;

	// Listening Application Broker Local
	@JmsListener(destination = "queue.kuehneNagel", containerFactory = "jmsListenerContainerFactory2")
	public void onReceiverQueue(String msg) throws Throwable {

		try {

			if (extractInf.formatSt(msg).compareTo(extractInf.getMsgExpectIn()) == 0) {

				log.info("MSG received in queue.kuehneNagel -- " + extractInf.beautyXML(msg) + " -- ");

				log.info("Extracting some information...");

				resultMsg = extractInf.processMsg(msg);

				log.info("Sending New MSG to a topic.tibcoClient ...");

				sender.senderTopic(jmsTemplateTopic2, topic, resultMsg);

				log.info("-- New MSG was sent ! -- ");

				log.info(extractInf.beautyXML(resultMsg));

			} else {
				log.info("You must send an XML like this one :" + extractInf.beautyXML(extractInf.getMsgExpectIn()));
				return;
			}

		} catch (Exception e) {
			log.info("XML Error - beautyXML !", e);
			e.printStackTrace();
		}

	}

		
}