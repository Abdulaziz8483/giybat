package dasturlashuz.giybat.dto.attach;

import java.time.LocalDateTime;

public record AttachResponse(
    String originName,
    Long size,
    String path,
    String type,
    LocalDateTime createdDate,
    String url
) {
    public record AttachUrl(
            String id,
            String url
    ){}
}
