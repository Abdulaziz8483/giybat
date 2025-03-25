package dasturlashuz.giybat.dto;

import dasturlashuz.giybat.dto.attach.AttachResponse;

import java.util.List;

public record LoginResponseDTO(
        String name,
        String username,
        List<String> roles,
        String token,
        AttachResponse.AttachUrl photo
) {
}
