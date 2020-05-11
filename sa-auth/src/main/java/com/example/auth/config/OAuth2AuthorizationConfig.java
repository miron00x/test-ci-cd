package com.example.auth.config;

import com.example.auth.service.CustomDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    private TokenStore tokenStore = new InMemoryTokenStore();

    private final AuthenticationManager authenticationManager;

    private final CustomDetailsService userDetailsService;

    private final Environment env;

    private final PasswordEncoder passwordEncoder;

    private final DiscoveryClient discoveryClient;

    @Autowired
    public OAuth2AuthorizationConfig(@Qualifier("authenticationManagerBean") AuthenticationManager authenticationManager,
                                     CustomDetailsService userDetailsService, Environment env, PasswordEncoder passwordEncoder, DiscoveryClient discoveryClient) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.env = env;
        this.passwordEncoder = passwordEncoder;
        this.discoveryClient = discoveryClient;
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        return new JwtAccessTokenConverter();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        //String frontendURL = discoveryClient.getInstances("sa-frontend").iterator().next().getHost();

        // TODO persist clients details

        // @formatter:off
        clients.inMemory()
                .withClient("browser")
                .authorizedGrantTypes("refresh_token", "password", "authorization_code", "implicit")
                .scopes("ui")
                .redirectUris("http://localhost:3000/login", "http://localhost:3000/")
                .and()
                .withClient("web-app-service")
                //.secret(env.getProperty("WEB_APP_SERVICE_PASSWORD"))
                .secret(passwordEncoder.encode("WEB_APP_SERVICE_PASSWORD"))
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server");
        // @formatter:on
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore)
                .accessTokenConverter(accessTokenConverter())
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .passwordEncoder(passwordEncoder)
                .allowFormAuthenticationForClients();
    }

}
