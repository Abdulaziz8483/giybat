package dasturlashuz.giybat.mapper.attach;

import dasturlashuz.giybat.dto.attach.AttachResponse;
import dasturlashuz.giybat.entity.AttachEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttachMapper {
    @Mapping(source = "originName", target = "originName")
    @Mapping(source = "size", target = "size")
    @Mapping(source = "path", target = "path")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "createdAt", target = "createdDate")
    AttachResponse attachEntityToAttachResponse(AttachEntity attachEntity);

}
