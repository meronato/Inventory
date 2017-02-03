package com.fusemachine.inventory;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	DataSource dataSource;
	
/*    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/")
                .permitAll()
                .defaultSuccessUrl("/home")
                .and()
            .logout()
                .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    
        auth
            .inMemoryAuthentication()
                .withUser("admin").password("admin").roles("USER");
    }*/
	 @Autowired
	 public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
	   auth.jdbcAuthentication().dataSource(dataSource)
	  .usersByUsernameQuery(
	   "select username,password, enabled from users where username=?")
	  .authoritiesByUsernameQuery(
	   "select username, role from user_roles where username=?");
	 }
	 
	 @Override
	 protected void configure(HttpSecurity http) throws Exception {
		 
	   http.authorizeRequests()
	  .antMatchers("/login").access("hasRole('ROLE_ADMIN')")  
	  .anyRequest().permitAll()
	  .and()
	    .formLogin().loginPage("/").defaultSuccessUrl("/home")
	    .usernameParameter("username").passwordParameter("password")
	  .and() 
	    .logout().logoutSuccessUrl("/login?logout") 
	   .and()
	   .exceptionHandling().accessDeniedPage("/403")
	  .and()
	    .csrf();
	 }
}
