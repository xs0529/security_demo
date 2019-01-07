package com.springboot.study.security_demo.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.annotation.Jsr250Voter;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
import org.springframework.security.access.vote.*;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import java.util.*;

/**
 * <p>
 *
 * </p>
 *
 * @author XieShuang
 * @version v1.0
 * @since 2019-01-05
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().permitAll().and()
                .authorizeRequests()
                .antMatchers("/test/**").permitAll()
                .anyRequest().authenticated()
                .accessDecisionManager( accessDecisionManager());
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new MyUserDetailsService());
    }

    class MyUserDetailsService implements UserDetailsService {

        private Map<String, User> userRepository = new HashMap<String, User>();

        public MyUserDetailsService() {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            User user1 = new User("user1", "password1", authorities);
            userRepository.put("user1", user1);
            User user2 = new User("user2", "password2", authorities);
            userRepository.put("user2", user2);
        }

        @Override
        public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
            User user = userRepository.get(s);
            return user;
        }
    }

    @Bean
    public NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    class MyAccessDecisionVoter implements AccessDecisionVoter{

        @Override
        public boolean supports(ConfigAttribute configAttribute) {
            return true;
        }

        @Override
        public int vote(Authentication authentication, Object o, Collection collection) {
            System.out.println("进来了");
            return ACCESS_DENIED;
        }

        @Override
        public boolean supports(Class aClass) {
            return true;
        }
    }

    @Bean
    public AccessDecisionManager accessDecisionManager(){
        List<AccessDecisionVoter<? extends Object>> decisionVoters
                = Arrays.asList(
                        new WebExpressionVoter(),
                new MyAccessDecisionVoter());
        return new AffirmativeBased(decisionVoters);
    }

    class MyAccessDecisionManager implements AccessDecisionManager{

        @Override
        public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
            throw new AccessDeniedException("nononono");
        }

        @Override
        public boolean supports(ConfigAttribute configAttribute) {
            return true;
        }

        @Override
        public boolean supports(Class<?> aClass) {
            return true;
        }
    }
}
