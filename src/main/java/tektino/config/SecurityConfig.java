package tektino.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(new AntPathRequestMatcher("/")).permitAll()// Mengizinkan akses ke halaman utama
                .requestMatchers(new AntPathRequestMatcher("/public/**")).permitAll()// Mengizinkan akses ke halaman utama
                .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()// Mengizinkan akses ke folder css
                .requestMatchers(new AntPathRequestMatcher("/js/**")).permitAll()// Mengizinkan akses ke folder js
                .requestMatchers(new AntPathRequestMatcher("/img/**")).permitAll()// Mengizinkan akses ke folder img
                .requestMatchers(new AntPathRequestMatcher("/lib/**")).permitAll()// Mengizinkan akses ke folder lib
                .requestMatchers(new AntPathRequestMatcher("/webjars/**")).permitAll()// Mengizinkan akses ke folder webjars
                .anyRequest().authenticated()
            )
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard")
                .permitAll()
                .and()
            .logout()
                .permitAll();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
