package ru.clevertec.ecl.knyazev.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.knyazev.client.KeycloakUserFeignClient;
import ru.clevertec.ecl.knyazev.data.http.owner.user.request.PostPutUserRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.user.response.GetUserResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.client.response.GetClientKeycloakClientResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.role.request.PostPutClientRoleRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.user.response.GetClientUserResponseDTO;
import ru.clevertec.ecl.knyazev.mapper.RoleMapper;
import ru.clevertec.ecl.knyazev.mapper.UserMapper;
import ru.clevertec.ecl.knyazev.service.ClientService;
import ru.clevertec.ecl.knyazev.service.UserService;
import ru.clevertec.ecl.knyazev.service.exception.ServiceException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final KeycloakUserFeignClient keycloakUserFeignClientImpl;

    private final ClientService clientServiceImpl;

    private final UserMapper userMapperImpl;
    private final RoleMapper roleMapperImpl;

    @Value("${spring.security.oauth2.client.registration.keycloak-feign-crud.client-id}")
    @Setter
    private String clientId;

    /**
     * {@inheritDoc}
     */
    @Override
    public GetUserResponseDTO getUserResponseDTO(String uuid) {
        return userMapperImpl.toGetUserResponseDTO(keycloakUserFeignClientImpl.getUser(uuid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetUserResponseDTO getUserResponseDTOByUsername(String username) throws ServiceException {
        GetClientUserResponseDTO[] getClientUserResponseDTOS =
                keycloakUserFeignClientImpl.getUserByUsername(username, true);

        if (getClientUserResponseDTOS.length == 0) {
            throw new ServiceException();
        }

        return userMapperImpl.toGetUserResponseDTO(getClientUserResponseDTOS[0]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetUserResponseDTO> getUserResponseDTOs() {
        return userMapperImpl.toGetUserResponseDTOs(keycloakUserFeignClientImpl.getAll());
    }

    /**
     * {@inheritDoc}
     *
     * @throws ServiceException when failed to add roles to
     *                          saved user
     */
    @Override
    public void add(PostPutUserRequestDTO postPutUserRequestDTO) throws ServiceException {
        keycloakUserFeignClientImpl.saveUser(userMapperImpl.toPostPutClientUserRequestDTO(postPutUserRequestDTO));
        addUserRoles(postPutUserRequestDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(String userUUID, PostPutUserRequestDTO postPutUserRequestDTO) {
        keycloakUserFeignClientImpl.updateUser(userUUID,
                userMapperImpl.toPostPutClientUserRequestDTO(postPutUserRequestDTO));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(String userUUID) {
        keycloakUserFeignClientImpl.deleteUser(userUUID);
    }

    /**
     * add(map) roles to user
     *
     * @param postPutUserRequestDTO user dto
     * @throws ServiceException when client user DTO not found
     */
    private void addUserRoles(PostPutUserRequestDTO postPutUserRequestDTO) throws ServiceException {
        GetClientUserResponseDTO[] getClientUserResponseDTOs =
                keycloakUserFeignClientImpl.getUserByUsername(postPutUserRequestDTO.username(),
                        true);

        if (getClientUserResponseDTOs.length == 0) {
            throw new ServiceException(String.format(ServiceException.USER_SEARCHING_ERROR_MESSAGE,
                    postPutUserRequestDTO.username()));
        }

        String userUUID = getClientUserResponseDTOs[0].id();
        GetClientKeycloakClientResponseDTO getClientUserResponseDTO =
                clientServiceImpl.getClientByUniqueIdName(clientId);

        PostPutClientRoleRequestDTO[] postPutClientRoleRequestDTOs = postPutUserRequestDTO.roles().stream()
                .map(roleName ->
                        clientServiceImpl.getClientRoleByClientUniqueIdNameAndRoleName(
                                getClientUserResponseDTO.id(),
                                roleName))
                .map(roleMapperImpl::toPostPutClientRoleRequestDTO)
                .toArray(PostPutClientRoleRequestDTO[]::new);

        keycloakUserFeignClientImpl.mapRoleToUser(userUUID,
                getClientUserResponseDTO.id(),
                postPutClientRoleRequestDTOs);
    }
}
