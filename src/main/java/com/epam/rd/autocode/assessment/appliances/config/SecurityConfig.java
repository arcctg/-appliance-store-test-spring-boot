package com.epam.rd.autocode.assessment.appliances.config;

import com.epam.rd.autocode.assessment.appliances.model.enums.Role;
import com.epam.rd.autocode.assessment.appliances.service.impl.MyUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String ADMIN = Role.ADMIN.name();
    private static final String EMPLOYEE = Role.EMPLOYEE.name();
    private static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-resources"
    };
    private static final String[] PUBLIC_WHITELIST = {
            "/", "/index", "/api", "/api/index",
            "/catalog", "/api/catalog",
            "/cart/**", "/api/cart/**", "/api/cart",
            "/register", "/new-user",
            "/api/auth/**",
            "/api/localization",
            "/actuator", "/actuator/**",
            "/h2-console/**"
    };
    private static final String[] EMPLOYEE_WHITELIST = {
            "/clients", "/clients/toggle",
            "/appliances", "/appliances/**", "/api/appliances", "/api/appliances/**",
            "/manufacturers", "/manufacturers/**", "/api/manufacturers", "/api/manufacturers/**",
            "/orders/update-status", "/api/orders/update-status"
    };
    private static final String[] ADMIN_WHITELIST = {
            "/employees/**", "/api/employees/**",
            "/clients/**", "/api/clients/**", "/api/clients",
            "/appliances/**", "/api/appliances/**",
            "/manufacturers/**", "/api/manufacturers/**"
    };
    private final MyUserDetailService myUserDetailService;

    public SecurityConfig(MyUserDetailService myUserDetailService) {
        this.myUserDetailService = myUserDetailService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_WHITELIST).permitAll()
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/clients").hasRole(EMPLOYEE)
                        .requestMatchers(HttpMethod.PATCH, "/api/clients/**").hasRole(EMPLOYEE)
                        .requestMatchers(EMPLOYEE_WHITELIST).hasAnyRole(EMPLOYEE, ADMIN)
                        .requestMatchers(ADMIN_WHITELIST).hasRole(ADMIN)
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(myUserDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration conf) throws Exception {
        return conf.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
