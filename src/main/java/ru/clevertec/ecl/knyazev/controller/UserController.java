package ru.clevertec.ecl.knyazev.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.knyazev.data.http.owner.user.request.PostPutUserRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.user.response.GetUserResponseDTO;
import ru.clevertec.ecl.knyazev.service.UserService;
import ru.clevertec.ecl.knyazev.validation.group.Save;
import ru.clevertec.ecl.knyazev.validation.group.Update;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Users API")
@RequiredArgsConstructor
public class UserController {

    private final UserService userServiceImpl;

    @Operation(summary = "Get user response DTO by user uuid")
    @ApiResponse(responseCode = "200", description = "Successfully found")
    @GetMapping("/{uuid}")
    public ResponseEntity<?> getUser(@PathVariable
                                     @Parameter(name = "uuid",
                                        required = true,
                                        description = "user uuid",
                                        example = "5dec0ec6-cf10-4234-94fc-78db68ee82dd")
                                     @NotNull(message = "User id (uuid) must not be null")
                                     @UUID(message =
                                             "user id must be uuid string in lower case")
                                     String uuid) {
        GetUserResponseDTO getUserResponseDTO = userServiceImpl.getUserResponseDTO(uuid);

        return ResponseEntity.ok(getUserResponseDTO);
    }

    @Operation(summary = "Get all users response DTOs")
    @ApiResponse(responseCode = "200", description = "Successfully found")
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<GetUserResponseDTO> getUserResponseDTOS = userServiceImpl.getUserResponseDTOs();

        return ResponseEntity.ok(getUserResponseDTOS);
    }

    @Operation(summary = "Save user")
    @ApiResponse(responseCode = "201", description = "Successfully saved")
    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody
                                      @Validated(value = {Default.class, Save.class})
                                      PostPutUserRequestDTO postPutUserRequestDTO) {
        userServiceImpl.add(postPutUserRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Update user")
    @ApiResponse(responseCode = "200", description = "Successfully updated")
    @PutMapping("/{uuid}")
    public ResponseEntity<?> updateUser(@PathVariable
                                        @Parameter(name = "uuid",
                                                required = true,
                                                description = "user uuid",
                                                example = "5dec0ec6-cf10-4234-94fc-78db68ee82dd")
                                        @NotNull(message = "User id (uuid) must not be null")
                                        @UUID(message = "user id must be uuid string in lower case")
                                        String uuid,
                                        @RequestBody
                                        @Validated(value = {Default.class, Update.class})
                                        PostPutUserRequestDTO postPutUserRequestDTO) {
        userServiceImpl.update(uuid, postPutUserRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Delete user")
    @ApiResponse(responseCode = "204", description = "No content - successfully deleted")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteUser(@PathVariable
                                        @Parameter(name = "uuid",
                                                required = true,
                                                description = "user uuid",
                                                example = "5dec0ec6-cf10-4234-94fc-78db68ee82dd")
                                        @NotNull(message = "User id (uuid) must not be null")
                                        @UUID(message = "user id must be uuid string in lower case")
                                        String uuid) {
        userServiceImpl.remove(uuid);

        return ResponseEntity.noContent().build();
    }
}
