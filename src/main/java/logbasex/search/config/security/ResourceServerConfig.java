package logbasex.search.config.security;

import logbasex.search.config.extractor.EAuthoritiesExtractor;
import logbasex.search.config.extractor.EPrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextListener;

@Configuration
@EnableOAuth2Sso
@EnableResourceServer
@ComponentScan(basePackages = {"logbasex.search"})
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/**").authorizeRequests().antMatchers("/app/login", "/public/**", "/share/**", "/instagram/**").permitAll()
				// swagger configure
				.antMatchers("/resources/**", "/static/**", "/webui/**", "/configuration/**", "/swagger-ui/**",
				"/swagger-resources/**", "/api-docs", "/api-docs/**", "/v2/api-docs/**", "/*.html", "/**/*.html",
				"/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.gif", "/**/*.svg", "/**/*.ico", "/**/*.ttf",
				"/**/*.woff").permitAll()
				.anyRequest().authenticated().and().csrf().disable();
	}

	@Bean
	public AuthoritiesExtractor ekoAuthoritiesExtractor() {
		return new EAuthoritiesExtractor();
	}

	@Bean
	public PrincipalExtractor ekoPrincipalExtractor() {
		return new EPrincipalExtractor();
	}

	@Bean
	@Primary
	protected ResourceServerTokenServices resourceServerTokenServices(ResourceServerProperties sso,
	                                                                  OAuth2ClientContext oauth2ClientContext,
	                                                                  UserInfoRestTemplateFactory restTemplateFactory) {
		UserInfoTokenServices services = new UserInfoTokenServices(sso.getUserInfoUri(), sso.getClientId());
		services.setRestTemplate(restTemplateFactory.getUserInfoRestTemplate());
		services.setTokenType(sso.getTokenType());
		services.setAuthoritiesExtractor(ekoAuthoritiesExtractor());
		services.setPrincipalExtractor(ekoPrincipalExtractor());
		return services;
	}

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}
}
