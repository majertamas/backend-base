package hu.mikrum.backendbase.config.security;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

public abstract class AbstractFilter extends OncePerRequestFilter {

    protected final ApplicationContext applicationContext;
    protected final AuthenticationManager authenticationManager;

    protected AbstractFilter(ApplicationContext applicationContext, AuthenticationManager authenticationManager) {
        this.applicationContext = applicationContext;
        this.authenticationManager = authenticationManager;
    }

    abstract void filter(@Nonnull HttpServletRequest request,
                         @Nonnull HttpServletResponse response,
                         @Nonnull FilterChain filterChain) throws ServletException, IOException;

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {

        if (isPermitted(request)) {
            filterChain.doFilter(request, response);
        } else {
            filter(request, response, filterChain);
        }
    }

    private boolean isPermitted(@Nonnull HttpServletRequest request) {
        AuthorizationFilter authorizationFilter = getAuthorizationFilter();
        AuthorizationManager<HttpServletRequest> authorizationManager = authorizationFilter.getAuthorizationManager();
        try {
            authorizationManager.verify(() -> SecurityContextHolder.getContext().getAuthentication(), request);
        } catch (AuthorizationDeniedException e) {
            return false;
        }
        return true;
    }

    private AuthorizationFilter getAuthorizationFilter() {
        Map<String, SecurityFilterChain> beansOfType = applicationContext
                .getBeansOfType(SecurityFilterChain.class);

        SecurityFilterChain generalSecurityFilterChain = beansOfType.get("generalSecurityFilterChain");

        if (generalSecurityFilterChain == null) {
            throw new IllegalStateException("No SecurityFilterChain bean found in the application context");
        }

        return generalSecurityFilterChain
                .getFilters()
                .stream()
                .filter(AuthorizationFilter.class::isInstance)
                .map(AuthorizationFilter.class::cast)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No AuthorizationFilter found in the SecurityFilterChain"));
    }

}
