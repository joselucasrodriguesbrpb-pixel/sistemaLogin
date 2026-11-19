package org.tbluu.registro.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.tbluu.registro.repository.UserRepository;

import java.io.IOException;

@Component //Intercepta todas as requisições do cliente
public class SecurityFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public SecurityFilter(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }


    @Override  //
    protected void doFilterInternal(HttpServletRequest request /*requisição do cliente*/
            , HttpServletResponse response /*resposta que será devolvida*/,
            FilterChain filterChain/* */) throws ServletException, IOException {
        //Recupera o token do cabeçalho da requisição
        var token = recuperarToken(request);

        //Se houver um token, valida e autentica o usuário no contexto do Spring
        if (token != null) {
            var email = tokenService.validarToken(token);
            UserDetails user = userRepository.findByEmail(email);

            if (user != null) {
                //null porque a senha ja foi autenticada no login
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        //Chama o próximo filtro da corrente (segue o fluxo normal da requisição, sem ele o programa quebra)
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return null;
        }

        // O padrão HTTP envia "Bearer eyJhbGci...", então removemos o prefixo "Bearer "
        return  authHeader.replace("Bearer ", "");
    }
}
