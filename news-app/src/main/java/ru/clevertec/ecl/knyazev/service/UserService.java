package ru.clevertec.ecl.knyazev.service;

import ru.clevertec.ecl.knyazev.data.http.owner.user.request.PostPutUserRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.user.response.GetUserResponseDTO;
import ru.clevertec.ecl.knyazev.service.exception.ServiceException;

import java.util.List;

/**
 * Represents service that working with web client to
 * operate with user
 */
public interface UserService {

    /**
     * Get user DTO using user uuid via web client
     *
     * @param uuid user uuid
     * @return user DTO
     */
    GetUserResponseDTO getUserResponseDTO(String uuid);

    /**
     * Get user DTO using username via web client
     *
     * @param username user name
     * @return user DTO
     * @throws ServiceException if user not found
     */
    GetUserResponseDTO getUserResponseDTOByUsername(String username) throws ServiceException;

    /**
     * Get all user DTOs via web client
     *
     * @return user DTOs or empty list
     */
    List<GetUserResponseDTO> getUserResponseDTOs();

    /**
     * Add user dto via web client
     * for save operation
     *
     * @param postPutUserRequestDTO user dto for saving
     */
    void add(PostPutUserRequestDTO postPutUserRequestDTO);

    /**
     * Add user dto via web client
     * for update operation
     *
     * @param userUUID user uuid
     * @param postPutUserRequestDTO  user dto for updating
     */
    void update(String userUUID, PostPutUserRequestDTO postPutUserRequestDTO);

    /**
     * Add user uuid via web client
     * for delete operation
     *
     * @param userUUID user uuid
     */
    void remove(String userUUID);
}
