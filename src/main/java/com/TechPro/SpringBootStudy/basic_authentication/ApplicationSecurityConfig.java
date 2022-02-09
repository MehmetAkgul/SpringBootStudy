package com.TechPro.SpringBootStudy.basic_authentication;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration // Class'ı konfiguratiıoın olarak tanımlar
@EnableWebSecurity // Tanımlı olduğu Class'ta from based olarak securty yerine configuration olanı kullanmamızı sağlar
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordConfig passwordConfig;

    @Autowired
    public ApplicationSecurityConfig(PasswordConfig passwordConfig) {
        this.passwordConfig = passwordConfig;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()//Requestler için yetki sorgula
                .antMatchers("/", "/index", "/css/*", "/js/*").permitAll()
                // antMatchers() method parametresindeki url'lere izin verir
                .anyRequest() // her request için
                .authenticated()
                .and() // neye göre
                .httpBasic(); // httpBasic'e göre
    }


    @Override
    @Bean
    protected UserDetailsService userDetailsService() {

        UserDetails user01 = User.builder().username("mehmet").password(passwordConfig.passwordEncoder().encode("1234")).roles("USER").build();
        UserDetails user02 = User.builder().username("turkan").password(passwordConfig.passwordEncoder().encode("1234")).roles("USER").build();
        UserDetails admin01 = User.builder().username("ayse").password(passwordConfig.passwordEncoder().encode("1234")).roles("ADMIN").build();
        UserDetails admin02 = User.builder().username("eymen").password(passwordConfig.passwordEncoder().encode("1234")).roles("ADMIN").build();
         return new InMemoryUserDetailsManager(user01, user02, admin01, admin02);
    }
}
