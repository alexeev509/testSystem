package testSystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import testSystem.handlers.Securityhandler;
import testSystem.services.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        http
//                .sessionManagement()
//                .invalidSessionUrl("/getTest")
//                .maximumSessions(1)
//                .maxSessionsPreventsLogin(true)
//                .sessionRegistry(sessionRegistry());
        // включаем защиту от CSRF атак
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/register", "/scripts/**", "/styles/**")
                .permitAll()
                .antMatchers("/testPage{\\id+}").hasRole("USER")
                .antMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/autorisation")
                .loginProcessingUrl("/j_spring_security_check")
                .usernameParameter("j_username")
                .passwordParameter("j_password")
                .successHandler(new Securityhandler())
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/autorisation")
                .invalidateHttpSession(true);

    }

    // Стандартная Spring имплементация SessionRegistry
//    @Bean(name = "sessionRegistry")
//    public SessionRegistry sessionRegistry() {
//        return new SessionRegistryImpl();
//    }

    @Autowired
    private UserService userService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }


}