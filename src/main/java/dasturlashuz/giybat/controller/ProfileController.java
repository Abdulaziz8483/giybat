package dasturlashuz.giybat.controller;

import dasturlashuz.giybat.dto.profile.ProfileCreateDTO;
import dasturlashuz.giybat.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    @Autowired
    ProfileService service;

    @PostMapping("")
    public ResponseEntity<ProfileCreateDTO.ProfileResponse> create(@RequestBody ProfileCreateDTO profileDTO) {
        return ResponseEntity.ok(service.create(profileDTO));
    }

    @GetMapping("")
    public ResponseEntity<List<ProfileCreateDTO.ProfileResponse>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{profileId}")
    public ResponseEntity<ProfileCreateDTO.ProfileResponse>update(@PathVariable Integer profileId, @RequestBody ProfileCreateDTO profileDTO) {
        return ResponseEntity.ok(service.update(profileId, profileDTO));
    }

    @DeleteMapping("/{profileId}")
    public ResponseEntity<String> delete(@PathVariable Integer profileId) {
        return ResponseEntity.ok(service.delete(profileId));
    }
}
