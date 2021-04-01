package logbasex.search.business.utils;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import logbasex.search.db.dto.sns.ElasticsearchSNSMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
@PropertySource("classpath:${spring.profiles.active:dev}_aws.properties")
public class SNSPublisher {

	@Autowired
	private AmazonSNS snsClient;

	@Value("${aws.sns.topicArn}")
	private String elasticsearchTopicArn;
	 
	public void send(ElasticsearchSNSMessage message) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String className = message.getClass().getName();
	
			MessageAttributeValue objectType = new MessageAttributeValue()
					.withDataType("String")
					.withStringValue(className);
	
			Map<String, MessageAttributeValue> attributes = new HashMap<>();
			attributes.put("_type", objectType);
			String messageAsJson = objectMapper.writeValueAsString(message);
			
			PublishRequest publishRequest = new PublishRequest()
					.withMessageAttributes(attributes)
					.withTopicArn(elasticsearchTopicArn)
					.withMessage(messageAsJson);
	
			snsClient.publish(publishRequest);
		} catch (Exception ex) {
			// TODO: log to database
			ex.printStackTrace();
			log.error("========================= Send to sns error");
		}
	}
}
