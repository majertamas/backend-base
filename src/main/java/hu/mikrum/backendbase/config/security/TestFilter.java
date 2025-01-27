package hu.mikrum.backendbase.config.security;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static hu.mikrum.backendbase.util.Util.AUTHORIZATION_HEADER_IS_MISSING;
import static hu.mikrum.backendbase.util.Util.PASSWORD;
import static hu.mikrum.backendbase.util.Util.USERNAME;

@Component
@Slf4j
public class TestFilter extends AbstractFilter {

    protected TestFilter(ApplicationContext applicationContext, AuthenticationManager authenticationManager) {
        super(applicationContext, authenticationManager);
    }

    @Override
    void filter(@Nonnull HttpServletRequest request,
                @Nonnull HttpServletResponse response,
                @Nonnull FilterChain filterChain) throws ServletException, IOException {

        String username = request.getHeader(USERNAME);
        String password = request.getHeader(PASSWORD);

        if (username == null) {
            throw new AuthorizationDeniedException(AUTHORIZATION_HEADER_IS_MISSING);
        }

        if (password == null) {
            throw new AuthorizationDeniedException(AUTHORIZATION_HEADER_IS_MISSING);
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

}
