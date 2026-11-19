package org.tbluu.registro.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final SecurityFilter securityFilter;

    public SecurityConfiguration(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean //Cria uma rede de regras que serão seguidas na criação e uso de um token
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //desabilita o csrf, assim não é necessário o token de CSRF e os POSTs funcionam sem problemas
        return httpSecurity.csrf(csrf -> csrf.disable())
                 //faz com que seja necessário a apresentação do token a cada requisição
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //permite a passagem nos endereços marcados (auth/register/login) das requisições sem a necessidade de mostrar o token
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers("/error").permitAll()
                        //Caso não tenha sido especificado alguma outra rota ou metodo http, aqui ele é capturada e exige o token
                        .anyRequest().authenticated())
                //cria uma prioridade de leitura no código, assim ja valida o token antes dos metodos anyRequest e authenticated serem chamados
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                //completa a montagem
                .build();
    }

    @Bean // Expõe o gerenciador de autenticação (AuthenticationManager) para o AuthController
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean //Criptografa a senha tanto para fazer o cadastro, quanto para comparar no login
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
