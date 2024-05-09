package com.encuentrame.project.encuentrame.config;

import com.encuentrame.project.encuentrame.services.MyUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class Security {

//Para que la contraseña no sea simple, sino se encripte se usan esos dos métodos:
@Autowired
public MyUserServiceImpl userServicio;

    //Este objeto lo recibimos por parámetro

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        //Apoyados de Spring Security revisamos el método que nos permite autenticar el user
        auth.userDetailsService(userServicio) //Este método de userDetailsService está en userServicio, por eso pasamos el userServicio
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        //Se permite cualquier tipo de archivo a las personas o sea ver.
        //No hizo faltar user ni password
        http
                .authorizeHttpRequests((authz) -> authz.anyRequest().permitAll())
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/logincheck") //No hace falta un controlador para eso, spring security se encarga
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/")
                        .permitAll()
                )
                .logout(logout ->  logout
                        .logoutUrl("/logout") //No hace falta un controlador para eso, spring security se encarga
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

}
