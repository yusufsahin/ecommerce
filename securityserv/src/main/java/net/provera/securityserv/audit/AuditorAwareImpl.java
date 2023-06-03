package net.provera.securityserv.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            if((authentication != null || authentication.isAuthenticated() || !(authentication instanceof AnonymousAuthenticationToken)))
                return  Optional.of(authentication.getName());
            else
                return Optional.of("anonymousUser");
        } catch (NullPointerException npe) {
            return Optional.of("anonymousUser");
        }

    }
}
