package dasturlashuz.giybat.dto.post;

import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record PostCreateDTO(
        String title,
        @Size(min = 100)
        String content,
        String photoId
) {
    public record PostResponse(
            String id,
            String title,
            String content,
            String photoUrl,
            String photoId,
            LocalDateTime createdDate
    ){}
    public record PostFilterDTO(
            String title,
            LocalDateTime publishedDateFrom,
            LocalDateTime publishedDateTo,
            Integer profileId
    ){}
    public record PostFilterResponse(
            String id,
            String title,
            String content,
            String postPhotoId,
            String postPhotoUrl,
            LocalDateTime createdDate,

            Integer profileId,
            String name,
            String profilePhotoUrl
    ){}
}
