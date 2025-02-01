package dasturlashuz.giybat.controller;

import dasturlashuz.giybat.dto.attach.AttachResponse;
import dasturlashuz.giybat.service.AttachService;
import dasturlashuz.giybat.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/attach")
@RequiredArgsConstructor
public class AttachController {
    private final AttachService attachService;

    @PostMapping("/upload")
    public ResponseEntity<AttachResponse> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(attachService.upload(file));
    }
}
