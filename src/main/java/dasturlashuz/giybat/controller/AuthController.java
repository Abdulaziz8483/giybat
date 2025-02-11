package dasturlashuz.giybat.controller;



import dasturlashuz.giybat.dto.AuthDto;
import dasturlashuz.giybat.dto.RegistrationDTO;
import dasturlashuz.giybat.exceptions.AppBadException;
import dasturlashuz.giybat.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<Map<String, Object>> Registration(@Valid @RequestBody RegistrationDTO dto) {
        Map<String, Object> response = new HashMap<>();

        try {
            String result = authService.Registration(dto).toString();
            response.put("success", true);
            response.put("message", result);
            return ResponseEntity.ok(response);
        } catch (AppBadException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }


    @GetMapping("/verification/{profileId}")
    public ResponseEntity<String> regVerification(@PathVariable("profileId") Integer profileId){

        return ResponseEntity.ok().body(authService.regVerification(profileId));
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody AuthDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }


}
