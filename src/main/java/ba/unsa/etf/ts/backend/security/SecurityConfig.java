package ba.unsa.etf.ts.backend.security;

import ba.unsa.etf.ts.backend.filter.CustomAuthenticationFilter;
import ba.unsa.etf.ts.backend.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()).and();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().antMatchers("/api/refresh-token").permitAll();
        http.authorizeRequests().antMatchers(POST,"/api/user").permitAll();
        //http.authorizeRequests().antMatchers("/api/user/**").permitAll();
//        http.authorizeRequests().antMatchers(GET, "/api/hello").permitAll();
//        http.authorizeRequests().antMatchers(GET, "/api/flights").permitAll();
//        http.authorizeRequests().antMatchers(GET,"/api/airports").permitAll();
        http.authorizeRequests().antMatchers(POST, "/api/image").hasAuthority("ADMIN");
//        http.authorizeRequests().antMatchers(POST, "/api/registration").permitAll();
//        http.authorizeRequests().antMatchers(GET, "/api/registration/confirm/**").permitAll();
//        http.authorizeRequests().antMatchers(POST, "/api/registration-employee").hasAnyAuthority("ADMIN");
//        http.authorizeRequests().antMatchers("/api/user/**").hasAnyAuthority("ADMIN","PASSENGER", "EMPLOYEE");
//        http.authorizeRequests().antMatchers(GET, "/api/users").hasAuthority("ADMIN");
//        http.authorizeRequests().antMatchers( "/api/flight/**").hasAuthority("EMPLOYEE");
////        http.authorizeRequests().antMatchers(GET, "/api/get-flight/**").hasAuthority("EMPLOYEE");
//        http.authorizeRequests().antMatchers(POST, "/api/ticket").hasAuthority("PASSENGER");
//        http.authorizeRequests().antMatchers(GET, "/api/tickets/**").hasAuthority("PASSENGER");
//        http.authorizeRequests().antMatchers(GET, "/api/flight-passengers/**").hasAuthority("EMPLOYEE");
//        http.authorizeRequests().antMatchers(POST, "/api/password-change").hasAnyAuthority("ADMIN","PASSENGER", "EMPLOYEE");
        http.requiresChannel().anyRequest().requiresSecure();
        http.authorizeRequests().anyRequest().authenticated();
        //http.authorizeRequests().anyRequest().permitAll();
        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
