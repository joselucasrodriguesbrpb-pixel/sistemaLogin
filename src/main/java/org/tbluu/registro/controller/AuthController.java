package org.tbluu.registro.controller;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tbluu.registro.domain.login.LoginRequestDTO;
import org.tbluu.registro.domain.login.LoginResponseDTO;
import org.tbluu.registro.domain.user.RegisterDTO;
import org.tbluu.registro.domain.user.User;
import org.tbluu.registro.infra.security.TokenService;
import org.tbluu.registro.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;
    private final AuthenticationManager manager;
    private final TokenService tokenService;

    public AuthController(AuthService service, AuthenticationManager manager, TokenService tokenService) {
        this.service = service;
        this.manager = manager;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registrar(@RequestBody @Valid RegisterDTO dto) {
        service.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login (@RequestBody @Valid LoginRequestDTO dto) {
        var userNamePassowrd = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());

        var auth =  manager.authenticate(userNamePassowrd);

        var token = tokenService.gerarToken((User) auth.getPrincipal());

        return  ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
