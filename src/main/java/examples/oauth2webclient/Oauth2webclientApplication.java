package examples.oauth2webclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
public class Oauth2webclientApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2webclientApplication.class, args);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//                .csrf(csrf -> csrf.disable())  // Disable CSRF protection
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login/**", "/error**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Client(Customizer.withDefaults());

        return http.build();
    }
}
