package logbasex.search.business.config;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Oauth2RestTemplateService {
	public OAuth2RestTemplate restTemplate(String clientId, String secret, String accessTokenUri) {
		
		ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
		resourceDetails.setAccessTokenUri(accessTokenUri);
		resourceDetails.setClientId(clientId);
		resourceDetails.setClientSecret(secret);
		List<String> scopes = new ArrayList<>();
		scopes.add("read");
		scopes.add("write");
		resourceDetails.setScope(scopes);
		
		return new OAuth2RestTemplate(resourceDetails);
	}
}
