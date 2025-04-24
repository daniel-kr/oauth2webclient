package examples.oauth2webclient;

import org.kohsuke.github.GitHub;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@SpringBootApplication
public class Oauth2webclientApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2webclientApplication.class, args);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login/**", "/error**", "/github/code").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Client(Customizer.withDefaults());

        return http.build();
    }

//    @Bean
//    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
//        return new RestClientAuthorizationCodeTokenResponseClient();
//    }

    @RestController
    public static class GithubController {
        @GetMapping("/github/code")
        public String getAuthCode(@RegisteredOAuth2AuthorizedClient("github43") OAuth2AuthorizedClient client) throws IOException {
            final OAuth2AccessToken accessToken = client.getAccessToken();
            final String accessTokenValue = accessToken.getTokenValue();
            final GitHub gitHub = GitHub.connectUsingOAuth(accessTokenValue);

            return gitHub.listOrganizations().toList().toString();
        }
    }
}
