package dasturlashuz.giybat.mapper.profile;

import dasturlashuz.giybat.dto.profile.ProfileCreateDTO;
import dasturlashuz.giybat.entity.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileCreateDTO.ProfileResponse profileToProfileDTO(Profile profile);
}
