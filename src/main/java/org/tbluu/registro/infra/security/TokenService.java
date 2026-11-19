package org.tbluu.registro.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tbluu.registro.domain.user.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service //Classe onde os Tokens são gerados
public class TokenService {

    @Value("${api.security.token.secret}") //Injeta o valor definido no application.properties
    private String secretKey;

    public String gerarToken(User user) {

        try {

            //Define o algoritmo de criptografia usando a chave secreta
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.create()
                    .withIssuer("registro-api") //Quem emitiu o token (nome da minha aplicação)
                    .withSubject(user.getEmail()) //Quem é o dono do token (identificador único)
                    .withExpiresAt(gerarDataDeAxpiracao()) //Quando o token vence
                    .sign(algorithm); //Assina e gera a String final do token

        }
        catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao gerar o token JWT ", e);
        }
    }

    public String validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            return JWT.require(algorithm)
                    .withIssuer("registro-api") //Verifica se foi emitido pela API
                    .build()
                    .verify(token) //Valida a assinatura e a validade, se falhar lança a exceção
                    .getSubject(); //Devolve o email guardado no token
        }
        catch (JWTVerificationException e) {
            return "";
        }
    }

    public Instant gerarDataDeAxpiracao() {
        //Define que o token expira em duas horas, utilizando o fuso horário de Brasília (-03:00)
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
