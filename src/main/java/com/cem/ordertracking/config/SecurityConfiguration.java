package com.cem.ordertracking.config;

import com.cem.ordertracking.security.Role;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


//        http
//            .authorizeHttpRequests()
//            .requestMatchers("")
//            .permitAll()

        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/api/v1/demo-controller").hasAuthority(String.valueOf(Role.ADMIN))
                        .requestMatchers(HttpMethod.PUT, "/customers").hasAnyAuthority(String.valueOf(Role.CUSTOMER), String.valueOf(Role.ADMIN))
                        .requestMatchers( "/customers/**").hasAuthority(String.valueOf(Role.ADMIN))
                        .requestMatchers(HttpMethod.POST, "/products").hasAuthority(String.valueOf(Role.ADMIN))
                        .requestMatchers(HttpMethod.PUT, "/products").hasAuthority(String.valueOf(Role.ADMIN))
                        .requestMatchers(HttpMethod.DELETE, "/products/**").hasAuthority(String.valueOf(Role.ADMIN))
                        .requestMatchers(HttpMethod.GET, "/orderInfos").hasAuthority(String.valueOf(Role.ADMIN))
                        .requestMatchers(HttpMethod.GET, "/orderInfos/{orderInfoId}").hasAuthority(String.valueOf(Role.ADMIN))

                        .requestMatchers(HttpMethod.GET, "/orderItems").hasAuthority(String.valueOf(Role.ADMIN))
                        .requestMatchers(HttpMethod.GET, "/orderItems/{orderItemId}").hasAuthority(String.valueOf(Role.ADMIN))


                        .requestMatchers(HttpMethod.POST, "/orderInfos").hasAuthority(String.valueOf(Role.ADMIN))
                        .requestMatchers(HttpMethod.PUT, "/orderInfos").hasAuthority(String.valueOf(Role.ADMIN))
                        .requestMatchers(HttpMethod.DELETE, "/orderInfos/{orderInfoId}").hasAuthority(String.valueOf(Role.ADMIN))

                        .requestMatchers(HttpMethod.POST, "/orderItems").hasAuthority(String.valueOf(Role.ADMIN))
                        .requestMatchers(HttpMethod.PUT, "/orderItems").hasAuthority(String.valueOf(Role.ADMIN))

                        .requestMatchers(HttpMethod.DELETE, "/orderItems/{orderItemId}").hasAuthority(String.valueOf(Role.ADMIN))






                        .requestMatchers("/api/v1/auth/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
        )
                        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .authenticationProvider(authenticationProvider)
                        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);




        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

}
