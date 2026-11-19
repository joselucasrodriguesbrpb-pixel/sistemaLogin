package org.tbluu.registro.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tbluu.registro.domain.user.RegisterDTO;
import org.tbluu.registro.domain.user.User;
import org.tbluu.registro.mapper.UserMapper;
import org.tbluu.registro.repository.UserRepository;

import java.time.LocalDate;
import java.time.Period;

@Service
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    public AuthService(UserRepository repository, PasswordEncoder passwordEncoder, UserMapper mapper) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    public void registrar(RegisterDTO dto) {
        LocalDate dataHoje = LocalDate.now();
        if (Period.between(dto.dataDeNascimento(), dataHoje).getYears() < 18) {
            throw new IllegalArgumentException("O usuário deve ter pelo menos 18 anos para se registrar.");
        }
        String senhaCriptografada = passwordEncoder.encode(dto.senha());
        User user = mapper.toEntity(dto, senhaCriptografada);
        repository.save(user);
    }
}
