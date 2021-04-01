package logbasex.search.config.extractor;

import logbasex.search.config.security.SecurityConst;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;
import java.util.Map;

public class EAuthoritiesExtractor implements AuthoritiesExtractor {
    @Override
    public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(asAuthorities(map));
    }

    @SuppressWarnings("unchecked")
    private String asAuthorities(Map<String, Object> map) {
    	
		if (map.containsKey(SecurityConst.CLIENT_ID)) {
			return "ROLE_ADMIN";
		}

		List<String> authorities =  (List<String>) map.get("roles");
//		List<String> auth = authorities.stream().map(a -> {
//			return String.valueOf(a.get("name"));
//		}).collect(Collectors.toList());
        return String.join(",", authorities);
    }
}
