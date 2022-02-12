package com.TechPro.SpringBootStudy.basic_authentication;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration // Class'ı konfiguratiıoın olarak tanımlar
@EnableWebSecurity // Tanımlı olduğu Class'ta from based olarak securty yerine configuration olanı kullanmamızı sağlar
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordConfig passwordConfig;

    @Autowired
    public ApplicationSecurityConfig(PasswordConfig passwordConfig) {
        this.passwordConfig = passwordConfig;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()//Requestler için yetki sorgula
                .antMatchers("/", "/index", "/css/*", "/js/*").permitAll()
                .antMatchers("/**").hasRole(ApplicationUserRoles.ADMIN.name())
                .anyRequest() // her request için
                .authenticated()
                .and() // neye göre
                .httpBasic(); // httpBasic'e göre
    }


    @Override
    @Bean
    protected UserDetailsService userDetailsService() {

        UserDetails admin = User
                .builder()
                .username("admin")
                .password(passwordConfig.passwordEncoder()
                        .encode("1234"))
                .authorities(ApplicationUserRoles.ADMIN.izinler())
                .build();
        UserDetails user = User
                .builder()
                .username("user")
                .password(passwordConfig.passwordEncoder()
                        .encode("1234"))
                .authorities(ApplicationUserRoles.USER.izinler())
                .build();

        return new InMemoryUserDetailsManager(admin, user );
    }
}
