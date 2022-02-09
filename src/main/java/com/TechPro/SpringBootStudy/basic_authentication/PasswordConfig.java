package com.TechPro.SpringBootStudy.basic_authentication;

import antlr.BaseAST;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordConfig {



    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return  new BCryptPasswordEncoder(10); // güvenlik seviyesi
    }

}
