package logbasex.search.config.cloudmessage;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import logbasex.search.business.common.QueueConst;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.config.annotation.SqsConfiguration;
import org.springframework.cloud.aws.messaging.support.NotificationMessageArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Collections;

@Configuration
@PropertySources(
		{@PropertySource("classpath:${spring.profiles.active:dev}_aws.properties")}
)
@Import({SqsConfiguration.class, AWSConfig.class})
public class QueueConfig {
	@Bean
	@Qualifier("SQSAsync")
	@Primary
	public AmazonSQSAsync SQSAsync(@Autowired AWSStaticCredentialsProvider provider,
	                               @Value("${cloud.aws.region.static}") String region) {
		
		return AmazonSQSAsyncClientBuilder.standard()
				.withCredentials(provider)
				.withRegion(region)
				.build();
	}
	
	@Bean
	@Qualifier(QueueConst.SEARCH_SQS_EXECUTOR)
	public ThreadPoolTaskExecutor searchSQSExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		
		executor.setCorePoolSize(3);
		executor.setAllowCoreThreadTimeOut(true);
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setMaxPoolSize(10);
		executor.setKeepAliveSeconds(20);
		executor.setThreadNamePrefix("sqs_search_executor_");
		executor.initialize();
		
		return executor;
	}
	
	@Bean
	public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(AmazonSQSAsync amazonSQS) {
		SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
		factory.setAmazonSqs(amazonSQS);
		factory.setMaxNumberOfMessages(10);
		factory.setAutoStartup(true);
		factory.setWaitTimeOut(20);
		
		return factory;
	}
	
	@Bean
	public QueueMessageHandlerFactory queueMessageHandlerFactory(AmazonSQSAsync amazonSQS, BeanFactory beanFactory,
	                                                             ObjectMapper objectMapper) {
		
		QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();
		factory.setAmazonSqs(amazonSQS);
		factory.setBeanFactory(beanFactory);
		
		MappingJackson2MessageConverter mappingJackson2MessageConverter = new MappingJackson2MessageConverter();
		mappingJackson2MessageConverter.setSerializedPayloadClass(String.class);
		mappingJackson2MessageConverter.setObjectMapper(objectMapper);
		mappingJackson2MessageConverter.setStrictContentTypeMatch(false);
		// NotificationMsgArgResolver is used to deserialize the "Message" data from SNS Notification
		factory.setArgumentResolvers(Collections.singletonList(new NotificationMessageArgumentResolver(mappingJackson2MessageConverter)));
		
		return factory;
		
	}
}
