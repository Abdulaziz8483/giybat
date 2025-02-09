package dasturlashuz.giybat.dto.profile;

import dasturlashuz.giybat.enums.ProfileRole;
import dasturlashuz.giybat.enums.ProfileStatus;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;


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
            ProfileStatus status
            //ProfileRole role
    ){}
    public record ProfileFilterDTO(
            String name,
            String username,
            ProfileStatus status,
            Boolean visible,
            LocalDateTime fromCreatedAt,
            LocalDateTime toCreatedAt,
            ProfileRole role
    ){}
    public record ProfileFilterResponse(
            Integer id,
            String name,
            String username,
            String photoId,
            String photoUrl,
            int postCount
    ){}
    public record ProfileDetailDTO(
            String name,
            String username,
            List<String> roles,
            String photoId,
            String photoUrl
    ){}
}
