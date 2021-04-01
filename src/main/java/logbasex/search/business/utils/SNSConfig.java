package logbasex.search.business.utils;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import logbasex.search.config.cloudmessage.AWSConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:${spring.profiles.active:dev}_aws.properties")
@Import({AWSConfig.class})
public class SNSConfig {

	@Bean
	public AmazonSNS elasticsearchAmazonSNS(@Autowired AWSStaticCredentialsProvider provider, @Value("${cloud.aws.region.static}") String region) {
		return AmazonSNSClientBuilder.standard()
				.withCredentials(provider)
				.withRegion(region)
				.build();
	}
}
