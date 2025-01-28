package dasturlashuz.giybat.controller;

import dasturlashuz.giybat.dto.profile.ProfileCreateDTO;
import dasturlashuz.giybat.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    @Autowired
    ProfileService service;

    @PostMapping("")
    public ResponseEntity<ProfileCreateDTO.ProfileResponse> create(@RequestBody ProfileCreateDTO profileDTO) {
        return ResponseEntity.ok(service.create(profileDTO));
    }
}
