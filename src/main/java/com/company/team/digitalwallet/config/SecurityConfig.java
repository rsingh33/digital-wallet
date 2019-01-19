package com.company.team.digitalwallet.config;


import com.company.team.digitalwallet.entity.Role;
import com.company.team.digitalwallet.entity.User;
import com.company.team.digitalwallet.repository.UserRepository;
import com.company.team.digitalwallet.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(encoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("**/users/").authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin().permitAll();
    }
    @Bean
    protected CommandLineRunner init(final UserRepository userRepository) {

        return args -> {

            User user = new User();
            user.setUserName("admin");
            user.setUserDescription("Administrator");
            user.setBalance(0);
            user.setActive(1);
            user.setUserSince(new Timestamp(System.currentTimeMillis()));
            user.setPin("0000");
            Set<Role> roles = new HashSet<>();
            Role role = new Role();
            role.setRole("ROLE_ADMIN");
            user.setRoles(roles);

            userRepository.save(user);

        };
    }

}
