package ru.clevertec.ecl.knyazev.client;

import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.clevertec.ecl.knyazev.config.FeignClientConfig;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.client.response.GetClientKeycloakClientResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.role.response.GetClientRoleResponseDTO;

/**
 * Represents web client for keycloak client data manipulation by communication
 * with remote keycloak service
 */
@FeignClient(value = "keycloakClientFeignClient")
public interface KeycloakClientFeignClient {

    /**
     * Get clients by client id
     *
     * @param clientId client id
     * @return array with clients or empty array
     * @throws FeignException when service error
     */
    @GetMapping
    GetClientKeycloakClientResponseDTO[] getClients(@RequestParam String clientId) throws FeignException;

    /**
     * Get client role by its name
     *
     * @param clientUUID client uuid
     * @param roleName   client role name
     * @return client Role DTO
     * @throws FeignException when service error or role or client not found
     */
    @GetMapping("/{clientUUID}/roles/{roleName}")
    GetClientRoleResponseDTO getClientRoleByClientUuidAndRoleName(@PathVariable String clientUUID,
                                                                  @PathVariable String roleName) throws FeignException;
}
