package dasturlashuz.giybat.dto.profile;

import dasturlashuz.giybat.enums.ProfileStatus;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;


public record ProfileCreateDTO(
        String name,
        String email,
        String phone,
        String password,
        ProfileStatus status
) {
    public record ProfileResponse(
            String name,
            String email,
            String phone,
            ProfileStatus status
    ){}
}
