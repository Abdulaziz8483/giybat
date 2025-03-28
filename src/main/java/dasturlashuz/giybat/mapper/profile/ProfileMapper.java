package dasturlashuz.giybat.mapper.profile;

import dasturlashuz.giybat.dto.profile.ProfileCreateDTO;
import dasturlashuz.giybat.entity.ProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    @Mapping(source = "name", target = "name")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    //@Mapping(source = "role", target = "role")
    ProfileCreateDTO.ProfileResponse profileToProfileDTO(ProfileEntity profile);
}
