package dasturlashuz.giybat.mapper.post;

import dasturlashuz.giybat.dto.post.PostCreateDTO;
import dasturlashuz.giybat.entity.PostEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    /*@Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "attachId", target = "photoId")
    PostCreateDTO.PostResponse postToPostResponse(PostEntity post);*/
}
