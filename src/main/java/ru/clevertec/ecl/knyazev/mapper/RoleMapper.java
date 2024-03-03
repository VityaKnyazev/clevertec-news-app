package ru.clevertec.ecl.knyazev.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.role.request.PostPutClientRoleRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.role.response.GetClientRoleResponseDTO;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    PostPutClientRoleRequestDTO toPostPutClientRoleRequestDTO(GetClientRoleResponseDTO getClientRoleResponseDTO);
}
