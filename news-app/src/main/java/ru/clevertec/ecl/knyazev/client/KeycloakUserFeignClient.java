package ru.clevertec.ecl.knyazev.client;

import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.clevertec.ecl.knyazev.config.FeignClientConfig;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.role.request.PostPutClientRoleRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.user.request.PostPutClientUserRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.user.response.GetClientUserResponseDTO;

import java.util.List;

/**
 * Represents web client for user data manipulation by communication
 * with remote keycloak service
 */
@FeignClient(value = "keycloakUserFeignClient")
public interface KeycloakUserFeignClient {

    /**
     * Get user dto by uuid from remote service
     *
     * @param uuid user dto uuid
     * @return user dto
     * @throws FeignException when not found or service error
     */
    @GetMapping("/{uuid}")
    GetClientUserResponseDTO getUser(@PathVariable String uuid) throws FeignException;

    /**
     * Get user DTOs array by username from remote service
     *
     * @param username username query param
     * @param exact query param
     * @return user DTOs array or empty array
     * @throws FeignException when service error
     */
    @GetMapping
    GetClientUserResponseDTO[] getUserByUsername(@RequestParam String username,
                                                 @RequestParam Boolean exact);

    /**
     * Get all user DTOs or empty list from remote service
     *
     * @return all user DTOs or empty list
     * @throws FeignException when service error
     */
    @GetMapping
    List<GetClientUserResponseDTO> getAll() throws FeignException;

    /**
     * Add user dto to remote service for save operation
     *
     * @param postPutClientUserRequestDTO user dto
     * @throws FeignException when saving or service error
     */
    @PostMapping
    void saveUser(@RequestBody PostPutClientUserRequestDTO postPutClientUserRequestDTO) throws FeignException;

    /**
     * Add client role DTOs array to remote service
     * for mapping client roles to saved user
     *
     * @param userUUID user uuid
     * @param clientUUID client uuid
     * @param postPutClientRoleRequestDTOs client role DTOs array
     * @throws FeignException when roles mapping error or service error
     */
    @PostMapping("/{userUUID}/role-mappings/clients/{clientUUID}")
    void mapRoleToUser(@PathVariable String userUUID,
                       @PathVariable String clientUUID,
                       @RequestBody PostPutClientRoleRequestDTO[] postPutClientRoleRequestDTOs) throws FeignException;

    /**
     * Add user dto to remote service for update operation
     *
     * @param uuid user uuid
     * @param postPutClientUserRequestDTO user dto
     * @throws FeignException when updating or service error
     */
    @PutMapping("/{uuid}")
    void updateUser(@PathVariable String uuid,
                    @RequestBody PostPutClientUserRequestDTO postPutClientUserRequestDTO) throws FeignException;

    /**
     * Add user dto uuid to remote service for delete operation
     *
     * @param uuid user dto uuid
     * @throws FeignException when deleting or service error
     */
    @DeleteMapping("/{uuid}")
    void deleteUser(@PathVariable String uuid) throws FeignException;
}
