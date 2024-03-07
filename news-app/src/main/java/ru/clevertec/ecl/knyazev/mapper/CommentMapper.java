package ru.clevertec.ecl.knyazev.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.clevertec.ecl.knyazev.data.http.owner.comment.request.PostPutCommentRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.comment.response.GetCommentResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.user.response.GetUserResponseDTO;
import ru.clevertec.ecl.knyazev.entity.Comment;
import ru.clevertec.ecl.knyazev.entity.News;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "uuid", source = "comment.uuid")
    @Mapping(target = "subscriberFirstName", source = "getUserResponseDTO.firstName")
    @Mapping(target = "subscriberLastName", source = "getUserResponseDTO.lastName")
    @Mapping(target = "subscriberEmail", source = "getUserResponseDTO.email")
    @Mapping(target = "text", source = "comment.text")
    @Mapping(target = "created", source = "comment.createDate", dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @Mapping(target = "updated", source = "comment.updateDate", dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    GetCommentResponseDTO toGetCommentResponseDTO(Comment comment, GetUserResponseDTO getUserResponseDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "subscriberUUID", source = "getUserResponseDTO.uuid")
    @Mapping(target = "text", source = "postPutCommentRequestDTO.text")
    @Mapping(target = "news", source = "news")
    @Mapping(target = "createDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updateDate", ignore = true)
    Comment toComment(PostPutCommentRequestDTO postPutCommentRequestDTO,
                      News news,
                      GetUserResponseDTO getUserResponseDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "subscriberUUID", ignore = true)
    @Mapping(target = "text", source = "postPutCommentRequestDTO.text")
    @Mapping(target = "news", source = "news")
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", expression = "java(java.time.LocalDateTime.now())")
    Comment toComment(@MappingTarget Comment comment,
                      News news,
                      PostPutCommentRequestDTO postPutCommentRequestDTO);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "uuid", source = "uuid")
    @Mapping(target = "subscriberUUID", source = "subscriberUUID")
    @Mapping(target = "text", source = "text")
    @Mapping(target = "news", expression = "java(null)")
    @Mapping(target = "updateDate", source = "updateDate")
    @Mapping(target = "createDate", source = "createDate")
    Comment toCommentWithoutNews(Comment comment);
}
