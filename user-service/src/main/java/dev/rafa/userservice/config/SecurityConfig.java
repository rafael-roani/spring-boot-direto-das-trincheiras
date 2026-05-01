package dev.rafa.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private static final String[] WHITE_LIST = {
      "/swagger-ui.html",
      "/swagger-ui/**",
      "/v3/api-docs/**",
      "/csrf"
  };

  //  @Bean
  //  public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
  //      UserDetails user = User.withUsername("takamura")
  //              .password(passwordEncoder.encode("ippo"))
  //              .roles("USER")
  //              .build();
  //
  //      UserDetails admin = User.withUsername("admin")
  //              .password(passwordEncoder.encode("devdojo"))
  //              .roles("USER", "ADMIN")
  //              .build();
  //
  //      return new InMemoryUserDetailsManager(user, admin);
  //  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        // .csrf(csrf -> csrf
        //         .csrfTokenRepository(new CookieCsrfTokenRepository())
        //         .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
        // )
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            auth -> auth
                .requestMatchers(WHITE_LIST).permitAll()
                .requestMatchers(HttpMethod.POST, "v1/users").permitAll()
                // .requestMatchers(HttpMethod.GET, "v1/users").hasRole("ADMIN")
                .anyRequest().authenticated()
        )
        .httpBasic(Customizer.withDefaults())
        .build();
  }

}
