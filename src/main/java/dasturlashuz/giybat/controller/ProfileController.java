package dasturlashuz.giybat.controller;

import dasturlashuz.giybat.dto.StandardResponse;
import dasturlashuz.giybat.dto.profile.ProfileCreateDTO;
import dasturlashuz.giybat.service.ProfileService;
import jakarta.validation.Valid;
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

    @GetMapping("/get-detail")
    public ResponseEntity<ProfileCreateDTO.ProfileDetailDTO> getDetail(){
        return ResponseEntity.ok(service.getDetail());
    }

    @PutMapping("/update-name")
    public ResponseEntity<ProfileCreateDTO.ProfileResponse> updateName(@RequestBody @Valid ProfileCreateDTO.ProfileUpdateName dto) {
        return ResponseEntity.ok(service.updateName(dto));
    }

    @PutMapping("/update-photo")
    public ResponseEntity<ProfileCreateDTO.ProfileResponse> updatePhoto(@RequestBody @Valid ProfileCreateDTO.ProfileUpdatePhoto dto){
        return ResponseEntity.ok(service.updatePhoto(dto));
    }
    @PutMapping("/update-password")
    public ResponseEntity<StandardResponse> updatePassword(@RequestBody @Valid ProfileCreateDTO.ProfileUpdatePassword dto){
        return ResponseEntity.ok(service.updatePassword(dto));
    }
    @PutMapping("/update-username")
    public ResponseEntity<String> updateUsername(@RequestBody @Valid ProfileCreateDTO.ProfileUpdateUsername dto){
        return ResponseEntity.ok(service.updateUsername(dto));
    }
    @PostMapping("/filter")
    public ResponseEntity<List<ProfileCreateDTO.ProfileFilterResponse>> filter(
            @RequestBody ProfileCreateDTO.ProfileFilterDTO dto,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size){
        return ResponseEntity.ok(service.filter(dto, page, size));
    }


}
