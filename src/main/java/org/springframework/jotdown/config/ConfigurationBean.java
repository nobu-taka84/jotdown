package org.springframework.jotdown.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;

@Configuration
public class ConfigurationBean {

    @Autowired
    private DataSourceProperties properties;

    @Autowired
    private DataSource dataSource;

    /**
     * DataSource
     */
    @Bean(destroyMethod = "close")
    public DataSource realDataSource() throws URISyntaxException {
        String url;
        String username;
        String password;

        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl != null) {
            // heroku側
            URI dbUri = new URI(databaseUrl);
            url = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();
            username = dbUri.getUserInfo().split(":")[0];
            password = dbUri.getUserInfo().split(":")[1];
        } else {
            // localhost側
            url = this.properties.getUrl();
            username = this.properties.getUsername();
            password = this.properties.getPassword();
        }

        DataSourceBuilder factory =
                DataSourceBuilder.create(this.properties.getClassLoader()).url(url)
                        .username(username).password(password);
        this.dataSource = factory.build();
        return this.dataSource;
    }

    /**
     * AuthenticationFailureHandler
     *
     * @return 認証失敗時のハンドラー
     */
    @Bean(name = "authenticationFailureHandler")
    public AuthenticationFailureHandler defaultAuthenticationFailureHandler() {

        // Exceptionと遷移先のマッピング
        Map<String, String> exceptionMappings = new HashMap<>();

        // パスワード期限切れ
        exceptionMappings.put(//
                AccountExpiredException.class.getCanonicalName(), "/login?error=expired");

        // アカウントロック
        exceptionMappings.put(//
                LockedException.class.getCanonicalName(), "/login?error=locked");

        final ExceptionMappingAuthenticationFailureHandler result =
                new ExceptionMappingAuthenticationFailureHandler();
        result.setExceptionMappings(exceptionMappings);

        // その他の認証エラー
        result.setDefaultFailureUrl("/login?error=default");

        return result;
    }

    /**
     * messageSource
     *
     * @return messageSource
     */
    @Bean(name = "messageSource")
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource bean = new ReloadableResourceBundleMessageSource();
        bean.setBasename("classpath:messages");
        bean.setDefaultEncoding("UTF-8");
        return bean;
    }

}
