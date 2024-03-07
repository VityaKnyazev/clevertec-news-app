package ru.clevertec.ecl.knyazev.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.clevertec.ecl.knyazev.data.http.owner.user.request.PostPutUserRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.user.response.GetUserResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.user.credential.request.PostPutClientCredentialRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.user.request.PostPutClientUserRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.user.response.GetClientUserResponseDTO;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "uuid", source = "id")
    GetUserResponseDTO toGetUserResponseDTO(GetClientUserResponseDTO getClientUserResponseDTO);

    List<GetUserResponseDTO> toGetUserResponseDTOs(List<GetClientUserResponseDTO> getClientUserResponseDTOs);

    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "postPutClientCredentialRequestDTOs",
            source = "password",
            qualifiedByName = "fromPasswordToCredentialsList")
    PostPutClientUserRequestDTO toPostPutClientUserRequestDTO(PostPutUserRequestDTO postPutUserRequestDTO);

    @Named("fromPasswordToCredentialsList")
    default List<PostPutClientCredentialRequestDTO> fromPasswordToCredentialsMap(String password) {
        return new ArrayList<>() {{
           add(PostPutClientCredentialRequestDTO.builder()
                   .type("password")
                   .value(password)
                   .temporary(false)
                   .build());
        }};
    }
}
