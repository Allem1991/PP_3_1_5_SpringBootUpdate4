package ru.khusnullin.SpringBootUpdate2.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.khusnullin.SpringBootUpdate2.service.UserServiceImpl;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserServiceImpl userServiceImpl;

    private final SuccessUserHandler successUserHandler;

    @Autowired
    public SecurityConfig(@Lazy UserServiceImpl userServiceImpl, SuccessUserHandler successUserHandler) {
        this.userServiceImpl = userServiceImpl;
        this.successUserHandler = successUserHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/auth/login", "/error").permitAll()
                    .antMatchers("/auth/registration").permitAll()
                    .antMatchers("/user/**").hasAnyRole("ADMIN", "USER")
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/").permitAll()
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/auth/login")
                    .loginProcessingUrl("/process_login")
                    .successHandler(successUserHandler)
                    .failureUrl("/auth/login?error")
                    .permitAll()
                .and()
                .logout()
                    .logoutUrl("/logout").permitAll()
                    .logoutSuccessUrl("/auth/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceImpl).passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
