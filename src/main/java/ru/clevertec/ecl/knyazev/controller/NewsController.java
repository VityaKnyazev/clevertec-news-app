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
import ru.clevertec.ecl.knyazev.data.http.owner.comment.response.GetCommentResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.news.request.PostPutNewsRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.news.response.GetNewsResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.pageresponse.PageDTO;
import ru.clevertec.ecl.knyazev.service.NewsService;

@RestController
@RequestMapping("/news")
@Tag(name = "News API")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsServiceImpl;

    @Operation(summary = "Get news response DTO by news uuid")
    @ApiResponse(responseCode = "200", description = "Successfully found")
    @GetMapping("/{uuid}")
    public ResponseEntity<GetNewsResponseDTO> getNews(@PathVariable
                                                      @Parameter(name = "uuid",
                                                              required = true,
                                                              description = "news uuid",
                                                              example = "548f8b02-4f28-49af-864b-b50faa1c1438")
                                                      @NotNull(message = "News id (uuid) must not be null")
                                                      @UUID(message =
                                                              "news id must be uuid string in lower case")
                                                      String uuid) {

        GetNewsResponseDTO getNewsResponseDTO =
                newsServiceImpl.getNewsResponseDTO(java.util.UUID.fromString(uuid));

        return ResponseEntity.ok(getNewsResponseDTO);
    }

    @Operation(summary = "Get news comments response DTO by news uuid")
    @ApiResponse(responseCode = "200", description = "Successfully found")
    @GetMapping("/{uuid}/comments")
    public ResponseEntity<PageDTO<GetCommentResponseDTO>> getNewsComments(@PathVariable
                                                       @Parameter(name = "uuid",
                                                               required = true,
                                                               description = "news uuid",
                                                               example = "548f8b02-4f28-49af-864b-b50faa1c1438")
                                                       @NotNull(message = "News id (uuid) must not be null")
                                                       @UUID(message =
                                                               "news id must be uuid string in lower case")
                                                       String uuid,
                                                       @ParameterObject
                                                       Pageable pageable) {
        PageDTO<GetCommentResponseDTO> pageGetCommentResponseDTO =
                newsServiceImpl.getNewsCommentResponseDTOs(java.util.UUID.fromString(uuid), pageable);

        return ResponseEntity.ok(pageGetCommentResponseDTO);
    }

    @Operation(summary = "Get news comment response DTO by news uuid and comment uuid")
    @ApiResponse(responseCode = "200", description = "Successfully found")
    @GetMapping("/{uuid}/comments/{commentUUID}")
    public ResponseEntity<GetCommentResponseDTO> getNewsComment(@PathVariable
                                                    @Parameter(name = "uuid",
                                                            required = true,
                                                            description = "news uuid",
                                                            example = "548f8b02-4f28-49af-864b-b50faa1c1438")
                                                    @NotNull(message = "News id (uuid) must not be null")
                                                    @UUID(message =
                                                            "news id must be uuid string in lower case")
                                                    String uuid,
                                                    @PathVariable
                                                    @Parameter(name = "commentUUID",
                                                            required = true,
                                                            description = "news comment uuid",
                                                            example = "5da9af91-1f22-4a09-85b6-a09eadace32e")
                                                    @NotNull(message = "Comment id (uuid) must not be null")
                                                    @UUID(message =
                                                             "comment id must be uuid string in lower case")
                                                    String commentUUID) {
        GetCommentResponseDTO getCommentResponseDTO =
                newsServiceImpl.getNewsCommentResponseDTO(java.util.UUID.fromString(uuid),
                        java.util.UUID.fromString(commentUUID));

        return ResponseEntity.ok(getCommentResponseDTO);
    }

    @Operation(summary = "Get all news DTO")
    @ApiResponse(responseCode = "200", description = "Successfully found")
    @GetMapping
    public ResponseEntity<PageDTO<GetNewsResponseDTO>> getAllNews(@RequestParam(required = false)
                                        @Parameter(name = "search",
                                                description = "parameter for searching on news title, text fields",
                                                example = "фестиваль")
                                        @Valid
                                        @Size(min = 3,
                                                max = 50,
                                                message =
                                                        "search argument must have length from 3 to 50 symbols")
                                        String search,
                                        @ParameterObject
                                        Pageable pageable) {
        Searching searching = new SearchingImpl(search);
        PageDTO<GetNewsResponseDTO> pageGetNewsResponseDTO =
                newsServiceImpl.getAllNewsDTO(pageable, searching);

        return ResponseEntity.ok(pageGetNewsResponseDTO);
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('JOURNALIST')")
    @Operation(summary = "Save news")
    @ApiResponse(responseCode = "201", description = "Successfully saved")
    @PostMapping
    public ResponseEntity<GetNewsResponseDTO> saveNews(@RequestBody
                                      @Valid
                                      PostPutNewsRequestDTO postPutNewsRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(newsServiceImpl.add(postPutNewsRequestDTO));
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('JOURNALIST')")
    @Operation(summary = "Update news")
    @ApiResponse(responseCode = "200", description = "Successfully updated")
    @PutMapping("/{uuid}")
    public ResponseEntity<GetNewsResponseDTO> changeNews(@PathVariable
                                        @Parameter(name = "uuid",
                                                required = true,
                                                description = "news uuid",
                                                example = "548f8b02-4f28-49af-864b-b50faa1c1438")
                                        @NotNull(message = "News id (uuid) must not be null")
                                        @UUID(message =
                                                "news id must be uuid string in lower case")
                                        String uuid,
                                        @RequestBody
                                        @Valid
                                        PostPutNewsRequestDTO postPutNewsRequestDTO) {
        return ResponseEntity.ok(newsServiceImpl.update(java.util.UUID.fromString(uuid),
                postPutNewsRequestDTO));
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('JOURNALIST')")
    @Operation(summary = "Delete news")
    @ApiResponse(responseCode = "204", description = "No content - successfully deleted")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteNews(@PathVariable
                                        @Parameter(name = "uuid",
                                                required = true,
                                                description = "news uuid",
                                                example = "548f8b02-4f28-49af-864b-b50faa1c1438")
                                        @NotNull(message = "News id (uuid) must not be null")
                                        @UUID(message =
                                                "news id must be uuid string in lower case")
                                        String uuid) {
        newsServiceImpl.remove(java.util.UUID.fromString(uuid));

        return ResponseEntity.noContent().build();
    }
}
