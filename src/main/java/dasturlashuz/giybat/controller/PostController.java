package dasturlashuz.giybat.controller;

import dasturlashuz.giybat.dto.post.PostCreateDTO;
import dasturlashuz.giybat.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("")
    public ResponseEntity<PostCreateDTO.PostResponse> create(@RequestBody @Valid PostCreateDTO dto) {
        return ResponseEntity.ok(postService.create(dto));
    }

    @GetMapping("")
    public ResponseEntity<List<PostCreateDTO.PostResponse>> getAll() {
        return ResponseEntity.ok(postService.getAll());
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostCreateDTO.PostResponse> update(@PathVariable String postId, @RequestBody @Valid PostCreateDTO dto) {
        return ResponseEntity.ok(postService.update(postId, dto));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> delete(@PathVariable String postId) {
        return ResponseEntity.ok(postService.delete(postId));
    }

    @GetMapping("/get-all-posts-owner")
    public ResponseEntity<List<PostCreateDTO.PostResponse>> getAllPostsOwner() {
        return ResponseEntity.ok(postService.getAllPostsOwner());
    }

    @GetMapping("/get-all-posts-pagination")
    public ResponseEntity<PageImpl<PostCreateDTO.PostResponse>> getAllPostsPagination(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {
        return ResponseEntity.ok().body(postService.getAllPostsPagination(page, size));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostCreateDTO.PostResponse> getById(@PathVariable String postId) {
        return ResponseEntity.ok(postService.getById(postId));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<PostCreateDTO.PostFilterResponse>> filter(
            @RequestBody  PostCreateDTO.PostFilterDTO dto,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size){
        return ResponseEntity.ok(postService.filter(dto, page-1, size));
    }
}
