package com.whl.spring.admin.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;

@Profile("insecure")
@Configuration(proxyBeanMethods = false)
public class SecurityPermitAllConfig {
    private final AdminServerProperties adminServer;

    public SecurityPermitAllConfig(AdminServerProperties adminServer) {
        this.adminServer = adminServer;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorizeRequests) -> authorizeRequests.anyRequest().permitAll())
                .csrf((csrf) -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .ignoringRequestMatchers(
                                PathPatternRequestMatcher.withDefaults().matcher(POST, this.adminServer.path("/instances")),
                                PathPatternRequestMatcher.withDefaults().matcher(POST, this.adminServer.path("/notifications/**")),
                                PathPatternRequestMatcher.withDefaults().matcher(DELETE, this.adminServer.path("/notifications/**")),
                                PathPatternRequestMatcher.withDefaults().matcher(DELETE, this.adminServer.path("/instances/*")),
                                PathPatternRequestMatcher.withDefaults().matcher(this.adminServer.path("/actuator/**"))));
        return http.build();
    }

}
