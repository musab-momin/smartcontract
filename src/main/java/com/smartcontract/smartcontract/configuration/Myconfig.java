package com.smartcontract.smartcontract.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@EnableWebSecurity
public class Myconfig extends WebSecurityConfigurerAdapter 
{
    @Bean
    public UserDetailsService getUserDetailsService() 
    {
        return new UserDetailsSerivceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() 
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() 
    {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(getUserDetailsService());
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception 
    {
        auth.authenticationProvider(authenticationProvider());
        super.configure(auth);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception 
    {
        http.authorizeRequests().antMatchers("/admin/**").hasAuthority("ADMIN")
        .antMatchers("/user/**").hasAnyAuthority("USER")
        .antMatchers("/**").permitAll().and().formLogin().loginPage("/signin")
        .loginProcessingUrl("/dologin")
        .defaultSuccessUrl("/user/home")
        .and().csrf().disable();
        super.configure(http);
    }

    
}
