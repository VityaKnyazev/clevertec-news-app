package ru.clevertec.ecl.knyazev.data.http.owner.pageresponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Represents page DTO that sends in
 * Response
 * <p>
 * on HTTP method GET
 * <p>
 * content - page content
 * pageNumber - current page number
 * pageSize - page size
 * totalElements - total data elements in page object
 * totalPages - total pages quantity in page object
 *
 * @param <T> type of page content
 */
@Schema(description = "page DTO on response")
@Value
public class PageDTO<T> {
    @Schema(description = "page DTO content",
            example = "json: content:[{\"id\":2,\"name\":\"Vanya\"}]")
    List<T> content;

    @Schema(description = "page number",
            example = "0")
    Integer pageNumber;

    @Schema(description = "page size",
            example = "20")
    Integer pageSize;

    @Schema(description = "page data total elements",
            example = "32")
    Long totalElements;

    @Schema(description = "page data total pages",
            example = "2")
    Integer totalPages;

    public PageDTO(Page<T> page) {
        this.content = page.getContent();
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }
}
