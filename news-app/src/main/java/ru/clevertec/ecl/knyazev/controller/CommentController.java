package ru.clevertec.ecl.knyazev.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.knyazev.data.domain.searching.Searching;
import ru.clevertec.ecl.knyazev.data.domain.searching.impl.SearchingImpl;
import ru.clevertec.ecl.knyazev.data.http.owner.comment.request.PostPutCommentRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.comment.response.GetCommentResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.pageresponse.PageDTO;
import ru.clevertec.ecl.knyazev.service.CommentService;

@RestController
@RequestMapping("/comments")
@Tag(name = "Comments API")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentServiceImpl;

    @Operation(summary = "Get comment response DTO by comment uuid")
    @ApiResponse(responseCode = "200", description = "Successfully found")
    @GetMapping("/{uuid}")
    public ResponseEntity<GetCommentResponseDTO> getComment(@PathVariable
                                                            @Parameter(name = "uuid",
                                                                    required = true,
                                                                    description = "comment uuid",
                                                                    example = "5da9af91-1f22-4a09-85b6-a09eadace32e")
                                                            @NotNull(message = "Comment id (uuid) must not be null")
                                                            @UUID(message =
                                                                    "comment id must be uuid string in lower case")
                                                            String uuid) {
        GetCommentResponseDTO getCommentResponseDTO =
                commentServiceImpl.getCommentResponseDTO(java.util.UUID.fromString(uuid));
        return ResponseEntity.ok(getCommentResponseDTO);
    }

    @Operation(summary = "Get all comments DTO")
    @ApiResponse(responseCode = "200", description = "Successfully found")
    @GetMapping
    public ResponseEntity<PageDTO<GetCommentResponseDTO>> getAllComments(@RequestParam(required = false)
                                            @Parameter(name = "search",
                                                    description = "parameter for searching on comments text field",
                                                    example = "шар")
                                            @Valid
                                            @Size(min = 3,
                                                    max = 50,
                                                    message =
                                                            "search argument must have length from 3 to 50 symbols")
                                            String search,
                                            @ParameterObject
                                            Pageable pageable) {
        Searching searching = new SearchingImpl(search);

        PageDTO<GetCommentResponseDTO> pageGetCommentResponseDTOPageDTO =
                commentServiceImpl.getAllCommentDTO(pageable, searching);
        return ResponseEntity.ok(pageGetCommentResponseDTOPageDTO);
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('SUBSCRIBER')")
    @Operation(summary = "Save comment")
    @ApiResponse(responseCode = "201", description = "Successfully saved")
    @PostMapping
    public ResponseEntity<GetCommentResponseDTO> saveComment(@RequestBody
                                                        @Valid
                                                        PostPutCommentRequestDTO postPutCommentRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(commentServiceImpl.add(postPutCommentRequestDTO));
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('SUBSCRIBER')")
    @Operation(summary = "Update comment")
    @ApiResponse(responseCode = "200", description = "Successfully updated")
    @PutMapping("/{uuid}")
    public ResponseEntity<GetCommentResponseDTO> changeComment(@PathVariable
                                                         @Parameter(name = "uuid",
                                                                 required = true,
                                                                 description = "comment uuid",
                                                                 example = "5da9af91-1f22-4a09-85b6-a09eadace32e")
                                                         @NotNull(message = "Comment id (uuid) must not be null")
                                                         @UUID(message =
                                                                 "comment id must be uuid string in lower case")
                                                         String uuid,
                                                         @RequestBody
                                                         @Valid
                                                         PostPutCommentRequestDTO postPutCommentRequestDTO) {
        return ResponseEntity.ok(commentServiceImpl.update(java.util.UUID.fromString(uuid),
                postPutCommentRequestDTO));
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('SUBSCRIBER')")
    @Operation(summary = "Delete comment")
    @ApiResponse(responseCode = "204", description = "No content - successfully deleted")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteComment(@PathVariable
                                        @Parameter(name = "uuid",
                                                required = true,
                                                description = "comment uuid",
                                                example = "5da9af91-1f22-4a09-85b6-a09eadace32e")
                                        @NotNull(message = "Comment id (uuid) must not be null")
                                        @UUID(message =
                                                "comment id must be uuid string in lower case")
                                        String uuid) {
        commentServiceImpl.remove(java.util.UUID.fromString(uuid));

        return ResponseEntity.noContent().build();
    }
}
