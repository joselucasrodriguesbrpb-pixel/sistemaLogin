package org.tbluu.registro.domain.user;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegisterDTO(@Email @NotBlank(message = "email não pode ser vazio") String email,
                          @NotBlank(message = "senha não pode ser vazia")
                          @Size(min = 6, message = "senha deve ter pelo menos 6 dígitos") String senha,
                          @NotNull(message = "você precisa dizer sua idade")
                          @Past(message = "A data de nascimento deve estar no passado") LocalDate dataDeNascimento) {
}
