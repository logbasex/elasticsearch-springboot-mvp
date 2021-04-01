package logbasex.search.config.elasticsearch;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

@Configuration
@PropertySource("classpath:${spring.profiles.active:dev}_elasticsearch.properties")
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {
	
	@Value("${elasticsearch.host}")
	private String host;
	
	@Value("${elasticsearch.port}")
	private Integer port;
	
	@Value("${elasticsearch.username}")
	private String userName;
	
	@Value("${elasticsearch.password}")
	private String password;
	
	@Override
	public RestHighLevelClient elasticsearchClient() {
		
		final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));
		
		RestClientBuilder builder = RestClient.builder(new HttpHost(host, port))
				.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
		
		return new RestHighLevelClient(builder);
	}
}
