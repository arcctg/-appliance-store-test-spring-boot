package com.epam.rd.autocode.assessment.appliances.config;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final ClientRepository clientRepository;

    @Autowired
    public SecurityConfig(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {;
        UserDetails employee = User.builder()
                .username("employee")
                .password(passwordEncoder.encode("password123"))
                .roles("EMPLOYEE")
                .build();

        UserDetails client = User.builder()
                .username("client")
                .password(passwordEncoder.encode("password123"))
                .roles("CLIENT")
                .build();

        return new InMemoryUserDetailsManager(client, employee);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/catalog").permitAll()
                        .requestMatchers("/employees/**", "/clients/**", "/appliances/**", "/manufacturers/**").hasRole("EMPLOYEE")
                        .requestMatchers("/orders/**").hasAnyRole("EMPLOYEE", "CLIENT")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
