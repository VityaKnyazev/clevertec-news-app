package ru.clevertec.ecl.knyazev.service.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.knyazev.client.KeycloakClientFeignClient;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.client.response.GetClientKeycloakClientResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.role.response.GetClientRoleResponseDTO;
import ru.clevertec.ecl.knyazev.service.ClientService;
import ru.clevertec.ecl.knyazev.service.exception.ServiceException;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final KeycloakClientFeignClient keycloakClientFeignClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public GetClientKeycloakClientResponseDTO getClientByUniqueIdName(String clientUniqueIdName)
            throws ServiceException {
        GetClientKeycloakClientResponseDTO[] getClientKeycloakClientResponseDTO =
                keycloakClientFeignClient.getClients(clientUniqueIdName);

        if (getClientKeycloakClientResponseDTO.length == 0) {
            throw new ServiceException();
        }

        return getClientKeycloakClientResponseDTO[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetClientRoleResponseDTO getClientRoleByClientUniqueIdNameAndRoleName(String clientUniqueIdName,
                                                                                 String roleName) {
        return keycloakClientFeignClient.getClientRoleByClientUuidAndRoleName(clientUniqueIdName,
                roleName);
    }
}
