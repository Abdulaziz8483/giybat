package dasturlashuz.giybat.dto.profile;

import dasturlashuz.giybat.enums.ProfileRole;
import dasturlashuz.giybat.enums.ProfileStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
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
    public record ProfileUpdateName(
            @NotBlank(message = "The name should not be empty")
            @Size(min = 3, message = "Please enter more than 3 characters.")
            String name
    ){}

    public record ProfileUpdatePhoto(
            @NotBlank(message = "The photoId should not be empty")
            String photoId
    ){}

    public record ProfileUpdatePassword(
            @NotBlank(message = "The password should not be empty")
            @Size(min = 4, message = "Please enter more than 6 characters")
            String oldPassword,

            @NotBlank(message = "The password should not be empty")
            @Size(min = 6, message = "Please enter more than 6 characters")
            String newPassword

    ) {
    }

    public record ProfileUpdateUsername(
            @NotBlank(message = "The username should not be empty")
            @Size(min = 16, message = "Please enter more than 16 characters")
            String username
    ) {
    }
}
