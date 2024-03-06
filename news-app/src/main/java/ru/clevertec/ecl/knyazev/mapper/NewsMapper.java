package ru.clevertec.ecl.knyazev.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.clevertec.ecl.knyazev.data.http.owner.news.request.PostPutNewsRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.news.response.GetNewsResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.user.response.GetUserResponseDTO;
import ru.clevertec.ecl.knyazev.entity.News;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    @Mapping(target = "uuid", source = "news.uuid")
    @Mapping(target = "authorFirstName", source = "getUserResponseDTO.firstName")
    @Mapping(target = "authorLastName", source = "getUserResponseDTO.lastName")
    @Mapping(target = "authorEmail", source = "getUserResponseDTO.email")
    @Mapping(target = "title", source = "news.title")
    @Mapping(target = "text", source = "news.text")
    @Mapping(target = "created", source = "news.createDate", dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @Mapping(target = "updated", source = "news.updateDate", dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    GetNewsResponseDTO toGetNewsResponseDTO(News news, GetUserResponseDTO getUserResponseDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "journalistUUID", source = "getUserResponseDTO.uuid")
    @Mapping(target = "title", source = "postPutNewsRequestDTO.title")
    @Mapping(target = "text", source = "postPutNewsRequestDTO.text")
    @Mapping(target = "createDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "comments", ignore = true)
    News toNews(PostPutNewsRequestDTO postPutNewsRequestDTO, GetUserResponseDTO getUserResponseDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "journalistUUID", ignore = true)
    @Mapping(target = "title", source = "postPutNewsRequestDTO.title")
    @Mapping(target = "text", source = "postPutNewsRequestDTO.text")
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "comments", ignore = true)
    News toNews(@MappingTarget News news, PostPutNewsRequestDTO postPutNewsRequestDTO);
}
