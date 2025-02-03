package dasturlashuz.giybat.controller;

import dasturlashuz.giybat.dto.attach.AttachResponse;
import dasturlashuz.giybat.entity.AttachEntity;
import dasturlashuz.giybat.service.AttachService;
import dasturlashuz.giybat.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/attach")
@RequiredArgsConstructor
public class AttachController {
    private final AttachService attachService;

    @PostMapping("/upload")
    public ResponseEntity<AttachResponse.AttachUrl> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(attachService.upload(file));
    }

    @GetMapping("")
    public ResponseEntity<List<AttachResponse>> getAll() {
        return ResponseEntity.ok(attachService.getAll());
    }

    /*@PutMapping("/{attachId}")
    public ResponseEntity<AttachResponse> update(@PathVariable Long attachId, @RequestBody AttachEntity attachEntity) {}*/

    @DeleteMapping("/{attachId}")
    public ResponseEntity<String> delete(@PathVariable String attachId) {
        return ResponseEntity.ok(attachService.delete(attachId));
    }

    @GetMapping("/open/{attachId}")
    public ResponseEntity<Resource> open(@PathVariable String attachId) {
        return attachService.open(attachId);
    }
}
