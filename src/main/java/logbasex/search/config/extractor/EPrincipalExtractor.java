package logbasex.search.config.extractor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import logbasex.search.config.security.SecurityConst;
import logbasex.search.db.entities.user.User;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;

import java.util.Map;

public class EPrincipalExtractor implements PrincipalExtractor {
    @Override
    public Object extractPrincipal(Map<String, Object> map) {
    	
    	if (map.containsKey(SecurityConst.CLIENT_ID)) {
			String clientId = (String) map.get(SecurityConst.CLIENT_ID);
			return clientId;
		}
    	
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        User user = objectMapper.convertValue(map, User.class);
        
        return user;
    }
}
