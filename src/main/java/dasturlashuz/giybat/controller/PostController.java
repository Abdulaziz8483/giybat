package dasturlashuz.giybat.controller;

import dasturlashuz.giybat.dto.post.PostCreateDTO;
import dasturlashuz.giybat.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("")
    public ResponseEntity<PostCreateDTO.PostResponse> create(@RequestBody @Valid PostCreateDTO dto) {
        return ResponseEntity.ok(postService.create(dto));
    }

}
