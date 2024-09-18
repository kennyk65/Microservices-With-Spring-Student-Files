package demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(
                authorizeRequests -> authorizeRequests
                    .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable())           // Needed to allow POST to /encrypt to work
            .httpBasic(Customizer.withDefaults() ); // Use basic authentication
        return http.build();
    }

}

