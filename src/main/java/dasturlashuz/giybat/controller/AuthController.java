package dasturlashuz.giybat.controller;

import dasturlashuz.giybat.dto.RegistrationDTO;
import dasturlashuz.giybat.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("")
    public ResponseEntity<String> Registration(@RequestBody RegistrationDTO dto){

        return ResponseEntity.ok().body(authService.Registration(dto));
    }
}
