package com.spring.security.websecurity;

import com.spring.security.db.UserRepository;
import com.spring.security.jwt.JwtAuthenticationFilter;
import com.spring.security.jwt.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;


    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                //Enaable all these 4 lines to use JWT
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
//                .addFilter(new JwtAuthorizationFilter(authenticationManager(),this.userRepository))


                .authorizeRequests()

                .antMatchers("/index").permitAll()
                .antMatchers("/profile/**").authenticated()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/management/**").hasAnyRole("ADMIN", "MANAGER")

//			    .antMatchers("/api/public/test1").hasRole("ADMIN")	//ADMIN user only can access
                .antMatchers("/api/public/test1").hasAuthority("ACCESS_TEST1")    //All admin and manager has authority Access test1 so all can have

//			    .antMatchers("/api/public/test2").authenticated()   // any user,admin,manager with username and passwword can access
                .antMatchers("/api/public/test2").hasAuthority("ACCESS_TEST2")   // ADMIN has authority Access test2 so admin can have

                .antMatchers("/api/public/test3").hasAuthority("ACCESS_TEST3")  // ADMINhas authority Access test3 so admin can have


                //Disable all below to use JWT
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")

                .and()
                .rememberMe()
                //  SESSION_ID expires in 30 minute of inactivity so Remember me is used to extend the time.
//                                // Where remember me is 2 WEEK is default  with : username + expiration date + md5 hash of username and expiration date
//                                // In form create checkbox with id remember-me and no need to do in controller
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))  // 21days
                .key("somethingverysecuredkeystring");//Use this key to hash  username and expiration date


    }
}
