package org.tbluu.registro.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.tbluu.registro.domain.user.RegisterDTO;
import org.tbluu.registro.domain.user.User;
import org.tbluu.registro.domain.user.UserResponseDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senha", source = "senhaCriptografada")
    User toEntity(RegisterDTO dto, String senhaCriptografada);

    UserResponseDTO toResponseDTO(User user);
}
