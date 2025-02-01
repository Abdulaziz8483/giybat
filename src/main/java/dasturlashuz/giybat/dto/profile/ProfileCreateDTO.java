package dasturlashuz.giybat.dto.profile;

import dasturlashuz.giybat.enums.ProfileRole;
import dasturlashuz.giybat.enums.ProfileStatus;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;


public record ProfileCreateDTO(
        String name,
        String username,
        String password,
        ProfileStatus status,
        ProfileRole role
) {
    public record ProfileResponse(
            String name,
            String username,
            ProfileStatus status,
            ProfileRole role
    ){}
}
