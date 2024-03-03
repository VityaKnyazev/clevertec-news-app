package ru.clevertec.ecl.knyazev.service;

import feign.FeignException;
import ru.clevertec.ecl.knyazev.data.http.owner.user.response.GetUserResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.client.response.GetClientKeycloakClientResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.role.response.GetClientRoleResponseDTO;
import ru.clevertec.ecl.knyazev.service.exception.ServiceException;

public interface ClientService {

    /**
     * Get client DTO from keycloak service
     * on clientUniqueIdName
     *
     * @param clientUniqueIdName client unique id name (client string id-name)
     * @return client DTO form keycloak service
     * @throws ServiceException when client not found
     */
    GetClientKeycloakClientResponseDTO getClientByUniqueIdName(String clientUniqueIdName) throws ServiceException;

    /**
     * Get client Role dto
     * on client unique id name (string id-name)
     * and role name
     *
     * @param clientUniqueIdName client unique id name (client string id-name)
     * @param roleName client role name
     * @return client Role dto
     */
    GetClientRoleResponseDTO getClientRoleByClientUniqueIdNameAndRoleName(String clientUniqueIdName,
                                                                          String roleName);
}
